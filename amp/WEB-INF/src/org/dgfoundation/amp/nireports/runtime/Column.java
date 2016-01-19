package org.dgfoundation.amp.nireports.runtime;

import java.util.List;
import java.util.function.Consumer;
import org.dgfoundation.amp.nireports.ReportHeadingCell;

/**
 * column-with-contents
 *
 */
public abstract class Column {
	public final String name;
	
	/**
	 * might be null
	 */
	protected final GroupColumn parent;
	
	protected final String hierarchicalName;
	
	/**
	 * will be null before {@link #calculatePositionInHeadingLayout(int, int, int)} has been called. This one will be called last step in report execution
	 */
	protected ReportHeadingCell reportHeaderCell;
	
	public abstract void forEachCell(Consumer<NiCell> acceptor);
	public abstract GroupColumn verticallySplitByCategory(VSplitStrategy strategy);
	public abstract String debugDigest(boolean withContents);
	
	// header-related stuff
	/**
	 * the total <b>rowspan</b> of this column and all of this subcolumns in the report's heading<br />
	 * for the rowspan of this column per se (the number of columns needed to display its title), please see {@link #getNewRowSpan()} <br />
	 * only called once per item when initialized CRD
	 * @return
	 */
	public abstract int calculateTotalRowSpan();
	
	public abstract void calculatePositionInHeadingLayout(int totalRowSpan, int startingDepth, int startingColumn);
	/** returns the width of a Column in the header. Each leaf counts for one */
	public abstract int getWidth();
	/**
	 * @return an ordered list of the leaf columns
	 */
	public abstract List<CellColumn> getLeafColumns();
	public abstract List<Column> getChildrenStartingAtDepth(int depth);
			
	protected Column(String name, GroupColumn parent) {
		this.name = name;
		this.parent = parent;
		this.hierarchicalName = parent == null ? name : String.format("%s / %s", parent.getHierName(), name);
	}
	
	public String getHierName() {
		return hierarchicalName;
	}
	
	public GroupColumn getParent() {
		return parent;
	}

	public ReportHeadingCell getReportHeaderCell() {
		return reportHeaderCell;
	}

	@Override
	public String toString() {
		return debugDigest(false);
		//return String.format("%s %s", this.getClass().getSimpleName(), getHierName());
	}
	
	@Override
	public int hashCode() {
		return getHierName().hashCode();
	}
	
	@Override
	public boolean equals(Object oth) {
		Column other = (Column) oth;
		return getHierName().equals(other.getHierName());
	}
}
