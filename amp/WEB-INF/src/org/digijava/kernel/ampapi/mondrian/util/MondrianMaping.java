/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;

/**
 * Provides the support for mapping from AMP tables & columns to Mondrian schema 
 * @author Nadejda Mandrescu
 *
 */
public class MondrianMaping {
	public static MDXElement toMDXElement(NamedTypedEntity entity) {
		MDXElement elem = entityMap.get(entity); 
		return elem==null ? null : elem.clone();
	}
	
	public static List<MDXAttribute> getDateElements(GroupingCriteria grouping) {
		List<MDXAttribute> dateTuple = new ArrayList<MDXAttribute>();
		switch(grouping) {
		case GROUPING_MONTHLY: dateTuple.add(new MDXLevel(MoConstants.DATES, MoConstants.H_MONTH, MoConstants.ATTR_MONTH));
		case GROUPING_QUARTERLY: dateTuple.add(0, new MDXLevel(MoConstants.DATES, MoConstants.H_QUARTER, MoConstants.ATTR_QUARTER));
		case GROUPING_YEARLY: dateTuple.add(0, new MDXLevel(MoConstants.DATES, MoConstants.H_YEAR, MoConstants.ATTR_YEAR));
		default:
			break;
		}
		return dateTuple;
	}
	
	public static MDXAttribute getElementByType(ElementType type) {
		if (ElementType.YEAR.equals(type))
			return new MDXLevel(MoConstants.DATES, MoConstants.H_YEAR, MoConstants.ATTR_YEAR);
		if (ElementType.QUARTER.equals(type))
			return new MDXLevel(MoConstants.DATES, MoConstants.H_QUARTER, MoConstants.ATTR_QUARTER);
		if (ElementType.MONTH.equals(type))
			return new MDXLevel(MoConstants.DATES, MoConstants.H_MONTH, MoConstants.ATTR_MONTH);
		return null;
	}
	
	public static String getDuplicateHierarchy(String hierarchy) {
		String dupHierarchy = duplicateHierarchy.get(hierarchy);
		if (dupHierarchy == null)
			dupHierarchy = hierarchy;
		return dupHierarchy;
	}
	
	/**
	 * Mappings between actual hierarchies and their duplicates to be used on Filter axis
	 */
	private static final Map<String, String> duplicateHierarchy = new HashMap<String, String>() {{
		put(null, "Duplicate");
		put(MoConstants.H_DATES, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_YEAR, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_QUARTER, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_MONTH, MoConstants.H_DATES_DUPLICATE);
	}};
	
	/**
	 * Mappings between AMP Data and Mondrian Schema 
	 */
	private static final Map<NamedTypedEntity,MDXElement> entityMap = new HashMap<NamedTypedEntity, MDXElement>() {{
			//Dimensions
			put(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.PROJECT_TITLE, MoConstants.ATTR_PROJECT_TITLE));
			put(new ReportColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.AMP_ID, MoConstants.ATTR_AMP_ID));
			//put(new ReportColumn(ColumnConstants.STATUS, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.STATUS, MoConstants.ATTR_STATUS_NAME));
			put(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.DONOR_GROUP, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.PRIMARY_SECTOR, MoConstants.ATTR_PRIMARY_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.SECONDARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.SECONDARY_SECTOR, MoConstants.ATTR_SECONDARY_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_SECTOR, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants.TERTIARY_SECTOR, MoConstants.ATTR_SECONDARY_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_0_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_1_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_2_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_3_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_4_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_5_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_6_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_7_NAME));
			put(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.NATIONAL_OBJECTIVES, MoConstants.ATTR_PROGRAM_LEVEL_8_NAME));
			put(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.PRIMARY_PROGRAMS, MoConstants.ATTR_PROGRAM_LEVEL_0_NAME));
			put(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.SECONDARY_PROGRAMS, MoConstants.ATTR_PROGRAM_LEVEL_1_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.TERTIARY_PROGRAMS, MoConstants.ATTR_PROGRAM_LEVEL_2_NAME));
			//put(new ReportColumn(ColumnConstants.FINANCING_INSTRUMENT, ReportEntityType.ENTITY_TYPE_ALL), new MDXAttribute(MoConstants., MoConstants.ATTR_));
			//put(new ReportColumn(ColumnConstants.MODALITIES, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants., MoConstants.ATTR_));
			//put(new ReportColumn(ColumnConstants.TYPE_OF_ASSISTANCE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants., MoConstants.ATTR_));
			//put(new ReportColumn(ColumnConstants.ON_OFF_TREASURY_BUDGET, ReportEntityType.ENTITY_TYPE_ACTIVITY, new MDXAttribute(MoConstants., MoConstants.ATTR_));
			
			
			//TODO: review/complete mappings based on Mondrian Schema
			
			//Measures - Entity type - All
			put(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.ACTUAL_COMMITMENTS));
			put(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.ACTUAL_DISBURSEMENTS));
			put(new ReportMeasure(MeasureConstants.ACTUAL_EXPENDITURES, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.ACTUAL_EXPENDITURES));
			put(new ReportMeasure(MeasureConstants.PLANNED_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.PLANNED_COMMITMENTS));
			put(new ReportMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.PLANNED_DISBURSEMENTS));
			put(new ReportMeasure(MeasureConstants.PLANNED_EXPENDITURES, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.PLANNED_EXPENDITURES));
			//put(new ReportMeasure(MeasureConstants.REAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.REAL_DISBURSEMENTS));
			//put(new ReportMeasure(MeasureConstants.UNCOMMITTED_BALANCE, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.UNCOMMITTED_BALANCE));
			//put(new ReportMeasure(MeasureConstants.TOTAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.TOTAL_COMMITMENTS));
			//put(new ReportMeasure(MeasureConstants.EXECUTION_RATE, ReportEntityType.ENTITY_TYPE_ALL), new MDXMeasure(MoConstants.EXECUTION_RATE));
			//TODO: review/complete mappings based on Mondrian Schema
			
			//Measures - Entity type - Pledges
			put(new ReportMeasure(MeasureConstants.PLEDGES_ACTUAL_COMMITMENTS, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXMeasure(MoConstants.PLEDGE_PLEDGES_COMMITMENTS));
			put(new ReportMeasure(MeasureConstants.PLEDGES_ACTUAL_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXMeasure(MoConstants.PLEDGE_PLEDGES_DISBURSEMENTS));
			put(new ReportMeasure(MeasureConstants.PLEDGES_COMMITMENT_GAP, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXMeasure(MoConstants.PLEDGE_PLEDGES_COMMITMENTS_GAP));
			//TODO: review/complete mappings based on Mondrian Schema
		}
	};
}
