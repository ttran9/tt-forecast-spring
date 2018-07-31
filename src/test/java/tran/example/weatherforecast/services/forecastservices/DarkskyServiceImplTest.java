package tran.example.weatherforecast.services.forecastservices;

import org.junit.Before;
import org.junit.Test;
import tran.example.weatherforecast.domain.forecast.DailyForecast;
import tran.example.weatherforecast.domain.forecast.Forecast;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        final int expectedValue = 0, firstIndex = 0;
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

        DailyForecast dailyForecast = forecast.getDailyForecastList().getDailyForecasts().get
                (firstIndex);
        HourlyForecast hourlyForecast = forecast.getHourlyForecastList().getHourlyForecasts().get
                (firstIndex);

        // perform extra validation.
        checkForDataMembers(dailyForecast, hourlyForecast);
    }

    /**
     * Helper method to check if other fields in the daily and hourly forecasts are not null and
     * if some have certain expected values.
     * @param dailyForecast The daily forecast with fields to be checked.
     * @param hourlyForecast The hourly forecast with fields to be checked.
     */
    private void checkForDataMembers(DailyForecast dailyForecast, HourlyForecast hourlyForecast) {
        final int expectedMillisecondsFactor = 1000;
        String at = " at ";

        assertNotNull(dailyForecast.getSummary());
        assertNotNull(dailyForecast.getTime());
        assertNotNull(dailyForecast.getTemperatureHighTime());
        assertNotNull(dailyForecast.getTemperatureLowTime());
        // the below are used to help format the date which is displayed on the daily forecast
        // graphs.
        assertNotNull(dailyForecast.getMonthDayYear());
        assertNotNull(dailyForecast.getTimeFormat());
        assertEquals(expectedMillisecondsFactor, dailyForecast.getSecondsToMilliSecondsFactor());
        assertEquals(at, dailyForecast.getAt());

        // check the fields for the hourly forecasts.
        assertNotNull(hourlyForecast.getSummary());
        assertNotNull(hourlyForecast.getTime());
        // the below are used to help format the date which is displayed on the daily forecast
        // graphs.
        assertNotNull(hourlyForecast.getMonthDayYear());
        assertNotNull(hourlyForecast.getTimeFormat());
        assertEquals(expectedMillisecondsFactor, hourlyForecast.getSecondsToMilliSecondsFactor());
        assertEquals(at, hourlyForecast.getAt());
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