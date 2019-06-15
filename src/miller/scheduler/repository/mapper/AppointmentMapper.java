package miller.scheduler.repository.mapper;

import miller.scheduler.domain.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentMapper implements Mapper<Appointment>{
    @Override
    public Appointment mapper(ResultSet rs) throws SQLException {
        Appointment entity = new Appointment();

        entity.setId(rs.getInt("appointmentId"));
        entity.setCustomerId(rs.getInt("customerId"));
        entity.setTitle(rs.getString("title"));
        entity.setDescription(rs.getString("description"));
        entity.setLocation(rs.getString("location"));
        entity.setContact(rs.getString("contact"));
        entity.setUrl(rs.getString("url"));
        entity.setStart(rs.getTimestamp("start").toInstant());
        entity.setEnd(rs.getTimestamp("end").toInstant());
        entity.setCreateDate(rs.getTimestamp("createDate").toInstant());
        entity.setCreatedBy(rs.getString("createdBy"));
        entity.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        entity.setLastUpdatedBy(rs.getString("lastUpdateBy"));

        return entity;
    }

    @Override
    public void mapper(PreparedStatement ps, Appointment entity) throws SQLException {
        ps.setInt(1, entity.getCustomerId());
        ps.setString(2, entity.getTitle());
        ps.setString(3, entity.getDescription());
        ps.setString(4, entity.getLocation());
        ps.setString(5, entity.getContact());
        ps.setString(6, entity.getUrl());
        if(entity.getStart() != null) {
            ps.setTimestamp(7, Timestamp.from(entity.getStart()));
        } else {
            ps.setTimestamp(7, null);
        }
        if(entity.getStart() != null) {
            ps.setTimestamp(8, Timestamp.from(entity.getEnd()));
        } else {
            ps.setTimestamp(8, null);
        }
        ps.setTimestamp(9, Timestamp.from(entity.getCreateDate()));
        ps.setString(10, entity.getCreatedBy());
        ps.setTimestamp(11, Timestamp.from(entity.getLastUpdate()));
        ps.setString(12, entity.getLastUpdatedBy());
        ps.setInt(13, entity.getId());
    }
}
