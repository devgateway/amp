package org.dgfoundation.amp.nireports.output;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

import static org.dgfoundation.amp.algo.AmpCollections.sorted;
import static org.dgfoundation.amp.algo.AmpCollections.orderedListWrapper;

/**
 * a cell which holds a text
 * @author Dolghier Constantin
 *
 */
public class NiDateCell extends NiOutCell {
	/**
	 * the main entityId. In case the cell holds a multitude of them, an arbitrary one of them will be stored. Values <= 0 mean "no entity" 
	 */
	public final long entityId; 
	
	/**
	 * never null for date cells
	 */
	public final Map<Long, LocalDate> entitiesIdsValues;
	
	public final List<LocalDate> sortedValues;
	
	@SuppressWarnings("rawtypes")
	public final Comparable comparableToken;
	
	public NiDateCell(long entityId, Map<Long, LocalDate> entitiesIdsValues) {
		this.entityId = entityId;
		this.entitiesIdsValues = entitiesIdsValues;
		this.sortedValues = sorted(entitiesIdsValues.values());
		this.comparableToken = orderedListWrapper(sortedValues);
	}
		
	@Override
	public int compareTo(Object o) {
		NiDateCell ntc = (NiDateCell) o;
		return this.comparableToken.compareTo(ntc.comparableToken);
	}

	@Override
	public String getDisplayedValue() {
		return sortedValues.isEmpty() ? "" : sortedValues.toString();
	}

	@Override
	public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
		return visitor.visit(this, niCellColumn);
	}

}
