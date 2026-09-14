package de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.muenchen.dave.geodataeai.infrastructure.adapter.UnixMilliTimestampToLocalDateConverter;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Verkehrsart;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

@Data
public class Messquerschnitt implements FeatureProperties {

    public static final String MQ_ID = "MQ_ID";

    @JsonProperty("OBJECTID")
    private String objectId;

    @JsonProperty("GISID")
    private Integer gisId;

    @JsonProperty("GLOBALID")
    private String globalId;

    @JsonProperty(MQ_ID)
    private Integer mqId;

    @JsonProperty("STATUS")
    private Integer status;

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
    private LocalDate erstellungDatum;

    @JsonProperty("BEARBEITUNG_NAME")
    private String bearbeitungName;

    @JsonProperty("BEARBEITUNG_DATUM")
    @JsonDeserialize(converter = UnixMilliTimestampToLocalDateConverter.class)
    private LocalDate bearbeitungDatum;

    // Messquerschnitt spezifische Attribute

    @JsonProperty(Messstelle.MST_ID)
    private Integer mstId;

    @JsonProperty("REF_MESSSTELLE")
    private Integer refMessstelle;

    @JsonProperty("STRASSENNAME")
    private String strassenname;

    // Lage Messquerschnitt
    @JsonProperty("BESCHREIBUNG")
    private String beschreibung;

    @JsonProperty("DATENANBINDUNG")
    private String datenanbindung;

    @JsonProperty("FAHRTRICHTUNG")
    private String fahrtrichtung;

    @JsonProperty("ANZAHL_FAHRSPUREN")
    private Integer anzahlFahrspuren;

    @JsonProperty("FAHRZEUG_KLASSEN")
    private String fahrzeugklasse;

    @JsonProperty("DETEKTIERTE_FAHRZEUGART")
    private Verkehrsart detektierteVerkehrsart;

    @JsonProperty("GESCHWINDIGKEIT")
    private String geschwindigkeit;

    @JsonProperty("HERSTELLER")
    private String hersteller;

    @JsonProperty("ANZAHL_DETEKTOREN")
    private Integer anzahlDetektoren;

    @JsonProperty("X_KOOR")
    private BigDecimal xcoordinate;

    @JsonProperty("Y_KOOR ")
    private BigDecimal ycoordinate;
}
