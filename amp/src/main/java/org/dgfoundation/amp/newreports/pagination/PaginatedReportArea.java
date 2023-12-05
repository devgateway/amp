/**
 * 
 */
package org.dgfoundation.amp.newreports.pagination;

import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Stores partial report area, with statistical information regarding the full report area,
 * like actual total count of children  
 * 
 * @author Dolghier Constantin
 */
public class PaginatedReportArea extends ReportAreaImpl {
    
    protected final transient Set<Long> currentLeafActivities; 
            
    public PaginatedReportArea(ReportArea src, List<PaginatedReportArea> children) {
        this.contents = src.getContents();
        this.owner = src.getOwner();
        this.children = children == null ? null : new ArrayList<>(children);
        this.nrEntities = src.getNrEntities();
        this.currentLeafActivities = collectIds();
    }

    /**
     * collects all the really-used ids in the paginated fragment
     * @return
     */
    protected Set<Long> collectIds() {
        if (this.getOwner() != null && this.getOwner().columnName.equals(AreaOwner.LEAF))
            return new HashSet<>(Arrays.asList(this.getOwner().id)); // this is a leaf with no children
        if (this.children != null) {
            return this.children.stream().map(z -> (PaginatedReportArea) z).flatMap(z -> z.currentLeafActivities.stream()).collect(toSet());
        }
        return Collections.emptySet();
    }
    
    /** 
     * @return the number of projects in this ReportArea unpaginated (includes activities which have been left out because of pagination)
     */
    public int getTotalLeafActivitiesCount() {
        return getNrEntities();
    }

    /**  
     * @return the number of projects in this ReportArea paginated
     */
    public int getCurrentLeafActivitiesCount() {
        return currentLeafActivities.size();
    }
}
