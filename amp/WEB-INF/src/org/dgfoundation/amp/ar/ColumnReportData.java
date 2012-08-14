/**
 * ColumnReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.digijava.module.aim.helper.KeyValue;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class ColumnReportData extends ReportData {
	
	/**
	 * Returns the visible rows for the column report. 
	 * Calculates the max number of visible rows for each column part of the column report
	 * The visible rows of the column report is max
	 * 
	 */
    @Override
	public int getVisibleRows() {
    	    int ret=0; //one was for the title/totals. now we are counting the title/totals only for summary report 
    	    //if the report is summary then stop the processing here and return 1;
        	if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
    			return 1; // consider the subtotals/titles as rows 

        	//compute the max for the underlying columns
    	    Iterator i=items.iterator();
    	    while (i.hasNext()) {
				Column element = (Column) i.next();
				//TODO same check should be done for all related colums (sectors, programs)
				if ( ARUtil.hasHierarchy(this.getReportMetadata().getHierarchies(), ArConstants.COLUMN_REGION) && 
						( ArConstants.COLUMN_ZONE.equals(element.name) || ArConstants.COLUMN_DISTRICT.equals(element.name) ) ){
					continue;
				}else if(checkProgramsHierarchy(ArConstants.PROGRAMS_COLUMNS,element)){
					continue;
				}
				int visCol=element.getVisibleRows();
				if(visCol>ret) ret=visCol;
		    }
    	    return ret; 
	}
    
    	
    private Boolean checkProgramsHierarchy(List<String> columnNameList, Column element){
		Boolean retval = false;
		for (Iterator<String> iterator = columnNameList.iterator(); iterator.hasNext();) {
			String program = iterator.next();
			if (ARUtil.hasHierarchy(this.getReportMetadata().getHierarchies(),program)){
				for (Iterator<String> iterator2 = columnNameList.iterator(); iterator2.hasNext();) {
					String program1 = iterator2.next();
						if (element.name.equalsIgnoreCase(program1)){
							retval = true;
							break;
						}
				}
			}
		}
		return retval;
    }
    
    
	public ColumnReportData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void addColumn(Column col) {
		items.add(col);
		col.setParent(this);

	}

	public void addColumns(Collection col) {
		Iterator i = col.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			addColumn(element);
		}

	}

	public Column getColumn(Object columnId) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if (element.getColumnId().equals(columnId))
				return element;
		}
		return null;
	}

	/**
	 * @see org.dgfoundation.amp.ar.ReportData#horizSplitByCateg(java.lang.String)
	 */
	@Override
	public GroupReportData horizSplitByCateg(String columnName)
			throws UnidentifiedItemException, IncompatibleColumnException {
		
		/** 
		 * There are 2 problems (bugs) handled in the lines below, while splitting horizontally by categ:
		 * 
		 * 1)Problem: If you do a report with hierarchy Sector and Sub-Sector and you have an activity which belongs to 
		 * Sector A (no sub-sector) and Sub-Sector b1 (which is a sub-sector of sector B) then in the Sector A 
		 * hierarchy your activity doesn't appear.
		 *  
		 * Solution: activitiesInColReport contains all activity ids in this report (ColumnReportData). 
		 * While we find activities that need to me moved to a category, we remove their ID from activitiesInColReport. (check below)
		 * At the end, if we still have activities in activitiesInColReport, they will be moved to the unallocated hierarchy.
		 * This can happen, for example, if you do a report with hierarchy Sector and Sub-Sector and you have an activity
		 * which belongs to Sector A (no sub-sector) and Sub-Sector b1 (which is a sub-sector of B). Since our activity would have 
		 * information on the Sub-Sector column (b1) the engine does not that it should also appear in hierachy A in the 
		 * 'Unallocated' sub-hierarchy. Check AmpReportGenerator.createHierachies() for more info.
		 * 
		 *  2)Problem: If you do a report with hierarchy Sub-Sector and you have an activity which belongs to 
		 * Sector A (no sub-sector) - 10% and Sub-Sector b1 - 90% (which is a sub-sector of sector B) then in the report you 
		 * only see the activity under Sub-Sector b1. (Same goes for locations and programs).
		 * ( https://jira.dgfoundation.org/browse/AMP-8141 )
		 * 
		 * Solution: for Region hierarchy. Add percentages for activities. If it doesn't add up to 100% create fake cell with the 
		 * rest of the percentage and add that cell to unallocated hierarchy.
		 * 
		 * ToDO: Solution for other hierarchies
		 * 
		 */
		Collection<Long> activitiesInColReport		= this.getOwnerIds();
		
		/*	percentagesMap will hold the added percentages (on the splitting categ) for each activity */
		HashMap<Long,Double>	percentagesMap		= new HashMap<Long, Double>();
		
		/* When summing up percentages for problem 2, we need to make sure we don't sum up the percentages of the same cell twice. 
		 * So we use this set to verify what percentages were already summed up.*/
		HashMap<Long,List> summedCellValues					= new HashMap<Long, List>();
		
		/* This will store the last MetaTextCell created manually -- for problem 2 */
		MetaTextCell metaFakeCell					= null;
		
		/* map where we hold for each new ReportData the activities that it will contain. */
		HashMap<ColumnReportData, Set<Long>> catToIds			= new HashMap<ColumnReportData, Set<Long>> ();
		
		/* flag to see if there are Cells with value unallocated */
		boolean existsUnallocatedCateg					= false;
		
		GroupReportData dest = new GroupReportData(this.getName());
		dest.setSplitterCell(this.getSplitterCell());

		// create set with unique values for the filtered col:
		Column keyCol = getColumn(columnName);
		
		Cell fakeCell = AmpReportGenerator.generateFakeCell(this, null, keyCol);

		removeColumnsByName(columnName);

		if (keyCol instanceof GroupColumn)
			throw new IncompatibleColumnException(
					"GroupColumnS cannot be used as filter keys!");
		if (keyCol == null)
			throw new UnidentifiedItemException(
					"Cannot found a Column with Id " + columnName
							+ " in this ReportData");
		TreeSet cats = new TreeSet();
		Iterator i = keyCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if(!element.isShow()) continue;
			cats.add(element);
		}

		// we iterate each category from the set and search for matching rows
		i = cats.iterator();
		while (i.hasNext()) {
			Cell cat = (Cell) i.next();
			
			if ( cat.compareTo(fakeCell) == 0 ) {
				existsUnallocatedCateg = true;
			}
			
			//we check the dimension of this cell. if this cell and the current report
			
			if(!ARDimension.isLinkedWith(this, cat)) continue;
			
			logger.info("Splitting by category: "+cat);
			ColumnReportData crd = new ColumnReportData((String) cat
					.getColumnId()
					+ ": " + cat.toString());
			crd.setSplitterCell(cat);

			dest.addReport(crd);

			int locationLevel		= ArConstants.LOCATION_COLUMNS.indexOf(cat.getColumn().getName().trim() );
			
			
			// construct the Set of ids that match the filter:
			Set ids = new TreeSet();
			// TODO: we do not allow GroupColumnS for keyColumns
			Iterator ii = keyCol.iterator();
			while (ii.hasNext()) {
				Cell element = (Cell) ii.next();
				if (element.compareTo(cat) == 0){
					Long id		= element.getOwnerId();
					ids.add( id );

					
					List summedValuesPerActivity	= summedCellValues.get(id);
					if ( summedValuesPerActivity == null ) {
						summedValuesPerActivity		= new ArrayList();
						summedCellValues.put(id, summedValuesPerActivity);
					}
					/* Adding region percentages for each activity (for problem 2) */
					AmpARFilter filterObj	= cat.getColumn().getWorker().getGenerator().getFilter();
					if ( locationLevel >= 0 &&  
							filterObj.getLocationSelected() == null &&  
							element instanceof MetaTextCell && !summedValuesPerActivity.contains(element.getValue()) ) {
						try {
							summedValuesPerActivity.add(element.getValue() );
							
							Double percentage 		= ARUtil.retrievePercentageFromCell( (MetaTextCell)element );
							Double tempPerc			= percentagesMap.get(id);
							percentagesMap.put(id, (tempPerc!=null?tempPerc:0.0) + percentage );
						} catch (Exception e) {
							logger.error(e);
							e.printStackTrace();
						}
						
					}
					
					/* We remove the ids that appear in a category */
					activitiesInColReport.remove( id );
				}
			}
			
			catToIds.put(crd, ids);
			// now we get each column and get the dest column by applying the
			// filter
			/*ii = this.getItems().iterator();
			while (ii.hasNext()) {
				Column col = (Column) ii.next();
				crd.addColumn(col.filterCopy(cat, ids));
			}*/
		}
		
		/* Adding fake MetaTextCells for the percentages that don't add up to 100% */
		if ( percentagesMap.size() > 0 ) {
			Iterator<Entry<Long, Double>> iter	= percentagesMap.entrySet().iterator();
			while ( iter.hasNext() ) {
				Entry<Long, Double> e		= iter.next();
				Double parentPercentage		= 100.0;
				if ( this.getSplitterCell() != null ) {
					try {
						parentPercentage	= ARUtil.retrieveParentPercetage(e.getKey(), this.getSplitterCell() );
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if ( e.getValue() < 100.0 ) {
					fakeCell		= AmpReportGenerator.generateFakeCell(this, e.getKey(), keyCol);
					fakeCell		= AmpReportGenerator.generateFakeMetaTextCell((TextCell)fakeCell, parentPercentage-e.getValue() );
					metaFakeCell	= (MetaTextCell)fakeCell;
					( (CellColumn)keyCol ).addCell(fakeCell);
				}
				else
					iter.remove();
			}
		}
		/* We create fake cells for all activities that would otherwise just disappear in the newly create GroupReportData */
		for ( Long id: activitiesInColReport ){
			logger.info("The following activity needs to be added to the Unallocated category: " + id );
			fakeCell	= AmpReportGenerator.generateFakeCell(this, id, keyCol);
			( (CellColumn)keyCol ).addCell(fakeCell);
		}
		/* If the unallocated category doesn't already exist we need to create it */
		if ( (activitiesInColReport.size() > 0 || percentagesMap.size() > 0 ) 
				&& !existsUnallocatedCateg  ) {
			logger.info("Unallocated category was not created for " + keyCol.getColumnId() + ". Adding it now.");
			ColumnReportData crd	= new ColumnReportData( (String) keyCol.getColumnId() + ": " + fakeCell.toString() );
			//fakeCell.setValue(fakeCell.getValue() + keyCol.getName());
			crd.setSplitterCell(fakeCell);
			dest.addReport(crd);
			catToIds.put(crd, new TreeSet<Long>()) ;
		}
		
		/* Now that we have everything set we can filter-copy all the columns in the new ColumnReportDatas*/
		Iterator<ColumnReportData> cellIter			= catToIds.keySet().iterator();
		while ( cellIter.hasNext() ) {
			ColumnReportData crd		= cellIter.next();
			Set ids								= catToIds.get(crd);
			Cell cat								= crd.getSplitterCell();
			
			/* If this is the Unallocated category we add all remaining activity IDs to it*/
			if ( crd.getSplitterCell().compareTo(fakeCell) == 0 ) {
				ids.addAll( activitiesInColReport );
				ids.addAll( percentagesMap.keySet() ) ;
				
				/* We need to make sure that splitter cell is a MetaTextCell otherwise percentages won't be applied */
				if ( metaFakeCell!= null ) {
					crd.setSplitterCell(metaFakeCell);
					cat		= metaFakeCell;
				}
			}
			
			Iterator<Column> ii = this.getItems().iterator();
			while (ii.hasNext()) {
				Column col =  ii.next();
				crd.addColumn(col.filterCopy(cat, ids));
			}
		}

		return dest;
	}

	public void replaceColumn(String name, Column column) {
		int idx = items.indexOf(getColumn(name));
		items.remove(idx);
		items.add(idx, column);
		column.setParent(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#postProcess()
	 */
	public void postProcess() {
		Iterator i = items.iterator();
		List destCols = new ArrayList();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			Column res = element.postProcess();
			//res.applyVisibility(this.getReportMetadata().getMeasures(),ArConstants.FUNDING_TYPE);
			
			//just remove all non visible columns (we might need to change this in the future)
			//if(!res.isVisible()) continue;
			
			destCols.add(res);
		}

		items = destCols;

		prepareAspect();

		// create trail cells...
		trailCells = new ArrayList();
		
		List<String> ctbr = this.getColumnsToBeRemoved();
		
		i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			List l = element.getTrailCells();
			if (l != null){
				trailCells.addAll(l);
			}else{
				if ((ctbr!=null)&& (!ctbr.contains(element.getName()))){
				//add just to keep the space
					trailCells.add(null);
				}
			}
		}
		
		//remove columns to be removed		
		
		if(ctbr!=null) {
		    i=ctbr.iterator();
		while (i.hasNext()) {
		    String name = (String) i.next();
		    items.remove(this.getColumn(name));		
		    logger.info("Removed previously added column "+name+" for filtering purposes");
		}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getOwnerIds()
	 */
	public Collection getOwnerIds() {
		Set allIds = new TreeSet();
		//get the entire set of ids:
		try {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			allIds.addAll(element.getOwnerIds());
		}
		
		//if there is no sorter column, just return all ids
		if(this.getSorterColumn()==null) return allIds;
		
		
		// if we have a sorter column, get all its items:
		Iterator<Column> it = items.iterator();
		Column theColumn = null;
//		while (it.hasNext()) {
//			Column element = (Column) it.next();
//			if (element instanceof CellColumn) {
//				if (element.getColumnId().equals(this.getSorterColumn())) {
//					theColumn = element;
//					break;
//				}
//			} else if (element instanceof GroupColumn) {
//				Column t = ((GroupColumn) element).getColumn(this
//						.getSorterColumn());
//				if (t != null) {
//					theColumn = t;
//					break;
//				}
//			}
//		}
		String mySorterColPath	= this.getSorterColumn();
		while ( it.hasNext() ) {
			Column element	= it.next();
			theColumn		= element.hasSorterColumn(mySorterColPath);
			if (theColumn != null)
				break;
		}
		
		if (theColumn == null) {
			logger.warn("Tried to sort by an invalid column:" + mySorterColPath);
			return allIds;
		}
		
		List sorterItems = theColumn.getItems();
		
		
		//remove null values
		i=sorterItems.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if(element.getValue()==null) i.remove();
		}
		
		
		Collections.sort(sorterItems,new Cell.CellComparator());
		
		//we read all the ownerIds from the sortedItems;
		List sortedIds=new ArrayList();
		HashMap referenceIds=new HashMap();
		i=sorterItems.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			sortedIds.add(element.getOwnerId());
			referenceIds.put(element.getOwnerId(),element.getOwnerId());
		}
		
		//we iterate allIds and see if we have more ids that are not present in the sortedIds. If yes, we add them at the top of the list:
		i=allIds.iterator();
		while (i.hasNext()) {
			Long element = (Long) i.next();
			if(!referenceIds.containsKey(element)) sortedIds.add(0,element);
		}
		
		if(!getSortAscending()) 
			Collections.reverse(sortedIds);
		
		return sortedIds;
		} catch (Exception e) {
			logger.error(e);
			return allIds;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Viewable#getCurrentView()
	 */
	public String getCurrentView() {
		return parent.getCurrentView();
	}

	public int getMaxColumnDepth() {
		Iterator i = items.iterator();
		int ret = 0;
		while (i.hasNext()) {
			Column element = (Column) i.next();
			int c = element.getColumnSpan();
			if (c > ret)
				ret = c;
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getTotalDepth()
	 */
	public int getTotalDepth() {
		Iterator i = items.iterator();
		int ret = 0;
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret += element.getColumnDepth();
		}
		return ret;
	}

	public List getColumnsByDepth() {
		List ret = new ArrayList();

		return ret;
	}

	/**
	 * Sets the rowspan for each column. This will be used only by viewers, 
	 * to correctly render the heading of the column.
	 */
	public void prepareAspect() {
		int maxDepth = getMaxColumnDepth();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			element.setRowSpan(maxDepth + 1);
		}
	}

	public void removeColumnsByName(String name) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if (element.getName().equals(name)) {
				i.remove();
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getSourceColsCount()
	 */
	public Integer getSourceColsCount() {
		return parent.getSourceColsCount();
	}

	public String getSorterColumn() {
		return parent.getSorterColumn();
	}

	public boolean getSortAscending() {
		return parent.getSortAscending();
	}

	public String getAbsoluteReportName() {
		return (parent.getAbsoluteReportName()+"--"+ this.getName()).replace("'", "");		
	}

	public int getLevelDepth() {
		return 1+parent.getLevelDepth();
	}

	public void applyLevelSorter() {
		// TODO Auto-generated method stub
		
	}

	public void removeEmptyChildren() {
		// TODO Auto-generated method stub
	}
	
	public String getColumnIdTrn(){
		if (this.name.indexOf(':') < 0)
			return this.name;
		String id = this.name.substring(0, this.name.indexOf(':'));
		return id.toLowerCase().replaceAll(" ",	"");
	}

	public String getRepNameTrn(){
		if (this.name.indexOf(':') < 0)
			return "";
		String id = this.name.substring(this.name.indexOf(':') + 1, name.length());
		return id.toLowerCase().replaceAll(" ",	"");
	}

	@Override
	public List<Column> getColumns() {
		return (List<Column>)items;
	}
	
	@Override
	public int getNumOfHierarchyRows() {
		return 1;
	}
	

	@Override
	public void computeRowSpan(int numOfPreviousRows, int startRow, int endRow) {
		this.setRowSpan(0);
		int realStartRow	= startRow+1;
		int realEndRow		= endRow+1;
		//RANGE is [realStartRow, realEndRow]
		
		int visibleRows		= this.getVisibleRows();
		
		if ( numOfPreviousRows < realEndRow && numOfPreviousRows+visibleRows >= realStartRow ) {
			// At least some activity rows need to be displayed on this page (there is overlapping)
			
			if ( numOfPreviousRows+visibleRows > realEndRow && numOfPreviousRows >= realStartRow) {
				// partial overlapping end of range
				this.setRowSpan( realEndRow - numOfPreviousRows + 1 );
				return;
			}
			if ( numOfPreviousRows+visibleRows <= realEndRow && numOfPreviousRows < realStartRow) {
				// partial overlapping beginning of range
				int rowsp	= numOfPreviousRows+visibleRows - realStartRow + 2;
				if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
					rowsp	= 1;
				this.setRowSpan( rowsp );
				return;
			}
			if ( numOfPreviousRows+visibleRows > realEndRow && numOfPreviousRows < realStartRow) {
				// full overlapping over both ends of the range
				this.setRowSpan( realEndRow - realStartRow + 2 );
				return;
			}
			if ( numOfPreviousRows+visibleRows <= realEndRow && numOfPreviousRows >= realStartRow) {
				// all rows are inside the range
				int rowsp	= visibleRows + 1;
				if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
					rowsp	= 1;
				this.setRowSpan( rowsp );
				return;
			}
			
		}
		//System.out.println("Shouldn't get here !!! " + this.toString() + " !! prev rows: " + numOfPreviousRows);
		//System.out.println("!! ");
		
	}	


	@Override
	public List<KeyValue> getLevelSorterPaths() {
		if ( this.parent != null )
			return parent.getLevelSorterPaths();
		else return null;
	}
	


	
}
