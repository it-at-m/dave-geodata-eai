/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IntervalModel extends MesswertModel {

    // Stammdaten

    private LocalDateTime datumUhrzeitVon;

    private LocalDateTime datumUhrzeitBis;
}
