package org.digijava.module.aim.action;	

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import java.util.*;

import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.Report;
import org.apache.log4j.Logger ;
import org.digijava.module.aim.helper.CommitmentByModalityDatasource;
import org.digijava.module.aim.helper.TrendXlsJrxml;


public class CommitmentByModalityXLS extends Action 
{

	private static Logger logger = Logger.getLogger(CommitmentByModalityXLS.class) ;
		  
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
		Integer year = null;
		Report report;
		AmpFund ampFund;
		ArrayList ampFunds, ampFisYears, totalFunds;
		Iterator totalFundIter, fundIter;
		int yy=0, yyTmp=0, colCnt=0, yyCnt=0, cnt=0, row=0, index=0, rowCnt=0, flag=0,ind=0;			
			
		if( coll.isEmpty() == false )
		{
			while(iter.hasNext()) 
			{
				report =(Report)iter.next();
				rowCnt  = rowCnt + 1;
			}
			rowCnt = rowCnt + 1;
		}
		if(formBean.getFiscalYearRange() != null)
		{
			yyCnt = formBean.getFiscalYearRange().size();
		}
		colCnt  = 15 + yyCnt + (yyCnt*3) + 4;
		logger.info("row : " + rowCnt + ": Col : "  + colCnt);
		Object[][] data = new Object[rowCnt][colCnt];
		if( coll.isEmpty() == false )
		{
			iter = coll.iterator();
			while (iter.hasNext()) 
			{
				report =(Report)iter.next();
				data[row][0]=formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
				int i=0;
				for(int l=0; l<formBean.getFilter().length; l++)
				{	
					data[row][l+1] = formBean.getFilter()[l].toString();
				}
				data[row][3] = report.getDonors().toString().replace('[',' ').replace(']',' ');
				data[row][4] = report.getTitle();
				data[row][5] = report.getStatus();
				data[row][6] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				data[row][7] = report.getModality().toString().replace('[',' ').replace(']',' ');
				data[row][8] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				data[row][9] = report.getLevel();
				data[row][10] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				data[row][11] = report.getStartDate();
				data[row][12] = report.getCloseDate();
				data[row][13] = report.getCommitmentDate().toString().replace('[',' ').replace(']',' ');
				data[row][14] = report.getAcCommitment();
					
				ampFisYears = new ArrayList(formBean.getFiscalYearRange());
				Iterator fiscIter = ampFisYears.iterator();
				if(fiscIter.hasNext())
				{
					year = (Integer)fiscIter.next();
					yy = year.intValue();
				}
				yyTmp = yy;		

				ampFunds = new ArrayList(report.getAmpFund());
				fundIter = ampFunds.iterator();
				index  = 14;
				int count = 0;
				if(ampFunds.size() > 0)
				{
					while (fundIter.hasNext() )	
					{
						if(count < formBean.getFiscalYearRange().size())
						{
							index = index + 1;
							data[row][index]= Integer.toString(yy);
							count = count + 1;
						}	
						ampFund = (AmpFund)fundIter.next();
						index+=1;
						data[row][index] = ampFund.getPlannedDisbAmount();
						index+=1;
						data[row][index] = ampFund.getDisbAmount();
						index+=1;
						data[row][index] = ampFund.getExpAmount();
						yy = yy + 1;
					}
					yy = yyTmp;
				}
				row = row + 1;
				flag = 1;
			}// end of Object Collection
				
			data[row][3] = "Total ";  
			index = 14;
			int count = 0;
			data[row][index] = formBean.getTotComm();
			totalFunds = new ArrayList(formBean.getTotFund());
			logger.info(" Total FUnd : " + formBean.getTotFund().size());
			totalFundIter = totalFunds.iterator();
			while(totalFundIter.hasNext())
			{
				if(count == 3){
					index = index + 1;
				}
				else{
					index+=2;
					count = count + 1;
				}
				ampFund = (AmpFund) totalFundIter.next();
				data[row][index] = ampFund.getPlannedDisbAmount();
				index+=1;
				data[row][index] = ampFund.getDisbAmount();
				index+=1;
				data[row][index] = ampFund.getExpAmount();
			}
		}
		else
		{
			flag = 0;
			logger.info("Not objects to get");
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


			String fileName="commitmentByModalityXls.jrxml";
			CommitmentByModalityDatasource dataSource = new CommitmentByModalityDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
					 "/WEB-INF/classes/org/digijava/module/aim/reports/commitmentByModalityXls.jrxml");
			TrendXlsJrxml jrxml = new TrendXlsJrxml();
			jrxml.createJrxml(yyCnt, realPathJrxml);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
			byte[] bytes = null;
			String jasperFile = s.getServletContext().getRealPath(
				 "/WEB-INF/classes/org/digijava/module/aim/reports/commitmentByModalityXls.jasper");
			Map parameters = new HashMap();
			JasperPrint jasperPrint = 
			JasperFillManager.fillReport(jasperFile,parameters,dataSource);
			ServletOutputStream outputStream = null;
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","inline; filename=commitmentByModalityXls.xls");
			try
			{
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
			}
		}
		else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
		{
				logger.info("EXPORTING CSV for CommitmentBymodality");
				ServletOutputStream outputStream = null;
				try
				{
				
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/commitmentByModalityXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/commitmentByModalityXls.csv");
					
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
		else
		{
			logger.info("NO PDF GENERATED ");
		}
		return null;
	} // end of Execute
}// end of CLass
