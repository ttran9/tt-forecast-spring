package tran.example.weatherforecast.services.registrationservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.security.EncryptionService;

/**
 * Defines a method to save (register) a user into the database.
 */
@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    /**
     * Used to save the user to the User table.
     */
    private final UserRepository userRepository;
    /**
     * Used to get the role object with the role "User"
     */
    private final RoleRepository roleRepository;
    /**
     * Used to assign the user name and password from the registration form into a User object's
     * username and password fields.
     */
    private final Converter<RegistrationFormCommand, User> converter;
    /**
     * Used to encrypt the password.
     */
    private final EncryptionService encryptionService;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                   @Qualifier("registrationFormCommandToUser")
                                           Converter<RegistrationFormCommand, User> converter,
                                   EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.converter = converter;
        this.encryptionService = encryptionService;
    }

    /**
     * Takes in the user name and password and converts the RecipeFormCommand object to a User
     * and then encrypts the User and finally saves it into the database.
     * @param registrationFormCommand The object holding the user name and password.
     * @return Returns a user with the user name and encrypted password fields assigned.
     */
    @Override
    public User registerUser(RegistrationFormCommand registrationFormCommand) {
        log.debug("attempting to register/create the user!");
        /*
         * The controller validates the form contents so while testing this method it will be
         * assumed there are no errors with the RegistrationFormObject.
          */
        User user = converter.convert(registrationFormCommand);
        // encrypt the password.
        user.setEncryptedPassword(encryptionService.encryptString(user.getPassword()));
        Role userRole = roleRepository.findRoleByRole(SpringJPABootstrap.USER);
        // add the user role.
        user.addRole(userRole);
        return userRepository.save(user);
    }
}
