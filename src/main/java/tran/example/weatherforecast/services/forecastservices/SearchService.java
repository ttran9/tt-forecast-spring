package tran.example.weatherforecast.services.forecastservices;

import tran.example.weatherforecast.domain.Search;

import java.util.List;

/**
 * Declares methods to perform read and insertion operations for Search object(s).
 */
public interface SearchService {

    /**
     * Gets a list of searches made by a user based on the id of the user provided.
     * @param userId The id value to identify a user.
     * @return Returns a list of Search objects.
     */
    List<Search> getSearchesByUserId(Long userId);

    /**
     * Saves the search into the database if the user is logged in.
     * Note: The search will have the daily and hourly forecasts in it allowing those to be
     * persisted when the search is saved.
     * @param search The search object to be persisted.
     * @param userId The id value to identify a user.
     * @return Returns the object that was saved into the database.
     */
    Search saveSearch(Search search, Long userId);
}
