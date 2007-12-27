package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.DbUtil;
import java.util.Collection;
import java.util.*;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpAhsurvey;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SelectOrganisationForAhsurvey extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        EditActivityForm eaform = (EditActivityForm) form;

        if (isEmpty(eaform.getSvAction())) {
            return mapping.findForward("forward");
        } else {
            eaform.setSvAction(eaform.getSvAction().toLowerCase());
        }

        Collection col = new ArrayList();
        if (eaform.getSvAction().equals("search")) {
            Collection orgCol = DbUtil.getBilMulOrganisations();
            if (!isEmpty(eaform.getKeyword())) {
                for (Iterator orgIter = orgCol.iterator(); orgIter.hasNext(); ) {
                    AmpOrganisation org = (AmpOrganisation) orgIter.next();
                    if (org.getName().toLowerCase().indexOf(eaform.getKeyword()) > -1) {
                        col.add(org);
                    }
                }
            } else {
                col = orgCol;
            }
            eaform.setPagedCol(col);
        } else if (eaform.getSvAction().equals("sel")) {
            AmpAhsurvey ahs=eaform.getAhsurvey();
            AmpOrganisation selOrg = DbUtil.getOrganisation(eaform.getSurveyOrgId());
            ahs.setPointOfDeliveryDonor(selOrg);
            eaform.setAhsurvey(ahs);

            eaform.setSvAction(null);
            return mapping.findForward("finish");
        }

        eaform.setSvAction(null);
        return mapping.findForward("forward");
    }

    private boolean isEmpty(String str) {
        if (str != null && !str.equals("")) {
            return false;
        }
        return true;
    }

    public SelectOrganisationForAhsurvey() {
    }
}
