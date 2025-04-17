package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.nireports.runtime.*;

import java.util.*;

/**
 * the header layout of a NiReport. The headers of a report are a forest of trees. The trees are as follows:
 * <ol>
 * <li>each of the columns, regardless of whether it is a hierarchy. Their names equal the name of the respective column</li>
 * <li>In case the report has a {@link GroupingCriteria} different from {@link GroupingCriteria#GROUPING_TOTALS_ONLY}, there is a root for time-split measures. Its name is {@link NiReportsEngine#FUNDING_COLUMN_NAME}</li>
 * <li>a tree holding the totals, in case any of the columns or measures present in the report generate totals. Please see {@link VSplitStrategy#getTotalSubcolumnName()}</li>
 * </ol> <br />
 * In order to simplify the code handling the headers of a report, an artificial root node named {@link NiReportsEngine#ROOT_COLUMN_NAME} is created by NiReports Core: 
 * the natural headers are pushed as being its children. Thus, code which outputs headers in an user-visible way should skip the root node.
 * Please see {@link NiReportsFormatter#buildHeaders} for an example
 * @author Dolghier Constantin
 *
 */
public class NiHeaderInfo {
    /** the artificial root */
    public final GroupColumn rootColumn;
    
    /**
     * the leaves of the header. Since each {@link Column} holds a link to its parent column (in {@link Column#getParent()}), it is possible to do a full reconstruction of the tree by following up-pointers from leaves
     */
    public final List<CellColumn> leafColumns;
    
    /** the number of hierarchies in the structure of the headers*/
    public final int nrHierarchies;
    
    /**
     * the headers rasterized (e.g rendered as a bidimensional matrix). Since:
     * <ul>
     *  <li>a given column's width equals the sum of the widths of its children. A leaf column's width is always 1</li>
     *  <li>a column's height (also called <i>depth</i>) is dynamically calculated by NiReports Core based on a number of heuristics which should not be reimplemented or reverse engineered</li>
     * </ul>,
     * an individual headers' row is rasterized as a SortedMap 
     * rasterizedHeaders[i] = columns which start on row i of the header, SortedMap<startingColumn, Column>
     */
    public final List<SortedMap<Integer, Column>> rasterizedHeaders;
    
    /**
     * given as input the artificial root column, calculates the columns' widths and heights (e.g. rasterizes the header) and constructs an instance of {@link NiHeaderInfo} <br />
     * {@link HeaderCalculator} is the rasterization implementation. This could, in principle, be made pluggable, but it is not needed ATM
     * @param engine
     * @param rootColumn
     * @param nrHierarchies
     */
    public NiHeaderInfo(NiReportsEngine engine, GroupColumn rootColumn, int nrHierarchies) {
        this.rootColumn = rootColumn;
        this.leafColumns = rootColumn.getLeafColumns();
        this.nrHierarchies = nrHierarchies;
        new HeaderCalculator(engine).calculate(rootColumn);
        this.rasterizedHeaders = Collections.unmodifiableList(buildRasterizedHeaders(rootColumn));
    }
        
    public List<SortedMap<Integer, Column>> buildRasterizedHeaders(GroupColumn rootColumn) {
        List<SortedMap<Integer, Column>> rh = new ArrayList<>();
        for(int i = 0; i < rootColumn.getReportHeaderCell().totalRowSpan; i++) {
            List<Column> cells = rootColumn.getChildrenStartingAtDepth(i);
            SortedMap<Integer, Column> maps = new TreeMap<>();
            for(Column col:cells)
                maps.put(col.getReportHeaderCell().startColumn, col);
            rh.add(maps);
        }
        return rh;
    }
    
    public List<CellColumn> getLeafColumns() {
        return leafColumns;
    }
}
