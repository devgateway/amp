package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;

public class EditAllIndicators extends Action {

    private static Logger logger = Logger.getLogger(EditAllIndicators.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception 
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) 
        {
            return mapping.findForward("index");
        } 
        else 
        {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) 
            {
                return mapping.findForward("index");
            }
        }
        
        ThemeForm indThemeForm = (ThemeForm) form;
        String type = request.getParameter("indicator");
        
        Long id = new Long(Long.parseLong(request.getParameter("indicatorId")));
//      AllMEIndicators allMEInd = new AllMEIndicators();
//      
//      if(type.equalsIgnoreCase("project"))
//      {
//          allMEInd = MEIndicatorsUtil.getMEIndicator(id);
//          indThemeForm.setIndicatorId(allMEInd.getAmpMEIndId());
//          indThemeForm.setName(allMEInd.getName());
//          indThemeForm.setCode(allMEInd.getCode());
//      }
//      if(type.equalsIgnoreCase("saveProj"))
//      {
//          allMEInd.setAmpMEIndId(id);
//          allMEInd.setName(indThemeForm.getName());
//          allMEInd.setCode(indThemeForm.getCode());
//          MEIndicatorsUtil.saveIndicator(allMEInd);
//          return mapping.findForward("saving");
//      }
//      if(type.equalsIgnoreCase("assign"))
//      {
//          indThemeForm.setThemes(ProgramUtil.getAllPrograms());
//          indThemeForm.setIndicatorId(id);
//          return mapping.findForward("assign");
//      }
        if(type.equalsIgnoreCase("deleteProgram"))
        {
             AmpThemeIndicators tempInd = new AmpThemeIndicators();
             
              tempInd = ProgramUtil.getThemeIndicatorById(id);
            
              if(tempInd.getThemes().size() != 0){
                
                  Iterator itr=tempInd.getThemes().iterator();
                   
                  while (itr.hasNext()) {
                    AmpTheme insect = (AmpTheme) itr.next();
                    indThemeForm.setThemeName(insect.getName());
                }
              }else{
                  ProgramUtil.deleteProgramIndicator(id);
              }
            return mapping.findForward("deleted");
        }
        if(type.equalsIgnoreCase("deleteGlobal"))
        {
            MEIndicatorsUtil.deleteProjIndicator(id);
            return mapping.findForward("deleted");
        }
        if(type.equalsIgnoreCase("deleteProject"))
        {
            MEIndicatorsUtil.deleteProjIndicator(id);
            return mapping.findForward("deleted");
        }
        
        return mapping.findForward("forward");
    }
}
