/**
 * 
 */
package org.digijava.module.dataExchange.utils;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;

/**
 * @author dan
 *
 */
public class Constants {

	public static final String STATUS_UNAPPROVED = "unapproved";
	public static final String STATUS_APPROVED = "approved";
	
	public static final String IDML_STATUS = "idml.status";
	public static final String IDML_IMPLEMENTATION_LEVELS = "idml.implementationLevels";
	public static final String IDML_ASSISTANCE_TYPE = "idml.assistanceType";
	public static final String IDML_FINANCING_INSTRUMENT = "idml.financingInstrument";
	public static final String IDML_FUNDING_PROJECTIONS_TYPE = "idml.funding.projections.type";
	public static final String IDML_PLAN = "Planned";
	public static final String IDML_ACTUAL = "Actual";
	
	public static final String IDML_REPORTING_AGENCY = "Reporting Agency";
	public static final String IDML_FUNDING_AGENCY = "Funding Agency";
	public static final String IDML_IMPLEMENTING_AGENCY = "Implementing Agency";
	public static final String IDML_BENEFICIARY_AGENCY = "Beneficiary Agency";
	public static final String IDML_CONTRACTING_AGENCY = "Contracting Agency";
	public static final String IDML_REGIONAL_GROUP = "Regional Group";
	public static final String IDML_SECTOR_GROUP = "Sector Group";
	public static final String IDML_EXECUTIN_AGENCY = "Executing Agency";
	public static final String IDML_RESPONSIBLE_ORGANIZATION = "Responsible Organization";
	public static final String IDML_CONTRACTOR = "Contractor";
	public static final String IDML_RELATED_INSTITUTIONS = "Related Institutions";
	
	public static final String AMP_ORGANIZATION = "amp."+AmpOrganisation.class.getName();
	public static final String AMP_SECTOR = "amp."+AmpSector.class.getName();
	
	public static final String CATEG_VALUE_ACTIVITY_STATUS = "activity_status";
	public static final String CATEG_VALUE_IMPLEMENTATION_LEVEL = "implementation_level";
	public static final String CATEG_VALUE_TYPE_OF_ASSISTANCE ="type_of_assistence";
	public static final String CATEG_VALUE_FINANCING_INSTRUMENT = "financing_instrument";
	public static final String CATEG_VALUE_MTEF_PROJECTION = "mtef_projection";
	
}
