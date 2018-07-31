package tran.example.weatherforecast.services.registrationservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.services.security.EncryptionService;

/**
 * Defines a method to save (register) a user into the database.
 */
@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    /**
     * Used to save the user to the CustomUser table.
     */
    private final CustomUserRepository userRepository;
    /**
     * Used to get the role object with the role "CustomUser"
     */
    private final RoleRepository roleRepository;
    /**
     * Used to assign the user name and password from the registration form into a CustomUser object's
     * username and password fields.
     */
    private final Converter<RegistrationFormCommand, CustomUser> converter;
    /**
     * Used to encrypt the password.
     */
    private final EncryptionService encryptionService;

    @Autowired
    public RegistrationServiceImpl(CustomUserRepository userRepository, RoleRepository roleRepository,
                                   @Qualifier("registrationFormCommandToCustomUser")
                                           Converter<RegistrationFormCommand, CustomUser> converter,
                                   EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.converter = converter;
        this.encryptionService = encryptionService;
    }

    /**
     * Takes in the user name and password and converts the RecipeFormCommand object to a CustomUser
     * and then encrypts the CustomUser and finally saves it into the database.
     * @param registrationFormCommand The object holding the user name and password.
     * @return Returns a user with the user name and encrypted password fields assigned.
     */
    @Override
    public CustomUser registerUser(RegistrationFormCommand registrationFormCommand) {
        log.debug("attempting to register/create the user!");
        /*
         * The controller validates the form contents so while testing this method it will be
         * assumed there are no errors with the RegistrationFormObject.
          */
        CustomUser user = converter.convert(registrationFormCommand);
        // encrypt the password.
        user.setEncryptedPassword(encryptionService.encryptString(user.getPassword()));
        Role userRole = roleRepository.findRoleByRole(SpringJPABootstrap.USER);
        // add the user role.
        user.addRole(userRole);
        return userRepository.save(user);
    }
}
