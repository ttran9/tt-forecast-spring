package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MissingServletRequestParameterException;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.repositories.SearchRepository;
import tran.example.weatherforecast.services.geocodeservices.GoogleGeocodeService;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import javax.swing.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceImplIntegrationTest {
    /**
     * The expected number of hourly forecasts from a search.
     */
    public static final int EXPECTED_NUMBER_OF_HOURLY_FORECASTS = 49;
    /**
     * The expected number of daily forecasts from a search.
     */
    public static final int EXPECTED_NUMBER_OF_DAILY_FORECASTS = 8;
    /**
     * Allows access to retrieve the CustomUser and to update the associated user object.
     */
    @Autowired
    private CustomUserRepository userRepository;
    /**
     * Allows access to make API requests to get the latitude and longitude of an address.
     */
    @Autowired
    private GoogleGeocodeService googleGeocodeService;
    /**
     * Allows access to make API requests to obtain forecasts.
     */
    @Autowired
    private DarkskyService darkskyService;
    /**
     * Interfaces with the Security context to check if the user is logged in.
     */
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    /**
     * Service to create the search.
     */
    private SearchService searchService;
    /**
     * Allows for the retrieval and creation of searches.
     */
    @Autowired
    private SearchRepository searchRepository;

    @Before
    public void setUp() {
        searchService = new SearchServiceImpl(userRepository, googleGeocodeService,
                darkskyService, userAuthenticationService, searchRepository);
    }

    /**
     * Tests if the service is able to get the searches bootstrapped into the application made by
     * the user with id of 1.
     */
    @Test()
    public void getSearchesByUserId() {
        int expectedNumberOfSearchesByUser = 5;
        // don't hard-code the user ID in.
        Long userId = userRepository.findByUsername(SpringJPABootstrap.MWESTON).getId();
        int firstPage = 0; // 0th based index.
        // given

        // when
        Page<Search> searchesFromService = searchService.getSearchesByUserId(userId, firstPage);

        // then
        assertEquals(expectedNumberOfSearchesByUser, searchesFromService.getNumberOfElements());
    }

    /**
     * Tests when the user makes a search while being logged in. Since the user is logged in the
     * search is persisted/saved so the returned Search object is expected to have an ID field
     * and all of its forecasts should have the same ID referencing this search object
     * (the variable, createdSearch).
     * @throws MissingServletRequestParameterException Throws this exception if the address
     * passed into the createSearch method is a null value.
     */
    @Test
    public void createSearchWhenLoggedIn() throws MissingServletRequestParameterException {
        // given
        Search search = new Search();
        search.setAddress(SpringJPABootstrap.SAMPLE_ADDRESS);
        // simulate a user being logged in.
        Collection<GrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("User"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
                (SpringJPABootstrap.MWESTON, SpringJPABootstrap.PASSWORD, authorities));

        // when
        Search createdSearch = searchService.createSearch(search.getAddress());

        // then
        assertEquals(search.getAddress(), createdSearch.getAddress());
        assertEquals(EXPECTED_NUMBER_OF_HOURLY_FORECASTS,
                createdSearch.getHourlyForecasts().size());
        assertEquals(EXPECTED_NUMBER_OF_DAILY_FORECASTS,
                createdSearch.getDailyForecasts().size());
        /**
         * ensure the persistence was done properly by checking the search ID after the save
         * operation.
          */
        Long createdSearchId = createdSearch.getId();
        assertNotNull(createdSearchId);
        createdSearch.getDailyForecasts().forEach(dailyForecast -> {
            // check the id of all the daily forecasts to ensure it matches the search's id.
            assertEquals(createdSearchId, dailyForecast.getSearch().getId());
        });
        createdSearch.getHourlyForecasts().forEach(hourlyForecast -> {
            // check the id of all the hourly forecasts to ensure it matches the search's id.
            assertEquals(createdSearchId, hourlyForecast.getSearch().getId());
        });

        // additional data members check.
        verifyAdditionalSearchFields(createdSearch);
    }

    /**
     * This will test for the search to be successful so it will look at the search object being
     * persisted (being assigned an ID) as well as properly saving the entered address (used in
     * the search), and to ensure the correct user ID was associated with the search.
     * Note: This is simulating to saving a search to when a user is logged in.
     */
    @Test
    public void saveSearch() {
        // given
        String enteredAddress = SpringJPABootstrap.STONERIDGE_MALL_RD_SAMPLE_ADDRESS;
        Long userId = userRepository.findByUsername(SpringJPABootstrap.MWESTON).getId();
        Search search = new Search();
        search.setAddress(enteredAddress);

        // when
        Search savedSearch = searchService.saveSearch(search, userId);

        // then
        assertNotNull(savedSearch.getId());
        assertEquals(enteredAddress, savedSearch.getAddress());
        assertEquals(userId, search.getUser().getId());

        // additional data members check.
        verifyAdditionalSearchFields(savedSearch);
    }

    /**
     * This will test for the search to be successful so it will look at the search object being
     * persisted (being assigned an ID) as well as properly saving the entered address (used in
     * the search), but there is no user associated with the search (this value should be null).
     * Note: This is simulating when the search is being saved by a user that cannot be found
     * from the database.
     */
    @Test
    public void saveSearchWithInvalidUserId() {
        // given
        String enteredAddress = SpringJPABootstrap.STONERIDGE_MALL_RD_SAMPLE_ADDRESS;
        Long invalidUserId = -1L;
        Search search = new Search();
        search.setAddress(enteredAddress);

        // when
        Search savedSearch = searchService.saveSearch(search, invalidUserId);

        // then
        assertNotNull(savedSearch.getId());
        assertEquals(enteredAddress, savedSearch.getAddress());
        assertNull(search.getUser());

        // additional data members check.
        verifyAdditionalSearchFields(savedSearch);
    }

    /**
     * This will perform a search with a valid entered address so it is expected the search
     * object being returned will have an expected number of daily and hourly forecasts.
     * @throws MissingServletRequestParameterException Throws this exception if the address
     * passed into the createSearch method is a null value.
     */
    @Test
    public void createSearchWhenNotLoggedIn() throws MissingServletRequestParameterException {
        // given
        Search search = new Search();
        search.setAddress(SpringJPABootstrap.SAMPLE_ADDRESS);

        // when
        Search createdSearch = searchService.createSearch(search.getAddress());

        // then
        assertEquals(search.getAddress(), createdSearch.getAddress());
        assertNotNull(createdSearch.getId()); // ensure persistence.
        assertEquals(EXPECTED_NUMBER_OF_HOURLY_FORECASTS, createdSearch.getHourlyForecasts().size());
        assertEquals(EXPECTED_NUMBER_OF_DAILY_FORECASTS, createdSearch.getDailyForecasts().size());

        // additional data members check.
        verifyAdditionalSearchFields(createdSearch);
    }

    /**
     * helper method to verify additional fields were set.
     * @param search The search object with fields to be verified.
     */
    private void verifyAdditionalSearchFields(Search search) {
        // additional checks to verify fields are properly initialized.
        assertNotNull(search.getMonthDayYear());
        assertNotNull(search.getTimeFormat());
        assertNotNull(search.getAt());
    }
}