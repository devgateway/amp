/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;


/**
 * A wrapper for {@link ReportAreaImpl} that stores for each report area element a link to its parent
 * and link to its next sibling to allow multi-directional navigation through the report result.   
 * @author Nadejda Mandrescu
 *
 */
public class ReportAreaMultiLinked extends ReportAreaImpl {
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
		if (area.getChildren() != null && area.getChildren().size() > 0) {
			this.children = new ArrayList<ReportArea>(area.getChildren().size());
			configureSibling(this, area.getChildren().iterator(), this.children);
			Collections.reverse(children);
		}
	}
	
	private ReportAreaMultiLinked configureSibling(ReportAreaMultiLinked parent, Iterator<ReportArea> iter, List<ReportArea> siblingsList) {
		if (iter.hasNext()) {
			ReportAreaMultiLinked newArea = new ReportAreaMultiLinked(iter.next(), parent, configureSibling(parent, iter, siblingsList));
			siblingsList.add(newArea);
			return newArea;
		}
		return null;
	}
}
