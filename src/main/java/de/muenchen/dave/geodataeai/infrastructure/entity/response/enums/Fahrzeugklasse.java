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
public enum Fahrzeugklasse {

    RAD("Rad"),

    SUMME_KFZ("Summe_Kraftfahrzeugverkehr"),

    ZWEI_PLUS_EINS("2+1"),

    ACHT_PLUS_EINS("8+1"),

    UNBEKANNT(StringUtils.EMPTY);

    private static final Map<String, Fahrzeugklasse> fahrzeugklasseByText = Stream
            .of(Fahrzeugklasse.values())
            .collect(Collectors.toMap(Fahrzeugklasse::getText, Function.identity()));

    @Getter
    private final String text;

    /**
     * Die Methode ist zur Deserialisierung des String zu Enum erforderlich.
     *
     * @param text als Bezeichnung der Fahrzeugklasse.
     * @return den entsprechenden Enum.
     */
    @JsonCreator
    public static Fahrzeugklasse getByText(final String text) {
        return fahrzeugklasseByText.getOrDefault(text, UNBEKANNT);
    }

}
