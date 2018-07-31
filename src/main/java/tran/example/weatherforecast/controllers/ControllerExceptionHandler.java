package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import tran.example.weatherforecast.commands.SearchCommand;
import tran.example.weatherforecast.exceptions.ImproperParamException;
import tran.example.weatherforecast.exceptions.NotFoundException;

/**
 * This is a global exception handler for the controllers which will redirect the user to a more
 * informative error page instead of a generic 4xx or 5xx error page.
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    /**
     * The exception attribute name.
     */
    public static final String EXCEPTION_KEY = "exception";
    /**
     * This occurs when there is an exception when attempting to retrieve a domain object
     * that cannot be retrieved from the database.
     * This also handles the case where the user enters an improper parameter when attempting to
     * find a resource.
     * @param exception An object containing information about the bad request such as trying to
     *                  find a non-existent user.
     * @return An object that returns the exception and a view (page).
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundException.class, NumberFormatException.class})
    public ModelAndView handleBadRequest(Exception exception) {
        log.error("Handling not found exception!");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView(IndexController.NOT_FOUND_VIEW_NAME);
        modelAndView.addObject(EXCEPTION_KEY, exception);
        return modelAndView;
    }

    /**
     * This occurs when the user fails to specify a required parameter when viewing certain pages.
     * @param exception The object with information about the exception.
     * @return Returns an object that holds a view (the home page) and the object containing
     * information about the error.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException exception) {
        log.error("Handling required missing parameter exception!");
        return populateModelAndViewWithInformation(exception, IndexController.INDEX_VIEW_NAME);
    }

    /**
     * Helper method to populate the ModelAndView object with the view name to be returned and
     * an object that contains information about the exception.
     * @param exception An object holding specific information (a message) about the error for
     *                  the user to see.
     * @return Returns an object that holds the view name and information about the error.
     */
    private ModelAndView populateModelAndViewWithInformation(Exception exception, String viewName) {
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("search", new SearchCommand());
        modelAndView.addObject(EXCEPTION_KEY, exception);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ImproperParamException.class)
    public ModelAndView handleImproperParam(ImproperParamException exception) {
        log.error("Handling when parameter entered is invalid!");
        return populateModelAndViewWithInformation(exception, IndexController.INDEX_VIEW_NAME);
    }


}
