package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.IOC.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Manufacturer manufacturer;
    private Beer beer;

    @BeforeEach
    void setUp() throws Exception {
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

        Beer newBeer = new Beer("IPA", "Blonde", 5.0, 10, manufacturer);
        String beerJson = objectMapper.writeValueAsString(newBeer);

        String beerResponse = this.mockMvc.perform(post("/beers/admin").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        beer = objectMapper.readValue(beerResponse, Beer.class);
    }

    @Test
    public void createOrderShouldReturnCreatedOrder() throws Exception {
        Order order = new Order(List.of(beer));
        String orderJson = objectMapper.writeValueAsString(order);

        this.mockMvc.perform(post("/orders").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beers.length()", is(0)));
    }

    @Test
    public void getAllOrdersShouldReturnList() throws Exception {
        this.mockMvc.perform(get("/orders").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    public void deleteOrderShouldRemoveOrder() throws Exception {
        Order order = new Order();
        String orderJson = objectMapper.writeValueAsString(order);

        String response = this.mockMvc.perform(post("/orders").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Order savedOrder = objectMapper.readValue(response, Order.class);

        this.mockMvc.perform(delete("/orders/" + savedOrder.getId() + "/checkout").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addAndRemoveBeerFromOrder() throws Exception {
        Order order = new Order();
        String orderJson = objectMapper.writeValueAsString(order);

        String response = this.mockMvc.perform(post("/orders").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Order savedOrder = objectMapper.readValue(response, Order.class);

        this.mockMvc.perform(post("/orders/" + savedOrder.getId() + "/addBeer/" + beer.getId()).with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beers.length()", greaterThan(0)));

        this.mockMvc.perform(delete("/orders/" + savedOrder.getId() + "/removeBeer/" + beer.getId()).with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beers.length()", is(0)));
    }
}

