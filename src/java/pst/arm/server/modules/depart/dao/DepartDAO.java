package pst.arm.server.modules.depart.dao;

import java.util.List;
import pst.arm.client.modules.depart.domain.Depart;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface DepartDAO {
    public List<Depart> select();
    
    public Depart insert(Depart domain);
    public boolean update(Depart domain);
    public boolean delete(long id);
  
}
