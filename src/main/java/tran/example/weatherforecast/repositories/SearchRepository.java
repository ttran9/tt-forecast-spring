package tran.example.weatherforecast.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import tran.example.weatherforecast.domain.Search;

public interface SearchRepository extends PagingAndSortingRepository<Search, Long> {
}
