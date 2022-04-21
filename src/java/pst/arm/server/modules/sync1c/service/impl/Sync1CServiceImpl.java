package pst.arm.server.modules.sync1c.service.impl;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Sync1CResult;
import pst.arm.client.modules.sync1c.domain.SyncConfig;
import pst.arm.client.modules.sync1c.domain.Worker;
import pst.arm.client.modules.sync1c.domain.Worker1C;
import pst.arm.client.modules.sync1c.domain.search.WorkerSearchCondition;
import pst.arm.server.common.service.UserService;
import pst.arm.server.modules.sync1c.dao.Sync1CDAO;
import pst.arm.server.modules.sync1c.service.Sync1CService;

/**
 * Created by akozhin on 05.05.15.
 */
@Service
public class Sync1CServiceImpl implements Sync1CService {

    private static Logger log = Logger.getLogger(Sync1CServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    Sync1CDAO dao;

    @Override
    public List<Sync1CResult> updatePosts(List<Post1C> posts, SyncConfig syncConfig) {
        List<Sync1CResult> results = new LinkedList<Sync1CResult>();
        Long userId = userService.getCurrentUser().getId();
        for (Post1C post : posts) {
            post.setUserId(userId);
            try {
                List<Post1C> postsByCode = dao.getPostsByCode(post.getPmascCode());
                if (!postsByCode.isEmpty() && postsByCode.size() == 1) {
                    Post1C postByCode = postsByCode.get(0);
                    if (!post.getName().equals(postByCode.getName())) {
                        post.setPostId(postByCode.getPostId());
                        post = dao.updatePost(post);

                        results.add(new Sync1CResult(Sync1CResult.SyncStatus.Updated, post));
                    } else {
                        if (!syncConfig.showOnlyChanged()) {
                            results.add(new Sync1CResult(Sync1CResult.SyncStatus.Equals, post));
                        }
                    }
                } else {
                    post = dao.createPost(post);
                    results.add(new Sync1CResult(Sync1CResult.SyncStatus.Created, post));
                }
            } catch (Exception e) {
                results.add(new Sync1CResult(Sync1CResult.SyncStatus.Error, post, String.format("Возникло исключение при обновлении данных о должности(%s)", e.getMessage())));
            }
        }

        return results;
    }

    @Override
    public List<Sync1CResult> updateWorkers(List<Worker1C> workers, SyncConfig syncConfig) {
        String error;
        List<Sync1CResult> results = new LinkedList<Sync1CResult>();
        Long userId = userService.getCurrentUser().getId();
        for (Worker1C worker : workers) {
            worker.setUserId(userId);
            try {
                List<Worker1C> workersByIdent = dao.getWorkers1CByIdent(worker.getIdent());
                if (!workersByIdent.isEmpty() && workersByIdent.size() == 1) {
                    Worker1C workerByIdent = workersByIdent.get(0);
                    if (!workerByIdent.equals(worker)) {
                        worker.setWorkerId(workerByIdent.getWorkerId());
                        worker = dao.updateWorker1C(worker);

                        results.add(new Sync1CResult(Sync1CResult.SyncStatus.Updated, worker));
                        log.info(String.format("Worker1C %s updated", worker.toString()));
                    } else {
                        worker.setWorkerId(workerByIdent.getWorkerId());
                        if (!syncConfig.showOnlyChanged()) {
                            results.add(new Sync1CResult(Sync1CResult.SyncStatus.Equals, worker));
                        }
                    }
                } else {
                    if (workersByIdent.size() > 1) {
                        error = String.format("Запись не создана, т.к. найдено %d записей по полю ident = %s", workersByIdent.size(), worker.getIdent());
                        results.add(new Sync1CResult(Sync1CResult.SyncStatus.Error, worker, error));
                        log.error(error);
                    } else {
                        worker = dao.createWorker1C(worker);
                        results.add(new Sync1CResult(Sync1CResult.SyncStatus.Created, worker));
                        log.info(String.format("Worker1C %s created", worker.toString()));
                    }
                }
                Sync1CResult updatePostWorker = updatePostWorker(worker, syncConfig);
                if (updatePostWorker != null) {
                    results.add(updatePostWorker);
                }
            } catch (Exception e) {
                results.add(new Sync1CResult(Sync1CResult.SyncStatus.Error, worker, String.format("Возникло исключение при обновлении данных о сотруднике (%s)", e.getLocalizedMessage())));
                log.error(String.format("Updating worker1C %s failed, %s", worker.toString(), e.getMessage()));
            }
        }
        return results;
    }

    private Sync1CResult updatePostWorker(Worker1C worker1C, SyncConfig syncConfig) {
        log.info("Updating post worker ....");
        Sync1CResult result = new Sync1CResult();
        result.setObject(worker1C);
        try {
            Worker worker = new Worker(worker1C);
            WorkerSearchCondition condition = new WorkerSearchCondition(worker1C);
            List<Worker> workers = dao.getWorkers(condition);
            if (workers.isEmpty()) {
                String error = fillWorkerData(worker1C, worker);

                if (StringUtils.isNotBlank(error)) {
                    result.setStatus(Sync1CResult.SyncStatus.Error);
                    result.setError(error);
                    result.setObject(worker);
                    log.error(error);
                } else {
                    worker = dao.createWorker(worker);
                    if (worker != null) {
                        result.setObject(worker);
                        result.setStatus(Sync1CResult.SyncStatus.Created);
                        log.info(String.format("Post worker %s created", worker.toString()));
                    } else {
                        result.setStatus(Sync1CResult.SyncStatus.Error);
                        log.error("Create post worker failed, create method return null");
                    }
                }
            } else {
                if (workers.size() == 1) {
                    worker = workers.get(0);

                    if ("Прием на работу".equals(worker1C.getReason()) || "Увольнение".equals(worker1C.getReason())) {
                        condition.setPostWorkerId(worker.getPostWorkerId());
                        condition.setSearchType(WorkerSearchCondition.SearchType.extended);
                        workers = dao.getWorkers(condition);
                        if (workers.isEmpty()) {
                            worker.copy(worker1C);
                            Worker updateWorker = dao.updateWorker(worker);
                            result.setStatus(Sync1CResult.SyncStatus.Updated);
                            result.setObject(updateWorker);
                            log.info(String.format("Post worker %s updated ", updateWorker.toString()));
                        } else {
                            if (!syncConfig.showOnlyChanged()) {
                                worker.copy(worker1C);
                                result.setStatus(Sync1CResult.SyncStatus.Equals);
                                result.setObject(worker);
                                log.info("Post worker exist");
                            } else {
                                return null;
                            }
                        }
                    } else if ("Перемещение".equals(worker1C.getReason())){
                        worker.copy(worker1C);
                        if (worker.getDateOut() != null) {
                            worker.setDateOut(DateUtils.addDays(worker1C.getDateOut(), -1));
                        }
                        Worker updateWorker = dao.updateWorker(worker);
                        result.setStatus(Sync1CResult.SyncStatus.Updated);
                        result.setObject(updateWorker);
                        log.info(String.format("Post worker %s updated ", updateWorker.toString()));

                        worker.copy(worker1C);
                        Worker createdWorker = dao.createWorkerTransfer(worker);
                        log.info(String.format("Post worker %s created for transfer", createdWorker.toString()));
                    }
                } else {
                    String errorMsg = String.format("Найдено %d сотрудников в таблице замещения должностей", workers.size());
                    result.setObject(worker);
                    result.setStatus(Sync1CResult.SyncStatus.Error);
                    result.setError(errorMsg);
                    log.error(errorMsg);
                }
            }
        } catch (Exception e) {
            result.setStatus(Sync1CResult.SyncStatus.Error);
            result.setError(String.format("Возникло исключение при обновлении данных о замещении должностей (%s)", e.getMessage()));
            log.error("Update post worker failed", e);

        }

        if (!syncConfig.showOnlyChanged()) {
            return result;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Sync1CResult> updateDepartments(List<Department1C> departments, SyncConfig syncConfig) {
        List<Sync1CResult> results = new LinkedList<Sync1CResult>();
        Long userId = userService.getCurrentUser().getId();

        for (Department1C department : departments) {
            department.setUserId(userId);
            try {
                if (!dao.isDepartmentExists(department)) {
                    Department1C newDepartment = dao.createDepartment(department);
                    results.add(new Sync1CResult(Sync1CResult.SyncStatus.Created, newDepartment));
                } else {
                    if (!syncConfig.showOnlyChanged()) {
                        results.add(new Sync1CResult(Sync1CResult.SyncStatus.Equals, department));
                    }
                }
            } catch (Exception e) {
                results.add(new Sync1CResult(Sync1CResult.SyncStatus.Error, department, String.format("Возникло исключение при обновлении данных о подразделении (%s)", e.getMessage())));
            }

            if (department.hasDepartments()) {
                results.addAll(updateDepartments(department.getDepartments(), syncConfig));
            }
        }
        return results;
    }

    private String fillWorkerData(Worker1C worker1C, Worker worker) {
        List<Integer> ids = dao.getWorkerIdsByNameAndInn(worker1C.getName(), worker1C.getInn());
        if (ids.size() == 1) {
            worker.setWorkerId(ids.get(0));
        } else {
            return ids.isEmpty() ? "Сотрудник не найден по ИНН и имени, " : String.format("Найдено %d сотрудников по имени и ИНН", ids.size());
        }

        ids = dao.getDepartmentIdsByCodeAndCompany(worker1C.getDepartmentCode(), worker1C.getCompanyId());
        if (ids.size() == 1) {
            worker.setDepartmentId(ids.get(0));
        } else {
            return ids.isEmpty() ? "Подразделение не найдено, " : String.format("Найдено  %d подразделений", ids.size());
        }

        ids = dao.getPostIdsByCode(worker1C.getPostCode());
        if (ids.size() == 1) {
            worker.setPostId(ids.get(0));
        } else {
            return ids.isEmpty() ? "Должность не найдена, " : String.format("Найдено %d должностей", ids.size());
        }

        return null;
    }
}
