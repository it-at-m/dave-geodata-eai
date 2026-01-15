package de.muenchen.dave.geodataeai.api.dto.messwerte;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagesaggregatDto extends MesswertDto {
    // Messtage

    private Long includedMeasuringDays;
}
