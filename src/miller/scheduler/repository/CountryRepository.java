package miller.scheduler.repository;

import miller.scheduler.domain.Country;
import miller.scheduler.repository.mapper.CountryMapper;

public class CountryRepository extends AbstractRepository<Country> {
    public CountryRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From country Where countryId = ?";

        nxtIdStatement = "Select max(countryId) + 1 From country";

        insertStatement = "Insert into country (country, createDate, createdBy, lastUpdate, lastUpdateBy, countryId) Values (?, ?, ?, ?, ?, ?)";

        updateStatement = "Update country Set country = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where countryId = ?";

        selectStatement = "Select * From country";

        findByIdStatement = "Select * from country Where countryId = ?";

        findByNameStatement = "Select * from country Where country = ?";

        deletePrecheckStatement = "select count(*) from city where countryId = ?";

        mapper = new CountryMapper();
    }
}
