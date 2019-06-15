package miller.scheduler.service;

import miller.scheduler.domain.Address;
import miller.scheduler.repository.*;
import miller.scheduler.service.impl.AddressServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AddressServiceTest {

    private int DEFAULT_COUNTRY_ID = 1;
    private int UPDATED_COUNTRY_ID = 2;
    private String DEFAULT_ADDRESS1 = "AAAAAAAAA";
    private String UPDATED_ADDRESS1 = "AAAAAAAAA";
    private String DEFAULT_ADDRESS2 = "BBBBBBBBB";
    private String UPDATED_ADDRESS2 = "BBBBBBBBB";
    private int DEFAULT_CITY_ID = 1;
    private int UPDATED_CITY_ID = 2;
    private String DEFAULT_POSTAL_CODE = "AAAAAAA";
    private String UPDATED_POSTAL_CODE = "BBBBBBB";
    private String DEFAULT_PHONE = "(999) 234-0000";
    private String UPDATED_PHONE = "(555) 234-2222";
    private String DEFAULT_USERNAME = "UNIT_TESTER";
    private String UPDATED_USERNAME = "UNIT_TESTED";

    private AddressService service;

    private Address entity;

    private Address createEntity() {
        entity = new Address();
        entity.setAddress(DEFAULT_ADDRESS1);
        entity.setAddress2(DEFAULT_ADDRESS2);
        entity.setCityId(DEFAULT_CITY_ID);
        entity.setPostalCode(DEFAULT_POSTAL_CODE);
        entity.setPhone(DEFAULT_PHONE);

        return entity;
    }

    @Before
    public void setup() throws SQLException {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        AddressRepository addressRepository = new AddressRepository(db);
        CityRepository cityRepository = new CityRepository(db);
        CountryRepository countryRepository = new CountryRepository(db);

        service = new AddressServiceImpl(cityRepository, countryRepository, addressRepository);

        entity = createEntity();
    }

    @After
    public void tearDown() {
        service.delete(entity.getId());
    }

    @Test
    public void save() {
        Address actual = service.save(entity, DEFAULT_USERNAME);
        assertNotNull(actual.getId());
        assertEquals(DEFAULT_ADDRESS1, actual.getAddress());
        assertEquals(DEFAULT_ADDRESS2, actual.getAddress2());
        assertEquals(DEFAULT_CITY_ID, actual.getCityId());
        assertEquals(DEFAULT_POSTAL_CODE, actual.getPostalCode());
        assertEquals(DEFAULT_PHONE, actual.getPhone());

        assertEquals(DEFAULT_USERNAME, actual.getCreatedBy());
        assertEquals(DEFAULT_USERNAME, actual.getLastUpdatedBy());
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    public void update() {
        Address actual = service.save(entity, DEFAULT_USERNAME);
        Optional<Address> expectedOptional = service.findOne(actual.getId());

        assertTrue(expectedOptional.isPresent());

        Address updated = new Address();
        updated.setId(actual.getId());
        updated.setAddress(UPDATED_ADDRESS1);
        updated.setAddress2(UPDATED_ADDRESS2);
        updated.setCityId(UPDATED_CITY_ID);
        updated.setPostalCode(UPDATED_POSTAL_CODE);
        updated.setPhone(UPDATED_PHONE);

        updated = service.save(updated, UPDATED_USERNAME);

        assertEquals(UPDATED_ADDRESS1, updated.getAddress());
        assertEquals(UPDATED_ADDRESS2, updated.getAddress2());
        assertEquals(UPDATED_CITY_ID, updated.getCityId());
        assertEquals(UPDATED_POSTAL_CODE, updated.getPostalCode());
        assertEquals(UPDATED_PHONE, updated.getPhone());

        assertEquals(DEFAULT_USERNAME, updated.getCreatedBy());
        assertEquals(UPDATED_USERNAME, updated.getLastUpdatedBy());
        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS), updated.getCreateDate().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(actual.getLastUpdate().isBefore(updated.getLastUpdate()));
    }

    @Test
    public void findOne(){
        Address actual = service.save(entity, DEFAULT_USERNAME);

        Optional<Address> optional = service.findOne(actual.getId());

        assertTrue(optional.isPresent());

        assertEquals(DEFAULT_ADDRESS1, optional.get().getAddress());
        assertEquals(DEFAULT_ADDRESS2, optional.get().getAddress2());
        assertEquals(DEFAULT_CITY_ID, optional.get().getCityId());
        assertEquals(DEFAULT_POSTAL_CODE, optional.get().getPostalCode());
        assertEquals(DEFAULT_PHONE, optional.get().getPhone());
        assertEquals(DEFAULT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getLastUpdatedBy());
    }

    @Test
    public void findAll() {
        Address actual = service.save(entity, DEFAULT_USERNAME);

        List<Address> all = service.findAll();
        assertTrue(all.size() >= 1);
    }
}
