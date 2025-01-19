package ch.hearc.jee2024.project.RepositoryBeer;

import ch.hearc.jee2024.project.IOC.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("beerRepository")
public interface IRepositoryBeer extends PagingAndSortingRepository<Beer, Long>, CrudRepository<Beer, Long> {

    Page<Beer> findByPriceLessThan(double price, Pageable pageable);

    Page<Beer> findByStockGreaterThan(int stock, Pageable pageable);

    Page<Beer> findByPriceLessThanAndStockGreaterThan(double price, int stock, Pageable pageable);
}
