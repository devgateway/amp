package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.IndicatorGridItem;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * Creates Excel file from Indicators list
 * 
 * @author Irakli Kobiashvili ikobiashvili@picktek.com
 * 
 */
public class ExportIndicators2XSLAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NpdForm npdForm = (NpdForm) form;
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		Site site = RequestUtils.getSite(request);
		 
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=AMPIndicatorsExport.xls");

		AmpTheme mainProg = ProgramUtil.getThemeObject(npdForm.getProgramId());
		Collection<IndicatorGridRow> rows = getGridRows(mainProg, npdForm.getRecursive(), npdForm
				.getSelYears());

		XLSExporter.resetStyles();

		HSSFWorkbook wb = new HSSFWorkbook();
		String sheetName = mainProg.getName();
		if (sheetName.length() > 31){
			sheetName = sheetName.substring(0, 31);
        }else {
            if (sheetName.length() == 0) {
            	// should not be possible, but still...
                sheetName = "blank";
            }
        }
                 
        // replacing odd symbols for sheet name...
        sheetName=sheetName.replace("/","|");
        sheetName=sheetName.replace("*","+");
        sheetName=sheetName.replace("?", " ");
        sheetName=sheetName.replace("\\", "|");
        sheetName=sheetName.replace("[", "(");
        sheetName=sheetName.replace("]", ")");
        sheetName =sheetName.replace(":", "-");
          
		HSSFSheet sheet = wb.createSheet(sheetName);

		
		HSSFCellStyle csHeader = wb.createCellStyle();
		csHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short)12);			
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csHeader.setFont(font);				

		HSSFCellStyle csSubHeader = wb.createCellStyle();
		csSubHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
		HSSFFont fontSubHeader = wb.createFont();
		fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
		//fontSubHeader.setFontHeightInPoints((short)12);			
		fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csSubHeader.setFont(fontSubHeader);				

		
		short rowNum = 0;
		short cellNum = 0;
		Long siteId=site.getId();
		HSSFRow row = sheet.createRow(rowNum++);

		HSSFCell cell = row.createCell(cellNum);
                String hierarchyName=ProgramUtil.getHierarchyName(mainProg);
                String header=TranslatorWorker.translateText("Indicators for", locale, siteId);

		cell.setCellValue(header+"  "+hierarchyName);
		cell.setCellStyle(csHeader);
		
		if (npdForm.getSelYears() != null && npdForm.getSelYears().length > 0) {

			// table header 1
			row = sheet.createRow(rowNum++);
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(" ");
			
			for (int i = 0; i < npdForm.getSelYears().length; i++) {
				cell = row.createCell(cellNum++);
				cellNum++;
				cellNum++;
				cell.setCellValue(npdForm.getSelYears()[i]);
				cell.setCellStyle(csSubHeader);
			}

			// table header 2
			cellNum = 0;
			row = sheet.createRow(rowNum++);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("indicator Name", locale, site.getId()));			
			cell.setCellStyle(csSubHeader);			
			
			cell=row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("indicator Description", locale, site.getId()));			
			cell.setCellStyle(csSubHeader);

			for (int i = 0; i < npdForm.getSelYears().length; i++) {
                                cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Base", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
				cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Actual", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
				cell = row.createCell(cellNum++);
				cell.setCellValue(TranslatorWorker.translateText("Target", locale, site.getId()));
				cell.setCellStyle(csSubHeader);
			}

			// rows
			if (rows != null && rows.size() > 0) {
				for (IndicatorGridRow indic : rows) {
					cellNum = 0;

					row = sheet.createRow(rowNum++);
                                        HSSFRow rowIndSource = sheet.createRow(rowNum++);
                                        HSSFCell cellIndSource =  rowIndSource.createCell(cellNum);
					cell = row.createCell(cellNum++);
					cell.setCellValue(indic.getName());
					cellIndSource.setCellValue(TranslatorWorker.translateText("Source", locale, site.getId()));
					cellIndSource =  rowIndSource.createCell(cellNum);
					cell = row.createCell(cellNum++);
					cell.setCellValue(indic.getDescription());
                                        cellIndSource.setCellValue(" ");
					
					
					List<IndicatorGridItem> values = indic.getValues();
					if (values!=null){
						for (IndicatorGridItem item: values) {
                            cellIndSource = rowIndSource.createCell(cellNum);
                            cell = row.createCell(cellNum++);
							cell.setCellValue(item.getBaseValue());                            
							cellIndSource.setCellValue(item.getBaseValueSource());
                            cellIndSource = rowIndSource.createCell(cellNum);
							cell = row.createCell(cellNum++);
                            cellIndSource.setCellValue(item.getActualValueSource());
							cell.setCellValue(item.getActualValue());
                            cellIndSource = rowIndSource.createCell(cellNum);
							cell = row.createCell(cellNum++);
                            cellIndSource.setCellValue(item.getTargetValueSource());
							cell.setCellValue(item.getTargetValue());
						}
					}
				}
			}
		}
		
		//activities
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		String statusId=npdForm.getStatusId();
		String donorIds=npdForm.getDonorIds();
		Date fromYear = NpdUtil.yearToDate(npdForm.getStartYear(), false);
		Date toYear = NpdUtil.yearToDate(npdForm.getEndYear(), true);
		Collection<ActivityItem> activities = NpdUtil.getActivities(npdForm.getProgramId(),statusId, donorIds, fromYear,toYear, null, tm, null,null);
		List<ActivityItem> acts=null;		
		
		AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
        AmpCurrency curr=ampAppSettings.getCurrency();
        String currCode="USD";
        if(curr!=null){
        	currCode=ampAppSettings.getCurrency().toString();
        }
        
        BigDecimal actualSum = new BigDecimal(0);
        BigDecimal actualDisbSum = new BigDecimal(0);
        BigDecimal plannedCommitments=new BigDecimal(0);
		if (activities != null && activities.size() > 0) {
			acts=new ArrayList<ActivityItem>(activities.size());
			for(ActivityItem activity : activities) {
                //create helper bean from activity and program percent
				ActivityItem item = new ActivityItem(activity.getAct(),currCode,activity.getPercent(), request);
				//get already calculated amounts from helper
				ActivityUtil.ActivityAmounts amounts = item.getAmounts();
				//calculate totals
				actualSum =actualSum.add( amounts.getActualAmount());
				actualDisbSum=actualDisbSum.add( amounts.getActualDisbAmoount());
				plannedCommitments=plannedCommitments.add(amounts.getPlannedAmount());
				acts.add(item);
			}
		}			
	
		if(acts!=null && acts.size()>0){
			cellNum=0;
			//empty row
			row = sheet.createRow(rowNum++);
			cell = row.createCell(cellNum);
			cell.setCellValue(" ");
			//header
			row = sheet.createRow(rowNum++);
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Activities for", locale, siteId)+"  "+hierarchyName);
			cell.setCellStyle(csHeader);
			//sub headers
			cellNum=0;			
			row = sheet.createRow(rowNum++);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("title", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("status", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("donor", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Start Date", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Planned Commitments ", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Actual Commitments ", locale, siteId));
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Actual Disbursements ", locale, siteId));
			cell.setCellStyle(csSubHeader);
			//activities info
			for (ActivityItem item : acts) {
				cellNum=0;
				row = sheet.createRow(rowNum++);
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(item.getName());
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(item.getStatus());
				
				cell = row.createCell(cellNum++);
				String donors="";
				if(item.getDonors()!=null){
					for (LabelValueBean lvb : item.getDonors()) {
						donors+=lvb.getLabel()+ "\n";
					}
				}else{
					donors="--";
				}
				cell.setCellValue(donors); 
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(item.getStartDate());
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(!item.getPlannedAmount().equals("N/A")?item.getPlannedAmount():"--");
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(!item.getActualAmount().equals("N/A")?item.getActualAmount():"--");
				
				cell = row.createCell(cellNum++);
				cell.setCellValue(!item.getActualDisbAmount().equals("N/A")?item.getActualDisbAmount():"--");
			}
			
			//totals
			cellNum=0;
			row = sheet.createRow(rowNum++);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue("");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue("");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue("");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(TranslatorWorker.translateText("Totals", locale, siteId)+": ");
			cell.setCellStyle(csSubHeader);
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(plannedCommitments.doubleValue()!=0? FormatHelper.formatNumber(plannedCommitments):"0");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue((actualSum.doubleValue()!=0)? FormatHelper.formatNumber(actualSum):"0");
			
			cell = row.createCell(cellNum++);
			cell.setCellValue((actualDisbSum.doubleValue()!=0)? FormatHelper.formatNumber(actualDisbSum):"0");
			
		}

		wb.write(response.getOutputStream());

		return null;
	}

	private Collection<IndicatorGridRow> getGridRows(AmpTheme prog, boolean recursive,String[] years) throws DgException{
		List<IndicatorGridRow> result = null;
		if (prog != null && prog.getAmpThemeId() != null) {
			//get all indicators and if recursive=true then all sub indicators too 
			Set<IndicatorTheme> indicators = IndicatorUtil.getIndicators(prog, recursive);
			if (indicators != null && indicators.size() > 0) {
				//convert to list
				List<IndicatorTheme> indicatorsList = new ArrayList<IndicatorTheme>(indicators);
				//sort
				Collections.sort(indicatorsList,new IndicatorUtil.IndThemeIndciatorNameComparator());
				result = new ArrayList<IndicatorGridRow>(indicatorsList.size());
				//create row object for each indicator connection
				for (IndicatorTheme connection : indicatorsList) {
					IndicatorGridRow row = new IndicatorGridRow(connection,years);
					result.add(row);
				}
			}
		}
		return result;
	}


	public static class IndicatorsByyear2XLS extends XLSExporter {

		public IndicatorsByyear2XLS(Exporter parent, Viewable item) {
			super(parent, item);
		}

		public IndicatorsByyear2XLS(HSSFWorkbook wb, HSSFSheet sheet,
				HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
				Viewable item) {
			super(wb, sheet, row, rowId, colId, ownerId, item);
		}

		public void generate() {
			// TODO Auto-generated method stub

		}

	}

}
