package ch.hearc.jee2024.project.RepositoryManufacturer;

import ch.hearc.jee2024.project.IOC.Manufacturer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("manufacturerRepository")
public interface IRepositoryManufacturer extends CrudRepository<Manufacturer, Long> {
}
