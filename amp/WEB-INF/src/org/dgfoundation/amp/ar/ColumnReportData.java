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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class ColumnReportData extends ReportData {
	
    @Override
	public int getVisibleRows() {
    	    int ret=1; //one is for the title, one is for totals
        	if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
    			return ret;

    	    Iterator i=items.iterator();
    	    while (i.hasNext()) {
				Column element = (Column) i.next();
				int visCol=element.getVisibleRows();
				if(visCol>ret) ret=visCol;
		    }
    	    return ret; 
	}
    
    	
    	/**
	 * @param name
	 */
	
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
		 * activitiesInColReport contains all activity ids in this report (ColumnReportData). 
		 * While we find activities that need to me moved to a category, we remove their ID from activitiesInColReport. (check below)
		 * At the end, if we still have activities in activitiesInColReport, they will be moved to the unallocated hierarchy.
		 * This can happen, for example, if you do a report with hierarchy Sector and Sub-Sector and you have an activity
		 * which belongs to Sector A (no sub-sector) and Sub-Sector b1 (which is a sub-sector of B). Since our activity would have 
		 * information on the Sub-Sector column (b1) the engine does not that it should also appear in hierachy A in the 
		 * 'Unallocated' sub-hierarchy. Check AmpReportGenerator.createHierachies() for more info. 
		 */
		Collection<Long> activitiesInColReport		= this.getOwnerIds();
		Cell fakeCell												= AmpReportGenerator.generateFakeCell(this, null);
		
		/* map where we hold for each new ReportData the activities that it will contain. */
		HashMap<ColumnReportData, Set<Long>> catToIds			= new HashMap<ColumnReportData, Set<Long>> ();
		
		/* flag to see if there are Cells with value unallocated */
		boolean existsUnallocatedCateg					= false;
		
		GroupReportData dest = new GroupReportData(this.getName());
		dest.setSplitterCell(this.getSplitterCell());

		// create set with unique values for the filtered col:
		Column keyCol = getColumn(columnName);

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

			// construct the Set of ids that match the filter:
			Set ids = new TreeSet();
			// TODO: we do not allow GroupColumnS for keyColumns
			Iterator ii = keyCol.iterator();
			while (ii.hasNext()) {
				Cell element = (Cell) ii.next();
				if (element.compareTo(cat) == 0){
					Long id		= element.getOwnerId();
					ids.add( id );
					
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
		
		/* We create fake cells for all activities that would otherwise just disappear in the newly create GroupReportData */
		for ( Long id: activitiesInColReport ){
			logger.info("The following activity needs to be added to the Unallocated category: " + id );
			fakeCell	= AmpReportGenerator.generateFakeCell(this, id);
			( (CellColumn)keyCol ).addCell(fakeCell);
		}
		/* If the unallocated category doesn't already exist we need to create it */
		if ( activitiesInColReport.size() > 0 && !existsUnallocatedCateg  ) {
			logger.info("Unallocated category was not created for " + keyCol.getColumnId() + ". Adding it now.");
			ColumnReportData crd	= new ColumnReportData( (String) keyCol.getColumnId() + ": " + fakeCell.toString() );
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
		Iterator it = items.iterator();
		Column theColumn = null;
		while (it.hasNext()) {
			Column element = (Column) it.next();
			if (element instanceof CellColumn) {
				if (element.getColumnId().equals(this.getSorterColumn())) {
					theColumn = element;
					break;
				}
			} else if (element instanceof GroupColumn) {
				Column t = ((GroupColumn) element).getColumn(this
						.getSorterColumn());
				if (t != null) {
					theColumn = t;
					break;
				}
			}
		}
		
		if (theColumn == null) {
			logger.warn("Tried to sort by an invalid column");
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
		return parent.getAbsoluteReportName()+"--"+ this.name;		
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




	


	
}
