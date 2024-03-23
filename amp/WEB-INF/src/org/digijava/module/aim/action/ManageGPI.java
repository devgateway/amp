package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.GPIDefaultFilters;
import org.digijava.module.aim.dbentity.GPISetup;
import org.digijava.module.aim.form.ManageGPIForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.GPISetupUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

public class ManageGPI extends DispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        ManageGPIForm gpiForm = (ManageGPIForm) form;

        Map<String, String> measures = GPISetup.getListOfValues();
        gpiForm.setMeasures(measures);

        // If there is a previous setup then populate the values in the form.
        // Notice the table amp_gpi_setup should never have more than one
        // record!!! (at least for now maybe we change the approach in the future).
        GPISetup setup = GPISetupUtil.getSetup();
        if (setup != null) {
            gpiForm.setIndicator5aActualDisbursement(setup.getIndicator5aActualDisbursement());
            gpiForm.setIndicator5aPlannedDisbursement(setup.getIndicator5aPlannedDisbursement());
            gpiForm.setIndicator6ScheduledDisbursements(setup.getIndicator6ScheduledDisbursements());
            gpiForm.setIndicator9bDisbursements(setup.getIndicator9bDisbursements());
        }
        
        // Populate the list of available indicators.
        gpiForm.setIndicators(DbUtil.getAllGPISurveyIndicators(false));
        
        // Populate the list of available donor types.
        gpiForm.setAvailableDonorTypes(DbUtil.getAllOrgTypesOfPortfolio());
        // Find previously saved donor types.
        gpiForm.setSelectedDonorTypes(GPISetupUtil.getSavedFilters(GPIDefaultFilters.GPI_DEFAULT_FILTER_ORG_GROUP));

        return mapping.findForward("forward");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ManageGPIForm gpiForm = (ManageGPIForm) form;
        GPISetup setup = GPISetupUtil.getSetup();
        if (setup == null) {
            setup = new GPISetup();
        }
        if (!"".equals(gpiForm.getIndicator5aActualDisbursement())) {
            setup.setIndicator5aActualDisbursement(gpiForm.getIndicator5aActualDisbursement());
        }
        if (!"".equals(gpiForm.getIndicator5aPlannedDisbursement())) {
            setup.setIndicator5aPlannedDisbursement(gpiForm.getIndicator5aPlannedDisbursement());
        }
        if (!"".equals(gpiForm.getIndicator6ScheduledDisbursements())) {
            setup.setIndicator6ScheduledDisbursements(gpiForm.getIndicator6ScheduledDisbursements());
        }
        if (!"".equals(gpiForm.getIndicator9bDisbursements())) {
            setup.setIndicator9bDisbursements(gpiForm.getIndicator9bDisbursements());
        }
        GPISetupUtil.saveGPISetup(setup);
        
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement().toString();
            if (paramName.startsWith("indicator_")) {
                String auxId = paramName.substring(10);
                GPISetupUtil.saveDescription(new Long(auxId), request.getParameter(paramName));
            }
        }
        
        GPISetupUtil.cleanupSavedDonorTypes();
        params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement().toString();
            if (paramName.startsWith("donorTypes_")) {
                String auxId = paramName.substring(11);
                GPISetupUtil.saveDonorType(new Long(auxId));
            }
        }
        
        return mapping.findForward("save");
    }
}
