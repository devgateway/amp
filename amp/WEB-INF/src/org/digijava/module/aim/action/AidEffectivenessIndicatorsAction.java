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
        AmpAidEffectivenessIndicator indicator = null;
        String actionParam = request.getParameter("actionParam");
        String indicatorIdParam = request.getParameter("ampIndicatorId");

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
            case "list" :
                return mapping.findForward("list");
            case "edit" :
                long indicatorId = 0;
                try {
                    indicatorId = Long.parseLong(indicatorIdParam);
                } catch (NumberFormatException nfe) {
                    // todo handleMe
                }

                indicator = AidEffectivenessIndicatorUtil.loadById(indicatorId);
                entityToForm(indicator, indicatorForm);
                return mapping.findForward("edit");
            case "add" :
                return mapping.findForward("add");
            case "save":
                // update
                if (indicatorForm.getAmpIndicatorId() != null && indicatorForm.getAmpIndicatorId() > 0){
                    indicator = AidEffectivenessIndicatorUtil.loadById(indicatorForm.getAmpIndicatorId());
                    indicator = formToEntity(indicatorForm, indicator);
                } else { // create
                    indicator = formToEntity(indicatorForm, null);
                }
                AidEffectivenessIndicatorUtil.saveIndicator(indicator);
                return mapping.findForward("search");
            case "delete" :
                indicatorId = 0;
                try {
                    indicatorId = Long.parseLong(indicatorIdParam);
                } catch (NumberFormatException nfe) {
                    // todo handleMe
                }
                AidEffectivenessIndicatorUtil.deleteIndicator(indicatorId);
                return mapping.findForward("list");
            default: //process "list"
                return mapping.findForward("list");

        }
    }

    /**
     * We can use BeanUtils.copy instead, but there are not so many fields here
     * @param form
     * @param entity
     * @return
     */
    private AmpAidEffectivenessIndicator formToEntity(
            AidEffectivenessIndicatorForm form,
            AmpAidEffectivenessIndicator entity) {
        if (entity == null) {
            entity = new AmpAidEffectivenessIndicator();
        }

        entity.setAmpIndicatorName(form.getAmpIndicatorName());
        entity.setTooltipText(form.getTooltipText());
        entity.setMandatory(form.isMandatory());
        entity.setActive(form.isActive());
        entity.setIndicatorType(form.getIndicatorType());

        // todo add options copying

        return entity;
    }

    /**
     * We can use BeanUtils.copy instead, but there are not so many fields here
     * @param entity
     * @param form
     * @return
     */
    private AidEffectivenessIndicatorForm entityToForm(
            AmpAidEffectivenessIndicator entity,
            AidEffectivenessIndicatorForm form) {

        form.setAmpIndicatorName(entity.getAmpIndicatorName());
        form.setTooltipText(entity.getTooltipText());
        form.setMandatory(entity.isMandatory());
        form.setActive(entity.isActive());
        form.setIndicatorType(entity.getIndicatorType());

        // todo copy options

        return form;
    }

}
