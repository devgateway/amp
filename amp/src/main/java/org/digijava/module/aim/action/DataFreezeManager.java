package org.digijava.module.aim.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.form.DataFreezeManagerForm;
import org.digijava.module.aim.util.DbUtil;

public class DataFreezeManager extends Action {
    private final static String CANCEL = "CANCEL";
    private final static String SAVE = "SAVE";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        DataFreezeManagerForm dataFreezeManagerForm = (DataFreezeManagerForm) form;

        List<AmpDataFreezeSettings> ampDataFreezeSettingsList = (List<AmpDataFreezeSettings>) DbUtil
                .getAll(AmpDataFreezeSettings.class);
        AmpDataFreezeSettings settings = ampDataFreezeSettingsList.isEmpty() ? new AmpDataFreezeSettings()
                : ampDataFreezeSettingsList.get(0);

        if (dataFreezeManagerForm.getAction() != null && dataFreezeManagerForm.getAction().equals(CANCEL)) {
            return mapping.findForward("index");
        } else {
            if (dataFreezeManagerForm.getAction() != null && dataFreezeManagerForm.getAction().equals(SAVE)) {
                settings.setEnabled(dataFreezeManagerForm.getEnabled());
                settings.setGracePeriod(dataFreezeManagerForm.getGracePeriod());
                DbUtil.saveOrUpdateObject(settings);
                return mapping.findForward("index");
            } else {
                if (!ampDataFreezeSettingsList.isEmpty()) {
                    dataFreezeManagerForm.setEnabled(settings.getEnabled());
                    dataFreezeManagerForm.setGracePeriod(settings.getGracePeriod());
                }

                return mapping.findForward("forward");
            }

        }
    }
}
