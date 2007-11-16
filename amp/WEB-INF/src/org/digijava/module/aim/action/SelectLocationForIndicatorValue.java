package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.LocationUtil;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.apache.struts.util.LabelValueBean;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;

public class SelectLocationForIndicatorValue
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        String strIndex = request.getParameter("index");
        if(strIndex == null && themeForm.getParentIndex() == null) {
            return mapping.findForward("forward");
        } else if(strIndex != null) {
            themeForm.setParentIndex(Long.valueOf(strIndex));
        }

        if(themeForm.getAction() != null && themeForm.getAction().equalsIgnoreCase("add")) {
            AmpRegion location = LocationUtil.getAmpRegion(themeForm.getSelectedLocationId());

            AmpPrgIndicatorValue indValue = (AmpPrgIndicatorValue) themeForm.getPrgIndValues().get(themeForm.getParentIndex().intValue());
            indValue.setLocation(location);

            themeForm.setParentIndex(null);
            themeForm.setAction(null);
            themeForm.setSelectedLocationId(null);
            return mapping.findForward("forward");
        }

        Collection<AmpRegion> locations = LocationUtil.getAmpLocations();
        if(locations != null) {
            List<AmpRegion> locationsList = new ArrayList<AmpRegion>(locations);
            if(locationsList != null && themeForm.getKeyword() != null) {
                for(Iterator iter = locationsList.iterator(); iter.hasNext(); ) {
                    AmpRegion location = (AmpRegion) iter.next();
                    if(location.getName().indexOf(themeForm.getKeyword()) == -1) {
                        iter.remove();
                    }
                }
            }
            Collections.sort(locationsList, new LocationUtil.HelperAmpRegionNameComparator());
            themeForm.setLocationsCol(locationsList);
        }
        return mapping.findForward("forward");
    }

    public SelectLocationForIndicatorValue() {
    }
}
