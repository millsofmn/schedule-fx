package miller.scheduler.repository;

import miller.scheduler.repository.mapper.CityMapper;
import miller.scheduler.domain.City;

import java.util.Optional;

public class CityRepository extends AbstractRepository<City> {


    @Override
    public Optional<City> findOneByName(String city) {
        return Optional.empty();
    }

    public CityRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From city Where cityId = ?";

        nxtIdStatement = "Select max(cityId) + 1 From city";

        insertStatement = "Insert into city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy, cityId) Values (?, ?, ?, ?, ?, ?, ?)";

        updateStatement = "Update city Set city = ?, countryId = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where cityId = ?";

        selectStatement = "Select * From city";

        findByIdStatement = "Select * from city Where cityId = ?";

        findByNameStatement = "Select * from city Where city = ?";

        deletePrecheckStatement = "select count(*) from address where cityId = ?";

         mapper = new CityMapper();
    }
}
