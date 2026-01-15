package de.muenchen.dave.geodataeai.api.dto.messwerte;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class MesswertRequestDto {

    @Schema(description = "Alle Messquerschnitt-Ids der Messstelle")
    @NotEmpty
    private List<Integer> allMessquerschnittIds;

    @Schema(description = "Die ausgewählten Messquerschnitt-Ids der Messstelle welche angezeigt werden sollen.")
    @NotEmpty
    private List<Integer> selectedMessquerschnittIds;

    @NotNull
    private IntervalSize intervalInMinutes;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private DaveTagesTyp tagesTyp;
}
