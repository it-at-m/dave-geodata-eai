package de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.muenchen.dave.geodataeai.infrastructure.adapter.UnixMilliTimestampToLocalDateConverter;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Fahrzeugklasse;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.ZaehldatenIntervall;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureProperties;
import java.time.LocalDate;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

@Data
public class Messfaehigkeit implements FeatureProperties {

    @JsonProperty("OBJECTID")
    private String objectId;

    @JsonProperty("REF_MESSSTELLE")
    private Integer refMessstelle;

    @JsonProperty(Messstelle.MST_ID)
    private Integer mstId;

    @JsonProperty("GUELTIG_AB")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate gueltigAb;

    @JsonProperty("GUELTIG_BIS")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate gueltigBis;

    @JsonProperty("FAHRZEUG_KLASSEN")
    private Fahrzeugklasse fahrzeugklasse;

    @JsonProperty("INTERVALL")
    private ZaehldatenIntervall intervall;
}
