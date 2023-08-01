package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestionType;
import org.digijava.module.aim.form.ParisIndicatorManagerForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ParisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created on 01/05/06
 * @author Govind G Dalwani
 */

public class ParisIndicatorAddNew extends Action 
{
    private static Logger logger = Logger.getLogger(ParisIndicatorAddNew.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception 
    {
        ParisIndicatorManagerForm parisForm = (ParisIndicatorManagerForm) form;
        AmpAhsurveyIndicator surveyInd = new AmpAhsurveyIndicator();
        surveyInd.setIndicatorCode(parisForm.getAddNewIndicatorCode());
        Integer a1 = new Integer(15);
        surveyInd.setIndicatorNumber(a1);
        surveyInd.setName(parisForm.getAddNewIndicatorText());      
        Integer a2 = new Integer(1); 
        surveyInd.setTotalQuestions(a2);        
        surveyInd.setStatus(null);
        DbUtil.add(surveyInd);

        
        
        AmpAhsurveyIndicator ahSurveyInd = ParisUtil.findIndicatorId(parisForm.getAddNewIndicatorText(),parisForm.getAddNewIndicatorCode());
        AmpAhsurveyQuestion surveyQuestion = new AmpAhsurveyQuestion();
        
        surveyQuestion.setQuestionText(parisForm.getPiQuestionGot());
        surveyQuestion.setQuestionNumber(parisForm.getPiQuestId());
        surveyQuestion.setAmpIndicatorId(ahSurveyInd);
        AmpAhsurveyQuestionType surveyQuestType = new AmpAhsurveyQuestionType();
        surveyQuestType.setAmpTypeId(parisForm.getPiQuestTypeId());
        surveyQuestion.setAmpTypeId(surveyQuestType);
        surveyQuestion.setStatus(null);
        
        DbUtil.add(surveyQuestion);

        return mapping.findForward("parisIndiManager");
    }
}
