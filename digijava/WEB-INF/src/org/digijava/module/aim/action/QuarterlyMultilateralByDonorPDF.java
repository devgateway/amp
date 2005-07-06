package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.MulitlateralbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.fiscalYrs;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.MultilateralDonorDatasource;
import org.digijava.module.aim.helper.QuartMultiPdfJrxml;


public class QuarterlyMultilateralByDonorPDF extends Action 
{
	private static Logger logger = Logger.getLogger(QuarterlyMultilateralByDonorPDF.class) ;
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form, javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception
	{
		
		//HttpSession session = request.getSession(); 
		//MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) session.getAttribute("ampReports");
		MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm)form;
		Collection coll = new ArrayList();
		if (formBean != null) {
			System.out.println("formBean is not null");
			coll= formBean.getMultiReport();
		} else {
			System.out.println("formbean is null");
		}
		if(coll == null)
			System.out.println("coll is null");
		else
			System.out.println("formBean is not null");
		Iterator iter = null;
		if (coll.size() == 0) {
			System.out.println("collection is empty");
		} else {
			System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		
		System.out.println("col size "+ coll.size());

		multiReport report;
		Project project = null;
		FundTotal fundTotal = null;
		TermFund termFund = null;
		TermFundTotal termFundTotal = null;
		AmpFund fund = null;
		AmpTeamDonors ampTeamDonors;
		
		Collection projects,funds, fundTotals, ampFisYears, teamDonors, totalDonorTermAssistFunds, termFundTotals;
		Collection totalTeamTermAssistFunds, totalTeamFunds;
		Iterator projectIter, fundIter, fundTotalIter, teamDonorsIter, totalDonorTermAssistFundIter;
		Iterator termFundTotalIter, totalTeamTermAssistFundIter, totalTeamFundIter;
		int row=0, col=0, yy=0, yyTmp=0, flag =0, rowCnt=0, colCnt=0, yyCount=0;
		String teamName = "";
		yyCount = formBean.getFiscalYearRange().size();
		if(coll.size() > 0)
		{
			while(iter.hasNext())
			{
				report = (multiReport)iter.next();
				teamDonors = new ArrayList(report.getDonors());
				teamDonorsIter =  teamDonors.iterator();
				while(teamDonorsIter.hasNext())
				{
					ampTeamDonors = (AmpTeamDonors) teamDonorsIter.next();
					rowCnt = rowCnt + 1;				
					if(ampTeamDonors.getTotalDonorTermAssistFund() != null)
					{
						totalDonorTermAssistFunds = new ArrayList(ampTeamDonors.getTotalDonorTermAssistFund());
						totalDonorTermAssistFundIter = totalDonorTermAssistFunds.iterator();
						while(totalDonorTermAssistFundIter.hasNext())
						{
							col = 3;
							termFund = (TermFund) totalDonorTermAssistFundIter.next();
							rowCnt = rowCnt + 1;
						}
					} // Check if Total Donor Term Assist Fund is NULL
				} // End of DOnor Iteration
				if(report.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(report.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						termFund = (TermFund) totalTeamTermAssistFundIter.next();
						rowCnt = rowCnt + 1;
					}
				}// Check if Total Team Term Assist Fund is NULL
			} // End of Objects Collection
		}

		rowCnt = rowCnt + 1;
		colCnt = 5 + formBean.getFiscalYearRange().size() + (formBean.getFiscalYearRange().size() * 12) + 4;
		Object[][] data = new Object[rowCnt][colCnt];
		Integer year = null;
		col = row = 0;
		if(coll.size() > 0)
		{
			iter = coll.iterator();	
			data[row][col] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
			for(int i=0; i<formBean.getFilter().length; i++)
			{	
				col = col + 1;
				data[row][col] = formBean.getFilter()[i].toString();
			}

			while(iter.hasNext())
			{
				report = (multiReport)iter.next();

				teamDonors = new ArrayList(report.getDonors());
				teamDonorsIter =  teamDonors.iterator();
				while(teamDonorsIter.hasNext())
				{
					col+=1;
					data[row][col] = report.getTeamName();
					teamName = report.getTeamName();
					ampTeamDonors = (AmpTeamDonors) teamDonorsIter.next();
					col+=1;
					data[row][col] = ampTeamDonors.getDonorAgency();

					// Stores the years consecutivily
					ampFisYears = new ArrayList((formBean.getFiscalYearRange()));
					Iterator fiscIter = ampFisYears.iterator();
					fiscalYrs fisc=null;
					if(fiscIter.hasNext() == true)
					{
						year = (Integer)fiscIter.next();
						yy = year.intValue();
					}
					yyTmp = yy;
					for(int i=0; i< formBean.getFiscalYearRange().size(); i++)
					{
						col+=1;
						data[row][col] = Integer.toString(yy);
						yy = yy + 1;
					}
					fundTotals = ampTeamDonors.getTotalDonorFund();
					fundTotalIter = fundTotals.iterator();
					while(fundTotalIter.hasNext())
					{
						fundTotal = (FundTotal) fundTotalIter.next();
						col+=1;
						data[row][col] = fundTotal.getTotPlannedDisbAmount();
						col+=1;
						data[row][col] = fundTotal.getTotDisbAmount();
						col+=1;
						data[row][col] = fundTotal.getTotExpAmount();
					}
					col+=1;
					data[row][col] = ampTeamDonors.getDonorPlannedDisbAmount();
					col+=1;
					data[row][col] = ampTeamDonors.getDonorDisbAmount();
					col+=1;
					data[row][col] = ampTeamDonors.getDonorExpAmount();
					
					
					if(ampTeamDonors.getTotalDonorTermAssistFund() != null)
					{
						totalDonorTermAssistFunds = new ArrayList(ampTeamDonors.getTotalDonorTermAssistFund());
						totalDonorTermAssistFundIter = totalDonorTermAssistFunds.iterator();
						while(totalDonorTermAssistFundIter.hasNext())
						{
							termFund = (TermFund) totalDonorTermAssistFundIter.next();
							row = row + 1;
							data[row][3] = report.getTeamName();	
							data[row][4] = "Total " + termFund.getTermAssistName();
							col = 4 + formBean.getFiscalYearRange().size();
							termFundTotals = new ArrayList(termFund.getTermFundTotal());
							termFundTotalIter = termFundTotals.iterator();
							while(termFundTotalIter.hasNext())
							{
								termFundTotal = (TermFundTotal)termFundTotalIter.next();
								col+=1;
								data[row][col]  = termFundTotal.getTotPlannedDisbAmount();
								col+=1;
								data[row][col]  = termFundTotal.getTotDisbAmount();
								col+=1;
								data[row][col]  = termFundTotal.getTotExpAmount();
							}
							col+=1;
							data[row][col]  = termFund.getTotDonorPlannedDisbAmount();
							col+=1;
							data[row][col]  = termFund.getTotDonorDisbAmount();
							col+=1;
							data[row][col]  = termFund.getTotDonorExpAmount();
						}
					} // Check if Total Donor Term Assist Fund is NULL
					row = row  + 1;
					col = 2;
				} // End of DOnor Iteration
				
				row = row - 1;
				if(report.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(report.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						row = row + 1;
						data[row][3] = teamName; 
						termFund = (TermFund) totalTeamTermAssistFundIter.next();
						data[row][4] = "Total " + termFund.getTermAssistName();
						col = 4 + formBean.getFiscalYearRange().size();
						System.out.println(  row + "  : Total Term Assit :  " + termFund.getTermAssistName());
						termFundTotals = new ArrayList(termFund.getTermFundTotal());
						termFundTotalIter = termFundTotals.iterator();
						while(termFundTotalIter.hasNext())
						{
							termFundTotal = (TermFundTotal)termFundTotalIter.next();
							col+=1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
						}
						col+=1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
					}
				}// Check if Total Team Term Assist Fund is NULL
				
				row = row + 1;
				data[row][3] = teamName; 
				data[row][4] = "Total for " + report.getTeamName();
				col = 4 + formBean.getFiscalYearRange().size();
				totalTeamFunds = new ArrayList(report.getTotalTeamFund());
				totalTeamFundIter = totalTeamFunds.iterator();
				while(totalTeamFundIter.hasNext())
				{
					fundTotal = (FundTotal) totalTeamFundIter.next();
					col+=1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotExpAmount();
				}
				col+=1;
				data[row][col] = report.getTeamPlannedDisbAmount();
				col+=1;
				data[row][col] = report.getTeamDisbAmount();
				col+=1;
				data[row][col] = report.getTeamExpAmount();
				flag = 1;
			} // End of Objects Collection
			col = 0;
		}
		
		if(flag == 1)
		{
			
			for(int i=0; i < rowCnt; i++)
			{
				for(int j=0; j < colCnt; j++)
				{
					if(data[i][j] != null)
					{
						if(data[i][j].equals("0")){
							data[i][j] = "";
						}
						data[i][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
						data[i][1] = formBean.getFilter()[0].toString();
						data[i][2] = formBean.getFilter()[1].toString();						
					}
						
				}
			}

			MultilateralDonorDatasource dataSource = new MultilateralDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyMultilaterByDonorPdf.jrxml");
			QuartMultiPdfJrxml jrxml = new QuartMultiPdfJrxml();
			jrxml.createJrxml( realPathJrxml, yyCount);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyMultilaterByDonorPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				System.out.println("Exception from quarterlyMultilaterByDonorPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				System.out.println("Generating PDF");
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=quarterlyMultilaterByDonorPdf.pdf");
				response.setContentLength(bytes.length);
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			else
			{
				System.out.println("Nothing to display");
			}
		}
		return null;
	}// end of Execute Func
}// end of Class
	