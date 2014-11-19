package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.reports.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.ObjectNotFoundException;

/**
 * Class that holds method related to filters for gis query (available options,
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
		return EndpointUtils.getAvailableMethods(Filters.class.getName());
	}

	
	/**
	 * Return activity status options
	 * 
	 * @return
	 */
	@GET
	@Path("/activityapprovalStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, id = "ActivityApprovalStatus", column = ColumnConstants.APPROVAL_STATUS,name="Approval Status")
	public JsonBean getActivityApprovalStatus() {
		JsonBean as=new JsonBean();

		List<SimpleJsonBean> activityStatus = new ArrayList<SimpleJsonBean>();
		Set<String> keys=AmpARFilter.activityStatusToNr.keySet();
		for (String key : keys) {
			SimpleJsonBean sjb = new SimpleJsonBean();
			sjb.setId(AmpARFilter.activityStatusToNr.get(key));
			sjb.setName(key);
			activityStatus.add(sjb);
		}
		as.set("filterId", ColumnConstants.APPROVAL_STATUS);
		as.set("values",activityStatus);
		return as;
	}

	/**
	 * Return the adminlevels for filtering
	 * 
	 * @return
	 */
	@GET
	@Path("/boundaries")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false,  id = "Boundaries")
	public List<String> getBoundaries() {
		return QueryUtil.getImplementationLocationsInUse();
	}

	/**
	 * Returns the sector schema lists
	 * 
	 * @return
	 */
	@GET
	@Path("/sectors")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "Sectors", id = "Sectors")
	public List<JsonBean> getSectorsSchemas() throws AmpApiException{
		List<JsonBean> schemaList = new ArrayList<JsonBean>();
		List<AmpClassificationConfiguration> schems = SectorUtil.getAllClassificationConfigs();
		Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
		for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
			final String columnName = AmpClassificationConfiguration.NAME_TO_COLUMN_MAP
					.get(ampClassificationConfiguration.getName()); 
			if (visibleColumns.contains(columnName)) {
				JsonBean schema=new JsonBean();
				schema.set("id", ampClassificationConfiguration.getId());
				schema.set("name", ampClassificationConfiguration.getName());
				schemaList.add(schema);
			}
		}
		return schemaList;
	}

	/**
	 * Return the sector filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/sectors/{sectorId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "SectorsById")
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
				ampSectorsList.add(getSectors(ampSector,sectorConfigName,1));
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
	@ApiMethod(ui = true, name = "Dates", id = "Dates")
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
	@ApiMethod(ui = true, name = "Programs", id = "Programs")
	
	public List<SimpleJsonBean> getPrograms() {
		List<SimpleJsonBean> programs = new ArrayList<SimpleJsonBean>();
		try {
			Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
			
			for (Object p : ProgramUtil.getAmpActivityProgramSettingsList()) {
				
				AmpActivityProgramSettings program=(AmpActivityProgramSettings)p;
				final String columnName = ProgramUtil.NAME_TO_COLUMN_MAP
						.get(program.getName()); 
				//only add if its enabled 
				if (visibleColumns.contains(columnName)){
					programs.add( new SimpleJsonBean(program.getAmpProgramSettingsId(),
							program.getName()) );
				}
				
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
	 *			"id": 39,
     *   		"groupIds": [43,67,46,33,49,66,21,64,41,63],
     *   		"name": "Multilateral"
     *		},
	 *	..
	 *	]
	 */
	@GET
	@Path("/org-types")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, name = "Types of Organizations", id = "organizationTypesList")
	
	public List<JsonBean> getOrgTypes() {

		return QueryUtil.getOrgTypes();
	}

	/**
	 * Return org grous with its orgs ids
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
	@ApiMethod(ui = true, name = "Organization Groups", id = "orgGroupsList")
	
	public List<JsonBean> getOrgGroups() {
		return QueryUtil.getOrgGroups();
		
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
	@ApiMethod(ui = true, id = "Organizations", name = "orgsList")
	
	public List<JsonBean> getOrgs() { 
		return QueryUtil.getOrgs();
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
	@ApiMethod(ui = true, name = "Organization Roles", id = "orgRolesList")
	
	public List<SimpleJsonBean> getorgRoles() {
		return QueryUtil.getOrgRoles();
	}	
	

	/**
	 * Return the programs filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/programs/{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = false, id = "ProgramsByProgramName")
	public SimpleJsonBean getPrograms(@PathParam("programId") Long programId) {
		try {
			AmpActivityProgramSettings npd = ProgramUtil.getAmpActivityProgramSettings(programId);
			
			if (npd != null && npd.getDefaultHierarchy() != null) {
				return getPrograms(npd.getDefaultHierarchy(),npd.getName(),0);
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
	@ApiMethod(ui = true, id = "TypeOfAssistanceList", column = ColumnConstants.TYPE_OF_ASSISTANCE,name="Type of Assistance")
	public JsonBean getTypeOfAssistance() {
		return getCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY,ColumnConstants.TYPE_OF_ASSISTANCE);
	}
	/**
	 * Return Activitystatus 
	 * 
	 * @return
	 */
	@GET
	@Path("/activityStatus/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, id = "ActivityStatusList", column = ColumnConstants.STATUS,name="Activity Status")
	public JsonBean getActivityStatus() {
		return getCategoryValue(CategoryConstants.ACTIVITY_STATUS_KEY,
				ColumnConstants.STATUS);
	}

	/**
	 * Return Activity Budget
	 * 
	 * @return
	 */
	@GET
	@Path("/activityBudget/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, id = "ActivityBudgetList", column = ColumnConstants.ON_OFF_TREASURY_BUDGET, name="Activity Budget")
	public JsonBean getActivityBudget() {
		return getCategoryValue(CategoryConstants.ACTIVITY_BUDGET_KEY, ColumnConstants.ON_OFF_TREASURY_BUDGET);
	}	
	
	
	/**
	 * Return financing instruments 
	 * 
	 * @return
	 */
	@GET
	@Path("/financingInstruments/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui = true, id = "FinancingInstrumentsList", column = ColumnConstants.FINANCING_INSTRUMENT,name="Financing Instruments")
	public JsonBean getFinancingInstruments() {
		return getCategoryValue(CategoryConstants.FINANCING_INSTRUMENT_KEY, ColumnConstants.FINANCING_INSTRUMENT);
	}

	private List<SimpleJsonBean> getCategoryValue(String categoryKey) {
		List<SimpleJsonBean> fi = new ArrayList<SimpleJsonBean>();

		Collection<AmpCategoryValue> col = CategoryManagerUtil
				.getAmpCategoryValueCollectionByKey(categoryKey);
		for (AmpCategoryValue ampCategoryValue : col) {
			String translatedValue = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue);
			fi.add(new SimpleJsonBean(ampCategoryValue.getIdentifier(),
					translatedValue));
		}
		return fi;
		
	}
/**
 * used to return AmpCategoryClass values wrapped to be provided to the filter widget
 * @param categoryKey
 * @param filterId
 * @return
 */
	private JsonBean getCategoryValue(String categoryKey,String filterId) {
		JsonBean js=new JsonBean();
		js.set("filterId",filterId);
		js.set("values",getCategoryValue(categoryKey));
		return js;
		
	}
	/**
	 * Get JsonEnable object for programs
	 * 
	 * @param t
	 *            AmpThem to get the programFrom
	 * @return
	 */
	private SimpleJsonBean getPrograms(AmpTheme t,String programName,Integer level) {
		SimpleJsonBean p = new SimpleJsonBean();
		p.setId(t.getAmpThemeId());
		p.setName(t.getName());
		p.setChildren(new ArrayList<SimpleJsonBean>());
		String columnName=null;
		if(level>0){
			if(programName.equals(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)){
				columnName=ColumnConstants.NATIONAL_PLANNING_OBJECTIVES +" Level " +level+ " Id";
			}else{
				if(programName.equals(ProgramUtil.PRIMARY_PROGRAM)){
					columnName=ColumnConstants.PRIMARY_PROGRAM +" Level " +level+ " Id";
				}else{
					if(programName.equals(ProgramUtil.SECONDARY_PROGRAM)){
						columnName=ColumnConstants.SECONDARY_PROGRAM +" Level " +level+ " Id";
					}else{
						if(programName.equals(ProgramUtil.TERTIARY_PROGRAM)){
							columnName=ColumnConstants.TERTIARY_PROGRAM +" Level " +level+ " Id";
						}
					}
				}
			}
			p.setFilterId(columnName);
		}
		level++;
		for (AmpTheme tt : t.getSiblings()) {
			p.getChildren().add(getPrograms(tt,programName,level));
		}
		return p;
	}

	/**
	 * Get Sectors from AmpSector
	 * 
	 * @param as
	 * @param sectorConfigName 
	 * @return
	 */

	private SimpleJsonBean getSectors(AmpSector as, String sectorConfigName,Integer level) {
		SimpleJsonBean s = new SimpleJsonBean();
		s.setId(as.getAmpSectorId());
		s.setCode(as.getSectorCodeOfficial());
		s.setName(as.getName());
		s.setChildren(new ArrayList<SimpleJsonBean>());
		String columnName=null;
		if(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME.equals(sectorConfigName)){
			switch (level) {
			case 1:
				columnName=ColumnConstants.PRIMARY_SECTOR_ID;
				break;
			case 2:
				columnName=ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR_ID;
				break;
			case 3:
				columnName=ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR_ID;				
				break;
			}
		}else{
			if(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME.equals(sectorConfigName)){	
				switch (level) {
				case 1:
					columnName=ColumnConstants.SECONDARY_SECTOR_ID;
					break;
				case 2:
					columnName=ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR_ID;
					break;
				case 3:
					columnName=ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR_ID;				
					break;
				}
			}else{
				if (AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME
						.equals(sectorConfigName)) {
					switch (level) {
					case 1:
						columnName = ColumnConstants.TERTIARY_SECTOR_ID;
						break;
					case 2:
						columnName = ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR_ID;
						break;
					case 3:
						columnName = ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR_ID;
						break;
					}
				}
			}
		}
		s.setFilterId(columnName);
		level++;
		for (AmpSector ampSectorChild : as.getSectors()) {
			s.getChildren().add(getSectors(ampSectorChild,sectorConfigName,level));
		}
		return s;
	}

	

}
