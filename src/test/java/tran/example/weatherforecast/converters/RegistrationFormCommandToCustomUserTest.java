package tran.example.weatherforecast.converters;

import org.junit.Before;
import org.junit.Test;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;

import static org.junit.Assert.*;

/**
 * These tests will verify if information can be converted from a form to a domain (CustomUser) object.
 */
public class RegistrationFormCommandToCustomUserTest {

    private RegistrationFormCommandToCustomUser converter;

    @Before
    public void setUp() {
        converter = new RegistrationFormCommandToCustomUser();
    }

    /**
     * Tests the case where a null object is converted.
     * A null object is expected to be returned in this case.
     */
    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    /**
     * Tests the case where the object being converted is not null so the returned object is not
     * null.
     */
    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new RegistrationFormCommand()));
    }

    /**
     * Checks if the contents from a form can be transferred to a user object.
     */
    @Test
    public void convert() {
        // given
        String userName = "user";
        String password = "password";
        RegistrationFormCommand registrationForm = new RegistrationFormCommand();
        registrationForm.setUserName(userName);
        registrationForm.setPassword(password);

        // when
        CustomUser user = converter.convert(registrationForm);

        // then.
        assertNotNull(user);
        assertEquals(userName, user.getUsername());
        assertEquals(password, user.getPassword());
    }
}