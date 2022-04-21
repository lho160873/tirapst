package pst.arm.server.modules.technology.nomenclature.dao.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;
import pst.arm.server.common.dao.jdbc.ArmNamedJdbcImpl;
import pst.arm.server.modules.technology.nomenclature.dao.NomenclatureDAO;
import pst.arm.server.modules.technology.nomenclature.dao.jdbc.mappers.NomenclatureMapper;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Repository
public class NomenclatureDAOJdbcIml extends ArmNamedJdbcImpl implements NomenclatureDAO {

    private static Logger log = Logger.getLogger(NomenclatureDAOJdbcIml.class);

    @Override
    public Map<Integer, List<Nomenclature>> getNomenclatures(NomenclatureSearchCondition condition) {
        String sql = "SELECT id,name,SIGN_DB FROM SPR_NOMENCLATURE n";
        if (!StringUtils.isBlank(condition.getName())) {
            sql += " WHERE not exists( select * from SSP_NOMENCLATURE_SEARCH where id_nomenclature = n.id ) and NAME like ";
        }
        Map<Integer, List<Nomenclature>> results = new HashMap<Integer, List<Nomenclature>>();
        Map<Integer, String> queries = new HashMap<Integer, String>();
        int level = 1;
        String where = "%";
        for (String part : condition.getName().split("\\s+")) {
            where += part + "%";
            queries.put(level, sql + "'" + where + "'");
            level++;
        }
        for (Map.Entry<Integer, String> entry : queries.entrySet()) {
            log.info("SQL:" + entry.getValue());
            List<Nomenclature> list = getJdbcTemplate().getJdbcOperations().query(entry.getValue(), new NomenclatureMapper());
            if (isNotEmpty(list)) {
                log.info("ROWS:" + list.size());
                results.put(entry.getKey(), list);
            } else {
                return results;
            }
        }
        return results;
    }

    private boolean isNotEmpty(Collection list) {
        return list != null && !list.isEmpty();
    }

    @Override
    public boolean isNomenclatureNotMatched(String idSearch) {
        String sql = "SELECT 1 FROM SSP_NOMENCLATURE_SEARCH WHERE id_search = :idSearch";
        log.info("SQL:" + sql + ", " + idSearch);
        List list = getJdbcTemplate().queryForList(sql, new MapSqlParameterSource("idSearch", idSearch));
        log.info("ROWS:" + list.size());
        return list.isEmpty();
    }

    @Override
    public void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc) {
        String sql = "INSERT INTO SSP_NOMENCLATURE_SEARCH(SIGN_DB,id_nomenclature,id_search) VALUES(:signDb,:idNomenclature,:idSearch)";
        MapSqlParameterSource params = new MapSqlParameterSource("idNomenclature", nomenclaturePmasc.getNomenclatureId());
        params.addValue("signDb", nomenclaturePmasc.getSignDb());
        params.addValue("idSearch", nomenclatureSearch.getId());
        log.info("SQL:" + sql);
        log.info("Params:" + params);
        getJdbcTemplate().update(sql, params);
    }

    @Override
    public List<Nomenclature> getMatchedNomenclatures(NomenclatureSearchCondition condition) {
        String sql = "SELECT n.id,n.name,n.SIGN_DB,s.id_search FROM SPR_NOMENCLATURE n "
                + "join SSP_NOMENCLATURE_SEARCH s on s.id_nomenclature = n.id ";
        if (!StringUtils.isBlank(condition.getName())) {
            sql += " WHERE n.name like '%" + condition.getName().trim() + "%'";
        }
        sql += " ORDER BY n.NAME";
        log.info("SQL:" + sql);
        List<Nomenclature> list = getJdbcTemplate().getJdbcOperations().query(sql, new NomenclatureMapper(true));
        return list;
    }

    @Override
    public void removeNomenclatureAssociation(Nomenclature nomenclature) {
        String sql = "DELETE FROM SSP_NOMENCLATURE_SEARCH WHERE id_nomenclature = :id";
        log.info("SQL:" + sql);
        getJdbcTemplate().update(sql, new MapSqlParameterSource("id", nomenclature.getNomenclatureId()));
    }

}
