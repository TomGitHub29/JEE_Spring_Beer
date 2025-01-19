package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Order;
import ch.hearc.jee2024.project.IOC.Manufacturer;
import ch.hearc.jee2024.project.RepositoryOrder.IRepositoryOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class OrdersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IRepositoryOrder orderRepository;


    @Test
    public void createOrderShouldReturnCreatedOrder() throws Exception {
        Manufacturer manufacturer = new Manufacturer("Test Brewery");
        Beer beer1 = new Beer("IPA", "Hoppy", 6.5, manufacturer);
        Beer beer2 = new Beer("Lager", "Blonde", 5.0, manufacturer);

        List<Beer> beers = Arrays.asList(beer1, beer2);
        String beersJson = objectMapper.writeValueAsString(beers);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beersJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(11.5));
    }

    @Test
    public void getOrderByIdShouldReturnOrder() throws Exception {
        Order order = new Order(List.of(new Beer("IPA", "Hoppy", 6.5, new Manufacturer("Brewery"))));
        orderRepository.save(order);

        mockMvc.perform(post("/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(6.5));
    }

    @Test
    public void deleteOrderShouldRemoveOrder() throws Exception {
        Order order = new Order(List.of(new Beer("IPA", "Hoppy", 6.5, new Manufacturer("Brewery"))));
        order = orderRepository.save(order);

        mockMvc.perform(delete("/orders/" + order.getId()))
                .andExpect(status().isNoContent());

        assertFalse(orderRepository.existsById((order.getId())));
    }


}
