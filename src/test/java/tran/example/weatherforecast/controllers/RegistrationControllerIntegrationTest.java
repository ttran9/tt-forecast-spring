package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.repositories.CustomUserRepository;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationControllerIntegrationTest {
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;
    /**
     * Holds the configuration of the context for the below tests.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
     * Used get the number of users in the database.
     */
    @Autowired
    private CustomUserRepository userRepository;
    /**
     * Used to bind the entered user parameter inside of post calls to the userName input field.
     */
    private final String userNameField = "userName";
    /**
     * Used to bind the entered password inside of post calls to the password input field.
     */
    private final String passwordField = "password";
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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
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
        mockMvc.perform(get(RegistrationController.BASE_URL))
                .andExpect(forwardedUrl(IndexController.DENIED_PAGE_MAPPING))
                .andExpect(status().is4xxClientError());
    }

    /**
     * This will attempt to create a user name that has already been pre-loaded (bootstrapped)
     * into the application.
     * The user is expected to be shown the registration page again.
     * @throws Exception Throws an exception if there is an error making a POST request to
     * process the registration.
     */
    @Test
    public void processRegistrationWithExistingUserName() throws Exception {
        long numberOfExpectedUsers = userRepository.count();
        mockMvc.perform(post(RegistrationController.BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(userNameField, SpringJPABootstrap.MWESTON)
                .param(passwordField, samplePasswordValue)
                .param(verifyPasswordField, samplePasswordValue))
                .andExpect(view().name(RegistrationController.REGISTRATION_DIRECTORY +
                        RegistrationController.REGISTRATION_PAGE_NAME))
                .andExpect(status().isOk());
        long updatedNumberOfUsers = userRepository.count();
        assertEquals(numberOfExpectedUsers, updatedNumberOfUsers);
    }

    /**
     * This will attempt to create a user with the password and verify password fields not
     * matching as well as the user name not being a sufficient length.
     * @throws Exception Throws an exception if there is an error making a POST request to
     * process the registration.
     */
    @Test
    public void processRegistrationWithMisMatchingPasswordsAndIncorrectUserNameLength() throws
            Exception {
        String newUserName = "greatusername";
        mockMvc.perform(post(RegistrationController.BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(userNameField, newUserName)
                .param(passwordField, samplePasswordValue + "1")
                .param(verifyPasswordField, samplePasswordValue))
                .andExpect(view().name(expectedErrorViewName))
                .andExpect(status().isOk());
    }
}