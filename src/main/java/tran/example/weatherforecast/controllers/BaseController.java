package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

/**
 * This class will contain methods used in all the controllers.
 */
@Slf4j
public abstract class BaseController {
    /**
     * The title attribute as part of the title tag on the views.
     */
    public static final String PAGE_ATTRIBUTE = "title";

    /**
     * Sets and adds the title attribute to the view's Model object.
     * @param model The UI object which will hold the title attribute.
     * @param title The title value (page name).
     */
    protected void addTitleAttribute(Model model, String title) {
        log.debug("adding in the title attribute!");
        model.addAttribute(PAGE_ATTRIBUTE, title);
    }
}
