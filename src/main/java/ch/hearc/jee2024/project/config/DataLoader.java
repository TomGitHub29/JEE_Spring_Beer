package ch.hearc.jee2024.project.config;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import ch.hearc.jee2024.project.RepositoryManufacturer.IRepositoryManufacturer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final IRepositoryBeer beerRepository;
    private final IRepositoryManufacturer manufacturerRepository;

    public DataLoader(IRepositoryBeer beerRepository, IRepositoryManufacturer manufacturerRepository) {
        this.beerRepository = beerRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @PostConstruct
    public void loadData() {
        if (manufacturerRepository.count() == 0) {
            Manufacturer manufacturer = new Manufacturer("Test Brewery");
            manufacturer = manufacturerRepository.save(manufacturer);

            beerRepository.save(new Beer("IPA", "Blonde", 5.0, 10, manufacturer));
            beerRepository.save(new Beer("Stout", "Dark", 6.5, 10, manufacturer));
            beerRepository.save(new Beer("Pilsner", "Blonde", 4.5, 10, manufacturer));
        }
    }
}
