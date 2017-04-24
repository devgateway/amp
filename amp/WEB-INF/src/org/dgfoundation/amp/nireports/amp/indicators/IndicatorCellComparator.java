package org.dgfoundation.amp.nireports.amp.indicators;

import java.util.Comparator;

import org.apache.commons.collections.ComparatorUtils;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * NiCell comparator for indicator values.
 * Indicator level cells are ordered by {@link org.dgfoundation.amp.nireports.Cell.entityId}.
 * Indicator connection level cells are ordered first by indicator id from metadata and then by entityId.
 * This behaviour allows to align multiple indicator values inside one cell in such a way that nth value from any
 * indicator column is related to the same indicator usage.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorCellComparator implements Comparator<NiCell> {

    public static final Comparator<NiCell> instance = new IndicatorCellComparator();

    private static final Comparator nullLowComparator =
            ComparatorUtils.nullLowComparator(ComparatorUtils.naturalComparator());

    @Override
    public int compare(NiCell c1, NiCell c2) {
        int c = nullLowComparator.compare(getIndicatorIdFrom(c1), getIndicatorIdFrom(c2));
        if (c != 0) {
            return c;
        }
        c = Long.compare(c1.getCell().entityId, c2.getCell().entityId);
        if (c != 0) {
            return c;
        }
        return c1.getCell().compareTo(c2.getCell());
    }

    private Object getIndicatorIdFrom(NiCell niCell) {
        if (niCell.getCell().getMetaInfo().hasMetaInfo(MetaCategory.INDICATOR_ID.category)) {
            return niCell.getCell().getMetaInfo().getMetaInfo(MetaCategory.INDICATOR_ID.category).getValue();
        }
        return null;
    }
}
