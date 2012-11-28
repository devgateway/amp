/**
 * ReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.KeyValue;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 *
 */
public abstract class ReportData<K extends Viewable> extends Viewable {
    	
    	/**
    	 * This property is set with the cell that is responsible of creating the reportdata (in hierarchy).
    	 * a hierarchy is created based on a unique set of cells coming from a column. each cell in this column creates a new sub-report
    	 * to be able to keep track of dimensions, we remember this cell as the "splitterCell"
    	 */
    	protected Cell splitterCell=null;
    
	protected static Logger logger = Logger.getLogger(ReportData.class);
	
	/**
	 * 
	 * @return a set with all splitter cells coming from all the parents of this reportdata, if any. This is used to implement
	 * dimensions
	 */
	public void appendAllSplitterCells(Set<Cell> s) {
	    if(this.getParent()!=null) this.getParent().appendAllSplitterCells(s);
	    if(this.getSplitterCell()!=null) s.add(this.getSplitterCell());
	}
	
	@Override
	public ReportData getNearestReportData() {
		return this;
	}
	
	
	public boolean getCanDisplayRow() {	    
    	    int startRow=getStartRow();
	    int endRow=getEndRow();
	    int rowNum = getCurrentRowNumber();
	    incCurrentRowNumberBy(1);
	    if(startRow==0 && endRow==0) return true;
	    if(startRow<=rowNum && endRow>=rowNum) return true;
	    return false;
    	}
    	
	
	/**
	 * @return returns true if it makes sense to render this reportdata, given
	 * the startRow and endRow (if the reportdata is within this window)
	 */
	public boolean getRenderBody() {
	    int startRow=getStartRow();
	    int endRow=getEndRow();
	    if(startRow==0 && endRow==0) return true;
	    int visibleRows=this.getVisibleRows();
	    int rowNum = getCurrentRowNumber();
	    //if the object is before the page window, or after the page window, ignore it
	    if(rowNum+visibleRows<=startRow || rowNum>endRow) {
		incCurrentRowNumberBy(visibleRows);
		return false;
	    }
	    return true;	    
	}
	
	protected int startRow;
	protected int endRow;
	
	public int getStartRow() {
		if(this.getParent()!=null) return this.getParent().getStartRow();
		return this.startRow;
	}
	
	public int getEndRow() {
		if(this.getParent()!=null) return this.getParent().getEndRow();
		return this.endRow;
	}
	
	protected Boolean globalHeadingsDisplayed;
	
	protected List<String> columnsToBeRemoved; 
	
	public List<String> getColumnsToBeRemoved() {
		if(this.getParent()!=null) return this.getParent().getColumnsToBeRemoved();
		return this.columnsToBeRemoved;
	}
	
	public int getCurrentRowNumber() {
		if(this.getParent()!=null) return this.getParent().getCurrentRowNumber();
		return this.currentRowNumber;
	}

	public void setCurrentRowNumber(int rowNumber) {
		if(this.getParent()!=null) this.getParent().setCurrentRowNumber(rowNumber);
		this.currentRowNumber=rowNumber;
	}

	public void setStartRow(int rowNumber) {
		if(this.getParent()!=null) this.getParent().setStartRow(rowNumber);
		this.startRow=rowNumber;
	}

	public void setEndRow(int rowNumber) {
		if(this.getParent()!=null) this.getParent().setEndRow(rowNumber);
		this.endRow=rowNumber;
	}

	
	
	public void incCurrentRowNumberBy(int amount) {
		if(this.getParent()!=null) this.getParent().incCurrentRowNumberBy(amount);
		this.currentRowNumber+=amount;
	}

	public void incCurrentRowNumberBy(Viewable object) {
		if(this.getParent()!=null) this.getParent().incCurrentRowNumberBy(object.getVisibleRows());
		this.currentRowNumber+=object.getVisibleRows();
	}

	
	
	protected int currentRowNumber;
	
	protected String name;
	
	protected List<AmountCell> trailCells;

	/**
	 * in GroupReportData the entries are of the type ReportData; in GroupColumn are of the type Column
	 */
	protected List<K> items;
	
	protected String sortByColumn;
	protected boolean sortAscending;
	
	protected GroupReportData parent;
	
	protected AmpReports reportMetadata;
	
	private int rowSpan;
	
	private static String [] htmlClassNames 	= {"firstLevel", "secondLevel", "thirdLevel"};
	
	public abstract Collection<Long> getOwnerIds();

	public abstract Integer getSourceColsCount();
	
	public abstract String getSorterColumn();
	
	public abstract boolean getSortAscending();
	
	public abstract void removeColumnsByName(String name);

	public abstract String getAbsoluteReportName();
	
	public abstract void applyLevelSorter();
	
	public abstract void removeEmptyChildren();
	
	public abstract List<Column> getColumns();
	
	public abstract int getNumOfHierarchyRows();
	
	public String getNameTrn(){
		return this.name.toLowerCase().replaceAll(" ", "");
	}
	
	public String getColumnId(){
		if (this.name.indexOf(':') < 0)
			return this.name;
		
		String id = this.name.substring(0, this.name.indexOf(':'));
		return id;
	}
	
	public String getRepName(){
		if (this.name.indexOf(':') < 0)
			return "";
		
		String id = this.name.substring(this.name.indexOf(':') + 1, name.length());
		return id;
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
	
	public int getTotalUniqueRows() {
		return getOwnerIds().size();
	}
	
	/**
	 * @return Returns the parent.
	 */
	public GroupReportData getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(GroupReportData parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the items.
	 */
	public List<K> getItems() {
		return items;
	}

	public Iterator<K> iterator() {
		return items.iterator();
	}
		
	public K getItem(int idx) {
		return items.get(idx);
	}
	
	/**
	 * Hierarchy generator. This method splits horizontally a report into subReports,
	 * based on categories (hierarchies). A category is a grouping factor here. The source report
	 * to be splitted is always a ColumnReportData while the destination report is always
	 * a GroupReportData holding one or more ColumnReportDataS.
	 * A => D (B,C) where A = C u B, D is the group report and A, B, C column reports
	 * The grouping is generic and does not take care of any kind of internal hierarchy order, 
	 * other than the one specified in the report wizard. 
	 * However the behavior can be changed easily.
	 * Filtering of cells is supported. Thus whenever several ColumnReports are created from one
	 * source ColumnReport, the cells are not copied verbatim but through a filter function
	 * @see Cell.#filter(Cell, java.util.Set)
	 * @param columnName
	 * @return
	 * @throws UnidentifiedItemException
	 * @throws IncompatibleColumnException
	 */
	public abstract GroupReportData horizSplitByCateg(String columnName)  throws UnidentifiedItemException,IncompatibleColumnException;
	
	/**
	 * Performs report data post processing. These are several customized processing tasks performed after the
	 * main structure is already defined and populated.
	 *
	 */
	public abstract void postProcess();
	
	public ReportData(String name) {
		this.name = name;
		items = new ArrayList<K>();
	}

	/**
	 * to speed up tables rendering - getName() called in the inner loop
	 */
	private String shadowName;
	
	/**
	 * @return Returns the name.
	 */
	public String getName() 
	{
		if (shadowName == null)
			setName(name);
		
		return shadowName;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		this.shadowName = name.replaceAll("\"", "'");
	}

		
	/*public Set<AmountCell> getTrailsCells()
	{
		return new java.util.HashSet<AmountCell>();
	}*/
	
	public List<AmountCell> getTrailCells() {
		return trailCells;
	}
	
	/*public List<Cell> getTrailRow() {
		ArrayList<Cell> ret=new ArrayList<Cell>();
		for(Column element:items) 
		{
			ret.addAll(element.getTrailCells());
		}
		return ret;
	}*/
	
	public abstract int getTotalDepth();
	
	public abstract int getLevelDepth();
	
	public int getRelativeRowNo(){
		if (parent == null)
			return 0;
        return 1 + parent.getItems().indexOf(this);
	}

	/**
	 * @return Returns the reportMetadata.
	 */
	public AmpReports getReportMetadata() {
		if(reportMetadata==null) return parent.getReportMetadata();else
			return reportMetadata;
	}

	/**
	 * @param reportMetadata The reportMetadata to set.
	 */
	public void setReportMetadata(AmpReports reportMetadata) {
		this.reportMetadata = reportMetadata;
	}

	/**
	 * @return Returns the globalHeadingsDisplayed.
	 */
	public Boolean getGlobalHeadingsDisplayed() {
		if(this.getParent()!=null) return this.getParent().getGlobalHeadingsDisplayed();
		return this.globalHeadingsDisplayed;
	}

	/**
	 * @param globalHeadingsDisplayed The globalHeadingsDisplayed to set.
	 */
	public void setGlobalHeadingsDisplayed(Boolean globalHeadingsDisplayed) {
		if (this.getParent() != null) 
			this.getParent().setGlobalHeadingsDisplayed(globalHeadingsDisplayed);
		else
			this.globalHeadingsDisplayed=globalHeadingsDisplayed;
	}


	/**
	 * Searches the trailCells list for a Cell belonging to the column name specified as parameter
	 * @param columnName the column name to which the cell belongs
	 * @return the cell or null if not found
	 */
	public AmountCell findTrailCell(String columnName) {
		for (AmountCell element:getTrailCells()) {
			if (element!=null){
				if(columnName.equals(element.getColumn().getAbsoluteColumnName())) 
					return element; 
			}
		}
		logger.error(this.getName()+":Could not find appropriate trail cell for column name "+columnName);
		return null;
	}
	
	public String getLevelBkgColor()
	{
		int v=this.getLevelDepth();
		//if (v==1) return "#BBEEFF";
		//if (v==2) return "#FFAAAA";
		//if (v==3) return "#BBFFBB";
		if (v==3) return "#8FBCFF";
		return "ffffff";
	}


	public Cell getSplitterCell() {
	    return splitterCell;
	}


	public void setSplitterCell(Cell splitterCell) {
	    this.splitterCell = splitterCell;
	}
	
	
	public String toString() {
		return name;
	}

	public void setColumnsToBeRemoved(List<String> columnsToBeRemoved) {
	    this.columnsToBeRemoved = columnsToBeRemoved;
	}
	
	public String getHtmlClassName() {
		return htmlClassNames[(getLevelDepth()-2)%htmlClassNames.length];
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
	
	public abstract List<KeyValue> getLevelSorterPaths();
	
	public abstract void computeRowSpan(int numOfPreviousRows, int startRow, int endRow) ;
	
}