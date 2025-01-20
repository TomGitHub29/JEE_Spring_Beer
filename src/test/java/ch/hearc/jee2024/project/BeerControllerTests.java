package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static java.util.Optional.empty;import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BeerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Manufacturer manufacturer;

    @BeforeEach
    void setUp() throws Exception {
        // Création d'un Manufacturer unique pour les tests
        Manufacturer newManufacturer = new Manufacturer("Test Brewery");
        String manufacturerJson = objectMapper.writeValueAsString(newManufacturer);

        String response = this.mockMvc.perform(post("/manufacturers").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        manufacturer = objectMapper.readValue(response, Manufacturer.class);

        // ajout de 3 bières pour les tests
        Beer beer1 = new Beer("IPA", "Blonde", 5.0, 10, manufacturer);
        Beer beer2 = new Beer("Stout", "Dark", 6.5, 10, manufacturer);
        Beer beer3 = new Beer("Pilsner", "Blonde", 4.5, 10, manufacturer);


        String beer1Json = objectMapper.writeValueAsString(beer1);
        String beer2Json = objectMapper.writeValueAsString(beer2);
        String beer3Json = objectMapper.writeValueAsString(beer3);

    }


    @Test
    public void createBeerShouldReturnCreatedBeer() throws Exception {
        Beer beer = new Beer("IPA", "Blonde", 5.0, 10, manufacturer);
        String beerJson = objectMapper.writeValueAsString(beer);

        this.mockMvc.perform(post("/beers/admin").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beer.getName())));
    }

    @Test
    public void getAllBeersShouldReturnListOfBeers() throws Exception {
        this.mockMvc.perform(get("/beers").with(user("user").password("password").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    public void deleteBeerShouldRemoveBeer() throws Exception {
        Beer beer = new Beer("Oister", "Dark", 6.5, 10, manufacturer);
        String beerJson = objectMapper.writeValueAsString(beer);

        String response = this.mockMvc.perform(post("/beers/admin").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Beer savedBeer = objectMapper.readValue(response, Beer.class);


        this.mockMvc.perform(delete("/beers/admin/" + savedBeer.getId()).with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getBeersWithPagination() throws Exception {
        this.mockMvc.perform(get("/beers/search?page=0&size=5").with(user("user").password("password").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", lessThanOrEqualTo(5)));
    }

    @Test
    public void getBeersSortedByName() throws Exception {
        this.mockMvc.perform(get("/beers?sortBy=name&direction=asc").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").isNotEmpty());
    }
}
