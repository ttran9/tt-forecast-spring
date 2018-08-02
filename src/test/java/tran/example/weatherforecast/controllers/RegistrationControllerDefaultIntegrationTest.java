package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tran.example.weatherforecast.repositories.CustomUserRepository;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"default"})
public class RegistrationControllerDefaultIntegrationTest {
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

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * This will successfully create a user so there should be one extra user.
     * The user is expected to be redirected to the index page.
     * @throws Exception Throws an exception if there is an error making a POST request to
     * process the registration.
     */
    @Test
    public void processRegistration() throws Exception {
        String newUserName = "goodusername";
        long numberOfExpectedUsers = userRepository.count() + 1;
        mockMvc.perform(post(RegistrationController.BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(userNameField, newUserName)
                .param(passwordField, samplePasswordValue)
                .param(verifyPasswordField, samplePasswordValue))
                .andExpect(view().name(SearchController.REDIRECT + IndexController.URL_PATH_SEPARATOR))
                .andExpect(status().is3xxRedirection());
        long updatedNumberOfUsers = userRepository.count();
        assertEquals(numberOfExpectedUsers, updatedNumberOfUsers);
    }
}
