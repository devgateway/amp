
package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.form.ShowParisIndicatorTableForm;
import org.digijava.module.widget.util.ParisIndicatorTableWidgetUtil;


public class ShowParisIndicatorTableWidget extends Action {
     private static Logger logger = Logger.getLogger(ShowSectorTable.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ShowParisIndicatorTableForm tableForm = (ShowParisIndicatorTableForm) form;
        Collection<AmpOrgGroup> donors = DbUtil.getBilMulOrgGroups();
        tableForm.setDonorGroups(new ArrayList<AmpOrgGroup>(donors));
        AmpParisIndicatorTableWidget table=ParisIndicatorTableWidgetUtil.getAmpParisIndicatorTableWidget(tableForm.getWidgetId());
        tableForm.setParisIndicators(table.getParisIndicators());
        return mapping.findForward("forward");
    }

}
