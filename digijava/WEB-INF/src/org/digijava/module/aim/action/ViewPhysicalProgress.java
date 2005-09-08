package org.digijava.module.aim.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.form.PhysicalProgressForm;
import org.digijava.module.aim.helper.COverSubString;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.SelectComponent;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewPhysicalProgress extends TilesAction {
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session
				.getAttribute("currentMember");

		PhysicalProgressForm formBean = (PhysicalProgressForm) form;
		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			Long ampActivityId = new Long(request.getParameter("ampActivityId"));
			AmpActivity ampActivity = DbUtil
					.getProjectChannelOverview(ampActivityId);
			ArrayList dbReturnSet1 = null;
			String dbReturn;
			Iterator iter1 = null;
			DecimalFormat mf = new DecimalFormat("###,###,###,###,###");

			if (ampActivity != null) {
				if (teamMember.getAppSettings().getPerspective() == null)
					formBean.setPerspective("Donor");
				else
					formBean.setPerspective(teamMember.getAppSettings()
							.getPerspective());
				formBean.setAmpActivityId(ampActivity.getAmpActivityId());
				formBean.setFlag(COverSubString.getCOverSubStringLength(
						ampActivity.getDescription(), 'D'));

				dbReturnSet1 = DbUtil.getAmpComponent(ampActivity
						.getAmpActivityId());

				iter1 = dbReturnSet1.iterator();
				String date = "";
				formBean.setSelectComponent(new ArrayList());

				while (iter1.hasNext()) {
					AmpComponent progress = (AmpComponent) iter1.next();
					SelectComponent sc = new SelectComponent();

					sc.setCid(progress.getAmpComponentId());
					sc.setTitle(progress.getTitle());
					sc.setAmount(mf.format(progress.getAmount()));
					sc.setPhysicalProgress(new ArrayList());
					if (progress.getCurrency() != null) {
						sc
								.setCurrency(progress.getCurrency()
										.getCurrencyCode());
					}
					date = DateConversion.ConvertDateToString(progress
							.getReportingDate());
					sc.setReportingDate(date);

					Iterator iter2 = null;
					iter2 = progress.getPhysicalProgress().iterator();
					while (iter2.hasNext()) {
						PhysicalProgress physicalProgress = new PhysicalProgress();
						AmpPhysicalPerformance phypm = (AmpPhysicalPerformance) iter2
								.next();
						physicalProgress.setPid(phypm.getAmpPpId());
						physicalProgress.setTitle(phypm.getTitle());
						date = DateConversion.ConvertDateToString(phypm
								.getReportingDate());
						physicalProgress.setReportingDate(date);
						sc.getPhysicalProgress().add(physicalProgress);
					}
					formBean.getSelectComponent().add(sc);
				}
				
				formBean.setIssues(ActivityUtil.getIssues(ampActivityId));
			}
		}
		return null;
	}
}
