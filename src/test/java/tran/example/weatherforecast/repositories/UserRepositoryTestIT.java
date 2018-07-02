package tran.example.weatherforecast.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.domain.User;

import static org.junit.Assert.*;

/**
 * Data will be bootstrapped in to test with the Users table for these set of tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTestIT {

    /**
     * An object to allowing interfacing with the data layer and the User table.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Tests if a User object can be found given a user name.
     */
    @Test
    public void findByUserName() {
        String userName = "mweston";
        User user = userRepository.findByUsername(userName);
        assertEquals(userName, user.getUsername());
    }

    /**
     * Tests if an invalid user name is searched for.
     */
    @Test
    public void findNonExistentUserName() {
        String nonExistantUser = "reallyweirdusername";
        User user = userRepository.findByUsername(nonExistantUser);
        assertNull(user);
    }
}