package tran.example.weatherforecast.commands;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The below tests will test the setter and getter methods for the object which binds the entered
 * address to a domain object.
 */
public class SearchCommandTest {

    /**
     * The object holding an address.
     */
    private SearchCommand searchCommand;

    @Before
    public void setUp() {
        searchCommand = new SearchCommand();
    }

    /**
     * Tests when the setter and getter method are both used.
     */
    @Test
    public void searchCommandWithAddress() {
        searchCommand.setAddress("test address");
        assertNotNull(searchCommand.getAddress());

    }

    /**
     * Tests when the getter is used to reference a null string.
     */
    @Test
    public void searchCommandWithoutAddress() {
        assertNull(searchCommand.getAddress());
    }

}