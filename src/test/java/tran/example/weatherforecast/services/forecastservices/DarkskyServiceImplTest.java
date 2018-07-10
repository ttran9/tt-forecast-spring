package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import tran.example.weatherforecast.domain.forecast.Forecast;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the service that leverages the Darksky API to get forecasts.
 */
public class DarkskyServiceImplTest {

    /**
     * Accesses the service makes HTTP get requests to the Darksky API.
     */
    private DarkskyServiceImpl darkskyService;

    @Before
    public void setUp() {
        darkskyService = new DarkskyServiceImpl();
    }

    /**
     * This will get the forecasts for a specified latitude and longitude pair and we will test
     * if there are expected objects such as a certain number of hourly forecasts, daily
     * forecasts, and if we can retrieve back the latitude and longitude pair we originally used
     * in the request.
     * @throws IOException Throws an exception if the request cannot be made.
     */
    @Test
    public void getForecasts() throws IOException {
        final String latitude = "37.8267";
        final String longitude = "-122.4233";
        final double expectedLatitude = 37.8267;
        final double expectedLongitude = -122.4233;
        final double delta = 0.0;
        final int expectedValue = 0;
        final int expectedNumberOfHourlyForecasts = 49;
        final int expectedNumberOfDailyForecasts = 8;

        String content = darkskyService.getContent(latitude, longitude);
        Forecast forecast = darkskyService.getForecasts(content);

        assertEquals(expectedValue, Math.abs(expectedLatitude - forecast.getLatitude()), delta);
        assertEquals(expectedValue, Math.abs(expectedLongitude - forecast.getLongitude()), delta);
        assertEquals(expectedNumberOfHourlyForecasts, forecast.getHourlyForecastList()
                .getHourlyForecasts().size());
        assertEquals(expectedNumberOfDailyForecasts, forecast.getDailyForecastList()
                .getDailyForecasts().size());
    }

    /**
     * This test will test the contents of the response which throws information about the error
     * because the latitude and longitude values are invalid (in this case they are blank).
     * @throws IOException Throws an exception if the request cannot be made.
     */
    @Test
    public void getContentFailure() throws IOException {
        String errorKey = "error";
        String expectedErrorMessage = "The given location (or time) is invalid.";
        String response = darkskyService.getContent("", "");
        String errorMessage = darkskyService.getErrorFromContent(response, errorKey);
        assertEquals(expectedErrorMessage, errorMessage);
    }
}