package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A class to hold mappings for views/pages not related to forecasts.
 */
@Slf4j
@Controller
public class IndexController {

    /**
     * The name of the view to display the index/home page.
     */
    public static final String INDEX_VIEW_NAME = "index";
    /**
     * A common portion of the URL mapping for all requests made for this controller.
     */
    public static final String URL_PATH_SEPARATOR = "/";
    /**
     * The name of the view/page to display the access denied page.
     */
    public static final String DENIED_VIEW_NAME = "access_denied";
    /**
     * The name of the view/page to display the login page.
     */
    public static final String SIGNIN_VIEW_NAME = "signin";
    /**
     * The mapping to process a GET request to request for the login page.
     */
    public static final String LOGIN_PAGE_MAPPING = "login";
    /**
     * The mapping to process a GET request to send the user to the access denied page.
     */
    public static final String DENIED_PAGE_MAPPING = "/denied";
    /**
     * The name of the page to display prior user searches.
     */
    public static final String PRIOR_SEARCHES_VIEW_NAME = "searches";

    /**
     * Processes the request to retrieve the index page.
     * @return The name of the index page.
     */
    @RequestMapping(URL_PATH_SEPARATOR)
    public String getIndexPage() {
        log.debug("At the home/index page!");
        return INDEX_VIEW_NAME;
    }

    /**
     * Processes the request to retrieve the access denied page.
     * @return The name of the access denied page.
     */
    @RequestMapping(DENIED_PAGE_MAPPING)
    public String getAccessDeniedPage() {
        log.debug("At the access denied page!");
        return DENIED_VIEW_NAME;
    }

    /**
     * Processes the request to retrieve the login page.
     * @return The name of the login page.
     */
    @RequestMapping(LOGIN_PAGE_MAPPING)
    public String loginForm() {
        log.debug("Displaying the login page/form!");
        return SIGNIN_VIEW_NAME;
    }
}
