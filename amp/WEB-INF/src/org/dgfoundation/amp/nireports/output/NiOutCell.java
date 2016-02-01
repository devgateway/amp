package org.dgfoundation.amp.nireports.output;

/**
 * a cell which stays in the output of NiReports
 * @author Dolghier Constantin
 *
 */
public interface NiOutCell extends Comparable {
	
	/**
	 * this is for debugging reasons; the final way the cells looks is to be decided by the export phase
	 * @return
	 */
	public String getDisplayedValue();
	
	public<K> K accept(CellVisitor<K> visitor);
	
}
