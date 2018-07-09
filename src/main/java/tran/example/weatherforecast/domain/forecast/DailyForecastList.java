package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Information about daily forecasts such as a list of objects or a summary of the daily forecasts.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"icon", "summary"})
public class DailyForecastList {
    /**
     * A summary of the daily forecasts.
     */
    private String summary;
    /**
     * A list of daily forecasts.
     */
    @JsonProperty(value = "data")
    private List<DailyForecast> dailyForecasts;
}
