package miller.scheduler.service.impl;

import miller.scheduler.domain.Address;
import miller.scheduler.domain.City;
import miller.scheduler.domain.Country;
import miller.scheduler.domain.Customer;
import miller.scheduler.repository.AddressRepository;
import miller.scheduler.repository.CityRepository;
import miller.scheduler.repository.CountryRepository;
import miller.scheduler.repository.CustomerRepository;
import miller.scheduler.service.CustomerService;
import miller.scheduler.service.dto.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CustomerRepository repository;

    public CustomerServiceImpl(AddressRepository addressRepository, CityRepository cityRepository, CountryRepository countryRepository, CustomerRepository repository) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.repository = repository;
    }

    @Override
    public Customer save(Customer entity, String username) {
        return repository.saveOrUpdate(entity, username);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> findOne(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<CustomerDto> findAllAsDto(){
        return findAll().stream()
                .map(this::customerToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto create(CustomerDto dto, String username){
        Customer entity = dtoToCustomer(dto);

        repository.saveOrUpdate(entity, username);

        return customerToDto(entity);
    }

    @Override
    public Optional<CustomerDto> update(CustomerDto dto, String username){
        return Optional.of(repository
                .findById(dto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(entity -> {
                    entity = dtoToCustomer(dto);
                    entity = repository.saveOrUpdate(entity, username);
                    return entity;
                })
                .map(this::customerToDto);
    }

    private CustomerDto customerToDto(Customer customer){
        Address address = addressRepository.getOne(customer.getAddressId());
        City city = cityRepository.getOne(address.getCityId());
        Country country = countryRepository.getOne(city.getCountryId());

        return new CustomerDto(customer, address, city, country);
    }

    private Customer dtoToCustomer(CustomerDto dto){
        Customer entity = new Customer();
        entity.setId(dto.getId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setActive(dto.isActive());
        entity.setAddressId(dto.getAddress().getId());

        return entity;
    }
}
