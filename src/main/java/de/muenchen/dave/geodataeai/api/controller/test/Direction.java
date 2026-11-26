package de.muenchen.dave.geodataeai.api.controller.test;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Direction {

    @NotEmpty
    private String id;

    @NotNull
    @Valid
    private List<@NotNull Interval> intervals;

}
