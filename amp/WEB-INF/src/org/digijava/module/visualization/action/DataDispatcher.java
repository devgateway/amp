package org.digijava.module.visualization.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
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
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
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
		String trnStep0, trnStep8;
		trnStep0 = trnStep8 = "";
		try{
			trnStep0 = TranslatorWorker.translateText("Initializing Data Collection");
			trnStep8 = TranslatorWorker.translateText("Step 10/10: Preparing to refresh charts");
		}
		catch(Exception e){
			logger.error("Couldn't retrieve translation for progress steps");
		}
        request.getSession().setAttribute(DashboardUtil.VISUALIZATION_PROGRESS_SESSION, trnStep0);

		VisualizationForm visualizationForm = (VisualizationForm)form;
		HttpSession session = request.getSession();

		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (tm!=null && tm.getTeamAccessType().equals("Management")) {
			visualizationForm.getFilter().setFromPublicView(true);// sets as public view when team is management, so it shows only approved activities
        } 
		if (visualizationForm.getFilter().getWorkspaceOnly() != null && visualizationForm.getFilter().getWorkspaceOnly()) {
			visualizationForm.getFilter().setTeamMember(tm);
        } else {
        	visualizationForm.getFilter().setTeamMember(null);
        }
		
		if (request.getParameter("orgGroupIds")!=null && !request.getParameter("orgGroupIds").equals("null"))
			visualizationForm.getFilter().setOrgGroupIds(getLongArrayFromParameter(request.getParameter("orgGroupIds")));
		if (request.getParameter("orgIds")!=null && !request.getParameter("orgIds").equals("null"))
			visualizationForm.getFilter().setSelOrgIds(getLongArrayFromParameter(request.getParameter("orgIds")));	
		if (request.getParameter("zoneIds")!=null && !request.getParameter("zoneIds").equals("null"))
			visualizationForm.getFilter().setZoneIds(getLongArrayFromParameter(request.getParameter("zoneIds")));
		if (request.getParameter("sectorIds")!=null && !request.getParameter("sectorIds").equals("null"))
			visualizationForm.getFilter().setSectorIds(getLongArrayFromParameter(request.getParameter("sectorIds")));
		if (request.getParameter("subSectorIds")!=null && !request.getParameter("subSectorIds").equals("null"))
			visualizationForm.getFilter().setSubSectorIds(getLongArrayFromParameter(request.getParameter("subSectorIds")));
		if (request.getParameter("regionIds")!=null && !request.getParameter("regionIds").equals("null"))
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
			Long[] temp = {orgsId};
			visualizationForm.getFilter().setSelOrgIds(temp);
			//visualizationForm.getFilter().setOrgIds(temp);
		} else {
			visualizationForm.getFilter().setOrgId(-1l);//unset orgId
			visualizationForm.getFilter().setSelOrgIds(orgsIds);
		}

		Long secsId = visualizationForm.getFilter().getSectorId();
		Long subSecsId = visualizationForm.getFilter().getSubSectorId();
		Long[] secsIds = visualizationForm.getFilter().getSectorIds();
		Long[] subSecsIds = visualizationForm.getFilter().getSubSectorIds();
		String configIdAsString=request.getParameter("selSectorConfigId");
		Long configId = null;
		if(configIdAsString!=null&&!configIdAsString.equals("") && !configIdAsString.equals("null")){
			configId = Long.parseLong(configIdAsString);
			visualizationForm.getFilter().setSelSectorConfigId(configId);
		}

		int arrayLength = 0; //workaround to allow multi selection of sectors and subsectors
		Long[] tempArray;
		if (secsIds != null)
			arrayLength += secsIds.length;
		if (subSecsIds != null)
			arrayLength += subSecsIds.length;
		
		if (arrayLength>0){
			tempArray = new Long[arrayLength];
			int offset = 0;
			for (int i = 0; i < secsIds.length; i++) {
				tempArray[i] = secsIds[i];
				offset = i;
			}
			if (subSecsIds != null){
				for (int i = 0; i < subSecsIds.length; i++) {
					tempArray[offset+1+i] = subSecsIds[i];
				}
			}
			visualizationForm.getFilter().setSelSectorIds(tempArray);
		} else {
			if (subSecsId!=null && subSecsId!=-1){
				Long[] temp = {subSecsId};
				visualizationForm.getFilter().setSelSectorIds(temp);
			} else {
				Long[] temp = {secsId};
				visualizationForm.getFilter().setSelSectorIds(temp);
			}
		}
		
		Long regsId = visualizationForm.getFilter().getRegionId();
		Long zonesId = visualizationForm.getFilter().getZoneId();
		Long[] regsIds = visualizationForm.getFilter().getRegionIds();
		Long[] zonesIds = visualizationForm.getFilter().getZoneIds();
		
		arrayLength = 0; //workaround to allow multi selection of regions and zones
		if (regsIds != null)
			arrayLength += regsIds.length;
		if (zonesIds != null)
			arrayLength += zonesIds.length;
		
		if (arrayLength>0){
			tempArray = new Long[arrayLength];
			int offset = 0;
			for (int i = 0; i < regsIds.length; i++) {
				tempArray[i] = regsIds[i];
				offset = i;
			}
			if (zonesIds != null){
				for (int i = 0; i < zonesIds.length; i++) {
					tempArray[offset+1+i] = zonesIds[i];
				}
			}
			visualizationForm.getFilter().setSelLocationIds(tempArray);
		} else {
			if (zonesId!=null && zonesId!=-1){
				Long[] temp = {zonesId};
				visualizationForm.getFilter().setSelLocationIds(temp);
			} else {
				Long[] temp = {regsId};
				visualizationForm.getFilter().setSelLocationIds(temp);
			}
		}
		
		DashboardUtil.getSummaryAndRankInformation(visualizationForm, request);
        request.getSession().setAttribute(DashboardUtil.VISUALIZATION_PROGRESS_SESSION, trnStep8);
		JSONObject root = new JSONObject();
		JSONArray children = new JSONArray();
		JSONArray topProjects = new JSONArray();
		JSONObject rootProjects = new JSONObject();
		JSONArray topSectors = new JSONArray();
		JSONObject rootSectors = new JSONObject();
		JSONArray topOrganizations = new JSONArray();
		JSONObject rootOrganizations = new JSONObject();
		JSONArray topRegions = new JSONArray();
		JSONObject rootRegions = new JSONObject();
		JSONArray topNPOs = new JSONArray();
		JSONObject rootNPOs = new JSONObject();
		JSONArray topPrograms = new JSONArray();
		JSONObject rootPrograms = new JSONObject();
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
				child.put("id", DbUtil.getOrgGroup(orgsGrpIds[i]).getAmpOrgGrpId());
				selOrgGroups.add(child);
			}
		}
		rootSelOrgGroups.put("type", "SelOrgGroupsList");
		rootSelOrgGroups.put("list", selOrgGroups);
		children.add(rootSelOrgGroups);
		
		if (orgsIds!=null && orgsIds.length>0 && orgsIds[0]!=-1) {
			for (int i = 0; i < orgsIds.length; i++) {
				child.put("name", DbUtil.getOrganisation(orgsIds[i]).getName());
				child.put("id", DbUtil.getOrganisation(orgsIds[i]).getAmpOrgId());
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
				child.put("id", LocationUtil.getAmpCategoryValueLocationById(regsIds[i]).getId());
				selRegions.add(child);
			}
		}
		rootSelRegions.put("type", "SelRegionsList");
		rootSelRegions.put("list", selRegions);
		children.add(rootSelRegions);
		
		if (zonesIds!=null && zonesIds.length>0 && zonesIds[0]!=-1) {
			for (int i = 0; i < zonesIds.length; i++) {
				child.put("name", LocationUtil.getAmpCategoryValueLocationById(zonesIds[i]).getName());
				child.put("id", LocationUtil.getAmpCategoryValueLocationById(zonesIds[i]).getId());
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
				child.put("id", SectorUtil.getAmpSector(secsIds[i]).getAmpSectorId());
				selSectors.add(child);
			}
		}
		rootSelSectors.put("type", "SelSectorsList");
		rootSelSectors.put("list", selSectors);
		children.add(rootSelSectors);
		
		if (subSecsIds!=null && subSecsIds.length>0 && subSecsIds[0]!=-1) {
			for (int i = 0; i < subSecsIds.length; i++) {
				child.put("name", SectorUtil.getAmpSector(subSecsIds[i]).getName());
				child.put("id", SectorUtil.getAmpSector(subSecsIds[i]).getAmpSectorId());
				selSubSectors.add(child);
			}
		}
		rootSelSubSectors.put("type", "SelSubSectorsList");
		rootSelSubSectors.put("list", selSubSectors);
		children.add(rootSelSubSectors);
		
		List list = null;
		Map<AmpActivityVersion, BigDecimal> projectsList = visualizationForm.getRanksInformation().getTopProjects();
		if (projectsList!=null) {
			list = new LinkedList(projectsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				AmpActivityVersion act = (AmpActivityVersion) entry.getKey();
				child.put("name", act.getName());
				child.put("id", act.getAmpActivityId());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topProjects.add(child);
			}
		}
		rootProjects.put("type", "ProjectsList");
		rootProjects.put("top", topProjects);
		children.add(rootProjects);

		Map<AmpSector, BigDecimal> sectorsList = visualizationForm.getRanksInformation().getTopSectors();
		if (sectorsList!=null) {
			list = new LinkedList(sectorsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topSectors.add(child);
			}
		}
		rootSectors.put("type", "SectorsList");
		rootSectors.put("top", topSectors);
		children.add(rootSectors);
		
		Map<AmpOrganisation, BigDecimal> organizationsList = visualizationForm.getRanksInformation().getTopOrganizations();
		if (organizationsList!=null) {
			list = new LinkedList(organizationsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topOrganizations.add(child);
			}
		}
		rootOrganizations.put("type", "OrganizationsList");
		rootOrganizations.put("top", topOrganizations);
		children.add(rootOrganizations);
		
		Map<AmpCategoryValueLocations, BigDecimal> regionsList = visualizationForm.getRanksInformation().getTopRegions();
		if (regionsList!=null) {
			list = new LinkedList(regionsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topRegions.add(child);
			}
		}
		rootRegions.put("type", "RegionsList");
		rootRegions.put("top", topRegions);
		children.add(rootRegions);
		
		Map<AmpTheme, BigDecimal> NPOsList = visualizationForm.getRanksInformation().getTopNPOs();
		if (NPOsList!=null) {
			list = new LinkedList(NPOsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topNPOs.add(child);
			}
		}
		rootNPOs.put("type", "NPOsList");
		rootNPOs.put("top", topNPOs);
		children.add(rootNPOs);
		
		Map<AmpTheme, BigDecimal> programsList = visualizationForm.getRanksInformation().getTopPrograms();
		if (programsList!=null) {
			list = new LinkedList(programsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
				topPrograms.add(child);
			}
		}
		rootPrograms.put("type", "ProgramsList");
		rootPrograms.put("top", topPrograms);
		children.add(rootPrograms);
		
		rootTotComms.put("type", "TotalComms");
		rootTotComms.put("value", FormatHelper.formatNumberNotRounded( visualizationForm.getSummaryInformation().getTotalCommitments().doubleValue()));
		rootTotComms.put("curr", visualizationForm.getFilter().getCurrencyCode());
		children.add(rootTotComms);
		
		rootTotDisbs.put("type", "TotalDisbs");
		rootTotDisbs.put("value", FormatHelper.formatNumberNotRounded(visualizationForm.getSummaryInformation().getTotalDisbursements().doubleValue()));
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
		rootNumOfDons.put("value", visualizationForm.getSummaryInformation().getNumberOfOrganizations().toString());
		children.add(rootNumOfDons);
		
		rootAvgProjs.put("type", "AvgProjSize");
		rootAvgProjs.put("value", FormatHelper.formatNumberNotRounded( visualizationForm.getSummaryInformation().getAverageProjectSize().doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
		children.add(rootAvgProjs);
		
		root.put("objectType", "lists");
		root.put("children", children);
        request.getSession().setAttribute(DashboardUtil.VISUALIZATION_PROGRESS_SESSION, " ");

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
				
		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		String othersTitle = TranslatorWorker.translateText("Other");
        
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), true);//IsProfile determines if the amounts were already divided

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
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

        try {
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        	Map map = null;
        	//If the startYear/endYear selected is the same as in the general filter, use the stored rank
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear())){
        		if(sectorId != null && !sectorId.equals("") && !sectorId.equals("-1")){ //If there's a selected sector, get subsectors
                	Long id = Long.parseLong(sectorId);
                	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), filter, null, null);
                }
        		else
            		map = visualizationForm.getRanksInformation().getFullSectors();
        	}
        	else 
        	{
	            if(sectorId != null && !sectorId.equals("") && !sectorId.equals("-1")){
	            	Long id = Long.parseLong(sectorId);
	            	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), filter, startYear.intValue(), endYear.intValue());
	            }
	            else
	            	map = DashboardUtil.getRankSectorsByKey(DbUtil.getSectors(filter), filter);
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
	        String headingFY = TranslatorWorker.translateText("FY");
			if(format != null && format.equals("xml")){
				StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
				xmlString = new StringBuffer();
				if(sectorTotal.compareTo(BigDecimal.ZERO) == 1){
					list = new LinkedList(map.entrySet());
					Iterator it = list.iterator();
					int index = 0;
					 while(it.hasNext() && index <= 4){
						Map.Entry entry = (Map.Entry)it.next();
						AmpSector sec = (AmpSector) entry.getKey();
	 	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), sectorTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
	 	                
	 	                if (donut){
    	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
    	                		xmlString.append("<dataField name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\"  yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
	     	                }
    	                }
	 				}
	 	            //Accumulate the rest in Others.
	 	            BigDecimal othersValue = new BigDecimal(0);
	 	            String idsArrayStr = "";
	 	            while(it.hasNext()){
	 	            	Map.Entry entry = (Map.Entry)it.next();
	 	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	 	                AmpSector sec = (AmpSector) entry.getKey();
	 	                idsArrayStr = idsArrayStr + String.valueOf(sec.getAmpSectorId()) + "_";
	 	            }
	 	            BigDecimal percentage = getPercentage(othersValue, sectorTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	              		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
    	                }
	                }
	 	            //if (!othersValue.equals(BigDecimal.ZERO)) {
	 				//	xmlString.append("<sector name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, sectorTotal) + "\"/>\n");
	 	            //}
				} else {
	            	xmlString.append("<dataField name=\"\">\n");
					xmlString.append("</dataField>\n");
				}
			} else {
	        	csvString = new StringBuffer();
	            list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
	    		csvString.append("\""+TranslatorWorker.translateText("Sector Name")+"\"");
	    		csvString.append(",");
	    		csvString.append("\""+TranslatorWorker.translateText("Amount")+"\"");
	    		csvString.append("\n");
	            BigDecimal othersValue = BigDecimal.ZERO;
	            // Take the top 5
	            while(it.hasNext()){
	                Map.Entry entry = (Map.Entry)it.next();
	        		csvString.append(entry.getKey().toString().replace(',', ';'));
	        		csvString.append(",");
	        		csvString.append(entry.getValue());
	        		csvString.append("\n");
	        		index++;
	            }
	            /*while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	            }
	            if (!othersValue.equals(BigDecimal.ZERO)) {
	        		csvString.append(othersTitle);
	        		csvString.append(",");
	        		csvString.append(othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	        		csvString.append("\n");
	            }*/
	    		if(divide){
	    			filter.setDivideThousands(false);
	    		}
	    		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    		response.setHeader("Pragma", "no-cache");
	    		response.setDateHeader("Expires", 0);
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
			sectorData += "<Year";
	        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
	        for (Long i = startYear; i <= endYear; i++) {
	        	sectorData += ">" + i;
	        }
	        while(it.hasNext()){
	            //Long[] key = it.next();
	            Map.Entry entry = (Map.Entry)it.next();
	            AmpSector sec = (AmpSector) entry.getKey();
	            if (index <= 4){
		            csvString.append(sec.getName());
		            csvString.append("#");
		            csvString.append(sec.getAmpSectorId());
		            csvString.append(",");
	            }
	            sectorData += "<" + sec.getName() + ">";
	            for (Long i = startYear; i <= endYear.intValue(); i++) {
	            	Long[] ids = {sec.getAmpSectorId()};
	            	//Long[] temp = filter.getSelSectorIds();
	            	DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			newFilter.setSelSectorIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	                //filter.setSelSectorIds(temp);
	                BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                sectorData += amount.compareTo(BigDecimal.ZERO) == 0 ? "0>" : amount.toPlainString() + ">";
	                if (index <= 4){
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
	            }
	            index++;
	        }
	        csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	        
	        ArrayList<Long> ids = new ArrayList<Long>();
	        while(it.hasNext()){
	        	Map.Entry entry = (Map.Entry)it.next();
	            AmpSector sec = (AmpSector) entry.getKey();
				ids.add(sec.getAmpSectorId());
	        }
	        Long[] idsArray = new Long[ids.size()];
	        String idsArrayStr = "";
	        index = 0;
	        for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				idsArrayStr = idsArrayStr + String.valueOf(long1) + "_";
				index++;
			}
	        if (ids.size()!=0){
	        	csvString.append(",");
	        	csvString.append("Others");
	        	csvString.append("#");
	            csvString.append(idsArrayStr);
	        }
	        csvString.append("\n");
	        for (Long i = startYear; i <= endYear.intValue(); i++) {
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            Long[] temp = filter.getSelSectorIds();
    			filter.setSelSectorIds(idsArray);
		        DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		        filter.setSelSectorIds(temp);
		        BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		        for (Long i = startYear; i <= endYear.intValue(); i++) {
		        	csvString.append(i);
		        	if (list.size()>0){
			        	csvString.append(",");
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	csvString.append(othersYearlyValue.get(i));
		        	}
		        	csvString.append("\n");
		        }
	        }
	        visualizationForm.getExportData().setSectorTableData(sectorData);
			if(divide){
				filter.setDivideThousands(false);
			}
	        	
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
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
            throw new DgException("Cannot load sector fundings by organizations from db", e);
        }
            
	}
	
	public ActionForward getProgramProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
			
		return getProgramProfile(false,mapping,form,request,response);
	}
		
	public ActionForward getNPOProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
			
		return getProgramProfile(true,mapping,form,request,response);
	}
		
	public ActionForward getProgramProfile(boolean NPO, ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

        VisualizationForm visualizationForm = (VisualizationForm) form;
		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		String sectorId = request.getParameter("itemId");
				
		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		String othersTitle = TranslatorWorker.translateText("Other");
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), true);//IsProfile determines if the amounts were already divided

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}
		
		
		BigDecimal programTotal = BigDecimal.ZERO;
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}

        Date startDate = null;
        Date endDate = null;

        try {
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        	Map map = null;
        	//If the startYear/endYear selected is the same as in the general filter, use the stored rank
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear())){
        		if (NPO) {
        			map = visualizationForm.getRanksInformation().getFullNPOs();
				} else {
					map = visualizationForm.getRanksInformation().getFullPrograms();
				}
        	}
        	else 
        	{
	            map = DashboardUtil.getRankProgramsByKey(DbUtil.getPrograms(filter,NPO), filter);
        	}
	        
	        if (map==null) {
	        	map = new HashMap<AmpTheme, BigDecimal>();
			}
	        
	        List list = new LinkedList(map.entrySet());
			for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        //result.put(entry.getKey(), entry.getValue());
		        programTotal = programTotal.add((BigDecimal) entry.getValue());
		    }
	        
	        StringBuffer xmlString = null;
	        StringBuffer csvString = null;
	        String headingFY = TranslatorWorker.translateText("FY");
			if(format != null && format.equals("xml")){
				StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
				xmlString = new StringBuffer();
				if(programTotal.compareTo(BigDecimal.ZERO) == 1){
					list = new LinkedList(map.entrySet());
					Iterator it = list.iterator();
					int index = 0;
					 while(it.hasNext() && index <= 4){
						Map.Entry entry = (Map.Entry)it.next();
						AmpTheme prog = (AmpTheme) entry.getKey();
	 	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), programTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
	 	                
	 	                if (donut){
    	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
    	                		xmlString.append("<dataField name=\"" + prog.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + prog.getAmpThemeId() + "\"  yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + prog.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + prog.getAmpThemeId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
	     	                }
    	                }
	 				}
	 	            //Accumulate the rest in Others.
	 	            BigDecimal othersValue = new BigDecimal(0);
	 	            String idsArrayStr = "";
	 	            while(it.hasNext()){
	 	            	Map.Entry entry = (Map.Entry)it.next();
	 	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	 	                AmpTheme prog = (AmpTheme) entry.getKey();
	 	                idsArrayStr = idsArrayStr + String.valueOf(prog.getAmpThemeId()) + "_";
	 	            }
	 	            BigDecimal percentage = getPercentage(othersValue, programTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	              		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
    	                }
	                }
	 	            //if (!othersValue.equals(BigDecimal.ZERO)) {
	 				//	xmlString.append("<sector name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, sectorTotal) + "\"/>\n");
	 	            //}
				} else {
	            	xmlString.append("<dataField name=\"\">\n");
					xmlString.append("</dataField>\n");
				}
			} else {
	        	csvString = new StringBuffer();
	            list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
	    		csvString.append("\""+TranslatorWorker.translateText("Program Name")+"\"");
	    		csvString.append(",");
	    		csvString.append("\""+TranslatorWorker.translateText("Amount")+"\"");
	    		csvString.append("\n");
	            BigDecimal othersValue = BigDecimal.ZERO;
	            // Take the top 5
	            while(it.hasNext()){
	                Map.Entry entry = (Map.Entry)it.next();
	        		csvString.append(entry.getKey().toString().replace(',', ';'));
	        		csvString.append(",");
	        		csvString.append(entry.getValue());
	        		csvString.append("\n");
	        		index++;
	            }
	            /*while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	            }
	            if (!othersValue.equals(BigDecimal.ZERO)) {
	        		csvString.append(othersTitle);
	        		csvString.append(",");
	        		csvString.append(othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	        		csvString.append("\n");
	            }*/
	    		if(divide){
	    			filter.setDivideThousands(false);
	    		}
	    		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    		response.setHeader("Pragma", "no-cache");
	    		response.setDateHeader("Expires", 0);
	            PrintWriter out = new PrintWriter(new OutputStreamWriter(
	        			response.getOutputStream(), "UTF-8"), true);
	    		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
	            	out.println(csvString.toString());
	            	out.close();
	            	return null;
	    		}
	        }
	          
			String programData = "";
			int index = 0;
			list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			csvString = new StringBuffer();
			csvString.append("Year,");
			programData += "<Year";
	        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
	        for (Long i = startYear; i <= endYear; i++) {
	        	programData += ">" + i;
	        }
	        while(it.hasNext()){
	            //Long[] key = it.next();
	            Map.Entry entry = (Map.Entry)it.next();
	            AmpTheme prog = (AmpTheme) entry.getKey();
	            if (index <= 4){
		            csvString.append(prog.getName().replaceAll(",", ""));
		            csvString.append("#");
		            csvString.append(prog.getAmpThemeId());
		            csvString.append(",");
	            }
	            programData += "<" + prog.getName() + ">";
	            for (Long i = startYear; i <= endYear.intValue(); i++) {
	            	Long[] ids = {prog.getAmpThemeId()};
	            	DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			newFilter.setSelProgramIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	                //filter.setSelProgramIds(null);
	                BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                programData += amount.compareTo(BigDecimal.ZERO) == 0 ? "0>" : amount.toPlainString() + ">";
	                if (index <= 4){
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
	            }
	            index++;
	        }
	        csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	        
	        ArrayList<Long> ids = new ArrayList<Long>();
	        while(it.hasNext()){
	        	Map.Entry entry = (Map.Entry)it.next();
	            AmpTheme prog = (AmpTheme) entry.getKey();
				ids.add(prog.getAmpThemeId());
	        }
	        Long[] idsArray = new Long[ids.size()];
	        String idsArrayStr = "";
	        index = 0;
	        for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				idsArrayStr = idsArrayStr + String.valueOf(long1) + "_";
				index++;
			}
	        if (ids.size()!=0){
	        	csvString.append(",");
	        	csvString.append("Others");
	        	csvString.append("#");
	            csvString.append(idsArrayStr);
	        }
	        csvString.append("\n");
	        for (Long i = startYear; i <= endYear.intValue(); i++) {
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            filter.setSelProgramIds(idsArray);
	            DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	            filter.setSelProgramIds(null);
		        BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		        for (Long i = startYear; i <= endYear.intValue(); i++) {
		        	csvString.append(i);
		        	if (list.size()>0){
			        	csvString.append(",");
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	csvString.append(othersYearlyValue.get(i));
		        	}
		        	csvString.append("\n");
		        }
	        }
	        if (NPO) {
	        	visualizationForm.getExportData().setNPOTableData(programData);
			} else {
				visualizationForm.getExportData().setProgramTableData(programData);
			}
	        
	        if(divide){
				filter.setDivideThousands(false);
			}
	        	
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
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
            throw new DgException("Cannot loading data from db", e);
        }
            
	}
	
	private BigDecimal getPercentage(BigDecimal currentSector, BigDecimal sectorTotal) {
		BigDecimal result = new BigDecimal(100).multiply(currentSector).divide(sectorTotal, 2, RoundingMode.HALF_UP);
		
		return result;
	}

	public ActionForward getOrganizationProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
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
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), true);

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}

		
		String othersTitle = "Other";
        
		BigDecimal organizationTotal = BigDecimal.ZERO;
		String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}
        
        Date startDate = null;
        Date endDate = null;

        try {
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        	Map map = null;
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear()))
        		map = visualizationForm.getRanksInformation().getFullOrganizations();
        	else {
        		HashMap<Long, AmpOrganisation> agencyList = new HashMap<Long, AmpOrganisation>();
        		Collection agencyListRed = DbUtil.getAgencies(filter);
                Iterator iter = agencyListRed.iterator();
                while (iter.hasNext()) {
                	AmpOrganisation org = (AmpOrganisation)iter.next();
                    agencyList.put(org.getAmpOrgId(), org);
                }
                map = DashboardUtil.getRankAgenciesByKey(agencyList.keySet(), filter);
        	}
        		
        	if (map==null) {
	        	map = new HashMap<AmpOrganisation, BigDecimal>();
			}
	        List list = new LinkedList(map.entrySet());
			for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        //result.put(entry.getKey(), entry.getValue());
		        organizationTotal = organizationTotal.add((BigDecimal) entry.getValue());
		    }
            
            StringBuffer xmlString = null;
            StringBuffer csvString = null;
	        String headingFY = TranslatorWorker.translateText("FY");
    		if(format != null && format.equals("xml")){
				StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
    			xmlString = new StringBuffer();
    			if(organizationTotal.compareTo(BigDecimal.ZERO) == 1){
    				list = new LinkedList(map.entrySet());
    				Iterator it = list.iterator();
    				int index = 0;
    				 while(it.hasNext() && index <= 4){
    					Map.Entry entry = (Map.Entry)it.next();
    					AmpOrganisation org = (AmpOrganisation) entry.getKey();
     	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), organizationTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
     	               //if(percentage.compareTo(new BigDecimal(0.01)) == 1) //if this is more than 0.01
     	                if (donut){
     	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
     	                		xmlString.append("<dataField name=\"" + org.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + org.getAmpOrgId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
     	                		index++;
     	                	}
     	                } else {
     	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
     	                		xmlString.append("<dataField name=\"" + org.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + org.getAmpOrgId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
     	                		index++;
 	     	                }
     	                }
     				}
     	            //Accumulate the rest in Others.
     	            BigDecimal othersValue = new BigDecimal(0);
     	            String idsArrayStr = "";
     	            while(it.hasNext()){
     	            	Map.Entry entry = (Map.Entry)it.next();
     	                othersValue = othersValue.add((BigDecimal) entry.getValue());
     	                AmpOrganisation org = (AmpOrganisation) entry.getKey();
	 	                idsArrayStr = idsArrayStr + String.valueOf(org.getAmpOrgId()) + "_";
	 	            }
     	           	BigDecimal percentage = getPercentage(othersValue, organizationTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
	                	}
	                }
	 	            
     	            //if (!othersValue.equals(BigDecimal.ZERO)) {
     				//	xmlString.append("<donor name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, donorTotal) + "\"/>\n");
     	            //}
    			}
    			else
    			{
                	xmlString.append("<dataField name=\"\">\n");
    				xmlString.append("</dataField>\n");
    			}
    		}
            else
            {
                csvString = new StringBuffer();
                list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
        		csvString.append("\""+TranslatorWorker.translateText("Organization Name")+"\"");
        		csvString.append(",");
        		csvString.append("\""+TranslatorWorker.translateText("Amount")+"\"");
        		csvString.append("\n");
                BigDecimal othersValue = BigDecimal.ZERO;

                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry)it.next();
            		csvString.append(entry.getKey().toString().replace(',', ';'));
            		csvString.append(",");
            		csvString.append(entry.getValue());
            		csvString.append("\n");
            		index++;
                }
               
        		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        		response.setHeader("Pragma", "no-cache");
        		response.setDateHeader("Expires", 0);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(
            			response.getOutputStream(), "UTF-8"), true);
        		if(!lineChart){ // Line Chart needs a special treatment (yearly values)
	            	out.println(csvString.toString());
	            	out.close();
	            	return null;
        		}
            }
    		String organizationData = "";
    		int index = 0;
    		list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			csvString = new StringBuffer();
			csvString.append("Year,");
            organizationData += "<Year";
            HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
            for (Long i = startYear; i <= endYear; i++) {
	        	organizationData += ">" + i;
	        }
	        while(it.hasNext()){
                //Long[] key = it.next();
                Map.Entry entry = (Map.Entry)it.next();
                AmpOrganisation org = (AmpOrganisation) entry.getKey();
                if (index <= 4){
	                csvString.append(org.getName());
	                csvString.append("#");
		            csvString.append(org.getAmpOrgId());
		            csvString.append(",");
                }
                organizationData += "<" + org.getName() + ">";
                for (Long i = startYear; i <= endYear; i++) {
        			Long[] ids = {org.getAmpOrgId()};
        			DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			newFilter.setSelOrgIds(ids);
                    startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
                    endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
                    DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
                    //filter.setOrgIds(temp);
                    BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
                    organizationData += amount.compareTo(BigDecimal.ZERO) == 0 ? "0>" : amount.toPlainString() + ">";
	                if (index <= 4){
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
                }
                index++;
            }
            csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
            
            ArrayList<Long> ids = new ArrayList<Long>();
            while(it.hasNext()){
            	Map.Entry entry = (Map.Entry)it.next();
                AmpOrganisation org = (AmpOrganisation) entry.getKey();
    			ids.add(org.getAmpOrgId());
            }
            Long[] idsArray = new Long[ids.size()];
            String idsArrayStr = "";
	        index = 0;
            for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				idsArrayStr = idsArrayStr + String.valueOf(long1) + "_";
				index++;
			}
            if (ids.size()!=0){
            	csvString.append(",");
	        	csvString.append("Others");
	        	csvString.append("#");
	            csvString.append(idsArrayStr);
	        }
            csvString.append("\n");
            for (Long i = startYear; i <= endYear; i++) {
                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
                DashboardFilter newFilter = filter.getCopyFilterForFunding();
    			newFilter.setSelOrgIds(idsArray);
	            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
                BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
                for (Long i = startYear; i <= endYear; i++) {
		        	csvString.append(i);
		        	if (list.size()>0){
			        	csvString.append(",");
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	csvString.append(othersYearlyValue.get(i));
		        	}
		        	csvString.append("\n");
		        }
	        }
            visualizationForm.getExportData().setOrganizationTableData(organizationData);
    		
    		if(divide){
    			filter.setDivideThousands(false);
    		}

    		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    		response.setHeader("Pragma", "no-cache");
    		response.setDateHeader("Expires", 0);
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
            throw new DgException("Cannot load sector fundings by organizations from db", e);
        }
	}	
	
	public ActionForward getAidModalityGraphData(ActionMapping mapping,
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

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}
		
		if(divide){
			filter.setDivideThousands(true);
		}

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

        Date startDate = null;
        Date endDate = null;

        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue> categoryValues = null;
        if (typeOfAid) {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        } else {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        
		startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
        endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        String headingFY = TranslatorWorker.translateText("FY");

        
        BigDecimal amtTotal = BigDecimal.ZERO;
        HashMap<AmpCategoryValue, BigDecimal> hm = new HashMap<AmpCategoryValue, BigDecimal>();
        
        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			DecimalWraper funding = null;
            if (typeOfAid) {
                funding = DbUtil.getFunding(filter, startDate, endDate, ampCategoryValue.getId(), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            } else {
                funding = DbUtil.getFunding(filter, startDate, endDate, null, ampCategoryValue.getId(), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            }
            amtTotal = amtTotal.add(funding.getValue());
            hm.put(ampCategoryValue, funding.getValue());
		}
       
        
       if(format != null && format.equals("xml")){
			
			StringBuffer xmlString = new StringBuffer();
			if(donut){
				StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
				if(amtTotal.compareTo(BigDecimal.ZERO) == 1){
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						DecimalWraper funding = null;
						if (typeOfAid) {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		                } else {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		                }
						BigDecimal percentage = getPercentage(funding.getValue(), amtTotal);
		                if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\""  +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText(value.getValue()) + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
					}
				} else {
					xmlString.append("<dataField name=\"\">\n");
					xmlString.append("</dataField>\n");
				}
			}
			else
			{
				String aidTypeData = "";
				
                for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
					startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
					xmlString.append("<year name=\"" + yearName + "\">\n");
					aidTypeData += "<" + yearName;
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					boolean hasValues = false;
					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						BigDecimal totalCategory = hm.get(value);
						if(!totalCategory.equals(BigDecimal.ZERO)){
			                DecimalWraper funding = null;
			                if (typeOfAid) {
			                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			                } else {
			                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			                }
			                hasValues = true;
							xmlString.append("<dataField category=\"" +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" amount=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
							aidTypeData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
						else
						{
							aidTypeData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + BigDecimal.ZERO.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
					}
					if (!hasValues){
						xmlString.append("<dataField category=\"Category\" id=\"0\" amount=\"0.00\" year=\"" + yearName + "\"/>\n");
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
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);
			out.println(xmlString.toString());
			out.close();
			return null;
		}
        
        
        
        StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Year");

		csvString.append("\"" + text + "\"");
		csvString.append(",");
		Iterator<AmpCategoryValue> it = categoryValues.iterator();
		while (it.hasNext()){
			AmpCategoryValue value = it.next();
            String title = TranslatorWorker.translateText(value.getValue());
			csvString.append("\"");
			csvString.append(title);
			csvString.append("#");
            csvString.append(value.getId());
			csvString.append("\"");
            if(it.hasNext()) 
				csvString.append(",");
			else
				csvString.append("\n");
		}
		BigDecimal total = new BigDecimal(0);
        for (Long i = startYear; i <= endYear; i++) {
            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
    		csvString.append(yearName);
    		csvString.append(",");
    		it = categoryValues.iterator();
    		while (it.hasNext()){
    			AmpCategoryValue value = it.next();
                // apply calendar filter
                DecimalWraper funding = null;
                if (typeOfAid) {
                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
                } else {
                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
                }
        		csvString.append(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
        		total = total.add(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
    			if(it.hasNext()) 
    				csvString.append(",");
    			else
    				csvString.append("\n");
    		}
        }
        if(total.intValue()==0){
        	csvString = new StringBuffer();
        	csvString.append("Year");
        }
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
    			response.getOutputStream(), "UTF-8"), true);

    	out.println(csvString.toString());

    	out.close();

    	return null;
		
	}
	
	public ActionForward getAidTypeGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		request.setAttribute("typeofaid",true);
		return getAidModalityGraphData(mapping, form, request, response);
		
	}
        
	public ActionForward getBudgetBreakdownGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Integer selectedYear = request.getParameter("year") != null ? Integer.valueOf(request.getParameter("year")) : null;


		//boolean typeOfAid = request.getParameter("typeofaid") != null ? Boolean.parseBoolean(request.getParameter("typeofaid")) : false;
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}
		
		if(divide){
			filter.setDivideThousands(true);
		}

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

        Date startDate = null;
        Date endDate = null;

        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue> categoryValues = null;
        
        categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_BUDGET_KEY);
        
		startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
        endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        String headingFY = TranslatorWorker.translateText("FY");

        
        BigDecimal amtTotal = BigDecimal.ZERO;
        HashMap<AmpCategoryValue, BigDecimal> hm = new HashMap<AmpCategoryValue, BigDecimal>();
        
        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			DecimalWraper funding = null;
			DashboardFilter newFilter = filter.getCopyFilterForFunding();
			Long[] selCVIds = {ampCategoryValue.getId()};
			newFilter.setSelCVIds(selCVIds);
            funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            amtTotal = amtTotal.add(funding.getValue());
            hm.put(ampCategoryValue, funding.getValue());
		}
       
        
       if(format != null && format.equals("xml")){
			
			StringBuffer xmlString = new StringBuffer();
			String budgetData = "";
			
            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				xmlString.append("<year name=\"" + yearName + "\">\n");
				budgetData += "<" + yearName;
				Iterator<AmpCategoryValue> it = categoryValues.iterator();
				Long[] budgetCVIds = new Long[categoryValues.size()];
				int j = 0;
				boolean hasValues = false;
				while (it.hasNext()){
					AmpCategoryValue value = it.next();
					budgetCVIds[j++] = value.getId();
					BigDecimal totalCategory = hm.get(value);
					if(!totalCategory.equals(BigDecimal.ZERO)){
		                DecimalWraper funding = null;
		                DashboardFilter newFilter = filter.getCopyFilterForFunding();
						Long[] selCVIds = {value.getId()};
						newFilter.setSelCVIds(selCVIds);
	                    funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		                hasValues = true;
						xmlString.append("<dataField category=\"" +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" amount=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
						budgetData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					}
					else
					{
						budgetData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + BigDecimal.ZERO.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					}
				}
				filter.setBudgetCVIds(budgetCVIds);
                DashboardFilter newFilter = filter.getCopyFilterForFunding();
				Long[] selCVIds = {-1l};
				newFilter.setSelCVIds(selCVIds);
				DecimalWraper funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\"" +TranslatorWorker.translateText("Unallocated") + "\" id=\"-1\" amount=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				budgetData += ">" + TranslatorWorker.translateText("Unallocated") + ">" + funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				if (!hasValues){
					xmlString.append("<dataField category=\"Category\" id=\"0\" amount=\"0.00\" year=\"" + yearName + "\"/>\n");
				}
				xmlString.append("</year>\n");
			}
	        visualizationForm.getExportData().setBudgetTableData(budgetData);
			if(donut){
				xmlString = new StringBuffer();
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
		        endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
		        StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
				if(amtTotal.compareTo(BigDecimal.ZERO) == 1){
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					Long[] budgetCVIds = new Long[categoryValues.size()];
					int i = 0;
					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						budgetCVIds[i++] = value.getId();
						DecimalWraper funding = null;
						DashboardFilter newFilter = filter.getCopyFilterForFunding();
						Long[] selCVIds = {value.getId()};
						newFilter.setSelCVIds(selCVIds);
		                funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
						BigDecimal percentage = getPercentage(funding.getValue(), amtTotal);
		                if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\""  +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText(value.getValue()) + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
					}
					filter.setBudgetCVIds(budgetCVIds);
	                DashboardFilter newFilter = filter.getCopyFilterForFunding();
					Long[] selCVIds = {-1l};
					newFilter.setSelCVIds(selCVIds);
					DecimalWraper funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					BigDecimal percentage = getPercentage(funding.getValue(), amtTotal);
	                if(percentage.compareTo(new BigDecimal(1)) == 1){
                		xmlString.append("<dataField name=\""  +TranslatorWorker.translateText("Unallocated") + "\" id=\"-1\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText("Unallocated") + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
                	}
				} else {
					xmlString.append("<dataField name=\"\">\n");
					xmlString.append("</dataField>\n");
				}
			}
			if(divide){
				filter.setDivideThousands(false);
			}
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);
			out.println(xmlString.toString());
			out.close();
			return null;
		}
        
        
        
        StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Year");

		csvString.append("\"" + text + "\"");
		csvString.append(",");
		Iterator<AmpCategoryValue> it = categoryValues.iterator();
		while (it.hasNext()){
			AmpCategoryValue value = it.next();
            String title = TranslatorWorker.translateText(value.getValue());
			csvString.append("\"");
			csvString.append(title);
			csvString.append("#");
            csvString.append(value.getId());
			csvString.append("\"");
			csvString.append(",");
		}
		String title = TranslatorWorker.translateText("Unallocated");
		csvString.append("\"");
		csvString.append(title);
		csvString.append("#");
        csvString.append("-1");
		csvString.append("\"");
		csvString.append("\n");
		
		BigDecimal total = new BigDecimal(0);
        for (Long i = startYear; i <= endYear; i++) {
            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
    		csvString.append(yearName);
    		csvString.append(",");
    		it = categoryValues.iterator();
    		Long[] budgetCVIds = new Long[categoryValues.size()];
			int j = 0;
			while (it.hasNext()){
    			AmpCategoryValue value = it.next();
    			DashboardFilter newFilter = filter.getCopyFilterForFunding();
    			budgetCVIds[j++] = value.getId();
				Long[] selCVIds = {value.getId()};
				newFilter.setSelCVIds(selCVIds);
                DecimalWraper funding = null;
                funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
        		csvString.append(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
        		total = total.add(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
    		}
			filter.setBudgetCVIds(budgetCVIds);
            DashboardFilter newFilter = filter.getCopyFilterForFunding();
			Long[] selCVIds = {-1l};
			newFilter.setSelCVIds(selCVIds);
			DecimalWraper funding = null;
            funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
    		csvString.append(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
    		total = total.add(funding.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append("\n");
        }
        if(total.intValue()==0){
        	csvString = new StringBuffer();
        	csvString.append("Year");
        }
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
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

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		
		boolean nodata = true; // for displaying no data message
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}

		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}

		Long fiscalCalendarId = filter.getFiscalCalendarId();

		String plannedTitle = TranslatorWorker.translateText("Planned");
        String actualTitle = TranslatorWorker.translateText("Actual"); ;

		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			String aidPredData = "";
            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
		        String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				xmlString.append("<year name=\"" + yearName + "\">\n");
				aidPredData += "<" + yearName;
	            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
			}
			
			visualizationForm.getExportData().setAidPredicTableData(aidPredData);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}
        
        
        StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Year");

		csvString.append("\"" + text + "\"");
		csvString.append(",");
		csvString.append("\"" + plannedTitle);
		csvString.append("#");
        csvString.append(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "\"");
        csvString.append(",");
		csvString.append("\"" + actualTitle);
		csvString.append("#");
        csvString.append(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "\"");
        csvString.append("\n");
        BigDecimal total = new BigDecimal(0);
        
        for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
            // apply calendar filter
            Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
            Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	        String headingFY = TranslatorWorker.translateText("FY");
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			csvString.append(yearName);
			csvString.append(",");
            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
				nodata = false;
			}
			csvString.append("\n");
		}
		if (nodata) {
//			result = new DefaultCategoryDataset();
		}
		if(total.intValue()==0){
        	csvString = new StringBuffer();
        	csvString.append("Year");
        }
		if(divide){
			filter.setDivideThousands(false);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				response.getOutputStream(), "UTF-8"), true);

		out.println(csvString.toString());

		out.close();

		return null;
	}
	
	public ActionForward getAidPredictabilityQuarterGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		
		boolean nodata = true; // for displaying no data message
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}

		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}

		Long fiscalCalendarId = filter.getFiscalCalendarId();

		String plannedTitle = TranslatorWorker.translateText("Planned");
        String actualTitle = TranslatorWorker.translateText("Actual"); ;

		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			String aidPredData = "";
            //for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, endYear.intValue());
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
				Calendar cal = Calendar.getInstance();  
				String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				DecimalWraper fundingPlanned = null;
				DecimalWraper fundingActual = null;
				Date startDateQ = null;
				Date endDateQ = null;
				//Q1
				startDateQ = startDate;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
				endDateQ = cal.getTime();
				xmlString.append("<year name=\"" + yearName + "\r\n-Q1-\">\n");
				aidPredData += "<" + yearName+ "-Q1";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-1\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-1\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q2
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
				endDateQ = cal.getTime();
				xmlString.append("<year name=\"" + yearName + "\r\n-Q2-\">\n");
				aidPredData += "<" + yearName + "-Q2";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-2\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-2\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q3
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
				endDateQ = cal.getTime();
				xmlString.append("<year name=\"" + yearName + "\r\n-Q3-\">\n");
				aidPredData += "<" + yearName + "-Q3";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-3\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-3\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q4
				startDateQ = endDateQ;
				endDateQ = endDate;
				xmlString.append("<year name=\"" + yearName + "\r\n-Q4-\">\n");
				aidPredData += "<" + yearName + "-Q4";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-4\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-4\" amount=\""+ fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				
			//}
			
			visualizationForm.getExportData().setAidPredicQuarterTableData(aidPredData);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}
        
        
        StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Period");

		csvString.append("\"" + text + "\"");
		csvString.append(",");
		csvString.append("\"" + plannedTitle);
		csvString.append("#");
        csvString.append(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "\"");
        csvString.append(",");
		csvString.append("\"" + actualTitle);
		csvString.append("#");
        csvString.append(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "\"");
        csvString.append("\n");
        BigDecimal total = new BigDecimal(0);
        
        //for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
            // apply calendar filter
            Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, endYear.intValue());
            Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	        String headingFY = TranslatorWorker.translateText("FY");
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			
			Calendar cal = Calendar.getInstance();  
			DecimalWraper fundingPlanned = null;
			DecimalWraper fundingActual = null;
			Date startDateQ = null;
			Date endDateQ = null;
			//Q1
			startDateQ = startDate;
			cal.setTime(startDate);  
			cal.add(Calendar.MONTH, 3); // add 3 month for quarter  
			endDateQ = cal.getTime();
			csvString.append(yearName+ " - Q1");
			csvString.append(",");
            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append("\n");
			//Q2
			startDateQ = endDateQ;
			cal.setTime(startDate);  
			cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
			endDateQ = cal.getTime();
			csvString.append(yearName+ " - Q2");
			csvString.append(",");
            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append("\n");
			//Q3
			startDateQ = endDateQ;
			cal.setTime(startDate);  
			cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
			endDateQ = cal.getTime();
			csvString.append(yearName+ " - Q3");
			csvString.append(",");
            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append("\n");
			//Q4
			startDateQ = endDateQ;
			endDateQ = endDate;
			csvString.append(yearName+ " - Q4");
			csvString.append(",");
            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingPlanned.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append(",");
            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			total = total.add(fundingActual.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			csvString.append("\n");
			
			if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
				nodata = false;
			}
		//}
		if (nodata) {
//			result = new DefaultCategoryDataset();
		}
		if(total.intValue()==0){
        	csvString = new StringBuffer();
        	csvString.append("Year");
        }
		if(divide){
			filter.setDivideThousands(false);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
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

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}

		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}

		Long fiscalCalendarId = filter.getFiscalCalendarId();
		String pledgesTranslatedTitle = TranslatorWorker.translateText("Pledges") ;
		String comTranslatedTitle = TranslatorWorker.translateText("Commitments") ;
		String disbTranslatedTitle = TranslatorWorker.translateText("Disbursements") ;
		String expTranslatedTitle = TranslatorWorker.translateText("Expenditures");

		Double totalPledges;
		BigDecimal totalCommitments, totalDisbursements, totalExpenditures;

		totalPledges = 0.0;
		totalCommitments = totalDisbursements = totalExpenditures = new BigDecimal(0);
		
		ServletContext ampContext = getServlet().getServletContext();
		boolean expendituresVisible = FeaturesUtil.isVisibleFeature("Expenditures", ampContext);
		boolean pledgesVisible = FeaturesUtil.isVisibleModule("Pledges", ampContext);

		StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Year");

		csvString.append("\"" + text + "\"");
		if (filter.isPledgeVisible() && pledgesVisible) {
			csvString.append(",");
			csvString.append("\"" + pledgesTranslatedTitle + "\"");
		}
		if (filter.isCommitmentsVisible()) {
			csvString.append(",");
			csvString.append("\"" + comTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.COMMITMENT + "\"");
		}
        if (filter.isDisbursementsVisible()) {
    		csvString.append(",");
	        csvString.append("\"" + disbTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.DISBURSEMENT + "\"");
        }
        if (filter.isExpendituresVisible() && expendituresVisible) {
			csvString.append(",");
			csvString.append("\"" + expTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.EXPENDITURE + "\"");
	    }
		csvString.append("\n");

		
		
		//TODO: Change this to use XML DOM Object
		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			String fundingData = "";
            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				
		        String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				xmlString.append("<year name=\"" + yearName + "\">\n");
				fundingData += "<" + yearName;
				if (filter.isPledgeVisible() && pledgesVisible) {
					DecimalWraper fundingPledge = DbUtil.getPledgesFunding(filter.getSelOrgIds(),
							filter.getOrgGroupIds(), startDate, endDate,
							currCode);
					xmlString
					.append("<dataField category=\""+TranslatorWorker.translateText("Pledges")+"\" amount=\""+ fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
					fundingData += ">" + pledgesTranslatedTitle + ">"+ fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				if (filter.isCommitmentsVisible()) {
					DecimalWraper fundingComm = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					xmlString
					.append("<dataField category=\""+ comTranslatedTitle +"\" id=\"" + Constants.COMMITMENT + "\" amount=\""+ fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
					fundingData += ">" + comTranslatedTitle + ">"+ fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				if (filter.isDisbursementsVisible()) {
					DecimalWraper fundingDisb = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					xmlString
					.append("<dataField category=\""+ disbTranslatedTitle +"\" id=\"" + Constants.DISBURSEMENT + "\" amount=\""+ fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) +  "\"  year=\"" + yearName + "\"/>\n");
					fundingData += ">" + disbTranslatedTitle + ">"+ fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}
				if (filter.isExpendituresVisible() && expendituresVisible) {
					DecimalWraper fundingExp = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					xmlString
					.append("<dataField category=\""+ expTranslatedTitle +"\" id=\"" + Constants.EXPENDITURE + "\" amount=\""+ fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
					fundingData += ">" + expTranslatedTitle + ">"+ fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				}

				xmlString.append("</year>\n");
			}

			visualizationForm.getExportData().setFundingTableData(fundingData);
			//resetting to avoid affecting all graphs
			if(divide){
				filter.setDivideThousands(false);
			}
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
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
		BigDecimal total = new BigDecimal(0);
        for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
			// apply calendar filter
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	        String headingFY = TranslatorWorker.translateText("FY");
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
			csvString.append(yearName);
			//csvString.append(",");

			DecimalWraper fundingPledge =  new DecimalWraper();
			if (filter.isPledgeVisible() && pledgesVisible) {
				fundingPledge = DbUtil.getPledgesFunding(filter.getSelOrgIds(),filter.getSelOrgGroupIds(), startDate, endDate,currCode);
				csvString.append(",");
				csvString.append(fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPledge.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}
			
			if (filter.isCommitmentsVisible()) {
				DecimalWraper fundingComm = DbUtil.getFunding(filter, startDate, endDate, null, null,Constants.COMMITMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				csvString.append(",");
				csvString.append(fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingComm.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}
			
			if (filter.isDisbursementsVisible()) {
	    		DecimalWraper fundingDisb = DbUtil.getFunding(filter, startDate,endDate, null, null, Constants.DISBURSEMENT, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	    		csvString.append(",");
	    		csvString.append(fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
	    		total = total.add(fundingDisb.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}
			
			DecimalWraper fundingExp = new DecimalWraper();
			if (filter.isExpendituresVisible() && expendituresVisible) {
				fundingExp = DbUtil.getFunding(filter, startDate, endDate,null, null, Constants.EXPENDITURE, CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				csvString.append(",");
				csvString.append(fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingExp.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
			}

			csvString.append("\n");
		}
        if(total.intValue()==0){
        	csvString = new StringBuffer();
        	csvString.append("Year");
        }
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

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
	        		orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, visualizationForm.getFilter().getFromPublicView());
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
		} else if (parentId != null && objectType != null && (objectType.equals("Config"))){
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
			
		} else if (parentId != null && objectType != null && (objectType.equals("FiscalCalendar"))){
			Long calendarId = Long.parseLong(parentId);
			int yearFrom = Integer.parseInt(FeaturesUtil
					.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
			int countYear = Integer.parseInt(FeaturesUtil
							.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
			long maxYear = yearFrom + countYear;
			if (maxYear < visualizationForm.getFilter().getStartYear()) {
				maxYear = visualizationForm.getFilter().getStartYear();
			}
			for (int i = yearFrom; i <= maxYear; i++) {
				Date startDate = DashboardUtil.getStartDate(calendarId, i);
				Date endDate = DashboardUtil.getEndDate(calendarId, i);
				String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, calendarId, startDate, endDate);
				JSONObject child = new JSONObject();
				child.put("value", i);
				child.put("key",yearName);
				children.add(child);
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
	
	public ActionForward getGraphsFromDashboard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		
		String id = request.getParameter("id");

        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();

	    List<AmpDashboardGraph> dashboardGraphs = DbUtil.getDashboardGraphByDashboard(Long.valueOf(id));
	    visualizationForm.setDashboard(DbUtil.getDashboardById(Long.valueOf(id)));
	    
	    try {
        	JSONObject child = new JSONObject();
			Iterator<AmpDashboardGraph> it = dashboardGraphs.iterator();
			while(it.hasNext()){
				AmpDashboardGraph dashboardGraph = it.next();
				child.put("ID", dashboardGraph.getId());
				String graphName = TranslatorWorker.translateText(dashboardGraph.getGraph().getName());
				child.put("name", graphName);
				children.add(child);
			}
			root.put("children", children);
        } catch (Exception e) {
            logger.error("unable to load organizations", e);
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
	
	public ActionForward getProgress(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		response.setContentType("text/plain");
		
		OutputStreamWriter outputStream = null;
		StringBuffer progressText = new StringBuffer();
		if(request.getSession().getAttribute(DashboardUtil.VISUALIZATION_PROGRESS_SESSION) != null)
			progressText.append(request.getSession().getAttribute(DashboardUtil.VISUALIZATION_PROGRESS_SESSION));
		else
			progressText.append(" ");
		

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
			outputStream.write(progressText.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}
	
	private Long[] getLongArrayFromParameter (String param){
		if (param!=null && param!="" && !param.equals("null")) {
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

		Boolean lineChart = false;
		if(request.getParameter("linechart") != null)
			lineChart = Boolean.parseBoolean(request.getParameter("linechart"));

		Boolean donut = false;
		if(request.getParameter("donut") != null)
			donut = Boolean.parseBoolean(request.getParameter("donut"));

		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear, endYear;
		if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
				&& !request.getParameter("startYear").toString().equalsIgnoreCase("") && !request.getParameter("endYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("startYear"));
			endYear = Long.parseLong(request.getParameter("endYear"));
		}
		else
		{
			startYear = filter.getStartYear();
			endYear = filter.getEndYear();
		}
		
		if(divide){
			filter.setDivideThousands(true);
		}

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), true);

		String othersTitle = TranslatorWorker.translateText("Other");
        
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

        try {
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
        	Map map = null;
        	//If the startYear/endYear selected is the same as in the general filter, use the stored rank
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear())){
	            if(regionId != null && !regionId.equals("") && !regionId.equals("-1")){
	            	Long id = Long.parseLong(regionId);
	            	map = DashboardUtil.getRankRegionsByKey(DbUtil.getSubRegions(id), filter,request);
	            }
	            else
	        		map = visualizationForm.getRanksInformation().getFullRegions();
	            	
            }
        	else
        	{
	            if(regionId != null && !regionId.equals("") && !regionId.equals("-1")){
	            	Long id = Long.parseLong(regionId);
	            	map = DashboardUtil.getRankRegionsByKey(DbUtil.getSubRegions(id), filter,request);
	            }
	            else
	            	map = DashboardUtil.getRankRegionsByKey(DbUtil.getRegions(filter), filter,request);
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
	        String headingFY = TranslatorWorker.translateText("FY");
			if(format != null && format.equals("xml")){
				StringBuffer yearLabels = new StringBuffer();
				for (int i = startDate.getYear(); i <= endDate.getYear(); i++) {
					Date startDateTemp = DashboardUtil.getStartDate(fiscalCalendarId, 1900+i);
					Date endDateTemp = DashboardUtil.getEndDate(fiscalCalendarId, 1900+i);
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDateTemp, endDateTemp);
					yearLabels.append(yearName);
					if(i != endDate.getYear())
						yearLabels.append(",");
				}
				xmlString = new StringBuffer();
				if(regionTotal.compareTo(BigDecimal.ZERO) == 1){
					list = new LinkedList(map.entrySet());
					Iterator it = list.iterator();
					int index = 0;
					 while(it.hasNext() && index <= 4){
						Map.Entry entry = (Map.Entry)it.next();
						AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
	 	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), regionTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
	 	                if (donut){
    	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
    	                		xmlString.append("<dataField name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
	     	                }
    	                }
	 				}
	 	            //Accumulate the rest in Others.
	 	            BigDecimal othersValue = new BigDecimal(0);
	 	            String idsArrayStr = "";
	 	            while(it.hasNext()){
	 	            	Map.Entry entry = (Map.Entry)it.next();
	 	                othersValue = othersValue.add((BigDecimal) entry.getValue());
	 	                AmpCategoryValueLocations acvl = (AmpCategoryValueLocations) entry.getKey();
	 	                idsArrayStr = idsArrayStr + String.valueOf(acvl.getId()) + "_";
	 	            }
	 	            BigDecimal percentage = getPercentage(othersValue, regionTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
	                	}
	                }
	 	            //if (!othersValue.equals(BigDecimal.ZERO)) {
	 				//	xmlString.append("<region name=\"" + othersTitle + "\" value=\""+ othersValue.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + getPercentage(othersValue, regionTotal) + "\"/>\n");
	 	            //}
				} else {
	            	xmlString.append("<dataField name=\"\">\n");
					xmlString.append("</dataField>\n");
				}
			} else {
	        	csvString = new StringBuffer();
	            list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				int index = 0;
	    		csvString.append("\""+TranslatorWorker.translateText("Region Name")+"\"");
	    		csvString.append(",");
	    		csvString.append("\""+TranslatorWorker.translateText("Amount")+"\"");
	    		csvString.append("\n");
	            BigDecimal othersValue = BigDecimal.ZERO;
	            
	            while(it.hasNext()){
	                Map.Entry entry = (Map.Entry)it.next();
	        		csvString.append(entry.getKey().toString().replace(',', ';'));
	        		csvString.append(",");
	        		csvString.append(entry.getValue());
	        		csvString.append("\n");
	        		index++;
	            }
	            
	    		if(divide){
	    			filter.setDivideThousands(false);
	    		}

	    		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    		response.setHeader("Pragma", "no-cache");
	    		response.setDateHeader("Expires", 0);
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
			regionData += "<Year";
	        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
	        divideByDenominator = DashboardUtil.getDividingDenominator(filter.getDivideThousands(), filter.shouldShowAmountsInThousands(), false);
	        for (Long i = startYear; i <= endYear; i++) {
	        	regionData += ">" + i;
	        }
	        while(it.hasNext()){
	            //Long[] key = it.next();
	            Map.Entry entry = (Map.Entry)it.next();
	            AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
	            if (index <= 4){
		            csvString.append(loc.getName());
		            csvString.append("#");
		            csvString.append(loc.getId());
		            csvString.append(",");
	            }
	            regionData += "<" + loc.getName() + ">";
                for (Long i = startYear; i <= endYear; i++) {
                	Long[] ids = {loc.getId()};
                	DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			newFilter.setSelLocationIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	                //filter.setSelLocationIds(tempLocationsIds);
	                BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                regionData += amount.compareTo(BigDecimal.ZERO) == 0 ? "0>" : amount.toPlainString() + ">";
	                if (index <= 4){
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
	            }
	            index++;
	        }
	        csvString.deleteCharAt(csvString.length()-1);
	        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	        
	        ArrayList<Long> ids = new ArrayList<Long>();
	        while(it.hasNext()){
	        	Map.Entry entry = (Map.Entry)it.next();
	        	AmpCategoryValueLocations loc = (AmpCategoryValueLocations) entry.getKey();
				ids.add(loc.getId());
	        }
	        Long[] idsArray = new Long[ids.size()];
	        String idsArrayStr = "";
	        index = 0;
	        for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
				Long long1 = (Long) iterator.next();
				idsArray[index] = long1;
				idsArrayStr = idsArrayStr + String.valueOf(long1) + "_";
				index++;
			}
	        if (ids.size()!=0){
	        	csvString.append(",");
	        	csvString.append("Others");
	        	csvString.append("#");
	            csvString.append(idsArrayStr);
	        }
	        csvString.append("\n");
            for (Long i = startYear; i <= endYear; i++) {
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	            Long[] temp = filter.getSelLocationIds();
	            filter.setSelLocationIds(idsArray);
		        DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
		        filter.setSelLocationIds(temp);
		        BigDecimal amount = fundingCal.getValue().divide(divideByDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
                for (Long i = startYear; i <= endYear; i++) {
		        	csvString.append(i);
		        	if (list.size()>0){
			        	csvString.append(",");
			            csvString.append(allData.get(i)[0].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[0].toPlainString());
			        }
		        	if (list.size()>1){
			            csvString.append(",");
			        	csvString.append(allData.get(i)[1].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[1].toPlainString());
			        }
		        	if (list.size()>2){
		        		csvString.append(",");
			        	csvString.append(allData.get(i)[2].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[2].toPlainString());
		        	}
		        	if (list.size()>3){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[3].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[3].toPlainString());
		        	}
		        	if (list.size()>4){
		        		csvString.append(",");
		        		csvString.append(allData.get(i)[4].compareTo(BigDecimal.ZERO) == 0 ? "0" : allData.get(i)[4].toPlainString());
		        	}
		        	if (ids.size()!=0){
			        	csvString.append(",");
			        	csvString.append(othersYearlyValue.get(i));
		        	}
		        	csvString.append("\n");
		        }
	        }
	        visualizationForm.getExportData().setRegionTableData(regionData);
			if(divide){
				filter.setDivideThousands(false);
			}
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
	        
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
		
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000 / 
				Math.max(1000000, FeaturesUtil.getAmountMultiplier())); // Math.max added for the future, in case "billions" appears, as we don't want to divide by zero lower down the road
		String othersTitle = TranslatorWorker.translateText("Other");
        
		boolean ignore = request.getParameter("ignore") != null ? Boolean.parseBoolean(request.getParameter("ignore")) : (format != null && format.equals("xml"))? true : false;

        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} else {
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
		}

        Date startDate = null;
        Date endDate = null;
        
        Long year = request.getParameter("currentYear") == null? filter.getEndYear() : Long.parseLong(request.getParameter("currentYear"));
        Long previousYear = request.getParameter("previousYear") == null? filter.getEndYear()-1 : Long.parseLong(request.getParameter("previousYear"));
       
        if (filter.getYearToCompare()==null || filter.getYearToCompare().intValue()==0 || filter.getYearToCompare().intValue()>= filter.getEndYear()){
        	filter.setYearToCompare(filter.getEndYear()-1);
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpOrganisation> organizationList = new ArrayList();
        if (filter.getSelOrgIds()!= null && filter.getSelOrgIds().length > 0 && filter.getSelOrgIds()[0]!=-1) {
			for (int i = 0; i < filter.getSelOrgIds().length; i++) {
				organizationList.add(DbUtil.getOrganisation(filter.getSelOrgIds()[i]));
			}
		} else if (filter.getSelOrgGroupIds() != null && filter.getSelOrgGroupIds().length == 1 && filter.getSelOrgGroupIds()[0]!=-1) {
			organizationList.addAll(DbUtil.getOrganisationsFromGroup(filter.getSelOrgGroupIds()[0]));
		} else {
			organizationList.addAll(DbUtil.getAgencies(filter));
		}
        //organizationList = DbUtil.getOrganizations(filter);
        if (filter.getSelOrgIds()!= null && filter.getSelOrgIds().length == 1 && filter.getSelOrgIds()[0]!=-1){
        	StringBuffer csvString = new StringBuffer();
            String odaGrowthData = "";
            Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
            String text = TranslatorWorker.translateText("Year");
			csvString.append("\"" + text + "\"");
			csvString.append(",");
			odaGrowthData += "<"+ text +">";
			text = TranslatorWorker.translateText("Fundings Previous Year");
			csvString.append("\"" + text + "\"");
			csvString.append(",");
			odaGrowthData += text + ">";
			text = TranslatorWorker.translateText("Fundings Year");
			csvString.append("\"" + text + "\"");
			csvString.append(",");
			odaGrowthData += text + ">";
			text = TranslatorWorker.translateText("Growth percent");
			csvString.append("\"" + text + "\"");
			csvString.append("\n");
			odaGrowthData += text +">";
			
			for (int i = year.intValue(); i >= previousYear.intValue(); i--) {
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	            DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	            BigDecimal amtCurrentYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i-1);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i-1);
	            fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	            BigDecimal amtPreviousYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            if (amtCurrentYear.compareTo(BigDecimal.ZERO) == 1 && amtPreviousYear.compareTo(BigDecimal.ZERO) == 1){
	            	BigDecimal growthPercent = amtCurrentYear.divide(amtPreviousYear, RoundingMode.HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100));
	                if (!ignore || (growthPercent.compareTo(new BigDecimal(100))==-1) && (growthPercent.compareTo(new BigDecimal(-100))==1)) {
	                	map.put("" + (i-1) + "-" + i, growthPercent);
	                    csvString.append("" + (i-1) + " - " + i);
	            		csvString.append(",");
	            		odaGrowthData += "<"+ "" + (i-1) + " - " + i +">";
	            		csvString.append(amtPreviousYear);
	            		csvString.append(",");
	            		odaGrowthData += amtPreviousYear +">";
	            		csvString.append(amtCurrentYear);
	            		csvString.append(",");
	            		odaGrowthData += amtCurrentYear +">";
	            		csvString.append(growthPercent);
	            		csvString.append("\n");
	            		odaGrowthData += growthPercent +">";
					} 
	            }
			}
			visualizationForm.getExportData().setODAGrowthTableData(odaGrowthData);
	        StringBuffer xmlString = new StringBuffer();
	        List list = new LinkedList(map.entrySet());
			Iterator it = list.iterator();
			int index = 0;
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				String yearInterval = (String) entry.getKey();
				BigDecimal value = (BigDecimal) entry.getValue();
				String valueStr = "" + value.toBigInteger();
				if (value.compareTo(new BigDecimal(105))==1) {
					value = new BigDecimal(105);
					valueStr = ">>> " + valueStr;
				} else if (value.compareTo(new BigDecimal(-105))==-1){
					value = new BigDecimal(-105);
					valueStr = valueStr + " <<<";
				}
				xmlString.append("<dataField name=\"" + yearInterval + "\" value=\""+ value + "\" valueStr=\""+ valueStr + "\" label=\"" + yearInterval + "\" />\n");
			}
	        
			if (xmlString.length()==0) {
				xmlString.append("<dataField name=\"\">\n");
				xmlString.append("</dataField>\n");
			}
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			if(format != null && format.equals("xml")){
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
        } else {
        	StringBuffer csvString = new StringBuffer();
            String odaGrowthData = "";
            Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
            String text = TranslatorWorker.translateText("Organization");
			csvString.append("\"" + text + "\"");
			csvString.append(",");
			odaGrowthData += "<"+ text +">";
			text = TranslatorWorker.translateText("Fundings Year");
			csvString.append("\"" + text + " " + previousYear + "\"");
			csvString.append(",");
			odaGrowthData += text + " " + previousYear + ">";
			csvString.append("\"" + text + " " + year + "\"");
			csvString.append(",");
			odaGrowthData += text + " " + year + ">";
			text = TranslatorWorker.translateText("Growth percent");
			csvString.append("\"" + text + "\"");
			csvString.append("\n");
			odaGrowthData += text +">";
			
	        for (Iterator iterator = organizationList.iterator(); iterator.hasNext();) {
				AmpOrganisation ampOrganisation = (AmpOrganisation) iterator.next();
				Long[] ids = {ampOrganisation.getAmpOrgId()};
				DashboardFilter newFilter = filter.getCopyFilterForFunding();
    			newFilter.setSelOrgIds(ids);
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, year.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, year.intValue());
	            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	            BigDecimal amtCurrentYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, previousYear.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, previousYear.intValue());
	            fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	            //filter.setOrgIds(temp);
	            BigDecimal amtPreviousYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            if (amtCurrentYear.compareTo(BigDecimal.ZERO) == 1 && amtPreviousYear.compareTo(BigDecimal.ZERO) == 1){
	            	BigDecimal growthPercent = amtCurrentYear.divide(amtPreviousYear, RoundingMode.HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100));
	                if (!ignore || (growthPercent.compareTo(new BigDecimal(100))==-1) && (growthPercent.compareTo(new BigDecimal(-100))==1)) {
	                	map.put(ampOrganisation, growthPercent);
	                    csvString.append(ampOrganisation.getName());
	            		csvString.append(",");
	            		odaGrowthData += "<"+ ampOrganisation.getName() +">";
	            		csvString.append(amtPreviousYear);
	            		csvString.append(",");
	            		odaGrowthData += amtPreviousYear +">";
	            		csvString.append(amtCurrentYear);
	            		csvString.append(",");
	            		odaGrowthData += amtCurrentYear +">";
	            		csvString.append(growthPercent);
	            		csvString.append("\n");
	            		odaGrowthData += growthPercent +">";
					} 
	            }
			}
	        visualizationForm.getExportData().setODAGrowthTableData(odaGrowthData);
	        
	        Map<AmpOrganisation, BigDecimal> mapSorted = DashboardUtil.sortByValue(map,9l);
	        
	        StringBuffer xmlString = new StringBuffer();
	        
	        List list = new LinkedList(mapSorted.entrySet());
			Iterator it = list.iterator();
			int index = 0;
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				AmpOrganisation org = (AmpOrganisation) entry.getKey();
				BigDecimal value = (BigDecimal) entry.getValue();
				String valueStr = "" + value.toBigInteger();
				if (value.compareTo(new BigDecimal(105))==1) {
					value = new BigDecimal(105);
					valueStr = ">>> " + valueStr;
				} else if (value.compareTo(new BigDecimal(-105))==-1){
					value = new BigDecimal(-105);
					valueStr = valueStr + " <<<";
				}
				xmlString.append("<dataField name=\"" + org.getAcronym() + "\" value=\""+ value + "\" valueStr=\""+ valueStr + "\" label=\"" + org.getName() + "\" />\n");
			}
	        
			if (xmlString.length()==0) {
				xmlString.append("<dataField name=\"\">\n");
				xmlString.append("</dataField>\n");
			}
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			
			if(format != null && format.equals("xml")){
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
        }
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
	                if (graph.equals("Fundings")) {
						vForm.getExportData().setFundingGraph(image);
						logger.info("Creating image from Funding graph");
	                }
	                if (graph.equals("AidPredictability")) {
						vForm.getExportData().setAidPredictabilityGraph(image);
						logger.info("Creating image from Aid Predictability graph");
	                }
	                if (graph.equals("AidPredictabilityQuarter")) {
						vForm.getExportData().setAidPredictabilityQuarterGraph(image);
						logger.info("Creating image from Aid Predictability Quarterly graph");
	                }
	                if (graph.equals("AidType")) {
						vForm.getExportData().setAidTypeGraph(image);
						logger.info("Creating image from Aid Type graph");
	                }
	                if (graph.equals("BudgetBreakdown")) {
						vForm.getExportData().setBudgetGraph(image);
						logger.info("Creating image from Budget Breakdown graph");
	                }
	                if (graph.equals("AidModality")) {
						vForm.getExportData().setFinancingInstGraph(image);
						logger.info("Creating image from Financing Instrument graph");
	                }
	                if (graph.equals("OrganizationProfile")) {
						vForm.getExportData().setOrganizationGraph(image);
						logger.info("Creating image from Organization graph");
	                }
	                if (graph.equals("SectorProfile")) {
						vForm.getExportData().setSectorGraph(image);
						logger.info("Creating image from Sector graph");
	                }
	                if (graph.equals("RegionProfile")) {
						vForm.getExportData().setRegionGraph(image);
						logger.info("Creating image from Region graph");
	                }
	                if (graph.equals("NPOProfile")) {
						vForm.getExportData().setNPOGraph(image);
						logger.info("Creating image from NPO graph");
	                }
	                if (graph.equals("ProgramProfile")) {
						vForm.getExportData().setProgramGraph(image);
						logger.info("Creating image from Program graph");
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
	
	public ActionForward getFullList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		
		VisualizationForm visualizationForm = (VisualizationForm)form;
		
		String objectType = request.getParameter("objectType");
		JSONObject child = new JSONObject();
		JSONArray rankObjects = new JSONArray();
		JSONObject rootObjects = new JSONObject();
		if(objectType != null && objectType.equalsIgnoreCase("projects")){
			Map<AmpActivityVersion, BigDecimal> projectsList = visualizationForm.getRanksInformation().getFullProjects();
			List list = null;
			if (projectsList!=null) {
				list = new LinkedList(projectsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					AmpActivityVersion act = (AmpActivityVersion) entry.getKey();
					child.put("name", act.getName());
					child.put("id", act.getAmpActivityId());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "ProjectsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("sectors")){
			Map<AmpSector, BigDecimal> sectorsList = visualizationForm.getRanksInformation().getFullSectors();
			List list = null;
			if (sectorsList!=null) {
				list = new LinkedList(sectorsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			} 
			rootObjects.put("type", "SectorsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("regions")){
			Map<AmpCategoryValueLocations, BigDecimal> regionsList = visualizationForm.getRanksInformation().getFullRegions();
			List list = null;
			if (regionsList!=null) {
				list = new LinkedList(regionsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "RegionsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("programs")){
			Map<AmpTheme, BigDecimal> programsList = visualizationForm.getRanksInformation().getFullPrograms();
			List list = null;
			if (programsList!=null) {
				list = new LinkedList(programsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "ProgramsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("NPOs")){
			Map<AmpTheme, BigDecimal> NPOsList = visualizationForm.getRanksInformation().getFullNPOs();
			List list = null;
			if (NPOsList!=null) {
				list = new LinkedList(NPOsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "NPOsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("organizations")){
			Map<AmpOrganisation, BigDecimal> organizationsList = visualizationForm.getRanksInformation().getFullOrganizations();
			List list = null;
			if (organizationsList!=null) {
				list = new LinkedList(organizationsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + visualizationForm.getFilter().getCurrencyCode());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "OrganizationsList");
			rootObjects.put("list", rankObjects);
		}
		
		JSONArray children = new JSONArray();
		children.add(rootObjects);
		JSONObject root = new JSONObject();
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
	
	
	
}
