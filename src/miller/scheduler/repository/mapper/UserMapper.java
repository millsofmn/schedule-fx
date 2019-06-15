package miller.scheduler.repository.mapper;

import miller.scheduler.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserMapper extends AuditMapper implements Mapper<User> {

    @Override
    public User mapper(ResultSet rs) throws SQLException {
        User entity = new User();

        entity.setId(rs.getInt("userId"));
        entity.setUserName(rs.getString("userName"));
        entity.setPassword(rs.getString("password"));
        entity.setActive(rs.getBoolean("active"));
        entity.setCreateDate(rs.getTimestamp("createDate").toInstant());
        entity.setCreatedBy(rs.getString("createBy"));
        entity.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        entity.setLastUpdatedBy(rs.getString("lastUpdatedBy"));

        return entity;
    }

    @Override
    public void mapper(PreparedStatement ps, User entity) throws SQLException {
        setAudit(entity);

        ps.setString(1, entity.getUserName());
        ps.setString(2, entity.getPassword());
        ps.setBoolean(3, entity.isActive());
        ps.setTimestamp(4, Timestamp.from(entity.getCreateDate()));
        ps.setString(5, entity.getCreatedBy());
        ps.setTimestamp(6, Timestamp.from(entity.getLastUpdate()));
        ps.setString(7, entity.getLastUpdatedBy());
        ps.setInt(8, entity.getId());
    }
}
