package tran.example.weatherforecast.services.geocodeservices;

import org.springframework.web.bind.MissingServletRequestParameterException;
import tran.example.weatherforecast.domain.geocode.Location;

import java.io.IOException;

/**
 * Declares methods to get content from the Google Geocoding API and parses the results.
 */
public interface GoogleGeocodeService {

    /**
     * Makes a GET request to the Google Geocoding API to return a JSON string which contains an
     * object that has the location of the address specified.
     * @param address The address to be used in the request to get location information.
     * @return Returns a JSON string which holds a variety of key/value pairs which contain
     * the location.
     * @throws IOException Throws an IOException if there is an error while trying to make the
     * API request.
     * @throws MissingServletRequestParameterException Throws this exception if the address is
     * null (wasn't provided from the controller or in the tests).
     */
    String getContent(String address) throws IOException, MissingServletRequestParameterException;

    /**
     * Parses the JSON content to get an object which consists of the latitude and longitude.
     * @param content The JSON content (unparsed) which holds the latitude and longitude.
     * @return Returns a domain object which contains the latitude and longitude fields.
     * @throws IOException Throws an IOException when unable to convert the JSON content to a
     * domain object using Jackson.
     */
    Location getLatitudeAndLongitude(String content) throws IOException;
}
