package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * This class consists of simple unit tests to verify if a user can go to a mapping and get back an
 * expected view name.
 */
public class IndexControllerTest {

    private IndexController indexController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        indexController = new IndexController();
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @Test
    public void getIndexPage() throws Exception {
        mockMvc.perform(get(IndexController.URL_PATH_SEPARATOR))
            .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
            .andExpect(status().isOk());
    }

    @Test
    public void getAccessDeniedPage() throws Exception {
        mockMvc.perform(get(IndexController.DENIED_PAGE_MAPPING))
                .andExpect(view().name(IndexController.DENIED_VIEW_NAME))
                .andExpect(status().isOk());
    }

    @Test
    public void getLoginForm() throws Exception {
        mockMvc.perform(get(IndexController.LOGIN_PAGE_MAPPING))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexController.LOGIN_VIEW_NAME));
    }
}