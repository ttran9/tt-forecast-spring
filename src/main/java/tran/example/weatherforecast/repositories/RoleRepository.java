package tran.example.weatherforecast.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.example.weatherforecast.domain.security.Role;

/**
 * An interface which will allow Spring Data JPA to create CRUD method implementations at runtime
 * for the Role object(s).
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * This method will allow for an implementation at run-time that allows for the retrieval of
     * a stored role by specifying in the the role.
     */
    Role findRoleByRole(String role);
}
