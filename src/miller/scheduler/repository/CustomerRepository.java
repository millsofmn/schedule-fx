package miller.scheduler.repository;

import miller.scheduler.domain.Customer;
import miller.scheduler.repository.mapper.CustomerMapper;

public class CustomerRepository extends AbstractRepository<Customer> {
    public CustomerRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From customer Where customerId = ?";

        NXT_ID_STATEMENT = "Select max(customerId) + 1 From customer";

        INSERT_STATEMENT = "Insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy, customerId) Values (?, ?, ?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update customer Set customerName = ?, addressId = ?, active = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where customerId = ?";

        SELECT_STATEMENT = "Select * From customer";

        FIND_BY_ID_STATEMENT = "Select * from customer Where customerId = ?";

        DELETE_PRECHECK_STATEMENT = "select count(*) from appointment where customerId = ?";

        mapper = new CustomerMapper();
    }
}
