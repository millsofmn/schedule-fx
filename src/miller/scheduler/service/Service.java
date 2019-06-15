package miller.scheduler.service;

import java.util.List;
import java.util.Optional;

public interface Service<T, X> {
    T save(T entity, String username);

    boolean delete(X id);

    List<T> findAll();

    Optional<T> findOne(X id);
}
