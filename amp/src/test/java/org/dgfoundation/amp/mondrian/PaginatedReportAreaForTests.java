/**
 * 
 */
package org.dgfoundation.amp.mondrian;

import java.util.Iterator;
import java.util.Map;

import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.pagination.PartialReportArea;

/**
 * 
 * @author Nadejda Mandrescu
 */
public class PaginatedReportAreaForTests extends ReportAreaForTests {

	protected int currentCount = -1;
	protected int totalCount = -1;
	
	public PaginatedReportAreaForTests withCounts(int currentCount, int totalCount) {
		this.currentCount = currentCount;
		this.totalCount = totalCount;
		return this;
	}
	
	@Override
	public String getDifferenceAgainst(ReportArea output) {
		String err = super.getDifferenceAgainst(output);
		if (err == null && !PartialReportArea.class.isAssignableFrom(output.getClass())) {
			return "not a pagination request, expecting PartialReportArea to be used";
		}
		if (err == null) {
			err = compareCounts(output);
		}
		return err;
	}
	
	public String compareCounts(ReportArea output) {
		/* till this place children content & size are verified =>
		 * can simplify on checks and just compare counts
		 */
		PartialReportArea partial = (PartialReportArea) output;
		if (this.currentCount != partial.getCurrentLeafActivitiesCount()) {
			return "different current count for " + partial.getContents();
		}
		if (this.totalCount != partial.getTotalLeafActivitiesCount()) {
			return "different total count for " + partial.getContents();
		}
		String err = null;
		if (partial.getChildren() != null && partial.getChildren().size() > 0) {
			Iterator<ReportArea> pChild = partial.getChildren().iterator(); 
			Iterator<ReportArea> thisChild = getChildren().iterator();
			while (pChild.hasNext() && err != null) {
				err = ((PaginatedReportAreaForTests) thisChild.next()).compareCounts(pChild.next());
			}
		}
		return err;
	}
	
	@Override
	public PaginatedReportAreaForTests withOwner(AreaOwner owner) {
		super.withOwner(owner);
		return this;
	}
	
	@Override
	public PaginatedReportAreaForTests withChildren(ReportArea...children) {
		super.withChildren(children);
		return this;
	}

	@Override
	public PaginatedReportAreaForTests withContents(Map<ReportOutputColumn, ReportCell> contents) {
		super.withContents(contents);
		return this;
	}
	
	@Override
	public PaginatedReportAreaForTests withContents(String...contents) {
		super.withContents(contents);
		return this;
	}
	
}
