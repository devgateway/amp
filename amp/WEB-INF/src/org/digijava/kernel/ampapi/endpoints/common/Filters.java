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
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.AvailableMethod;
import org.digijava.kernel.ampapi.endpoints.util.GisUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
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
	@Path("/activityStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="ActivityStatus")
	public List<SimpleJsonBean> getActivityStatus() {

		List<SimpleJsonBean> activityStatus = new ArrayList<SimpleJsonBean>();

		Set<String> aprovedStatus = AmpARFilter.activityStatus;
		for (String s : aprovedStatus) {
			SimpleJsonBean sjb = new SimpleJsonBean();

			switch (s) {
			case Constants.APPROVED_STATUS:
				sjb.setId(Constants.APPROVED_STATUS);
				sjb.setName("Edited and validated");
				break;
			case Constants.EDITED_STATUS:
				sjb.setId(Constants.EDITED_STATUS);
				sjb.setName("Edited but not validated");
				break;
			case Constants.STARTED_APPROVED_STATUS:
				sjb.setId(Constants.STARTED_APPROVED_STATUS);
				sjb.setName("New and validated");
				break;
			case Constants.STARTED_STATUS:
				sjb.setId(Constants.STARTED_STATUS);
				sjb.setName("New");
				break;
			case Constants.NOT_APPRVED:
				sjb.setId(Constants.NOT_APPRVED);
				sjb.setName("Not Approved");
				break;
			case Constants.REJECTED_STATUS:
				sjb.setId(Constants.REJECTED_STATUS);
				sjb.setName("Edited and rejected");
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
		try {
			List<AmpClassificationConfiguration> schems = SectorUtil
					.getAllClassificationConfigs();
			for (AmpClassificationConfiguration ampClassificationConfiguration : schems) {
				schemalist.add(new SimpleJsonBean(
						ampClassificationConfiguration.getId(),
						ampClassificationConfiguration.getName()));
			}
		} catch (DgException e) {
			logger.error("cannot get Sectors List",e);
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
	 * Return the sector filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/organizations/{ampRoleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=false,name="OrganizationsByRole")
	public List<SimpleJsonBean> getOrganizations(
			@PathParam("ampRoleId") Long orgRole) {
		List<SimpleJsonBean> organizations = new ArrayList<SimpleJsonBean>();
		List<OrganizationSkeleton> s = QueryUtil.getOrganizations(orgRole);
		for (OrganizationSkeleton organizationSkeleton : s) {
			organizations.add(new SimpleJsonBean(organizationSkeleton
					.getAmpOrgId(), organizationSkeleton.getName()));
		}

		return organizations;
	}

	/**
	 * Return the organization type list
	 * 
	 * @return
	 */
	@GET
	@Path("/organizations")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiMethod(ui=true,name="Organizations")
	public List<SimpleJsonBean> getOrganizations() {
		List<SimpleJsonBean> orgs = new ArrayList<SimpleJsonBean>();
		List<AmpRole> roles = OnePagerUtil.getOrgRoles();
		for (AmpRole ampRole : roles) {
			SimpleJsonBean o = new SimpleJsonBean();
			o.setId(ampRole.getAmpRoleId());
			o.setCode(ampRole.getRoleCode());
			o.setName(ampRole.getName());
			orgs.add(o);
		}

		return orgs;
	}

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
