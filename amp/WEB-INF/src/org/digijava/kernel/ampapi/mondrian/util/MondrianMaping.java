/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
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
