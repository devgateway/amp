package org.digijava.module.aim.action;	
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

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
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.fiscalYrs ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.ProjectbyDonorDatasource;

import org.digijava.module.aim.helper.PbdXlsJrxml;


public class ProjectbyDonorXLS extends Action {
	
	private static Logger logger = Logger.getLogger(ProjectbyDonorXLS.class) ;
	 
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
		ArrayList ampDonors,ampFunds,ampFundTotal,ampProjects,termsAssist,termAssistFund,ampFisYears;
		ArrayList totalDonorTermAssistFund;
		Collection totalTeamTermAssistFund;
		AmpTeamDonors teamDonors = null;
		Iterator totalDrrTermAssistFundIter;
		Iterator projectIter,donorIter,fundIter,totalTeamTermAssistFundIter,termsIter,assistFundIter = null;
		int row, col, count = 0, yy=0, yyTmp=0, flag=0, rowCnt=0, colCnt=0, yyCnt=0, drSize=0;
		int prev ,curr = 0;
		
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
					logger.info("   : Project : " + project.getName());
					if(project.getTermAssist() != null)
					{
						termsAssist = new ArrayList(project.getTermAssist());
						termsIter = termsAssist.iterator();
						while(termsIter.hasNext())
						{
							rowCnt = rowCnt + 1;	
							projTermsAssist = (ProjectTermAssist) termsIter.next();
							logger.info( " : Term Assist : " + projTermsAssist.getTermAssistName());
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
							logger.info(" Total Dr Term Assist FUnd : ");
							termFund = (TermFund) totalDrrTermAssistFundIter.next();
							rowCnt = rowCnt + 1;
						}	
					}
				} // Check if TotalDonorTermAssistFund NULL
			}	// End of while Donor iteration
		}
  		rowCnt = rowCnt + drSize + (curr + 1);
  		//ch
  		colCnt = 3 + 3 + yyCnt + (yyCnt *3) + 3 +4;
  		logger.info("Curr Value " + curr );
  		logger.info(rowCnt +": ROW : COL " + colCnt);  		
		Object[][] data = new Object[rowCnt+1][colCnt+1];
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2= new StringBuffer();
		col = row = 0;
		if(coll.size() > 0)
		{	
			iter = coll.iterator();
			data[row][col] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
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
						teamDonors = (AmpTeamDonors) donorIter.next();
						ampProjects = new ArrayList(teamDonors.getProject());
						projectIter = ampProjects.iterator();
						if(ampProjects.size() > 0 )
						{	
							while(projectIter.hasNext())
							{	
								
								col = col + 1;
								if(col >= 26)
								{
									logger.info(" this is why" +col);
									col=col-1;
									break;
									
								}
								else
								{
									logger.info(" you are lucky");
								}
								data[row][col] = teamDonors.getDonorAgency();
								drName = teamDonors.getDonorAgency();
								logger.info(row +":  DR : " + data[row][col]);
								project = (Project)projectIter.next();
								col = col + 1;
								data[row][col] = project.getName();
								logger.info( row +"  :  Pr  :" + data[row][col]);
								ampFunds = new ArrayList(project.getAmpFund());
								fundIter = ampFunds.iterator();
								while(fundIter.hasNext())
								{
									col = col+1;
									data[row][col] = Integer.toString(yy);
									ampFund = (AmpFund)fundIter.next();
									col = col+1;
									data[row][col] = ampFund.getCommAmount();
									//here
									col=col+1;
									data[row][col]= ampFund.getPlannedDisbAmount();
									col = col+1;
									data[row][col] = ampFund.getDisbAmount();
									col = col+1;
									data[row][col] = ampFund.getExpAmount();
									yy = yy + 1;
								}// end of Funds loop
								yyCnt = yy - yyTmp;
								yy = yyTmp ;
								//col = 5 + yyCnt + (yyCnt*3)+1;
								col = 5 + yyCnt + (yyCnt*3)+1+3;
								
								data[row][col] = project.getProjCommAmount();
								//here
								col = col+1;
								data[row][col]=project.getProjPlannedDisbAmount();
								col = col+1;
								data[row][col] = project.getProjDisbAmount();
								col = col+1;
								data[row][col] = project.getProjExpAmount();

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
											logger.info( row + " : Term Assist " +projTermsAssist.getTermAssistName());
											termAssistFund = new ArrayList(projTermsAssist.getTermAssistFund());
											assistFundIter = termAssistFund.iterator();
											if(termAssistFund.size() > 0)
											{
												while(assistFundIter.hasNext())
												{
													ampFund = (AmpFund) assistFundIter.next();
													col = col + 2;
													data[row][col] = ampFund.getCommAmount();
													//here
													col = col+1;
													data[row][col]= ampFund.getPlannedDisbAmount();
													col = col + 1;
													data[row][col] = ampFund.getDisbAmount();
													col = col + 1;
													data[row][col] = ampFund.getExpAmount();
												}
												col = col + 1;
												data[row][col] = projTermsAssist.getTermCommAmount();
												//here
												col=col+1;
												data[row][col]= projTermsAssist.getTermPlannedDisbAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermDisbAmount();
												col = col + 1;
												data[row][col] = projTermsAssist.getTermExpAmount();
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
									logger.info( row + ":  TotalDonorTermAssistFund : " + termFund.getTermAssistName());
									Collection termFundT = new ArrayList(termFund.getTermFundTotal());
									Iterator termFundTotalIter = termFundT.iterator();
									while(termFundTotalIter.hasNext())
									{
										termFundTotal = (TermFundTotal) termFundTotalIter.next();
										col = col + 2;
										data[row][col] = termFundTotal.getTotCommAmount();
										//here
										col=col+1;
										data[row][col]= termFundTotal.getTotPlannedDisbAmount();
										col = col + 1;
										data[row][col] = termFundTotal.getTotDisbAmount();
										col = col + 1;
										data[row][col] = termFundTotal.getTotExpAmount();
									}
									col = col + 1;
									data[row][col] =  termFund.getTotDonorCommAmount();
									//here
									col = col+1;
									data[row][col]= termFund.getTotDonorPlannedDisbAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorDisbAmount();
									col = col + 1;
									data[row][col] = termFund.getTotDonorExpAmount();
								}
								
							}// End of Total Donor Term Assist Fund
						} // Check if TotalDonorTermAssistFund NULL

						flag = 1;
						row = row + 1;
						logger.info( row + " Total for ---  " + teamDonors.getDonorAgency());						
						col = 5;
						data[row][4] = teamDonors.getDonorAgency();
						ampFundTotal = new ArrayList(teamDonors.getTotalDonorFund());
						Iterator fundTotalIter = ampFundTotal.iterator();
						data[row][col] = "Total for  " + teamDonors.getDonorAgency();
						while(fundTotalIter.hasNext())
						{
							FundTotal ampFundT = (FundTotal)fundTotalIter.next();
							col = col + 2;
							data[row][col] = ampFundT.getTotCommAmount() ;
							//here
							col=col+1;
							data[row][col]=ampFundT.getTotPlannedDisbAmount();
							col = col + 1;
							data[row][col] = ampFundT.getTotDisbAmount() ;
							col = col + 1;
							data[row][col] = ampFundT.getTotExpAmount() ;
						}
						col = col+1;
						data[row][col] = teamDonors.getDonorCommAmount();
						//here
						col=col+1;
						data[row][col]= teamDonors.getDonorPlannedDisbAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorDisbAmount();
						col = col+1;
						data[row][col] = teamDonors.getDonorExpAmount();
						col = 3;
						//col = 4;
						row = row + 1;
						logger.info(" Next Donor " + row);						
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
						data[row][col] = "Total " + termFund.getTermAssistName();
						logger.info( row + " : totalTeamTermAssistFund  :  " + termFund.getTermAssistName());
						Collection termFundTotalColl = new ArrayList(termFund.getTermFundTotal());
						Iterator termFundTotalIter = termFundTotalColl.iterator();
						while(termFundTotalIter.hasNext())
						{
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col = col + 2;							
							data[row][col] = termFundTotal.getTotCommAmount();
							//here
							col = col+1;
							data[row][col]= termFundTotal.getTotPlannedDisbAmount();
							col = col + 1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col = col + 1;
							data[row][col] = termFundTotal.getTotExpAmount();
						}
						col = col + 1;
						data[row][col] = termFund.getTotDonorCommAmount();
						//here
						col = col+1;
						data[row][col] = termFund.getTotDonorPlannedDisbAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col = col + 1;
						data[row][col] = termFund.getTotDonorExpAmount();
					}
				}
				row = row + 1;
				logger.info( row + ":  For Total *** " + teamDonors.getDonorAgency());				
				col = 5;
				//data[row][4] = teamDonors.getDonorAgency();
				Collection totalTeamFund = new ArrayList(rep.getTotalTeamFund());
				Iterator totalTeamFundIter = totalTeamFund.iterator();
				data[row][col] = "Total for " + rep.getTeamName();
				while(totalTeamFundIter.hasNext())
				{
					data[row][4] = teamDonors.getDonorAgency();
					FundTotal fundTotal = (FundTotal) totalTeamFundIter.next();
					col = col + 2;
					data[row][col] = fundTotal.getTotCommAmount();
					//here	
					col= col+1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col = col + 1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col = col + 1;
					data[row][col] = fundTotal.getTotExpAmount();
				}
				col = col + 1;
				data[row][col] = rep.getTeamCommAmount();
				//here
				col=col+1;
				data[row][col]=rep.getTeamPlannedDisbAmount();
				col = col + 1;
				data[row][col] = rep.getTeamDisbAmount();
				col = col + 1;
				data[row][col] = rep.getTeamExpAmount();
				
				row = row + 1;
			}
			flag = 1;
		}// end of Big Loop

		if(flag == 1 )
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
					if(data[i][j] == null)
					{
						//logger.info("NULLLLLLLLLLLLLLLLLLLLLLL");
						data[i][j]="";
					}
						
				}
			}
			
			String fileName="ProjectbyDonorXls.jrxml";
			ProjectbyDonorDatasource dataSource = new ProjectbyDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
						  "/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
								 "/WEB-INF/classes/org/digijava/module/aim/reports/ProjectbyDonorXLS.jrxml");
			
			PbdXlsJrxml jrxml = new PbdXlsJrxml();
			jrxml.createJrxml(yyCnt, realPathJrxml);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
				byte[] bytes = null;
				String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/ProjectbyDonorXLS.jasper");
				Map parameters = new HashMap();
			
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,	dataSource);
				String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/ProjectbyDonorXLS.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");
				//response.setHeader("Content-Disposition","inline; filename=ProjectbyDonorXLS.xls");
				response.setHeader("Content-Disposition","filename=ProjectbyDonorXLS.xls");
			
				try
				{
				
					outputStream = response.getOutputStream();
					logger.info("In here .........................................");
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.exportReport();
					outputStream.flush();
					/*for(int m = 0 ;m<row;m++)
					{
						for(int ll=0;ll<col;ll++)
						{
							logger.info("datad   "+data[m][ll] +"  row  "+m+" col "+ll);
						}
					}*/
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
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/ProjectbyDonorXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/ProjectbyDonorXls.csv");
					
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
	}	// end of Execute Func
}	// end of Class





