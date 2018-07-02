package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * A test for the forecast controller mappings which require a user to be authenticated and
 * properly authorized, this will be an integration test as I do not want to simply map if a user
 * can go to a mapping and just get a view name back. I am also trying to test if the
 * authentication and authorization has been configured properly by the SpringSecurityConfig
 * class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ForecastControllerTestIT {

    /**
     * a string representing a role that does not have the authorization to access certain URL
     * mappings.
     */
    private static final String INVALID_ROLE = "NoRole";
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;
    /**
     * Holds the configuration of the context for the below tests.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * simulates when a user is authenticated and has the proper role to view the page.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    @WithMockUser(authorities = SpringJPABootstrap.USER)
    public void getSearchesPage() throws Exception {
          mockMvc.perform(get(ForecastController.BASE_URL +
                IndexController.URL_PATH_SEPARATOR +
                ForecastController.USER_SEARCH_VIEW_NAME))
                .andExpect(view().name(ForecastController.BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR + ForecastController.USER_SEARCH_VIEW_NAME))
                .andExpect(status().isOk());
    }

    /**
     * simulates when the user is authenticated but not authorized to view the page.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    @WithMockUser(authorities = INVALID_ROLE)
    public void getSearchesPageInvalidUserRole() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                IndexController.URL_PATH_SEPARATOR +
                ForecastController.USER_SEARCH_VIEW_NAME))
                .andExpect(status().is4xxClientError());
    }

    /**
     * simulates when the user is not logged in and trying to view a page that requires
     * authentication.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    @WithAnonymousUser
    public void getSearchesPageUserNotLoggedIn() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                IndexController.URL_PATH_SEPARATOR +
                ForecastController.USER_SEARCH_VIEW_NAME))
                .andExpect(status().is3xxRedirection());
    }


}