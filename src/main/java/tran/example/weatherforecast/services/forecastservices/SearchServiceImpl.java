package tran.example.weatherforecast.services.forecastservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of methods to save a Search made by a specified User and to find all the
 * searches made by a user.
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    /**
     * Allows access to retrieve the User and to update the associated user object.
     */
    private UserRepository userRepository;

    /**
     * Performs DI and wires the userRepository object accordingly so CRUD operations can be
     * performed.
     * @param userRepository The dependency required to wire this service to perform CRUD
     *                       operations.
     */
    @Autowired
    public SearchServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user based on the specified id and returns a list of the searches made by the user.
     * @param userId The id value to identify a user.
     * @return Returns a list of searches made by a user.
     */
    @Override
    public List<Search> getSearchesByUserId(Long userId) {
        String debugMessage = "attempting to find the searches made by a user with id: " + userId;
        String exceptionMessage = "The user could not be found while attempting to retrieve" +
                " the searches";
        User user = checkIfUserIsPresent(debugMessage, exceptionMessage, userId);
        return user.getSearches();
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
            User user = checkIfUserIsPresent(debugMessage, exceptionMessage, userId);
            // do other things to save the search.
            user.addSearch(search);
            User userWithUpdatedSearches = userRepository.save(user);
            /**
             * returning the last element in the searches list because I assume the user cannot save
             * the same search twice and if this was possible it would have identical content except
             * the ID would differ which would not make a difference to the user viewing forecasts.
             */
            return userWithUpdatedSearches.getSearches().get(userWithUpdatedSearches.getSearches()
                    .size() - 1);
        } catch (NotFoundException notFoundException) {
            log.debug("user is not logged in so this search will not be saved");
            return search;
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
    private User checkIfUserIsPresent(String debugMessage, String notFoundExceptionMessage, Long
            userId) {
        log.debug(debugMessage);
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            throw new NotFoundException(notFoundExceptionMessage);
        }
        return userOptional.get();
    }
}
