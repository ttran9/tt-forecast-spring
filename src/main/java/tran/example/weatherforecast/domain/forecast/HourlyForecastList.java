package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * An object holding the list of hourly forecasts and also a summary field.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"icon", "summary"})
public class HourlyForecastList {
    /**
     * A list of hourly forecasts.
     */
    @JsonProperty(value = "data")
    private List<HourlyForecast> hourlyForecasts;
}
