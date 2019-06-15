package miller.scheduler.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * https://www.baeldung.com/java-connection-pooling
 */
public interface DatabaseConnection {
    Connection getConnection() throws SQLException;

    boolean releaseConnection(Connection connection);

    int getSize();

    String getUrl();

    String getUser();

    String getPassword();

}
