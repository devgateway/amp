/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.ComparableMonth;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.FeaturesUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends MetaCellColumnWorker {

    private static Logger logger    = Logger.getLogger(CategAmountColWorker.class);
            
    /**
     * @param condition
     * @param viewName
     * @param columnName
     */
    public CategAmountColWorker(String condition, String viewName,
            String columnName,ReportGenerator generator) {
        super(condition, viewName, columnName,generator);
    }

    /**filter.getFromYear()!=null
     * Decides if the CategAmountCell is showable or not, based on the measures selected
     * in the report wizard.
     * @param td the sql transaction date for the cell
     * @return true if showable
     */
    public boolean isShowable(Date td) {
        boolean showable=true;
        
        //proposed cost is by default not showable and should not appear in any funding totals. it is used to ease the use of destination post processed columns
        if (this.getViewName().equals(ArConstants.VIEW_PROPOSED_COST) || this.getViewName().equals(ArConstants.VIEW_REVISED_COST))
            return false;
        AmpARFilter filter=(AmpARFilter) generator.getFilter();
        
    
        //we now check if the year filtering is used - we do not want items from other years to be shown
        //now this is null due we have one field 
        try {
            java.util.Date fromDate = filter.buildFromDateAsDate();
            java.util.Date toDate = filter.buildToDateAsDate();
            
            if (fromDate != null || toDate != null) {
            //  java.util.Date tDate=(java.util.Date) MetaInfo.getMetaInfo(td.getMetaData(),ArConstants.TRANSACTION_DATE).getValue();
                java.util.Date tDate=new Date(td.getTime());
                
                if (fromDate != null && tDate.before(fromDate)){
                    showable = false;
                }
                
                if (toDate != null && tDate.after(toDate)){
                    showable = false;
                }
        }
            
            
        } catch (Exception e) {
            logger.error("Can't define if cell is Showable possible parse error detected",e );
        }

        return showable;
    }

    
//  public boolean isRenderizable(CategAmountCell cac) {
//      boolean renderizable=true;
//      
//  
//      AmpARFilter filter=(AmpARFilter) generator.getFilter();
//      
//      
//      //we now check if the year filtering is used - we do not want items from other years to be shown
//      if((filter.getRenderStartYear()!=null && filter.getRenderStartYear()> 0) || (filter.getRenderEndYear()!=null && filter.getRenderEndYear() > 0  )) {
//          Integer itemYear=(Integer) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.YEAR).getValue();
//          
//          if(filter.getRenderStartYear()!=null &&  filter.getRenderStartYear() > 0 &&
//              itemYear.intValue() < filter.getRenderStartYear().intValue()) renderizable=false;
//          
//          if(filter.getRenderEndYear()!=null &&  filter.getRenderEndYear()>0 &&
//              itemYear.intValue() > filter.getRenderEndYear().intValue()) renderizable=false;
//      }
//      return renderizable;
//  }
    
    protected boolean isCummulativeShowable(CategAmountCell cac) {
        AmpARFilter filter=(AmpARFilter) generator.getFilter();
        if(filter.getYearTo()==null) return true;
        int cellYear=Integer.parseInt(cac.getMetaValueString(ArConstants.YEAR));
        if(cellYear>filter.getYearTo().intValue()) return false;
        return true;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet,
     *      java.lang.String)
     */
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        String baseCurrency = FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY );
        if ( baseCurrency == null )
            baseCurrency = Constants.DEFAULT_CURRENCY;
        
        Long ownerId = rs.getLong(1);
        Long id = rs.getLong(3);
        CategAmountCell acc = new CategAmountCell(ownerId);

        acc.setId(id);

        AmpARFilter filter = generator.getFilter();
        
        
        int tr_type = -1;
        String adj_type = "";
        double tr_amount = rs.getDouble("transaction_amount");
        java.sql.Date td = rs.getDate("transaction_date");
        
        String currencyCode = "";
        
        if (columnsMetaData.containsKey("currency_code")){
            currencyCode = rs.getString("currency_code");
        }
        
        String donorGroupName = null;
        String donorTypeName = null;
        Double fixedExchangeRate = null;
        Double pledgetotal = null;
        Double capitalPercent   = null;
        
        if (columnsMetaData.containsKey("fixed_exchange_rate")){
            fixedExchangeRate = rs.getDouble("fixed_exchange_rate");
        }

        if (columnsMetaData.containsKey("adjustment_type_name"))
        {
            adj_type = rs.getString("adjustment_type_name");        
        }
        
        if(columnsMetaData.containsKey("donor_type_name"))
            donorTypeName = retrieveValueFromRS(rs,columnsMetaData.get(  "donor_type_name") );
                    
        
        if (columnsMetaData.containsKey("transaction_type")){
            tr_type  = rs.getInt("transaction_type");
        }
        
        if (columnsMetaData.containsKey("org_grp_name")) {
            donorGroupName  = retrieveValueFromRS(rs,columnsMetaData.get(  "org_grp_name") );
        }
        
        if (columnsMetaData.containsKey("total_pledged")) {
            pledgetotal = rs.getDouble("total_pledged");
        }
        
        if (columnsMetaData.containsKey("capital_spend_percent") ) {
            capitalPercent  = rs.getDouble("capital_spend_percent");
        }
        
        if (columnsMetaData.containsKey("disaster_response_code")) {
            Integer val = rs.getInt("disaster_response_code");
            String displayedVal = decodeBoolean(val);
            MetaInfo disasterResponseMeta = this.getCachedMetaInfo(ArConstants.DISASTER_RESPONSE_MARKER, displayedVal);
            acc.getMetaData().add(disasterResponseMeta);
        }
        
        if (columnsMetaData.containsKey("expenditure_class_id")/* && tr_type == Constants.EXPENDITURE*/) {
            addMetaIfExists(rs, acc, "expenditure_class_name", ColumnConstants.EXPENDITURE_CLASS, ColumnConstants.EXPENDITURE_CLASS + " Unallocated", false);
        }
        //the most important meta name, the source name (donor name, region name, component name)
        String headMetaName=rsmd.getColumnName(4).toLowerCase();
        if (this.getViewName().equals("v_proposed_cost") || this.getViewName().equals("cached_v_proposed_cost") || tr_type == Constants.ANNUAL_PROPOSED_PROJECT_COST
            || this.getViewName().equals("v_revised_project_cost") || this.getViewName().equals("cached_v_revised_project_cost"))
            headMetaName = null; // no source name for v_proposed_cost

//      String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE);
/*      boolean skpyCategorize = ("false".equalsIgnoreCase(value));
        //if (generator.getReportMetadata().getHierarchies().contains()
        //check if the column is a into Hierarchies //Ned to talk with mihai to find a better solution
        
        Set<AmpReportHierarchy> hierarchies = generator.getReportMetadata().getHierarchies(); 
        for (AmpReportHierarchy ampReportHierarchy : hierarchies) {
            if (ArConstants.TERMS_OF_ASSISTANCE.equalsIgnoreCase(ampReportHierarchy.getColumn().getColumnName())){
                skpyCategorize = false;
                break ;
            }
        } -- not used anymore */
        
        addMetaIfExists(rs, acc, "terms_assist_name", createMetadataNameBasedOnSource(ArConstants.TERMS_OF_ASSISTANCE), null, false);
        addMetaIfExists(rs, acc, "financing_instrument_name", ArConstants.FINANCING_INSTRUMENT, null, false);
        addMetaIfExists(rs, acc, "mode_of_payment_name", ArConstants.MODE_OF_PAYMENT, ArConstants.MODE_OF_PAYMENT_UNALLOCATED, false);
        addMetaIfExists(rs, acc, "funding_status_name", ArConstants.FUNDING_STATUS, null, false);
        addMetaIfExists(rs, acc, "related_project", ArConstants.RELATED_PROJECTS, null, false);
        addMetaIfExists(rs, acc, "agreement_code", ArConstants.AGREEMENT_CODE, null, false);
        addMetaIfExists(rs, acc, "agreement_title_code", ArConstants.AGREEMENT_TITLE_CODE, null, false);
        addMetaIfExists(rs, acc, "component_type", ArConstants.COMPONENT_TYPE_S, null, true);
        addMetaIfExists(rs, acc, "component_name", ArConstants.COMPONENT_NAME, null, true);

        
        fetchDirectedDisbursementMeta(rs, acc, tr_type);
        
        addMetaIfExists(rs, acc, "activity_pledges_title_name", ArConstants.ACTIVITY_PLEDGES_TITLE_NAME, null, false);
        
        MetaInfo headMeta=null;
        
        if("region_name".equals(headMetaName)){
            String regionName = retrieveValueFromRS(rs,columnsMetaData.get(  "region_name") );
            headMeta = this.getCachedMetaInfo(ArConstants.COLUMN_LOC_ADM_LEVEL_1, regionName);
        } else
            
        if("component_type".equals(headMetaName)){
            String componentType = rs.getString("component_type");
            headMeta= this.getCachedMetaInfo(ArConstants.COMPONENT_TYPE_S, componentType);          
        } else  

        if("donor_name".equals(headMetaName)){
            String donorName = retrieveValueFromRS(rs,columnsMetaData.get(  "donor_name") );
            ////System.out.println("donor name is " + donorName);
            headMeta = this.getCachedMetaInfo(ArConstants.DONOR, (donorName != null) ? donorName.trim() : donorName);           
        }

        if ("component_name".equals(headMetaName))
        {
            String componentName = rs.getString("component_name");
            headMeta = this.getCachedMetaInfo(ArConstants.COMPONENT_NAME, componentName);
        }
        
//      if (filter.getAmountinthousand() == null) {
//          filter.setAmountinthousand(Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)));
//      } 
        
        acc.setAmount(tr_amount / filter.getAmountDivider());
                
        //use fixed exchange rate only if it has been entered. Else use Agency
        if (fixedExchangeRate != null && fixedExchangeRate != 0) {
            acc.setFromExchangeRate(fixedExchangeRate);
        } else {
            acc.setFromExchangeRate(Util.getExchange(currencyCode, td));
        }
        
        acc.setCurrencyDate(td);
        acc.setCurrencyCode(currencyCode);
        //put toExchangeRate
        acc.setToExchangeRate(1);
        
        if (adj_type == null)
            return null; // no transaction to fetch
        MetaInfo adjMs = this.getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, adj_type);
        String trStr = null;

        switch (tr_type) {
        case Constants.COMMITMENT:
            trStr = ArConstants.COMMITMENT;
            break;
        case Constants.DISBURSEMENT:
            trStr = ArConstants.DISBURSEMENT;
            break;
        case Constants.MTEFPROJECTION:
            trStr = ArConstants.MTEF_PROJECTION;
            break;
            
        case Constants.EXPENDITURE:
            trStr = ArConstants.EXPENDITURE;
            break;
        case Constants.DISBURSEMENT_ORDER:
            trStr = ArConstants.DISBURSEMENT_ORDERS;
            break;
        case Constants.PLEDGES_COMMITMENT:
            trStr = ArConstants.PLEDGES_COMMITMENT;
            break;
        case Constants.PLEDGES_DISBURSEMENT:
            trStr = ArConstants.PLEDGES_DISBURSEMENT;
            break;
        case Constants.PLEDGE:
            trStr = ArConstants.PLEDGE;
            break;
        case Constants.RELEASE_OF_FUNDS:
            trStr = ArConstants.RELEASE_OF_FUNDS;
            break;
        case Constants.ESTIMATED_DONOR_DISBURSEMENT:
            trStr = ArConstants.ESTIMATED_DISBURSEMENTS;
            break;
        case Constants.ANNUAL_PROPOSED_PROJECT_COST:
            trStr = ArConstants.ANNUAL_PROPOSED_PROJECT_COST;
            break;          
        }

        if(trStr != null) {
            MetaInfo trMs = this.getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, trStr);
            String fundMes = tr_type == Constants.ANNUAL_PROPOSED_PROJECT_COST ? trStr : ((String) adjMs.getValue()+ " " + (String) trMs.getValue());
            Integer order = this.generator.getReportMetadata().getMeasureOrder(fundMes);
            MetaInfo fundMs = this.getCachedMetaInfo(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(fundMes, order));
            acc.getMetaData().add(trMs);
            acc.getMetaData().add(fundMs);
            acc.getMetaData().add(adjMs);
        }   
        
        //Group all pledges without funding year under the same fake year 
        if (td==null && trStr.equals(ArConstants.PLEDGE)){
            td= ArConstants.PLEDGE_FAKE_YEAR;
        }
        
        if (td==null) {
            logger.error("MISSING DATE FOR FUNDING id ="+id+ " of activity id ="+ ownerId);
            return null;
        }
        
        
        String quarter = null;
        ComparableMonth month = null;       
        Integer year = null;
        String fiscalYear = null;
        ComparableMonth fiscalMonth = null;
                
        if (filter.getCalendarType() != null) {
            try {
                ICalendarWorker worker = filter.getCalendarType().getworker();
                worker.setTime(td);
                month = worker.getMonth();
                quarter = "Q" + worker.getQuarter();
                year = worker.getYear();
                
                fiscalYear=worker.getFiscalYear();
                fiscalMonth=worker.getFiscalMonth();
                
                // very very very ugly workaround for AMP-19405
                if (tr_type == Constants.MTEFPROJECTION) {
                    year = td.getYear() + 1900;
                    fiscalYear = filter.getCalendarType().getIsFiscal() ?
                            (filter.getCalendarType().getStartMonthNum() == 1 ?
                                    "Fiscal Year " + (year) : 
                                    ("Fiscal Year " + (year) + " - " + (year + 1))) :
                            Integer.toString(year);
                    String check = worker.getFiscalYear();
//                  fiscalYear = new GregorianBasedWorker(td).getFiscalYear(); 
//                  String check = DateConversion.convertDateToFiscalYearString(td);
                    //System.err.format("FY vs Check: %s vs %s\n", fiscalYear, check);
                            //DateConversion.convertDateToFiscalYearString(td); // AMP-19405 - emulate AF bug in AP and reports
                }
                
                //The complete will be used to see if the cell is showable
                MetaInfo dateInfo = this.getCachedMetaInfo(ArConstants.TRANSACTION_DATE, worker.getDate());
                acc.getMetaData().add(dateInfo);
                
            } catch (Exception e) {
                logger.error("Error doing calendar calc for activity id =" + id, e);
            }
        }

        //Date handling..
        Integer computedYear=new GregorianCalendar().get(Calendar.YEAR);
          if (filter.getComputedYear()!=null){
              computedYear=filter.getComputedYear();
          }
          if (year.compareTo(computedYear)==0){
              MetaInfo computedOnYear = this.getCachedMetaInfo(ArConstants.COMPUTE_ON_YEAR, null);
              acc.getMetaData().add(computedOnYear);
          }
          
        MetaInfo qMs = this.getCachedMetaInfo(ArConstants.QUARTER,quarter);
        MetaInfo mMs = this.getCachedMetaInfo(ArConstants.MONTH,month);
        MetaInfo aMs = this.getCachedMetaInfo(ArConstants.YEAR, year);
        
        MetaInfo fmMs = this.getCachedMetaInfo(ArConstants.FISCAL_M, fiscalMonth);
        MetaInfo faMs = this.getCachedMetaInfo(ArConstants.FISCAL_Y, fiscalYear);

        
        
        
        //add the newly created metainfo objects to the virtual funding object
    
        acc.getMetaData().add(aMs);
        acc.getMetaData().add(qMs);
        acc.getMetaData().add(mMs); 
        acc.getMetaData().add(faMs);
        acc.getMetaData().add(fmMs);
        boolean headMetaCanBeNull = headMetaName == null || "v_pledges_funding_st".equals(this.getViewName()) || "cached_v_pledges_funding_st".equals(this.getViewName());
        if (headMeta != null)
        {
            acc.getMetaData().add(headMeta);
        }
        else
        {
            if (!headMetaCanBeNull)
                throw new RuntimeException("headMeta is null!");
        }
        
        if (this.getViewName().equals("v_proposed_cost") || this.getViewName().equals("cached_v_proposed_cost")) {
            //used as a flag, no value needed
            MetaInfo costMs = this.getCachedMetaInfo(ArConstants.PROPOSED_COST, null);
            acc.getMetaData().add(costMs);
        }
        
        if(donorGroupName!=null) {
            MetaInfo donorGroupMs = this.getCachedMetaInfo(ArConstants.DONOR_GROUP, donorGroupName);
            acc.getMetaData().add(donorGroupMs);
        }
        
        if(donorTypeName!=null) {
            MetaInfo donorTypeMs = this.getCachedMetaInfo(ArConstants.DONOR_TYPE_COL, donorTypeName);
            acc.getMetaData().add(donorTypeMs);
        }
        
        if(pledgetotal!=null) {
            MetaInfo pledgedtotalname = this.getCachedMetaInfo(ArConstants.PLEDGED_TOTAL, null);
            acc.getMetaData().add(pledgedtotalname);
        }
        
        if(capitalPercent != null) {
            MetaInfo capitalPercentMI = this.getCachedMetaInfo(ArConstants.CAPITAL_PERCENT, capitalPercent);
            acc.getMetaData().add(capitalPercentMI);
        }
        
        //set the showable flag, based on selected measures - THIS NEEDS TO BE MOVED OUT
        //TODO: move this to postProcess!!
        acc.setShow(isShowable(td));
        //acc.setRenderizable(isRenderizable(acc));
        acc.setCummulativeShow(isCummulativeShowable(acc));
        
        
        
        //UGLY get exchage rate if cross-rates are needed (if we need to convert from X to base currency and then to Y)
        String usedCurrency = ReportContextData.getFromRequest().getSelectedCurrencyCode();
        if(usedCurrency != null ) {
            /* If source and destination currency are the same we need to set exactly the same exchange rate for 'toExchangeRate' and 'fromExchangeRate.
             * That way, AmountCell.convert won't do any computation' */
            if (currencyCode !=null && currencyCode.equals(usedCurrency)) 
                acc.setToExchangeRate( acc.getFromExchangeRate() );
            else if ( !baseCurrency.equals(usedCurrency))  
                acc.setToExchangeRate(Util.getExchange(usedCurrency, td));
        }
        else 
            logger.error("The filter.currency property should not be null !");
                        
        fillDirectedDisbursementTypes(acc);
        return acc;
    }
        
    String translatedYes = TranslatorWorker.translateText("Yes");
    String translatedNo = TranslatorWorker.translateText("No");
    String translatedUndefined = TranslatorWorker.translateText(AmpReportGenerator.generateFakeCell(1l, this.getColumnName()).getValue().toString());
    
    protected String decodeBoolean(Integer code) {
        if (code == null) return translatedUndefined;
        if (code.intValue() == 1) return translatedYes;
        if (code.intValue() == 2) return translatedNo;
        return translatedUndefined;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
     */
    protected Cell getCellFromCell(Cell src) {
        // TODO Auto-generated method stub
        return null;
    }

    public CellColumn newColumnInstance(int initialCapacity) {
        return new AmountCellColumn(columnName,initialCapacity);
    }

    public Cell newCellInstance() {
        return new CategAmountCell();
    }
    
    @Override
    protected void cleanup()
    {
        metaInfoCache.clear();
    }
    
    private String createMetadataNameBasedOnSource(String normalName) {
        if ( ArConstants.VIEW_PLEDGES_FUNDING.equals(this.viewName) ) {
            return ArConstants.PLEDGES_METADATA_NAME + normalName;
        }
        return normalName;
    }

}
