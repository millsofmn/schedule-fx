package miller.scheduler.service;

import miller.scheduler.domain.Customer;
import miller.scheduler.repository.*;
import miller.scheduler.service.impl.CustomerServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomerServiceTest {

    private String DEFAULT_CUSTOMER_NAME = "AAAAAAAAA";
    private String UPDATED_CUSTOMER_NAME = "BBBBBBBBB";
    private int DEFAULT_ADDRESS_ID = 1;
    private int UPDATED_ADDRESS_ID = 2;
    private boolean DEFAULT_ACTIVE = true;
    private boolean UPDATED_ACTIVE = false;
    private String DEFAULT_USERNAME = "UNIT_TESTER";
    private String UPDATED_USERNAME = "UNIT_TESTED";

    private CustomerService service;

    private Customer entity;

    private Customer createEntity() {
        entity = new Customer();
        entity.setCustomerName(DEFAULT_CUSTOMER_NAME);
        entity.setAddressId(DEFAULT_ADDRESS_ID);
        entity.setActive(DEFAULT_ACTIVE);

        return entity;
    }

    @Before
    public void setup() throws SQLException {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        AddressRepository addressRepository = new AddressRepository(db);
        CityRepository cityRepository = new CityRepository(db);
        CountryRepository countryRepository = new CountryRepository(db);
        CustomerRepository customerRepository = new CustomerRepository(db);

        service = new CustomerServiceImpl(addressRepository, cityRepository, countryRepository, customerRepository);

        entity = createEntity();
    }

    @After
    public void tearDown() {
        service.delete(entity.getId());
    }

    @Test
    public void save() {
        Customer actual = service.save(entity, DEFAULT_USERNAME);
        assertNotNull(actual.getId());
        assertEquals(DEFAULT_CUSTOMER_NAME, actual.getCustomerName());
        assertEquals(DEFAULT_ADDRESS_ID, actual.getAddressId());
        assertEquals(DEFAULT_ACTIVE, actual.isActive());

        assertEquals(DEFAULT_USERNAME, actual.getCreatedBy());
        assertEquals(DEFAULT_USERNAME, actual.getLastUpdatedBy());
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    public void update() {
        Customer actual = service.save(entity, DEFAULT_USERNAME);
        Optional<Customer> expectedOptional = service.findOne(actual.getId());

        assertTrue(expectedOptional.isPresent());

        Customer updated = new Customer();
        updated.setId(actual.getId());
        updated.setCustomerName(UPDATED_CUSTOMER_NAME);
        updated.setAddressId(UPDATED_ADDRESS_ID);
        updated.setActive(UPDATED_ACTIVE);

        updated = service.save(updated, UPDATED_USERNAME);

        assertEquals(UPDATED_CUSTOMER_NAME, updated.getCustomerName());
        assertEquals(UPDATED_ADDRESS_ID, updated.getAddressId());
        assertEquals(UPDATED_ACTIVE, updated.isActive());

        assertEquals(DEFAULT_USERNAME, updated.getCreatedBy());
        assertEquals(UPDATED_USERNAME, updated.getLastUpdatedBy());
        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS), updated.getCreateDate().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(actual.getLastUpdate().isBefore(updated.getLastUpdate()));
    }

    @Test
    public void findOne(){
        Customer actual = service.save(entity, DEFAULT_USERNAME);

        Optional<Customer> optional = service.findOne(actual.getId());

        assertTrue(optional.isPresent());

        assertEquals(DEFAULT_CUSTOMER_NAME, optional.get().getCustomerName());
        assertEquals(DEFAULT_ADDRESS_ID, optional.get().getAddressId());
        assertEquals(DEFAULT_ACTIVE, optional.get().isActive());
        assertEquals(DEFAULT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getLastUpdatedBy());
    }

    @Test
    public void findAll() {
        Customer actual = service.save(entity, DEFAULT_USERNAME);

        List<Customer> all = service.findAll();
        assertTrue(all.size() >= 1);
    }
}