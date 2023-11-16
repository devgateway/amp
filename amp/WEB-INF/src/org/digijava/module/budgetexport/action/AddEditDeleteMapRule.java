package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.digijava.module.budgetexport.form.BEMapRuleForm;
import org.digijava.module.budgetexport.util.BudgetExportUtil;
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
    private static Logger logger    = Logger.getLogger(AddEditDeleteMapRule.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return edit(mapping, form, request, response);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapRuleForm beMapRuleForm = (BEMapRuleForm) form;
        List<AmpColumns> cols = DbUtil.getAvailableColumns();
        beMapRuleForm.setAvailColumns(cols);
        beMapRuleForm.setAvailRetrieverClasses(BudgetExportUtil.getAvailRetrievers());
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
        beMapRuleForm.setCsvColDelimiter(rule.getCsvColDelimiter());
        beMapRuleForm.setAllowAll(rule.isAllowAllItem());
        beMapRuleForm.setAllowNone(rule.isAllowNoneItem());
        beMapRuleForm.setDataRetrieverClass(rule.getDataRetrieverClass());
        beMapRuleForm.setAvailRetrieverClasses(BudgetExportUtil.getAvailRetrievers());


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
        ActionErrors errors = null;
        
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
        mapRule.setCsvColDelimiter(beMapRuleForm.getCsvColDelimiter());
        mapRule.setAllowAllItem(beMapRuleForm.isAllowAll());
        mapRule.setAllowNoneItem(beMapRuleForm.isAllowNone());
        mapRule.setDataRetrieverClass(beMapRuleForm.getDataRetrieverClass());


        if (DbUtil.ruleWithNameExists(beMapRuleForm.getCurProjectId(), mapRule.getName(), mapRule.getId())) {
            if (errors == null) errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.budgetExport.notUniqueRuleName"));
        }
        if (DbUtil.ruleWithViewExists(beMapRuleForm.getCurProjectId(), mapRule.getAmpColumn().getExtractorView(), mapRule.getId())) {
            if (errors == null) errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.budgetExport.notUniqueRuleView"));
        }

        ActionForward fwd = null;
        if (errors == null || errors.isEmpty()) {
            DbUtil.saveOrUpdateMapRule(mapRule);
            StringBuilder fwdBuilder = new StringBuilder("/");
            fwdBuilder.append(mapping.findForward("ruleList").getPath());
            fwdBuilder.append("?id=");
            fwdBuilder.append(beMapRuleForm.getCurProjectId());
            fwd = new ActionForward(fwdBuilder.toString());
            fwd.setRedirect(true);
        } else {
            saveErrors(request, errors);
            List<AmpColumns> cols = DbUtil.getAvailableColumns();
            beMapRuleForm.setAvailColumns(cols);
            fwd = mapping.findForward("forward");
        }
        return fwd;
    }
}
