/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.model.enums;

import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MobidamFeatureTagesTyp;
import de.muenchen.mobidam.eai.gen.model.LoadMesswerteTimeRangeTagestypenParameterInner;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DaveTagesTyp {
    DTV_W3(
            BigDecimal.valueOf(1),
            List.of(MobidamFeatureTagesTyp.WERKTAG_DI_ODER_MI_ODER_DO),
            List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO)),

    DTV_W5(
            BigDecimal.valueOf(2),
            List.of(MobidamFeatureTagesTyp.WERKTAG_DI_ODER_MI_ODER_DO, MobidamFeatureTagesTyp.WERKTAG_MO_ODER_FR),
            List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO,
                    LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR)),

    SAMSTAG(
            BigDecimal.valueOf(3),
            List.of(MobidamFeatureTagesTyp.SAMSTAG),
            List.of(LoadMesswerteTimeRangeTagestypenParameterInner.SAMSTAG)),

    SONNTAG_FEIERTAG(
            BigDecimal.valueOf(4),
            List.of(MobidamFeatureTagesTyp.SONNTAG_ODER_FEIERTAG),
            List.of(LoadMesswerteTimeRangeTagestypenParameterInner.SONNTAG_FEIERTAG)),

    WERKTAG_FERIEN(
            BigDecimal.valueOf(5),
            List.of(MobidamFeatureTagesTyp.WERKTAG_FERIEN),
            List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_FERIEN)),

    /**
     * Setzt sich zusammen aus allen anderen Tagestypen zusammen (WERKTAG_DI_ODER_MI_ODER_DO +
     * WERKTAG_MO_ODER_FR + SAMSTAG + SONNTAG_ODER_FEIERTAG +
     * WERKTAG_FERIEN).
     *
     * Somit muss bei der Abfrage in Mobidam keine Filterung nach den Tagestypen vorgenommen werden.
     */
    DTV(
            BigDecimal.valueOf(6),
            List.of(),
            List.of(
                    LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO,
                    LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR,
                    LoadMesswerteTimeRangeTagestypenParameterInner.SAMSTAG,
                    LoadMesswerteTimeRangeTagestypenParameterInner.SONNTAG_FEIERTAG,
                    LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_FERIEN));

    @Getter
    private BigDecimal typ;

    @Getter
    private List<MobidamFeatureTagesTyp> mobidamFeatureTagesTyp;

    @Getter
    private List<LoadMesswerteTimeRangeTagestypenParameterInner> mobidamMesswerteTagesTyp;
}
