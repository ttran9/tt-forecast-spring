package tran.example.weatherforecast.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * These tests will test if a user can view the daily and hourly forecasts under a variety of
 * scenarios such as passing in valid parameters an invalid parameters and expecting different
 * results back such as getting back paginated results and sometimes being redirected to a page
 * indicating the user passed in an improperly formatted parameter.
 * Pager Tests Description/Overview (below):
 * There are a series of tests which will test the custom Pager object which is used to display
 * the five pages a user can view as well as 4 other buttons which allow to go to the first page,
 * the previous page, the next page, and the final page. However, the focus of these tests
 * looking at the Pager will focus more on which five pages are generated. In order to do so, the
 * hourly forecasts will be used because the daily forecasts will not have enough pages to be
 * able to test the Pager.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"development"})
public class ForecastControllerIntegrationTest {
    /**
     * Entry point for MVC testing.
     */
    private MockMvc mockMvc;
    /**
     * Holds the configuration of the context for the below tests.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
     * An Id value which can be used to request for the first search.
     */
    private String idOne = "1";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    /**
     * Gets the daily forecasts from the search with an ID of 1.
     */
    @Test
    public void getDailyForecastsFromSearch() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.DAILY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.DAILY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.DAILY_FORECAST_TITLE))
                .andExpect(model().attributeExists(ForecastController.DAILY_FORECAST_LOW_TEMPERATURE_LIST_PARAMETER))
                .andExpect(model().attributeExists(ForecastController.DAILY_FORECAST_HIGH_TEMPERATURE_LIST_PARAMETER))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the daily forecasts without specifying a search id.
     * This is expected to throw a MissingServletRequestParameterException which is caught by a
     * defined ControllerAdvice exception handler.
     */
    @Test
    public void getDailyForecastsFromSearchWithoutId() throws Exception{
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.DAILY_FORECASTS_MAPPING))
                .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
                .andExpect(model().attributeExists(ControllerExceptionHandler.EXCEPTION_KEY))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Gets the daily forecasts while specifying a valid search id but a page number that isn't a
     * valid value (-1 instead of 1).
     * This is expected to work fine because the service will convert the page number of -1 to 1.
     */
    @Test
    public void getDailyForecastsFromSearchWithIdAndInvalidPageNumber() throws Exception{
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.DAILY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, SearchControllerIntegrationTest.INVALID_PAGE_NUMBER))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.DAILY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.DAILY_FORECAST_TITLE))
                .andExpect(model().attributeExists(ForecastController.DAILY_FORECAST_LOW_TEMPERATURE_LIST_PARAMETER))
                .andExpect(model().attributeExists(ForecastController.DAILY_FORECAST_HIGH_TEMPERATURE_LIST_PARAMETER))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the daily forecasts while specifying a valid search id but a page number that is not
     * in the proper format ("asdf" vs "1").
     * This is expected to throw a NumberFormatException which is caught by a defined
     * ControllerAdvice exception handler.
     */
    @Test
    public void getDailyForecastsFromSearchWithIdAndImproperFormattedPageNumber() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.DAILY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, SearchControllerIntegrationTest.IMPROPER_FORMATTED_PAGE_NUMBER))
                .andExpect(view().name(IndexController.NOT_FOUND_VIEW_NAME))
                .andExpect(model().attributeExists(ControllerExceptionHandler.EXCEPTION_KEY))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Gets the hourly forecasts from the search with an ID of 1.
     */
    @Test
    public void getHourlyForecastsFromSearch() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the hourly forecasts without specifying a search id.
     * This will redirect the user to the home page as the user has not passed in a required
     * parameter (search id).
     */
    @Test
    public void getHourlyForecastsFromSearchWithoutId() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING))
                .andExpect(view().name(IndexController.INDEX_VIEW_NAME))
                .andExpect(model().attributeExists(ControllerExceptionHandler.EXCEPTION_KEY))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id but a page number that isn't a
     * valid value (-1 instead of 1).
     * This is expected to work fine because the service will convert the page number of -1 to 1.
     */
    @Test
    public void getHourlyForecastsFromSearchWithIdAndInvalidPageNumber() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, SearchControllerIntegrationTest.INVALID_PAGE_NUMBER))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id but a page number that is not
     * in the proper format ("asdf" vs "1").
     * This will redirect the user to the home page and will have an attribute containing
     * information about the user's error.
     */
    @Test
    public void getHourlyForecastsFromSearchWithIdAndImproperFormattedPageNumber() throws
            Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, SearchControllerIntegrationTest.IMPROPER_FORMATTED_PAGE_NUMBER))
                .andExpect(view().name(IndexController.NOT_FOUND_VIEW_NAME))
                .andExpect(model().attributeExists(ControllerExceptionHandler.EXCEPTION_KEY))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id and a page number meeting
     * the condition of line 45 in the Pager class.
     */
    @Test
    public void getHourlyForecastsFromSearchWithSearchIdAndPageZero() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, "0"))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id and the page number meeting the
     * condition of line 52 in the Pager class.
     */
    @Test
    public void getHourlyForecastsFromSearchWithSearchIdAndPageIsTotal() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, "9"))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id and the page number meeting the
     * condition of line 59 in the Pager class.
     */
    @Test
    public void getHourlyForecastsFromSearchWithSearchIdAndPageIsGreaterThanTotal()
            throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, "10"))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }

    /**
     * Gets the hourly forecasts while specifying a valid search id and the page number meeting the
     * condition of line 66 in the Pager class.
     */
    @Test
    public void getHourlyForecastsFromSearchWithSearchIdAndPageisHalfTotalPages() throws Exception {
        mockMvc.perform(get(ForecastController.BASE_URL +
                ForecastController.HOURLY_FORECASTS_MAPPING)
                .param(ForecastController.SEARCH_PARAMETER, idOne)
                .param(ForecastController.PAGE_PARAMETER, "5"))
                .andExpect(view().name(ForecastController.FORECAST_BASE_VIEW_URL_RETURN +
                        IndexController.URL_PATH_SEPARATOR +
                        ForecastController.HOURLY_FORECASTS_VIEW_NAME))
                .andExpect(model().attribute(ControllerHelper.PAGE_ATTRIBUTE,
                        ForecastController.HOURLY_FORECAST_TITLE))
                .andExpect(model().attributeExists(SearchController.LIST_KEY))
                .andExpect(model().attributeExists(SearchController.PAGER_KEY))
                .andExpect(model().attributeExists(SearchController.TOTAL_PAGES_KEY))
                .andExpect(model().attributeExists(SearchController.CURRENT_PAGE_KEY))
                .andExpect(status().isOk());
    }
}