package com.funcar.mapper;

import com.funcar.persistence.entity.Vehicle;
import com.funcar.api.v1.resource.VehicleResource;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * For simplicity, I chose this approach, for complex object mappings we can use mapstruct
 */

public class VehicleMapper {

    public static Vehicle mapToEntity(VehicleResource vehicleResource) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleResource, vehicle);
        return vehicle;
    }

    public static VehicleResource mapToResource(Vehicle vehicle) {
        VehicleResource vehicleResource = new VehicleResource();
        BeanUtils.copyProperties(vehicle, vehicleResource);
        return vehicleResource;
    }

    public static List<Vehicle> mapToEntityList(List<VehicleResource> vehicleResourceList) {
        return vehicleResourceList.stream().map(VehicleMapper::mapToEntity).collect(Collectors.toList());
    }

    public static List<VehicleResource> mapToResourceList(List<Vehicle> vehicleList) {
        return vehicleList.stream().map(VehicleMapper::mapToResource).collect(Collectors.toList());
    }
}
