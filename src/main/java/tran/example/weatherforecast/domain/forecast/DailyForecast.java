package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.Search;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * This is a class which holds the fields for the daily forecasts. Note there are many properties
 * that are ignored but some of these may later be included.
 */
@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = {"icon", "sunriseTime", "sunsetTime", "moonPhase", "precipIntensity",
        "precipIntensityMax", "precipIntensityMaxTime", "precipProbability", "precipType",
        "apparentTemperatureHigh", "apparentTemperatureHighTime", "apparentTemperatureLow",
        "apparentTemperatureLowTime", "dewPoint", "humidity", "pressure", "windSpeed", "windGust",
        "windGustTime", "windBearing", "cloudCover", "uvIndex", "uvIndexTime", "visibility",
        "ozone", "apparentTemperatureMin", "apparentTemperatureMinTime", "apparentTemperatureMax",
        "apparentTemperatureMaxTime", "temperatureMin", "temperatureMinTime", "temperatureMax",
        "temperatureMaxTime"})
public class DailyForecast extends AbstractForecast {
    /**
     * The high temperature for the day.
     */
    @JsonProperty(value = "temperatureHigh")
    private double temperatureHigh;
    /**
     * The time in seconds from Jan 1st, 1970 of the high temperature.
     */
    @JsonProperty(value = "temperatureHighTime")
    private long temperatureHighTime;
    /**
     * The low temperature for the day
     */
    @JsonProperty(value = "temperatureLow")
    private double temperatureLow;
    /**
     * The time in seconds from Jan 1st, 1970 of the low temperature.
     */
    @JsonProperty(value = "temperatureLowTime")
    private long temperatureLowTime;

    /**
     * The date of the high temperature in a custom format defined by a helper method.
     */
    private String formattedTemperatureHighTime;
    /**
     * The date of the low temperature in a custom format defined by a helper method.
     */
    private String formattedTemperatureLowTime;
    /**
     * The search that contains this associated daily forecast.
     */
    @ManyToOne
    private Search search;

    /**
     * Uses the seconds from Jan 1st 1970 to convert this into a custom formatted string which
     * will display the date of the highest temperature.
     */
    public void convertTemperatureHighTime() {
        Date date = new Date(temperatureHighTime * secondsToMilliSecondsFactor);
        formattedTemperatureHighTime = monthDayYear.format(date) + at + timeFormat.format(date);
    }

    /**
     * Uses the seconds from Jan 1st 1970 to convert this into a custom formatted string which
     * will display the date of the lowest object.
     */
    public void convertTemperatureLowTime() {
        Date date = new Date(temperatureLowTime * secondsToMilliSecondsFactor);
        formattedTemperatureLowTime = monthDayYear.format(date) + at + timeFormat.format(date);
    }
}
