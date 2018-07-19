package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.CustomUserRepository;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class will load in (bootstrap) data and check if the user data was properly loaded into the
 * database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomUserServiceRepositoryImplIT {

    /**
     * An object to allowing interfacing with the data layer and the CustomUser table.
     */
    @Autowired
    private CustomUserRepository userRepository;
    /**
     * A service providing access to CRUD methods on the CustomUser table.
     */
    private UserServiceRepositoryImpl userServiceRepository;

    @Before
    public void setUp() {
        userServiceRepository = new UserServiceRepositoryImpl(userRepository);
    }

    /**
     * This test checks if there are four users inserted into the database.
     */
    @Test
    public void listAll() {
        List<CustomUser> users = userServiceRepository.listAll();
        long expectedNumberOfUsers = userRepository.count();
        assertEquals(expectedNumberOfUsers, users.size());
    }

    /**
     * This test is expected to throw an exception because an invalid user id is used to search
     * for a user.
     */
    @Test(expected = NotFoundException.class)
    public void getById() {
        Long invalidUserId = -1L;
        userServiceRepository.getById(invalidUserId);
    }

    /**
     * This test will attempt to modify a user field and it is expected that this value will
     * differ once it has been updated from the prior stored value.
     */
    @Test
    public void updateUser() {
        // given
        Iterable<CustomUser> users = userRepository.findAll();
        CustomUser user = users.iterator().next();
        Date currentDate = new Date();
        user.setLastUpdated(currentDate);
        // when
        CustomUser updatedUser = userServiceRepository.saveOrUpdate(user);
        Date updatedTime = updatedUser.getLastUpdated();
        // then
        assertEquals(currentDate.toString(), updatedTime.toString());
        assertNotEquals(updatedUser.getDateCreated().toString(), updatedTime.toString());
    }

    /**
     * This test attempts to find a user by specifying the user name and comparing the name of
     * the retrieved user to ensure it matches with the user name used in the search.
     */
    @Test
    public void findByUserName() {
        String validUserName = "mweston";
        CustomUser user = userServiceRepository.findByUserName(validUserName);
        assertEquals(validUserName, user.getUsername());
    }

    /**
     * This test expects to not find a user (null) because the user name used for the search is
     * an invalid user name.
     */
    @Test
    public void findByInvalidUserName() {
        String invalidUserName = "aninvalidname";
        CustomUser user = userServiceRepository.findByUserName(invalidUserName);
        assertNull(user);
    }
}
