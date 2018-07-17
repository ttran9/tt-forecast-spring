package tran.example.weatherforecast.domain;

import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.forecast.DailyForecast;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a search that is only saved when the user is logged in with a role.
 */
@Getter
@Setter
@Entity
public class Search extends AbstractDomainClass {
    /**
     * Searches associated with a user. A user can have more than one search made. A search can
     * only be made to one user. Cascading operations are not needed here.
     */
    @ManyToOne
    private User user;
    /**
     * The entered address while making this search
     */
    private String address;
    /**
     * A list of the hourly forecasts associated with this search.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "search")
    private List<HourlyForecast> hourlyForecasts = new ArrayList<>();
    /**
     * A list of the daily forecasts associated with this search.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "search")
    private List<DailyForecast> dailyForecasts = new ArrayList<>();
    /**
     * A string holding when this object was created (when the search was made).
     */
    private String formattedDateSearch;
    /**
     * An object to define how the date portion of the formattedTime string will appear.
     */
    @Transient
    protected SimpleDateFormat monthDayYear = new SimpleDateFormat("MMMMM d yyyy");
    /**
     * An object to define how the time portion of the formattedTime string will appear.
     */
    @Transient
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    /**
     * A string to separate the date from the time.
     */
    @Transient
    protected String at = " at ";

    /**
     * Method to convert the creation timestamp to a formatted string which will display when
     * this search was made.
     */
    public void setFormattedDateSearch() {
        // Multiplied a factor of one thousand because it is in seconds when it is
        // obtained from the Darksky API.
        formattedDateSearch = monthDayYear.format(dateCreated) + at + timeFormat.format(dateCreated);
    }


}
