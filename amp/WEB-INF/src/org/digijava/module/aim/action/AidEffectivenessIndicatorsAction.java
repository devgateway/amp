package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.aim.form.AidEffectivenessIndicatorForm;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Struts action that incapsulates actions for effectiveness indicators:
 * view, add, delete, edit
 */
public class AidEffectivenessIndicatorsAction extends Action {

    private static Logger logger = Logger.getLogger(AidEffectivenessIndicatorsAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        AidEffectivenessIndicatorForm indicatorForm = (AidEffectivenessIndicatorForm)form;
        AmpAidEffectivenessIndicator indicator = null;
        String actionParam = request.getParameter("actionParam");
        String indicatorIdParam = request.getParameter("ampIndicatorId");
        String optionIdParam = request.getParameter("ampIndicatorOptionId");
        String optionIndexParam = request.getParameter("optionIndex");


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
                        indicatorForm.getActive());
                request.setAttribute("searchResult", searchResult);
                return mapping.findForward("search");
            case "list" :
                return mapping.findForward("list");
            case "edit" :
                long indicatorId = 0;
                try {
                    indicatorId = Long.parseLong(indicatorIdParam);
                    indicator = AidEffectivenessIndicatorUtil.loadById(indicatorId);
                } catch (RuntimeException nfe) {
                    handleLocalException(request, nfe, "error.admin.aidEffectivenessIndicator.notExist", indicatorIdParam);
                    return mapping.findForward("search");
                }

                entityToForm(indicator, indicatorForm);
                return mapping.findForward("edit");
            case "add" :
                clearForm(indicatorForm);
                return mapping.findForward("add");
            case "addOption" :
                AmpAidEffectivenessIndicatorOption fo = new AmpAidEffectivenessIndicatorOption();
                indicatorForm.getOptions().add(fo);
                return mapping.findForward("edit");
            case "deleteOption" :
                long optionId = 0;
                try {
                    optionId = Long.parseLong(optionIdParam);
                } catch (NumberFormatException nfe) {
                    handleLocalException(request, nfe, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIdParam);
                    return mapping.findForward("error");
                }
                if (optionId > 0) {
                    // deleting already saved option
                    try {
                        indicator = AidEffectivenessIndicatorUtil.deleteOption(optionId);
                        entityToForm(indicator, indicatorForm);
                    } catch (RuntimeException rte) {
                        handleLocalException(request, rte, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIdParam);
                        return mapping.findForward("error");
                    }
                } else {
                    // deleting just added option from the form
                    try {
                        int optionIndex = Integer.parseInt(optionIndexParam);
                        indicatorForm.getOptions().remove(optionIndex);
                    } catch (RuntimeException rte) {
                        handleLocalException(request, rte, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIndexParam);
                        return mapping.findForward("error");
                    }
                }

                return mapping.findForward("edit");
            case "save":
                if (validateData(indicatorForm, request).size() > 0) {
                    return mapping.findForward("error");
                }
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
                    AidEffectivenessIndicatorUtil.deleteIndicator(indicatorId);
                } catch (RuntimeException nfe) {
                    handleLocalException(request, nfe, "error.admin.aidEffectivenessIndicator.notExist", indicatorIdParam);
                    return mapping.findForward("search");
                }
                return mapping.findForward("search");
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
        entity.setMandatory(form.getMandatory());
        entity.setActive(form.getActive());
        entity.setIndicatorType(form.getIndicatorType());

        // todo consider use collection.retainAll
        if (entity.getOptions() == null) {
            entity.setOptions(form.getOptions());
        } else {
            for (int i = 0; i < form.getOptions().size() ; i++) {
                if (entity.getOptions().size() > i) {
                    entity.getOptions().get(i).setAmpIndicatorOptionName(form.getOptions().get(i).getAmpIndicatorOptionName());
                    entity.getOptions().get(i).setDefaultOption(form.getOptions().get(i).getDefaultOption());
                    entity.getOptions().get(i).setIndicator(entity);
                } else {
                    AmpAidEffectivenessIndicatorOption formOption = form.getOptions().get(i);
                    formOption.setIndicator(entity);
                    entity.getOptions().add(formOption);
                }
            }
        }


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
        form.setMandatory(entity.getMandatory());
        form.setActive(entity.getActive());
        form.setIndicatorType(entity.getIndicatorType());



        /*
        if (entity.getOptions() != null) {
            form.setOptions(new ArrayList<AmpAidEffectivenessIndicatorOption>());
            for (AmpAidEffectivenessIndicatorOption eo : entity.getOptions()) {
                AmpAidEffectivenessIndicatorOption fo = new AmpAidEffectivenessIndicatorOption();
                fo.setDefaultOption(eo.getDefaultOption());
                fo.setAmpIndicatorOptionName(eo.getAmpIndicatorOptionName());
                fo.setAmpIndicatorOptionId(eo.getAmpIndicatorOptionId());
                form.getOptions().add(fo);
            }
        }*/
        form.setOptions(entity.getOptions());

        return form;
    }

    /**
     * Do not push this method to form clear
     * We are using session form and it will be cleared each time!!!
     */
    public void clearForm(AidEffectivenessIndicatorForm indicatorForm) {
        indicatorForm.setTooltipText(null);
        indicatorForm.setAmpIndicatorName(null);
        indicatorForm.setMandatory(false);
        indicatorForm.setActive(false);
        indicatorForm.setAmpIndicatorId(null);
        indicatorForm.setIndicatorType(-1);
        indicatorForm.setOptions(new ArrayList<AmpAidEffectivenessIndicatorOption>());
    }

    public ActionMessages validateData(AidEffectivenessIndicatorForm indicatorForm, HttpServletRequest request) {
        ActionMessages errors = new ActionMessages();
        if (indicatorForm.getAmpIndicatorName() == null || "".equals(indicatorForm.getAmpIndicatorName())) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.indicatorName.required"));
        }

        if (indicatorForm.getIndicatorType() == -1) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.indicatorType"));
        }

        int numberOfDefaultOptions = 0;
        if (indicatorForm.getOptions() != null && indicatorForm.getOptions().size() > 0) {
            for (AmpAidEffectivenessIndicatorOption option : indicatorForm.getOptions()) {
                if (option.getAmpIndicatorOptionName() == null || "".equals(option.getAmpIndicatorOptionName())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.options.optionName.required"));
                    // do not repeat the error message
                    break;
                }
            }

            for (AmpAidEffectivenessIndicatorOption option : indicatorForm.getOptions()) {
                if (option.getDefaultOption()) {
                    numberOfDefaultOptions++;
                }
            }
        } else {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.atLeastOne"));
        }

        if (numberOfDefaultOptions != 1) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.options.oneDefault"));
        }

        saveErrors(request, errors);
        return errors;
    }

    private void handleLocalException(HttpServletRequest request, Exception ex, String exceptionKey, Object param) {
        logger.error(ex.getStackTrace());
        ActionMessages errors = new ActionMessages();
        ActionMessage message = null;
        if (param != null) {
            message = new ActionMessage(exceptionKey, param);
        } else {
            message = new ActionMessage(exceptionKey);
        }
        errors.add(ActionMessages.GLOBAL_MESSAGE, message);
        saveErrors(request, errors);
    }

}
