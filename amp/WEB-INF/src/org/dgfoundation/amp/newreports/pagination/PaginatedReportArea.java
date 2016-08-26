/**
 * 
 */
package org.dgfoundation.amp.newreports.pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;


/**
 * Stores partial report area, with statistical information regarding the full report area,
 * like actual total count of children  
 * 
 * @author Dolghier Constantin
 */
public class PaginatedReportArea extends ReportAreaImpl {
	
	protected final transient Set<Long> currentLeafActivities; 
	public PaginatedReportArea(ReportArea src, List<PaginatedReportArea> children, int ownerId) {
		this.contents = src.getContents();
		this.owner = src.getOwner();
		this.children = children == null ? null : new ArrayList<ReportArea>(children);
//		this.nrEntities = src.getNrEntities();
		this.currentLeafActivities = collectIds(ownerId);
	}

	/**
	 * collects all the really-used ids in the paginated fragment
	 * @return
	 */
	protected Set<Long> collectIds(int ownerId) {
		if (owner != null && this.children.isEmpty())
			return new HashSet<>(Arrays.asList((long)ownerId)); // this is a leaf with no children
		
//		if (this.getOwner() != null && this.getOwner().columnName.equals(AreaOwner.LEAF))
//			return new HashSet<>(Arrays.asList(this.getOwner().id)); // this is a leaf with no children
//		
		Set<Long> res = new HashSet<>();		
		if (this.children != null) {
			for (ReportArea ra : this.children) {
				PaginatedReportArea pra = (PaginatedReportArea) ra;
				res.addAll(pra.currentLeafActivities);
			}
//			return this.children.stream().map(z -> (PaginatedReportArea) z).flatMap(z -> z.currentLeafActivities.stream()).collect(toSet());
		}
		return res;
	}
	
	/** 
	 * @return the number of projects in this ReportArea unpaginated (includes activities which have been left out because of pagination)
	 */
//	public int getTotalLeafActivitiesCount() {
//		return getNrEntities();
//	}

	/**  
	 * @return the number of projects in this ReportArea paginated
	 */
	public int getCurrentLeafActivitiesCount() {
		return currentLeafActivities.size();
	}
}
