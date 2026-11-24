package de.muenchen.dave.geodataeai.api.controller.test;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrafficMeasurementPointDto {

    private Integer id;

    @NotEmpty
    @Valid
    private List<@NotNull CountingUnitDto> countingUnits;

}
