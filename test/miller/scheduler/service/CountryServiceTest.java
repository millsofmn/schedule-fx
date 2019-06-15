package miller.scheduler.service;

import miller.scheduler.domain.Country;
import miller.scheduler.repository.*;
import miller.scheduler.service.impl.CountryServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CountryServiceTest {
    private String DEFAULT_COUNTRY_NAME = "AAAAAAAAAAAAAA";
    private String UPDATED_COUNTRY_NAME = "BBBBBBBBBBBBBB";
    private String DEFAULT_USERNAME = "UNIT_TESTER";
    private String UPDATED_USERNAME = "UNIT_TESTED";

    private CountryService service;

    private Country entity;

    public Country createEntity(){
        entity = new Country();
        entity.setCountry(DEFAULT_COUNTRY_NAME);

        return entity;
    }

    @Before
    public void setup() throws SQLException {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        CountryRepository countryRepository = new CountryRepository(db);

        service = new CountryServiceImpl(countryRepository);

        entity = createEntity();
    }

    @After
    public void tearDown(){
        service.delete(entity.getId());
    }

    @Test
    public void save(){
        Country actual = service.save(entity, DEFAULT_USERNAME);
        assertNotNull(actual.getId());
        assertEquals(DEFAULT_COUNTRY_NAME, actual.getCountry());
        assertEquals(DEFAULT_USERNAME, actual.getCreatedBy());
        assertEquals(DEFAULT_USERNAME, actual.getLastUpdatedBy());
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    public void update(){

        Country actual = service.save(entity, DEFAULT_USERNAME);

        Country updated = new Country();
        updated.setId(actual.getId());
        updated.setCountry(UPDATED_COUNTRY_NAME);

        updated = service.save(updated, UPDATED_USERNAME);

        assertEquals(UPDATED_COUNTRY_NAME, updated.getCountry());
        assertEquals(DEFAULT_USERNAME, updated.getCreatedBy());
        assertEquals(UPDATED_USERNAME, updated.getLastUpdatedBy());
        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS), updated.getCreateDate().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(actual.getLastUpdate().isBefore(updated.getLastUpdate()));
    }

    @Test
    public void findOne(){
        Country actual = service.save(entity, DEFAULT_USERNAME);

        Optional<Country> optional = service.findOne(actual.getId());

        assertTrue(optional.isPresent());

        assertEquals(DEFAULT_COUNTRY_NAME, optional.get().getCountry());
        assertEquals(DEFAULT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_USERNAME, optional.get().getLastUpdatedBy());
        assertNotNull(optional.get().getId());
        assertNotNull(optional.get().getCreateDate());
        assertNotNull(optional.get().getLastUpdate());
    }

    @Test
    public void findAll(){
        Country actual = service.save(entity, DEFAULT_USERNAME);

        List<Country> all = service.findAll();
        assertTrue(all.size() >= 1);
    }
}