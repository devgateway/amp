package org.digijava.module.mondrian.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import mondrian.olap.DriverManager;
import mondrian.rolap.CacheControlImpl;
import mondrian.spi.impl.CatalogLocatorImpl;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.digijava.module.mondrian.form.ShowReportForm;
import org.digijava.module.mondrian.query.QueryThread;
import org.digijava.module.mondrian.query.SchemaManager;

import com.tonbeller.jpivot.mondrian.MondrianMdxQuery;
import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.jpivot.tags.MondrianModelFactory;
import com.tonbeller.jpivot.tags.OlapModelProxy;

public class ShowReport extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		ShowReportForm tf = (ShowReportForm) form;
		HttpSession session = request.getSession();
		
		
		//Set currencies
		tf.setCurrencies(CurrencyUtil.getAmpCurrency());
		if (request.getParameter("id")!=null && !request.getParameter("id").equalsIgnoreCase("null")){
			tf.setReportid(request.getParameter("id"));
		}
		if (request.getParameter("pagename")!=null &&!request.getParameter("pagename").equalsIgnoreCase("null")){
			tf.setJspname(request.getParameter("pagename"));
		}
		
		String baseCurrCode= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
		if ( baseCurrCode == null && request.getParameter("currency")==null) {
			baseCurrCode= "USD";
			tf.setCurrency(baseCurrCode);
			QueryThread.setCurrency(baseCurrCode);
		}else if(request.getParameter("currency")!=null){
			baseCurrCode = request.getParameter("currency");
			QueryThread.setCurrency(baseCurrCode);	
			tf.setCurrency(baseCurrCode);
		}else{
			tf.setCurrency(baseCurrCode);
			QueryThread.setCurrency(baseCurrCode);
		}
		
		
		
		if (session.getAttribute("DuplicateName")!=null && (Boolean) session.getAttribute("DuplicateName")==true){
			tf.addError("aim:reportwizard:duplicateName", "There is already a report with the same name. Please choose a different one.");
			session.removeAttribute("DuplicateName");
		}
		String id = null;
		
		
		if (request.getParameter("id")!= null && !request.getParameter("id").equalsIgnoreCase("null")){
			 id = request.getParameter("id");
		}else if(tf.getReportid()!=null){
			id = tf.getReportid();
			request.setAttribute("id", id);
			request.setAttribute("pagename", tf.getJspname());
		}
		
		if (id!=null){
			OffLineReports report = EntityHelper.LoadReport(Long.parseLong(id));
			session.setAttribute("querystring", report.getQuery());
		}
		ArrayList<QuartzJobForm> jobs = QuartzJobUtils.getAllJobs();
		for (Iterator<QuartzJobForm> iterator = jobs.iterator(); iterator.hasNext();) {
			QuartzJobForm job = iterator.next();
			if (job.getClassFullname().equalsIgnoreCase("org.digijava.module.mondrian.job.RefreshMondrianCacheJob")){
				tf.setLastdate(job.getPrevFireDateTime());
			}
		}
	
			return mapping.findForward("forward");
	
	}
}
