package com.funcar.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.persistence.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class VehicleMapperTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testMapper() throws Exception {

        // GIVEN
        File dataFile = ResourceUtils.getFile("classpath:cars.json");
        List<VehicleResource> vehicleResourceList = objectMapper.readValue(dataFile, new TypeReference<List<VehicleResource>>() {
        });

        List<Vehicle> vehicleList = VehicleMapper.mapToEntityList(vehicleResourceList);

        assertEquals(vehicleResourceList.size(), vehicleList.size());
        assertEquals(vehicleResourceList.get(0).getYear(), vehicleList.get(0).getYear());
        assertEquals(vehicleResourceList.get(0).getColor(), vehicleList.get(0).getColor());
        assertEquals(vehicleResourceList.get(0).getCode(), vehicleList.get(0).getCode());
        assertEquals(vehicleResourceList.get(0).getModel(), vehicleList.get(0).getModel());


        List<VehicleResource> vehicleResources = VehicleMapper.mapToResourceList(vehicleList);

        assertEquals(vehicleList.size(), vehicleResources.size());
        assertEquals(vehicleList.get(0).getYear(), vehicleResources.get(0).getYear());
        assertEquals(vehicleList.get(0).getColor(), vehicleResources.get(0).getColor());
        assertEquals(vehicleList.get(0).getCode(), vehicleResources.get(0).getCode());
        assertEquals(vehicleList.get(0).getModel(), vehicleResources.get(0).getModel());


    }
}
