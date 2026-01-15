package de.muenchen.dave.geodataeai.api.dto.messwerte;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IntervalDto extends MesswertDto {

    // Stammdaten

    private LocalDateTime datumUhrzeitVon;

    private LocalDateTime datumUhrzeitBis;

    private DaveTagesTyp tagesTyp;
}
