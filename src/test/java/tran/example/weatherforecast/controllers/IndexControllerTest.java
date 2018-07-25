package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class consists of simple unit tests to verify if a user can go to a mapping and get back an
 * expected view name.
 */
public class IndexControllerTest {
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        IndexController indexController = new IndexController();
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    /**
     * Tests if the user can go to the index page with the http status code of 200 (ok).
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getIndexPage() throws Exception {
        mockMvc.perform(get(IndexController.URL_PATH_SEPARATOR))
            .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
            .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE, IndexController.INDEX_PAGE_TITLE))
            .andExpect(status().isOk());
    }

    /**
     * Tests if the user can view the denied page which is open to all users.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getAccessDeniedPage() throws Exception {
        mockMvc.perform(get(IndexController.DENIED_PAGE_MAPPING))
                .andExpect(view().name(IndexController.DENIED_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE, IndexController.ACCESS_DENIED_PAGE_TITLE))
                .andExpect(status().isOk());
    }

    /**
     * Tests if the user can view the login/signin page which is open to all users.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getLoginForm() throws Exception {
        mockMvc.perform(get(IndexController.URL_PATH_SEPARATOR +
                IndexController.LOGIN_PAGE_MAPPING))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexController.SIGNIN_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE, IndexController.LOGIN_PAGE_TITLE));
    }

    /**
     * Tests if the user can view the resource not found page which is open to all users.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getNotFoundPage() throws Exception {
        mockMvc.perform(get(IndexController.URL_PATH_SEPARATOR +
                IndexController.NOT_FOUND_MAPPING))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexController.NOT_FOUND_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE, IndexController.RESOURCE_NOT_FOUND_PAGE_TITLE));
    }

}