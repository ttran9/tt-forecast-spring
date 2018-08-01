package tran.example.weatherforecast.services.registrationservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.services.security.EncryptionService;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("default")
public class RegistrationServiceImplIntegrationTest {
    /**
     * Used to save the user to the CustomUser table.
     */
    @Autowired
    private CustomUserRepository userRepository;
    /**
     * Used to get the role object with the role "CustomUser"
     */
    @Autowired
    private RoleRepository roleRepository;
    /**
     * Used to assign the user name and password from the registration form into a CustomUser object's
     * username and password fields.
     */
    @Autowired
    private Converter<RegistrationFormCommand, CustomUser> converter;
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
     * a role object which has the role "CustomUser" as well as having an encrypted password and the
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
        CustomUser registeredUser = registrationService.registerUser(registrationFormCommand);

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