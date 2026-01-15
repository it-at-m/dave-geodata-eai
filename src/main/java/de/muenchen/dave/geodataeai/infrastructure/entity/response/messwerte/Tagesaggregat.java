package de.muenchen.dave.geodataeai.infrastructure.entity.response.messwerte;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.muenchen.dave.geodataeai.infrastructure.adapter.UnixMilliTimestampToLocalDateTimeConverter;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MobidamFeatureTagesTyp;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureProperties;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messquerschnitt;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Tagesaggregat implements FeatureProperties {

    public static final String TAGESTYP = "TAGESTYP";

    public static final String DATUM = "DATUM";

    // Stammdaten

    @JsonProperty("OBJECTID")
    private String objectId;

    @JsonProperty(Messquerschnitt.MQ_ID)
    private Integer mqId;

    @JsonProperty(DATUM)
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateTimeConverter.class)
    private LocalDateTime datum;

    @JsonProperty(TAGESTYP)
    private MobidamFeatureTagesTyp mobidamFeatureTagesTyp;

    @JsonProperty("PLAUSIBILITAETS_INFO")
    private String plausibilitaetsInfo;

    @JsonProperty("STOERUNG")
    private String stoerung;

    // Anzahl

    @JsonProperty("ANZAHL_LFW")
    private BigDecimal anzahlLfw;

    @JsonProperty("ANZAHL_KRAD")
    private BigDecimal anzahlKrad;

    @JsonProperty("ANZAHL_LKW")
    private BigDecimal anzahlLkw;

    @JsonProperty("ANZAHL_BUS")
    private BigDecimal anzahlBus;

    @JsonProperty("ANZAHL_RAD")
    private BigDecimal anzahlRad;

    // Summen

    @JsonProperty("SUMME_ALLE_PKW")
    private BigDecimal summeAllePkw;

    @JsonProperty("SUMME_LASTZUG")
    private BigDecimal summeLastzug;

    @JsonProperty("SUMME_GUETERVERKEHR")
    private BigDecimal summeGueterverkehr;

    @JsonProperty("SUMME_SCHWERVERKEHR")
    private BigDecimal summeSchwerverkehr;

    @JsonProperty("SUMME_KRAFTFAHRZEUGVERKEHR")
    private BigDecimal summeKraftfahrzeugverkehr;
}
