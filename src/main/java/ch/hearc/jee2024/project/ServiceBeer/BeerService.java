package ch.hearc.jee2024.project.ServiceBeer;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import ch.hearc.jee2024.project.RepositoryManufacturer.IRepositoryManufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerService implements IBeerService {

    private final IRepositoryBeer beerRepository;
    private final IRepositoryManufacturer manufacturerRepository;

    public BeerService(IRepositoryBeer beerRepository, IRepositoryManufacturer manufacturerRepository) {
        this.beerRepository = beerRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Beer createBeer(Beer beer) {
        if (beer.getManufacturer() != null && beer.getManufacturer().getId() == null) {
            Manufacturer manufacturer = manufacturerRepository.save(beer.getManufacturer());
            beer.setManufacturer(manufacturer);
        }
        return beerRepository.save(beer);
    }

    @Override
    public List<Beer> findAll() {
        return (List<Beer>) beerRepository.findAll();
    }

    @Override
    public Optional<Beer> getBeerById(int id) {
        return beerRepository.findById((long) id);
    }

    @Override
    public Beer updateBeer(int id, Beer beerDetails) {
        Beer beer = beerRepository.findById((long)id).orElseThrow(() -> new IllegalArgumentException("Beer not found"));
        beer.setName(beerDetails.getName());
        beer.setType(beerDetails.getType());
        beer.setPrice(beerDetails.getPrice());
        beer.setStock(beerDetails.getStock());
        beer.setManufacturer(beerDetails.getManufacturer());
        return beerRepository.save(beer);
    }

    @Override
    public void deleteBeer(int id) {
        Beer beer = beerRepository.findById((long)id).orElseThrow(() -> new IllegalArgumentException("Beer not found"));
        beerRepository.delete(beer);
    }


    public Page<Beer> getFilteredBeers(Integer maxPrice, Integer minStock, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if (direction.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // Cas 1 : Filtre sur le prix uniquement
        if (maxPrice != null && minStock == null) {
            return beerRepository.findByPriceLessThan(maxPrice, pageable);
        }
        // Cas 2 : Filtre sur le stock uniquement
        if (maxPrice == null && minStock != null) {
            return beerRepository.findByStockGreaterThan(minStock, pageable);
        }
        // Cas 3 : Filtre sur prix ET stock
        if (maxPrice != null && minStock != null) {
            return beerRepository.findByPriceLessThanAndStockGreaterThan(maxPrice, minStock, pageable);
        }
        // Cas 4 : Pas de filtre → juste tri et pagination
        return beerRepository.findAll(pageable);
    }

}
