package tran.example.weatherforecast.services.geocodeservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.geocode.GeocodeResult;
import tran.example.weatherforecast.domain.geocode.Geometry;
import tran.example.weatherforecast.domain.geocode.Location;
import tran.example.weatherforecast.domain.geocode.Result;
import tran.example.weatherforecast.services.ApiService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Implements methods to get latitude and longitude from an entered address from the Google
 * Geocoding API.
 */
@Service
@Slf4j
public class GoogleGeocodeServiceImpl extends ApiService implements GoogleGeocodeService {

    private final static String GEOCODE_URL = "https://maps.googleapis" +
            ".com/maps/api/geocode/json?address=";
    /**
     * Calls a parent method with a properly formated URL such as the necessary API key and the
     * address being URL encoded to obtain a JSON object containing the latitude and longitude.
     * @param address The address to be used in the request to get location information.
     * @return Returns a JSON object containing key/value pairs including the latitude and
     * longitude values.
     * @throws IOException Throws an IOException if there is an error while trying to make the
     * API request.
     */
    @Override
    public String getContent(String address) throws IOException {
        log.debug("Making a GET request to get the latitude and longitude from the Geocoding API!");
        String encodeScheme = "UTF-8";
        String geocodeUrlKeyParam = "&key=";
        String geocodeUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String urlEncodedAddress = URLEncoder.encode(address, encodeScheme);
        String geocodeApiKey = System.getenv("GOOGLE_MAPS_GC_KEY");
        String url = geocodeUrl + urlEncodedAddress + geocodeUrlKeyParam + geocodeApiKey;
        return getData(url);
    }

    /**
     * Parses the JSON content to obtain the latitude and longitude from domain objects.
     * @param content The JSON content (unparsed) which holds the latitude and longitude.
     * @return A domain object containing the latitude and longitude.
     * @throws IOException Throws an IOException when unable to convert the JSON content to a
     * domain object using Jackson.
     */
    @Override
    public Location getLatitudeAndLongitude(String content) throws IOException {
        log.debug("Parsing the information from the Geocoding API!");
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResult results = objectMapper.readValue(content, GeocodeResult.class);
        List<Result> resultsList = results.getResults();
        if(resultsList.size() == 1) {
            Result result = resultsList.get(0);
            Geometry geometry = result.getGeometry();
            return geometry.getLocation();
        } else {
            /**
             * if there is an improper return just return the latitude and longitude for the
             * address of 1600 Amphitheatre Pkwy
             */
            Location location = new Location();
            location.setLatitude(37.4215421);
            location.setLongitude(-122.0840106);
            return location;
        }
    }
}
