package org.digijava.module.esrigis.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.ActivityPoint;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.QueryUtil;
import org.digijava.module.esrigis.helpers.SimpleLocation;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.util.DbUtil;

public class DataDispacher extends MultiAction{
	private static Logger logger = Logger.getLogger(DataDispacher.class);
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm)form;
			MapFilter filter = maphelperform.getFilter(); 
			if (filter == null || !filter.isIsinitialized()){
				maphelperform.setFilter(QueryUtil.getNewFilter());
				maphelperform.getFilter().setWorkspaceOnly(true);
			}
			response.setContentType("text/json");
		return modeSelect(mapping, maphelperform, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
			if (request.getParameter("showactivities") != null){
				return modeShowActivities(mapping, form, request, response);
			}
			else if (request.getParameter("showhighlights") != null){
				return modeShowHighlights(mapping, form, request, response);
			}
		return null;
	}
	
	public ActionForward modeShowActivities(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm)form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		
		JSONArray jsonArray = new JSONArray();
		 List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		 list = DbHelper.getActivities(maphelperform.getFilter());
		 Boolean isaggregatable=true;
		 for (Iterator<AmpActivityVersion> iterator = list.iterator(); iterator.hasNext();) {
			 ActivityPoint ap = new ActivityPoint();
			 AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			 ap.setActivityname(aA.getName());
			 
			ArrayList<SimpleLocation> sla = new ArrayList<SimpleLocation>();
			for (Iterator iterator2 =aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				if (!alocation.getLocation().getLocation().getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey())){
					isaggregatable=true;
					String lat=alocation.getLocation().getLocation().getGsLat();
					String lon=alocation.getLocation().getLocation().getGsLong();
					SimpleLocation sl = new SimpleLocation();
					sl.setName(alocation.getLocation().getLocation().getName());
					sl.setGeoId(alocation.getLocation().getLocation().getGeoCode());
					sl.setLat(lat);
					sl.setLon(lon);
					if ("".equalsIgnoreCase(lat) && "".equalsIgnoreCase(lon)){
						sl.setIslocated(false);
					}else{
						sl.setIslocated(true);
					}
					sl.setPercentage(alocation.getLocationPercentage().toString());
					sla.add(sl);
				}else{
					isaggregatable = false;
					break;
				}
			}
				if (isaggregatable){
					ap.setLocations(sla);
					jsonArray.add(ap);
				}
			}
	
		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();
		return null;
	}
	
	public ActionForward modeShowHighlights(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm)form;
		/* 
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		MapFilter filter = maphelperform.getFilter();
		filter.setTeamMember(tm);
		
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue() - filter.getYearsInRange());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());

        //TODO: Move this to a helper, see how to make Filters compatible and use just one class to access database with Visualization

		JSONArray jsonArray = new JSONArray();

		//Get list of locations
        //List<AmpCategoryValueLocations> locations = DbHelper.getRegions(filter);
        //Iterator<AmpCategoryValueLocations> locationsIt = locations.iterator();
        
       while (locationsIt.hasNext()) {
            AmpCategoryValueLocations location = locationsIt.next();

            Long[] ids = {location.getId()};
			MapFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelLocationIds(ids);
            DecimalWraper fundingCal = DbHelper.getFunding(newFilter, startDate, endDate, null, null, Constants.DISBURSEMENT, Constants.ACTUAL);
            BigDecimal amount = fundingCal.getValue().setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
            String keyName = "";
            String implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey();
            if (location.getParentCategoryValue().getValue().equals(implLocation)) {
                keyName = "National";
            } else {
                Long zoneIds[] = filter.getZoneIds();
                if (zoneIds != null && zoneIds.length > 0 && zoneIds[0] != -1) {
                    implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_REGION.getValueKey();
                    if (location.getParentCategoryValue().getValue().equals(implLocation)) {
                        keyName = "Regional";
                    } else {
                        AmpCategoryValueLocations parent = LocationUtil.getTopAncestor(location, implLocation);
                        keyName = parent.getName();
                    }
                } else {
                    AmpCategoryValueLocations parent = LocationUtil.getTopAncestor(location, implLocation);
                    keyName = parent.getName();
                }

            }
            SimpleLocation locationJSON = new SimpleLocation();
            locationJSON.setName(keyName);
            locationJSON.setCommitments(amount.toPlainString());
            jsonArray.add(locationJSON);
        }

        PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();	*/
		return null;
	}

	public ActionForward getJSONObject(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		
		String parentId = request.getParameter("parentId");
		String objectType = request.getParameter("objectType");

        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();

	    //Gets children object according to objectType
		
		if(parentId != null && objectType != null && (objectType.equals("Organization") || objectType.equals("Organizations")))
		{
			//Get list of sub organizations
	        Long orgGroupId = Long.parseLong(parentId);

	        List<AmpOrganisation> orgs;
	        try {
	            orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, false); //TODO: Second parameter for public view
				JSONObject child = new JSONObject();
				Iterator<AmpOrganisation> it = orgs.iterator();
				while(it.hasNext()){
					AmpOrganisation org = it.next();
					child.put("ID", org.getAmpOrgId());
					child.put("name", org.getName());
					children.add(child);
				}
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
	        } catch (Exception e) {
	            logger.error("unable to load organizations", e);
	        }
		} else if (parentId != null && objectType != null && (objectType.equals("Sector") || objectType.equals("Sectors"))) {
			Long sectorId = Long.parseLong(parentId);
	        List<AmpSector> sectors;
	        try {
	        	sectors = DbUtil.getSubSectors(sectorId); 
				JSONObject child = new JSONObject();
				Iterator<AmpSector> it = sectors.iterator();
				while(it.hasNext()){
					AmpSector sector = it.next();
					child.put("ID", sector.getAmpSectorId());
					child.put("name", sector.getName());
					children.add(child);
				}
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
	        } catch (Exception e) {
	            logger.error("unable to load organizations", e);
	        }
		} else if (parentId != null && objectType != null && (objectType.equals("Region") || objectType.equals("Regions"))){
			Long regionId = Long.parseLong(parentId);
	        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

	        if (regionId != null && regionId != -1) {
                AmpCategoryValueLocations region = LocationUtil.getAmpCategoryValueLocationById(regionId);
                if (region.getChildLocations() != null) {
    				JSONObject child = new JSONObject();
                	Iterator<AmpCategoryValueLocations> it = region.getChildLocations().iterator();
    				while(it.hasNext()){
    					AmpCategoryValueLocations loc = it.next();
    					child.put("ID", loc.getId());
    					child.put("name",loc.getName());
    					children.add(child);
    				}
                }
            }
			root.put("ID", parentId);
			root.put("objectType", objectType);
			root.put("children", children);
		}
		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream = null;

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
			outputStream.write(root.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}
}



