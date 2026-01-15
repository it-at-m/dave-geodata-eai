package de.muenchen.dave.geodataeai.domain.mapper;

import de.muenchen.dave.geodataeai.configuration.MapstructConfiguration;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalsForMqIdModel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface IntervalMapper {

    IntervalsForMqIdModel deepCopy(final IntervalsForMqIdModel model);

    List<IntervalModel> deepCopy(final List<IntervalModel> intervals);

    IntervalModel deepCopy(final IntervalModel interval);

}
