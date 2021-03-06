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
    public Role getById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isPresent()) {
            log.debug("Retrieving a role by ID");
            return roleOptional.get();
        }
        log.debug("Role cannot be found with provided ID");
        throw new NotFoundException("role can't be found!");
    }

    /**
     * Saves the role if not in the database.
     * @param domainObject The role to be saved.
     * @return Returns a role after being saved, null if it already exists.
     */
    @Override
    public Role save(Role domainObject) {
        Role role = roleRepository.findRoleByRole(domainObject.getRole());
        if(role == null) {
            log.debug("Saving and creating a new role!");
            return roleRepository.save(domainObject);
        } else {
            log.debug("The role already exists and will not be created again!");
            return null;
        }
    }

    /**
     * Updates the role.
     * @param domainObject The role to be updated.
     * @return Return a role after being updated.
     */
    @Override
    public Role update(Role domainObject) {
        return roleRepository.save(domainObject);
    }

    /**
     * Removes a role object from the database with the specified id.
     * @param id The id of the role to be removed.
     */
    @Override
    public void delete(Long id) {
        log.debug("Removing a role!");
        roleRepository.deleteById(id);
    }
}
