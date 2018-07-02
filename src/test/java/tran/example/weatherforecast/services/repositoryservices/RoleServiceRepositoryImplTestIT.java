package tran.example.weatherforecast.services.repositoryservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.RoleRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class will load in (bootstrap) data and check if the role data was properly loaded into the
 * database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceRepositoryImplTestIT {

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
        String oldRoleName = testRole.getRole();
        String newRole = "NewRole";
        testRole.setRole(newRole);

        // when
        Role savedRole = roleServiceRepository.saveOrUpdate(testRole);

        // then
        assertNotEquals(oldRoleName, savedRole.getRole());
        assertEquals(newRole, savedRole.getRole());
    }

    /**
     * This test is expected to throw an exception because an invalid id will be used.
     */
    @Test(expected = NotFoundException.class)
    public void getByIdFailure() {
        int invalidId = -1;
        roleServiceRepository.getById(invalidId);
    }

    /**
     * This test checks if there are two roles inserted into the database.
     */
    @Test
    public void listAll() {
        List<Role> roles = roleServiceRepository.listAll();
        int expectedNumberOfRoles = 1;
        assertEquals(expectedNumberOfRoles, roles.size());
    }
}