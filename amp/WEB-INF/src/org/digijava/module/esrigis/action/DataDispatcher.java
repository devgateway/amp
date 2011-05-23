package org.digijava.module.esrigis.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.ActivityPoint;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.QueryUtil;
import org.digijava.module.esrigis.helpers.SimpleLocation;
import org.digijava.module.esrigis.helpers.Structure;
import org.digijava.module.visualization.util.DbUtil;

public class DataDispatcher extends MultiAction {
	private static Logger logger = Logger.getLogger(DataDispatcher.class);

	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		MapFilter filter = maphelperform.getFilter();
		if (filter == null || !filter.isIsinitialized()) {
			maphelperform.setFilter(QueryUtil.getNewFilter());
			maphelperform.getFilter().setWorkspaceOnly(true);
		}
		response.setContentType("text/json");
		return modeSelect(mapping, maphelperform, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (request.getParameter("showactivities") != null) {
			return modeShowActivities(mapping, form, request, response);
		}else if (request.getParameter("showhighlights") != null) {
			return modeShowHighlights(mapping, form, request, response);
		}else if (request.getParameter("jsonobject") != null) { 
			return modeJSONObject(mapping, form, request, response);
		}else if (request.getParameter("applyfilter") != null) { 
			return modeapplyFilter(mapping, form, request, response);
		}else if (request.getParameter("showstructures") != null) { 
				return modeShowStructures(mapping, form, request, response);
			}
		return null;
	}

	public ActionForward modeShowActivities(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);

		JSONArray jsonArray = new JSONArray();
		List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		list = DbHelper.getActivities(maphelperform.getFilter());
		Boolean isaggregatable = true;
		for (Iterator<AmpActivityVersion> iterator = list.iterator(); iterator
				.hasNext();) {
			ActivityPoint ap = new ActivityPoint();
			AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			ap.setId(aA.getIdentifier().toString());
			ap.setAmpactivityid(aA.getAmpId());
			ap.setActivityname(aA.getName());

			FundingCalculationsHelper calculations = new FundingCalculationsHelper();
			Iterator fundItr = aA.getFunding().iterator();
			while (fundItr.hasNext()) {
				AmpFunding ampFunding = (AmpFunding) fundItr.next();
				Collection fundDetails = ampFunding.getFundingDetails();
				calculations.doCalculations(fundDetails, maphelperform.getFilter().getCurrencyCode());
			}
			ap.setCommitments(calculations.getTotalCommitments().toString());
			ap.setDisbursements(calculations.getTotActualDisb().toString());
			ap.setExpenditures(calculations.getTotPlannedExp().toString());
			ap.setSectors(SectorsToJson(aA));
			
			ArrayList<SimpleLocation> sla = new ArrayList<SimpleLocation>();
			for (Iterator iterator2 = aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				boolean implocation = alocation.getLocation().getLocation().getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				ArrayList<Long> locationIds = new ArrayList<Long>(Arrays.asList(maphelperform.getFilter().getSelLocationIds()));
				boolean isfiltered = locationIds != null && locationIds.size() > 0 && !locationIds.get(0).equals(-1l) ;
				if (!implocation) {
					isaggregatable = true;
					String lat = alocation.getLocation().getLocation().getGsLat();
					String lon = alocation.getLocation().getLocation().getGsLong();
					SimpleLocation sl = new SimpleLocation();
					sl.setName(alocation.getLocation().getLocation().getName());
					sl.setGeoId(alocation.getLocation().getLocation().getGeoCode());
					sl.setLat(lat);
					sl.setLon(lon);
					if ("".equalsIgnoreCase(lat) && "".equalsIgnoreCase(lon)) {
						sl.setIslocated(false);
					} else {
						sl.setIslocated(true);
					}
					sl.setPercentage(alocation.getLocationPercentage().toString());
					sl.setCommitments(QueryUtil.getPercentage(calculations.getTotalCommitments().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					sl.setDisbursements(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					sl.setExpenditures(QueryUtil.getPercentage(calculations.getTotActualExp().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					
					if (isfiltered){
						if (locationIds.contains(alocation.getLocation().getLocation().getId())){
							sla.add(sl);
						}
					}else{
						sla.add(sl);
					}
					
				} else {
					isaggregatable = false;
					break;
				}
			}
			if (isaggregatable) {
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

	public ActionForward modeShowHighlights(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		MapFilter filter = maphelperform.getFilter();
		filter.setTeamMember(tm);

		Long fiscalCalendarId = filter.getFiscalCalendarId();
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter
				.getYear().intValue() - filter.getYearsInRange());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getYear()
				.intValue());
		String implementationLevel = "";
		if (request.getParameter("level") != null && request.getParameter("level").equals("Region")) { 
			implementationLevel = "Region";
		} else {
			implementationLevel = "Zone";
		}

		// TODO: Move this to a helper, see how to make Filters compatible and
		// use just one class to access database with Visualization

		JSONArray jsonArray = new JSONArray();

		// Get list of locations

		// List<AmpCategoryValueLocations> locations =
		// DbHelper.getRegions(filter);
		List<AmpCategoryValueLocations> locations = DbHelper.getLocations(
				filter, implementationLevel);
		Iterator<AmpCategoryValueLocations> locationsIt = locations.iterator();

		while (locationsIt.hasNext()) {
			AmpCategoryValueLocations location = locationsIt.next();

			Long[] ids = { location.getId() };
			MapFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelLocationIds(ids);
			BigDecimal amountCommitments = DbHelper
					.getFunding(newFilter, startDate, endDate, null, null,
							Constants.COMMITMENT, Constants.ACTUAL).getValue()
					.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
			BigDecimal amountDisbursements = DbHelper
					.getFunding(newFilter, startDate, endDate, null, null,
							Constants.DISBURSEMENT, Constants.ACTUAL)
					.getValue()
					.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
			BigDecimal amountExpenditures = DbHelper
					.getFunding(newFilter, startDate, endDate, null, null,
							Constants.EXPENDITURE, Constants.ACTUAL).getValue()
					.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
			String keyName = "";
			String implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY
					.getValueKey();
			if (location.getParentCategoryValue().getValue()
					.equals(implLocation)) {
				keyName = "National";
			} else {
				Long zoneIds[] = filter.getZoneIds();
				if (zoneIds != null && zoneIds.length > 0 && zoneIds[0] != -1) {
					implLocation = CategoryConstants.IMPLEMENTATION_LOCATION_REGION
							.getValueKey();
					if (location.getParentCategoryValue().getValue()
							.equals(implLocation)) {
						keyName = "Regional";
					} else {
						AmpCategoryValueLocations parent = LocationUtil
								.getTopAncestor(location, implLocation);
						keyName = parent.getName();
					}
				} else {
					AmpCategoryValueLocations parent = LocationUtil
							.getTopAncestor(location, implLocation);
					keyName = parent.getName();
				}

			}
			SimpleLocation locationJSON = new SimpleLocation();
			locationJSON.setName(keyName);
			locationJSON.setGeoId(location.getGeoCode());
			locationJSON.setCommitments(amountCommitments.toPlainString());
			locationJSON.setDisbursements(amountDisbursements.toPlainString());
			locationJSON.setExpenditures(amountExpenditures.toPlainString());
			jsonArray.add(locationJSON);
		}

		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();
		return null;
	}
	
	public ActionForward modeShowStructures(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm)form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		
		JSONArray jsonArray = new JSONArray();
		 List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		 list = DbHelper.getActivities(maphelperform.getFilter());
   		 boolean structuresExists = false;
		 for (Iterator<AmpActivityVersion> iterator = list.iterator(); iterator.hasNext();) {
			 ActivityPoint ap = new ActivityPoint();
			 AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			 ap.setActivityname(aA.getName());
			 
			ArrayList<Structure> structures = new ArrayList<Structure>();
			for (Iterator<AmpStructure> iterator2 =aA.getStructures().iterator(); iterator2.hasNext();) {
				AmpStructure structure = iterator2.next();
				Structure structureJSON = new Structure();
				structureJSON.setDescription(structure.getDescription());
				structureJSON.setLat(structure.getLatitude());
				structureJSON.setLon(structure.getLongitude());
				structureJSON.setName(structure.getTitle());
				structureJSON.setShape(structure.getShape());
				structureJSON.setType(structure.getType().getName());
				structures.add(structureJSON);
				structuresExists = true;
			}
			ap.setStructures(structures);
			if(structuresExists) {
				jsonArray.add(ap);
				structuresExists = false;
			}
		 }

	
		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();
		return null;
	}

	public ActionForward modeJSONObject(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		DataDispatcherForm from = (DataDispatcherForm) form;

		String parentId = request.getParameter("parentId");
		String objectType = request.getParameter("objectType");

		JSONObject root = new JSONObject();
		JSONArray children = new JSONArray();

		// Gets children object according to objectType

		if (parentId != null
				&& objectType != null
				&& (objectType.equals("Organization") || objectType
						.equals("Organizations"))) {
			// Get list of sub organizations
			Long orgGroupId = Long.parseLong(parentId);

			List<AmpOrganisation> orgs;
			try {
				orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, false); 
				JSONObject child = new JSONObject();
				Iterator<AmpOrganisation> it = orgs.iterator();
				while (it.hasNext()) {
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
		} else if (parentId != null
				&& objectType != null
				&& (objectType.equals("Sector") || objectType.equals("Sectors"))) {
			Long sectorId = Long.parseLong(parentId);
			List<AmpSector> sectors;
			try {
				sectors = DbUtil.getSubSectors(sectorId);
				JSONObject child = new JSONObject();
				Iterator<AmpSector> it = sectors.iterator();
				while (it.hasNext()) {
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
		} else if (parentId != null
				&& objectType != null
				&& (objectType.equals("Region") || objectType.equals("Regions"))) {
			Long regionId = Long.parseLong(parentId);
			List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

			if (regionId != null && regionId != -1) {
				AmpCategoryValueLocations region = LocationUtil
						.getAmpCategoryValueLocationById(regionId);
				if (region.getChildLocations() != null) {
					JSONObject child = new JSONObject();
					Iterator<AmpCategoryValueLocations> it = region
							.getChildLocations().iterator();
					while (it.hasNext()) {
						AmpCategoryValueLocations loc = it.next();
						child.put("ID", loc.getId());
						child.put("name", loc.getName());
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
	
	
	public ActionForward modeapplyFilter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		DataDispatcherForm  datadispatcherform = (DataDispatcherForm) form;
		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (datadispatcherform.getFilter().getWorkspaceOnly() != null &&  datadispatcherform.getFilter().getWorkspaceOnly()) {
			datadispatcherform.getFilter().setTeamMember(tm);
        } else {
        	datadispatcherform.getFilter().setTeamMember(null);
        }
		
		datadispatcherform.getFilter().setOrgGroupIds(getLongArrayFromParameter(request.getParameter("orgGroupIds")));
		datadispatcherform.getFilter().setOrgIds(getLongArrayFromParameter(request.getParameter("orgIds")));
		datadispatcherform.getFilter().setSectorIds(getLongArrayFromParameter(request.getParameter("sectorIds")));
		datadispatcherform.getFilter().setSubSectorIds(getLongArrayFromParameter(request.getParameter("subSectorIds")));
		datadispatcherform.getFilter().setRegionIds(getLongArrayFromParameter(request.getParameter("regionIds")));
		datadispatcherform.getFilter().setZoneIds(getLongArrayFromParameter(request.getParameter("zoneIds")));
		
		Long[] orgsGrpIds =  datadispatcherform.getFilter().getOrgGroupIds();
		Long orgsGrpId =  datadispatcherform.getFilter().getOrgGroupId();
		if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
			if (orgsGrpId!=null){
				Long[] temp = {orgsGrpId};
				datadispatcherform.getFilter().setSelOrgGroupIds(temp);
			}else{
				Long[] temp = {-1l};
				datadispatcherform.getFilter().setSelOrgGroupIds(temp);
			}
			
		} else {
			datadispatcherform.getFilter().setOrgGroupId(-1l);//unset orgsGrpIds
			datadispatcherform.getFilter().setSelOrgGroupIds(orgsGrpIds);
		}	
		
		Long[] orgsIds =  datadispatcherform.getFilter().getOrgIds();
		Long orgsId =  datadispatcherform.getFilter().getOrgId();
		if (orgsIds == null || orgsIds.length == 0 || orgsIds[0] == -1) {
			orgs.add(DbUtil.getOrganisation(orgsId));
		} else {
			datadispatcherform.getFilter().setOrgId(-1l);//unset orgId
			for (int i = 0; i < orgsIds.length; i++) {
				Long long1 = orgsIds[i];
				if(long1 != -1){
					orgs.add(DbUtil.getOrganisation(long1));
				}
			}
		}
		datadispatcherform.getFilter().setOrganizationsSelected(orgs);

		Long secsId =  datadispatcherform.getFilter().getSectorId();
		Long subSecsId =  datadispatcherform.getFilter().getSubSectorId();
		Long[] secsIds =  datadispatcherform.getFilter().getSectorIds();
		Long[] subSecsIds =  datadispatcherform.getFilter().getSubSectorIds();
		
		
		if ((subSecsIds == null || subSecsIds.length == 0 || subSecsIds[0] == -1) && (subSecsId == null || subSecsId == -1)) {
			if (secsIds == null || secsIds.length == 0 || secsIds[0] == -1) {
				if (secsId!=null){
					Long[] temp = {secsId};
					datadispatcherform.getFilter().setSelSectorIds(temp);
				}else{
					Long[] temp = {-1l};
					datadispatcherform.getFilter().setSelSectorIds(temp);
				}
			} else {
				datadispatcherform.getFilter().setSectorId(-1l);//unset sectorId
				datadispatcherform.getFilter().setSelSectorIds(secsIds);
			}	
		} else {
			if (subSecsIds == null || subSecsIds.length == 0 || subSecsIds[0] == -1) {
				Long[] temp = {subSecsId};
				datadispatcherform.getFilter().setSelSectorIds(temp);
			} else {
				datadispatcherform.getFilter().setSubSectorId(-1l);//unset subSectorId
				 datadispatcherform.getFilter().setSelSectorIds(subSecsIds);
			}
		}
		
		Long regsId =  datadispatcherform.getFilter().getRegionId();
		Long zonesId =  datadispatcherform.getFilter().getZoneId();
		Long[] regsIds =  datadispatcherform.getFilter().getRegionIds();
		Long[] zonesIds =  datadispatcherform.getFilter().getZoneIds();
		
		if ((zonesIds == null || zonesIds.length == 0 || zonesIds[0] == -1) && (zonesId == null || zonesId == -1)) {
			if (regsIds == null || regsIds.length == 0 || regsIds[0] == -1) {
				if (regsId!=null){
					Long[] temp = {regsId};
					datadispatcherform.getFilter().setSelLocationIds(temp);
				}else{
					Long[] temp = {-1l};
					datadispatcherform.getFilter().setSelLocationIds(temp);
				}
			} else {
				datadispatcherform.getFilter().setRegionId(-1l);//unset regionId
				datadispatcherform.getFilter().setSelLocationIds(regsIds);
			}
		} else {
			if (zonesIds == null || zonesIds.length == 0 || zonesIds[0] == -1) {
				Long[] temp = {zonesId};
				datadispatcherform.getFilter().setSelLocationIds(temp);
			} else {
				datadispatcherform.getFilter().setZoneId(-1l);//unset zoneId
				datadispatcherform.getFilter().setSelLocationIds(zonesIds);
			}
		}
		
		return modeShowActivities(mapping, datadispatcherform, request, response);
	}
	
	
	private Long[] getLongArrayFromParameter (String param){
		if (param!=null && param!="") {
			String[] spl = param.split(",");
			Long[] ret = new Long [spl.length];
			for (int i = 0; i < spl.length; i++) {
				if (spl[i] != null && spl[i].length()>0)
					ret[i] = Long.valueOf(spl[i]);
				else
					ret[i] = 0L;
			}
			return ret; 
		}
		return null;
	}


	private List<ActivitySector> SectorsToJson(AmpActivityVersion activity) {
	Collection sectors = activity.getSectors();
	List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
	if (sectors != null && sectors.size() > 0) {
		Iterator sectItr = sectors.iterator();
		while (sectItr.hasNext()) {
			AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
			if (ampActSect != null) {
				AmpSector sec = ampActSect.getSectorId();
				if (sec != null) {
					AmpSector parent = null;
					AmpSector subsectorLevel1 = null;
					AmpSector subsectorLevel2 = null;
					if (sec.getParentSectorId() != null) {
						if (sec.getParentSectorId().getParentSectorId() != null) {
							subsectorLevel2 = sec;
							subsectorLevel1 = sec.getParentSectorId();
							parent = sec.getParentSectorId().getParentSectorId();
						} else {
							subsectorLevel1 = sec;
							parent = sec.getParentSectorId();
						}
					} else {
						parent = sec;
					}
					ActivitySector actSect = new ActivitySector();
                                            actSect.setConfigId(ampActSect.getClassificationConfig().getId());
					if (parent != null) {
						actSect.setId(parent.getAmpSectorId());
						String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
						if (view != null)
							if (view.equalsIgnoreCase("On")) {
								actSect.setCount(1);
							} else {
								actSect.setCount(2);
							}

						actSect.setSectorId(parent.getAmpSectorId());
						actSect.setSectorName(parent.getName());
						if (subsectorLevel1 != null) {
							actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
							actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
							if (subsectorLevel2 != null) {
								actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
								actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
							}
						}
						actSect.setSectorPercentage(ampActSect.getSectorPercentage());
                                                    actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
                                                    
					}
                                           
					activitySectors.add(actSect);
				}
			}
		}
		Collections.sort(activitySectors);
	}
		return activitySectors;
	}
	
}
