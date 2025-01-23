package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ServiceBeer.BeerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/beers")
public class BeerController {
    private static final Logger LOGGER = Logger.getLogger(BeerController.class.getName());
    private final BeerService beerService;

    // ici on fait l'injection par constructeur, on peut faire autowired mais comme on a vu, c'est mieux cette méthode.
    public BeerController(@Qualifier("beerService")BeerService beerService) {
        this.beerService = beerService;
    }

    @Operation(summary = "Créer une nouvelle bière", description = "Ajoute une nouvelle bière à la base de données. Nécessite un compte admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bière créée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        try {
            Beer createdBeer = beerService.createBeer(beer);
            LOGGER.info("Created Beer: " + beer);
            long id = createdBeer.getId();
            // Construire l'URI pour l'accès à toutes les bières
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/"+id)
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);

            //  la bière créée avec l'URI dans le header
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(createdBeer);
        } catch (Exception e) {
            LOGGER.severe("Failed to create Beer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtenir toutes les bières", description = "Retourne la liste de toutes les bières disponibles dans le magasin.")
    @GetMapping
    public ResponseEntity<List<Beer>> getBeers() {
        try {
            List<Beer> beers = beerService.findAll();
            return new ResponseEntity<>(beers, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe("Failed to get beers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Rechercher des bières avec filtres", description = "Permet de rechercher des bières selon plusieurs critères et de paginer les résultats.")
    @GetMapping("/search")
    public ResponseEntity<Page<Beer>> getFilteredBeers(
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        try {
            Page<Beer> beers = beerService.getFilteredBeers(maxPrice, minStock, page, size, sortBy, direction);
            return ResponseEntity.ok(beers);
        } catch (Exception e) {
            LOGGER.severe("Failed to get filtered beers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtenir une bière par ID", description = "Retourne une bière spécifique en fonction de son identifiant unique.")
    @GetMapping("/{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable int id) {
        try {
            Optional<Beer> beer = beerService.getBeerById(id);
            return new ResponseEntity<>(beer.get(), HttpStatus.OK);
        }
        catch (Exception e) {
            LOGGER.severe("Failed to get beer by id: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour une bière", description = "Modifie les informations d'une bière existante. Nécessite un compte admin.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<Beer> updateBeer(@PathVariable int id, @RequestBody Beer beer) {
        try {
            Beer updatedBeer = beerService.updateBeer(id, beer);
            return new ResponseEntity<>(updatedBeer, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Failed to update beer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            LOGGER.severe("Failed to update beer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Supprimer une bière", description = "Supprime une bière de la base de données. Nécessite un compte admin.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Beer> deleteBeer(@PathVariable int id) {
        try {
            beerService.deleteBeer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (IllegalArgumentException e) {
            LOGGER.severe("Failed to delete beer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
