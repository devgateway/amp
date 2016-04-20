package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.ReportHeadingCell;

/**
 * class used for calculating {@link Column#reportHeaderCell}
 * It is quite messy because it is imperative code with heuristics
 * @author Dolghier Constantin
 *
 */
public class HeaderCalculator {

	protected final NiReportsEngine engine;
	public HeaderCalculator(NiReportsEngine engine) {
		this.engine = engine;
	}
	
	public void calculate(GroupColumn root) {
		calculatePositionInHeadingLayout(root, calculateTotalRowSpan(root), 0, 0);
	}
	
	/**
	 * the total <b>rowspan</b> of this column and all of this subcolumns in the report's heading<br />
	 * for the rowspan of this column per se (the number of columns needed to display its title), please see {@link #getNewRowSpan()} <br />
	 * only called once per item when initialized CRD
	 * @return
	 */
	public int calculateTotalRowSpan(Column c) {
		if (c instanceof CellColumn) return 1;
		
		GroupColumn gc = (GroupColumn) c;
		return calculateChildrenMaxRowSpan(gc) + computeSelfRowSpanNoSplit(gc);
	}
	
	public void calculatePositionInHeadingLayout(Column c, int totalRowSpan, int startingDepth, int startingColumn) {
		if (c instanceof CellColumn) {
			c.reportHeaderCell = new ReportHeadingCell(startingDepth, totalRowSpan, totalRowSpan, startingColumn, getWidth(c), c.name);
			return;
		}
		
		GroupColumn gc = (GroupColumn) c;
		int selfRowSpan = computeSelfRowSpan(gc, totalRowSpan);

		//selfRowSpan = getRowSpanInHeading_internal();
		if (selfRowSpan <= 0)
			throw new RuntimeException("selfRowSpan should be >= 1!");
		
		c.reportHeaderCell = new ReportHeadingCell(startingDepth, totalRowSpan, selfRowSpan, startingColumn, getWidth(c), gc.name);
		
		int startColumnSum = 0;
		for(Column item:gc.getSubColumns()) {
			calculatePositionInHeadingLayout(item, totalRowSpan - c.reportHeaderCell.getRowSpan(), startingDepth + c.reportHeaderCell.getRowSpan(), startingColumn + startColumnSum);
			startColumnSum += getWidth(item);
		}

	}
	
	/** returns the width of a Column in the header. Each leaf counts for one */
	protected int getWidth(Column c) {
		if (c instanceof CellColumn) return 1;
		
		GroupColumn gc = (GroupColumn) c;
		int ret = 0;
		for(Column column:gc.getSubColumns()){			
			ret += getWidth(column);
		}
		return Math.max(1, ret); // at least the column title
	}
	
    protected int calculateChildrenMaxRowSpan(GroupColumn gc) {    	
		int maxColSpan = 0;
		for(Column c:gc.getSubColumns()) {
			maxColSpan = Math.max(maxColSpan, calculateTotalRowSpan(c));
		}
		return maxColSpan;
    }
    
	protected int computeSelfRowSpan(GroupColumn gc, int totalRowSpan) {
		if (gc.splitCell != null)
			if (gc.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_COLUMN) || gc.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_MEASURE))
				return totalRowSpan - calculateChildrenMaxRowSpan(gc);
		
		return computeSelfRowSpanNoSplit(gc);
	}
	
	protected int computeSelfRowSpanNoSplit(GroupColumn gc) {
		if (gc.name.equals(NiReportsEngine.ROOT_COLUMN_NAME))
			return 1;

		if (gc.hierarchicalName.equals("RAW / Funding"))
			return 1;

		if (gc.hierarchicalName.equals("RAW / Totals"))
			return 1 + engine.premeasureSplitDepth;

		return 1;
	}
}
