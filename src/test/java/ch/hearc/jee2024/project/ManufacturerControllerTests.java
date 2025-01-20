package ch.hearc.jee2024.project;

import ch.hearc.jee2024.project.IOC.Manufacturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ManufacturerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Manufacturer manufacturer;

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
    }

    @Test
    public void createManufacturerShouldReturnCreatedManufacturer() throws Exception {
        Manufacturer newManufacturer = new Manufacturer("New Brewery");
        String manufacturerJson = objectMapper.writeValueAsString(newManufacturer);

        this.mockMvc.perform(post("/manufacturers").with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newManufacturer.getName())));
    }

    @Test
    public void getAllManufacturersShouldReturnList() throws Exception {
        this.mockMvc.perform(get("/manufacturers").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    public void deleteManufacturerShouldRemoveManufacturer() throws Exception {
        this.mockMvc.perform(delete("/manufacturers/admin/" + manufacturer.getId()).with(user("admin").password("admin123").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createManufacturerShouldFailForNonAdmin() throws Exception {
        Manufacturer newManufacturer = new Manufacturer("Unauthorized Brewery");
        String manufacturerJson = objectMapper.writeValueAsString(newManufacturer);

        this.mockMvc.perform(post("/manufacturers").with(user("user").password("password").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(manufacturerJson))
                .andExpect(status().isForbidden());
    }
}
