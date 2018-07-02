package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.repositories.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * The tests below will test for expected behavior when using the user repository service.
 */
public class UserServiceRepositoryImplTest {

    /**
     * An example user name for a sample test user.
     */
    private static final String USER_NAME = "username";
    /**
     * A service providing access to CRUD methods on the User table.
     */
    private UserServiceRepositoryImpl userServiceRepository;
    /**
     * An object to allowing interfacing with the data layer and the User table.
     */
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        userServiceRepository = new UserServiceRepositoryImpl(userRepository);
    }

    /**
     * This expects to find a list of users with an expected size and tests to see if the findAll
     * method was invoked an expected number of times.
     */
    @Test
    public void listAll() {
        // given
        User user = new User();
        List<User> users = new LinkedList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> expectedUsers = userServiceRepository.listAll();

        // then
        assertEquals(users.size(), expectedUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * This test expects to be able to find a user (a non null object) and checks if the findById
     * method was invoked an expected number of times.
     */
    @Test
    public void getById() {
        // given
        User user = new User();
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findById(anyInt())).thenReturn(optionalUser);

        // when
        User obtainedUser = userServiceRepository.getById(anyInt());

        // then
        assertNotNull(obtainedUser);
        verify(userRepository, times(1)).findById(anyInt());
    }

    /**
     * This tests if the persisted user has the same user name as the given user and tests if the
     * save method was invoked an expected number of times.
     */
    @Test
    public void saveUser() {
        // given
        User user = new User();
        user.setUsername(USER_NAME);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User savedUser = userServiceRepository.saveOrUpdate(user);

        // then
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * This will test if the deleteById method was invoked an expected number of times.
     */
    @Test
    public void delete() {
        // when
        int idToDelete = 1;
        userServiceRepository.delete(idToDelete);
        // then
        verify(userRepository, times(1)).deleteById(idToDelete);
    }

    /**
     * This will test if the findByUsername method was invoked an expected number of times.
     */
    @Test
    public void findByUserName() {
        // when
        userServiceRepository.findByUserName(USER_NAME);
        // then
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }
}