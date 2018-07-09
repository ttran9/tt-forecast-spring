package tran.example.weatherforecast.services.security;

/**
 * Declares method(s) to check if a user is logged in based a request token from the controller.
 */
public interface UserAuthenticationService {
    /**
     * Uses the SpringSecurityContext to determine if there is authentication information present
     * and returns a boolean value.
     * @return True if the user is logged in.
     */
    boolean checkIfUserIsLoggedIn();
}
