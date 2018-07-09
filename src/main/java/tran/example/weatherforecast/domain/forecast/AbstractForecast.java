package tran.example.weatherforecast.domain.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains fields that are common to both hourly and daily forecasts and default
 * method(s) which will be used by both of these domain objects.
 */
@Getter
@Setter
public abstract class AbstractForecast {
    /**
     * The summary of the forecast.
     */
    @JsonProperty(value = "summary")
    private String summary;
    /**
     * The time of this forecast object since Jan 1, 1970 in seconds.
     */
    @JsonProperty(value = "time")
    private long time;

    /**
     * An object to define how the date portion of the formattedTime string will appear.
     */
    protected SimpleDateFormat monthDayYear = new SimpleDateFormat("MMMMM d yyyy");
    /**
     * An object to define how the time portion of the formattedTime string will appear.
     */
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    /**
     * A factor to convert from seconds to milliseconds.
     */
    protected int secondsToMilliSecondsFactor = 1000;
    /**
     * A string to separate the date from the time.
     */
    protected String at = " at ";
    /**
     * The date of the forecast in a custom format defined by a helper method.
     */
    protected String formattedTime;

    /**
     * Uses the seconds from Jan 1st 1970 to convert this into a custom formatted string which
     * will display the date of the forecast object.
     */
    public void convertTime() {
        // Multiplied a factor of one thousand because it is in seconds when it is
        // obtained from the Darksky API.
        Date date = new Date(time * secondsToMilliSecondsFactor);
        formattedTime = monthDayYear.format(date) + at + timeFormat.format(date);
    }
}
