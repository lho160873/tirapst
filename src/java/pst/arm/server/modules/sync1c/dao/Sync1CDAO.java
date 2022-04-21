package pst.arm.server.modules.sync1c.dao;

import java.util.List;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Worker;
import pst.arm.client.modules.sync1c.domain.Worker1C;
import pst.arm.client.modules.sync1c.domain.search.WorkerSearchCondition;

/**
 * Created by akozhin on 05.05.15.
 */
public interface Sync1CDAO {

    //Posts
    public Post1C createPost(Post1C post);

    public Post1C updatePost(Post1C post);

    public List<Post1C> getPostsByCode(String code);

    //Departments
    public boolean isDepartmentExists(Department1C department);

    public Department1C createDepartment(Department1C department);

    //Workers
    public List<Worker1C> getWorkers1CByIdent(String ident);

    public Worker1C createWorker1C(Worker1C worker);

    public Worker1C updateWorker1C(Worker1C worker);

    public List<Worker> getWorkers(WorkerSearchCondition condition);

    public Worker createWorker(Worker worker);

    public Worker createWorkerTransfer(Worker worker);

    public Worker updateWorker(Worker worker);

    public List<Integer> getWorkerIdsByNameAndInn(String name, String inn);

    public List<Integer> getDepartmentIdsByCodeAndCompany(String departmentCode, Integer companyId);

    public List<Integer> getPostIdsByCode(String postCode);
}
