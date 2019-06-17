package miller.scheduler.repository;

import miller.scheduler.domain.User;
import miller.scheduler.repository.mapper.UserMapper;

public class UserRepository extends AbstractRepository<User> {
    public UserRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From user Where userId = ?";

        nxtIdStatement = "Select max(userId) + 1 From user";

        insertStatement = "Insert into user (username, password, active, createDate, createBy, lastUpdate, lastUpdatedBy, userId) Values (?, ?, ?, ?, ?, ?, ?, ?)";

        updateStatement = "Update user Set username = ?, password = ?, active = ?, createDate = ?, createBy = ?, lastUpdate = ?, lastUpdatedBy = ? Where userId = ?";

        selectStatement = "Select * From user";

        findByIdStatement = "Select * from user Where userId = ?";

        findByNameStatement = "Select * from user Where username = ?";

        mapper = new UserMapper();
    }
}