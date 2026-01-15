package de.muenchen.dave.geodataeai.infrastructure.entity.request.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FeatureServerLayer {
    STADTBEZIRKE("basis_wfs_feature", 8),

    MESSSTELLE("MDAS/LAPAS_VERKEHRSDETEKTOREN", 10),

    MESSQUERSCHNITT("MDAS/LAPAS_VERKEHRSDETEKTOREN", 20),

    MESSFAEHIGKEIT("MDAS/LAPAS_VERKEHRSDETEKTOREN", 30),

    MESSWERTE_INTERVALL("testinger", 999),

    MESSWERTE_TAGESAGGREGAT("testinger", 999);

    /**
     * Der Name des Service in welchem sich das Feature identifiziert mit der id befindet.
     */
    @Getter
    private final String serviceName;

    /**
     * Die id des Layer im FeatureServer für das jeweilige Feature.
     */
    @Getter
    private final int layerId;

}
