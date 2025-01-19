package ch.hearc.jee2024.project.BeerService;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import ch.hearc.jee2024.project.RepositoryManufacturer.IRepositoryManufacturer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Beer updateBeer(int id, Beer beerDetails) {
        Beer beer = beerRepository.findById((long)id).orElseThrow(() -> new IllegalArgumentException("Beer not found"));
        beer.setName(beerDetails.getName());
        beer.setType(beerDetails.getType());
        beer.setPrice(beerDetails.getPrice());
        return beerRepository.save(beer);
    }

    public void deleteBeer(int id) {
        Beer beer = beerRepository.findById((long)id).orElseThrow(() -> new IllegalArgumentException("Beer not found"));
        beerRepository.delete(beer);
    }
    public Page<Beer> getBeersByPriceLessThan(double price, int page, int size) {
        return beerRepository.findByPriceLessThan(price, PageRequest.of(page, size));
    }
}
