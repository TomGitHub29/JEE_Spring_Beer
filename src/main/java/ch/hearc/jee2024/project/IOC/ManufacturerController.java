package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ManufacturerService.ManufacturerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManu = manufacturerService.createManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManu);
    }

    @GetMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.getAllManufacturers();
    }
}
