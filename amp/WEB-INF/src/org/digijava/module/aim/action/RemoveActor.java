/*
 * RemoveActor.java
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

public class RemoveActor extends Action {
	
	private static Logger logger = Logger.getLogger(RemoveActor.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		
		if (eaForm.getIssues().getSelActors() != null && 
				eaForm.getIssues().getSelActors().length > 0) {
			Long actors[] = eaForm.getIssues().getSelActors();
			
			if (eaForm.getIssues().getIssueId() != null &&
					eaForm.getIssues().getIssueId().longValue() > -1) {
				Issues issue = new Issues();
				issue.setId(eaForm.getIssues().getIssueId());
				int index = eaForm.getIssues().getIssues().indexOf(issue);
				issue = (Issues) eaForm.getIssues().getIssues().get(index);
				if (eaForm.getIssues().getMeasureId() != null &&
						eaForm.getIssues().getMeasureId().longValue() > -1) {
					Measures measure = new Measures();
					measure.setId(eaForm.getIssues().getMeasureId());
					int mIndex = issue.getMeasures().indexOf(measure);
					measure = (Measures) issue.getMeasures().get(mIndex);
					for (int i = 0;i < actors.length;i ++) {
						AmpActor ampActor = new AmpActor();
						ampActor.setAmpActorId(actors[i]);
						measure.getActors().remove(ampActor);
					}
					issue.getMeasures().set(mIndex,measure);
				}
				eaForm.getIssues().getIssues().set(index,issue);
				
				

				eaForm.getIssues().getIssues().set(index,issue);
			}
			eaForm.getIssues().setSelMeasures(null);
		}
		return mapping.findForward("forward");
	}
}