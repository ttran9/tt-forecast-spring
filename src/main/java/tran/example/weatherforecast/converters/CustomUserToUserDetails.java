package tran.example.weatherforecast.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.services.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class to convert a CustomUser object to UserDetails.
 */
@Component
public class CustomUserToUserDetails implements Converter<CustomUser, UserDetails> {
    /**
     * @param customUser The user object with fields to be converted.
     * @return An object with the details of a user or null if an empty user object..
     */
    @Override
    public UserDetails convert(CustomUser customUser) {
        if(customUser == null)
            return null;
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(customUser.getUsername());
        userDetails.setPassword(customUser.getEncryptedPassword());
        userDetails.setEnabled(customUser.getEnabled());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        customUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });

        userDetails.setAuthorities(authorities);

        return userDetails;
    }
}
