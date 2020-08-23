package com.funcar.persistence.repository;

import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.spec.VehicleSearchSpecifications;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void testSave() {

        Vehicle car1 = new Vehicle().setMake("BMW").setCode("a").setColor("Red")
                .setKW(200).setDealerId("1").setModel("X5").setPrice(BigDecimal.valueOf(20000))
                .setYear(2005);

        this.vehicleRepository.save(car1);

        assertTrue(this.vehicleRepository.count() == 1);

        Optional<Vehicle> vehicleOptional = this.vehicleRepository.findById(1L);
        vehicleOptional.ifPresent(vehicle -> {
            assertEquals(BigDecimal.valueOf(20000), vehicle.getPrice());
            assertEquals((short) 0, vehicle.getVersion());
            assertNotNull(vehicle.getAudit().getCreatedOn());
        });
    }

    @Test
    public void testSpecification() {
        Vehicle car1 = new Vehicle().setMake("VW").setCode("a").setColor("Black")
                .setKW(100).setDealerId("2").setModel("Polo").setPrice(BigDecimal.valueOf(20000))
                .setYear(2008);

        this.vehicleRepository.save(car1);

        Specification<Vehicle> specification = Specification.where(VehicleSearchSpecifications.havingMake("VW"));
        List<Vehicle> allVehicles = this.vehicleRepository.findAll(specification);

        assertEquals(1, allVehicles.size());


        Specification<Vehicle> specification1 = Specification.where(VehicleSearchSpecifications.havingMake("BMW"));
        List<Vehicle> allVehicles1 = this.vehicleRepository.findAll(specification1);

        assertEquals(0, allVehicles1.size());


        Specification<Vehicle> specification2 = Specification.where(VehicleSearchSpecifications.havingMake(null))
                .and(VehicleSearchSpecifications.havingColor("Black"))
                .and(VehicleSearchSpecifications.havingYear(2008));
        List<Vehicle> allVehicles2 = this.vehicleRepository.findAll(specification2);

        assertEquals(1, allVehicles2.size());
    }



}
