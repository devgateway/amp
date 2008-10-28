package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.*;
import java.util.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.PlannedProjPdfJrxml;
import org.digijava.module.aim.helper.ViewProjectDatasource;
import org.apache.log4j.Logger ;
import javax.servlet.*;


public class PlannedProjectPDF extends Action 
{
	private static Logger logger = Logger.getLogger(PlannedProjectPDF.class) ;
	private static int fieldHeight = 0; 	 		  
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
		
		int col=0, j=0, flag=0, colCnt=0, row=0, year=0, rowCnt=0, tempYear, yearCount;
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
				calculateFieldHeight(data[row][col].toString());
				
				col+=1;
				data[row][col] = report.getTitle();
				calculateFieldHeight(report.getTitle());
				
				col+=1;
				data[row][col] = report.getStatus();
				col+=1;				
				data[row][col] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getModality().toString().replace('[',' ').replace(']',' ');
				col+=1;				
				data[row][col] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][col].toString());
				
				col+=1;				
				data[row][col] = report.getLevel();
				col+=1;				
				data[row][col] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][col].toString());
				
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
		}
		
		int height = (( fieldHeight / 25 ) * 10 ) + 50;
		logger.info(" Column Height = " + height );

		if(flag == 1)
		{
			
			for(int i=0; i < rowCnt; i++)
			{
				for(j=0; j < colCnt; j++)
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

			ViewProjectDatasource dataSource = new ViewProjectDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
						  "/WEB-INF/lib/jasperreports-0.6.1.jar");
		 	System.setProperty("jasper.reports.compile.class.path",jarFile);
		 	String realPathJrxml = s.getServletContext().getRealPath(
			 					"/WEB-INF/classes/org/digijava/module/aim/reports/PlannedProjectPdf.jrxml");
		 	PlannedProjPdfJrxml jrxml = new PlannedProjPdfJrxml();
			// calling dynamic jrxml
		 	jrxml.createJrxml(realPathJrxml, yearCount, height);
		 	JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/PlannedProjectPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				logger.info("Exception from PlannedProjectPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=PlannedProjectPdf.pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
				logger.info(" Generating PDF complete " );
			}
			else
			{
				logger.info("Nothing to display");
			}
		}		
		return null;
	}


	void calculateFieldHeight(String input)
	{
		////System.out.println(" Large ::" + fieldHeight + " :: CUrrent : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}
