package org.dgfoundation.amp.nireports.output.sorting;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a class which knows how to compare two ids which denote two entitites (e.g. activities) 
 * @author Dolghier Constantin
 *
 */
public class IdsComparator implements Comparator<Long> {

	final LinkedHashMap<CellColumn, Boolean> colsSorting;
	final NiColumnReportData crd;
	
	public IdsComparator(LinkedHashMap<CellColumn, Boolean> colsSorting, NiColumnReportData crd) {
		this.colsSorting = colsSorting;
		this.crd = crd;
	}
	
	@Override
	public int compare(Long idA, Long idB) {
		Comparator<NiOutCell> ascComp = Comparator.nullsFirst(Comparator.naturalOrder());
		Comparator<NiOutCell> descComp = ascComp.reversed();
		
		for(Map.Entry<CellColumn, Boolean> cs:colsSorting.entrySet()) {
			if (cs.getValue() == null)
				continue;
			Map<Long, NiOutCell> col = crd.contents.get(cs.getKey());
			NiOutCell cA = col.get(idA);
			NiOutCell cB = col.get(idB);
			Comparator<NiOutCell> comp = cs.getValue() ? ascComp : descComp;
			int delta = comp.compare(cA, cB);
			if (delta != 0)
				return delta;
		}
		return Long.compare(idA, idB);
	}	
}
