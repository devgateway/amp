package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;

import org.digijava.module.aim.form.MulitlateralbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.QuarterlyReportBySectorPdfJrxml;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.fiscalYrs;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.SectorByProjectDatasource;


public class QuarterlyReportBySectorPDF extends Action 
{
	private static Logger logger = Logger.getLogger(QuarterlyReportBySectorPDF.class) ;
	private static int fieldHeight = 0; 	 	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form, javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception
	{
		MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) form;
		Collection coll = new ArrayList();
		if (formBean != null) {
			logger.info("formBean is not null");
			coll= formBean.getMultiReport();
		} else {
			logger.info("formbean is null");
		}
		if(coll == null)
			logger.info("coll is null");
		else
			logger.info("Collection is not null");
		Iterator iter = null;
		if (coll.size() == 0) {
			logger.info("collection is empty");
		} else {
			logger.info("collection is not empty");
			iter = coll.iterator();
		}
		
		//System.out.println("col size "+ coll.size());

		multiReport multiReport;
		AmpTeamDonors  ampTeamDonor;
		ProjectTermAssist projectTermAssist;
		TermFund termFund;
		TermFundTotal termFundTotal;
		Project project = null;
		FundTotal fundTotal=null;
		AmpFund ampFund =null;
		AmpDonors ampDonor=null;
				
		Collection donors, funds, fundTotals, projects, termAssists, ampFiscalYears, totalTeamTermAssistFunds;
		Collection totalSectorTermAssistFunds, totalSectorFunds, termAssistFunds, termFundTotals, totalTeamFunds;
		Iterator projectIter, donorIter,fundIter,fundTotalIter, termAssistIter, termFundTotalIter, totalTeamFundIter;
		Iterator totalSectorTermAssistFundIter, totalSectorFundIter, termAssistFundIter, totalTeamTermAssistFundIter;
		
		int row=0, col=0, yy=0, yyTmp=0, flag =0, rowCnt=0, colCnt=0, yyCnt=0, yyCount, sectorSize=0, count=0; 
		Integer year = null;
		String sector ="",donor="";
		yyCount = formBean.getFiscalYearRange().size();
		
		if(coll != null)
		{
			while(iter.hasNext())
			{
				multiReport = (multiReport)iter.next();
				sectorSize = sectorSize + 1;
				donors = new ArrayList(multiReport.getDonors());
				donorIter = donors.iterator();
				while(donorIter.hasNext())
				{
					ampTeamDonor = (AmpTeamDonors) donorIter.next();
					projects = ampTeamDonor.getProject();
					projectIter = projects.iterator();
					while(projectIter.hasNext())
					{
						project = (Project) projectIter.next();
						logger.info( rowCnt + ":  Project : " + project.getName());
						rowCnt = rowCnt + 1;
						if(project.getTermAssist() != null)
						{
							termAssists = new ArrayList(project.getTermAssist());
							termAssistIter = termAssists.iterator();
							while(termAssistIter.hasNext())
							{
								projectTermAssist = (ProjectTermAssist) termAssistIter.next();
								logger.info( rowCnt + ":  TermAssit  : " + projectTermAssist.getTermAssistName());
								rowCnt = rowCnt + 1;
							}
						}// Term Assist is NOT NULL
					}// End of Project Iteration
				}// End of Donor Iteration
				if(multiReport.getTotalSectorTermAssistFund()  != null)
				{
					totalSectorTermAssistFunds = multiReport.getTotalSectorTermAssistFund();
					totalSectorTermAssistFundIter = totalSectorTermAssistFunds.iterator();
					while(totalSectorTermAssistFundIter.hasNext())
					{
						termFund = (TermFund) totalSectorTermAssistFundIter.next();
						logger.info( rowCnt + ":  TermAssit  : " +  termFund.getTermAssistName());
						rowCnt = rowCnt + 1;
					}
				}//Total Sector Term Assist Fund Iter
				logger.info(rowCnt + "  : Total for " + multiReport.getSector());
				rowCnt = rowCnt + 1;
				
				if(multiReport.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(multiReport.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						termFund = (TermFund)totalTeamTermAssistFundIter.next();
						logger.info(rowCnt + " :  Total " + termFund.getTermAssistName());
						rowCnt = rowCnt + 1;
					}
				}
				if(multiReport.getTotalTeamFund() != null)
				{
					logger.info(rowCnt + " : Grand Total");
				}
			} // End of Objects Collection
		}

		rowCnt = rowCnt + 1;
		colCnt = 5 + formBean.getFiscalYearRange().size() + (formBean.getFiscalYearRange().size() * 12) + 4;
		logger.info(rowCnt + ":Row   	Col : " + colCnt);
		logger.info(" Sector Size  : " + sectorSize);
		Object[][] data = new Object[rowCnt][colCnt];
		
		if(coll != null)
		{
			col = 0;
			iter = coll.iterator();
			data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
			for(int i=0; i<formBean.getFilter().length; i++)
			{	
				col = col + 1;
				data[row][col] = formBean.getFilter()[i].toString();
			}
			
			while(iter.hasNext())
			{
				multiReport = (multiReport)iter.next();
				donors = new ArrayList(multiReport.getDonors());
				donorIter = donors.iterator();
				sector = multiReport.getSector();
				while(donorIter.hasNext())
				{
					ampTeamDonor = (AmpTeamDonors) donorIter.next();
					projects = new ArrayList(ampTeamDonor.getProject());
					projectIter = projects.iterator();
					
					while(projectIter.hasNext())
					{
						col = 3;
						project = (Project) projectIter.next();
						data[row][col] = multiReport.getSector();
						calculateFieldHeight(multiReport.getSector());
						
						col+=1;
						data[row][col] = ampTeamDonor.getDonorAgency();
						calculateFieldHeight(ampTeamDonor.getDonorAgency());
						
						donor = ampTeamDonor.getDonorAgency();
						col+=1;
						data[row][col] = project.getName();
						calculateFieldHeight(project.getName());
						
						// Stores the years consecutivily
						ampFiscalYears = new ArrayList((formBean.getFiscalYearRange()));
						Iterator fiscIter = ampFiscalYears.iterator();
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

						funds = new ArrayList(project.getAmpFund());
						fundIter = funds.iterator();
						while(fundIter.hasNext())
						{
							ampFund = (AmpFund) fundIter.next();
							col+=1;
							data[row][col] = ampFund.getPlannedDisbAmount();
							col+=1;
							data[row][col] = ampFund.getDisbAmount();
							col+=1;
							data[row][col] = ampFund.getExpAmount();
						}
						col+=1;
						data[row][col] = project.getProjPlannedDisbAmount();
						col+=1;
						data[row][col] = project.getProjDisbAmount();
						col+=1;
						data[row][col] = project.getProjExpAmount();
						
						if(project.getTermAssist() != null)
						{
							termAssists = new ArrayList(project.getTermAssist());
							termAssistIter = termAssists.iterator();
							while(termAssistIter.hasNext())
							{
								row = row + 1;
								data[row][3] = multiReport.getSector();
								data[row][4] = ampTeamDonor.getDonorAgency();
								col = 5;
								projectTermAssist = (ProjectTermAssist) termAssistIter.next();
								data[row][col] = projectTermAssist.getTermAssistName();
								termAssistFunds = projectTermAssist.getTermAssistFund();
								termAssistFundIter = termAssistFunds.iterator();
								col = 5 + formBean.getFiscalYearRange().size();
								while(termAssistFundIter.hasNext())
								{
									ampFund = (AmpFund) termAssistFundIter.next();
									col+=1;
									data[row][col] = ampFund.getPlannedDisbAmount();
									col+=1;
									data[row][col] = ampFund.getDisbAmount();
									col+=1;
									data[row][col] = ampFund.getExpAmount();
								}
								col+=1;
								data[row][col] = projectTermAssist.getTermPlannedDisbAmount();
								col+=1;
								data[row][col] = projectTermAssist.getTermDisbAmount();
								col+=1;
								data[row][col]  = projectTermAssist.getTermExpAmount();
							}
						}// Term Assist is NOT NULL
						row = row + 1;
					}// End of Project Iteration
					row = row - 1;
					row = row + 1;
				}// End of Donor Iteration
				row = row - 1;
				
				if(multiReport.getTotalSectorTermAssistFund()  != null)
				{
					totalSectorTermAssistFunds = multiReport.getTotalSectorTermAssistFund();
					totalSectorTermAssistFundIter = totalSectorTermAssistFunds.iterator();
					while(totalSectorTermAssistFundIter.hasNext())
					{
						
						row = row + 1;
						data[row][3] = multiReport.getSector();
						data[row][4] = donor; 
						termFund = (TermFund) totalSectorTermAssistFundIter.next();
						
						col = 5;
						data[row][col] = "Total " + termFund.getTermAssistName();
						calculateFieldHeight(data[row][col].toString());
						
						col = 5 + formBean.getFiscalYearRange().size();
						termFundTotals = termFund.getTermFundTotal();
						termFundTotalIter = termFundTotals.iterator();
						while(termFundTotalIter.hasNext())
						{
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
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
				}//Total Sector Term Assist Fund Iter 
				
				row = row + 1;
				data[row][3] = multiReport.getSector();
				data[row][4] = donor;
				col = 5;
				data[row][col] = "Total for " + multiReport.getSector();
				calculateFieldHeight(data[row][col].toString());
				
				col = 5 + formBean.getFiscalYearRange().size();
				totalSectorFunds = new ArrayList(multiReport.getTotalSectorFund());
				totalSectorFundIter = totalSectorFunds.iterator();
				while(totalSectorFundIter.hasNext())
				{
					fundTotal  = (FundTotal) totalSectorFundIter.next();
					col+=1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotExpAmount();
				}
				col+=1;
				data[row][col] = multiReport.getSectorPlannedDisbAmount();
				col+=1;
				data[row][col] = multiReport.getSectorDisbAmount();
				col+=1;
				data[row][col] = multiReport.getSectorExpAmount();
				
				//-----------------------------------zzz---------------------------

				
				if(multiReport.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(multiReport.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					
					while(totalTeamTermAssistFundIter.hasNext())
					{
						row = row + 1;
						data[row][3] = multiReport.getSector();
						data[row][4] = donor;
						col = 5;
						termFund = (TermFund)totalTeamTermAssistFundIter.next();
						data[row][col] = "Total " + termFund.getTermAssistName();
						calculateFieldHeight(data[row][col].toString());
						
						col = 5 + formBean.getFiscalYearRange().size();
						termFundTotals = new ArrayList(termFund.getTermFundTotal());
						termFundTotalIter = termFundTotals.iterator();
						while(termFundTotalIter.hasNext())
						{
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col+=1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
							yy = yy + 1;
						}
						col+=1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
					}
				} // Check if  Total Team Term Assist Fund is not NULL
				
				
				count = count + 1;
				if(count == sectorSize)
				{
					if(multiReport.getTotalTeamFund() != null)
					{
						col = 5;
						row = row + 1;
						data[row][3] = multiReport.getSector();
						data[row][4] = donor;
						data[row][col] = "Grand Total ";
						col = 5 + formBean.getFiscalYearRange().size();
						yy = yyTmp;
						totalTeamFunds = new ArrayList(multiReport.getTotalTeamFund());
						totalTeamFundIter  = totalTeamFunds.iterator();
						while(totalTeamFundIter.hasNext())
						{
							fundTotal = (FundTotal)totalTeamFundIter.next();
							col+=1;
							data[row][col] = fundTotal.getTotPlannedDisbAmount();
							col = col+1;
							data[row][col] = fundTotal.getTotDisbAmount();
							col = col+1;
							data[row][col] = fundTotal.getTotExpAmount() ;
							yy = yy + 1;
						}
						col+=1;
						data[row][col] = multiReport.getTeamPlannedDisbAmount();
						col+=1;
						data[row][col] = multiReport.getTeamDisbAmount();
						col+=1;
						data[row][col] = multiReport.getTeamExpAmount();
					}// Check of Total Team Fund is NOT NULL
				}
				else
				{
					logger.info("*******     Count != SECTOR SIX **********");
				}
				flag = 1;
				row = row + 1;
			} // End of Objects Collection
		}
		
		int height = (( fieldHeight / 25 ) * 10 ) + 40;
		logger.info(" Column Height = " + height );

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

			SectorByProjectDatasource dataSource = new SectorByProjectDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyReportBySectorPdf.jrxml");
			QuarterlyReportBySectorPdfJrxml jrxml = new QuarterlyReportBySectorPdfJrxml();
			jrxml.createJrxml(realPathJrxml, yyCount, height);

			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyReportBySectorPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				logger.info("Exception from quarterlyReportBySectorPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				logger.info("Generating PDF");
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=quarterlyReportBySectorPdf.pdf");
				response.setContentLength(bytes.length);
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			else
			{
				logger.info("Nothing to display");
			}
			
		}
	
		return null;
	}// end of Execute Func


	void calculateFieldHeight(String input)
	{
		//System.out.println(" Large ::" + fieldHeight + " :: CUrrent : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}// end of Class
	