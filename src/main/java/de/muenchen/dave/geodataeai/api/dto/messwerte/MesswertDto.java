package de.muenchen.dave.geodataeai.api.dto.messwerte;

import java.math.BigDecimal;
import lombok.Data;

@Data
public abstract class MesswertDto {

    // Stammdaten

    private Integer mqId;

    // Anzahl

    private BigDecimal anzahlLfw;

    private BigDecimal anzahlKrad;

    private BigDecimal anzahlLkw;

    private BigDecimal anzahlBus;

    private BigDecimal anzahlRad;

    // Summen

    private BigDecimal summeAllePkw;

    private BigDecimal summeLastzug;

    private BigDecimal summeGueterverkehr;

    private BigDecimal summeSchwerverkehr;

    private BigDecimal summeKraftfahrzeugverkehr;

    private BigDecimal prozentSchwerverkehr;

    private BigDecimal prozentGueterverkehr;
}
