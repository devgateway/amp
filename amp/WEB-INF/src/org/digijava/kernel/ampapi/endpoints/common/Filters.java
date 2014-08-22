package org.digijava.kernel.ampapi.endpoints.common;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.digijava.kernel.ampapi.endpoints.gis.GisEndPoints;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

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
	public List<AvailableFilters> getAvailableFilters() {
		List<AvailableFilters> availableFilters = new ArrayList<Filters.AvailableFilters>();

		AvailableFilters sector = new AvailableFilters();
		sector.setName("Sectors");
		sector.setEndpoint("/rest/filters/sectors");
		sector.setUi(Boolean.TRUE);
		availableFilters.add(sector);

		AvailableFilters activityStatus = new AvailableFilters();
		activityStatus.setName("ActivityStatus");
		activityStatus.setUi(Boolean.TRUE);
		activityStatus.setEndpoint("/rest/filters/activityStatus");
		availableFilters.add(activityStatus);

		AvailableFilters boundaries = new AvailableFilters();
		boundaries.setName("Boundaries");
		boundaries.setEndpoint("/rest/filters/boundaries");
		availableFilters.add(boundaries);

		AvailableFilters programs = new AvailableFilters();
		programs.setName("Programs");
		programs.setEndpoint("/rest/filters/programs");
		programs.setUi(Boolean.TRUE);
		availableFilters.add(programs);

		
		AvailableFilters organization= new AvailableFilters();
		organization.setName("Organizations");
		organization.setEndpoint("/rest/filters/organizations");
		organization.setUi(Boolean.TRUE);
		availableFilters.add(organization);
		return availableFilters;
	}

	/**
	 * Return activity status options
	 * 
	 * @return
	 */
	@GET
	@Path("/activityStatus")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Path("/sectors/")
	@Produces(MediaType.APPLICATION_JSON)
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
			// TODO till we find out the exception strategy
			e.printStackTrace();
		}
		return schemalist;
	}

	/**
	 * Return the sector filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/sectors/{sectorConfigName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SimpleJsonBean> getSectors(
			@PathParam("sectorConfigName") String sectorConfigName) {
		// DozerBeanMapperSingletonWrapper.getInstance().
		List<SimpleJsonBean> ampSectorsList = new ArrayList<SimpleJsonBean>();

		// Primary
		List<AmpSector> s = SectorUtil
				.getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
		for (AmpSector ampSector : s) {
			ampSectorsList.add(getSectors(ampSector));
		}
		return ampSectorsList;
	}

	/**
	 * Return the programs filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/programs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SimpleJsonBean> getPrograms() {
		List<SimpleJsonBean> programas = new ArrayList<SimpleJsonBean>();
		SimpleJsonBean npo = new SimpleJsonBean("National Plan Objective",
				"National Plan Objective");
		programas.add(npo);
		SimpleJsonBean pp = new SimpleJsonBean("Primary Program",
				"Primary Program");
		programas.add(pp);
		SimpleJsonBean sp = new SimpleJsonBean("Secondary Program",
				"Secondary Program");
		programas.add(sp);
		SimpleJsonBean tp = new SimpleJsonBean("Tertiary Program",
				"Tertiary Program");
		programas.add(tp);
		return programas;
	}

	/**
	 * Return the sector filtered by the given sectorName
	 * 
	 * @return
	 */
	@GET
	@Path("/organizations/{ampRoleId}")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Path("/programs/{programName}")
	@Produces(MediaType.APPLICATION_JSON)
	public SimpleJsonBean getPrograms(
			@PathParam("programName") String programName) {
		try {
			AmpActivityProgramSettings npd = ProgramUtil
					.getAmpActivityProgramSettings(programName);

			return getPrograms(npd.getDefaultHierarchy());

		} catch (DgException e) {
			logger.error("Cannot get program",e);
			return null;
		}
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

	public class AvailableFilters {
		public AvailableFilters() {
			this.ui = false;
		}

		private String name;
		private Boolean ui;
		private String endpoint;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public Boolean getUi() {
			return ui;
		}

		public void setUi(Boolean ui) {
			this.ui = ui;
		}

	}

}
