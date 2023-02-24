package org.digijava.module.aim.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.form.ActivitiesForm;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FilteredAmpTheme;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.ProgramUtil;

public class GetProgramIndicators extends Action {
    private static Logger logger = Logger.getLogger(GetProgramIndicators.class);
    public static final String ROOT_TAG = "indicatorsList";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.debug("got asynchronous request for indicators list");

        response.setContentType("text/xml");
        NpdForm npdForm = (NpdForm) form;

        OutputStreamWriter outputStream = new OutputStreamWriter(
                response.getOutputStream(), "UTF-8");
        PrintWriter out = new PrintWriter(outputStream, true);

        try {

            Long programId = npdForm.getProgramId();
            AmpTheme currentTheme = ProgramUtil.getThemeById(programId);
            Set<IndicatorTheme> indicators = currentTheme.getIndicators();
            // if there are indicators
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            xml += "<" + ROOT_TAG + ">";
            if (indicators != null && indicators.size() > 0) {
                // convert to list
                SortedSet<IndicatorTheme> sortedIndicators = new TreeSet<IndicatorTheme>(
                        new IndicatorUtil.IndThemeIndciatorNameComparator());
                // sort
                sortedIndicators.addAll(indicators);
                for (IndicatorTheme ind : sortedIndicators) {
                    AmpIndicator indicator = ind.getIndicator();
                    xml += "<"
                            + "indicator name=\""
                            + org.digijava.module.aim.util.DbUtil
                                    .filter(indicator.getName()) + "\" ";
                    xml += " id=\"" + indicator.getIndicatorId() + "\" />";
                }
            }
                xml += "</" + ROOT_TAG + ">";
                out.println(xml);
                // outputStream.write(xml.getBytes());
                out.close();
                // return xml
                logger.debug("closing and returning response XML of NPD Activities");
                outputStream.close();
            
        } catch (Exception e) {
            logger.info(e.getMessage(), e);

        }

        return null;

    }

}
