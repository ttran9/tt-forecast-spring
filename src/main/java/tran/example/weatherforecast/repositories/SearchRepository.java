package tran.example.weatherforecast.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import tran.example.weatherforecast.domain.Search;

/**
 * This interface will allow access to get a list of paginated searches from the Search table.
 */
public interface SearchRepository extends PagingAndSortingRepository<Search, Long> {
    /**
     * Finds searches by a specific user.
     * @param pageable An object containing information to paginate the results.
     * @param id The id of the user the search is associated with.
     * @return Returns a paginated sublist by the user specified by the id.
     */
    Page<Search> findAllByUserId(Pageable pageable, Long id);
}
