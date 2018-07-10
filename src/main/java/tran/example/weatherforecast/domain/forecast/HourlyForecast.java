package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.Search;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * This holds data members for the hourly forecasts. Similar to the daily forecast implementation
 * there are many fields which are excluded in this application.
 */
@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = {"icon", "precipIntensity", "precipProbability",
        "precipType", "apparentTemperature", "dewPoint", "humidity", "pressure", "windSpeed",
        "windGust", "windBearing", "cloudCover", "uvIndex", "visibility", "ozone"})
public class HourlyForecast extends AbstractForecast {
    /**
     * The temperature for the current hour.
     */
    @JsonProperty(value = "temperature")
    private double temperature;
    /**
     * The search that contains this associated hourly forecast.
     */
    @ManyToOne
    private Search search;
}
