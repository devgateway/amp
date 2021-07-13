/**
 * ViewSelectActivityTabs.java
 *
 * @author mouhamad
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

public class ViewSelectActivityTabs extends Action {

    private static Logger logger = Logger
            .getLogger(ViewSelectActivityTabs.class);

    private ServletContext ampContext = null;

    // {{"FM type", "FM tabs name", "mapping forward"}}
    // respects tab order in mainProjectDetails.jsp
    private static String[][] fmTabs = {
            { "field", "Channel Overview Tab", "channelOverview" },
            { "field", "References Tab", "references" },
            { "field", "Financial Progress Tab", "financialProgress" },
            { "field", "Funding Organizations Tab", "physicalProgress" },
            { "module", "Document", "documents" },
            { "field", "Regional Funding Tab", "regionalFunding" },
            { "field", "Paris Survey", "parisSurvey" },
            { "feature", "Activity Dashboard", "activityDashboard" },
            { "field", "Contracting Tab", "contracting" },
            { "feature", "Regional Observations", "regionalObservations" }};

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //
        ActionForward actionForward = null;
        HttpSession session = request.getSession();
        ampContext = getServlet().getServletContext();

        String urlParams = "&";
        String paramName = "";
        for (Enumeration enumeration = request.getParameterNames(); enumeration
                .hasMoreElements();) {
            paramName = (String) enumeration.nextElement();
            if(urlParams.equals("&"))
                urlParams += paramName + "=" + request.getParameter(paramName);
            else
                urlParams += "&" + paramName + "=" + request.getParameter(paramName);

        }
        synchronized (ampContext) {
            //
            AmpTreeVisibility ampTreeVisibility =FeaturesUtil.getAmpTreeVisibility(ampContext, session);
            //
            String type = "";
            String name = "";
            String forward = "";
            AmpFieldsVisibility fieldsVisibility = null;
            AmpFeaturesVisibility featuresVisibility = null;
            AmpModulesVisibility modulesVisibility = null;
            boolean isVisible = false;
            for (int i = 0; ((i < fmTabs.length) && (!isVisible)); i++) {
                type = fmTabs[i][0];
                name = fmTabs[i][1];
                forward = fmTabs[i][2];
                //
                if (type.equals("field")) {
                    fieldsVisibility = ampTreeVisibility
                            .getFieldByNameFromRoot(name);
                    if (fieldsVisibility != null) {
                        isVisible = fieldsVisibility
                                .isVisibleTemplateObj(ampTreeVisibility
                                        .getRoot().getTemplate());
                    }
                } else if (type.equals("feature")) {
                    featuresVisibility = ampTreeVisibility
                            .getFeatureByNameFromRoot(name);
                    if (featuresVisibility != null) {
                        isVisible = featuresVisibility
                                .isVisibleTemplateObj(ampTreeVisibility
                                        .getRoot().getTemplate());
                    }
                } else if (type.equals("module")) {
                    modulesVisibility = ampTreeVisibility
                            .getModuleByNameFromRoot(name);
                    if (modulesVisibility != null) {
                        isVisible = modulesVisibility
                                .isVisibleTemplateObj((AmpTemplatesVisibility)ampTreeVisibility
                                        .getRoot());
                    }
                }
                if (isVisible) {
                    actionForward = mapping.findForward(forward);
                }
            }
        }
        //
        return new ActionForward(actionForward.getPath() + urlParams);
    }
}
