package tran.example.weatherforecast.commands.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tran.example.weatherforecast.commands.RegistrationFormCommand;

/**
 * This class overrides a validator which will allow for the password fields on the registration
 * page to be checked if they are identical and will return an error if they do not match.
 */
@Component
public class RegistrationFormCommandPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationFormCommand.class.isAssignableFrom(aClass);
    }

    /**
     * Verifies if the "password" and the "password again" fields are identical from the
     * registration page.
     * @param o The binding object from the registration form which holds the fields entered into
     *          this form.
     * @param errors An object to store the errors as key/value pairs which will hold a
     *               description of the error.
     */
    @Override
    public void validate(Object o, Errors errors) {
        RegistrationFormCommand registrationFormCommand = (RegistrationFormCommand) o;
        if(registrationFormCommand.getPassword() == null || registrationFormCommand
                .getVerifyPassword() == null) {
            return ;
        }
        if(!registrationFormCommand.getPassword().equals(registrationFormCommand.getVerifyPassword
                ())) {
            errors.rejectValue("verifyPassword", "PasswordsDontMatch", "passwords don't " +
                    "match!");
        }
    }
}
