package miller.scheduler.repository.mapper;

import miller.scheduler.domain.City;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CityMapper implements Mapper<City> {
    @Override
    public City mapper(ResultSet rs) throws SQLException {
        City entity = new City();

        entity.setId(rs.getInt("cityId"));
        entity.setCity(rs.getString("city"));
        entity.setCountryId(rs.getInt("countryId"));
        entity.setCreateDate(rs.getTimestamp("createDate").toInstant());
        entity.setCreatedBy(rs.getString("createdBy"));
        entity.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        entity.setLastUpdatedBy(rs.getString("lastUpdateBy"));

        return entity;
    }

    @Override
    public void mapper(PreparedStatement ps, City entity) throws SQLException {
        ps.setString(1, entity.getCity());
        ps.setInt(2, entity.getCountryId());
        ps.setTimestamp(3, Timestamp.from(entity.getCreateDate()));
        ps.setString(4, entity.getCreatedBy());
        ps.setTimestamp(5, Timestamp.from(entity.getLastUpdate()));
        ps.setString(6, entity.getLastUpdatedBy());
        ps.setInt(7, entity.getId());
    }
}
