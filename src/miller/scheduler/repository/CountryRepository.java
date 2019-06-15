package miller.scheduler.repository;

import miller.scheduler.domain.Country;
import miller.scheduler.repository.mapper.CountryMapper;

public class CountryRepository extends AbstractRepository<Country> {
    public CountryRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From country Where countryId = ?";

        NXT_ID_STATEMENT = "Select max(countryId) + 1 From country";

        INSERT_STATEMENT = "Insert into country (country, createDate, createdBy, lastUpdate, lastUpdateBy, countryId) Values (?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update country Set country = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where countryId = ?";

        SELECT_STATEMENT = "Select * From country";

        FIND_BY_ID_STATEMENT = "Select * from country Where countryId = ?";

        FIND_BY_NAME_STATEMENT = "Select * from country Where country = ?";

        mapper = new CountryMapper();
    }
}
