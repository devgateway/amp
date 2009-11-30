package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;

/**
 *
 *Todo refactoring whole code using Dynamic location manager
 * but not today. This is a quick bug fix version.
 */
public class SelectLocationForIndicatorValue extends Action {

@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ThemeForm themeForm = (ThemeForm) form;
        String strIndex = request.getParameter("index");
        if (strIndex == null && themeForm.getParentIndex() == null) {
            return mapping.findForward("forward");
        } else if (strIndex != null) {
            themeForm.setParentIndex(Long.valueOf(strIndex));
        }

        AmpPrgIndicatorValue indValue = (AmpPrgIndicatorValue) themeForm.getPrgIndValues().get(themeForm.getParentIndex().intValue());

        Long[] id = themeForm.getUserSelectedLocs();
        AmpLocation ampLoc = null;
        if (id != null) {
            ampLoc = LocationUtil.getAmpLocationByCVLocation(id[0]);
            if (ampLoc == null) {
                AmpCategoryValueLocations selectedLoc = DynLocationManagerUtil.getLocation(id[0], false);
                ampLoc = new AmpLocation();
                ampLoc.setCountry(FeaturesUtil.getDefaultCountryIso());
                ampLoc.setRegionLocation(selectedLoc);
                ampLoc.setLocation(selectedLoc);
                LocationUtil.saveLocation(ampLoc);
            }
        }

        indValue.setLocation(ampLoc);
        themeForm.setLocationLevelIndex(-1);
        themeForm.setParentIndex(null);
        themeForm.setSelectedLocationId(null);
        themeForm.setLocation(null);
        themeForm.setLocationLevelIndex(null);
        themeForm.setAction(null);
        themeForm.setUserSelectedLocs(null);
        return mapping.findForward("backToAddDataPage");
    }

}