package org.digijava.module.orgProfile.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;
import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;

/**
 *
 * @author medea
 */
public class GetZones extends Action {

    private static Logger logger = Logger.getLogger(GetZones.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        OrgProfileFilterForm orgForm = (OrgProfileFilterForm) form;
        Long regionId = orgForm.getSelRegionId();
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();
        try {
            if (regionId != null && regionId != -1) {
                AmpCategoryValueLocations region = LocationUtil.getAmpCategoryValueLocationById(regionId);
                if (region.getChildLocations() != null) {
                    zones.addAll(region.getChildLocations());

                }

            }
            orgForm.setZones(zones);
            response.setContentType("text/xml");

            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            PrintWriter out = new PrintWriter(outputStream, true);
            logger.debug("generating drop down...");
            out.println(generateZoneDropDown(zones));
            out.close();
            outputStream.close();
        } catch (Exception e) {
            // TODO handle this exception
            logger.error("unable to load organizations", e);
        }


        return null;
    }

    private static String generateZoneDropDown(List<AmpCategoryValueLocations> zones) {

        String zoneSelect = "<select name=\"selZoneIds\" class=\"selectDropDown\" id=\"zone_dropdown_id\"  multiple=\"true\" size=\"8\">";
        Iterator<AmpCategoryValueLocations> zoneIter = zones.iterator();
        if (zones != null && !zones.isEmpty()) {
            zoneSelect += "<option value=\"-1\">All</option>";
        }
        while (zoneIter.hasNext()) {
            AmpCategoryValueLocations zone = zoneIter.next();
            zoneSelect += "<option value=\"" + zone.getId() + "\">" + DbUtil.filter(zone.getName()) + "</option>";
        }
        zoneSelect += "</select>";
        return zoneSelect;

    }
}
