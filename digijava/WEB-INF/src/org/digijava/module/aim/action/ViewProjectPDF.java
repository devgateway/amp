package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import java.util.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.viewProjectsJrxml;
import org.apache.log4j.Logger ;
import javax.servlet.*;
import org.digijava.module.aim.helper.ViewProjectDatasource;


public class ViewProjectPDF extends Action 
{
	private static Logger logger = Logger.getLogger(ViewProjectPDF.class) ;
	private static int size = 0; 
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
					data[row][col] = formBean.getFilter()[i].toString();
				}
				col+=1;
				data[row][col] = report.getDonors().toString().replace('[',' ').replace(']',' ');
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getTitle();
				logger.info(data[row][col].toString().length() + ":    :" + data[row][col].toString());
				calculateSize(data[row][col].toString());
				
				col+=1;
				data[row][col] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getStartDate();
				col+=1;
				data[row][col] = report.getCloseDate();
				col+=1;
				data[row][col] = report.getLevel();
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getStatus();
				calculateSize(data[row][col].toString());
				col+=1;
				data[row][col] = report.getAcCommitment();
				col+=1;
				data[row][col] = report.getAcDisbursement();
				col+=1;
				data[row][col] = report.getAcUnDisbursement();

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
		
		
		
		logger.info("Final Size : " + size);
		logger.info( "  Max Text Size is : *****  :" + size );
		int height = (( size / 25 ) * 10 ) + 90;
		logger.info(" Column Height = " + height );

		
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
			
			ViewProjectDatasource dataSource = new ViewProjectDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
						  "/WEB-INF/lib/jasperreports-0.6.1.jar");
		 	System.setProperty("jasper.reports.compile.class.path",jarFile);
		 	String realPathJrxml = s.getServletContext().getRealPath(
			 					"/WEB-INF/classes/org/digijava/module/aim/reports/viewProjectPdf.jrxml");
		 	viewProjectsJrxml jrxml = new viewProjectsJrxml();
		 	logger.info( " RowCnt =  " + rowCnt);
		 	jrxml.createJrxml(rowCnt, realPathJrxml);
		 	JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/viewProjectPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				logger.info("Exception from ViewProjectPDF = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=viewProjectPdf.pdf");
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
	
	void calculateSize(String input)
	{
		if(input.length() > size)
			size = input.length();
	}
}
