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
		if (eaForm.getMeasureId() != null && 
				eaForm.getIssueId() != null && 
				eaForm.getActorId() != null &&
				eaForm.getIssueId().longValue() > 0 && 
				eaForm.getMeasureId().longValue() > 0 &&
				eaForm.getActorId().longValue() > 0) {
			logger.debug("In here");
			Issues issue = new Issues();
			issue.setId(eaForm.getIssueId());
			int index = eaForm.getIssues().indexOf(issue);
			issue = (Issues) eaForm.getIssues().get(index);
			
			Measures measures = new Measures();
			measures.setId(eaForm.getMeasureId());
			index = issue.getMeasures().indexOf(measures);
			measures = (Measures) issue.getMeasures().get(index);
			AmpActor actor = new AmpActor();
			actor.setAmpActorId(eaForm.getActorId());
			index = measures.getActors().indexOf(actor);
			actor = (AmpActor) measures.getActors().get(index);
			eaForm.setActor(actor.getName());
		} else {
			eaForm.setActor(null);
		}
		return mapping.findForward("forward");
	}
}