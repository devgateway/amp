package org.dgfoundation.amp.nireports.amp.indicators;

import org.apache.commons.collections.ComparatorUtils;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.Comparator;

/**
 * NiCell comparator for indicator values.
 * Indicator cells are ordered by indicator hierarchy coordinates.
 * This behaviour allows to align multiple indicator values inside one cell in such a way that nth value from any
 * indicator column is related to the same indicator usage.
 * Lastly the values in the cell are ordered by text value, because Indicator Sectors can have multiple values for
 * the same indicator.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorCellComparator implements Comparator<NiCell> {

    private static final Comparator nullLowComparator =
            ComparatorUtils.nullLowComparator(ComparatorUtils.naturalComparator());

    public IndicatorCellComparator(NiDimensionUsage indicatorDimensionUsage) {
        this.indicatorDimensionUsage = indicatorDimensionUsage;
    }

    private NiDimensionUsage indicatorDimensionUsage;

    @Override
    public int compare(NiCell c1, NiCell c2) {
        int c = nullLowComparator.compare(getIndicatorIdFrom(c1), getIndicatorIdFrom(c2));
        if (c != 0) {
            return c;
        }
        return c1.getCell().compareTo(c2.getCell());
    }

    private Object getIndicatorIdFrom(NiCell niCell) {
        return niCell.getCell().coordinates.get(indicatorDimensionUsage).id;
    }
}
