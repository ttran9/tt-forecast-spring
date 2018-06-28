package tran.example.weatherforecast.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    public static final String INDEX_VIEW_NAME = "index";
    public static final String URL_PATH_SEPARATOR = "/";
    public static final String DENIED_VIEW_NAME = "access_denied";
    public static final String LOGIN_VIEW_NAME = "login";
    public static final String LOGIN_PAGE_MAPPING = "/signin";
    public static final String DENIED_PAGE_MAPPING = "/denied";
    public static final String PATH_MATCHER = "**";

    @RequestMapping(URL_PATH_SEPARATOR)
    public String getIndexPage() {
        return INDEX_VIEW_NAME;
    }

    @RequestMapping(DENIED_PAGE_MAPPING)
    public String getAccessDeniedPage() {
        return DENIED_VIEW_NAME;
    }

    @RequestMapping(LOGIN_PAGE_MAPPING)
    public String getLoginForm() {
        return LOGIN_VIEW_NAME;
    }
}
