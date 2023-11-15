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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created on 01/05/06
 * @author Govind G Dalwani
 */

public class ParisIndicatorAdd extends Action 
{
    private static Logger logger = Logger.getLogger(ParisIndicatorAdd.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception 
    {
        ParisIndicatorManagerForm parisForm = (ParisIndicatorManagerForm) form;

        String editquestion = request.getParameter("editquestion");
        
        if(editquestion!=null)
        {
            AmpAhsurveyQuestion surveyQuestion = new AmpAhsurveyQuestion();     
            surveyQuestion.setQuestionText(parisForm.getPiQuestionGot());           
            surveyQuestion.setQuestionNumber(parisForm.getPiQuestId());         
            AmpAhsurveyIndicator surveyInd = new AmpAhsurveyIndicator();
            surveyInd.setAmpIndicatorId(parisForm.getPiQuestionIndicatorId());      
            surveyQuestion.setAmpIndicatorId(surveyInd);            
            AmpAhsurveyQuestionType surveyQuestType = new AmpAhsurveyQuestionType();
            surveyQuestType.setAmpTypeId(parisForm.getPiQuestTypeId());
            surveyQuestion.setAmpTypeId(surveyQuestType);
            surveyQuestion.setStatus(null);
            

            if(editquestion.equalsIgnoreCase("false"))
            {
                surveyQuestion.setAmpQuestionId(parisForm.getPiQuestionId());
                DbUtil.update(surveyQuestion);
            }
            if(editquestion.equalsIgnoreCase("true"))
            {
                DbUtil.add(surveyQuestion);
            }
        }
        String create = request.getParameter("create");
        logger.info("this is create..............."+create);
        if(create!=null &&create.equals("indi"))
        {
            logger.info("in the edit indicator goin to save now....");
            AmpAhsurveyIndicator surveyInd = new AmpAhsurveyIndicator();
            surveyInd.setAmpIndicatorId(parisForm.getIndicatorvalue());
            surveyInd.setName(parisForm.getAddNewIndicatorText());
            surveyInd.setIndicatorCode(parisForm.getAddNewIndicatorCode());
            surveyInd.setTotalQuestions(parisForm.getNumberOfQuestions());
            surveyInd.setIndicatorNumber(parisForm.getIndicatorNumber());
            DbUtil.update(surveyInd);
            //parisForm.setParisIndicatorName(ParisUtil.getParisIndicators());
            return mapping.findForward("parisIndiManager");
        }
            
        
        return mapping.findForward("parisIndiManager");
    }
}
