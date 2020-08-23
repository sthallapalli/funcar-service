package com.funcar.api.v1.controller;

import com.funcar.api.v1.resource.SearchResource;
import com.funcar.api.v1.resource.VehicleResource;
import com.funcar.service.VehicleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class VehicleSearchController {

    private final VehicleSearchService vehicleSearchService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleResource>> search() {
        log.info("Received a search request to list all vehicles ");

        List<VehicleResource> vehicleResourceList = vehicleSearchService.search();
        return ResponseEntity.ok(vehicleResourceList);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleResource>> search(@RequestBody SearchResource searchResource) {
        log.info("Received a search request with search data={}", searchResource);

        List<VehicleResource> vehicleResourceList = vehicleSearchService.search(searchResource);
        return ResponseEntity.ok(vehicleResourceList);
    }

}
