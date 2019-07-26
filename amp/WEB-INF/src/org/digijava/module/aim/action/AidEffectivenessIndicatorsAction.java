package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.aim.form.AidEffectivenessIndicatorForm;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Struts action that encapsulates actions for effectiveness indicators:
 * view, add, delete, edit
 */
public class AidEffectivenessIndicatorsAction extends Action {

    private static Logger logger = Logger.getLogger(AidEffectivenessIndicatorsAction.class);
    public final static String MULTILINGUAL_AID_PREFIX = "multilingual_aid";

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
            clearForm(indicatorForm);
            executeSearch(request, indicatorForm);
            return mapping.findForward("list");
        }
        request.setAttribute(MULTILINGUAL_AID_PREFIX + "_tooltip", indicatorForm.buildMultilingualInputInstance("tooltipText"));
        request.setAttribute(MULTILINGUAL_AID_PREFIX + "_name", indicatorForm.buildMultilingualInputInstance("ampIndicatorName"));
        // String switches already work in java7
        switch(actionParam) {
            case "search" :
                executeSearch(request, indicatorForm);
                return mapping.findForward("search");
            case "list" :
                clearForm(indicatorForm);
                executeSearch(request, indicatorForm);
                return mapping.findForward("list");
            case "edit" :
                long indicatorId = 0;

                indicatorId = Long.parseLong(indicatorIdParam);
                indicator = AidEffectivenessIndicatorUtil.loadIndicatorById(indicatorId);

                if (indicator == null) {
                    handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.notExist", indicatorIdParam);
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
                long optionId = -1;
                int optionIndex = -1;
                try {
                    if (!"".equals(optionIdParam)) {
                        optionId = Long.parseLong(optionIdParam);
                    }
                    if (!"".equals(optionIndexParam)) {
                        optionIndex = Integer.parseInt(optionIndexParam);
                    }
                } catch (NumberFormatException nfe) {
                    handleLocalException(request, nfe, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIdParam);
                    return mapping.findForward("error");
                }
                if (optionId > 0) {
                    // deleting already saved option
                    try {
                        if (! AidEffectivenessIndicatorUtil.hasOptionActivities(optionId)) {
                            indicator = AidEffectivenessIndicatorUtil.deleteOption(optionId);
                            entityToForm(indicator, indicatorForm);
                        } else {
                            AmpAidEffectivenessIndicatorOption option = AidEffectivenessIndicatorUtil.loadOptionById(optionId);
                            if (option == null) {
                                handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIdParam);
                                executeSearch(request, indicatorForm);
                                return mapping.findForward("error");
                            }

                            handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.option.hasRelatedActivities",
                                    DgUtil.dehtmlize(option.getAmpIndicatorOptionName()));
                            executeSearch(request, indicatorForm);
                            return mapping.findForward("error");
                        }
                    } catch (RuntimeException rte) {
                        handleLocalException(request, rte, "error.admin.aidEffectivenessIndicator.options.option.notExist", optionIdParam);
                        executeSearch(request, indicatorForm);
                        return mapping.findForward("error");
                    }
                } else {
                    // deleting just added option from the form
                    try {
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
                    indicator = AidEffectivenessIndicatorUtil.loadIndicatorById(indicatorForm.getAmpIndicatorId());
                    if (indicator == null) {
                        handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.notExist", indicatorIdParam);
                        return mapping.findForward("error");
                    }
                    indicator = formToEntity(indicatorForm, indicator);
                } else { // create
                    indicator = formToEntity(indicatorForm, null);
                }
                
                if(ContentTranslationUtil.multilingualIsEnabled()) {
                    indicator.setTooltipText(indicatorForm.getTooltipText());  
                    indicator.setAmpIndicatorName(indicatorForm.getAmpIndicatorName());  
                } else {
                    String langCode = TLSUtils.getSite().getDefaultLanguage().getCode();
                    String tooltipParam = "AmpAidEffectivenessIndicator_tooltipText_" + langCode;
                    String tooltipText = MultilingualInputFieldValues.readParameter(
                            tooltipParam, "AmpAidEffectivenessIndicator_tooltipText", request).getRight();
                    indicator.setTooltipText(tooltipText);

                    String indicatorNameParam = "AmpAidEffectivenessIndicator_ampIndicatorName_" + langCode;
                    String indicatorName = MultilingualInputFieldValues.readParameter(
                            indicatorNameParam, "AmpAidEffectivenessIndicator_ampIndicatorName", request).getRight();
                    indicator.setAmpIndicatorName(indicatorName);
                }
                
                AidEffectivenessIndicatorUtil.saveIndicator(indicator);
                
                if (ContentTranslationUtil.multilingualIsEnabled()) {
                    MultilingualInputFieldValues.serialize(indicator, "tooltipText", null, null, request);
                    MultilingualInputFieldValues.serialize(indicator, "ampIndicatorName", null, null, request);
                }
                
                if (indicatorForm.getOldAmpIndicatorName() != null && ! "".equals(indicatorForm.getOldAmpIndicatorName())) {
                    AidEffectivenessIndicatorUtil.updateModulesVisibility(indicator, indicatorForm.getOldAmpIndicatorName());
                }
                
                // AMP-19893. Synch FM tree with the active/inactive flag
                if (indicator.getActive()) {
                    AidEffectivenessIndicatorUtil.createModulesVisibility(indicator.getFmName());
                } else {
                    AidEffectivenessIndicatorUtil.cleanUpModulesVisibility(indicator);
                }

                request.setAttribute("confirmationMessage", "saveSuccess");
                clearForm(indicatorForm);
                // display list of indicators after one has been deleted
                executeSearch(request, indicatorForm);

                return mapping.findForward("search");
            case "delete" :

                indicatorId = Long.parseLong(indicatorIdParam);
                indicator = AidEffectivenessIndicatorUtil.loadIndicatorById(indicatorId);

                if (indicator == null) {
                    handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.notExist", indicatorIdParam);
                    executeSearch(request, indicatorForm);
                    return mapping.findForward("search");
                }

                if (!AidEffectivenessIndicatorUtil.hasIndicatorActivities(indicatorId)) {
                    AidEffectivenessIndicatorUtil.deleteIndicator(indicatorId);
                }  else {
                    handleLocalException(request, null, "error.admin.aidEffectivenessIndicator.hasRelatedActivities",
                            DgUtil.dehtmlize(indicator.getAmpIndicatorName()));
                    executeSearch(request, indicatorForm);
                    return mapping.findForward("search");
                }

                // display list of indicators after one has been deleted
                executeSearch(request, indicatorForm);
                return mapping.findForward("search");
            default: //process "list"
                return mapping.findForward("list");

        }
    }

    private void executeSearch(HttpServletRequest request, AidEffectivenessIndicatorForm indicatorForm) {
        String indicatorName = indicatorForm.getAmpIndicatorName() == null ? "" :
                indicatorForm.getAmpIndicatorName().trim().toLowerCase();
        List<AmpAidEffectivenessIndicator> searchResult = AidEffectivenessIndicatorUtil.searchIndicators(
                indicatorForm.getIndicatorType(),
                indicatorName,
                indicatorForm.getActive());
        request.setAttribute("searchResult", searchResult);
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
            for (AmpAidEffectivenessIndicatorOption option : entity.getOptions()) {
                option.setIndicator(entity);
            }
        } else {
            for (int i = 0; i < form.getOptions().size() ; i++) {
                if (entity.getOptions().size() > i) {
                    entity.getOptions().get(i).setAmpIndicatorOptionName(form.getOptions().get(i).getAmpIndicatorOptionName());
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
        form.setOldAmpIndicatorName(entity.getFmName());
        form.setTooltipText(entity.getTooltipText());
        form.setMandatory(entity.getMandatory());
        form.setActive(entity.getActive());
        form.setIndicatorType(entity.getIndicatorType());

        form.setOptions(entity.getOptions());

        return form;
    }

    /**
     * Do not push this method to form clear
     * We are using session form and it will be cleared each time!!!
     */
    public void clearForm(AidEffectivenessIndicatorForm indicatorForm) {
        indicatorForm.setTooltipText("");
        indicatorForm.setAmpIndicatorName("");
        indicatorForm.setMandatory(false);
        indicatorForm.setActive(false);
        indicatorForm.setAmpIndicatorId(null);
        indicatorForm.setIndicatorType(-1);
        indicatorForm.setOptions(new ArrayList<AmpAidEffectivenessIndicatorOption>());
    }

    public ActionMessages validateData(AidEffectivenessIndicatorForm indicatorForm, HttpServletRequest request) {
        ActionMessages errors = new ActionMessages();
//        if (indicatorForm.getAmpIndicatorName() == null || "".equals(indicatorForm.getAmpIndicatorName())) {
//            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.indicatorName.required"));
//        }

        if (indicatorForm.getIndicatorType() == -1) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.indicatorType.required"));
        }

        Set<String> existingOptionNames = new HashSet<>();

        if (indicatorForm.getOptions() != null && indicatorForm.getOptions().size() > 0) {
            for (AmpAidEffectivenessIndicatorOption option : indicatorForm.getOptions()) {
                if (option.getAmpIndicatorOptionName() == null || "".equals(option.getAmpIndicatorOptionName())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.options.optionName.required"));
                    // do not repeat the error message
                    break;
                } else {
                    if (existingOptionNames.contains(option.getAmpIndicatorOptionName())) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.options.alreadyExists",
                                option.getAmpIndicatorOptionName()));
                        // do not repeat the error message
                        break;
                    } else {
                        existingOptionNames.add(option.getAmpIndicatorOptionName());
                    }
                }
            }
        } else {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.aidEffectivenessIndicator.atLeastOne"));
        }

        saveErrors(request, errors);
        return errors;
    }

    private void handleLocalException(HttpServletRequest request, Exception ex, String exceptionKey, Object param) {
        if (ex != null) {
            logger.error(ex.getStackTrace());
        }
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
