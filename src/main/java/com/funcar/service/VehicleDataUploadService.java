package com.funcar.service;

import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.mapper.VehicleMapper;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class VehicleDataUploadService {

    private final VehicleRepository vehicleRepository;

    public VehicleDataUploadService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * @param vehicleDataList
     * @param dealerId
     */
    @Transactional
    public void upload(List<VehicleResource> vehicleDataList, String dealerId) {
        List<Vehicle> vehicleList = VehicleMapper.mapToEntityList(vehicleDataList)
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
