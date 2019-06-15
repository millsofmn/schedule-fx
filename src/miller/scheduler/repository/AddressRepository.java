package miller.scheduler.repository;

import miller.scheduler.domain.Address;
import miller.scheduler.repository.mapper.AddressMapper;

public class AddressRepository extends AbstractRepository<Address> {

    public AddressRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From address Where addressId = ?";

        NXT_ID_STATEMENT = "Select max(addressId) + 1 From address";

        INSERT_STATEMENT = "Insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy, addressId) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update address Set address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where addressId = ?";

        SELECT_STATEMENT = "Select * From address";

        FIND_BY_ID_STATEMENT = "Select * from address Where addressId = ?";

        mapper = new AddressMapper();
    }
}
