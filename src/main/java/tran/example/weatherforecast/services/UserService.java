package tran.example.weatherforecast.services;

import tran.example.weatherforecast.domain.CustomUser;

/**
 * Interface for declaring CRUD methods for CustomUser objects.
 */
public interface UserService extends CRUDService<CustomUser> {
    /**
     * Finds a user based on the specified user name.
     * @param userName The user name of a user.
     * @return A user object with the provided userName.
     */
    CustomUser findByUserName(String userName);
}
