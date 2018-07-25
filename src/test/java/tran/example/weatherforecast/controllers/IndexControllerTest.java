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


}