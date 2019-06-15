package miller.scheduler.service.impl;

import miller.scheduler.domain.Address;
import miller.scheduler.domain.City;
import miller.scheduler.domain.Country;
import miller.scheduler.repository.AddressRepository;
import miller.scheduler.repository.CityRepository;
import miller.scheduler.repository.CountryRepository;
import miller.scheduler.service.AddressService;
import miller.scheduler.service.dto.AddressDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddressServiceImpl implements AddressService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AddressRepository repository;

    public AddressServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, AddressRepository repository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.repository = repository;
    }

    @Override
    public Address save(Address entity, String username) {
        return repository.saveOrUpdate(entity, username);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    @Override
    public List<Address> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Address> findOne(Integer id) {
        return repository.findById(id);
    }


    @Override
    public List<AddressDto> findAllAsDto(){
        return findAll().stream()
                .map(this::addressToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto create(AddressDto dto, String username){
        Address address = dtoToAddress(dto);

        repository.saveOrUpdate(address, username);

        return addressToDto(address);
    }

    @Override
    public Optional<AddressDto> update(AddressDto dto, String username){
        return Optional.of(repository
                .findById(dto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(entity -> {
                    entity = dtoToAddress(dto);
                    entity = repository.saveOrUpdate(entity, username);
                    return entity;
                })
                .map(this::addressToDto);
    }

    private AddressDto addressToDto(Address address){
        City city = cityRepository.getOne(address.getCityId());
        Country country = countryRepository.getOne(city.getCountryId());

        return new AddressDto(address, city, country);
    }

    private Address dtoToAddress(AddressDto dto){
        Address address = new Address();
        address.setId(dto.getId());
        address.setPhone(dto.getPhone());
        address.setAddress(dto.getAddress());
        address.setAddress2(dto.getAddress2());
        address.setPostalCode(dto.getPostalCode());
        address.setCityId(dto.getCity().getId());

        return address;
    }
}
