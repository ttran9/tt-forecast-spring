package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.repositories.RoleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * The tests below will test for the expected behavior when using the role repository service.
 */
public class RoleServiceRepositoryImplTest {

    /**
     * An object to allowing interfacing with the data layer and the Role table.
     */
    @Mock
    private RoleRepository roleRepository;
    /**
     * A service providing access to CRUD methods on the Role table.
     */
    private RoleServiceRepositoryImpl roleServiceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        roleServiceRepository = new RoleServiceRepositoryImpl(roleRepository);
    }

    /**
     * This test is expected to be able to invoke a findAll method and to get back a list with
     * the expected number of roles as well as ensure that the findAll method was invoked an
     * expected number of times.
     */
    @Test
    public void listAll() {
        Role role = new Role();

        // given
        List<Role> roles = new LinkedList<>();
        roles.add(role);
        when(roleRepository.findAll()).thenReturn(roles);
//        when(roleServiceRepository.listAll()).thenReturn(roles);

        // when
        List<Role> expectedRoles = roleServiceRepository.listAll();

        // then
        assertEquals(roles.size(), expectedRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    /**
     * This test checks if the role can be retrieved by passing in an ID and the findById method
     * will be checked to see if it was invoked an expected number of times.
     */
    @Test
    public void getById() {
        // given
        Role role = new Role();
        role.setRole(SpringJPABootstrap.USER);
        Optional<Role> roleOptional = Optional.of(role);
        when(roleRepository.findById(anyLong())).thenReturn(roleOptional);
        // when
        Role returnedRole = roleServiceRepository.getById(anyLong());

        // then
        assertNotNull(returnedRole);
        verify(roleRepository, times(1)).findById(anyLong());
    }

    /**
     * This test will check if the save method properly persists a user, returning a non null
     * user after the save operation.
     * The save method will also be checked to see if it was invoked a certain number of times.
     */
    @Test
    public void saveRole() {
        // given
        Role role = new Role();
        role.setRole(SpringJPABootstrap.USER);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        // when
        Role savedRole = roleServiceRepository.update(role);

        // then
        assertNotNull(savedRole);
        assertEquals(SpringJPABootstrap.USER, savedRole.getRole());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    /**
     * This test expects for the delete method to invoke the deleteById method a certain number
     * of times.
     */
    @Test
    public void delete() {
        // when
        Long idToDelete = 1L;
        roleServiceRepository.delete(idToDelete);
        // then
        verify(roleRepository, times(1)).deleteById(anyLong());
    }




}