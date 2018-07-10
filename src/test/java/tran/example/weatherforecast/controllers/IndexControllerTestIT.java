package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.repositoryservices.UserServiceRepositoryImpl;
import tran.example.weatherforecast.services.security.UserAuthenticationServiceImpl;

import java.util.Collection;
import java.util.LinkedList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexControllerTestIT {
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;
    /**
     * Object used to verify if the user is logged in.
     */
    @Autowired
    private UserService userService;
    /**
     * Allows the UserService to interface with the Users table .
     */
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceRepositoryImpl(userRepository);
        IndexController indexController = new IndexController(new UserAuthenticationServiceImpl(userService));
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
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
                .andExpect(status().is3xxRedirection());
    }
}
