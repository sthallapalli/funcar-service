package com.funcar.service;

import com.funcar.api.v1.resource.VehicleResource;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class VehicleDataUploadServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleDataUploadService dealerVehicleListService;

    @Captor
    private ArgumentCaptor<List<Vehicle>> vehicleArgumentCaptor;

    @Test
    public void testUpload() {

        // GIVEN
        Vehicle car1 = new Vehicle().setMake("BMW").setCode("a").setColor("Red")
                .setKW(200).setModel("X5").setPrice(BigDecimal.valueOf(20000))
                .setYear(2005);

        Vehicle car2 = new Vehicle().setMake("Fiat").setCode("c").setColor("White")
                .setKW(300).setModel("500").setPrice(BigDecimal.valueOf(25000))
                .setYear(2010);

        List<Vehicle> vehicleList = Arrays.asList(car1, car2);
        List<VehicleResource> vehicleData = VehicleMapper.mapToResourceList(vehicleList);

        when(this.vehicleRepository.saveAll(anyList())).thenReturn(vehicleList);

        // WHEN
        this.dealerVehicleListService.upload(vehicleData, "1");

        //THEN
        verify(this.vehicleRepository).saveAll(vehicleArgumentCaptor.capture());
        List<Vehicle> capturedVehicles = vehicleArgumentCaptor.getValue();
        capturedVehicles.forEach(vehicle -> Assertions.assertTrue(vehicle.getDealerId().equals("1")));
    }


}
