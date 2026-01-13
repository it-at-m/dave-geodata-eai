package de.muenchen.dave.geodataeai.domain.model.messstelle;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UnauffaelligerTagModel {

    private String mstId;

    private LocalDate datum;

}
