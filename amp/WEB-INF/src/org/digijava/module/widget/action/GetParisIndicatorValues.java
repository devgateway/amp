package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.widget.dbentity.AmpParisIndicatorBaseTargetValues;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.util.ParisIndicatorTableWidgetUtil;

public class GetParisIndicatorValues extends Action {

    public static final String ROOT_TAG = "ParisIndicatorValuesList";
    public static final String INDICATOR_TAG = "Indicator";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String widgetId = request.getParameter("widgetId");
        String donorGroupId = request.getParameter("donorGroupId");
        String fiscalCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);

        xml += "<" + ROOT_TAG + ">";
        try {
            AmpParisIndicatorTableWidget widget = ParisIndicatorTableWidgetUtil.getAmpParisIndicatorTableWidget(Long.parseLong(widgetId));
            List<AmpParisIndicatorBaseTargetValues> parisIndicators = new ArrayList(widget.getParisIndicators());
            Long year = widget.getDonorGroupYear();
            AmpCurrency curr=CurrencyUtil.getAmpcurrency("USD");
            FilterHelper filter = new FilterHelper(Long.parseLong(donorGroupId), year, Long.parseLong(fiscalCalendarId));
            filter.setCurrId(curr.getAmpCurrencyId());
            Iterator<AmpParisIndicatorBaseTargetValues> indicatorIter = parisIndicators.iterator();
            while (indicatorIter.hasNext()) {
                AmpParisIndicatorBaseTargetValues indicatorRow = indicatorIter.next();
                AmpAhsurveyIndicator piIndicator =indicatorRow.getParisIndicator();
                ParisIndicatorHelper piHelper = new ParisIndicatorHelper(piIndicator, filter);
            	if (piIndicator.getIndicatorCode().equals("10b")) {
					TeamMember member = filter.getTeamMember();
					Long teamId=null;
					if(member!=null){
						teamId=member.getTeamId();
					}
					boolean fromPublicView=filter.getFromPublicView();
					List<NodeWrapper> nodeWrappers=OrgProfileUtil.getNodeWrappers(request, teamId, fromPublicView);
					piHelper.setNodesWrappers(nodeWrappers);
				}
                xml += "<" + INDICATOR_TAG + " ";
                xml += " id=\"" + "donorGroup_" + widgetId + "_" + indicatorRow.getParisIndicator().getAmpIndicatorId() + "\" ";
                String value=piHelper.getOrgValue()+"";
                if(!indicatorRow.getParisIndicator().getIndicatorCode().equals("6")){
                    value+="%";
                }
                xml += " value=\"" + value + "\" ";
                if (donorGroupId.equals("-1")) {
                    xml += " empty=\"" + true + "\" ";
                } else {
                    xml += " empty=\"" + false + "\" ";
                }
                xml += "/>";


            }


            xml += "</" + ROOT_TAG + "> ";
            response.getWriter().print(xml);
        } catch (Exception e) {
            // TODO handle this exception
            e.printStackTrace();
        }


        return null;
    }
}
