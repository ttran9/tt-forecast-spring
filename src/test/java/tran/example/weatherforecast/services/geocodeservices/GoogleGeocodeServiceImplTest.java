package tran.example.weatherforecast.services.geocodeservices;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.MissingServletRequestParameterException;
import tran.example.weatherforecast.domain.geocode.Location;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the functionality of the service that leverages the Google Geocoding API to obtain the
 * latitude and longitude.
 */
public class GoogleGeocodeServiceImplTest {

    /**
     * Accesses the service that makes requests to the Google Geocoding API.
     */
    private GoogleGeocodeServiceImpl googleGeocodeService;
    public static final String ERROR_MESSAGE_KEY = "error_message";
    public static final String EXPECTED_ERROR_MESSAGE = "Invalid request. Missing the 'address', " +
            "'components', " +
            "'latlng' or 'place_id' parameter.";

    @Before
    public void setUp() throws Exception {
        googleGeocodeService = new GoogleGeocodeServiceImpl();
    }

    /**
     * Makes a request to the Google Geocoding API to get a JSON object which will be parsed to
     * obtain a latitude and longitude pair.
     * A sample latitude and longitude value will be expected for validity.
     * @throws IOException Throws an exception if the request cannot be made.
     * @throws MissingServletRequestParameterException Throws this exception if the address passed
     * into the getContent method is a null value.
     */
    @Test
    public void getLatitudeAndLongitude() throws IOException, MissingServletRequestParameterException {
        final String address = "1600 Amphitheatre Parkway";

        String content = googleGeocodeService.getContent(address);
        Location location = googleGeocodeService.getLatitudeAndLongitude(content);
        assertNotNull(location);
        assertNotNull(location.getLongitude());
        assertNotNull(location.getLatitude());
    }

    /**
     * This test will make the API request with a blank address which is expected to return content
     * but this will not contain the latitude and longitude but instead key/value pairs which
     * contain information about the error.
     * @throws IOException Throws an exception if the request cannot be made.
     * @throws MissingServletRequestParameterException Throws this exception if the address
     * passed into the getContent method is a null value.
     */
    @Test
    public void getContentWithBlankAddress() throws IOException,
            MissingServletRequestParameterException {
        String response = googleGeocodeService.getContent("");
        String errorMessage = googleGeocodeService.getErrorFromContent(response, ERROR_MESSAGE_KEY);
        assertEquals(EXPECTED_ERROR_MESSAGE, errorMessage);
    }

    /**
     * This test will make the API request with an erroneous address which is expected to return
     * no results but the service returns a default latitude and longitude.
     * @throws IOException Throws an exception if the request cannot be made.
     * @throws MissingServletRequestParameterException Throws this exception if the address
     * passed into the getContent method is a null value.
     */
    @Test
    public void getContentWithInvalidAddress() throws IOException,
            MissingServletRequestParameterException {
        double delta = 0.0;
        double expectedLatitude = 37.4224764;
        double expectedLongitude = -122.0842499;
        String response = googleGeocodeService.getContent("fdsfdsfsdfsdfds");
        Location location = googleGeocodeService.getLatitudeAndLongitude(response);
        assertNotNull(location);
        assertEquals(expectedLatitude, location.getLatitude(), delta);
        assertEquals(expectedLongitude, location.getLongitude(), delta);
    }
}