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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@AutoConfigureMockMvc
@SpringBootTest
public class BeerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createBeerShouldReturnCreatedBeer() throws Exception {
        Manufacturer manufacturer = new Manufacturer("Feldschlösschen");
        Beer beer = new Beer("BeerName", "BeerType", 5.0, manufacturer);
        // Convertir l'objet Beer en JSON
        String beerJson = objectMapper.writeValueAsString(beer);

        this.mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(beerJson));
    }

    @Test
    public void getAllBeersShouldReturnEmptyListInitially() throws Exception {
        this.mockMvc.perform(get("/beers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

//    @Test
//    public void getAllBeersShouldReturnBeerListNotEmpty() throws Exception {
//        this.mockMvc.perform(get("/beers"))
//                .andExpect(status().isOk())
//                .andExpect(
//                        content().json("[1]")
//                )
//    }
}