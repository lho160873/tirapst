package pst.arm.server.modules.sync1c.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pst.arm.client.modules.sync1c.domain.Post1C;

/**
 * Created by akozhin on 05.05.15.
 */
public class Post1CRowMapper implements RowMapper<Post1C> {

    @Override
    public Post1C mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post1C post = new Post1C();
        post.setPostId(rs.getInt("POST_ID"));
        post.setPmascCode(rs.getString("PMASC_POST_CODE"));
        post.setCode(rs.getString("CODE"));
        post.setName(rs.getString("NAME"));
        return post;
    }
}
