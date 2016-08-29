package org.dgfoundation.amp.nireports.behaviours;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiDateCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * @author Octavian Ciubotaru
 */
public class MinTransactionDateBehaviour extends AbstractComputedBehaviour<NiDateCell> {

    public final static String TRANSACTION_DATE = "transaction_date";

    public final static MinTransactionDateBehaviour instance = new MinTransactionDateBehaviour(TimeRange.MONTH);

    private MinTransactionDateBehaviour(TimeRange timeRange) {
        super(timeRange);
    }

    @Override
    public NiDateCell doHorizontalReduce(List<NiCell> cells) {
        return wrap(cells.stream().map(this::getTransactionDate).reduce(this::minDate));
    }

    private LocalDate getTransactionDate(NiCell c) {
        return (LocalDate) ((CategAmountCell) c.getCell()).metaInfo.getMetaInfo(TRANSACTION_DATE).v;
    }

    private LocalDate minDate(LocalDate minDate, LocalDate date) {
        return minDate == null || minDate.compareTo(date) > 0 ? date : minDate;
    }

    @Override
    public NiOutCell doVerticalReduce(Collection<NiDateCell> cells) {
        return wrap(cells.stream().map(c -> c.entitiesIdsValues.get(-1L)).reduce(this::minDate));
    }

    private NiDateCell wrap(Optional<LocalDate> date) {
        if (!date.isPresent()) {
            return getZeroCell();
        }
        return new NiDateCell(-1L, Collections.singletonMap(-1L, date.get()));
    }

    @Override
    public NiDateCell getZeroCell() {
        return new NiDateCell(-1L, Collections.emptyMap());
    }
}
