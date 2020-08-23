package com.funcar.persistence.repository;

import com.funcar.persistence.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
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
}
