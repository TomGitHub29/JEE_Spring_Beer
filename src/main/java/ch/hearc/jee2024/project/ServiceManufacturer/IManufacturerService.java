package ch.hearc.jee2024.project.ServiceManufacturer;

import ch.hearc.jee2024.project.IOC.Manufacturer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IManufacturerService {
    Manufacturer createManufacturer(Manufacturer manufacturer);
    List<Manufacturer> getAllManufacturers();
    Optional<Manufacturer> getManufacturerById(int id);
    Manufacturer updateManufacturer(int id, Manufacturer manufacturer);
    void deleteManufacturer(int id);
}
