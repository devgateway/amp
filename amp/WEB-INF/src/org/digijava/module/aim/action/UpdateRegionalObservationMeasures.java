/*
 * UpdateMeasures.java
 * Created : 06-Sep-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;

public class UpdateRegionalObservationMeasures extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		if (eaForm.getObservations().getMeasure() != null && 
				eaForm.getObservations().getMeasure().trim().length() > 0) {
			if (eaForm.getObservations().getIssueId() != null) {
				Issues issue = new Issues();
				issue.setId(eaForm.getObservations().getIssueId());
				Measures measure = new Measures();
				if (eaForm.getObservations().getMeasureId() == null || 
						eaForm.getObservations().getMeasureId().longValue() < 0) {
					measure.setId(new Long(System.currentTimeMillis()));
				} else {
					measure.setId(eaForm.getObservations().getMeasureId());
				}
				int index = eaForm.getObservations().getIssues().indexOf(issue);
				issue = (Issues) eaForm.getObservations().getIssues().get(index);
				if (issue.getMeasures() == null) {
					issue.setMeasures(new ArrayList());
					measure.setName(eaForm.getObservations().getMeasure());
					issue.getMeasures().add(measure);
				} else {
					int mIndex = issue.getMeasures().indexOf(measure);
					if (mIndex < 0) {
						measure.setName(eaForm.getObservations().getMeasure());
						issue.getMeasures().add(measure);
					} else {
						measure = (Measures) issue.getMeasures().get(mIndex);
						measure.setName(eaForm.getObservations().getMeasure());
						issue.getMeasures().set(mIndex,measure);	
					}
				}
				eaForm.getObservations().getIssues().set(index,issue);				
				eaForm.getObservations().setMeasure(null);
				eaForm.getObservations().setIssueId(null);
			}
		}
		return mapping.findForward("forward");
	}
}