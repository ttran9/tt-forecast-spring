package tran.example.weatherforecast.repositories;

import org.springframework.data.repository.CrudRepository;
import tran.example.weatherforecast.domain.security.Role;

/**
 * An interface which will allow Spring Data JPA to create CRUD method implementations at runtime
 * for the Role object(s).
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
}
