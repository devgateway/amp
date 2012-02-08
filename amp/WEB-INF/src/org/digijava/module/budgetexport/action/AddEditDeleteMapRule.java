package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.digijava.module.budgetexport.form.BEMapRuleForm;
import org.digijava.module.budgetexport.form.BEProjectForm;
import org.digijava.module.budgetexport.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 1:08 PM
 */
public class AddEditDeleteMapRule extends DispatchAction {
    private static Logger logger	= Logger.getLogger(AddEditDeleteMapRule.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return edit(mapping, form, request, response);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapRuleForm beMapRuleForm = (BEMapRuleForm) form;
        List<AmpColumns> cols = DbUtil.getAvailableColumns();
        beMapRuleForm.setAvailColumns(cols);
        return mapping.findForward("forward");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapRuleForm beMapRuleForm = (BEMapRuleForm) form;
        List<AmpColumns> cols = DbUtil.getAvailableColumns();
        beMapRuleForm.setAvailColumns(cols);

        AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapRuleForm.getId());
        beMapRuleForm.setAmpColumnId(rule.getAmpColumn().getColumnId());
        beMapRuleForm.setName(rule.getName());
        beMapRuleForm.setHeader(rule.isHeader());


        return mapping.findForward("forward");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapRuleForm beMapRuleForm = (BEMapRuleForm) form;
        DbUtil.deleteRuleById(beMapRuleForm.getId());



        StringBuilder fwdBuilder = new StringBuilder("/");
        fwdBuilder.append(mapping.findForward("ruleList").getPath());
        fwdBuilder.append("?id=");
        fwdBuilder.append(beMapRuleForm.getCurProjectId());
        ActionForward fwd = new ActionForward(fwdBuilder.toString());
        fwd.setRedirect(true);
        return fwd;
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapRuleForm beMapRuleForm = (BEMapRuleForm) form;

        AmpBudgetExportMapRule mapRule = null;
        //New
        if (beMapRuleForm.getId() == null || beMapRuleForm.getId().longValue() <= 0l) {
            mapRule = new AmpBudgetExportMapRule();
            AmpBudgetExportProject prj = DbUtil.getProjectById(beMapRuleForm.getCurProjectId());
            mapRule.setProject(prj);
        } else {
            mapRule = DbUtil.getMapRuleById(beMapRuleForm.getId());
        }

        if (mapRule.getAmpColumn() == null || !mapRule.getAmpColumn().getColumnId().equals(beMapRuleForm.getAmpColumnId())) {
            AmpColumns col = DbUtil.getAmpColumnById(beMapRuleForm.getAmpColumnId());
                    mapRule.setAmpColumn(col);
        }

        mapRule.setName(beMapRuleForm.getName());
        mapRule.setHeader(beMapRuleForm.isHeader());

        DbUtil.saveOrUpdateMapRule(mapRule);

        StringBuilder fwdBuilder = new StringBuilder("/");
        fwdBuilder.append(mapping.findForward("ruleList").getPath());
        fwdBuilder.append("?id=");
        fwdBuilder.append(beMapRuleForm.getCurProjectId());
        ActionForward fwd = new ActionForward(fwdBuilder.toString());
        fwd.setRedirect(true);
        return fwd;
    }
}
