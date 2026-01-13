/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientNonRequestException extends Exception {

    public ClientNonRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientNonRequestException(final String message) {
        super(message);
    }
}
