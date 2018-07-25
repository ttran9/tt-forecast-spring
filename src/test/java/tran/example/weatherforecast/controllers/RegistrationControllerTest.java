package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.services.registrationservices.RegistrationService;
import tran.example.weatherforecast.services.registrationservices.RegistrationServiceImpl;
import tran.example.weatherforecast.services.security.EncryptionService;

/**
 * This class will test if the register page can be shown to the user.
 */
public class RegistrationControllerTest {
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;
    /**
     * Used to save the user to the CustomUser table.
     */
    @Mock
    private CustomUserRepository userRepository;
    /**
     * Used to get the role object with the role "CustomUser"
     */
    @Mock
    private RoleRepository roleRepository;
    /**
     * Used to assign the user name and password from the registration form into a CustomUser object's
     * username and password fields.
     */
    @Mock
    private Converter<RegistrationFormCommand, CustomUser> converter;
    /**
     * Used to encrypt the password.
     */
    @Mock
    private EncryptionService encryptionService;
    /**
     * Custom validator used for password verification.
     */
    @Mock
    private Validator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RegistrationService registrationService = new RegistrationServiceImpl(userRepository,
                roleRepository, converter, encryptionService);
        RegistrationController registerController = new RegistrationController
                (registrationService, validator);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .build();
    }



}