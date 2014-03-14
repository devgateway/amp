package org.digijava.module.aim.action;

import java.util.*;

import org.apache.struts.action.*;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.util.LocationUtil.HelperLocationAncestorLocationNamesAsc;

public class RemoveSelLocations extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

				EditActivityForm eaForm = (EditActivityForm) form;

				Long selLocs[] = eaForm.getLocation().getSelLocs();
				Collection prevSelLocs = eaForm.getLocation().getSelectedLocs(); 
				List<Location> locs = new ArrayList<Location>();

				Iterator itr = prevSelLocs.iterator();
                //For regional funding check
                Map <Long, String> regFndIdNameMap = null;
                StringBuffer regFundingReferencedRegions = null;
                if (eaForm.getFunding().getRegionalFundings() != null &&
                        !eaForm.getFunding().getRegionalFundings().isEmpty()) {
                    regFndIdNameMap = new HashMap <Long, String>();

                    for (Object regFndObj : eaForm.getFunding().getRegionalFundings()) {
                        RegionalFunding regFnd = (RegionalFunding) regFndObj;
                        regFndIdNameMap.put(regFnd.getRegionId(), regFnd.getRegionName());
                    }
                }

				while (itr.hasNext()) {
                  boolean flag = false;
                  Location loc = (Location) itr.next();
                      for (int i = 0;i < selLocs.length;i ++) {
                          Long regId = null;
                          if (regFndIdNameMap != null) {
                            regId = new Long(selLocs[i]);    
                          }

                        if (loc.getLocId().equals(selLocs[i]) && (regFndIdNameMap == null || !regFndIdNameMap.containsKey(regId))) {
                            flag = true;
                            break;
                        } else if (loc.getLocId().equals(selLocs[i]) && (regFndIdNameMap != null && regFndIdNameMap.containsKey(regId))) {
                          if (regFundingReferencedRegions == null) {
                              regFundingReferencedRegions = new StringBuffer();
                          } else {
                            regFundingReferencedRegions.append(", ");
                          }
                          regFundingReferencedRegions.append(regFndIdNameMap.get(regId));
                          flag = false;
                          break;
                        }
                      }
                    if (!flag) {
                             locs.add(loc);
                    }

				}

        if (regFundingReferencedRegions != null) {
            eaForm.clearMessages();
            eaForm.addError("error.aim.addActivity.locationsUsedInRegFunding",regFundingReferencedRegions.toString());
        }
        String langCode = RequestUtils.getNavigationLanguage(request).getCode();
 	Collections.sort(locs,new HelperLocationAncestorLocationNamesAsc(langCode));
        eaForm.getLocation().setSelectedLocs(locs);
        eaForm.getLocation().setSelLocs(null);
        eaForm.getLocation().setCols(null);
        eaForm.getLocation().setNumResults(0);

        return mapping.findForward("forward");
    }
}

