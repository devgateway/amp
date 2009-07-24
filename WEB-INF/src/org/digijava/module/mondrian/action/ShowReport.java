package org.digijava.module.mondrian.action;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import mondrian.olap.MondrianProperties;

import org.apache.ecs.xhtml.object;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.digijava.module.mondrian.form.ShowReportForm;
import org.eigenbase.util.property.BooleanProperty;
import org.exolab.castor.xml.Marshaller;

import com.tonbeller.jpivot.mondrian.MondrianMdxQuery;
import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.jpivot.tags.MondrianModelFactory;
import com.tonbeller.jpivot.tags.OlapModelProxy;
import com.tonbeller.wcf.bookmarks.BookmarkManager;
import com.tonbeller.wcf.bookmarks.Bookmarkable;

public class ShowReport extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		ShowReportForm tf = (ShowReportForm) form;
		HttpSession session = request.getSession();
		String id = request.getParameter("id");
		if (id!=null){
			OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			session.setAttribute("querystring", report.getQuery());
		}
		ArrayList<QuartzJobForm> jobs = QuartzJobUtils.getAllJobs();
		for (Iterator iterator = jobs.iterator(); iterator.hasNext();) {
			QuartzJobForm job = (QuartzJobForm) iterator.next();
			if (job.getClassFullname().equalsIgnoreCase("org.digijava.module.mondrian.job.RefreshMondrianCacheJob")){
				tf.setLastdate(job.getPrevFireDateTime());
			}
		}
		return mapping.findForward("forward");
	}
}
