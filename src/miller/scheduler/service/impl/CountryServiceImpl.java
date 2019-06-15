package miller.scheduler.service.impl;

import miller.scheduler.domain.Country;
import miller.scheduler.repository.CountryRepository;
import miller.scheduler.service.CountryService;
import miller.scheduler.service.dto.CountryDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Country save(Country country, String username){
        return repository.saveOrUpdate(country, username);
    }

    @Override
    public boolean delete(Integer id){
        return repository.delete(id);
    }

    @Override
    public List<Country> findAll(){
        return repository.findAll();
    }

    @Override
    public Optional<Country> findOne(Integer id){
        return repository.findById(id);
    }

    @Override
    public Optional<Country> findByName(String name){
        return repository.findOneByName(name);
    }

    @Override
    public List<CountryDto> findAllAsDto(){
        return findAll().stream().map(CountryDto::new).collect(Collectors.toList());
    }

    @Override
    public CountryDto create(CountryDto countryDto, String username){
        Country country = new Country();
        country.setCountry(countryDto.getCountry());
        repository.saveOrUpdate(country, username);

        return new CountryDto(country);
    }

    @Override
    public Optional<CountryDto> update(CountryDto countryDto, String username){
        return Optional.of(repository
                .findById(countryDto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(country -> {
                    country.setCountry(countryDto.getCountry());
                    country = repository.saveOrUpdate(country, username);
                    return country;
                })
                .map(CountryDto::new);
    }
}
