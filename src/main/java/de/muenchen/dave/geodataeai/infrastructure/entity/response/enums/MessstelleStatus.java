/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.response.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MessstelleStatus {
    IN_PLANUNG(1),

    IN_BESTAND(2),

    AUSSER_BETRIEB(3),

    ABGEBAUT(4),

    UNBEKANNT(999);

    private static final Map<Integer, MessstelleStatus> messstelleStatusByStatusValue = Stream
            .of(MessstelleStatus.values())
            .collect(Collectors.toMap(MessstelleStatus::getStatus, Function.identity()));

    @Getter
    private Integer status;

    /**
     * Die Methode ist zur Deserialisierung des Integer zu Enum erforderlich.
     *
     * @param status als Integerwert
     * @return den entsprechenden Enum.
     */
    @JsonCreator
    public static MessstelleStatus getById(final Integer status) {
        return messstelleStatusByStatusValue.getOrDefault(status, MessstelleStatus.UNBEKANNT);
    }

}
