package miller.scheduler.service;

import java.util.List;
import java.util.Optional;

public interface DtoService<T> {
    List<T> findAllAsDto();

    T create(T dto, String username);

    Optional<T> update(T dto, String username);
}
