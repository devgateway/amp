package org.dgfoundation.amp.codegenerators;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used in funding types generator. 
 * (shouldn't be used anywhere else; there's @see HardcodedFundingNames for that!)
 * 
 * Let 'funding category' be the name of some column used in v_ni_donor_funding.
 * This class is a container for mapping 
 * funding_cat_name -> {cat_id -> cat_name}
 * and
 * funding_cat_name -> {cat_name -> cat_id}
 * @author acartaleanu
 *
 */
public class FundingIdsMapper {

    String _RAW_ACV_QUERY = "SELECT acv.id, acv.category_value FROM amp_category_value acv JOIN amp_category_class acc ON acv.amp_category_class_id = acc.id" + 
            " WHERE (acc.category_name = '%s')";    
    String PLEDGE_QUERY = "SELECT pledge_id, title FROM v_pledges_titles";
    String TRANSACTION_TYPE_QUERY = "select * from (values (0, 'commitment'), (1, 'disbursement'), (2, 'expenditure'), (3, 'mtef projection'), "
            + "(4, 'disbursement order'), (5, 'pledges commitment'), (6, 'pledges disbursement'), (7, 'pledge'), (8, 'release of funds'), "
            + "(9, 'estimated donor disbursement'), (15, 'annual proposed project cost') ) as t(id, type)";
    String AGREEMENT_QUERY = "SELECT id, title FROM amp_agreement";
    String RECIPIENT_ORG_QUERY = "SELECT org_id, org_name FROM ni_all_orgs_dimension where org_id > 0";
    String RECIPIENT_ROLE_QUERY = "SELECT amp_role_id, name FROM amp_role";
    String SOURCE_ROLE_QUERY = "SELECT amp_role_id, name FROM amp_role";
    String ADJUSTMENT_TYPE_QUERY = String.format(_RAW_ACV_QUERY, CategoryConstants.ADJUSTMENT_TYPE_NAME);
    String DONOR_ORG_QUERY = RECIPIENT_ORG_QUERY;
    String FUNDING_STATUS_QUERY = String.format(_RAW_ACV_QUERY, CategoryConstants.FUNDING_STATUS_NAME);
    String MODE_OF_PAYMENT_QUERY = String.format(_RAW_ACV_QUERY, CategoryConstants.MODE_OF_PAYMENT_NAME);
    /* it's "Type of Assistence" in constants, but too scared to change it, since it might break something
     * it's "Type of Assistance" in the DB, though*/
    String TERMS_ASSIST_QUERY = String.format(_RAW_ACV_QUERY, "Type of Assistance"); 
    
    String FINANCING_INSTRUMENT_QUERY = String.format(_RAW_ACV_QUERY, CategoryConstants.FINANCING_INSTRUMENT_NAME);
    
    private Map<String, Map<String, Long>> allParams = null;
    private Map<String, Map<Long, String>> allIds = null;   
    private Map<String, LevelColumn> optionalIdsMap = null;
    
    private static Map<String, Long> reverseMap(Map<Long, String> map) {
        Map<String, Long> reverted = new HashMap<String, Long>();
        for (Map.Entry<Long, String> entry : map.entrySet()) {
            reverted.put(entry.getValue(), entry.getKey());
        }
        if (reverted.size() != map.size())
            throw new RuntimeException("Map turns out to be non-bijective!");
        return reverted;
    }
    private void populateMap(String paramName, String query) {
        Map<Long, String> map = (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
        Map<String, Long> reverseMap = Collections.unmodifiableMap(reverseMap(map));
        allParams.put(paramName, reverseMap);
        allIds.put(paramName, Collections.unmodifiableMap(map));
    }

    public Map<String, Map<String, Long>> getAllParams() {
        if (allParams == null)
            populateAllParams();
        return allParams;
    }
    
    public Map<String, Map<Long, String>> getAllIds() {
        if (allIds == null)
            populateAllParams();
        return allIds;
    }
    
    private void populateAllParams() {
        allParams = new HashMap<>();
        allIds = new HashMap<>();
        populateMap(TestModelConstants.PLEDGE_ID, PLEDGE_QUERY);
        populateMap(TestModelConstants.TRANSACTION_TYPE, TRANSACTION_TYPE_QUERY);
        populateMap(TestModelConstants.AGREEMENT_ID, AGREEMENT_QUERY);
        populateMap(TestModelConstants.RECIPIENT_ORG, RECIPIENT_ORG_QUERY);
        populateMap(TestModelConstants.RECIPIENT_ROLE, RECIPIENT_ROLE_QUERY);
        populateMap(TestModelConstants.SOURCE_ROLE, SOURCE_ROLE_QUERY);
        populateMap(TestModelConstants.ADJUSTMENT_TYPE, ADJUSTMENT_TYPE_QUERY);
        populateMap(TestModelConstants.DONOR_ORG_ID, DONOR_ORG_QUERY);
        populateMap(TestModelConstants.FUNDING_STATUS_ID, FUNDING_STATUS_QUERY);
        populateMap(TestModelConstants.MODE_OF_PAYMENT_ID, MODE_OF_PAYMENT_QUERY);
        populateMap(TestModelConstants.TERMS_ASSIST_ID, TERMS_ASSIST_QUERY);
        populateMap(TestModelConstants.FINANCING_INSTRUMENT_ID, FINANCING_INSTRUMENT_QUERY);
    }
    
    public Map<String, LevelColumn> getOptionalIdsMap() {
        if (optionalIdsMap == null)
            optionalIdsMap = buildOptionalDimensionCols(HardcodedReportsTestSchema.getInstance());
        return optionalIdsMap;
    }
    
    private Map<String, LevelColumn> buildOptionalDimensionCols(HardcodedReportsTestSchema schema) {
        Map<String, NiReportColumn<?>> cols = schema.getColumns();
        Map<String, LevelColumn> res = new HashMap<>();
        for (Map.Entry<String, String> entry : getCoosFundingViewFilter().entrySet()) {
            String colName = entry.getKey();
            String viewColName = entry.getValue();
            res.put(viewColName, cols.get(colName).levelColumn.get());
        }
        return res;
    }
    
    public static Map<String, String> getCoosFundingViewFilter() {
        Map<String, String> res = new HashMap<>();
        res.put(ColumnConstants.TYPE_OF_ASSISTANCE, "terms_assist_id");
        res.put(ColumnConstants.FINANCING_INSTRUMENT, "financing_instrument_id");
        res.put(ColumnConstants.DONOR_AGENCY, "donor_org_id");
        res.put(ColumnConstants.MODE_OF_PAYMENT, "mode_of_payment_id");
        res.put(ColumnConstants.FUNDING_STATUS, "funding_status_id");
        return res;
    }
}
