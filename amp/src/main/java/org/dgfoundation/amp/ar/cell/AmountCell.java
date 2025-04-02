/**
 * AmountCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.*;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.PercentageHelperMap;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.workers.AmountColWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006 Cell holding amounts.82
 * 
 */
public class AmountCell extends Cell {
    // public static DecimalFormat mf = new DecimalFormat("###,###,###,###.##");

    public double amount;

    protected double originalAmount;
    
    //protected double percentage = 100;

    protected Set<AmountCell> mergedCells;

    public Map<String, Double> getColumnPercent() {
        return columnPercent;
    }

    /**
     * @return the columnCellValue
     */
    public PercentageHelperMap getColumnCellValue() {
        return columnCellValue;
    }

    /**
     * @param columnCellValue the columnCellValue to set
     */
    public void setColumnCellValue(PercentageHelperMap columnCellValue) {
        this.columnCellValue = columnCellValue;
    }



    public void setColumnPercent(Map<String, Double> columnPercent) {
        this.columnPercent = columnPercent;
    }

    public int compareTo(Object o) 
    {
        AmountCell ac = (AmountCell) o;
        Long thisId = this.getId();
        Long otherId = ac.getId();

        // AMP-14246: safeguard for NULLs, treat null as INFINITY
        if (thisId == null)
        {
            if (otherId == null)
                return 0;
            
            return 1; 
        }
        
        if (otherId == null)
        {
            if (thisId == null)
                return 0;
            return -1;
        }
        
        return this.getId().compareTo(ac.getId());
    }

    protected double fromExchangeRate;

    protected double toExchangeRate;

    protected String currencyCode;

    protected Date currencyDate;

    /**
     * We apply percentage only if there were no other percentages applied
     */
    protected Map<String, Double> columnPercent;
    protected PercentageHelperMap columnCellValue;

    /**
     * @return Returns the toExchangeRate.
     */
    public double getToExchangeRate() {
        return toExchangeRate;
    }

    /**
     * @param toExchangeRate
     *            The toExchangeRate to set.
     */
    public void setToExchangeRate(double toExchangeRate) {
        this.toExchangeRate = toExchangeRate;
    }

    private void initializePercentageMaps() {
        if (columnPercent == null || columnCellValue == null) {
            columnPercent = new HashMap<String, Double>();
            columnCellValue = new PercentageHelperMap();
        }
    }

    /**
     * 
     */
    public AmountCell() {
        super();
        mergedCells = new HashSet<AmountCell>(8);
    }

    public AmountCell(int ensureCapacity) {
        super();
        mergedCells = new HashSet<AmountCell>(ensureCapacity);
    }

    /**
     * @param id
     */
    public AmountCell(Long id) {
        super(id);
        mergedCells = new HashSet<AmountCell>(8);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.cell.Cell#toString()
     */
    public String toString() {
        
        if (getFrozenValue() != null)
            return getFrozenValue();
        
        // mf.setMaximumFractionDigits(2);
        double am = getAmount();
        if (am == 0)
            return "0";
        else
        {
            String res = ReportContextData.formatNumberUsingCustomFormat(getAmount());
            return res;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.cell.Cell#getValue()
     */
    public Object getValue() {
        return new Double(amount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.cell.Cell#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        amount = ((Double) value).doubleValue();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
     */
    protected MetaInfo[] getViewArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * c HAS to be AmountCell !
     */
    public AmountCell merge(Cell c) {
        AmountCell ret = new AmountCell();
        AmountCell ac = (AmountCell) c;
        ret.setOwnerId(c.getOwnerId());
        if (ac.getId() == null)
            ret.getMergedCells().addAll(ac.getMergedCells());
        else
            ret.getMergedCells().add(ac);

        if (this.getId() == null)
            ret.getMergedCells().addAll(this.getMergedCells());
        else
            ret.getMergedCells().add(this);

        return ret;
    }
 
    /**
     * debug function for future use - leave it here
     * @param input
     */
    private static void printAsSorted(Set<AmountCell> input)
    {
        Comparator<AmountCell> comparator = new Comparator<AmountCell>(){
                    public int compare(AmountCell a, AmountCell b)
                    {
                        return new Double(a.getAmount()).compareTo(new Double(b.getAmount()));
                    }
                };
        SortedSet<AmountCell> v = new TreeSet<AmountCell>(comparator);
        v.addAll(input);
        //System.out.println("=== mergedCells dump start ===");
        int i = 0;
        for(AmountCell cell:v)
        {
            i++;
            //System.out.println("i = " + i + ", cell = " + cell);
        }
        //System.out.println("=== DONE ===");
    }
    
    private static void printSet(String title, Set<? extends Object> uset)
    {
        SortedSet set = new TreeSet();
        set.addAll(uset);
        //System.out.println("==== printing " + title + " =====");
        int i = 0;
        for(Object obj:set)
        {
            i++;
            //System.out.println("i = " + i + ", item = " + obj);
        }
        //System.out.println("=== DONE ===");
    }
    
    public static long merged_cells_get_amount_calls = 0;
    public static long merged_cells_get_amount_iterations = 0;
    
    /**
     * @return Returns the amount.
     */
    public double getAmount() {
        
        double ret = 0;
        if (id != null){
            double res = convert() * getPercentage() / 100;
            return res;
        }
        
        /**
         * AMP-13848
         * there are 3 types of cells which get into this cycle:
         *  1) percentageless cells
         *  2) percentageful cells
         *  3) cells which are both percentageless and percentageful
         *  
         *  Cells at (3) are actually percentageful cells with zero-percent entries masked as percentageless ones and they end up doubly-counted (once as percentageless and once as percentageful). We need to find this situation and subtract them
         * 
         */
        HashSet<String> summedCells = new HashSet<String>();
        HashMap<Long, List<CategAmountCell>> percentageless = new HashMap<Long, List<CategAmountCell>>();
        HashSet<Long> percentagefulIds = new HashSet<Long>();
        //boolean displayDebugData = this.getColumn().getAbsoluteColumnName().startsWith("Total Costs") && mergedCells != null && mergedCells.size() == 4;
        if (mergedCells != null)
        {
            //if (displayDebugData) logger.error("getAmount() of " + mergedCells.size() + " cells and path of " + this.getColumn().getAbsoluteColumnName());
            merged_cells_get_amount_calls ++;
            merged_cells_get_amount_iterations += mergedCells.size();
            
            for(AmountCell element:mergedCells)
            {
                //if (displayDebugData) logger.error("\telement[" + element.getOwnerId() + "] has class " + element.getClass().getName() + " and origAmount of " + element.getOriginalAmount());
                //if (displayDebugData) logger.error("\t\tand the column percent is " +element.getColumnPercent());
                if (element instanceof CategAmountCell && (element.getColumnPercent() == null)) {
                    CategAmountCell caCell  = (CategAmountCell)element;
                    if (caCell.getOwnerId() != null){
                        if (!percentageless.containsKey(caCell.getOwnerId()))
                            percentageless.put(caCell.getOwnerId(), new ArrayList<CategAmountCell>());
                        percentageless.get(caCell.getOwnerId()).add(caCell);
                    }
                    String idString         = caCell.getId() + "_" + caCell.getOwnerId();
                    if ( !summedCells.contains(idString) ) {
                        ret += element.getAmount();
                        summedCells.add(idString);
                    }
                }
                else
                {
                    ret += element.getAmount();
                    if ((element instanceof CategAmountCell) && (element.getOwnerId() != null))
                        percentagefulIds.add(element.getOwnerId());
                }
            }
        }
        //if (displayDebugData){
        //  logger.error("percentagefulIds = " + percentagefulIds);
        //  logger.error("percentageless = " + percentageless);
        //}
        // now fix part (3) described above: decrease each "double element"'s cost once
        for(Long ownerId:percentagefulIds)
            if (percentageless.containsKey(ownerId))
            {
                for(CategAmountCell cell:percentageless.get(ownerId))
                    if (!cell.hasMetaInfo(CategAmountCell.disablePercentMetaInfo))
                    {
                        double toDecrease = cell.getAmount(); 
                        ret -= toDecrease;
                    }
            }
        //if (displayDebugData) logger.error("\t the returned amount is " + ret);
        // logger.info("******total amount for owner
        // "+this.getOwnerId()+"="+ret);        
        return ret;
    }

    public String getWrappedAmount() {
        if (id != null)
            return ReportContextData.formatNumberUsingCustomFormat(convert()
                    * getPercentage() / 100);
        else
            return "";
    }

    /**
     * @param amount
     *            The amount to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
        this.originalAmount=amount;
        
    }

    public Class getWorker() {
        return AmountColWorker.class;
    }

    /**
     * @return Returns the mergedCells.
     */
    public Set<AmountCell> getMergedCells() {
        return mergedCells == null ? new HashSet<AmountCell>() : mergedCells;
    }

    public void setNullMergedCellsIfEmpty()
    {
        if ((mergedCells != null) && (mergedCells.isEmpty()))
            this.mergedCells = null;
    }
    
    /**
     * @return Returns the currencyCode.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode
     *            The currencyCode to set.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return Returns the fromExchangeRate.
     */
    public double getFromExchangeRate() {
        return fromExchangeRate;
    }

    /**
     * @param fromExchangeRate
     *            The fromExchangeRate to set.
     */
    public void setFromExchangeRate(double exchangeRate) {
        this.fromExchangeRate = exchangeRate;
    }

    public double convert() {
        double resultDbl = 0.0;
        if (fromExchangeRate != toExchangeRate) {
            double inter = 1 / fromExchangeRate;
            inter = inter * amount;
            resultDbl = inter * toExchangeRate;
        } else {
            resultDbl = amount;
        }
        return resultDbl;// Math.round(resultDbl);
    }
    
    public double convert(double mnt) {
        double resultDbl = 0.0;
        if (fromExchangeRate != toExchangeRate) {
            double inter = 1 / fromExchangeRate;
            inter = inter * mnt;
            resultDbl = inter * toExchangeRate;
        } else {
            resultDbl = mnt;
        }
        return resultDbl;// Math.round(resultDbl);
    }

    /**
     * Adds amount directly to the amount property. Do not use this to perform
     * horizontal totals, use merge() instead !
     * 
     * @param amount
     *            the amount to be added to the internal property.
     */
    public void rawAdd(double amount) {
        this.amount += amount;
    }

    public Date getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(Date CurrencyDate) {
        this.currencyDate = CurrencyDate;
    }

    @Override
    public Cell filter(Cell metaCell, Set ids) {
        AmountCell ret = (AmountCell) super.filter(metaCell, ids);
        
        if (ret == null)    return ret;
        
        if(this.getColumnCellValue()!=null)
            ret.setColumnCellValue( new PercentageHelperMap(this.getColumnCellValue()) );
        
        if(this.getColumnPercent()!=null) ret.setColumnPercent(new HashMap<String, Double>(this.getColumnPercent()));
        
        if (ret.getMergedCells() == null || ret.getMergedCells().size() == 0)
            return ret;
        
        // we need to filter the merged cells too...
        AmountCell realRet = (AmountCell) this.newInstance();       
        realRet.setOwnerId(ret.getOwnerId());
        
        boolean mergedAnything = false;
        for(AmountCell element:ret.getMergedCells()){
            if (element.getColumn()==null) element.setColumn(ret.getColumn());
            AmountCell filtered = (AmountCell) element.filter(metaCell, ids);
            if (filtered != null)
            {
                realRet.mergeWithCell(filtered);
                mergedAnything = true;
            }
        }
        return mergedAnything ? realRet : null;
    }

    public AmountCell newInstance() {
        return new AmountCell();
    }

    public Comparable comparableToken() {
        return new Double(getAmount());
    }

    public static long getPercentageCalls = 0;
    public static long getPercentageIterations = 0;
    
    public double getPercentage() {
        double ret = 100;
        getPercentageCalls ++;
        if(columnPercent!=null)
        for (Double perc : columnPercent.values()) {
            ret *= perc / 100;
            getPercentageIterations ++;
        }
        return ret;
    }

    /**
     * 
     * @param percentage
     * @param source
     * @param hierarchyPurpose tells whether this percentage comes from a filter or a hierarchy
     */
    public void setPercentage(double percentage, MetaTextCell source, boolean hierarchyPurpose) {
        Column sourceCol = source.getColumn();
        
        initializePercentageMaps();
        //logger.debug("percent "+percentage +" apply to "+this+" (hash"+this.getHashCode()+") with source "+sourceCol.getName());
        //logger.debug("...column already has "+columnPercent+" for "+columnCellValue);
        // never apply same percentage of same value again
        //if (columnPercent.containsKey(sourceCol.getName()) && columnCellValue.get(sourceCol.getName()).equals(source.getValue())) return;

        // we search if there is another percentage of the same column - we add
        // (the sum) of it
        Class dimensionClass    = sourceCol.getDimensionClass();
        String keyName          = sourceCol.getName();
        if ( keyName.endsWith(ArConstants.COLUMN_ANY_SECTOR)  || keyName.contains(ArConstants.COLUMN_SECTOR_TAG))
            keyName = ArConstants.COLUMN_ANY_SECTOR;
        else if ( keyName.contains(ArConstants.COLUMN_ANY_NATPROG) )
            keyName = ArConstants.COLUMN_ANY_NATPROG;
        else if ( keyName.contains(ArConstants.COLUMN_ANY_SECONDARYPROG) )
            keyName = ArConstants.COLUMN_ANY_SECONDARYPROG;
        else if ( keyName.contains(ArConstants.COLUMN_ANY_PRIMARYPROG) )
            keyName = ArConstants.COLUMN_ANY_PRIMARYPROG;
        else if (keyName.contains(ArConstants.COLUMN_LOC_ADM_LEVEL_1)) {
            keyName = ArConstants.COLUMN_LOC_ADM_LEVEL_1;
        } else if (keyName.contains(ArConstants.COLUMN_LOC_ADM_LEVEL_2)) {
            keyName = ArConstants.COLUMN_LOC_ADM_LEVEL_1;
        } else if (keyName.contains(ArConstants.COLUMN_LOC_ADM_LEVEL_3)) {
            keyName = ArConstants.COLUMN_LOC_ADM_LEVEL_1;
        }
        
        
        columnCellValue.put(keyName, source.getValue().toString(), source.getId(), percentage, dimensionClass, hierarchyPurpose);
        double percentSum   = columnCellValue.getPercentageSum(keyName);
        if ( percentSum > 0 )
            columnPercent.put(keyName, percentSum);
        
//      if (columnPercent.containsKey(sourceCol.getName())) {
//          
//          columnCellValue.put
//          
//          columnPercent.put(sourceCol.getName(), columnPercent.get(sourceCol
//                  .getName())
//                  + percentage);
//          columnCellValue.put(sourceCol.getName(), (Comparable) source
//                  .getValue());
//          return;
//      }
//
//      String sectorPercentColName=null;
//      for (String colPercent : columnPercent.keySet()) 
//          if(colPercent.endsWith(ArConstants.COLUMN_ANY_SECTOR)) sectorPercentColName=colPercent; 
//      
//      
//      if (sectorPercentColName!=null
//              && sourceCol.getName().endsWith(ArConstants.COLUMN_ANY_SECTOR)) {
//          // we forget the sector percentage, and apply the sub-sector
//          // percentage:
//          columnPercent.remove(sectorPercentColName);
//          columnCellValue.remove(sectorPercentColName);
//          columnPercent.put( sourceCol.getName(), percentage);
//          columnCellValue.put(sourceCol.getName(),
//                  (Comparable) source.getValue());
//          return;
//      }
        
//      /**
//       * For hierarchies with programs
//       */
//      this.replacePercentage(ArConstants.COLUMN_ANY_NATPROG, ArConstants.COLUMN_ANY_NATPROG, source, sourceCol, percentage);
//      this.replacePercentage(ArConstants.COLUMN_ANY_SECONDARYPROG, ArConstants.COLUMN_ANY_SECONDARYPROG, source, sourceCol, percentage);
//      this.replacePercentage(ArConstants.COLUMN_ANY_PRIMARYPROG, ArConstants.COLUMN_ANY_PRIMARYPROG, source, sourceCol, percentage);
//
//

//      columnPercent.put(sourceCol.getName(), percentage);
//      columnCellValue
//              .put(sourceCol.getName(), (Comparable) source.getValue());
    }
    
//  private boolean replacePercentage (String srcColumnsName, String destColumnsName,  MetaTextCell source,  Column sourceCol, double percentage) {
//      String percentColName   = null;
//      for (String colPercent : columnPercent.keySet()) 
//          if(colPercent.contains(srcColumnsName)) percentColName=colPercent;
//      if ( percentColName != null &&
//              sourceCol.getName().contains(destColumnsName) ) {
//          columnPercent.remove(percentColName);
//          columnCellValue.remove(percentColName);
//          columnPercent.put( sourceCol.getName(), percentage);
//          columnCellValue.put(sourceCol.getName(),
//                  (Comparable) source.getValue());
//          return true;
//      }
//      return false;
//  }

    @Override
    public void merge(Cell c1, Cell c2) {
        AmountCell ac1 = (AmountCell) c1;
        AmountCell ac2 = (AmountCell) c2;
        if (this.getOwnerId() == null)
            if (ac1.getOwnerId() != null)
                this.setOwnerId(ac1.getOwnerId());
            else
                this.setOwnerId(ac2.getOwnerId());

        // merge with c1 only if this is different than c1
        if (!this.equals(c1)) {
            if (ac1.getId() == null)
                this.getMergedCells().addAll(ac1.getMergedCells());
            else
                this.getMergedCells().add(ac1);
        }

        // merge with c2 only if this is different than c2
        if (!this.equals(c2)) {
            if (ac2.getId() == null)
                this.getMergedCells().addAll(ac2.getMergedCells());
            else
                this.getMergedCells().add(ac2);
        }
    }
    
    /**
     * a well-specified variant of {@link #merge(Cell, Cell)}, which is used inconsistently through AMP with regarding to who is the source and who is the destination
     * TODO in AMP 2.8, mergeCell(cell1, cell2) should be deleted altogether and replaced by uses of either this function or {@link #merge(Cell)} - too risky of a change to do in AMP 2.6
     * @param another
     */
    public void mergeWithCell(AmountCell another)
    {
        if (this.getOwnerId() == null)
            this.setOwnerId(another.getOwnerId());
        if (another.getMergedCells().isEmpty() && Math.abs(another.getOriginalAmount()) <= 0.00001)
            return;
        if (another.getMergedCells().isEmpty())
            this.getMergedCells().add(another);
        else
            this.getMergedCells().addAll(another.getMergedCells());
    }

    /**
     * 
     * @return the original amount converted to the report's currency
     */
    public double getOriginalAmount() {
        return convert(this.originalAmount);
    }
    /**
     * 
     * @return the initial amount as read from the database. No conversions are done on this value.
     */
    public double getInitialAmount() {
        return this.originalAmount;
    }
    
    protected String frozenValue = null;

    public void freeze()
    {
        frozenValue = toString();
    }    

    public String getFrozenValue()
    {
        return frozenValue;
    }

}

