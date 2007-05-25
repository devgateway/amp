package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;

import java.util.*;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.PlannedProjectXlsJrxml;
import org.digijava.module.aim.helper.ViewProjectDatasource;
import org.apache.log4j.Logger ;
import javax.servlet.*;

public class PlannedProjectXLS extends Action 
{
	private static Logger logger = Logger.getLogger(PlannedProjectXLS.class) ;
		  
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
						throws java.lang.Exception 
	{

		CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;
		Collection coll = new ArrayList();
		if (formBean != null) {
			logger.info("formBean is not null");
			coll = formBean.getAllReports();
		} else {
			logger.info("formbean is null");
		}

		Iterator iter = null;
		if (coll.size() == 0) {
			logger.info("collection is empty");
		} else {
			logger.info("collection is not empty");
			iter = coll.iterator();
		}
		logger.info("col size =  "+ coll.size());
		
		int col=0, flag=0, colCnt=0, row=0, year=0, rowCnt=0, tempYear, yearCount;
		Report report;
		Integer yy;
		AmpFund ampFund = null;
		ArrayList years, ampFunds, totalFund;
		Iterator fundIter, sectorIter, regionIter, donorIter,yyIter, totalFundIter;
		
		yearCount = formBean.getFiscalYearRange().size();
		rowCnt = coll.size() + 1;
		colCnt = 15 + formBean.getFiscalYearRange().size() + (formBean.getFiscalYearRange().size() * 12) + 3;
		logger.info( coll.size() + "  : ROw : Col : " +col);
		Object[][] data = new Object[rowCnt][colCnt];
		//Object[][] data = new Object[coll.size() + 1][colCnt];
		if(coll.size() > 0)
		{	
			iter = coll.iterator();
			while (iter.hasNext()) 
			{
				col=0;
				report =(Report)iter.next();
				data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
				for(int i=0; i< formBean.getFilter().length; i++)
				{	
					col+=1;
					data[row][col] = formBean.getFilter()[i].toString();
				}
				col+=1;
				data[row][col] = report.getDonors().toString().replace('[',' ').replace(']',' ');
				col+=1;
				data[row][col] = report.getTitle();
				col+=1;
				data[row][col] = report.getStatus();
				col+=1;				
				data[row][col] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getModality().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getLevel();
				col+=1;				
				data[row][col] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getStartDate();
				col+=1;				
				data[row][col] = report.getCloseDate();
				col+=1;				
				data[row][col] = report.getCommitmentDate().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getAcCommitment();
				
				ampFunds = new ArrayList(report.getAmpFund());
				fundIter = ampFunds.iterator();
				col = 14;
				years = new ArrayList(formBean.getFiscalYearRange());
				yyIter = years.iterator();
				while(yyIter.hasNext())
				{
					yy = (Integer)yyIter.next();
					col+=1;
					data[row][col] = yy.toString();
				}

				while(fundIter.hasNext())
				{
					ampFund = (AmpFund)fundIter.next();
					col = col + 1;
					data[row][col] = ampFund.getPlannedDisbAmount();
					col = col + 1;
					data[row][col] = ampFund.getDisbAmount();
					col = col + 1;
					data[row][col] = ampFund.getExpAmount();
				}
				row = row + 1;
				flag = 1;
			} // end of Iteration
			
			data[row][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() + " Team";
			totalFund = new ArrayList(formBean.getTotFund());
			totalFundIter = totalFund.iterator();
			col = 3;
			data[row][col] = " Total ";
			data[row][14] = formBean.getTotComm();
			col = 14 + formBean.getFiscalYearRange().size();
			
			while(totalFundIter.hasNext())
			{
				ampFund = (AmpFund) totalFundIter.next();
				col+=1;
				data[row][col]  = ampFund.getPlannedDisbAmount();
				col+=1;
				data[row][col]  = ampFund.getDisbAmount();
				col+=1;
				data[row][col]  = ampFund.getExpAmount();
			}
			flag = 1;
		}

		
		if(flag == 1)
		{
			
			for(int i=0; i < rowCnt; i++)
			{
				for(int j=0; j < colCnt; j++)
				{
					if(data[i][j] != null)
					{
						if(data[i][j].equals("0"))
							data[i][j] = "";
					}
						
				}
			}
			String fileName="PlannedProjectXls.jrxml";

			ViewProjectDatasource dataSource = new ViewProjectDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
							  "/WEB-INF/lib/jasperreports-0.6.1.jar");
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/PlannedProjectXls.jrxml");
		 	PlannedProjectXlsJrxml jrxml = new PlannedProjectXlsJrxml();
		 	jrxml.createJrxml(realPathJrxml, yearCount);
			JasperCompileManager.compileReportToFile(realPathJrxml);

			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
				logger.info("EXPORTING XLS PLANNED PROJECTS Project");
			byte[] bytes = null;
			String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/PlannedProjectXls.jasper");
			Map parameters = new HashMap();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,	dataSource);
			String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/PlannedProjectXls.xls");
			ServletOutputStream outputStream = null;
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","inline; filename=PlannedProjectXls.xls");
			
			try
			{
				outputStream = response.getOutputStream();
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.exportReport();
				outputStream.flush();
			}
			catch (Exception e) 
			{
				logger.info("Exception : " + e);
				if (outputStream != null) 
					outputStream.close();
			}
		}
		else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
		{
				logger.info("EXPORTING CSV for Quarterly TEam REport Project");
				ServletOutputStream outputStream = null;
				try
				{
				
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/PlannedProjectXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/PlannedProjectXls.csv");
					
					//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
						response.setContentType("application/vnd.ms-excel");
						String responseHeader = "inline; filename="+destFile;
						logger.info("--------------" + responseHeader);
						response.setHeader("Content-Disposition", responseHeader);
						//response.setHeader("Content-Disposition","inline; filename=commitmentByModalityXls.xls");
						logger.info("--------------");
					//JRXlsExporter exporter = new JRXlsExporter();
					JRCsvExporter exporter = new JRCsvExporter();
					outputStream = response.getOutputStream();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
					//exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.exportReport();
				}
				catch (Exception e) 
				{
					if (outputStream != null) 
					{
						outputStream.close();
					}	
				}
		}
		}
		return null;
	}
}
