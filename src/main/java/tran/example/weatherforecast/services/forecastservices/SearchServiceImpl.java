package tran.example.weatherforecast.services.forecastservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.forecast.Forecast;
import tran.example.weatherforecast.domain.geocode.Location;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.repositories.SearchRepository;
import tran.example.weatherforecast.services.geocodeservices.GoogleGeocodeService;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import java.io.IOException;
import java.util.Optional;

/**
 * Implementation of methods to save a Search made by a specified CustomUser and to find all the
 * searches made by a user.
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    /**
     * 0th indexed original page number.
     */
    public static final int INITIAL_PAGE = 0;
    /**
     * Allows access to retrieve the CustomUser and to update the associated user object.
     */
    private CustomUserRepository userRepository;
    /**
     * Allows access to make API requests to get the latitude and longitude of an address.
     */
    private GoogleGeocodeService googleGeocodeService;
    /**
     * Allows access to make API requests to obtain forecasts.
     */
    private DarkskyService darkskyService;
    /**
     * Interfaces with the Security context to check if the user is logged in.
     */
    private UserAuthenticationService userAuthenticationService;
    /**
     * Allows for the retrieval and creation of searches.
     */
    private SearchRepository searchRepository;

    /**
     * Performs DI and wires the userRepository object accordingly so CRUD operations can be
     * performed.
     * @param userRepository The dependency required to wire this service to perform CRUD
     *                       operations.
     */
    @Autowired
    public SearchServiceImpl(CustomUserRepository userRepository, GoogleGeocodeService
            googleGeocodeService, DarkskyService darkskyService, UserAuthenticationService
            userAuthenticationService, SearchRepository searchRepository) {
        this.userRepository = userRepository;
        this.googleGeocodeService = googleGeocodeService;
        this.darkskyService = darkskyService;
        this.userAuthenticationService = userAuthenticationService;
        this.searchRepository = searchRepository;
    }

    /**
     * Finds a user based on the specified id and returns a paginated list of the searches made by
     * the user.
     * For now I will be hard-coding how many entries are on each page and the initial page
     * should be hardcoded to the first page.
     * @param userId The id value to identify a user.
     * @param currentPage The current page of searches.
     * @return Returns a sublist of a list containing search objects.
     */
    @Override
    public Page<Search> getSearchesByUserId(Long userId, int currentPage) {
        String debugMessage = "attempting to find the searches made by a user with id: " + userId;
        String exceptionMessage = "The user could not be found while attempting to retrieve" +
                " the searches";
        int searchesPerPage = 5;
        checkIfUserIsPresent(debugMessage, exceptionMessage, userId);

        Optional<Integer> optionalPage = Optional.of(currentPage);
        // PageRequest is 0th based index while this currentPage parameter is 1th based.
        // if the page is an invalid parameter we will start at the first page.
        int evaluatedPage = (optionalPage.orElse(INITIAL_PAGE) < 1) ? INITIAL_PAGE : optionalPage.get() - 1;
        return searchRepository.findAllByUserId(PageRequest.of(evaluatedPage, searchesPerPage), userId);
    }

    /**
     * Adds the search to a list of searches made by the user and then updates this user object and
     * returns the search object.
     * The search will not be saved if the user is not logged in.
     * which saves the search into the database.
     * @param search The search object to be persisted.
     * @param userId The id value to identify a user.
     * @return Returns the search object saved to the database.
     */
    @Override
    public Search saveSearch(Search search, Long userId) {
        String debugMessage = "attempting to save the search made by user with" +  "with id: " +
                userId;
        String exceptionMessage = "The user could not be found while attempting to retrieve" +
                " the searches";
        try {
            CustomUser user = checkIfUserIsPresent(debugMessage, exceptionMessage, userId);
            search.setUser(user);
            return searchRepository.save(search);
        } catch (NotFoundException notFoundException) {
            log.debug("user that is logged in cannot be found!");
            return searchRepository.save(search);
        }
    }

    /**
     * Takes in an address and obtains a location and gets forecasts for the location specified.
     * If the user is not logged in this method does not attempt to save the search.
     * @param address The address to get the forecasts for.
     * @return A search object with the user entered address and the forecasts.
     */
    @Override
    public Search createSearch(String address) {
        try {
            // get the location
            String locationContent = googleGeocodeService.getContent(address);
            Location location = googleGeocodeService.getLatitudeAndLongitude(locationContent);
            // use the obtained location to get the forecasts.
            String forecastContent = darkskyService.getContent(String.valueOf(location
                            .getLatitude()), String.valueOf(location.getLongitude()));
            Forecast forecast = darkskyService.getForecasts(forecastContent);
            // transfer data from the forecast object to the search object.
            Search search = new Search();
            search.setAddress(address);
            search.setFormattedDateSearch();
            // create a reference from the forecast to this search.
            search.setDailyForecasts(forecast.getDailyForecastList().getDailyForecasts());
            search.setHourlyForecasts(forecast.getHourlyForecastList().getHourlyForecasts());
            search.getDailyForecasts().forEach(dailyForecast -> dailyForecast.setSearch(search));
            search.getHourlyForecasts().forEach(hourlyForecast -> hourlyForecast.setSearch(search));
            // get the user id of the currently logged in user.
            CustomUser user = userAuthenticationService.checkIfUserIsLoggedIn();
            if(user != null) {
                // make the search if the user is logged in.
                return saveSearch(search, user.getId());
            } else {
                /*
                 * save it using the search repository which gives it an ID but has no user
                 * associated with it.
                 */
               return searchRepository.save(search);
            }
        } catch(IOException ex) {
            log.debug("while searching there was an error!");
            return null;
        }
    }

    /**
     * helper method to check if the user can be found from the specified user id.
     * This is necessary if the operation requires for the user to be logged in.
     * @param debugMessage The debug message to print to the console.
     * @param notFoundExceptionMessage The message that indicates there was an error finding the
     *                                 user if applicable
     * @param userId The id of the logged in user.
     */
    private CustomUser checkIfUserIsPresent(String debugMessage, String notFoundExceptionMessage, Long
            userId) {
        log.debug(debugMessage);
        Optional<CustomUser> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            throw new NotFoundException(notFoundExceptionMessage);
        }
        return userOptional.get();
    }


}
