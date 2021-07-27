/**
 * CategAmountCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Categorizable;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.CommitmentGapCellGenerator;
import org.dgfoundation.amp.ar.FundingTypeSortedString;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.MetaInfoSet;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.helper.Constants;

import java.util.Arrays;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006 AmountCell that also holds metadata
 */
public class CategAmountCell extends AmountCell implements Categorizable {

    protected MetaInfoSet metaData;

    @Override
    public AmountCell merge(Cell c) {
        AmountCell ret=(AmountCell) super.merge(c);
        CategAmountCell realRet=new CategAmountCell(ret.getOwnerId());
        realRet.getMergedCells().addAll(ret.getMergedCells());
        CategAmountCell categ=(CategAmountCell) c;
        realRet.getMetaData().addAll(categ.getMetaData());
        return realRet;
    }   
    @Override
    public void merge(Cell c1, Cell c2) {
        super.merge(c1, c2);
        CategAmountCell categ1=(CategAmountCell) c1;
        CategAmountCell categ2=(CategAmountCell) c2;
        categ1.getMetaData().addAll(categ2.getMetaData());
    }
    
    @Override
    public void mergeWithCell(AmountCell anoth)
    {
        super.mergeWithCell(anoth);
        CategAmountCell another = (CategAmountCell) anoth;
        for(MetaInfo mi:another.getMetaData())
        {
            if (!this.hasMetaInfo(mi))
                this.getMetaData().add(mi);
        }
    }
    
    /**
     * this item is a customized show only for cummulative amounts
     */
    protected boolean cummulativeShow;
    
    /**
     * @return Returns the cummulativeShow.
     */
    public boolean isCummulativeShow() {
        return cummulativeShow;
    }

    @Override
    public AmountCell newInstance() {
        return new CategAmountCell();
    }
    
    /**
     * @param cummulativeShow The cummulativeShow to set.
     */
    public void setCummulativeShow(boolean cummulativeShow) {
        this.cummulativeShow = cummulativeShow;
    }

    /**
     * @return Returns the metaData.
     */
    public MetaInfoSet getMetaData() {
        return metaData;
    }

    /**
     * @param metaData
     *            The metaData to set.
     */
    public void setMetaData(MetaInfoSet metaData) {
        this.metaData = metaData;
    }

    public String getMetaValueString(String category) {
        if (metaData == null)
            return null;
        MetaInfo mi = metaData.getMetaInfo(category);
        if (mi == null || mi.getValue()==null)
            return null;
        return  mi.getValue().toString();
    }

    
    public boolean existsMetaString(String category) {
        MetaInfo mi = metaData.getMetaInfo(category);
        if (mi != null)
            return true;
        return false;
        
    }


    public CategAmountCell() {
        super();
        metaData = new MetaInfoSet();
    }

    public Class getWorker() {
        return CategAmountColWorker.class;
    }

    /**
     * @param ownerId
     * @param name
     * @param value
     */
    public CategAmountCell(Long id) {
        super(id);
        metaData = new MetaInfoSet();
    }

    public void setValue(Object o) {
        this.amount = ((Double) o).doubleValue();
    }

    public Object getValue() {
        return new Double(amount);
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

    public final static MetaInfo<Boolean> disablePercentMetaInfo = new MetaInfo<Boolean>(ArConstants.DISABLE_PERCENT, Boolean.TRUE);
    
    /**
     * this is an ugly hack because Real Disbursements + Actual Disbursements are both fetched off the same data source and are basically cloned with somewhat differing metadata
     */
    //public final static MetaInfo<Boolean> isRealDisbursementsColumn = new MetaInfo<Boolean>(ArConstants.IS_REAL_DISBURSEMENTS_COLUMN, Boolean.TRUE);
    
public void applyMetaFilter(String columnName,Cell metaCell,CategAmountCell ret, boolean hierarchyPurpose, boolean disablePercentage) {
    
    if(metaCell.getColumn().getName().equals(columnName) ) {
        if (disablePercentage)
            ret.getMetaData().add(disablePercentMetaInfo); // mark transaction as "dedicated" so that it doesn't get ignored in AmountCell::getAmount()
        
        //we need to get the percentage, it is stored in the MetaText of related to the owner of the current cell
        CellColumn c = (CellColumn) metaCell.getColumn();
        Cell temp   = c.getByOwnerAndValue(this.getOwnerId(), metaCell.getValue());
        if ( temp instanceof MetaTextCell) {
            MetaTextCell relatedHierarchyCell=(MetaTextCell) temp;
            if ((relatedHierarchyCell != null) && (!disablePercentage)) 
            { 
                MetaInfo percentMeta = relatedHierarchyCell.getMetaData().getMetaInfo(ArConstants.PERCENTAGE);
                if(percentMeta != null) {
                    Double percentage = (Double) percentMeta.getValue() ;
                    ret.setPercentage(percentage.doubleValue(), relatedHierarchyCell, hierarchyPurpose);            
                }
            }
        }
    }
    
}
    

    protected boolean passesFilter(String columnName, Cell metaCell, CategAmountCell ret)
    {
        if ( metaCell.getColumn().getName().equals(columnName) )
        {
            String meta = ret.getMetaValueString(columnName);
            return meta == null || metaCell.getValue().toString().equals(meta);
        }

        return true; // not filtering on this column - so filter is "passed"
    }
    
    /**
     * column names by which to filter funding data
     */
    public final static String[] fundingFilteringColumnsArr = 
        {
            ArConstants.COLUMN_CAPITAL_EXPENDITRURE, ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT,
            ArConstants.DONOR_GROUP, ArConstants.DONOR_TYPE_COL, ArConstants.TERMS_OF_ASSISTANCE, ArConstants.FINANCING_INSTRUMENT, ArConstants.ACTIVITY_PLEDGES_TITLE_NAME,
            ArConstants.FUNDING_STATUS, ArConstants.MODE_OF_PAYMENT, ArConstants.COMPONENT_NAME, ArConstants.COMPONENT_TYPE_S, ArConstants.AGREEMENT_CODE, ArConstants.AGREEMENT_TITLE_CODE, 
            ArConstants.DISASTER_RESPONSE_MARKER, ColumnConstants.EXPENDITURE_CLASS,
            ArConstants.PLEDGES_METADATA_NAME + ArConstants.TERMS_OF_ASSISTANCE
        };
    public final static Set<String> fundingFilteringColumns = new HashSet<String>(Arrays.asList(fundingFilteringColumnsArr));
    
    public void cloneMetaData()
    {
        this.metaData = new MetaInfoSet(this.metaData); // do a distinct, unshared copy with other instances
        for(AmountCell amCell:mergedCells)
            if (amCell instanceof CategAmountCell)
                ((CategAmountCell) amCell).cloneMetaData();     
    }
    
    public void removeDirectedFundingMetadata()
    {       
        this.metaData.removeItemsByCategory(Arrays.asList(ArConstants.RECIPIENT_NAME, ArConstants.RECIPIENT_ROLE_NAME, ArConstants.RECIPIENT_ROLE_CODE, ArConstants.SOURCE_ROLE_CODE));
        this.metaData.removeItemsByCategory(ArConstants.TRANSACTION_TYPE_TO_DIRECTED_TRANSACTION_VALUE.values());
        for(AmountCell amCell:mergedCells)
            if (amCell instanceof CategAmountCell)
                ((CategAmountCell) amCell).removeDirectedFundingMetadata();
    }
    
    public boolean passesDirectedTransactionFilter(Cell metaCell)
    {
        boolean passesTest = false;
        String columnRoleCode = ArConstants.COLUMN_ROLE_CODES.get(metaCell.getColumn().getName());
        
        String sourceRole = this.getMetaValueString(ArConstants.SOURCE_ROLE_CODE);
        String sourceName = this.getMetaValueString(ArConstants.DONOR);
        String recipientRole = this.getMetaValueString(ArConstants.RECIPIENT_ROLE_CODE);
        String recipientName = this.getMetaValueString(ArConstants.RECIPIENT_NAME);
        
//      if ((sourceRole == null) || (sourceName == null) || (recipientRole == null) || (recipientName == null) || (metaCell == null) || (metaCell.getValue() == null))
//          //System.out.println("my name is msh");
        
        passesTest = (sourceRole.equals(columnRoleCode) && sourceName.equals(metaCell.getValue().toString())) // all transactions have a non-null source
                    ||
                    (recipientRole != null && recipientName != null && recipientRole.equals(columnRoleCode) && recipientName.equals(metaCell.getValue().toString()));

        return passesTest;
    }
    
@Override
public Cell filter(Cell metaCell, Set ids) {
    CategAmountCell ret = (CategAmountCell) super.filter(metaCell,ids);
    if(ret==null) return null;
        
//  if (this.hasMetaInfo(isRealDisbursementsColumn))
//      ret.getMetaData().add(isRealDisbursementsColumn); // super.filter does NOT copy metadata except from children 
        
    if (fundingFilteringColumns.contains(metaCell.getColumn().getName()))
    {
        if (!passesFilter(metaCell.getColumn().getName(), metaCell, ret))
            return null;
    }
    
    
    if (!passesFilter(ArConstants.RELATED_PROJECTS, metaCell, ret)){
        // relatedProject filter is enabled but does not pass -> let's decide whether it is relevant
        // it is relevant when NOT doing one of: ACTUAL_PLEDGE
        if (!ret.isGlobalAmount()){
            return null;
        }
    }
    
    if (metaCell.getColumn().getName().equals(ArConstants.DONOR) || metaCell.getColumn().getName().equals(ArConstants.RELATED_PROJECTS))
    {
        if (!passesFilter(metaCell.getColumn().getName(), metaCell, ret))
            if (!ret.isGlobalAmount())
                return null;  
    }
    
    if (metaCell.getColumn().getName().equals(ArConstants.COLUMN_LOC_ADM_LEVEL_1)
            && this.getNearestReportData().getReportMetadata().getType() == ArConstants.REGIONAL_TYPE) {
        String retRegionName = ret.getMetaValueString(ArConstants.COLUMN_LOC_ADM_LEVEL_1);
        retRegionName = (retRegionName != null) ? retRegionName.trim() : retRegionName;
        
        if (retRegionName != null && !metaCell.getValue().toString().equals(retRegionName)) {
            return null;
        }
    }
    
    if (metaCell.getColumn().getName().equals(ArConstants.COLUMN_LOC_ADM_LEVEL_3)
            && this.getNearestReportData().getReportMetadata().getType() == ArConstants.REGIONAL_TYPE) {
        
        String retDistrictName = ret.getMetaValueString(ArConstants.COLUMN_LOC_ADM_LEVEL_3);
        retDistrictName = (retDistrictName != null) ? retDistrictName.trim() : retDistrictName;
        if (retDistrictName != null && !metaCell.getValue().toString().equals(retDistrictName)) {
            return null;
        }
    }
    
    if (metaCell.getColumn().getName().equals(ArConstants.COLUMN_LOC_ADM_LEVEL_2)
            && this.getNearestReportData().getReportMetadata().getType() == ArConstants.REGIONAL_TYPE) {
        String retZoneName = ret.getMetaValueString(ArConstants.COLUMN_LOC_ADM_LEVEL_2);
        retZoneName = (retZoneName != null) ? retZoneName.trim() : retZoneName;
        if (retZoneName != null
                && !metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.COLUMN_LOC_ADM_LEVEL_2))) {
            return null;
        }
    }
    
    boolean disablePercentage = false; // whether this cell should NOT have percentages applied to its value when filtering - this is for transactions towards an organisation
    if (mergedCells.isEmpty() && ArConstants.COLUMN_ANY_RELATED_ORGS.contains(metaCell.getColumn().getName()))
    {
        // only apply the test for "basic" CategAmountCell's, as merged ones merge all the metadata from all the merged cells and we might filter out unneedingly
        // if column by which we are doing the hierarchy is one of the related organisations' column
        String relatedOrgRole = this.getMetaValueString(ArConstants.RECIPIENT_ROLE_NAME);
        String relatedOrgName = this.getMetaValueString(ArConstants.RECIPIENT_NAME);
        
        String srcOrgRole = this.getMetaValueString(ArConstants.SOURCE_ROLE_NAME);
        String srcOrgName = this.getMetaValueString(ArConstants.DONOR);

        String currentOrgName = metaCell.getValue().toString();
        
        if (metaCell.getColumn().getName().equals(relatedOrgRole))
        {
            // doing hierarchy by "Implementing Agency" and this is a transaction towards an Implementing Agency org
            if (relatedOrgName != null) {
                // this funding is directed to a certain organisation only
                if (!currentOrgName.equals(relatedOrgName))
                    return null; // filter out
                disablePercentage = true;
            }
        }
        
        if (metaCell.getColumn().getName().equals(srcOrgRole))
        {
            // doing hierarchy by "Implementing Agency" and this is a transaction towards an Implementing Agency org
            if (relatedOrgName != null) {
                // this funding is directed to a certain organisation only
                if (!currentOrgName.equals(srcOrgName))
                    return null; // filter out
                disablePercentage = true;
            }
        }

    }
    

    /**
     * when doing a hierarchy by Donor Agency, the <Real Disbursements> column should only have flow DN-EXEC shown
     * when doing a hierarchy by Executing Agency, the <Real Disbursements> column should only have flows DN-EXEC and EXEC-IMPL shown
     * when doing a hierarchy by Implementing Agency, the <Real Disbursements> column should only have flows EXEC-IMPL and IMPL-BENF shown
     * when doing a hierarchy by Beneficiary Agency, the <Real Disbursements> column should only have flow IMPL-BENF shown
     * 
     * also see AmpReportGenerator.removeUnusedRealDisbursementsFlowsFromReport
     */
    String directedType = null;
    for(String directed_tr_type:ArConstants.TRANSACTION_TYPE_TO_DIRECTED_TRANSACTION_VALUE.values())
        if (directedType == null)
            directedType = this.getMetaValueString(directed_tr_type);
    
    boolean isDirectedTransaction = directedType != null;
    boolean isMtefCell = this.existsMetaString(ArConstants.IS_AN_MTEF_FUNDING);
    
    boolean doingADirectedTransactionFilter = isDirectedTransaction && mergedCells.isEmpty() && ArConstants.COLUMN_ROLE_CODES.containsKey(metaCell.getColumn().getName());
    boolean doingAMtefFilter = isMtefCell && mergedCells.isEmpty() && ArConstants.COLUMN_ROLE_CODES.containsKey(metaCell.getColumn().getName());
    
    if (doingADirectedTransactionFilter || doingAMtefFilter)
    {
        boolean passesTest = passesDirectedTransactionFilter(metaCell);
        if (!passesTest)
            return null;
    }
    
    //apply metatext filters
    if (metaCell instanceof MetaTextCell) {
        for (AmpReportHierarchy col : this.getNearestReportData().getReportMetadata().getHierarchies()) {
            if (col.getColumn().getCellType().contains(MetaTextCell.class.getSimpleName())) {
                //NEVER apply this for regional reports with regional metaCell:
                if (metaCell.getColumn().getName().equals(ArConstants.COLUMN_LOC_ADM_LEVEL_1)
                        && this.getNearestReportData().getReportMetadata().getType() == ArConstants.REGIONAL_TYPE) {
                    continue;
                }
            }
            //column is needed to get the tokenExpression on computed fields
            ret.setColumn(this.getColumn());
            applyMetaFilter(col.getColumn().getColumnName(), metaCell, ret, true, disablePercentage);
        }
    }
        
        //if(ret.getMergedCells().size()>0) 
            //logger.info(ret.getMergedCells());
    return ret;
}   
        
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Categorizable#hasMeta(org.dgfoundation.amp.ar.MetaInfo)
     */
    public boolean hasMetaInfo(MetaInfo m) 
    {
        if (metaData == null || m == null || m.getCategory() == null)
            return false;
        MetaInfo internal = metaData.getMetaInfo(m.getCategory());
        if (internal == null ) {
            return false;
        }       
        return internal.equals(m);
    }
    
    /**
     * returns true IFF this cell holds an amount which is not splitable by hier.
     * Examples: ACTUAL PLEDGE (total amount pledged for a pledge)
     * @return
     */
    public boolean isGlobalAmount(){
        boolean isAmountIndependentOfHier = ArConstants.PLEDGE.equals(getMetaValueString(ArConstants.TRANSACTION_TYPE));
        isAmountIndependentOfHier |= CommitmentGapCellGenerator.COMMITMENT_GAP_FUNDING_TYPE.equals(this.getMetaData().getMetaInfo(ArConstants.FUNDING_TYPE));
        return isAmountIndependentOfHier;
    }

    /**
     * cleans all metadata which is safe to be deleted after the report has been generated and is only used for viewing
     * @param cell
     * @return
     */
    public int clearMetaData()
    {
        if (metaData != null)
        {
            int sz = metaData.size();
            metaData.clear();
            metaData = null;
            return sz;
        }
        return 0;
    }
    
    /**
     * returns true IFF an actual disbursement has a recipient specified 
     * @param item
     * @return
     */
    public boolean isDirectedTransaction(String rawTransaction) {
        if (rawTransaction == null)
            return false;
        if (!ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsKey(rawTransaction))
            return false;
            
        MetaInfoSet metaData = this.getMetaData();
        MetaInfo fundingTypeMetaInfo = metaData.getMetaInfo(ArConstants.FUNDING_TYPE); 
        
        boolean isActualDisbursement = (fundingTypeMetaInfo != null) && fundingTypeMetaInfo.getValue().toString().equals(rawTransaction);
        boolean hasDestination = metaData.hasMetaInfo(ArConstants.RECIPIENT_NAME);      
            
        return isActualDisbursement && hasDestination;
    }

    /**
     * returns true iff an actual disbursement has the source not specified or specified as a donor (mirrors {@link org.digijava.module.aim.dbentity.AmpFunding#detachCells(Column)})
     * @param item
     * @return
     */
    public boolean isNonDirectedTransaction(String rawTransaction) {
        if (rawTransaction == null)
            return false;
        if (!ArConstants.NONDIRECTED_MEASURE_TO_DIRECTED_MEASURE.containsKey(rawTransaction))
            return false;

        MetaInfoSet metaData = this.getMetaData();
        MetaInfo fundingTypeMetaInfo = metaData.getMetaInfo(ArConstants.FUNDING_TYPE);
        MetaInfo sourceRoleMetaInfo = metaData.getMetaInfo(ArConstants.SOURCE_ROLE_CODE);
        
        boolean isActualDisbursement = (fundingTypeMetaInfo != null) && fundingTypeMetaInfo.getValue().toString().equals(rawTransaction);
        boolean hasSource = sourceRoleMetaInfo != null;
        boolean hasDonorSource = hasSource && (sourceRoleMetaInfo.getValue().toString().equals(Constants.ROLE_CODE_DONOR));

        return isActualDisbursement && ((!hasSource) || (hasSource && hasDonorSource));
    }    

    @Override
    public String prettyPrint()
    {
        String isRealTransaction = (this.metaData != null && this.metaData.hasMetaInfo(ArConstants.RECIPIENT_ROLE_CODE)) ? "Real " : "";
        return String.format("%s%s %s %s on %s", isRealTransaction, this.getMetaValueString(ArConstants.ADJUSTMENT_TYPE), this.getMetaValueString(ArConstants.TRANSACTION_TYPE),  
                this.toString(), this.getMetaValueString(ArConstants.QUARTER) + " " + this.getMetaValueString(ArConstants.YEAR));
    }
    
}
