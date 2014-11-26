/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.dgfoundation.amp.newreports.ReportArea;
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
	protected int totalLeafChildrenCount = -1;
	
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

	/**
	 * @return the totalLeafChildrenCount
	 */
	public int getTotalLeafChildrenCount() {
		if (totalLeafChildrenCount == -1) {
			// initialize to default on 1st request
			totalLeafChildrenCount = 0;
			/* 
			 * if it has grand children => sum-up leaf totals of children, 
			 * otherwise consider the direct totals of the children
			 */
			if (this.getChildren() != null && this.getChildren().size() > 0) {
				// at the moment we have no mixed up areas at the same level, i.e. some with grand-children and some without grand-children, but all either have them or all do not have them
				PartialReportArea child = (PartialReportArea) this.getChildren().get(0);
				if (child.getChildren() == null || child.getChildren().size() == 0) {
					totalLeafChildrenCount = getTotalChildrenCount();
				} else {
					for (ReportArea areaChild : this.getChildren()) {
						totalLeafChildrenCount += ((PartialReportArea) areaChild).getTotalLeafChildrenCount();
					}
				}
			}
				
		}
		return totalLeafChildrenCount;
	}

	/**
	 * @param totalLeafChildrenCount the totalLeafChildrenCount to set
	 */
	public void setTotalLeafChildrenCount(int totalLeafChildrenCount) {
		this.totalLeafChildrenCount = totalLeafChildrenCount;
	}
}
