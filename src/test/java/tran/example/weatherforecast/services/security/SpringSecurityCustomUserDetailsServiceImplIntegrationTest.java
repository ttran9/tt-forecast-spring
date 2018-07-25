package tran.example.weatherforecast.services.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.converters.CustomUserToUserDetails;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.services.repositoryservices.UserServiceRepositoryImpl;

import static org.junit.Assert.*;

/**
 * The below will test if a CustomUser can be retrieved and converted properly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityCustomUserDetailsServiceImplIntegrationTest {
    /**
     * An object to allowing interfacing with the data layer and the CustomUser table.
     */
    @Autowired
    private CustomUserRepository userRepository;
    /**
     * A service providing access to CRUD methods on the CustomUser table.
     */
    private UserServiceRepositoryImpl userServiceRepository;
    /**
     * A service providing the ability to grab a CustomUser through a specified user name and if it can
     * be converted to a domain object without error.
     */
    private SpringSecurityUserDetailsServiceImpl springSecurityUserDetailsService;

    @Before
    public void setUp() {
        userServiceRepository = new UserServiceRepositoryImpl(userRepository);
        springSecurityUserDetailsService = new SpringSecurityUserDetailsServiceImpl();
        springSecurityUserDetailsService.setUserService(userServiceRepository);
        springSecurityUserDetailsService.setUserToUserDetailsConverter(new CustomUserToUserDetails());
    }

    /**
     * This tests if the user can be retrieved given a user name and if the found user has the
     * proper user name.
     */
    @Test
    public void loadUserByUserName() {
        String userName = "mweston";
        UserDetails userDetails = springSecurityUserDetailsService.loadUserByUsername(userName);
        assertEquals(userName, userDetails.getUsername());
    }

    /**
     * This tests if an expected exception is thrown due to an invalid/non-existent user name
     * being used in the search.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByInvalidUserName() {
        String invalidUserName = "notmweston";
        UserDetails userDetails = springSecurityUserDetailsService.loadUserByUsername
                (invalidUserName);
        assertNull(userDetails);
    }
}