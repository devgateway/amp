package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.NpdUtil;

public class SaveDefaultYearsForNpdGraph extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        HttpSession session=request.getSession();
        // Get the current member 
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        
        NpdForm npdForm=(NpdForm)form;      
        
        String [] years= npdForm.getSelYears();
        if(years!=null){
            String createdString=createStringFromSelectedYears(years);
            NpdSettings npdSettings=NpdUtil.getCurrentSettings(teamMember.getTeamId());
            if(npdSettings==null){
                npdSettings=new NpdSettings();
                npdSettings.setWidth(new Integer(ChartUtil.CHART_WIDTH));
                npdSettings.setHeight(new Integer(ChartUtil.CHART_HEIGHT));
            }
            npdSettings.setSelectedYearsForTeam(createdString);
            NpdUtil.updateSettings(npdSettings);
        }
        
        return null;
    }
    
    private String createStringFromSelectedYears(String [] years){
        String result=new String();
        int size=years.length;
        for (int i = 0; i < size; i++) {
            result+=years[i];
            if(i< size-1){
                result+=",";
            }
        }
        return result;
    }

}
