package trelud.energienutzung.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import trelud.energienutzung.pojo.*;
import trelud.energienutzung.service.ConnectionService;
import trelud.energienutzung.service.DtoService;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConnectionController.class)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConnectionService connectionService;

    @Test
    void get_shouldReturnOneConnection() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Connection> expectedConnections = new ArrayList<>();

        Connection expectedConnection = new Connection();

        Sector expectedSector = new Sector();
        expectedSector.setSectorName("Agriculture");
        expectedConnection.setSector(expectedSector);

        Region expectedRegion = new Region();
        expectedRegion.setRegionName("Styria");
        expectedConnection.setRegion(expectedRegion);

        trelud.energienutzung.pojo.Year expectedYear = new Year();
        expectedYear.setYear(2020);
        expectedConnection.setYear(expectedYear);

        Fuel expectedFuel = new Fuel();
        expectedFuel.setFuelName("Hard coal");
        expectedConnection.setFuel(expectedFuel);

        expectedConnections.add(expectedConnection);

        List<Map<String, Object>> expectedList =
                DtoService.convertList(
                        expectedConnections
                );

        String expectedJson = objectMapper.writeValueAsString(expectedList);

        when(
                connectionService
                        .getConnectionsByYearByRegionBySectorByFuelType(
                                2020,
                                "Styria",
                                "Agriculture",
                                "Hard coal"))
                .thenReturn(expectedList);

        mockMvc.perform(
                get(
                        "http://localhost:8080/energy/connection?year=2020&region=Styria&sector=Agriculture&fuelType=Hard coal"
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void get_shouldReturnError() throws Exception {
        when(
                connectionService.getConnectionsByYearByRegionBySectorByFuelType(0,
                        "a",
                        "a",
                        "a"))
                .thenThrow(new NoSuchElementException("No Element found"));
        mockMvc.perform(
                get("http://localhost:8080/energy/connection?year=0&region=a&sector=a&fuelType=a")
        ).andExpect(status().isNotFound())
                .andExpect(content().string("No Element found"));
    }

    @Test
    void get_shouldSetDefault() throws Exception {

        List<Map<String, Object>> list = List.of();

        when(
                connectionService.getConnectionsByYearByRegionBySectorByFuelType(-1,
                        "*",
                        "*",
                        "*"))
                .thenReturn(list);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(list);

        mockMvc.perform(
                get("http://localhost:8080/energy/connection")
        ).andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}