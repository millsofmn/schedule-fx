package miller.scheduler.repository;

import miller.scheduler.domain.Customer;
import miller.scheduler.repository.mapper.CustomerMapper;

public class CustomerRepository extends AbstractRepository<Customer> {
    public CustomerRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From customer Where customerId = ?";

        nxtIdStatement = "Select max(customerId) + 1 From customer";

        insertStatement = "Insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy, customerId) Values (?, ?, ?, ?, ?, ?, ?, ?)";

        updateStatement = "Update customer Set customerName = ?, addressId = ?, active = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where customerId = ?";

        selectStatement = "Select * From customer";

        findByIdStatement = "Select * from customer Where customerId = ?";

        deletePrecheckStatement = "select count(*) from appointment where customerId = ?";

        mapper = new CustomerMapper();
    }
}
