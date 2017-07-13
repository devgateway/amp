package org.digijava.kernel.ampapi.endpoints.datafreeze;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

@Path("data-freeze")
public class DataFreezeEndPoints implements ErrorReportingEndpoint {

	@POST
	@Path("event")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "saveDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
	public JsonBean saveDataFreezeEvent(DataFreezeEvent dataFreezeEvent) {
		return DataFreezeService.saveDataFreezeEvent(dataFreezeEvent);
	}

	@DELETE
	@Path("event/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "deleteDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
	public void deleteDataFreezeEvent(@PathParam("id") long id) {
		DataFreezeService.deleteDataFreezeEvent(id);
	}

	@GET
	@Path("event/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "fetchDataFreezeEvent", authTypes = { AuthRule.IN_ADMIN })
	public AmpDataFreezeSettings fetchDataFreezeEvent(@PathParam("id") long id) {
		return DataFreezeService.fetchOneDataFreezeEvent(id);
	}

	@GET
	@Path("event/list")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "fetchDataFreezeEventList", authTypes = { AuthRule.IN_ADMIN })
	public Page<DataFreezeEvent> fetchDataFreezeEventList(@QueryParam("offset") Integer offset,
			@QueryParam("count") Integer count, @QueryParam("orderby") String orderBy,
			@QueryParam("sort") String sort) {
		return DataFreezeService.fetchDataFreezeEventList(offset, count, orderBy, sort);
	}
	
	@GET
	@Path("event/list-frozen-activities")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "fetchFrozenActivities", authTypes = { AuthRule.IN_ADMIN })
	public JsonBean fetchFrozenActivities() {
		return DataFreezeService.getFronzeActivitiesInformation();
	}

	@POST
	@Path("event/unfreeze-all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "unfreezeAll", authTypes = { AuthRule.IN_ADMIN })
	public void unfreezeAll(JsonBean data) {
		DataFreezeService.unfreezeAll();
	}

	@GET
	@Path("event/activity-test")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "activityTest")
	public boolean activityTest(@QueryParam("id") Long id) {
		TeamMember tm = TeamUtil.getCurrentMember();
		AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
		AmpActivityFrozen af = DataFreezeService.getActivityFrozenForActivity(id);
		return DataFreezeService.isEditable(af, atm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class getErrorsClass() {
		return DataFreezeEndPoints.class;
	}

}
