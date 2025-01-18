package ch.hearc.jee2024.project.ManufacturerService;

import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.RepositoryManufacturer.IRepositoryManufacturer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService implements IManufacturerService {
    private final IRepositoryManufacturer manufacturerRepository;

    public ManufacturerService(@Qualifier("manufacturerRepository") IRepositoryManufacturer manufacturerRepository
    ) {
        this.manufacturerRepository = manufacturerRepository;
    }


    @Override
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return (List<Manufacturer>) manufacturerRepository.findAll();
    }

    @Override
    public Optional<Manufacturer> getManufacturerById(int id) {
        return Optional.empty();
    }

    @Override
    public Manufacturer updateManufacturer(int id, Manufacturer manufacturer) {
        return null;
    }

    @Override
    public void deleteManufacturer(int id) {

    }
}
