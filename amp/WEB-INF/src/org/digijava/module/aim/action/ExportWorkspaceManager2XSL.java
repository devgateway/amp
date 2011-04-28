package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ExportWorkspaceManager2XSL extends Action {

    private final static char BULLETCHAR = '\u2022';

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=Export.xls");
        Site site = RequestUtils.getSite(request);
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        Long siteId = site.getId();
        String locale = navigationLanguage.getCode();
       
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        String[] teams=request.getParameterValues("team");

        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 12);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        HSSFCellStyle style =wb.createCellStyle();
        style.setFont(font);

        int rowIndex = 0;
        short cellIndex = 0;
        HSSFRow titleRow = sheet.createRow(rowIndex++);
        HSSFCell cellName = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Name",locale,siteId));
        nameTitle.applyFont(fontHeader);
        cellName.setCellValue(nameTitle);
        HSSFCell membersTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString membersTitle = new HSSFRichTextString(TranslatorWorker.translateText("Team Members",locale,siteId));
        membersTitle.applyFont(fontHeader);
        membersTitleCell.setCellValue(membersTitle);
        HSSFCell activitiesTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString activitiesTitle = new HSSFRichTextString(TranslatorWorker.translateText("Activities",locale,siteId));
        activitiesTitle.applyFont(fontHeader);
        activitiesTitleCell.setCellValue(activitiesTitle);

        if (teams != null) {
            
            for (String teamId :  teams) {
                AmpTeam team=TeamUtil.getAmpTeam(Long.valueOf(teamId));
                List<AmpActivityVersion> activityList =null;
                if (team.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                    activityList =new ArrayList<AmpActivityVersion>(TeamUtil.getManagementTeamActivities(team.getAmpTeamId(),null));

                } else {
                    activityList = new ArrayList<AmpActivityVersion>(TeamUtil.getAllTeamActivities(team.getAmpTeamId(),null));

                }
                List<TeamMember> teamMembers = new ArrayList<TeamMember>(TeamMemberUtil.getAllTeamMembers(team.getAmpTeamId()));
                int merge=1;
                int activitySize=1;
                int memberSize=1;
                if(activityList!=null&&!activityList.isEmpty()){
                    activitySize=activityList.size();
                }
                if(teamMembers!=null&&!teamMembers.isEmpty()){
                    memberSize=teamMembers.size();
                }
                merge=(activitySize>memberSize)?activitySize:memberSize;
                cellIndex = 0;
                HSSFRow row = sheet.createRow(rowIndex++);
                HSSFCell nameCell = row.createCell(cellIndex++);
                HSSFRichTextString teamName = new HSSFRichTextString(team.getName());
                nameCell.setCellValue(teamName);
                row.createCell(cellIndex++);
                row.createCell(cellIndex++);

                for(int j=1;j<merge;j++){

                    row = sheet.createRow(rowIndex++);
                    cellIndex = 0;
                    row.createCell(cellIndex++);
                    if (memberSize > j) {
                        HSSFCell membCell = row.createCell(cellIndex++);
                        String teamMembersString = BULLETCHAR + teamMembers.get(j-1).getMemberName();
                        HSSFRichTextString membValue = new HSSFRichTextString(teamMembersString);
                        membCell.setCellStyle(style);
                        membCell.setCellValue(membValue);
                    }
                    else{
                        row.createCell(cellIndex++);
                    }
                    if (activitySize > j) {
                        HSSFCell activityCell = row.createCell(cellIndex++);
                        String activityName = BULLETCHAR + activityList.get(j-1).getName();
                        HSSFRichTextString membValue = new HSSFRichTextString(activityName);
                        activityCell.setCellStyle(style);
                        activityCell.setCellValue(membValue);
                    }
                    else{
                        row.createCell(cellIndex++);
                    }

                }
                          
            }
        }
        sheet.autoSizeColumn(0); //adjust width of the first column
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        wb.write(response.getOutputStream());
        return null;
    }
}
