package org.digijava.module.aim.action ;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.form.ParisIndicatorManagerForm;
import org.digijava.module.aim.util.ParisUtil;

/**
 * created on 01/05/06
 * @author Govind G Dalwani
 */

public class ParisIndicatorManager extends Action 
{
    private static Logger logger = Logger.getLogger(ParisIndicatorManager.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception 
    {
        ParisIndicatorManagerForm parisForm = (ParisIndicatorManagerForm) form;

        parisForm.setParisIndicatorName(ParisUtil.getParisIndicators());
        logger.info("in the manager.java so be happy");
        
        /*
         * this is to add a new Paris Indicator
         * getparameter parisIndicator is got from the JSP
         */
        Long Indicid = null ;
        String parisIndicator =request.getParameter("parisIndicator");
        if(parisIndicator!=null && parisIndicator.equals("add"))
        {
            AmpAhsurveyIndicator surveyInd = new AmpAhsurveyIndicator();
            surveyInd.setIndicatorNumber(null);
            surveyInd.setName(null);
            parisForm.setAddNewIndicatorText(null);
            parisForm.setAddNewIndicatorCode(null);
            parisForm.setPiQuestionGot(null);
            parisForm.setPiQuestId(null);
            AmpAhsurveyQuestion surveyQuestion = new AmpAhsurveyQuestion();
            surveyQuestion.setQuestionText(null);
            surveyQuestion.setQuestionNumber(null);
            surveyQuestion.setAmpTypeId(null);
            /*
             surveyInd.setIndicatorNumber(a1);      
        surveyInd.setName(parisForm.getAddNewIndicatorText());      
        Integer a2 = new Integer(1); 
        surveyInd.setTotalQuestions(a2);        
        surveyInd.setStatus(null);
        DbUtil.add(surveyInd);
        AmpAhsurveyQuestion surveyQuestion = new AmpAhsurveyQuestion();
        
        surveyQuestion.setQuestionText(parisForm.getPiQuestionGot());
        surveyQuestion.setQuestionNumber(parisForm.getPiQuestId());
        surveyQuestion.setAmpIndicatorId(ahSurveyInd);
        AmpAhsurveyQuestionType surveyQuestType = new AmpAhsurveyQuestionType();
        surveyQuestType.setAmpTypeId(parisForm.getPiQuestTypeId());
        surveyQuestion.setAmpTypeId(surveyQuestType);
        surveyQuestion.setStatus(null);
             */
            
            return mapping.findForward("parisAddIndicator");
        }
            
        /* 
         * this is to add the question
         * create is got from the JSP
         */
        String create = request.getParameter("create");
        logger.info("this is create( parisIndicatorManager action )..............."+create);
        if(create!=null && create.equals("new"))
        {
            //logger.info("pid.. : "+parisForm.getIndicatorvalue());
            
            parisForm.setPiQuestId(null);
            parisForm.setPiQuestionGot(null);
            parisForm.setPiQuestTypeId(null);
            parisForm.setPiQuestionIndicatorId(parisForm.getIndicatorvalue());
            return mapping.findForward("parisAddQuest");
        }
        /*
         * this is to edit the question
         */
        if(create !=null && create.equals("edit"))
        {
            
            String quid2 = request.getParameter("qid2");
            Long a = new Long(quid2);
            Collection editQuest = ParisUtil.getParisQuestionToBeEdited(a);
            Iterator itr = editQuest.iterator();
            while(itr.hasNext())
            {
                AmpAhsurveyQuestion ampAhSurveyQuestion = (AmpAhsurveyQuestion) itr.next();
                parisForm.setPiQuestionId(ampAhSurveyQuestion.getAmpQuestionId());
                parisForm.setPiQuestionGot(ampAhSurveyQuestion.getQuestionText());
                parisForm.setPiQuestionIndicatorId(ampAhSurveyQuestion.getAmpIndicatorId().getAmpIndicatorId());
                parisForm.setPiQuestId(ampAhSurveyQuestion.getQuestionNumber());
                parisForm.setPiQuestTypeId(ampAhSurveyQuestion.getAmpTypeId().getAmpTypeId());
                
            }
            return mapping.findForward("parisEditQuest");
        }
        
        
        if(create!=null && create.equals("delete"))
        {
            String quid2 = request.getParameter("qid2");
            Long questId = new Long(quid2);
            ParisUtil.deleteQuestion(questId);
        }

        String action = request.getParameter("event");
        if (action != null && action.equals("delete")) 
        {
            return mapping.findForward(("parisIndiDelete"));
        }
        if (action != null && action.equals("edit"))
        {
            String a = (String)(request.getParameter("pid"));
            Integer t = new Integer(a);
            Indicid = new Long(a);
            parisForm.setIndicatorvalue(Indicid);
            Collection quest = ParisUtil.getParisQuestions(t);
            parisForm.setFormQuestion(quest);
            parisForm.setFormQuestId(quest);
            
            /*for indicator*/
            Collection indicatorValue =ParisUtil.getParisIndicatorToBeEdited(t);
            Iterator itr1 = indicatorValue.iterator();
            while(itr1.hasNext())
            {
                AmpAhsurveyIndicator ampAhSurveyIndicator = (AmpAhsurveyIndicator) itr1.next();
                parisForm.setAddNewIndicatorCode(ampAhSurveyIndicator.getIndicatorCode());
                parisForm.setAddNewIndicatorText(ampAhSurveyIndicator.getName());
                parisForm.setIndicatorNumber(ampAhSurveyIndicator.getIndicatorNumber());
                parisForm.setNumberOfQuestions(ampAhSurveyIndicator.getTotalQuestions());
                
            }
            return mapping.findForward(("parisIndiEdit"));
        }
        /*this is to update the Indicator editIndicator is parameter passed 
          from parisIndicator Edit */
    

        
        
        return mapping.findForward("parisIndiManager");
    }
}
        
