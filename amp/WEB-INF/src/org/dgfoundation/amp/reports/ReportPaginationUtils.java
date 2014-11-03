/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;

/**
 * Reports pagination utility methods
 * @author Nadejda Mandrescu
 */
public class ReportPaginationUtils {
	
	/**
	 * @return maximum records number per page, excluding rows that display sub-totals and totals
	 */
	public static int getRecordsNumberPerPage() {
		AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();				
		
		int recordsPerPage = 100;
		
		if (ampAppSettings != null){
			if (ampAppSettings.getDefaultRecordsPerPage() != 0) {
				recordsPerPage = ampAppSettings.getDefaultRecordsPerPage();
			}else{
				recordsPerPage = Integer.MAX_VALUE;
			}
		}
		
		return recordsPerPage;
	}
	
	/**
	 * @param areas - list of records (leaf areas), without totals
	 * @param recordsPerPage - maximum number of records per page
	 * @return page count for the given list of leaf areas and number of records per page
	 */
	public static int getPageCount(ReportAreaMultiLinked[] areas, int recordsPerPage) {
		return areas == null ? 0 : (areas.length + recordsPerPage - 1) / recordsPerPage;
	}
	
	/**
	 * Adds to the cache report result records
	 * @param reportId - report id for the provided results  
	 * @param generatedReport - report result
	 * @return list of records, excluding totals
	 */
	public final static CachedReportData cacheReportData(Long reportId, GeneratedReport generatedReport) {
		if (generatedReport == null) return null;
		//adding
		ReportAreaMultiLinked[] res = convert(generatedReport.reportContents);
		CachedReportData crd = new CachedReportData(generatedReport, res);
		ReportCacher.addReportData(reportId, crd);
		return crd;
	}
	
	/**
	 * Converts a report area to its ordered list of records excluding totals 
	 * @param area - report area to convert
	 * @return list of records excluding totals
	 */
	public static ReportAreaMultiLinked[] convert(ReportArea area) {
		ReportAreaMultiLinked root = new ReportAreaMultiLinked(area, null, null);
		//transform 
		List<ReportAreaMultiLinked> dftList = new ArrayList<ReportAreaMultiLinked>();
		DFT(root, dftList);
		return dftList.toArray(new ReportAreaMultiLinked[dftList.size()]);
	}
	
	/**
	 * Traverses the areas and builds the areas list in Depth First Order  
	 * @param current - current parent area
	 * @param dftList - the list to store the traversal result
	 */
	private static void DFT(ReportAreaMultiLinked current, List<ReportAreaMultiLinked> dftList) {
		if (current.getChildren() != null && current.getChildren().size() > 0)
			for(ReportArea child : current.getChildren()) {
				DFT((ReportAreaMultiLinked)child, dftList);
			}
		//based on Tabs, page records count includes only leaf entries, no totals => keep only leaf entries that determine the pagination
		else if (current.getContents() != null && current.getContents().size() > 0)
			dftList.add(current);
	}
	
	/**
	 * Generates a report area from the specified 'start' position for the given 'size' offset
	 * @param root - the list of records, excluding totals 
	 * @param start - the record index
	 * @param size - offset size from the starting record to be included into the report area 
	 * @return report area that starts from 'start' record with a maximum 'size' (limited by the end of records)  
	 */
	public static ReportArea getReportArea(ReportAreaMultiLinked[] root, int start, int size) {
		if (root == null || root.length == 0 || start < 0 || start >= root.length || size == 0) return null;
		ReportAreaMultiLinked startArea = root[start];
		Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
		//first time we do not traverse children: either this is a leaf already, either this is an aggregate, i.e. leafs are behind in DFT order
		convert(startArea, stack, size, false);
		while (stack.size() > 1) {
			ReportAreaImpl newArea = new ReportAreaImpl();
			newArea.setChildren(stack.pop());
			stack.peek().add(newArea);
		}	
		if (stack.peek().size() == 1)
			return stack.pop().get(0);
		ReportAreaImpl currentRoot = new ReportAreaImpl();
		currentRoot.setChildren(stack.pop());
		return currentRoot;
	}
	
	// TODO: change the recursive processing to semi/full iterative one
	private static int convert(ReportAreaMultiLinked current, Deque<List<ReportArea>> stack, int size, boolean traverseChildren) {
		if (current == null) return 0;
		
		boolean hasChildren = current.getChildren() != null && current.getChildren().size() > 0;
		
		if (traverseChildren && hasChildren) {
			stack.push(new ArrayList<ReportArea>());
			// start with 1st child only, then it will navigate to the next  
			size = convert((ReportAreaMultiLinked)current.getChildren().get(0), stack, size, true); //all children can traverse their own children
			// return now, because the navigation will continue via children 
			return size;
		}

		if (current.getContents() != null && current.getContents().size() > 0) {
			ReportAreaImpl newReportArea = new ReportAreaImpl();
			newReportArea.setContents(current.getContents());
			//based on Tabs, page records count includes only leaf entries, no totals
			if (!hasChildren)
				size --;
			if (stack.peek() == null) {
				stack.push(new ArrayList<ReportArea>());
				stack.peek().add(newReportArea);
			} else {
				if (hasChildren) {
					newReportArea.setChildren(stack.pop());
					if (stack.peek() == null)
						stack.push(new ArrayList<ReportArea>());
				}
				stack.peek().add(newReportArea);
			}
			//if we filled out all required entries (size == 0), then now we just have to add up the totals for the last sub-area
			if (size > 0 && current.next != null)
				//traverse children of siblings
				size = convert(current.next, stack, size, true);
			else if (current.parent != null)
				//configure the parent to not traverse children when moving back from latest child
				size = convert(current.parent, stack, size, false);
		}
		return size;
	}
}
