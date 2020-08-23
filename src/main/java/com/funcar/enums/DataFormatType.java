package com.funcar.enums;

import com.funcar.data.processor.CSVProcessor;
import com.funcar.mapper.VehicleMapper;
import com.funcar.persistence.entity.Vehicle;
import com.funcar.api.v1.resource.VehicleResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.BiFunction;

public enum DataFormatType {

    CSV {
        @Override
        public BiFunction<MultipartFile, String, List<Vehicle>> getProcessor() {
            return CSV_PROCESSOR;
        }
    },

    JSON {
        @Override
        public BiFunction<List<VehicleResource>, String, List<Vehicle>> getProcessor() {
            return JSON_PROCESSOR;
        }
    },

    XML,

    EXCEL;

    public <T> BiFunction<T, String, List<Vehicle>> getProcessor() {
        throw new RuntimeException("No data processor found");
    }

    private static final BiFunction<MultipartFile, String, List<Vehicle>> CSV_PROCESSOR = new CSVProcessor()::process;

    private static final BiFunction<List<VehicleResource>, String, List<Vehicle>> JSON_PROCESSOR =
            (vehicleDataList, dealerId) -> VehicleMapper.mapToEntityList(vehicleDataList);
}
