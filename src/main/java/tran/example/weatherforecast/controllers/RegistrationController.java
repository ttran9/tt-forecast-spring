package tran.example.weatherforecast.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.registrationservices.RegistrationService;

import javax.validation.Valid;

/**
 * Holds mappings for displaying the registration page and processing a user registration.
 */
@Slf4j
@Controller
@RequestMapping({RegistrationController.BASE_URL})
public class RegistrationController extends ControllerHelper {
    /**
     * A common portion of the URL mapping for all registration requests.
     */
    public static final String BASE_URL = "/register";
    /**
     * Name of the registration view page.
     */
    public static final String REGISTRATION_PAGE_NAME = "register";
    /**
     * Directory that holds the registration related view(s).
     */
    public static final String REGISTRATION_DIRECTORY = "registration/";
    /**
     * The title of the registration page.
     */
    public static final String REGISTRATION_VIEW_TITLE = "Registration!";
    /**
     * Allows for the creation of users.
     */
    private final RegistrationService registrationService;
    /**
     * The name of the object to bind/transfer the input fields from the registration form.
     */
    public static final String registrationFormCommand = "registrationFormCommand";
    /**
     * Used to verify if the two passwords are identical.
     */
    private Validator passwordValidator;
    /**
     * A service used to verify if the user already exists before registering the user.
     */
    private UserService userService;

    @Autowired
    public RegistrationController(RegistrationService registrationService, @Qualifier
            ("registrationFormCommandPasswordValidator") Validator
            passwordValidator, UserService userService) {
        this.registrationService = registrationService;
        this.passwordValidator = passwordValidator;
        this.userService = userService;
    }

    /**
     * Handles the request to display the registration page and adds the title attribute.
     * @param registrationFormCommand An object to transfer/bind entered data values.
     * @param model An object holding attributes such as the title of the registration page.
     * @return Returns the path to the registration page.
     */
    @GetMapping
    public String getRegistrationPage(@ModelAttribute(RegistrationController.registrationFormCommand)
                                              RegistrationFormCommand registrationFormCommand,
                                      Model model) {
        log.debug("displaying registration page!");
        addTitleAttribute(model, REGISTRATION_VIEW_TITLE);
        return REGISTRATION_DIRECTORY + REGISTRATION_PAGE_NAME;
    }

    /**
     * Processes the attempt to create a user. If validation succeeds the user will be created
     * with an encrypted password and the user will be redirected to the home page.
     * @param registrationFormCommand An object to transfer/bind entered data values.
     * @param bindingResult An object which will hold errors if any.
     * @return Returns the path to the home page if the registration had no errors, if there were
     * errors the user then the path to the registration page.
     */
    @PostMapping
    public String processRegistration(@Valid @ModelAttribute(RegistrationController.registrationFormCommand)
                                              RegistrationFormCommand registrationFormCommand,
                                      BindingResult bindingResult) {
        log.debug("processing the registration from the controller!");
        passwordValidator.validate(registrationFormCommand, bindingResult);
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return REGISTRATION_DIRECTORY + REGISTRATION_PAGE_NAME;
        }
        if(registrationService.registerUser(registrationFormCommand) != null) {
            return SearchController.REDIRECT + IndexController.URL_PATH_SEPARATOR;
        } else {
            return displayRegistrationPageWithUserNameError(bindingResult);
        }
    }

    /**
     * This will add a user name error to an object which will be used on the registration page to
     * notify the user that the user name is taken.
     * @param bindingResult An object which will hold information about the user name error.
     * @return Returns the path to the registration page.
     */
    private String displayRegistrationPageWithUserNameError(BindingResult bindingResult) {
        log.debug("user name exists so the user will not be registered!");
        bindingResult.rejectValue("userName", "User name exists", "The user name is already " +
                "taken!");
        bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
        return REGISTRATION_DIRECTORY + REGISTRATION_PAGE_NAME;
    }
}
