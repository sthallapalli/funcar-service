package com.funcar.api.v1.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleResource {

    @NotNull
    private String code;

    @NotNull
    private String make;

    @NotNull
    private String model;

    @NotNull
    @Min(4)
    @JsonProperty("kW")
    private Integer kW;

    @NotNull
    private Integer year;

    @NotNull
    private String color;

    @NotNull
    @Positive
    private BigDecimal price;

    private String dealerId;

}
