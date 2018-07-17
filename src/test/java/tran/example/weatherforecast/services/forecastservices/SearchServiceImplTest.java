package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MissingServletRequestParameterException;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.SearchRepository;
import tran.example.weatherforecast.repositories.UserRepository;
import tran.example.weatherforecast.services.geocodeservices.GoogleGeocodeServiceImpl;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.anyInt;

/**
 * The ability to save a search and retrieve the searches made by a user are tested in this class.
 */
public class SearchServiceImplTest {
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
    /**
     * Allows for the retrieval and creation of searches.
     */
    @Mock
    private SearchRepository searchRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchService = new SearchServiceImpl(userRepository, new GoogleGeocodeServiceImpl(),
                new DarkskyServiceImpl(), userAuthenticationService, searchRepository);
    }

    /**
     * When we search by an invalid ID (such as a negative value it is expected that a
     * NotFoundException will be thrown since a user with that id cannot exist.
     */
    @Test(expected = NotFoundException.class)
    public void getSearchesByUserInvalidId() {
        // when
        searchService.getSearchesByUserId(-1L, anyInt());
    }

    /**
     * A search without an address (blank) will return a default latitude and longitude.
     * This test will expect a null Search object because the response from the Google Geocoding
     * API cannot be parsed.
     * @throws MissingServletRequestParameterException Throws this exception if the address passed
     * into the createSearch method is a null value.
     */
    @Test
    public void createSearchWithBlankAddress() throws MissingServletRequestParameterException {
        // when
        Search createdSearch = searchService.createSearch("");

        // then
        assertNull(createdSearch);
    }
}