package de.muenchen.dave.geodataeai.api.dto.messstelle;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UnauffaelligerTagDto {

    private String mstId;

    private LocalDate datum;

}
