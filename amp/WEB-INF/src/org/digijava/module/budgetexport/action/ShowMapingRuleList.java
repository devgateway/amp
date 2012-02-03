package org.digijava.module.budgetexport.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.form.BEMappingForm;
import org.digijava.module.budgetexport.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: flyer
 * Date: 2/3/12
 * Time: 3:48 PM
 */
public class ShowMapingRuleList extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMappingForm beMappingForm = (BEMappingForm) form;
        List<AmpBudgetExportMapRule> projectRules =
                DbUtil.getProjectMappingRules(beMappingForm.getId());
        beMappingForm.setRules(projectRules);
        return mapping.findForward("forward");
    }
}
