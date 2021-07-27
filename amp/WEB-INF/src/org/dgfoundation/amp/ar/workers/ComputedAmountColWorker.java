/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalComputedAmountColumn;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class ComputedAmountColWorker extends MetaCellColumnWorker {

    /**
     * @param destName
     * @param sourceGroup
     */
    public ComputedAmountColWorker(String condition, String viewName, String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);
    }

    public ComputedAmountColWorker(String columnName, GroupColumn rawColumns, ReportGenerator generator) {
        super(columnName, rawColumns, generator);
        sourceName = ArConstants.COLUMN_FUNDING;
    }

    public CellColumn newColumnInstance(int initialCapacity) {
        TotalComputedAmountColumn cc = new TotalComputedAmountColumn(columnName, false, initialCapacity);
        cc.setWorker(this);
        cc.setDescription(this.getRelatedColumn().getDescription());
        cc.setExpression(this.getRelatedColumn().getTokenExpression());
        cc.setTotalExpression(this.getRelatedColumn().getTokenExpression());
        return cc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.
     * ResultSet)
     */
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        
        AmpARFilter filter = generator.getFilter();     
        Long ownerId = new Long(rs.getLong(1));
        Long id = new Long(rs.getLong(2));
        BigDecimal value = rs.getBigDecimal(3);
        String currencyCode = rs.getString(4);
        Date currencyDate = rs.getDate(5);
        //java.sql.Date td= rs.getDate("transaction_date");
        //double exchangeRate = rs.getDouble(6);
        //String donorName = rs.getString("donor_name");

        ComputedAmountCell ret = new ComputedAmountCell(ownerId);
        ret.setId(id);
        ret.setAmount(value.doubleValue() / filter.getAmountDivider());
        
                
        ret.setFromExchangeRate(Util.getExchange(currencyCode, currencyDate));
        //ret.setFromExchangeRate(exchangeRate);
        ret.setCurrencyDate(currencyDate);
        ret.setCurrencyCode(currencyCode);
        ret.setToExchangeRate(1);
        addMetaIfExists(rs, ret, "source_role_code", ArConstants.SOURCE_ROLE_CODE, null, false);
        
        //MetaInfo donorNameMI = getCachedMetaInfo(ArConstants.DONOR, donorName);
        //ret.getMetaData().add(donorNameMI);

        MetaInfo costMs = getCachedMetaInfo(this.getColumnName(), null);
        ret.getMetaData().add(costMs);
        
        MetaInfo dateMi = getCachedMetaInfo(ArConstants.TRANSACTION_DATE, currencyDate);
        ret.getMetaData().add(dateMi);

        if ( this.columnsMetaData.containsKey("donor_name") ) {
            String donorName        = rs.getString("donor_name");
            MetaInfo donorNameMi    = getCachedMetaInfo(ArConstants.DONOR, donorName);
            ret.getMetaData().add(donorNameMi);
        }
        if (columnsMetaData.containsKey("terms_assist_name")){
            String termsAssist = rs.getString("terms_assist_name");
            MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.TERMS_OF_ASSISTANCE,
                    termsAssist);
            ret.getMetaData().add(termsAssistMeta);
        }
        if (columnsMetaData.containsKey("financing_instrument_name")){          
            String financingInstrument = rs.getString("financing_instrument_name");
            MetaInfo termsAssistMeta = this.getCachedMetaInfo(ArConstants.FINANCING_INSTRUMENT,
                    financingInstrument);
            ret.getMetaData().add(termsAssistMeta);
        }
        if(columnsMetaData.containsKey("donor_type_name")) {
            String donorTypeName=rs.getString("donor_type_name");
            MetaInfo donorTypeMeta = this.getCachedMetaInfo(ArConstants.DONOR_TYPE_COL,
                    donorTypeName );
            ret.getMetaData().add(donorTypeMeta);
        }
        if (columnsMetaData.containsKey("org_grp_name")) {
            String donorGroupName   = rs.getString("org_grp_name");
            MetaInfo donorGroupMeta = this.getCachedMetaInfo(ArConstants.DONOR_GROUP,
                    donorGroupName );
            ret.getMetaData().add(donorGroupMeta);
        }
        
        String baseCurrency = FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY );
        if ( baseCurrency == null )
            baseCurrency = Constants.DEFAULT_CURRENCY;
        
        // UGLY get exchage rate if cross-rates are needed (if we need to
        // convert from X to USD and then to Y)
        String usedCurrency = ReportContextData.getFromRequest().getSelectedCurrencyCode();
        if (usedCurrency != null && !baseCurrency.equals(usedCurrency))
            ret.setToExchangeRate(Util.getExchange(usedCurrency, currencyDate));

        if (this.getViewName().equals("v_mtef_funding")) {
            AmpReportGenerator arg = (AmpReportGenerator) this.getGenerator();
            if (arg.getMtefExtractOnlyDonorData()) {
                String srcRole = ret.getMetaValueString(ArConstants.SOURCE_ROLE_CODE);
                if ((srcRole != null) && (!srcRole.equals(Constants.ROLE_CODE_DONOR)))
                    return null;
            }
                
            MetaInfo minfo = this.getCachedMetaInfo(ArConstants.IS_AN_MTEF_FUNDING, "yes");
            ret.getMetaData().add(minfo);
            
            fetchDirectedDisbursementMeta(rs, ret, Constants.MTEFPROJECTION);
            fillDirectedDisbursementTypes(ret);
        }
        return ret;
    }

    protected Cell getCellFromCell(Cell src) {
        CategAmountCell categ = (CategAmountCell) src;
        ComputedAmountCell cell = new ComputedAmountCell();
        cell.setValuesFromCell((CategAmountCell) src);
        return cell;
    }

    public Cell newCellInstance() {
        return new ComputedAmountCell();

    }

    protected Column generateCellColumn() {
        CellColumn dest = null;
        Column sourceCol = sourceGroup.getColumn(sourceName);
        dest = newColumnInstance(sourceCol.getItems().size());
        Iterator i = sourceCol.iterator();
        while (i.hasNext()) {
            Cell element = (Cell) i.next();
            Cell destCell = getCellFromCell(element);
            if (destCell != null)
                dest.addCell(destCell);
        }

        return dest;
    }
}
