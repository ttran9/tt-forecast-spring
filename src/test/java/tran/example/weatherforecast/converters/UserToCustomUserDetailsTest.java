package tran.example.weatherforecast.converters;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.services.security.EncryptionServiceImpl;
import tran.example.weatherforecast.services.security.UserDetailsImpl;

import static org.junit.Assert.*;

/**
 * This class holds a series of tests to check if the user object can be converted.
 */
public class UserToCustomUserDetailsTest {
    /**
     * A converter to convert from a CustomUser domain object to a UserDetails object to ensure that
     * conversion can be done without error.
     */
    private CustomUserToUserDetails customUserToUserDetailsConverter;
    /**
     * A service used to assist with encrypting plaintext passwords for comparison to stored
     * encrypted passwords.
     */
    private EncryptionServiceImpl encryptionService;

    @Before
    public void setUp() {
        customUserToUserDetailsConverter = new CustomUserToUserDetails();
        encryptionService = new EncryptionServiceImpl();
        encryptionService.setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Tests the conversion of a null CustomUser object.
     */
    @Test
    public void testConvertNullObject() {
        UserDetailsImpl userDetails = (UserDetailsImpl) customUserToUserDetailsConverter.convert(null);
        assertNull(userDetails);
    }

    /**
     * Tests the conversion of a CustomUser object without any fields (but not null).
     */
    @Test
    public void testEmptyObjectConversion() {
        CustomUser user = new CustomUser();
        UserDetailsImpl userDetails = (UserDetailsImpl) customUserToUserDetailsConverter.convert(user);
        assertNotNull(userDetails);
    }

    /**
     * Tests if a CustomUser objects with fields can be converted.
     */
    @Test
    public void convert() {
        // given
        String password = "password";
        String username = "username";
        String roleName = "CustomUser";
        int expectedSize = 1;
        Role role = new Role();
        role.setRole(roleName);
        CustomUser user = new CustomUser();
        user.setPassword(password);
        user.setEncryptedPassword(encryptionService.encryptString(password));
        user.setUsername(username);
        user.addRole(role);

        // when
        UserDetailsImpl userDetails = (UserDetailsImpl) customUserToUserDetailsConverter.convert(user);
        // then
        assertNotNull(userDetails); // conversion test.
        // validity below
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEncryptedPassword(), userDetails.getPassword());
        assertEquals(expectedSize, userDetails.getAuthorities().size());
        assertTrue(userDetails.getEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isEnabled());
    }

    /**
     * Tests if a CustomUser objects with some fields can be converted and attempting to add the
     * same role will cause any issues.
     * It is expected that adding the same role object more than once will have no undesired side
     * effect).
     */
    @Test
    public void convertWithMultipleIdenticalRoleInsertions() {
        // given
        String password = "diffpw";
        String username = "customusername";
        String roleName = "User";
        int invalidSize = 2;
        int expectedSize = 1;
        Role role = new Role();
        role.setRole(roleName);
        CustomUser user = new CustomUser();
        user.setPassword(password);
        user.setEncryptedPassword(encryptionService.encryptString(password));
        user.setUsername(username);
        user.addRole(role);
        user.addRole(role);

        // when
        UserDetailsImpl userDetails = (UserDetailsImpl) customUserToUserDetailsConverter.convert(user);
        // then
        assertNotNull(userDetails); // conversion test.
        // validity below
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEncryptedPassword(), userDetails.getPassword());
        // instead of checking for an expected size ensure there is only one role.
        assertNotEquals(invalidSize, userDetails.getAuthorities().size());
        // now that we know there is not an un-expected size verify it has the expected size.
        assertEquals(expectedSize, userDetails.getAuthorities().size());
        assertTrue(userDetails.getEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isEnabled());
    }


}