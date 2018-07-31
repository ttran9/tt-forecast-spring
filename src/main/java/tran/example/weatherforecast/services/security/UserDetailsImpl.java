package tran.example.weatherforecast.services.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * An object holding information about a user which can be used to assist with authentication.
 */
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private Collection<SimpleGrantedAuthority> authorities;
    private String username;
    private String password;
    private Boolean enabled;

    /**
     * @return A list holding the roles associated with a user.
     */
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * @return The user name for a certain user.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @return An encrypted password for a user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * unused in this application, used to verify if an account has been expired or not.
     * @return True if the account is not expired, otherwise false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * unused in this application, used to verify if an account is locked or not.
     * @return True if the account is not locked, otherwise false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * unused in this application, used to verify if the credentials on an account has been
     * expired or not.
     * @return True if the credentials have not expired, otherwise false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return True if the account is currently enabled, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
