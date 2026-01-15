package de.muenchen.dave.geodataeai.api.dto.messwerte;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class TagesaggregatRequestDto {

    @NotEmpty
    private List<@NotNull Integer> messquerschnittIds;

    @NotEmpty
    private List<@NotNull @Valid ZeitraumDto> zeitraeume;

    @NotNull
    private DaveTagesTyp tagesTyp;
}
