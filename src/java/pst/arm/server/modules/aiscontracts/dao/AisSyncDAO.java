package pst.arm.server.modules.aiscontracts.dao;

import pst.arm.client.modules.aiscontracts.domain.AisSyncObject;
import pst.arm.client.modules.aiscontracts.domain.Field;
import pst.arm.client.modules.aiscontracts.domain.Instanciable;

public interface AisSyncDAO {

    public <T extends AisSyncObject> T create(T object);

    public <T extends AisSyncObject> T update(T object);

    public <T extends AisSyncObject & Instanciable> T get(T object, Field... fields);
}
