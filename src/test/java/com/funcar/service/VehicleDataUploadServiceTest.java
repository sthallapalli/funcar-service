package com.funcar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.data.processor.CSVProcessor;
import com.funcar.enums.DataFormatType;
import com.funcar.mapper.VehicleMapper;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleDataUploadServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleDataUploadService dealerVehicleListService;

    @Captor
    private ArgumentCaptor<List<Vehicle>> vehicleArgumentCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testUploadJson() throws Exception {

        // GIVEN
        File dataFile = ResourceUtils.getFile("classpath:cars.json");
        List<VehicleResource> vehicleResourceList = objectMapper.readValue(dataFile, new TypeReference<List<VehicleResource>>() {
        });

        when(this.vehicleRepository.saveAll(anyList())).thenReturn(VehicleMapper.mapToEntityList(vehicleResourceList));

        // WHEN
        this.dealerVehicleListService.upload(vehicleResourceList, "1", DataFormatType.JSON);

        //THEN
        verify(this.vehicleRepository).saveAll(vehicleArgumentCaptor.capture());
        List<Vehicle> capturedVehicles = vehicleArgumentCaptor.getValue();
        capturedVehicles.forEach(vehicle -> Assertions.assertTrue(vehicle.getDealerId().equals("1")));
    }

    @Test
    public void testUploadCsv() throws Exception {

        // GIVEN
        File dataFile = ResourceUtils.getFile("classpath:cars.csv");
        MultipartFile multipartFile = new MockMultipartFile("file", "cars.csv", null, new FileInputStream(dataFile));

        List<Vehicle> vehicles = new CSVProcessor().process(multipartFile, "1");
        when(this.vehicleRepository.saveAll(anyList())).thenReturn(vehicles);

        // WHEN
        this.dealerVehicleListService.upload(multipartFile, "1", DataFormatType.CSV);

        //THEN
        verify(this.vehicleRepository).saveAll(vehicleArgumentCaptor.capture());
        List<Vehicle> capturedVehicles = vehicleArgumentCaptor.getValue();
        capturedVehicles.forEach(vehicle -> Assertions.assertTrue(vehicle.getDealerId().equals("1")));
    }

}
