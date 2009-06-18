package org.digijava.module.mondrian.query;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mondrian.olap.Util.PropertyList;
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

/**
 * @author Diego Dimunzio
 * @since January 09,2009
 * 
 */


public class SchemaManager extends FilterDynamicSchemaProcessor implements
		DynamicSchemaProcessor {
	protected static Logger logger = Logger.getLogger(SchemaManager.class);
	
	
	
	@Override
	public String processSchema(String arg0, PropertyList arg1)
			throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		File userDir = new File("").getAbsoluteFile();
		FileObject file = fsManager.resolveFile(userDir, arg0);
		FileContent fileContent = ((org.apache.commons.vfs.FileObject) file)
				.getContent();
		String schema = super.filter(arg0, arg1, fileContent.getInputStream());
	 	CacheControlImpl cache = new  mondrian.rolap.CacheControlImpl();
	 	cache.flushSchemaCache();
	 	return setTeamQuery(schema); 
	}
	
	public String setTeamQuery(String schema) {
		String newschema = "";
		newschema = schema.replaceAll("(?:@donorquery)+", getQueryText());
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
		Long siteId = QueryThread.getSite().getId();
		String locale = QueryThread.getLocale().getCode();
		
		try {
			shema = shema.replaceAll("#Activity#", TranslatorWorker.translateText(MoConstants.ACTIVITY, locale, siteId));
			shema = shema.replaceAll("#Primary_Program#", TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#Secundary_Program#", TranslatorWorker.translateText(MoConstants.SECUNDARY_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#Primary_Sector#", TranslatorWorker.translateText(MoConstants.PRIMARY_SECTOR, locale, siteId));
			shema = shema.replaceAll("#Donor_Dates#", TranslatorWorker.translateText(MoConstants.DONOR_DATES, locale, siteId));
			shema = shema.replaceAll("#Regions#", TranslatorWorker.translateText(MoConstants.REGIONS, locale, siteId));
			shema = shema.replaceAll("#Status#", TranslatorWorker.translateText(MoConstants.STATUS, locale, siteId));
			shema = shema.replaceAll("#Donor#", TranslatorWorker.translateText(MoConstants.DONOR, locale, siteId));
			shema = shema.replaceAll("#Donor_Group#", TranslatorWorker.translateText(MoConstants.DONOR_GROUP, locale, siteId));
			shema = shema.replaceAll("#Donors_types#", TranslatorWorker.translateText(MoConstants.DONOR_TYPES, locale, siteId));
			shema = shema.replaceAll("#Financing_Instrument#", TranslatorWorker.translateText(MoConstants.FINANCING_INTRUMENT, locale, siteId));
			shema = shema.replaceAll("#Terms_of_Assistance#", TranslatorWorker.translateText(MoConstants.TERMS_OF_ASSISTANCE, locale, siteId));
			shema = shema.replaceAll("#National_Program#", TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM, locale, siteId));

			shema = shema.replaceAll("#All_Activities#", TranslatorWorker.translateText(MoConstants.ALL_ACTIVITIES, locale, siteId));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#All_Primary_Sector#", TranslatorWorker.translateText(MoConstants.ALL_PRIMARY_SECTOR, locale, siteId));
			shema = shema.replaceAll("#All_Donor_Dates#", TranslatorWorker.translateText(MoConstants.ALL_PERIODS, locale, siteId));
			shema = shema.replaceAll("#All_Regions#", TranslatorWorker.translateText(MoConstants.ALL_REGIONS, locale, siteId));
			shema = shema.replaceAll("#All_Status#", TranslatorWorker.translateText(MoConstants.ALL_STATUS, locale, siteId));
			shema = shema.replaceAll("#All_Donor#", TranslatorWorker.translateText(MoConstants.ALL_DONOR, locale, siteId));
			shema = shema.replaceAll("#All_Donors_types#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_TYPES, locale, siteId));
			shema = shema.replaceAll("#All_Donor_Group#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_GROUP, locale, siteId));
			shema = shema.replaceAll("#All_Financing_Instrument#", TranslatorWorker.translateText(MoConstants.ALL_FINANCING_INTRUMENT, locale, siteId));
			shema = shema.replaceAll("#All_Terms_of_Assistance#", TranslatorWorker.translateText(MoConstants.ALL_TERMS_OF_ASSISTANCE, locale, siteId));
			
			shema = shema.replaceAll("#Raw_Actual_Commitments#", TranslatorWorker.translateText(MoConstants.RAW_ACTUAL_COMMITMENTS, locale, siteId));
			shema = shema.replaceAll("#Raw_Actual_Disbursements#", TranslatorWorker.translateText(MoConstants.RAW_ACTUAL_DISBURSEMENTS, locale, siteId));
			shema = shema.replaceAll("#Raw_Actual_Expenditures#", TranslatorWorker.translateText(MoConstants.RAW_ACTUAL_EXPENDITURES, locale, siteId));
			shema = shema.replaceAll("#Raw_Planned_Commitments#", TranslatorWorker.translateText(MoConstants.RAW_PLANNED_COMMITMENTS, locale, siteId));
			shema = shema.replaceAll("#Raw_Planned_Disbursements#", TranslatorWorker.translateText(MoConstants.RAW_PLANNED_DISBURSEMENTS, locale, siteId));
			shema = shema.replaceAll("#Raw_Planned_Expenditures#", TranslatorWorker.translateText(MoConstants.RAW_PLANNED_EXPENDITURES, locale, siteId));
			shema = shema.replaceAll("#Activity_Count#", TranslatorWorker.translateText(MoConstants.ACTIVITY_COUNT, locale, siteId));
			shema = shema.replaceAll("#Actual_Commitments#", TranslatorWorker.translateText(MoConstants.ACTUAL_COMMITMENTS, locale, siteId));
			shema = shema.replaceAll("#Actual_Disbursements#", TranslatorWorker.translateText(MoConstants.ACTUAL_DISBURSEMENTS, locale, siteId));
			shema = shema.replaceAll("#Actual_Expenditures#", TranslatorWorker.translateText(MoConstants.ACTUAL_EXPENDITURES, locale, siteId));
			shema = shema.replaceAll("#Planned_Commitments#", TranslatorWorker.translateText(MoConstants.PLANNED_COMMITMENTS, locale, siteId));
			shema = shema.replaceAll("#Planned_Disbursements#", TranslatorWorker.translateText(MoConstants.PLANNED_DISBURSEMENTS, locale, siteId));
			shema = shema.replaceAll("#Planned_Expenditures#", TranslatorWorker.translateText(MoConstants.PLANNED_EXPENDITURES, locale, siteId));
			
			
			shema = shema.replaceAll("#No_Data#", TranslatorWorker.translateText(ArConstants.UNALLOCATED, locale, siteId));
			
		} catch (WorkerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shema;
	}
}
