package miller.scheduler.service;

import miller.scheduler.domain.User;
import miller.scheduler.repository.*;
import miller.scheduler.service.exception.InvalidUsernameOrPasswordException;
import miller.scheduler.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceTest {

    private String DEFAULT_USERNAME = "AAAAAAA";
    private String UPDATED_USERNAME = "BBBBBBB";
    private String DEFAULT_PASSWORD = "AAAAAAA";
    private String UPDATED_PASSWORD = "BBBBBBB";
    private boolean DEFAULT_ACTIVE = true;
    private boolean UPDATED_ACTIVE = true;
    private String DEFAULT_AUDIT_USERNAME = "UNIT_TESTER";
    private String UPDATED_AUDIT_USERNAME = "UNIT_TESTED";

    private UserService service;

    private User entity;

    private User createEntity() {
        entity = new User();
        entity.setUserName(DEFAULT_USERNAME);
        entity.setPassword(DEFAULT_PASSWORD);
        entity.setActive(DEFAULT_ACTIVE);

        return entity;
    }

    @Before
    public void setup() throws SQLException {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        UserRepository userRepository = new UserRepository(db);

        service = new UserServiceImpl(userRepository);

        entity = createEntity();
    }

    @After
    public void tearDown() {
        service.delete(entity.getId());
    }

    @Test
    public void save() {
        User actual = service.save(entity, DEFAULT_AUDIT_USERNAME);
        assertNotNull(actual.getId());
        assertEquals(DEFAULT_USERNAME, actual.getUserName());
        assertEquals(DEFAULT_PASSWORD, actual.getPassword());
        assertEquals(DEFAULT_ACTIVE, actual.isActive());
        assertEquals(DEFAULT_AUDIT_USERNAME, actual.getCreatedBy());
        assertEquals(DEFAULT_AUDIT_USERNAME, actual.getLastUpdatedBy());
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    public void update() {
        User actual = service.save(entity, DEFAULT_AUDIT_USERNAME);
        Optional<User> expectedOptional = service.findOne(actual.getId());

        assertTrue(expectedOptional.isPresent());

        User updated = new User();
        updated.setId(actual.getId());
        updated.setUserName(UPDATED_USERNAME);
        updated.setPassword(UPDATED_PASSWORD);
        updated.setActive(UPDATED_ACTIVE);
        updated = service.save(updated, UPDATED_AUDIT_USERNAME);


        assertEquals(UPDATED_USERNAME, updated.getUserName());
        assertEquals(UPDATED_PASSWORD, updated.getPassword());
        assertEquals(UPDATED_ACTIVE, updated.isActive());

        assertEquals(DEFAULT_AUDIT_USERNAME, updated.getCreatedBy());
        assertEquals(UPDATED_AUDIT_USERNAME, updated.getLastUpdatedBy());
        assertEquals(actual.getId(), updated.getId());
        assertEquals(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS), updated.getCreateDate().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(actual.getLastUpdate().isBefore(updated.getLastUpdate()));
    }

    @Test
    public void findOne(){
        User actual = service.save(entity, DEFAULT_AUDIT_USERNAME);

        Optional<User> optional = service.findOne(actual.getId());

        assertTrue(optional.isPresent());
        assertEquals(DEFAULT_USERNAME, optional.get().getUserName());
        assertEquals(DEFAULT_PASSWORD, optional.get().getPassword());
        assertEquals(DEFAULT_ACTIVE, optional.get().isActive());
        assertEquals(DEFAULT_AUDIT_USERNAME, optional.get().getCreatedBy());
        assertEquals(DEFAULT_AUDIT_USERNAME, optional.get().getLastUpdatedBy());
    }

    @Test
    public void findAll() {
        User actual = service.save(entity, DEFAULT_AUDIT_USERNAME);

        List<User> all = service.findAll();
        assertTrue(all.size() >= 1);
    }

    @Test(expected = InvalidUsernameOrPasswordException.class)
    public void authenticateUserNotFound(){
        entity = service.save(entity, DEFAULT_AUDIT_USERNAME);
        service.authenticate(UPDATED_USERNAME, DEFAULT_PASSWORD);
    }

    @Test
    public void authenticate(){
        entity = service.save(entity, DEFAULT_AUDIT_USERNAME);
        User notValid = service.authenticate(DEFAULT_USERNAME, UPDATED_PASSWORD);
        assertNull(notValid);

       User valid = service.authenticate(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertNotNull(valid);
    }
}