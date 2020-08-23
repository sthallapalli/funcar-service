package com.funcar.data.processor;

import com.funcar.persistence.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CSVProcessorTest {

    private CSVProcessor csvProcessor = new CSVProcessor();

    @Test
    public void test() throws Exception {
        File dataFile = ResourceUtils.getFile("classpath:cars.csv");
        MultipartFile file = new MockMultipartFile("file", new FileInputStream(dataFile));

        List<Vehicle> vehicleList = csvProcessor.process(file, "1");

        assertEquals(3, vehicleList.size());
        assertEquals("green", vehicleList.get(2).getColor());
    }


}
