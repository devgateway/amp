/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;

/**
 * Sorts {@link GeneratedReport} based on sorting information from {@link ReportSpecification}, 
 * but only for the sorting that is needed during post-processing.<br> 
 * Lowest level sorting is delegated to MDX, while totals, hierarchies and non-hierarchies sorting have to be done 
 * after totals are calculated and non-hierarchies columns are merged during post-processing phase.  
 * @author Nadejda Mandrescu
 */
public class MondrianReportSorter {
	protected ReportArea rootArea;
	protected ReportSpecification spec;
	protected ReportEnvironment environment;
	protected List<ReportOutputColumn> leafHeaders;
	
	protected MondrianReportSorter(GeneratedReport report, ReportEnvironment environment) {
		this.rootArea = report.reportContents;
		this.spec = report.spec;
		this.leafHeaders = report.leafHeaders;
		this.environment = environment;
	}
	
	/**
	 * Sorts {@link GeneratedReport} based on sorting information from {@link ReportSpecification}.
	 * @see MondrianReportSorter
	 * @param report - {@link GeneratedReport} to sort
	 * @return sorting duration or -1 if no sorting was performed
	 * @throws AMPException 
	 */
	public static int sort(GeneratedReport report, ReportEnvironment environment) throws AMPException {
		return (new MondrianReportSorter(report, environment)).sort();
	}
	
	protected int sort() throws AMPException {
		if (spec.getSorters() == null || spec.getSorters().size() == 0) return -1;
		long startTime = System.currentTimeMillis();
		//we need to sort by hierarchies titles only if any other sorting in post-processing phase breaks up the sorting done in MDX, in our case is the sorting by measures totals 
		boolean wasSortedByMeasuresTotals = false;
		boolean sorted = false;
		
		//lowest level sorting is done via MDX, now we need to sort by non-hierarchical columns that were merged and by totals
		for(ListIterator<SortingInfo> iter = spec.getSorters().listIterator(spec.getSorters().size()); iter.hasPrevious(); ) {
			SortingInfo sortingInfo = iter.previous();
			//based on the 1st element from the tuple we are going to detect the sorting expectation
			ReportElement first = sortingInfo.sortByTuple.keySet().iterator().next();
			
			if (sortingInfo.isTotals) {
				if (ElementType.ENTITY.equals(first.type)) {
					if (ReportMeasure.class.isAssignableFrom(first.entity.getClass())) {
						sortMeasureTotals(sortingInfo);
						wasSortedByMeasuresTotals = true;
					} else {
						sortByHierarchy(sortingInfo, wasSortedByMeasuresTotals);
					}
					sorted = true;
				} else
					throw new AMPException("Not supported sorting configuration for isTotals = true and non entity");
			} else {
				if (ElementType.ENTITY.equals(first.type)) {
					if(spec.getHierarchies().contains(first.entity)) {
						sortByHierarchy(sortingInfo, wasSortedByMeasuresTotals);
						sorted = true;
					} else if(spec.getColumns().contains(first.entity)) {
						sortByNonHierarchyColumn(sortingInfo);
						sorted = true;
					} //else -> this is a funding column sorting, which was already done in MDX and thus nothing to post-sort
				}
			} 
		}
		
		return sorted ? (int)(System.currentTimeMillis() - startTime) : -1;
	}
	
	private void sortMeasureTotals(SortingInfo sInfo) throws AMPException {
		//validation for report measure was done before, we can cast now
		ReportMeasure measure =  (ReportMeasure) sInfo.sortByTuple.keySet().iterator().next().entity;
		sortByMeasuresTotals(measure, -1, sInfo.ascending);
	}
	
	/**
	 * Sorting by Total Measure column 
	 * @param measure - the measure which totals to sort
	 * @param level - level number (hierarchy number) for which the totals to consider or -1 only actual data must be sorted 
	 * @throws AMPException 
	 */
	private void sortByMeasuresTotals(ReportMeasure measure, int level, boolean asc) throws AMPException {
		//detect first the measure number
		int colId = spec.getMeasures().indexOf(measure);
		if (colId == -1)
			throw new AMPException("Cannot sort by inexistent ReportMeasure = " + measure);
		colId = leafHeaders.size() - spec.getMeasures().size() + colId;
		sortByColumn(rootArea, colId , level, asc);
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
				ReportMeasure measure = (ReportMeasure)nextEntry.getKey().entity;
				sortByMeasuresTotals(measure, level, sInfo.ascending);
			} else {
				//next entry is funding column sorting  
				//build the expected leaf header name
				ReportOutputColumn sortCol = null;
				while(nextEntry.getKey().entity == null) {
					sortCol = ReportOutputColumn.buildTranslated(nextEntry.getValue().value, environment.locale, sortCol);
					if (iter.hasNext()) 
						nextEntry = iter.next();
				}
				MDXMeasure mdxMeasure = null;
				if (nextEntry.getKey().entity != null) 
					mdxMeasure = (MDXMeasure)MondrianMapping.toMDXElement(nextEntry.getKey().entity);
				if (mdxMeasure == null)
					throw new AMPException("Invalid sorting info: " + sInfo);
				sortCol = ReportOutputColumn.buildTranslated(mdxMeasure.getName(), environment.locale, sortCol);
				int colId = leafHeaders.indexOf(sortCol);
				if (colId == -1)
					throw new AMPException("Cannot sort by inexistent leafcolumn: " + sortCol);
				sortByColumn(rootArea, colId , level, sInfo.ascending);
			}
		} else if (doSortingByTitle) {
			//this is the sorting by hierarchy title
			//if the sorting by hierarchy was a sorting by title, then it was already sorted via MDX and nothing to do if not explicitly requested
			sortByColumn(rootArea, level - 1, level, sInfo.ascending);
		}
	}
	
	private void sortByNonHierarchyColumn(SortingInfo sInfo) throws AMPException {
		ReportColumn nonHierarchyColumn =  (ReportColumn) sInfo.sortByTuple.keySet().iterator().next().entity;
		sortByColumn(rootArea, getColumnId(nonHierarchyColumn), -1, sInfo.ascending);
	}
	
	private int getColumnId(ReportColumn col) {
		return MondrianReportUtils.getColumnId(col, spec);
	}
	
	/**
	 * Actual sorting by the exact column index
	 * @param parent - the parent area to sort
	 * @param colId - column index to sort by
	 * @param level - the level of sorting if this is for totals sorting only or -1 if only non-hierarchical data must be sorted
	 * @param asc - true if sorting ascending
	 */
	private void sortByColumn(ReportArea parent, int colId, int level, boolean asc) {
		//we'll collect the cells to sort, so that we don't have to iterate through the area content each time we compare 2 areas during the sorting by the specified column
		ReportCell[] cellsToSort = new ReportCell[parent.getChildren().size()];
		int childId = 0;
		boolean hasContent = true;
		boolean goToChildren = level == -1 || level > 1;
		ReportArea prevFirstChild = null;
		for(ReportArea child : parent.getChildren()) {
			//if there are grandchildren, then sort them first
			if (goToChildren && hasChildren(child))
				sortByColumn(child, colId, level == -1 ? level : level - 1, asc);
			if (!goToChildren || level == -1) {
				//detect first leaf content that will be used for sorting the current level
				prevFirstChild = findFirstLeafArea(child);
				//all children must have content, is abnormal to have a mix of children with content and without
				hasContent = hasContent && prevFirstChild.getContents() != null && prevFirstChild.getContents().size() > 0;
				if (hasContent) {
					cellsToSort[childId] = findCell(child, colId);
					cellsToSort[childId].area = child; //remember the area we are sorting 
					childId++;
				}
			}
		}
		//check if any content was detected at this level 
		if (childId == 0) return;
		
		int first = asc ? 0 : cellsToSort.length - 1;
		int last = asc ? cellsToSort.length : -1;
		int inc = asc ? 1 : -1;
		prevFirstChild = findFirstLeafArea(cellsToSort[0].area);
		
		/* Sorts ascending.
		 There is no need to declare the comparator over ReportCell, 
		 because ReportCell has already compare method defined over value
		 and each ReportCell can override the default mechanism, like TextCell ignore case comparison
		 */
		Arrays.sort(cellsToSort);
				
		//update first child prefix
		ReportArea currFirstChld = findFirstLeafArea(cellsToSort[first].area);
		swapPrefix(prevFirstChild, currFirstChld);
		
		//update children list in sorting order
		parent.getChildren().clear();
		for(int idx = first; idx != last; idx += inc) {
			parent.getChildren().add(cellsToSort[idx].area);
			cellsToSort[idx].area = null;//clear reference
		}
	} 
	
	private ReportCell findCell(ReportArea area, int colId) {
		Iterator<ReportCell> iter = area.getContents().values().iterator();
		for(int pos = 0; pos < colId ; pos++, iter.next()) {
			//do nothing, just iterate till required column id; no need to convert to Array that will probably iterate as well?
			//assumption that content size is a valid size, between all report areas and that col id was correctly detected to be within the size
		}
		ReportCell cell = iter.next();
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

			isPrefix = StringUtils.isBlank(currEntry.getValue().displayedValue);
			if (isPrefix) {
				ReportCell tmp = prevEntry.getValue();
				prevEntry.setValue(currEntry.getValue());
				currEntry.setValue(tmp);
			}
		}
	}
	
	private ReportArea findFirstLeafArea(ReportArea current) {
		while(hasChildren(current))
			current = current.getChildren().get(0);
		return current;
	}
	
	private boolean hasChildren(ReportArea reportArea) {
		return reportArea.getChildren() != null && reportArea.getChildren().size() > 0; 
	}
}
