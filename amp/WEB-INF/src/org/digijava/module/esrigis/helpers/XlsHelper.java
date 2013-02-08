package org.digijava.module.esrigis.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.action.DataDispatcher;

public class XlsHelper {
	private static Logger logger = Logger.getLogger(DataDispatcher.class);
	public XlsHelper() {
		super();
		// TODO Auto-generated constructor stub
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
		columnNames.add(TranslatorWorker.translateText("Commitments for this location"));
		columnNames.add(TranslatorWorker.translateText("Disbursement for this location"));
		columnNames.add(TranslatorWorker.translateText("Total Commitments"));
		columnNames.add(TranslatorWorker.translateText("Total Disbursement"));
		
		
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
			if (aA.getStructures().size() >0){
				for (Iterator iterator2 = aA.getStructures().iterator(); iterator2
						.hasNext();) {
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
					String sectorstr = "";
					for (Iterator iterator3 = aA.getSectors().iterator(); iterator3.hasNext();) {
						AmpActivitySector sector = (AmpActivitySector) iterator3.next();
						if (iterator3.hasNext()) {
							sectorstr = sectorstr+ sector.getSectorId().getName() + " - ";
						} else {
							sectorstr = sectorstr+ sector.getSectorId().getName();
						}
					}
					values.add(sectorstr);
					FundingCalculationsHelper calculations = new FundingCalculationsHelper();
					String donors = "";
					Iterator fundItr = aA.getFunding().iterator();
					while (fundItr.hasNext()) {
						AmpFunding ampFunding = (AmpFunding) fundItr.next();
						Collection fundDetails = ampFunding.getFundingDetails();
						calculations.doCalculations(fundDetails,filter.getCurrencyCode());
						if (fundItr.hasNext()) {
							donors = donors+ ampFunding.getAmpDonorOrgId().getName()+ " - ";
						} else {
							donors = donors+ ampFunding.getAmpDonorOrgId().getName();
						}
					}
					values.add(donors);
					values.add("");
					values.add("");
					values.add(calculations.getTotalCommitments().toString());
					values.add(calculations.getTotActualDisb().toString());
					
					for (int j = 0; j < columnNames.size(); j++) {
						HSSFCell cell = row.createCell(j);
						str = new HSSFRichTextString(values.get(j));
						cell.setCellValue(str);
						cell.setCellStyle(style);
					}
					
				}
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
					String body = getEditTagValue(request,aA.getDescription());
					if (body != null){
						Html2Text h2t = new Html2Text();
						h2t.parse(body);
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
					String sectorstr = "";
					for (Iterator iterator3 = aA.getSectors().iterator(); iterator3.hasNext();) {
						AmpActivitySector sector = (AmpActivitySector) iterator3.next();
						if (iterator3.hasNext()) {
							sectorstr = sectorstr+ sector.getSectorId().getName() + " - ";
						} else {
							sectorstr = sectorstr+ sector.getSectorId().getName();
						}
					}
					values.add(sectorstr);
					FundingCalculationsHelper calculations = new FundingCalculationsHelper();
					String donors = "";
					Iterator fundItr = aA.getFunding().iterator();
					while (fundItr.hasNext()) {
						AmpFunding ampFunding = (AmpFunding) fundItr.next();
						Collection fundDetails = ampFunding.getFundingDetails();
						calculations.doCalculations(fundDetails,filter.getCurrencyCode());
						if (fundItr.hasNext()) {
							donors = donors+ ampFunding.getAmpDonorOrgId().getName()+ " - ";
						} else {
							donors = donors+ ampFunding.getAmpDonorOrgId().getName();
						}
					}
					values.add(donors);
					values.add(QueryUtil.getPercentage(calculations.getTotalCommitments().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					values.add(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					values.add(calculations.getTotalCommitments().toString());
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
			//for (short k = 0; k <= 8; k++)
			//	sheet.autoSizeColumn(k);

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
