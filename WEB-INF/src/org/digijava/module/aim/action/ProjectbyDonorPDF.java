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
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.fiscalYrs ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.ProjectbyDonorDatasource;
import org.digijava.module.aim.helper.PbdJrxml;


public class ProjectbyDonorPDF extends Action {
	
	private static Logger logger = Logger.getLogger(ProjectbyDonorPDF.class) ;
	private static int fieldHeight = 0; 	 

	public ActionForward execute(ActionMapping mapping,
							ActionForm form,
							javax.servlet.http.HttpServletRequest request,
							javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception 
	{
		MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) form;
		Collection coll = new ArrayList();
		logger.info(" in here project by donor pdf ");
		if (formBean != null) {
			logger.info("formBean is not null");
			coll= formBean.getMultiReport();
		} 
		else {
			logger.info("formbean is null");
		}
		if(coll == null)
			logger.info("coll is null");
		else
			logger.info("formBean is not null");

		Iterator iter = null;
		if (coll.size() == 0) {
			logger.info("collection is empty");
		} 
		else {
			logger.info("collection is not empty");
			iter = coll.iterator();
		}
	 	
		////System.out.println("col size "+ coll.size());
		multiReport rep;
		AmpFund ampFund = null;
		TermFund termFund = null;
		Project project= null;
		TermFundTotal termFundTotal;
		ProjectTermAssist projTermsAssist = null;
		ArrayList ampDonors,ampFunds,ampFundTotal,ampProjects,termsAssist,termAssistFund,ampFisYears =null;
		ArrayList totalDonorTermAssistFund;
		Collection totalTeamTermAssistFund;
		AmpTeamDonors teamDonors = null;
		Iterator totalDrrTermAssistFundIter,projectIter,donorIter,fundIter,totalTeamTermAssistFundIter;
		Iterator termsIter, assistFundIter = null;
		int row, col, count = 0, yy=0, yyTmp=0, flag=0, rowCnt=0, colCnt=0, yyCnt=0, drSize=0,prev ,curr = 0, size=0;
			
		Integer year = null;
		String drName="";
		boolean check = false;
  		while(iter.hasNext())
		{
			rep = (multiReport)iter.next();
			ampFisYears = new ArrayList((formBean.getFiscalYearRange()));
			yyCnt = ampFisYears.size();
			ampDonors = new ArrayList(rep.getDonors());
			drSize = rep.getDonors().size();
			donorIter = ampDonors.iterator();
			while(donorIter.hasNext())
			{
				teamDonors = (AmpTeamDonors) donorIter.next();
				ampProjects = new ArrayList(teamDonors.getProject());
				projectIter = ampProjects.iterator();
				while(projectIter.hasNext())
				{	
					rowCnt = rowCnt + 1;
					project = (Project)projectIter.next();
//					logger.info("   : Project : " + project.getName());
					if(project.getTermAssist() != null)
					{
						termsAssist = new ArrayList(project.getTermAssist());
						termsIter = termsAssist.iterator();
						while(termsIter.hasNext())
						{
							rowCnt = rowCnt + 1;	
							projTermsAssist = (ProjectTermAssist) termsIter.next();
//							logger.info( " : Term Assist : " + projTermsAssist.getTermAssistName());
						}
					}
				}	// end of WHILE Project loop
				if(teamDonors.getTotalDonorTermAssistFund() != null)
				{
					totalDonorTermAssistFund = new ArrayList(teamDonors.getTotalDonorTermAssistFund());
					totalDrrTermAssistFundIter = totalDonorTermAssistFund.iterator();
					if(teamDonors.getTotalDonorTermAssistFund().size() > curr){
						curr = teamDonors.getTotalDonorTermAssistFund().size(); 
					}
					if(totalDonorTermAssistFund.size() > 0)
					{	
						while(totalDrrTermAssistFundIter.hasNext())
						{
//							logger.info(" Total Dr Term Assist FUnd : ");
							termFund = (TermFund) totalDrrTermAssistFundIter.next();
							rowCnt = rowCnt + 1;
						}	
					}
				} // Check if TotalDonorTermAssistFund NULL
			}	// End of while Donor iteration
		}
 		logger.info("Row Cnt : " + rowCnt +": dr Size :" + drSize + " :CURR : " + curr );
  		rowCnt = rowCnt + drSize + (curr + 1);
  		colCnt = 3 + 3 + yyCnt + (yyCnt *4) + 5;
// 		logger.info("Curr Value " + curr );
 		logger.info(rowCnt +": ROW : COL " + colCnt);  		
		Object[][] data = new Object[rowCnt+2][colCnt];
		col = row = 0;
		if(coll.size() > 0)
		{	
			iter = coll.iterator();
			data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
				String filterName[] = new String[2];
				filterName = formBean.getFilter();
				for(int i=0; i<formBean.getFilter().length; i++)
				{	
					col = col + 1;
					data[row][col] = formBean.getFilter()[i].toString();
				}
				
			while(iter.hasNext())
			{
				
				rep = (multiReport)iter.next();
				col = col + 1;
				if(col >= 26)
				{
					logger.info(" this is why" +col);
					col=col-1;
					break;
				}
				else
				{
					logger.info(" thi is col "+ col);
				}
				logger.info(" row.. "+row+"  col... "+col);
				data[row][col] = rep.getTeamName();
				ampFisYears = new ArrayList((formBean.getFiscalYearRange()));
				yyCnt = ampFisYears.size();
				Iterator fiscIter = ampFisYears.iterator();
				fiscalYrs fisc=null;
				if(fiscIter.hasNext() == true)
				{
					year = (Integer)fiscIter.next();
					yy = year.intValue();
				}
				yyTmp = yy;
				
				ampDonors = new ArrayList(rep.getDonors());
				if(ampDonors.size() > 0)
				{
					donorIter = ampDonors.iterator();
					while(donorIter.hasNext())
					{
						//data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
						
						teamDonors = (AmpTeamDonors) donorIter.next();
						ampProjects = new ArrayList(teamDonors.getProject());
						projectIter = ampProjects.iterator();
						if(ampProjects.size() > 0 )
						{	
							while(projectIter.hasNext())
							{	
								
								col = col + 1;
								data[row][col] = teamDonors.getDonorAgency();
								calculateFieldHeight(teamDonors.getDonorAgency());
								
								drName = teamDonors.getDonorAgency();
//								logger.info(row +":  DR : " + data[row][col]);
								project = (Project)projectIter.next();
								col = col + 1;
								data[row][col] = project.getName();
								calculateFieldHeight(project.getName());

								if(project.getName().length() > size ){
									size = project.getName().length();
								}
									
								ampFunds = new ArrayList(project.getAmpFund());
								fundIter = ampFunds.iterator();
								while(fundIter.hasNext())
								{
									col = col+1;
									data[row][col] = Integer.toString(yy);
									ampFund = (AmpFund)fundIter.next();
									col = col+1;
									data[row][col] = ampFund.getCommAmount();
									col = col+1;
									data[row][col] = ampFund.getPlannedDisbAmount();
									col = col+1;
									data[row][col] = ampFund.getDisbAmount();
									col = col+1;
									data[row][col] = ampFund.getExpAmount();
									yy = yy + 1;
								}// end of Funds loop
								yyCnt = yy - yyTmp;
								yy = yyTmp ;
								col = 5 + yyCnt + (yyCnt*4)+1;
								data[row][col] = project.getProjCommAmount();
								col = col+1;
								data[row][col] = project.getProjPlannedDisbAmount();
								col = col+1;
								data[row][col] = project.getProjDisbAmount();
								col = col+1;
								data[row][col] = project.getProjExpAmount();
								col = col+1;
								data[row][col] = project.getProjUnDisbAmount();

								
								if(project.getTermAssist() != null)
								{
									termsAssist = new ArrayList(project.getTermAssist());
									termsIter = termsAssist.iterator();
									if(termsAssist.size() > 0)
									{
										while(termsIter.hasNext())
										{
											col=5;
											row = row + 1;
											data[row][4] = teamDonors.getDonorAgency();
											projTermsAssist = (ProjectTermAssist) termsIter.next();
											data[row][col] = projTermsAssist.getTermAssistName();
//											logger.info( row + " : Term Assist " +projTermsAssist.getTermAssistName());
											termAssistFund = new ArrayList(projTermsAssist.getTermAssistFund());
											assistFundIter = termAssistFund.iterator();
											if(termAssistFund.size() > 0)
											{
												while(assistFundIter.hasNext())
												{
													col = col+1;
													data[row][col] = Integer.toString(yy);

													ampFund = (AmpFund) assistFundIter.next();
													col = col + 1;
													data[row][col] = ampFund.getCommAmount();
													col = col + 1;
													data[row][col] = ampFund.getPlannedDisbAmount();
													col = col + 1;
													data[row][col] = ampFund.getDisbAmount();
													col = col + 1;
													data[row][col] = ampFund.getExpAmount();
												}
												col = col + 1;
												data[row][col] = projTermsAssist.getTermCommAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermPlannedDisbAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermDisbAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermExpAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermUnDisbAmount();
											}
										}
									}// End of  IF Terms Assist 
								} // Check if TermAssist is not NUll
								row = row + 1;
								col = 3;
							}	// end of WHILE Project loop
						}// end of IF Project loop

						row = row - 1;
						if(teamDonors.getTotalDonorTermAssistFund() != null)
						{
							//data[row][4] = teamDonors.getDonorAgency();
							totalDonorTermAssistFund = new ArrayList(teamDonors.getTotalDonorTermAssistFund());
							totalDrrTermAssistFundIter = totalDonorTermAssistFund.iterator();
							if(totalDonorTermAssistFund.size() > 0)
							{	
								while(totalDrrTermAssistFundIter.hasNext())
								{
									row = row + 1;
									col = 5;
									data[row][4] = teamDonors.getDonorAgency();	
									termFund = (TermFund) totalDrrTermAssistFundIter.next();
									data[row][col] = "Total " + termFund.getTermAssistName();
									calculateFieldHeight(data[row][col].toString());
									
//									logger.info( row + ":  TotalDonorTermAssistFund : " + termFund.getTermAssistName());
									Collection termFundT = new ArrayList(termFund.getTermFundTotal());
									Iterator termFundTotalIter = termFundT.iterator();
									while(termFundTotalIter.hasNext())
									{
										termFundTotal = (TermFundTotal) termFundTotalIter.next();
										col = col+1;
										data[row][col] = Integer.toString(yy);
										
										col = col + 1;
										data[row][col] = termFundTotal.getTotCommAmount();
										col = col + 1;
										data[row][col] = termFundTotal.getTotPlannedDisbAmount();
										col = col + 1;
										data[row][col] = termFundTotal.getTotDisbAmount();
										col = col + 1;
										data[row][col] = termFundTotal.getTotExpAmount();
									}
									yyCnt = yy - yyTmp;
									yy = yyTmp ;

									col = col + 1;
									data[row][col] =  termFund.getTotDonorCommAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorPlannedDisbAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorDisbAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorExpAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorUnDisbAmount();

								}
								
							}// End of Total Donor Term Assist Fund
						} // Check if TotalDonorTermAssistFund NULL

						flag = 1;
						row = row + 1;
//						logger.info( row + " Total for ---  " + teamDonors.getDonorAgency());						
						col = 5;
						data[row][4] = teamDonors.getDonorAgency();
						ampFundTotal = new ArrayList(teamDonors.getTotalDonorFund());
						Iterator fundTotalIter = ampFundTotal.iterator();
						data[row][col] = "Total for  " + teamDonors.getDonorAgency();
						calculateFieldHeight(data[row][col].toString());						
						
						while(fundTotalIter.hasNext())
						{
							col = col+1;
							data[row][col] = Integer.toString(yy);
							
							FundTotal ampFundT = (FundTotal)fundTotalIter.next();
							col = col + 1;
							data[row][col] = ampFundT.getTotCommAmount() ;
							col = col + 1;
							data[row][col] = ampFundT.getTotPlannedDisbAmount();
							col = col + 1;
							data[row][col] = ampFundT.getTotDisbAmount() ;
							col = col + 1;
							data[row][col] = ampFundT.getTotExpAmount() ;
						}
						yyCnt = yy - yyTmp;
						yy = yyTmp ;

						col = col+1;
						data[row][col] = teamDonors.getDonorCommAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorPlannedDisbAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorDisbAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorExpAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorUnDisbAmount();

						col = 3;
						row = row + 1;
//						logger.info(" Next Donor " + row);						
					}	// End of while Donor iteration
				} // End of if DOnor iteration
				
				row = row - 1;
				if(rep.getTotalTeamTermAssistFund() != null)
				{
					
					col = 5;
					//data[row][4] = teamDonors.getDonorAgency();
					totalTeamTermAssistFund = new ArrayList(rep.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFund.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						row = row + 1;
						col = 5;
						data[row][4] = teamDonors.getDonorAgency();
						termFund = (TermFund) totalTeamTermAssistFundIter.next();
						data[row][col] = "Total for" + termFund.getTermAssistName();
						calculateFieldHeight(data[row][col].toString());
												
//						logger.info( row + " : totalTeamTermAssistFund  :  " + termFund.getTermAssistName());
						Collection termFundTotalColl = new ArrayList(termFund.getTermFundTotal());
						Iterator termFundTotalIter = termFundTotalColl.iterator();
						while(termFundTotalIter.hasNext())
						{
							col = col+1;
							data[row][col] = Integer.toString(yy);

							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col = col + 1;
							data[row][col] = termFundTotal.getTotCommAmount();
							col = col + 1;
							data[row][col] = termFundTotal.getTotPlannedDisbAmount();
							col = col + 1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col = col + 1;
							data[row][col] = termFundTotal.getTotExpAmount();
						}
						yyCnt = yy - yyTmp;
						yy = yyTmp ;
						
						col = col + 1;
						data[row][col] = termFund.getTotDonorCommAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorExpAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorUnDisbAmount();
						
					}
				}
				row = row + 1;
//				logger.info( row + ":  For Total *** " + teamDonors.getDonorAgency());				
				col = 5;
				//data[row][4] = teamDonors.getDonorAgency();
				Collection totalTeamFund = new ArrayList(rep.getTotalTeamFund());
				Iterator totalTeamFundIter = totalTeamFund.iterator();
				logger.info("COLLLL :"+col);
				data[row][col] = "Total for " + rep.getTeamName();
				calculateFieldHeight(data[row][col].toString());
				
				while(totalTeamFundIter.hasNext())
				{
					data[row][4] = teamDonors.getDonorAgency();
					col = col+1;
					data[row][col] = Integer.toString(yy);

					FundTotal fundTotal = (FundTotal) totalTeamFundIter.next();
					col = col + 1;
					data[row][col] = fundTotal.getTotCommAmount();
					col = col + 1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col = col + 1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col = col + 1;
					data[row][col] = fundTotal.getTotExpAmount();
				}
				yyCnt = yy - yyTmp;
				yy = yyTmp ;

				col = col + 1;
				data[row][col] = rep.getTeamCommAmount();
				col = col + 1;
				data[row][col] = rep.getTeamPlannedDisbAmount();
				col = col + 1;
				data[row][col] = rep.getTeamDisbAmount();
				col = col + 1;
				data[row][col] = rep.getTeamExpAmount();
				col = col + 1;
				data[row][col] = rep.getTeamUnDisbAmount();

				row = row + 1;
			}
			flag = 1;
		}// end of Big Loop
		
		
		int height = (( fieldHeight / 25 ) * 10 ) + 50;
		logger.info(" Column Height = " + height );

		if(flag == 1)
		{
			// ReInit Year Count Value ......
			yyCnt = formBean.getFiscalYearRange().size();

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

			ProjectbyDonorDatasource dataSource = new ProjectbyDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
						  "/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
								 "/WEB-INF/classes/org/digijava/module/aim/reports/ProjectbyDonorPdf.jrxml");
			logger.info("Count ::::" + yyCnt);
			PbdJrxml jrxml = new PbdJrxml();
			if(yyCnt < 4)
			{	
				jrxml.createJrxml(yyCnt, realPathJrxml, height);
				JasperCompileManager.compileReportToFile(realPathJrxml);
				byte[] bytes = null;
				try
				{
					String jasperFile = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/ProjectbyDonorPDF.jasper");
					Map parameters = new HashMap();
					bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
				}
				catch (JRException e)
				{
					logger.info("Exception from ProjectbyDonorPDF = " + e);
				}	
				if (bytes != null && bytes.length > 0)
				{
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition","inline; filename=ProjectbyDonorPDF.pdf");
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
				logger.info(" Year Range is Greater that 3");
			}
		}
		return null;
	}	// end of Execute Func

	void calculateFieldHeight(String input)
	{
		////System.out.println(" Large ::" + fieldHeight + " :: CUrrent : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}	// end of Class






