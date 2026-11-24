package de.muenchen.dave.geodataeai.api.controller.test;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class IntervalDto {

    @NotNull
    private LocalDateTime start;

    private Integer pedestrian;
    private Integer bike;

    private Integer scooter;

    // ...

    private Integer other;
}
