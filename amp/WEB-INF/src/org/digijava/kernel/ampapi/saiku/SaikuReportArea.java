/**
 * 
 */
package org.digijava.kernel.ampapi.saiku;

import org.dgfoundation.amp.newreports.ReportAreaImpl;

/**
 * This is Saiku specific report area, that also stores the location of the data in Saiku structures, to facilitate the sorting during post-processing phase.
 * TODO: delete when we get rid of Mondrian
 * @see ReportAreaImpl
 * @author Nadejda Mandrescu
 *
 */
@Deprecated
public class SaikuReportArea extends ReportAreaImpl {
	private int origId;
	private int origLeafId = -1; //not set
	private boolean isTotalRow = false;

	/**
	 * @return row index from the Saiku CellDataSet structure of the current report area content
	 */
	public int getOrigId() {
		return origId;
	}

	/**
	 * @param origId - the original index, e.g. row index from the Saiku CellDataSet structure of the current report area content
	 */
	public void setOrigId(int origId) {
		this.origId = origId;
	}

	/**
	 * @return true if this the total row content
	 */
	public boolean isTotalRow() {
		return isTotalRow;
	}

	/**
	 * @param isTotalRow the isTotalRow to set
	 */
	public void setTotalRow(boolean isTotalRow) {
		this.isTotalRow = isTotalRow;
	}

	/**
	 * @return the origLeafId
	 */
	public int getOrigLeafId() {
		return origLeafId;
	}

	/**
	 * @param origLeafId the origLeafId to set
	 */
	public void setOrigLeafId(int origLeafId) {
		this.origLeafId = origLeafId;
	}
	
}
