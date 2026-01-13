/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.exception;

public class ArcgisNonRequestException extends Exception {

    public ArcgisNonRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ArcgisNonRequestException(final String message) {
        super(message);
    }
}
