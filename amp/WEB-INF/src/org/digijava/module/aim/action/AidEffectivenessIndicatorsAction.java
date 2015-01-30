package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.form.AidEffectivenessIndicatorForm;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Struts action that incapsulates actions for effectiveness indicators:
 * view, add, delete, edit
 */
public class AidEffectivenessIndicatorsAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        AidEffectivenessIndicatorForm indicatorForm = (AidEffectivenessIndicatorForm)form;
        String actionParam = request.getParameter("actionParam");

        if (actionParam == null) {
            // the default one is "list"
            return mapping.findForward("list");
        }

        // String switches already work in java7
        switch(actionParam) {
            case "search" :
                List<AmpAidEffectivenessIndicator> searchResult = AidEffectivenessIndicatorUtil.searchIndicators(
                        indicatorForm.getIndicatorType(),
                        indicatorForm.getAmpIndicatorName(),
                        indicatorForm.isActive());
                request.setAttribute("searchResult", searchResult);
                return mapping.findForward("search");
            case "list" :;
                return mapping.findForward("list");
            case "edit" :;
                return mapping.findForward("edit");
            case "add" :;
                return mapping.findForward("add");
            case "save": ;
                return mapping.findForward("save");
            case "delete" : ;
                return mapping.findForward("delete");
            default: //process "list"
                return mapping.findForward("list");

        }
    }
}
