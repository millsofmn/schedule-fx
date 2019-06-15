package miller.scheduler.service;

import miller.scheduler.domain.Customer;
import miller.scheduler.service.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends Service<Customer, Integer> {
    List<CustomerDto> findAllAsDto();

    CustomerDto create(CustomerDto dto, String username);

    Optional<CustomerDto> update(CustomerDto dto, String username);
}
