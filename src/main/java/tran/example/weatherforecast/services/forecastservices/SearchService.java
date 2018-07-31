package tran.example.weatherforecast.services.forecastservices;

import org.springframework.data.domain.Page;
import tran.example.weatherforecast.domain.Search;

/**
 * Declares methods to perform read and insertion operations for Search object(s).
 */
public interface SearchService {

    /**
     * Gets a list of searches made by a user based on the id of the user provided.
     * @param userId The id value to identify a user.
     * @param page The current page of searches.
     * @return Returns a sublist of a list containing search objects.
     */
    Page<Search> getSearchesByUserId(Long userId, int page);

    /**
     * Saves the search into the database if the user is logged in.
     * Note: The search will have the daily and hourly forecasts in it allowing those to be
     * persisted when the search is saved.
     * @param search The search object to be persisted.
     * @param userId The id value to identify a user.
     * @return Returns the object that was saved into the database.
     */
    Search saveSearch(Search search, Long userId);

    /**
     * Takes in an address and queries the Google Geocoding API to get the location and the
     * Darksky API to get the forecasts for the location.
     * @param address The address to get the forecasts for.
     * @return Returns a search object that contains the hourly and daily forecasts.
     */
    Search createSearch(String address);
}
