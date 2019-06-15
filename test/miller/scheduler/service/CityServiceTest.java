package miller.scheduler.service;

import miller.scheduler.domain.City;
import miller.scheduler.repository.*;
import miller.scheduler.service.impl.CityServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class CityServiceTest {

    private int DEFAULT_COUNTRY_ID = 1;
    private int UPDATED_COUNTRY_ID = 2;
    private String DEFAULT_CITY = "AAAAAAA";
    private String UPDATED_CITY = "BBBBBBB";
    private String DEFAULT_USERNAME = "UNIT_TESTER";
    private String UPDATED_USERNAME = "UNIT_TESTED";

    private CityService service;

    private City entity;

    public City createEntity(){
        entity = new City();
        entity.setCity(DEFAULT_CITY);
        entity.setCountryId(DEFAULT_COUNTRY_ID);

        return entity;
    }

    @Before
    public void setup() throws SQLException {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        CityRepository cityRepository = new CityRepository(db);
        CountryRepository countryRepository = new CountryRepository(db);

        service = new CityServiceImpl(cityRepository, countryRepository);

        entity = createEntity();
    }

    @After
    public void tearDown(){
        service.delete(entity.getId());
    }

    @Test
    public void save(){
        City actual = service.save(entity, DEFAULT_USERNAME);
        assertNotNull(actual.getId());
        assertEquals(DEFAULT_CITY, actual.getCity());
        assertEquals(DEFAULT_USERNAME, actual.getCreatedBy());
        assertEquals(DEFAULT_USERNAME, actual.getLastUpdatedBy());
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    public void update(){
        City actual = service.save(entity, DEFAULT_USERNAME);
        Optional<City> expectedOptional = service.findOne(actual.getId());

        assertTrue(expectedOptional.isPresent());

        City updated = new City();
        updated.setId(actual.getId());
        updated.setCountryId(UPDATED_COUNTRY_ID);
        updated.setCity(UPDATED_CITY);
        updated = service.save(updated, UPDATED_USERNAME);

        assertEquals(UPDATED_CITY, updated.getCity());
        assertEquals(UPDATED_COUNTRY_ID, updated.getCountryId());

        assertEquals(DEFAULT_USERNAME, updated.getCreatedBy());
        assertEquals(UPDATED_USERNAME, updated.getLastUpdatedBy());
        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS), updated.getCreateDate().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(actual.getLastUpdate().isBefore(updated.getLastUpdate()));
    }

    @Test
    public void findOne(){
        City actual = service.save(entity, DEFAULT_USERNAME);

        Optional<City> optional = service.findOne(actual.getId());

        assertTrue(optional.isPresent());

        assertEquals(DEFAULT_CITY, actual.getCity());
        assertEquals(DEFAULT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getLastUpdatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getLastUpdatedBy());
    }

    @Test
    public void findAll(){
        City actual = service.save(entity, DEFAULT_USERNAME);

        List<City> all = service.findAll();
        assertTrue(all.size() >= 1);
    }
}