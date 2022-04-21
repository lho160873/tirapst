package pst.arm.server.modules.technology.nomenclature.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;

/**
 * Nomenclature Pmasc row mapper
 * 
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class NomenclatureMapper implements ParameterizedRowMapper<Nomenclature>{
    private boolean withIdSearch = false;

    public NomenclatureMapper() {
    }

    public NomenclatureMapper(boolean b) {
        this.withIdSearch = b;
    }

    @Override
    public Nomenclature mapRow(ResultSet rs, int i) throws SQLException {
        Nomenclature n = new Nomenclature();
        n.setSource(Nomenclature.NomenclatureSource.Pmasc);
        n.setNomenclatureId(rs.getInt("id"));
        n.setName(rs.getString("name"));
        n.setSignDb(rs.getInt("SIGN_DB"));
        if( withIdSearch ) n.setId(rs.getString("id_search"));
        return n;
    }
}
