/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.response.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MobidamFeatureTagesTyp {
    WERKTAG_DI_ODER_MI_ODER_DO(BigDecimal.valueOf(1)),

    WERKTAG_MO_ODER_FR(BigDecimal.valueOf(2)),

    SAMSTAG(BigDecimal.valueOf(3)),

    SONNTAG_ODER_FEIERTAG(BigDecimal.valueOf(4)),

    WERKTAG_FERIEN(BigDecimal.valueOf(5)),

    /**
     * Ist nicht in Mobidam persistiert.
     */
    MO_BIS_SO(BigDecimal.valueOf(6));

    private static final Map<BigDecimal, MobidamFeatureTagesTyp> tagesTypByTypValue = Stream
            .of(MobidamFeatureTagesTyp.values())
            .collect(Collectors.toMap(MobidamFeatureTagesTyp::getTyp, Function.identity()));

    @Getter
    private BigDecimal typ;

    /**
     * Die Methode ist zur Deserialisierung des Integer zu Enum erforderlich.
     *
     * @param typ als Integerwert
     * @return den entsprechenden Enum.
     */
    @JsonCreator
    public static MobidamFeatureTagesTyp getByTyp(final Integer typ) {
        return tagesTypByTypValue.get(BigDecimal.valueOf(typ));
    }
}
