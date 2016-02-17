package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a cell which stays in the output of NiReports
 * @author Dolghier Constantin
 *
 */
public abstract class NiOutCell implements Comparable {
	
	/**
	 * this is for debugging reasons; the final way the cells looks is to be decided by the export phase
	 * @return
	 */
	public abstract String getDisplayedValue();
	
	public abstract<K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn);
	
	@Override
	public String toString() {
		return getDisplayedValue();
	}
}
