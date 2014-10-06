package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.OrgTypeSkeleton;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.ObjectNotFoundException;

/**
 * Class that holds method related to filtres for gis querys (available options,
 * available filters)
 * 
 * @author jdeanquin@developmentgateway.org
 * 
 */
@Path("filters")
public class Filters {
	private static final Logger logger = Logger.getLogger(Filters.class);

	AmpARFilter filters;

	public Filters() {
		filters = new AmpARFilter();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AvailableMethod> getAvailableFilters() {
		return GisUtil.getAvailableMethods(Filters.class.getName());
	}

	
	/**
	 * Return activity status options
	 * 
	 * @return
	 */
	@GET
	@Path("/activityapprovalStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="ActivityApprovalStatus")
	public List<SimpleJsonBean> getActivityApprovalStatus() {

		List<SimpleJsonBean> activityStatus = new ArrayList<SimpleJsonBean>();

		Set<String> aprovedStatus = AmpARFilter.activityStatus;
		for (String s : aprovedStatus) {
			SimpleJsonBean sjb = new SimpleJsonBean();

			switch (s) {
			case Constants.APPROVED_STATUS:
				sjb.setId(Constants.APPROVED_STATUS);
				sjb.setName(TranslatorWorker.translateText("Edited and validated"));
				break;
			case Constants.EDITED_STATUS:
				sjb.setId(Constants.EDITED_STATUS);
				sjb.setName(TranslatorWorker.translateText("Edited but not validated"));
				break;
			case Constants.STARTED_APPROVED_STATUS:
				sjb.setId(Constants.STARTED_APPROVED_STATUS);
				sjb.setName(TranslatorWorker.translateText("New and validated"));
				break;
			case Constants.STARTED_STATUS:
				sjb.setId(Constants.STARTED_STATUS);
				sjb.setName(TranslatorWorker.translateText("New"));
				break;
			case Constants.NOT_APPRVED:
				sjb.setId(Constants.NOT_APPRVED);
				sjb.setName(TranslatorWorker.translateText("Not Approved"));
				break;
			case Constants.REJECTED_STATUS:
				sjb.setId(Constants.REJECTED_STATUS);
				sjb.setName(TranslatorWorker.translateText("Edited and rejected"));
				break;
			}
			activityStatus.add(sjb);
		}

		return activityStatus;
	}

	/**
	 * Return the adminlevels for filtering
	 * 
	 * @return
	 */
	@GET
	@Path("/boundaries")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="Boundaries")
	public List<String> getBoundaries() {
		// This should never change should they return from database?
		return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone",
				"District"));
	}

	/**
	 * Returns the sector schema lists
	 * 
	 * @return
	 */
	@GET
	@Path("/sectors")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="Sectors")
	public List<SimpleJsonBean> getSectorsSchemas() {
		List<SimpleJsonBean> schemalist = new ArrayList<SimpleJsonBean>();
		List<AmpClassificationConfiguration> schems = SectorUtil.getAllClassificationConfigs();
		for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
			schemalist.add(new SimpleJsonBean(
					ampClassificationConfiguration.getId(),
					ampClassificationConfiguration.getName()));
		}
		return schemalist;
	}

	/**
	 * Return the sector filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/sectors/{sectorId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, name = "SectorsById")
	public SimpleJsonBean getSectors(@PathParam("sectorId") Long sectorId) {

		SimpleJsonBean sector = new SimpleJsonBean();

		try {
			AmpClassificationConfiguration c = SectorUtil
					.getClassificationConfigById(sectorId);

			String sectorConfigName = c.getName();
			List<SimpleJsonBean> ampSectorsList = new ArrayList<SimpleJsonBean>();
			sector.setId(sectorId);
			sector.setName(sectorConfigName);
			List<AmpSector> s = SectorUtil
					.getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
			for (AmpSector ampSector : s) {
				ampSectorsList.add(getSectors(ampSector));
			}
			sector.setChildren(ampSectorsList);
		} catch (DgException e) {
			logger.error("Cannot get sector by id",e);
		}
		return sector;
	}
	/**
	 * Return the year range configure for GIS
	 * @return
	 */
	@GET
	@Path("/dates/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="Dates")
	public JsonBean getDates(){
		JsonBean date=new JsonBean();
		date.set("startYear", FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.GIS_DEFAUL_MIN_YEAR_RANGE));
		date.set("endYear", FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.GIS_DEFAUL_MAX_YEAR_RANGE));
		return date;
	}
	

	/**
	 * Return the programs filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/programs")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="Programs")
	
	public List<SimpleJsonBean> getPrograms() {
		List<SimpleJsonBean> programs = new ArrayList<SimpleJsonBean>();
		try {
			
			for (Object p : ProgramUtil.getAmpActivityProgramSettingsList()) {
				
				AmpActivityProgramSettings program=(AmpActivityProgramSettings)p;
				programs.add( new SimpleJsonBean(program.getAmpProgramSettingsId(),
						program.getName()) );
				
			}
			return programs;
		} catch (DgException e) {
			logger.error("cannot get program list",e);
			return programs;
		}

		
	}
	/**
	 * Return org types with its orgs groups
	 * 
	 * @return
	 *	[
	 *		{
     * 		id: 0,
     * 		name: “some org type”,
     * 		groupIds: [:id, :id, :id]  //ids of child groups
   	 *		},
	 *	..
	 *	]
	 */
	@GET
	@Path("/org-types")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="organizationTypesList")
	
	public List<JsonBean> getOrgTypes() {

		return QueryUtil.getOrgTypes();
	}

	/**
	 * Return org types with its orgs groups
	 * 
	 * @return
	 *	[
   	 *		{
     * 		id: 0,
     * 		name: “some org group”,
     * 		typeId: :id,                  //id of parent type
     * 		orgIds: [:id, :id, :id]    //ids of child orgs
   	 *		},
   	 *	....
	 *	]
	 */
	@GET
	@Path("/org-groups")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="orgGroupsList")
	
	public List<SimpleJsonBean> getOrgGroups() {
		return null;
	}	

	/**
	 *  List all available orgs

	 * @return
	 *	[
   	 *	 	{
     *		id: 0,
     *		name: “some org”,
     *		groupId: :id,                  //id of parent group
     *		roleIds: [:id, :id, :id]    //ids of all roles this org appears in ie. [0,2]
   	 *		},
	 *	...
	 *	]
	 */
	@GET
	@Path("/orgs")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="orgsList")
	
	public List<SimpleJsonBean> getOrgs() {
		return null;
	}	

	/**
	 * List all available orgs roles
	 * 
	 * @return
	 *	[
	 *		{
     * 		id: 0,
     * 		name: “Donor”
   	 *		},
	 *	...
	 *	]
	 */	

	@GET
	@Path("/org-roles")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="orgRolesList")
	
	public List<SimpleJsonBean> getorgRoles() {
		return null;
	}	
	
////begin org old
//	/**
//	 * Return the organization filtered by the given role or by the given orgGroup
//	 * 
//	 * @return
//	 */
//	@GET
//	@Path("/organizations")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiMethod(ui=false,name="OrganizationsByRoleAndByGroupId")
//	public List<SimpleJsonBean> getOrganizations(
//			@QueryParam("ampRoleId") Long orgRole,@QueryParam("orgGroupId") List<Long> ampOrgGroupId) {
//		
//		List<SimpleJsonBean> organizations = new ArrayList<SimpleJsonBean>();
//		List<OrganizationSkeleton> s=null;
//		if(orgRole!=null){
//		 s= QueryUtil.getOrganizations(orgRole);
//		}else{
//			s= QueryUtil.getOrganizations(ampOrgGroupId);
//		}
//		for (OrganizationSkeleton organizationSkeleton : s) {
//			organizations.add(new SimpleJsonBean(organizationSkeleton
//					.getAmpOrgId(), organizationSkeleton.getName()));
//		}
//
//		return organizations;
//	}
//	/**
//	 * Return the organization  list
//	 * 
//	 * @return
//	 */
//	@POST
//	@Path("/organizations")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiMethod(ui=true,name="Organizations")
//	public List<SimpleJsonBean> getOrganizations(final JsonBean filter) {
//		List<SimpleJsonBean> orgs = new ArrayList<SimpleJsonBean>();
//		return orgs;
//	}
//	/**
//	 * Return the organization role list
//	 * 
//	 * @return
//	 */
//	@POST
//	@Path("/organizationsRoles")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiMethod(ui=true,name="organizationsRoles")
//	public List<SimpleJsonBean> getOrganizationsRoles(final JsonBean filter) {
//		List<SimpleJsonBean> orgs = new ArrayList<SimpleJsonBean>();
//		List<AmpRole> roles = OnePagerUtil.getOrgRoles();
//		for (AmpRole ampRole : roles) {
//			SimpleJsonBean o = new SimpleJsonBean();
//			o.setId(ampRole.getAmpRoleId());
//			o.setCode(ampRole.getRoleCode());
//			o.setName(ampRole.getName());
//			orgs.add(o);
//		}
//
//		return orgs;
//	}
///end orgs old
	/**
	 * Return the programs filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/programs/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, name = "ProgramsByProgramName")
	public SimpleJsonBean getPrograms(@PathParam("programId") Long programId) {
		try {
			AmpActivityProgramSettings npd = ProgramUtil.getAmpActivityProgramSettings(programId);
			if (npd != null && npd.getDefaultHierarchy() != null) {
				return getPrograms(npd.getDefaultHierarchy());
			} else {
				return new SimpleJsonBean();
			}

		} catch (ObjectNotFoundException e) {
			return new SimpleJsonBean();
		} catch (DgException e) {
			logger.error("Cannot get program", e);
			return null;
		}
	}


	/**
	 * Return financing instruments 
	 * 
	 * @return
	 */
	@GET
	@Path("/typeOfAssistance/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "TypeOfAssistanceList")
	public List<SimpleJsonBean> getTypeOfAssistance() {
		return getCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
	}
	/**
	 * Return Activitystatus 
	 * 
	 * @return
	 */
	@GET
	@Path("/activityStatus/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "ActivityStatusList")
	public List<SimpleJsonBean> getActivityStatus() {
		return getCategoryValue(CategoryConstants.ACTIVITY_STATUS_KEY);
	}

	/**
	 * Return Activity Budget
	 * 
	 * @return
	 */
	@GET
	@Path("/activityBudget/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "ActivityBudgetList")
	public List<SimpleJsonBean> getActivityBudget() {
		return getCategoryValue(CategoryConstants.ACTIVITY_BUDGET_KEY);
	}	
	
	
	/**
	 * Return financing instruments 
	 * 
	 * @return
	 */
	@GET
	@Path("/financingInstruments/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "FinancingInstrumentsList")
	public List<SimpleJsonBean> getFinancingInstruments() {
		return getCategoryValue(CategoryConstants.FINANCING_INSTRUMENT_KEY);
	}

	private List<SimpleJsonBean> getOrgGroup(Long orgTypeId) {
		List<SimpleJsonBean>orgGroupReturn=new ArrayList<SimpleJsonBean>();
		Collection<AmpOrgGroup> aogList;
		if(orgTypeId==null){
			aogList = DbUtil.getAllOrgGroups();
		}else{
			aogList =DbUtil.searchForOrganisationGroupByType(orgTypeId);
		}
		for (AmpOrgGroup ampOrgGroup : aogList) {
			orgGroupReturn.add(new SimpleJsonBean(ampOrgGroup.getIdentifier(),ampOrgGroup.getName(),ampOrgGroup.getOrgGrpCode(),ampOrgGroup.getOrgType().getAmpOrgTypeId()));
		}
		return orgGroupReturn;
	}
	

//	/**
//	 * Return Organization Group filtered by orgTypeId 
//	 * 
//	 * @return
//	 */
//	@GET
//	@Path("/organizationGroup")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiMethod(ui = true, name = "OrganizationGroupList")
//	public List<SimpleJsonBean> getorganizationGroupByOrgTypeId(@QueryParam("orgTypeId") Long orgTypeId) {
//		return getOrgGroup(orgTypeId);
//	}
//	
//	/**
//	 * Return Organization Group 
//	 * 
//	 * @return
//	 */
//	@GET
//	@Path("/organisationTypes/")
//	@Produces(MediaType.APPLICATION_JSON)
//	@ApiMethod(ui = true, name = "OrgTypesList")
//	public List<SimpleJsonBean> getOrgTypes(){
//		List<SimpleJsonBean> orgTypes=new ArrayList<SimpleJsonBean>();
//		List<OrgTypeSkeleton> orgTypesSk= DbUtil.getAllOrgTypesFaster();
//		for (OrgTypeSkeleton orgTypeSkeleton : orgTypesSk) {
//			orgTypes.add(new SimpleJsonBean(orgTypeSkeleton.getOrgTypeId(),orgTypeSkeleton.getOrgTypeName(),orgTypeSkeleton.getOrgTypeCode()));
//		}
//		
//		return orgTypes;
//	}
	
	private List<SimpleJsonBean> getCategoryValue(String categoryKey) {
		List<SimpleJsonBean> fi = new ArrayList<SimpleJsonBean>();
		Collection<AmpCategoryValue> col = CategoryManagerUtil
				.getAmpCategoryValueCollectionByKey(categoryKey);
		for (AmpCategoryValue ampCategoryValue : col) {
			fi.add(new SimpleJsonBean(ampCategoryValue.getIdentifier(),
					ampCategoryValue.getLabel()));
		}
		return fi;
	}
	/**
	 * Get JsonEnable object for programs
	 * 
	 * @param t
	 *            AmpThem to get the programFrom
	 * @return
	 */
	private SimpleJsonBean getPrograms(AmpTheme t) {
		SimpleJsonBean p = new SimpleJsonBean();
		p.setId(t.getAmpThemeId());
		p.setName(t.getName());
		p.setChildren(new ArrayList<SimpleJsonBean>());

		for (AmpTheme tt : t.getSiblings()) {
			p.getChildren().add(getPrograms(tt));
		}
		return p;
	}

	/**
	 * Get Sectors from AmpSector
	 * 
	 * @param as
	 * @return
	 */

	private SimpleJsonBean getSectors(AmpSector as) {
		SimpleJsonBean s = new SimpleJsonBean();
		s.setId(as.getAmpSectorId());
		s.setCode(as.getSectorCodeOfficial());
		s.setName(as.getName());
		s.setChildren(new ArrayList<SimpleJsonBean>());
		for (AmpSector ampSectorChild : as.getSectors()) {
			s.getChildren().add(getSectors(ampSectorChild));
		}

		return s;
	}

	

}
