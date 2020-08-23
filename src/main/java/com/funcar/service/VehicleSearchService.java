package com.funcar.service;

import com.funcar.exception.ResourceNotFoundException;
import com.funcar.mapper.VehicleMapper;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.persistence.repository.VehicleRepository;
import com.funcar.api.v1.resource.SearchResource;
import com.funcar.api.v1.resource.VehicleResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.funcar.persistence.spec.VehicleSearchSpecifications.havingColor;
import static com.funcar.persistence.spec.VehicleSearchSpecifications.havingMake;
import static com.funcar.persistence.spec.VehicleSearchSpecifications.havingModel;
import static com.funcar.persistence.spec.VehicleSearchSpecifications.havingYear;

@Service
@RequiredArgsConstructor
public class VehicleSearchService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleResource> search() {

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        if (CollectionUtils.isEmpty(vehicleList))
            throw new ResourceNotFoundException(String.format("No vehicle found"));

        return VehicleMapper.mapToResourceList(vehicleList);
    }

    public List<VehicleResource> search(SearchResource searchResource) {

        Specification<Vehicle> specification = Specification.where(havingMake(searchResource.getMake()))
                .and(havingModel(searchResource.getModel()))
                .and(havingYear(searchResource.getYear()))
                .and(havingColor(searchResource.getColor()));

        List<Vehicle> vehicleList = vehicleRepository.findAll(specification);

        if (CollectionUtils.isEmpty(vehicleList))
            throw new ResourceNotFoundException(String.format("No vehicle found for search=%s", searchResource));

        return VehicleMapper.mapToResourceList(vehicleList);
    }
}
