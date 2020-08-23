package com.funcar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funcar.persistence.repository.VehicleRepository;
import com.funcar.api.v1.resource.SearchResource;
import com.funcar.api.v1.resource.VehicleResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = FuncarServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FuncarServiceApplicationIntegrationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void cleanUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    public void testJsonUploadWithDiffDealerAndSameCode() throws Exception {

        File dataFile = ResourceUtils.getFile("classpath:cars.json");
        List<VehicleResource> vehicleResourceList = objectMapper.readValue(dataFile, new TypeReference<List<VehicleResource>>() {
        });

        HttpEntity<List<VehicleResource>> entity1 = new HttpEntity<>(vehicleResourceList, new HttpHeaders());

        ResponseEntity<String> response1 = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/1/vehicle_listings"),
                HttpMethod.POST, entity1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());


        vehicleResourceList.get(0).setColor("Yellow");
        HttpEntity<List<VehicleResource>> entity2 = new HttpEntity<>(vehicleResourceList, new HttpHeaders());

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/2/vehicle_listings"),
                HttpMethod.POST, entity2, String.class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());


        ResponseEntity<List<VehicleResource>> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/search"), HttpMethod.GET, null, new ParameterizedTypeReference<List<VehicleResource>>() {
        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(2, responseEntity.getBody().size());

        assertEquals(responseEntity.getBody().get(0).getCode(), responseEntity.getBody().get(1).getCode());

        assertNotEquals(responseEntity.getBody().get(0).getDealerId(), responseEntity.getBody().get(1).getDealerId());
        assertNotEquals(responseEntity.getBody().get(0).getColor(), responseEntity.getBody().get(1).getColor());
    }


    @Test
    public void testJsonUploadWithSameDealerUpdate() throws Exception {

        File dataFile = ResourceUtils.getFile("classpath:cars.json");
        List<VehicleResource> vehicleResourceList = objectMapper.readValue(dataFile, new TypeReference<List<VehicleResource>>() {
        });
        HttpEntity<List<VehicleResource>> entity1 = new HttpEntity<>(vehicleResourceList, new HttpHeaders());

        ResponseEntity<String> response1 = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/1/vehicle_listings"),
                HttpMethod.POST, entity1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());


        vehicleResourceList.get(0).setColor("Yellow");
        HttpEntity<List<VehicleResource>> entity2 = new HttpEntity<>(vehicleResourceList, new HttpHeaders());

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/1/vehicle_listings"),
                HttpMethod.POST, entity2, String.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());


        ResponseEntity<List<VehicleResource>> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/search"), HttpMethod.GET, null, new ParameterizedTypeReference<List<VehicleResource>>() {
        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Yellow", responseEntity.getBody().get(0).getColor());
        assertEquals("1", responseEntity.getBody().get(0).getDealerId());

    }

    @Test
    public void testCsvUpload() throws Exception {

        File dataFile = ResourceUtils.getFile("classpath:cars.csv");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(dataFile));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/upload_csv/1"),
                HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<List<VehicleResource>> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/search"), HttpMethod.GET, null, new ParameterizedTypeReference<List<VehicleResource>>() {
        });

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(3, responseEntity.getBody().size());
    }


    @Test
    public void testSearchWithSpecifications() throws Exception {

        File dataFile = ResourceUtils.getFile("classpath:cars.csv");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(dataFile));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/dealer_vehicle/upload_csv/1"),
                HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        SearchResource searchResource = new SearchResource().setMake("mercedes").setColor("black");
        HttpEntity<MultiValueMap<String, Object>> searchEntity = new HttpEntity(searchResource, new HttpHeaders());
        ResponseEntity<List<VehicleResource>> responseEntity = restTemplate.exchange(createURLWithPort("/api/v1/search?"),
                HttpMethod.POST, searchEntity, new ParameterizedTypeReference<List<VehicleResource>>() {
        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(searchResource.getColor(), responseEntity.getBody().get(0).getColor());
        assertEquals(searchResource.getMake(), responseEntity.getBody().get(0).getMake());
        assertEquals("a 180", responseEntity.getBody().get(0).getModel());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
