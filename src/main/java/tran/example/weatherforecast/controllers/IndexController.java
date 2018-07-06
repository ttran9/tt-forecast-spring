package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A class to hold mappings for views/pages not related to forecasts.
 */
@Slf4j
@Controller
public class IndexController extends BaseController {
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
    public static final String DENIED_VIEW_NAME = "accessdenied";
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
     * The title of the index page.
     */
    public static final String INDEX_PAGE_TITLE = "Home Page";
    /**
     * The title of the access denied page.
     */
    public static final String ACCESS_DENIED_PAGE_TITLE = "Access Denied Page";
    /**
     * The title of the login/signin page.
     */
    public static final String LOGIN_PAGE_TITLE = "Login Page";
    /**
     * The title of the page that informs the user that the resource being attempted to be
     * retrieved can't be found.
     */
    public static final String RESOURCE_NOT_FOUND_PAGE_TITLE = "Resource Not Found";
    /**
     * The mapping to lead to the not found page.
     */
    public static final String NOT_FOUND_MAPPING = "notfound";
    /**
     * The name of the not found page.
     */
    public static final String NOT_FOUND_VIEW_NAME = "resourcenotfound";

    /**
     * Processes the request to retrieve the index page.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of the index page.
     */
    @RequestMapping(URL_PATH_SEPARATOR)
    public String getIndexPage(Model model) {
        log.debug("At the home/index page!");
        addTitleAttribute(model, INDEX_PAGE_TITLE);
        return INDEX_VIEW_NAME;
    }

    /**
     * Processes the request to retrieve the access denied page.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of the access denied page.
     */
    @RequestMapping(DENIED_PAGE_MAPPING)
    public String getAccessDeniedPage(Model model) {
        log.debug("At the access denied page!");
        addTitleAttribute(model, ACCESS_DENIED_PAGE_TITLE);
        return DENIED_VIEW_NAME;
    }

    /**
     * Processes the request to retrieve the login page.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of the login page.
     */
    @RequestMapping(LOGIN_PAGE_MAPPING)
    public String loginForm(Model model) {
        log.debug("Displaying the login page/form!");
        addTitleAttribute(model, LOGIN_PAGE_TITLE);
        return SIGNIN_VIEW_NAME;
    }

    /**
     * Processes the request to display the resource not found page.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of the not found page.
     */
    @RequestMapping(NOT_FOUND_MAPPING)
    public String notFoundPage(Model model) {
        log.debug("Returning the not found page!");
        addTitleAttribute(model, RESOURCE_NOT_FOUND_PAGE_TITLE);
        return NOT_FOUND_VIEW_NAME;
    }
}
