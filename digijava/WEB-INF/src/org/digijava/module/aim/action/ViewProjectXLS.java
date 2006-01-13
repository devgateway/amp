package org.digijava.module.aim.action;	


import java.util.*;

import javax.servlet.*;
import org.apache.log4j.Logger ;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ViewProjectsXlsJrxml;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.ViewProjectDatasource;


public class ViewProjectXLS extends Action 
{
	private static Logger logger = Logger.getLogger(ViewProjectXLS.class) ;
		  
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
		System.out.println("col size "+ coll.size());
		
		int col=0, j=0, flag=0, rowCnt=0, row=0, year=0, colCnt=0, tempYear=0;
		Report report;
		Project project;
		ArrayList ampFunds ,ampDonors ,ampSectors, ampRegions, ampAssist, projects, donors, years;
		AmpFund ampFund = null;
		Iterator fundIter, sectorIter, regionIter, donorIter, projIter, yyIter;
		
		if(coll.size() > 0)
		{	
			while(iter.hasNext()) 
			{
				report =(Report)iter.next();
				logger.info(" Donors : " + report.getDonor());
				rowCnt = rowCnt + 1;
			}
		}
		rowCnt = rowCnt + 1;
		colCnt = 15 + (formBean.getForecastYear().size() * 2) ; 
		logger.info( rowCnt + " Row : Col " +colCnt);
		Object[][] data = new Object[rowCnt][colCnt];
		col = row = 0;
		if(coll.size() > 0)
		{	
			iter = coll.iterator();
			while (iter.hasNext()) 
			{
				report =(Report)iter.next();
				data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
				
				for(int i=0; i< formBean.getFilter().length; i++)
				{	
					col = col + 1;
					logger.info("Filter ");
					data[row][col] = formBean.getFilter()[i].toString();
				}
				col+=1;
				data[row][col] = report.getDonors().toString().replace('[',' ').replace(']',' ');
				col+=1;
				data[row][col] = report.getTitle();
				col+=1;
				data[row][col] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				col+=1;
				data[row][col] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				col+=1;
				data[row][col] = report.getStartDate();
				col+=1;
				data[row][col] = report.getCloseDate();
				col+=1;
				data[row][col] = report.getLevel();
				col+=1;
				data[row][col] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				col+=1;
				data[row][col] = report.getStatus();
				col+=1;
				data[row][col] = report.getAcCommitment();
				col+=1;
				data[row][col] = report.getAcDisbursement();
				col+=1;
				data[row][col] = report.getAcUnDisbursement();
				logger.info(" Col  : " + col );
				col = 14;
				years = new ArrayList(formBean.getForecastYear());
				yyIter = years.iterator();
				if(yyIter.hasNext())
				{
					Integer yy = (Integer)yyIter.next();
					year = yy.intValue();
				}
				tempYear = year;
				ampFunds = new ArrayList(report.getAmpFund());
				fundIter = ampFunds.iterator();
				while(fundIter.hasNext())
				{
					col = col + 1;
					data[row][col] = Integer.toString(year);
					ampFund = (AmpFund)fundIter.next();
					col = col + 1;						
					data[row][col] = ampFund.getDisbAmount();
					year+=1;
				}
				row = row + 1;
				col = 0;
			} // End of REport Collection
			data[row][3] = "Total ";
			for(int i=4; i<12; i++)
			{
				data[row][1]= " ";
			}
			col = 12; 
			flag = 1;
			data[row][col] = formBean.getTotComm();
			col+=1;
			data[row][col] = formBean.getTotDisb();
			col+=1;
			data[row][col] = formBean.getTotUnDisb();
			ArrayList totalDisbFund = new ArrayList(formBean.getTotDisbFund());
			Iterator disbFundIter = totalDisbFund.iterator();
			while(disbFundIter.hasNext())
			{
				col+=1;
				data[row][col] = Integer.toString(tempYear);
				ampFund = (AmpFund) disbFundIter.next();
				col+=1;
				data[row][col] =  ampFund.getDisbAmount();
				tempYear = tempYear + 1;
			}
			flag = 1;
		} // End of Check Collection
		
		if(flag == 1)
		{

			for(int i=0; i < rowCnt; i++)
			{
				for(j=0; j < colCnt; j++)
				{
					if(data[i][j] != null)
					{
						if(data[i][j].equals("0"))
							data[i][j] = "";
					}
						
				}
			}

			String fileName="ViewProjectsXls.Jrxml";
			ViewProjectDatasource dataSource = new ViewProjectDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			String realPathJrxml = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/viewProjectXls.jrxml");
			ViewProjectsXlsJrxml jrxml = new ViewProjectsXlsJrxml();
		 	logger.info( " RowCnt =  " + rowCnt);
		 	jrxml.createJrxml(realPathJrxml, rowCnt);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
									logger.info("EXPORTING xls for Forecasting Report");

				byte[] bytes = null;
				String jasperFile = s.getServletContext().getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/viewProjectXls.jasper");
				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, parameters, dataSource);
				String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/viewProjectXls.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","inline; filename=viewProjectXls.xls");
				try 
				{
					JRXlsExporter exporter = new JRXlsExporter();
					outputStream = response.getOutputStream();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.exportReport();
				}	
				catch (Exception e) 
				{
					if (outputStream != null) 
					outputStream.close();
				}
			}
				else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
				{	
					logger.info("EXPORTING CSV for Forecasting Report");
					ServletOutputStream outputStream = null;
					try
					{
				
						Map parameters = new HashMap();
						byte[] bytes = null;
						String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/viewProjectXls.jasper");
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
						String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/viewProjectXls.csv");
					
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
