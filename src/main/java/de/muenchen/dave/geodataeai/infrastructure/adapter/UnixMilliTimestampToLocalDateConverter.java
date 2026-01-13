package de.muenchen.dave.geodataeai.infrastructure.adapter;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnixMilliTimestampToLocalDateConverter extends StdConverter<Long, LocalDate> {

    public static final String ZONE_ID = "Europe/Berlin";

    /**
     * Konvertiert die Unixzeit gegeben im Parameter in den Rückgabewert mit der Systemeigenen Zeitzone.
     *
     * @param value als Unixtime in Millisekunden.
     * @return die Repräsentation der Unixzeit in Millisekunden.
     */
    @Override
    public LocalDate convert(final Long value) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(value), ZoneId.of(ZONE_ID));
    }

}
