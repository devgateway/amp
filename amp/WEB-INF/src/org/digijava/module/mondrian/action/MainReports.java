package org.digijava.module.mondrian.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import mondrian.mdx.MdxVisitorImpl;
import mondrian.web.servlet.MDXQueryServlet;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.digijava.module.mondrian.form.MainReportsForm;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.query.Query;

import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.jpivot.olap.mdxparse.parser;
import com.tonbeller.jpivot.olap.model.OlapModelDecorator;
import com.tonbeller.jpivot.olap.navi.MdxQuery;
import com.tonbeller.jpivot.olap.query.MdxOlapModel;
import com.tonbeller.jpivot.olap.query.QueryAdapter;
import com.tonbeller.jpivot.tags.OlapModelProxy;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class MainReports extends Action {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MainReports.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		MainReportsForm mrform = (MainReportsForm) form;
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (tm != null && action == null) {
			mrform.setCurrentMemberId(tm.getMemberId());
			mrform.setReports(getOffLineReports(TeamUtil.getAmpTeamMember(tm.getMemberId())));
		
		} else if (tm == null) {
			mrform.setReports(getOffLineReports());
		
		} else if (action !=null && action.equalsIgnoreCase("public") && id != null) {
			OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			report.setPublicreport(true);
			EntityHelper.UpdateReport(report);
			mrform.setReports(getOffLineReports(TeamUtil.getAmpTeamMember(tm.getMemberId())));

		} else if (action !=null && action.equalsIgnoreCase("nopublic") && id != null) {
			OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			report.setPublicreport(false);
			EntityHelper.UpdateReport(report);
			mrform.setReports(getOffLineReports(TeamUtil.getAmpTeamMember(tm.getMemberId())));
		}
		
		 else if (action !=null && action.equalsIgnoreCase("delete") && id != null) {
			 OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			 EntityHelper.DeleteReport(report);
			 mrform.setReports(getOffLineReports(TeamUtil.getAmpTeamMember(tm.getMemberId())));
		 }
		return mapping.findForward("forward");
	}

	private Collection<OffLineReports> getOffLineReports() {
		ArrayList<OffLineReports> reports = new ArrayList<OffLineReports>();
		reports = (ArrayList<OffLineReports>) EntityHelper.getPublicReports();
		return reports;
	}

	private Collection<OffLineReports> getOffLineReports(AmpTeamMember tm) {
		ArrayList<OffLineReports> reports = new ArrayList<OffLineReports>();
		reports = (ArrayList<OffLineReports>) EntityHelper.getReports(tm);
		return reports;
	}

	private Collection<String> getColumns(OffLineReports report) {
		return null;
	}
}
