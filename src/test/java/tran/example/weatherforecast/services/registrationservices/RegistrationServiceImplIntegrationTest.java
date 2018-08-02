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
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.security.EncryptionService;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
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
    /**
     * Used to verify if the user name is taken.
     */
    @Autowired
    private UserService userService;

    @Before
    public void setUp()  {
        registrationService = new RegistrationServiceImpl(userRepository, roleRepository,
                converter, encryptionService, userService);
    }

    /**
     * This will test where the user attempts to register when a user already exists so the user
     * is not created and the returned object is null.
     */
    @Test
    public void registerUser() {
        // given
        RegistrationFormCommand registrationFormCommand = new RegistrationFormCommand();
        registrationFormCommand.setUserName(SpringJPABootstrap.MWESTON);
        registrationFormCommand.setPassword(SpringJPABootstrap.PASSWORD + "123");

        // when
        CustomUser nullUser = registrationService.registerUser(registrationFormCommand);

        // then
        assertNull(nullUser);
    }
}
