package miller.scheduler.service.impl;

import miller.scheduler.domain.City;
import miller.scheduler.domain.Country;
import miller.scheduler.repository.CityRepository;
import miller.scheduler.repository.CountryRepository;
import miller.scheduler.service.CityService;
import miller.scheduler.service.dto.CityDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CityServiceImpl implements CityService {

    private final CityRepository repository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository repository, CountryRepository countryRepository) {
        this.repository = repository;
        this.countryRepository = countryRepository;
    }

    @Override
    public City save(City entity, String username) {
        return repository.saveOrUpdate(entity, username);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    @Override
    public List<City> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<City> findOne(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<CityDto> findAllAsDto(){
        return findAll().stream().map(city -> {
            Country country = countryRepository.getOne(city.getCountryId());
            CityDto dto = new CityDto(city, country);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public CityDto create(CityDto dto, String username){
        City city = new City();
        city.setCity(dto.getCity());
        city.setCountryId(dto.getCountry().getId());

        repository.saveOrUpdate(city, username);

        Country country = countryRepository.getOne(city.getCountryId());

        return new CityDto(city, country);
    }

    @Override
    public Optional<CityDto> update(CityDto dto, String username){
        // lambda - this searches repository for city and updated it if found
        return Optional.of(repository
                .findById(dto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(entity -> {
                    entity.setCity(dto.getCity());
                    entity.setCountryId(dto.getCountry().getId());
                    entity = repository.saveOrUpdate(entity, username);
                    return entity;
                })
                .map(city -> {
                    Country country = countryRepository.getOne(city.getCountryId());
                    return new CityDto(city, country);
                });
    }
}
