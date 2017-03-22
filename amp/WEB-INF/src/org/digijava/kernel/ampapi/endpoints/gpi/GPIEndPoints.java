package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.List;
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
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;

@Path("gpi")
public class GPIEndPoints implements ErrorReportingEndpoint {
	/***
	 * retrieves a list of aid on budget objects
	 * @param offset
	 * @param count maximum number of records to return
	 * @param orderBy field that will be used for sorting
	 * @param sort asc or desc order
	 * @return A json object with a list of aid on budget and paging information
	 */
	@GET
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetList", ui = false)
	public JsonBean getAidOnBudgetList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
			@QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
		return GPIDataService.getAidOnBudgetList(offset, count, orderBy, sort);
	}

	/***
	 * Retrieves object aid on budget by id 
	 * @param id the ID that will be used to query the database
	 * @return a json object of the found aid on budget object
	 */
	@GET
	@Path("/aid-on-budget/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetById", ui = false)
	public JsonBean getAidOnBudgetById(@PathParam("id") long id) {
		return GPIDataService.getAidOnBudgetById(id);
	}

	/***
	 * saves an aid on budget object to the database
	 * @param aidOnBudget json data that will be used to create or update an aid on budget row
	 * @return json object of the created or updated aid on budget object. Also returns any error messages that occur
	 */
	@POST
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAidOnBudget", ui = false)
	public JsonBean saveAidOnBudget(JsonBean aidOnBudget) {
		return GPIDataService.saveAidOnBudget(aidOnBudget);
	}

	/**
	 * Saves a list of aid on budget objects to the database
	 * @param aidOnBudgetList - list of aid on budget objects that will be saved
	 * @return list of aid on budget objects that were saved. Also returns any server side validation errors
	 */
	@POST
	@Path("/aid-on-budget/save-all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAllEdits", ui = false)
	public List<JsonBean> saveAllEdits(List<JsonBean> aidOnBudgetList) {
		return GPIDataService.saveAidOnBudget(aidOnBudgetList);
	}

	/***
	 * Deletes the aid on budget row on the database
	 * @param id identifier to query for aid on budget to delete
	 * @return object containing the result
	 */
	@DELETE
	@Path("/aid-on-budget/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "deleteAidOnBudgetById", ui = false)
	public JsonBean deleteAidOnBudgetById(@PathParam("id") long id) {
		return GPIDataService.deleteAidOnBudgetById(id);
	}

	/***
	 * saves a donorNotes object to the database
	 * @param donorNotes json representation of the donorNotes object to be saved
	 * @return json object containing the donorNotes object that and any server side validation errors
	 */
	@POST
	@Path("/donor-notes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveDonorNotes", ui = false)
	public JsonBean saveDonorNotes(JsonBean donorNotes) {
		return GPIDataService.saveDonorNotes(donorNotes);
	}

	/***
	 * saves a list donorNotes objects to the database
	 * @param donorNotes list of donorNotes objects
	 * @return list of donorNotes objects that were saved. Also returns any server side validation errors
	 */
	@POST
	@Path("/donor-notes/save-all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAllDonorNotes", ui = false)
	public List<JsonBean> saveAllDonorNotes(List<JsonBean> donorNotes) {
		return GPIDataService.saveDonorNotes(donorNotes);
	}

	/***
	 * Retrieves a list of donor notes
	 * @param offset
	 * @param count maximum number of records to return
	 * @param orderBy field that will be used for sorting
	 * @param sort asc or desc order
	 * @return a json object containing a list of donors notes and paging information
	 */
	@GET
	@Path("/donor-notes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getDonorNotesList", ui = false)
	public JsonBean getDonorNotesList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
			@QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
		return GPIDataService.getDonorNotesList(offset, count, orderBy, sort);
	}

	/***
	 * Deletes the donor notes object from the database
	 * @param id identifier to query for object to delete
	 * @return json object containing the result
	 */
	@DELETE
	@Path("/donor-notes/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "deleteDonorNotesId", ui = false)
	public JsonBean deleteDonorNotesById(@PathParam("id") long id) {
		return GPIDataService.deleteDonorNotesById(id);
	}

	/***
	 * Retrieves a list of verified organizations associated with the logged in user
	 * @return list of verified organizations associated with the logged in user
	 */
	@GET
	@Path("/users-verified-orgs")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getUsersVerifiedOrganizations", ui = false)
	public List<JsonBean> getUsersVerifiedOrganizations() {
		return GPIDataService.getUsersVerifiedOrganizations();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class getErrorsClass() {
		return GPIErrors.class;
	}
}
