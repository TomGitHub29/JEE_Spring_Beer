package ch.hearc.jee2024.project.BeerService;

import ch.hearc.jee2024.project.IOC.Beer;

import java.util.List;
import java.util.Optional;

public interface IBeerService {
    Beer createBeer(Beer beer);
    List<Beer> getAllBeers();
    Optional<Beer> getBeerById(int id);
    Beer updateBeer(int id, Beer beer);
    void deleteBeer(int id);
}
