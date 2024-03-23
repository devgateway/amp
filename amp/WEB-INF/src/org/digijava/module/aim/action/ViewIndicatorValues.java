package org.digijava.module.aim.action;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.form.ViewIndicatorForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;

public class ViewIndicatorValues extends TilesAction {

    private static Logger logger = Logger.getLogger(ViewIndicatorValues.class);

    public ActionForward execute(ComponentContext ctx,ActionMapping mapping,
            ActionForm form,HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ViewIndicatorForm viForm = (ViewIndicatorForm) form;
        viForm.setIndicators(new ArrayList<ActivityIndicator>());
        String indParam = request.getParameter("ind");
        Long ind = indParam != null ? Long.parseLong(indParam) : null;
        String risk = request.getParameter("risk");
        AmpActivityVersion  activity = ActivityUtil.loadActivity(viForm.getAmpActivityId());
        Collection<IndicatorActivity> indicators=activity.getIndicators();
        if (indicators!=null){
            for (IndicatorActivity connection : indicators) {
                if (ind != null){
                    if (connection.getIndicator().getIndicatorId().equals(ind)){
                        // :( because have no time to change JSP let's use old helper bean.
                        ActivityIndicator bean = IndicatorUtil.createIndicatorHelperBean(connection);

                        //add indicator helper bean to form
                        viForm.getIndicators().add(bean);
                    }
                }else if (risk!=null){
                    AmpIndicatorRiskRatings riskValue=IndicatorUtil.getRisk(connection);

                    if (riskValue != null) {
                        if (riskValue.getRatingName().equalsIgnoreCase(risk)) {
                            ActivityIndicator bean = IndicatorUtil.createIndicatorHelperBean(connection);
                            viForm.getIndicators().add(bean);

                        }

                    }
                }
            }

        }

        return null;
    }

}
