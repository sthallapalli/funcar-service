package com.funcar.service;

import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VehicleDataUploadService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    public <T> void upload(T data,
                           String dealerId,
                           BiFunction<T, String, List<Vehicle>> processor) {

        List<Vehicle> vehicleList = processor.apply(data, dealerId)
                .stream()
                .map(vehicle -> vehicle.setDealerId(dealerId))
                .collect(Collectors.toList());

        List<Vehicle> finalVehicleList = vehicleList.stream().map(this::updateCheck).collect(Collectors.toList());
        List<Vehicle> resultList = vehicleRepository.saveAll(finalVehicleList);
        log.info("Saved {} vehicles data from dealer id={}", resultList.size(), dealerId);
    }


    private Vehicle updateCheck(Vehicle incomingVehicle) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findByDealerIdAndCode(incomingVehicle.getDealerId(), incomingVehicle.getCode());
        existingVehicle.ifPresent(vehicle -> BeanUtils.copyProperties(incomingVehicle, vehicle, "id"));
        return existingVehicle.orElse(incomingVehicle);
    }
}
