package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.IOC.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class OrdersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addBeerToOrderShouldUpdateOrderTotalPrice() throws Exception {
        // Créer une commande
        String orderResponse = mockMvc.perform(post("/orders"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Order savedOrder = objectMapper.readValue(orderResponse, Order.class);

        // Créer une bière
        Beer beer = new Beer("IPA", "Hoppy", 6.5, new Manufacturer("Brewdog"));
        String beerResponse = mockMvc.perform(post("/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Beer savedBeer = objectMapper.readValue(beerResponse, Beer.class);

        // Ajouter la bière à la commande
        this.mockMvc.perform(post("/orders/" + savedOrder.getId() + "/addBeer/" + savedBeer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(6.5));
    }

}
