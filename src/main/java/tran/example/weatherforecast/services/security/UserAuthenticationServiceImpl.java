package tran.example.weatherforecast.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.services.UserService;


/**
 * This service uses an authentication token to determine if the user has been logged in and
 * authenticated.
 */
@Service
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    /**
     * The name of a user that is not currently logged in.
     */
    private static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * A service used to get the user by username.
     */
    private UserService userService;

    /**
     * Wires up the userService and injects it into this service to find a user by user name.
     * @param userService An object to be injected into this service.
     */
    @Autowired
    public UserAuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * The security context is analyzed (if present) to determine if the user has been
     * authenticated. If there is no security context the user isn't authenticated.
     * @return True if the user has been authenticated, false otherwise.
     */
    @Override
    public CustomUser checkIfUserIsLoggedIn() {
        log.debug("check if user is logged in");
        SecurityContext securityContext = SecurityContextHolder.getContext(); // never null.
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null) {
            /*
             * the userName if the user is not logged in will not be null it will have the
             * value 'anonymousUser'.
             */
            String userName = authentication.getName();
            if (userName.equals(ANONYMOUS_USER)) {
                return null;
            } else {
                return userService.findByUserName(userName);
            }
        }
        return null;
    }
}
