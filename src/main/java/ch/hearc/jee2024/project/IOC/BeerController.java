package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.BeerService.BeerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
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

    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerService.getAllBeers();
        return new ResponseEntity<>(beers, HttpStatus.OK);
    }

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

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Beer> deleteBeer(@PathVariable int id) {
        try {
            beerService.deleteBeer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (IllegalArgumentException e) {
            LOGGER.severe("Failed to delete beer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/price")
    public ResponseEntity<Page<Beer>> getBeersByPrice(
            @RequestParam double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Beer> beers = (Page<Beer>) beerService.getBeersByPriceLessThan(maxPrice, page, size);
            return new ResponseEntity<>(beers, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe("Failed to get beers by price: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
