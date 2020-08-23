package com.funcar.api.v1.controller;

import com.funcar.api.v1.resource.SearchResource;
import com.funcar.exception.ApiExceptionHandler;
import com.funcar.exception.ResourceNotFoundException;
import com.funcar.mapper.VehicleMapper;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.service.VehicleSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VehicleSearchControllerTest {

    @Mock
    private VehicleSearchService vehicleSearchService;

    @InjectMocks
    private VehicleSearchController vehicleSearchController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.vehicleSearchController)
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    public void testSearchSuccess() throws Exception {
        // GIVEN
        Vehicle car = new Vehicle().setMake("Fiat").setCode("c").setColor("White")
                .setKW(300).setModel("500").setPrice(BigDecimal.valueOf(25000))
                .setYear(2010);
        VehicleResource vehicleResource = VehicleMapper.mapToResource(car);

        when(vehicleSearchService.search(any(SearchResource.class))).thenReturn(Arrays.asList(vehicleResource));

        // WHEN
        mockMvc.perform(post("/api/v1/search")
                .content("{\"make\" : \"Fiat\", \"model\" : \"500\",\"year\" : 2000,\"color\" : \"White\"}")
                .contentType(MediaType.APPLICATION_JSON))

                //THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make", is("Fiat")))
                .andExpect(jsonPath("$[0].kW", is(300)))
                .andExpect(jsonPath("$[0].price", is(25000)));
        ;
    }

    @Test
    public void testSearchNotFound() throws Exception {
        // GIVEN
        when(vehicleSearchService.search(any(SearchResource.class))).thenThrow(new ResourceNotFoundException("no resource"));

        // WHEN
        mockMvc.perform(post("/api/v1/search")
                .content("{\"make\" : \"Fiat\", \"model\" : \"500\",\"year\" : 2000,\"color\" : \"White\"}")
                .contentType(MediaType.APPLICATION_JSON))

                //THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchFail() throws Exception {
        // GIVEN
        when(vehicleSearchService.search(any(SearchResource.class))).thenThrow(new RuntimeException());

        // WHEN
        mockMvc.perform(post("/api/v1/search")
                .content("{\"make\" : \"Fiat\", \"model\" : \"500\",\"year\" : 2000,\"color\" : \"White\"}")
                .contentType(MediaType.APPLICATION_JSON))

                //THEN
                .andExpect(status().isInternalServerError());
    }
}
