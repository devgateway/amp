package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AllIndicatorForm;
import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.helper.AllThemes;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class OverallIndicatorManager extends Action
{
    private static Logger logger = Logger.getLogger(OverallIndicatorManager.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null)
        {
            return mapping.findForward("index");
        }
        else
        {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no"))
            {
                return mapping.findForward("index");
            }
        }
        AllIndicatorForm allIndForm = (AllIndicatorForm) form;
        String viewPreference = request.getParameter("view");
        String indicatorFlag = request.getParameter("indicatorFlag");
        String flagShowThemeAndIndicators = request.getParameter("flagShow");
        String strPrgId = request.getParameter("prgId");
        Long prgId = new Long(0);
        if(strPrgId!=null)
        prgId = new Long(strPrgId);
        if(flagShowThemeAndIndicators!=null)
            if(flagShowThemeAndIndicators.equalsIgnoreCase("true")){
                if(strPrgId!=null){
                    Integer tmp = new Integer(strPrgId); 
                    allIndForm.setProgramId(tmp.intValue());
                }
            }
        if(indicatorFlag!=null){
            if(indicatorFlag.equalsIgnoreCase("true"))
                allIndForm.setIndicatorFlag(true);
        }
        else
            allIndForm.setIndicatorFlag(false);
        if(viewPreference!=null)
        {
            if(viewPreference.equals("indicators"))
            {
                allIndForm.setPrgIndicators(ProgramUtil.getAllThemeIndicators());
                allIndForm.setProjIndicators(MEIndicatorsUtil.getAllActivityIds());
                return mapping.findForward("forward");
            }
            else if(viewPreference.equals("multiprogram"))
                return mapping.findForward("gotoMultiProgram");
            else if(viewPreference.equals("meindicators"))
                return mapping.findForward("gotoMEIndicators");
        }
        Collection allThemes = ProgramUtil.getAllThemes();
        Collection subPrograms = new ArrayList();
        Collection indicatorsById = new ArrayList();
        allIndForm.setAllThemes(allThemes);
        Iterator itr = allThemes.iterator();
        Collection doubleColl = new ArrayList();
        HashMap  hMap = new HashMap();
        HashMap themeMap = new HashMap();
        while(itr.hasNext()) {
            AmpTheme tmpAmpTheme = (AmpTheme)itr.next();
             subPrograms = ProgramUtil.getAllSubThemes(tmpAmpTheme.getAmpThemeId());
                Iterator _itr = subPrograms.iterator();
                    while(_itr.hasNext()) {
                        AmpTheme _tmpTheme = (AmpTheme)_itr.next();
                        AmpTheme finalTheme = new AmpTheme();
                        finalTheme.setName(_tmpTheme.getName());
                        finalTheme.setAmpThemeId(_tmpTheme.getAmpThemeId());
                        finalTheme.setIndicators(_tmpTheme.getIndicators());
                        doubleColl.add(finalTheme);
                        indicatorsById = ProgramUtil.getThemeIndicators(finalTheme.getAmpThemeId());
                        themeMap.put(finalTheme.getAmpThemeId(),indicatorsById);
                    }
            hMap.put(tmpAmpTheme.getAmpThemeId(),subPrograms);
        }
        allIndForm.setMap(hMap);
        allIndForm.setThemeIndi(themeMap);
        allIndForm.setSubPrograms(doubleColl);
        allIndForm.setDoubleColl(doubleColl);
        Collection allPrg = null;   
        Iterator prgItr = null;     
        allPrg = ProgramUtil.getAllThemesIndicators();
        prgItr = allPrg.iterator();
        while(prgItr.hasNext()) {
            AllThemes theme =(AllThemes)prgItr.next();
            List prgInds=new ArrayList(theme.getAllPrgIndicators());
            Collections.sort(prgInds, new ProgramUtil.HelperAllPrgIndicatorNameComparator());
            theme.setAllPrgIndicators(prgInds);
        }
        allIndForm.setPrgIndicators(allPrg);
        allPrg = MEIndicatorsUtil.getAllActivityIds();
        prgItr = allPrg.iterator();
        Collection inds=null;
        while(prgItr.hasNext()) {
            AllActivities  act = (AllActivities) prgItr.next();
            inds=act.getAllMEIndicators();
            if(inds!=null){
                List prgInds = new ArrayList(inds);
                Collections.sort(prgInds, new ProgramUtil.HelperAllMEIndicatorNameComparator());
                act.setAllMEIndicators(prgInds);
            }
        }
        allIndForm.setProjIndicators(allPrg);
        return mapping.findForward("forward");
    }
}
