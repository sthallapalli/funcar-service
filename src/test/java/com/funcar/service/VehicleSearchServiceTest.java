package com.funcar.service;

import com.funcar.api.v1.resource.SearchResource;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleSearchServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleSearchService vehicleSearchService;

    @Test
    public void testSearch() {
        // GIVEN
        Vehicle car = new Vehicle().setMake("Fiat").setCode("c").setColor("White")
                .setKW(300).setModel("500").setPrice(BigDecimal.valueOf(25000))
                .setYear(2010);

        when(this.vehicleRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(car));

        // WHEN
        List<VehicleResource> vehicleResources = vehicleSearchService.search(new SearchResource().setMake("Fiat").setColor("White"));

        // THEN
        verify(vehicleRepository).findAll(any(Specification.class));
        assertEquals(1, vehicleResources.size());
        assertEquals("Fiat", vehicleResources.get(0).getMake());
        assertEquals(300, vehicleResources.get(0).getKW());
        assertEquals(BigDecimal.valueOf(25000), vehicleResources.get(0).getPrice());
    }


    @Test
    public void testSearchAll() {
        // GIVEN
        Vehicle car = new Vehicle().setMake("Fiat").setCode("c").setColor("White")
                .setKW(300).setModel("500").setPrice(BigDecimal.valueOf(25000))
                .setYear(2010);

        when(this.vehicleRepository.findAll()).thenReturn(Arrays.asList(car));

        // WHEN
        List<VehicleResource> vehicleResources = vehicleSearchService.search();

        // THEN
        verify(vehicleRepository).findAll();
        assertEquals(1, vehicleResources.size());
        assertEquals("Fiat", vehicleResources.get(0).getMake());
        assertEquals(300, vehicleResources.get(0).getKW());
        assertEquals(BigDecimal.valueOf(25000), vehicleResources.get(0).getPrice());
    }

}
