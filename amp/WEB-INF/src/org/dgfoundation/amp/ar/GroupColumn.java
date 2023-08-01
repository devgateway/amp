/**
 * GroupColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.helper.ReportHeadingLayoutCell;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.text.SimpleDateFormat;
import java.util.*;



/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Column that is built
 *         for grouping other ColumnS. This Column can hold only items of type
 *         Column and is basically a list of ColumnS.
 * @since Jun 22, 2006
 * 
 */
public class GroupColumn extends Column<Column> {

    /**
     * Returns the max of the underlying visible rows of the subcolumns
     * Constantin: function probably incorrect, but impossible to debug because it seems it is not used from anywhere
     * Constantin: case where this function probably fails: column 1: 1/0, column 2: 0/1, return of this function will be probably 1, although the correct result is definitely 2
     */
    @Override
    public int getVisibleRows() {
        //TODO-CONSTANTIN: is it sure that we don't have to return getOwnerIds().size() here (if reportMetaData.getHideActivities is not true)
        Iterator i=items.iterator();
        int ret=0;
        while (i.hasNext()) {
            Column element = (Column) i.next();
            int visCol=element.getVisibleRows();
            if(visCol>ret) ret=visCol;
        }
        //return getOwnerIds().size();
        return ret;
    }
        
    public int getWidth() {
        int ret = 0;
        for(Column column:this.getItems()){         
            ret += column.getWidth();
        }
        return Math.max(1, ret); // at least the column title
    }   
    
    @Override
    public GroupColumn verticalSplitByCateg(String category, Set ids, boolean generateTotalCols, AmpReports reportMetadata) 
    { 
        GroupColumn dest = null;
        dest = new GroupColumn(this);       
        for(Column element:this.getItems()){
            if((category.equals(ArConstants.TERMS_OF_ASSISTANCE) || category.equals(ArConstants.MODE_OF_PAYMENT))
                    && element instanceof TotalCommitmentsAmountColumn){ 
                continue;
            }
            
            Column splitted = element.verticalSplitByCateg(category, ids, generateTotalCols, reportMetadata);
                
            if(splitted != null){
                dest.addColumn(splitted);
                splitted.setContentCategory(category);
            }
            else
                dest.addColumn(element);
        }
        return dest;
    }
    
   /**
     * clone all cells and detach them
     * @param column
     * @return
     */
    public static void detachCells(Column column)
    {
        if (column instanceof CellColumn)
        {
            for(Object cll:column.getItems())
            {
                if (cll instanceof CategAmountCell)
                    ((CategAmountCell) cll).cloneMetaData();
            }
        }
        
        if (column instanceof GroupColumn)
        {
            GroupColumn gc = (GroupColumn) column;
            for(Object item:gc.getItems())
                detachCells((Column) item);
        }
    }
    
    /**
     * clone all cells and detach them
     * @param column
     * @return
     */
    public static void removeDirectFundingMetadata(Column column)
    {
        if (column instanceof CellColumn)
        {
            for(Object cll:column.getItems())
            {
                if (cll instanceof CategAmountCell)
                    ((CategAmountCell) cll).removeDirectedFundingMetadata();
            }
        }
        
        if (column instanceof GroupColumn)
        {
            GroupColumn gc = (GroupColumn) column;
            for(Object item:gc.getItems())
                removeDirectFundingMetadata((Column) item);
        }
    }
    
    /**
     * detaches cell from their siblings (e.g. clones metadata in Actual-disbursements-bound-cells) and removes funding metadata from copies
     */
    public void detachCells()
    {
        // postprocess Funding columns as to detach completely the ACTUAL DISBURSEMENTS and REAL DISBURSEMENTS columns
        List<Column> columns = this.getItems();
        for(Column column:columns)
        {
            //System.out.println("column = " + column);
            if (ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsKey(column.getName()) || ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsValue(column.getName())) {
                detachCells(column);
            }
        }
        // these cycles HAVE to be separated, as we can remove metadata from a cell only after it has been throughoutly detached
        for(Column column:columns)
            if (ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsKey(column.getName()))
            {
                removeDirectFundingMetadata(column);
            }
    }
    
    public static boolean itemIsRelevant(CategAmountCell item, String trType) {
        for (String rawTr:ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.keySet()) {
            String directedTr = ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.get(rawTr);
            if (trType.equals(rawTr) || trType.equals(directedTr))
                if (item.isDirectedTransaction(rawTr))
                    return true;
        }
        return false;
    }
    
    /**
     * Helper method that only uses one category to create a categorized tree. This method is internally used and should not
     * be invoked by the developer directly.
     * <b>this function actually belongs in CellColumn, but leaving it here because it is big and it would screw svn diffs too much</b>
     * @param src the CellColumn source
     * @param category category the category to categorize the data with 
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     */
    public static GroupColumn verticalSplitByCateg_internal(CellColumn<? extends CategAmountCell> src, String category, Set ids, boolean generateTotalCols,AmpReports reportMetadata) {
        
        //logger.error(String.format("called with cat = %s,  generateTotalCalls = %s", category, generateTotalCols));
        if (src instanceof TotalComputedMeasureColumn)
            return null;
        
        HashMap<String,String> yearMapping = new HashMap<String, String>();
        HashMap<String,String> monthMapping = new HashMap<String, String>();
                        
        GroupColumn ret = new GroupColumn(src);
        SortedSet<MetaInfo> metaSet = new TreeSet<MetaInfo>(new java.util.Comparator<MetaInfo>(){
            public int compare(MetaInfo a, MetaInfo b){
                return ((Comparable)a.value).compareTo((Comparable) b.value);
            }});
       // Iterator<? extends CategAmountCell> i = src.iterator();
       
        AmpARFilter myFilters   = null;
        try{
            myFilters   = src.getWorker().getGenerator().getFilter();
        }
        catch (NullPointerException e) {
            //logger.warn("Could not get filter object when type is: " + category);
        }
       if ( (reportMetadata.getAllowEmptyFundingColumns() != null && reportMetadata.getAllowEmptyFundingColumns()) && 
              ( category.equals(ArConstants.YEAR) || category.equals(ArConstants.QUARTER) 
                || category.equals(ArConstants.MONTH) ) ) {
          ARUtil.insertEmptyColumns(category, src, metaSet, myFilters);
       } 
        
       String unspecified = TranslatorWorker.translateText("Unspecified");
       
       SimpleDateFormat pledgesfakeyear = new SimpleDateFormat("yyyy"); // ugly hack since AMP 1 times
       String pledgesFakeYear = pledgesfakeyear.format(new Date(ArConstants.PLEDGE_FAKE_YEAR.getTime())).toString();
 
       for(Categorizable element:src.getItems()){
            if(!element.isShow()) continue;
            MetaInfo minfo = element.getMetaData().getMetaInfo(category);
            if (minfo == null || minfo.getValue() == null) 
                continue;
                //if the year is not renderizable just not add it to minfo
           
            if (generateTotalCols || true/*element.isRenderizable()*/) { // eliminating "renderizable" elements is now a postprocessing step, this isRenderizable() function has been eliminated, so assuming it is true
                metaSet.add(minfo);
                
                if (category.equalsIgnoreCase(ArConstants.YEAR)){
                    MetaInfo minfo2 = element.getMetaData().getMetaInfo(ArConstants.FISCAL_Y);
                    
                    //Replace the year in pledges report for unspecified dates funding
                    if (reportMetadata.getType() == ArConstants.PLEDGES_TYPE){
                        if (minfo.getValue().toString().equalsIgnoreCase(pledgesFakeYear)){
                            element.getMetaData().replace(new MetaInfo(minfo2.getCategory(), unspecified));
                            //minfo2.setValue(unspecified);
                        }
                    }
                    
                    yearMapping.put(minfo.getValue().toString(),minfo2.getValue() == null ? unspecified : minfo2.getValue().toString());
               }
                if (category.equalsIgnoreCase(ArConstants.MONTH)){
                    MetaInfo minfo2 = element.getMetaData().getMetaInfo(ArConstants.FISCAL_M);
                    monthMapping.put(minfo.getValue().toString(), minfo2.getValue() == null ? unspecified : minfo2.getValue().toString());
               }
            }
        }
        
        FiscalPeriodHelper fiscalYearHelper     = null;
        if ( yearMapping.size() > 0 )
            try {
                fiscalYearHelper    = new FiscalPeriodHelper(yearMapping);
            } catch (Exception e) {
                e.printStackTrace();
            }
        FiscalPeriodHelper fiscalMonthHelper    = null;
   
        try {
            fiscalMonthHelper       = new FiscalPeriodHelper(monthMapping, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
      
        
        //TODO: ugly stuff... i have no choice
        //manually add all quarters
//       if(category.equals(ArConstants.QUARTER)) {
//          metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q1"));
//          metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q2"));
//          metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q3"));
//          metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q4"));
//        }
// this has been moved to ARUtil.insertEmptyColumns   
      if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE).equalsIgnoreCase("true")) {
          if(category.equals(ArConstants.TERMS_OF_ASSISTANCE) ) {
            metaSet.add(new MetaInfo<String>(ArConstants.TERMS_OF_ASSISTANCE,ArConstants.TERMS_OF_ASSISTANCE_TOTAL));
          }
          
          if(category.equals(ArConstants.QUARTER)) {
              metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,ArConstants.QUARTERS_TOTAL));
          }
        
      }
      if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_MODE_OF_PAYMENT)!=null &&
              FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_MODE_OF_PAYMENT).equalsIgnoreCase("true")) {
          if(category.equals(ArConstants.MODE_OF_PAYMENT) ) {
            metaSet.add(new MetaInfo<String>(ArConstants.MODE_OF_PAYMENT,ArConstants.MODE_OF_PAYMENT_TOTAL));
          }
          
          if(category.equals(ArConstants.QUARTER)) {
              metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,ArConstants.QUARTERS_TOTAL));
          }
        
      }
        
           //manually add at least one term :(
        if(category.equals(ArConstants.TERMS_OF_ASSISTANCE) && ARUtil.containsMeasure(ArConstants.UNDISBURSED_BALANCE,reportMetadata.getMeasures())) {
           //Commented for Bolivia
            if (metaSet.size()==0)
                metaSet.add(new MetaInfo<String>(ArConstants.TERMS_OF_ASSISTANCE,"Grant"));
            }
       
       
       //manually add measures selected
       if(category.equals(ArConstants.FUNDING_TYPE)) {
           metaSet.clear();
           Set<AmpReportMeasures> measures=reportMetadata.getMeasures();
           Iterator<AmpReportMeasures> ii = measures.iterator();
           while (ii.hasNext()) {
                AmpReportMeasures ampReportMeasurement = ii.next();
                AmpMeasures element = ampReportMeasurement.getMeasure();
                if (!element.isSplittable())
                    continue;
                
                MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(
                        ArConstants.FUNDING_TYPE, new FundingTypeSortedString(
                        element.getMeasureName(), reportMetadata.getMeasureOrder(element.getMeasureName())));
                metaSet.add(metaInfo);
           }
           /*
            * if there isn't a measure selected and TOTAL_COMMITMENTS isn't selected.
            * We add at least one measure.
            */         
           if(metaSet.isEmpty() && !ARUtil.containsMeasure(ArConstants.TOTAL_COMMITMENTS,reportMetadata.getMeasures())){
                MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE,new FundingTypeSortedString(ArConstants.PLANNED + " " + ArConstants.COMMITMENT, 0));
                metaSet.add(metaInfo);             
           }
       }
                
       
        // iterate the set and create a subColumn for each of the metainfo
        for (MetaInfo element:metaSet) {
            //do not consider the Totals subcolumn in years/quarters as a real category
            //if this category is found inside the grand totals column, ignore it 
            if(element.getCategory().equals(ArConstants.TERMS_OF_ASSISTANCE) && 
                    element.getValue().equals(ArConstants.TERMS_OF_ASSISTANCE_TOTAL) && src instanceof TotalAmountColumn) continue;
            
            if(element.getCategory().equals(ArConstants.MODE_OF_PAYMENT) && 
                    element.getValue().equals(ArConstants.MODE_OF_PAYMENT_TOTAL) && src instanceof TotalAmountColumn) continue;

            CellColumn cc = null;
            if (generateTotalCols)
                cc = new TotalAmountColumn(element.getValue().toString(),true);
            else
               {
                if(category.equalsIgnoreCase(ArConstants.YEAR)){
                    String fYear    = yearMapping.get(element.getValue().toString());
                    if (fYear == null && fiscalYearHelper != null) {
                        try {
                            fYear       = fiscalYearHelper.getFiscalYear( element.getValue().toString() );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(fYear == null) {
                        //AMP-17215: Sometimes fYear is null (ie: no data on report but showing all columns) and in that case we have to change it to "" or it will fail later
                        //(ie: this.getName().equals(bla) which is used widely in the code), neither we can keep cc as null because is used after in this method.
                        fYear = "";
                    }
                    cc = new AmountCellColumn( fYear );
                }else if(category.equalsIgnoreCase(ArConstants.MONTH)){
                    String fMonth   = monthMapping.get(element.getValue().toString());
                    if (fMonth == null && fiscalMonthHelper != null) {
                        try {
                            fMonth      = fiscalMonthHelper.getFiscalMonth( element.getValue().toString() );
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if(fMonth == null) {
                        fMonth = "";
                    }
                    cc = new AmountCellColumn( fMonth );
                    }
                else{
                cc = new AmountCellColumn( element.getValue().toString());
                }
            }
            ret.getItems().add(cc);
            cc.setParent(ret);
            
            cc.setContentCategory(category);
            //iterate the src column and add the items with same MetaInfo
            //Iterator<? extends CategAmountCell> ii = src.iterator();
            for(CategAmountCell item:src.getItems()){
                
                //add terms of assistance total items if this subcategory was forced from above.
                //this means ALL cells be added here regardless of their category
                if(element.getCategory().equals(ArConstants.TERMS_OF_ASSISTANCE) && 
                        element.getValue().equals(ArConstants.TERMS_OF_ASSISTANCE_TOTAL)){
                        cc.addCell(item);
                        continue;
                }
                
                if(element.getCategory().equals(ArConstants.MODE_OF_PAYMENT) && 
                        element.getValue().equals(ArConstants.MODE_OF_PAYMENT_TOTAL)){
                        cc.addCell(item);
                        continue;
                }
                
                //add totals for quarters for each year
                if(element.getCategory().equals(ArConstants.QUARTER) && 
                        element.getValue().equals(ArConstants.QUARTERS_TOTAL)){
                        cc.addCell(item);
                        continue;
                }
                
                // if now we are creating a REAL DISBURSEMENTS column, we do it by cloning the ACTUAL DISBURSEMENTS cells
                // the cloning is followed by a deep copy of metadata, because as a further step, metadata in ACTUAL/REAL disbursements cells is altered and just doing a clone() does not duplicate metaData in deep mergedCells
                if (category.equals(ArConstants.FUNDING_TYPE) &&
                    element.getCategory().equals(ArConstants.FUNDING_TYPE) &&
                    itemIsRelevant(item, element.getValue().toString()))
                {
                    //
                    if (ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsKey(element.getValue().toString()) && (!item.isNonDirectedTransaction(element.getValue().toString())))
                        continue;
                    try
                    {
                        Cell obj = (Cell)(item.clone());
                        if (obj instanceof CategAmountCell)
                            ((CategAmountCell) obj).cloneMetaData();
                        cc.addCell(obj);
                    }
                    catch(Exception e)
                    {
                        logger.warn("while generating REAL DISBURSEMENTS, error cloning cell " + item.toString());
                        // do nothing
                    }
                    
                    continue;
                }
                
                if(item.hasMetaInfo(element)){
                    cc.addCell(item);
                }
            }
            
        }
        
        if (category.equals(ArConstants.FUNDING_TYPE))
            ret.detachCells();
        
        // Start AMP-2724
        if(category.equals(ArConstants.FUNDING_TYPE)) {
            
            int index=0;
            List theItems = ret.getItems();
            if (reportMetadata.needsTotalCommitments())
            {
    
                TotalCommitmentsAmountColumn tac = new TotalCommitmentsAmountColumn(
                        ArConstants.TOTAL_COMMITMENTS);
                
               
                index = reportMetadata.getMeasureOrder(ArConstants.TOTAL_COMMITMENTS) - 1;
                
                theItems.add(index <  0  || index > theItems.size() ?  theItems.size() : index, tac);
                
                tac.setParent(ret);
                
                tac.setContentCategory(category);
                
                Iterator it = src.iterator();
                while (it.hasNext()) {
                    AmountCell element = (AmountCell) it.next();
                    tac.addCell(element);
                }
    
            }
            if (ARUtil.containsMeasure(ArConstants.UNDISBURSED_BALANCE,
                    reportMetadata.getMeasures())) {

                TotalComputedMeasureColumn tcmc = new TotalComputedMeasureColumn(

                ArConstants.UNDISBURSED_BALANCE);

                index = reportMetadata
                        .getMeasureOrder(ArConstants.UNDISBURSED_BALANCE) - 1;

                theItems.add(
                        index < 0 || index > theItems.size() ? theItems.size()
                                : index, tcmc);

                tcmc.setParent(ret);

                tcmc.setContentCategory(category);

                tcmc.setExpression(MathExpressionRepository.UNDISBURSED_BALANCE);

                Iterator<? extends CategAmountCell> it = src.iterator();

                while (it.hasNext()) {

                    AmountCell element = (AmountCell) it.next();

                    tcmc.addCell(element);

                }

            }
        }
        // End AMP-2724

        if (ret.getItems().isEmpty() && ArConstants.TRANSACTION_TYPE_TO_DIRECTED_TRANSACTION_VALUE.values().contains(category))
            return ret; // empty, drop everything else to the hell
        
//        if (ret.getItems().isEmpty() && category.equals(ArConstants.TRANSACTION_REAL_DISBURSEMENT_TYPE) && 
//          src.getContentCategory().equals(ArConstants.FUNDING_TYPE) && src.getName().equals(ArConstants.ACTUAL_DISBURSEMENTS))
//        {
//          return null; // nothing to do, keep initial column
//        }
        
        if(ret.getItems().size()==0) {
            AmountCellColumn acc = new AmountCellColumn(ret);
            for(CategAmountCell element:src.getItems()){
                acc.addCell(element);
            }
            
            ret.getItems().add(acc);
            acc.setParent(ret);
        }
        
        return ret;
    }

    /**
     * Constructs a GroupColumn from another Column. The resulting GroupColumn will inherit the parent and the name of its 
     * predecessor.
     * @param src the predecessor Column
     */
    public GroupColumn(Column src) {
        super(src.getParent(),src.getName());
        this.setContentCategory(src.getContentCategory());
    }
    
    /**
     * Constructs a GroupColumn using a given name
     * @param name the name of the new GroupColumn
     */
    public GroupColumn(String name) {
        super(name);
    }
    
    
    private GroupColumn() {
        super();
    }
    
    public static long addColumnCalls = 0;
    public static long addColumnAddingCalls = 0;
    public static long addColumnTotalItems = 0;
    
    /**
     * Adds a Column to this GroupColumn. The Parent property of the added Column will be set to this GroupColumn.
     * If the column already exists then the items are appended to the column end so we'll end up with one 
     * unique column per group
     * @param c
     */
    public void addColumn(Column c) {
        addColumnCalls ++;
        addColumnTotalItems += items.size();
        
        if (!items.contains(c)){
            getItems().add(c);        
            c.setParent(this);
        } else {
            addColumnAddingCalls ++;
            Column older = (Column) items.get(items.indexOf(c));
            older.getItems().addAll(c.getItems());
        }
    }
    
    public void addColumn(Integer idx,Column c){
        if (items.size() < idx.intValue() || idx.intValue() == 0) 
            addColumn(c);
        else {
            if(idx.intValue() + 1 > items.size()) 
                getItems().add(idx.intValue() - 1, c);
            else
                getItems().add(idx.intValue(), c);
            c.setParent(this);
            }
    }

    /**
     * Returns a specific column based on its ColumnId. The ColumnId is an arbitrary object that uniquely identifies this
     * column.  
     * @param columnId the columnId to be used to track the Column
     * @return the requested Column or null if not found
     * @see Column
     */
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
     * returns a subcolumn with a matching name OR null, if none found
     * @param name
     * @return
     */
    public Column getColumnByName(String name)
    {
        for(Column col:this.getItems())
            if (col.getName().equals(name))
                return col;
        return null;
    }
    /**
     * Returns the Column occupying the specific position in the internal list of this GroupColumn 
     * @param idx the index of the requested column
     * @return the requested column
     */
    public Column getColumn(int idx) {
        return (Column) getItem(idx);
    }

    /**
     * Replaces the column identified by columnId with the new specified Column
     * @param columnId the identifier for the column to be replaced
     * @param newColumn the new column
     */
    public void replaceColumn(Object columnId, Column newColumn) {
        int idx = items.indexOf(getColumn(columnId));
        if (idx < 0)
            throw new RuntimeException("column-to-be-replaced with id " + columnId + " not found in GroupColumn " + this.getName());
        getItems().remove(idx);
        getItems().add(idx, newColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
     */
    @Override
    public Column filterCopy(Cell filter, Set<Long> ids) {
        GroupColumn dest = new GroupColumn(this.getName());
        for(Column element:items) {
            dest.addColumn(element.filterCopy(filter, ids));
        }
        return dest;

    }

    @Override
    public void filterByIds(Set<Long> idsToRetain)
    {
        for(Object obj:items)
        {
            Column column = (Column) obj;
            column.filterByIds(idsToRetain);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#process()
     */
    public Column postProcess() {
        GroupColumn dest = new GroupColumn(this);
        Iterator i = this.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            dest.addColumn(element.postProcess());

        }
        return dest;
    }
       
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
     */
    @Override
    public List<Column> getSubColumns(int depth) {
        ArrayList<Column> ret=new ArrayList<Column>();
        if (depth < this.positionInHeading.getStartRow())
            return ret; // we are at a lower depth than this column - nothing starts here
        if (depth == this.positionInHeading.getStartRow())
        {
            // this GroupColumn starts here -> add it
            ret.add(this);
            return ret;
        }
        // none of the previous applied, so one of the (direct/indirect) children might start at this depth -> search them 
        for(Column subColumn:this.getItems())
        {
            ret.addAll(subColumn.getSubColumns(depth));
        }
        return ret;
    }

    public int getChildrenMaxRowSpan() {        
        if (this.getItems().isEmpty()) {
            return 0;
        }
        int maxColSpan = 0;
        for(Column c:this.getItems())
        {
            if (c != null)
                maxColSpan = Math.max(maxColSpan, c.calculateTotalRowSpan());
        }
        return maxColSpan;
    }
    
    /**
     * gets the max of all the subcolumns' rowspans and then adds the self rowspan
     * only called once, when a CRD is initialized. After that, get the info from positionInHeading
     */
    @Override
    public int calculateTotalRowSpan()
    {
        if (this.getItems().isEmpty()) {
            return 0;
        }
        return getChildrenMaxRowSpan() + getRowSpanInHeading_internal();
    }
    
    @Override
    public int getRowSpanInHeading_internal() {
//      if (isTotalColumn())
//          return 2;
        return 1;
    }
    
    @Override
    public void setPositionInHeadingLayout(int totalRowSpan, int startingDepth, int startColumn)
    {
        int selfRowSpan = this.isTotalColumn() ? totalRowSpan - getChildrenMaxRowSpan() : 1;
        //selfRowSpan = getRowSpanInHeading_internal();
        if (selfRowSpan <= 0)
            throw new RuntimeException("selfRowSpan should be >= 1!");
        this.positionInHeading = new ReportHeadingLayoutCell(this, startingDepth, totalRowSpan, selfRowSpan, startColumn, this.getWidth(), this.getName());
        //super.setPositionInHeadingLayout(totalRowSpan, startingDepth);
        if (this.getItems() == null)
            return;
        int startColumnSum = 0;
        for(Column item:this.getItems())
        {
            item.setPositionInHeadingLayout(totalRowSpan - this.positionInHeading.getRowSpan(), startingDepth + this.positionInHeading.getRowSpan(), startColumn + startColumnSum);
            startColumnSum += item.getWidth();
        }
    }
    
    public boolean isTotalColumn(){
        return this.getName().equals(ArConstants.COLUMN_TOTAL);
    }
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
     */
    public Set getOwnerIds() {
        TreeSet ret=new TreeSet();
        Iterator i=items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            ret.addAll(element.getOwnerIds());
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getTrailCell()
     */
    public Cell getTrailCell() {
        return null;
    }

    protected boolean fundingYearPassesFilter(Column column, AmpARFilter filter)
    {
        String yearStr = column.getName();
        Integer year = AmpReportGenerator.getYearInteger(yearStr);
        if (year == null)
            return true;
        return filter.passesYearRangeFilter(year);
    }       
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getTrailCells()
     */
    public List<AmountCell> getTrailCells() {
        ArrayList<AmountCell> ret = new ArrayList<AmountCell>();
        if (this.items.isEmpty())
        {
            for(int i = 0; i < this.getWidth(); i++)
            {
                AmountCell ac = new AmountCell();
                ac.setColumn(this);
                ret.add(ac);
            }
            return ret;
        }
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            boolean passesFilter = ArConstants.COLUMN_FUNDING.equals(this.getName()) && fundingYearPassesFilter(element, this.getReportGenerator().getFilter());
            passesFilter |= (!ArConstants.COLUMN_FUNDING.equals(this.getName()));
            if (passesFilter)
            {
                List subTrailCells = element.getTrailCells();
                if (subTrailCells != null)
                    ret.addAll(subTrailCells);
            }
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#newInstance()
     */
    public Column newInstance() {
        return new GroupColumn(this);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
     */
    public int getColumnDepth() {
        int ret=0;
        Iterator i=items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            ret+=element.getColumnDepth();          
        }
        return ret;
    }

    
    public int getRowSpan() {
        return 1+items.size();
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#cellCount()
     */
    public int getCellCount() {
        int count=0;
        Iterator i=items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            count+=element.getCellCount();
        }
        return count;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Column#getVisibleCellCount()
     */
    public int getVisibleCellCount(Long ownerId) {
        int count=0;
        Iterator i=items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            count+=element.getVisibleCellCount(ownerId);
            }
            return count;
    }

    
    public boolean hasTrailCells() {
        Iterator i=items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            if(element.hasTrailCells()) return true;
        }
        return false;
    }
        
    @Override
    public boolean removeEmptyChildren(boolean checkFunding) {
        List<Column> myItems    = getItems();
        Iterator<Column> iter   = myItems.iterator();
        boolean allEmpty        = true;
        while ( iter.hasNext() ) {
            Column col          = iter.next();
            if ( checkFunding && !ArConstants.COLUMN_FUNDING.equals(col.name)) {
                continue;
            }
            if ( checkFunding && col.getItems().size() == 1 ) {
                Object childObj=col.getItems().get(0);
                if(childObj instanceof Column){
                    Column child    = (Column) col.getItems().get(0);
                    if ( ArConstants.COLUMN_FUNDING.equals(child.name) )
                        iter.remove();
                    continue;
                }
                
            }
            if ( col.removeEmptyChildren(false) ) {
                if (col instanceof GroupColumn)
                    iter.remove();
            }
            else
                allEmpty        = false;
        }
        return allEmpty;
    }
    
    @Override
    public Column hasSorterColumn(String namePath) {
        if (items != null && items.size() > 0) {
            Iterator<Column> it = items.iterator();
            while (it.hasNext() ) {
                Column result   = it.next().hasSorterColumn(namePath);
                if ( result != null )
                    return result;
                
            }
        }
        
        return null;
    }
    
    @Override
    public List<Cell> getAllCells(List<Cell> src, boolean freeze)
    {
        for(Object obj:items)
            ((Column) obj).getAllCells(src, freeze);
        return src;
    }
    
    @Override
    public void deleteByOwnerId(Set<Long> ownerId)
    {
        for(Object columnObj:items)
            ((Column) columnObj).deleteByOwnerId(ownerId);
    }
    
    @Override
    public boolean isSortableBy()
    {
        return false;
    }
}

