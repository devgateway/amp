package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.io.*;
import java.sql.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import org.digijava.module.aim.helper.WebappScriptlet;
import org.digijava.module.aim.helper.WebappDataSource;	
import org.digijava.module.aim.form.AdvancedReportForm;	
import org.digijava.module.aim.helper.Report;
import javax.servlet.*;

public class ViewProjectsChart extends Action 
{
//	private static Logger logger = Logger.getLogger(MultilateralDonorPDF.class) ;

	private static int fieldHeight = 0; 	 		  
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
			throws java.lang.Exception 
	{

		AdvancedReportForm formbean = (AdvancedReportForm) form;
		//logger.info("CHART FORM SIZE::::::::::::::::"+formbean.getFinalData().size());
		
		Iterator iter= formbean.getFinalData().iterator();
		
		Collection chart_coll=new ArrayList();
	
		chart_coll.add("60");
		chart_coll.add("Donor 1");
		
		while(iter.hasNext()){
			Report r= (Report) iter.next();
			chart_coll.add(r.getAcCommitment().replaceAll("," , ""));
			//logger.info("filling COMM into the Chart Scriptlet."+r.getAcCommitment());
			chart_coll.add(r.getDonor());
			//logger.info("filling DONOR NAME into the Chart Scriptlet."+r.getDonor());
		} 

/*
		chart_coll.add("60");
		chart_coll.add("Donor 1");
		chart_coll.add("50");
		chart_coll.add("Donor 2");
		chart_coll.add("90");
		chart_coll.add("Donor 3");
*/

		
	ActionServlet s = getServlet();
	////logger.info("###########################Inside VIEW Projects JfreeChart Action...SIZE:"+chart_coll.size());
			
			WebappScriptlet ws= new WebappScriptlet();
			ws.setV(chart_coll);	
	
//chart compile jsp

	System.setProperty(
		"jasper.reports.compile.class.path", 
			s.getServletContext().getRealPath("/WEB-INF/lib/jasperreports-0.6.8.jar") +
		System.getProperty("path.separator") + 
			s.getServletContext().getRealPath("/WEB-INF/classes/")
		);

//	//logger.info("###########################Inside JfreeChart Compile...4444444444444");
	System.setProperty(
		"jasper.reports.compile.temp", 
			s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/")
		);
//	//logger.info("###########################Inside JfreeChart Compile...55555555555555");

String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/WebappReport.jrxml");

//JasperCompileManager.compileReportToFile(s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/WebappReport.jrxml"));
JasperCompileManager.compileReportToFile(realPathJrxml);

//chart pdf jsp

//	//logger.info("###########################Inside JfreeChart Compile...22222222");

	File reportFile = new File(s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/WebappReport.jasper"));

	Map parameters = new HashMap();
	parameters.put("ReportTitle", "JFREE ChArt Report - MULTILATERAL");
	parameters.put("BaseDir", reportFile.getParentFile());
				
	//logger.info("Inside Jfree PDF Compile....2");

	byte[] bytes = 
		JasperRunManager.runReportToPdf(
			reportFile.getPath(), 
			parameters, 
			new WebappDataSource()
			);
	//logger.info("Inside Jfree PDF EXPORT...3");
	
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();

	//logger.info("Inside Jfree PDF EXPORT...FINISHED..");

		
		return null;
	}// end of Execute Func
}// end of Class
	
