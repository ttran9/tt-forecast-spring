package tran.example.weatherforecast.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.services.UserService;

/**
 * A service used to get information from the database that can be used to assist with user
 * authentication.
 */
@Service("userDetailsService")
public class SpringSecurityUserDetailsServiceImpl implements UserDetailsService {

    /**
     * The service to grab persisted/stored user information.
     */
    private UserService userService;
    /**
     * A converter to transfer information to be used to assist with authentication.
     */
    private Converter<User, UserDetails> userToUserDetailsConverter;

    /**
     * Initializes the service to be used to grab information about the stored user(s).
     * @param userService The service used to grab user information from the database.
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Initializes the converter to be used to transfer information from the database to an
     * object holding user details.
     * @param converter The converter to transfer information from the database to an object
     *                  holding necessary user information.
     */
    @Autowired
    @Qualifier("userToUserDetails")
    public void setUserToUserDetailsConverter(Converter<User, UserDetails> converter) {
        this.userToUserDetailsConverter = converter;
    }

    /**
     * Gets an object holding user information given a specified user name.
     * @param userName The name of the user.
     * @return An object holding the details with a specified user name.
     * @throws UsernameNotFoundException If the user name cannot be found.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) {
        if(userService.findByUserName(userName) == null) {
            throw new UsernameNotFoundException("user cannot be loaded!");
        }
        return userToUserDetailsConverter.convert(userService.findByUserName(userName));
    }
}
