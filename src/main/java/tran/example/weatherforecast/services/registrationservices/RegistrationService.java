package tran.example.weatherforecast.services.registrationservices;

import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;

/**
 * Declares a method to process user registration (create a user).
 */
public interface RegistrationService {

    /**
     * Verifies that the user name is not taken and if not it takes in the userName and password
     * from the form and saves it into the CustomUser object and then the password is encrypted
     * and the user is saved into the database.
     * @param registrationFormCommand The object holding the user name and password.
     * @return Returns a user object with a username and an encrypted password, or null if the
     * user does not exist.
     */
    CustomUser registerUser(RegistrationFormCommand registrationFormCommand);
}
