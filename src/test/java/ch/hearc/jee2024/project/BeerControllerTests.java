package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class BeerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllBeersShouldReturnListOfBeers() throws Exception {
        Manufacturer manufacturer = new Manufacturer("He-Arc Brewing");
        String manufacturerJson = objectMapper.writeValueAsString(manufacturer);

        String manufacturerResponse = this.mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Manufacturer savedManufacturer = objectMapper.readValue(manufacturerResponse, Manufacturer.class);

        // Création de plusieurs bières
        Beer beer1 = new Beer("Lager", "Blonde", 4.5, savedManufacturer);
        Beer beer2 = new Beer("Stout", "Dark", 6.0, savedManufacturer);

        this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer1)))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer2)))
                .andExpect(status().isCreated());

        // Vérification que la liste contient bien les 2 bières
        this.mockMvc.perform(get("/beers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }



    @Test
    public void createBeerShouldReturnCreatedBeer() throws Exception {
        // persiste un Manufacturer en base de données -> Probleme vue en classe.
        Manufacturer manufacturer = new Manufacturer("Feldschlösschen");
        String manufacturerJson = objectMapper.writeValueAsString(manufacturer);

        String manufacturerResponse = this.mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Manufacturer savedManufacturer = objectMapper.readValue(manufacturerResponse, Manufacturer.class);

        // Beer avec le Manufacturer persisté
        Beer beer = new Beer( "BeerName", "BeerType", 5.0, savedManufacturer);
        String beerJson = objectMapper.writeValueAsString(beer);

        this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void updateBeerShouldReturnUpdatedBeer() throws Exception {
        // persiste un Manufacturer en base de données -> Probleme vue en classe.
        Manufacturer manufacturer = new Manufacturer("Feldschlösschen");
        String manufacturerJson = objectMapper.writeValueAsString(manufacturer);

        String manufacturerResponse = this.mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Manufacturer savedManufacturer = objectMapper.readValue(manufacturerResponse, Manufacturer.class);

        // Beer avec le Manufacturer persisté
        Beer beer = new Beer("Test2", "Blonde", 5.0, savedManufacturer);
        String beerJson = objectMapper.writeValueAsString(beer);

        String beerResponse = this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Beer savedBeer = objectMapper.readValue(beerResponse, Beer.class);

        // on test de find la beer par id et on la modifie
        Beer beerUpdated = new Beer("Test2", "Blonde", 6.0, savedManufacturer);
        String beerUpdatedJson = objectMapper.writeValueAsString(beerUpdated);

        this.mockMvc.perform(put("/beers/" + savedBeer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerUpdatedJson))
                .andExpect(status().isOk());
    }


    @Test
    public void deleteBeerShouldRemoveBeer() throws Exception {
        Manufacturer manufacturer = new Manufacturer("Test Brewery");
        String manufacturerJson = objectMapper.writeValueAsString(manufacturer);

        String manufacturerResponse = this.mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Manufacturer savedManufacturer = objectMapper.readValue(manufacturerResponse, Manufacturer.class);

        Beer beer = new Beer("IPA", "Hoppy", 6.5, savedManufacturer);
        String beerJson = objectMapper.writeValueAsString(beer);

        String beerResponse = this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Beer savedBeer = objectMapper.readValue(beerResponse, Beer.class);

        // Vérifie que l'ID est bien généré
        assert savedBeer.getId() != null;

        this.mockMvc.perform(delete("/beers/" + savedBeer.getId()))
                .andExpect(status().isNoContent());

        // Vérifie que la bière est bien supprimée
        this.mockMvc.perform(get("/beers/" + savedBeer.getId()))
                .andExpect(status().isNotFound());
    }


}