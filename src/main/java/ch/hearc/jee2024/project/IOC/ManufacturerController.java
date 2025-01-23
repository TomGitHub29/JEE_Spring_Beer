package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ServiceManufacturer.ManufacturerService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Créer un fabricant", description = "Ajoute un fabricant de bière à la base de données. Nécessite un compte admin.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        Manufacturer savedManu = manufacturerService.createManufacturer(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManu);
    }
    @Operation(summary = "Obtenir la liste des fabricants", description = "Retourne tous les fabricants de bières enregistrés.")
    @GetMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerService.getAllManufacturers();
    }

    @Operation(summary = "Supprimer un fabricant", description = "Supprime un fabricant de bière de la base de données. Nécessite un compte admin.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(Math.toIntExact(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mettre à jour un fabricant", description = "Met à jour un fabricant de bière dans la base de données. Nécessite un compte admin.")
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

    @Operation(summary = "Obtenir un fabricant", description = "Retourne un fabricant de bière enregistré.")
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
