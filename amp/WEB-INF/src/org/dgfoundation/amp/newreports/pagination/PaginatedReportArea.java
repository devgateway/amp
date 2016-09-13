/**
 * 
 */
package org.dgfoundation.amp.newreports.pagination;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.reports.PartialReportArea;


/**
 * Stores partial report area, with statistical information regarding the full report area,
 * like actual total count of children  
 * 
 * @author Dolghier Constantin
 */
public class PaginatedReportArea extends ReportAreaImpl {
	
	/**
	 * might be null if unknown
	 */
	transient protected final Long ownerId;
	protected final transient Set<Long> currentLeafActivities;
	protected static final Logger logger = Logger.getLogger(PaginatedReportArea.class);
	
	public PaginatedReportArea(ReportArea src, List<PaginatedReportArea> children) {
		this.contents = src.getContents();
		this.nrEntities = getNrEntities(src);
		this.owner = src.getOwner();
		this.ownerId = getOwnerId(src);
		this.children = children == null ? null : new ArrayList<ReportArea>(children);
		this.currentLeafActivities = collectOwnerIds();//collectIds(ownerId);
	}

	protected int getNrEntities(ReportArea src) {
		if (src instanceof PartialReportArea)
			return ((PartialReportArea) src).getTotalLeafActivitiesCount();
		return 0;
	}
	
	protected Long getOwnerId(ReportArea src) {
		if (src instanceof PartialReportArea) {
			PartialReportArea pra = (PartialReportArea) src;
			if (pra.getLeafActivities() != null && pra.getLeafActivities().size() == 1)
				return Long.valueOf(pra.getLeafActivities().iterator().next());
		}
		return null;
	}
		
	/**
	 * collects all the really-used ids in the paginated fragment
	 * @return
	 */
	protected Set<Long> collectOwnerIds() {
		Set<Long> res = new HashSet<>();
		if (this.children != null) {
			for (ReportArea ra : this.children) {
				PaginatedReportArea pra = (PaginatedReportArea) ra;
				res.addAll(pra.currentLeafActivities);
			}
		}
		if (this.ownerId != null)
			res.add(this.ownerId);
		return res;
	}
	
	/**  
	 * @return the number of projects in this ReportArea paginated
	 */
	public int getCurrentLeafActivitiesCount() {
		return currentLeafActivities.size();
	}
	
	public int getTotalLeafActivitiesCount() {
		return this.nrEntities;
	}
}
