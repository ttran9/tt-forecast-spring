package tran.example.weatherforecast.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tran.example.weatherforecast.domain.forecast.HourlyForecast;

/**
 * This interface will allow for the retrieval of paginated hourly forecasts.
 */
@Repository
public interface HourlyForecastRepository extends PagingAndSortingRepository<HourlyForecast, Long> {
    /**
     * Finds the hourly forecasts of a search.
     * @param pageable An object containing information to paginate the results.
     * @param id The id of the search that hourly forecasts are a part of.
     * @return Returns a paginated sublist by the search specified by the id.
     */
    Page<HourlyForecast> findHourlyForecastsBySearchId(Pageable pageable, Long id);
}
