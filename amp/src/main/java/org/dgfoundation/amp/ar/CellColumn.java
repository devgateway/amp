/**
 * CellColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.exception.IncompatibleCellException;
import org.dgfoundation.amp.ar.helper.ReportHeadingLayoutCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.digijava.module.aim.dbentity.AmpReports;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 19, 2006 a column that holds cells
 */
public class CellColumn<K extends Cell> extends Column<K> {

    /**
     * view used for having extracted the column. Might be null!
     */
    protected String extractorView;
    
    /**
     * Returns the number of items in the column as the visible rows (they are already unique) so this means
     * the number activities that have data for this column
     */
    @Override
    public int getVisibleRows() {
        return items.size();
    }

    /**
     * Iterator for the internal list of items
     * 
     * @return the Iterator
     */
    public Iterator<K> iterator() {
        return items.iterator();
    }

    protected HashMap<Long, List<Cell>> itemsMap = new HashMap<Long, List<Cell>>();

    public CellColumn(ColumnWorker worker) {
        super(worker);
    }

    public CellColumn(String name) {
        super(name);
    }

    public CellColumn(String name, int initialCapacity) {
        super(name, initialCapacity);
        itemsMap = new HashMap<Long, List<Cell>>(initialCapacity);
    }

    public CellColumn(Column source) {
        super(source.getParent(), source.getName());
        copyColumnData(this, source);
    }

    public CellColumn(Column parent, String name) {
        super(parent, name);
        this.contentCategory = parent.getContentCategory();
    }

    /**
     * copies all the misc fields from a source column to a destination column
     * @param dest
     * @param source
     */
    private static void copyColumnData(CellColumn dest, Column source)
    {
        dest.contentCategory = source.getContentCategory();
        dest.dimensionClass = source.getDimensionClass();
        dest.relatedContentPersisterClass = source.getRelatedContentPersisterClass();
        
        if (source instanceof CellColumn)
            dest.setExtractorView(((CellColumn) source).getExtractorView());
        
        dest.worker=source.getWorker();
        
        dest.setDescription(source.getDescription());
        dest.setExpression(source.getExpression());
        dest.setTotalExpression(source.getTotalExpression());
    }
    
    
    public Cell getByOwner(Long ownerId) {
        List<Cell> cells = itemsMap.get(ownerId);
        if (cells == null || cells.isEmpty())
            return null;
        
        return cells.get(0); // return the first one since the caller doesn't care
    }

    public Set<Long> getAllOwnerIds()
    {
        return this.itemsMap.keySet();
    }
    
    static long calls = 0;
    static long iterations = 0;
    
    public Cell getByOwnerAndValue(Long ownerId, Object value) {
        calls ++;
        
        List<Cell> list = itemsMap.get(ownerId);
        if (list == null)
            return null;
        
        for(Cell element:list)
        {
            iterations ++;
            if (value.equals(element.getValue()))
                return element;
        }
        
//      Iterator i = items.iterator();
//      while (i.hasNext()) {
//          iterations ++;
//          Cell element = (Cell) i.next();
//          if (element.getOwnerId().equals(ownerId)
//                  && value.equals(element.getValue()))
//              return element;
//      }
        return null;
    }

    public K getCell(int i) {
        return (K) getItem(i);
    }

    /**
     * do not parametrize this function, as we need it to accept raw Cell as argument for overriding purposes in subclasses (some subclasses do an "addCell(AmountCell cell)" which call "super.addCell(new TotalAmountCell(cell))"
     * @param c
     */
    public void addCell(Cell c) {
        //Cell c = (Cell) cc;
        addCellRaw(c);
    }

    public final void addCellRaw(Cell c)
    {
        c.setColumn(this);
        if (!itemsMap.containsKey(c.getOwnerId()))
            itemsMap.put(c.getOwnerId(), new ArrayList<Cell>());
        itemsMap.get(c.getOwnerId()).add(c);
        items.add((K) c);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
     */
    @Override
    public Column filterCopy(final Cell metaCell, final Set<Long> ids) {
        
        CellFilterCriteria filter = new CellFilterCriteria(){
            public Cell filter(Cell source){
                return source.filter(metaCell, ids);
            }};     
        return filterCopy(filter);
    }
    
    public Column filterCopy(CellFilterCriteria crit)
    {
        CellColumn dest = (CellColumn) this.newInstance();
        for(Cell element:items){
            Cell destCell = crit.filter(element);
            if (destCell != null)
                dest.addCell(destCell);
        }
        return dest;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#process()
     */
    @Override
    public Column postProcess() {
        CellColumn dest = (CellColumn) this.newInstance();
        ListCell lc = new ListCell();
        Iterator i = this.iterator();
        HashMap<Long, ListCell> ownerToCells = new HashMap<Long, ListCell>();
        while (i.hasNext()) {
            try {
                Object objelement = i.next();
                // If type total column do nothing
                if (!(objelement instanceof Column)) {

                    Cell element = (Cell) objelement;
                    
                    // if we don't have items in the cell list, just add the
                    // cell
                    /*if (lc.size() == 0)
                        lc.addCell(element);
                    else*/
                    // if we have, verify if the owner of one cell in the list
                    // is
                    // the same
                    // if it is, add this cell to the list. if not, verify if
                    // the
                    // list has
                    // more than one element. if it does, add the whole list to
                    // the
                    // dest column
                    // if it has only one, add just that element to the dest
                    // colmn
                    /*if (lc.getCell(0).getOwnerId().equals(element.getOwnerId()))
                        lc.addCell(element);
                    else {
                        if (lc.size() == 1)
                            dest.addCell(lc.getCell(0));
                        else
                            dest.addCell(lc);
                        lc = new ListCell();
                        lc.addCell(element);
                    }*/
                    
                    if (element.toString().equals("Expenditure Class Unallocated") && this.getName().equals("Expenditure Class"))
                        continue; // this is so hacky I have no words. Luckily we're dumping LegacyReports really soon (c)
                    
                    ListCell listCell   = ownerToCells.get(element.getOwnerId());
                    if ( listCell == null ) {
                        listCell    = new ListCell();
                        ownerToCells.put(element.getOwnerId(), listCell);
                    }
                    listCell.addCell(element);
//                  System.err.format("ListCell: adding cell %s to column %s\n", element, this.getName());
                }
            } catch (IncompatibleCellException e) {
                logger.error(e.getMessage(), e);
            }

        }
        Iterator<Entry<Long, ListCell>> iter    = ownerToCells.entrySet().iterator();
        while (iter.hasNext() ) {
            Entry<Long, ListCell> entry         = iter.next();
            ListCell lCell                      = entry.getValue();
            if ( lCell != null && lCell.size() > 0 ) {
                if ( lCell.size() == 1 ) {
                    dest.addCell( lCell.getFirstCell() );
                }
                else {
                    dest.addCell(lCell);
                }
            }
        }

        if (lc.size() == 1)
            dest.addCell(lc.getFirstCell());
        if (lc.size() > 1)
            dest.addCell(lc);

        return dest;

    }

    public int getWidth() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
     */
    @Override
    public List<Column> getSubColumns(int depth) {
        ArrayList<Column> ret = new ArrayList<Column>();
        if (depth == this.positionInHeading.getStartRow())
            ret.add(this);
        return ret;
    }
    
    @Override
    public void setPositionInHeadingLayout(int totalRowSpan, int startingDepth, int startingColumn)
    {
        this.positionInHeading = new ReportHeadingLayoutCell(this, startingDepth, totalRowSpan, totalRowSpan, startingColumn, this.getWidth(), this.getName());
    }
    
    @Override
    protected int getRowSpanInHeading_internal()
    {
        return 1;
    }
    
    @Override
    public int calculateTotalRowSpan()
    {
        return getRowSpanInHeading_internal();
    }

//  @Override
//  public int getNewRowSpan()
//  {
////        if (this.getName().equals("Actual Disbursements"))
////            //System.out.println("afigheti");
//      return this.positionInHeading.getRowSpan(); // a CellColumn is a leaf - so it always go upto the bottom of the report's heading
//  }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
     */
    @Override
    public Set<Long> getOwnerIds() {
        TreeSet<Long> ret = new TreeSet<Long>();
        for(Cell element:((List<Cell>)items))
            ret.add(element.getOwnerId());
        return ret;
    }

    @Override
    public List<? extends AmountCell> getTrailCells() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#newInstance()
     */
    @Override
    public Column newInstance() {
        return new CellColumn(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
     */
    @Override
    public int getColumnDepth() {
        if (this.getName() != null && !this.getName().equals("")){
            return 1;   
        }else{
            return 0;
        }
        
    }

    /**
     * will crash if oldCell does not exist in the column
     * @param oldCell
     * @param newCell
     */
    public void replaceCell(K oldCell, K newCell) {
        int idx = items.indexOf(oldCell);
        items.remove(idx);
        items.add(idx, newCell);
        
        int idx2 = itemsMap.get(oldCell.getOwnerId()).indexOf(oldCell);
        itemsMap.get(oldCell.getOwnerId()).remove(idx2);
        itemsMap.get(oldCell.getOwnerId()).add(newCell);

        newCell.setColumn(this);
    }

    /*;
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#cellCount()
     */
    @Override
    public int getCellCount() {
        return items.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#getVisibleCellCount()
     */
    @Override
    public int getVisibleCellCount(Long ownerId) {
        int count = 0;
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Cell element = (Cell) i.next();
            if (element.getOwnerId().equals(ownerId)
                    && !(element.toString().trim().equals("")))
                count++;
        }
        return count;
    }

    public boolean hasTrailCells()
    {
        return false;
    }

//  public HashMap getItemsMap() {
//      return itemsMap;
//  }
//
//  public void setItemsMap(HashMap itemsMap) {
//      this.itemsMap = itemsMap;
//  }
    
    @Override
    public boolean removeEmptyChildren(boolean checkFunding) {
        if ( this.getOwnerIds().size() > 0 ) {
            return false;
        }
        else
            return true;
    }
    
    @Override
    public Column hasSorterColumn(String namePath) {
        if ( namePath == null ) {
            logger.error("namePath param is not supposed to be null here. Something's wrong");
            return null;
        }
        if ( namePath.equals(this.getNamePath()) ) {
            return this;
        }
        else
            return null;
    }
    
    @Override
    public void filterByIds(Set<Long> idsToRetain)
    {
        Iterator<K> cells = this.iterator();
        while(cells.hasNext())
        {
            Cell cell = cells.next();
            if (!idsToRetain.contains(cell.getOwnerId()))
                cells.remove();
        }
    }
    
    @Override
    public List<Cell> getAllCells(List<Cell> src, boolean freeze)
    {
        for(Object obj:this.getItems())
        {
            src.add((Cell) obj);
            if (obj instanceof AmountCell)
                ((AmountCell) obj).freeze();
        }
        return src;
    }
    
    @Override
    public void deleteByOwnerId(Set<Long> ownerId)
    {
        // pay attention when merging with AMP2.4, as the way cells are kept there has been slightly changed
        for(Long id:ownerId)
            itemsMap.remove(id);
        
        Iterator<Cell> iter = (Iterator<Cell>) items.iterator();
        while (iter.hasNext())
        {
            Cell cell = iter.next();
            if (ownerId.contains(cell.getOwnerId()))
                iter.remove();
        }
    }
    
    public void setExtractorView(String ev)
    {
        this.extractorView = ev;
    }
    
    /**
     * the view used for having extracted this column
     * @return
     */
    public String getExtractorView()
    {
        return this.extractorView;
    }
    
    @Override
    public GroupColumn verticalSplitByCateg(String category, Set ids, boolean generateTotalCols, AmpReports reportMetadata)
    {
        for(Entry<String, String> meas:ArConstants.DIRECTED_MEASURE_TO_DIRECTED_TRANSACTION_VALUE.entrySet())
            if (category.equals(meas.getValue()) && (!this.getName().equals(meas.getKey())))
                return null; // only REAL DISBURSEMENTS columns can be split by real disbursements
        return GroupColumn.verticalSplitByCateg_internal((CellColumn<? extends CategAmountCell>)this, category, ids, generateTotalCols, reportMetadata);
    }
    
    @Override
    public boolean isSortableBy()
    {
        return true;
    }
    
    public List<Cell> getCells(){
        return (List<Cell>) items;
    }

}

