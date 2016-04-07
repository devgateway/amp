/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Provides the support for mapping from AMP tables & columns to Mondrian schema 
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianMapping {
	public static MDXElement toMDXElement(NamedTypedEntity entity) {
		MDXElement elem = entityMap.get(entity); 
		return elem==null ? null : elem.clone();
	}
	
	public static List<MDXAttribute> getDateElements(GroupingCriteria grouping) {
		List<MDXAttribute> dateTuple = new ArrayList<MDXAttribute>();
		
		String dateDimension = MoConstants.DATES;
		String monthHierarchy = MoConstants.H_MONTH;
		String quarterHierarchy = MoConstants.H_QUARTER;
		String yearHierarchy = MoConstants.H_YEAR;
		boolean addQuarter = true;
		
		switch(grouping) {
		case GROUPING_MONTHLY: 
			dateTuple.add(new MDXLevel(dateDimension, monthHierarchy, MoConstants.ATTR_MONTH));
			addQuarter = false;
		case GROUPING_QUARTERLY:
			if (addQuarter)
				dateTuple.add(0, new MDXLevel(dateDimension, quarterHierarchy, MoConstants.ATTR_QUARTER));
		case GROUPING_YEARLY: 
			dateTuple.add(0, new MDXLevel(dateDimension, yearHierarchy, MoConstants.ATTR_YEAR));
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
	
	public static String getAll(MDXAttribute mdxAttr) {
		if (mdxAttr.getDimension().equals(MoConstants.DATES)) {
			String hierarchy = mdxAttr instanceof MDXLevel ? ((MDXLevel)mdxAttr).getHierarchy() : MoConstants.H_DATES; 
			return (new MDXLevel(MoConstants.DATES, hierarchy , MoConstants.ATTR_ALL_DATES)).getFullName();
		}
		return null;
	}
	
	/**
	 * Mappings between actual hierarchies and their duplicates to be used on Filter axis
	 */
	
	public static final Set<String> definedColumns = new HashSet<String>();
	public static final Set<String> definedMeasures = new HashSet<String>();
	public static final Map<String, String> dependency = new HashMap<String, String>();
	
	final static String[] idSuffixList = new String[] {"", " Id"};
	
	/**
	 * Mappings between AMP Data and Mondrian Schema 
	 */
	public static final Map<NamedTypedEntity,MDXElement> entityMap = new HashMap<NamedTypedEntity, MDXElement>() {
		
		void addColumnDefinition(String columnName, MDXLevel mdxLevel) {
			definedColumns.add(columnName);
			ReportColumn rc = new ReportColumn(columnName);
			if (this.containsKey(rc))
				throw new RuntimeException(String.format("column %s defined at least twice: once as %s, and then as %s", rc, this.get(rc), mdxLevel));
			put(rc, mdxLevel);
		}
		
		void addMeasureDefinition(String measureName) {
			definedMeasures.add(measureName);
			addMeasureDefinition(measureName, measureName);
		}
		
		void addMeasureDefinition(String measureName, String mondrianMeasureName) {
			ReportMeasure rm = new ReportMeasure(measureName);
			if (this.containsKey(rm))
				throw new RuntimeException(String.format("measure %s defined at least twice: once as %s, and then as %s", rm, this.get(rm).getName(), mondrianMeasureName));
			put(rm, new MDXMeasure(mondrianMeasureName));
		}
		
		
		{
			//Dimensions
			addColumnDefinition(ColumnConstants.PROJECT_TITLE, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_PROJECT_TITLE, MoConstants.ATTR_PROJECT_TITLE));
			addColumnDefinition(ColumnConstants.PLEDGES_TITLES, new MDXLevel(MoConstants.PLEDGE_TEXTS, MoConstants.H_PROJECT_TITLE, MoConstants.ATTR_PROJECT_TITLE));
			addColumnDefinition(ColumnConstants.RELATED_PROJECTS, new MDXLevel("Related Projects", "Related Projects", "Related Projects"));
			addColumnDefinition(ColumnConstants.RELATED_PLEDGES, new MDXLevel("Related Pledges", "Related Pledges", "Related Pledges"));
			
			addColumnDefinition(ColumnConstants.AMP_ID, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_AMP_ID, MoConstants.ATTR_AMP_ID));
			addColumnDefinition(ColumnConstants.CONSTANT, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, ColumnConstants.CONSTANT, ColumnConstants.CONSTANT));
			addColumnDefinition(ColumnConstants.APPROVAL_STATUS, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_APPROVAL_STATUS, MoConstants.ATTR_APPROVAL_STATUS));
			addColumnDefinition(ColumnConstants.TEAM, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_TEAM, MoConstants.ATTR_TEAM));
			addColumnDefinition(ColumnConstants.TEAM_ID, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_TEAM, MoConstants.ATTR_TEAM_ID));
			addColumnDefinition(ColumnConstants.ACTIVITY_CREATED_BY, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.ATTR_ACTIVITY_CREATED_BY, MoConstants.ATTR_ACTIVITY_CREATED_BY));
			addColumnDefinition(ColumnConstants.ACTIVITY_UPDATED_BY, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.ATTR_ACTIVITY_UPDATED_BY, MoConstants.ATTR_ACTIVITY_UPDATED_BY));
			addColumnDefinition(ColumnConstants.ACTIVITY_APPROVED_BY, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.ATTR_ACTIVITY_APPROVED_BY, MoConstants.ATTR_ACTIVITY_APPROVED_BY));
			addColumnDefinition(ColumnConstants.IMPLEMENTATION_LEVEL, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_IMPLEMENTATION_LEVEL, MoConstants.ATTR_IMPLEMENTATION_LEVEL));
			addColumnDefinition(ColumnConstants.IMPLEMENTATION_LOCATION, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_IMPLEMENTATION_LOCATION, MoConstants.ATTR_IMPLEMENTATION_LOCATION));
			addColumnDefinition(ColumnConstants.ON_OFF_TREASURY_BUDGET, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_ON_OFF_TREASURY_BUDGET, MoConstants.ATTR_ON_OFF_TREASURY_BUDGET));
			addColumnDefinition(ColumnConstants.HUMANITARIAN_AID, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_HUMANITARIAN_AID, MoConstants.ATTR_HUMANITARIAN_AID));
			addColumnDefinition(ColumnConstants.DISASTER_RESPONSE_MARKER, new MDXLevel(MoConstants.DISASTER_RESPONSE_MARKER, MoConstants.H_HARDCODED_TEXT, MoConstants.H_HARDCODED_TEXT));

			addColumnDefinition(ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_GOVERNMENT_APPROVAL_PROCEDURES, MoConstants.ATTR_GOVERNMENT_APPROVAL_PROCEDURES));
			addColumnDefinition(ColumnConstants.JOINT_CRITERIA, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.H_JOINT_CRITERIA, MoConstants.ATTR_JOINT_CRITERIA));
			addColumnDefinition(ColumnConstants.INDIRECT_ON_BUDGET, new MDXLevel(MoConstants.ACTIVITY_TEXTS, MoConstants.ATTR_INDIRECT_ON_BUDGET, MoConstants.ATTR_INDIRECT_ON_BUDGET));
			addColumnDefinition(ColumnConstants.STATUS, new MDXLevel(MoConstants.ACTIVITY_STATUS, MoConstants.H_ACTIVITY_STATUS, MoConstants.ATTR_ACTIVITY_STATUS));
			addColumnDefinition(ColumnConstants.PLEDGES_AID_MODALITY, new MDXLevel(MoConstants.PLEDGE_MODALITIES, MoConstants.H_CATEGORY_NAME, MoConstants.ATTR_ACTIVITY_STATUS));
			addColumnDefinition(ColumnConstants.MODALITIES, new MDXLevel(MoConstants.MODALITIES, MoConstants.H_CATEGORY_NAME, MoConstants.ATTR_ACTIVITY_STATUS));
			addColumnDefinition(ColumnConstants.TYPE_OF_COOPERATION, new MDXLevel(MoConstants.TYPE_OF_COOPERATION, MoConstants.H_TYPE_OF_COOPERATION, MoConstants.ATTR_TYPE_OF_COOPERATION));
			addColumnDefinition(ColumnConstants.TYPE_OF_IMPLEMENTATION, new MDXLevel(MoConstants.TYPE_OF_IMPLEMENTATION, MoConstants.H_TYPE_OF_IMPLEMENTATION, MoConstants.ATTR_TYPE_OF_IMPLEMENTATION));
			addColumnDefinition(ColumnConstants.PROCUREMENT_SYSTEM, new MDXLevel(MoConstants.PROCUREMENT_SYSTEM, MoConstants.H_PROCUREMENT_SYSTEM, MoConstants.ATTR_PROCUREMENT_SYSTEM));
			addColumnDefinition(ColumnConstants.FUNDING_STATUS, new MDXLevel(MoConstants.FUNDING_STATUS, MoConstants.H_FUNDING_STATUS, MoConstants.ATTR_FUNDING_STATUS));
			addColumnDefinition(ColumnConstants.MODE_OF_PAYMENT, new MDXLevel(MoConstants.MODE_OF_PAYMENT, MoConstants.H_MODE_OF_PAYMENT, MoConstants.ATTR_MODE_OF_PAYMENT));
			addColumnDefinition(ColumnConstants.FINANCING_INSTRUMENT, new MDXLevel(MoConstants.FINANCING_INSTRUMENT, MoConstants.H_FINANCING_INSTRUMENT, MoConstants.ATTR_FINANCING_INSTRUMENT));
			addColumnDefinition(ColumnConstants.TYPE_OF_ASSISTANCE, new MDXLevel(MoConstants.TYPE_OF_ASSISTANCE, MoConstants.H_TYPE_OF_ASSISTANCE, MoConstants.ATTR_TYPE_OF_ASSISTANCE));
			addColumnDefinition(ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE, new MDXLevel("Pledges " + MoConstants.TYPE_OF_ASSISTANCE, MoConstants.H_TYPE_OF_ASSISTANCE, MoConstants.ATTR_TYPE_OF_ASSISTANCE));
//			addColumnDefinition(ColumnConstants.PROJECT_DESCRIPTION, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_DESCRIPTION, MoConstants.ATTR_PROJECT_DESCRIPTION));
//			addColumnDefinition(ColumnConstants.OBJECTIVE, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_OBJECTIVE, MoConstants.ATTR_OBJECTIVE));
//			addColumnDefinition(ColumnConstants.RESULTS, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_RESULTS, MoConstants.ATTR_RESULTS));
//			addColumnDefinition(ColumnConstants.PURPOSE, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PURPOSE, MoConstants.ATTR_PURPOSE));
//			addColumnDefinition(ColumnConstants.PROJECT_COMMENTS, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_COMMENTS, MoConstants.ATTR_PROJECT_COMMENTS));
//			addColumnDefinition(ColumnConstants.PROJECT_IMPACT, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROJECT_IMPACT, MoConstants.ATTR_PROJECT_IMPACT));
//			addColumnDefinition(ColumnConstants.EQUAL_OPPORTUNITY, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_EQUAL_OPPORTUNITY, MoConstants.ATTR_EQUAL_OPPORTUNITY));
//			addColumnDefinition(ColumnConstants.ENVIRONMENT, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_ENVIRONMENT, MoConstants.ATTR_ENVIRONMENT));
//			addColumnDefinition(ColumnConstants.MINORITIES, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_MINORITIES, MoConstants.ATTR_MINORITIES));
//			addColumnDefinition(ColumnConstants.PROGRAM_DESCRIPTION, new MDXLevel(MoConstants.ACTIVITY_LONG_TEXTS, MoConstants.H_PROGRAM_DESCRIPTION, MoConstants.ATTR_PROGRAM_DESCRIPTION));
			addColumnDefinition(ColumnConstants.ORIGINAL_COMPLETION_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ORIG_COMPLETION_DATE, MoConstants.ATTR_ORIG_COMPLETION_DATE));
			addColumnDefinition(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_FINAL_DATE_FOR_CONTRACTING, MoConstants.ATTR_FINAL_DATE_FOR_CONTRACTING));
			addColumnDefinition(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_FINAL_DATE_FOR_DISBURSEMENTS, MoConstants.ATTR_FINAL_DATE_FOR_DISBURSEMENTS));
			addColumnDefinition(ColumnConstants.PROPOSED_START_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_START_DATE, MoConstants.ATTR_PROPOSED_START_DATE));
			addColumnDefinition(ColumnConstants.ACTUAL_START_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_START_DATE, MoConstants.ATTR_ACTUAL_START_DATE));
			addColumnDefinition(ColumnConstants.PROPOSED_APPROVAL_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_APPROVAL_DATE, MoConstants.ATTR_PROPOSED_APPROVAL_DATE));
			addColumnDefinition(ColumnConstants.ACTUAL_APPROVAL_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_APPROVAL_DATE, MoConstants.ATTR_ACTUAL_APPROVAL_DATE));
			addColumnDefinition(ColumnConstants.ACTUAL_COMPLETION_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTUAL_COMPLETION_DATE, MoConstants.ATTR_ACTUAL_COMPLETION_DATE));
			addColumnDefinition(ColumnConstants.CURRENT_COMPLETION_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_CURRENT_COMPLETION_DATE, MoConstants.ATTR_CURRENT_COMPLETION_DATE));
			addColumnDefinition(ColumnConstants.PROPOSED_COMPLETION_DATE, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_PROPOSED_COMPLETION_DATE, MoConstants.ATTR_PROPOSED_COMPLETION_DATE));
			addColumnDefinition(ColumnConstants.ACTIVITY_CREATED_ON, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTIVITY_CREATED_ON, MoConstants.ATTR_ACTIVITY_CREATED_ON));
			addColumnDefinition(ColumnConstants.ACTIVITY_UPDATED_ON, new MDXLevel(MoConstants.ACTIVITY_DATES, MoConstants.H_ACTIVITY_UPDATED_ON, MoConstants.ATTR_ACTIVITY_UPDATED_ON));
			addColumnDefinition(ColumnConstants.ACTIVITY_ID, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_ACTIVITY_ID, MoConstants.ATTR_ACTIVITY_ID));
			addColumnDefinition(ColumnConstants.INTERNAL_USE_ID, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_INTERNAL_USE_ID, MoConstants.ATTR_INTERNAL_USE_ID));
			addColumnDefinition(ColumnConstants.GOVERNMENT_AGREEMENT_NUMBER, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_GOVERNMENT_AGREEMENT_NUMBER, MoConstants.ATTR_GOVERNMENT_AGREEMENT_NUMBER));
			addColumnDefinition(ColumnConstants.BUDGET_CODE_PROJECT_ID, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_BUDGET_CODE_PROJECT_ID, MoConstants.ATTR_BUDGET_CODE_PROJECT_ID));
			addColumnDefinition(ColumnConstants.DRAFT, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.ATTR_DRAFT, MoConstants.ATTR_DRAFT));
			addColumnDefinition(ColumnConstants.FY, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_FY, MoConstants.ATTR_FY));
			addColumnDefinition(ColumnConstants.VOTE, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_VOTE, MoConstants.ATTR_VOTE));
			addColumnDefinition(ColumnConstants.SUB_VOTE, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_SUB_VOTE, MoConstants.ATTR_SUB_VOTE));
			addColumnDefinition(ColumnConstants.PROJECT_CODE, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_PROJECT_CODE, MoConstants.ATTR_PROJECT_CODE));
			addColumnDefinition(ColumnConstants.MINISTRY_CODE, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_MINISTRY_CODE, MoConstants.ATTR_MINISTRY_CODE));
			addColumnDefinition(ColumnConstants.CRIS_NUMBER, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_CRIS_NUMBER, MoConstants.ATTR_CRIS_NUMBER));
			addColumnDefinition(ColumnConstants.PROJECT_IMPLEMENTING_UNIT, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_PROJECT_IMPLEMENTING_UNIT, MoConstants.ATTR_PROJECT_IMPLEMENTING_UNIT));
			addColumnDefinition(ColumnConstants.PROPOSED_PROJECT_LIFE, new MDXLevel(MoConstants.ACTIVITY_FIXED_TEXTS, MoConstants.H_PROPOSED_PROJECT_LIFE, MoConstants.ATTR_PROPOSED_PROJECT_LIFE));
			addColumnDefinition(ColumnConstants.DONOR_TYPE, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.DONOR_GROUP, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.PLEDGES_DONOR_GROUP, new MDXLevel("Pledges " + MoConstants.DONOR_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.DONOR_AGENCY, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.ATTR_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.DONOR_ACRONYM, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_ACRONYM));
			addColumnDefinition(ColumnConstants.DONOR_ID, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			addColumnDefinition(ColumnConstants.EXECUTING_AGENCY_TYPE, new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.EXECUTING_AGENCY_GROUPS, new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.EXECUTING_AGENCY, new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.EXECUTING_AGENCY_ID, new MDXLevel(MoConstants.EXECUTING_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			addColumnDefinition(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.IMPLEMENTING_AGENCY, new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.IMPLEMENTING_AGENCY_ID, new MDXLevel(MoConstants.IMPLEMENTING_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			//put(new ReportColumn(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.BENEFICIARY_AGENCY, new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.BENEFICIARY_AGENCY_ID, new MDXLevel(MoConstants.BENEFICIARY_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			//put(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION??, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			addColumnDefinition(ColumnConstants.RESPONSIBLE_ORGANIZATION, new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.RESPONSIBLE_ORGANIZATION_ID, new MDXLevel(MoConstants.RESPONSIBLE_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			
			addColumnDefinition(ColumnConstants.CONTRACTING_AGENCY, new MDXLevel(MoConstants.CONTRACTING_AGENCY, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.CONTRACTING_AGENCY_ID, new MDXLevel(MoConstants.CONTRACTING_AGENCY, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			addColumnDefinition(ColumnConstants.CONTRACTING_AGENCY_GROUPS, new MDXLevel(MoConstants.CONTRACTING_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
			
			addColumnDefinition(ColumnConstants.REGIONAL_GROUP, new MDXLevel(MoConstants.REGIONAL_GROUP, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.REGIONAL_GROUP_ID, new MDXLevel(MoConstants.REGIONAL_GROUP, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			addColumnDefinition(ColumnConstants.REGIONAL_GROUP_GROUP, new MDXLevel(MoConstants.REGIONAL_GROUP, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));

			addColumnDefinition(ColumnConstants.SECTOR_GROUP, new MDXLevel(MoConstants.SECTOR_GROUP, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.SECTOR_GROUP_ID, new MDXLevel(MoConstants.SECTOR_GROUP, MoConstants.ATTR_ORG_ID, MoConstants.ATTR_ORG_ID));
			addColumnDefinition(ColumnConstants.SECTOR_GROUP_GROUP, new MDXLevel(MoConstants.SECTOR_GROUP, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));

			//put(new ReportColumn(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, ReportEntityType.ENTITY_TYPE_ALL), new MDXLevel(MoConstants.??, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR,  new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR_ID, new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_ID, MoConstants.ATTR_LEVEL_0_SECTOR_ID));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID, new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_ID, MoConstants.ATTR_LEVEL_1_SECTOR_ID));
			addColumnDefinition(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID, new MDXLevel(MoConstants.PRIMARY_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_ID, MoConstants.ATTR_LEVEL_2_SECTOR_ID));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR_ID, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_ID, MoConstants.ATTR_LEVEL_0_SECTOR_ID));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_ID, MoConstants.ATTR_LEVEL_1_SECTOR_ID));
			addColumnDefinition(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR_ID, new MDXLevel(MoConstants.SECONDARY_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_ID, MoConstants.ATTR_LEVEL_2_SECTOR_ID));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_1_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.H_LEVEL_2_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR_ID, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_ID, MoConstants.ATTR_LEVEL_0_SECTOR_ID));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR_ID, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.ATTR_LEVEL_1_SECTOR_ID, MoConstants.ATTR_LEVEL_1_SECTOR_ID));
			addColumnDefinition(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID, new MDXLevel(MoConstants.TERTIARY_SECTOR, MoConstants.ATTR_LEVEL_2_SECTOR_ID, MoConstants.ATTR_LEVEL_2_SECTOR_ID));

			addColumnDefinition(ColumnConstants.PLEDGES_SECTORS, new MDXLevel(MoConstants.PLEDGE_PRIMARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.PLEDGES_SECONDARY_SECTORS, new MDXLevel(MoConstants.PLEDGE_SECONDARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));
			addColumnDefinition(ColumnConstants.PLEDGES_TERTIARY_SECTORS, new MDXLevel(MoConstants.PLEDGE_TERTIARY_SECTOR, MoConstants.H_LEVEL_0_SECTOR, MoConstants.ATTR_LEVEL_0_SECTOR_NAME));

			final String[][] programConstantsList = {
					{ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, MoConstants.NATIONAL_OBJECTIVES, "National Planning Objectives Level "},
					{ColumnConstants.PRIMARY_PROGRAM, MoConstants.PRIMARY_PROGRAMS, "Primary Program Level "},
					{ColumnConstants.SECONDARY_PROGRAM, MoConstants.SECONDARY_PROGRAMS, "Secondary Program Level "},
					{ColumnConstants.TERTIARY_PROGRAM, MoConstants.TERTIARY_PROGRAMS, "Tertiary Program Level "}
					};
			for (String[] programConstants : programConstantsList) {
				addColumnDefinition(programConstants[0] + " Detail", new MDXLevel(programConstants[1], "Detail", "Detail Name"));
				addColumnDefinition(programConstants[0] + " Detail Id", new MDXLevel(programConstants[1], "Detail Id", "Detail Id"));
				
				addColumnDefinition(programConstants[0], new MDXLevel(programConstants[1], "Normal", "Level 1 Name"));
				addColumnDefinition(programConstants[0] + " Id", new MDXLevel(programConstants[1], "Normal Id", "Level 1 Id"));
				for(int i = 1; i <= 8; i++) {
					addColumnDefinition(programConstants[2] + i, new MDXLevel(programConstants[1], "Level " + i, "Level " + i + " Name"));
					addColumnDefinition(programConstants[2] + i + " Id", new MDXLevel(programConstants[1], "Level " + i + " Id", "Level " + i + " Id"));
				}
			}
			addColumnDefinition(ColumnConstants.PLEDGES_PROGRAMS, new MDXLevel("Pledges " + MoConstants.PRIMARY_PROGRAMS, "Level 1", "Level 1 Name"));
			addColumnDefinition(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS, new MDXLevel("Pledges " + MoConstants.SECONDARY_PROGRAMS, "Level 1", "Level 1 Name"));
			addColumnDefinition(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS, new MDXLevel("Pledges " + MoConstants.TERTIARY_PROGRAMS, "Level 1", "Level 1 Name"));
			addColumnDefinition(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES, new MDXLevel("Pledges " + MoConstants.NATIONAL_OBJECTIVES, "Level 1", "Level 1 Name"));
								
			addColumnDefinition(ColumnConstants.COUNTRY, new MDXLevel(MoConstants.LOCATION, MoConstants.H_COUNTRIES,  MoConstants.ATTR_COUNTRY_NAME));
			addColumnDefinition(ColumnConstants.REGION, new MDXLevel(MoConstants.LOCATION, MoConstants.H_REGIONS,  MoConstants.ATTR_REGION_NAME));
			addColumnDefinition(ColumnConstants.ZONE, new MDXLevel(MoConstants.LOCATION, MoConstants.H_ZONES, MoConstants.ATTR_ZONE_NAME));
			addColumnDefinition(ColumnConstants.DISTRICT, new MDXLevel(MoConstants.LOCATION, MoConstants.H_DISTRICTS, MoConstants.ATTR_DISTRICT_NAME));
			addColumnDefinition(ColumnConstants.LOCATION, new MDXLevel(MoConstants.LOCATION, MoConstants.ATTR_LOCATION_NAME,  MoConstants.ATTR_LOCATION_NAME));
			addColumnDefinition(ColumnConstants.GEOCODE, new MDXLevel(MoConstants.LOCATION, MoConstants.H_GEO_CODE, MoConstants.ATTR_GEO_ID));
			
			addColumnDefinition(ColumnConstants.PLEDGES_REGIONS, new MDXLevel("Pledges " + MoConstants.LOCATION, MoConstants.H_REGIONS,  MoConstants.ATTR_REGION_NAME));
			addColumnDefinition(ColumnConstants.PLEDGES_ZONES, new MDXLevel("Pledges " + MoConstants.LOCATION, MoConstants.H_ZONES, MoConstants.ATTR_ZONE_NAME));
			addColumnDefinition(ColumnConstants.PLEDGES_DISTRICTS, new MDXLevel("Pledges " + MoConstants.LOCATION, MoConstants.H_DISTRICTS, MoConstants.ATTR_DISTRICT_NAME));
			
			addColumnDefinition(ColumnConstants.COMPONENT_NAME, new MDXLevel(MoConstants.COMPONENT, MoConstants.H_COMPONENT_NAME, MoConstants.ATTR_COMPONENT_NAME));
			addColumnDefinition(ColumnConstants.COMPONENT_DESCRIPTION, new MDXLevel(MoConstants.COMPONENT, MoConstants.H_COMPONENT_DESCRIPTION, MoConstants.ATTR_COMPONENT_DESCRIPTION));
			addColumnDefinition(ColumnConstants.COMPONENT_TYPE, new MDXLevel(MoConstants.COMPONENT, MoConstants.H_COMPONENT_TYPE, MoConstants.ATTR_COMPONENT_TYPE));
			addColumnDefinition(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, new MDXLevel(MoConstants.COMPONENT_FUNDING_ORGANIZATION, MoConstants.H_ORG_NAME, MoConstants.ATTR_ORG_NAME));
			addColumnDefinition(ColumnConstants.PROPOSED_PROJECT_AMOUNT, new MDXLevel(MoConstants.ACTIVITY_CURRENCY_AMOUNTS, MoConstants.ATTR_PROPOSED_PROJECT_AMOUNT, MoConstants.ATTR_PROPOSED_PROJECT_AMOUNT));
			addColumnDefinition(ColumnConstants.UNCOMMITTED_BALANCE, new MDXLevel(MoConstants.ACTIVITY_CURRENCY_AMOUNTS, MoConstants.ATTR_UNCOMMITTED_BALANCE, MoConstants.ATTR_UNCOMMITTED_BALANCE));
			addColumnDefinition(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE, new MDXLevel(MoConstants.ACTIVITY_CURRENCY_AMOUNTS, MoConstants.ATTR_UNCOMMITTED_CUMULATIVE_BALANCE, MoConstants.ATTR_UNCOMMITTED_CUMULATIVE_BALANCE));
			addColumnDefinition(ColumnConstants.FORECAST_EXECUTION_RATE, new MDXLevel(MoConstants.ACTIVITY_CURRENCY_AMOUNTS, MoConstants.ATTR_FORECAST_EXECUTION_RATE, MoConstants.ATTR_FORECAST_EXECUTION_RATE));
			addColumnDefinition(ColumnConstants.PLEDGE_STATUS, new MDXLevel("Pledge Status", MoConstants.H_CATEGORY_NAME, MoConstants.ATTR_CATEGORY_NAME));
			
			for (String idx : new String[]{"1", "2"}) {
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___NAME.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_NAME + idx, MoConstants.ATTR_CONTACT_NAME + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___TITLE.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_TITLE + idx, MoConstants.ATTR_CONTACT_TITLE + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___ADDRESS.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_ADDRESS + idx, MoConstants.ATTR_CONTACT_ADDRESS + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___EMAIL.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_EMAIL + idx, MoConstants.ATTR_CONTACT_EMAIL + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___MINISTRY.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_MINISTRY + idx, MoConstants.ATTR_CONTACT_MINISTRY + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___TELEPHONE.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_PHONE + idx, MoConstants.ATTR_CONTACT_PHONE + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___FAX.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_FAX + idx, MoConstants.ATTR_CONTACT_FAX + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___ALTERNATE_CONTACT.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_ALTERNAME_NAME + idx, MoConstants.ATTR_CONTACT_ALTERNAME_NAME + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___ALTERNATE_EMAIL.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_ALTERNAME_EMAIL + idx, MoConstants.ATTR_CONTACT_ALTERNAME_EMAIL + idx));
				addColumnDefinition(ColumnConstants.PLEDGE_CONTACT_1___ALTERNATE_PHONE.replace("1", idx), new MDXLevel(MoConstants.PLEDGES_CONTACTS, MoConstants.ATTR_CONTACT_ALTERNAME_PHONE + idx, MoConstants.ATTR_CONTACT_ALTERNAME_PHONE + idx));
			}
			
			addColumnDefinition(ColumnConstants.CUMULATIVE_COMMITMENT, new MDXLevel(MoConstants.ACTIVITY_TOTAL_AMOUNTS, MoConstants.ATTR_CUMULATIVE_COMMITMENT, MoConstants.ATTR_CUMULATIVE_COMMITMENT));
			addColumnDefinition(ColumnConstants.CUMULATIVE_DISBURSEMENT, new MDXLevel(MoConstants.ACTIVITY_TOTAL_AMOUNTS, MoConstants.ATTR_CUMULATIVE_DISBURSEMENT, MoConstants.ATTR_CUMULATIVE_DISBURSEMENT));
			addColumnDefinition(ColumnConstants.CUMULATIVE_EXECUTION_RATE, new MDXLevel(MoConstants.ACTIVITY_TOTAL_AMOUNTS, MoConstants.ATTR_CUMULATIVE_EXECUTION_RATE, MoConstants.ATTR_CUMULATIVE_EXECUTION_RATE));
			addColumnDefinition(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE, new MDXLevel(MoConstants.ACTIVITY_TOTAL_AMOUNTS, MoConstants.ATTR_UNDISBURSED_CUMULATIVE_BALANCE, MoConstants.ATTR_UNDISBURSED_CUMULATIVE_BALANCE));

			
			for(String colName: Arrays.asList(ColumnConstants.AGREEMENT_CLOSE_DATE, ColumnConstants.AGREEMENT_CODE, 
					ColumnConstants.AGREEMENT_EFFECTIVE_DATE, ColumnConstants.AGREEMENT_SIGNATURE_DATE, ColumnConstants.AGREEMENT_TITLE_CODE, ColumnConstants.AGREEMENT_PARLIAMENTARY_APPROVAL_DATE))
				addColumnDefinition(colName, new MDXLevel(MoConstants.AGREEMENT, colName, colName));
			
			addColumnDefinition(ColumnConstants.PLEDGES_DETAIL_START_DATE, new MDXLevel(MoConstants.PLEDGES_DETAIL_DATES, MoConstants.PLEDGES_DETAIL_START_DATE, MoConstants.PLEDGES_DETAIL_START_DATE));
			addColumnDefinition(ColumnConstants.PLEDGES_DETAIL_END_DATE, new MDXLevel(MoConstants.PLEDGES_DETAIL_DATES, MoConstants.PLEDGES_DETAIL_END_DATE, MoConstants.PLEDGES_DETAIL_END_DATE));
			addColumnDefinition(ColumnConstants.PLEDGES_DETAIL_DATE_RANGE, new MDXLevel(MoConstants.PLEDGES_DETAIL_DATES, MoConstants.PLEDGES_DETAIL_DATE_RANGE, MoConstants.PLEDGES_DETAIL_DATE_RANGE));
			
			//Measures - Entity type - All
			AmpCategoryValue actualAdjType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
			// standard trivial measures
			for (String transactionType:ArConstants.TRANSACTION_TYPE_NAME_TO_ID.keySet())
				for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
					if (transactionType.equals(ArConstants.MTEF_PROJECTION))
						continue; // MTEF projections live their own life
					
					String measureName = adj.getValue() + " " + transactionType;
					addMeasureDefinition(measureName);
					
					// adding Totals (unaffected by filters or % distribution)
					// full name computed columns
					String[] prefixList = {"Total ", "Total Grand "};
					for (String prefix : prefixList) {
						String computedTotals = prefix + measureName;
						MDXLevel level = new MDXLevel(MoConstants.ACTIVITY_TOTAL_AMOUNTS, computedTotals, computedTotals);
						if (adj.equals(actualAdjType) && prefix.equals("Total ")) {
							// current measures names for compatibility with old reports
							computedTotals = "Total "  + transactionType;
						}
						addColumnDefinition(computedTotals, level);
					}
				}
			
			// ssc custom trivial measures
			for (String transactionType : ArConstants.SSC_TRANSACTION_TYPE_NAME_TO_ID.keySet()) {
				for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY)) {
					addMeasureDefinition(adj.getValue() + " " + transactionType);
				}
			}
			addMeasureDefinition(MeasureConstants.CUMULATED_SSC_COMMITMENTS);
					
			// define Execution Rate only of dependent measures were also defined
			if (definedMeasures.contains(MeasureConstants.ACTUAL_DISBURSEMENTS) && definedMeasures.contains(MeasureConstants.PLANNED_DISBURSEMENTS)) {
				addMeasureDefinition(MeasureConstants.EXECUTION_RATE);
			}
			
			addMeasureDefinition(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL);
			addMeasureDefinition(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE);
			addMeasureDefinition(MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL);
			addMeasureDefinition(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT);
			addMeasureDefinition(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS);
			addMeasureDefinition(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS);
			dependency.put(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, ColumnConstants.TOTAL_GRAND_ACTUAL_COMMITMENTS);
			dependency.put(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS, ColumnConstants.TOTAL_GRAND_ACTUAL_DISBURSEMENTS);
			addMeasureDefinition(MeasureConstants.UNDISBURSED_BALANCE);
			addMeasureDefinition(MeasureConstants.PLEDGES_COMMITMENT_GAP);
			
			addMeasureDefinition(MeasureConstants.MTEF_PROJECTIONS);
			addMeasureDefinition(MeasureConstants.PIPELINE_MTEF_PROJECTIONS);
			addMeasureDefinition(MeasureConstants.PROJECTION_MTEF_PROJECTIONS);
			
			addMeasureDefinition(MeasureConstants.REAL_COMMITMENTS);
			addMeasureDefinition(MeasureConstants.REAL_DISBURSEMENTS);
			addMeasureDefinition(MeasureConstants.REAL_MTEFS);
			
			addMeasureDefinition(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
			addMeasureDefinition(MeasureConstants.CUMULATED_DISBURSEMENTS);
			addMeasureDefinition(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
			
			addMeasureDefinition(MeasureConstants.ALWAYS_PRESENT);
		}
	};
	
	public final static Map<String, String> fromFullNameToColumnName = buildReverseForColumns();
	
	static Map<String, String> buildReverseForColumns() {
		Map<String, String> res = new HashMap<>();
		boolean shouldCrash = false;
		for(NamedTypedEntity nte:entityMap.keySet())
			if (nte instanceof ReportColumn) {
				String fullName = entityMap.get(nte).getFullName();
				if (res.containsKey(fullName)) {
					shouldCrash = true;
					Logger.getLogger(MondrianMapping.class).error("two columns map to the same Mondrian level: " + nte.getEntityName() + " and " + res.get(fullName));
				}
				res.put(fullName, nte.getEntityName());
			}
		if (shouldCrash)
			throw new RuntimeException("crashing because of the above-mentioned doubly-linked Mondrian Level");
		return res;
	}
}
