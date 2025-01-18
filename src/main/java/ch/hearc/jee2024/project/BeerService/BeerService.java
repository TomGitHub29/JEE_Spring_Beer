package ch.hearc.jee2024.project.BeerService;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerService implements IBeerService {

    private final IRepositoryBeer beerRepository;

    public BeerService(@Qualifier("beerRepository") IRepositoryBeer beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public Beer createBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public List<Beer> getAllBeers() {
        return (List<Beer>) beerRepository.findAll();
    }

    @Override
    public Optional<Beer> getBeerById(int id) {
        return beerRepository.findById((long) id);
    }

    @Override
    public Beer updateBeer(int id, Beer beer) {
        Optional<Beer> beerToUpdate = beerRepository.findById((long) id);
        if (beerToUpdate.isPresent()) {
            Beer updatedBeer = beerToUpdate.get();
            updatedBeer.setName(beer.getName());
            updatedBeer.setType(beer.getType());
            updatedBeer.setPrice(beer.getPrice());
            return beerRepository.save(updatedBeer);
        } else {
            throw new IllegalArgumentException("Beer with ID " + id + " does not exist.");
        }
    }

    @Override
    public void deleteBeer(int id) {
        beerRepository.deleteById((long) id);
    }

    public List<Beer> getBeersByPriceLessThan(double price, int page, int size) {
        return beerRepository.findByPriceLessThan(price, PageRequest.of(page, size));
    }
}
