package de.muenchen.dave.geodataeai.infrastructure.adapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.util.StdConverter;

@Slf4j
public class UnixMilliTimestampToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {

    /**
     * Konvertiert die Unixzeit gegeben im Parameter in den Rückgabewert mit der Systemeigenen Zeitzone.
     *
     * @param value als Unixtime in Millisekunden.
     * @return die Repräsentation der Unixzeit in Millisekunden.
     */
    @Override
    public LocalDateTime convert(final Long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of(UnixMilliTimestampToLocalDateConverter.ZONE_ID));
    }

}
