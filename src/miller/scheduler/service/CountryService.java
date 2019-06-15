package miller.scheduler.service;

import miller.scheduler.domain.Country;
import miller.scheduler.service.dto.CountryDto;

import java.util.List;
import java.util.Optional;

public interface CountryService extends Service<Country, Integer> {
    Optional<Country> findByName(String name);

    List<CountryDto> findAllAsDto();

    CountryDto create(CountryDto countryDto, String username);

    Optional<CountryDto> update(CountryDto countryDto, String username);
}
