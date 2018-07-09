package tran.example.weatherforecast.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 * This service uses an authentication token to determine if the user has been logged in and
 * authenticated.
 */
@Service
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * The security context is analyzed (if present) to determine if the user has been
     * authenticated. If there is no security context the user isn't authenticated.
     * @return True if the user has been authenticated, false otherwise.
     */
    @Override
    public boolean checkIfUserIsLoggedIn() {
        log.debug("check if user is logged in");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if(authentication != null) {
                if(authentication.getName() != null) {
                    return !authentication.getName().equals(ANONYMOUS_USER);
                }
            }
        }
        return false;
    }
}
