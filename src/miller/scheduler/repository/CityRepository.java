package miller.scheduler.repository;

import miller.scheduler.repository.mapper.CityMapper;
import miller.scheduler.domain.City;

import java.util.Optional;

public class CityRepository extends AbstractRepository<City> {


    public Optional<City> findOneByName(String city) {
        return null;
    }

    public CityRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From city Where cityId = ?";

        NXT_ID_STATEMENT = "Select max(cityId) + 1 From city";

        INSERT_STATEMENT = "Insert into city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy, cityId) Values (?, ?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update city Set city = ?, countryId = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where cityId = ?";

        SELECT_STATEMENT = "Select * From city";

        FIND_BY_ID_STATEMENT = "Select * from city Where cityId = ?";

        FIND_BY_NAME_STATEMENT = "Select * from city Where city = ?";

         mapper = new CityMapper();
    }
}
