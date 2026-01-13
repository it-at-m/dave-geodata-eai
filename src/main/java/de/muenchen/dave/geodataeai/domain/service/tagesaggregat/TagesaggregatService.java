package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatResponseModel;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagesaggregatService {

    private final TagesaggregatAveragingService tagesaggregatAveragingService;

    /**
     * Die Methode extrahiert die Tagesaggregate entsprechend der im Parameter gegebenen Informationen.
     * <p>
     * Bildet für den Zeitraum je Messquerschnitt den Durchschnitt über alle Tagesaggregate.
     *
     * @param request
     * @return die ermittelten Durschnitte für jeden Messquerschnitt über alle
     *         Tagesaggregate.
     * @throws FeatureRequestFailedException
     */
    public TagesaggregatResponseModel getMeanOfTagesaggregateForAllMqIds(final TagesaggregatRequestModel request) throws FeatureRequestFailedException {
        try {
            final var tagesaggregatResponse = tagesaggregatAveragingService.getMeanOfAggregatesForAllMqIds(request);
            log.debug(tagesaggregatResponse.toString());
            return tagesaggregatResponse;

        } catch (Exception exception) {
            final var error = "Bei der Durchschnittsbildung der Tagesaggregate ist ein Fehler aufgetreten.";
            log.error(error, exception);
            throw new FeatureRequestFailedException(error, exception);
        }
    }
}
