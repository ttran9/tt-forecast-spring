package tran.example.weatherforecast.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.example.weatherforecast.domain.CustomUser;

/**
 * An interface which will allow Spring Data JPA to create CRUD method implementations at runtime
 * for the CustomUser object(s).
 */
@Repository
public interface CustomUserRepository extends CrudRepository<CustomUser, Long> {

    /**
     * The implementation of this method will utilize a UserService to query the database
     * with a specified user name to find a CustomUser object.
     * @param username The name of the user to be searched for.
     * @return A user with the specified user name.
     */
    CustomUser findByUsername(String username);
}
