package de.muenchen.dave.geodataeai.domain.model.messwerte;

import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Die Klasse dient zum Sammeln von Intervallen mittels
 * {@link SubintervalCollectorModel#collectIfSubintervalIsWithinVonBis}
 * und der anschließenden Vereinigung der gesammelten Intervalle mit
 * {@link SubintervalCollectorModel#unificationOfCollectedSubintervals}.
 */
@Data
public class SubintervalCollectorModel {

    private final IntervalModel interval;

    private final List<IntervalModel> subIntervals;

    public SubintervalCollectorModel(final IntervalModel interval) {
        this.interval = interval;
        this.subIntervals = new ArrayList<>();
    }

    public boolean collectIfSubintervalIsWithinVonBis(final IntervalModel subinterval) {
        final var isSubintervalWithinVonBis = isSubintervalWithinVonBis(subinterval);
        if (isSubintervalWithinVonBis) {
            subIntervals.add(subinterval);
        }
        return isSubintervalWithinVonBis;

    }

    protected boolean isSubintervalWithinVonBis(final IntervalModel subinterval) {
        return (subinterval.getDatumUhrzeitVon().isEqual(interval.getDatumUhrzeitVon())
                || subinterval.getDatumUhrzeitVon().isAfter(interval.getDatumUhrzeitVon()))
                && (subinterval.getDatumUhrzeitBis().isEqual(interval.getDatumUhrzeitBis())
                        || subinterval.getDatumUhrzeitBis().isBefore(interval.getDatumUhrzeitBis()))
                && (!subinterval.getDatumUhrzeitBis().isBefore(interval.getDatumUhrzeitVon()))
                && (!subinterval.getDatumUhrzeitVon().isAfter(interval.getDatumUhrzeitBis()));
    }

    public IntervalModel unificationOfCollectedSubintervals() {
        final var mqId = interval.getMqId();
        final var von = interval.getDatumUhrzeitVon();
        final var bis = interval.getDatumUhrzeitBis();
        final var tagesTyp = ObjectUtils.isNotEmpty(interval.getTagesTyp())
                ? interval.getTagesTyp()
                : subIntervals.stream().findFirst().orElseGet(IntervalModel::new).getTagesTyp();
        final var summedSubintervall = sumSubintervals();
        final var joinedIntervalls = MesswertUtils.sumCountingValuesOfIntervalsAndReturnNewInterval(interval, summedSubintervall);
        joinedIntervalls.setMqId(mqId);
        joinedIntervalls.setDatumUhrzeitVon(von);
        joinedIntervalls.setDatumUhrzeitBis(bis);
        joinedIntervalls.setTagesTyp(tagesTyp);
        return joinedIntervalls;
    }

    protected IntervalModel sumSubintervals() {
        return subIntervals
                .stream()
                .reduce(new IntervalModel(), MesswertUtils::sumCountingValuesOfIntervalsAndReturnNewInterval);
    }

}
