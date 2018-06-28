package tran.example.weatherforecast.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@RequestMapping({ForecastController.BASE_URL})
public class ForecastController {

    public static final String USER_SEARCH_VIEW_NAME = "searches";
    public static final String BASE_URL = "/forecast";
    public static final String BASE_VIEW_URL_RETURN = "forecast";

    @RequestMapping(IndexController.URL_PATH_SEPARATOR + USER_SEARCH_VIEW_NAME)
    public String getSearchesPage() {
        return BASE_VIEW_URL_RETURN + IndexController.URL_PATH_SEPARATOR + USER_SEARCH_VIEW_NAME;
    }
}
