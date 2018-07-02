package tran.example.weatherforecast.services;

import tran.example.weatherforecast.domain.User;

/**
 * Interface for declaring CRUD methods for User objects.
 */
public interface UserService extends CRUDService<User> {
    /**
     * Finds a user based on the specified user name.
     * @param userName The user name of a user.
     * @return A user object with the provided userName.
     */
    User findByUserName(String userName);
}
