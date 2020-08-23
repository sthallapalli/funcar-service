package com.funcar.api.v1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.exception.ApiExceptionHandler;
import com.funcar.service.VehicleDataUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VehicleDataUploadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleDataUploadService dealerVehicleListService;

    @InjectMocks
    private VehicleDataUploadController dealerVehicleListController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.dealerVehicleListController)
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    public void testSuccessWhenUploadJson() throws Exception {

        //GIVEN
        File file = ResourceUtils.getFile("classpath:cars.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<VehicleResource> vehicleResources = objectMapper.readValue(file, new TypeReference<List<VehicleResource>>() {
        });

        doNothing().when(this.dealerVehicleListService).upload(anyList(), anyString());

        // WHEN
        mockMvc.perform(post("/api/v1/dealer_vehicle/1/vehicle_listings")
                .content(Files.readAllBytes(file.toPath()))
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().isOk());
    }

    @Test
    public void testFailureWhenExceptionWhileUploadJson() throws Exception {

        //GIVEN
        doThrow(new RuntimeException()).when(this.dealerVehicleListService).upload(anyList(), anyString());
        File file = ResourceUtils.getFile("classpath:cars.json");

        // WHEN
        mockMvc.perform(post("/api/v1/dealer_vehicle/1/vehicle_listings")
                .content(Files.readAllBytes(file.toPath()))
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().is5xxServerError());
    }
}
