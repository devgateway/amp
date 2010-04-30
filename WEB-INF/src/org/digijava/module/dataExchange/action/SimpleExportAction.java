package org.digijava.module.dataExchange.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

public class SimpleExportAction extends Action {

	private static Logger logger = Logger.getLogger(SimpleExportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"inline; filename=export.csv ");
		//
		StringBuffer buffer = new StringBuffer();
		//
		List<AmpActivity> list = ActivityUtil.getAllActivitiesByName("");
		AmpActivity activity = null;
		//		
		for (Iterator<AmpActivity> it = list.iterator(); it.hasNext();) {
			activity = it.next();
			//
			List<AmpFunding> fundings = DbUtil.getAmpFunding(activity.getAmpActivityId());
			if ((fundings != null) && (fundings.size() > 0)) {
				AmpFunding funding = null;
				AmpOrganisation organisation = null;
				Collection fundingDetails = null;
				for (Iterator itf = fundings.iterator(); itf.hasNext();) {
					funding = (AmpFunding) itf.next();
					organisation = funding.getAmpDonorOrgId();					
//					fundingDetails = DbUtil.getFundingDetails(funding.getAmpFundingId());
//					AmpFundingDetail fundingDetail = null;
//					for (Iterator itfd = fundingDetails.iterator(); itfd.hasNext();) {
//						fundingDetail = (AmpFundingDetail) itfd.next();
//						fundingDetail.
//					}
					//
					buffer.append("\""+organisation.getOrgCode()+"\""+","+"\""+activity.getBudgetCodeProjectID()+"\""+","+"\""+activity.getProjectCode()+"\""+"\n");
				}
			} else {
				buffer.append("\"\","+"\""+activity.getBudgetCodeProjectID()+"\""+","+"\""+activity.getProjectCode()+"\""+"\n");
			}
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(response
				.getOutputStream(), "UnicodeLittle"), true);
		out.println(buffer.toString());
		out.close();
		//
		return mapping.findForward("forward");
	}
}
