package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.repositories.CustomUserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The tests below will test for expected behavior when using the user repository service.
 */
public class CustomUserServiceRepositoryImplTest {

    /**
     * An example user name for a sample test user.
     */
    private static final String USER_NAME = "username";
    /**
     * A service providing access to CRUD methods on the CustomUser table.
     */
    private UserServiceRepositoryImpl userServiceRepository;
    /**
     * An object to allowing interfacing with the data layer and the CustomUser table.
     */
    @Mock
    private CustomUserRepository userRepository;

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
        CustomUser user = new CustomUser();
        List<CustomUser> users = new LinkedList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<CustomUser> expectedUsers = userServiceRepository.listAll();

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
        CustomUser user = new CustomUser();
        Optional<CustomUser> optionalUser = Optional.of(user);
        when(userRepository.findById(anyLong())).thenReturn(optionalUser);

        // when
        CustomUser obtainedUser = userServiceRepository.getById(anyLong());

        // then
        assertNotNull(obtainedUser);
        verify(userRepository, times(1)).findById(anyLong());
    }

    /**
     * This tests if the persisted user has the same user name as the given user and tests if the
     * save method was invoked an expected number of times.
     */
    @Test
    public void saveUser() {
        // given
        CustomUser user = new CustomUser();
        user.setUsername(USER_NAME);
        when(userRepository.save(any(CustomUser.class))).thenReturn(user);

        // when
        CustomUser savedUser = userServiceRepository.save(user);

        // then
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).save(any(CustomUser.class));
    }

    /**
     * This will test if the deleteById method was invoked an expected number of times.
     */
    @Test
    public void delete() {
        // when
        long idToDelete = 1;
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

    @Test
    public void isUserNameTaken() {
        // when
        userServiceRepository.isUserNameTaken(USER_NAME);
        // then
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }
}