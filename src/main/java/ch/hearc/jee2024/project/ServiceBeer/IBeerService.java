package ch.hearc.jee2024.project.ServiceBeer;

import ch.hearc.jee2024.project.IOC.Beer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IBeerService {
    Beer createBeer(Beer beer);

    List<Beer> findAll();

    Optional<Beer> getBeerById(int id);

    Beer updateBeer(int id, Beer beer);

    void deleteBeer(int id);

}