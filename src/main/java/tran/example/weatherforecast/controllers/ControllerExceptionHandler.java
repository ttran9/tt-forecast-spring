package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import tran.example.weatherforecast.exceptions.NotFoundException;

/**
 * This is a global exception handler for the controllers which will redirect the user to a more
 * informative error page instead of a generic 4xx or 5xx error page.
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    /**
     * @param exception An object containing information about the bad request such as trying to
     *                  find a non-existent user.
     * @return An object that returns the exception and a view (page).
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleBadRequest(Exception exception) {
        log.error("Handling not found exception!");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView(IndexController.NOT_FOUND_VIEW_NAME);
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
