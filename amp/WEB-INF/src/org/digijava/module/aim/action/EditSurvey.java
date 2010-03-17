/*
 * Created on 9/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Survey;
import org.digijava.module.aim.helper.Indicator;
import org.digijava.module.aim.helper.Question;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class EditSurvey extends Action {

    private static Logger logger = Logger.getLogger(EditSurvey.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        if (request.getSession().getAttribute("currentMember") == null) {
            return mapping.findForward("index");
        }

        TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");

        EditActivityForm svForm = (EditActivityForm) form;

        final int NUM_RECORDS = 5;
        long surveyId = 0;
        boolean firstLoad = false;
        
        if(svForm.getAmpAhsurveys() == null) {
        	svForm.setAmpAhsurveys(new HashSet<AmpAhsurvey>());
        }
        if(svForm.getSurveys() == null) {
        	svForm.setSurveys(new HashSet<Survey>());
        }

        try {
            String strSurvey = request.getParameter("surveyId");
            if (null != strSurvey && strSurvey.trim().length() > 0) {
                surveyId = Long.parseLong(strSurvey);
                if (svForm.getSurvey() == null || null == svForm.getSurvey().getAmpSurveyId() || surveyId != svForm.getSurvey().getAmpSurveyId().longValue()) {
                    //svForm.getSurvey().setAmpSurveyId(new Long(surveyId));
                    firstLoad = true;
                }
            }
        } catch (NumberFormatException ex) {
            svForm.getSurvey().setAmpSurveyId(null);
        }

        AmpAhsurvey auxAmpAhsurvey = null;
        Survey auxSurvey = null;
        
        svForm.setEditAct(false);
        //firstLoad = true;
        
        if (svForm.isEditAct() == true) {
	        	if (firstLoad) {
		            auxAmpAhsurvey = getAmpAhsurvey(surveyId, svForm.getAmpAhsurveys());
		            if (auxAmpAhsurvey == null) {
		            	auxAmpAhsurvey = DbUtil.getAhSurvey(surveyId);
		            	svForm.getAmpAhsurveys().add(auxAmpAhsurvey);
		            }
		            auxSurvey = getSurvey(surveyId, svForm.getSurveys());
		            if(auxSurvey == null) {
		            	auxSurvey = svForm.new Survey();
		            	svForm.setSurvey(auxSurvey);
		            	auxSurvey.setIndicators(DbUtil.getResposesBySurvey(surveyId, svForm.getActivityId()));
		            	auxSurvey.setAmpSurveyId(surveyId);
		            	svForm.getSurvey().setAhsurvey(auxAmpAhsurvey);
			            svForm.getSurvey().setFundingOrg(auxAmpAhsurvey.getAmpDonorOrgId().getAcronym());
		            	
		            	svForm.getSurveys().add(auxSurvey);
		            } else {
		            	svForm.setSurvey(auxSurvey);
		            }
		
		            svForm.getSurvey().setPageColl(new ArrayList());
		            int numPages = svForm.getSurvey().getIndicators().size() / NUM_RECORDS;
		            numPages += (svForm.getSurvey().getIndicators().size() % NUM_RECORDS != 0) ? 1 : 0;
		            if (numPages > 1) {
		                for (int i = 0; i < numPages; i++)
		                    svForm.getSurvey().getPageColl().add(new Integer(i + 1));
		            }
	        	}
        } else { //This is a new activity.
        	boolean edited = false;
        	if (firstLoad == true) {
        		auxAmpAhsurvey = getAmpAhsurvey(surveyId, svForm.getAmpAhsurveys());
	            if (auxAmpAhsurvey == null) {
	            	// Added for activity versioning.
	            	auxAmpAhsurvey = DbUtil.getAhSurvey(surveyId);
	            	if (auxAmpAhsurvey == null
							|| (auxAmpAhsurvey.getAmpDonorOrgId() == null && auxAmpAhsurvey.getPointOfDeliveryDonor() == null)) {
			        	auxAmpAhsurvey = new AmpAhsurvey();
			        	auxAmpAhsurvey.setAmpActivityId(null);
			        	auxAmpAhsurvey.setAmpAHSurveyId(new Long(0));
			        	//aca deberia recibir de la pagina otro parametro con el id de la organizacion.
			        	String orgID = request.getParameter("orgId");
			        	Long organizationId = new Long(orgID);
			        	auxAmpAhsurvey.setAmpDonorOrgId(DbUtil.getOrganisation(organizationId));
			        	auxAmpAhsurvey.setPointOfDeliveryDonor(DbUtil.getOrganisation(organizationId));
			        	auxAmpAhsurvey.setResponses(new HashSet<AmpAhsurveyResponse>());
	            	} else {
	            		edited = true;
	            	}
		        	svForm.getAmpAhsurveys().add(auxAmpAhsurvey);
	            }
	        	
	            auxSurvey = getSurvey(surveyId, svForm.getSurveys());
	            if(auxSurvey == null) {
	            	auxSurvey = svForm.new Survey();
	            	auxSurvey.setAmpSurveyId(new Long(surveyId));
	            	svForm.setSurvey(auxSurvey);
	            	
	            	if(edited == false) {
		        		List indicators = new ArrayList<Indicator>();
		        		Collection<AmpAhsurveyIndicator> ampAhSurveyIndicators = DbUtil.getAllAhSurveyIndicators();      	
		        		Iterator<AmpAhsurveyIndicator> iterAhSurveyIndicators = ampAhSurveyIndicators.iterator();
			        	while (iterAhSurveyIndicators.hasNext()) {
			        		AmpAhsurveyIndicator auxAmpAhSurveyIndicator = iterAhSurveyIndicators.next();
			        		//AMP doesn't calculate the 8th indicator, but we need in the result matrix
	                    	if(auxAmpAhSurveyIndicator.getIndicatorCode().equals("8")){
	                        	continue;
	                    	}
			        		Indicator auxIndicator = new Indicator();
			        		auxIndicator.setIndicatorCode(auxAmpAhSurveyIndicator.getIndicatorCode());
			        		auxIndicator.setName(auxAmpAhSurveyIndicator.getName());
			        		auxIndicator.setQuestion(new ArrayList<Question>());
			        		Iterator<AmpAhsurveyQuestion> iterQuestions = auxAmpAhSurveyIndicator.getQuestions().iterator();
			        		while (iterQuestions.hasNext()) {
			        			AmpAhsurveyQuestion auxAhSurveyQuestion = iterQuestions.next();
			        			Question auxQuestion = new Question();
			        			auxQuestion.setQuestionId(auxAhSurveyQuestion.getAmpQuestionId());
			        			auxQuestion.setQuestionText(auxAhSurveyQuestion.getQuestionText());
			        			auxQuestion.setQuestionType(auxAhSurveyQuestion.getAmpTypeId().getName());
			        			auxQuestion.setResponse("");
			        			auxQuestion.setResponseId(null);
			        			auxIndicator.getQuestion().add(auxQuestion);
			        		}
			        		indicators.add(auxIndicator);
			        	}
			        	svForm.getSurvey().setIndicators(new ArrayList<Indicator>(indicators));        	
			        	svForm.getSurvey().setAhsurvey(auxAmpAhsurvey);
			            svForm.getSurvey().setFundingOrg(auxAmpAhsurvey.getAmpDonorOrgId().getAcronym());
		        	} else {
		        		auxSurvey.setIndicators(DbUtil.getResposesBySurvey(surveyId, svForm.getActivityId()));
		            	auxSurvey.setAmpSurveyId(surveyId);
		            	svForm.getSurvey().setAhsurvey(auxAmpAhsurvey);
			            svForm.getSurvey().setFundingOrg(auxAmpAhsurvey.getAmpDonorOrgId().getAcronym());
		        	}
		            
		            svForm.getSurveys().add(auxSurvey);
	            } else {
	            	svForm.setSurvey(auxSurvey);
	            }
	            
	            svForm.getSurvey().setPageColl(new ArrayList());
	            int numPages = svForm.getSurvey().getIndicators().size() / NUM_RECORDS;
	            numPages += (svForm.getSurvey().getIndicators().size() % NUM_RECORDS != 0) ? 1 : 0;
	            if (numPages > 1) {
	                for (int i = 0; i < numPages; i++)
	                    svForm.getSurvey().getPageColl().add(new Integer(i + 1));
	            }
        	}
        }
        
        int page = 0;
        if (request.getParameter("page") == null || request.getParameter("page").trim().length() < 1) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException nex) {
                logger.debug("incorrect page in request scope : " + nex.getMessage());
            }
        }
        svForm.getSurvey().setCurrPage(new Integer(page));
        svForm.getSurvey().setStartIndex(new Integer(NUM_RECORDS * (page - 1)));

        return mapping.findForward("forward");
    }
    
    /**
     * Look for the AmpAHsurvey object in the collection through the ID.
     * @param id
     * @param surveys
     * @return
     */
    private static AmpAhsurvey getAmpAhsurvey(Long id, Set<AmpAhsurvey> surveys) {
		AmpAhsurvey ret = null;
		Iterator<AmpAhsurvey> iterSurvey = surveys.iterator();
		while (iterSurvey.hasNext()) {
			AmpAhsurvey aux = iterSurvey.next();
			if (aux.getAmpAHSurveyId() != null && aux.getAmpAHSurveyId().equals(id)) {
				ret = aux;
			}
		}
		return ret;
	}
    
    public static Survey getSurvey(Long id, Set<Survey> surveys) {
    	Survey ret = null;
    	Iterator<Survey> iterSurveys = surveys.iterator();
    	while (iterSurveys.hasNext()) {
    		Survey aux = iterSurveys.next();
    		if (aux.getAmpSurveyId().equals(id)) {
    			ret = aux;
    		}
    	}
    	return ret;
    }
}
