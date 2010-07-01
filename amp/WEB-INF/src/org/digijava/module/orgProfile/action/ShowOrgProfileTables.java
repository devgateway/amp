package org.digijava.module.orgProfile.action;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.util.SectorUtil;

import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.orgProfile.form.OrgProfileNameValueYearForm;
import org.digijava.module.orgProfile.helper.NameValueYearHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil.NameValueYearHelperComparatorByName;
import org.digijava.module.widget.util.WidgetUtil;

public class ShowOrgProfileTables extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileNameValueYearForm orgForm = (OrgProfileNameValueYearForm) form;
        HttpSession session = request.getSession();
        FilterHelper filter = (FilterHelper) session.getAttribute("orgProfileFilter");
        Long sectorClassConfigId = orgForm.getSectorClassConfigId();
        if (sectorClassConfigId == null || sectorClassConfigId == 0) {
            sectorClassConfigId = SectorUtil.getPrimaryConfigClassification().getId();
        }
        List<NameValueYearHelper> helpers = OrgProfileUtil.getData(filter, orgForm.getType(), sectorClassConfigId);
        if (orgForm.getType() == WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN || orgForm.getType() == WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN) {
            String langCode = RequestUtils.getNavigationLanguage(request).getCode();
            Locale locale = new Locale(langCode);
            Collator collator = Collator.getInstance(locale);
            collator.setStrength(Collator.PRIMARY);
            collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
            NameValueYearHelperComparatorByName comparator = new NameValueYearHelperComparatorByName(collator);
           Collections.sort(helpers, comparator);
        }
        orgForm.setValues(helpers);
        return mapping.findForward("forward");

    }
}
