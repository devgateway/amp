package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Displays NPD options window.
 * @author irakli kobiashvili - ikobiashvili@picktek.com
 *
 */
public class ViewNPDoptions extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NpdForm npdForm=(NpdForm)form;
        Long pid=npdForm.getProgramId();
        //get theme with id from form
        AmpTheme prog = ProgramUtil.getThemeById(pid);
        //get indicators of the theme
        Set<IndicatorTheme> indicators=prog.getIndicators();
        //if there are indicators
        if (indicators != null && indicators.size()>0){
            //convert to list
            List<IndicatorTheme> sortedIndicators=new ArrayList<IndicatorTheme>(indicators);
            //sort
            Collections.sort(sortedIndicators,new IndicatorUtil.IndThemeIndciatorNameComparator());
            //prepare list of LabelValue beans
            List<LabelValueBean> indicatorLVBs=new ArrayList<LabelValueBean>(indicators.size());
            for (IndicatorTheme indicator : sortedIndicators) {
                //convert each indicator to LabelValueBean
                LabelValueBean lvb=new LabelValueBean(indicator.getIndicator().getName(),indicator.getIndicator().getIndicatorId().toString());
                //add to results list
                indicatorLVBs.add(lvb);
            }
            //set to form
            npdForm.setIndicators(indicatorLVBs);
        }
        //years list for user
        npdForm.setYears(new ArrayList<LabelValueBean>(ProgramUtil.getYearsBeanList()));
        return mapping.findForward("forward");
    }

}
