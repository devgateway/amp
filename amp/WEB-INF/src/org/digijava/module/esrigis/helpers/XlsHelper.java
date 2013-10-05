package org.digijava.module.esrigis.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
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
	 * @param aA
	 * @return
	 */
	protected String buildSectorsString(AmpActivityVersion aA)
	{
		StringBuilder sectors = new StringBuilder();
		Set<Long> outputSectors = new HashSet<Long>();
		for (Iterator iterator3 = aA.getSectors().iterator(); iterator3.hasNext();) {
			AmpActivitySector sector = (AmpActivitySector) iterator3.next();
			
			if (outputSectors.contains(sector.getAmpActivitySectorId()))
				continue;
			
			if (!outputSectors.isEmpty())
				sectors.append(" - ");
			
			sectors.append(sector.getSectorId().getName());
		}
		
		return sectors.toString();
	}
	
	/**
	 * builds a String containing the list of all unique donors for a project
	 * @param aA
	 * @return
	 */
	protected String buildDonorsString(AmpActivityVersion aA)
	{
		StringBuilder donors = new StringBuilder();
		Set<Long> outputDonors = new HashSet<Long>();
		
		Iterator fundItr = aA.getFunding().iterator();
		
		while (fundItr.hasNext()) {
			AmpFunding ampFunding = (AmpFunding) fundItr.next();
			if (outputDonors.contains(ampFunding.getAmpDonorOrgId().getAmpOrgId()))
				continue;
			
			if (!outputDonors.isEmpty())
				donors.append(" - ");
			
			donors.append(ampFunding.getAmpDonorOrgId().getName());
			outputDonors.add(ampFunding.getAmpDonorOrgId().getAmpOrgId());
		}
		return donors.toString();
	}
	
	protected FundingCalculationsHelper getTotalsForActivity(AmpActivityVersion aA, String currencyCode)
	{
		FundingCalculationsHelper calculations = new FundingCalculationsHelper();
		Iterator fundItr = aA.getFunding().iterator();					
		while (fundItr.hasNext()) {
			AmpFunding ampFunding = (AmpFunding) fundItr.next();
			Collection<AmpFundingDetail> fundDetails = ampFunding.getFundingDetails();
			calculations.doCalculations(fundDetails, currencyCode);
		}
		return calculations;
	}
	
	public HSSFWorkbook XlsMaker(List<AmpActivityVersion> activitylist,HttpServletRequest request, HttpServletResponse response,
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
		
		
		int x = 0;
		for (Iterator iterator = columnNames.iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			HSSFCell cell = row.createCell((short) x);
			str = new HSSFRichTextString(name);
			cell.setCellValue(str);
			cell.setCellStyle(tstyle);
			x++;
		}

		style.setFont(font);
		int i = 1;
		java.util.Date date = new java.util.Date();
		
		long startTS=System.currentTimeMillis();
		long endTS=System.currentTimeMillis();
		logger.info("getActivities in "+(endTS-startTS)/1000.0+" seconds. ");
		logger.info("Iteration Starts");
		
		startTS=System.currentTimeMillis();
		for (Iterator iterator = activitylist.iterator(); iterator.hasNext();) {
			
			AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			
			FundingCalculationsHelper calculations = getTotalsForActivity(aA, filter.getCurrencyCode());
			String sectorstr = buildSectorsString(aA);
			String donors = buildDonorsString(aA);

			String body = getEditTagValue(request,aA.getDescription());
			
			Html2Text h2t = null;
			if (body != null){
				h2t = new Html2Text();
				h2t.parse(body);
			}
			
			if (aA.getStructures().size() >0){
				
				long startActivity=System.currentTimeMillis();
				
				long startactivity=System.currentTimeMillis();
				for (Iterator iterator2 = aA.getStructures().iterator(); iterator2.hasNext();) {
					AmpStructure st = (AmpStructure) iterator2.next();
					row = sheet.createRow((short) (i));
					i++;
					ArrayList<String> values = new ArrayList<String>();
					values.add(date.toString());
					values.add(TranslatorWorker.translateText("Structure"));
					values.add(aA.getAmpId());
					values.add(aA.getName());
					values.add(st.getDescription());
					if (aA.getApprovalDate()!=null){
						values.add(aA.getApprovalDate().toString());
					}else{
						values.add("");
					}
					values.add("");
					values.add(st.getTitle());
					values.add(st.getLatitude());
					values.add(st.getLongitude());
					values.add(sectorstr);
					values.add(donors);
					values.add("");
					values.add("");
					values.add(calculations.getTotActualComm().toString());
					values.add(calculations.getTotActualDisb().toString());
	
					for (int j = 0; j < columnNames.size(); j++) {
						HSSFCell cell = row.createCell(j);
						str = new HSSFRichTextString(values.get(j));
						cell.setCellValue(str);
						cell.setCellStyle(style);
					}
					
				}
				
				long endTSActivity=System.currentTimeMillis();
				logger.info("Actiuvity" + aA.getIdentifier() + " in "+(endTSActivity-startActivity)/1000.0+" seconds. Structures = " + aA.getStructures().size());
				
			}
			
			for (Iterator iterator2 = aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				boolean implocation = alocation.getLocation().getLocation().getParentCategoryValue().getValue()
						.equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				if (!implocation) {
					row = sheet.createRow((short) (i));
					ArrayList<String> values = new ArrayList<String>();
					values.add(date.toString());
					values.add(alocation.getLocation().getLocation().getParentCategoryValue().getValue());
					values.add(aA.getAmpId());
					values.add(aA.getName());
					
					if(h2t !=null){
						values.add(h2t.getText());
					}else{
						values.add("");
					}
					
					if (aA.getApprovalDate()!=null){
						values.add(aA.getApprovalDate().toString());
					}else{
						values.add("");
					}
					values.add(alocation.getLocation().getLocation().getGeoCode());
					values.add(alocation.getLocation().getLocation().getName());
					values.add(alocation.getLocation().getLocation().getGsLat());
					values.add(alocation.getLocation().getLocation().getGsLong());
					
					values.add(sectorstr);
					
					values.add(donors);
					values.add(QueryUtil.getPercentage(calculations.getTotActualComm().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					values.add(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					values.add(calculations.getTotActualComm().toString());
					values.add(calculations.getTotActualDisb().toString());
					
					for (int j = 0; j < columnNames.size(); j++) {
						HSSFCell cell = row.createCell(j);
						str = new HSSFRichTextString(values.get(j));
						cell.setCellValue(str);
						cell.setCellStyle(style);
					}
					i++;
				}
			}
		}
		endTS=System.currentTimeMillis();
		logger.info("iteration done in "+(endTS-startTS)/1000.0+" seconds. ");
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
