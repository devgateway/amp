package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.AllIndicatorForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

import javax.servlet.http.*;
import java.util.Collection;
import java.util.Iterator;
import org.digijava.module.aim.helper.AllThemes;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.digijava.module.aim.helper.AllActivities;

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

        Collection allPrg = null;
        Iterator prgItr = null;

        AllIndicatorForm allIndForm = (AllIndicatorForm) form;
		String viewPreference = request.getParameter("view");

		if(viewPreference!=null)
		{
			if(viewPreference.equals("indicators"))
			{
                allPrg = ProgramUtil.getAllThemeIndicators();
                prgItr = allPrg.iterator();
                while(prgItr.hasNext()) {
                    AllThemes theme = (AllThemes) prgItr.next();
                    List prgInds = new ArrayList(theme.getAllPrgIndicators());
                    Collections.sort(prgInds,
                                     new ProgramUtil.HelperAllPrgIndicatorNameComparator());
                    theme.setAllPrgIndicators(prgInds);
                }
                allIndForm.setPrgIndicators(allPrg);

                allPrg = MEIndicatorsUtil.getAllActivityIds();
                prgItr = allPrg.iterator();
                while(prgItr.hasNext()) {
                    AllActivities act = (AllActivities) prgItr.next();
                    List prgInds = new ArrayList(act.getAllMEIndicators());
                    Collections.sort(prgInds,
                                     new ProgramUtil.HelperAllMEIndicatorNameComparator());
                    act.setAllMEIndicators(prgInds);
                }
                allIndForm.setProjIndicators(allPrg);

				return mapping.findForward("forward");
			}
			else if(viewPreference.equals("multiprogram"))
				return mapping.findForward("gotoMultiProgram");
			else if(viewPreference.equals("meindicators"))
				return mapping.findForward("gotoMEIndicators");
		}

        allPrg = ProgramUtil.getAllThemeIndicators();
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
