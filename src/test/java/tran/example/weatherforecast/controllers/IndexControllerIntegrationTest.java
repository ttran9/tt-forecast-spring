package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;

import java.util.Collection;
import java.util.LinkedList;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexControllerIntegrationTest {
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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * Tests when the user is already logged in and attempting to view the signin page.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getLoginFormWhileLoggedIn() throws Exception {
        Collection<GrantedAuthority> authorities = new LinkedList<>();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
                (SpringJPABootstrap.MWESTON, SpringJPABootstrap.PASSWORD, authorities));
        mockMvc.perform(get(IndexController.URL_PATH_SEPARATOR +
                IndexController.LOGIN_PAGE_MAPPING))
                .andExpect(forwardedUrl(IndexController.DENIED_PAGE_MAPPING))
                .andExpect(status().is4xxClientError());
    }
}
