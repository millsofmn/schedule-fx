package miller.scheduler.service.impl;

import miller.scheduler.domain.*;
import miller.scheduler.repository.*;
import miller.scheduler.service.AppointmentService;
import miller.scheduler.service.dto.AppointmentDto;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AppointmentServiceImpl implements AppointmentService {
    private final Logger log = Logger.getLogger(AppointmentServiceImpl.class.getName());

    private final AppointmentRepository repository;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public AppointmentServiceImpl(AppointmentRepository repository,
                                  AddressRepository addressRepository,
                                  CityRepository cityRepository,
                                  CountryRepository countryRepository,
                                  CustomerRepository customerRepository, UserRepository userRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AppointmentDto> findAllAsDto() {
        return findAll().stream()
                .map(this::appointmentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto create(AppointmentDto dto, String username) {
        Appointment entity = dtoToAppointment(dto);

        repository.saveOrUpdate(entity, username);

        return appointmentToDto(entity);
    }

    @Override
    public Optional<AppointmentDto> update(AppointmentDto dto, String username) {
        return Optional.of(repository
                .findById(dto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(entity -> {
                    entity = dtoToAppointment(dto);
                    entity = repository.saveOrUpdate(entity, username);
                    return entity;
                })
                .map(this::appointmentToDto);
    }

    @Override
    public Appointment save(Appointment entity, String username) {
        return repository.saveOrUpdate(entity, username);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    @Override
    public List<Appointment> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Appointment> findOne(Integer id) {
        return repository.findById(id);
    }

    private AppointmentDto appointmentToDto(Appointment appointment){
        Customer customer = customerRepository.getOne(appointment.getCustomerId());
        Address address = addressRepository.getOne(customer.getAddressId());
        City city = cityRepository.getOne(address.getCityId());
        Country country = countryRepository.getOne(city.getCountryId());

        return new AppointmentDto(appointment, customer, address, city, country);
    }

    private Appointment dtoToAppointment(AppointmentDto dto){
        Appointment entity = new Appointment();
        entity.setId(dto.getId());
        entity.setCustomerId(dto.getCustomer().getId());
        entity.setUserId(dto.getUser());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setLocation(dto.getLocation());
        entity.setContact(dto.getContact());
        entity.setUrl(dto.getUrl());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        log.info("Created Appointment from DTO : " + entity);
        return entity;
    }
}
