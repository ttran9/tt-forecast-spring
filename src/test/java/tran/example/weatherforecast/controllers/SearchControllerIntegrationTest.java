package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.repositories.SearchRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * A series of tests to verify that if the user is properly authenticated then the user can view
 * paginated search results. When the user is logged in some cases tested would be if the user
 * enters in an invalid page parameter such as a negative valid and a value that is not a number.
 * Another series of tests will verify the case where a user is not logged in and attempts to
 * view prior searches (this should redirect a user).
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchControllerIntegrationTest {
    /**
     * Invalid page number as a string.
     */
    public static final String INVALID_PAGE_NUMBER = "-1";
    /**
     * Improperly formatted page number.
     */
    public static final String IMPROPER_FORMATTED_PAGE_NUMBER = "asdf";
    /**
     *
     */
    public static final String INVALID_ADDRESS = "1";
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
     * Used to return the number of searches currently stored.
     */
    @Autowired
    private SearchRepository searchRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * simulates when a user is authenticated and attempts to view their prior searches page.
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getSearchesPage() throws Exception {
        // simulate a user being logged in.
        logUserIn();
        mockMvc.perform(get(SearchController.BASE_URL +
                SearchController.PRIOR_USER_SEARCHES_MAPPING))
                .andExpect(view().name(SearchController.SEARCH_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR + SearchController.USER_SEARCH_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        SearchController.PRIOR_USER_SEARCHES_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * simulates when a user is authenticated but has not made a search.
     * @throws Exception Throws an exception if there is an error performing the get request to
     * show the searches page.
     */
    @Test
    public void getSearchesPageWhileLoggedInWithNoSearchesMade() throws Exception {
        logUserInWithoutSearches();
        mockMvc.perform(get(SearchController.BASE_URL +
                SearchController.PRIOR_USER_SEARCHES_MAPPING))
                .andExpect(view().name(SearchController.SEARCH_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR + SearchController.USER_SEARCH_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        SearchController.PRIOR_USER_SEARCHES_TITLE))
                .andExpect(model().attributeDoesNotExist(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * simulates when a user is authenticated and attempts to view their prior searches page but
     * enters in an improper page value (such as "asdf" instead of a number such as "1").
     * @throws Exception If there is an error performing the get request.
     */
    @Test
    public void getSearchesPageWithInvalidFormattedParameter() throws Exception {
        // simulate a user being logged in.
        logUserIn();
        mockMvc.perform(get(SearchController.BASE_URL +
                SearchController.PRIOR_USER_SEARCHES_MAPPING)
                .param(ForecastController.PAGE_PARAMETER, IMPROPER_FORMATTED_PAGE_NUMBER))
                .andExpect(view().name(IndexController.NOT_FOUND_VIEW_NAME))
                .andExpect(model().attributeExists(ControllerExceptionHandler.EXCEPTION_KEY))
                .andExpect(status().is4xxClientError());
    }

    /**
     * simulates when a user is authenticated and attempts to view their prior searches page but
     * enters in a page value that is out of range ("-1" instead of a a positive such as "1").
     * The expected result is that this will actually function as a good search because the
     * service will just default the page parameter to page 1.
     * @throws Exception Throws an exception if there is an error performing the get request.
     */
    @Test
    public void getSearchesPageWithInvalidParameter() throws Exception {
        // simulate a user being logged in.
        logUserIn();
        // make the request
        mockMvc.perform(get(SearchController.BASE_URL +
                SearchController.PRIOR_USER_SEARCHES_MAPPING)
                .param(ForecastController.PAGE_PARAMETER, INVALID_PAGE_NUMBER))
                .andExpect(view().name(SearchController.SEARCH_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR + SearchController.USER_SEARCH_VIEW_NAME))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * simulates when the user is not logged in and trying to view a page that requires
     * authentication.
     * @throws Exception Throws an exception if there is an error performing the get request.
     */
    @Test
    public void getSearchesPageUserNotLoggedIn() throws Exception {
        mockMvc.perform(get(SearchController.BASE_URL +
                SearchController.PRIOR_USER_SEARCHES_MAPPING))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Tests for making a request with a valid address while the user is not logged in.
     * @throws Exception Throws an exception if there is an error while making the search to get
     * forecasts. An exception can also be thrown if the address passed in is a null value or the
     * user does not pass it in (this results in null as well)
     */
    @Test
    public void processSearchForForecastWithValidAddress() throws Exception {
        long expectedSearchId = getLastSearchId() + 1;
        String expectedViewReturn = SearchController.REDIRECT + ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING + SearchController.FIRST_PARAMETER_SEPARATOR +
                ForecastController.SEARCH_PARAMETER + SearchController.PARAMETER_KEY_VALUE_SEPARATOR
                + expectedSearchId;
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING)
                .param(SearchController.ADDRESS_PARAMETER, SpringJPABootstrap.SAMPLE_ADDRESS))
                .andExpect(view().name(expectedViewReturn))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * This will simulate the case where the user (not logged in) does not pass in the address (the
     * address String is null) which will lead to a MissingServletRequestParameterException
     * being thrown and this is caught by the ControllerExceptionHandler and redirects to the
     * home page.
     * @throws Exception Throws the exception if the post request cannot be made.
     */
    @Test
    public void processSearchForForecastWithMissingAddress() throws Exception {
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING))
                .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
                .andExpect(status().is4xxClientError());
    }

    /**
     * This will simulate the case where the user (not logged in) passes in the address but it is
     * not an actual address and a location is still able to be obtained.
     * @throws Exception Throws the exception if the post request cannot be made.
     */
    @Test
    public void processSearchForForecastWithInvalidAddress() throws Exception {
        long expectedSearchId = getLastSearchId() + 1;
        String expectedViewReturn = SearchController.REDIRECT + ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING + SearchController.FIRST_PARAMETER_SEPARATOR +
                ForecastController.SEARCH_PARAMETER + SearchController.PARAMETER_KEY_VALUE_SEPARATOR
                + expectedSearchId;
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING)
                .param(SearchController.ADDRESS_PARAMETER, INVALID_ADDRESS))
                .andExpect(view().name(expectedViewReturn))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Makes a search while logged in so the search is persisted to the database. To verify the
     * search being saved the search will be looked for by id and should turn a search object.
     * @throws Exception Throws an exception if the post request cannot be made.
     */
    @Test
    public void processSearchForForecastWhileLoggedIn() throws Exception {
        logUserIn();
        Long expectedSearchId = getLastSearchId() + 1;
        String expectedViewReturn = SearchController.REDIRECT + ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING + SearchController.FIRST_PARAMETER_SEPARATOR +
                ForecastController.SEARCH_PARAMETER + SearchController.PARAMETER_KEY_VALUE_SEPARATOR
                + expectedSearchId;
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING)
                .param(SearchController.ADDRESS_PARAMETER, SpringJPABootstrap.SAMPLE_ADDRESS))
                .andExpect(view().name(expectedViewReturn))
                .andExpect(status().is3xxRedirection());
        Search savedSearch = searchRepository.findById(expectedSearchId).get();
        assertNotNull(savedSearch);
        assertEquals(expectedSearchId, savedSearch.getId());
    }

    /**
     * Makes a search while logged in but there is no provided address. This will throw an error
     * and the user would be redirected to the home (index) page and client error will be returned.
     * @throws Exception Throws an exception if the post request cannot be made.
     */
    @Test
    public void processSearchForForecastWhileLoggedInWithNoAddress() throws Exception {
        logUserIn();
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING))
                .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Makes a search and passes in the address but it is not an actual address and a location is
     * still able to be obtained. Since a location is able to be retrieved the search is
     * successfully saved and should be able to be retrieved with the expected search id.
     * @throws Exception Throws an exception if the post request cannot be made.
     */
    @Test
    public void processSearchForeCastWhileLoggedInWithInvalidAddress() throws Exception {
        logUserIn();
        Long expectedSearchId = getLastSearchId() + 1;
        String expectedViewReturn = SearchController.REDIRECT + ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING + SearchController.FIRST_PARAMETER_SEPARATOR +
                ForecastController.SEARCH_PARAMETER + SearchController.PARAMETER_KEY_VALUE_SEPARATOR
                + expectedSearchId;
        mockMvc.perform(post(SearchController.BASE_URL + SearchController
                .FORECAST_SEARCH_RESULT_MAPPING)
                .param(SearchController.ADDRESS_PARAMETER, INVALID_ADDRESS))
                .andExpect(view().name(expectedViewReturn))
                .andExpect(status().is3xxRedirection());
        Search savedSearch = searchRepository.findById(expectedSearchId).get();
        assertNotNull(savedSearch);
        assertEquals(expectedSearchId, savedSearch.getId());
    }

    /**
     * simulates a user login.
     */
    private void logUserIn() {
        Collection<GrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("User"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
                (SpringJPABootstrap.MWESTON, SpringJPABootstrap.PASSWORD, authorities));
    }

    /**
     * simulate user login that has no made no searches
     */
    private void logUserInWithoutSearches() {
        Collection<GrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("User"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
                (SpringJPABootstrap.TEST_ACCOUNT_USER_NAME, SpringJPABootstrap.TEST_ACCOUNT_PASSWORD,
                        authorities));
    }

    /**
     * A convenience method (after using multiple profiles) I noticed that sometimes the search
     * IDs would be inconsistent and skip some values so this method will get the id of the last
     * search stored and return that if there are no searches then return -1 but this is not
     * expected to occur because data will be generated for the profile(s) expected to run this
     * test.
     * @return The id of the last search, if not present return -1 (an invalid value).
     */
    private Long getLastSearchId() {
        List<Search> searches = (List<Search>) searchRepository.findAll();
        Optional<Search> searchOptional = searches.stream().skip(searches.size() - 1).findFirst();
        if(searchOptional.isPresent()) {
            return searchOptional.get().getId();
        }
        return -1L;
    }
}