package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * The ability to save a search and retrieve the searches made by a user are tested in this class.
 */
public class SearchServiceImplTest {

    SearchServiceImpl searchService;
    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchService = new SearchServiceImpl(userRepository);
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
     * notfoundexception will be thrown since a user with that id cannot exist.
     */
    @Test(expected = NotFoundException.class)
    public void getSearchesByUserInvalidId() {
        // when
        searchService.getSearchesByUserId(-1L);
    }

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
}