package org.digijava.module.esrigis.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.HTMLUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.action.DataDispatcher;

import bsh.Console;

public class XlsHelper {
	private static Logger logger = Logger.getLogger(DataDispatcher.class);
	public XlsHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * builds a String containing the list of all unique sectors for a project
	 * @param ampActivityId
	 * @return
	 */
	protected  Map<Long, StringBuilder> buildSectorsString(Collection<Long> activityIds)
	{
		Map<Long, StringBuilder> sectors = new HashMap<Long, StringBuilder>();
		Map<Long, Set<Long>> outputSectors = new HashMap<Long, Set<Long>>();
		for(Long actId:activityIds)
		{
			sectors.put(actId, new StringBuilder());
			outputSectors.put(actId, new HashSet<Long>());
		}
		List<Object[]> sss = PersistenceManager.getSession().createQuery("SELECT aas.activityId.ampActivityId, aas.sectorId FROM " + AmpActivitySector.class.getName() + " aas WHERE aas.activityId.ampActivityId IN (" + Util.toCSStringForIN(activityIds) + ")").list();
		for (Object[] sectorData:sss)
		{
			Long actId = PersistenceManager.getLong(sectorData[0]);
			AmpSector sector = (AmpSector) sectorData[1];
			
			if (outputSectors.get(actId).contains(sector.getAmpSectorId()))
				continue;
			
			if (!outputSectors.get(actId).isEmpty())
				sectors.get(actId).append(" - ");
			
			sectors.get(actId).append(sector.getName());
			outputSectors.get(actId).add(sector.getAmpSectorId());
		}		
		return sectors;
	}
	
	/**
	 * builds a String containing the list of all unique donors for a project
	 * @param aA
	 * @return
	 */
	protected Map<Long, StringBuilder> buildDonorsStrings(Collection<Long> activityIds)
	{		
		Map<Long, StringBuilder> donors = new HashMap<Long, StringBuilder>();
		Map<Long, Set<Long>> outputDonors = new HashMap<Long, Set<Long>>();
		for(Long actId:activityIds)
		{
			donors.put(actId, new StringBuilder());
			outputDonors.put(actId, new HashSet<Long>());
		}
		
		List<Object[]> sss = PersistenceManager.getSession().createQuery("SELECT af.ampActivityId.ampActivityId, af.ampDonorOrgId.ampOrgId, " + AmpOrganisation.hqlStringForName("af.ampDonorOrgId") + " FROM " + AmpFunding.class.getName() + " af WHERE af.ampActivityId.ampActivityId IN (" + Util.toCSStringForIN(activityIds) + ")").list();
		for(Object[] donorInfo:sss)
		{
			Long actId = PersistenceManager.getLong(donorInfo[0]);
			Long donorOrgId = PersistenceManager.getLong(donorInfo[1]);
			String donorOrgName = PersistenceManager.getString(donorInfo[2]);
			
			if (outputDonors.get(actId).contains(donorOrgId))
				continue;
			
			if (!outputDonors.get(actId).isEmpty())
				donors.get(actId).append(" - ");
			
			donors.get(actId).append(donorOrgName);
			outputDonors.get(actId).add(donorOrgId);
		}
		return donors;
	}
	
//	protected FundingCalculationsHelper getTotalsForActivity(AmpActivityVersion aA, String currencyCode)
//	{
//		FundingCalculationsHelper calculations = new FundingCalculationsHelper();
//		Iterator fundItr = aA.getFunding().iterator();					
//		while (fundItr.hasNext()) {
//			AmpFunding ampFunding = (AmpFunding) fundItr.next();
//			if (!ampFunding.isDonorFunding())
//				continue;
//			//Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();
//			calculations.doCalculations(ampFunding, currencyCode);
//		}
//		return calculations;
//	}
	
	/**
	 * returns Map<ampActivityId, TotalsForActivity>
	 * @param activityIdsCondition
	 * @return
	 */
	public static Map<Long, FundingCalculationsHelper> getActivitiesTotals(String activityIdsCondition, String userCurrencyCode)
	{
		// step 1: pre-fetch in Hibernate all the AmpFundingDetail and AmpFundingMTEFProjection instances
		List<Object[]> fundingInfoPrefetched = PersistenceManager.getSession().createQuery("SELECT afd, afd.ampFundingId.ampActivityId.ampActivityId FROM " + AmpFundingDetail.class.getName() + " afd " + activityIdsCondition).list();
		List<Object[]> mtefInfoPrefetched = PersistenceManager.getSession().createQuery("SELECT afd, afd.ampFunding.ampActivityId.ampActivityId FROM " + AmpFundingMTEFProjection.class.getName() + " afd " + activityIdsCondition).list();
		
		Map<Long, FundingCalculationsHelper> calculators = new HashMap<Long, FundingCalculationsHelper>();
		List<Object[]> fundingInfo = PersistenceManager.getSession().createQuery("SELECT af.ampActivityId.ampActivityId, af FROM " + AmpFunding.class.getName() + " af " + activityIdsCondition).list();
		
		for(Object[] donor:fundingInfo)
		{
			Long actId = PersistenceManager.getLong(donor[0]);
			AmpFunding funding = (AmpFunding) donor[1];
			calculators.put(actId, new FundingCalculationsHelper());
			calculators.get(actId).doCalculations(funding, userCurrencyCode);
		}
		return calculators;
	}
		
			
	public HSSFWorkbook XlsMaker(HttpServletRequest request, HttpServletResponse response,
			MapFilter filter) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		ArrayList<String> columnNames = new ArrayList<String>();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition","inline; filename=MapExport.xls");
		HSSFSheet sheet = wb.createSheet("MapExport.xls");

		HSSFRow row = sheet.createRow((short) (0));
		HSSFRichTextString str = null;
		HSSFFont titlefont = wb.createFont();

		titlefont.setFontHeightInPoints((short) 10);
		titlefont.setBoldweight(titlefont.BOLDWEIGHT_BOLD);
		
		Site site = RequestUtils.getSiteDomain(request).getSite();
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		font.setBoldweight(font.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle tstyle = wb.createCellStyle();
		tstyle.setFont(titlefont);
		tstyle.setAlignment(style.ALIGN_CENTER);
		columnNames.add(TranslatorWorker.translateText("Time Stamp"));
		columnNames.add(TranslatorWorker.translateText("Type"));
		columnNames.add(TranslatorWorker.translateText("Activity Id"));
		columnNames.add(TranslatorWorker.translateText("Project Title"));
		columnNames.add(TranslatorWorker.translateText("Description"));
		columnNames.add(TranslatorWorker.translateText("Approval Date"));
		columnNames.add(TranslatorWorker.translateText("GEOID"));
		columnNames.add(TranslatorWorker.translateText("Name"));
		columnNames.add(TranslatorWorker.translateText("Latitude"));
		columnNames.add(TranslatorWorker.translateText("Longitude"));
		columnNames.add(TranslatorWorker.translateText("Sectors"));
		columnNames.add(TranslatorWorker.translateText("Donors"));
		columnNames.add(TranslatorWorker.translateText("Actual Commitments for this location"));
		columnNames.add(TranslatorWorker.translateText("Actual Disbursement for this location"));
		columnNames.add(TranslatorWorker.translateText("Total Actual Commitments"));
		columnNames.add(TranslatorWorker.translateText("Total Actual Disbursement"));
		if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"))
		{
			columnNames.add(TranslatorWorker.translateText("Total MTEF"));
		}
		
		
		int x = 0;
		for (String columnName:columnNames) {
			HSSFCell cell = row.createCell((short) x);
			str = new HSSFRichTextString(columnName);
			cell.setCellValue(str);
			cell.setCellStyle(tstyle);
			x++;
		}

		style.setFont(font);
		int i = 1;
		java.util.Date date = new java.util.Date();
		

		logger.info("Iteration Starts");
		
		int cacheUses = 0, cacheHits = 0;
		
		List<Long> actIds = DbHelper.getActivitiesIds(filter, null);
		String activityIdsCondition = "WHERE amp_activity_id IN (" + Util.toCSStringForIN(actIds) + ")";
		
		// fetch non-html-escaped Activity Descriptions
		Map<Long, String> activityDescriptions = DatabaseViewFetcher.fetchInternationalizedView("v_description", activityIdsCondition, "amp_activity_id", "ebody");
		
		// fetch Structures information
		Map<Long, Set<Long>> structsByAmpIds = DataDispatcher.populateStructs(actIds); // Map<activityId, Set<Struct_Ids>>
		Map<Long, Structure> structsByIds = DataDispatcher.fetchStructs(DataDispatcher.collectIds(structsByAmpIds)); // Map<Struct_id, Structure_info>
				
		Map<Long, List<ActivityLocationDigest>> locations = DataDispatcher.fetchLocationInfos(activityIdsCondition); //Map<activityId, List<ActivityDigest>>
		
		Map<Long, StringBuilder> donorStrings = buildDonorsStrings(actIds); // Map<activityId, donor_string>
		Map<Long, StringBuilder> sectorStrings = buildSectorsString(actIds); // Map<activityId, sector_string>
		String translatedStructure = TranslatorWorker.translateText("Structure");
		
//		// calculating set of activities which need their totals calculated
//		// calculating financial totals for an activity is a (very) expensive operation, so we are trying to minimize the activities we do it for to the bare minimum
//		Set<Long> activitiesWhichNeedTotals = new HashSet<Long>();
//		for(Long actId:structsByAmpIds.keySet())
//			if (!structsByAmpIds.get(actId).isEmpty())
//				activitiesWhichNeedTotals.add(actId);
//		
//		for(Long actId:locations.keySet())
//			if (!locations.get(actId).isEmpty())
//				activitiesWhichNeedTotals.add(actId);
//		
		Map<Long, FundingCalculationsHelper> calculators = getActivitiesTotals(activityIdsCondition, filter.getCurrencyCode());
		
		List<Object[]> activitiesInfo = PersistenceManager.getSession().createQuery("SELECT act.ampActivityId, act.ampId, " + AmpActivityVersion.hqlStringForName("act") + " AS actname, act.approvalDate FROM " + AmpActivityVersion.class.getName() + " act " + activityIdsCondition + " ORDER BY act.ampActivityId").list();
		long startTS = System.currentTimeMillis();
		for (Object[] activityInfo:activitiesInfo)
		{
			long ampActivityId = PersistenceManager.getLong(activityInfo[0]);
			String ampId = PersistenceManager.getString(activityInfo[1]);
			String activityName = PersistenceManager.getString(activityInfo[2]);
			String approvalDate = activityInfo[3] == null ? "" : activityInfo[3].toString();
			
			String sectorstr = sectorStrings.get(ampActivityId).toString();
			String donors = donorStrings.get(ampActivityId).toString();			
			FundingCalculationsHelper calculations = calculators.get(ampActivityId);
			String descriptionBodyRaw = activityDescriptions.get(ampActivityId);
			String description = HTMLUtil.removeHtml(descriptionBodyRaw, true); 

			Set<Long> actStructIds = structsByAmpIds.get(ampActivityId);
			if (actStructIds != null && (!actStructIds.isEmpty()))
			{								
				for(Long structId:actStructIds)
				{
					Structure st = structsByIds.get(structId);
					row = sheet.createRow((short) (i));
					i++;
					ArrayList<String> values = new ArrayList<String>();
					values.add(date.toString());
					values.add(translatedStructure);
					values.add(ampId);
					values.add(activityName);
					values.add(st.getDescription());
					values.add(approvalDate);
					values.add("");
					values.add(st.getName());
					values.add(st.getLat());
					values.add(st.getLon());
					values.add(sectorstr);
					values.add(donors);
					values.add("");
					values.add("");
					values.add(calculations.getTotActualComm().toString());
					values.add(calculations.getTotActualDisb().toString());
					values.add(calculations.getTotalMtef().toString());
	
					for (int j = 0; j < columnNames.size(); j++) {
						HSSFCell cell = row.createCell(j);
						str = new HSSFRichTextString(values.get(j));
						cell.setCellValue(str);
						cell.setCellStyle(style);
					}
					
				}								
			}

			
		if (locations.containsKey(ampActivityId))
			for (ActivityLocationDigest ald:locations.get(ampActivityId))
			{				
				boolean implLocationIsCountry = ald.acvl.getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				if (implLocationIsCountry)
					continue;
				
				row = sheet.createRow((short) (i));
				ArrayList<String> values = new ArrayList<String>();
				values.add(date.toString());
				values.add(TranslatorWorker.translateText(ald.acvl.getParentCategoryValue().getValue()));
				values.add(ampId);
				values.add(activityName);				
				values.add(description);
				values.add(approvalDate);
				
				values.add(ald.acvl.getGeoCode());
				values.add(ald.acvl.getName());
				values.add(ald.acvl.getGsLat());
				values.add(ald.acvl.getGsLong());
					
				values.add(sectorstr);
					
				values.add(donors);
				Double locationPercentage = ald.aalPercentage !=null ? ald.aalPercentage:0d;
				values.add(QueryUtil.getPercentage(calculations.getTotActualComm().getValue(),new BigDecimal(locationPercentage)));
				values.add(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(locationPercentage)));
				values.add(calculations.getTotActualComm().toString());
				values.add(calculations.getTotActualDisb().toString());
				if (FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"))
				{
					values.add(calculations.getTotalMtef().toString());
				}		
				for (int j = 0; j < columnNames.size(); j++) {
					HSSFCell cell = row.createCell(j);
					str = new HSSFRichTextString(values.get(j));
					cell.setCellValue(str);
					cell.setCellStyle(style);
				}
				i++;
			}
		}
		int columnNr = 0;
		for (String columnName:columnNames)
		{
			sheet.autoSizeColumn(columnNr);
			if (sheet.getColumnWidth(columnNr) > 256 * 40)
				sheet.setColumnWidth(columnNr, 256 * 40);
			columnNr ++;
		}
		long endTS = System.currentTimeMillis();
		logger.info("iteration done in "+(endTS-startTS)/1000.0+" seconds, body Cache hits = " + cacheHits * 100.0 / (cacheUses + 0.00001) + "%");
		return wb;
	}
	
	private String  getEditTagValue(HttpServletRequest request,String editKey) throws Exception{
		Site site = RequestUtils.getSite(request);
        String editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, RequestUtils.getNavigationLanguage(request).getCode());
        
        if (editorBody == null) {
        	editorBody = org.digijava.module.editor.util.DbUtil.getEditorBody(site, editKey, SiteUtils.getDefaultLanguages(site).getCode());

        }
        return editorBody;
	}
}
