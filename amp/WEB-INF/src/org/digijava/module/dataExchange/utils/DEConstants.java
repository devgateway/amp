/**
 * 
 */
package org.digijava.module.dataExchange.utils;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;

/**
 * @author dan
 *
 */
public class DEConstants {

	public static final String STATUS_UNAPPROVED = "unapproved";
	public static final String STATUS_APPROVED = "approved";
	
	public static final String IDML_STATUS = "idml.status";
	public static final String IDML_IMPLEMENTATION_LEVELS = "idml.implementationLevels";
	public static final String IDML_IMPLEMENTATION_LOCATION = "idml.implementationLocation";
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
	public static final String IDML_EXECUTING_AGENCY = "Executing Agency";
	public static final String IDML_RESPONSIBLE_ORGANIZATION = "Responsible Organization";
	public static final String IDML_CONTRACTOR = "Contractor";
	public static final String IDML_RELATED_INSTITUTIONS = "Related Institutions";
	
	public static final String AMP_ORGANIZATION = "amp."+AmpOrganisation.class.getName();
	public static final String AMP_SECTOR = "amp."+AmpSector.class.getName();
	public static final String AMP_PROGRAM ="amp."+AmpTheme.class.getName();
	
	public static final String CATEG_VALUE_ACTIVITY_STATUS = "activity_status";
	public static final String CATEG_VALUE_IMPLEMENTATION_LEVEL = "implementation_level";
	public static final String CATEG_VALUE_IMPLEMENTATION_LOCATION = "implementation_location";
	public static final String CATEG_VALUE_TYPE_OF_ASSISTANCE ="type_of_assistence";
	public static final String CATEG_VALUE_FINANCING_INSTRUMENT = "financing_instrument";
	public static final String CATEG_VALUE_MTEF_PROJECTION = "mtef_projection";
	
	public static final String IDML_JAXB_INSTANCE = "org.digijava.module.dataExchange.jaxb";
	public static final String IATI_JAXB_INSTANCE = "org.digijava.module.dataExchangeIATI.iatiSchema.jaxb";
	//this.getServlet().getServletContext().getRealPath("/")+
	//public static final String IATI_SCHEMA_LOCATION = "/doc/iati/schemas/iati-activities-schema.xsd";
	public static final String IDML_IATI_XSL= "/doc/iati/xslt/idml2iati.xslt";
	public static final String IATI_IDML_XSL = "/doc/iati/xslt/iati2idml.xslt";
	public static final String SYNERGY_IATI_IDML_XSL = "/doc/iati/xslt/synergyIati2idml.xslt";
	public static final String CATEG_VALUE_ACTIVITY_EXPORT_OPTIONS = "activity_export_filter_options";
	public static final String IDML_SCHEMA_LOCATION = "/doc/iati/schemas/IDML2.0.xsd";
	//public static final String IATI_SCHEMA_LOCATION = "/doc/dataExchange/iati-activities-schema.xsd";
    public static final String IATI_SCHEMA_LOCATION = "/doc/dataExchange/v1_03/iati-activities-schema-1_03.xsd";
    public static final String IATI_SCHEMA_LOCATION_V_1_03 = "doc/dataExchange/v1_03/iati-activities-schema-1_03.xsd";


    public static final String FIELD_ACTIVITY="activity";
	public static final String FIELD_ACTIVITY_STATUS="activity.status";
	public static final String FIELD_ACTIVITY_DESCRIPTION="activity.description";
	
	public static final int RECORDS_AMOUNT_PER_PAGE 	= 10;
	public static final int MAPPING_RECORDS_AMOUNT_PER_PAGE 	= 25;
	
	public static final String LOG_PER_EXECUTION_DESC_CHECK = "Check feed source";
	public static final String LOG_PER_EXECUTION_DESC_IMPORT = "Import activities";
	
	//using long to ease comparison with AmpId
	public static final Long AMP_ID_UNMAPPED = 0l;
	public static final Long AMP_ID_CREATE_NEW = -1l;
	public static final Long AMP_ID_SAME_AS_MAPPING = -2l;
	public static final Long AMP_ID_DO_NOT_IMPORT = -3l; 
	
}
