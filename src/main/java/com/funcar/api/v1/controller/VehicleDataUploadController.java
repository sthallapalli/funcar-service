package com.funcar.api.v1.controller;

import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.service.VehicleDataUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/dealer_vehicle")
public class VehicleDataUploadController {

    private final VehicleDataUploadService dealerVehicleListService;

    @PostMapping(path = "/{dealer_id}/vehicle_listings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadJson(@Valid @RequestBody List<VehicleResource> vehicleData, @PathVariable(name = "dealer_id") @NotNull String dealerId) {

        log.info("Received a request to upload the json vehicle data from dealer id={}", dealerId);
        dealerVehicleListService.upload(vehicleData, dealerId);
        return ResponseEntity.ok("SUCCESS");
    }
}

