package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.weatherforecast.bootstrap.SpringJPABootstrap;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.geocodeservices.GoogleGeocodeServiceImpl;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * The ability to save a search and retrieve the searches made by a user are tested in this class.
 */
public class SearchServiceImplTest {
    /**
     * The expected number of hourly forecasts from a search.
     */
    public static final int EXPECTED_NUMBER_OF_HOURLY_FORECASTS = 49;
    /**
     * The expected number of daily forecasts from a search.
     */
    public static final int EXPECTED_NUMBER_OF_DAILY_FORECASTS = 8;
    /**
     * Object which will make the search using an address to get forecasts.
     */
    private SearchService searchService;
    /**
     * Allows access to the data layer and to the users table.
     */
    @Mock
    private UserRepository userRepository;
    /**
     * Allows access to the data layer to verify if the user is logged in.
     */
    @Mock
    private UserAuthenticationService userAuthenticationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchService = new SearchServiceImpl(userRepository, new GoogleGeocodeServiceImpl(),
                new DarkskyServiceImpl(), userAuthenticationService);
    }

    /**
     * Tests if the service is able to grab all the searches made by a user and if the proper
     * findById method is invoked the expected number of times.
     */
    @Test()
    public void getSearchesByUserId() {
        // given
        User user = new User();
        user.addSearch(new Search());
        List<Search> searches = user.getSearches();
        Optional<User> userOptional = Optional.of(user);

        // when
        when(userRepository.findById(anyLong())).thenReturn(userOptional);
        List<Search> searchesFromService = searchService.getSearchesByUserId(anyLong());

        // then
        assertEquals(searches.size(), searchesFromService.size());
        verify(userRepository, times(1)).findById(anyLong());
    }

    /**
     * When we search by an invalid ID (such as a negative value it is expected that a
     * NotFoundException will be thrown since a user with that id cannot exist.
     */
    @Test(expected = NotFoundException.class)
    public void getSearchesByUserInvalidId() {
        // when
        searchService.getSearchesByUserId(-1L);
    }

    /**
     * This will test for an expected number of times for method calls used by the service
     * performing the search as well as checking if the search after being persisted was assigned
     * an ID value.
     */
    @Test
    public void saveSearch() {
        // given
        Search search = new Search();
        long userId = 1L;
        search.setId(userId);
        User user = new User();
        user.setId(userId);
        user.addSearch(search);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        Search savedSearch = searchService.saveSearch(search, userId);

        // then
        assertEquals(search.getId(), savedSearch.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * This will perform a search with a valid entered address so it is expected the search
     * object being returned will have an expected number of daily and hourly forecasts.
     */
    @Test
    public void createSearchWhenNotLoggedIn() {
        // given
        Search search = new Search();
        search.setAddress(SpringJPABootstrap.SAMPLE_ADDRESS);

        // when
        Search createdSearch = searchService.createSearch(search.getAddress());

        // then
        assertEquals(search.getAddress(), createdSearch.getAddress());
        assertEquals(EXPECTED_NUMBER_OF_HOURLY_FORECASTS, createdSearch.getHourlyForecasts().size());
        assertEquals(EXPECTED_NUMBER_OF_DAILY_FORECASTS, createdSearch.getDailyForecasts().size());
    }

    /**
     * A search without an address (blank) will return a default latitude and longitude.
     * This test will expect a null Search object because the response from the Google Geocoding
     * API cannot be parsed.
     */
    @Test
    public void createSearchWithBlankAddress() {
        // when
        Search createdSearch = searchService.createSearch("");

        // then
        assertNull(createdSearch);
    }
}