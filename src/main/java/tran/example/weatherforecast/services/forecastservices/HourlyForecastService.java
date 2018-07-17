package tran.example.weatherforecast.services.forecastservices;

import org.springframework.data.domain.Page;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;

import java.util.List;
import java.util.Map;

/**
 * Declares methods to obtain a list of paginated hourly forecasts and to parse hourly forecast
 * contents.
 */
public interface HourlyForecastService {
    /**
     * Gets a paginated list of hourly forecasts from the specified search and page number.
     * @param searchId The search that the hourly forecasts are part of.
     * @param currentPage The current page of hourly forecasts.
     * @return A paginated list (sub-list) of hourly forecasts.
     */
    Page<HourlyForecast> getHourlyForecastsFromSearch(Long searchId, int currentPage);

    /**
     * Goes through the paginated hourly forecast sublist and converts the objects to a list of
     * maps that have two objects, the temperature and the formatted date and time of the forecast.
     * @param hourlyForecasts The paginated list of hourly forecasts.
     * @return Returns a list of maps that hold the formatted date and time and temperature of
     * the hourly forecast.
     */
    List<Map<Object, Object>> parseHourlyForecastInformationToList(Page<HourlyForecast> hourlyForecasts);
}
