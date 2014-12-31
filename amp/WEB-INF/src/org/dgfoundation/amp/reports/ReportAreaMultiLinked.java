/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.LinkedList;
import java.util.ListIterator;

import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;


/**
 * A wrapper for {@link ReportAreaImpl} that stores for each report area element a link to its parent
 * and link to its next sibling to allow multi-directional navigation through the report result.   
 * @author Nadejda Mandrescu
 *
 */
public class ReportAreaMultiLinked extends PartialReportArea {
	/** parent area, null for root area */
	public final ReportAreaMultiLinked parent;
	/** next sibling area, null for last child */
	public final ReportAreaMultiLinked next;
	
	/**
	 * Initializes a wrapper for {@link ReportAreaImpl} to 
	 * @param parent - parent area, null for root area
	 * @param next -next sibling area, null for last child
	 */
	public ReportAreaMultiLinked(ReportArea area, ReportAreaMultiLinked parent, ReportAreaMultiLinked next) {
		this.parent = parent;
		this.next = next;
		init(area);
	}
	
	private void init(ReportArea area) {
		this.owner = area.getOwner();
		this.contents = area.getContents();
		if (area instanceof PartialReportArea) {
			PartialReportArea pra = (PartialReportArea) area;
			this.totalLeafActivitiesCount = pra.totalLeafActivitiesCount;
			this.leafActivities = pra.leafActivities;
		}
		if (area.getChildren() != null && area.getChildren().size() > 0) {
			LinkedList<ReportArea> cList = new LinkedList<ReportArea>();
			ListIterator<ReportArea> iter = area.getChildren().listIterator(area.getChildren().size());
			ReportAreaMultiLinked prevSibling = null;
			while (iter.hasPrevious()) {
				prevSibling = new ReportAreaMultiLinked(iter.previous(), this, prevSibling);
				cList.addFirst(prevSibling);
			}
			this.children = cList;
		}
	}
}
