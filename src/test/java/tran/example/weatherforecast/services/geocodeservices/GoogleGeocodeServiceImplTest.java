package tran.example.weatherforecast.services.geocodeservices;

import org.junit.Before;
import org.junit.Test;
import tran.example.weatherforecast.domain.geocode.Location;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the service that leverages the Google Geocoding API to obtain the
 * latitude and longitude.
 */
public class GoogleGeocodeServiceImplTest {

    /**
     * Accesses the service that makes requests to the Google Geocoding API.
     */
    private GoogleGeocodeServiceImpl googleGeocodeService;

    @Before
    public void setUp() throws Exception {
        googleGeocodeService = new GoogleGeocodeServiceImpl();
    }

    /**
     * Makes a request to the Google Geocoding API to get a JSON object which will be parsed to
     * obtain a latitude and longitude pair.
     * A sample latitude and longitude value will be expected for validity.
     * @throws IOException Throws an exception if the request cannot be made.
     */
    @Test
    public void getLatitudeAndLongitude() throws IOException {
        final String address = "1600 Amphitheatre Parkway";

        final double expectedLatitude = 37.4215421;
        final double expectedLongitude = -122.0840106;
        final double delta = 0.0;
        final int expectedValue = 0;

        String content = googleGeocodeService.getContent(address);
        Location location = googleGeocodeService.getLatitudeAndLongitude(content);

        assertEquals(expectedValue, Math.abs(expectedLatitude - location.getLatitude()), delta);
        assertEquals(expectedValue, Math.abs(expectedLongitude - location.getLongitude()), delta);
    }

    /**
     * This test will make the API request without a required parameter which is expected to
     * return content but this will not contain the latitude and longitude but instead key/value
     * pairs which contain information about the error.
     * @throws IOException Throws an exception if the request cannot be made.
     */
    @Test
    public void getContent() throws IOException {
        String errorMessageKey = "error_message";
        String expectedErrorMessage = "Invalid request. Missing the 'address', 'components', " +
                "'latlng' or 'place_id' parameter.";
        String response = googleGeocodeService.getContent("");
        String errorMessage = googleGeocodeService.getErrorFromContent(response, errorMessageKey);
        assertEquals(expectedErrorMessage, errorMessage);
    }
}