package tran.example.weatherforecast.converters;

import org.junit.Before;
import org.junit.Test;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.User;

import static org.junit.Assert.*;

public class RegistrationFormCommandToUserTest {

    private RegistrationFormCommandToUser converter;

    @Before
    public void setUp() {
        converter = new RegistrationFormCommandToUser();
    }

    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new RegistrationFormCommand()));
    }

    @Test
    public void convert() {
        // given
        String userName = "user";
        String password = "password";
        RegistrationFormCommand registrationForm = new RegistrationFormCommand();
        registrationForm.setUserName(userName);
        registrationForm.setPassword(password);

        // when
        User user = converter.convert(registrationForm);

        // then.
        assertNotNull(user);
        assertEquals(userName, user.getUsername());
        assertEquals(password, user.getPassword());
    }
}