package org.digijava.module.orgProfile.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.form.OrgProfilePIForm;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;

/**
 *
 * @author medea
 */
public class OrgProfileParisIndicatorAction extends Action {

    private static Logger logger = Logger.getLogger(OrgProfileParisIndicatorAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OrgProfilePIForm piForm = (OrgProfilePIForm) form;
        Collection<AmpAhsurveyIndicator> indicators = DbUtil.getAllAhSurveyIndicators();
        HttpSession session = request.getSession();
        FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
        Iterator<AmpAhsurveyIndicator> iter = indicators.iterator();
        List<ParisIndicatorHelper> indicatorHelpers = new ArrayList<ParisIndicatorHelper>();
        while (iter.hasNext()) {
            AmpAhsurveyIndicator piIndicator = iter.next();
            // currently the rules are undefined for indicator 10b
            if (piIndicator.getIndicatorCode().equals("10b")) {
                continue;
            }
            ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter);
            indicatorHelpers.add(piHelper);

            /* we should add indicator 5aii and indicator 5bii,
            these indicators don't exist in db so we add them manually*/

            if (piIndicator.getIndicatorCode().equals("5a")) {
                AmpAhsurveyIndicator ind5aii = new AmpAhsurveyIndicator();
                ind5aii.setIndicatorCode("5aii");
                ind5aii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
                ind5aii.setName("Number of donors using country PFM");
                ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(ind5aii, filter);
                indicatorHelpers.add(piInd5aHelper);
            }
            if (piIndicator.getIndicatorCode().equals("5b")) {
                AmpAhsurveyIndicator ind5bii = new AmpAhsurveyIndicator();
                ind5bii.setIndicatorCode("5bii");
                ind5bii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
                ind5bii.setName("Number of donors using country procurement system");
                ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(ind5bii, filter);
                indicatorHelpers.add(piInd5bHelper);
            }


        }
        piForm.setFiscalYear(filter.getYear());
        String name = "";
        if (filter.getOrgIds() != null) {
            if (filter.getOrgIds().length == 1) {
                name = filter.getOrganization().getName();
            } else {
                name = "Multiple Organizations Selected";
            }
        } else {
            if (filter.getOrgGroupId() != -1) {
                name = filter.getOrgGroup().getOrgGrpName();
            } else {
                name = "All";
            }
        }
        piForm.setName(name);
        piForm.setIndicators(indicatorHelpers);
        return mapping.findForward("forward");
    }
}
