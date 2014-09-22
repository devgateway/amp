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
public class MondrianMapping {
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
		switch(type) {
		case YEAR : return new MDXLevel(MoConstants.DATES, MoConstants.H_YEAR, MoConstants.ATTR_YEAR);
		case QUARTER: return new MDXLevel(MoConstants.DATES, MoConstants.H_QUARTER, MoConstants.ATTR_QUARTER);
		case MONTH: return new MDXLevel(MoConstants.DATES, MoConstants.H_MONTH, MoConstants.ATTR_MONTH);
		case DATE: return new MDXLevel(MoConstants.DATES, MoConstants.H_DATES, MoConstants.ATTR_DATE);
		default: return null;
		}
	}
	
	/* candidate for removal
	public static String getPropertyName(ReportElement elem) {
		switch(elem.type) {
		case ENTITY: return getPropertyName(elem.entity);
		case DATE: return MoConstants.P_DATE;
		default: return null;
		}
	}
		
	private static String getPropertyName(NamedTypedEntity entity) {
		return idProperty.get(entity);
	}
	*/
	
	public static String getAll(MDXAttribute mdxAttr) {
		if (mdxAttr.getDimension().equals(MoConstants.DATES)) {
			String hierarchy = mdxAttr instanceof MDXLevel ? ((MDXLevel)mdxAttr).getHierarchy() : MoConstants.H_DATES; 
			return (new MDXLevel(MoConstants.DATES, hierarchy , MoConstants.ATTR_ALL_DATES)).getFullName();
		}
		return null;
	}
	
	/* candidate for removal
	public static String getDuplicateHierarchy(String hierarchy) {
		String dupHierarchy = duplicateHierarchy.get(hierarchy);
		if (dupHierarchy == null)
			dupHierarchy = hierarchy;
		return dupHierarchy;
	}
	*/
	
	/**
	 * Mappings between actual hierarchies and their duplicates to be used on Filter axis
	 */
	/* candidate for removal
	private static final Map<String, String> duplicateHierarchy = new HashMap<String, String>() {{
		put(null, "Duplicate");
		put(MoConstants.H_DATES, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_YEAR, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_QUARTER, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_MONTH, MoConstants.H_DATES_DUPLICATE);
		put(MoConstants.H_ORG_TYPE_NAME, MoConstants.H_ORG_DUPLICATE);
		put(MoConstants.H_ORG_GROUP_NAME, MoConstants.H_ORG_DUPLICATE);
		put(MoConstants.H_ORG_NAME, MoConstants.H_ORG_DUPLICATE);
		put(MoConstants.H_LOCATIONS, MoConstants.H_LOCATIONS_DUPLICATE);
	}};
	*/
	
	/**
	 * Mappings between AMP Data and Mondrian Schema 
	 */
	private static final Map<NamedTypedEntity,MDXElement> entityMap = new HashMap<NamedTypedEntity, MDXElement>() {{
			//Dimensions
			put(new ReportColumn(ColumnConstants.PROJECT_TITLE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_PROJECT_TITLE, MoConstants.ATTR_PROJECT_TITLE));
			put(new ReportColumn(ColumnConstants.AMP_ID, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_AMP_ID, MoConstants.ATTR_AMP_ID));
			//put(new ReportColumn(ColumnConstants.??, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_APPROVAL_STATUS, MoConstants.ATTR_APPROVAL_STATUS));
			put(new ReportColumn(ColumnConstants.TEAM, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_TEAM, MoConstants.ATTR_TEAM));
			put(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_BY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_ACTIVITY_CREATED_BY, MoConstants.ATTR_ACTIVITY_CREATED_BY));
			put(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_BY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_ACTIVITY_UPDATED_BY, MoConstants.ATTR_ACTIVITY_UPDATED_BY));
			//put(new ReportColumn(ColumnConstants.??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_ACTIVITY_APPROVED_BY, MoConstants.ATTR_ACTIVITY_APPROVED_BY));
			put(new ReportColumn(ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_GOVERNMENT_APPROVAL_PROCEDURES, MoConstants.ATTR_GOVERNMENT_APPROVAL_PROCEDURES));
			put(new ReportColumn(ColumnConstants.JOINT_CRITERIA, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_JOINT_CRITERIA, MoConstants.ATTR_JOINT_CRITERIA));
			//put(new ReportColumn(ColumnConstants.??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_INDIRECT_ON_BUDGET, MoConstants.ATTR_INDIRECT_ON_BUDGET));
			put(new ReportColumn(ColumnConstants.STATUS, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_STATUS, MoConstants.H_ACTIVITY_STATUS, MoConstants.ATTR_ACTIVITY_STATUS));
			put(new ReportColumn(ColumnConstants.MODALITIES, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.MODALITIES, MoConstants.H_MODALITIES, MoConstants.ATTR_ACTIVITY_STATUS));
			put(new ReportColumn(ColumnConstants.TYPE_OF_COOPERATION, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TYPE_OF_COOPERATION, MoConstants.H_TYPE_OF_COOPERATION, MoConstants.ATTR_TYPE_OF_COOPERATION));
			put(new ReportColumn(ColumnConstants.TYPE_OF_IMPLEMENTATION, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TYPE_OF_IMPLEMENTATION, MoConstants.H_TYPE_OF_IMPLEMENTATION, MoConstants.ATTR_TYPE_OF_IMPLEMENTATION));
			put(new ReportColumn(ColumnConstants.PROCUREMENT_SYSTEM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.PROCUREMENT_SYSTEM, MoConstants.H_PROCUREMENT_SYSTEM, MoConstants.ATTR_PROCUREMENT_SYSTEM));
			put(new ReportColumn(ColumnConstants.FUNDING_STATUS, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.FUNDING_STATUS, MoConstants.H_FUNDING_STATUS, MoConstants.ATTR_FUNDING_STATUS));
			put(new ReportColumn(ColumnConstants.MODE_OF_PAYMENT, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.MODE_OF_PAYMENT, MoConstants.H_MODE_OF_PAYMENT, MoConstants.ATTR_MODE_OF_PAYMENT));
			put(new ReportColumn(ColumnConstants.FINANCING_INSTRUMENT, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.FINANCING_INSTRUMENT, MoConstants.H_FINANCING_INSTRUMENT, MoConstants.ATTR_FINANCING_INSTRUMENT));
			put(new ReportColumn(ColumnConstants.TYPE_OF_ASSISTANCE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TYPE_OF_ASSISTANCE, MoConstants.H_TYPE_OF_ASSISTANCE, MoConstants.ATTR_TYPE_OF_ASSISTANCE));
			put(new ReportColumn(ColumnConstants.PROJECT_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_DESCRIPTION, MoConstants.ATTR_PROJECT_DESCRIPTION));
			put(new ReportColumn(ColumnConstants.OBJECTIVE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_OBJECTIVE, MoConstants.ATTR_OBJECTIVE));
			put(new ReportColumn(ColumnConstants.RESULTS, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_RESULTS, MoConstants.ATTR_RESULTS));
			put(new ReportColumn(ColumnConstants.PURPOSE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PURPOSE, MoConstants.ATTR_PURPOSE));
			put(new ReportColumn(ColumnConstants.PROJECT_COMMENTS, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_COMMENTS, MoConstants.ATTR_PROJECT_COMMENTS));
			put(new ReportColumn(ColumnConstants.PROJECT_IMPACT, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_IMPACT, MoConstants.ATTR_PROJECT_IMPACT));
			put(new ReportColumn(ColumnConstants.EQUAL_OPPORTUNITY, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_EQUAL_OPPORTUNITY, MoConstants.ATTR_EQUAL_OPPORTUNITY));
			put(new ReportColumn(ColumnConstants.ENVIRONMENT, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_ENVIRONMENT, MoConstants.ATTR_ENVIRONMENT));
			put(new ReportColumn(ColumnConstants.MINORITIES, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_MINORITIES, MoConstants.ATTR_MINORITIES));
			put(new ReportColumn(ColumnConstants.PROGRAM_DESCRIPTION, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROGRAM_DESCRIPTION, MoConstants.ATTR_PROGRAM_DESCRIPTION));
			put(new ReportColumn(ColumnConstants.ORIGINAL_COMPLETION_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ORIG_COMPLETION_DATE, MoConstants.ATTR_ORIG_COMPLETION_DATE));
			put(new ReportColumn(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_FINAL_DATE_FOR_CONTRACTING, MoConstants.ATTR_FINAL_DATE_FOR_CONTRACTING));
			put(new ReportColumn(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_FINAL_DATE_FOR_DISBURSEMENTS, MoConstants.ATTR_FINAL_DATE_FOR_DISBURSEMENTS));
			put(new ReportColumn(ColumnConstants.PROPOSED_START_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_START_DATE, MoConstants.ATTR_PROPOSED_START_DATE));
			put(new ReportColumn(ColumnConstants.ACTUAL_START_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_START_DATE, MoConstants.ATTR_ACTUAL_START_DATE));
			put(new ReportColumn(ColumnConstants.PROPOSED_APPROVAL_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_APPROVAL_DATE, MoConstants.ATTR_PROPOSED_APPROVAL_DATE));
			put(new ReportColumn(ColumnConstants.ACTUAL_APPROVAL_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_APPROVAL_DATE, MoConstants.ATTR_ACTUAL_APPROVAL_DATE));
			put(new ReportColumn(ColumnConstants.ACTUAL_COMPLETION_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_COMPLETION_DATE, MoConstants.ATTR_ACTUAL_COMPLETION_DATE));
			put(new ReportColumn(ColumnConstants.PROPOSED_COMPLETION_DATE, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_COMPLETION_DATE, MoConstants.ATTR_PROPOSED_COMPLETION_DATE));
			put(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTIVITY_CREATED_ON, MoConstants.ATTR_ACTIVITY_CREATED_ON));
			put(new ReportColumn(ColumnConstants.ACTIVITY_UPDATED_ON, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTIVITY_UPDATED_ON, MoConstants.ATTR_ACTIVITY_UPDATED_ON));
			put(new ReportColumn(ColumnConstants.DONOR_TYPE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.DONOR_GROUP, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.DONOR_AGENCY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			put(new ReportColumn(ColumnConstants.EXECUTING_AGENCY_TYPE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.EXECUTING_AGENCY_GROUPS, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.EXECUTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			put(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			//put(new ReportColumn(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.BENEFICIARY_AGENCY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			//put(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			put(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			put(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			//put(new ReportColumn(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.??, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			//put(new ReportColumn(ColumnConstants.CONTRACTING_AGENCY_GROUPS, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.??, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			//put(new ReportColumn(ColumnConstants.CONTRACTING_AGENCY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.??, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			put(new ReportColumn(ColumnConstants.PRIMARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.SECONDARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.PLEDGES_SECTORS, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.PLEDGES_SECONDARY_SECTORS, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			put(new ReportColumn(ColumnConstants.PLEDGES_TERTIARY_SECTORS, ReportEntityType.ENTITY_TYPE_PLEDGE), new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
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
			put(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.SECONDARY_PROGRAMS, MoConstants.ATTR_PROGRAM_LEVEL_0_NAME));
			put(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM, ReportEntityType.ENTITY_TYPE_ACTIVITY), new MDXAttribute(MoConstants.TERTIARY_PROGRAMS, MoConstants.ATTR_PROGRAM_LEVEL_0_NAME));
			put(new ReportColumn(ColumnConstants.COUNTRY, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.LOCATION, MoConstants.H_LOCATIONS,  MoConstants.ATTR_COUNTRY_NAME));
			put(new ReportColumn(ColumnConstants.REGION, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.LOCATION, MoConstants.H_REGIONS,  MoConstants.ATTR_REGION_NAME));
			put(new ReportColumn(ColumnConstants.ZONE, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.LOCATION, MoConstants.H_LOCATIONS,  MoConstants.ATTR_ZONE_NAME));
			put(new ReportColumn(ColumnConstants.DISTRICT, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.LOCATION, MoConstants.H_LOCATIONS,  MoConstants.ATTR_DISTRICT_NAME));
			//put(new ReportColumn(ColumnConstants. location?, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.LOCATION, MoConstants.H_LOCATIONS,  MoConstants.ATTR_LOCATION_NAME));
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
