package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import tran.example.weatherforecast.commands.SearchCommand;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.User;
import tran.example.weatherforecast.domain.pagination.Pager;
import tran.example.weatherforecast.services.forecastservices.SearchService;
import tran.example.weatherforecast.services.security.UserAuthenticationService;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping({SearchController.BASE_URL})
public class SearchController extends ControllerHelper {
    /**
     * A common portion of the URL mapping for all requests made for forecasts.
     */
    public static final String BASE_URL = "/searches";
    /**
     * The name of view that displays the prior made by a user!
     */
    public static final String USER_SEARCH_VIEW_NAME = "priorsearches";
    /**
     * The name of the directory containing all the views for requesting pages related to searches.
     */
    public static final String SEARCH_BASE_VIEW_URL_RETURN = "search";
    /**
     * The title of the page that displays a user's prior searches.
     */
    public static final String PRIOR_USER_SEARCHES_TITLE = "Prior Searches";
    /**
     * The mapping to get the searches page for a user.
     */
    public static final String PRIOR_USER_SEARCHES_MAPPING = "/prior_searches";
    /**
     * The name of the attribute holding the paginated list of objects.
     */
    public static final String LIST_KEY = "list";
    /**
     * The name of the attribute that holds information on how to create the widgets to navigate
     * between pages.
     */
    public static final String PAGER_KEY = "pager";
    /**
     * Name of the attribute to hold the total name of pages of items in the paginated list.
     */
    public static final String TOTAL_PAGES_KEY = "totalPages";
    /**
     * Name of the attribute to keep track of what page the user is currently viewing for the
     * paginated lists.
     */
    public static final String CURRENT_PAGE_KEY = "currentPage";
    /**
     * The name of the address parameter
     */
    public static final String ADDRESS_PARAMETER = "address";
    /**
     * The mapping to process the search for forecasts.
     */
    public static final String FORECAST_SEARCH_RESULT_MAPPING = "/search_result";
    /**
     * A character to separate the parameter in the URL.
     */
    public static final String FIRST_PARAMETER_SEPARATOR = "?";
    /**
     * The character to separate a parameter key and it's value.
     */
    public static final String PARAMETER_KEY_VALUE_SEPARATOR = "=";
    /**
     * The string to forward a user to another mapping.
     */
    public static final String REDIRECT = "redirect:";
    /**
     * Service to access the searches.
     */
    private final SearchService searchService;
    /**
     * Service to return a user object which can be used to get information about the logged in
     * user.
     */
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public SearchController(SearchService searchService, UserAuthenticationService userAuthenticationService) {
        this.searchService = searchService;
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * Processes a request to view the user's prior searches.
     * @param model UI component holding attributes to be used in the view.
     * @return The name of view prior searches page.
     */
    @GetMapping(PRIOR_USER_SEARCHES_MAPPING)
    public String getSearchesPage(Model model, @RequestParam(value = "page", required = false,
            defaultValue = "1") String page) {
        Optional<Integer> pageOptional = Optional.of(Integer.parseInt(page));
        log.debug("Showing a user's prior searches!");
        addTitleAttribute(model, PRIOR_USER_SEARCHES_TITLE);
        // with the help of spring security the below is expected to not be null.
        User user = userAuthenticationService.checkIfUserIsLoggedIn();
        if(user == null) {
            return IndexController.URL_PATH_SEPARATOR + IndexController.LOGIN_PAGE_MAPPING;
        }
        setModelWithPaginatedContent(model, pageOptional, user);
        return SEARCH_BASE_VIEW_URL_RETURN + IndexController.URL_PATH_SEPARATOR + USER_SEARCH_VIEW_NAME;
    }

    /**
     * Helper method to get a sublist of searches to be displayed onto a view.
     * @param model An object holding information to be displayed onto the view such as the list
     *              of searches.
     * @param page The page to request searches of.
     * @param user The user currently logged in.
     */
    private void setModelWithPaginatedContent(Model model, @RequestParam(value = "page")
            Optional<Integer> page, User user) {
        int defaultPage = 1;
        int currentPage = page.orElse(defaultPage);
        Page<Search> searches = searchService.getSearchesByUserId(user.getId(), currentPage);
        Pager pager = new Pager(searches.getTotalPages(), searches.getNumber());
        if(searches.getTotalElements() == 0) {
            model.addAttribute(LIST_KEY, null);
        } else {
            model.addAttribute(LIST_KEY, searches);
        }
        model.addAttribute(PAGER_KEY, pager);
        model.addAttribute(TOTAL_PAGES_KEY, searches.getTotalPages());
        model.addAttribute(CURRENT_PAGE_KEY, currentPage);
    }

    /**
     * This will take an address entered from the form, save the search, and display a list of
     * hourly forecasts.
     * @param search An object holding the search address entered from the form submission.
     * @throws MissingServletRequestParameterException Throws this exception if the address is
     * null.
     * @return Returns the path to view the hourly forecasts of this search.
     */
    @PostMapping(FORECAST_SEARCH_RESULT_MAPPING)
    public String processSearchForForecast(@ModelAttribute(ForecastController.SEARCH_PARAMETER)
                                                       SearchCommand search) throws MissingServletRequestParameterException {

        Search savedSearch = searchService.createSearch(search.getAddress());

        return REDIRECT + ForecastController.BASE_URL + ForecastController
                .HOURLY_FORECASTS_MAPPING + FIRST_PARAMETER_SEPARATOR + ForecastController
                .SEARCH_PARAMETER + PARAMETER_KEY_VALUE_SEPARATOR + savedSearch.getId();
    }
}
