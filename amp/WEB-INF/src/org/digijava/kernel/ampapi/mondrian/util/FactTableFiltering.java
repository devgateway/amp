package org.digijava.kernel.ampapi.mondrian.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.reports.mondrian.ActivityFilter;
import org.digijava.module.aim.helper.Constants;

/**
 * class used to add fact-table-filtering subquery(ies) to be used by Mondrian
 * generally one wouldn't use this for activity filtering (e.g. statements on entity_id)
 * @author Constantin Dolghier
 *
 */
public class FactTableFiltering {
    
    protected static final Logger logger = Logger.getLogger(FactTableFiltering.class);
    
    public static final String DATE_FILTERS_TAG_START = "@@date-filters-start@@";
    public static final String DATE_FILTERS_TAG_END = "@@date-filters-end@@";
    public static final String DATE_FILTERS_PATTERN = DATE_FILTERS_TAG_START + ".*" + DATE_FILTERS_TAG_END;
    
    protected final AmpReportFilters mrf;
    
    public FactTableFiltering(AmpReportFilters mrf) {
        this.mrf = mrf;
    }
    
    /**
     * builds a query fragment on the fact table
     * @return an SQL fragment starting with AND, or alternatively an empty string
     */
    public String getQueryFragment() {
        long start = System.currentTimeMillis();
        StringBuilder subquery = new StringBuilder();
        if (mrf != null) {
            String expenditureClassFragment = buildAcvStatement(ColumnConstants.EXPENDITURE_CLASS, "expenditure_class");
            String appendedQuery = String.format(" AND ((transaction_type <> 2) OR ((transaction_type = 2) %s))", expenditureClassFragment); 
            subquery.append(appendedQuery);         
            // process regular columns
            
//          for(Entry<String, List<FilterRule>> sqlFilterRule:mrf.getSqlFilterRules().entrySet()) {
//              String mainColumnName = sqlFilterRule.getKey();
//              /*expenditure class is processed above, because it's a property of transactions and should affect
//               * only transactiontype=2 (expenditures), which is not a working mechanism on regular columns*/
//              if (mainColumnName.equals(ColumnConstants.EXPENDITURE_CLASS))
//                  continue;
//              String fragment = buildQuerySubfragment(mainColumnName, sqlFilterRule.getValue());
//              subquery.append(fragment);
//          }
            
            String dateFilteringFragment = buildDateFilteringFragment(ReportElement.ElementType.DATE, String.format("(transaction_type <> %d) AND (transaction_type <> %d)", Constants.MTEFPROJECTION, 200 + Constants.MTEFPROJECTION));
            String realMtefFilteringFragment = buildDateFilteringFragment(ReportElement.ElementType.REAL_MTEF_DATE, "transaction_type = " + (200 + Constants.MTEFPROJECTION));
            String mtefFilteringFragment = "transaction_type = " + Constants.MTEFPROJECTION;
            
            String noDateFilter = "transaction_type >= " + MoConstants.TRANSACTION_TYPE_GAP;
            
            String datesQuery = String.format("%sAND ((%s) OR (%s) OR (%s) OR (%s))%s", 
                    DATE_FILTERS_TAG_START,
                    dateFilteringFragment, mtefFilteringFragment, realMtefFilteringFragment, noDateFilter, 
                    DATE_FILTERS_TAG_END);
            subquery.append(datesQuery);
            
        }
        String ret = subquery.toString().trim();
        long delta = System.currentTimeMillis() - start;
        if (delta > 30) {
            logger.warn("generating sql filter query took: " + delta + " millies");
            if (ret != null && !ret.isEmpty())
                logger.info("\tthe filter query fragment is: " + ret);
        }
        return ret;
    }
    
    protected String buildAcvStatement(String ampColumnName, String factTableColumnName) {
        if (mrf == null || mrf.getFilterRules() == null)
            return "";
        FilterRule rule = mrf.getFilterRules().get(new ReportElement(new ReportColumn(ampColumnName)));
        if (rule == null)
            return "";
        return buildRuleStatement(rule, new IdentityExpander(factTableColumnName));
    }
    
    
    /**
     * process the funding-date filter(s), excluding MTEFs
     * @return
     */
    public String buildDateFilteringFragment(ReportElement.ElementType elementType, String transactionTypeFilteringQuery) {
        StringBuilder fragment = new StringBuilder();
        ActivityFilter flt = new ActivityFilter("date_code");
        if (mrf != null && mrf.getFilterRules() != null) {
            // tag date filters section to reuse "all filters without date filters" criteria in other queries 
            for(Entry<ReportElement, FilterRule> filterElement:mrf.getFilterRules().entrySet())
                if (filterElement.getKey().type.equals(elementType)) {
                    String dateQuery = flt.buildQuery(filterElement.getValue());
                    if (dateQuery != null && !dateQuery.isEmpty())
                        fragment.append(dateQuery);
                }
        }
        String res = String.format("%s %s", transactionTypeFilteringQuery, fragment.toString());
        return res;
    }
    
    /**
     * builds a subquery of the form AND (primary_sector_id IN (...))
     * @param mainColumnName
     * @param initRules
     * @return
     */
    protected String buildQuerySubfragment(String mainColumnName, List<FilterRule> initRules) {
        IdsExpander expander = MAIN_COLUMN_EXPANDERS.get(mainColumnName);
        if (expander == null)
            throw new RuntimeException("the following SQL mainColumn filter not implemented: " + mainColumnName);
        
        List<FilterRule> rules = FilterRule.mergeRules(initRules);
        List<String> statements = new ArrayList<>();
        for(FilterRule rule:rules) {
            
            String statement = buildRuleStatement(rule, expander);
            if (statement != null && (!statement.isEmpty()))
                statements.add(statement);
        }
        return AmpARFilter.mergeStatements(statements);
    }
    
    protected String buildRuleStatement(FilterRule rule, IdsExpander expander) {
        Set<Long> ids;
        switch(rule.filterType) {
            case RANGE:
                throw new RuntimeException("range filter for ids makes no sense");
                
            case SINGLE_VALUE:
                ids = expander.expandIds(Arrays.asList(Long.parseLong(rule.value)));
                break;
                
            case VALUES:
                if (rule.values == null || rule.values.isEmpty())
                    return "";
                ids = expander.expandIds(AlgoUtils.collectLongs(rule.values));
            break;
            
            default:
                throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
        }
        
        if (ids != null) {
            StringBuilder result = new StringBuilder();
            result.append(expander.factTableColumn).append(rule.valuesInclusive ? " IN " : " NOT IN ").append(" (").append(Util.toCSStringForIN(ids)).append(")");
            return result.toString();
        }
        return "";
    }
    
    /**
     * Map<mainColumnName, column_in_mondrian_fact_table>.
     * the key is one of the values in #FiltersGroup.FILTER_GROUP
     */
    public final static Map<String, IdsExpander> MAIN_COLUMN_EXPANDERS = Collections.unmodifiableMap(new HashMap<String, IdsExpander>() {
        {
            add(ColumnConstants.PRIMARY_SECTOR, new SectorIdsExpander("primary_sector_id"));
            add(ColumnConstants.SECONDARY_SECTOR, new SectorIdsExpander("secondary_sector_id"));
            add(ColumnConstants.TERTIARY_SECTOR, new SectorIdsExpander("tertiary_sector_id"));
        
            add(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, new ProgramIdsExpander("primary_program_id"));
            add(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1, new ProgramIdsExpander("secondary_program_id"));
            add(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1, new ProgramIdsExpander("tertiary_program_id"));
            add(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, 
                    new ProgramIdsExpander("national_objectives_program_id"));
            
            //add(FiltersGroup.LOCATION_FILTER, new LocationIdsExpander("location_id"));
            
            add(ColumnConstants.DONOR_AGENCY, new IdentityExpander("donor_id"));
            add(ColumnConstants.DONOR_GROUP, new OrgGrpIdsExpander("donor_id"));
            add(ColumnConstants.DONOR_TYPE, new OrgTypeIdsExpander("donor_id"));
            
            add(ColumnConstants.EXECUTING_AGENCY, new IdentityExpander("ea_org_id"));
            add(ColumnConstants.EXECUTING_AGENCY_GROUPS, new OrgGrpIdsExpander("ea_org_id"));
            add(ColumnConstants.EXECUTING_AGENCY_TYPE, new OrgTypeIdsExpander("ea_org_id"));
            
            add(ColumnConstants.BENEFICIARY_AGENCY, new IdentityExpander("ba_org_id"));
            add(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, new OrgGrpIdsExpander("ba_org_id"));
            //add(ColumnConstants.BENEFICIARY_AGENCY_TYPE, new OrgTypeIdsExpander("ba_org_id"));
            
            add(ColumnConstants.IMPLEMENTING_AGENCY, new IdentityExpander("ia_org_id"));
            add(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, new OrgGrpIdsExpander("ia_org_id"));
            add(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, new OrgTypeIdsExpander("ia_org_id"));
            
            add(ColumnConstants.RESPONSIBLE_ORGANIZATION, new IdentityExpander("ro_org_id"));
            add(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, new OrgGrpIdsExpander("ro_org_id"));
            
            add(ColumnConstants.CONTRACTING_AGENCY, new IdentityExpander("ca_org_id"));
            add(ColumnConstants.CONTRACTING_AGENCY_GROUPS, new OrgGrpIdsExpander("ca_org_id"));
            
            add(ColumnConstants.SECTOR_GROUP, new IdentityExpander("sg_org_id"));
            add(ColumnConstants.SECTOR_GROUP_GROUP, new OrgGrpIdsExpander("sg_org_id"));
            
            add(ColumnConstants.REGIONAL_GROUP, new IdentityExpander("rg_org_id"));
            add(ColumnConstants.REGIONAL_GROUP_GROUP, new OrgGrpIdsExpander("rg_org_id"));
            
            add(ColumnConstants.IMPLEMENTATION_LEVEL, new ActACVIdsExpander("entity_id"));
            add(ColumnConstants.IMPLEMENTATION_LOCATION, new ActACVIdsExpander("entity_id"));
            add(ColumnConstants.ON_OFF_TREASURY_BUDGET, new ActACVIdsExpander("entity_id"));
            add(ColumnConstants.AC_CHAPTER, new ActACVIdsExpander("entity_id"));
            
            //ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.TYPE_OF_COOPERATION, ColumnConstants.TYPE_OF_IMPLEMENTATION
            add(ColumnConstants.FINANCING_INSTRUMENT, new IdentityExpander("financing_instrument_id"));
            add(ColumnConstants.MODE_OF_PAYMENT, new IdentityExpander("mode_of_payment_id"));
            add(ColumnConstants.TYPE_OF_ASSISTANCE, new IdentityExpander("terms_of_assistance_id"));
            add(ColumnConstants.TYPE_OF_COOPERATION, new IdentityExpander("type_of_cooperation_id"));
            add(ColumnConstants.TYPE_OF_IMPLEMENTATION, new IdentityExpander("type_of_implementation_id"));
            add(ColumnConstants.FUNDING_STATUS, new IdentityExpander("funding_status_id"));
            add(ColumnConstants.PROCUREMENT_SYSTEM, new IdentityExpander("procurement_system_id"));
            add(ColumnConstants.PLEDGES_AID_MODALITY, new IdentityExpander("modality_id"));
            add(ColumnConstants.STATUS, new IdentityExpander("status_id"));         
            add(ColumnConstants.EXPENDITURE_CLASS, new IdentityExpander("expenditure_class"));
            
            add(ColumnConstants.ACTIVITY_ID, new IdentityExpander("entity_id"));
            add(ColumnConstants.DISASTER_RESPONSE_MARKER, new BooleanExpander("disaster_response"));
            
            // non-direct-fact-table-filtering entries
            add(ColumnConstants.TEAM, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE amp_team_id IN (QQQ)"));
            add(ColumnConstants.ACTIVITY_APPROVED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE approvedby IN (QQQ)"));
            add(ColumnConstants.ACTIVITY_CREATED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE activity_creator IN (QQQ)"));
            add(ColumnConstants.ACTIVITY_UPDATED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE modified_by IN (QQQ)"));
        }
        
        void add(String mainColumn, IdsExpander expander) {
            if (this.containsKey(mainColumn))
                throw new RuntimeException("multiple entries for main column group " + mainColumn);
//          if (this.containsValue(mftColumn))
//              throw new RuntimeException("multiple entries map to mft column " + mftColumn);
            if (!MondrianTablesRepository.FACT_TABLE.columns.containsKey(expander.factTableColumn)) 
                throw new RuntimeException("column " + expander.factTableColumn + " does not exist in the MFT!");
            put(mainColumn, expander);
        }
        });
}
