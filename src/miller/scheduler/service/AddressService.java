package miller.scheduler.service;

import miller.scheduler.domain.Address;
import miller.scheduler.service.dto.AddressDto;

import java.util.List;
import java.util.Optional;

public interface AddressService extends Service<Address, Integer> {
    List<AddressDto> findAllAsDto();

    AddressDto create(AddressDto dto, String username);

    Optional<AddressDto> update(AddressDto dto, String username);
}
