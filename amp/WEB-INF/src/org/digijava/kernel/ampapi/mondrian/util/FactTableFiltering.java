package org.digijava.kernel.ampapi.mondrian.util;

import java.util.*;
import java.util.Map.Entry;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.reports.mondrian.ActivityFilter;
import org.dgfoundation.amp.reports.mondrian.FiltersGroup;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;

/**
 * class used to add fact-table-filtering subquery(ies) to be used by Mondrian
 * generally one wouldn't use this for activity filtering (e.g. statements on entity_id)
 * @author Constantin Dolghier
 *
 */
public class FactTableFiltering {
	protected final MondrianReportFilters mrf;
	
	public FactTableFiltering(MondrianReportFilters mrf) {
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
			
			// process regular columns
			for(Entry<String, List<FilterRule>> sqlFilterRule:mrf.getSqlFilterRules().entrySet()) {
				String mainColumnName = sqlFilterRule.getKey();
				String fragment = buildQuerySubfragment(mainColumnName, sqlFilterRule.getValue());
				subquery.append(fragment);
			}

			ActivityFilter flt = new ActivityFilter("date_code");
			// process the funding-date filter(s)
			for(Entry<ReportElement, List<FilterRule>> filterElement:mrf.getFilterRules().entrySet())
				if (filterElement.getKey().type.equals(ReportElement.ElementType.DATE)) {
					String dateQuery = flt.buildQuery(filterElement.getValue());
					if (dateQuery != null && !dateQuery.isEmpty())
						subquery.append(dateQuery);
				}
		}
		String ret = subquery.toString().trim();
		long delta = System.currentTimeMillis() - start;
		if (delta > 20) {
			System.out.println("generating sql filter query took: " + delta + " millies");
			if (ret != null && !ret.isEmpty())
				System.out.println("\tthe filter query fragment is: " + ret);
		}
		return ret;
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
		
			add(ColumnConstants.PRIMARY_PROGRAM, new ProgramIdsExpander("primary_program_id"));
			add(ColumnConstants.SECONDARY_PROGRAM, new ProgramIdsExpander("secondary_program_id"));
			add(ColumnConstants.TERTIARY_PROGRAM, new ProgramIdsExpander("tertiary_program_id"));
			add(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, new ProgramIdsExpander("national_objectives_program_id"));
			
			add(FiltersGroup.LOCATION_FILTER, new LocationIdsExpander("location_id"));
			
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
			
			add(ColumnConstants.ACTIVITY_ID, new IdentityExpander("entity_id"));
			
			// non-direct-fact-table-filtering entries
			add(ColumnConstants.TEAM_ID, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE amp_team_id IN (QQQ)"));
			add(ColumnConstants.TEAM, get(ColumnConstants.TEAM_ID));
			add(ColumnConstants.ACTIVITY_APPROVED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE approvedby IN (QQQ)"));
			add(ColumnConstants.ACTIVITY_CREATED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE activity_creator IN (QQQ)"));
			add(ColumnConstants.ACTIVITY_UPDATED_BY, new CustomQueryIdExpander("entity_id", "SELECT amp_activity_id FROM amp_activity_version WHERE modified_by IN (QQQ)"));
		}
		
		void add(String mainColumn, IdsExpander expander) {
			if (this.containsKey(mainColumn))
				throw new RuntimeException("multiple entries for main column group " + mainColumn);
//			if (this.containsValue(mftColumn))
//				throw new RuntimeException("multiple entries map to mft column " + mftColumn);
			if (!MondrianTablesRepository.FACT_TABLE.columns.containsKey(expander.factTableColumn)) 
				throw new RuntimeException("column " + expander.factTableColumn + " does not exist in the MFT!");
			put(mainColumn, expander);
		}
		});
}
