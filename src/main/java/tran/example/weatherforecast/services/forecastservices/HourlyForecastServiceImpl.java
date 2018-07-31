package tran.example.weatherforecast.services.forecastservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;
import tran.example.weatherforecast.repositories.HourlyForecastRepository;

import java.util.*;

/**
 * Defines methods to return a list of paginated hourly forecast objects and to parse hourly forecast
 *  contents.
 */
@Service
@Slf4j
public class HourlyForecastServiceImpl implements HourlyForecastService {
    private HourlyForecastRepository hourlyForecastRepository;

    @Autowired
    public HourlyForecastServiceImpl(HourlyForecastRepository hourlyForecastRepository) {
        this.hourlyForecastRepository = hourlyForecastRepository;
    }

    /**
     * Returns a list of hourly forecasts from the specified page number and search ID.
     * @param searchId The search that the hourly forecasts are part of.
     * @param currentPage The current page of hourly forecasts.
     * @return Returns a list of paginated hourly forecasts linked to a specific search and
     * specified page number.
     */
    @Override
    public Page<HourlyForecast> getHourlyForecastsFromSearch(Long searchId, int currentPage) {
        log.debug("attempting to get hourly forecasts from a search with id: " + searchId);
        int hourlyForecastsPerPage = 5;
        int initialPage = 0; // 0th index paging.
        Optional<Integer> optionalPage = Optional.of(currentPage);
        int evaluatedPage = (optionalPage.orElse(initialPage) < 1) ? initialPage : optionalPage.get() - 1;
        return hourlyForecastRepository.findHourlyForecastsBySearchId(PageRequest.of
                (evaluatedPage, hourlyForecastsPerPage), searchId);
    }

    /**
     * Goes through the paginated hourly forecast sublist and converts the objects to a list of
     * maps that have two objects, the temperature and the formatted date and time of the forecast.
     * @param hourlyForecasts The paginated list of hourly forecasts.
     * @return Returns a list of maps that hold the formatted date and time and temperature of
     * the hourly forecast.
     */
    @Override
    public List<Map<Object, Object>> parseHourlyForecastInformationToList(Page<HourlyForecast> hourlyForecasts) {
        log.debug("attempting to convert the necessary information to display hourly forecasts");
        List<Map<Object, Object>> parsedHourlyForecasts = new ArrayList<>();
        String xAxisLabel = "label";
        String yAxisLabel = "y";
        for (HourlyForecast hourlyForecast : hourlyForecasts) {
            hourlyForecast.convertTime();
            Map<Object, Object> map = new HashMap<>();
            map.put(xAxisLabel, hourlyForecast.getFormattedTime());
            map.put(yAxisLabel, hourlyForecast.getTemperature());
            parsedHourlyForecasts.add(map);
        }
        return parsedHourlyForecasts;
    }
}
