package org.digijava.module.visualization.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.visualization.util.DbUtil;
import org.jfree.data.category.DefaultCategoryDataset;

import sun.misc.BASE64Decoder;

public class DataDispatcher extends DispatchAction {
	private static Logger logger = Logger.getLogger(DataDispatcher.class);
	
	//This class will return the XMLs or CSVs needed for each kind of graph
	//It will use an instance of VisualizationFilter to handle all filters
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		return null;
	}

	public ActionForward applyFilter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (visualizationForm.getFilter().getWorkspaceOnly() != null && visualizationForm.getFilter().getWorkspaceOnly()) {
			visualizationForm.getFilter().setTeamMember(tm);
        } else {
        	visualizationForm.getFilter().setTeamMember(null);
        }
		
		visualizationForm.getFilter().setOrgGroupIds(getLongArrayFromParameter(request.getParameter("orgGroupIds")));
		visualizationForm.getFilter().setOrgIds(getLongArrayFromParameter(request.getParameter("orgIds")));	
		visualizationForm.getFilter().setZoneIds(getLongArrayFromParameter(request.getParameter("zoneIds")));
		visualizationForm.getFilter().setSectorIds(getLongArrayFromParameter(request.getParameter("sectorIds")));
		visualizationForm.getFilter().setSubSectorIds(getLongArrayFromParameter(request.getParameter("subSectorIds")));
		visualizationForm.getFilter().setRegionIds(getLongArrayFromParameter(request.getParameter("regionIds")));

		
		Long[] orgsGrpIds = visualizationForm.getFilter().getOrgGroupIds();
		Long orgsGrpId = visualizationForm.getFilter().getOrgGroupId();
		if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
			Long[] temp = {orgsGrpId};
			visualizationForm.getFilter().setSelOrgGroupIds(temp);
		} else {
			visualizationForm.getFilter().setOrgGroupId(-1l);//unset orgsGrpIds
			visualizationForm.getFilter().setSelOrgGroupIds(orgsGrpIds);
		}	
		
		Long[] orgsIds = visualizationForm.getFilter().getOrgIds();
		Long orgsId = visualizationForm.getFilter().getOrgId();
		if (orgsIds == null || orgsIds.length == 0 || orgsIds[0] == -1) {
			orgs.add(DbUtil.getOrganisation(orgsId));
		} else {
			visualizationForm.getFilter().setOrgId(-1l);//unset orgId
			for (int i = 0; i < orgsIds.length; i++) {
				Long long1 = orgsIds[i];
				if(long1 != -1){
					orgs.add(DbUtil.getOrganisation(long1));
				}
			}
		}
		visualizationForm.getFilter().setOrganizationsSelected(orgs);

		Long secsId = visualizationForm.getFilter().getSectorId();
		Long subSecsId = visualizationForm.getFilter().getSubSectorId();
		Long[] secsIds = visualizationForm.getFilter().getSectorIds();
		Long[] subSecsIds = visualizationForm.getFilter().getSubSectorIds();
		String configIdAsString=request.getParameter("selSectorConfigId");
		Long configId = null;
		if(configIdAsString!=null&&!configIdAsString.equals("")){
			configId = Long.parseLong(configIdAsString);
			visualizationForm.getFilter().setSelSectorConfigId(configId);
		}
		
		
		
		
		if ((subSecsIds == null || subSecsIds.length == 0 || subSecsIds[0] == -1) && (subSecsId == null || subSecsId == -1)) {
			if (secsIds == null || secsIds.length == 0 || secsIds[0] == -1) {
				Long[] temp = {secsId};
				visualizationForm.getFilter().setSelSectorIds(temp);
			} else {
				visualizationForm.getFilter().setSectorId(-1l);//unset sectorId
				visualizationForm.getFilter().setSelSectorIds(secsIds);
			}	
		} else {
			if (subSecsIds == null || subSecsIds.length == 0 || subSecsIds[0] == -1) {
				Long[] temp = {subSecsId};
				visualizationForm.getFilter().setSelSectorIds(temp);
			} else {
				visualizationForm.getFilter().setSubSectorId(-1l);//unset subSectorId
				visualizationForm.getFilter().setSelSectorIds(subSecsIds);
			}
		}
		
		Long regsId = visualizationForm.getFilter().getRegionId();
		Long zonesId = visualizationForm.getFilter().getZoneId();
		Long[] regsIds = visualizationForm.getFilter().getRegionIds();
		Long[] zonesIds = visualizationForm.getFilter().getZoneIds();
		
		if ((zonesIds == null || zonesIds.length == 0 || zonesIds[0] == -1) && (zonesId == null || zonesId == -1)) {
			if (regsIds == null || regsIds.length == 0 || regsIds[0] == -1) {
				Long[] temp = {regsId};
				visualizationForm.getFilter().setSelLocationIds(temp);
			} else {
				visualizationForm.getFilter().setRegionId(-1l);//unset regionId
				visualizationForm.getFilter().setSelLocationIds(regsIds);
			}
		} else {
			if (zonesIds == null || zonesIds.length == 0 || zonesIds[0] == -1) {
				Long[] temp = {zonesId};
				visualizationForm.getFilter().setSelLocationIds(temp);
			} else {
				visualizationForm.getFilter().setZoneId(-1l);//unset zoneId
				visualizationForm.getFilter().setSelLocationIds(zonesIds);
			}
		}
		
		DashboardUtil.getSummaryAndRankInformation(visualizationForm);
		
		JSONObject root = new JSONObject();
		JSONArray children = new JSONArray();
		JSONArray rankProjects = new JSONArray();
		JSONArray topProjects = new JSONArray();
		JSONObject rootProjects = new JSONObject();
		JSONArray rankSectors = new JSONArray();
		JSONArray topSectors = new JSONArray();
		JSONObject rootSectors = new JSONObject();
		JSONArray rankDonors = new JSONArray();
		JSONArray topDonors = new JSONArray();
		JSONObject rootDonors = new JSONObject();
		JSONArray rankRegions = new JSONArray();
		JSONArray topRegions = new JSONArray();
		JSONObject rootRegions = new JSONObject();
		JSONObject child = new JSONObject();
		JSONObject rootTotComms = new JSONObject();
		JSONObject rootTotDisbs = new JSONObject();
		JSONObject rootNumOfProjs = new JSONObject();
		JSONObject rootNumOfSecs = new JSONObject();
		JSONObject rootNumOfRegs = new JSONObject();
		JSONObject rootNumOfDons = new JSONObject();
		JSONObject rootAvgProjs = new JSONObject();
		
		JSONObject rootSelOrgGroups = new JSONObject();
		JSONArray selOrgGroups = new JSONArray();
		JSONObject rootSelOrgs = new JSONObject();
		JSONArray selOrgs = new JSONArray();
		JSONObject rootSelRegions = new JSONObject();
		JSONArray selRegions = new JSONArray();
		JSONObject rootSelZones = new JSONObject();
		JSONArray selZones = new JSONArray();
		JSONObject rootSelSectors = new JSONObject();
		JSONArray selSectors = new JSONArray();
		JSONObject rootSelConfigs = new JSONObject();
		JSONArray selConfigs = new JSONArray();
		JSONObject rootSelSubSectors = new JSONObject();
		JSONArray selSubSectors = new JSONArray();
		JSONObject rootOrgContacts = new JSONObject();
		JSONArray selOrgContacts = new JSONArray();
		JSONObject rootAdditionalInfo = new JSONObject();
		JSONObject selAdditionalInfo = new JSONObject();
		
		if (orgsGrpIds!=null && orgsGrpIds.length>0 && orgsGrpIds[0]!=-1) {
			for (int i = 0; i < orgsGrpIds.length; i++) {
				child.put("name", DbUtil.getOrgGroup(orgsGrpIds[i]).getOrgGrpName());
				selOrgGroups.add(child);
			}
		}
		rootSelOrgGroups.put("type", "SelOrgGroupsList");
		rootSelOrgGroups.put("list", selOrgGroups);
		children.add(rootSelOrgGroups);
		
		if (orgsIds!=null && orgsIds.length>0 && orgsIds[0]!=-1) {
			for (int i = 0; i < orgsIds.length; i++) {
				child.put("name", DbUtil.getOrganisation(orgsIds[i]).getName());
				selOrgs.add(child);
			}
		}
		rootSelOrgs.put("type", "SelOrgsList");
		rootSelOrgs.put("list", selOrgs);
		children.add(rootSelOrgs);
		
		if(visualizationForm.getFilter().getDashboardType()==1){
			Long currentOrgId=null;
			if (orgsIds == null || orgsIds.length == 0 || orgsIds[0] == -1) {
				if(orgsId!=-1){
					currentOrgId=orgsId;
				}
			}
			else{
				if(selOrgs.size()==1){
					currentOrgId=orgsIds[0];
				}
			}
			if( currentOrgId!=null){
			AmpContact contact=DbUtil.getPrimaryContactForOrganization(currentOrgId);
			if(contact!=null){
			JSONObject jcontact = new JSONObject();
			jcontact.put("title", contact.getTitle()!=null?contact.getTitle().getValue():"");
			jcontact.put("name", contact.getName()+" "+contact.getLastname());
			JSONArray emails=new JSONArray();
			JSONArray phones=new JSONArray();
			JSONArray faxes=new JSONArray();
			if(contact.getProperties()!=null){
				for (AmpContactProperty property : contact.getProperties()) {
					if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL) && property.getValue().length()>0){
						JSONObject email= new JSONObject();
						email.put("value",property.getValue());
						emails.add(email);
					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE) && property.getValueAsFormatedPhoneNum().length()>0){
						JSONObject phone= new JSONObject();
						phone.put("value",property.getValueAsFormatedPhoneNum());
						phones.add(phone);
					}else if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_FAX) && property.getValue().length()>0){
						JSONObject fax= new JSONObject();
						fax.put("value",property.getValue());
						faxes.add(fax);
					}
				}
			}
			jcontact.put("email", emails);
			jcontact.put("phones", phones);
			jcontact.put("faxes", faxes);
			selOrgContacts.add(jcontact);
			
			}
				AmpOrganisation organization=DbUtil.getOrganisation(currentOrgId);
				JSONObject jorganizationInfo = new JSONObject();
				jorganizationInfo.put("orgId", currentOrgId);
				jorganizationInfo.put("orgBackground", organization.getOrgBackground());
				jorganizationInfo.put("orgDescription", organization.getOrgDescription());
				selAdditionalInfo.put("info", jorganizationInfo);
				
			}
			rootOrgContacts.put("type", "SelOrgContact");
			rootOrgContacts.put("list", selOrgContacts);
			children.add(rootOrgContacts);
			rootAdditionalInfo.put("type","SelAdditionalInfo");
			rootAdditionalInfo.put("additionalInfo",selAdditionalInfo);
			children.add(rootAdditionalInfo);
		}
		
		    	
		if (regsIds!=null && regsIds.length>0 && regsIds[0]!=-1) {
			for (int i = 0; i < regsIds.length; i++) {
				child.put("name", LocationUtil.getAmpCategoryValueLocationById(regsIds[i]).getName());
				selRegions.add(child);
			}
		}
		rootSelRegions.put("type", "SelRegionsList");
		rootSelRegions.put("list", selRegions);
		children.add(rootSelRegions);
		
		if (zonesIds!=null && zonesIds.length>0 && zonesIds[0]!=-1) {
			for (int i = 0; i < zonesIds.length; i++) {
				child.put("name", LocationUtil.getAmpCategoryValueLocationById(zonesIds[i]).getName());
				selZones.add(child);
			}
		}
		rootSelZones.put("type", "SelZonesList");
		rootSelZones.put("list", selZones);
		children.add(rootSelZones);
		
		if (configId!=null) {
				child.put("name", SectorUtil.getClassificationConfigById(configId).getName());
				selConfigs.add(child);
		}
		rootSelConfigs.put("type", "SelSectorConfig");
		rootSelConfigs.put("list", selConfigs);
		children.add(rootSelConfigs);
		
		if (secsIds!=null && secsIds.length>0 && secsIds[0]!=-1) {
			for (int i = 0; i < secsIds.length; i++) {
				child.put("name", SectorUtil.getAmpSector(secsIds[i]).getName());
				selSectors.add(child);
			}
		}
		rootSelSectors.put("type", "SelSectorsList");
		rootSelSectors.put("list", selSectors);
		children.add(rootSelSectors);
		
		if (subSecsIds!=null && subSecsIds.length>0 && subSecsIds[0]!=-1) {
			for (int i = 0; i < subSecsIds.length; i++) {
				child.put("name", SectorUtil.getAmpSector(subSecsIds[i]).getName());
				selSubSectors.add(child);
			}
		}
		rootSelSubSectors.put("type", "SelSubSectorsList");
		rootSelSubSectors.put("list", selSubSectors);
		children.add(rootSelSubSectors);
		
		Map<AmpActivityVersion, BigDecimal> projectsList = visualizationForm.getRanksInformation().getFullProjects();
		List list = null;
		if (projectsList!=null) {
			list = new LinkedList(projectsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				AmpActivityVersion act = (AmpActivityVersion) entry.getKey();
				child.put("name", act.getName());
				child.put("id", act.getAmpActivityId());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				rankProjects.add(child);
			}
		}
		projectsList = visualizationForm.getRanksInformation().getTopProjects();
		if (projectsList!=null) {
			list = new LinkedList(projectsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				AmpActivityVersion act = (AmpActivityVersion) entry.getKey();
				child.put("name", act.getName());
				child.put("id", act.getAmpActivityId());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				topProjects.add(child);
			}
		}
		rootProjects.put("type", "ProjectsList");
		rootProjects.put("list", rankProjects);
		rootProjects.put("top", topProjects);
		children.add(rootProjects);

		Map<AmpSector, BigDecimal> sectorsList = visualizationForm.getRanksInformation().getFullSectors();
		if (sectorsList!=null) {
			list = new LinkedList(sectorsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				rankSectors.add(child);
			}
		}
		sectorsList = visualizationForm.getRanksInformation().getTopSectors();
		if (sectorsList!=null) {
			list = new LinkedList(sectorsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				topSectors.add(child);
			}
		}
		rootSectors.put("type", "SectorsList");
		rootSectors.put("list", rankSectors);
		rootSectors.put("top", topSectors);
		children.add(rootSectors);
		
		Map<AmpOrganisation, BigDecimal> donorsList = visualizationForm.getRanksInformation().getFullDonors();
		if (donorsList!=null) {
			list = new LinkedList(donorsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				rankDonors.add(child);
			}
		}
		donorsList = visualizationForm.getRanksInformation().getTopDonors();
		if (donorsList!=null) {
			list = new LinkedList(donorsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				topDonors.add(child);
			}
		}
		rootDonors.put("type", "DonorsList");
		rootDonors.put("list", rankDonors);
		rootDonors.put("top", topDonors);
		children.add(rootDonors);
		
		Map<AmpCategoryValueLocations, BigDecimal> regionsList = visualizationForm.getRanksInformation().getFullRegions();
		if (regionsList!=null) {
			list = new LinkedList(regionsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				rankRegions.add(child);
			}
		}
		regionsList = visualizationForm.getRanksInformation().getTopRegions();
		if (regionsList!=null) {
			list = new LinkedList(regionsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumber((BigDecimal) entry.getValue()));
				topRegions.add(child);
			}
		}
		rootRegions.put("type", "RegionsList");
		rootRegions.put("list", rankRegions);
		rootRegions.put("top", topRegions);
		children.add(rootRegions);
		
		rootTotComms.put("type", "TotalComms");
		rootTotComms.put("value", FormatHelper.formatNumber(visualizationForm.getSummaryInformation().getTotalCommitments()));
		rootTotComms.put("curr", visualizationForm.getFilter().getCurrencyCode());
		children.add(rootTotComms);
		
		rootTotDisbs.put("type", "TotalDisbs");
		rootTotDisbs.put("value", FormatHelper.formatNumber(visualizationForm.getSummaryInformation().getTotalDisbursements()));
		children.add(rootTotDisbs);
		
		rootNumOfProjs.put("type", "NumberOfProjs");
		rootNumOfProjs.put("value", visualizationForm.getSummaryInformation().getNumberOfProjects().toString());
		children.add(rootNumOfProjs);
		
		rootNumOfSecs.put("type", "NumberOfSecs");
		rootNumOfSecs.put("value", visualizationForm.getSummaryInformation().getNumberOfSectors().toString());
		children.add(rootNumOfSecs);
		
		rootNumOfRegs.put("type", "NumberOfRegs");
		rootNumOfRegs.put("value", visualizationForm.getSummaryInformation().getNumberOfRegions().toString());
		children.add(rootNumOfRegs);
		
		rootNumOfDons.put("type", "NumberOfDons");
		rootNumOfDons.put("value", visualizationForm.getSummaryInformation().getNumberOfDonors().toString());
		children.add(rootNumOfDons);
		
		rootAvgProjs.put("type", "AvgProjSize");
		rootAvgProjs.put("value", FormatHelper.formatNumber( visualizationForm.getSummaryInformation().getAverageProjectSize()));
		children.add(rootAvgProjs);
		
		root.put("objectType", "lists");
		root.put("children", children);

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

	public ActionForward getSectorProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		String sectorId = request.getParameter("itemId");
				
		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;
		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
		String othersTitle = "Other";
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}
        
		if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        BigDecimal sectorTotal = BigDecimal.ZERO;
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}

        Date startDate = null;
        Date endDate = null;
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        int yearsInRange=filter.getYearsInRange()-1;
        if (lineChart) {
        	yearsInRange=filter.getYearsInRangeLine()-1;
		}
        if (donut) {
        	yearsInRange=filter.getYearsInRangePie()-1;
		}
        //if(!lineChart){ // Line Chart needs a special treatment (yearly values)
        try {
	        Map map = visualizationForm.getRanksInformation().getFullSectors();
            if(sectorId != null && !sectorId.equals("-1")){
            	Long id = Long.parseLong(sectorId);
            	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), filter, null);
            }
	        
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        if (selectedYear!=null) {
	        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
	            if(sectorId != null && !sectorId.equals("-1")){
	            	Long id = Long.parseLong(sectorId);
	            	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), filter, selectedYear);
	            }
	            else
	            	map = DashboardUtil.getRankSectors(DbUtil.getSectors(filter), filter, selectedYear);
			} else {
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-yearsInRange);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
			}
	        
	        if (map==null) {
	        	map = new HashMap<AmpSector, BigDecimal>();
			}
	        
	        List list = new LinkedList(map.entrySet());
			for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        //result.put(entry.getKey(), entry.getValue());
		        sectorTotal = sectorTotal.add((BigDecimal) entry.getValue());
		    }
	        
	        StringBuffer xmlString = null;
	        StringBuffer csvString = null;
			if(format != null && format.equals("xml")){
				xmlString = new StringBuffer();
				if(sectorTotal.compareTo(BigDecimal.ZERO) == 1){
					list = new LinkedList(map.entrySet());
					Iterator it = list.iterator();
					int index = 0;
					 while(it.hasNext() && index <= 4){
						Map.Entry entry = (Map.Entry)it.next();
						AmpSector sec = (AmpSector) entry.getKey();
	 	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), sectorTotal);
	 	                if (donut){
    	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
    	                		xmlString.append("<sector name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<sector name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\"/>\n");
    	                		index++;
	     	                }
    	                }
	 				}
	 	            //Accumulate the rest in Others.
	 	            BigDecimal othersValue = new BigDecimal(0);
	 	            while(it.hasNext()){
	 	            	Map.Entry entry = (Map.Entry)it.next();
	 	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	 	            }
	 	            BigDecimal percentage = getPercentage(othersValue, sectorTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	              		xmlString.append("<sector name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<sector name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                		index++;
    	                }
	                }
	 	            //if (!othersValue.equals(BigDecimal.ZERO)) {
	 				//	xmlString.append("<sector name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, sectorTotal) + "\"/>\n");
	 	            //}
				} else {
	            	xmlString.append("<sector name=\"\">\n");
					xmlString.append("</sector>\n");
				}
			} else {
	        	csvString = new StringBuffer();
	            list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
	    		csvString.append("Sector Name");
	    		csvString.append(",");
	    		csvString.append("Amount");
	    		csvString.append("\n");
	            BigDecimal othersValue = BigDecimal.ZERO;
	            // Take the top 5
	            while(it.hasNext() && index <= 4){
	                Map.Entry entry = (Map.Entry)it.next();
	        		csvString.append(entry.getKey());
	        		csvString.append(",");
	        		csvString.append(entry.getValue());
	        		csvString.append("\n");
	        		index++;
	            }
	            while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	            }
	            if (!othersValue.equals(BigDecimal.ZERO)) {
	        		csvString.append(othersTitle);
	        		csvString.append(",");
	        		csvString.append(othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	        		csvString.append("\n");
	            }
	    		if(divide){
	    			filter.setDivideThousands(false);
	    		}
	            PrintWriter out = new PrintWriter(new OutputStreamWriter(
	        			response.getOutputStream(), "UTF-8"), true);
	    		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
	            	out.println(csvString.toString());
	            	out.close();
	            	return null;
	    		}
	        }
	          
			String sectorData = "";
			int index = 0;
			list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			csvString = new StringBuffer();
			csvString.append("Year,");
			sectorData += "<Year>";
	        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        while(it.hasNext() && index <= 4){
	            //Long[] key = it.next();
	            Map.Entry entry = (Map.Entry)it.next();
	            AmpSector sec = (AmpSector) entry.getKey();
	            csvString.append(sec.getName());
	            csvString.append("#");
	            csvString.append(sec.getAmpSectorId());
	            csvString.append(",");
	            sectorData += sec.getName() + ">";
	            for (Long i = year - yearsInRange; i <= year; i++) {
	    			DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			Long[] ids = {sec.getAmpSectorId()};
	    			newFilter.setSelSectorIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	                BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                if(allData.containsKey(i)){
	                	BigDecimal[] currentAmounts = allData.get(i);
	                	currentAmounts[index] = amount;
	                	allData.put(i, currentAmounts);
	                }
	                else
	                {
	                	BigDecimal[] currentAmounts = {BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO};
	                	currentAmounts[index] = amount;
	                	allData.put(i, currentAmounts);
	                }
	            }
	            index++;
	        }
	        csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	        
	        DashboardFilter newFilter = filter.getCopyFilterForFunding();
	        ArrayList<Long> ids = new ArrayList<Long>();
	        while(it.hasNext()){
	        	Map.Entry entry = (Map.Entry)it.next();
	            AmpSector sec = (AmpSector) entry.getKey();
				ids.add(sec.getAmpSectorId());
	        }
	        Long[] idsArray = new Long[ids.size()];
	        index = 0;
	        for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				index++;
			}
	        newFilter.setSelSectorIds(idsArray);
	        if (ids.size()!=0){
	        	csvString.append(",");
	        	csvString.append("Others");
		        sectorData += "Others";
	        }
	        csvString.append("\n");
	        for (Long i = year - yearsInRange; i <= year; i++) {
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	            BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            if (ids.size()==0){
	            	othersYearlyValue.put(i, BigDecimal.ZERO);
	            } else {
		            if(othersYearlyValue.containsKey(i)){
		            	BigDecimal currentAmount = othersYearlyValue.get(i);
		                othersYearlyValue.put(i, currentAmount.add(amount));
		            } else {
		            	othersYearlyValue.put(i, amount);
		            }
	            }
	        }
	      //Put headers
	        if (!allData.isEmpty()){
		        for (Long i = year - yearsInRange; i <= year; i++) {
		        	csvString.append(i);
		        	sectorData += "<" + i;
		        	if (list.size()>0){
			        	csvString.append(",");
			        	sectorData += ">";
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			            sectorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString();
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			            sectorData += ">";
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        	sectorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString();
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
		        		sectorData += ">";
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
			        	sectorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString();
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		sectorData += ">";
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        		sectorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString();
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		sectorData += ">";
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        		sectorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString();
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	sectorData += ">";
			        	csvString.append(othersYearlyValue.get(i));
			        	sectorData += othersYearlyValue.get(i);
		        	}
		        	csvString.append("\n");
		        }
	        }
	        visualizationForm.getExportData().setSectorTableData(sectorData);
			if(divide){
				filter.setDivideThousands(false);
			}
	        	
			if(!lineChart){ // Line Chart needs a special treatment (yearly values)
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
	        			response.getOutputStream(), "UTF-8"), true);
				out.println(xmlString.toString());
				out.close();
				return null;
				
			} else {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
						response.getOutputStream(), "UTF-8"), true);
	        	out.println(csvString.toString());
	        	out.close();
	        	return null;
			}
            
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
            
	}
	
	private BigDecimal getPercentage(BigDecimal currentSector, BigDecimal sectorTotal) {
		BigDecimal result = new BigDecimal(100).multiply(currentSector).divide(sectorTotal, 2, RoundingMode.HALF_UP);
		
		return result;
	}

	public ActionForward getDonorProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;
		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}
		
		
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
		String othersTitle = "Other";
        
		if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
		BigDecimal donorTotal = BigDecimal.ZERO;
		String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}
        
        Date startDate = null;
        Date endDate = null;
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        int yearsInRange=filter.getYearsInRange()-1;
        
        if (lineChart) {
        	yearsInRange=filter.getYearsInRangeLine()-1;
		}
        if (donut) {
        	yearsInRange=filter.getYearsInRangePie()-1;
		}

        try {
        	Map map = visualizationForm.getRanksInformation().getFullDonors();
        	Long fiscalCalendarId = filter.getFiscalCalendarId();
            
            if (selectedYear!=null) {
            	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
                endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
                map = DashboardUtil.getRankDonors(DbUtil.getDonors(filter), filter, selectedYear);
			} else {
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-yearsInRange);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
			}
            if (map==null) {
	        	map = new HashMap<AmpOrganisation, BigDecimal>();
			}
	        List list = new LinkedList(map.entrySet());
			for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        //result.put(entry.getKey(), entry.getValue());
		        donorTotal = donorTotal.add((BigDecimal) entry.getValue());
		    }
            
            StringBuffer xmlString = null;
            StringBuffer csvString = null;
    		if(format != null && format.equals("xml")){
    			xmlString = new StringBuffer();
    			if(donorTotal.compareTo(BigDecimal.ZERO) == 1){
    				list = new LinkedList(map.entrySet());
    				Iterator it = list.iterator();
    				int index = 0;
    				 while(it.hasNext() && index <= 4){
    					Map.Entry entry = (Map.Entry)it.next();
    					AmpOrganisation org = (AmpOrganisation) entry.getKey();
     	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), donorTotal);
     	               //if(percentage.compareTo(new BigDecimal(0.01)) == 1) //if this is more than 0.01
     	                if (donut){
     	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
     	                		xmlString.append("<donor name=\"" + org.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + org.getAmpOrgId() + "\"/>\n");
     	                		index++;
     	                	}
     	                } else {
     	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
     	                		xmlString.append("<donor name=\"" + org.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + org.getAmpOrgId() + "\"/>\n");
     	                		index++;
 	     	                }
     	                }
     				}
     	            //Accumulate the rest in Others.
     	            BigDecimal othersValue = new BigDecimal(0);
     	            while(it.hasNext()){
     	            	Map.Entry entry = (Map.Entry)it.next();
     	                othersValue = othersValue.add((BigDecimal) entry.getValue());
     	            }
     	           	BigDecimal percentage = getPercentage(othersValue, donorTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<donor name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<donor name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                		index++;
	                	}
	                }
	 	            
     	            //if (!othersValue.equals(BigDecimal.ZERO)) {
     				//	xmlString.append("<donor name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, donorTotal) + "\"/>\n");
     	            //}
    			}
    			else
    			{
                	xmlString.append("<donor name=\"\">\n");
    				xmlString.append("</donor>\n");
    			}
    		}
            else
            {
                csvString = new StringBuffer();
                list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
        		csvString.append("Donor Name");
        		csvString.append(",");
        		csvString.append("Amount");
        		csvString.append("\n");
                BigDecimal othersValue = BigDecimal.ZERO;
                // Take the top 5
                while(it.hasNext() && index <= 4){
                    Map.Entry entry = (Map.Entry)it.next();
            		csvString.append(entry.getKey());
            		csvString.append(",");
            		csvString.append(entry.getValue());
            		csvString.append("\n");
            		index++;
                }
                while(it.hasNext()){
                	Map.Entry entry = (Map.Entry)it.next();
                    othersValue = othersValue.add((BigDecimal) entry.getValue());
                }
                if (!othersValue.equals(BigDecimal.ZERO)) {
            		csvString.append(othersTitle);
            		csvString.append(",");
            		csvString.append(othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
            		csvString.append("\n");
                }
                PrintWriter out = new PrintWriter(new OutputStreamWriter(
            			response.getOutputStream(), "UTF-8"), true);
        		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
	            	out.println(csvString.toString());
	            	out.close();
	            	return null;
        		}
            }
    		String donorData = "";
    		int index = 0;
    		list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			csvString = new StringBuffer();
			csvString.append("Year,");
            donorData += "<Year>";
            HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
            while(it.hasNext() && index <= 4){
                //Long[] key = it.next();
                Map.Entry entry = (Map.Entry)it.next();
                AmpOrganisation org = (AmpOrganisation) entry.getKey();
                csvString.append(org.getName());
                csvString.append("#");
	            csvString.append(org.getAmpOrgId());
	            csvString.append(",");
                donorData += org.getName() + ">";
                for (Long i = year - yearsInRange; i <= year; i++) {
        			DashboardFilter newFilter = filter.getCopyFilterForFunding();
        			Long[] ids = {org.getAmpOrgId()};
        			newFilter.setOrgIds(ids);
                    startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
                    endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
                    DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
                    BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
                    if(allData.containsKey(i)){
                    	BigDecimal[] currentAmounts = allData.get(i);
                    	currentAmounts[index] = amount;
                    	allData.put(i, currentAmounts);
                    }
                    else
                    {
                    	BigDecimal[] currentAmounts = {BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO};
                    	currentAmounts[index] = amount;
                    	allData.put(i, currentAmounts);
                    }
                }
                index++;
            }
            csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
            
            DashboardFilter newFilter = filter.getCopyFilterForFunding();
            ArrayList<Long> ids = new ArrayList<Long>();
            while(it.hasNext()){
            	Map.Entry entry = (Map.Entry)it.next();
                AmpOrganisation org = (AmpOrganisation) entry.getKey();
    			ids.add(org.getAmpOrgId());
            }
            Long[] idsArray = new Long[ids.size()];
            index = 0;
            for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				index++;
			}
            newFilter.setOrgIds(idsArray);
            if (ids.size()!=0){
            	csvString.append(",");
	        	csvString.append("Others");
		        donorData += "Others";
	        }
            csvString.append("\n");
	        for (Long i = year - yearsInRange; i <= year; i++) {
                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
                BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
                if (ids.size()==0){
	            	othersYearlyValue.put(i, BigDecimal.ZERO);
	            } else {
		            if(othersYearlyValue.containsKey(i)){
	                	BigDecimal currentAmount = othersYearlyValue.get(i);
	                    othersYearlyValue.put(i, currentAmount.add(amount));
	                } else {
	                    othersYearlyValue.put(i, amount);
	                }
	            }
            }
          //Put headers
	        if (!allData.isEmpty()){
		        for (Long i = year - yearsInRange; i <= year; i++) {
		        	csvString.append(i);
		        	donorData += "<" + i;
		        	if (list.size()>0){
			        	csvString.append(",");
			        	donorData += ">";
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			            donorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString();
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			            donorData += ">";
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        	donorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString();
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
		        		donorData += ">";
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
			        	donorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString();
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		donorData += ">";
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        		donorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString();
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		donorData += ">";
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        		donorData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString();
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	donorData += ">";
			        	csvString.append(othersYearlyValue.get(i));
			        	donorData += othersYearlyValue.get(i);
		        	}
		        	csvString.append("\n");
		        }
	        }
            visualizationForm.getExportData().setDonorTableData(donorData);
    		
    		if(divide){
    			filter.setDivideThousands(false);
    		}

    		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
    			PrintWriter out = new PrintWriter(new OutputStreamWriter(
            			response.getOutputStream(), "UTF-8"), true);
    			out.println(xmlString.toString());
    			out.close();
    			return null;
    			
    		} else {
    			PrintWriter out = new PrintWriter(new OutputStreamWriter(
    					response.getOutputStream(), "UTF-8"), true);
	        	out.println(csvString.toString());
	        	out.close();
	        	return null;
    		}
            
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
	}	
	
	public ActionForward getSixthGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Long selectedYear = request.getParameter("year") != null ? Long.parseLong(request.getParameter("year")) : null;
		return null;
	}
	
	public ActionForward getAidTypeGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;


		boolean typeOfAid = request.getParameter("typeofaid") != null ? Boolean.parseBoolean(request.getParameter("typeofaid")) : false;
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        BigDecimal divideByDenominator;

        if (filter.getDivideThousands())
        	divideByDenominator=new BigDecimal(1000000000);
        else
        	divideByDenominator=new BigDecimal(1000000);
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands())
            	divideByDenominator=new BigDecimal(1000000);
            else
            	divideByDenominator=new BigDecimal(1000);
        }
        Date startDate = null;
        Date endDate = null;
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue> categoryValues = null;
        if (typeOfAid) {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        } else {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        
        int yearsInRange=filter.getYearsInRange()-1;
        if (linechart) {
        	yearsInRange=filter.getYearsInRangeLine()-1;
		}

        if (donut) {
        	yearsInRange=filter.getYearsInRangePie()-1;
		}

        /*if(selectedYear != null){
        	year = selectedYear;
        	yearsInRange = 0;
        }*/
        if (selectedYear!=null) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
		} else {
			startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-yearsInRange);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
		}
        
        BigDecimal amtTotal = BigDecimal.ZERO;
        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			DecimalWraper funding = null;
            if (typeOfAid) {
                funding = DbUtil.getFunding(filter, startDate, endDate, ampCategoryValue.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
            } else {
                funding = DbUtil.getFunding(filter, startDate, endDate, null, ampCategoryValue.getId(), filter.getTransactionType(),Constants.ACTUAL);
            }
            amtTotal = amtTotal.add(funding.getValue());
		}
       
        
       if(format != null && format.equals("xml")){
			
			StringBuffer xmlString = new StringBuffer();
			if(donut){
				if(amtTotal.compareTo(BigDecimal.ZERO) == 1){
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						DecimalWraper funding = null;
						if (typeOfAid) {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
		                } else {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(),Constants.ACTUAL);
		                }
						BigDecimal percentage = getPercentage(funding.getValue(), amtTotal);
		                if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<aidtype name=\"" + value.getValue() + "\" id=\"" + value.getId() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + value.getValue() + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
					}
				} else {
					xmlString.append("<aidtype name=\"\">\n");
					xmlString.append("</aidtype>\n");
				}
			}
			else
			{
				String aidTypeData = "";
				
				for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
					xmlString.append("<year name=\"" + i + "\">\n");
					aidTypeData += "<" + i;
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
						endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
		                DecimalWraper funding = null;
		                if (typeOfAid) {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
		                } else {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(),Constants.ACTUAL);
		                }
						xmlString.append("<aidtype category=\"" + value.getValue() + "\" id=\"" + value.getId() + "\" amount=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + i + "\"/>\n");
						aidTypeData += ">" + value.getValue() + ">" + funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					}
					xmlString.append("</year>\n");
				}
				if (typeOfAid) {
			        visualizationForm.getExportData().setAidTypeTableData(aidTypeData);
				} else {
					visualizationForm.getExportData().setFinancingInstTableData(aidTypeData);
				}
			}
			if(divide){
				filter.setDivideThousands(false);
			}
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);
			out.println(xmlString.toString());
			out.close();
			return null;
		}
        
        
        
        StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		Iterator<AmpCategoryValue> it = categoryValues.iterator();
		while (it.hasNext()){
			AmpCategoryValue value = it.next();
//            String title = TranslatorWorker.translateText(categoryValue.getValue(), opt.getLangCode(), opt.getSiteId());
			csvString.append(value.getValue());
			csvString.append("#");
            csvString.append(value.getId());
            if(it.hasNext()) 
				csvString.append(",");
			else
				csvString.append("\n");
		}
        for (Long i = year - yearsInRange; i <= year; i++) {
    		csvString.append(i);
    		csvString.append(",");
    		it = categoryValues.iterator();
    		while (it.hasNext()){
    			AmpCategoryValue value = it.next();
                // apply calendar filter
                startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i.intValue());
                endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i.intValue());
                DecimalWraper funding = null;
                if (typeOfAid) {
                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
                } else {
                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(),Constants.ACTUAL);
                }
        		csvString.append(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
    			if(it.hasNext()) 
    				csvString.append(",");
    			else
    				csvString.append("\n");
    		}
        }
		
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
    			response.getOutputStream(), "UTF-8"), true);

    	out.println(csvString.toString());

    	out.close();

    	return null;
		}
        
	
	public ActionForward getAidPredictabilityGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		
		boolean nodata = true; // for displaying no data message
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}

		Long year = filter.getYear();
		BigDecimal divideByDenominator;

        if (filter.getDivideThousands())
        	divideByDenominator=new BigDecimal(1000000000);
        else
        	divideByDenominator=new BigDecimal(1000000);
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands())
            	divideByDenominator=new BigDecimal(1000000);
            else
            	divideByDenominator=new BigDecimal(1000);
        }


        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		if (linechart) {
			yearsInRange = filter.getYearsInRangeLine() - 1;
		}
		if (donut) {
			yearsInRange = filter.getYearsInRangePie() - 1;
		}
		Long fiscalCalendarId = filter.getFiscalCalendarId();

		String plannedTitle = TranslatorWorker.translateText("Planned", locale, siteId);
        String actualTitle = TranslatorWorker.translateText("Actual", locale, siteId); ;

		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			String aidPredData = "";
			for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
				xmlString.append("<year name=\"" + i + "\">\n");
				aidPredData += "<" + i;
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.PLANNED);
				xmlString.append("<fundingtype category=\""+plannedTitle+"\" id=\"" + Constants.PLANNED + "\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + i + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.ACTUAL);
				xmlString.append("<fundingtype category=\""+actualTitle+"\" id=\"" + Constants.ACTUAL + "\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + i + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
			}
			
			visualizationForm.getExportData().setAidPredicTableData(aidPredData);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}
        
        
        StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		csvString.append(plannedTitle);
		csvString.append("#");
        csvString.append(Constants.PLANNED);
        csvString.append(",");
		csvString.append(actualTitle);
		csvString.append("#");
        csvString.append(Constants.ACTUAL);
        csvString.append("\n");

        for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
            // apply calendar filter
			csvString.append(i);
			csvString.append(",");
            Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
            Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
            DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), Constants.PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
            if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
				nodata = false;
			}
			csvString.append("\n");
		}
		if (nodata) {
//			result = new DefaultCategoryDataset();
		}
		if(divide){
			filter.setDivideThousands(false);
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				response.getOutputStream(), "UTF-8"), true);

		out.println(csvString.toString());

		out.close();

		return null;
	}
	
	public ActionForward getFundingsGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long year = filter.getYear();
		BigDecimal divideByDenominator;
		//Set it here, and unset it at the end. This should go to the Advanced Filters
		if(divide){
			filter.setDivideThousands(true);
		}

		if (filter.getDivideThousands())
			divideByDenominator = new BigDecimal(1000000000);
		else
			divideByDenominator = new BigDecimal(1000000);

		if ("true"
				.equals(FeaturesUtil
						.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
			if (filter.getDivideThousands())
				divideByDenominator = new BigDecimal(1000000);
			else
				divideByDenominator = new BigDecimal(1000);
		}

		if (year == null || year == -1) {
			year = Long.parseLong(FeaturesUtil
					.getGlobalSettingValue("Current Fiscal Year"));
		}

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		if (linechart) {
			yearsInRange = filter.getYearsInRangeLine() - 1;
		}
		if (donut) {
			yearsInRange = filter.getYearsInRangePie() - 1;
		}
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		String pledgesTranslatedTitle =TranslatorWorker.translateText("Pledges", locale, siteId) ;
		String actComTranslatedTitle =TranslatorWorker.translateText("Actual commitments", locale, siteId) ;
		String actDisbTranslatedTitle = TranslatorWorker.translateText("Actual disbursements", locale, siteId) ;
		String actExpTranslatedTitle = TranslatorWorker.translateText("Actual expenditures", locale, siteId) ;;
		// String pledgesTranslatedTitle =
		// TranslatorWorker.translateText("Pledges", opt.getLangCode(),
		// opt.getSiteId());
		// String actComTranslatedTitle =
		// TranslatorWorker.translateText("Actual commitments",
		// opt.getLangCode(), opt.getSiteId());
		// String actDisbTranslatedTitle =
		// TranslatorWorker.translateText("Actual disbursements",
		// opt.getLangCode(), opt.getSiteId());
		// String actExpTranslatedTitle =
		// TranslatorWorker.translateText("Actual expenditures",
		// opt.getLangCode(), opt.getSiteId());

		Double totalPledges;
		BigDecimal totalCommitments, totalDisbursements, totalExpenditures;

		totalPledges = 0.0;
		totalCommitments = totalDisbursements = totalExpenditures = new BigDecimal(0);
		
		ServletContext ampContext = getServlet().getServletContext();
		boolean expendituresVisible = FeaturesUtil.isVisibleFeature("Expenditures", ampContext);
		boolean pledgesVisible = FeaturesUtil.isVisibleModule("Pledges", ampContext);

		StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		csvString.append(actComTranslatedTitle);
		csvString.append("#");
        csvString.append(Constants.COMMITMENT);
        csvString.append(",");
		csvString.append(actDisbTranslatedTitle);
		csvString.append("#");
        csvString.append(Constants.DISBURSEMENT);
        if (filter.isExpendituresVisible()) {
			csvString.append(",");
			csvString.append(actExpTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.EXPENDITURE);
	    }
		if (filter.isPledgeVisible()) {
			csvString.append(",");
			csvString.append(pledgesTranslatedTitle);
		}
		csvString.append("\n");

		
		
		//TODO: Change this to use XML DOM Object
		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			String fundingData = "";
			for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
				xmlString.append("<year name=\"" + i + "\">\n");
				fundingData += "<" + i;
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				if (filter.isDisbursementsVisible()) {
					DecimalWraper fundingDisb = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.DISBURSEMENT, Constants.ACTUAL);
					xmlString
					.append("<fundingtype category=\""+TranslatorWorker.translateText("Disbursements", locale, siteId)+"\" id=\"" + Constants.DISBURSEMENT + "\" amount=\""+ fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) +  "\"  year=\"" + i + "\"/>\n");
					fundingData += ">Disbursements>"+ fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				if (filter.isCommitmentsVisible()) {
					DecimalWraper fundingComm = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.COMMITMENT, Constants.ACTUAL);
					xmlString
					.append("<fundingtype category=\""+ TranslatorWorker.translateText("Commitments", locale, siteId)+"\" id=\"" + Constants.COMMITMENT + "\" amount=\""+ fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + i + "\"/>\n");
					fundingData += ">Commitments>"+ fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				if (filter.isExpendituresVisible() && expendituresVisible) {
					DecimalWraper fundingExp = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.EXPENDITURE, Constants.ACTUAL);
					xmlString
					.append("<fundingtype category=\""+ TranslatorWorker.translateText("Expenditures", locale, siteId)+"\" id=\"" + Constants.EXPENDITURE + "\" amount=\""+ fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + i + "\"/>\n");
					fundingData += ">Expenditures>"+ fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}

				if (filter.isPledgeVisible() && pledgesVisible) {
					DecimalWraper fundingPledge = DbUtil.getPledgesFunding(filter.getOrgIds(),
							filter.getOrgGroupIds(), startDate, endDate,
							currCode);
					xmlString
					.append("<fundingtype category=\""+TranslatorWorker.translateText("Pledges", locale, siteId)+"\" amount=\""+ fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + i + "\"/>\n");
					fundingData += ">Pledges>"+ fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				xmlString.append("</year>\n");
			}

			visualizationForm.getExportData().setFundingTableData(fundingData);
			//resetting to avoid affecting all graphs
			if(divide){
				filter.setDivideThousands(false);
			}
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}
		//resetting to avoid affecting all graphs
		if(divide){
			filter.setDivideThousands(false);
		}

		for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
			// apply calendar filter
			csvString.append(i);
			csvString.append(",");
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);

			DecimalWraper fundingComm = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.COMMITMENT, Constants.ACTUAL);
			csvString
					.append(fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
			DecimalWraper fundingDisb = DbUtil.getFunding(filter, startDate,
					endDate, null, null, Constants.DISBURSEMENT,
					Constants.ACTUAL);
			csvString
					.append(fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			DecimalWraper fundingExp = new DecimalWraper();
			if (filter.isExpendituresVisible()) {
				csvString.append(",");
				fundingExp = DbUtil.getFunding(filter, startDate, endDate,
						null, null, Constants.EXPENDITURE, Constants.ACTUAL);
				csvString.append(fundingExp.getValue().divide(
						divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}

			DecimalWraper fundingPledge =  new DecimalWraper();
			if (filter.isPledgeVisible()) {
				fundingPledge = DbUtil.getPledgesFunding(filter.getOrgIds(),
						filter.getSelOrgGroupIds(), startDate, endDate,
						currCode);
				csvString.append(",");
				csvString.append(fundingPledge.getValue().divide(
						divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}
			if (fundingPledge.doubleValue() != 0
					|| fundingComm.doubleValue() != 0
					|| fundingDisb.doubleValue() != 0
					|| fundingExp.doubleValue() != 0) {
				// nodata = false;
			}
			csvString.append("\n");
			// totalPledges += fundingPledge;
			// totalCommitments = totalCommitments.add(fundingComm.getValue());
			// totalDisbursements =
			// totalDisbursements.add(fundingDisb.getValue());
			// totalExpenditures = totalExpenditures.add(fundingExp.getValue());
		}

		// if (totalPledges == 0.0&&filter.isPledgeVisible())
		// result.removeRow(pledgesTranslatedTitle);
		// if (totalCommitments.equals(new BigDecimal(0)))
		// result.removeRow(actComTranslatedTitle);
		// if (totalDisbursements.equals(new BigDecimal(0)))
		// result.removeRow(actDisbTranslatedTitle);
		// if (totalExpenditures.equals(new
		// BigDecimal(0))&&filter.isExpendituresVisible())
		// result.removeRow(actExpTranslatedTitle);
		//
		//
		// if (nodata) {
		// result = new DefaultCategoryDataset();
		// }
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				response.getOutputStream(), "UTF-8"), true);

		out.println(csvString.toString());

		out.close();

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

	        List<AmpOrganisation> orgs=new ArrayList<AmpOrganisation>();
	        try {
	        	if(orgGroupId!=null&&orgGroupId!=-1){
	        		orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, false); //TODO: Second parameter for public view
	        	}
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
		else{
			if (parentId != null && objectType != null && (objectType.equals("Config") || objectType.equals("Config"))){
				Long configId = Long.parseLong(parentId);
		        if (configId != null) {
		        	List<AmpSector> sectors=DbUtil.getParentSectorsFromConfig(configId);
	                for(AmpSector sector:sectors){
	                	JSONObject child = new JSONObject();
    					child.put("ID", sector.getAmpSectorId());
    					child.put("name",sector.getName());
    					children.add(child);
	                }
	            }
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
			}
			
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

	public ActionForward getRegionProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		String regionId = request.getParameter("itemId");

		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;
		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}
		
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
		String othersTitle = "Other";
        
		if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        BigDecimal regionTotal = BigDecimal.ZERO;
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}

        Date startDate = null;
        Date endDate = null;
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        int yearsInRange=filter.getYearsInRange()-1;
        if (lineChart) {
        	yearsInRange=filter.getYearsInRangeLine()-1;
		}
        if (donut) {
        	yearsInRange=filter.getYearsInRangePie()-1;
		}
        try {
	        Map map = visualizationForm.getRanksInformation().getFullRegions();
            if(regionId != null && !regionId.equals("-1")){
            	Long id = Long.parseLong(regionId);
            	map = DashboardUtil.getRankRegions(DbUtil.getSubRegions(id), filter, null);
            }
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        if (selectedYear!=null) {
	        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, selectedYear);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, selectedYear);
	            if(regionId != null && !regionId.equals("-1")){
	            	Long id = Long.parseLong(regionId);
	            	map = DashboardUtil.getRankRegions(DbUtil.getSubRegions(id), filter, selectedYear);
	            }
	            else
		            map = DashboardUtil.getRankRegions(DbUtil.getRegions(filter), filter, selectedYear);

	        
	        } else {
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-yearsInRange);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
			}
	        if (map==null) {
	        	map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
			}
	        List list = new LinkedList(map.entrySet());
			for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        //result.put(entry.getKey(), entry.getValue());
		        regionTotal = regionTotal.add((BigDecimal) entry.getValue());
		    }
	        
	        StringBuffer xmlString = null;
	        StringBuffer csvString = null;
			if(format != null && format.equals("xml")){
				xmlString = new StringBuffer();
				if(regionTotal.compareTo(BigDecimal.ZERO) == 1){
					list = new LinkedList(map.entrySet());
					Iterator it = list.iterator();
					int index = 0;
					 while(it.hasNext() && index <= 4){
						Map.Entry entry = (Map.Entry)it.next();
						AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
	 	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), regionTotal);
	 	                if (donut){
    	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
    	                		xmlString.append("<region name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<region name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ entry.getValue() + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\"/>\n");
    	                		index++;
	     	                }
    	                }
	 				}
	 	            //Accumulate the rest in Others.
	 	            BigDecimal othersValue = new BigDecimal(0);
	 	            while(it.hasNext()){
	 	            	Map.Entry entry = (Map.Entry)it.next();
	 	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	 	            }
	 	            BigDecimal percentage = getPercentage(othersValue, regionTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<region name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<region name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                		index++;
	                	}
	                }
	 	            //if (!othersValue.equals(BigDecimal.ZERO)) {
	 				//	xmlString.append("<region name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, regionTotal) + "\"/>\n");
	 	            //}
				} else {
	            	xmlString.append("<region name=\"\">\n");
					xmlString.append("</region>\n");
				}
			} else {
	        	csvString = new StringBuffer();
	            list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
	    		csvString.append("Region Name");
	    		csvString.append(",");
	    		csvString.append("Amount");
	    		csvString.append("\n");
	            BigDecimal othersValue = BigDecimal.ZERO;
	            // Take the top 5
	            while(it.hasNext() && index <= 4){
	                Map.Entry entry = (Map.Entry)it.next();
	        		csvString.append(entry.getKey());
	        		csvString.append(",");
	        		csvString.append(entry.getValue());
	        		csvString.append("\n");
	        		index++;
	            }
	            while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	            }
	            if (!othersValue.equals(BigDecimal.ZERO)) {
	        		csvString.append(othersTitle);
	        		csvString.append(",");
	        		csvString.append(othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	        		csvString.append("\n");
	            }
	    		if(divide){
	    			filter.setDivideThousands(false);
	    		}

	            PrintWriter out = new PrintWriter(new OutputStreamWriter(
	        			response.getOutputStream(), "UTF-8"), true);
	    		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
	            	out.println(csvString.toString());
	            	out.close();
	            	return null;
	    		}
	        }
            
			String regionData = "";
			int index = 0;
			list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			csvString = new StringBuffer();
			csvString.append("Year,");
			regionData += "<Year>";
	        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        while(it.hasNext() && index <= 4){
	            //Long[] key = it.next();
	            Map.Entry entry = (Map.Entry)it.next();
	            AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
	            csvString.append(loc.getName());
	            csvString.append("#");
	            csvString.append(loc.getId());
	            csvString.append(",");
	            regionData += loc.getName() + ">";
	            for (Long i = year - yearsInRange; i <= year; i++) {
	    			DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			Long[] ids = {loc.getId()};
	    			newFilter.setSelLocationIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	                BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                if(allData.containsKey(i)){
	                	BigDecimal[] currentAmounts = allData.get(i);
	                	currentAmounts[index] = amount;
	                	allData.put(i, currentAmounts);
	                }
	                else
	                {
	                	BigDecimal[] currentAmounts = {BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO};
	                	currentAmounts[index] = amount;
	                	allData.put(i, currentAmounts);
	                }
	            }
	            index++;
	        }
	        csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	        
	        DashboardFilter newFilter = filter.getCopyFilterForFunding();
	        ArrayList<Long> ids = new ArrayList<Long>();
	        while(it.hasNext()){
	        	Map.Entry entry = (Map.Entry)it.next();
	        	AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
				ids.add(loc.getId());
	        }
	        Long[] idsArray = new Long[ids.size()];
	        index = 0;
	        for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				index++;
			}
	        newFilter.setSelLocationIds(idsArray);
	        if (ids.size()!=0){
	        	csvString.append(",");
	        	csvString.append("Others");
		        regionData += "Others";
	        }
	        csvString.append("\n");
	        for (Long i = year - yearsInRange; i <= year; i++) {
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	            BigDecimal amount = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            if (ids.size()==0){
	            	othersYearlyValue.put(i, BigDecimal.ZERO);
	            } else {
		            if(othersYearlyValue.containsKey(i)){
		            	BigDecimal currentAmount = othersYearlyValue.get(i);
		                othersYearlyValue.put(i, currentAmount.add(amount));
		            } else {
		                othersYearlyValue.put(i, amount);
		            }
	            }
	        }
			
            //Put headers
	        if (!allData.isEmpty()){
		        for (Long i = year - yearsInRange; i <= year; i++) {
		        	csvString.append(i);
		        	regionData += "<" + i;
		        	if (list.size()>0){
			        	csvString.append(",");
			        	regionData += ">";
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			            regionData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString();
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			        	regionData += ">";
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        	regionData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString();
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
			        	regionData += ">";
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
			        	regionData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString();
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		regionData += ">";
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        		regionData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString();
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		regionData += ">";
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        		regionData += allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString();
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	regionData += ">";
			        	csvString.append(othersYearlyValue.get(i));
			        	regionData += othersYearlyValue.get(i);
		        	}
		        	csvString.append("\n");
		        }
	        }
	        visualizationForm.getExportData().setRegionTableData(regionData);
			if(divide){
				filter.setDivideThousands(false);
			}
	        
			if(!lineChart){ // Line Chart needs a special treatment (yearly values)
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
	        			response.getOutputStream(), "UTF-8"), true);
				out.println(xmlString.toString());
				out.close();
				return null;
				
			} else {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
						response.getOutputStream(), "UTF-8"), true);
	        	out.println(csvString.toString());
	        	out.close();
	        	return null;
			}
            
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load region fundings from db", e);
        }
	      
	}	

	public ActionForward getODAGrowthGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;
		
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
		String othersTitle = "Other";
        
		if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}

        Date startDate = null;
        Date endDate = null;
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        int yearsInRange=filter.getYearsInRange()-1;
        
        if (filter.getYearToCompare()==null || filter.getYearToCompare().intValue()==0 || filter.getYearToCompare().intValue()>= filter.getYear()){
        	filter.setYearToCompare(filter.getYear()-1);
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpOrganisation> donorList = new ArrayList();
        if (filter.getOrgIds()!= null && filter.getOrgIds().length > 0 && filter.getOrgIds()[0]!=-1) {
			for (int i = 0; i < filter.getOrgIds().length; i++) {
				donorList.add(DbUtil.getOrganisation(filter.getOrgIds()[i]));
			}
		} else {
			Map<AmpOrganisation, BigDecimal> map = visualizationForm.getRanksInformation().getFullDonors();
			List list = new LinkedList(map.entrySet());
		    int counter = 0;
		    for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        donorList.add((AmpOrganisation) entry.getKey());
		        counter++;
		        if (counter>=10) {
					break;
				}
		    }
		}
        //donorList = DbUtil.getDonors(filter);
        Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
        
        for (Iterator iterator = donorList.iterator(); iterator.hasNext();) {
			AmpOrganisation ampOrganisation = (AmpOrganisation) iterator.next();
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			Long[] ids = {ampOrganisation.getAmpOrgId()};
			newFilter.setOrgIds(ids);
            startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            BigDecimal amtCurrentYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
            startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYearToCompare().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYearToCompare().intValue());
            fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            BigDecimal amtPreviousYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
            if (amtCurrentYear.compareTo(BigDecimal.ZERO) == 1 && amtPreviousYear.compareTo(BigDecimal.ZERO) == 1){
            	BigDecimal growthPercent = amtCurrentYear.divide(amtPreviousYear, RoundingMode.HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100));
                map.put(ampOrganisation, growthPercent);
            }
		}
        
        Map<AmpOrganisation, BigDecimal> mapSorted = DashboardUtil.sortByValue(map,10l);
        
        StringBuffer xmlString = new StringBuffer();
        List list = new LinkedList(mapSorted.entrySet());
		Iterator it = list.iterator();
		int index = 0;
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();
			AmpOrganisation org = (AmpOrganisation) entry.getKey();
			xmlString.append("<organization name=\"" + org.getAcronym() + "\" value=\""+ entry.getValue() + "\" label=\"" + org.getName() + "\" />\n");
		}
        
		if (xmlString.length()==0) {
			xmlString.append("<organization name=\"\">\n");
			xmlString.append("</organization>\n");
		}
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
    			response.getOutputStream(), "UTF-8"), true);
		out.println(xmlString.toString());
		out.close();
		return null;
			
	}	

	
	public ActionForward setChartImageFromSnapshot(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		 
		VisualizationForm vForm = (VisualizationForm) form;
		int i = 0;
        int k = 0;
        int maxLength = request.getContentLength();
        byte[] bytes = new byte[maxLength];
        String method = request.getParameter("method");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String length = request.getParameter("length");
        String graph = request.getParameter("graph");
        logger.info("About to create image from swf - " + " Type:" + type + " Graph:" + graph + " Length:" + bytes.length);
        try {
			ServletInputStream si = request.getInputStream();
	        while (true){
	            k = si.read(bytes,i,maxLength);
	            i += k;
	            if (k <= 0)
	            break;
	        }
	
	        if (bytes != null && bytes.length>0) {
	           /*if (type==null||type.equals("")) {
	                ServletOutputStream stream = response.getOutputStream();
	                response.setContentType("application/pdf");
	                response.setContentLength(bytes.length);
	                response.setHeader("Content-Disposition",method + ";filename=" + name);
	                stream.write(bytes);
	                stream.flush();
	                stream.close();
	            }
				*/
	            if (type!=null&&(type.equals("jpg")||type.equals("png"))) {
	                BASE64Decoder decoder = new BASE64Decoder();
	                byte[] imagen=decoder.decodeBuffer(new String(bytes));
	                InputStream in = new ByteArrayInputStream(imagen);
	                BufferedImage image =  ImageIO.read(in);
	                if (graph.equals("FundingChart")) {
						vForm.getExportData().setFundingGraph(image);
						logger.info("Creating image from Funding graph");
	                }
	                if (graph.equals("AidPredictability")) {
						vForm.getExportData().setAidPredictabilityGraph(image);
						logger.info("Creating image from Aid Predictability graph");
	                }
	                if (graph.equals("AidType")) {
						vForm.getExportData().setAidTypeGraph(image);
						logger.info("Creating image from Aid Type graph");
	                }
	                if (graph.equals("FinancingInstrument")) {
						vForm.getExportData().setFinancingInstGraph(image);
						logger.info("Creating image from Financing Instrument graph");
	                }
	                if (graph.equals("DonorProfile")) {
						vForm.getExportData().setDonorGraph(image);
						logger.info("Creating image from Donor graph");
	                }
	                if (graph.equals("SectorProfile")) {
						vForm.getExportData().setSectorGraph(image);
						logger.info("Creating image from Sector graph");
	                }
	                if (graph.equals("RegionProfile")) {
						vForm.getExportData().setRegionGraph(image);
						logger.info("Creating image from Region graph");
	                }
	                if (graph.equals("ODAGrowth")) {
						vForm.getExportData().setODAGrowthGraph(image);
						logger.info("Creating image from ODA growth graph");
					}
	            }
	        } else {
	        	response.setContentType("text");
	        	response.getWriter().write("bytes is null");
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /*   
	public ActionForward getActivitiesList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		DashboardFilter filter = visualizationForm.getFilter();
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String year = request.getParameter("year");

		if (id==null && id.equals("0") && id.length()!=0) {
			return null;
		}
        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();
	    JSONObject child = new JSONObject();
	    
	    Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (year!=null && !year.equals("0") && year.length()!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, Integer.parseInt(year));
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, Integer.parseInt(year));
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue()-filter.getYearsInRange()-1);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
    	}
    	
        if (type.equals("region")){
	    	Long[] ids = {Long.parseLong(id)};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelLocationIds(ids);
            List<AmpActivityVersion> activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("sector")){
	    	Long[] ids = {Long.parseLong(id)};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelSectorIds(ids);
            List<AmpActivityVersion> activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("donor")){
	    	Long[] ids = {Long.parseLong(id)};
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			newFilter.setOrgIds(ids);
            List<AmpActivityVersion> activities = DbUtil.getActivityList(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("funding")){
            List<AmpActivityVersion> activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, Integer.parseInt(id), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("aidPredictability")){
            List<AmpActivityVersion> activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), Integer.parseInt(id));
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("aidType")){
            List<AmpActivityVersion> activities = DbUtil.getActivityList(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
			root.put("children", children);
	    }

        if (type.equals("finInstrument")){
            List<AmpActivityVersion> activities = DbUtil.getActivityList(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), Constants.ACTUAL);
            Iterator<AmpActivityVersion> it = activities.iterator();
            while(it.hasNext()){
            	AmpActivityVersion act = it.next();
				child.put("ID", act.getAmpActivityId());
				child.put("name", act.getName());
				children.add(child);
			}
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
	*/
}
