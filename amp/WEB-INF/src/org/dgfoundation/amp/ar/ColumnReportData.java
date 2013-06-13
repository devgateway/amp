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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * flat report: X x Y
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class ColumnReportData extends ReportData<Column> {
	
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

        	return getOwnerIds().size(); // ColumnReportData.jsp creates a row for each getOwnerId element, so we should return the same number anyway, else AMP-12515 will come back to haunt us
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

    @Override
    public void removeChildrenWithoutActivities()
    {
    	// do nothing - the children are columns and we never remove columns 
    }        
	
    /**
     * removes from all the columns all the activities without any funding attached
     * only call for non-drill-down tabs!
     * @return
     */
    public void removeActivitiesWithoutFunding()
    {
    	Column fundingColumn = getMainFundingColumn();
    	if (fundingColumn == null)
    		return;
    	Set<Long> allFundedActivities = fundingColumn.getOwnerIds();
    	for(Column column:this.getItems())
    	{
    		Set<Long> columnOwnerIds = column.getOwnerIds();
    		columnOwnerIds.removeAll(allFundedActivities);
   			column.deleteByOwnerId(columnOwnerIds);
    	}
    }
    
    /**
     * calculates the "funding" column of this report - the column which, if it is empty for an activity, would mean that the activity has no business of remaining in the CRD
     * @return
     */
    protected Column getMainFundingColumn()
    {
    	Column fundingColumn = getColumn(ArConstants.COLUMN_FUNDING);
    	
    	if (fundingColumn == null)
    		fundingColumn = getColumn(ArConstants.COLUMN_TOTAL);
    	
    	if (fundingColumn == null)
    		fundingColumn = getColumn(ArConstants.COSTING_GRAND_TOTAL);
    	
    	return fundingColumn;
    }
    
    /**
     * calculates whether this ColumnReportData is useless and should not appear in a report
     * at the moment, "useless" means that ALL of the conditions below hold:
     * 1) one of the columns is FUNDING
     * 2) the FUNDING column has no activities inside
     * 
     * This is used for multi-hierarchy reports (see AMP-14836)
     * @return
     */
    public boolean shouldBeRemoved()
    {
    	Column fundingColumn = getMainFundingColumn(); 	
    	if (fundingColumn == null)
    		return false;
    	return fundingColumn.getOwnerIds().isEmpty();
    }
    
	public void addColumns(Collection col) {
		Iterator i = col.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			addColumn(element);
		}

	}

	public Column getColumn(Object columnId) {
		for(Column element:items) 
		{
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
		HashMap<Long, List> summedCellValues					= new HashMap<Long, List>();
		
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
					"Cannot find a Column with Id " + columnName
							+ " in this ReportData");
		TreeSet<Cell> cats = new TreeSet<Cell>();
		Iterator i = keyCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if(!element.isShow()) continue;
			cats.add(element);
		}
		// TODO-Constantin: O(N^2) iteration in [categories x keys]
		// we iterate each category from the set and search for matching rows
		for (Cell cat:cats) {			
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
			Set<Long> ids = new TreeSet<Long>();
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
							// ROTTEN: swallowing exceptions thrown up by ARUtil.retrievePercentageFromCell on 0.0% cells masked as non-procented ones. See AmountCell::getAmount() for more comments of ways in which this is broken. AMP-13848
							//logger.error(e);
							//e.printStackTrace();
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
//				if ( this.getSplitterCell() != null ) { // percentages are applied by multiplication further down the line , so no need to subtract - just multiple by remaining
//					try {
//						parentPercentage	= ARUtil.retrieveParentPercetage(e.getKey(), this.getSplitterCell() );
//					} catch (Exception e1) {
//						logger.warn (e1.getMessage() );
//					}
//				}
				if ( e.getValue() < 100.0 ) {
					fakeCell		= AmpReportGenerator.generateFakeCell(this, e.getKey(), keyCol);
					fakeCell		= AmpReportGenerator.generateFakeMetaTextCell((TextCell)fakeCell, parentPercentage - e.getValue());
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
			ColumnReportData crd	= cellIter.next();
			//if (crd.toString().equals("Implementing Agency: Global Fund"))
			if (crd.toString().equals("Implementing Agency: European Union"))
				System.out.println("breakpoint!");
			Set<Long> ids			= catToIds.get(crd);
			Cell cat				= crd.getSplitterCell();
			
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
		List<Column> destCols = new ArrayList<Column>();
		for (Column element:items) {
			Column res = element.postProcess();
			//res.applyVisibility(this.getReportMetadata().getMeasures(),ArConstants.FUNDING_TYPE);
			
			//just remove all non visible columns (we might need to change this in the future)
			//if(!res.isVisible()) continue;
			
			destCols.add(res);
		}

		items = destCols;

		prepareAspect();

		// create trail cells...
		trailCells = new ArrayList<AmountCell>();
		
		List<String> ctbr = this.getColumnsToBeRemoved();
		
		for (Column element:items) {
			List<AmountCell> l = element.getTrailCells();
			if (l != null){
				trailCells.addAll(filterTrailCells(l, element));
			}else{
				if ((ctbr!=null)&& (!ctbr.contains(element.getName()))){
				//add just to keep the space
					trailCells.add(null);
				}
			}
		}
		
		//remove columns to be removed		
		
		if(ctbr!=null) 
		{
		    for (String name:ctbr) {
		    	items.remove(this.getColumn(name));		
		    	logger.info("Removed previously added column "+name+" for filtering purposes");
		    }
		}
		
		boolean dateFilterHidesProjects = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DATE_FILTER_HIDES_PROJECTS));
		boolean dateFilterUsed = (getReportMetadata() != null) && (getReportMetadata().getReportGenerator() != null) && (getReportMetadata().getReportGenerator().getFilter() != null) && (getReportMetadata().getReportGenerator().getFilter().hasDateFilter());
		
		if (dateFilterHidesProjects && dateFilterUsed)
			removeRowsWithoutFundingData(); // AMP-14382
	}

	protected Column getColumnByName(String name)
	{
		for(Column column:getColumns())
			if (column.getName().equals(name))
				return column;
		return null;
	}
		
	protected List<AmountCell> filterTrailCells(List<AmountCell> input, Column column)
	{
		if (!column.getName().equals(ArConstants.COLUMN_FUNDING))
			return input;
		
		List<AmountCell> result = new ArrayList<AmountCell>();
		for(AmountCell cell:input)
		{
			if (cell instanceof CategAmountCell)
			{
				CategAmountCell cac = (CategAmountCell) cell;
			}
		}
		return input;
	}
	
	/**
	 * removes all rows whose funding columns have no data
	 */
	protected void removeRowsWithoutFundingData()
	{
		Column column = getColumnByName(ArConstants.COLUMN_FUNDING);
		if (column == null)
			return; //nothing to do
		
		Set<Long> allRelevantIds = column.getOwnerIds();
		//System.out.println("doehali cu " + allRelevantIds.size());
		for(Column col:this.getColumns())
			col.filterByIds(allRelevantIds);
	}
	
	/**
	 * it is important for the input to be of type SortedSet, as ampActivityIds grow with each modification, so having the input sorted by ampActivityId is equivalent to having them sorted oldest-to-newest
	 * @param ids
	 * @return
	 */
	public List<Long> sortActivitiesByAge(SortedSet<Long> ids)
	{
		boolean newestComeFirst = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_MOST_RECENT_ACTIVITIES_ON_TOP));
		
		List<Long> res = new ArrayList<Long>(ids);
		if (newestComeFirst)
		{
			Collections.reverse(res);
		}
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getOwnerIds()
	 */
	public List<Long> getOwnerIds() {
		SortedSet<Long> allIds = new TreeSet<Long>();
		//get the entire set of ids:
		try {
			for(Column element:items) {
				allIds.addAll(element.getOwnerIds());
			}
		
			//if there is no sorter column, just return all ids
			if(this.getSorterColumn()==null) 
				return sortActivitiesByAge(allIds);
		
		
		// if we have a sorter column, get all its items:
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
				for(Column element:items) {
					theColumn		= element.hasSorterColumn(mySorterColPath);
					if (theColumn != null)
						break;
				}
		
				if (theColumn == null) {
					logger.warn("Tried to sort by an invalid column:" + mySorterColPath);
					return sortActivitiesByAge(allIds);
				}
		
				List<Cell> sorterItems = theColumn.getItems();
		
		
				//remove null values
				Iterator<Cell> i=sorterItems.iterator();
				while (i.hasNext()) {
					Cell element = i.next();
					if(element.getValue()==null) i.remove();
				}
		
		
				Collections.sort(sorterItems, new Cell.CellComparator());
		
				//we read all the ownerIds from the sortedItems;
				List<Long> sortedIds = new ArrayList<Long>();
				HashMap<Long, Long> referenceIds = new HashMap<Long, Long>();
				for(Cell element:sorterItems) {
					sortedIds.add(element.getOwnerId());
					referenceIds.put(element.getOwnerId(), element.getOwnerId());
				}
		
				//we iterate allIds and see if we have more ids that are not present in the sortedIds. If yes, we add them at the top of the list:
				for (Long element:allIds) {
					if(!referenceIds.containsKey(element)) 
						sortedIds.add(0,element);
				}
		
				if(!getSortAscending()) 
					Collections.reverse(sortedIds);
		
				return sortedIds;
		} catch (Exception e) {
			logger.error(e);
			return sortActivitiesByAge(allIds);
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
		int ret = 0;
		for(Column element:items) {
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
		int ret = 0;
		for (Column element:items) {
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
		for (Column element:items) {
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
		return items;
	}
	
	@Override
	public int getNumOfHierarchyRows() {
		return 1;
	}
	

	@Override
	public void computeRowSpan(int numOfPreviousRows, int startRow, int endRow) {
		this.setRowSpan(1);
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
	

	@Override
	public List<Cell> getAllCells(List<Cell> src, boolean freeze)
	{
		for(Object obj:items)
		{
			Column col = (Column) obj;
			col.getAllCells(src, freeze);
		}
		return src;
	}
	
}
