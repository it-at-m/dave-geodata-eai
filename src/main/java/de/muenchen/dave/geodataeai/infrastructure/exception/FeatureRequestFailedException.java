package de.muenchen.dave.geodataeai.infrastructure.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeatureRequestFailedException extends Exception {

    public FeatureRequestFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FeatureRequestFailedException(final String message) {
        super(message);
    }
}
