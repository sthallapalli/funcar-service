package com.funcar.persistence.spec;

import com.funcar.persistence.entity.Vehicle;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class VehicleSearchSpecifications {

    private static final String MAKE = "make";
    private static final String MODEL = "model";
    private static final String YEAR = "year";
    private static final String COLOR = "color";

    public static Specification<Vehicle> havingMake(String make) {
        return (vehicleRoot, query, cb) -> {

            if (StringUtils.isEmpty(make))
                return cb.isTrue(cb.literal(true));

            return cb.like(cb.lower(vehicleRoot.get(MAKE)), make.toLowerCase());
        };
    }

    public static Specification<Vehicle> havingModel(String model) {
        return (vehicleRoot, query, cb) -> {

            if (StringUtils.isEmpty(model))
                return cb.isTrue(cb.literal(true));

            return cb.like(cb.lower(vehicleRoot.get(MODEL)), model.toLowerCase());
        };
    }

    public static Specification<Vehicle> havingYear(Integer year) {
        return (vehicleRoot, query, cb) -> {
            if (ObjectUtils.isEmpty(year))
                return cb.isTrue(cb.literal(true));

            return cb.equal(vehicleRoot.get(YEAR), year);
        };
    }

    public static Specification<Vehicle> havingColor(String color) {
        return (vehicleRoot, query, cb) -> {

            if (StringUtils.isEmpty(color))
                return cb.isTrue(cb.literal(true));

            return cb.like(cb.lower(vehicleRoot.get(COLOR)), color.toLowerCase());
        };
    }
}
