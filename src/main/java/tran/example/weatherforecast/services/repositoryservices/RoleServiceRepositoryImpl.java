package tran.example.weatherforecast.services.repositoryservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.services.RoleService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A service implementing CRUD operations on the Role entity and table.
 */
@Slf4j
@Service
public class RoleServiceRepositoryImpl implements RoleService {

    /**
     * Data member used to perform operations on the Role table.
     */
    private RoleRepository roleRepository;

    /**
     * Injects a RoleRepository into the service.
     * @param roleRepository The repository object required to perform CRUD operation(s) on Roles.
     */
    @Autowired
    public RoleServiceRepositoryImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Returns a list of all the roles stored.
     * @return A list containing roles.
     */
    @Override
    public List<Role> listAll() {
        log.debug("Retrieving the roles from the database!");
        List<Role> roles = new LinkedList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    /**
     * Returns a role object with the associated id.
     * @param id The unique identifier (id) of a role object.
     * @return A role object with the specified id.
     */
    @Override
    public Role getById(Integer id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isPresent()) {
            log.debug("Retrieving a role by ID");
            return roleOptional.get();
        }
        log.debug("Role cannot be found with provided ID");
        throw new NotFoundException("role can't be found!");
    }

    /**
     * Saves the specified role object into the database or updates the existing role object if
     * already present.
     * @param domainObject The role object to be persisted.
     * @return The role object after being persisted to the database.
     */
    @Override
    public Role saveOrUpdate(Role domainObject) {
        log.debug("Saving or updating a role!");
        return roleRepository.save(domainObject);
    }

    /**
     * Removes a role object from the database with the specified id.
     * @param id The id of the role to be removed.
     */
    @Override
    public void delete(Integer id) {
        log.debug("Removing a role!");
        roleRepository.deleteById(id);
    }
}
