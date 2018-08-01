package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.registrationservices.RegistrationService;
import tran.example.weatherforecast.services.registrationservices.RegistrationServiceImpl;
import tran.example.weatherforecast.services.security.EncryptionService;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
    /**
     * Object used to access the CustomUser table.
     */
    @Mock
    private UserService userService;
    /**
     * Used to bind the entered user parameter inside of post calls to the userName input field.
     */
    private final String userNameField = "userName";
    /**
     * Used to bind the entered verify password inside of post calls to the password input field.
     */
    private final String verifyPasswordField = "verifyPassword";
    /**
     * A sample password (not secure).
     */
    private final String samplePasswordValue = "anewpassword";
    /**
     * The view name to be returned when the user has made an error on the registration page
     */
    private final String expectedErrorViewName = RegistrationController.REGISTRATION_DIRECTORY +
            RegistrationController.REGISTRATION_PAGE_NAME;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RegistrationService registrationService = new RegistrationServiceImpl(userRepository,
                roleRepository, converter, encryptionService);
        RegistrationController registerController = new RegistrationController
                (registrationService, validator, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .build();
    }
    /**
     * Tests if the user can make the request to the registration page and if there is a title
     * attribute.
     * @throws Exception Throws an exception if the GET request cannot be made to the
     * registration page.
     */
    @Test
    public void getRegistrationPage() throws Exception {
        mockMvc.perform(get(RegistrationController.BASE_URL))
                .andExpect(view().name(RegistrationController.REGISTRATION_DIRECTORY
                        + RegistrationController.REGISTRATION_PAGE_NAME))
                .andExpect(model().attribute(IndexController.PAGE_ATTRIBUTE,
                        RegistrationController.REGISTRATION_VIEW_TITLE))
                .andExpect(status().isOk());
    }

    /**
     * This will attempt to create a user with a missing field (no password).
     * @throws Exception Throws an exception if there is an error making a POST request to
     * process the registration.
     */
    @Test
    public void processRegistrationWithMissingField() throws Exception {
        String newUserName = "greatusername";
        mockMvc.perform(post(RegistrationController.BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(userNameField, newUserName)
                .param(verifyPasswordField, samplePasswordValue))
                .andExpect(view().name(expectedErrorViewName))
                .andExpect(status().isOk());
    }
}