/*
 * ShowUpdateActor.java
 * Created : 07-Sep-2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;

public class ShowUpdateActor extends Action {
	
	private static Logger logger = Logger.getLogger(ShowUpdateActor.class);
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		if (eaForm.getIssues().getMeasureId() != null && 
				eaForm.getIssues().getIssueId() != null && 
				eaForm.getIssues().getActorId() != null &&
				eaForm.getIssues().getIssueId().longValue() > 0 && 
				eaForm.getIssues().getMeasureId().longValue() > 0 &&
				eaForm.getIssues().getActorId().longValue() > 0) {
			logger.debug("In here");
			Issues issue = new Issues();
			issue.setId(eaForm.getIssues().getIssueId());
			int index = eaForm.getIssues().getIssues().indexOf(issue);
			issue = (Issues) eaForm.getIssues().getIssues().get(index);
			
			Measures measures = new Measures();
			measures.setId(eaForm.getIssues().getMeasureId());
			index = issue.getMeasures().indexOf(measures);
			measures = (Measures) issue.getMeasures().get(index);
			AmpActor actor = new AmpActor();
			actor.setAmpActorId(eaForm.getIssues().getActorId());
			index = measures.getActors().indexOf(actor);
			actor = (AmpActor) measures.getActors().get(index);
			eaForm.getIssues().setActor(actor.getName());
		} else {
			eaForm.getIssues().setActor(null);
		}
		return mapping.findForward("forward");
	}
}