package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.List;

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

@Path("gpi")
public class GPIEndPoints implements ErrorReportingEndpoint {

	@GET
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetList", ui = false)
	public JsonBean getAidOnBudgetList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
			@QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
		return GPIDataService.getAidOnBudgetList(offset, count, orderBy, sort);
	}

	@GET
	@Path("/aid-on-budget/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getAidOnBudgetById", ui = false)
	public JsonBean getAidOnBudgetById(@PathParam("id") long id) {
		return GPIDataService.getAidOnBudgetById(id);
	}

	@POST
	@Path("/aid-on-budget")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAidOnBudget", ui = false)
	public JsonBean saveAidOnBudget(JsonBean aidOnBudget) {
		return GPIDataService.saveAidOnBudget(aidOnBudget);
	}

	@POST
	@Path("/aid-on-budget/save-all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAllEdits", ui = false)
	public List<JsonBean> saveAllEdits(List<JsonBean> aidOnBudgetList) {
		return GPIDataService.saveAidOnBudget(aidOnBudgetList);
	}

	@DELETE
	@Path("/aid-on-budget/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "deleteAidOnBudgetById", ui = false)
	public JsonBean deleteAidOnBudgetById(@PathParam("id") long id) {
		return GPIDataService.deleteAidOnBudgetById(id);
	}
	
	@POST
	@Path("/donor-notes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveDonorNotes", ui = false)
	public JsonBean saveDonorNotes(JsonBean donorNotes) {
		return GPIDataService.saveDonorNotes(donorNotes);
	}
	
	@POST
	@Path("/donor-notes/save-all")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "saveAllDonorNotes", ui = false)
	public List<JsonBean> saveAllDonorNotes(List<JsonBean> donorNotes) {
		return GPIDataService.saveDonorNotes(donorNotes);
	}
	
	@GET
	@Path("/donor-notes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getDonorNotesList", ui = false)
	public JsonBean getDonorNotesList(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count,
			@QueryParam("orderby") String orderBy, @QueryParam("sort") String sort) {
		return GPIDataService.getDonorNotesList(offset, count, orderBy, sort);
	}
	
	@DELETE
	@Path("/donor-notes/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "deleteDonorNotesId", ui = false)
	public JsonBean deleteDonorNotesById(@PathParam("id") long id) {
		return GPIDataService.deleteDonorNotesById(id);		
	}
	
	 /**
     * {@inheritDoc}
     */
    @Override
    public Class getErrorsClass() {
        return GPIErrors.class;
    }
}
