package org.digijava.module.aim.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.form.ViewAhSurveyFormulasForm;
import org.digijava.module.aim.util.DbUtil;

public class ViewAhSurveyFormulas extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {
        ViewAhSurveyFormulasForm svform = (ViewAhSurveyFormulasForm) form;

        AmpAhsurveyIndicator sv = null;
        if (svform.getIndId() == null) {
            return mapping.findForward("forward");
        }

        sv = DbUtil.getIndicatorById(svform.getIndId());
        if (sv == null) {
            return mapping.findForward("forward");
        }

        svform.setIndCode(sv.getIndicatorCode());

        if (svform.getAction()!=null && svform.getAction().equals("save")) {
            sv = DbUtil.getIndicatorById(svform.getIndId());

            Set calcFormulas = sv.getCalcFormulas();

            AmpAhsurveyIndicatorCalcFormula selFormula = getSurvyByColumnIndex(calcFormulas);
            if (selFormula == null) {
                if (calcFormulas == null || calcFormulas.isEmpty()) {
                    calcFormulas = new HashSet();
                }
                selFormula = new AmpAhsurveyIndicatorCalcFormula();
            }

            selFormula.setParentIndicator(sv);
            selFormula.setBaseLineValue(svform.getBaseLineValue());
            selFormula.setCalcFormula(svform.getFormulaText());
            selFormula.setColumnIndex(svform.getSelectedColumnIndex());
            selFormula.setConstantName(svform.getConstantName());
            selFormula.setTargetValue(svform.getTargetValue());
            selFormula.setEnabled(svform.isFormulaEnabled());

            setSurvyByColumnIndex(calcFormulas, selFormula);

            sv.setCalcFormulas(calcFormulas);
            DbUtil.updateIndicator(sv);

            svform.setAction(null);
        }

        if (svform.getSelectedColumnIndex() == null || svform.getSelectedColumnIndex().equals( -1)) {
            svform.setSelectedColumnIndex(new Long(0));
        }

        Set calcFormulas = sv.getCalcFormulas();

        AmpAhsurveyIndicatorCalcFormula selFormula = getSurvyByColumnIndex(calcFormulas);
        if (selFormula == null) {
            if (calcFormulas == null || calcFormulas.isEmpty()) {
                calcFormulas = new HashSet();
            }

            selFormula = new AmpAhsurveyIndicatorCalcFormula();
            selFormula.setColumnIndex(svform.getSelectedColumnIndex());
            selFormula.setParentIndicator(sv);
            calcFormulas.add(selFormula);
            sv.setCalcFormulas(calcFormulas);
            DbUtil.updateIndicator(sv);
        }

        svform.setConstantName(selFormula.getConstantName());
        svform.setColumnIndex(selFormula.getColumnIndex());
        svform.setFormulaId(selFormula.getId());
        svform.setFormulaText(selFormula.getCalcFormula());
        svform.setBaseLineValue(selFormula.getBaseLineValue());
        svform.setTargetValue(selFormula.getTargetValue());
        if(selFormula.getEnabled()==null){
            selFormula.setEnabled(false);
        }
        svform.setFormulaEnabled(selFormula.getEnabled());

        return mapping.findForward("forward");
    }

    private void setSurvyByColumnIndex(Set set, AmpAhsurveyIndicatorCalcFormula survey) {
        if (set != null && !set.isEmpty()) {
            set.clear();
            set.add(survey);
        }
    }

    private AmpAhsurveyIndicatorCalcFormula getSurvyByColumnIndex(Set set) {
        AmpAhsurveyIndicatorCalcFormula retSurvey = null;
        if (set != null && !set.isEmpty()) {
            Iterator itr=set.iterator();
            retSurvey=(AmpAhsurveyIndicatorCalcFormula)itr.next();
        }
        return retSurvey;
    }
}
