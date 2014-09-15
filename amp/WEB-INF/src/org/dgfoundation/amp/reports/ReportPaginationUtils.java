/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;

/**
 * Reports pagination utility methods
 * @author Nadejda Mandrescu
 */
public class ReportPaginationUtils {
	
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
		if (current.getContents() != null && current.getContents().size() > 0)
			dftList.add(current);
	}
	
	public static ReportArea getReportArea(ReportAreaMultiLinked[] root, int start, int size) {
		if (root == null || root.length == 0 || start >= root.length || size == 0) return null;
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
	
	private static int convert(ReportAreaMultiLinked current, Deque<List<ReportArea>> stack, int size, boolean traverseChildren) {
		if (size == 0 || current == null) return 0;
		
		boolean hasChildren = current.getChildren() != null && current.getChildren().size() > 0;
		
		if (traverseChildren && hasChildren) {
			stack.push(new ArrayList<ReportArea>());
			for(ReportArea child : current.getChildren()) {
				size = convert((ReportAreaMultiLinked)child, stack, size, true); //all children can traverse their own children
			}
			if (size == 0) return 0;
		}

		if (current.getContents() != null && current.getContents().size() > 0) {
			ReportAreaImpl newReportArea = new ReportAreaImpl();
			newReportArea.setContents(current.getContents());
			size --;
			if (stack.peek() == null) {
				stack.push(new ArrayList<ReportArea>());
				stack.peek().add(newReportArea);
			} else {
				if (hasChildren) {
					newReportArea.setChildren(stack.pop());
					if (stack.peek() == null) {
						stack.push(new ArrayList<ReportArea>());
					}
				}
				stack.peek().add(newReportArea);
			}
			if (current.next != null)
				//traverse children of siblings
				size = convert(current.next, stack, size, true);
			else if (current.parent != null)
				//configure the parent to not traverse children when moving back from latest child
				size = convert(current.parent, stack, size, false);
		}
		return size;
	}
}
