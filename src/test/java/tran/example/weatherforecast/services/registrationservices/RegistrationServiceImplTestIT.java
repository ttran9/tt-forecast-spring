package tran.example.weatherforecast.services.registrationservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.security.EncryptionService;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationServiceImplTestIT {
    /**
     * Used to save the user to the User table.
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * Used to get the role object with the role "User"
     */
    @Autowired
    private RoleRepository roleRepository;
    /**
     * Used to assign the user name and password from the registration form into a User object's
     * username and password fields.
     */
    @Autowired
    private Converter<RegistrationFormCommand, User> converter;
    /**
     * Used to encrypt the password.
     */
    @Autowired
    private EncryptionService encryptionService;
    /**
     * Allows access to invoke the method to register a user.
     */
    private RegistrationService registrationService;

    @Before
    public void setUp()  {
        registrationService = new RegistrationServiceImpl(userRepository, roleRepository,
                converter, encryptionService);
    }

    /**
     * This will test a successful user registration, the user is expected to have the default
     * a role object which has the role "User" as well as having an encrypted password and the
     * username specified.
     */
    @Test
    public void registerUser() {
        // given
        String newUser = SpringJPABootstrap.MWESTON + "1";

        RegistrationFormCommand registrationFormCommand = new RegistrationFormCommand();
        registrationFormCommand.setUserName(SpringJPABootstrap.MWESTON + "1");
        registrationFormCommand.setPassword(SpringJPABootstrap.PASSWORD);

        // when
        User registeredUser = registrationService.registerUser(registrationFormCommand);

        assertTrue(encryptionService.checkPassword(SpringJPABootstrap.PASSWORD, registeredUser
                .getEncryptedPassword()));
        // then
        assertEquals(newUser, registeredUser.getUsername());
        assertTrue(encryptionService.checkPassword(SpringJPABootstrap.PASSWORD, registeredUser
                .getEncryptedPassword()));

        Optional<Role> roleOptional = registeredUser.getRoles().stream()
                                        .filter(role1 -> role1.getRole().equals(SpringJPABootstrap.USER))
                                        .findFirst();
        assertNotNull(roleOptional);
        Role assignedRole = roleOptional.get();
        assertEquals(SpringJPABootstrap.USER, assignedRole.getRole());

    }
}