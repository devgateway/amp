package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.ViewActivityHistoryForm;
import org.hibernate.Session;

public class ViewActivityHistory extends Action {

	private ServletContext ampContext = null;

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final Comparator<Object> compareVersions = new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				AmpActivityVersion a1 = (AmpActivityVersion) o1;
				AmpActivityVersion a2 = (AmpActivityVersion) o2;
				return a1.getAmpActivityId().compareTo(a2.getAmpActivityId() * -1);
//				if (a1.getModifiedDate() == null && a2.getModifiedDate() == null) {
//					return 0;
//				} else if (a1.getModifiedDate() == null) {
//					return (a1.getCreatedDate().compareTo(a2.getModifiedDate()));
//				} else if (a2.getModifiedDate() == null) {
//					return (a2.getCreatedDate().compareTo(a1.getModifiedDate()));
//				}
//				return a1.getModifiedDate().compareTo(a2.getModifiedDate());
			}
		};

		ViewActivityHistoryForm hForm = (ViewActivityHistoryForm) form;
		// Load current activity, get group and retrieve past versions.
		Session session = PersistenceManager.getRequestDBSession();
		AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, hForm.getActivityId());
		hForm.setActivities(new TreeSet<AmpActivity>(compareVersions));
		
		// AMP-7706: Filter last 5 versions.
		List<AmpActivity> auxList = new ArrayList<AmpActivity>(currentActivity.getAmpActivityGroup().getActivities());
		Collections.sort(auxList, compareVersions);
		Iterator<AmpActivity> iter = auxList.iterator();
		int i = 5;
		while(iter.hasNext()) {
			AmpActivity auxActivity = iter.next();
			if(i > 0) {
				hForm.getActivities().add(auxActivity);
			}
			i--;
		}
		//hForm.getActivities().addAll(currentActivity.getAmpActivityGroup().getActivities());

		return mapping.findForward("forward");
	}
}