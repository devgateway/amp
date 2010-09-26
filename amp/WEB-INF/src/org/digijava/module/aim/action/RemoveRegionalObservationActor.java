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

public class RemoveRegionalObservationActor extends Action {

	private static Logger logger = Logger.getLogger(RemoveActor.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		if (eaForm.getObservations().getIssueId() != null && eaForm.getObservations().getMeasureId() != null) {

			Long issueId = eaForm.getObservations().getIssueId();
			Long measureId = eaForm.getObservations().getMeasureId();
			Long actorId = eaForm.getObservations().getActorId();

			ArrayList<Issues> issues = eaForm.getObservations().getIssues();

			for (Issues issue : issues) {
				if (issue.getId().equals(issueId)) {
					ArrayList<Measures> measures = issue.getMeasures();
					for (Measures measure : measures) {
						if (measure.getId().equals(measureId)) {

							ArrayList<AmpActor> actors = measure.getActors();

							for (AmpActor ampActor : actors) {
								if (ampActor.getAmpActorId().equals(actorId)) {
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
			eaForm.getObservations().setIssueId(null);
			eaForm.getObservations().setMeasureId(null);
			eaForm.getObservations().setActorId(null);

			if (actorId < 100000) {
				eaForm.getObservations().getDeletedActors().add(actorId);
			}

		}
		return mapping.findForward("forward");
	}
}