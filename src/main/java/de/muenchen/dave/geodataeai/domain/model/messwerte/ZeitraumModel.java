package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZeitraumModel {

    private LocalDate startDate;

    private LocalDate endDate;

}
