package org.digijava.module.aim.action;	

import java.util.*;

import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.apache.log4j.Logger ;
import org.digijava.module.aim.helper.AdvancedReport;
import org.digijava.module.aim.helper.AdvancedReportQtrlyJrxml;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Column;
import org.digijava.module.aim.helper.AdvancedReportPdfJrxml;
import org.digijava.module.aim.helper.AdvancedReportDatasource ;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ReportSelectionCriteria;
import org.digijava.module.aim.util.ReportUtil;
import org.digijava.module.aim.helper.multiReport;
import org.digijava.module.aim.helper.AdvancedHierarchyReport;

public class AdvancedReportPDF extends Action 
{
	
	Object dataArray[][];
	Collection columnColl;
	AdvancedReport advReport;
	int flag = 0;
	
	AdvancedReportForm formBean;
	ReportSelectionCriteria rsc;
	AdvancedHierarchyReport ahr,ahr2,ahr3;
	
	private static Logger logger = Logger.getLogger(AdvancedReportPDF.class) ;
	private static int fieldHeight = 0; 
	private static String columnDetails [][];
	public ActionForward execute(ActionMapping mapping,
									ActionForm form,
									javax.servlet.http.HttpServletRequest request,
									javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception
	{
	
		try {
			
		
		formBean = (AdvancedReportForm) form;
		//formBean = (AdvancedReportForm) form;
		//logger.info("IN PDF generation of Advanced Report........" + formBean.getReportName());
		
		if (formBean.getHierarchyFlag()=="true" && formBean.getColumnHierarchie() != null) {
			logger.info("PDF with HIERARCHYYYYYYYY..."+formBean.getColumnHierarchie().size());
		} else {
			logger.info("PDF with NOOOOOOO HIERARCHYYYYYYYY...");
		}
		
		Collection reportColl = new ArrayList();
		columnColl = new ArrayList();
		Iterator iter = null, funds = null, yearIter;
		Long ampReportId=null;
		int ind = 0, position = 0;
		boolean undisbFlag = false;
		boolean qtrlyFlag = false;

		if(formBean.getOption()!=null && formBean.getOption().equals("Q")){
			qtrlyFlag=true;
			logger.info(" Option= "+formBean.getOption());
			logger.info(" QuarterlyColumns= "+formBean.getQuarterColumns());
		}
		else
			logger.info("#### JRXML without QQQQQQQQQQQQQQ ########");

		if (formBean != null) {
			logger.info("formBean is not null");
			reportColl = formBean.getAllReports();
		} else {
			logger.info("formbean is null");
		}
		
		/*if(reportColl != null)
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
		*/
		logger.info("All Report Size : " + formBean.getAllReports().size());
		logger.info("Report ID : " + formBean.getCreatedReportId());

		// ReportSelectionCriteria.getColumns() - helper.Columns && ReportSelectionCriteria.getMeasures() - id(Long)
		rsc=ReportUtil.getReportSelectionCriteria(new Long(formBean.getCreatedReportId()));
		logger.info("Column Size : " + rsc.getColumns().size());
		logger.info("Measusure Size : " + rsc.getMeasures().size());
		iter = rsc.getColumns().iterator();
		
		int row = 0, year = 0, index = 0;
		Integer yearValue = null;
		//Object dataArray[][];
		int n=3;
		int totalcnt=1;
		if (formBean.getHierarchyFlag()=="true" && formBean.getColumnHierarchie() != null)
			n+=formBean.getColumnHierarchie().size();
		columnDetails = new String[rsc.getColumns().size() + n][2];

		columnDetails[0][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
		columnDetails[0][1] = new String(""+0);
		columnDetails[1][0] = formBean.getFilter()[0];
		columnDetails[1][1] = new String(""+1);
		columnDetails[2][0] = formBean.getFilter()[1];
		columnDetails[2][1] = new String(""+2);
		
		ind = 3+formBean.getColumnHierarchie().size();
		while(iter.hasNext())
		{
			Column column = (Column)iter.next();
			columnDetails[ind][0] = column.getColumnName();
			columnDetails[ind][1] = new String(""+ind);
			columnColl.add(column.getColumnName());
			logger.info(columnDetails[ind][1] + "************* >>>" +columnDetails[ind][0]);
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
			ind = ( (rsc.getMeasures().size() - 1) * formBean.getFiscalYearRange().size() ) + (formBean.getFiscalYearRange().size() + 1) +  rsc.getMeasures().size();
		}
		else
		{
			if(qtrlyFlag){
				logger.info("@@@@@@@@@@@@@Quarterly dataarray...");
				ind = rsc.getMeasures().size() * (formBean.getFiscalYearRange().size()*4 + 1)+ formBean.getFiscalYearRange().size() + 1;
				//(formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()*4
			}
			else
				ind = rsc.getMeasures().size() * (formBean.getFiscalYearRange().size() + 1)+ formBean.getFiscalYearRange().size() + 1;
		}
		
		if(formBean.getOption()!=null && formBean.getOption().equals("Q")){
			ind+=rsc.getMeasures().size()*4;
			logger.info("indddddddddd: "+ind);
		}
		
		//Object dataArray[][];
		if (formBean.getColumnHierarchie() != null && formBean.getColumnHierarchie().size()>0) {
			int i=0;
			Iterator one=formBean.getAllReports().iterator();
			while(one.hasNext()){
				multiReport mr =(multiReport)one.next();
				Iterator two=mr.getHierarchy().iterator();
				while(two.hasNext()){
					ahr=(AdvancedHierarchyReport)two.next();
					//Level 1
					logger.info("project Size:::: "+ahr.getProject().size());
					if(ahr.getLevels()!=null && ahr.getLevels().size()>0){
						logger.info("project Levels:::: "+ahr.getLevels().size());
						Iterator three = ahr.getLevels().iterator();
						while(three.hasNext()){
							//logger.info("**********"+three.next().getClass().getName());
							ahr2=(AdvancedHierarchyReport)three.next();
							//Level 2
							logger.info("project Size---"+ahr2.getProject().size());
							if(ahr2.getLevels()!=null && ahr2.getLevels().size()>0){
								logger.info("project Levels---"+ahr2.getLevels().size());
								Iterator four=ahr2.getLevels().iterator();
								while(four.hasNext()){
									ahr3= (AdvancedHierarchyReport) four.next();
									//logger.info("project Levels 3333"+ahr2.getLevels().size());
									logger.info("project Size 3333="+ahr3.getProject().size());
									i+=ahr3.getProject().size();
									totalcnt++;
									logger.info("iiiiiiiiiiiiiiii== "+i+" h3 ==== "+ahr3.getName()+" ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);									
								}//four
								//totalcnt++;
								//logger.info("iiiiiiiiiiiiiiii== "+i+" h3 ==== "+ahr2.getName()+" ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);
							}
							i+=ahr2.getProject().size();
							totalcnt++;
							logger.info("iiiiiiiiiiiiiiii== "+i+" h2 ==== "+ahr2.getName()+" ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);
						}//three
						//totalcnt++;
						//logger.info("iiiiiiiiiiiiiiii== "+i+" h2 ==== "+ahr.getName()+" ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);
					}
					i+=ahr.getProject().size();
					totalcnt++;
					logger.info("iiiiiiiiiiiiiiii== "+i+" h1 ==== "+ahr.getName()+" ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);
				}//two
				//totalcnt++;
				//logger.info("iiiiiiiiiiiiiiii== "+i+" h1 ==== "+formBean.getColumnHierarchie().size()+"totalcnt="+totalcnt);
			}//one
			
			
			logger.info("::::::::: -ind= "+ind+" -columnDetails.lenght= "+columnDetails.length+" -col hierarchy= "+formBean.getColumnHierarchie().size());
			//dataArray = new Object[i+1][columnDetails.length + ind+ formBean.getColumnHierarchie().size()];
			logger.info("---------TOTAL COUNT = "+totalcnt);
			i+=totalcnt*rsc.getHierarchy().size();
			logger.info("---------TOTAL COUNT with HRRCHY= "+totalcnt*rsc.getHierarchy().size()+"iiiiiiiiiiiiiiii="+i);
			dataArray = new Object[i+1][columnDetails.length + (formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()*4+ formBean.getColumnHierarchie().size()];
			logger.info("****dataArray size with H= "+(i+1)+" :"+(columnDetails.length + (formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()*4+ formBean.getColumnHierarchie().size()));
		}
		else{
			dataArray = new Object[formBean.getAllReports().size()+ 1+rsc.getHierarchy().size()*totalcnt][columnDetails.length + ind];
			logger.info("****dataArray size no H= "+(formBean.getAllReports().size()+ 1)+" :"+(columnDetails.length + ind));
		}		

//		String rowData[] = new String[columnDetails.length + ind - n];
		String rowData[];
		
		if(qtrlyFlag){
			if(undisbFlag)
				rowData = new String[(columnDetails.length-4) + (formBean.getFiscalYearRange().size()+1)*(rsc.getMeasures().size()-1)*4+5];
			else
				rowData = new String[(columnDetails.length-4) + (formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()*4+5];
		}
		else{
			if(undisbFlag)
				rowData = new String[(columnDetails.length) + (formBean.getFiscalYearRange().size()+1)*(rsc.getMeasures().size()-1)+5];
			else
				rowData = new String[(columnDetails.length) + (formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()+5];
		}
		//rowData = new String[(columnDetails.length-4) + (formBean.getFiscalYearRange().size()+1)*rsc.getMeasures().size()*4];
		logger.info("...............row Data.........="+rowData.length+" ----n="+n);

		
		/* 
		 * Code inserts the LABELS selected columns, year value followed by the selected Measures
		 * at row 0 in the DataArray[][].
		 */
		int j2=3;
		if(formBean.getHierarchyFlag()=="true"){
			j2+=formBean.getColumnHierarchie().size();
		}
		logger.info("jjjjjjjjjjjjjjj-j= "+j2);
		
		index = 0;
		for(int j=j2; j<columnDetails.length; j++)
		{
			rowData[index] = columnDetails[j][0];
			logger.info("row dataaaaaaaaa: "+rowData[index]+" : "+index);
			index++;
		}
		yearIter = formBean.getFiscalYearRange().iterator();
		if(yearIter.hasNext()){
			yearValue = (Integer) yearIter.next();
		}
		year = yearValue.intValue();
		ind = 0;
		Object temp =new Object();
		logger.info("i-i-i-i-i-i-i-i-i-i= "+index);
		
		for(int i=0; i<=formBean.getFiscalYearRange().size(); i++)
		{
			logger.info("INDDDDD====="+ind+":::"+"---INDEX===== "+index);
					
			if(qtrlyFlag)
			{
				logger.info("condition one....."+index);
				for(int j=0;j<4;j++)
				{
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
				}//for
			}
			else
			{
				logger.info("condition TWO....."+index);
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
			}//end else
			
			ind++;
			
		}//for
	
		for(int i=0; i<rowData.length; i++)
		{
			logger.info("Rows Labels : i= " + i +" -- "+rowData[i]);
		}
		logger.info("Column Size : " + columnColl.size());
		Iterator  dataIter = formBean.getAllReports().iterator();
		
		while(dataIter.hasNext())
		{
			Object obj = dataIter.next();
			if (obj instanceof Report) {
			logger.info("H000000000000000000000000000000000000000000000000000000");
			Report report = (Report) obj;
			
			int tot=0;
			if(report.getRecords() != null)
			{
				tot++;
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
					if(columnColl.contains("Project Title") && advReport.getTitle() != null)
					{
						position = getColumnIndex("Project Title");
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

					if(columnColl.contains("Implementation Level") && advReport.getLevel() != null)
					{
						position = getColumnIndex("Implementation Level");
						if(advReport.getLevel().trim().length() > 0)
							dataArray[row][position] = advReport.getLevel().trim(); 
							
							//logger.info("Level : " + advReport.getLevel());
					}
					
					if(columnColl.contains("Description") && advReport.getDescriptionPDFXLS() != null)
					{
						position = getColumnIndex("Description");
						if(advReport.getDescriptionPDFXLS().trim().length() > 0)
							dataArray[row][position] = advReport.getDescriptionPDFXLS().trim(); 
							
							//logger.info("Description : " + advReport.getDescription());
					}
					
					if(columnColl.contains("Objective") && advReport.getObjectivePDFXLS() != null)
					{
						position = getColumnIndex("Objective");
						if(advReport.getObjectivePDFXLS().trim().length() > 0)
							dataArray[row][position] = advReport.getObjectivePDFXLS().trim();
							
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
						//logger.info(position + "::: CONTACT NAME");
						if(advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
							dataArray[row][position] = advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim();
							
							//logger.info("Contact  : " + advReport.getContacts().toString().replace('[',' ').replace(']',' '));
					}
					
					if(columnColl.contains("Financing Instrument") && advReport.getModality() != null)
					{
						position = getColumnIndex("Financing Instrument");
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
					logger.info("------------------------------------------"+ position);
					
					
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
							
							logger.info(" WITHOUT HRRCHY IND intrrrrrrrrrrrrr "+ind);
							ind ++;
							
							/*//ind = ind + 1;
							if(ind > formBean.getFiscalYearRange().size())
							{
								logger.info(" filling total...");
								position = position + 1;
								dataArray[row][position] = new String(" Total ");
							}*/
							/*else
							{
								position = position + 1;
								dataArray[row][position] = new String(""+year);
							}*/
							
							/*if(qtrlyFlag)
							{
								if(ind > (formBean.getFiscalYearRange().size()*4))
								{
									position = position + 1;

									dataArray[row][position] = new String(" Total ");
								}
								else
								{
									if((ind%4)==1)
									{	
										position = position + 1;
										dataArray[row][position] = new String(""+year);
										year = year + 1;
									}
								}
							}
							else
							{
								if(ind > formBean.getFiscalYearRange().size())
								{
									position = position + 1;
									dataArray[row][position] = new String(" Total ");
								}
								else
								{
										position = position + 1;
										dataArray[row][position] = new String(""+year);
										year = year + 1;
								}
							}	
*/
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
							
							//year = year + 1;
							
						} // END Of AmpFund Iteration
					}
				} // End of Records Iteration
				
				logger.info("+++++ Row ++-->"+ row);
				row++;
				
				logger.info("TOOOOOOOOOOT="+tot);
				if(tot==report.getRecords().size()){
					
					logger.info("::::::::::::::::::filling TOTALS NOW::::::::k=");
					row++;
					
				}//total if
				
			}
			}
			else if (obj instanceof multiReport) {
				row=0;
				logger.info("#################### HIERARCHY DATA MANIPULATION.....");
				multiReport mltireport = (multiReport) obj;
				if(mltireport.getHierarchy()!=null && formBean.getColumnHierarchie().size()>0){
					Iterator itrh=mltireport.getHierarchy().iterator();
					
					//more than ONE Level hierarchy..
					while(itrh.hasNext()){
						//AdvancedHierarchyReport ahr=(AdvancedHierarchyReport) itrh.next();
						ahr=(AdvancedHierarchyReport) itrh.next();
						
						// more than TWO Level hierarchy..
						if(ahr.getLevels()!=null && ahr.getLevels().size()>0){
							logger.info("project Levels-2-2-2-"+ahr.getLevels().size());
							Iterator three = ahr.getLevels().iterator();
							while(three.hasNext()){
								//logger.info("**********"+three.next().getClass().getName());
								ahr2=(AdvancedHierarchyReport)three.next();
								logger.info("project Size-2-2-2-"+ahr2.getProject().size());
								
								// for THREE Level Hierarchy
								if(ahr2.getLevels()!=null && ahr2.getLevels().size()>0){
									logger.info("H3333333333333333333333333333333333333333333");
									logger.info("project Levels-3-3-3-"+ahr2.getLevels().size());
									Iterator four=ahr2.getLevels().iterator();
									while(four.hasNext()){
										ahr3=(AdvancedHierarchyReport) four.next();
										logger.info("********* HRRCHY 33333:::"+ahr3.getLabel()+" ::: "+ahr3.getName());
										
										Iterator itrr=ahr3.getProject().iterator();
										int j=0,k=0;
										while(itrr.hasNext()){
											logger.info("roww3w3w3w3w3w3w3w3w3:"+row+"  - ITRRRRR No."+ ++j+"::row:"+row);

											//Object obj1=(Object)itrr.next();								
												Report report=(Report) itrr.next();
												
												logger.info("record sizez3z3z3z3z:::"+report.getRecords().size());
												if(report.getRecords() != null)
												{
													logger.info("flagg3g3g3:"+ ++k);
													Iterator reportIter = report.getRecords().iterator();
													while(reportIter.hasNext())
													{
														dataArray[row][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
														//logger.info("@@@@@@@@ WorkspaceName="+formBean.getWorkspaceName());
														String filterName[] = new String[2];
														position = 1;
														filterName = formBean.getFilter();
														for(int i=0; i<filterName.length; i++)
														{	
															dataArray[row][position] = filterName[i];
															position = position + 1;						
														}
														
														dataArray[row][position] = ahr.getLabel()+" - "+ahr.getName();
														logger.info("Filling ahr.name H1------->"+ahr.getLabel());
														position++;	
														dataArray[row][position] = ahr2.getLabel()+" - "+ahr2.getName();
														logger.info("ing ahr.name H2------->"+ahr2.getLabel());
														position++;
														dataArray[row][position] = ahr3.getLabel()+" - "+ahr3.getName();
														logger.info("Filling ahr.name H3------->"+ahr3.getLabel());
														
														
														getHierarchyData(row, position,reportIter);
														
														position = n + rsc.getColumns().size()-1;
														logger.info("#######################################################"+ position);
														
														
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
																/*ind = ind + 1;
																if(ind > formBean.getFiscalYearRange().size())
																{
																	position = position + 1;
																	dataArray[row][position] = new String(" Total ");
																}
																else
																{
																	position = position + 1;
																	dataArray[row][position] = new String(""+year);
																}*/

																logger.info(" IND intrrrrrrrrrrrrr "+ind);
																ind ++;
																if(qtrlyFlag)
																{
																	if(ind > (formBean.getFiscalYearRange().size()*4))
																	{
																		position = position + 1;
																		dataArray[row][position] = new String(" Total ");
																	}
																	else
																	{
																		if((ind%4)==1)
																		{	
																			position = position + 1;
																			dataArray[row][position] = new String(""+year);
																			year = year + 1;
																		}
																	}
																}
																else
																{
																	if(ind > formBean.getFiscalYearRange().size())
																	{
																		position = position + 1;
																		dataArray[row][position] = new String(" Total ");
																	}
																	else
																	{
																			position = position + 1;
																			dataArray[row][position] = new String(""+year);
																			year = year + 1;
																	}
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
																
																//year = year + 1;																
															}
														} // END Of AmpFund Iteration
														
														
														
													} // End of Records Iteration
													
													logger.info("&&&& Row &&&-->"+ row);
													row = row + 1;
											}//if
												
												/*if(k==report.getRecords().size()){	
													logger.info("::::::::::::::::::filling TOTALS::::::::k="+k);
													dataArray[row][0] = "TOTAL for" +ahr3.getName();
													position = rsc.getColumns().size()+1;
													Iterator itrtot= ahr3.getFundSubTotal().iterator();
													while(itrtot.hasNext()){
														AmpFund ampFund=(AmpFund)itrtot.next();
														if(formBean.getAcCommFlag().equals("true") && ampFund.getCommAmount()!= null){
															dataArray[row][position++] = ampFund.getCommAmount();
															logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
														}
														if(formBean.getAcDisbFlag().equals("true") && ampFund.getCommAmount()!= null){
															dataArray[row][position++] = ampFund.getDisbAmount();
															logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
														}
														if(formBean.getAcExpFlag().equals("true") && ampFund.getCommAmount()!= null){
															dataArray[row][position++] = ampFund.getExpAmount();
															logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
														}
														//position++;
													}
													row++;
												}*/
												
										}//while itrr3
																			
									}//while itrr4
									
									fillTotal(row, position, ahr,ahr2,ahr3,3);
									row++;
								}
								else{
								logger.info("==========H22222222222222222222222222222222222222222=============\n" +
										"---------------------------------------------");									
								logger.info("********* HRRCHY:::"+ahr2.getLabel()+" ::: "+ahr2.getName());
								Iterator itrr=ahr2.getProject().iterator();
								logger.info("|=|=|=|=|=|=| Total No. of activities..."+ahr2.getProject().size());
								int j=0,k=0;
								
						while(itrr.hasNext()){
							
							logger.info("rowwwwwwwwww:"+row+"  - ITRRRRR No."+ ++j);
							//Object obj1=(Object)itrr.next();								
								Report report=(Report) itrr.next();
								logger.info("record sizezzzzz:::"+report.getRecords().size());
								if(report.getRecords() != null)
								{
									logger.info("flagggg:"+ ++k);
									Iterator reportIter = report.getRecords().iterator();
									while(reportIter.hasNext())
									{
										dataArray[row][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
										//logger.info("@@@@@@@@ WorkspaceName="+formBean.getWorkspaceName());
										String filterName[] = new String[2];
										position = 1;
										filterName = formBean.getFilter();
										for(int i=0; i<filterName.length; i++)
										{	
											dataArray[row][position] = filterName[i];
											position = position + 1;						
										}
										
										dataArray[row][position] = ahr.getLabel()+" - "+ahr.getName();
										logger.info("Filling ahr.name H1------->"+ahr.getLabel()+"-"+ahr.getName());
										position++;	
										dataArray[row][position] = ahr2.getLabel()+" - "+ahr2.getName();
										logger.info("Filling ahr.name H2------->"+ahr2.getLabel()+"-"+ahr2.getName());
										position++;	
										
										getHierarchyData(row, position,reportIter);
										
										position = n + rsc.getColumns().size()-1;
										//logger.info("#######################################################"+ position);
										
										
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
												logger.info(" IND intrrrrrrrrrrrrr "+ind);
												ind ++;
												if(qtrlyFlag)
												{
													if(ind > (formBean.getFiscalYearRange().size()*4))
													{
														position = position + 1;
														dataArray[row][position] = new String(" Total ");
													}
													else
													{
														if((ind%4)==1)
														{	
															position = position + 1;
															dataArray[row][position] = new String(""+year);
															year = year + 1;
														}
													}
												}
												else
												{
													if(ind > formBean.getFiscalYearRange().size())
													{
														position = position + 1;
														dataArray[row][position] = new String(" Total ");
													}
													else
													{
															position = position + 1;
															dataArray[row][position] = new String(""+year);
															year = year + 1;
													}
												}	

												AmpFund ampFund = (AmpFund) funds.next();
												if(formBean.getAcCommFlag().equals("true") && ampFund.getCommAmount() != null)
												{
													position = position + 1;
													dataArray[row][position] = ampFund.getCommAmount();
													//logger.info("^^^^ row="+row+"========="+dataArray[row][position]);
												}
												if(formBean.getAcDisbFlag().equals("true") && ampFund.getDisbAmount() != null)
												{
													position = position + 1;
													dataArray[row][position] = ampFund.getDisbAmount();
													//logger.info("^^^^ row="+row+"========="+dataArray[row][position]);
												}
												if(formBean.getAcExpFlag().equals("true") && ampFund.getExpAmount() != null)
												{
													position = position + 1;
													dataArray[row][position] = ampFund.getExpAmount();
													//logger.info("^^^^ row="+row+"========="+dataArray[row][position]);
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
												
												//year = year + 1;
												
											}
										} // END Of AmpFund Iteration
										 logger.info("h222222222222222=====END Of AmpFund Iteration");
									} // End of Records Iteration
									
									logger.info("&&&& Row &&&-->"+ row);
									row = row + 1;
							}
								
						}//while itrr
						
						logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$ total for h2.........."+ahr2.getLabel()+"rowww="+row);
						fillTotal(row, position,ahr,ahr2,ahr3,2);
						row++;
						
							}
								
						//	i+=ahr.getProject().size();
							}
							
							logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$ total for h1.........."+ahr2.getLabel()+"rowww="+row);
							fillTotal(row, position,ahr,ahr2,ahr3,1);
							row++;
							
						}
						else{
							logger.info("H111111111111111111111111111111111111111111111111111111");
							
							Iterator itrr=ahr.getProject().iterator();
							while(itrr.hasNext()){
								//logger.info("rowwwwwwwwww:"+row+"  - ITRRRRR No."+ ++j);
								//Object obj1=(Object)itrr.next();								
									Report report=(Report) itrr.next();
									logger.info("record sizezzzzz:::"+report.getRecords().size());
									int k=0;
									if(report.getRecords() != null)
									{
										Iterator reportIter = report.getRecords().iterator();
										while(reportIter.hasNext())
										{
											logger.info("flagggg:"+ row);
											dataArray[row][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
											//logger.info("@@@@@@@@ WorkspaceName="+formBean.getWorkspaceName());
											String filterName[] = new String[2];
											position = 1;
											filterName = formBean.getFilter();
											for(int i=0; i<filterName.length; i++)
											{	
												dataArray[row][position] = filterName[i];
												position = position + 1;						
											}
											
											dataArray[row][position] = ahr.getLabel()+" - "+ahr.getName();
											logger.info("Filling ahr.name ------->"+ahr.getLabel());
											position++;
											
											getHierarchyData(row, position,reportIter);
											
											position = n + rsc.getColumns().size()-1;
											//logger.info("#######################################################"+ position);
											
											if(advReport.getAmpFund() != null)
											{
												logger.info("================ AMpFUND SIZE:::"+ advReport.getAmpFund().size());
												funds = advReport.getAmpFund().iterator();
												yearIter = formBean.getFiscalYearRange().iterator();
												if(yearIter.hasNext()){
													yearValue = (Integer) yearIter.next();
												}
												year = yearValue.intValue();
												
												ind = 0;
												while(funds.hasNext())
												{	
													//logger.info(" IND intrrrrrrrrrrrrr "+ind);
													ind ++;
													if(qtrlyFlag)
													{
														if(ind > (formBean.getFiscalYearRange().size()*4))
														{
															position = position + 1;
															dataArray[row][position] = new String(" Total ");
														}
														else
														{
															if((ind%4)==1)
															{	
																position = position + 1;
																dataArray[row][position] = new String(""+year);
																year = year + 1;
															}
														}
													}
													else
													{
														if(ind > formBean.getFiscalYearRange().size())
														{
															position = position + 1;
															dataArray[row][position] = new String(" Total ");
														}
														else
														{
																position = position + 1;
																dataArray[row][position] = new String(""+year);
																year = year + 1;
														}
													}	

													AmpFund ampFund = (AmpFund) funds.next();
													
													if(formBean.getAcCommFlag().equals("true") && ampFund.getCommAmount() != null)
													{
														position = position + 1;
														//logger.info("%%%%%%%%%%%%%%%%row= "+row+" position= "+position);
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
													
													//year = year + 1;
													//logger.info("#### Row ###-->"+ row);
												}
											} // END Of AmpFund Iteration
																						
										} // End of Records Iteration
										
										logger.info("&&&& Row &&&-->"+ row+"--k=="+k);
										row++;
								}
							}//end of while
							
							fillTotal(row, position,ahr, ahr2, ahr3,1);
							row++;
						}
						//row++;
						
						if(ahr.getProject()!=null)
						logger.info("=======Total No of activities..."+ahr.getProject().size());
/*						if(ahr.getActivities()!=null)
						//logger.info("^^^^++^^^^"+ahr.getActivities().size());
						if(ahr.getLevels()!=null)
						//logger.info("^^^^--^^^^"+ahr.getLevels().size());
*/					}
					
					
				}

			} else
				logger.info("HIERARCHY DATA ERROR.....");
		}// End of ALlReport() Iteration
	
		logger.info(dataArray.length + " ----------------: FINAL DATA START-------------- :" + dataArray[0].length);
		for(int i=0; i< dataArray.length; i++)
		{
			for(int j=0; j< dataArray[0].length; j++)
			{
				if(dataArray[i][j] == null)
					dataArray[i][j] = "";
				logger.info("i="+i+" j="+j+" "+dataArray[i][j]);
			}
			logger.info("\n");
		}
		logger.info(dataArray.length + " ----------------: FINAL DATA END-------------- :" + dataArray[0].length);

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
			logger.info("EXPORTING NOW......");
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("pdf"))
			{
				AdvancedReportDatasource dataSource = new AdvancedReportDatasource(dataArray,rsc.getColumns().size(), rowData);
				
				ActionServlet s = getServlet();
				String jarFile = s.getServletContext().getRealPath(
									  "/WEB-INF/lib/jasperreports-0.6.8.jar");
				System.setProperty("jasper.reports.compile.class.path",jarFile);
				String realPathJrxml = s.getServletContext().getRealPath(
									 "/WEB-INF/classes/org/digijava/module/aim/reports");
				realPathJrxml = realPathJrxml + "\\" + formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jrxml";
				logger.info("Path : " + realPathJrxml);
	
				//calling dynamic jrxml
				AdvancedReportPdfJrxml jrxml = new AdvancedReportPdfJrxml();
				AdvancedReportQtrlyJrxml qjrxml = new AdvancedReportQtrlyJrxml();
				
				if(formBean.getColumnHierarchie()!=null && formBean.getColumnHierarchie().size()>0){
					if(qtrlyFlag)
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "pdf",formBean.getColumnHierarchie().size());
					else
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "pdf",formBean.getColumnHierarchie().size());
				}
				else{
					if(qtrlyFlag)
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "pdf",0);
					else
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "pdf",0);
				}
				JasperCompileManager.compileReportToFile(realPathJrxml);
				byte[] bytes = null;
				try
				{
					String jasperFile = s.getServletContext().getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports"+"\\"+formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jasper");
					logger.info("Jasper FIle ::::::::::::::::;" + jasperFile);
					
					
					Map parameters = new HashMap();
					logger.debug("jasperFile "+jasperFile );
					logger.debug("parameters"+parameters);
					bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
				}
				catch (JRException e)
				{
					logger.debug("Exception from MultilateralDonorDatasource = " + e);
				}
				if (bytes != null && bytes.length > 0)
				{
					ServletOutputStream ouputStream = response.getOutputStream();
					logger.debug("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition","inline; filename=MultilateralDonorPdf.pdf");
					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				}
				else
				{
					logger.debug("Nothing to display");
				}
			}
			
			
			/*
			 * 	Creates Excel document when excel link is clicked
			 */
			
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("excel"))
			{
				AdvancedReportDatasource dataSource = new AdvancedReportDatasource(dataArray,rsc.getColumns().size(), rowData);
				
				ActionServlet s = getServlet();
				String jarFile = s.getServletContext().getRealPath(
									  "/WEB-INF/lib/jasperreports-0.6.8.jar");
				System.setProperty("jasper.reports.compile.class.path",jarFile);
				String realPathJrxml = s.getServletContext().getRealPath(
									 "/WEB-INF/classes/org/digijava/module/aim/reports");
				realPathJrxml = realPathJrxml + "\\" + formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jrxml";
				logger.info("XLS jrxml Path : " + realPathJrxml);

				//calling dynamic jrxml
				AdvancedReportPdfJrxml jrxml = new AdvancedReportPdfJrxml();
				AdvancedReportQtrlyJrxml qjrxml = new AdvancedReportQtrlyJrxml();
				//jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_").replaceAll("#", " "), "xls",false);
				if(formBean.getColumnHierarchie()!=null && formBean.getColumnHierarchie().size()>0){
					if(qtrlyFlag){
						logger.info("#### JRXML with h with Q ########");
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "xls",formBean.getColumnHierarchie().size());
					}
					else{
						logger.info("#### JRXML with h without Q ########");
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "xls",formBean.getColumnHierarchie().size());
					}
				}
				else{
					if(qtrlyFlag){
						logger.info("#### JRXML without h with Q ########");
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "xls",0);
					}
					else{
						logger.info("#### JRXML without h without Q ########");
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_"), "xls",0);
					}
				}
					
				JasperCompileManager.compileReportToFile(realPathJrxml);
				byte[] bytes = null;
				ServletOutputStream outputStream = null;
				try
				{
					Map parameters = new HashMap();
					
					String jasperFile = s.getServletContext().getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports"+"\\"+formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jasper");
					logger.info("Jasper FIle ::::::::::::::::;" + jasperFile);
					JasperPrint jasperPrint = 
						JasperFillManager.fillReport(jasperFile,parameters,dataSource);
						response.setContentType("application/vnd.ms-excel");
						String responseHeader = "inline; filename="+formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "");
						logger.info("XLS --------------" + responseHeader);
						response.setHeader("Content-Disposition", responseHeader);
						//response.setHeader("Content-Disposition","inline; filename=commitmentByModalityXls.xls");

						logger.info("XLS--------------XLS");
					
					JRXlsExporter exporter = new JRXlsExporter();
					outputStream = response.getOutputStream();
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
					e.printStackTrace(System.out);
				}

			}
			
			/*
			 * 	Creates CSV document when csv link is clicked
			 */
			
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
			{
				AdvancedReportDatasource dataSource = new AdvancedReportDatasource(dataArray,rsc.getColumns().size(), rowData);
				
				ActionServlet s = getServlet();
				String jarFile = s.getServletContext().getRealPath(
									  "/WEB-INF/lib/jasperreports-0.6.8.jar");
				System.setProperty("jasper.reports.compile.class.path",jarFile);
				String realPathJrxml = s.getServletContext().getRealPath(
									 "/WEB-INF/classes/org/digijava/module/aim/reports");
				realPathJrxml = realPathJrxml + "\\" + formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jrxml";
				logger.info("Path : " + realPathJrxml);

				//calling dynamic jrxml
				AdvancedReportPdfJrxml jrxml = new AdvancedReportPdfJrxml();
				AdvancedReportQtrlyJrxml qjrxml = new AdvancedReportQtrlyJrxml();
				
				//jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(),	formBean.getReportName().replaceAll(" ", "_").replaceAll("#", ""),"csv",false);
				if(formBean.getColumnHierarchie()!=null && formBean.getColumnHierarchie().size()>0){
					if(qtrlyFlag)
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_").replaceAll("#", ""), "csv",formBean.getColumnHierarchie().size());
					else
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_").replaceAll("#", ""), "csv",formBean.getColumnHierarchie().size());
				}
				else
					if(qtrlyFlag)
						qjrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_").replaceAll("#", ""), "csv",0);
					else
						jrxml.createJRXML(realPathJrxml, undisbFlag ,rowData, dataArray, rsc.getColumns().size(), rsc.getMeasures().size(), formBean.getReportName().replaceAll(" ", "_").replaceAll("#", ""), "csv",0);
				
				JasperCompileManager.compileReportToFile(realPathJrxml);
				byte[] bytes = null;
				ServletOutputStream outputStream = null;
				try
				{
					Map parameters = new HashMap();
					
					String jasperFile = s.getServletContext().getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports"+"\\"+formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "")+".jasper");
					logger.info("Jasper FIle ::::::::::::::::;" + jasperFile);
					JasperPrint jasperPrint = 
						JasperFillManager.fillReport(jasperFile,parameters,dataSource);
						response.setContentType("application/vnd.ms-excel");
						String responseHeader = "inline; filename="+formBean.getReportName().replaceAll(" ", "_").replaceAll("#", "");
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
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return null;
	} // end of Execute
	
	int getColumnIndex(String str)
	{
		int i = 0, pos = 0;
		for(i=0; i < columnDetails.length; i++)
		{
			//logger.info("grrrrrrr= "+i);
			if(columnDetails[i][0]!=null && columnDetails[i][0].equals(str))
			{
				pos = Integer.parseInt(columnDetails[i][1]);
				break;
			}
		}
		return pos;
	}
	
	void getHierarchyData(int row, int position,Iterator reportIter){

		//logger.info("~~~~~~~~~~~~~~inside getHierarchy Data..........");
		advReport = (AdvancedReport) reportIter.next();
		flag = 1;
		if(columnColl.contains("Status") == true && advReport.getStatus() != null )
		{
			position = getColumnIndex("Status");
			if(advReport.getStatus().replaceAll(",", "").length() > 2)
				dataArray[row][position] = advReport.getStatus().replaceAll(",", "");
		
			logger.info(advReport.getStatus().replaceAll(",", "").length() + " : Status : " + advReport.getStatus().replaceAll(",", ""));
		}
		if((columnColl.contains("Project Title") || columnColl.contains("Project Title")) && advReport.getTitle() != null)
		{
			position = getColumnIndex("Project Title");
			if(advReport.getTitle().trim().length() > 0)
				dataArray[row][position] = advReport.getTitle().trim();
				logger.info("Name : " + advReport.getTitle());
		}
		if(columnColl.contains("Actual Start Date") && advReport.getActualStartDate() != null)
		{
			position = getColumnIndex("Actual Start Date");
			if(advReport.getActualStartDate().trim().length() > 0)
				dataArray[row][position] = advReport.getActualStartDate().trim();
				
				logger.info("Start Date : " + advReport.getActualStartDate());
		}
		if(columnColl.contains("Actual Completion Date") && advReport.getActualCompletionDate() != null)
		{
			position = getColumnIndex("Actual Completion Date");
			if(advReport.getActualCompletionDate().trim().length() > 0)
				dataArray[row][position] = advReport.getActualCompletionDate().trim();
				
				logger.info("Completion Date : " + advReport.getActualCompletionDate());
		}					

		if(columnColl.contains("Implementation Level") && advReport.getLevel() != null)
		{
			position = getColumnIndex("Implementation Level");
			if(advReport.getLevel().trim().length() > 0)
				dataArray[row][position] = advReport.getLevel().trim(); 
				
				logger.info("Implementation Level: " + advReport.getLevel());
		}
		
		if(columnColl.contains("Description") && advReport.getDescriptionPDFXLS() != null)
		{
			position = getColumnIndex("Description");
			if(advReport.getDescriptionPDFXLS().trim().length() > 0)
				dataArray[row][position] = advReport.getDescriptionPDFXLS().trim(); 
				
				logger.info("Description : " + advReport.getDescription());
		}
		
		if(columnColl.contains("Objective") && advReport.getObjectivePDFXLS() != null)
		{
			position = getColumnIndex("Objective");
			if(advReport.getObjectivePDFXLS().trim().length() > 0)
				dataArray[row][position] = advReport.getObjectivePDFXLS().trim();
				
				logger.info("Objective : " + advReport.getObjective());
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
			//logger.info("DONORRRRRR"+advReport.getDonors().toString()+">>>"+advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim()+"<><><>"+row+":"+position);
			if(advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim().length() > 0){
				logger.info("inserting DONOR");
				dataArray[row][position] = advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim();
			}
			
				//logger.info(advReport.getDonors().toString().replace('[',' ').replace(']',' ').trim().length() + " : Donor : " + advReport.getDonors().toString().replace('[',' ').replace(']',' '));
		}					

		if(columnColl.contains("Sector") && advReport.getSectors() != null)
		{
				position = getColumnIndex("Sector");
				if(advReport.getSectors().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
					dataArray[row][position] = advReport.getSectors().toString().replace('[',' ').replace(']',' ').trim();

		}					
		if(columnColl.contains("Region") && advReport.getRegions() != null)
		{
			position = getColumnIndex("Region");
			if(advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
				dataArray[row][position] = advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim();
				
//				//logger.info(advReport.getRegions().toString().replace('[',' ').replace(']',' ').trim().length() + " : Region : " );
		}
		else
			//logger.info("Region is NULL..................");
		
		if(columnColl.contains("Contact Name") && advReport.getContacts() != null)
		{
			position = getColumnIndex("Contact Name");
			//logger.info(position + "::: CONTACT NAME");
			if(advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim().length() > 0)
				dataArray[row][position] = advReport.getContacts().toString().replace('[',' ').replace(']',' ').trim();
				
				//logger.info("Contact  : " + advReport.getContacts().toString().replace('[',' ').replace(']',' '));
		}
		
		if(columnColl.contains("Financing Instrument") && advReport.getModality() != null)
		{
			position = getColumnIndex("Financing Instrument");
			if(advReport.getModality().toString().replace('[',' ').replace(']',' ').trim().length() > 0){
				dataArray[row][position] = advReport.getModality().toString().replace('[',' ').replace(']',' ').trim();
				//logger.info("Financing Instrument::::::::: "+dataArray[row][position]+" position "+ position);
			}
				
				//logger.info(advReport.getModality().toString().replace('[',' ').replace(']',' ').trim().length() + " : Modality : " + advReport.getModality().toString().replace('[',' ').replace(']',' '));
		}
		
		if(columnColl.contains("Project Id") && advReport.getAmpId() != null)
		{
			position = getColumnIndex("Project Id");
				if(advReport.getAmpId().trim().length() > 0 )
					dataArray[row][position] = advReport.getAmpId().trim();
					
				//logger.info("ID  : " + advReport.getAmpId());
		}					
		
		//position = n + rsc.getColumns().size()-1;
		//logger.info("#######################################################"+ position);
		
		
		//logger.info("~~~~~~~~~~~~~~END getHierarchy Data..........");
	}
	
	//void fillTotal(int row, int position, AdvancedHierarchyReport ahr, AdvancedHierarchyReport ahr2, AdvancedHierarchyReport ahr3)
	void fillTotal(int row, int position, AdvancedHierarchyReport ahr, AdvancedHierarchyReport ahr2, AdvancedHierarchyReport ahr3, int h)
	{
		logger.info("*********************start Fill TOTAL.......... row="+row+" pos= "+position);
		
		int hsize=formBean.getColumnHierarchie().size();
		//		int temp2=report.getRecords().size();
		//logger.info("kkkkkkkkkkkkkkkkk="+k+"tempppppppppppppp2:"+temp2);
		//if(k==temp2){	
		dataArray[row][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
		//logger.info("@@@@@@@@ WorkspaceName="+formBean.getWorkspaceName());
		String filterName[] = new String[2];
		position = 1;
		
		filterName = formBean.getFilter();
		for(int i=0; i<filterName.length; i++)
		{	
			dataArray[row][position] = filterName[i];
			position++;						
		}
		
		
		if(hsize>=1){
			dataArray[row][position] = ahr.getLabel()+" - "+ahr.getName();
			logger.info("total ahr.name H1------->"+ahr.getLabel());
			position++;
		}
		if(hsize>=2){
			dataArray[row][position] = ahr2.getLabel()+" - "+ahr2.getName();
			logger.info("total ahr.name H2------->"+ahr2.getLabel());
			position++;
		}
		if(hsize>=3){
			dataArray[row][position] = ahr3.getLabel()+" - "+ahr3.getName();
			logger.info("total ahr.name H3------->"+ahr3.getLabel());
			position++;
		}
		
			
			logger.info("::::::::::::::::::filling TOTALS::::::::k="+row);
			
			position = 4+rsc.getColumns().size();
			
			Iterator itrtot= ahr.getFundSubTotal().iterator();
			
			if(h==1){
				dataArray[row][3+hsize] = "TOTAL for " +ahr.getName();
				itrtot= ahr.getFundSubTotal().iterator();
				logger.info("total for H1>>>>>>"+ahr.getLabel());
			}
			if(h==2){
				dataArray[row][3+hsize] = "TOTAL for " +ahr2.getName();
				itrtot= ahr2.getFundSubTotal().iterator();
				logger.info("total for H2>>>>>>"+ahr2.getLabel());
			}
			if(h==3){
				dataArray[row][3+hsize] = "TOTAL for " +ahr3.getName();
				itrtot= ahr3.getFundSubTotal().iterator();
				logger.info("total for H3>>>>>>"+ahr3.getLabel());
			}
			
			while(itrtot.hasNext()){
				AmpFund ampFund=(AmpFund)itrtot.next();
				if(formBean.getAcCommFlag().equals("true") && ampFund.getCommAmount()!= null){
					dataArray[row][position++] = ampFund.getCommAmount();
					logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
				}
				if(formBean.getAcDisbFlag().equals("true") && ampFund.getCommAmount()!= null){
					dataArray[row][position++] = ampFund.getDisbAmount();
					logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
				}
				if(formBean.getAcExpFlag().equals("true") && ampFund.getCommAmount()!= null){
					dataArray[row][position++] = ampFund.getExpAmount();
					logger.info("total values.........."+row+":::"+position+"::::"+dataArray[row][position-1]);
				}
			}
			row++;
		//}//end total
		
			logger.info("********************* END Fill TOTAL..........row="+row);
	}

}// end of CLass

	
