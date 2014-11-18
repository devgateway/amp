package org.digijava.kernel.ampapi.mondrian.util;

import java.util.*;
import java.util.Map.Entry;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.ReportElement;
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
			
			// process the funding-date filter(s)
			for(Entry<ReportElement, List<FilterRule>> filterElement:mrf.getFilterRules().entrySet())
				if (filterElement.getKey().type.equals(ReportElement.ElementType.DATE)) {
					String dateQuery = buildDateQuery(filterElement.getValue());
					if (dateQuery != null && !dateQuery.isEmpty())
						subquery.append(dateQuery);
				}
		}
		String ret = subquery.toString().trim();
		if (ret != null && !ret.isEmpty())
			System.err.println("filter query fragment: " + ret);
		long end = System.currentTimeMillis() - start;
		System.err.println("generating sql filter query took: " + end + " millies");
		return ret;
	}
	
	protected String buildDateQuery(List<FilterRule> dateFilterElements) {
		List<String> statements = new ArrayList<>();
		String COLUMN_NAME = "date_code";
		
		for(FilterRule rule:dateFilterElements) {
			String statement = "";
			switch(rule.filterType) {
			
				case RANGE:
					if (rule.min != null)
						statement = COLUMN_NAME.concat(rule.minInclusive ? " >= " : " > ").concat(rule.min);
					if (rule.min != null && rule.max != null)
						statement += " AND ";
					if (rule.max != null)
						statement = statement.concat(COLUMN_NAME).concat(rule.maxInclusive ? " <= " : " < ").concat(rule.max);
					break;
				
				case SINGLE_VALUE:
					statement = COLUMN_NAME + " = " + rule.value;
					break;
				
				case VALUES:
					if (rule.values != null && rule.values.size() > 0) {
						statement = COLUMN_NAME.concat(" IN (") + Util.toCSStringForIN(AlgoUtils.collectLongs(rule.values));
					}
			
				default:
					throw new RuntimeException("unimplemented type of sql filter type: " + rule.filterType);
			}
			if (statement != null && !statement.isEmpty())
				statements.add(statement);
		}
		return mergeStatements(statements);
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
		
		List<FilterRule> rules = mergeRules(initRules);
		List<String> statements = new ArrayList<>();
		for(FilterRule rule:rules) {
			
			String statement = buildRuleStatement(rule, expander);
			if (statement != null && (!statement.isEmpty()))
				statements.add(statement);
		}
		return mergeStatements(statements);
	}
	
	/**
	 * returns:
	 * 		"", if statements.empty
	 * 		AND (statements[0]), if statements.len = 1
	 * 		AND ((statements[0]) OR (statements[1]) OR (statements[2])), if statements.len > 1
	 * @param statements
	 * @return
	 */
	public static String mergeStatements(List<String> statements) {
		if (statements.isEmpty())
			return "";
		if (statements.size() == 1)
			return String.format(" AND (%s)", statements.get(0));
					
		StringBuilder ret = new StringBuilder(" AND (");
		
		for(int i = 0; i < statements.size(); i++) {
			if (i > 0) {
				ret.append(" OR ");
			}
			ret.append("(").append(statements.get(i)).append(")");
		}
		ret.append(" )");
		return ret.toString();
	}
	
	public static List<FilterRule> mergeRules(List<FilterRule> initRules) {
		
		if (initRules == null || initRules.isEmpty() || initRules.size() == 1)
			return initRules;
		
		List<FilterRule> res = new ArrayList<>();
		Set<String> mergedValues = new HashSet<>();
		for(FilterRule rule:initRules) {
			switch(rule.filterType) {
			case RANGE:
				res.add(rule);
				break;
				
			case SINGLE_VALUE:
				mergedValues.add(rule.value);
				break;
				
			case VALUES:
				mergedValues.addAll(rule.values);
				break;
			}
		}
		if (!mergedValues.isEmpty())
			res.add(new FilterRule(new ArrayList<String>(mergedValues), true));
		return res;
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
		
		if (!ids.isEmpty()) {
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
			
			add(ColumnConstants.DONOR_AGENCY, new OrgIdsExpander("donor_id"));
			add(ColumnConstants.DONOR_GROUP, new OrgGrpIdsExpander("donor_id"));
			add(ColumnConstants.DONOR_TYPE, new OrgTypeIdsExpander("donor_id"));
			
			add(ColumnConstants.EXECUTING_AGENCY, new OrgIdsExpander("ea_org_id"));
			add(ColumnConstants.EXECUTING_AGENCY_GROUPS, new OrgGrpIdsExpander("ea_org_id"));
			add(ColumnConstants.EXECUTING_AGENCY_TYPE, new OrgTypeIdsExpander("ea_org_id"));
			
			add(ColumnConstants.BENEFICIARY_AGENCY, new OrgIdsExpander("ba_org_id"));
			add(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, new OrgGrpIdsExpander("ba_org_id"));
			//add(ColumnConstants.BENEFICIARY_AGENCY_TYPE, new OrgTypeIdsExpander("ba_org_id"));
			
			add(ColumnConstants.IMPLEMENTING_AGENCY, new OrgIdsExpander("ia_org_id"));
			add(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, new OrgGrpIdsExpander("ia_org_id"));
			add(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, new OrgTypeIdsExpander("ia_org_id"));
			
			add(ColumnConstants.RESPONSIBLE_ORGANIZATION, new OrgIdsExpander("ro_org_id"));
			add(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, new OrgGrpIdsExpander("ro_org_id"));
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
