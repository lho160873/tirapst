package pst.arm.server.modules.sync1c.service;

import java.util.List;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Sync1CResult;
import pst.arm.client.modules.sync1c.domain.SyncConfig;
import pst.arm.client.modules.sync1c.domain.Worker1C;

/**
 * Created by akozhin on 05.05.15.
 */
public interface Sync1CService {

    List<Sync1CResult> updatePosts(List<Post1C> posts, SyncConfig syncConfig);

    List<Sync1CResult> updateWorkers(List<Worker1C> workers, SyncConfig syncConfig);

    List<Sync1CResult> updateDepartments(List<Department1C> departments, SyncConfig syncConfig);
}
