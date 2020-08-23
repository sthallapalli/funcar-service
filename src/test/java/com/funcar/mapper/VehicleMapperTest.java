package com.funcar.mapper;

import com.funcar.persistence.entity.Vehicle;
import com.funcar.api.v1.resource.VehicleResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class VehicleMapperTest {

    @Test
    public void testMapper() {
        Vehicle car1 = new Vehicle().setMake("BMW").setCode("a").setColor("Red")
                .setKW(200).setDealerId("1").setModel("X5").setPrice(BigDecimal.valueOf(20000))
                .setYear(2005);


        Vehicle car2 = new Vehicle().setMake("Fiat").setCode("c").setColor("White")
                .setKW(300).setDealerId("1").setModel("500").setPrice(BigDecimal.valueOf(25000))
                .setYear(2010);

        List<Vehicle> vehicles = Arrays.asList(car1, car2);
        List<VehicleResource> vehicleData = VehicleMapper.mapToResourceList(vehicles);

        assertTrue(vehicleData.size() == vehicles.size());
        assertTrue(vehicleData.get(0).getYear() == vehicles.get(0).getYear());
        assertTrue(vehicleData.get(0).getColor() == vehicles.get(0).getColor());
        assertTrue(vehicleData.get(0).getCode() == vehicles.get(0).getCode());
        assertTrue(vehicleData.get(0).getModel() == vehicles.get(0).getModel());


        List<Vehicle> vehicleList = VehicleMapper.mapToEntityList(vehicleData);

        assertTrue(vehicleData.size() == vehicles.size());
        assertTrue(vehicleData.get(1).getYear() == vehicleList.get(1).getYear());
        assertTrue(vehicleData.get(1).getColor() == vehicleList.get(1).getColor());
        assertTrue(vehicleData.get(1).getCode() == vehicleList.get(1).getCode());
        assertTrue(vehicleData.get(1).getModel() == vehicleList.get(1).getModel());

    }
}
