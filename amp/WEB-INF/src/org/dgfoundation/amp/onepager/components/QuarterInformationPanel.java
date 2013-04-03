/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 04 Feb 2013
 */
public class QuarterInformationPanel extends AmpFieldPanel {
    private static final JavaScriptResourceReference JS_FILE = new JavaScriptResourceReference(QuarterInformationPanel.class, "QuarterInformationPanel.js");

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(JS_FILE));

        AmpFiscalCalendar fiscalCalendar = null;

        AmpAuthWebSession session = (AmpAuthWebSession) getSession();
        AmpTeamMember currentMember = session.getAmpCurrentMember();
        AmpApplicationSettings appSettings = DbUtil.getMemberAppSettings(currentMember.getAmpTeamMemId());
        if (appSettings != null){ //try the workspace fiscal calendar
            fiscalCalendar = appSettings.getFiscalCalendar();
        }
        if (fiscalCalendar == null){ //if not then use the global fiscal calendar
            String defaultCalendar = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
            Long fiscalCalId = Long.parseLong(defaultCalendar);
            fiscalCalendar = DbUtil.getFiscalCalendar(fiscalCalId);
        }

        String currentFiscalYear = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CURRENT_SYSTEM_YEAR);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, fiscalCalendar.getStartMonthNum() - 1);//January is 0
        cal.set(Calendar.DAY_OF_MONTH, fiscalCalendar.getStartDayNum());
        if (currentFiscalYear != null){
            Integer val = Integer.parseInt(currentFiscalYear);
            cal.set(Calendar.YEAR, val);
        }
        String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        pattern = pattern.replace('m', 'M');
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        String params = "";
        for (int i = 0; i < 4; i++){
            String quarter = formatter.format(cal.getTime()) + " - ";
            cal.add(Calendar.MONTH, 3);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            quarter += formatter.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);

            params += "'" + quarter + "'";
            if (i < 3)
                params += ", ";
        }

        response.render(JavaScriptReferenceHeaderItem.forScript("initializeQuarter(" + params + ");", "QuarterInformationPanelData"));
    }

    public QuarterInformationPanel(String id, String fmName) {
        super(id, fmName, true, false);
    }
}
