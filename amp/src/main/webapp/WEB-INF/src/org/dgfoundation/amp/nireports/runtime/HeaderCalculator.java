package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.ReportHeadingCell;

/**
 * An implementation used for rasterizing the headers. Populates {@link Column#reportHeaderCell}.
 * It is quite messy because it is imperative code with heuristics. Long term we might want to have it a pluggable strategy. It is not needed a.t.m. so enjoy the code!
 * @author Dolghier Constantin
 *
 */
public class HeaderCalculator {

    protected final NiReportsEngine engine;
    
    /**
     * constructs an instance of the rasterizer. 
     * @param engine the context to be used as datasource for the various heuristics driving the depth allocation
     */
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
    
    /**
     * recursively rasterizes a column tree by populating {@link Column#reportHeaderCell}
     * @param c the root of the tree to rasterize
     * @param totalRowSpan the total rowspan available for the tree
     * @param startingDepth the depth (y-position, 0-based) at which the root should reside
     * @param startingColumn the x-position (0-based) at which the root should reside
     */
    public void calculatePositionInHeadingLayout(Column c, int totalRowSpan, int startingDepth, int startingColumn) {
        if (c instanceof CellColumn) {
            // leaves always take the whole resource allocation
            c.reportHeaderCell = new ReportHeadingCell(startingDepth, totalRowSpan, totalRowSpan, startingColumn, getWidth(c), c.name);
            return;
        }
        
        // this is not a leaf column, so let's distribute the rowSpan
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
    
    /**
     * a heuristic to calculate the selfrowspan of a column given the total depth availability
     * @param gc
     * @param totalRowSpan
     * @return
     */
    protected int computeSelfRowSpan(GroupColumn gc, int totalRowSpan) {
        if (gc.splitCell != null)
            if (gc.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_COLUMN) || gc.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_MEASURE))
                return totalRowSpan - calculateChildrenMaxRowSpan(gc); // vertically-split columns and measure take the maximum possible depth, leaving a minimum for the children
        
        return computeSelfRowSpanNoSplit(gc);
    }
    
    protected int computeSelfRowSpanNoSplit(GroupColumn gc) {
        if (gc.name.equals(NiReportsEngine.ROOT_COLUMN_NAME))
            return 1; // artificial root always gets 1, since it is not going to be displayed anyway

        if (gc.hierarchicalName.equals(FUNDING_COLUMN_PATH))
            return 1; // funding always gets 1, since it is used for grouping measures and bears no other useful or variable information

        if (gc.hierarchicalName.equals(TOTALS_COLUMN_PATH)) {
            /** Totals have the same structure as Funding, except that they are not being split pre-measure (a.t.m. this means time-based splitting). 
             * Thus, to align the "Totals" and "Funding" subtrees, "Totals" will get the depth got by "Funding" (e.g. 1) plus the depth got by the temporal columns (e.g. 1 each)
             * NiReportsEngine exports these in the generic name NiReportsEngine.premeasureSplitDepth
             */
            return 1 + engine.premeasureSplitDepth; 
        }

        return 1;
    }
    
    protected final static String FUNDING_COLUMN_PATH = String.format("%s / %s", NiReportsEngine.ROOT_COLUMN_NAME, NiReportsEngine.FUNDING_COLUMN_NAME);
    protected final static String TOTALS_COLUMN_PATH = String.format("%s / %s", NiReportsEngine.ROOT_COLUMN_NAME, NiReportsEngine.TOTALS_COLUMN_NAME);
}
