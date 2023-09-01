/*
 * Created on 9/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.form.EditSurveyForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class GetSurvey extends TilesAction {

    private static Logger logger = Logger.getLogger(GetSurvey.class);

    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        EditSurveyForm svForm = (EditSurveyForm) form;

        final int NUM_RECORDS = 5;
        long surveyId = 0;
        boolean flag = false;

        try {
            String strSurvey = request.getParameter("surveyId");
            if (null != strSurvey && strSurvey.trim().length() > 0) {
                surveyId = Long.parseLong(strSurvey);
                if (svForm.getAmpSurveyId()==null || surveyId != svForm.getAmpSurveyId().longValue()) {
                    svForm.setAmpSurveyId(new Long(surveyId));
                    flag = true;
                }
            }
        } catch (NumberFormatException ex) {
            svForm.setAmpSurveyId(null);
        }

        if (svForm.getAmpSurveyId()!=null) {
            svForm.setIndicators(DbUtil.getResposesBySurvey(svForm.getAmpSurveyId(), svForm.getAmpActivityId()));
            svForm.setReset(Boolean.FALSE);
            AmpAhsurvey survey = DbUtil.getAhSurvey(svForm.getAmpSurveyId());
            svForm.setFundingOrg(survey.getAmpDonorOrgId().getAcronym());
            svForm.setDeliveryDonor(survey.getPointOfDeliveryDonor().getName());

            svForm.setPages(new ArrayList());
            int numPages = svForm.getIndicators().size() / NUM_RECORDS;
            numPages += (svForm.getIndicators().size() % NUM_RECORDS != 0) ? 1 : 0;
            if (numPages > 1) {
                for (int i = 0; i < numPages; i++)
                    svForm.getPages().add(new Integer(i + 1));
            }
        }

        int page = 0;
        if (request.getParameter("page") == null) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException nex) {
                logger.debug("incorrect page in request scope : " + nex.getMessage());
            }
        }
        svForm.setCurrentPage(new Integer(page));
        svForm.setOffset(new Integer(NUM_RECORDS * (page - 1)));
        logger.debug("currentPage : " + svForm.getCurrentPage());
        logger.debug("offset : " + svForm.getOffset());

        return null;
    }
}
