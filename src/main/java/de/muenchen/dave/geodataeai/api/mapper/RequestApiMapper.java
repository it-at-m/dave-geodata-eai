package de.muenchen.dave.geodataeai.api.mapper;

import de.muenchen.dave.geodataeai.api.dto.messwerte.MesswertRequestDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.TagesaggregatRequestDto;
import de.muenchen.dave.geodataeai.configuration.MapstructConfiguration;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatRequestModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface RequestApiMapper {

    MesswertRequestModel dto2Model(final MesswertRequestDto dto);

    TagesaggregatRequestModel dto2Model(final TagesaggregatRequestDto dto);

}
