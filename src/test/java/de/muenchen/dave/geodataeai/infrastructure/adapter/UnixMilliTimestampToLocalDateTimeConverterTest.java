package de.muenchen.dave.geodataeai.infrastructure.adapter;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UnixMilliTimestampToLocalDateTimeConverterTest {

    private final UnixMilliTimestampToLocalDateTimeConverter toTest = new UnixMilliTimestampToLocalDateTimeConverter();

    @Test
    void convert() {
        final long epochMilliSecondsAtTime = 1405268590000L;

        final LocalDateTime expected = LocalDateTime.of(2014, 7, 13, 18, 23, 10);

        Assertions.assertThat(toTest.convert(epochMilliSecondsAtTime))
                .isNotNull()
                .isEqualTo(expected);
    }

}
