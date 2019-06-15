package miller.scheduler.repository.mapper;

import miller.scheduler.domain.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AddressMapper implements Mapper<Address> {

    @Override
    public Address mapper(ResultSet rs) throws SQLException {
        Address entity = new Address();

        entity.setId(rs.getInt("addressId"));
        entity.setAddress(rs.getString("address"));
        entity.setAddress2(rs.getString("address2"));
        entity.setCityId(rs.getInt("cityId"));
        entity.setPostalCode(rs.getString("postalCode"));
        entity.setPhone(rs.getString("phone"));
        entity.setCreateDate(rs.getTimestamp("createDate").toInstant());
        entity.setCreatedBy(rs.getString("createdBy"));
        entity.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        entity.setLastUpdatedBy(rs.getString("lastUpdateBy"));

        return entity;
    }

    @Override
    public void mapper(PreparedStatement ps, Address entity) throws SQLException {
        ps.setString(1, entity.getAddress());
        ps.setString(2, entity.getAddress2());
        ps.setInt(3, entity.getCityId());
        ps.setString(4, entity.getPostalCode());
        ps.setString(5, entity.getPhone());
        ps.setTimestamp(6, Timestamp.from(entity.getCreateDate()));
        ps.setString(7, entity.getCreatedBy());
        ps.setTimestamp(8, Timestamp.from(entity.getLastUpdate()));
        ps.setString(9, entity.getLastUpdatedBy());
        ps.setInt(10, entity.getId());
    }
}
