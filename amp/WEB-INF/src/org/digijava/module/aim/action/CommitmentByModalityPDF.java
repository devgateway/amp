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
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.form.CommitmentbyDonorForm ;
import org.digijava.module.aim.helper.Report;
import org.apache.log4j.Logger ;
import org.digijava.module.aim.helper.CommitmentByModalityDatasource;
import org.digijava.module.aim.helper.TrendJrxml;


public class CommitmentByModalityPDF extends Action 
{

	private static Logger logger = Logger.getLogger(CommitmentByModalityPDF.class) ;
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
				data[row][0]=formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
				int i=0;
				for(int l=0; l<formBean.getFilter().length; l++)
				{	
					data[row][l+1] = formBean.getFilter()[l].toString();
				}
				data[row][3] = report.getDonors().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][3].toString());
				
				data[row][4] = report.getTitle();
				calculateFieldHeight(report.getTitle());
				
				data[row][5] = report.getStatus();
				data[row][6] = report.getAssistance().toString().replace('[',' ').replace(']',' ');
				data[row][7] = report.getModality().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][7].toString());
				
				data[row][8] = report.getSectors().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][8].toString());
				
				data[row][9] = report.getLevel();
				data[row][10] = report.getRegions().toString().replace('[',' ').replace(']',' ');
				calculateFieldHeight(data[row][10].toString());
				
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
			CommitmentByModalityDatasource dataSource = new CommitmentByModalityDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
					 "/WEB-INF/classes/org/digijava/module/aim/reports/commitmentByModalityPdf.jrxml");
			TrendJrxml jrxml = new TrendJrxml();
			//calling dynamic jrxml
			jrxml.createJrxml(yyCnt, realPathJrxml, height);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
						 "/WEB-INF/classes/org/digijava/module/aim/reports/commitmentByModalityPdf.jasper");
				Map parameters = new HashMap();
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				logger.info("Exception from commitmentByModalityPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=commitmentByModalityPdf.pdf");
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
		return null;
	} // end of Execute

	void calculateFieldHeight(String input)
	{
		////System.out.println(" Large ::" + fieldHeight + " :: Current : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}// end of CLass

