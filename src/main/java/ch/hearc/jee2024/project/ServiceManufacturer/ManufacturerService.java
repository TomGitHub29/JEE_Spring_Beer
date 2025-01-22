package ch.hearc.jee2024.project.ServiceManufacturer;

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
    public Manufacturer updateManufacturer(int id, Manufacturer manufacturer) {
        Optional<Manufacturer> manufacturerToUpdate = manufacturerRepository.findById((long) id);
        if (manufacturerToUpdate.isPresent()) {
            Manufacturer updatedManufacturer = manufacturerToUpdate.get();
            updatedManufacturer.setName(manufacturer.getName());
            return manufacturerRepository.save(updatedManufacturer);
        } else {
            throw new IllegalArgumentException("Manufacturer with ID " + id + " does not exist.");
        }
    }

    @Override
    public void deleteManufacturer(int id) {
        try {
            this.manufacturerRepository.deleteById((long) id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting manufacturer with ID " + id, e);
        }
    }

    @Override
    public Optional<Manufacturer> getManufacturerById(int id) {
        try {
            return manufacturerRepository.findById((long) id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving manufacturer with ID " + id, e);
        }    }


}
