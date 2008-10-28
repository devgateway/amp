package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;

import javax.servlet.ServletOutputStream;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.MulitlateralbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.SbpXlsJrxml;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.helper.fiscalYrs;
import org.digijava.module.aim.helper.FundTotal;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.SectorByProjectDatasource;


public class SectorByProjectXLS
		  extends Action {

		  private static Logger logger = Logger.getLogger(SectorByProjectXLS.class) ;
		  
		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
					 throws java.lang.Exception {

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
				logger.info("formBean is not null");
			
			Iterator iter = null;
			if (coll.size() == 0) {
				logger.info("collection is empty");
			} else {
				logger.info("collection is not empty");
				iter = coll.iterator();
			}
			////System.out.println("col size "+ coll.size());
			multiReport multiReport;
			Project project;
			AmpFund ampFund;
			TermFund termFund;
			FundTotal fundTotal;
			TermFundTotal termFundTotal;
			ProjectTermAssist projectTermAssist;
			ArrayList ampProjects,ampDonors,ampFunds,ampFisYears;
			ArrayList ampFundTotal = null;
			AmpTeamDonors teamDonors;
			Collection projTermAssists, termAssistFunds, totalTeamFunds, totalTeamTermAssistFunds;
			Project ampProject= null;
			Iterator donorIter, termAssistFdIter, totalTeamFundIter, totalTeamTermAssistFundIter;
			int row=0, col=0, yy=0, yyTmp=0, flag =0 ,rowCnt=0, colCnt=0, yyCnt=0, ind=0, sectorSize=0, curr=0, count=0;
			boolean term = false, isProject = false;
			String sector="", donor="'";
			yyCnt = formBean.getFiscalYearRange().size();
			
			if( coll.isEmpty() == false )
			{
				while(iter.hasNext())
				{
					multiReport = (multiReport)iter.next();
					sector = multiReport.getSector();
					sectorSize = sectorSize + 1;
					ampDonors = new ArrayList(multiReport.getDonors());
					
					donorIter = ampDonors.iterator();
					if(ampDonors.size() > 0)
					{
						while(donorIter.hasNext())
						{	
							teamDonors = (AmpTeamDonors)donorIter.next();
							donor = teamDonors.getDonorAgency();
							ampProjects = new ArrayList(teamDonors.getProject());
							Iterator projIter = ampProjects.iterator();
							if( ampProjects.isEmpty() == false )
							{
								while(projIter.hasNext())
								{
									project = (Project)projIter.next();
									logger.info( rowCnt + " : "+ project.getName());
									rowCnt = rowCnt + 1;
									if(project.getTermAssist() != null)
									{
										projTermAssists = new ArrayList(project.getTermAssist());
										Iterator projTermAssistIter = projTermAssists.iterator();
										while(projTermAssistIter.hasNext())
										{
											projectTermAssist = (ProjectTermAssist)projTermAssistIter.next();
											logger.info(rowCnt + " : "+projectTermAssist.getTermAssistName());
											rowCnt = rowCnt + 1;
										} // End of TermAssistFund 
									} // Check if TermAssist is not NULL
									
								}// end of Project Iteration
							}// Check if Project size > 0
						}// End of Donor Iteration
						flag = 1;
					}// Check if ampDonors.size() > 0

					if(multiReport.getTotalSectorTermAssistFund() != null)
					{
						if(multiReport.getTotalSectorTermAssistFund().size() > curr){
							curr = multiReport.getTotalSectorTermAssistFund().size(); 
						}

						Collection totalSectorTermAssistFunds = new ArrayList(multiReport.getTotalSectorTermAssistFund());
						Iterator totatSecTermAssistFdIter = totalSectorTermAssistFunds.iterator();
						while(totatSecTermAssistFdIter.hasNext())
						{
							termFund = (TermFund)totatSecTermAssistFdIter.next();
							logger.info("getTotalSectorTermAssistFund");
							rowCnt = rowCnt + 1;
						}
					} // Check if  TotalSectorTermAssistFund is not NULL
					logger.info(rowCnt + " ::: " + multiReport.getSector());
					rowCnt = rowCnt + 1;
					
					if(multiReport.getTotalTeamTermAssistFund() != null)
					{
						totalTeamTermAssistFunds = new ArrayList(multiReport.getTotalTeamTermAssistFund());
						totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
						
						while(totalTeamTermAssistFundIter.hasNext())
						{
							termFund = (TermFund)totalTeamTermAssistFundIter.next();
							logger.info(rowCnt +  "  :totalTeamTermAssistFund: " + termFund.getTermAssistName());
							rowCnt = rowCnt + 1;
						}
					}
					
						if(multiReport.getTotalTeamFund() != null)
						{
							logger.info(rowCnt + "  : getTotalTeamFund + Grand Total " );
							//rowCnt = rowCnt + 1;
						}
					
				}	// ENd of Main Loop
			}
			//rowCnt = rowCnt + sectorSize;
			rowCnt = rowCnt + 1;
			colCnt = 5 + formBean.getFiscalYearRange().size() + ( formBean.getFiscalYearRange().size() *  3) + 5+4;	
			logger.info(rowCnt +" : Row : Col : "+ colCnt);	
			
			Integer year =null;
			boolean isTotalSectTermAssist = false;
			Object[][] data = new Object[rowCnt][colCnt];
			
			row = col = 0;
			if( coll.isEmpty() == false )
			{
				iter = coll.iterator();
				while(iter.hasNext())
				{
					multiReport = (multiReport)iter.next();
					sector = multiReport.getSector();
					col = 0;
					data[row][col] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
					for(int i=0; i<formBean.getFilter().length; i++)
					{
						col+=1;
						data[row][col] = formBean.getFilter()[i].toString();
					}

					ampFisYears = new ArrayList(formBean.getFiscalYearRange());
					Iterator fiscIter = ampFisYears.iterator();
					fiscalYrs fisc=null;
					if(fiscIter.hasNext() == true)
					{
						year = (Integer)fiscIter.next();
						yy = year.intValue();
					}
					yyTmp = yy;			
					
					ampDonors = new ArrayList(multiReport.getDonors());
					donorIter = ampDonors.iterator();
					if(ampDonors.size() > 0)
					{
						col = 2;
						while(donorIter.hasNext())
						{	
							teamDonors = (AmpTeamDonors)donorIter.next();
							donor = teamDonors.getDonorAgency();
							ampProjects = new ArrayList(teamDonors.getProject());
							Iterator projIter = ampProjects.iterator();
							if( ampProjects.isEmpty() == false )
							{
								while(projIter.hasNext())
								{
									isProject = true;
									col = col + 1;
									data[row][col] = multiReport.getSector();
									col = col+ 1;
									data[row][col] = teamDonors.getDonorAgency();
									project = (Project)projIter.next();
									col= col+ 1;
									data[row][col] = project.getName();
									ampFunds = new ArrayList(project.getAmpFund());
									Iterator fundIter = ampFunds.iterator();
									if( ampFunds.isEmpty() == false )
									{
										while(fundIter.hasNext())
										{
											col = col+1;
											data[row][col] = Integer.toString(yy);
											ampFund = (AmpFund)fundIter.next();
											//////////here
											col=col+1;
											data[row][col] = ampFund.getCommAmount();
											col = col+1;
											data[row][col] = ampFund.getPlannedDisbAmount();
											col = col+1;
											data[row][col] = ampFund.getDisbAmount();
											col = col+1;
											data[row][col] = ampFund.getExpAmount();
											yy = yy + 1;
										}
										yy = yyTmp;
									}
									/////////here
									col = col+1;
									data[row][col]= project.getProjCommAmount();	
									col = col + 1;
									data[row][col] = project.getProjPlannedDisbAmount();
									col = col+1;
									data[row][col] = project.getProjDisbAmount();
									col = col+1;
									data[row][col] = project.getProjExpAmount();
										
									if(project.getTermAssist() != null)
									{
										projTermAssists = new ArrayList(project.getTermAssist());
										Iterator projTermAssistIter = projTermAssists.iterator();
										yy = yyTmp;
										while(projTermAssistIter.hasNext())
										{
											//boolean istermAssist = true;
											row = row + 1;
											data[row][3] = sector;
											data[row][4] = teamDonors.getDonorAgency();
											col = 5;
											projectTermAssist = (ProjectTermAssist)projTermAssistIter.next();
											data[row][col] = projectTermAssist.getTermAssistName();
											termAssistFunds = new ArrayList(projectTermAssist.getTermAssistFund());
											termAssistFdIter = termAssistFunds.iterator();
											while(termAssistFdIter.hasNext())
											{
												col = col+1;
												data[row][col] = Integer.toString(yy);
												ampFund = (AmpFund)termAssistFdIter.next();
												//////////here
												col+=1;
												data[row][col] = ampFund.getCommAmount();
												col+=1;
												data[row][col] = ampFund.getPlannedDisbAmount();
												col+=1;
												data[row][col] = ampFund.getDisbAmount();
												col+=1;
												data[row][col] = ampFund.getExpAmount();
												yy = yy + 1;
											}
											////////here
											col+=1;
											data[row][col] = projectTermAssist.getTermCommAmount();
											col+=1;
											data[row][col] = projectTermAssist.getTermPlannedDisbAmount();
											col+=1;
											data[row][col] = projectTermAssist.getTermDisbAmount();
											col+=1;
											data[row][col] = projectTermAssist.getTermExpAmount();
										} // End of TermAssistFund 
									} // Check if TermAssist is not NULL
									
									row = row + 1;
									col = 2;
								}// end of Project Iteration
							}// Check if Project size > 0
							col = 2;
							row = row - 1;
							row = row + 1;
						}// End of Donor Iteration
						flag = 1;
					}// Check if ampDonors.size() > 0

					row = row - 1;
					yy = yyTmp;
					boolean isSectorTermAssist = false;
					if(multiReport.getTotalSectorTermAssistFund() != null)
					{
						Collection totalSectorTermAssistFunds = new ArrayList(multiReport.getTotalSectorTermAssistFund());
						Iterator totatSecTermAssistFdIter = totalSectorTermAssistFunds.iterator();
						while(totatSecTermAssistFdIter.hasNext())
						{
							isTotalSectTermAssist = true;
							row = row + 1;
							data[row][3] = multiReport.getSector();
							data[row][4] = donor;
							col = 5;
							termFund = (TermFund)totatSecTermAssistFdIter.next();
							data[row][col] = "Total " + termFund.getTermAssistName();
							Collection termFundTotals = new ArrayList(termFund.getTermFundTotal());
							Iterator termFundTotalIter = termFundTotals.iterator();
							while(termFundTotalIter.hasNext())
							{
								col+=1;
								data[row][col]  = Integer.toString(yy);
								termFundTotal = (TermFundTotal) termFundTotalIter.next();
								///////here
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
							//////////here
							col+=1;
							data[row][col] = termFund.getTotDonorCommAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorPlannedDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorExpAmount();
							
						}
					} // Check if  TotalSectorTermAssistFund is not NULL
					
					col = 5;
					row = row + 1;
					data[row][3] = multiReport.getSector();
					data[row][4] = donor;
					
					data[row][col] = "Total for " + multiReport.getSector();
					yy = yyTmp;
					Collection totalSectorFunds = new ArrayList(multiReport.getTotalSectorFund());
					Iterator totalSectorFundIter  = totalSectorFunds.iterator();
					while(totalSectorFundIter.hasNext())
					{
						col+=1;
						data[row][col] = Integer.toString(yy);
						fundTotal = (FundTotal)totalSectorFundIter.next();
						//////////////here
						col+=1;
						data[row][col] = fundTotal.getTotCommAmount();
						col+=1;
						data[row][col] = fundTotal.getTotPlannedDisbAmount();
						col = col+1;
						data[row][col] = fundTotal.getTotDisbAmount();
						col = col+1;
						data[row][col] = fundTotal.getTotExpAmount() ;
						yy = yy + 1;
					}
					////here
					col+=1;
					data[row][col] = multiReport.getSectorCommAmount();
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
							Collection termFundTotals = new ArrayList(termFund.getTermFundTotal());
							Iterator termFundTotalIter = termFundTotals.iterator();
							while(termFundTotalIter.hasNext())
							{
								col+=1;
								data[row][col]  = Integer.toString(yy);
								termFundTotal = (TermFundTotal) termFundTotalIter.next();
								//////here
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
							////here
							col+=1;
							data[row][col] = termFund.getTotDonorCommAmount();
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
							yy = yyTmp;
							totalTeamFunds = new ArrayList(multiReport.getTotalTeamFund());
							totalTeamFundIter  = totalTeamFunds.iterator();
							while(totalTeamFundIter.hasNext())
							{
								col+=1;
								data[row][col] = Integer.toString(yy);
								fundTotal = (FundTotal)totalTeamFundIter.next();
								//////here
								col+=1;
								data[row][col] = fundTotal.getTotCommAmount();
								col+=1;
								data[row][col] = fundTotal.getTotPlannedDisbAmount();
								col = col+1;
								data[row][col] = fundTotal.getTotDisbAmount();
								col = col+1;
								data[row][col] = fundTotal.getTotExpAmount() ;
								yy = yy + 1;
							}
							/////here
							col+=1;
							data[row][col]= multiReport.getTeamCommAmount();
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
						logger.info("*******     Count != SECTOR SIZE **********");
					}
					row = row + 1;
					flag = 1;
				}	// ENd of Main Loop
			}
			else
			{
				flag = 0;	
				logger.info("Collection empty");
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

				String fileName="SbpXls.jrxml";
				SectorByProjectDatasource dataSource = new SectorByProjectDatasource(data);
				ActionServlet s = getServlet();
				String jarFile = s.getServletContext().getRealPath(
										"/WEB-INF/lib/jasperreports-0.6.1.jar");
				String realPathJrxml = s.getServletContext().getRealPath(
										 "/WEB-INF/classes/org/digijava/module/aim/reports/SectorByProjectXls.jrxml");
				SbpXlsJrxml jrxml = new SbpXlsJrxml();
				jrxml.createJrxml(yyCnt, realPathJrxml);
				JasperCompileManager.compileReportToFile(realPathJrxml);
				if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
				{
				byte[] bytes = null;
				String jasperFile = s.getServletContext().getRealPath(
    									"/WEB-INF/classes/org/digijava/module/aim/reports/SectorByProjectXls.jasper");
				Map parameters = new HashMap();

				JasperPrint jasperPrint =JasperFillManager.fillReport(jasperFile,parameters,dataSource);
				String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/SectorXLS.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","inline; filename=SectorByProjectXls.xls");
				
					try
					{
						outputStream = response.getOutputStream();
						JRXlsExporter exporter = new JRXlsExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
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
				else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
		{
				logger.info("EXPORTING CSV for ProjectbyDonor");
				ServletOutputStream outputStream = null;
				try
				{
				
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/SectorByProjectXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/SectorByProjectXls.csv");
					
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
