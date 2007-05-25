package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.util.ProgramUtil;

public class ViewNpdGrid extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest reqest, HttpServletResponse sponse)
			throws Exception {
		NpdForm npdForm = (NpdForm) form;
		if (npdForm.getProgramId() != null) {
			AmpTheme mainProg = ProgramUtil.getThemeObject(npdForm
					.getProgramId());
			Set indicators = getIndicators(mainProg, npdForm.getRecursive());
			if (indicators != null && indicators.size() > 0) {
				List indicatorsList = new ArrayList(indicators);
				Collections.sort(indicatorsList,
						new ProgramUtil.IndicatorNameComparator());
				List result = new ArrayList(indicatorsList.size());
				// npdForm.setIndicators(indicatorsList);
				for (Iterator iter = indicatorsList.iterator(); iter.hasNext();) {
					AmpThemeIndicators indicator = (AmpThemeIndicators) iter
							.next();
					IndicatorGridRow row = new IndicatorGridRow(indicator,
							npdForm.getSelYears());
					result.add(row);
				}
				npdForm.setIndicators(result);
			}
		}
		return mapping.findForward("forward");
	}

	private Set getIndicators(AmpTheme prog, boolean childrenToo) {
		Set indicators = new TreeSet();
		if (prog.getIndicators() != null) {
			indicators.addAll(prog.getIndicators());
		}
		if (childrenToo) {
			Collection children = ProgramUtil.getAllSubThemes(prog
					.getAmpThemeId());
			if (children != null) {
				for (Iterator iter = children.iterator(); iter.hasNext();) {
					AmpTheme chid = (AmpTheme) iter.next();
					Set subIndicators = getIndicators(chid, childrenToo);
					indicators.addAll(subIndicators);
				}
			}
		}
		return indicators;
	}

}
