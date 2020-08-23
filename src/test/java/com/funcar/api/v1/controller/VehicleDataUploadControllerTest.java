package com.funcar.api.v1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcar.enums.DataFormatType;
import com.funcar.exception.ApiExceptionHandler;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.service.VehicleDataUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

        doNothing().when(this.dealerVehicleListService).upload(anyList(), anyString(), any(DataFormatType.class));

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
        doThrow(new RuntimeException()).when(this.dealerVehicleListService).upload(anyList(), anyString(), any(DataFormatType.class));
        File file = ResourceUtils.getFile("classpath:cars.json");

        // WHEN
        mockMvc.perform(post("/api/v1/dealer_vehicle/1/vehicle_listings")
                .content(Files.readAllBytes(file.toPath()))
                .contentType(MediaType.APPLICATION_JSON))

                // THEN
                .andExpect(status().is5xxServerError());
    }


    @Test
    public void testSuccessWhenUploadCsv() throws Exception {

        //GIVEN
        File dataFile = ResourceUtils.getFile("classpath:cars.csv");
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(dataFile));

        // WHEN
        mockMvc.perform(multipart("/api/v1/dealer_vehicle/upload_csv/1")
                .file(file))

                // THEN
                .andExpect(status().isOk());
    }

    @Test
    public void testFailureWhenExceptionWhileUploadCsv() throws Exception {

        //GIVEN
        doThrow(new RuntimeException()).when(this.dealerVehicleListService).upload(any(MultipartFile.class), anyString(), any(DataFormatType.class));
        File dataFile = ResourceUtils.getFile("classpath:cars.csv");
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(dataFile));

        // WHEN
        mockMvc.perform(multipart("/api/v1/dealer_vehicle/upload_csv/1")
                .file(file))

                // THEN
                .andExpect(status().is5xxServerError());
    }
}
