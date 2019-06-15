package miller.scheduler.repository.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T mapper(ResultSet rs) throws SQLException;

    void mapper(PreparedStatement ps, T entity) throws SQLException;

}
