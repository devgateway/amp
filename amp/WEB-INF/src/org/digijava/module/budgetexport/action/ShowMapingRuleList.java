package org.digijava.module.budgetexport.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
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

        //Set total/mapped counts  (not persistent field values)
        for (AmpBudgetExportMapRule ruleItem : projectRules) {
            MappingEntityAdapter adapter = MappingEntityAdapterUtil.getEntityAdapter(ruleItem.getAmpColumn().getExtractorView());
            ruleItem.setTotalAmpEntityCount(adapter.getObjectCount());
        }
        
        beMappingForm.setRules(projectRules);
        
        AmpBudgetExportProject proj = DbUtil.getProjectById(beMappingForm.getId());
        beMappingForm.setAmpReportId(proj.getAmpReportId() );
        
        return mapping.findForward("forward");
    }
}
