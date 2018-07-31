package tran.example.weatherforecast.services.forecastservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.forecast.DailyForecast;
import tran.example.weatherforecast.repositories.DailyForecastRepository;

import java.util.*;

/**
 * Defines methods to return a list of paginated daily forecast objects and to parse daily
 * forecast contents.
 */
@Service
@Slf4j
public class DailyForecastServiceImpl implements DailyForecastService {

    private DailyForecastRepository dailyForecastRepository;

    @Autowired
    public DailyForecastServiceImpl(DailyForecastRepository dailyForecastRepository) {
        this.dailyForecastRepository = dailyForecastRepository;
    }

    /**
     * Returns a range of daily forecasts from the specified page number and with the specified
     * search ID.
     * @param searchId The search that the daily forecasts are part of.
     * @param currentPage The current page of daily forecasts.
     * @return Returns a list of paginated daily forecasts linked to a specific search and
     * specified page number.
     */
    @Override
    public Page<DailyForecast> getDailyForecastsFromSearch(Long searchId, int currentPage) {
        log.debug("attempting to get daily forecasts from a search with id: " + searchId);
        int dailyForecastsPerPage = 5;
        int initialPage = 0; // 0th index paging.

        Optional<Integer> optionalPage = Optional.of(currentPage);
        int evaluatedPage = (optionalPage.orElse(initialPage) < 1) ? initialPage : optionalPage.get() - 1;
        return dailyForecastRepository.findDailyForecastsBySearchId(PageRequest.of(evaluatedPage,
                dailyForecastsPerPage), searchId);
    }

    /**
     * Goes through the paginated daily forecast sublist and converts the objects to a JSON
     * string containing the low temperatures as well as the date and time of these.
     * @param dailyForecasts The paginated list of daily forecasts.
     * @return Returns a list of maps that contain the low temperatures and the date
     * and time of the daily forecast.
     */
    @Override
    public List<Map<Object, Object>> parseDailyForecastLowTemperaturesToList(Page<DailyForecast>
                                                                            dailyForecasts) {
        log.debug("attempting to convert low temperatures to be displayed!");
        List<Map<Object, Object>> parsedDailyForecastsWithLowTemperatures = new ArrayList<>();
        String xAxisLabel = "label";
        String yAxisLabel = "y";
        for(DailyForecast dailyForecast : dailyForecasts) {
            Map<Object, Object> map = new HashMap<>();
            map.put(xAxisLabel, dailyForecast.getFormattedTemperatureLowTime());
            map.put(yAxisLabel, dailyForecast.getTemperatureLow());
            parsedDailyForecastsWithLowTemperatures.add(map);
        }
        return parsedDailyForecastsWithLowTemperatures;
    }

    /**
     * Goes through the paginated daily forecast sublist and converts the objects to a JSON
     * string containing the high temperatures as well as the date and time of these.
     * @param dailyForecasts The paginated list of daily forecasts.
     * @return Returns a list of maps that contain the high temperatures and the date
     * and time of the daily forecast.
     */
    @Override
    public List<Map<Object, Object>> parseDailyForecastHighTemperaturesToList(Page<DailyForecast>
                                                                             dailyForecasts) {
        log.debug("attempting to convert high temperatures to be displayed!");
        List<Map<Object, Object>> parsedDailyForecastsWithHighTemperatures = new ArrayList<>();
        String xAxisLabel = "label";
        String yAxisLabel = "y";
        for(DailyForecast dailyForecast : dailyForecasts) {
            Map<Object, Object> map = new HashMap<>();
            map.put(xAxisLabel, dailyForecast.getFormattedTemperatureHighTime());
            map.put(yAxisLabel, dailyForecast.getTemperatureHigh());
            parsedDailyForecastsWithHighTemperatures.add(map);
        }
        return parsedDailyForecastsWithHighTemperatures;
    }
}
