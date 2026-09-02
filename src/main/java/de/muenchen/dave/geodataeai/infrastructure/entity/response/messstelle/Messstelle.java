package de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.muenchen.dave.geodataeai.infrastructure.adapter.UnixMilliTimestampToLocalDateConverter;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MessstelleStatus;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messstelle implements FeatureProperties {

    public static final String MST_ID = "MST_ID";

    // Stammdaten

    @JsonProperty("OBJECTID")
    private String objectId;

    @JsonProperty("GISID")
    private Integer gisId;

    @JsonProperty("GLOBALID")
    private String globalId;

    @JsonProperty(MST_ID)
    private Integer mstId;

    @JsonProperty("STATUS")
    private MessstelleStatus status;

    @JsonProperty("PLANUNGSSTATUS")
    private Integer planungsstatus;

    @JsonProperty("PLANUNGSDATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate planungsdatum;

    @JsonProperty("REALISIERUNGSDATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate realisierungsdatum;

    @JsonProperty("ABBAUDATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate abbaudatum;

    @JsonProperty("ERSTELLUNG_NAME")
    private String erstellungName;

    @JsonProperty("ERSTELLUNG_DATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate erstellungDatum;

    @JsonProperty("BEARBEITUNG_NAME")
    private String bearbeitungName;

    @JsonProperty("BEARBEITUNG_DATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate bearbeitungDatum;

    // Messstellen spezifische Attribute

    @JsonProperty("NAME")
    private String name;

    @JsonProperty("STANDORT")
    private String standort;

    @JsonProperty("STADTBEZIRKSNUMMER")
    private Integer stadtbezirkNummer;

    @JsonProperty("BEMERKUNG")
    private String bemerkung;

    @JsonProperty("DATUM_LETZTE_PLAUSIBLE_MESSUNG")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate datumLetztePlausibleMessung;

    @JsonProperty("X_KOOR")
    private BigDecimal xcoordinate;

    @JsonProperty("Y_KOOR")
    private BigDecimal ycoordinate;
}
