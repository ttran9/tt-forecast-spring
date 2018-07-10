package tran.example.weatherforecast.domain;

import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.forecast.DailyForecast;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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


}
