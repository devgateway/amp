/*
 * RemoveActor.java
 * Created : 07-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;

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
		
		if (eaForm.getIssues().getIssueId() != null && eaForm.getIssues().getMeasureId() != null) {
			
			Long issueId = eaForm.getIssues().getIssueId();
			Long measureId = eaForm.getIssues().getMeasureId();
			Long actorId = eaForm.getIssues().getActorId();
			
			ArrayList<Issues> issues = eaForm.getIssues().getIssues();
			
			for (Issues issue : issues) {
				if(issue.getId().equals(issueId)){
					ArrayList<Measures> measures = issue.getMeasures();
					for (Measures measure : measures) {
						if(measure.getId().equals(measureId)){
							
							ArrayList<AmpActor> actors = measure.getActors();
							
							for (AmpActor ampActor : actors) {
								if(ampActor.getAmpActorId().equals(actorId)){
									actors.remove(ampActor);
									break;
								}
							}
							
							break;
						}
					}
					break;
				}
			}
			eaForm.getIssues().setIssueId(null);
			eaForm.getIssues().setMeasureId(null);
			eaForm.getIssues().setActorId(null);
			
		}
		return mapping.findForward("forward");
	}
}