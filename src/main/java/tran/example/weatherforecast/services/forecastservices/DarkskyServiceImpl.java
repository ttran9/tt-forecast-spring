package tran.example.weatherforecast.services.forecastservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.forecast.Forecast;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;
import tran.example.weatherforecast.services.ApiService;

import java.io.IOException;

/**
 * Implements methods to get daily and hourly forecasts from the Darksky API.
 */
@Slf4j
@Service
public class DarkskyServiceImpl extends ApiService implements DarkskyService {

    /**
     * The URL of the Darksky API which requests will be made to.
     */
    public final static String DARKSKY_URL = "https://api.darksky.net/forecast/";

    /**
     * This calls a parent helper method with a specified URL that contains the URL, the API key,
     * the latitude, and longitude to return JSON content which will hold the daily and hourly
     * forecasts.
     * @param latitude The latitude for the location to request forecasts for.
     * @param longitude The longitude for the location to request forecasts for.
     * @return Returns a JSON string containing the forecasts (daily and hourly).
     * @throws IOException Throws an exception if the request cannot be made (not if the request
     * does not return valid data).
     */
    @Override
    public String getContent(String latitude, String longitude) throws IOException {
        log.debug("Making a request to get forecasts from the Darksky API!");
        String darkskyApiKeyVariableName = "DARK_SKY_KEY";
        String darkskyApiKey = System.getenv(darkskyApiKeyVariableName);
        String url = DARKSKY_URL + darkskyApiKey + "/" + latitude + "," + longitude;
        return getData(url);
    }

    /**
     * This method implementation parses the JSON content from making the GET request to the
     * Darksky API to return an object which has the list of daily and hourly forecasts.
     * @param content The JSON content (consisting of forecasts) from making the request to the
     *                Darksky API.
     * @return Returns a Forecast domain object which holds a list of daily and hourly forecast
     * objects.
     * @throws IOException Throws an IOException when unable to convert the JSON content to a
     * domain object using Jackson.
     */
    @Override
    public Forecast getForecasts(String content) throws IOException {
        log.debug("Parsing the forecast information from the Darksky API!");
        ObjectMapper objectMapper = new ObjectMapper();
        Forecast results = objectMapper.readValue(content, Forecast.class);
        results.getHourlyForecastList().getHourlyForecasts().forEach(HourlyForecast::convertTime);
        results.getDailyForecastList().getDailyForecasts().forEach(dailyForecast -> {
            dailyForecast.convertTime();
            dailyForecast.convertTemperatureHighTime();
            dailyForecast.convertTemperatureLowTime();
        });
        return results;
    }
}
