package org.dgfoundation.amp.nireports.output.sorting;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a class which knows how to compare two ids which denote two entities (e.g. activities) 
 * @author Dolghier Constantin
 *
 */
public class IdsComparator implements Comparator<NiRowId> {

    private static final Comparator ASC_COMP = Comparator.nullsFirst(Comparator.naturalOrder());
    private static final Comparator DESC_COMP = ASC_COMP.reversed();

    /**
     * The keys are the output-columns to sort for, in meaningful order of iteration. The values are either true
     * (ascending) or false (descending).
     */
    private final LinkedHashMap<CellColumn, Boolean> colsSorting;
    
    /**
     * the report leaf whose entities are being sorted
     */
    private final NiColumnReportData crd;
    
    public IdsComparator(LinkedHashMap<CellColumn, Boolean> colsSorting, NiColumnReportData crd) {
        this.colsSorting = colsSorting;
        this.crd = crd;
    }
    
    @Override
    public int compare(NiRowId idA, NiRowId idB) {
        for(Map.Entry<CellColumn, Boolean> cs:colsSorting.entrySet()) {
            if (cs.getValue() == null)
                continue;
            Map<NiRowId, NiOutCell> col = crd.contents.get(cs.getKey());
            NiOutCell cA = col.get(idA);
            NiOutCell cB = col.get(idB);
            Comparator comp = cs.getValue() ? ASC_COMP : DESC_COMP;
            int delta = comp.compare(cA, cB);
            if (delta != 0)
                return delta;
        }
        return ASC_COMP.compare(idA, idB);
    }
}
