package de.muenchen.dave.geodataeai.api.dto.messwerte;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ZeitraumDto {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
