/**
 * GroupReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class GroupReportData extends ReportData {

	protected String currentView;
	
	
	protected Integer sourceColsCount;

	public GroupReportData(GroupReportData d) {
		super(d.getName());
		this.parent = d.getParent();
		this.reportMetadata=d.getReportMetadata();
		this.sourceColsCount = d.getSourceColsCount();
		this.globalHeadingsDisplayed=new Boolean(false);
	}

	/**
	 * @param sourceColsCount
	 *            The sourceColsCount to set.
	 */
	public void setSourceColsCount(Integer sourceColsNumber) {
		this.sourceColsCount = sourceColsNumber;
	}

	public void addReport(ReportData rd) {
		items.add(rd);
		rd.setParent(this);
	}

	/**
	 * @return Returns the currentView.
	 */
	public String getCurrentView() {
		if (parent == null)
			return currentView;
		else
			return parent.getCurrentView();
	}

	/**
	 * @param currentView
	 *            The currentView to set.
	 */
	public void setCurrentView(String currentView) {
		this.currentView = currentView;
	}

	public GroupReportData(String name) {
		super(name);
		this.globalHeadingsDisplayed=new Boolean(false);
		// TODO Auto-generated constructor stub
	}

	public ReportData getReport(String name) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#categorizeBy(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public GroupReportData horizSplitByCateg(String columnName)
			throws UnidentifiedItemException, IncompatibleColumnException {
		GroupReportData dest = new GroupReportData(this);
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			dest.addReport(element.horizSplitByCateg(columnName));

		}
		return dest;
	}

	public void postProcess() {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.postProcess();
		}
		// create trail cells
		try {

			trailCells = new ArrayList();
			if (items.size() > 0) {
				ReportData firstRd = (ReportData) items.iterator().next();
				for (int k = 0; k < firstRd.getTrailCells().size(); k++) {
					Cell c=(Cell) firstRd.getTrailCells().get(k);
					trailCells.add(c.newInstance());
				}
					
				logger.debug("GroupTrail.size=" + trailCells.size());

				i = items.iterator();
				while (i.hasNext()) {
					ReportData element = (ReportData) i.next();
					if (element.getTrailCells().size() < trailCells.size()) {
						logger
								.error("INVALID Report TrailCells size for report: "
										+ element.getParent().getName()
										+ "->"
										+ element.getName());
						logger.error("ReportTrail.getTrailCells().size()="
								+ element.getTrailCells().size());
					} else
						for (int j = 0; j < trailCells.size(); j++) {
							Cell c = (Cell) trailCells.get(j);
							Cell c2 = (Cell) element.getTrailCells().get(j);
							Cell newc = c.merge(c2);
							newc.setColumn(c2.getColumn());
							trailCells.remove(j);
							trailCells.add(j, newc);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getTotalDepth()
	 */
	public int getTotalDepth() {
		if (items.size() == 0)
			return -1;
		ReportData rd = (ReportData) items.get(0);
		return rd.getTotalDepth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getSourceColsCount()
	 */
	public Integer getSourceColsCount() {
		if (parent == null)
			return sourceColsCount;
		else
			return parent.getSourceColsCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getOwnerIds()
	 */
	public Collection getOwnerIds() {
		Set ret = new TreeSet();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			ret.addAll(element.getOwnerIds());
		}
		return ret;
	}

	public String getSorterColumn() {
		if (parent == null)
			return sortByColumn;
		else
			return parent.getSorterColumn();
	}

	public boolean getSortAscending() {
		if (parent == null)
			return sortAscending;
		else
			return parent.getSortAscending();
	}

	
	public void setSorterColumn(String sortByColumn) {
		if(sortByColumn.equals(this.sortByColumn)) sortAscending= !sortAscending; else sortAscending=true; 
		this.sortByColumn=sortByColumn;
	}

	
	/**
	 * UGLY :(
	 * @return the first column report found in this tree
	 */
	public ColumnReportData getFirstColumnReport() {
		if(items.size()==0) return null;
		ReportData rd=(ReportData) items.get(0);
		if (rd instanceof GroupReportData) return ((GroupReportData)rd).getFirstColumnReport();
		return (ColumnReportData) rd;
	}
	
}
