package de.muenchen.dave.geodataeai.domain.model.messwerte;

import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.feature.FeaturePropertiesModel;
import java.math.BigDecimal;
import lombok.Data;

@Data
public abstract class MesswertModel implements FeaturePropertiesModel {

    // Stammdaten

    private String objectId;

    private Integer mqId;

    private DaveTagesTyp tagesTyp;

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

    // Anteil

    public BigDecimal getProzentSchwerverkehr() {
        return MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(this.summeSchwerverkehr, this.summeKraftfahrzeugverkehr);
    }

    public BigDecimal getProzentGueterverkehr() {
        return MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(this.summeGueterverkehr, this.summeKraftfahrzeugverkehr);

    }
}
