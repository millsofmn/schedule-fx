package miller.scheduler.repository;

import miller.scheduler.domain.Address;
import miller.scheduler.repository.mapper.AddressMapper;

public class AddressRepository extends AbstractRepository<Address> {

    public AddressRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From address Where addressId = ?";

        nxtIdStatement = "Select max(addressId) + 1 From address";

        insertStatement = "Insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy, addressId) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        updateStatement = "Update address Set address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where addressId = ?";

        selectStatement = "Select * From address";

        findByIdStatement = "Select * from address Where addressId = ?";

        mapper = new AddressMapper();
    }
}
