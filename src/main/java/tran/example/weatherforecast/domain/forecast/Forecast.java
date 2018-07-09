package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * This object contains both the hourly and daily forecasts which will be returned when
 * attempting to retrieve the forecasts. From the API call we can also return a minute by minute
 * forecast but this is excluded in the @JsonIgnoreProperties annotation.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"timezone", "currently", "minutely", "alerts", "flags", "offset"})
public class Forecast {
    /**
     * The latitude at which the request was made for.
     */
    @JsonProperty(value = "latitude")
    private double latitude;
    /**
     * The longitude at which the request was made for.
     */
    @JsonProperty(value = "longitude")
    private double longitude;
    /**
     * An object holding the hourly forecasts.
     */
    @JsonProperty(value = "hourly")
    private HourlyForecastList hourlyForecastList;
    /**
     * An object holding the daily forecasts.
     */
    @JsonProperty(value = "daily")
    private DailyForecastList dailyForecastList;
}
