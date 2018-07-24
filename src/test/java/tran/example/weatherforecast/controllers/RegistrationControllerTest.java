package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.repositories.RoleRepository;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.services.registrationservices.RegistrationService;
import tran.example.weatherforecast.services.registrationservices.RegistrationServiceImpl;
import tran.example.weatherforecast.services.security.EncryptionService;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import java.util.Collection;
import java.util.LinkedList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
     * A service used to determine if the user is logged in.
     */
    @Mock
    private UserAuthenticationService userAuthenticationService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RegistrationService registrationService = new RegistrationServiceImpl(userRepository,
                roleRepository, converter, encryptionService);
        RegistrationController registerController = new RegistrationController
                (registrationService, validator, userAuthenticationService);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
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
                .andExpect(forwardedUrl(RegistrationController.REGISTRATION_DIRECTORY
                        + RegistrationController.REGISTRATION_PAGE_NAME))
                .andExpect(model().attribute(IndexController.PAGE_ATTRIBUTE,
                        RegistrationController.REGISTRATION_VIEW_TITLE))
                .andExpect(status().isOk());
    }

    /**
     * Tests when the user attempts to view the registration page while already logged in.
     * @throws Exception Throws an exception if the GET request to the registration page cannot
     * be made.
     */
    @Test
    public void getRegistrationPageWhileLoggedIn() throws Exception {
        // simulate a user being logged in.
        Collection<GrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("User"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
                (SpringJPABootstrap.MWESTON, SpringJPABootstrap.PASSWORD, authorities));
        // expected view name
        String expectedViewName = RegistrationController.REGISTRATION_DIRECTORY +
                RegistrationController.REGISTRATION_PAGE_NAME;
        mockMvc.perform(get(RegistrationController.BASE_URL))
                .andExpect(view().name(expectedViewName))
                .andExpect(model().attribute(IndexController.PAGE_ATTRIBUTE,
                        RegistrationController.REGISTRATION_VIEW_TITLE))
                .andExpect(status().isOk());
    }

}