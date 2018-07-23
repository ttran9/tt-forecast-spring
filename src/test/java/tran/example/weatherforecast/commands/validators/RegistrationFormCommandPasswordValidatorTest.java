package tran.example.weatherforecast.commands.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;

import static org.junit.Assert.*;

/**
 * Tests cases of input into the registration form such as a valid case and invalid case (where
 * the passwords don't match).
 * Note: I found the below Stack Overflow thread to be helpful:
 * https://stackoverflow.com/questions/9744988/writing-junit-tests-for-spring-validator-implementation
 */
public class RegistrationFormCommandPasswordValidatorTest {
    /**
     * The custom domain object that holds in the user name, password, and verify password fields.
     */
    private RegistrationFormCommand registrationFormCommand;

    /**
     * The custom validator which will validate if the password fields are filled out and if they
     * match.
     */
    private RegistrationFormCommandPasswordValidator validator;

    /**
     * An object which can store error(s) when a user is filling out a form.
     */
    private Errors errors;

    @Before
    public void setUp() {
        registrationFormCommand = new RegistrationFormCommand();
        validator = new RegistrationFormCommandPasswordValidator();
        errors = new BeanPropertyBindingResult(registrationFormCommand, "registrationFormCommand");
    }

    /**
     * Tests the case where the entered form data is valid.
     */
    @Test
    public void validate() {

        registrationFormCommand.setUserName(SpringJPABootstrap.USER);
        registrationFormCommand.setPassword(SpringJPABootstrap.PASSWORD);
        registrationFormCommand.setVerifyPassword(SpringJPABootstrap.PASSWORD);

        validator.validate(registrationFormCommand, errors);

        // fields are all valid.
        assertFalse(errors.hasErrors());
    }

    /**
     * Tests when the password and verify password does not match.
     */
    @Test
    public void validateWithMismatchingPasswords() {
        RegistrationFormCommandPasswordValidator validator = new
                RegistrationFormCommandPasswordValidator();

        registrationFormCommand.setUserName(SpringJPABootstrap.USER);
        registrationFormCommand.setPassword(SpringJPABootstrap.PASSWORD);
        // verify password doesn't match the password.
        registrationFormCommand.setVerifyPassword(SpringJPABootstrap.PASSWORD + "1");

        validator.validate(registrationFormCommand, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("verifyPassword"));

    }

    /**
     * Tests when there is a missing (null) password.
     */
    @Test
    public void validateWithNullPassword() {

        registrationFormCommand.setUserName(SpringJPABootstrap.USER);
        // no password present (null)
        registrationFormCommand.setVerifyPassword(SpringJPABootstrap.PASSWORD + "1");

        validator.validate(registrationFormCommand, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("verifyPassword"));
    }

    /**
     * Tests if this custom validator can validate the RegistrationFormCommand object.
     */
    @Test
    public void supports() throws ClassNotFoundException {
        Class<?> rfcClass = Class.forName(RegistrationFormCommand.class.getName());
        assertTrue(validator.supports(rfcClass));

    }
}