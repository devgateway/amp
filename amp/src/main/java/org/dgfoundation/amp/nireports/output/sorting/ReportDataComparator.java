package org.dgfoundation.amp.nireports.output.sorting;

import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * a class which knows how to compare two {@link NiReportData} instances according to the trail cell values in given leaf headers
 * @author Dolghier Constantin
 *
 */
public class ReportDataComparator implements Comparator<NiReportData> {

    final LinkedHashMap<CellColumn, Boolean> colsSorting;
    
    public ReportDataComparator(LinkedHashMap<CellColumn, Boolean> colsSorting) {
        this.colsSorting = colsSorting;
    }
    
    @Override
    public int compare(NiReportData nrd1, NiReportData nrd2) {
        if (colsSorting == null || colsSorting.isEmpty())
            return 0;
        Comparator<NiOutCell> ascComp = Comparator.nullsFirst(Comparator.naturalOrder());
        Comparator<NiOutCell> descComp = ascComp.reversed();
        
        for(Map.Entry<CellColumn, Boolean> cs:colsSorting.entrySet()) {
            if (cs.getValue() == null)
                continue;
            NiOutCell cA = nrd1.trailCells.get(cs.getKey());
            NiOutCell cB = nrd2.trailCells.get(cs.getKey());
            Comparator<NiOutCell> comp = cs.getValue() ? ascComp : descComp;
            int delta = comp.compare(cA, cB);
            if (delta != 0)
                return delta;
        }
        return 0;
    }
}
