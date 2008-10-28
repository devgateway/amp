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
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.fiscalYrs;
import org.digijava.module.aim.helper.FundTotal;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.SectorByProjectDatasource;
import org.digijava.module.aim.helper.SbpJrxml;

public class SectorByProjectPDF	extends Action 
{
	private static Logger logger = Logger.getLogger(SectorByProjectPDF.class) ;
	private static int fieldHeight = 0; 	 	  
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
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
		logger.info("yycnt  "+yyCnt);
		
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
		//colCnt = 5 + formBean.getFiscalYearRange().size() + ( formBean.getFiscalYearRange().size() *  3) + 5;
		colCnt = 5 + formBean.getFiscalYearRange().size() + ( formBean.getFiscalYearRange().size() *  3) + 5+4;	
		logger.info(rowCnt +" : Row : Col : "+ colCnt);	
		logger.info(" Sector Size  : "+ sectorSize);
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
				/////////////logger.info("yearrrrrrrrrrrr" +yy);
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
								calculateFieldHeight(multiReport.getSector());
								
								col = col+ 1;
								data[row][col] = teamDonors.getDonorAgency();
								calculateFieldHeight(teamDonors.getDonorAgency());
								
								project = (Project)projIter.next();

								col= col+ 1;
								data[row][col] = project.getName();
								calculateFieldHeight(project.getName());
								logger.info("project name" +project.getName());
								
								ampFunds = new ArrayList(project.getAmpFund());
								Iterator fundIter = ampFunds.iterator();
								if( ampFunds.isEmpty() == false )
								{
									while(fundIter.hasNext())
									{
										col = col+1;
										data[row][col] = Integer.toString(yy);
										ampFund = (AmpFund)fundIter.next();
										
										col = col+1;
										data[row][col]= ampFund.getCommAmount();
										col=col+1;
										data[row][col] = ampFund.getPlannedDisbAmount();
										col = col+1;
										data[row][col] = ampFund.getDisbAmount();
										col = col+1;
										data[row][col] = ampFund.getExpAmount();
										yy = yy + 1;
										logger.info("yy  "+yy);
									}
									yy = yyTmp;
									
								}
								col = col+1;
								data[row][col]= project.getProjCommAmount();
									
								col = col + 1;
								data[row][col] = project.getProjPlannedDisbAmount();
								col = col+1;
								data[row][col] = project.getProjDisbAmount();
								col = col+1;
								data[row][col] = project.getProjExpAmount();
								///////logger.info("1234row"+row+" column "+col+"  data"+ data[row][col]);
									
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
											col+=1;
											data[row][col]= ampFund.getCommAmount();
											col+=1;
											data[row][col] = ampFund.getPlannedDisbAmount();
											col+=1;
											data[row][col] = ampFund.getDisbAmount();
											col+=1;
											data[row][col] = ampFund.getExpAmount();
											yy = yy + 1;
										//	logger.info("termAssisthas row"+row+" column "+col+"  data"+ data[row][col]);
										}
										yy=yyTmp;
										col+=1;
										data[row][col]= projectTermAssist.getTermCommAmount();
										col+=1;
										data[row][col] = projectTermAssist.getTermPlannedDisbAmount();
										col+=1;
										data[row][col] = projectTermAssist.getTermDisbAmount();
										col+=1;
										data[row][col] = projectTermAssist.getTermExpAmount();
										/////////logger.info("123termAssisthas row"+row+" column "+col+"  data"+ data[row][col]);
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
						calculateFieldHeight(data[row][col].toString());
						
						Collection termFundTotals = new ArrayList(termFund.getTermFundTotal());
						Iterator termFundTotalIter = termFundTotals.iterator();
						while(termFundTotalIter.hasNext())
						{
							col+=1;
							data[row][col]  = Integer.toString(yy);
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col+=1;
							data[row][col]= termFundTotal.getTotCommAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
							yy = yy + 1;
							////////////logger.info("termFundtotaler row"+row+" column "+col+"  data"+ data[row][col]);
							logger.info("yyyy"+yy);
						}
						col+=1;
						data[row][col]= termFund.getTotDonorCommAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
						//////////logger.info("123termFundtotaler row"+row+" column "+col+"  data"+ data[row][col]);
						
					}
				} // Check if  TotalSectorTermAssistFund is not NULL
				
				col = 5;
				row = row + 1;
				data[row][3] = multiReport.getSector();
				data[row][4] = donor;
				
				data[row][col] = "Total for " + multiReport.getSector();
				calculateFieldHeight(data[row][col].toString());
				
				yy = yyTmp;
				//logger.info("yy11111111111  " +yy);
				Collection totalSectorFunds = new ArrayList(multiReport.getTotalSectorFund());
				Iterator totalSectorFundIter  = totalSectorFunds.iterator();
				while(totalSectorFundIter.hasNext())
				{
					col+=1;
					data[row][col] = Integer.toString(yy);
					fundTotal = (FundTotal)totalSectorFundIter.next();
					col+=1;
					data[row][col]= fundTotal.getTotCommAmount();
					col+=1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col = col+1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col = col+1;
					data[row][col] = fundTotal.getTotExpAmount() ;
					yy = yy + 1;
					/////////logger.info("totalsectorfund row"+row+" column "+col+"  data"+ data[row][col]+"  year"+yy);
				}
				col+=1;
				data[row][col]= multiReport.getSectorCommAmount();
				col+=1;
				data[row][col] = multiReport.getSectorPlannedDisbAmount();
				col+=1;
				data[row][col] = multiReport.getSectorDisbAmount();
				col+=1;
				data[row][col] = multiReport.getSectorExpAmount();
				//////////logger.info("totalsectorfund row"+row+" column "+col+"  data"+ data[row][col]+"  year"+yy);
				
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
						
						Collection termFundTotals = new ArrayList(termFund.getTermFundTotal());
						Iterator termFundTotalIter = termFundTotals.iterator();
						yy=yyTmp;
						while(termFundTotalIter.hasNext())
						{
							col+=1;
							data[row][col]  = Integer.toString(yy);
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col+=1;
							data[row][col]= termFundTotal.getTotCommAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
							yy = yy + 1;
							///////logger.info("termfund row"+row+" column "+col+"  data"+ data[row][col]+"  year"+yy);
							logger.info("y3333"+yy);
						}
						col+=1;
						data[row][col] = termFund.getTotDonorCommAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
						////////logger.info("termfund row"+row+" column "+col+"  data"+ data[row][col]+"  year"+yy);
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
							col+=1;
							data[row][col] = fundTotal.getTotCommAmount();
							col+=1;
							data[row][col] = fundTotal.getTotPlannedDisbAmount();
							col = col+1;
							data[row][col] = fundTotal.getTotDisbAmount();
							col = col+1;
							data[row][col] = fundTotal.getTotExpAmount() ;
							yy = yy + 1;
							logger.info("y55555"+yy);
						}
						col=+1;
						data[row][col] = multiReport.getTeamCommAmount();
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
					logger.info("*******     Count != SECTOR SIXE **********");
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
		
		int height = (( fieldHeight / 25 ) * 10 ) + 50;
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
									"/WEB-INF/classes/org/digijava/module/aim/reports/SectorByProjectPdf.jrxml");
			SbpJrxml jrxml = new SbpJrxml();
			// calling dynamic jrxml
			jrxml.createJrxml(yyCnt, realPathJrxml, height);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
											"/WEB-INF/classes/org/digijava/module/aim/reports/SectorByProjectPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				logger.info("Exception from SectorByProjectPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=SectorByProjectPdf.pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			}
			else
			{
				logger.info("Nothing to display");
			}
		}
		else
		{
			logger.info("NO PDF GENERATED ");
		}
		/*for(int lol= 0 ;lol<row;lol++)
		{
			for(int hhh = 0;hhh<col;hhh++)
			{
			logger.info(data[lol][hhh] + "   rowis  " +lol+"   col is  "+hhh);
			}
		}*/
		return null;
	}// end of Execute Func

	void calculateFieldHeight(String input)
	{
		////System.out.println(" Large ::" + fieldHeight + " :: CUrrent : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}// end of Class
