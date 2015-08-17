package org.digijava.module.visualization.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import java.util.TreeMap;

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
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
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
import org.digijava.module.aim.util.TeamUtil;
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
	private static int maxOrgChars = 45;
	
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
		
		AmpTeam currentTeam = null;
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (tm!=null && tm.getTeamAccessType().equals("Management")) {
			//TODO: Separate logic from "public view" and "Management ws" because ARE NOT THE SAME THING and it leads to data inconsistency.
			visualizationForm.getFilter().setFromPublicView(true);// sets as public view when team is management, so it shows only approved activities
        } 
		
		if (visualizationForm.getFilter().getWorkspaceOnly() != null && visualizationForm.getFilter().getWorkspaceOnly()) {
	    	currentTeam=TeamUtil.getAmpTeam(tm.getTeamId());
	    	//if((currentTeam.getComputation() != null && currentTeam.getComputation()) || "Management".equals(currentTeam.getAccessType())){
	    		AmpARFilter filter = (AmpARFilter)session.getAttribute(ArConstants.TEAM_FILTER);
	    		ArrayList<BigInteger> activityList = DbUtil.getInActivities(filter.getFilterConditionOnly());
	    		
	    		// Explanation: There is an issue when "show only activities from this workspace" option is enabled AND the current workspace is of type
	    		// ("Management" OR "Computed") AND the current workspace HAS NO VALID ACTIVITIES, in that case the user will see ALL activities (from
	    		// child workspaces) like the "show only activities from this worskspaces" option wasnt enabled. To solve that in one place will add a fake
	    		// activity so the user doesnt see wrong results.
	    		if(activityList == null || activityList.size() == 0) {
	    			activityList = new ArrayList<BigInteger>();
	    			activityList.add(new BigInteger("-99999999999"));
	    		}
	    		
	    		visualizationForm.getFilter().setActivityComputedList(activityList);
			//}
			visualizationForm.getFilter().setTeamMember(tm);
        } else {
        	visualizationForm.getFilter().setActivityComputedList(null);
        	visualizationForm.getFilter().setTeamMember(null);
        }
		
		// Parameters coming from queryString for the list of Organization Groups/Organizations, Regions/Zones, Sector/Sub-sector
		if (request.getParameter("orgGroupIds")!=null && !request.getParameter("orgGroupIds").equals("null"))
			visualizationForm.getFilter().setOrgGroupIds(getLongArrayFromParameter(request.getParameter("orgGroupIds")));
		if (request.getParameter("orgIds")!=null && !request.getParameter("orgIds").equals("null"))
			visualizationForm.getFilter().setOrgIds(getLongArrayFromParameter(request.getParameter("orgIds")));	
		if (request.getParameter("regionIds")!=null && !request.getParameter("regionIds").equals("null"))
			visualizationForm.getFilter().setRegionIds(getLongArrayFromParameter(request.getParameter("regionIds")));
		if (request.getParameter("zoneIds")!=null && !request.getParameter("zoneIds").equals("null"))
			visualizationForm.getFilter().setZoneIds(getLongArrayFromParameter(request.getParameter("zoneIds")));
		if (request.getParameter("sectorIds")!=null && !request.getParameter("sectorIds").equals("null"))
			visualizationForm.getFilter().setSectorIds(getLongArrayFromParameter(request.getParameter("sectorIds")));
		if (request.getParameter("subSectorIds")!=null && !request.getParameter("subSectorIds").equals("null"))
			visualizationForm.getFilter().setSubSectorIds(getLongArrayFromParameter(request.getParameter("subSectorIds")));
		if (request.getParameter("statusIds")!=null && !request.getParameter("statusIds").equals("null"))
			visualizationForm.getFilter().setSelStatusIds(getLongArrayFromParameter(request.getParameter("statusIds")));
		if (request.getParameter("beneficiaryAgencyIds")!=null && !request.getParameter("beneficiaryAgencyIds").equals("null"))
			visualizationForm.getFilter().setBeneficiaryAgencyIds(getLongArrayFromParameter(request.getParameter("beneficiaryAgencyIds")));
		if (request.getParameter("responsibleOrganizationIds")!=null && !request.getParameter("responsibleOrganizationIds").equals("null"))
			visualizationForm.getFilter().setResponsibleOrganizationIds(getLongArrayFromParameter(request.getParameter("responsibleOrganizationIds")));
		if (request.getParameter("implementingAgencyIds")!=null && !request.getParameter("implementingAgencyIds").equals("null"))
			visualizationForm.getFilter().setImplementingAgencyIds(getLongArrayFromParameter(request.getParameter("implementingAgencyIds")));
		if (request.getParameter("secondaryProgramIds")!=null && !request.getParameter("secondaryProgramIds").equals("null"))
			visualizationForm.getFilter().setSecondaryProgramIds(getLongArrayFromParameter(request.getParameter("secondaryProgramIds")));
		if (request.getParameter("showAcronymForOrgNames")!=null && !request.getParameter("showAcronymForOrgNames").equals("null"))
			visualizationForm.getFilter().setShowAcronymForOrgNames(new Boolean(request.getParameter("showAcronymForOrgNames")));
		if (request.getParameter("showOnlyNationalProjects")!=null && !request.getParameter("showOnlyNationalProjects").equals("null"))
			visualizationForm.getFilter().setShowOnlyNationalProjects(new Boolean(request.getParameter("showOnlyNationalProjects")));
		if (request.getParameter("nationalProjectsToo")!=null && !request.getParameter("nationalProjectsToo").equals("null"))
			visualizationForm.getFilter().setNationalProjectsToo(new Boolean(request.getParameter("nationalProjectsToo")));
		if (request.getParameter("showGroupsNotOrgs")!=null && !request.getParameter("showGroupsNotOrgs").equals("null"))
			visualizationForm.getFilter().setShowGroupsNotOrgs(new Boolean(request.getParameter("showGroupsNotOrgs")));
		
		// The organization groups can either be only one selected, or many, from either the Quick Filter or the Advanced Filter
		// This checks that Filter.getSelOrgGroupsIds always has the correct list of organizationGroup/s (either one or many)
		Long[] orgsGrpIds = visualizationForm.getFilter().getOrgGroupIds();
		Long orgsGrpId = visualizationForm.getFilter().getOrgGroupId();
		if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
			Long[] temp = {orgsGrpId};
			visualizationForm.getFilter().setSelOrgGroupIds(temp);
		} else {
			visualizationForm.getFilter().setOrgGroupId(-1l);
			visualizationForm.getFilter().setSelOrgGroupIds(orgsGrpIds);
		}	
		
		// The organization can either be only one selected, or many, from either the Quick Filter or the Advanced Filter
		// This checks that Filter.getSelOrgIds always has the correct list of organization/s (either one or many)
		Long[] orgsIds = visualizationForm.getFilter().getOrgIds();
		Long orgsId = visualizationForm.getFilter().getOrgId();
		if (orgsIds == null || orgsIds.length == 0 || orgsIds[0] == -1) {
			if (orgsId != null){
				Long[] temp = {orgsId};
				visualizationForm.getFilter().setSelOrgIds(temp);
			}
		} else {
			visualizationForm.getFilter().setOrgId(-1l);//unset orgId
			visualizationForm.getFilter().setSelOrgIds(orgsIds);
		}

		Long secsId = visualizationForm.getFilter().getSectorId();
		Long subSecsId = visualizationForm.getFilter().getSubSectorId();
		Long[] secsIds = visualizationForm.getFilter().getSectorIds();
		Long[] subSecsIds = visualizationForm.getFilter().getSubSectorIds();
		Long[] statusIds = visualizationForm.getFilter().getSelStatusIds();
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
			} else if(secsId != null){
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
			} else if(regsId != null){
				Long[] temp = {regsId};
				visualizationForm.getFilter().setSelLocationIds(temp);
			}
		}
		
		Long[] impAgIds = visualizationForm.getFilter().getImplementingAgencyIds();
		Long impAgId = visualizationForm.getFilter().getImplementingAgencyId();
		if (impAgIds == null || impAgIds.length == 0 || impAgIds[0] == -1) {
			if (impAgId != null){
				Long[] temp = {impAgId};
				visualizationForm.getFilter().setSelImplementingAgencyIds(temp);
			}
		} else {
			visualizationForm.getFilter().setImplementingAgencyId(-1l);//unset implementingAgencyId
			visualizationForm.getFilter().setSelImplementingAgencyIds(impAgIds);
		}
		
		Long[] benAgIds = visualizationForm.getFilter().getBeneficiaryAgencyIds();
		Long benAgId = visualizationForm.getFilter().getBeneficiaryAgencyId();
		if (benAgIds == null || benAgIds.length == 0 || benAgIds[0] == -1) {
			if (benAgId != null){
				Long[] temp = {benAgId};
				visualizationForm.getFilter().setSelBeneficiaryAgencyIds(temp);
			}
		} else {
			visualizationForm.getFilter().setBeneficiaryAgencyId(-1l);//unset beneficiaryAgencyId
			visualizationForm.getFilter().setSelBeneficiaryAgencyIds(benAgIds);
		}
		
		Long[] respOrgIds = visualizationForm.getFilter().getResponsibleOrganizationIds();
		Long respOrgId = visualizationForm.getFilter().getResponsibleOrganizationId();
		if (respOrgIds == null || respOrgIds.length == 0 || respOrgIds[0] == -1) {
			if (respOrgId != null){
				Long[] temp = {respOrgId};
				visualizationForm.getFilter().setSelResponsibleOrganizationIds(temp);
			}
		} else {
			visualizationForm.getFilter().setResponsibleOrganizationId(-1l);//unset beneficiaryAgencyId
			visualizationForm.getFilter().setSelResponsibleOrganizationIds(respOrgIds);
		}

		Long[] secProgIds = visualizationForm.getFilter().getSecondaryProgramIds();
		Long secProgId = visualizationForm.getFilter().getSecondaryProgramId();
		if (secProgIds == null || secProgIds.length == 0 || secProgIds[0] == -1) {
			if (secProgId != null){
				Long[] temp = {secProgId};
				visualizationForm.getFilter().setSelSecondaryProgramIds(temp);
			}
		} else {
			visualizationForm.getFilter().setSecondaryProgramId(-1l);//unset beneficiaryAgencyId
			visualizationForm.getFilter().setSelSecondaryProgramIds(secProgIds);
		}

		visualizationForm.getFilter().setSelProgramIds(visualizationForm.getFilter().getSelSecondaryProgramIds());//set programs on filter with secondary programs (for NDD)
		
		if(visualizationForm.getFilter().getDashboardType()==4){ //if it is deal dashboard, it fill the filter by peacebuiding marker with the 1, 2 and 3 markers
			if (visualizationForm.getFilter().getPeacebuilderMarkerId()!=null && visualizationForm.getFilter().getPeacebuilderMarkerId()!=-1){
				Long[] temp = {visualizationForm.getFilter().getPeacebuilderMarkerId()};
				visualizationForm.getFilter().setSelPeacebuilderMarkerIds(temp);
			} else {
				ArrayList<AmpCategoryValue> catList = (ArrayList<AmpCategoryValue>) visualizationForm.getFilter().getPeacebuilderMarkerList();
				Long[] temp = new Long[catList.size()];
				int i=0;
				for (Iterator iterator = catList.iterator(); iterator.hasNext();) {
					AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
					temp[i++] = ampCategoryValue.getId();
				}
				visualizationForm.getFilter().setSelPeacebuilderMarkerIds(temp);
			}
		}
		
		DashboardUtil.getSummaryAndRankInformation(visualizationForm, true, request);
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
		JSONArray topSecondaryPrograms = new JSONArray();
		JSONObject rootSecondaryPrograms = new JSONObject();
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
		JSONObject rootSelImpAg = new JSONObject();
		JSONArray selImpAg = new JSONArray();
		JSONObject rootSelBenAg = new JSONObject();
		JSONArray selBenAg = new JSONArray();
		JSONObject rootSelSecProg = new JSONObject();
		JSONArray selSecProg = new JSONArray();
		JSONObject rootSelRegions = new JSONObject();
		JSONArray selRegions = new JSONArray();
		JSONObject rootSelZones = new JSONObject();
		JSONArray selZones = new JSONArray();
		JSONObject rootSelStatus = new JSONObject();
		JSONArray selStatus = new JSONArray();
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
				if(orgsId != null && orgsId!=-1){
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
				jorganizationInfo.put("id", currentOrgId);
				jorganizationInfo.put("type", "Organization");
				jorganizationInfo.put("background", organization.getOrgBackground());
				jorganizationInfo.put("description", organization.getOrgDescription());
				jorganizationInfo.put("keyAreas", organization.getOrgKeyAreas());
				selAdditionalInfo.put("info", jorganizationInfo);
			}
			else
			{
				Long currentOrgGrpId=null;
				if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
					if(orgsGrpId != null && orgsGrpId!=-1){
						currentOrgGrpId=orgsGrpId;
					}
				}
				else{
					if(selOrgGroups.size()==1){
						currentOrgGrpId=orgsGrpIds[0];
					}
				}			
				if( currentOrgGrpId!=null){
					AmpOrgGroup orgGroup=DbUtil.getOrgGroup(currentOrgGrpId);
					JSONObject jorganizationGroupInfo = new JSONObject();
					jorganizationGroupInfo.put("id", currentOrgGrpId);
					jorganizationGroupInfo.put("type", "OrganizationGroup");
					jorganizationGroupInfo.put("background", orgGroup.getOrgGrpBackground());
					jorganizationGroupInfo.put("description", orgGroup.getOrgGrpDescription());
					jorganizationGroupInfo.put("keyAreas", orgGroup.getOrgGrpKeyAreas());
					selAdditionalInfo.put("info", jorganizationGroupInfo);
				}
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
		
		if (statusIds!=null && statusIds.length>0 && statusIds[0]!=-1) {
			for (int i = 0; i < statusIds.length; i++) {
				child.put("name", CategoryManagerUtil.getAmpCategoryValueFromDb(statusIds[i]).getValue());
				child.put("id", CategoryManagerUtil.getAmpCategoryValueFromDb(statusIds[i]).getId());
				selStatus.add(child);
			}
		}
		rootSelStatus.put("type", "SelStatusList");
		rootSelStatus.put("list", selStatus);
		children.add(rootSelStatus);
		
		if (configId!=null) {
				child.put("name", SectorUtil.getClassificationConfigById(configId).getClassification().getSecSchemeName());
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
		
		if (impAgIds!=null && impAgIds.length>0 && impAgIds[0]!=-1) {
			for (int i = 0; i < impAgIds.length; i++) {
				child.put("name", DbUtil.getOrganisation(impAgIds[i]).getName());
				child.put("id", DbUtil.getOrganisation(impAgIds[i]).getAmpOrgId());
				selImpAg.add(child);
			}
		}
		rootSelImpAg.put("type", "SelImpAgList");
		rootSelImpAg.put("list", selImpAg);
		children.add(rootSelImpAg);
		
		if (benAgIds!=null && benAgIds.length>0 && benAgIds[0]!=-1) {
			for (int i = 0; i < benAgIds.length; i++) {
				child.put("name", DbUtil.getOrganisation(benAgIds[i]).getName());
				child.put("id", DbUtil.getOrganisation(benAgIds[i]).getAmpOrgId());
				selBenAg.add(child);
			}
		}
		rootSelBenAg.put("type", "SelBenAgList");
		rootSelBenAg.put("list", selBenAg);
		children.add(rootSelBenAg);
		
		if (secProgIds!=null && secProgIds.length>0 && secProgIds[0]!=-1) {
			for (int i = 0; i < secProgIds.length; i++) {
				child.put("name", DbUtil.getAmpThemeById(secProgIds[i]).getName());
				child.put("id", DbUtil.getAmpThemeById(secProgIds[i]).getAmpThemeId());
				selSecProg.add(child);
			}
		}
		rootSelSecProg.put("type", "SelSecProgList");
		rootSelSecProg.put("list", selSecProg);
		children.add(rootSelSecProg);
		
		List list = null;
		AmpCurrency currency = CurrencyUtil.getCurrencyByCode(visualizationForm.getFilter().getCurrencyCode());
		
		Map<AmpActivityVersion, BigDecimal> projectsList = visualizationForm.getRanksInformation().getTopProjects();
		if (projectsList!=null) {
			list = new LinkedList(projectsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				AmpActivityVersion act = (AmpActivityVersion) entry.getKey();
				child.put("name", act.getName());
				child.put("id", act.getAmpActivityId());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
				topPrograms.add(child);
			}
		}
		rootPrograms.put("type", "ProgramsList");
		rootPrograms.put("top", topPrograms);
		children.add(rootPrograms);
		
		Map<AmpTheme, BigDecimal> secProgramsList = visualizationForm.getRanksInformation().getTopSecondaryPrograms();
		if (secProgramsList!=null) {
			list = new LinkedList(secProgramsList.entrySet());
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				child.put("name", entry.getKey().toString());
				child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
				topSecondaryPrograms.add(child);
			}
		}
		rootSecondaryPrograms.put("type", "SecondaryProgramsList");
		rootSecondaryPrograms.put("top", topSecondaryPrograms);
		children.add(rootSecondaryPrograms);
		rootTotComms.put("type", "TotalComms");
		rootTotComms.put("value", FormatHelper.formatNumberNotRounded( visualizationForm.getSummaryInformation().getTotalCommitments().doubleValue()));
		rootTotComms.put("curr", currency.getCurrencyName());
		children.add(rootTotComms);
		
		rootTotDisbs.put("type", "TotalDisbs");
		rootTotDisbs.put("value", FormatHelper.formatNumberNotRounded(visualizationForm.getSummaryInformation().getTotalDisbursements().doubleValue()));
		rootTotDisbs.put("curr", currency.getCurrencyName());
		children.add(rootTotDisbs);
		
		rootNumOfProjs.put("type", "NumberOfProjs");
		rootNumOfProjs.put("value", visualizationForm.getSummaryInformation().getNumberOfProjects() != null ? visualizationForm.getSummaryInformation().getNumberOfProjects().toString() : "0");
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
		rootAvgProjs.put("value", FormatHelper.formatNumberNotRounded( visualizationForm.getSummaryInformation().getAverageProjectSize().doubleValue()) );
		rootAvgProjs.put("curr", currency.getCurrencyName());
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

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);//IsProfile determines if the amounts were already divided

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("SectorProfileStartYear") != null 
				&& !request.getParameter("SectorProfileStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("SectorProfileEndYear") != null 
				&& !request.getParameter("SectorProfileEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("SectorProfileStartYear"));
			endYear = Long.parseLong(request.getParameter("SectorProfileEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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
        	Long id = filter.getSelSectorIds().length == 1? filter.getSelSectorIds()[0]:null;
        	//If the startYear/endYear selected is the same as in the general filter, use the stored rank
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear())){
        		if(id!=null && id!=-1){ //If there's a selected sector, get subsectors
                	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), filter, null, null);
                	if (map.size()==0)
                		map = visualizationForm.getRanksInformation().getFullSectors();
                } else {
            		map = visualizationForm.getRanksInformation().getFullSectors();
                }
        	}
        	else 
        	{
        		DashboardFilter newFilter = filter.getCopyFilterForFunding();
            	newFilter.setStartYear(startYear);
            	newFilter.setEndYear(endYear);
            	if(id!=null){
	            	map = DashboardUtil.getRankSubSectors(DbUtil.getSubSectors(id), newFilter, startYear.intValue(), endYear.intValue());
	            } else {
	            	map = DashboardUtil.getRankSectorsByKey(DbUtil.getSectors(newFilter), DbUtil.getSectors(newFilter), newFilter);
	            }
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
    	                		xmlString.append("<dataField name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\"  yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + sec.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + sec.getAmpSectorId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
	              		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
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
	        		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
	        
			visualizationForm.getExportData().setSectorTableData(null);
			if (lineChart) {
				String sectorData = "";
				int index = 0;
				list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
				sectorData += "<Year";
		        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
		        for (Long i = startYear; i <= endYear; i++) {
		        	sectorData += ">" + i;
		        }
		        while(it.hasNext()){
		            //Long[] key = it.next();
		            Map.Entry entry = (Map.Entry)it.next();
		            AmpSector sec = (AmpSector) entry.getKey();
		            if (index <= 4){
			            csvString.append(sec.getName().replace(",", "").replace('\r', ' ').replace('\n', ' '));
			            csvString.append("#");
			            csvString.append(sec.getAmpSectorId());
			            csvString.append(",");
		            }
		            sectorData += "<" + sec.getName() + ">";
		            
		            Long[] ids = {sec.getAmpSectorId()};
	            	//Long[] temp = filter.getSelSectorIds();
	            	DashboardFilter newFilter = filter.getCopyFilterForFunding();
	    			newFilter.setSelSectorIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	                newFilter.setFiscalCalendarId(fiscalCalendarId);
	                DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), true);
	                //filter.setSelSectorIds(temp);
		            int j = 0;
		            for (Long i = startYear; i <= endYear.intValue(); i++) {
		            	BigDecimal value = null;
	                	if(fundingCal[j] == null) {
	                		value = new BigDecimal(0);
	                	} else {
	                		value = fundingCal[j].getValue();
	                	}
		                BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		                j++;
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
		        int j = 0;
		        startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	            Long[] temp = filter.getSelSectorIds();
    			filter.setSelSectorIds(idsArray);
    			filter.setFiscalCalendarId(fiscalCalendarId);
		        DecimalWraper[] fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
		        filter.setSelSectorIds(temp);
		        for (Long i = startYear; i <= endYear.intValue(); i++) {
		        	BigDecimal value = null;
                	if(fundingCal[j] == null) {
                		value = new BigDecimal(0);
                	} else {
                		value = fundingCal[j].getValue();
                	}
			        BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
			        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
	        			//csvString.append(i);
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
            throw new DgException("Cannot load sector fundings by organizations from db", e);
        }
            
	}
	
	public ActionForward getSecondaryProgramProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
			
		return getProgramProfile(2,mapping,form,request,response);
	}
	
	public ActionForward getProgramProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
			
		return getProgramProfile(1,mapping,form,request,response);
	}
		
	public ActionForward getNPOProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
			
		return getProgramProfile(0,mapping,form,request,response);
	}
		
	public ActionForward getProgramProfile(int programSetting, ActionMapping mapping,
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

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);//IsProfile determines if the amounts were already divided

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("ProgramProfileStartYear") != null 
				&& !request.getParameter("ProgramProfileStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("ProgramProfileEndYear") != null 
				&& !request.getParameter("ProgramProfileEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("ProgramProfileStartYear"));
			endYear = Long.parseLong(request.getParameter("ProgramProfileEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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
        		if (programSetting==0) {
        			map = visualizationForm.getRanksInformation().getFullNPOs();
				} else if (programSetting==1) {
        			map = visualizationForm.getRanksInformation().getFullPrograms();
				} else {
					map = visualizationForm.getRanksInformation().getFullSecondaryPrograms();
				}
        	} else {
        		DashboardFilter newFilter = filter.getCopyFilterForFunding();
            	newFilter.setStartYear(startYear);
            	newFilter.setEndYear(endYear);
            	map = DashboardUtil.getRankProgramsByKey(DbUtil.getPrograms(newFilter,programSetting), newFilter, true);
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
    	                		xmlString.append("<dataField name=\"" + prog.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + prog.getAmpThemeId() + "\"  yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + prog.getName() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + prog.getAmpThemeId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
	              		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
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
	        		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
	        
			if (programSetting==0) {
	        	visualizationForm.getExportData().setNPOTableData(null);
			} else if (programSetting==1) {
	        	visualizationForm.getExportData().setProgramTableData(null);
			} else {
				visualizationForm.getExportData().setSecondaryProgramTableData(null);
			}
			if (lineChart) {
				String programData = "";
				int index = 0;
				list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
				programData += "<Year";
		        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
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
		                DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		                //filter.setSelProgramIds(null);
		                BigDecimal amount = fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		            DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		            filter.setSelProgramIds(null);
			        BigDecimal amount = fundingCal.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
			        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
	        			//csvString.append(i);
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
		        if (programSetting==0) {
		        	visualizationForm.getExportData().setNPOTableData(programData);
				} else if (programSetting==1) {
		        	visualizationForm.getExportData().setProgramTableData(programData);
				} else {
					visualizationForm.getExportData().setSecondaryProgramTableData(programData);
				}
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

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("OrganizationProfileStartYear") != null 
				&& !request.getParameter("OrganizationProfileStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("OrganizationProfileEndYear") != null 
				&& !request.getParameter("OrganizationProfileEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("OrganizationProfileStartYear"));
			endYear = Long.parseLong(request.getParameter("OrganizationProfileEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		
		String othersTitle = TranslatorWorker.translateText("Other");
		
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
        		DashboardFilter newFilter = filter.getCopyFilterForFunding();
            	newFilter.setStartYear(startYear);
            	newFilter.setEndYear(endYear);
            	Collection agencyListRed = DbUtil.getAgencies(newFilter, true);
                Iterator iter = agencyListRed.iterator();
                while (iter.hasNext()) {
                	AmpOrganisation org = (AmpOrganisation)iter.next();
                    agencyList.put(org.getAmpOrgId(), org);
                }
                map = DashboardUtil.getRankAgenciesByKey(agencyList.keySet(), newFilter);
        	}
        		
        	if (map==null) {
	        	map = new HashMap<AmpOrganisation, BigDecimal>();
			}
	        
        	List list = null;
        	Map<AmpOrgGroup, BigDecimal> groupedMap = null;         	
        	if(filter.getShowGroupsNotOrgs()) {
        		groupedMap = DashboardUtil.groupOrganizations(map);
        		list = new LinkedList(groupedMap.entrySet());
        	} else {
        		list = new LinkedList(map.entrySet());
        	}
        	
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
    				if(filter.getShowGroupsNotOrgs()) {
    					list = new LinkedList(groupedMap.entrySet());
    				} else {
    					list = new LinkedList(map.entrySet());
    				}
    				Iterator it = list.iterator();
    				int index = 0;
    				 while(it.hasNext() && index <= 4){
    					Map.Entry entry = (Map.Entry)it.next();
    					String name = null;
    					Long id = null;
    					if(filter.getShowGroupsNotOrgs()) {
    						name = ((AmpOrgGroup) entry.getKey()).getOrgGrpName();
    						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
    					} else {
    						name = ((AmpOrganisation) entry.getKey()).getName();
    						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
    					}
     	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), organizationTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
     	               //if(percentage.compareTo(new BigDecimal(0.01)) == 1) //if this is more than 0.01
     	                if (donut){
     	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
     	                		xmlString.append("<dataField name=\"" + name.replace("\"", "") + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + name.replace("\"", "") + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
     	                		index++;
     	                	}
     	                } else {
     	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
     	                		xmlString.append("<dataField name=\"" + name.replace("\"", "") + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + name.replace("\"", "") + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
     	               Long id = null;
	   					if(filter.getShowGroupsNotOrgs()) {
	   						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
	   					} else {
	   						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
	   					}
	   					
	 	                idsArrayStr = idsArrayStr + String.valueOf(id) + "_";
	 	            }
     	           	BigDecimal percentage = getPercentage(othersValue, organizationTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
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
                if(filter.getShowGroupsNotOrgs()) {
                	list = new LinkedList(groupedMap.entrySet());
                } else {
                	list = new LinkedList(map.entrySet());
                }
				Iterator it = list.iterator();
				int index = 0;
        		csvString.append("\""+TranslatorWorker.translateText("Organization Name")+"\"");
        		csvString.append(",");
        		csvString.append("\""+TranslatorWorker.translateText("Amount")+"\"");
        		csvString.append("\n");
                BigDecimal othersValue = BigDecimal.ZERO;

                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry)it.next();
            		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
    		
    		visualizationForm.getExportData().setOrganizationTableData(null);
    		if (lineChart) {
	    		String organizationData = "";
	    		int index = 0;
	    		if(filter.getShowGroupsNotOrgs()) {
	    			list = new LinkedList(groupedMap.entrySet());
	    		} else {
	    			list = new LinkedList(map.entrySet());
	    		}
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
	            organizationData += "<Year";
	            HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
	            for (Long i = startYear; i <= endYear; i++) {
		        	organizationData += ">" + i;
		        }
		        while(it.hasNext()){
	                //Long[] key = it.next();
	                Map.Entry entry = (Map.Entry)it.next();
	                String name = null;
	                Long id = null;
	                if(filter.getShowGroupsNotOrgs()) {
	                	AmpOrgGroup org = (AmpOrgGroup) entry.getKey();
	                	name = org.getOrgGrpName();
	                	id = org.getAmpOrgGrpId();
	                } else {
	                	AmpOrganisation org = (AmpOrganisation) entry.getKey();
	                	name = org.getName();
	                	id = org.getAmpOrgId();
	                }
	                if (index <= 4){
		                csvString.append(name);
		                csvString.append("#");
			            csvString.append(id);
			            csvString.append(",");
	                }
	                organizationData += "<" + name + ">";
	                Long[] ids = {id};
        			DashboardFilter newFilter = filter.getCopyFilterForFunding();
        			if(filter.getShowGroupsNotOrgs()) {
        				newFilter.setOrgGroupIds(ids);
        			} else {
        				newFilter.setSelOrgIds(ids);
        			}
                    startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
                    endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
                    newFilter.setFiscalCalendarId(fiscalCalendarId);
                    DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
                    int j = 0;
                    for (Long i = startYear; i <= endYear; i++) {
	                	BigDecimal value = null;
	                	if(fundingCal[j] == null) {
	                		value = new BigDecimal(0);
	                	} else {
	                		value = fundingCal[j].getValue();
	                	}
	                    //filter.setOrgIds(temp);
	                    BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		                j++;
	                }
	                index++;
	            }
	            csvString.deleteCharAt(csvString.length()-1);
		        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	            
	            ArrayList<Long> ids = new ArrayList<Long>();
	            while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	            	Long id = null;
	            	if(filter.getShowGroupsNotOrgs()) {
	                	AmpOrgGroup org = (AmpOrgGroup) entry.getKey();
	                	id = org.getAmpOrgGrpId();
	                } else {
	                	AmpOrganisation org = (AmpOrganisation) entry.getKey();
	                	id = org.getAmpOrgId();
	                }
	    			ids.add(id);
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
	            int j = 0;
	            for (Long i = startYear; i <= endYear; i++) {
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	                DashboardFilter newFilter = filter.getCopyFilterForFunding();
	                if(filter.getShowGroupsNotOrgs()) {
	    				newFilter.setOrgGroupIds(idsArray);
	    			} else {
	    				newFilter.setSelOrgIds(idsArray);
	    			}
	                newFilter.setFiscalCalendarId(fiscalCalendarId);
		            DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
		            BigDecimal value = null;
                	if(fundingCal[j] == null) {
                		value = new BigDecimal(0);
                	} else {
                		value = fundingCal[j].getValue();
                	}
	                BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
	                j++;
	            }
	          //Put headers
		        if (!allData.isEmpty()){
	                for (Long i = startYear; i <= endYear; i++) {
	                	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
	        			//csvString.append(i);
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
            throw new DgException("Cannot load sector fundings by organizations from db", e);
        }
	}	
	
	//Special chart for Timor AMP-14337
	public ActionForward getBeneficiaryAgencyProfileGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();

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

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("BeneficiaryAgencyProfileStartYear") != null 
				&& !request.getParameter("BeneficiaryAgencyProfileStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("BeneficiaryAgencyProfileEndYear") != null 
				&& !request.getParameter("BeneficiaryAgencyProfileEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("BeneficiaryAgencyProfileStartYear"));
			endYear = Long.parseLong(request.getParameter("BeneficiaryAgencyProfileEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		
		String othersTitle = TranslatorWorker.translateText("Other");
		
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
        	
    		HashMap<Long, AmpOrganisation> agencyList = new HashMap<Long, AmpOrganisation>();
    		DashboardFilter newFilter = filter.getCopyFilterForFunding();
        	newFilter.setStartYear(startYear);
        	newFilter.setEndYear(endYear);
        	newFilter.setAgencyType(2);//set beneficiary agency 
        	Collection agencyListRed = DbUtil.getAgencies(newFilter, true);
            Iterator iter = agencyListRed.iterator();
            while (iter.hasNext()) {
            	AmpOrganisation org = (AmpOrganisation)iter.next();
                agencyList.put(org.getAmpOrgId(), org);
            }
            map = DashboardUtil.getRankAgenciesByKey(agencyList.keySet(), newFilter);
        		
        	if (map==null) {
	        	map = new HashMap<AmpOrganisation, BigDecimal>();
			}
        	       	
        	List list = null;
        	Map<AmpOrgGroup, BigDecimal> groupedMap = null;         	
        	if(filter.getShowGroupsNotOrgs()) {
        		groupedMap = DashboardUtil.groupOrganizations(map);
        		list = new LinkedList(groupedMap.entrySet());
        	} else {
        		list = new LinkedList(map.entrySet());
        	}
        	
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
    				if(filter.getShowGroupsNotOrgs()) {
    					list = new LinkedList(groupedMap.entrySet());
    				} else {
    					list = new LinkedList(map.entrySet());
    				}
    				Iterator it = list.iterator();
    				int index = 0;
    				 while(it.hasNext() && index <= 4){
    					Map.Entry entry = (Map.Entry)it.next();
    					String name = null;
    					Long id = null;
    					if(filter.getShowGroupsNotOrgs()) {
    						name = ((AmpOrgGroup) entry.getKey()).getOrgGrpName();
    						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
    					} else {
    						name = ((AmpOrganisation) entry.getKey()).getName();
    						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
    					}
    					if(name.length() > maxOrgChars) {
    						name = name.substring(0, maxOrgChars) + " ...";
    					}
     	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), organizationTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
     	               //if(percentage.compareTo(new BigDecimal(0.01)) == 1) //if this is more than 0.01
     	                if (donut){
     	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
     	                		xmlString.append("<dataField name=\"" + name + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
     	                		index++;
     	                	}
     	                } else {
     	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
     	                		xmlString.append("<dataField name=\"" + name + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
     	                Long id = null;
	   					if(filter.getShowGroupsNotOrgs()) {
	   						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
	   					} else {
	   						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
	   					}	   					
	 	                idsArrayStr = idsArrayStr + String.valueOf(id) + "_";
	 	            }
     	           	BigDecimal percentage = getPercentage(othersValue, organizationTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
	                	}
	                }
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
            		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
    		
    		visualizationForm.getExportData().setBeneficiaryAgencyTableData(null);
    		if (lineChart) {
	    		String organizationData = "";
	    		int index = 0;
	    		if(filter.getShowGroupsNotOrgs()) {
	        		groupedMap = DashboardUtil.groupOrganizations(map);
	        		list = new LinkedList(groupedMap.entrySet());
	        	} else {
	        		list = new LinkedList(map.entrySet());
	        	}
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
	            organizationData += "<Year";
	            HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
	            for (Long i = startYear; i <= endYear; i++) {
		        	organizationData += ">" + i;
		        }
		        while(it.hasNext()){
	                //Long[] key = it.next();
	                Map.Entry entry = (Map.Entry)it.next();
	                String name = null;
	                Long id = null;
	                if(filter.getShowGroupsNotOrgs()) {
	                	AmpOrgGroup org = (AmpOrgGroup) entry.getKey();
	                	name = org.getOrgGrpName();
	                	id = org.getAmpOrgGrpId();
	                } else {
	                	AmpOrganisation org = (AmpOrganisation) entry.getKey();
	                	name = org.getName();
	                	id = org.getAmpOrgId();
	                }
	                if(name.length() > maxOrgChars) {
						name = name.substring(0, maxOrgChars) + " ...";
					}
	                if (index <= 4){
		                csvString.append(name.replace(",", ";"));
		                csvString.append("#");
			            csvString.append(id);
			            csvString.append(",");
	                }
	                organizationData += "<" + name + ">";
	                int j = 0;
	                Long[] ids = {id};
        			//DashboardFilter newFilter2 = filter.getCopyFilterForFunding();
        			//newFilter2.setStartYear(startYear);
                	//newFilter2.setEndYear(endYear);
                	newFilter.setAgencyType(2);//set beneficiary agency 
                	if(filter.getShowGroupsNotOrgs()) {
                		newFilter.setOrgGroupIds(ids);
        			} else {
        				newFilter.setSelOrgIds(ids);
        			}
                    startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
                    endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
                    newFilter.setFiscalCalendarId(fiscalCalendarId);
                    DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
	                for (Long i = startYear; i <= endYear; i++) {
	                	BigDecimal value = null;
	                	if(fundingCal[j] == null) {
	                		value = new BigDecimal(0);
	                	} else {
	                		value = fundingCal[j].getValue();
	                	}
	                    BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		                j++;
	                }	               
	                index++;
	            }
	            csvString.deleteCharAt(csvString.length()-1);
		        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
		        
	            ArrayList<Long> ids = new ArrayList<Long>();
	            while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	            	String name = null;
	                Long id = null;
	                if(filter.getShowGroupsNotOrgs()) {
	                	AmpOrgGroup org = (AmpOrgGroup) entry.getKey();
	                	name = org.getOrgGrpName();
	                	id = org.getAmpOrgGrpId();
	                } else {
	                	AmpOrganisation org = (AmpOrganisation) entry.getKey();
	                	name = org.getName();
	                	id = org.getAmpOrgId();
	                }
	                if(name.length() > maxOrgChars) {
						name = name.substring(0, maxOrgChars) + " ...";
					}
	    			ids.add(id);
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
	            int j = 0;
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
                endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
                //DashboardFilter newFilter = filter.getCopyFilterForFunding();
                if(filter.getShowGroupsNotOrgs()) {
    				newFilter.setOrgGroupIds(idsArray);
    			} else {
    				newFilter.setSelOrgIds(idsArray);
    			}
                newFilter.setFiscalCalendarId(fiscalCalendarId);
	            DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
	            for (Long i = startYear; i <= endYear; i++) {
	            	BigDecimal value = null;
                	if(fundingCal[j] == null) {
                		value = new BigDecimal(0);
                	} else {
                		value = fundingCal[j].getValue();
                	}
	                BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
	                j++;
	            }
	          //Put headers
		        if (!allData.isEmpty()){
	                for (Long i = startYear; i <= endYear; i++) {
	                	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
	        			//csvString.append(i);
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
	            visualizationForm.getExportData().setBeneficiaryAgencyTableData(organizationData);
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
            throw new DgException("Cannot load sector fundings by organizations from db", e);
        }
	}
	
	public ActionForward getResponsibleOrganizationGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		
		//System.out.println(request.getParameterNames());

		String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();

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

		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("ResponsibleOrganizationStartYear") != null 
				&& !request.getParameter("ResponsibleOrganizationStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("ResponsibleOrganizationEndYear") != null 
				&& !request.getParameter("ResponsibleOrganizationEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("ResponsibleOrganizationStartYear"));
			endYear = Long.parseLong(request.getParameter("ResponsibleOrganizationEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		String othersTitle = TranslatorWorker.translateText("Other");
		
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
        	
    		HashMap<Long, AmpOrganisation> agencyList = new HashMap<Long, AmpOrganisation>();
    		DashboardFilter newFilter = filter.getCopyFilterForFunding();
        	newFilter.setStartYear(startYear);
        	newFilter.setEndYear(endYear);
        	int respOrgConstant = org.digijava.module.visualization.util.Constants.RESPONSIBLE_ORGANIZATION;
        	newFilter.setAgencyType(respOrgConstant); 
        	Collection agencyListRed = DbUtil.getAgencies(newFilter, true);
            Iterator iter = agencyListRed.iterator();
            while (iter.hasNext()) {
            	AmpOrganisation org = (AmpOrganisation)iter.next();
                agencyList.put(org.getAmpOrgId(), org);
            }
            if(agencyList.size() > 0) {
            	map = DashboardUtil.getRankAgenciesByKey(agencyList.keySet(), newFilter);
            }
        		
        	if (map==null) {
	        	map = new HashMap<AmpOrganisation, BigDecimal>();
			}
        	       	
        	List list = null;
        	Map<AmpOrgGroup, BigDecimal> groupedMap = null;         	
        	if(filter.getShowGroupsNotOrgs()) {
        		groupedMap = DashboardUtil.groupOrganizations(map);
        		list = new LinkedList(groupedMap.entrySet());
        	} else {
        		list = new LinkedList(map.entrySet());
        	}
        	
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
    				if(filter.getShowGroupsNotOrgs()) {
    					list = new LinkedList(groupedMap.entrySet());
    				} else {
    					list = new LinkedList(map.entrySet());
    				}
    				Iterator it = list.iterator();
    				int index = 0;
    				 while(it.hasNext() && index <= 4){
    					Map.Entry entry = (Map.Entry)it.next();
    					String name = null;
    					Long id = null;
    					if(filter.getShowGroupsNotOrgs()) {
    						name = ((AmpOrgGroup) entry.getKey()).getOrgGrpName();
    						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
    					} else {
    						name = ((AmpOrganisation) entry.getKey()).getName();
    						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
    					}
     	                BigDecimal percentage = getPercentage((BigDecimal) entry.getValue(), organizationTotal);
	 	                BigDecimal value = (BigDecimal)entry.getValue();
     	               //if(percentage.compareTo(new BigDecimal(0.01)) == 1) //if this is more than 0.01
     	                if (donut){
     	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
     	                		xmlString.append("<dataField name=\"" + name + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
     	                		index++;
     	                	}
     	                } else {
     	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
     	                		xmlString.append("<dataField name=\"" + name + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + id + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
     	                
	   					Long id = null;
	   					if(filter.getShowGroupsNotOrgs()) {
	   						id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
	   					} else {
	   						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
	   					}
	   					
	 	                idsArrayStr = idsArrayStr + String.valueOf(id) + "_";
	 	            }
     	           	BigDecimal percentage = getPercentage(othersValue, organizationTotal);
	 	            if (donut){
	                	if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                		index++;
	                	}
	                }
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
            		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
    		
    		visualizationForm.getExportData().setResponsibleOrganizationTableData(null);
    		if (lineChart) {
	    		String organizationData = "";
	    		int index = 0;
	    		if(filter.getShowGroupsNotOrgs()) {
	        		list = new LinkedList(groupedMap.entrySet());
	        	} else {
	        		list = new LinkedList(map.entrySet());
	        	}
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
	            organizationData += "<Year";
	            HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
	            for (Long i = startYear; i <= endYear; i++) {
		        	organizationData += ">" + i;
		        }
		        while(it.hasNext()){
	                //Long[] key = it.next();
	                Map.Entry entry = (Map.Entry)it.next();
	                String name = null;
	                Long id = null;
	                if(filter.getShowGroupsNotOrgs()) {
	                	id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
	                	name = ((AmpOrgGroup) entry.getKey()).getOrgGrpName();
					} else {
						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
						name = ((AmpOrganisation) entry.getKey()).getName();
					}
	                if (index <= 4){
		                csvString.append(name.replace(",", ";"));
		                csvString.append("#");
			            csvString.append(id);
			            csvString.append(",");
	                }
	                organizationData += "<" + name + ">";
	                int j = 0;
	                Long[] ids = {id};
        			DashboardFilter newFilter2 = filter.getCopyFilterForFunding();
        			newFilter2.setAgencyType(respOrgConstant);
        			//newFilter2.setStartYear(startYear);
                	//newFilter2.setEndYear(endYear);        			
                	newFilter2.setAgencyType(respOrgConstant); 
                	if(filter.getShowGroupsNotOrgs()) {
        				newFilter2.setOrgGroupIds(ids);
        			} else {
        				newFilter2.setSelOrgIds(ids);
        			}
                    startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
                    endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
                    newFilter2.setFiscalCalendarId(fiscalCalendarId);
                    DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter2, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
                    //filter.setOrgIds(temp);                    
	                for (Long i = startYear; i <= endYear; i++) {	        			
	                	BigDecimal value = null;
	                	if(fundingCal[j] == null) {
	                		value = new BigDecimal(0);
	                	} else {
	                		value = fundingCal[j].getValue();
	                	}
	                    BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	                    //System.out.println(name+"-"+i+"-"+amount);
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
		                j++;
	                }
	                index++;
	            }
	            csvString.deleteCharAt(csvString.length()-1);
		        HashMap<Long, BigDecimal> othersYearlyValue = new HashMap<Long, BigDecimal>();
	            
	            ArrayList<Long> ids = new ArrayList<Long>();
	            while(it.hasNext()){
	            	Map.Entry entry = (Map.Entry)it.next();
	            	Long id = null;
	            	if(filter.getShowGroupsNotOrgs()) {
	                	id = ((AmpOrgGroup) entry.getKey()).getAmpOrgGrpId();
					} else {
						id = ((AmpOrganisation) entry.getKey()).getAmpOrgId();
					}
	    			ids.add(id);
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
	            int j = 0;
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
                endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
                //DashboardFilter newFilter = filter.getCopyFilterForFunding();
                if(filter.getShowGroupsNotOrgs()) {
    				newFilter.setOrgGroupIds(idsArray);
    			} else {
    				newFilter.setSelOrgIds(idsArray);
    			}
                newFilter.setFiscalCalendarId(fiscalCalendarId);
	            DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
	            for (Long i = startYear; i <= endYear; i++) {
	            	BigDecimal value = null;
                	if(fundingCal[j] == null) {
                		value = new BigDecimal(0);
                	} else {
                		value = fundingCal[j].getValue();
                	}
	                BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);                
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
	                	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
	        			//csvString.append(i);
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
	            visualizationForm.getExportData().setResponsibleOrganizationTableData(organizationData);
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

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("AidModalityStartYear") != null 
				&& !request.getParameter("AidModalityStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("AidModalityEndYear") != null 
				&& !request.getParameter("AidModalityEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("AidModalityStartYear"));
			endYear = Long.parseLong(request.getParameter("AidModalityEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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
		

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);

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
                funding = DbUtil.getFunding(filter, startDate, endDate, ampCategoryValue.getId(), null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
            } else {
            	funding = DbUtil.getFunding(filter, startDate, endDate, null, ampCategoryValue.getId(), filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
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
		                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		                } else {
		                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		                }
						BigDecimal percentage = getPercentage(funding.getValue(), amtTotal);
		                if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\""  +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText(value.getValue()) + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
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
			                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
			                } else {
			                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
			                }
			                hasValues = true;
							xmlString.append("<dataField category=\"" +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" amount=\""+ funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
							aidTypeData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
                } else {
                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
                }
        		csvString.append(funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
        		total = total.add(funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
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

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("BudgetBreakdownStartYear") != null 
				&& !request.getParameter("BudgetBreakdownStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("BudgetBreakdownEndYear") != null 
				&& !request.getParameter("BudgetBreakdownEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("BudgetBreakdownStartYear"));
			endYear = Long.parseLong(request.getParameter("BudgetBreakdownEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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
		
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);

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
        
        Long[] budgetCVIds = new Long[categoryValues.size()];
		int cnt = 0;
		DashboardFilter newFilter;
		DecimalWraper grandTotal;
		{
			newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelCVIds(null);
			grandTotal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
		}
		
		AmpCategoryValue acGrandTotal = new AmpCategoryValue();
		acGrandTotal.setValue("Grand Total");
		hm.put(acGrandTotal, grandTotal.getValue());
		
		for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
			AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
			DecimalWraper funding = null;
			newFilter = filter.getCopyFilterForFunding();
			budgetCVIds[cnt++] = ampCategoryValue.getId();
			Long[] selCVIds = {ampCategoryValue.getId()};
			newFilter.setSelCVIds(selCVIds);
            funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
            amtTotal = amtTotal.add(funding.getValue());
            hm.put(ampCategoryValue, funding.getValue());
		}
		AmpCategoryValue acUnallocated = new AmpCategoryValue();
		acUnallocated.setValue("Unallocated");
		//grandTotal.getValue().compareTo(amtTotal)
		hm.put(acUnallocated, grandTotal.getValue().subtract(amtTotal));

//		filter.setBudgetCVIds(budgetCVIds);
//        DashboardFilter newFilter1 = filter.getCopyFilterForFunding();
//		Long[] selCVIds1 = {-1l};
//		newFilter1.setSelCVIds(selCVIds1);
//		DecimalWraper fund = DbUtil.getFunding(newFilter1, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
//		amtTotal = amtTotal.add(fund.getValue());
        
       if(format != null && format.equals("xml")){
			
			StringBuffer xmlString = new StringBuffer();
			String budgetData = "";
			
            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
            	BigDecimal yearAllocatedTotal = BigDecimal.ZERO;
            	BigDecimal yearUnallocatedTotal = BigDecimal.ZERO;
            	BigDecimal yearGrandTotal = BigDecimal.ZERO;
				startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				xmlString.append("<year name=\"" + yearName + "\">\n");
				budgetData += "<" + yearName;
				Iterator<AmpCategoryValue> it = categoryValues.iterator();
				budgetCVIds = new Long[categoryValues.size()];
				int j = 0;
				boolean hasValues = false;
				while (it.hasNext()){
					AmpCategoryValue value = it.next();
					budgetCVIds[j++] = value.getId();
					BigDecimal totalCategory = hm.get(value);
					if(!totalCategory.equals(BigDecimal.ZERO)){
		                DecimalWraper funding = null;
		                newFilter = filter.getCopyFilterForFunding();
						Long[] selCVIds = {value.getId()};
						newFilter.setSelCVIds(selCVIds);
	                    funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	                    yearAllocatedTotal = yearAllocatedTotal.add(funding.getValue());
		                hasValues = true;
						xmlString.append("<dataField category=\"" +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" amount=\""+ funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
						budgetData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					}
					else
					{
						budgetData += ">" + TranslatorWorker.translateText(value.getValue()) + ">" + BigDecimal.ZERO.setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					}
				}
				filter.setBudgetCVIds(budgetCVIds);
                newFilter = filter.getCopyFilterForFunding();
				newFilter.setSelCVIds(null);
				yearGrandTotal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0].getValue();
				yearUnallocatedTotal = yearGrandTotal.subtract(yearAllocatedTotal);
				xmlString.append("<dataField category=\"" +TranslatorWorker.translateText("Unallocated") + "\" id=\"-1\" amount=\""+ yearUnallocatedTotal.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				budgetData += ">" + TranslatorWorker.translateText("Unallocated") + ">" + yearUnallocatedTotal.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
				BigDecimal donutGrandTotal = hm.get(acGrandTotal).divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				if(donutGrandTotal.compareTo(BigDecimal.ZERO) == 1){
					Iterator<AmpCategoryValue> it = categoryValues.iterator();
					BigDecimal unallocated = hm.get(acUnallocated).divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);

					while (it.hasNext()){
						AmpCategoryValue value = it.next();
						BigDecimal currentCVvalue = hm.get(value).divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						BigDecimal percentage = getPercentage(currentCVvalue, donutGrandTotal);
		                if(percentage.compareTo(new BigDecimal(1)) == 1){
	                		xmlString.append("<dataField name=\""  +TranslatorWorker.translateText(value.getValue()) + "\" id=\"" + value.getId() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ currentCVvalue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText(value.getValue()) + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
	                	}
					}
					
					BigDecimal percentage = getPercentage(unallocated, donutGrandTotal);
					if(percentage.compareTo(new BigDecimal(1)) == 1)
						xmlString.append("<dataField name=\""  +TranslatorWorker.translateText("Unallocated") + "\" id=\"-1\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ unallocated.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" yearLabels=\"" + yearLabels + "\" label=\"" + TranslatorWorker.translateText("Unallocated") + "\" percentage=\"" + percentage.toPlainString() + "\"/>\n");
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
        	BigDecimal yearAllocatedTotal = BigDecimal.ZERO;
        	BigDecimal yearUnallocatedTotal = BigDecimal.ZERO;
        	BigDecimal yearGrandTotal = BigDecimal.ZERO;
            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
    		csvString.append(yearName);
    		csvString.append(",");
    		it = categoryValues.iterator();
    		budgetCVIds = new Long[categoryValues.size()];
			int j = 0;
			while (it.hasNext()){
    			AmpCategoryValue value = it.next();
    			newFilter = filter.getCopyFilterForFunding();
    			budgetCVIds[j++] = value.getId();
				Long[] selCVIds = {value.getId()};
				newFilter.setSelCVIds(selCVIds);
                DecimalWraper funding = null;
                funding = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
        		csvString.append(funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
        		yearAllocatedTotal = yearAllocatedTotal.add(funding.getValue());
        		total = total.add(funding.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
    		}
			filter.setBudgetCVIds(budgetCVIds);
            newFilter = filter.getCopyFilterForFunding();
			newFilter.setSelCVIds(null);
			
			yearGrandTotal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0].getValue();
			yearUnallocatedTotal = yearGrandTotal.subtract(yearAllocatedTotal);
    		csvString.append(yearUnallocatedTotal.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
    		total = total.add(yearUnallocatedTotal.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
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

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("AidPredictabilityStartYear") != null 
				&& !request.getParameter("AidPredictabilityStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("AidPredictabilityEndYear") != null 
				&& !request.getParameter("AidPredictabilityEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("AidPredictabilityStartYear"));
			endYear = Long.parseLong(request.getParameter("AidPredictabilityEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);

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
			if (filter.getTransactionType()==Constants.MTEFPROJECTION){
				xmlString.append("<year name=\"" + startYear + "\">\n");
				xmlString.append("<dataField category=\"category\" amount=\"0.00\"  year=\""+startYear+"\"/>\n");
				xmlString.append("</year>\n");
			} else {
	            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
					Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
			        String headingFY = TranslatorWorker.translateText("FY");
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
					xmlString.append("<year name=\"" + yearName + "\">\n");
					aidPredData += "<" + yearName;
		            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
					xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
					aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
					xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "\" amount=\""+ fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
					aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
					xmlString.append("</year>\n");
				}
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
        	if (filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
	            Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
		        String headingFY = TranslatorWorker.translateText("FY");
				String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
				csvString.append(yearName);
				csvString.append(",");
	            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				csvString.append(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
	            DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				csvString.append(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
					nodata = false;
				}
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

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("AidPredictabilityStartYear") != null 
				&& !request.getParameter("AidPredictabilityStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("AidPredictabilityEndYear") != null 
				&& !request.getParameter("AidPredictabilityEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("AidPredictabilityStartYear"));
			endYear = Long.parseLong(request.getParameter("AidPredictabilityEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);

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
			if (filter.getTransactionType()==Constants.MTEFPROJECTION){
				xmlString.append("<year name=\"" + startYear + "\">\n");
				xmlString.append("<dataField category=\"category\" amount=\"0.00\"  year=\""+startYear+"\"/>\n");
				xmlString.append("</year>\n");
			} else {
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
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-1\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-1\" amount=\""+ fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q2
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
				endDateQ = cal.getTime();
				xmlString.append("<year name=\"" + yearName + "\r\n-Q2-\">\n");
				aidPredData += "<" + yearName + "-Q2";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-2\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-2\" amount=\""+ fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q3
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
				endDateQ = cal.getTime();
				xmlString.append("<year name=\"" + yearName + "\r\n-Q3-\">\n");
				aidPredData += "<" + yearName + "-Q3";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-3\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-3\" amount=\""+ fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
				//Q4
				startDateQ = endDateQ;
				endDateQ = endDate;
				xmlString.append("<year name=\"" + yearName + "\r\n-Q4-\">\n");
				aidPredData += "<" + yearName + "-Q4";
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+plannedTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey() + "-4\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Planned>" + fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				xmlString.append("<dataField category=\""+actualTitle+"\" id=\"" + CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey() + "-4\" amount=\""+ fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" year=\"" + yearName + "\"/>\n");
				aidPredData += ">Actual>" + fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
				xmlString.append("</year>\n");
			}
			
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
        
        for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
            // apply calendar filter
        	if (filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
	            Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
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
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				csvString.append(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
	            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				csvString.append(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append("\n");
				//Q2
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 6); // add 3 month for quarter  
				endDateQ = cal.getTime();
				csvString.append(yearName+ " - Q2");
				csvString.append(",");
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				csvString.append(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
	            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				csvString.append(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append("\n");
				//Q3
				startDateQ = endDateQ;
				cal.setTime(startDate);  
				cal.add(Calendar.MONTH, 9); // add 3 month for quarter  
				endDateQ = cal.getTime();
				csvString.append(yearName+ " - Q3");
				csvString.append(",");
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				csvString.append(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
	            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				csvString.append(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append("\n");
				//Q4
				startDateQ = endDateQ;
				endDateQ = endDate;
				csvString.append(yearName+ " - Q4");
				csvString.append(",");
	            fundingPlanned = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey(), false)[0];
				csvString.append(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingPlanned.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append(",");
	            fundingActual = DbUtil.getFunding(filter, startDateQ, endDateQ,null,null,filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey(), false)[0];
				csvString.append(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				total = total.add(fundingActual.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				csvString.append("\n");
				
				if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
					nodata = false;
				}
        	}
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
	
	public ActionForward getFundingsGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		HttpSession session=request.getSession();
		String format = request.getParameter("format");
		boolean donut = request.getParameter("donut") != null ? Boolean.parseBoolean(request.getParameter("donut")) : false;
		boolean linechart = request.getParameter("linechart") != null ? Boolean.parseBoolean(request.getParameter("linechart")) : false;
		boolean divide = request.getParameter("divide") != null ? Boolean.parseBoolean(request.getParameter("divide")) : false;

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("FundingsStartYear") != null 
				&& !request.getParameter("FundingsStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("FundingsEndYear") != null 
				&& !request.getParameter("FundingsEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("FundingsStartYear"));
			endYear = Long.parseLong(request.getParameter("FundingsEndYear"));

		}
	
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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

		BigDecimal divideByDenominator;
        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);

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
		boolean expendituresVisible = FeaturesUtil.isVisibleFeature("Expenditures");
		boolean pledgesVisible = FeaturesUtil.isVisibleModule("Pledges");

		StringBuffer csvString = new StringBuffer();
        String text = TranslatorWorker.translateText("Year");

		csvString.append("\"" + text + "\"");
		if (filter.getPledgeVisible()!=null && filter.getPledgeVisible() && pledgesVisible) {
			csvString.append(",");
			csvString.append("\"" + pledgesTranslatedTitle + "\"");
		}
		if (filter.getCommitmentsVisible()!=null && filter.getCommitmentsVisible()) {
			csvString.append(",");
			csvString.append("\"" + comTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.COMMITMENT + "\"");
		}
        if (filter.getDisbursementsVisible()!=null && filter.getDisbursementsVisible()) {
    		csvString.append(",");
	        csvString.append("\"" + disbTranslatedTitle);
			csvString.append("#");
	        csvString.append(Constants.DISBURSEMENT + "\"");
        }
        if (filter.getExpendituresVisible()!=null && filter.getExpendituresVisible() && expendituresVisible) {
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
			if (filter.getTransactionType()==Constants.MTEFPROJECTION){
				xmlString.append("<year name=\"" + startYear + "\">\n");
				xmlString.append("<dataField category=\"category\" amount=\"0.00\"  year=\""+startYear+"\"/>\n");
				xmlString.append("</year>\n");
			} else {
	            for (int i = startYear.intValue(); i <= endYear.intValue(); i++) {
					Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
					
			        String headingFY = TranslatorWorker.translateText("FY");
					String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
					xmlString.append("<year name=\"" + yearName + "\">\n");
					fundingData += "<" + yearName;
					
						if (filter.getPledgeVisible()!=null && filter.getPledgeVisible() && pledgesVisible) {
							DecimalWraper fundingPledge = DbUtil.getPledgesFunding(filter, startDate, endDate,
									currCode);
							xmlString
							.append("<dataField category=\""+TranslatorWorker.translateText("Pledges")+"\" amount=\""+ fundingPledge.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
							fundingData += ">" + pledgesTranslatedTitle + ">"+ fundingPledge.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
						if (filter.getCommitmentsVisible()!=null && filter.getCommitmentsVisible()) {
							DecimalWraper fundingComm = DbUtil
							.getFunding(filter, startDate, endDate, null, null,
									Constants.COMMITMENT, filter.getAdjustmentType(), false)[0];
							xmlString
							.append("<dataField category=\""+ comTranslatedTitle +"\" id=\"" + Constants.COMMITMENT + "\" amount=\""+ fundingComm.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
							fundingData += ">" + comTranslatedTitle + ">"+ fundingComm.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
						if (filter.getDisbursementsVisible()!=null && filter.getDisbursementsVisible()) {
							DecimalWraper fundingDisb = DbUtil
							.getFunding(filter, startDate, endDate, null, null,
									Constants.DISBURSEMENT, filter.getAdjustmentType(), false)[0];
							xmlString
							.append("<dataField category=\""+ disbTranslatedTitle +"\" id=\"" + Constants.DISBURSEMENT + "\" amount=\""+ fundingDisb.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) +  "\"  year=\"" + yearName + "\"/>\n");
							fundingData += ">" + disbTranslatedTitle + ">"+ fundingDisb.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
						if (filter.getExpendituresVisible()!=null && filter.getExpendituresVisible() && expendituresVisible) {
							DecimalWraper fundingExp = DbUtil
							.getFunding(filter, startDate, endDate, null, null,
									Constants.EXPENDITURE, filter.getAdjustmentType(), false)[0];
							xmlString
							.append("<dataField category=\""+ expTranslatedTitle +"\" id=\"" + Constants.EXPENDITURE + "\" amount=\""+ fundingExp.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\"  year=\"" + yearName + "\"/>\n");
							fundingData += ">" + expTranslatedTitle + ">"+ fundingExp.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
						}
					xmlString.append("</year>\n");
				}
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
			if (filter.getTransactionType()!=Constants.MTEFPROJECTION){
				DecimalWraper fundingPledge =  new DecimalWraper();
				if (filter.getPledgeVisible() && pledgesVisible) {
					fundingPledge = DbUtil.getPledgesFunding(filter, startDate, endDate,currCode);
					csvString.append(",");
					csvString.append(fundingPledge.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
					total = total.add(fundingPledge.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				}
				
				if (filter.getCommitmentsVisible()) {
					DecimalWraper fundingComm = DbUtil.getFunding(filter, startDate, endDate, null, null,Constants.COMMITMENT, filter.getAdjustmentType(), false)[0];
					csvString.append(",");
					csvString.append(fundingComm.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
					total = total.add(fundingComm.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				}
				
				if (filter.getDisbursementsVisible()) {
		    		DecimalWraper fundingDisb = DbUtil.getFunding(filter, startDate,endDate, null, null, Constants.DISBURSEMENT, filter.getAdjustmentType(), false)[0];
		    		csvString.append(",");
		    		csvString.append(fundingDisb.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
		    		total = total.add(fundingDisb.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				}
				
				DecimalWraper fundingExp = new DecimalWraper();
				if (filter.getExpendituresVisible() && expendituresVisible) {
					fundingExp = DbUtil.getFunding(filter, startDate, endDate,null, null, Constants.EXPENDITURE, filter.getAdjustmentType(), false)[0];
					csvString.append(",");
					csvString.append(fundingExp.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
					total = total.add(fundingExp.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP));
				}
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
	        		orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, visualizationForm.getFilter().getFromPublicView(),visualizationForm.getFilter());
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
			visualizationForm.getFilter().setYears(new TreeMap<String, Integer>());
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
				visualizationForm.getFilter().getYears().put(yearName,i);
				JSONObject child = new JSONObject();
				child.put("value", i);
				child.put("key",yearName);
				children.add(child);
			}
			
			root.put("ID", parentId);
			root.put("objectType", objectType);
			root.put("children", children);
			
		}
		else if(objectType != null && (objectType.equals("OrganizationGroup"))) {
	        List<AmpOrgGroup> orgGrps=new ArrayList<AmpOrgGroup>();
	        try {
	        	orgGrps = DbUtil.getOrganisationGroupsByRole(visualizationForm.getFilter().getFromPublicView(),visualizationForm.getFilter());
				JSONObject child = new JSONObject();
				Iterator<AmpOrgGroup> it = orgGrps.iterator();
				while(it.hasNext()){
					AmpOrgGroup org = it.next();
					child.put("ID", org.getAmpOrgGrpId());
					child.put("name", org.getOrgGrpName());
					children.add(child);
				}
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
	        } catch (Exception e) {
	            logger.error("unable to load organization groups", e);
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
			//logger.error("getProgress: outputting " + progressText.toString());
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

		Long startYear = null;
		Long endYear = null;
		if(request.getParameter("RegionProfileStartYear") != null 
				&& !request.getParameter("RegionProfileStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("RegionProfileEndYear") != null 
				&& !request.getParameter("RegionProfileEndYear").toString().equalsIgnoreCase(""))
		{
			startYear = Long.parseLong(request.getParameter("RegionProfileStartYear"));
			endYear = Long.parseLong(request.getParameter("RegionProfileEndYear"));

		}
		else if(request.getParameter("startYear") != null && request.getParameter("endYear") != null 
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
		
		BigDecimal divideByDenominator;
		divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), true);

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
        	Long id = filter.getSelLocationIds().length == 1? filter.getSelLocationIds()[0]:null;
        	//If the startYear/endYear selected is the same as in the general filter, use the stored rank
        	if(startYear.equals(filter.getStartYear()) && endYear.equals(filter.getEndYear())){
        		if(id!=null && id!=-1){
	            	map = DashboardUtil.getRankSubRegions(DbUtil.getSubRegions(id), filter, null, null);
	            	if (map.size()==0) {
                		map = visualizationForm.getRanksInformation().getFullRegions();
	            	}
	            	//AMP-16514: This is needed when region has been selected but it doesnt have sub-regions.
	            	if(map == null) {
	            		DashboardFilter newFilter = filter.getCopyFilterForFunding();
	                	newFilter.setStartYear(startYear);
	                	newFilter.setEndYear(endYear);
	            		map = DashboardUtil.getRankRegionsByKey(DbUtil.getRegions(newFilter), DbUtil.getRegions(newFilter), newFilter, true, request);
	            	}
	            } else {
	        		map = visualizationForm.getRanksInformation().getFullRegions();
	        	}	            	
            }
        	else
        	{
        		DashboardFilter newFilter = filter.getCopyFilterForFunding();
            	newFilter.setStartYear(startYear);
            	newFilter.setEndYear(endYear);
            	if(id!=null && id!=-1){
	            	map = DashboardUtil.getRankSubRegions(DbUtil.getSubRegions(id), newFilter, startYear.intValue(), endYear.intValue());
	            	if (map.size()==0)
	            		map = DashboardUtil.getRankRegionsByKey(DbUtil.getRegions(newFilter), DbUtil.getRegions(newFilter), newFilter, true, request);
	            } else {
	            	map = DashboardUtil.getRankRegionsByKey(DbUtil.getRegions(newFilter), DbUtil.getRegions(newFilter), newFilter, true, request);
	            }
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
    	                		xmlString.append("<dataField name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
    	                		index++;
    	                	}
    	                } else {
    	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
    	                		xmlString.append("<dataField name=\"" + entry.getKey() + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + entry.getKey() + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + loc.getId() + "\" yearLabels=\"" + yearLabels + "\"/>\n");
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
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
	                	}
	                } else {
	                	if(percentage.compareTo(new BigDecimal(0.01)) == 1){
	                		xmlString.append("<dataField name=\"" + othersTitle + "\" startYear=\"" + (startDate.getYear() + 1900) + "\" endYear=\"" + (endDate.getYear() + 1900) + "\" value=\""+ othersValue.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP) + "\" label=\"" + othersTitle + "\" percentage=\"" + percentage.toPlainString() + "\" id=\"" + idsArrayStr + "\"/>\n");
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
	        		csvString.append(entry.getKey().toString().replace(',', ';').replace('\r', ' ').replace('\n', ' '));
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
            
			visualizationForm.getExportData().setRegionTableData(null);
			if (lineChart) {
				String regionData = "";
				int index = 0;
				list = new LinkedList(map.entrySet());
				Iterator it = list.iterator();
				csvString = new StringBuffer();
				csvString.append("Year,");
				regionData += "<Year";
		        HashMap<Long, BigDecimal[]> allData = new HashMap<Long, BigDecimal[]>();
		        divideByDenominator = DashboardUtil.getDividingDenominator(divide, filter.shouldShowAmountsInThousands(), false);
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
		            
		            Long[] ids = {loc.getId()};
                	DashboardFilter newFilter = filter.getCopyFilterForFunding();
                	
	    			newFilter.setSelLocationIds(ids);
	                startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
	                endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	                newFilter.setFiscalCalendarId(fiscalCalendarId);
	                DecimalWraper[] fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), true);
	                int j = 0;
	                for (Long i = startYear; i <= endYear; i++) {
	                	BigDecimal value = null;
	                	if(fundingCal[j] == null) {
	                		value = new BigDecimal(0);
	                	} else {
	                		value = fundingCal[j].getValue();
	                	}	                	
	                	BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		                j++;
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
		        startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYear.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYear.intValue());
	            Long[] temp = filter.getSelLocationIds();
	            filter.setSelLocationIds(idsArray);
	            filter.setFiscalCalendarId(fiscalCalendarId);
	            
		        DecimalWraper[] fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), true);
		        int j = 0;
		        filter.setSelLocationIds(temp);
	            for (Long i = startYear; i <= endYear; i++) {
	            	BigDecimal value = null;
                	if(fundingCal[j] == null) {
                		value = new BigDecimal(0);
                	} else {
                		value = fundingCal[j].getValue();
                	}
			        BigDecimal amount = value.divide(divideByDenominator, RoundingMode.HALF_UP).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
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
		            j++;
		        }
				
	            //Put headers
		        if (!allData.isEmpty()){
	                for (Long i = startYear; i <= endYear; i++) {
	                	startDate = DashboardUtil.getStartDate(fiscalCalendarId, i.intValue());
	        			endDate = DashboardUtil.getEndDate(fiscalCalendarId, i.intValue());
	        	        headingFY = TranslatorWorker.translateText("FY");
	        			String yearName = DashboardUtil.getYearName(headingFY, fiscalCalendarId, startDate, endDate);
	        			csvString.append(yearName);
			        	//csvString.append(i);
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
            throw new DgException("Cannot load region fundings from db", e);
        }
	      
	}	

	
	public ActionForward getODAGrowthGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;
		DashboardFilter filter = visualizationForm.getFilter().getCopyFilterForFunding();
		filter.setAgencyType(org.digijava.module.visualization.util.Constants.DONOR_AGENCY);//Set agency type as donor to get the growth
		
		String format = request.getParameter("format");
		
		BigDecimal divideByMillionDenominator = new BigDecimal(1000000 / 
				Math.max(1000000, AmountsUnits.getDefaultValue().divider)); // Math.max added for the future, in case "billions" appears, as we don't want to divide by zero lower down the road
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
        
        Long year = null;
		Long previousYear = null;
		if(request.getParameter("ODAGrowthStartYear") != null 
				&& !request.getParameter("ODAGrowthStartYear").toString().equalsIgnoreCase("")
				&& request.getParameter("ODAGrowthEndYear") != null 
				&& !request.getParameter("ODAGrowthEndYear").toString().equalsIgnoreCase(""))
		{
			previousYear = Long.parseLong(request.getParameter("ODAGrowthStartYear"));
			year = Long.parseLong(request.getParameter("ODAGrowthEndYear"));

		}
		else if(request.getParameter("currentYear") != null && request.getParameter("previousYear") != null 
				&& !request.getParameter("currentYear").toString().equalsIgnoreCase("") && !request.getParameter("previousYear").toString().equalsIgnoreCase(""))
		{
			year = Long.parseLong(request.getParameter("currentYear"));
			previousYear = Long.parseLong(request.getParameter("previousYear"));
		}
		else
		{
			year = filter.getEndYear();
			previousYear = filter.getEndYear()-1;
		}
		
       
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
			organizationList.addAll(DbUtil.getAgencies(filter, true));
		}
        //organizationList = DbUtil.getOrganizations(filter);
        if (filter.getSelOrgIds()!= null && filter.getSelOrgIds().length == 1 && filter.getSelOrgIds()[0]!=-1){
        	// the user has selected / filtered by org
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
	            DecimalWraper fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	            BigDecimal amtCurrentYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, i-1);
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, i-1);
	            fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), filter.getAdjustmentType(), false)[0];
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
        	
        	// no org filter - take all the organizations one by one and compare
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
	            DecimalWraper fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	            BigDecimal amtCurrentYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            startDate = DashboardUtil.getStartDate(fiscalCalendarId, previousYear.intValue());
	            endDate = DashboardUtil.getEndDate(fiscalCalendarId, previousYear.intValue());
	            fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, newFilter.getTransactionType(), filter.getAdjustmentType(), false)[0];
	            //filter.setOrgIds(temp);
	            BigDecimal amtPreviousYear = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	            if (amtCurrentYear.compareTo(BigDecimal.ZERO) == 1 && amtPreviousYear.compareTo(BigDecimal.ZERO) == 1){
	            	BigDecimal growthPercent = amtCurrentYear.divide(amtPreviousYear, RoundingMode.HALF_UP).subtract(new BigDecimal(1)).multiply(new BigDecimal(100));
	                if (!ignore || (growthPercent.compareTo(new BigDecimal(100))==-1) && (growthPercent.compareTo(new BigDecimal(-100))==1)) {
	                	map.put(ampOrganisation, growthPercent);
	                    csvString.append(ampOrganisation.getName().replace(",", " ").replace('\r', ' ').replace('\n', ' '));
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
	                if (graph.equals("BeneficiaryAgencyProfile")) {
						vForm.getExportData().setBeneficiaryAgencyGraph(image);
						logger.info("Creating image from Beneficiary Agency graph");
	                }
	                if (graph.equals("ResponsibleOrganization")) {
						vForm.getExportData().setResponsibleOrganizationGraph(image);
						logger.info("Creating image from ResponsibleOrganization graph");
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
	                if (graph.equals("SecondaryProgramProfile")) {
						vForm.getExportData().setSecondaryProgramGraph(image);
						logger.info("Creating image from Secondary Program graph");
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
		AmpCurrency currency = CurrencyUtil.getAmpcurrency(visualizationForm.getFilter().getCurrencyId());
		
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "ProgramsList");
			rootObjects.put("list", rankObjects);
		}
		else if (objectType != null && objectType.equalsIgnoreCase("secondaryPrograms")){
			Map<AmpTheme, BigDecimal> secProgramsList = visualizationForm.getRanksInformation().getFullSecondaryPrograms();
			List list = null;
			if (secProgramsList!=null) {
				list = new LinkedList(secProgramsList.entrySet());
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map.Entry entry = (Map.Entry) iterator.next();
					child.put("name", entry.getKey().toString());
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
					rankObjects.add(child);
				}
			}
			rootObjects.put("type", "SecondaryProgramsList");
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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
					child.put("value", FormatHelper.formatNumberNotRounded(((BigDecimal) entry.getValue()).doubleValue()) + " " + currency.getCurrencyName());
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