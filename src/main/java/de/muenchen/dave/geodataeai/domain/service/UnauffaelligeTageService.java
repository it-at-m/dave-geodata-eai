package de.muenchen.dave.geodataeai.domain.service;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapper;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.UnauffaelligerTagModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.ZeitraumModel;
import de.muenchen.dave.geodataeai.domain.service.tagesaggregat.TagesaggregatExtractionService;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnauffaelligeTageService {

    private final MessstelleService messstelleService;

    private final TagesaggregatExtractionService tagesaggregatExtractionService;

    private final FeatureResponseDomainMapper featureResponseDomainMapper;

    /**
     * Ermittelt für alle Messstellen die unauffälligen Tage für den gegebenen Zeitraum.
     *
     * Ein Tag ist für eine Messstelle unauffällig, wenn für diesen Tag Tagesaggregate existieren.
     *
     * @param startDate für den Ermittlungszeitraum.
     * @param endDate für den Ermittlungszeitraum.
     * @return die Liste der unauffälligen Tage.
     * @throws FeatureRequestFailedException
     */
    public List<UnauffaelligerTagModel> getUnauffaelligeTageForEachMessstelle(final LocalDate startDate, final LocalDate endDate)
            throws FeatureRequestFailedException {
        final var mstIdByMqId = CollectionUtils.emptyIfNull(messstelleService.getMessstellen().getFeatures())
                .parallelStream()
                .map(FeatureModel::getProperties)
                .filter(Objects::nonNull)
                // Mapping MqIds by MstIds
                .collect(
                        Collectors.groupingByConcurrent(MessstelleModel::getMstId,
                                Collectors.flatMapping(
                                        messstelle -> CollectionUtils.emptyIfNull(messstelle.getMessquerschnitte().getFeatures())
                                                .stream()
                                                .map(FeatureModel::getProperties)
                                                .filter(Objects::nonNull),
                                        Collectors.toList())))
                // invertieren der Map -> MstIds by MqIds
                .entrySet()
                .parallelStream()
                .flatMap(messquerschnitteByMstId -> messquerschnitteByMstId.getValue().stream()
                        .map(messquerschnitt -> Map.entry(messquerschnitteByMstId.getKey(), messquerschnitt.getMqId())))
                // Es wird nur einmal die MstId benötigt, daher werden doppelte Einträge entfernt
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (mstId1, stId2) -> mstId1))
                .entrySet()
                .parallelStream()
                // Es wird nur einmal die MqId benötigt, daher werden doppelte Einträge entfernt
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (integer, integer2) -> integer));

        final var zeitraum = new ZeitraumModel(startDate, endDate);
        return tagesaggregatExtractionService.getTagesaggregate(mstIdByMqId.keySet(), List.of(zeitraum))
                .parallel()
                .map(tagesaggregat -> featureResponseDomainMapper.tagesaggregat2UnauffaelligerTag(tagesaggregat, mstIdByMqId))
                .distinct()
                .toList();
    }

}
