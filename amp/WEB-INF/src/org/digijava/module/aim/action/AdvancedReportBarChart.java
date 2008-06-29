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

import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.JFreeChartScriptlet;
import org.digijava.module.aim.helper.WebappDataSource;	
import org.digijava.module.aim.form.AdvancedReportForm;	
import org.digijava.module.aim.helper.Report;
import javax.servlet.*;

public class AdvancedReportBarChart	extends Action 
{
//	private static Logger logger = Logger.getLogger(Login.class) ;

	private static int fieldHeight = 0; 	 		  
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
			throws java.lang.Exception 
	{

		AdvancedReportForm formbean = (AdvancedReportForm) form;
		////System.out.println("CHART FORM SIZE::::::::::::::::"+formbean.getFinalData().size());
		
		Iterator iter= formbean.getFinalData().iterator();
		
		Collection chart_coll=new ArrayList();
	
//		chart_coll.add("60");
//		chart_coll.add("Donor 1");
		String title = "", commit = "", comm="", disb="", exp="";
		iter = formbean.getReport().iterator();
		Collection colls = null;
		Iterator it, fundItr=null;

		while(iter.hasNext())
		{
			Report report = (Report) iter.next();
			colls = report.getRecords();
			
			it = colls.iterator();

			while(it.hasNext())
			{
				org.digijava.module.aim.helper.AdvancedReport advReport = (org.digijava.module.aim.helper.AdvancedReport)it.next();
				if(advReport.getTitle() != null)
					title = advReport.getTitle();
				
				if(advReport.getAmpFund() != null){
						////System.out.println("ampFund is NOT NULL....");
					fundItr = advReport.getAmpFund().iterator();

					AmpFund ampFund1 = new AmpFund();
					while(fundItr.hasNext()){
						ampFund1 = (AmpFund) fundItr.next();
						comm = ampFund1.getCommAmount();
						disb = ampFund1.getDisbAmount();
						exp = ampFund1.getExpAmount();
					}
				}

				//if(advReport.getActualCommitment() != null)
					//commit = advReport.getActualCommitment();
					//chart_coll.add(advReport.getActualCommitment().replaceAll("," , ""));
				//chart_coll.add(advReport.getTitle());
			}//end of while
			////System.out.println("ZZZZZZZZZZz"+title+"<------***********------->"  + comm +"<------***********------->" +disb + "<------***********------->"  + exp );
			//chart_coll.add(new Double(commit.replaceAll(",", "")) );

			chart_coll.add(new Double(comm.replaceAll(",", "")) );
			chart_coll.add(title);
			chart_coll.add(new Double(disb.replaceAll(",", "")) );
			chart_coll.add(title);
			chart_coll.add(new Double(exp.replaceAll(",", "")) );
			chart_coll.add(title);

		}
		
		////System.out.println("  Chart Size : " +chart_coll.size());

/*
		chart_coll.add("60");
		chart_coll.add("Donor 1");
		chart_coll.add("50");
		chart_coll.add("Donor 2");
		chart_coll.add("90");
		chart_coll.add("Donor 3");
*/

		
	ActionServlet s = getServlet();
	////////System.out.println("###########################Inside VIEW Projects JfreeChart Action...SIZE:"+chart_coll.size());
			
			JFreeChartScriptlet ws= new JFreeChartScriptlet();
			ws.setV(chart_coll);	
	
//chart compile jsp

	System.setProperty(
		"jasper.reports.compile.class.path", 
			s.getServletContext().getRealPath("/WEB-INF/lib/jasperreports-0.6.8.jar") +
		System.getProperty("path.separator") + 
			s.getServletContext().getRealPath("/WEB-INF/classes/")
		);

	System.setProperty(
		"jasper.reports.compile.temp", 
			s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/")
		);

	String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/BarChartReport.jrxml");

//JasperCompileManager.compileReportToFile(s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/WebappReport.jrxml"));
	JasperCompileManager.compileReportToFile(realPathJrxml);

//chart pdf jsp

	File reportFile = new File(s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/BarChartReport.jasper"));

	Map parameters = new HashMap();
	parameters.put("ReportTitle", "JFREE ChArt Report - MULTILATERAL");
	parameters.put("BaseDir", reportFile.getParentFile());
				
	//////System.out.println("Inside Jfree PDF Compile....2");

	byte[] bytes = 
		JasperRunManager.runReportToPdf(
			reportFile.getPath(), 
			parameters, 
			new WebappDataSource()
			);
	//////System.out.println("Inside Jfree PDF EXPORT...3");

			if (bytes != null && bytes.length > 0)
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				////System.out.println("Generating Bar Chart PDF");
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=AdvancedReportBarChart.pdf");
				response.setContentLength(bytes.length);
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			else
			{
				////System.out.println("Nothing to display");
			}

	//////System.out.println("Inside Jfree PDF EXPORT...FINISHED..");

		return null;
	}// end of Execute Func
}// end of Class
	
