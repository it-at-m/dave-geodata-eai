package de.muenchen.dave.geodataeai.infrastructure.entity.response.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum ZaehldatenIntervall {

    STUNDE_VIERTEL("15min"),

    STUNDE_VIERTEL_EINGESCHRAENKT("15min (eingeschränkt)"),

    STUNDE_HALB("30min"),

    STUNDE_KOMPLETT("60min"),

    UNBEKANNT(StringUtils.EMPTY);

    private static final Map<String, ZaehldatenIntervall> zaehldatenIntervallByMinutesPerIntervall = Stream
            .of(ZaehldatenIntervall.values())
            .collect(Collectors.toMap(ZaehldatenIntervall::getMinutesPerIntervall, Function.identity()));

    @Getter
    private final String minutesPerIntervall;

    /**
     * Die Methode ist zur Deserialisierung des String zu Enum erforderlich.
     *
     * @param minutesPerIntervall als Minuten je Intervalll.
     * @return den entsprechenden Enum.
     */
    @JsonCreator
    public static ZaehldatenIntervall getByMinutesPerIntervall(final String minutesPerIntervall) {
        return zaehldatenIntervallByMinutesPerIntervall.getOrDefault(minutesPerIntervall, UNBEKANNT);
    }

}
