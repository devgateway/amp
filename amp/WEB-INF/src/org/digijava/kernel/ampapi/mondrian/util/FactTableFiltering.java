package org.digijava.kernel.ampapi.mondrian.util;

import java.util.*;
import java.util.Map.Entry;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
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
		StringBuilder subquery = new StringBuilder();
		if (mrf != null) {
			for(Entry<String, List<FilterRule>> sqlFilterRule:mrf.getSqlFilterRules().entrySet()) {
				String mainColumnName = sqlFilterRule.getKey();
				String fragment = buildQuerySubfragment(mainColumnName, sqlFilterRule.getValue());
				subquery.append(fragment);
			}
		}
		return subquery.toString().trim();
	}
	
	protected String buildQuerySubfragment(String mainColumnName, List<FilterRule> rules) {
		IdsExpander expander = MAIN_COLUMN_EXPANDERS.get(mainColumnName);
		if (expander == null)
			throw new RuntimeException("the following SQL mainColumn filter not implemented: " + mainColumnName);
		StringBuilder ret = new StringBuilder();
		for(FilterRule rule:rules) {
			if (!rule.isIdFilter)
				throw new RuntimeException(String.format("filtering %s by values not implemented", mainColumnName));
			ret.append(buildRuleStatement(rule, expander));
		}
		return ret.toString();
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
		
		if (rule.values != null && rule.values.size() > 0) {
			StringBuilder result = new StringBuilder(" AND (");
			result.append(expander.factTableColumn).append(rule.valuesInclusive ? " IN " : " NOT IN ").append(" (").append(Util.toCSStringForIN(ids)).append(")");
			result.append(")");
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
