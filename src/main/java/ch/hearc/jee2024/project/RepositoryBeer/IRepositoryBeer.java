package ch.hearc.jee2024.project.RepositoryBeer;

import ch.hearc.jee2024.project.IOC.Beer;

import java.util.List;
import java.util.Optional;

// Déclaration de l'interface IRepositoryBeer
public interface IRepositoryBeer {
    Beer save(Beer beer);
    List<Beer> findAll();
    Optional<Beer> findById(Long id);
}
