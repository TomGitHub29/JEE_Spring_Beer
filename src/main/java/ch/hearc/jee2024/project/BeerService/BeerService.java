package ch.hearc.jee2024.project.BeerService;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BeerService implements IBeerService {

    private final IRepositoryBeer beerRepository;

    // Injection par constructeur
    public BeerService(@Qualifier("beerRepository") IRepositoryBeer beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public Beer createBeer(Beer beer) {
        beerRepository.save(beer);
        return beer;
    }

    @Override
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Optional<Beer> getBeerById(int id) {
        return this.beerRepository.findById((long) id);
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
}
