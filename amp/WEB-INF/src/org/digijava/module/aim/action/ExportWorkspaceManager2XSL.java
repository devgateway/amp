package org.digijava.module.aim.action;

import java.util.Collection;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
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
                cellIndex = 0;
                HSSFRow row = sheet.createRow(rowIndex++);
                
                HSSFCell nameCell = row.createCell(cellIndex++);
                HSSFRichTextString teamName = new HSSFRichTextString(team.getName());
                nameCell.setCellStyle(style);
                nameCell.setCellValue(teamName);
                Collection<AmpActivity> activityList =null;
                if (team.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                    activityList = TeamUtil.getManagementTeamActivities(team.getAmpTeamId(),null);

                } else {
                    activityList = TeamUtil.getAllTeamActivities(team.getAmpTeamId(),null);

                }       
                
                HSSFCell membCell = row.createCell(cellIndex++);
                String actString = "";
                String teamMembersString = "";
                Collection<TeamMember> teamMembers = TeamMemberUtil.getAllTeamMembers(team.getAmpTeamId());
                if (teamMembers != null) {
                    for (TeamMember teamMember : teamMembers) {
                        teamMembersString += BULLETCHAR + teamMember.getMemberName();
                    }

                    HSSFRichTextString membValue = new HSSFRichTextString(teamMembersString);
                    membCell.setCellStyle(style);
                    membCell.setCellValue(membValue);

                }


                if (activityList != null) {
                    for (AmpActivity activity : activityList) {
                        actString += BULLETCHAR + activity.getName();
                    }
                    int size =(int) Math.ceil(actString.length() / 30000.0);
                    String activity = "";
                    for (int i = 0; i <= size; i++) {
                        HSSFCell actCell = row.createCell(cellIndex++);
                        actCell.setCellStyle(style);
                        if (actString.length() <= 30000.0) {
                            activity = actString;
                        } else {
                            activity = actString.substring(0, 30001);
                            actString = actString.substring(30001);
                        }
                        HSSFRichTextString actValue = new HSSFRichTextString(activity);
                        actCell.setCellValue(actValue);


                    }
               
                }
            }
        }
        wb.write(response.getOutputStream());
        return null;
    }
}
