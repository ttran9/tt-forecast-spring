package tran.example.weatherforecast.services.forecastservices;

import tran.example.weatherforecast.domain.forecast.Forecast;

import java.io.IOException;

/**
 * Declares methods to get content from the Darksky API and parses the results.
 */
public interface DarkskyService {

    /**
     * Gets content (forecasts) from the Darksky API with a specified latitude and longitude.
     * @param latitude The latitude for the location to request forecasts for.
     * @param longitude The longitude for the location to request forecasts for.
     * @return Returns a string which holds forecasts (daily and hourly).
     * @throws IOException Throws an exception if the request cannot be made (not if the request
     * does not return valid data).
     */
    String getContent(String latitude, String longitude) throws IOException;

    /**
     * This method parses the JSON content into domain objects which hold a list of daily and
     * hourly forecasts.
     * @param content The JSON content (consisting of forecasts) from making the request to the
     *                Darksky API.
     * @return Returns an object that holds the daily and hourly forecasts.
     * @throws IOException Throws an IOException when unable to convert the JSON content to a
     * domain object using Jackson.
     */
    Forecast getForecasts(String content) throws IOException;
}
