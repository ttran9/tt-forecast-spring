package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.RoleRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class will load in (bootstrap) data and check if the role data was properly loaded into the
 * database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceRepositoryImplIntegrationTest {

    /**
     * An object to allowing interfacing with the data layer and the Role table.
     */
    @Autowired
    private RoleRepository roleRepository;
    /**
     * A service providing access to CRUD methods on the Role table.
     */
    private RoleServiceRepositoryImpl roleServiceRepository;


    @Before
    public void setUp() {
        roleServiceRepository = new RoleServiceRepositoryImpl(roleRepository);
    }

    /**
     * This test attempt to update the name associated with a role and check to see if update
     * properly changes the role.
     */
    @Test
    public void testRoleNameUpdate() {
        // given
        Iterable<Role> roles = roleRepository.findAll();
        Role testRole = roles.iterator().next(); // grab the first role.
        String newRole = SpringJPABootstrap.USER;
        testRole.setRole(newRole);

        // when
        Role savedRole = roleServiceRepository.update(testRole);

        // then
        assertEquals(newRole, savedRole.getRole());

    }

    /**
     * This will attempt to save/create a role that is already there (the role of "User") so it
     * is expected that this will not be saved/created again and return null.
     */
    @Test
    public void saveAlreadyExistingRole() {
        // given
        Role role = new Role();
        role.setRole(SpringJPABootstrap.USER);

        // when
        Role nullRole = roleServiceRepository.save(role);

        // then
        assertNull(nullRole);
    }

    /**
     * This test is expected to throw an exception because an invalid id will be used.
     */
    @Test(expected = NotFoundException.class)
    public void getByIdFailure() {
        Long invalidId = -1L;
        roleServiceRepository.getById(invalidId);
    }

    /**
     * This test verifies if there is a certain number of roles (at this time just a single role)
     * has been inserted into the database.
     */
    @Test
    public void listAll() {
        List<Role> roles = roleServiceRepository.listAll();
        long expectedNumberOfRoles = roleRepository.count();
        assertEquals(expectedNumberOfRoles, roles.size());
    }
}