package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.geocodeservices.GoogleGeocodeService;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import javax.swing.*;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceImplTestIT {
    /**
     * Allows access to retrieve the User and to update the associated user object.
     */
    @Autowired
    private UserRepository userRepository;
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

    @Before
    public void setUp() {
        searchService = new SearchServiceImpl(userRepository, googleGeocodeService,
                darkskyService, userAuthenticationService);
    }

    /**
     * Tests when the user makes a search while being logged in. Since the user is logged in the
     * search is persisted/saved so the returned Search object is expected to have an ID field
     * and all of its forecasts should have the same ID referencing this search object
     * (the variable, createdSearch).
     */
    @Test
    @Transactional
    public void createSearchWhenLoggedIn() {
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
        assertEquals(SearchServiceImplTest.EXPECTED_NUMBER_OF_HOURLY_FORECASTS,
                createdSearch.getHourlyForecasts().size());
        assertEquals(SearchServiceImplTest.EXPECTED_NUMBER_OF_DAILY_FORECASTS,
                createdSearch.getDailyForecasts().size());
        /**
         * ensure the persistence was done properly by checking the search ID after the save
         * operation.
          */
        Long createdSearchId = createdSearch.getId();
        assertNotNull(createdSearchId);
        // check the id of all the daily forecasts to ensure it matches the search's id.
        createdSearch.getDailyForecasts().forEach(dailyForecast -> {
            assertEquals(createdSearchId, dailyForecast.getSearch().getId());
        });
        // check the id of all the hourly forecasts to ensure it matches the search's id.
        createdSearch.getHourlyForecasts().forEach(hourlyForecast -> {
            assertEquals(createdSearchId, hourlyForecast.getSearch().getId());
        });
    }
}