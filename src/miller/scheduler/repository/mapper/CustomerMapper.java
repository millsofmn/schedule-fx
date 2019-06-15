package miller.scheduler.repository.mapper;

import miller.scheduler.domain.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CustomerMapper implements Mapper<Customer> {

    @Override
    public Customer mapper(ResultSet rs) throws SQLException {
        Customer entity = new Customer();

        entity.setId(rs.getInt("customerId"));
        entity.setCustomerName(rs.getString("customerName"));
        entity.setAddressId(rs.getInt("addressId"));
        entity.setActive(rs.getBoolean("active"));
        entity.setCreateDate(rs.getTimestamp("createDate").toInstant());
        entity.setCreatedBy(rs.getString("createdBy"));
        entity.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        entity.setLastUpdatedBy(rs.getString("lastUpdateBy"));

        return entity;
    }

    @Override
    public void mapper(PreparedStatement ps, Customer entity) throws SQLException {
        ps.setString(1, entity.getCustomerName());
        ps.setInt(2, entity.getAddressId());
        ps.setBoolean(3, entity.isActive());
        ps.setTimestamp(4, Timestamp.from(entity.getCreateDate()));
        ps.setString(5, entity.getCreatedBy());
        ps.setTimestamp(6, Timestamp.from(entity.getLastUpdate()));
        ps.setString(7, entity.getLastUpdatedBy());
        ps.setInt(8, entity.getId());
    }
}
