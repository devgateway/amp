/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;

/**
 * Sorts {@link GeneratedReport} based on sorting information from {@link ReportSpecification}, 
 * but only for the sorting that is needed during post-processing.<br> 
 * Lowest level sorting is delegated to MDX, while totals, hierarchies and non-hierarchies sorting have to be done 
 * after totals are calculated and non-hierarchies columns are merged during post-processing phase.  
 * @author Nadejda Mandrescu
 */
public class ReportSorter {
	protected ReportArea rootArea;
	protected ReportSpecification spec;
	protected List<ReportOutputColumn> leafHeaders;
	
	protected ReportSorter(GeneratedReport report) {
		this.rootArea = report.reportContents;
		this.spec = report.spec;
		this.leafHeaders = report.leafHeaders;
	}
	
	/**
	 * Sorts {@link GeneratedReport} based on sorting information from {@link ReportSpecification}.
	 * @see ReportSorter
	 * @param report - {@link GeneratedReport} to sort
	 * @return sorting duration or -1 if no sorting was performed
	 * @throws AMPException 
	 */
	public static int sort(GeneratedReport report) throws AMPException {
		return (new ReportSorter(report)).sort();
	}
	
	protected int sort() throws AMPException {
		if (spec.getSorters() == null || spec.getSorters().size() == 0) return -1;
		long startTime = System.currentTimeMillis();
		//we need to sort by hierarchis titles only if any other sorting in post-processing phase breaks up the sorting done in MDX, in our case is the sorting by measures totals 
		boolean wasSortedByMeasuresTotals = false;
		
		//lowest level sorting is done via MDX, now we need to sort by non-hierarchical columns that were merged and by totals
		for(ListIterator<SortingInfo> iter = spec.getSorters().listIterator(spec.getSorters().size() - 1); iter.hasPrevious(); ) {
			SortingInfo sortingInfo = iter.previous();
			//based on the 1st element from the tuple we are going to detect the sorting expectation
			ReportElement first = sortingInfo.sortByTuple.keySet().iterator().next();
			
			if (sortingInfo.isTotals) {
				if (ElementType.ENTITY.equals(first.type))
					if (ReportMeasure.class.isAssignableFrom(first.entity.getClass())) {
						sortMeasureTotals(sortingInfo);
						wasSortedByMeasuresTotals = true;
					} else {
						sortByHierarchy(sortingInfo, wasSortedByMeasuresTotals);
					}
				else
					throw new AMPException("Not supported sorting configuration for isTotals = true and non entity");
			} else {
				if (ElementType.ENTITY.equals(first.type))
					if(spec.getHierarchies().contains(first)){
						sortByHierarchy(sortingInfo, wasSortedByMeasuresTotals);
					} else if(spec.getColumns().contains(first)) {
						sortByNonHierarchyColumn(sortingInfo);
					} //else -> this is a funding column sorting, which was already done in MDX and thus nothing to post-sort
			} 
		}
		
		return (int)(System.currentTimeMillis() - startTime);
	}
	
	private void sortMeasureTotals(SortingInfo sInfo) throws AMPException {
		//validation for report measure was done before, we can cast now
		ReportMeasure measure =  (ReportMeasure) sInfo.sortByTuple.keySet().iterator().next().entity;
		sortByMeasuresTotals(measure, -1);
	}
	
	/**
	 * Sorting by Total Measure column 
	 * @param measure - the measure which totals to sort
	 * @param level - level number (hierarchy number) for which the totals to consider or -1 only actual data must be sorted 
	 * @throws AMPException 
	 */
	private void sortByMeasuresTotals(ReportMeasure measure, int level) throws AMPException {
		//detect first the measure number
		int colId = spec.getMeasures().indexOf(measure);
		if (colId == -1)
			throw new AMPException("Cannot sort by inexistent ReportMeasure = " + measure);
		colId = leafHeaders.size() - spec.getMeasures().size() + colId;
		sortByColumn(rootArea, colId , level);
	}
	
	private void sortByHierarchy(SortingInfo sInfo, boolean doSortingByTitle) throws AMPException {
		Iterator<Entry<ReportElement, FilterRule>> iter = sInfo.sortByTuple.entrySet().iterator();
		//first entry must be the hierarchy column itself
		ReportColumn hierarchyColumn =  (ReportColumn) iter.next().getKey().entity;
		int level = getColumnId(hierarchyColumn) + 1; //stores the exact level totals that must be sorted
		
		if (iter.hasNext()) {
			//this is a sorting by hierarchy totals
			Entry<ReportElement, FilterRule> nextEntry = iter.next();
					
			if (sInfo.isTotals && nextEntry.getKey().entity != null) {
				//this is sorting by hierarchy total on Total Costs (Total Measures) column
				ReportMeasure measure = (ReportMeasure)iter.next().getKey().entity;
				sortByMeasuresTotals(measure, level);
			} else {
				//next entry is funding column sorting  
				//build the expected leaf header name
				String colName = "";
				if (ElementType.QUARTER.equals(nextEntry.getKey().type) || ElementType.MONTH.equals(nextEntry.getKey().type) )
					for (String path : nextEntry.getKey().hierarchyPath)
						colName += "[" + path + "]";
				colName += "[" + nextEntry.getValue().value + "]";
				sortByColumn(rootArea, leafHeaders.indexOf(colName), level);
			}
		} else if (doSortingByTitle) {
			//this is the sorting by hierarchy title
			//if the sorting by hierarchy was a sorting by title, then it was already sorted via MDX and nothing to do if not explicitly requested
			sortByColumn(rootArea, level, level);
		}
	}
	
	private void sortByNonHierarchyColumn(SortingInfo sInfo) throws AMPException {
		ReportColumn nonHierarchyColumn =  (ReportColumn) sInfo.sortByTuple.keySet().iterator().next().entity;
		sortByColumn(rootArea, getColumnId(nonHierarchyColumn), -1);
	}
	
	private int getColumnId(ReportColumn col) {
		int colId = 0;
		for (Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); colId++)
			if (iter.next().equals(col))
				break;
		return colId; 
	}
	
	/**
	 * Actual sorting by the exact column index
	 * @param parent - the parent area to sort
	 * @param colId - column index to sort by
	 * @param level - the level of sorting if this is for totals sorting only or -1 if only non-hierarchical data must be sorted
	 */
	private void sortByColumn(ReportArea parent, int colId, int level) {
		//we'll collect the cells to sort, so that we don't have to iterate through the area content each time we compare 2 areas during the sorting by the specified column
		ReportCell[] cellsToSort = new ReportCell[parent.getChildren().size()];
		int childId = 0;
		boolean hasContent = true;
		boolean hasGrandChildren = false;
		for(ReportArea child : parent.getChildren()) {
			//if there are grandchildren, then sort them first
			if (child.getChildren() != null && child.getChildren().size() > 0) {
				sortByColumn(child, colId, level);
				hasGrandChildren = true;
			}
			if (!(hasGrandChildren && level == -1)) {
				//all children must have content, is abnormal to have a mix of children with content and without
				hasContent = hasContent && child.getContents() != null && child.getContents().size() > 0;
				if (hasContent)
					cellsToSort[childId++] = findCell(child, colId);
			}
		}
		//sorting must be done at the lowest level for nonHierarchical sort, that means parent area must have only children and no grandchildren 
		if (hasGrandChildren && level == -1) return;
		
		ReportArea prevFirstChild = cellsToSort[0].getArea();
		
		Arrays.sort(cellsToSort);
		
		ReportArea currFirstChld = cellsToSort[0].getArea();
		swapPrefix(prevFirstChild, currFirstChld);
		
		parent.getChildren().clear();
		for(ReportCell cell : cellsToSort)
			parent.getChildren().add(cell.getArea());
	}
	
	private ReportCell findCell(ReportArea area, int colId) {
		Iterator<ReportCell> iter = area.getContents().values().iterator();
		for(int pos = 0; pos < colId ; pos++, iter.next()) {
			//do nothing, just iterate till required column id; no need to convert to Array that will probably iterate as well?
			//assumption that content size is a valid size, between all report areas and that col id was correctly detected to be within the size
		}
		ReportCell cell = iter.next();
		cell.setArea(area);
		return cell;
	}
	
	private void swapPrefix(ReportArea prevFirstChild, ReportArea currFirstChld) {
		if (prevFirstChild == currFirstChld) return;
		
		Iterator<Entry<ReportOutputColumn, ReportCell>> currIter = currFirstChld.getContents().entrySet().iterator(); 
		Iterator<Entry<ReportOutputColumn, ReportCell>> prevIter = prevFirstChild.getContents().entrySet().iterator();
		boolean isPrefix = true;
		while (isPrefix) {
			//update context
			Entry<ReportOutputColumn, ReportCell> currEntry = currIter.next();
			Entry<ReportOutputColumn, ReportCell> prevEntry = prevIter.next();
			isPrefix = StringUtils.isBlank(((TextCell)currEntry.getValue()).displayedValue);
			if (isPrefix) {
				ReportCell tmp = prevEntry.getValue();
				prevEntry.setValue(currEntry.getValue());
				currEntry.setValue(tmp);
			}
		}
	}
}
