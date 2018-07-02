package tran.example.weatherforecast.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.services.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to convert a User object to UserDetails.
 */
@Component
public class UserToUserDetails implements Converter<User, UserDetails> {
    /**
     * @param user The user object with fields to be converted.
     * @return An object with the details of a user or null if an empty user object..
     */
    @Override
    public UserDetails convert(User user) {
        if(user == null)
            return null;
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getEncryptedPassword());
        userDetails.setEnabled(user.getEnabled());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });

        userDetails.setAuthorities(authorities);

        return userDetails;
    }
}
