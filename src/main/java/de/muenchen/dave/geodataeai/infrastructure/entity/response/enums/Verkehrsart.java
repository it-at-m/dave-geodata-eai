package de.muenchen.dave.geodataeai.infrastructure.entity.response.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Verkehrsart {
    KFZ("JA"),

    RAD("NEIN"),

    UNBEKANNT("");

    private static final Map<String, Verkehrsart> verkehrsartenByTextValue = Stream
            .of(Verkehrsart.values())
            .collect(Collectors.toMap(Verkehrsart::getText, Function.identity()));

    @Getter
    private String text;

    /**
     * Die Methode ist zur Deserialisierung des Texts zu Enum erforderlich.
     *
     * @param text Verkehrsart als JA oder NEIN
     * @return den entsprechenden Enum.
     */
    @JsonCreator
    public static Verkehrsart getByText(final String text) {
        return verkehrsartenByTextValue.getOrDefault(text, Verkehrsart.UNBEKANNT);
    }

}
