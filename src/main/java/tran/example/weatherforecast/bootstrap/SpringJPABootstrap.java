package tran.example.weatherforecast.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import tran.example.weatherforecast.domain.Search;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.domain.security.Role;
import tran.example.weatherforecast.services.RoleService;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.forecastservices.SearchService;

import java.util.List;

/**
 * This is a class that loads in default users and roles which can be used to view the forecasts
 * page which will require a user to be properly authenticated. There will be one user without
 * authorization that can be authenticated to demonstrate the important of the role granting the
 * proper permission/authorization.
 */
@Slf4j
@Component
@Profile({"default"})
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * A generic role that is required to view certain pages.
     */
    public static final String USER = "User";
    /**
     * Sample account/username with the required role to view certain pages such as the prior
     * searches made by a user.
     */
    public static final String MWESTON = "mweston";
    /**
     * Sample password (not a recommended password)
     */
    public static final String PASSWORD = "password";
    /**
     * A user that doesn't make searches but has a role.
     */
    public static final String TEST_ACCOUNT_USER_NAME = "testAccount";
    /**
     * The password for this test account
     */
    public static final String TEST_ACCOUNT_PASSWORD = "samplepw";
    /**
     * The address to make a sample search and forecast research for.
     */
    public static final String SAMPLE_ADDRESS = "1600 amphitheater pkwy";
    /**
     * A sample address create another sample search.
     */
    public static final String SAMPLE_ADDRESS_TWO = "2855 Stevens Creek Blvd";
    /**
     * A sample address used to create a search without an associated user.
     * This is used in the search service integration test.
     */
    public static final String STONERIDGE_MALL_RD_SAMPLE_ADDRESS = "1 Stoneridge Mall Rd";
    /**
     * An object allowing for interfacing with the data layer and the CustomUser table.
     */
    private UserService userService;
    /**
     * An object allowing for interfacing with the data layer and the Role table.
     */
    private RoleService roleService;
    /**
     * A service which allows requests to be made to the Google Geocoding and Darksky APIs and
     * the parsing of the results.
     */
    private SearchService searchService;

    /**
     * Wires up the userService to this class in order use necessary CRUD operations such as
     * creating a user.
     * @param userService The UserService implementation to be used at run time.
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Wires up the roleService to this class in order use necessary CRUD operations such as
     * creating a role.
     * @param roleService The RoleService implementation to be used at run time.
     */
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Wires up the searchService to this class in order use necessary CRUD operations such as
     * creating a search.
     * @param searchService The SearchService implementation to be used at run time.
     */
    @Autowired
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Responds to when the ApplicationContext gets refreshed or created.
     * @param contextRefreshedEvent An ApplicationContext event.
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadUsers();
        loadRoles();
        assignUsersToDefaultRole();
        createSampleSearch();
    }

    /**
     * Assigns the "CustomUser" role to all of the bootstrapped users.
     * There is also one user without a role that gets generated from this method.
     */
    private void assignUsersToDefaultRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<CustomUser> users = (List<CustomUser>) userService.listAll();

        users.forEach(user -> {
            roles.forEach(role -> {
                user.addRole(role);
                userService.saveOrUpdate(user);
            });
        });
        log.debug("Default roles have been assigned to users!");
    }

    /**
     * Helper method to create users.
     */
    public void loadUsers() {
        CustomUser userOne = new CustomUser();
        userOne.setUsername(MWESTON);
        userOne.setPassword(PASSWORD);

        CustomUser userTwo = new CustomUser();
        userTwo.setUsername(TEST_ACCOUNT_USER_NAME);
        userTwo.setPassword(TEST_ACCOUNT_PASSWORD);

        userService.saveOrUpdate(userOne);
        userService.saveOrUpdate(userTwo);

        log.debug("Test user accounts have been loaded!");
    }

    /**
     * Helper method to create roles.
     */
    private void loadRoles() {
        Role role = new Role();
        role.setRole(USER);
        roleService.saveOrUpdate(role);

        log.debug("Test roles have been loaded!");
    }

    /**
     * Helper method to make a request for the fore at 1600 amphitheater pkwy by the user "mweston".
     */
    private void createSampleSearch() {
        try {
            Search search = searchService.createSearch(SAMPLE_ADDRESS);
            searchService.saveSearch(search, 1L);

            Search secondSearch = searchService.createSearch(SAMPLE_ADDRESS_TWO);
            searchService.saveSearch(secondSearch, 1L);

            searchService.createSearch(STONERIDGE_MALL_RD_SAMPLE_ADDRESS);

            search = searchService.createSearch(SAMPLE_ADDRESS);
            searchService.saveSearch(search, 1L);

            search = searchService.createSearch(SAMPLE_ADDRESS);
            searchService.saveSearch(search, 1L);

            search = searchService.createSearch(SAMPLE_ADDRESS);
            searchService.saveSearch(search, 1L);

            search = searchService.createSearch(SAMPLE_ADDRESS);
            searchService.saveSearch(search, 1L);

            searchService.createSearch(null);
        } catch(MissingServletRequestParameterException exception) {
            log.debug("error while trying to create sample/bootstrapped searches!");
            exception.printStackTrace();
        }
    }

}
