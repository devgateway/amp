/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.dgfoundation.amp.newreports.ReportAreaImpl;

/**
 * Stores partial report area, with statistical information regarding the full report area,
 * like actual total count of children  
 * 
 * @author Nadejda Mandrescu
 */
public class PartialReportArea extends ReportAreaImpl {
	// configured to -1 to mark uninitialized state
	protected int totalChildrenCount = -1;
	
	public PartialReportArea() {
		super();
	}

	/**
	 * @return the actual total count of children, 
	 * while the current report area can have only a partial list
	 */
	public int getTotalChildrenCount() {
		if (totalChildrenCount == -1) {
			// initialize to default on 1st request
			if (this.getChildren() == null)
				totalChildrenCount = 0;
			else 
				totalChildrenCount = this.getChildren().size();
		}
		return totalChildrenCount;
	}

	/**
	 * @param totalChildrenCount the totalChildrenCount to set
	 */
	public void setTotalChildrenCount(int totalChildrenCount) {
		this.totalChildrenCount = totalChildrenCount;
	}
}
