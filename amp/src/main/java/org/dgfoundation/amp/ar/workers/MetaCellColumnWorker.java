package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public abstract class MetaCellColumnWorker extends ColumnWorker {

    protected HashMap<MetaInfo, MetaInfo> metaInfoCache = new HashMap<MetaInfo, MetaInfo>();

    protected MetaInfo<?> getCachedMetaInfo(String category, Object value) 
    {       
        MetaInfo mi = new MetaInfo(category, value);
        MetaInfo cachedMi = metaInfoCache.get(mi);
        if (cachedMi == null) // no cached canonical instance, put it into database
        {
            cachedMi = mi;
            metaInfoCache.put(cachedMi, cachedMi);
        }
        return cachedMi;
    }

    protected MetaCellColumnWorker(String condition, String viewName, String columnName, ReportGenerator generator) 
    {
        super(condition, viewName, columnName,generator);
        //this.metaInfoCache = new HashMap<String, Map<Comparable, MetaInfo>>();    
    }

    protected MetaCellColumnWorker(String destName, GroupColumn source, ReportGenerator generator) 
    {
        super(destName, source, generator);
    }
    
    protected String retrieveValueFromRS ( ResultSet rs, String columnName ) throws SQLException {
        return rs.getString(columnName);
    }
    
    protected void addMetaIfExists(ResultSet rs, CategAmountCell acc, String columnName, String metaKeyName, String defaultValue, boolean retrieveDirectly) throws SQLException
    {
        addMetaIfExists(rs, acc, columnName, metaKeyName, defaultValue, retrieveDirectly, false);
    }
    
    protected void addMetaIfExists(ResultSet rs, CategAmountCell acc, String columnName, String metaKeyName, String defaultValue, boolean retrieveDirectly, boolean translate) throws SQLException {
        if (columnsMetaData.containsKey(columnName)) {
            
            String fundingStatus = retrieveDirectly ? rs.getString(columnsMetaData.get(columnName) ) :
                                                    retrieveValueFromRS(rs, columnsMetaData.get(columnName));
            
            if (fundingStatus == null && defaultValue != null)
                fundingStatus = defaultValue;
            
            if (fundingStatus != null && translate)
                fundingStatus = TranslatorWorker.translateText(fundingStatus);
            
            if (fundingStatus != null) {
                MetaInfo termsAssistMeta = this.getCachedMetaInfo(metaKeyName, fundingStatus);
                acc.getMetaData().add(termsAssistMeta);
            }
                
        }   
    }
    
    protected void fillDirectedDisbursementTypes(CategAmountCell acc) {
        String recipientRoleTypeCode = acc.getMetaValueString(ArConstants.RECIPIENT_ROLE_CODE);
        String sourceRoleTypeCode = acc.getMetaValueString(ArConstants.SOURCE_ROLE_CODE);
        if (recipientRoleTypeCode != null && sourceRoleTypeCode != null)
        {
            // we have a directed transaction!
            String recognizedTransactionType = null;
            if (sourceRoleTypeCode.equals(Constants.FUNDING_AGENCY) && recipientRoleTypeCode.equals(Constants.EXECUTING_AGENCY))
                recognizedTransactionType = ArConstants.TRANSACTION_DN_EXEC;
            
            if (sourceRoleTypeCode.equals(Constants.EXECUTING_AGENCY) && recipientRoleTypeCode.equals(Constants.IMPLEMENTING_AGENCY))
                recognizedTransactionType = ArConstants.TRANSACTION_EXEC_IMPL;

            if (sourceRoleTypeCode.equals(Constants.IMPLEMENTING_AGENCY) && recipientRoleTypeCode.equals(Constants.BENEFICIARY_AGENCY))
                recognizedTransactionType = ArConstants.TRANSACTION_IMPL_BENF;
            
            if (recognizedTransactionType == null)
                recognizedTransactionType = ArConstants.userFriendlyNameOfRole(sourceRoleTypeCode) + "-" + ArConstants.userFriendlyNameOfRole(recipientRoleTypeCode);
            
            String directedTransactionType = ArConstants.TRANSACTION_TYPE_TO_DIRECTED_TRANSACTION_VALUE.get(acc.getMetaValueString(ArConstants.TRANSACTION_TYPE));
            if ((directedTransactionType == null) && (acc.getMetaValueString(ArConstants.IS_AN_MTEF_FUNDING) != null))
                directedTransactionType = ArConstants.TRANSACTION_REAL_MTEF_TYPE;
            
            if (recognizedTransactionType != null && directedTransactionType != null) {
                acc.getMetaData().add(this.getCachedMetaInfo(directedTransactionType, recognizedTransactionType));
            }
        }
    }
    
    protected void fetchDirectedDisbursementMeta(ResultSet rs, CategAmountCell acc, int tr_type) throws SQLException {
        boolean isDirectedMetadataRelevant = (tr_type == Constants.DISBURSEMENT || tr_type == Constants.COMMITMENT || tr_type == Constants.MTEFPROJECTION);
        if (isDirectedMetadataRelevant)
        {
            /**
             * this way we cut off SSC directed metadata (Ben's specification says to ignore it for SSC, and all SSC are commitments)
             * also we only have directed disbursements IRL, it is / should be ignored for non-disbursements
             */
            addMetaIfExists(rs, acc, "recipient_name", ArConstants.RECIPIENT_NAME, null, false);
            addMetaIfExists(rs, acc, "recipient_role_name", ArConstants.RECIPIENT_ROLE_NAME, null, false);
            addMetaIfExists(rs, acc, "recipient_role_code", ArConstants.RECIPIENT_ROLE_CODE, null, false);
            
            addMetaIfExists(rs, acc, "source_role_code", ArConstants.SOURCE_ROLE_CODE, null, false);
            addMetaIfExists(rs, acc, "source_role_name", ArConstants.SOURCE_ROLE_NAME, null, false);
        }
    }
}
