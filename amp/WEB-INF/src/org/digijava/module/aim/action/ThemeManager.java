/*
 * ThemeManager.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.dimension.NPODimension;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.ProgramUtil.ProgramHierarchyDefinition;
import org.digijava.module.aim.util.ProgramUtil.XMLtreeItemFactory;


public class ThemeManager extends Action {

    private static Logger logger = Logger.getLogger(ThemeManager.class);
    
    public final static int ACTIVITIES_ERROR_COUNT = 10;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        HttpSession session = request.getSession();
        ActionMessages errors = new ActionMessages();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
        ThemeForm themeForm = (ThemeForm) form;
        
        //added for the indicator
        
        String viewPreference = request.getParameter("view");
        String flag = request.getParameter("flag");
        if(viewPreference!=null)
        {
            if(viewPreference.equals("multiprogram"))
            {
                
                if(flag != null){
                    themeForm.setFlag("error");
                }

                /**
                 * New Code, AMP-
                 */
                  // get All themes from DB
                Collection<AmpTheme> allThemes = ProgramUtil.getAllThemes(true);
//              Collection themeFlatTree = CollectionUtils.getFlatHierarchy(
//                      allThemes, 
//                      true,
//                      new ProgramHierarchyDefinition(),
//                      new ProgramUtil.ThemeIdComparator(),
//                      new XMLtreeItemFactory());

                Collection themeFlatTree = CollectionUtils.getHierarchy(
                        allThemes, 
                        new ProgramHierarchyDefinition(),
                        new XMLtreeItemFactory());
                
//              List<AmpTheme> themes
                
                
                themeForm.setThemes(themeFlatTree);
                
                /**
                 * Old Code
                 */
//                  Collection themes = new ArrayList();
//                  Collection Subthemes = new LinkedList();
//                  Subthemes  = new ArrayList();
//              themes = ProgramUtil.getParentThemes();
//              //themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
//              themeForm.setThemes(themes);
//              for(Iterator itr=themeForm.getThemes().iterator();itr.hasNext();)
//              {
//                  AmpTheme item=(AmpTheme)itr.next();
//                  Long id = item.getAmpThemeId();
//                  Subthemes.addAll(ProgramUtil.getAllSubThemes(id));
//              }
//              themeForm.setSubPrograms(Subthemes);
                
                return mapping.findForward("forward");
            }
            else if(viewPreference.equals("indicators"))
            {
                return mapping.findForward("gotoIndicators");
            }
            else if(viewPreference.equals("meindicators"))
            {
                return mapping.findForward("gotoMEIndicators");
            }
        }
        
        Collection themes = new ArrayList();
        Collection Subthemes = new LinkedList();
        Subthemes  = new ArrayList();
        String event = request.getParameter("event");
        
        if (event != null && event.equals("delete"))
        {
            //Iterator itr = DbUtil.getActivityTheme(themeForm.getThemeId()).iterator();
            logger.info(" theme Id is ... "+themeForm.getThemeId());
            boolean flagProblemFound    = false;
        
            //Iterator itr = DbUtil.getActivityThemeFromAAT(new Long(Long.parseLong(request.getParameter("themeId")))).iterator();
                        Long programId=themeForm.getThemeId();
            List<String> acts = ProgramUtil.getActivityNamesUsingProgram(programId);
            Collection col2 = ProgramUtil.getProgramIndicators(programId);
                      
            String nameOfSettingsUsedInActivity = ProgramUtil.getNameOfProgramSettingsUsed( themeForm.getThemeId() );
            
            if( !flagProblemFound && (acts!=null) && (!(acts.isEmpty())) )
            {
                flagProblemFound    = true;
                themeForm.setFlag("activityReferences");
                themeForm.setActivitiesUsingTheme(buildMessageFromActivities(acts));
            }
            if ( !flagProblemFound && nameOfSettingsUsedInActivity != null && nameOfSettingsUsedInActivity.length() > 0 ) {
                flagProblemFound    = true;
                themeForm.setFlag("settingUsedInActivity");
                themeForm.setSettingsUsedByTheme( nameOfSettingsUsedInActivity );
            } 
            if ( !flagProblemFound && (col2 != null) && (!(col2.isEmpty())) ){
                flagProblemFound    = true;
                themeForm.setFlag("indicatorsNotEmpty");
            }
            if ( !flagProblemFound )
            {
                themeForm.setFlag("deleted");
                ////////System.out.println("I deleted this theme....ups!!!!!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
                Long id = new Long(Long.parseLong(request.getParameter("themeId")));
                
                try {
                    ARUtil.clearDimension(NPODimension.class);
                    ProgramUtil.deleteTheme(id);
                } catch (AimException e) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.theme.cannotDeleteTheme"));
                    saveErrors(request, errors);
                }catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                
                return mapping.findForward("delete");
            }
            
            /*Iterator itr = DbUtil.getActivityThemeFromAAT(themeForm.getThemeId()).iterator();
             if (itr.hasNext()) {
                //////System.out.println("activity references i can not delete this theme!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
                themeForm.setFlag("activityReferences");
            }
            else {
                themeForm.setFlag("deleted");
                //////System.out.println("I deleted this theme....ups!!!!!!!!and ThemeID="+themeForm.getThemeId()+"and request param="+request.getParameter("themeId"));
                Long id = new Long(Long.parseLong(request.getParameter("themeId")));
                ProgramUtil.deleteTheme(id);
                mapping.findForward("delete");
            }*/
            
            
        }
        themes = ProgramUtil.getParentThemes();
//      for(Iterator itr=themeForm.getThemes().iterator();itr.hasNext();){
//          AmpTheme item=(AmpTheme)itr.next();
//      Long id = item.getAmpThemeId();
//      
//      themeForm.setSubPrograms(ProgramUtil.getAllSubThemes(id));
//  }
        
//      themeForm.setThemes(themes);
//      for(Iterator itr=themeForm.getThemes().iterator();itr.hasNext();)
//      {
//          AmpTheme item=(AmpTheme)itr.next();
//          Long id = item.getAmpThemeId();
//          Subthemes.addAll(ProgramUtil.getAllSubThemes(id));
//          
//      
//          
//      }
//      themeForm.setSubPrograms(Subthemes);
        //themeForm.setProgramTypeNames(ProgramUtil.getProgramTypes());
         Collection<AmpTheme> allThemes = ProgramUtil.getAllThemes(true);


            Collection themeFlatTree = CollectionUtils.getHierarchy(
                    allThemes, 
                    new ProgramHierarchyDefinition(),
                    new XMLtreeItemFactory());
            
            themeForm.setThemes(themeFlatTree);
        
        
        return mapping.findForward("forward");
    }

    private String buildMessageFromActivities(List<String> acts) {
        StringBuilder sb = new StringBuilder();
        
        int cnt = acts.size() > ACTIVITIES_ERROR_COUNT ? ACTIVITIES_ERROR_COUNT : acts.size();
        for (int i=0; i < cnt; i++) {
            sb.append("<br/>");
            sb.append("\"" + acts.get(i) + "\";");
        }
        
        if (acts.size() > ACTIVITIES_ERROR_COUNT) {
            sb.append("<br/><br/>");
            sb.append("<b>" + TranslatorWorker.translateText("and") + " ");
            sb.append(acts.size() - ACTIVITIES_ERROR_COUNT);
            sb.append(" " + TranslatorWorker.translateText(" more") + "...</b>");
        }
        
        return sb.toString();
    }
}
