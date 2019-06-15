package miller.scheduler.repository.mapper;

import miller.scheduler.domain.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CountryMapper implements Mapper<Country> {

    @Override
    public Country mapper(ResultSet rs) throws SQLException {
        Country country = new Country();

        country.setId(rs.getInt("countryId"));
        country.setCountry(rs.getString("country"));
        country.setCreateDate(rs.getTimestamp("createDate").toInstant());
        country.setCreatedBy(rs.getString("createdBy"));
        country.setLastUpdate(rs.getTimestamp("lastUpdate").toInstant());
        country.setLastUpdatedBy(rs.getString("lastUpdateBy"));

        return country;
    }

    @Override
    public void mapper(PreparedStatement ps, Country entity) throws SQLException {
        ps.setString(1, entity.getCountry());
        ps.setTimestamp(2, Timestamp.from(entity.getCreateDate()));
        ps.setString(3, entity.getCreatedBy());
        ps.setTimestamp(4, Timestamp.from(entity.getLastUpdate()));
        ps.setString(5, entity.getLastUpdatedBy());
        ps.setInt(6, entity.getId());
    }
}
