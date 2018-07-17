package tran.example.weatherforecast.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tran.example.weatherforecast.domain.forecast.DailyForecast;

/**
 * This interface will allow for the retrieval of paginated daily forecasts.
 */
@Repository
public interface DailyForecastRepository extends PagingAndSortingRepository<DailyForecast, Long> {
    /**
     * Finds the daily forecasts of a search.
     * @param pageable An object containing information to paginate the results.
     * @param id The id of the search that daily forecasts are a part of.
     * @return Returns a paginated sublist by the search specified by the id.
     */
    Page<DailyForecast> findDailyForecastsBySearchId(Pageable pageable, Long id);
}
