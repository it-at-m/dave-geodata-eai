package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagesaggregatModel extends MesswertModel {

    // Stammdaten

    private LocalDateTime datum;

    // Messtage
    private Long includedMeasuringDays;
}
