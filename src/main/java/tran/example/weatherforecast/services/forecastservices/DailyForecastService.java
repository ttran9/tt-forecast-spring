package tran.example.weatherforecast.services.forecastservices;

import org.springframework.data.domain.Page;
import tran.example.weatherforecast.domain.forecast.DailyForecast;

import java.util.List;
import java.util.Map;

/**
 * Declares methods to obtain a list of paginated daily forecasts and to parse daily forecast
 * contents.
 */
public interface DailyForecastService {
    /**
     * Gets a paginated list of daily forecasts from the specified search and page number.
     * @param searchId The search that the daily forecasts are part of.
     * @param currentPage The current page of daily forecasts.
     * @return A paginated list (sub-list) of daily forecasts.
     */
    Page<DailyForecast> getDailyForecastsFromSearch(Long searchId, int currentPage);

    /**
     * Goes through the paginated daily forecast sublist and converts the objects to a JSON
     * string containing the low temperatures as well as the date and time of these.
     * @param dailyForecasts The paginated list of daily forecasts.
     * @return Returns a list of maps that contain the low temperatures and the date
     * and time of the daily forecast.
     */
    List<Map<Object, Object>> parseDailyForecastLowTemperaturesToList(Page<DailyForecast> dailyForecasts);

    /**
     * Goes through the paginated daily forecast sublist and converts the objects to a JSON
     * string containing the high temperatures as well as the date and time of these.
     * @param dailyForecasts The paginated list of daily forecasts.
     * @return Returns a list of maps that contain the high temperatures and the date
     * and time of the daily forecast.
     */
    List<Map<Object, Object>> parseDailyForecastHighTemperaturesToList(Page<DailyForecast> dailyForecasts);
}
