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
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.ActivityLocationDigest;
import org.digijava.module.esrigis.helpers.ActivityPoint;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.PointContent;
import org.digijava.module.esrigis.helpers.QueryUtil;
import org.digijava.module.esrigis.helpers.SimpleDonor;
import org.digijava.module.esrigis.helpers.SimpleLocation;
import org.digijava.module.esrigis.helpers.Structure;
import org.digijava.module.esrigis.helpers.XlsHelper;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.util.Constants;
import org.digijava.module.visualization.util.DbUtil;
import org.hibernate.Query;
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
        List<Long> locations = filter.buildFilteredLocationIds();
        ArrayList<SimpleLocation> mapregions = new ArrayList<SimpleLocation>();


        filter.setFilterByPeacebuildingMarker(true);
        List<AmpCategoryValue> allMarkers = DbHelper.getPeacebuildingMarkers();

        List <AbstractMap.SimpleEntry> indicatorValueMap = new ArrayList <AbstractMap.SimpleEntry> ();

        Long curMarkerId = filter.getSelectedPeacebuildingMarkerId();
        for(AmpCategoryValue indicator : allMarkers) {
            filter.setSelectedPeacebuildingMarkerId(indicator.getId());
            mapregions = DbHelper.getFundingByRegionList(locations, implementationLevel, filter.getCurrencyCode(), startDate, endDate,
				/*filter.getTransactionType(),*/ CategoryConstants.ADJUSTMENT_TYPE_ACTUAL,
                    new Integer(3), new BigDecimal(1), filter);
            AbstractMap.SimpleEntry entry = new AbstractMap.SimpleEntry(indicator.getId(), mapregions);
            indicatorValueMap.add(entry);
        }
        filter.setSelectedPeacebuildingMarkerId(curMarkerId);
        filter.setFilterByPeacebuildingMarker(false);

        Collection<AmpCategoryValueLocations> allRegions = DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();

        List allRegs = new ArrayList();
        for(AmpCategoryValueLocations catValLoc : allRegions) {
            if (catValLoc.getGeoCode() != null && !catValLoc.getGeoCode().trim().isEmpty()) {
                allRegs.add(new AbstractMap.SimpleEntry<String, Long>(catValLoc.getGeoCode(), catValLoc.getId()));
            }
        }

        List withGeoIdMapping = new ArrayList();
        //withGeoIdMapping.add(mapregions);
        withGeoIdMapping.add(indicatorValueMap);
        withGeoIdMapping.add(allRegs);
        
        //Select regional dashboard if exists or another if there is no other option.
        AmpDashboard dashboard = null;
        List<AmpDashboard> allDashboards = DbUtil.getAllDashboards();
        Iterator<AmpDashboard> iDashboards = allDashboards.iterator();
        while(iDashboards.hasNext()) {
        	dashboard = iDashboards.next();
        	if(dashboard.getBaseType() == Constants.DashboardType.REGION) {
        		break;
        	}
        }        
        //Added another element to the array that holds extra info (ie: dashboard id).
        List extraInfo = new ArrayList();
        extraInfo.add(dashboard);
        withGeoIdMapping.add(extraInfo);

        jsonArray.addAll(withGeoIdMapping);
        PrintWriter pw = response.getWriter();
        pw.write(jsonArray.toString());
        pw.flush();
        pw.close();
        return null;
    }
    
	private ActionForward modeExportToCsv(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		long bigStart = System.currentTimeMillis();
//		StringBuffer line = new StringBuffer();
	    DataDispatcherForm maphelperform = (DataDispatcherForm) form;
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		List<AmpActivityVersion> list = new ArrayList<AmpActivityVersion>();
		
//		long startTS=System.currentTimeMillis();
//		list = DbHelper.getActivities(maphelperform.getFilter());
//		long endTS=System.currentTimeMillis();
//		logger.info("getActivities with " + list.size() + " results in " + (endTS - startTS)  + " ms");
		
		logger.info("Iteration Starts");
	    OutputStream out = response.getOutputStream();
        XlsHelper Xls = new XlsHelper(); 
		try {
			Xls.XlsMaker(request, response, maphelperform.getFilter()).write(out);
			out.flush();
			out.close();
		}catch (Exception e) {
			logger.error("error doing the XLS maps export", e);
		}
		long bigEnd = System.currentTimeMillis();
		long bigDelta = bigEnd - bigStart;
		logger.info("doing a CSV export took " + (bigDelta / 1000.0) + " seconds");
		return null;
	}

	/**
	 * calculates per-activity totals and percentages
	 * @param activityPoints
	 */
	protected void fetchFundingInfo(Map<Long, ActivityPoint> activityPoints, String userCurrencyCode)
	{
		Set<Long> relevantAaids = new HashSet<Long>();
		for(Long aaid:activityPoints.keySet())
			if (!activityPoints.get(aaid).getLocations().isEmpty())
				relevantAaids.add(aaid); // add the ids of the activities
		
		Map<Long, FundingCalculationsHelper> calculators = new HashMap<Long, FundingCalculationsHelper>();
		for(Long aaid:relevantAaids)
			calculators.put(aaid, new FundingCalculationsHelper());
		
		String activityIdsCondition = "WHERE af.ampActivityId.ampActivityId IN (" + Util.toCSStringForIN(relevantAaids) + ")";
		List<Object[]> fundingInfo = PersistenceManager.getSession().createQuery("SELECT af.ampActivityId.ampActivityId, af FROM " + AmpFunding.class.getName() + " af " + activityIdsCondition).list();
		
		for(Object[] donor:fundingInfo)
		{
			Long actId = PersistenceManager.getLong(donor[0]);
			AmpFunding funding = (AmpFunding) donor[1];
			calculators.get(actId).doCalculations(funding, userCurrencyCode);
		}
		
		double totalComm = 0, totalDisb = 0;
		for(Long aaid:relevantAaids)
		{
			FundingCalculationsHelper calculator = calculators.get(aaid);
			ActivityPoint ap = activityPoints.get(aaid);
			
			ap.setCommitments(calculator.getTotalCommitments().toString());
			ap.setDisbursements(calculator.getTotActualDisb().toString());
			
			totalComm += calculator.getTotalCommitments().doubleValue();
			totalDisb += calculator.getTotActualDisb().doubleValue();
			
			for(SimpleLocation sl:ap.getLocations())
				if (sl.getPercentage() != null)
				{
					BigDecimal percentage = new BigDecimal(Double.parseDouble(sl.getPercentage()));
					sl.setCommitments(QueryUtil.getPercentage(calculator.getTotalCommitments().getValue(), percentage));
					sl.setDisbursements(QueryUtil.getPercentage(calculator.getTotActualDisb().getValue(), percentage));
					sl.setExpenditures(QueryUtil.getPercentage(calculator.getTotActualExp().getValue(), percentage));
				}
		}
		logger.info(String.format("showNational found %.2f comms and %.2f disbs\n", totalComm, totalDisb));	
	}
	
	public ActionForward modeShowNational(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);
		long startTS = System.currentTimeMillis();

		JSONArray jsonArray = new JSONArray();
		
		// fetch activity names
		List<Long> actIds = DbHelper.getActivitiesIds(maphelperform.getFilter(), null);
		String activityIdsCondition = "WHERE amp_activity_id IN (" + Util.toCSStringForIN(actIds) + ")";		
		Map<Long, ActivityPoint> activityPoints = new HashMap<Long, ActivityPoint>();		
		
		fetchActivityIdentificationInfo(activityPoints, activityIdsCondition);
		
		long mda = System.currentTimeMillis();
		fetchDonorInfo(activityPoints, activityIdsCondition);
		logger.info("fetching donor info took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");
				
		mda = System.currentTimeMillis();
		fetchLocationsInfo(activityPoints, maphelperform.getFilter().getSelLocationIds(), activityIdsCondition, true);
		logger.info("fetching location info took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");

		mda = System.currentTimeMillis();
		fetchFundingInfo(activityPoints, maphelperform.getFilter().getCurrencyCode());
		for(ActivityPoint aP:activityPoints.values())
		{
			if (!aP.getLocations().isEmpty()) // only national projects are filtered out
				jsonArray.add(aP);
		}

		PrintWriter pw = response.getWriter();
		pw.write(jsonArray.toString());
		pw.flush();
		pw.close();

		long endTS = System.currentTimeMillis();
		logger.info("showNational returned " + jsonArray.size() + " elements in " + (endTS - startTS) / 1000.0 + " seconds");
//		logger.info(String.format("showNational found %.2f comms and %.2f disbs\n", totalComm, totalDisb));		
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
		String currencyCode = maphelperform.getFilter().getCurrencyCode();
		ct.setCurrecycode(currencyCode);
		AmpCurrency currency = CurrencyUtil.getCurrencyByCode (currencyCode);
		ct.setCurrencyname(currency.getCurrencyName());
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
	
	/**
	 * fills activityPoints with entries, populating them with initial (identification) data
	 * @param activityPoints
	 * @param activityIdsCondition
	 */
	protected void fetchActivityIdentificationInfo(Map<Long, ActivityPoint> activityPoints, String activityIdsCondition)
	{
		long mda = System.currentTimeMillis();
		Map<Long, String> activityNames = DatabaseViewFetcher.fetchInternationalizedView("v_titles", activityIdsCondition, "amp_activity_id", "name");
		logger.info("fetching activity names took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");
		
		mda = System.currentTimeMillis();
		// fetch all the id data, as it is faster to fetch all the ampIds from the database than it is to push huge lists of ampActIds to the PSQL parser. Use the activityNames as a aaid filter
		List<Object[]> activityIdentification = PersistenceManager.getSession().createQuery("SELECT act.ampActivityId, act.ampId FROM " + AmpActivity.class.getName() + " act").list();
		for(Object[] actId:activityIdentification)
		{
			Long aaId = PersistenceManager.getLong(actId[0]);
			String ampId = PersistenceManager.getString(actId[1]);
			if (activityNames.containsKey(aaId))
			{
				ActivityPoint aP = new ActivityPoint();
				aP.setActivityname(activityNames.get(aaId));
				aP.setId(Long.toString(aaId));
				aP.setAmpactivityid(ampId);
				aP.setDonors(new ArrayList<SimpleDonor>());
				aP.setLocations(new ArrayList<SimpleLocation>());
				activityPoints.put(aaId, aP);
			}
		}
		logger.info("fetching ampIDs took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");
	}
	
	/**
	 * fills activityPoints with donor information
	 * @param activityPoints
	 * @param activityIdsCondition
	 */
	protected void fetchDonorInfo(Map<Long, ActivityPoint> activityPoints, String activityIdsCondition)
	{
		// donor info, step 1: make a list of all the existing donors, retaining for each of them the list of activities which "have" them
		Map<Long, Set<Long>> donorIds = new HashMap<Long, Set<Long>>(); // Map<ampOrgId, Set<ampActivityId>>

		List<Object[]> fundingDonors = PersistenceManager.getSession().createQuery("SELECT af.ampActivityId.ampActivityId, af.ampDonorOrgId.ampOrgId FROM " + AmpFunding.class.getName() + " af " + activityIdsCondition).list();
		for(Object[] dInfo:fundingDonors)
		{
			Long aaid = PersistenceManager.getLong(dInfo[0]);
			Long donorId = PersistenceManager.getLong(dInfo[1]);
			if (!donorIds.containsKey(donorId))
				donorIds.put(donorId, new HashSet<Long>());
			donorIds.get(donorId).add(aaid);
		}
		
		// donor info, step 2: fetch info regarding the donors and add it to the respective dIds
		List<Object[]> donorsInfo = PersistenceManager.getSession().createQuery("SELECT org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + ", org.acronym FROM " + AmpOrganisation.class.getName() + " org WHERE org.ampOrgId IN (" + Util.toCSStringForIN(donorIds.keySet()) + ")").list();
		for(Object[] dInfo:donorsInfo)
		{
			SimpleDonor donor = new SimpleDonor();
			Long donorId = PersistenceManager.getLong(dInfo[0]);
			donor.setDonorname(PersistenceManager.getString(dInfo[1]));
			donor.setDonorCode(PersistenceManager.getString(dInfo[2]));
			for(Long activityId:donorIds.get(donorId))
			{
				activityPoints.get(activityId).getDonors().add(donor);
			}
		}
	}
	
	/**
	 * returns a mapping from ampActivityId to location digests
	 * @param activityIdsCondition
	 */
	public static Map<Long, List<ActivityLocationDigest>> fetchLocationInfos(String activityIdsCondition)
	{
		Map<Long, List<ActivityLocationDigest>> res = new HashMap<Long, List<ActivityLocationDigest>>();
		List<Object[]> locationsInfo = PersistenceManager.getSession().createQuery("SELECT aal.activity.ampActivityId, aal.location.location, aal.latitude, aal.longitude, aal.locationPercentage FROM " + AmpActivityLocation.class.getName() + " aal " + activityIdsCondition).list();		
		for(Object[] locationInfo:locationsInfo)
		{
			Long ampActivityId = PersistenceManager.getLong(locationInfo[0]);
			AmpCategoryValueLocations acvl = (AmpCategoryValueLocations) locationInfo[1];
			String aalLatitude = PersistenceManager.getString(locationInfo[2]);
			String aalLongitude = PersistenceManager.getString(locationInfo[3]);
			Double aalPercentage = PersistenceManager.getDouble(locationInfo[4]);
			if (!res.containsKey(ampActivityId))
				res.put(ampActivityId, new ArrayList<ActivityLocationDigest>());
			res.get(ampActivityId).add(new ActivityLocationDigest(ampActivityId, acvl, aalLatitude, aalLongitude, aalPercentage));
		}
		return res;
	}

	/**
	 * fills activityPoints with locations info from the database
	 * @param activityPoints
	 * @param filterLocationIds
	 * @param activityIdsCondition
	 * @param nationalLevel - if true, only select national level projects (disobeying any location filters). if false, only select non-national levels (obeying location filters)
	 */
	protected void fetchLocationsInfo(Map<Long, ActivityPoint> activityPoints, Long[] filterLocationIds, String activityIdsCondition, boolean nationalLevel)
	{
		Set<Long> legalLocationIds = new HashSet<Long>(Arrays.asList(filterLocationIds));
		boolean isFiltered = (!nationalLevel) // no filtering except for the implementation level for national projects 
				&& 
				(legalLocationIds.size() > 0 && !legalLocationIds.contains(-1L));
		
		Set<Long> aaidsToDelete = new HashSet<Long>(); // ampActivityIds to mark as deleted (done through a somewhat ugly hack - by deleting locations)
		Map<Long, List<ActivityLocationDigest>> locationInfos = fetchLocationInfos(activityIdsCondition);
		//List<Object[]> locationsInfo = PersistenceManager.getSession().createQuery("SELECT aal.activity.ampActivityId, aal.location.location, aal.latitude, aal.longitude, aal.locationPercentage FROM " + AmpActivityLocation.class.getName() + " aal " + activityIdsCondition).list();		
		for(Long ampActivityId:locationInfos.keySet())
			for(ActivityLocationDigest ald:locationInfos.get(ampActivityId))
			{						
				ActivityPoint ap = activityPoints.get(ampActivityId);
			
				boolean locationPassesFilter = isFiltered ? legalLocationIds.contains(ald.acvl.getId()) : true;
				if (!locationPassesFilter)
					continue;
			
				boolean impLocationIsCountry = ald.acvl.getParentCategoryValue().getValue().equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey());
				if (nationalLevel ^ impLocationIsCountry)
				{
					if (nationalLevel)
						aaidsToDelete.add(ampActivityId); // signal this location as deleted
					continue;
				}
						
				// got till here -> nationalLevel == impLocationIsCountry, e.g. processing goes on as normal
			
				SimpleLocation sl = new SimpleLocation();
				String lat = ald.acvl.getGsLat();
				String lon = ald.acvl.getGsLong();
				
				if (ald.aalLatitude != null && ald.aalLongitude !=null && !ald.aalLatitude.isEmpty() && !ald.aalLongitude.isEmpty())
				{
					sl.setExactlocation(true);
					sl.setExactlocation_lat(ald.aalLatitude);
					sl.setExactlocation_lon(ald.aalLongitude);
				}else{
					sl.setExactlocation(false);
				}
				
				sl.setName(ald.acvl.getName());
				sl.setGeoId(ald.acvl.getGeoCode());
				sl.setLat(lat);
				sl.setLon(lon);
				if (ald.aalPercentage != null)
					sl.setPercentage(ald.aalPercentage.toString());
			
				if ("".equalsIgnoreCase(lat) && "".equalsIgnoreCase(lon)) {
					sl.setIslocated(false);
				} else {
					sl.setIslocated(true);
				}
				
				ap.getLocations().add(sl);
			}

		for(Long aaidToDelete:aaidsToDelete)
			activityPoints.get(aaidToDelete).getLocations().clear();
	}
	
	public ActionForward modeShowActivities(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		DataDispatcherForm maphelperform = (DataDispatcherForm) form;

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		maphelperform.getFilter().setTeamMember(tm);

		JSONArray jsonArray = new JSONArray();
		
		logger.info("Iteration Starts");
		long startTS = System.currentTimeMillis();

		// fetch activity names
		List<Long> actIds = DbHelper.getActivitiesIds(maphelperform.getFilter(), null);
		String activityIdsCondition = "WHERE amp_activity_id IN (" + Util.toCSStringForIN(actIds) + ")";		
		Map<Long, ActivityPoint> activityPoints = new HashMap<Long, ActivityPoint>();		
		
		fetchActivityIdentificationInfo(activityPoints, activityIdsCondition);
				
		long mda = System.currentTimeMillis();
		fetchDonorInfo(activityPoints, activityIdsCondition);
		logger.info("fetching donor info took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");

		mda = System.currentTimeMillis();
		fetchLocationsInfo(activityPoints, maphelperform.getFilter().getSelLocationIds(), activityIdsCondition, false);
		logger.info("fetching location info took " + (System.currentTimeMillis() - mda) / 1000.0 + " secs");

		for(ActivityPoint aP:activityPoints.values())
		{
			if (!aP.getLocations().isEmpty()) // only national projects are filtered out
				jsonArray.add(aP);
		}
		
		long endTS=System.currentTimeMillis();
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
		List<Long> locations = filter.buildFilteredLocationIds();
		ArrayList<SimpleLocation> mapregions = new ArrayList<SimpleLocation>();
		
		
		long startTS = System.currentTimeMillis();
		mapregions = DbHelper.getFundingByRegionList(locations, implementationLevel, filter.getCurrencyCode(), startDate, endDate, 
				/*filter.getTransactionType(),*/ CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, 
				new Integer(3), new BigDecimal(1), filter);
		long delta = System.currentTimeMillis() - startTS;
		logger.info("showHighlights took " + (delta / 1000.0) + " seconds");
		
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
	
//	/**
//	 * fetches a Map<ActivityId, ActivityName> of activities which have an id in a predetermined set
//	 * @param activityIdList
//	 * @return
//	 */
//	protected Map<Long, String> getActivityNamesByIds(List<Long> activityIdList)
//	{
//		Map<Long, String> activityNamesByAmpIds = new TreeMap<Long, String>();
//
//        Session sess = PersistenceManager.getSession();
//        String activityName = AmpActivityVersion.hqlStringForName("a.ampActivityId");
//        StringBuilder qs = new StringBuilder("select a.ampActivityId, ").
//                append(activityName).
//                append(" from ").
//                append(AmpActivityVersion.class.getName()).
//                append(" a where a.ampActivityId in (").
//                append(Util.toCSStringForIN(activityIdList)).append(")");
//
//        List<Object[]> rs = sess.createQuery(qs.toString()).list();
//		for(Object[] entry:rs)
//		{
//			Long actId = PersistenceManager.getLong(entry[0]);
//            String name = PersistenceManager.getString(entry[1]);
//			activityNamesByAmpIds.put(actId, name);
//		}
//		return activityNamesByAmpIds;
//	}
	
	/**
	 * fetches structs attached to any of the ampActivityIds given in the input
	 * @param activityIdList
	 * @return Map<ActivityId, Set<StructId>>
	 */
	public static Map<Long, Set<Long>> populateStructs(List<Long> activityIdList)
	{
		Map<Long, Set<Long>> structsByAmpIds = new TreeMap<Long, Set<Long>>();
		List<Object[]> rs = PersistenceManager.getSession().createSQLQuery("SELECT amp_activity_id, amp_structure_id FROM amp_activity_structures WHERE amp_activity_id IN (" + Util.toCSStringForIN(activityIdList) + ")").list();
		for(Object[] entry:rs)
		{
			Long actId = PersistenceManager.getLong(entry[0]);
			Long strucId = PersistenceManager.getLong(entry[1]);
			if (!structsByAmpIds.containsKey(actId))
				structsByAmpIds.put(actId, new TreeSet<Long>());
				
			structsByAmpIds.get(actId).add(strucId);
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
		
		// populate set of structures which have an image
		Set<Long> strucImages = new HashSet<Long>();
		List<Object> structureIds = PersistenceManager.getSession().createSQLQuery("SELECT DISTINCT(amp_structure_id) FROM amp_structure_img").list();
		for(Object obj:structureIds)
			strucImages.add(PersistenceManager.getLong(obj));
			
		//cache structure type names
		Map<Long, String> typeNames = new HashMap<Long, String>();
		Collection<AmpStructureType> sts = new ArrayList<AmpStructureType>();
		sts = DbHelper.getAllStructureTypes();
		for(AmpStructureType strucType:sts)
			typeNames.put(strucType.getTypeId(), strucType.getName());

        String structureTitle = AmpStructure.sqlStringForTitle("amp_structure_id");
        String structureDescription = AmpStructure.sqlStringForDescription("amp_structure_id");
		List<Object[]> structs = PersistenceManager.getSession().
                createSQLQuery(new StringBuilder("SELECT amp_structure_id, ").append(structureTitle).
                        append(" as title, ").
                        append(structureDescription).
                        append(" as description, latitude, longitude, shape, type FROM amp_structure WHERE amp_structure_id IN (").
                        append(Util.toCSStringForIN(ids)).append(")").toString()).list();
		for(Object[] struct:structs)
		{
			Long strucId = PersistenceManager.getLong(struct[0]);
			String title = PersistenceManager.getString(struct[1]);
			String description = PersistenceManager.getString(struct[2]);
			String latitude = PersistenceManager.getString(struct[3]);
			String longitude = PersistenceManager.getString(struct[4]);
			String shape = PersistenceManager.getString(struct[5]);			
				
			long typeId = PersistenceManager.getLong(struct[6]); // guaranteed not null			
			String typeName = typeNames.get(typeId);
				
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
		activityIdList = DbHelper.getActivitiesIds(maphelperform.getFilter(), null);
		
		long bbb = System.currentTimeMillis();
		long gettingActivitiesTime = bbb - aaa;
		logger.info("fetching all activities took " + gettingActivitiesTime + " millies");
		
		Map<Long, Set<Long>> structsByAmpIds = populateStructs(activityIdList);
		Map<Long, Structure> structsByIds = fetchStructs(collectIds(structsByAmpIds));
		Map<Long, String> activityNamesByIds = DatabaseViewFetcher.fetchInternationalizedView("v_titles", "WHERE amp_activity_id IN (" + Util.toCSStringForIN(activityIdList) + ")", "amp_activity_id", "name");//getActivityNamesByIds(activityIdList);
				   		 
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
   		//System.out.println("doing the second part took " + secondPartMillies);
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
		datadispatcherform.getFilter().setSelectedNatPlanObj(getLongArrayFromParameter(request.getParameter("selectedNatPlanObj")));
		datadispatcherform.getFilter().setSelectedPrimaryPrograms(getLongArrayFromParameter(request.getParameter("selectedPrimaryPrograms")));
		datadispatcherform.getFilter().setSelectedSecondaryPrograms(getLongArrayFromParameter(request.getParameter("selectedSecondaryPrograms")));
		
		
		
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
						actSect.setSectorPercentage(FormatHelper.formatPercentage(ampActSect.getSectorPercentage()));
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
