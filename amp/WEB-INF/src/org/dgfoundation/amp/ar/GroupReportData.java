/**
 * GroupReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.ComputedMeasureCell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.Values;
import org.digijava.module.aim.helper.KeyValue;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class GroupReportData extends ReportData {

    
	/**
	 * Returns the visible rows for the sub-report. 
	 * Calculates the sum number of visible rows for each report of the group report
	 * 
	 */
	@Override
	public int getVisibleRows() {
    	Iterator i=items.iterator();
    	int ret=0; //one was for the title/totals. now we are counting the title/totals only for summary report

    	//if the report is summary then stop the processing here and return 1;
    	if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
			return 1; // consider the subtotals/titles as rows 
		
    	    while (i.hasNext()) {
				ReportData element = (ReportData) i.next();
				ret+=element.getVisibleRows();
    	    }
    	    return ret;
	}
		 	
	/**
	 * GroupReportData comparator class. This class implements reportData comparison. 
	 * Comparison is based on the level of this ReportData and the chosen sorter for that particular level
	 * @author mihai
	 * @see GroupReportData.getThisLevelSorter()
	 */
	public class GroupReportDataComparator implements Comparator {
		public final int compare (Object o1, Object o2) {
			Comparator c=new Cell.CellComparator();
			String sorterName=getThisLevelSorter().getCategory();
			String sorterType=(String) getThisLevelSorter().getValue();
			boolean ascending="ascending".equals(sorterType)?true:false;
			ReportData c1=(ReportData) o1;
			ReportData c2=(ReportData) o2;
			if(ArConstants.HIERARCHY_SORTER_TITLE.equals(sorterName)){
					return ascending?c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()):c2.getName().toLowerCase().compareTo(c1.getName().toLowerCase());
			}else{ 
				if (ascending){
					int result = c.compare(c1.findTrailCell(sorterName),c2.findTrailCell(sorterName));
					if (result == 0){
						return c1.getName().compareTo(c2.getName());
					}else{
						return result;
					}
				}else{
					int result = c.compare(c2.findTrailCell(sorterName),c1.findTrailCell(sorterName)); 
					if(result == 0){
						return c2.getName().compareTo(c1.getName());
					}else{
						return result;
					}
				}
			}
		}
	}
	
	protected List levelSorters;
		
	protected String currentView;
	
	public void applyLevelSorter() {
		if(getThisLevelSorter()!=null)
		Collections.sort(items,new GroupReportDataComparator());
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.applyLevelSorter();
		}
	}
	
	protected Integer sourceColsCount;

	private BigDecimal totalac;

	public MetaInfo getThisLevelSorter() {
		int myDepth=getLevelDepth()-1;
		if(myDepth<0 || getLevelSorters().size()<=myDepth) return null;
		return (MetaInfo) getLevelSorters().get(myDepth);
	}
	
	public GroupReportData(GroupReportData d) {
		super(d.getName());
		this.parent = d.getParent();
		this.reportMetadata=d.getReportMetadata();
		this.sourceColsCount = d.getSourceColsCount();
		this.globalHeadingsDisplayed=new Boolean(false);
		this.columnsToBeRemoved=d.getColumnsToBeRemoved();
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


	public GroupReportData horizSplitByCateg(String columnName)
			throws UnidentifiedItemException, IncompatibleColumnException {
		GroupReportData dest = new GroupReportData(this);
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			
			ReportData result= element.horizSplitByCateg(columnName);
			if(result.getItems().size()!=0)
			    dest.addReport(result);
			else dest.addReport(element);
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
					if (c!=null){
						Cell newc=c.newInstance();
						newc.setColumn(c.getColumn());
						trailCells.add(newc);
					}else{
						trailCells.add(null);
					}
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
							Cell newc =null;
							
							Cell c = (Cell) trailCells.get(j);
							Cell c2 = (Cell) element.getTrailCells().get(j);
							if (c!=null){
								newc = c.merge(c2);
								newc.setColumn(c2.getColumn());
							}
							trailCells.remove(j);
							trailCells.add(j, newc);

						}
				}
			}
			
			Iterator iter=trailCells.iterator();
			while (iter.hasNext()) {
				Object cell=iter.next();
				if (cell instanceof ComputedAmountCell) {
					String totalExpression=((ComputedAmountCell) cell).getColumn().getWorker().getRelatedColumn().getTotalExpression();
					String rowExpression=((ComputedAmountCell) cell).getColumn().getWorker().getRelatedColumn().getTokenExpression();
					ComputedAmountCell c0=(ComputedAmountCell) cell ;
					c0.getValues().put(ArConstants.COUNT_PROJECTS, new BigDecimal(this.getTotalUniqueRows()));
					if (totalExpression!=null){
						c0.getValues().prepareCountValues();
						c0.setComputedVaule(MathExpressionRepository.get(totalExpression).result(c0.getValues()));
					}
				}
				if (cell instanceof ComputedMeasureCell) {
//					String totalExpression=.getColumn().getWorker().getRelatedColumn().getTotalExpression();
					MathExpression math = null;
					if (((ComputedMeasureCell) cell).getColumn().getExpression() != null) {
						math = MathExpressionRepository.get(((ComputedMeasureCell) cell).getColumn().getExpression());
					} else {
						math = MathExpressionRepository.get(((ComputedMeasureCell) cell).getColumn().getWorker().getRelatedColumn().getTokenExpression());
					}
					ComputedMeasureCell c0=(ComputedMeasureCell) cell ;
					Values values = c0.getValues();
					if(this.getTotalActualCommitments() != null)
						values.put(ArConstants.GRAND_TOTAL_ACTUAL_COMMITMENTS, this.getTotalActualCommitments());
					else if (this.getParent().getTotalActualCommitments() != null)
						values.put(ArConstants.GRAND_TOTAL_ACTUAL_COMMITMENTS, this.getParent().getTotalActualCommitments());
					
					if (math!=null){
						values.put("COMPUTED_VALUE", math.result(values));
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
		//if(sortByColumn.equals(this.sortByColumn)) sortAscending= !sortAscending; else sortAscending=true; 
		this.sortByColumn=sortByColumn;
	}
	
	public void setSortAscending(Boolean sortAscending) {
		if ( sortAscending == null ) {
			this.sortAscending	= true;
		}
		else 
			this.sortAscending	= sortAscending;
	}

	
	/**
	 * @return the first column report found in this tree
	 */
	public ColumnReportData getFirstColumnReport() {
		if(items.size()==0) return null;
		ReportData rd=(ReportData) items.get(0);
		if (rd instanceof GroupReportData) return ((GroupReportData)rd).getFirstColumnReport();
		return (ColumnReportData) rd;
	}

	public void removeColumnsByName(String name) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.removeColumnsByName(name);
		}
	}

	public String getAbsoluteReportName() {
		if (parent!=null){
			return (parent.getAbsoluteReportName()+"--"+ this.getName()).replace("'", "");
		} else {
			return this.name.replace("'", "");
		}
	}

	public int getLevelDepth() {
		if(parent==null) return 0; else return 1+parent.getLevelDepth();
	}

	public List getLevelSorters() {
		if(parent==null) return levelSorters; else return parent.getLevelSorters();
	}
	
	@Override
	public List<KeyValue> getLevelSorterPaths() {
		List<MetaInfo> levelSorters		= this.getLevelSorters();
		List<KeyValue> ret						= new ArrayList<KeyValue>();
		if ( levelSorters != null ) {
			for (MetaInfo metaInfo : levelSorters) {
					if ( metaInfo != null ) {
						String key		= "/" + metaInfo.getCategory().replace("--", "/");
						KeyValue kv		= new KeyValue(key, metaInfo.getValue().toString() );
						ret.add(kv);
					}			
					else {
						logger.info("metainfo is null in groupreportdata " + this.getName() );
					}
			}
		}
		return ret;
	}

	public void importLevelSorters(Map sorterMap, int levels) {
		levelSorters=new ArrayList(levels);
		for(int k=0;k<levels;k++) levelSorters.add(null);
		Iterator i=sorterMap.keySet().iterator();
		while (i.hasNext()) {
			Long element;
			Object obj=i.next();
			if (obj instanceof String) {
				String src = (String) i.next();
				element=Long.parseLong(src);
			}else{
				element=(Long) obj;
			}
			
			if(element==null || element -1>=levels) i.remove(); else
			levelSorters.set(element.intValue() -1,sorterMap.get(element));
			
		}
	}

	public void removeEmptyChildren() {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			if(element.getItems().size()==0) {
				i.remove(); 
			} else {
				element.removeEmptyChildren();
			}
		}
	}
	
	public List<Column> getColumns(){
		Set<Column> retValue = new HashSet<Column>();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			ReportData reportData = (ReportData) iterator.next();
			retValue.addAll(reportData.getColumns());
		}
		return new ArrayList<Column>(retValue);
	}

	@Override
	public int getNumOfHierarchyRows() {
		int result = 1;
		if ( this.items != null ) {
			for (Object o: this.items) {
				ReportData rd	= (ReportData) o;
				result 			+= rd.getNumOfHierarchyRows();
			}
		}
		return result;
	}
	
	@Override
	public void computeRowSpan(int numOfPreviousRows, int startRow, int endRow) {
		int rowspan		= 0;
		if (items != null) {
			Iterator<Object> iter	= items.iterator();
			while ( iter.hasNext() ) {
				ReportData rd		= (ReportData)iter.next();
				rd.computeRowSpan(numOfPreviousRows, startRow, endRow);
				numOfPreviousRows	+= rd.getVisibleRows();
				rowspan				+= rd.getRowSpan();
			}
		}
		this.setRowSpan(rowspan +1);
	}

	public void setTotalActualCommitments(BigDecimal total) {
		this.totalac = total;
	}
	public BigDecimal getTotalActualCommitments() {
		return this.totalac;
	}

	
}
