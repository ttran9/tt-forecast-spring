package tran.example.weatherforecast.converters;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.services.security.EncryptionServiceImpl;
import tran.example.weatherforecast.services.security.UserDetailsImpl;

import static org.junit.Assert.*;

/**
 * This class holds a series of tests to check if the user object can be converted.
 */
public class UserToUserDetailsTest {
    /**
     * A converter to convert from a User domain object to a UserDetails object to ensure that
     * conversion can be done without error.
     */
    private UserToUserDetails userToUserDetailsConverter;
    /**
     * A service used to assist with encrypting plaintext passwords for comparison to stored
     * encrypted passwords.
     */
    private EncryptionServiceImpl encryptionService;

    @Before
    public void setUp() {
        userToUserDetailsConverter = new UserToUserDetails();
        encryptionService = new EncryptionServiceImpl();
        encryptionService.setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Tests the conversion of a null User object.
     */
    @Test
    public void testConvertNullObject() {
        UserDetailsImpl userDetails = (UserDetailsImpl) userToUserDetailsConverter.convert(null);
        assertNull(userDetails);
    }

    /**
     * Tests the conversion of a User object without any fields (but not null).
     */
    @Test
    public void testEmptyObjectConversion() {
        User user = new User();
        UserDetailsImpl userDetails = (UserDetailsImpl) userToUserDetailsConverter.convert(user);
        assertNotNull(userDetails);
    }

    /**
     * Tests if a User objects with fields can be converted.
     */
    @Test
    public void convert() {
        // given
        String password = "password";
        String username = "username";
        String roleName = "User";
        int expectedSize = 1;
        Role role = new Role();
        role.setRole(roleName);
        User user = new User();
        user.setPassword(password);
        user.setEncryptedPassword(encryptionService.encryptString(password));
        user.setUsername(username);
        user.addRole(role);

        // when
        UserDetailsImpl userDetails = (UserDetailsImpl) userToUserDetailsConverter.convert(user);
        // then
        assertNotNull(userDetails); // conversion test.
        // validity below
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEncryptedPassword(), userDetails.getPassword());
        assertEquals(expectedSize, userDetails.getAuthorities().size());
    }


}