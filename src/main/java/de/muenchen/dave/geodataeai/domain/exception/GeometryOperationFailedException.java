package de.muenchen.dave.geodataeai.domain.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GeometryOperationFailedException extends Exception {

    public GeometryOperationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GeometryOperationFailedException(final String message) {
        super(message);
    }
}
