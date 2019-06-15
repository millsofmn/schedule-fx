/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miller.scheduler.repository;

import java.time.Instant;
import java.util.Optional;

import miller.scheduler.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author m108491
 */
public class UserRepositoryTest {

//    private UserRepository userRepository;
//
//    private User user1;
//
//    @Before
//    public void setUp() throws Exception {
//        user1 = new User();
//        user1.setUserName("test-user");
//        user1.setPassword("password");
//        user1.setActive(true);
//        user1.setCreateDate(Instant.now());
//        user1.setCreatedBy("test");
//        user1.setLastUpdatedBy("test");
//
//        userRepository.insert(user1, "test");
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        userRepository.delete(user1.getId());
//    }
//
//    @Test
//    public void testFindByUserName(){
//        Optional<User> savedUser = userRepository.findOneByName(user1.getUserName());
//        assertNotNull(savedUser);
//    }
//
//    @Test
//    public void testUpdate(){
//        String newPassword = "newPassword";
//
//        user1.setPassword(newPassword);
//        userRepository.update(user1, "test");
//
//        Optional<User> updated = userRepository.findOneByName(user1.getUserName());
//        assertEquals(newPassword, updated.get().getPassword());
//    }
}
