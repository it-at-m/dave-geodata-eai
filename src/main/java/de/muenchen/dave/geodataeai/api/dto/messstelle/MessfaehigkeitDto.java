package de.muenchen.dave.geodataeai.api.dto.messstelle;

import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Fahrzeugklasse;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.ZaehldatenIntervall;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MessfaehigkeitDto {

    private LocalDate gueltigAb;

    private LocalDate gueltigBis;

    private Fahrzeugklasse fahrzeugklasse;

    private ZaehldatenIntervall intervall;
}
