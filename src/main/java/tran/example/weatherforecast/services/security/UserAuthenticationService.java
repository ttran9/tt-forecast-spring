package tran.example.weatherforecast.services.security;

import tran.example.weatherforecast.domain.User;

/**
 * Declares method(s) to check if a user is logged in based a request token from the controller.
 */
public interface UserAuthenticationService {
    /**
     * Uses the SpringSecurityContext to determine if there is authentication information present
     * and returns a user object if the user is logged in and null if not logged in.
     * @return Returns a user object if the user is logged in, otherwise null.
     */
    User checkIfUserIsLoggedIn();
}
