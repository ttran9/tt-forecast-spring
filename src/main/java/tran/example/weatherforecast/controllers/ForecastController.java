package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tran.example.weatherforecast.domain.forecast.DailyForecast;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;
import tran.example.weatherforecast.domain.pagination.Pager;
import tran.example.weatherforecast.services.forecastservices.DailyForecastService;
import tran.example.weatherforecast.services.forecastservices.HourlyForecastService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A controller component to handle the mappings to view forecasts.
 */
@Slf4j
@Controller
@RequestMapping({ForecastController.BASE_URL})
public class ForecastController extends ControllerHelper {
    /**
     * A common portion of the URL mapping for all requests made for forecasts.
     */
    public static final String BASE_URL = "/forecast";
    /**
     * The name of the directory containing all the views for requesting pages related to searches.
     */
    public static final String FORECAST_BASE_VIEW_URL_RETURN = "forecast";
    /**
     * The mapping to display the daily forecasts for a specified search.
     */
    public static final String DAILY_FORECASTS_MAPPING = "/daily_forecast";
    /**
     * The mapping to display the hourly forecasts for a specified search.
     */
    public static final String HOURLY_FORECASTS_MAPPING = "/hourly_forecast";
    /**
     * The view displaying the daily forecasts.
     */
    public static final String DAILY_FORECASTS_VIEW_NAME = "dailyforecasts";
    /**
     * The view displaying the hourly forecasts.
     */
    public static final String HOURLY_FORECASTS_VIEW_NAME = "hourlyforecasts";
    /**
     * The search ID attribute key.
     */
    public static final String SEARCH_ID_KEY = "searchId";
    /**
     * The name of the search parameter.
     */
    public static final String SEARCH_PARAMETER = "search";
    /**
     * The name of the page parameter.
     */
    public static final String PAGE_PARAMETER = "page";
    /**
     * The name of the parsed hourly forecast list.
     */
    public static final String HOURLY_FORECAST_LIST_PARAMETER = "hourlyList";
    /**
     * The name of the parsed daily forecast list with the high temperatures.
     */
    public static final String DAILY_FORECAST_HIGH_TEMPERATURE_LIST_PARAMETER = "dailyListHighTemps";
    /**
     * The name of the parsed daily forecast list with the low temperatures.
     */
    public static final String DAILY_FORECAST_LOW_TEMPERATURE_LIST_PARAMETER = "dailyListLowTemps";
    /**
     * The title of the page that displays the daily forecasts.
     */
    public static final String DAILY_FORECAST_TITLE = "Daily Forecasts";
    /**
     * The title of the page that displays the daily forecasts.
     */
    public static final String HOURLY_FORECAST_TITLE = "Hourly Forecasts";
    /**
     * Allows access to retrieve the daily forecasts from a search.
     */
    private final DailyForecastService dailyForecastService;
    /**
     * Allows access to retrieve the hourly forecasts from a search.
     */
    private final HourlyForecastService hourlyForecastService;

    @Autowired
    public ForecastController(DailyForecastService dailyForecastService, HourlyForecastService
            hourlyForecastService) {
        this.dailyForecastService = dailyForecastService;
        this.hourlyForecastService = hourlyForecastService;
    }

    /**
     * Responds to the user's request to get the daily forecasts from the specified search and
     * a view with the daily forecasts returned as sub-lists (paginated).
     * @param model An object holding information to be displayed onto the view such as the list
     *              of daily forecasts and the total number of pages of daily forecasts.
     * @param searchId The id of the search of the daily forecasts.
     * @param page The current page of daily forecasts to get.
     * @return Returns the path to the view that will display the daily forecasts to the user.
     */
    @GetMapping(DAILY_FORECASTS_MAPPING)
    public String getDailyForecastsFromSearch(Model model, @RequestParam(value=SEARCH_PARAMETER)
            String searchId, @RequestParam(value=PAGE_PARAMETER, required = false, defaultValue =
            "1") String page) {
        log.debug("getting the daily forecasts from a search with id: " + searchId);
        Optional<Integer> searchIdOptional = Optional.of(Integer.parseInt(searchId));
        Long longSearchId = searchIdOptional.get().longValue();
        Optional<Integer> pageOptional = Optional.of(Integer.parseInt(page));
        addTitleAttribute(model, DAILY_FORECAST_TITLE);

        setModelWithPaginatedDailyForecasts(model, longSearchId, pageOptional);

        return FORECAST_BASE_VIEW_URL_RETURN + IndexController.URL_PATH_SEPARATOR +
                DAILY_FORECASTS_VIEW_NAME;
    }

    /**
     * Gets a sublist of daily forecasts to be displayed onto a view.
     * @param model An object holding information to be displayed onto the view such as the list
     *              of daily forecasts and the total number of pages of daily forecasts.
     * @param searchId The id of the search of the daily forecasts.
     * @param page The current page of daily forecasts to get.
     */
    private void setModelWithPaginatedDailyForecasts(Model model, Long searchId, Optional<Integer>
            page) {
        int currentPage = page.get();
        Page<DailyForecast> dailyForecasts = dailyForecastService.getDailyForecastsFromSearch
                (searchId, currentPage);
        Pager pager = new Pager(dailyForecasts.getTotalPages(), dailyForecasts.getNumber());
        List<Map<Object, Object>> dailyForecastHighTemperaturesList = dailyForecastService
                .parseDailyForecastHighTemperaturesToList(dailyForecasts);
        List<Map<Object, Object>> dailyForecastLowTemperaturesList = dailyForecastService
                .parseDailyForecastLowTemperaturesToList(dailyForecasts);
        model.addAttribute(DAILY_FORECAST_LOW_TEMPERATURE_LIST_PARAMETER, dailyForecastLowTemperaturesList);
        model.addAttribute(DAILY_FORECAST_HIGH_TEMPERATURE_LIST_PARAMETER, dailyForecastHighTemperaturesList);
        model.addAttribute(SearchController.LIST_KEY, dailyForecasts);
        model.addAttribute(SearchController.PAGER_KEY, pager);
        model.addAttribute(SearchController.TOTAL_PAGES_KEY, dailyForecasts.getTotalPages());
        model.addAttribute(SearchController.CURRENT_PAGE_KEY, currentPage);
        model.addAttribute(SEARCH_ID_KEY, searchId);
    }

    /**
     * Responds to the user's request to get the hourly forecasts from the specified search and
     * a view with the daily forecasts returned as sub-lists (paginated).
     * @param model An object holding information to be displayed onto the view such as the list
     *              of daily forecasts and the total number of pages of daily forecasts.
     * @param searchId The id of the search of the daily forecasts.
     * @param page The current page of daily forecasts to get.
     * @return Returns the path to the view that will display the hourly forecasts to the user.
     */
    @GetMapping(HOURLY_FORECASTS_MAPPING)
    public String getHourlyForecastsFromSearch(Model model, @RequestParam(value=SEARCH_PARAMETER)
            String searchId, @RequestParam(value=PAGE_PARAMETER, required = false, defaultValue =
            "1") String page) {
        log.debug("getting the hourly forecasts from a search with id: " + searchId);
        Optional<Integer> searchIdOptional = Optional.of(Integer.parseInt(searchId));
        Long longSearchId = searchIdOptional.get().longValue();
        Optional<Integer> pageOptional = Optional.of(Integer.parseInt(page));
        addTitleAttribute(model, HOURLY_FORECAST_TITLE);

        setModelWithPaginatedHourlyForecasts(model, longSearchId, pageOptional);

        return FORECAST_BASE_VIEW_URL_RETURN + IndexController.URL_PATH_SEPARATOR +
                HOURLY_FORECASTS_VIEW_NAME;
    }

    /**
     * Retrieves a sublist of hourly forecasts to be displayed onto a view.
     * @param model An object holding information to be displayed onto the view such as the list
     *              of daily forecasts and the total number of pages of daily forecasts.
     * @param searchId The id of the search of the daily forecasts.
     * @param page The current page of daily forecasts to get.
     */
    private void setModelWithPaginatedHourlyForecasts(Model model, Long searchId, Optional<Integer>
            page) {
        int currentPage = page.get();
        Page<HourlyForecast> hourlyForecasts = hourlyForecastService.getHourlyForecastsFromSearch
                (searchId, currentPage);
        Pager pager = new Pager(hourlyForecasts.getTotalPages(), hourlyForecasts.getNumber());
        List<Map<Object, Object>> hourlyForecastsJsonList = hourlyForecastService
                .parseHourlyForecastInformationToList(hourlyForecasts);
        model.addAttribute(HOURLY_FORECAST_LIST_PARAMETER, hourlyForecastsJsonList);
        model.addAttribute(SearchController.LIST_KEY, hourlyForecasts);
        model.addAttribute(SearchController.PAGER_KEY, pager);
        model.addAttribute(SearchController.TOTAL_PAGES_KEY, hourlyForecasts.getTotalPages());
        model.addAttribute(SearchController.CURRENT_PAGE_KEY, currentPage);
        model.addAttribute(SEARCH_ID_KEY, searchId);
    }


}
