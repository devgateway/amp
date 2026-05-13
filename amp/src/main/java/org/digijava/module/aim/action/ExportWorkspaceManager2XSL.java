package org.digijava.module.aim.action;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportWorkspaceManager2XSL extends Action {

    private final static char BULLETCHAR = '\u2022';

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        WorkspaceForm wsForm = (WorkspaceForm) form;
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
        Collection<AmpTeam> teams=TeamUtil.getAllTeams(wsForm.getKeyword(), wsForm.getWorkspaceType());
        
     // title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 10);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCS.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleCS.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleCS.setFont(fontHeader);

        // ordinary cells
        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setColor(HSSFColor.BLACK.index);
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat df = wb.createDataFormat();
        cs.setDataFormat(df.getFormat("General"));
        

        cs.setFont(font);
        cs.setWrapText(true);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        
        // ordinary cells with bottom border
        HSSFCellStyle csLastCell = wb.createCellStyle();

        csLastCell.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csLastCell.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csLastCell.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csLastCell.setFont(font);
        csLastCell.setWrapText(true);
        csLastCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFCellStyle style =wb.createCellStyle();
        style.setFont(font);

        int rowIndex = 0;
        short cellIndex = 0;
        HSSFRow titleRow = sheet.createRow(rowIndex++);
        HSSFCell cellName = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Name",locale,siteId));
        cellName.setCellValue(nameTitle);
        cellName.setCellStyle(titleCS);
        HSSFCell membersTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString membersTitle = new HSSFRichTextString(TranslatorWorker.translateText("Team Members",locale,siteId));
        membersTitleCell.setCellValue(membersTitle);
        membersTitleCell.setCellStyle(titleCS);
        HSSFCell activitiesTitleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString activitiesTitle = new HSSFRichTextString(TranslatorWorker.translateText("Activities",locale,siteId));
        activitiesTitleCell.setCellValue(activitiesTitle);
        activitiesTitleCell.setCellStyle(titleCS);

        if (teams != null) {
            
            for ( AmpTeam team :  teams) {
                List<AmpActivityVersion> activityList =null;
                if (team.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                    activityList =new ArrayList<AmpActivityVersion>(TeamUtil.getManagementTeamActivities(team.getAmpTeamId(),null));

                } else {
                    activityList = new ArrayList<AmpActivityVersion>(TeamUtil.getAllTeamActivities(team.getAmpTeamId(),true,null));

                }
                List<TeamMember> teamMembers = new ArrayList<TeamMember>(TeamMemberUtil.getAllTeamMembers(team.getAmpTeamId()));
                int merge=1;
                int activitySize=0;
                int memberSize=0;
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
                HSSFCell emptyCell=row.createCell(cellIndex++);
                HSSFCell emptyCell2=row.createCell(cellIndex++);
                if(merge>1){
                    nameCell.setCellStyle(cs); 
                    emptyCell.setCellStyle(cs);
                    emptyCell2.setCellStyle(cs);
                }
                else{
                    nameCell.setCellStyle(csLastCell);
                    emptyCell.setCellStyle(csLastCell);
                    emptyCell2.setCellStyle(csLastCell);
                }
              
                for(int j=1;j<=merge;j++){

                    row = sheet.createRow(rowIndex++);
                    cellIndex = 0;
                    HSSFCell workspaceNameCell=row.createCell(cellIndex++);
                    boolean isLastCellInColumn = j==merge;
                    if(isLastCellInColumn){ // last cell from merged cells
                         workspaceNameCell.setCellStyle(csLastCell);
                    }
                    else{
                         workspaceNameCell.setCellStyle(cs);
                    }
                    HSSFCell membCell;
                    if (memberSize >= j) {
                        membCell = row.createCell(cellIndex++);
                        String teamMembersString = BULLETCHAR + teamMembers.get(j-1).getMemberName();
                        HSSFRichTextString membValue = new HSSFRichTextString(teamMembersString);
                        membCell.setCellStyle(style);
                        membCell.setCellValue(membValue);
                    }
                    else{
                        membCell=row.createCell(cellIndex++);
                    }
                    if(isLastCellInColumn){ // last cell from merged cells
                        membCell.setCellStyle(csLastCell);
                    }
                    else{
                        membCell.setCellStyle(cs);
                    }
                    HSSFCell activityCell;
                    if (activitySize >= j) {
                        activityCell = row.createCell(cellIndex++);
                        String activityName = BULLETCHAR + activityList.get(j-1).getName();
                        HSSFRichTextString membValue = new HSSFRichTextString(activityName);
                        activityCell.setCellStyle(style);
                        activityCell.setCellValue(membValue);
                    }
                    else{
                         activityCell=row.createCell(cellIndex++);
                    }
                    if(isLastCellInColumn){ // last cell from merged cells
                        activityCell.setCellStyle(csLastCell);
                    }
                    else{
                        activityCell.setCellStyle(cs);
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
