package org.digijava.kernel.ampapi.mondrian.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mondrian.olap.Util;
import mondrian.rolap.CacheControlImpl;
import mondrian.spi.DynamicSchemaProcessor;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author Diego Dimunzio
 * @since January 09,2009
 * 
 */


public class SchemaManager extends FilterDynamicSchemaProcessor implements
		DynamicSchemaProcessor {
	protected static Logger logger = Logger.getLogger(SchemaManager.class);
	
	@Override
	public String processSchema(String arg0, Util.PropertyList connectInfo)
			throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		File userDir = new File("").getAbsoluteFile();
		FileObject file = fsManager.resolveFile(userDir, arg0);
		FileContent fileContent = ((org.apache.commons.vfs.FileObject) file)
				.getContent();
		String schema = super.filter(arg0, connectInfo, fileContent.getInputStream());
		//TODO: Flush the schema cache porperly replace the parameter null with rolap connection
	 	CacheControlImpl cache = new  mondrian.rolap.CacheControlImpl(null);
	 	cache.flushSchemaCache();
	 	return setTeamQuery(schema); 
	}
	
	public String setTeamQuery(String schema) {
		String newschema = "";
		newschema = schema.replaceAll("(?:@donorquery)+", getQueryText());
		try {
			newschema = newschema.replaceAll("(?:@Actual)+", CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase().toString());
			newschema = newschema.replaceAll("(?:@Planned)+", CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getIdInDatabase().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Query = " + getQueryText());
		return Translate(newschema);
	}
	
	
	public String getQueryText() {
		String result = QueryThread.getQuery(); 
		Pattern p = Pattern.compile(MoConstants.AMP_ACTIVITY_TABLE);
		Matcher m = p.matcher(result);
		result = m.replaceAll(MoConstants.CACHED_ACTIVITY_TABLE);
		return result;
	}
	
	private String Translate(String shema){	
			shema = shema.replaceAll("#Activity#", TranslatorWorker.translateText(MoConstants.OLD_ACTIVITY));
			shema = shema.replaceAll("#Primary_Program#", TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAMS));
			shema = shema.replaceAll("#Secondary_Program#", TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS));
			shema = shema.replaceAll("#Primary_Sector#", TranslatorWorker.translateText(MoConstants.PRIMARY_SECTOR));
			shema = shema.replaceAll("#Secondary_Sector#", TranslatorWorker.translateText(MoConstants.SECONDARY_SECTOR));
			shema = shema.replaceAll("#Donor_Dates#", TranslatorWorker.translateText(MoConstants.OLD_DONOR_DATES));
			shema = shema.replaceAll("#Regions#", TranslatorWorker.translateText(MoConstants.OLD_REGIONS));
			shema = shema.replaceAll("#Status#", TranslatorWorker.translateText(MoConstants.OLD_STATUS));
			shema = shema.replaceAll("#Donor#", TranslatorWorker.translateText(MoConstants.OLD_DONOR));
			shema = shema.replaceAll("#Donor_Group#", TranslatorWorker.translateText(MoConstants.OLD_DONOR_GROUP));
			shema = shema.replaceAll("#Donors_types#", TranslatorWorker.translateText(MoConstants.OLD_DONOR_TYPES));
			shema = shema.replaceAll("#Financing_Instrument#", TranslatorWorker.translateText(MoConstants.OLD_FINANCING_INTRUMENT));
			shema = shema.replaceAll("#Terms_of_Assistance#", TranslatorWorker.translateText(MoConstants.OLD_TERMS_OF_ASSISTANCE));
			shema = shema.replaceAll("#National_Program#", TranslatorWorker.translateText(MoConstants.OLD_NATIONAL_PROGRAM));
			shema = shema.replaceAll("#Sub-Sectors#", TranslatorWorker.translateText(MoConstants.OLD_SUB_SECTORS));
			shema = shema.replaceAll("#Sub-Sub-Sectors#", TranslatorWorker.translateText(MoConstants.OLD_SUB_SUB_SECTORS));
			shema = shema.replaceAll("#Secondary-Sub-Sectors#", TranslatorWorker.translateText(MoConstants.OLD_SEC_SUB_SECTORS));
			shema = shema.replaceAll("#Secondary-Sub-Sub-Sectors#", TranslatorWorker.translateText(MoConstants.OLD_SEC_SUB_SUB_SECTORS));
			shema = shema.replaceAll("#Currency#", TranslatorWorker.translateText(MoConstants.OLD_CURRENCY));
			
			shema = shema.replaceAll("#All_Activities#", TranslatorWorker.translateText(MoConstants.ALL_ACTIVITIES));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS));
			shema = shema.replaceAll("#All_Primary_Sectors#", TranslatorWorker.translateText(MoConstants.ALL_PRIMARY_SECTOR));
			shema = shema.replaceAll("#All_Secondary_Sectors#", TranslatorWorker.translateText(MoConstants.ALL_SECONDARY_SECTOR));
			shema = shema.replaceAll("#All_Donor_Dates#", TranslatorWorker.translateText(MoConstants.ALL_PERIODS));
			shema = shema.replaceAll("#All_Regions#", TranslatorWorker.translateText(MoConstants.ALL_REGIONS));
			shema = shema.replaceAll("#All_Status#", TranslatorWorker.translateText(MoConstants.ALL_STATUS));
			shema = shema.replaceAll("#All_Donor#", TranslatorWorker.translateText(MoConstants.ALL_DONOR));
			shema = shema.replaceAll("#All_Donors_types#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_TYPES));
			shema = shema.replaceAll("#All_Donor_Group#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_GROUP));
			shema = shema.replaceAll("#All_Financing_Instrument#", TranslatorWorker.translateText(MoConstants.ALL_FINANCING_INTRUMENT));
			shema = shema.replaceAll("#All_Terms_of_Assistance#", TranslatorWorker.translateText(MoConstants.ALL_TERMS_OF_ASSISTANCE));
			shema = shema.replaceAll("#All Sub-Sectors#", TranslatorWorker.translateText(MoConstants.ALL_SUB_SECTORS));
			shema = shema.replaceAll("#All Sub-Sub-Sectors#", TranslatorWorker.translateText(MoConstants.ALL_SUB_SUB_SECTORS));
			
			shema = shema.replaceAll("#All Currencies#", TranslatorWorker.translateText(MoConstants.ALL_CURRENCIES));
			
			shema = shema.replaceAll("#Activity_Count#", TranslatorWorker.translateText(MoConstants.ACTIVITY_COUNT));
			shema = shema.replaceAll("#Actual_Commitments#", TranslatorWorker.translateText(MoConstants.ACTUAL_COMMITMENTS));
			shema = shema.replaceAll("#Actual_Disbursements#", TranslatorWorker.translateText(MoConstants.ACTUAL_DISBURSEMENTS));
			shema = shema.replaceAll("#Actual_Expenditures#", TranslatorWorker.translateText(MoConstants.ACTUAL_EXPENDITURES));
			shema = shema.replaceAll("#Planned_Commitments#", TranslatorWorker.translateText(MoConstants.PLANNED_COMMITMENTS));
			shema = shema.replaceAll("#Planned_Disbursements#", TranslatorWorker.translateText(MoConstants.PLANNED_DISBURSEMENTS));
			shema = shema.replaceAll("#Planned_Expenditures#", TranslatorWorker.translateText(MoConstants.PLANNED_EXPENDITURES));
			
			shema = shema.replaceAll("#No_Data#", TranslatorWorker.translateText(ArConstants.UNALLOCATED));
			
			shema = shema.replaceAll("#Total Pledges#", TranslatorWorker.translateText(MoConstants.PLEDGES_MEASURE));
			shema = shema.replaceAll("#Tilte#", TranslatorWorker.translateText(MoConstants.PLEDGE_TITTLE));
			shema = shema.replaceAll("#All_Titles#", TranslatorWorker.translateText(MoConstants.PLEDGE_ALL_TITTLE));
			shema = shema.replaceAll("#Type_of_Assistance#", TranslatorWorker.translateText(MoConstants.PLEDGE_TYPE_OF_ASSINETANCE));
			shema = shema.replaceAll("#All_Type_of_Assistance#", TranslatorWorker.translateText(MoConstants.PLEDGE_ALL_TYPE_OF_ASSINETANCE));
			shema = shema.replaceAll("#Aid_Modality#", TranslatorWorker.translateText(MoConstants.PLEDGE_AID_MODALITY));
			shema = shema.replaceAll("#All_Aid_Modality#", TranslatorWorker.translateText(MoConstants.PLEDGE_ALL_AID_MODALITY));
			shema = shema.replaceAll("#Pledges_Dates#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_DATES));
			shema = shema.replaceAll("#All_Pledges_Dates#", TranslatorWorker.translateText(MoConstants.PLEDGE_ALL_PLEDGES_DATES));
			shema = shema.replaceAll("#All_Pledges_Types#", TranslatorWorker.translateText(MoConstants.PLEDGE_ALL_PLEDGES_TYPES));
			shema = shema.replaceAll("#Pledge_Type#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_TYPES));
			shema = shema.replaceAll("#contact_name#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_CONTACT_NAME));
			shema = shema.replaceAll("#contact_email#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_CONTACT_EMAIL));
			shema = shema.replaceAll("#Pledges Actual Commitments#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_COMMITMENTS));
			shema = shema.replaceAll("#Pledges Actual Disbursements#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_DISBURSEMENTS));
			shema = shema.replaceAll("#Commitment Gap#", TranslatorWorker.translateText(MoConstants.PLEDGE_PLEDGES_COMMITMENTS_GAP));
			
			shema = shema.replaceAll("#january#", TranslatorWorker.translateText(MoConstants.MONTH_JANUARY));
			shema = shema.replaceAll("#february#", TranslatorWorker.translateText(MoConstants.MONTH_FEBRUARY));
			shema = shema.replaceAll("#march#", TranslatorWorker.translateText(MoConstants.MONTH_MARCH));
			shema = shema.replaceAll("#april#", TranslatorWorker.translateText(MoConstants.MONTH_APRIL));
			shema = shema.replaceAll("#may#", TranslatorWorker.translateText(MoConstants.MONTH_MAY));
			shema = shema.replaceAll("#june#", TranslatorWorker.translateText(MoConstants.MONTH_JUNE));
			shema = shema.replaceAll("#july#", TranslatorWorker.translateText(MoConstants.MONTH_JULY));
			shema = shema.replaceAll("#august#", TranslatorWorker.translateText(MoConstants.MONTH_AGOUST));
			shema = shema.replaceAll("#september#", TranslatorWorker.translateText(MoConstants.MONTH_SEPTEMBER));
			shema = shema.replaceAll("#october#", TranslatorWorker.translateText(MoConstants.MONTH_OCTOBER));
			shema = shema.replaceAll("#november#", TranslatorWorker.translateText(MoConstants.MONTH_NOVEMBER));
			shema = shema.replaceAll("#december#", TranslatorWorker.translateText(MoConstants.MONTH_DECEMBER));

			shema = shema.replaceAll("@currency", QueryThread.getCurrency());
		return shema;
	}
}
