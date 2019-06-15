package miller.scheduler.repository;

import miller.scheduler.domain.User;
import miller.scheduler.repository.mapper.UserMapper;

public class UserRepository extends AbstractRepository<User> {
    public UserRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From user Where userId = ?";

        NXT_ID_STATEMENT = "Select max(userId) + 1 From user";

        INSERT_STATEMENT = "Insert into user (username, password, active, createDate, createBy, lastUpdate, lastUpdatedBy, userId) Values (?, ?, ?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update user Set username = ?, password = ?, active = ?, createDate = ?, createBy = ?, lastUpdate = ?, lastUpdatedBy = ? Where userId = ?";

        SELECT_STATEMENT = "Select * From user";

        FIND_BY_ID_STATEMENT = "Select * from user Where userId = ?";

        FIND_BY_NAME_STATEMENT = "Select * from user Where username = ?";

        mapper = new UserMapper();
    }
}