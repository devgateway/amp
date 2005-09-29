
/*
 * AdvancedReportPDF.java 
 * @Author Ronald & Gopinath 
 * Created: 23-September-2005
 * THis Action Class is for Generating PDF report for report created by Report Builder.
 * 
 */

package org.digijava.module.aim.action;	

import java.util.*;

import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.apache.log4j.Logger ;
import org.digijava.module.aim.helper.AdvancedReport;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Column;
import org.digijava.module.aim.helper.AdvancedReportPdfJrxml;
import org.digijava.module.aim.helper.AdvancedReportDatasource ;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ReportSelectionCriteria;
import org.digijava.module.aim.util.ReportUtil;

public class AdvancedReportPDF extends Action 
{

	private static Logger logger = Logger.getLogger(AdvancedReportPDF.class) ;
	private static int fieldHeight = 0; 
	private static String columnDetails [][] = null;
	public ActionForward execute(ActionMapping mapping,
									ActionForm form,
									javax.servlet.http.HttpServletRequest request,
									javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception
	{
		AdvancedReportForm formBean = (AdvancedReportForm) form;
		logger.info("IN PDF generation of Advanced Report........" + formBean.getReportName().replaceAll(" ", ""));
		Collection reportColl = new ArrayList();
		Collection columnColl = new ArrayList();
		Iterator iter = null, funds = null, yearIter;
		Long ampReportId=null;
		int ind = 0, position = 0, flag = 0;
		boolean undisbFlag = false;
		
		if (formBean != null) {
			logger.info("formBean is not null");
			reportColl = formBean.getAllReports();
		} else {
			logger.info("formbean is null");
		}
	
		if(reportColl != null)
		{
			if (reportColl.size() == 0) {
				logger.info("collection is empty");
			} else {
				logger.info("collection is not empty");
				iter = reportColl.iterator();
			}
		}
		else
		logger.info("Collection is NULL..........." + reportColl.size());
		logger.info("Final Data : " + formBean.getAllReports().size());
		logger.info("Report ID : " + formBean.getCreatedReportId());

		// ReportSelectionCriteria.getColumns() - helper.Columns && ReportSelectionCriteria.getMeasures() - id(Long)
		ReportSelectionCriteria rsc=ReportUtil.getReportSelectionCriteria(new Long(formBean.getCreatedReportId()));
		logger.info("Column Size : " + rsc.getColumns().size());
		logger.info("Measusure Size : " + rsc.getMeasures().size());
		iter = rsc.getColumns().iterator();
		
		columnDetails = new String[rsc.getColumns().size() + 3][2];

		columnDetails[0][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
		columnDetails[0][1] = new String(""+0);
		columnDetails[1][0] = formBean.getFilter()[0];
		columnDetails[1][1] = new String(""+1);
		columnDetails[2][0] = formBean.getFilter()[1];
		columnDetails[2][1] = new String(""+2);
		
		ind = 3;
		while(iter.hasNext())
		{
			Column column = (Column)iter.next();
			columnDetails[ind][0] = column.getColumnName();
			columnDetails[ind][1] = new String(""+ind);
			columnColl.add(column.getColumnName());
			logger.info(columnDetails[ind][1] + "*************" +columnDetails[ind][0]);
			ind++;
		}

		iter = columnColl.iterator();
		while(iter.hasNext())
		{
			//Column column = (Column)iter.next();
			//logger.info("ColumnColl : " + column.getColumnName());
			logger.info("ColumnColl : " + iter.next());
		}
		
		/*
		 * DataArray[][] contains data for Reports
		 * The report contains Selected columns, Year, and Measure Selected. 
		 */
		
		ind = 0;
		if(formBean.getAcBalFlag().equals("true"))
		{
			ind = ( (rsc.getMeasures().size() - 1) * formBean.getFiscalYearRange().size() ) 
			+ (formBean.getFiscalYearRange().size() + 1) +  rsc.getMeasures().size();
		}
		else
		{
			ind = rsc.getMeasures().size() * (formBean.getFiscalYearRange().size() + 1)
			+ formBean.getFiscalYearRange().size() + 1;
		}
			
		Object dataArray[][] = new Object[formBean.getAllReports().size()+ 1]
		     [columnDetails.length + ind];

		String rowData[] = new String[columnDetails.length + ind - 3];
		
		int row = 0, year = 0, index = 0;
		Integer yearValue = null;
		
		/* 
		 * Code inserts the LABELS selected columns, year value followed by the selected Measures
		 * at row 0 in the DataArray[][].
		 */
		index = 0;
		for(int j=3; j<columnDetails.length; j++)
		{
			rowData[index] = columnDetails[j][0];
			index = index + 1;
		}
		yearIter = formBean.getFiscalYearRange().iterator();
		if(yearIter.hasNext()){
			yearValue = (Integer) yearIter.next();
		}
		year = yearValue.intValue();
		ind = 0;
		Object temp =new Object();
		for(int i=0; i< formBean.getFiscalYearRange().size() + 1; i++)
		{
			
			ind = ind + 1;
			if(ind > formBean.getFiscalYearRange().size())
				rowData[index++] = "Total";
			else
				rowData[index++]  = new String("" +year);
			
			if(formBean.getAcCommFlag().equals("true" ))
				rowData[index++]  =  "Actual Commitment";
			if(formBean.getAcDisbFlag().equals("true") )
				rowData[index++]  = "Actual Disbursements";
			if(formBean.getAcExpFlag().equals("true") )
				rowData[index++]  = "Actual Expenditures";
			if(formBean.getPlCommFlag().equals("true") )
				rowData[index++]  = "Planned Commitments";
			if(formBean.getPlDisbFlag().equals("true") )
				rowData[index++]  = "Planned Disbursements";
			if(formBean.getPlExpFlag().equals("true") )
				rowData[index++]  = "Planned Expenditures";
			if(formBean.getAcBalFlag().equals("true") )
			{
				if(ind > formBean.getFiscalYearRange().size())
				{
					rowData[index++]  = "Undisbursed";
					undisbFlag = true;
				}
				
			}
			year = year + 1;
			
		}
	
		for(int i=0; i<rowData.length; i++)
		{
			logger.info("Rows Labels : " + rowData[i]);
		}
		logger.info("Column Size : " + columnColl.size());
		Iterator  dataIter = formBean.getAllReports().iterator();
		
		while(dataIter.hasNext())
		{
			Report report = (Report) dataIter.next();
			if(report.getRecords() != null)
			{
				Iterator reportIter = report.getRecords().iterator();
				while(reportIter.hasNext())
				{
					dataArray[row][0] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
					
					String filterName[] = new String[2];
					position = 1;
					filterName = formBean.getFilter();
					for(int i=0; i<filterName.length; i++)
					{	
						dataArray[row][position] = filterName[i];
						position = position + 1;						
					}

					AdvancedReport advReport = (AdvancedReport) reportIter.next();
					flag = 1;
					if(columnColl.contains("Status") == true && advReport.getStatus() != null )
					{
						position = getColumnIndex("Status");
						if(advReport.getStatus().replaceAll(",", "").length() > 2)
							dataArray[row][position] = advReport.getStatus().replaceAll(",", "");
					
						//logger.info(advReport.getStatus().replaceAll(",", "").length() + " : Status : " + advReport.getStatus().replaceAll(",", ""));
					}
					if(columnColl.contains("Activity Name") && advReport.getTitle() != null)
					{
						position = getColumnIndex("Activity Name");
						if(advReport.getTitle().trim().length() > 0)
							dataArray[row][position] = advReport.getTitle().trim();
							
							//logger.info("Name : " + advReport.getTitle());
					}
					if(columnColl.contains("Actual Start Date") && advReport.getActualStartDate() != null)
					{
						position = getColumnIndex("Actual Start Date");
						if(advReport.getActualStartDate().trim().length() > 0)
							dataArray[row][position] = advReport.getActualStartDate().trim();
							
							//logger.info("Start Date : " + advReport.getActualStartDate());
					}
					if(columnColl.contains("Actual Completion Date") && advReport.getActualCompletionDate() != null)
					{
						position = getColumnIndex("Actual Completion Date");
						if(advReport.getActualCompletionDate().trim().length() > 0)
							dataArray[row][position] = advReport.getActualCompletionDate().trim();
							
							//logger.info("Completion Date : " + advReport.getActualCompletionDate());
					}					

					if(columnColl.contains("Level") && advReport.getLevel() != null)
					{
						position = getColumnIndex("Level");
						if(advReport.getLevel().trim().length() > 0)
							dataArray[row][position] = advReport.getLevel().trim(); 
							
							//logger.info("Level : " + advReport.getLevel());
					}
					
					if(columnColl.contains("Description") && advReport.getDescription() != null)
					{
						position = getColumnIndex("Description");
						if(advReport.getDescription().trim().length() > 0)
							dataArray[row][position] = advReport.getDescription().trim(); 
							
							//logger.info("Description : " + advReport.getDescription());
					}
					
					if(columnColl.contains("Objective") && advReport.getObjective() != null)
					{
						position = getColumnIndex("Objective");
						if(advReport.getObjective().trim().length() > 0)
							dataArray[row][position] = advReport.getObjective().trim();
							
							//logger.info("Objective : " + advReport.getObjective());
					}					

					if(columnColl.contains("Type Of Assistance") && advReport.getAssistance() != null)
					{
						position = getColumnIndex("Type Of Assistance");
						if(advReport.getAssistance().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getAssistance().toString().replace('[',' ').replace(']',' ').trim();
							
							//logger.info("TOA : " + advReport.getAssistance().toString().replace('[',' ').replace(']',' '));
					}
					
					if(columnColl.contains("Donor") && advReport.getDonors() != null)
					{
						position = getColumnIndex("Donor");
						if(advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim();
							
							//logger.info(advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim().length() + " : Donor : " + advReport.getDonors().toString().replace('[',' ').replace(']',' '));
					}					

					if(columnColl.contains("Sector"))
					{
						if(advReport.getSectors() != null)
						{
							position = getColumnIndex("Sector");
							if(advReport.getSectors().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
								dataArray[row][position] = advReport.getSectors().toString().replace('[',' ').replace(']',' ').trim();
						}
					}					
					if(columnColl.contains("Region") && advReport.getRegions() != null)
					{
						position = getColumnIndex("Region");
						if(advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim();
							
							logger.info(advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim().length() + " : Region : " );
					}
					else
						logger.info("Region is NULL..................");
					
					if(columnColl.contains("Contact Name") && advReport.getContacts() != null)
					{
						position = getColumnIndex("Contact Name");
						logger.info(position + "::: CONTACT NAME");
						if(advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim();
							
							//logger.info("Contact  : " + advReport.getContacts().toString().replace('[',' ').replace(']',' '));
					}
					
					if(columnColl.contains("Funding Instrument") && advReport.getModality() != null)
					{
						position = getColumnIndex("Funding Instrument");
						if(advReport.getModality().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getModality().toString().replace('[',' ').replace(']',' ').trim();
							
							//logger.info(advReport.getModality().toString().replace('[',' ').replace(']',' ').trim().length() + " : Modality : " + advReport.getModality().toString().replace('[',' ').replace(']',' '));
					}
					
					if(columnColl.contains("Project Id") && advReport.getAmpId() != null)
					{
						position = getColumnIndex("Project Id");
							if(advReport.getAmpId().trim().length() > 0 )
								dataArray[row][position] = advReport.getAmpId().trim();
								
							//logger.info("ID  : " + advReport.getAmpId());
					}					
					
					position = 3 + rsc.getColumns().size()-1;
					logger.info("---------------"+ position);
					
					
					if(advReport.getAmpFund() != null)
					{
						funds = advReport.getAmpFund().iterator();
						yearIter = formBean.getFiscalYearRange().iterator();
						if(yearIter.hasNext()){
							yearValue = (Integer) yearIter.next();
						}
						year = yearValue.intValue();
						
						ind = 0;
						while(funds.hasNext())
						{
							
							
							ind = ind + 1;
							if(ind > formBean.getFiscalYearRange().size())
							{
								position = position + 1;
								dataArray[row][position] = new String(" Total ");
							}
							else
							{
								position = position + 1;
								dataArray[row][position] = new String(""+year);
							}

							AmpFund ampFund = (AmpFund) funds.next();
							if(formBean.getAcCommFlag().equals("true") && ampFund.getCommAmount() != null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getCommAmount();
							}
							if(formBean.getAcDisbFlag().equals("true") && ampFund.getDisbAmount() != null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getDisbAmount();
							}
							if(formBean.getAcExpFlag().equals("true") && ampFund.getExpAmount() != null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getExpAmount();
							}

							if(formBean.getPlCommFlag().equals("true") && ampFund.getPlCommAmount() != null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getPlCommAmount();
							}
							if(formBean.getPlDisbFlag().equals("true") && ampFund.getPlDisbAmount()!= null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getPlDisbAmount();
							}
							if(formBean.getPlExpFlag().equals("true") && ampFund.getPlExpAmount()!= null)
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getPlExpAmount();
							}

							if(formBean.getAcBalFlag().equals("true") && ampFund.getUnDisbAmount() != null 
									&& ind > formBean.getFiscalYearRange().size() )
							{
								position = position + 1;
								dataArray[row][position] = ampFund.getUnDisbAmount();
								undisbFlag = true;
							}
							
							year = year + 1;
							
						} // END Of AmpFund Iteration
					}
				} // End of Records Iteration
				row = row + 1;
			}
		}// End of ALlReport() Iteration
	
		logger.info(dataArray.length + " : FINAL DATA :" + dataArray[0].length);
		for(int i=0; i< dataArray.length; i++)
		{
			for(int j=0; j< dataArray[0].length; j++)
			{
				if(dataArray[i][j] == null)
				{
					dataArray[i][j] = "";
				}
			}
		}

		if(flag == 1)
		{

			for(int i=0; i < dataArray.length; i++)
			{
				for(int j=0; j < dataArray[0].length; j++)
				{
					if(dataArray[i][j] != null)
					{
						if(dataArray[i][j].equals("0")){
							dataArray[i][j] = "";
						}
					}
						
				}
			}

			AdvancedReportDatasource dataSource = new AdvancedReportDatasource(dataArray,rsc.getColumns().size(), rowData);
			
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								  "/WEB-INF/lib/jasperreports-0.6.8.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
								 "/WEB-INF/classes/org/digijava/module/aim/reports");
			realPathJrxml = realPathJrxml + "\\" + formBean.getReportName().replaceAll(" ", "").replaceAll("#", "")+".jrxml";
			logger.info("Path : " + realPathJrxml);

			//calling dynamic jrxml
			AdvancedReportPdfJrxml jrxml = new AdvancedReportPdfJrxml();
			jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), 
					formBean.getReportName().replaceAll(" ", "").replaceAll("#", ""));
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
						"/WEB-INF/classes/org/digijava/module/aim/reports"+"\\"+formBean.getReportName().replaceAll(" ", "").replaceAll("#", "")+".jasper");
				logger.info("Jasper FIle ::::::::::::::::;" + jasperFile);
				
				
				Map parameters = new HashMap();
				System.out.println(jasperFile );
				System.out.println(parameters);
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				System.out.println("Exception from MultilateralDonorDatasource = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				System.out.println("Generating PDF");
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=MultilateralDonorPdf.pdf");
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
	} // end of Execute
	
	int getColumnIndex(String str)
	{
		int i = 0, pos = 0;
		for(i=0; i < columnDetails.length; i++)
		{
			if(columnDetails[i][0].equals(str))
			{
				pos = Integer.parseInt(columnDetails[i][1]);
				break;
			}
		}
		return pos;
	}
	
}// end of CLass

