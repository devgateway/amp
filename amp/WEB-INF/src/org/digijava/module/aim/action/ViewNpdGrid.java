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
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * Displays NPD data grid.
 * @author Irakli Kobiashvili
 * @see IndicatorGridRow
 *
 */
public class ViewNpdGrid extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest reqest, HttpServletResponse sponse)
            throws Exception {
        NpdForm npdForm = (NpdForm) form;
        if (npdForm.getProgramId() != null) {
            //load theme
            AmpTheme mainProg = ProgramUtil.getThemeById(npdForm.getProgramId());
            //Retrieve theme indicators, and if second param true then all sub indicators.
            Set<IndicatorTheme> indicators = IndicatorUtil.getIndicators(mainProg, npdForm.getRecursive());
            //if there are indicators.
            if (indicators != null && indicators.size() > 0) {
                //convert set to list
                List<IndicatorTheme> indicatorsList = new ArrayList<IndicatorTheme>(indicators);
                //sort by indicator name.
                Collections.sort(indicatorsList,new IndicatorUtil.IndThemeIndciatorNameComparator());
                List<IndicatorGridRow> result = new ArrayList<IndicatorGridRow>(indicatorsList.size());
                //generate row objects from each connection for specified years.
                for (IndicatorTheme connection : indicatorsList) {
                    IndicatorGridRow row = new IndicatorGridRow(connection,npdForm.getSelYears());

                    if (npdForm.getSelIndicators() != null) {
                        for (Long selIndId : npdForm.getSelIndicators()){
                            if (selIndId.equals(row.getId())) {
                                result.add(row);
                                break;
                            }
                        }
                    }
                }
                npdForm.setIndicators(result);
            }
        }
        return mapping.findForward("forward");
    }


}
