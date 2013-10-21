package org.digijava.module.esrigis.action;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.ActivityPoint;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.PointContent;
import org.digijava.module.esrigis.helpers.QueryUtil;
import org.digijava.module.esrigis.helpers.SimpleDonor;
import org.digijava.module.esrigis.helpers.SimpleLocation;
import org.digijava.module.esrigis.helpers.Structure;
import org.digijava.module.esrigis.helpers.XlsHelper;
import org.digijava.module.visualization.util.DbUtil;
import org.hibernate.Session;
public class DataDispatcher extends MultiAction {
	private static Logger logger = Logger.getLogger(DataDispatcher.class);

	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		MapFilter filter = maphelperform.getFilter();
		if (filter == null || !filter.isIsinitialized()) {
			maphelperform.setFilter(QueryUtil.getNewFilter(request));
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
		}else if (request.getParameter("selectedfilter") != null) { 
			return modeGetSelectedFilter(mapping, form, request, response);
		}else if (request.getParameter("getconfig") != null) { 
			return modeGetConfiguration(mapping, form, request, response);
		}else if (request.getParameter("getmedia") != null) { 
			return modeGetMedia(mapping, form, request, response);
		}else if (request.getParameter("shownational") != null) { 
			return modeShowNational(mapping, form, request, response);
		}else if (request.getParameter("getcontent") != null){
			return modeGetContent(mapping, form, request, response);
		}else if (request.getParameter("exporttocsv") != null){
			return modeExportToCsv(mapping, form, request, response);
		}else if (request.getParameter("getPeaceBuildingValues") != null){
            return modeGetPeaceBuildingValues(mapping, form, request, response);
        }
		return null;
	}



    private ActionForward modeGetPeaceBuildingValues(ActionMapping mapping,ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        DataDispatcherForm maphelperform = (DataDispatcherForm) form;
        /*
        String geoIDsStr = request.getParameter("geoIds");
        List<String> geoIDs = new ArrayList<String>();
        StringTokenizer geoIDTokenizer = new StringTokenizer(geoIDsStr, "|", false);
        while (geoIDTokenizer.hasMoreTokens()) {
            geoIDs.add(geoIDTokenizer.nextToken());
        } */

        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        MapFilter filter = maphelperform.getFilter();
        filter.setTeamMember(tm);

        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());


        String implementationLevel = "";
        if (request.getParameter("level") != null && request.getParameter("level").equals("Region")) {
            implementationLevel = "Region";
        } else {
            implementationLevel = "Zone";
        }
        JSONArray jsonArray = new JSONArray();
        List<AmpCategoryValueLocations> locations = DbHelper.getLocations(filter, implementationLevel);
        ArrayList<SimpleLocation> mapregions = new ArrayList<SimpleLocation>();


        filter.setFilterByPeacebuildingMarker(true);
        mapregions = DbHelper.getFundingByRegionList(locations, implementationLevel, filter.getCurrencyCode(), startDate, endDate,
				/*filter.getTransactionType(),*/ CategoryConstants.ADJUSTMENT_TYPE_ACTUAL,
                new Integer(3), new BigDecimal(1), filter);
        filter.setFilterByPeacebuildingMarker(false);

        jsonArray.addAll(mapregions);
        PrintWriter pw = response.getWriter();
        pw.write(jsonArray.toString());
        pw.flush();
        pw.close();
        return null;
    }
	private ActionForward modeExportToCsv(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		StringBuffer line = new StringBuffer();
	    DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		
		long startTS=System.currentTimeMillis();
		list = DbHelper.getActivities(maphelperform.getFilter());
	    OutputStream out = response.getOutputStream();
        XlsHelper Xls = new XlsHelper(); 
		try {
			Xls.XlsMaker(list, request, response, maphelperform.getFilter()).write(out);
			out.flush();
			out.close();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward modeShowNational(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);

		JSONArray jsonArray = new JSONArray();
		List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		list = DbHelper.getActivities(maphelperform.getFilter());
		Boolean isaggregatable = true;
		for (Iterator<AmpActivityVersion> iterator = list.iterator(); iterator.hasNext();) {
			ActivityPoint ap = new ActivityPoint();
			AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			ap.setId(aA.getIdentifier().toString());
			ap.setAmpactivityid(aA.getAmpId());
			ap.setActivityname(aA.getName());

			FundingCalculationsHelper calculations = new FundingCalculationsHelper();
			Iterator fundItr = aA.getFunding().iterator();
			ap.setDonors(new ArrayList<SimpleDonor>());
			while (fundItr.hasNext()) {
				AmpFunding ampFunding = (AmpFunding) fundItr.next();
				calculations.doCalculations(ampFunding, maphelperform.getFilter().getCurrencyCode());
				SimpleDonor donor = new SimpleDonor(); 
				donor.setDonorname(ampFunding.getAmpDonorOrgId().getName());
				donor.setDonorCode(ampFunding.getAmpDonorOrgId().getAcronym());
				donor.setDonorgroup(ampFunding.getAmpDonorOrgId().getOrgGroup());
				ap.getDonors().add(donor);
			}
			ap.setCommitments(calculations.getTotalCommitments().toString());
			ap.setDisbursements(calculations.getTotActualDisb().toString());
			//ap.setExpenditures(calculations.getTotPlannedExp().toString());
			//ap.setSectors(SectorsToJson(aA));
			//ap.setCurrecycode(maphelperform.getFilter().getCurrencyCode());
			ArrayList<SimpleLocation> sla = new ArrayList<SimpleLocation>();
			for (Iterator iterator2 = aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				boolean implocation = alocation.getLocation().getLocation().getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				ArrayList<Long> locationIds = new ArrayList<Long>(Arrays.asList(maphelperform.getFilter().getSelLocationIds()));
				if (maphelperform.getFilter().getZoneIds()!= null && maphelperform.getFilter().getZoneIds().length>0){
					locationIds.addAll(Arrays.asList(maphelperform.getFilter().getZoneIds()));
				}
				boolean isfiltered = locationIds != null && locationIds.size() > 0 && !locationIds.get(0).equals(-1l) ;
				if (implocation) {
					isaggregatable = true;
					SimpleLocation sl = new SimpleLocation();
					String lat = alocation.getLocation().getLocation().getGsLat();
					String lon = alocation.getLocation().getLocation().getGsLong();
					
					if (alocation.getLatitude() != null && alocation.getLatitude() !=null 
							&& !"".equalsIgnoreCase(alocation.getLatitude()) && !"".equalsIgnoreCase(alocation.getLongitude())){
						sl.setExactlocation(true);
						sl.setExactlocation_lat(alocation.getLatitude());
						sl.setExactlocation_lon(alocation.getLongitude());
					}else{
						sl.setExactlocation(false);
					}
					
					sl.setName(alocation.getLocation().getLocation().getName());
					sl.setGeoId(alocation.getLocation().getLocation().getGeoCode());
					sl.setLat(lat);
					sl.setLon(lon);
					if ("".equalsIgnoreCase(lat) && "".equalsIgnoreCase(lon)) {
						sl.setIslocated(false);
					} else {
						sl.setIslocated(true);
					}
					if (alocation.getLocationPercentage()!=null){
						sl.setPercentage(alocation.getLocationPercentage().toString());
						sl.setCommitments(QueryUtil.getPercentage(calculations.getTotalCommitments().getValue(),new BigDecimal(alocation.getLocationPercentage())));
						sl.setDisbursements(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(alocation.getLocationPercentage())));
						sl.setExpenditures(QueryUtil.getPercentage(calculations.getTotActualExp().getValue(),new BigDecimal(alocation.getLocationPercentage())));
					}
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

	private ActionForward modeGetMedia(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
	  URL url;
	  String searchtext = request.getParameter("searchtext");
	  String data = "text="+searchtext;
	try {
		url = new URL("http://api.metalayer.com/s/datalayer/1/bundle");
	  HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	  
	  httpCon.setDoOutput(true);
	  httpCon.setRequestMethod("POST");
	  OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
	  out.write(data);
	  out.flush();
	  out.close();
	  
	//Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
	    String line;
	    response.setContentType("text/json-comment-filtered");
	    PrintWriter pw = response.getWriter();
	    while ((line = rd.readLine()) != null) {
	    	JSON jso = JSONSerializer.toJSON(line);
	    	pw.write(jso.toString());
	    	
	    }
	    pw.flush();
		pw.close();
	    rd.close();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	  
	  return null;
	}
	
	private ActionForward modeGetConfiguration(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		List<AmpMapConfig> maps = (List<AmpMapConfig>) DbHelper.getMaps();
		JSONArray jsonArray = new JSONArray();
		
		for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
			AmpMapConfig map = (AmpMapConfig) iterator.next();
			jsonArray.add(map);
		}
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(jsonArray.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;	
		
	}
	
	
	private ActionForward modeGetSelectedFilter(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(maphelperform.getFilter().toJson().toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public ActionForward modeGetContent(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		HttpSession session = request.getSession();
		AmpActivityVersion activity = DbHelper.getActivityById(new Long(request.getParameter("id")));
		FundingCalculationsHelper calculations = new FundingCalculationsHelper();
		Iterator fundItr = activity.getFunding().iterator();
		
		JSONArray jsonArray = new JSONArray();
		PointContent ct = new PointContent();
		ct.setDonors(new ArrayList<SimpleDonor>());
		while (fundItr.hasNext()) {
			AmpFunding ampFunding = (AmpFunding) fundItr.next();
			calculations.doCalculations(ampFunding, maphelperform.getFilter().getCurrencyCode());
			SimpleDonor donor = new SimpleDonor(); 
			donor.setDonorname(ampFunding.getAmpDonorOrgId().getName());
			donor.setDonorCode(ampFunding.getAmpDonorOrgId().getAcronym());
			donor.setDonorgroup(ampFunding.getAmpDonorOrgId().getOrgGroup());
			ct.getDonors().add(donor);
		}
		
		ct.setId(activity.getIdentifier().toString());
		ct.setActivityname(activity.getName());
		ct.setCommitments(calculations.getTotalCommitments().toString());
		ct.setDisbursements(calculations.getTotActualDisb().toString());
		ct.setExpenditures(calculations.getTotPlannedExp().toString());
		ct.setMtef(calculations.getTotalMtef().toString());
		ct.setSectors(SectorsToJson(activity));
		ct.setCurrecycode(maphelperform.getFilter().getCurrencyCode());
		for (Iterator iterator = activity.getLocations().iterator(); iterator.hasNext();) {
			AmpActivityLocation alocation = (AmpActivityLocation) iterator.next();
			if (alocation.getLocation().getLocation().getName().equalsIgnoreCase(request.getParameter("name"))){
				ct.setCommitmentsforlocation(QueryUtil.getPercentage(calculations.getTotalCommitments().getValue(),new BigDecimal(alocation.getLocationPercentage())));
				ct.setDisbursementsforlocation(QueryUtil.getPercentage(calculations.getTotActualDisb().getValue(),new BigDecimal(alocation.getLocationPercentage())));
				ct.setExpendituresforlocation(QueryUtil.getPercentage(calculations.getTotActualExp().getValue(),new BigDecimal(alocation.getLocationPercentage())));
				ct.setMtefforlocation(QueryUtil.getPercentage(calculations.getTotalMtef().getValue(), new BigDecimal(alocation.getLocationPercentage())));
				break;
			}
		}

		jsonArray.add(ct);
		
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(jsonArray.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ActionForward modeShowActivities(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);

		JSONArray jsonArray = new JSONArray();
		List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		
		long startTS=System.currentTimeMillis();
		list = DbHelper.getActivities(maphelperform.getFilter());
		long endTS=System.currentTimeMillis();
		logger.info("getActivities in "+(endTS-startTS)/1000.0+" seconds. ");
		logger.info("Iteration Starts");
		startTS=System.currentTimeMillis();
		
		Boolean isaggregatable = true;
		for (Iterator<AmpActivityVersion> iterator = list.iterator(); iterator.hasNext();) {
			ActivityPoint ap = new ActivityPoint();
			AmpActivityVersion aA = (AmpActivityVersion) iterator.next();
			ap.setId(aA.getIdentifier().toString());
			ap.setAmpactivityid(aA.getAmpId());
			ap.setActivityname(aA.getName());
			
			Iterator fundItr = aA.getFunding().iterator();
			ap.setDonors(new ArrayList<SimpleDonor>());
			while (fundItr.hasNext()) {
				AmpFunding ampFunding = (AmpFunding) fundItr.next();
				//Collection fundDetails = ampFunding.getFundingDetails();
				SimpleDonor donor = new SimpleDonor(); 
				donor.setDonorname(ampFunding.getAmpDonorOrgId().getName());
				donor.setDonorCode(ampFunding.getAmpDonorOrgId().getAcronym());
				donor.setDonorgroup(ampFunding.getAmpDonorOrgId().getOrgGroup());
				ap.getDonors().add(donor);
			}
			
			ArrayList<SimpleLocation> sla = new ArrayList<SimpleLocation>();
			for (Iterator iterator2 = aA.getLocations().iterator(); iterator2.hasNext();) {
				AmpActivityLocation alocation = (AmpActivityLocation) iterator2.next();
				boolean implocation = alocation.getLocation().getLocation().getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				ArrayList<Long> locationIds = new ArrayList<Long>(Arrays.asList(maphelperform.getFilter().getSelLocationIds()));
				
				boolean isfiltered = locationIds != null && locationIds.size() > 0 && !locationIds.get(0).equals(-1l) ;
				if (!implocation) {
					isaggregatable = true;
					SimpleLocation sl = new SimpleLocation();
					String lat = alocation.getLocation().getLocation().getGsLat();
					String lon = alocation.getLocation().getLocation().getGsLong();
					
					if (alocation.getLatitude() != null && alocation.getLatitude() !=null 
							&& !"".equalsIgnoreCase(alocation.getLatitude()) && !"".equalsIgnoreCase(alocation.getLongitude())){
						sl.setExactlocation(true);
						sl.setExactlocation_lat(alocation.getLatitude());
						sl.setExactlocation_lon(alocation.getLongitude());
					}else{
						sl.setExactlocation(false);
					}
					
					sl.setName(alocation.getLocation().getLocation().getName());
					sl.setGeoId(alocation.getLocation().getLocation().getGeoCode());
					sl.setLat(lat);
					sl.setLon(lon);
					if ("".equalsIgnoreCase(lat) && "".equalsIgnoreCase(lon)) {
						sl.setIslocated(false);
					} else {
						sl.setIslocated(true);
					}
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
		
		endTS=System.currentTimeMillis();
		logger.info("iteration done in "+(endTS-startTS)/1000.0+" seconds. ");
		
		
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
		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
		
		
		String implementationLevel = "";
		if (request.getParameter("level") != null && request.getParameter("level").equals("Region")) { 
			implementationLevel = "Region";
		} else {
			implementationLevel = "Zone";
		}
		JSONArray jsonArray = new JSONArray();
		List<AmpCategoryValueLocations> locations = DbHelper.getLocations(filter, implementationLevel);
		ArrayList<SimpleLocation> mapregions = new ArrayList<SimpleLocation>();
		
		
		
		mapregions = DbHelper.getFundingByRegionList(locations, implementationLevel, filter.getCurrencyCode(), startDate, endDate, 
				/*filter.getTransactionType(),*/ CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, 
				new Integer(3), new BigDecimal(1), filter);
		
		jsonArray.addAll(mapregions);
		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();
		return null;
	}
	
	/**
	 * safely closes a connection, swallowing any exception
	 * @param conn
	 */
	public final static void closeConnection(Connection conn)
	{
		try
		{
			if (conn != null)
				conn.close();
		}
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * fetches a Map<ActivityId, ActivityName> of activities which have an id in a predetermined set
	 * @param activityIdList
	 * @return
	 */
	protected Map<Long, String> getActivityNamesByIds(List<Long> activityIdList)
	{
		Map<Long, String> activityNamesByAmpIds = new TreeMap<Long, String>();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = PersistenceManager.getJdbcConnection();
			rs = conn.createStatement().executeQuery("SELECT amp_activity_id, name FROM amp_activity_version WHERE amp_activity_id IN (" + Util.toCSString(activityIdList) + ")");
			while (rs.next())
			{
				Long actId = rs.getLong(1);
				String actName = rs.getString(2);
				
				activityNamesByAmpIds.put(actId, actName);
			}
			rs.close();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			closeConnection(conn);
		}
		return activityNamesByAmpIds;
	}
	
	/**
	 * fetches structs attached to any of the ampActivityIds given in the input
	 * @param activityIdList
	 * @return Map<ActivityId, Set<StructId>>
	 */
	protected Map<Long, Set<Long>> populateStructs(List<Long> activityIdList)
	{
		Map<Long, Set<Long>> structsByAmpIds = new TreeMap<Long, Set<Long>>();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = PersistenceManager.getJdbcConnection();
			rs = conn.createStatement().executeQuery("SELECT amp_activity_id, amp_structure_id FROM amp_activity_structures WHERE amp_activity_id IN (" + Util.toCSString(activityIdList) + ")");
			while (rs.next())
			{
				Long actId = rs.getLong(1);
				Long strucId = rs.getLong(2);
				
				if (!structsByAmpIds.containsKey(actId))
					structsByAmpIds.put(actId, new TreeSet<Long>());
				
				structsByAmpIds.get(actId).add(strucId);
			}
			rs.close();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			closeConnection(conn);
		}
		return structsByAmpIds;
	}
	
	/**
	 * collects all values from within the values if a map
	 * @param in
	 * @return
	 */
	public final static Set<Long> collectIds(Map<Long, Set<Long>> in)
	{
		Set<Long> res = new TreeSet<Long>();
		for(Collection<Long> set:in.values())
			res.addAll(set);				
		return res;
	}
	
	
	/**
	 * fetches Amp_structures, fills their info in Structure instances and returns the fetched instances mapped by amp_structure_id
	 * @param ids
	 * @return
	 */
	public final static Map<Long, Structure> fetchStructs(Set<Long> ids)
	{
		Map<Long, Structure> z = new TreeMap<Long, Structure>();
		
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = PersistenceManager.getJdbcConnection();
			
			// populate set of structures which have an image
			Set<Long> strucImages = new HashSet<Long>();
			rs = conn.createStatement().executeQuery("SELECT DISTINCT(amp_structure_id) FROM amp_structure_img");
			while (rs.next())
				strucImages.add(rs.getLong(1));
			rs.close();
			
			// cache structure type names
			Map<Long, String> typeNamesById = new HashMap<Long, String>();
			rs = conn.createStatement().executeQuery("SELECT typeid, name FROM amp_structure_type");
			while (rs.next())
				typeNamesById.put(rs.getLong(1), rs.getString(2));
			rs.close();
			
			rs = conn.createStatement().executeQuery("SELECT amp_structure_id, title, description, latitude, longitude, shape, type FROM amp_structure WHERE amp_structure_id IN (" + Util.toCSString(ids) + ")");
			while (rs.next())
			{
				Long strucId = rs.getLong(1);
				String title = rs.getString(2);
				String description = rs.getString(3);
				String latitude = rs.getString(4);
				String longitude = rs.getString(5);
				String shape = rs.getString(6);
				
				long typeId = rs.getLong(7); // guaranteed not null
				String typeName = typeNamesById.get(typeId);
				
				Structure structureJSON = new Structure();
				structureJSON.setId(strucId);
				structureJSON.setDescription(description);
				structureJSON.setLat(latitude);
				structureJSON.setLon(longitude);
				structureJSON.setName(title);
				structureJSON.setShape(shape);
				structureJSON.setType(typeName);
				structureJSON.setTypeId(typeId);
				structureJSON.setHasImage(strucImages.contains(strucId));
				
				z.put(strucId, structureJSON);
			}
			rs.close();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			closeConnection(conn);
		}
		return z;
	}
	
	/**
	 * returns true IFF structure passes the "structure type id" part of a MapFilter
	 * @param filter
	 * @param structureJSON
	 * @return
	 */
	public boolean structurePassesFilter(MapFilter filter, Structure structureJSON)
	{
   		Long[] selectedStructures = filter.getSelStructureTypes();
		boolean structureMatch = false;
		
		if (selectedStructures != null){
			for(int idx = 0; idx < selectedStructures.length; idx++){
				if (selectedStructures[idx].equals(structureJSON.getTypeId())){
					structureMatch = true;
				}
				else if(selectedStructures[idx].equals(-1l)){
					structureMatch = true;
				}
			}
		}
			
		return (structureMatch || selectedStructures == null || selectedStructures.length == 0 );	
	}
	
	
	public ActionForward modeShowStructures(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm)form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		
		long aaa = System.currentTimeMillis();
		
		JSONArray jsonArray = new JSONArray();
		List<Long> activityIdList = new ArrayList<Long>();
		activityIdList = DbHelper.getActivitiesIds(maphelperform.getFilter());
		
		long bbb = System.currentTimeMillis();
		long gettingActivitiesTime = bbb - aaa;
		System.out.println("fetching all activities took " + gettingActivitiesTime + " millies");
		
		Map<Long, Set<Long>> structsByAmpIds = populateStructs(activityIdList);
		Map<Long, Structure> structsByIds = fetchStructs(collectIds(structsByAmpIds));
		Map<Long, String> activityNamesByIds = getActivityNamesByIds(activityIdList);
				   		 
   		for(Long activityId:structsByAmpIds.keySet())
   		{
   	  		boolean structuresExists = false;
   	  		
   			Set<Long> curStructs = structsByAmpIds.get(activityId);
   			ActivityPoint ap = new ActivityPoint();
   			ArrayList<Structure> structures = new ArrayList<Structure>();
   			
   			ap.setActivityname(activityNamesByIds.get(activityId));
   			ap.setAmpactivityid(activityId.toString());
   			
   			for(Long structId:curStructs)
   			{
   				Structure structure = structsByIds.get(structId);
   				boolean structureMatch = structurePassesFilter(maphelperform.getFilter(), structure);
   				if (structureMatch)
   				{
   					structures.add(structure);
   					structuresExists = true;
   				}
   			}
   			
   			ap.setStructures(structures);
   			if(structuresExists)
   			{
				jsonArray.add(ap);
			}
   		}

   		long ccc = System.currentTimeMillis();
		 
   		long secondPartMillies = ccc - bbb;
   		System.out.println("doing the second part took " + secondPartMillies);
   		//watch STRUCTS here
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

		if (parentId != null && objectType != null && (objectType.equals("Organizations")||objectType.equals("Organization")||objectType.equals("ImplementingOrganizations"))) {
			// Get list of sub organizations
			Long orgGroupId = Long.parseLong(parentId);

			List<AmpOrganisation> orgs;
			try {
				orgs = DbHelper.getDonorOrganisationByGroupId(orgGroupId, false); 
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
		}else if (parentId != null && objectType != null && objectType.equals("Orgtype")){
			// Get list of sub organizations
			Long orgtypeid = Long.parseLong(parentId);

			List<AmpOrganisation> orgs;
			try {
				orgs = DbHelper.getDonorOrganisationByType(orgtypeid, false); 
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
		}else if (parentId != null && objectType != null && (objectType.equals("Sector") || objectType.equals("Sectors"))) {
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
		} else if (parentId != null && objectType != null && (objectType.equals("Region") || objectType.equals("Regions"))) {
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
	
	
	public ActionForward modeapplyFilter(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		DataDispatcherForm  datadispatcherform = (DataDispatcherForm) form;
		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		
		if (request.getParameter("reset") != null){
			
			datadispatcherform.setFilter(QueryUtil.getNewFilter(request));
			datadispatcherform.getFilter().setWorkspaceOnly(true);
			return modeShowActivities(mapping, datadispatcherform, request, response);
		}
		
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
		datadispatcherform.getFilter().setSelStructureTypes(getLongArrayFromParameter(request.getParameter("structuresIds")));
		datadispatcherform.getFilter().setSelfinancingInstruments(getLongArrayFromParameter(request.getParameter("aidmodalityIds")));
		datadispatcherform.getFilter().setSeltypeofassistence(getLongArrayFromParameter(request.getParameter("typeofassissIds")));
		datadispatcherform.getFilter().setSelprojectstatus(getLongArrayFromParameter(request.getParameter("projectstIds")));
		datadispatcherform.getFilter().setSelorganizationsTypes(getLongArrayFromParameter(request.getParameter("orgtypesIds")));
		
		
		
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

		Long[] secsIds =  datadispatcherform.getFilter().getSectorIds();
		Long[] subSecsIds =  datadispatcherform.getFilter().getSubSectorIds();
		
		if (secsIds == null && subSecsIds == null){
			Long[] temp = {-1l};
			datadispatcherform.getFilter().setSelSectorIds(temp);
		}else{
			datadispatcherform.getFilter().setSelSectorIds((Long[]) ArrayUtils.addAll(secsIds, subSecsIds));
		}
		
		Long[] regsIds =  datadispatcherform.getFilter().getRegionIds();
		Long[] zonesIds =  datadispatcherform.getFilter().getZoneIds();
		
		if (zonesIds == null && regsIds == null){
			Long[] temp = {-1l};
			datadispatcherform.getFilter().setSelLocationIds(temp);
		}else{
			datadispatcherform.getFilter().setSelLocationIds((Long[]) ArrayUtils.addAll(regsIds, zonesIds));
		}
		
		return null;
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
