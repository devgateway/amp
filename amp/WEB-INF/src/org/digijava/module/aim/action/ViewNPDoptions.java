package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * Displays NPD options window.
 * @author irakli kobiashvili - ikobiashvili@picktek.com
 *
 */
public class ViewNPDoptions extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		NpdForm npdForm=(NpdForm)form;
		Long pid=npdForm.getProgramId();
		AmpTheme prog = ProgramUtil.getThemeObject(pid);
		Set<IndicatorTheme> indicators=prog.getIndicators();
		if (indicators != null && indicators.size()>0){
			List<IndicatorTheme> sortedIndicators=new ArrayList<IndicatorTheme>(indicators);
			Collections.sort(sortedIndicators,new IndicatorUtil.IndThemeIndciatorNameComparator());
			List<LabelValueBean> indicatorLVBs=new ArrayList<LabelValueBean>(indicators.size());
			for (IndicatorTheme indicator : sortedIndicators) {
				LabelValueBean lvb=new LabelValueBean(indicator.getIndicator().getName(),indicator.getIndicator().getIndicatorId().toString());
				indicatorLVBs.add(lvb);
			}
			npdForm.setIndicators(indicatorLVBs);
		}
		npdForm.setYears(new ArrayList<LabelValueBean>(ProgramUtil.getYearsBeanList()));
		return mapping.findForward("forward");
	}

}
