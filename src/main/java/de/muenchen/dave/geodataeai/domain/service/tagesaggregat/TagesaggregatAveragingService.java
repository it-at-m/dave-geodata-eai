package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatResponseModel;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagesaggregatAveragingService {

    private final TagesaggregatExtractionService tagesaggregatExtractionService;

    /**
     * Die Methode berechnet pro Messquerschnitt den Durchschnitt aller Tagesaggregate.
     *
     * @param request zur Extraktion der Tagesaggregate.
     * @return die durchschnittlichen Tagesaggregate pro Messquerschnitt.
     * @throws FeatureRequestFailedException
     */
    @LogExecutionTime
    public TagesaggregatResponseModel getMeanOfAggregatesForAllMqIds(final TagesaggregatRequestModel request)
            throws FeatureRequestFailedException {

        final var tagesaggregate = tagesaggregatExtractionService.getTagesaggregate(
                request.getMessquerschnittIds(),
                request.getZeitraeume(),
                request.getTagesTyp())
                .toList();

        final var response = new TagesaggregatResponseModel();
        final var meanOfAggregatesForEachMqId = this.calculateMeanOfAggregatesForEachMessquerschnitt(tagesaggregate);
        response.setMeanOfAggregatesForEachMqId(new ArrayList<>(meanOfAggregatesForEachMqId));

        // Liste mit alle MessquerschnittIds, die auch ein Tagesaggreagat haben.
        final List<Integer> mqIdsWithTagesaggregat = response.getMeanOfAggregatesForEachMqId().stream().map(MesswertModel::getMqId).toList();

        // Wenn ein angefragter Messquerschnitt kein Tagesaggregat hat, so wird für diesen ein Dummy angelegt und zurückgeliefert.
        request.getMessquerschnittIds()
                .stream()
                .filter(mqId -> !mqIdsWithTagesaggregat.contains(mqId))
                .forEach(mqId -> {
                    final TagesaggregatModel model = new TagesaggregatModel();
                    model.setMqId(mqId);
                    response.getMeanOfAggregatesForEachMqId().add(model);
                });

        final var sumOverAllAggregatesAndAllMessquerschnitte = response.getMeanOfAggregatesForEachMqId()
                .stream()
                .reduce(
                        new TagesaggregatModel(),
                        MesswertUtils::sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel);
        response.setSumOverAllAggregatesOfAllMqId(sumOverAllAggregatesAndAllMessquerschnitte);

        return response;
    }

    /**
     * Die Methode bildet je Messquerschnitt den Durchschnitt über alle Tagesaggregate pro
     * Messquerschnitt.
     *
     * @param tagesaggregate für die Summenbildung
     * @return der Durchschnitt über alle Tagesaggregate für jeden Messquerschnitt.
     */
    public List<TagesaggregatModel> calculateMeanOfAggregatesForEachMessquerschnitt(final List<TagesaggregatModel> tagesaggregate) {
        return tagesaggregate
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(TagesaggregatModel::getMqId))
                .values()
                .parallelStream()
                .map(MesswertUtils::avarageTagesaggregatCountingValuesByNumberOfElements)
                .toList();
    }
}
