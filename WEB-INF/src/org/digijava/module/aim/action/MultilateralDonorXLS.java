package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.MulitlateralbyDonorForm ;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.fiscalYrs ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.MultilateralDonorDatasource;
import org.digijava.module.aim.helper.MultiXlsJrxml;

public class MultilateralDonorXLS extends Action 
{
	private static Logger logger = Logger.getLogger(MultilateralDonorXLS.class) ;
		  
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception 
	{
		MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) form;
		Collection coll = new ArrayList();
		if (formBean != null) {
			////System.out.println("formBean is not null");
			coll= formBean.getMultiReport();
		} 
		
		Iterator iter = null;
		if (coll.size() == 0) {
			////System.out.println("collection is empty");
		} else {
			////System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		////System.out.println("col size "+ coll.size());
		
		multiReport report;
		FundTotal fundTotal;
		AmpTeamDonors teamDonors;
		TermFundTotal termFundTotal;
		TermFund termFund;
		Project project = null;
		ArrayList ampProjects,ampDonors,ampFunds,ampFundTotal, ampDonor,ampFisYears;
		Iterator projectIter, donorIter;
		FundTotal ampFundT=null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2= new StringBuffer();
		int row=0, col=0,yy=0, yyTmp=0, flag =0, rowCnt=0, colCnt=0,yyCount=0;
		boolean term = false;

		if(coll.size() > 0)
		{
			while(iter.hasNext())
			{
				report = (multiReport)iter.next();
				ampDonors = new ArrayList(report.getDonors());
				donorIter = ampDonors.iterator();
				while(donorIter.hasNext())
				{
					teamDonors = (AmpTeamDonors)donorIter.next();
					logger.info(" Donor : " + teamDonors.getDonorAgency());
					rowCnt = rowCnt + 1;
					if(teamDonors.getTotalDonorTermAssistFund() != null)
					{
						ArrayList totalDrTermAssistFunds = new ArrayList(teamDonors.getTotalDonorTermAssistFund());
						Iterator totalDrTermAssistFundIter = totalDrTermAssistFunds.iterator();
						while(totalDrTermAssistFundIter.hasNext())
						{
							termFund = (TermFund)totalDrTermAssistFundIter.next();
							logger.info(" Term Fund : " + termFund.getTermAssistName());
							rowCnt = rowCnt + 1;
						}	
					}

				}// End of Donor Collection
				
				ArrayList totalTeamTermAssistFund = new ArrayList(report.getTotalTeamTermAssistFund());
				Iterator totalTeamTermAssistFd = totalTeamTermAssistFund.iterator();
				if(report.getTotalTeamTermAssistFund() != null)
				{
					while(totalTeamTermAssistFd.hasNext() )
					{
						termFund = (TermFund) totalTeamTermAssistFd.next();
						logger.info( " TotalTeam Term : " + termFund.getTermAssistName());
						rowCnt = rowCnt + 1;
					}
				}
			}
		}
		rowCnt = rowCnt + 1;
		yyCount = formBean.getFiscalYearRange().size();
		colCnt = 4 + yyCount + (yyCount*4) + 6 + 2 ;
		////System.out.println(rowCnt +" :  Row :Cnt : Col : " + colCnt);

		Integer year = null;
		String teamName="";
		Object[][] data = new Object[rowCnt][colCnt];
		if(coll.size() > 0)
		{
			iter = coll.iterator();
			row = 0;	
			data[row][col] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
			String filterName[] = new String[2];
			filterName = formBean.getFilter();
			for(int i=0; i<filterName.length; i++)
			{	
				col = col + 1;
				data[row][col] = filterName[i];
			}
			
			while(iter.hasNext())
			{
				
				report = (multiReport)iter.next();
				ampFisYears = new ArrayList(formBean.getFiscalYearRange());
				Iterator fiscIter = ampFisYears.iterator();
				fiscalYrs fisc=null;
				if(fiscIter.hasNext() == true)
				{
					year = (Integer)fiscIter.next();
					yy = year.intValue();
				}
				yyTmp = yy;
				col = 2;
				teamName = report.getTeamName();
				ampDonors = new ArrayList(report.getDonors());
				donorIter = ampDonors.iterator();
				while(donorIter.hasNext())
				{
					teamDonors = (AmpTeamDonors)donorIter.next();
					col = col + 1;
					data[row][col] = teamName;
					col = col + 1;
					data[row][col] = teamDonors.getDonorAgency();
					logger.info(row + ": Donor :" + data[row][col]);
					ArrayList totalDonorFund = new ArrayList(teamDonors.getTotalDonorFund());
					Iterator totalDrFundIter = totalDonorFund.iterator();
					while(totalDrFundIter.hasNext())
					{
						col+=1;
						data[row][col] = Integer.toString(yy);
						fundTotal = (FundTotal)totalDrFundIter.next();
						col+=1;
						data[row][col] = fundTotal.getTotCommAmount();					
						col+=1;
						data[row][col] = fundTotal.getTotPlannedDisbAmount();					
						col+=1;
						data[row][col] = fundTotal.getTotDisbAmount();
						col+=1;
						data[row][col] = fundTotal.getTotExpAmount();						
						yy = yy + 1;
					}
					yy = yyTmp;
					col+=1;
					data[row][col] = teamDonors.getDonorCommAmount();
					col+=1;
					data[row][col] = teamDonors.getDonorPlannedDisbAmount();
					col+=1;
					data[row][col] = teamDonors.getDonorDisbAmount();
					col+=1;
					data[row][col] = teamDonors.getDonorExpAmount();
					col+=1;
					data[row][col] = teamDonors.getDonorUnDisbAmount();
					
					//col = 2;
					if(teamDonors.getTotalDonorTermAssistFund() != null)
					{
						//row = row + 1;
						yy = yyTmp;
						ArrayList totalDrTermAssistFunds = new ArrayList(teamDonors.getTotalDonorTermAssistFund());
						Iterator totalDrTermAssistFundIter = totalDrTermAssistFunds.iterator();
						while(totalDrTermAssistFundIter.hasNext())
						{
							row = row + 1;
							col = 2;
							termFund = (TermFund)totalDrTermAssistFundIter.next();
							col+=1;
							data[row][col] =  teamName;
							col+=1;
							data[row][col] = termFund.getTermAssistName();
							logger.info(row + ": Term Assist :" + data[row][col]);
							ArrayList termFundTotals= new ArrayList(termFund.getTermFundTotal());
							Iterator termFundTotalter = termFundTotals.iterator();
							while(termFundTotalter.hasNext())
							{
								col+=1;
								data[row][col] = Integer.toString(yy);
								termFundTotal = (TermFundTotal) termFundTotalter.next();
								col+=1;
								data[row][col] = termFundTotal.getTotCommAmount();
								col+=1;
								data[row][col] = termFundTotal.getTotPlannedDisbAmount();
								col+=1;
								data[row][col] = termFundTotal.getTotDisbAmount();
								col+=1;
								data[row][col] = termFundTotal.getTotExpAmount();
								yy = yy + 1;
							}
							yy = yyTmp;
							col+=1;
							data[row][col] = termFund.getTotDonorCommAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorPlannedDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorExpAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorUnDisbAmount();
						}
					} // Check if TotalDonorTermAssistFund is Not NULL
					row = row + 1;
					col = 2;
				}// End of Donor Collection
				
				col = 2;
				yy = yyTmp;
				ArrayList totalTeamTermAssistFund = new ArrayList(report.getTotalTeamTermAssistFund());
				Iterator totalTeamTermAssistFd = totalTeamTermAssistFund.iterator();
				if(report.getTotalTeamTermAssistFund() != null)
				{
					while(totalTeamTermAssistFd.hasNext() )
					{
						col = 3;
						data[row][col] =  teamName;
						termFund = (TermFund) totalTeamTermAssistFd.next();
						col+=1;
						data[row][col] = "Total " + termFund.getTermAssistName();
						logger.info(row + ": Total TErm Assist  :" + data[row][col]);
						ArrayList termFundTotals = new ArrayList(termFund.getTermFundTotal());
						Iterator termFdTotalIter = termFundTotals.iterator();
						while(termFdTotalIter.hasNext())
						{
							col+=1;
							data[row][col] = Integer.toString(yy);
							termFundTotal = (TermFundTotal) termFdTotalIter.next();
							col+=1;
							data[row][col] = termFundTotal.getTotCommAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
							yy = yy + 1;
						}
						col+=1;
						data[row][col] = termFund.getTotDonorCommAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorUnDisbAmount();
						
						term = true;
						row = row + 1;
						logger.info("term =   "+term);
					}
				}
				
				if(term == true){
					row = row - 1;
					term = false;
					logger.info("row in term = true  "+row);
				}
			/*	else
				{

				row = row + 1;
				logger.info("row in else  "+row);
				}*/
				row = row+1;
				col = 2;
				
				yy = yyTmp;
				col=col + 1;
				logger.info("row and col"+row+"   "+col);
				
				data[row][col] = teamName;
				
				logger.info("row and col"+row+"   "+col);
				col=col+1;
				data[row][col] = "Total for " + report.getTeamName();
				logger.info(row + ": Total for :" + data[row][col]);
				Collection totalTeamFunds = new ArrayList(report.getTotalTeamFund());
				Iterator totalTeamFdIter = totalTeamFunds.iterator();
				while(totalTeamFdIter.hasNext())
				{
					col+=1;
					data[row][col] = Integer.toString(yy);
					fundTotal = (FundTotal) totalTeamFdIter.next();
					col+= 1;
					data[row][col] = fundTotal.getTotCommAmount();
					col+= 1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col+= 1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col+= 1;
					data[row][col] = fundTotal.getTotExpAmount();
					yy = yy + 1;
				}
				col+=1;
				data[row][col] =  report.getTeamCommAmount();
				col+=1;
				data[row][col] =  report.getTeamPlannedDisbAmount();
				col+=1;
				data[row][col] =  report.getTeamDisbAmount();
				col+=1;
				data[row][col] =  report.getTeamExpAmount();
				col+=1;
				data[row][col] =  report.getTeamUnDisbAmount();
				col = 2;
			}
			col = 0;
			flag = 1;
		}// end of Big Loop
		else
		{
			flag = 0;	
			////System.out.println("Collection empty");
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

			String fileName="MultilateralDonorXls.jrxml";
			MultilateralDonorDatasource dataSource = new MultilateralDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/MultilateralDonorXls.jrxml");
			MultiXlsJrxml jrxml = new MultiXlsJrxml();
			jrxml.createJrxml( yyCount, realPathJrxml);

			JasperCompileManager.compileReportToFile(realPathJrxml);
			
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls")){
				logger.info("EXPORTING XLS for MUltilateralbyDonor");
				
				byte[] bytes = null;
				String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/MultilateralDonorXls.jasper");
				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
				String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/MultilateralDonorXls.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","inline; filename=MultilateralDonorXls.xls");
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
				catch (Exception e) {
					if (outputStream != null) 
					{
						outputStream.close();
					}
				}
			}
			else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
			{
				logger.info("EXPORTING CSV for MUltilateralbyDonor");
				ServletOutputStream outputStream = null;
				try
				{
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/MultilateralDonorXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/MultilateralDonorXls.csv");
					
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
					e.printStackTrace(System.out);
					if (outputStream != null) 
					{
						outputStream.close();
					}
				}
				
			}
		}
		return null;
	}// end of Execute Func
}// end of Class
