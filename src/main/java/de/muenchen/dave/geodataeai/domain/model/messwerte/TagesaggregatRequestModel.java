package de.muenchen.dave.geodataeai.domain.model.messwerte;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class TagesaggregatRequestModel {

    @NotEmpty
    private List<Integer> messquerschnittIds;

    private List<ZeitraumModel> zeitraeume;

    @NotNull
    private DaveTagesTyp tagesTyp;
}
