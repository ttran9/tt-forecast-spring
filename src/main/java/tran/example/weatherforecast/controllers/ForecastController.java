package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller component to handle the mappings to view forecasts.
 */
@Slf4j
@Controller
@RequestMapping({ForecastController.BASE_URL})
public class ForecastController extends BaseController {
    /**
     * The name of view that displays the prior made by a user!
     */
    public static final String USER_SEARCH_VIEW_NAME = "searches";
    /**
     * A common portion of the URL mapping for all requests made for forecasts.
     */
    public static final String BASE_URL = "/forecast";
    /**
     * The name of the directory containing all the views for requesting pages related to forecasts.
     */
    public static final String BASE_VIEW_URL_RETURN = "forecast";
    /**
     * The title of the page that displays a user's prior searches.
     */
    public static final String PRIOR_USER_SEARCHES_TITLE = "Prior Searches";

    /**
     * Processes a request to view the user's prior searches.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of view prior searches page.
     */
    @RequestMapping(IndexController.URL_PATH_SEPARATOR + USER_SEARCH_VIEW_NAME)
    public String getSearchesPage(Model model) {
        log.debug("Showing a user's prior searches!");
        addTitleAttribute(model, PRIOR_USER_SEARCHES_TITLE);
        return BASE_VIEW_URL_RETURN + IndexController.URL_PATH_SEPARATOR + USER_SEARCH_VIEW_NAME;
    }
}
