package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ServiceManufacturer.ManufacturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManu = manufacturerService.createManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManu);
    }

    @GetMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.getAllManufacturers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(Math.toIntExact(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(@PathVariable Long id, @RequestBody Manufacturer manufacturer) {
        try {
            Manufacturer updatedManufacturer = manufacturerService.updateManufacturer(Math.toIntExact(id), manufacturer);
            return ResponseEntity.ok(updatedManufacturer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
       try {
           Manufacturer manufacturer = manufacturerService.getManufacturerById(Math.toIntExact(id)).orElseThrow();
           return ResponseEntity.ok(manufacturer);
       } catch (Exception e) {
           return ResponseEntity.notFound().build();
       }
    }
}
