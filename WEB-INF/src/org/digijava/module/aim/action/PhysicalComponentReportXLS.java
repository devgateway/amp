package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
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
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.AmpComponent;
import org.digijava.module.aim.helper.MultilateralDonorDatasource;
import org.digijava.module.aim.helper.PhysicalCompReportXLS;

public class PhysicalComponentReportXLS extends Action 
{
	private static Logger logger = Logger.getLogger(PhysicalComponentReportPDF.class) ;
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
			////System.out.println("formBean is not null");
			coll= formBean.getMultiReport();
		} 
		Iterator iter = null;
		if (coll.size() == 0) {
			////System.out.println("collection is empty");
		} else {
			////System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		////System.out.println("col size "+ coll.size());
		
		multiReport report;
		AmpTeamDonors teamDonors;
		Project project = null;
		AmpComponent ampComponent = null;
		
		ArrayList ampProjects,ampDonors,ampDonor,components;
		Iterator projectIter, donorIter, componentIter;
		
		int row=0, col=0, flag =0, rowCnt=0, colCnt=0;
		boolean term = false;

		if(coll.size() > 0)
		{
			while(iter.hasNext())
			{
				report = (multiReport)iter.next();
				ampDonors = new ArrayList(report.getDonors());
				donorIter = ampDonors.iterator();
				while(donorIter.hasNext())
				{
					teamDonors = (AmpTeamDonors)donorIter.next();
					ampProjects = new ArrayList(teamDonors.getProject());
					projectIter = ampProjects.iterator();
					while(projectIter.hasNext())
					{
						project = (Project) projectIter.next();
						rowCnt = rowCnt + 1;
						//components = new ArrayList(project.getComponent());
						//componentIter = components.iterator();
					}// End of Project Collection
				}// End of Donor Collection
			}// end of Report Collection			
		}// end of Big Loop and check for NULL

		logger.info(rowCnt + " : Row : Col :" + 17);
		Object data[][] = new Object[rowCnt][17];

		for(int i=0; i<rowCnt; i++)
		{
			for(int j=0; j< 17; j++)
				data[i][j] = "";
		}
		String temp="";
		
		if(coll.size() > 0)
		{
			iter = coll.iterator();
			col = row = 0;
			data[row][col] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName();
			String filterName[] = new String[2];
			filterName = formBean.getFilter();
			for(int i=0; i<filterName.length; i++)
			{	
				col = col + 1;
				data[row][col] = filterName[i];
			}
			
			while(iter.hasNext())
			{
				report = (multiReport)iter.next();
				ampDonors = new ArrayList(report.getDonors());
				donorIter = ampDonors.iterator();
				while(donorIter.hasNext())
				{
					teamDonors = (AmpTeamDonors)donorIter.next();
					ampProjects = new ArrayList(teamDonors.getProject());
					projectIter = ampProjects.iterator();
					while(projectIter.hasNext())
					{
						String filtName[] = new String[2];
						int ind = 0;
						filtName = formBean.getFilter();
						for(int i=0; i<filterName.length; i++)
						{	
							ind = ind + 1;
							data[row][ind] = filterName[i];
						}

						temp = "Team Name: " + report.getTeamName();
						data[row][3] = data[row][3].toString()+temp;
						
						data[row][4] = teamDonors.getDonorAgency().trim();
						calculateFieldHeight(teamDonors.getDonorAgency());
						
						project = (Project) projectIter.next();
						data[row][5] = project.getName().trim();
						calculateFieldHeight(project.getName());
						if(project.getDescription() != null)
						{
							data[row][5] = data[row][5] + "  \n\nDescription :  " +project.getDescriptionPDFXLS().trim();
							calculateFieldHeight(project.getDescriptionPDFXLS());
						}
						data[row][6] = project.getSignatureDate();
						data[row][7] = project.getPlannedCompletionDate();
						data[row][8] = project.getAcCommitment();
						data[row][9] = project.getAcDisbursement();
						data[row][10] = project.getAcUnDisbursement();
						
						data[row][11] = project.getProgress().toString().trim().replace('[',' ').replace(']',' ');
						data[row][12] = project.getIssues().toString().trim().replace('[',' ').replace(']',' ');
						data[row][13] = project.getMeasures().toString().trim().replace('[',' ').replace(']',' ');
						data[row][14] = project.getResponsibleActor().toString().trim().replace('[',' ').replace(']',' ');
						
						row = row + 1;
					}// End of Project Collection
				}// End of Donor Collection
			}// end of Report Collection			
			col = 0;
			flag = 1;
		}// end of Big Loop and check for NULL
		else
		{
			flag = 0;	
			////System.out.println("Collection empty");
		}
		
		int height = (( fieldHeight / 25 ) * 5 ) + 50;
		logger.info(" Column Height = " + height );
		if(height>300)
			height=300;

		if(flag == 1)
		{

			String fileName="PhysicalComponentReport.jrxml";
			MultilateralDonorDatasource dataSource = new MultilateralDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportXls.jrxml");
			
			PhysicalCompReportXLS jrxml = new PhysicalCompReportXLS();
			jrxml.createJrxml(height, realPathJrxml);
			
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
			byte[] bytes = null;
			String jasperFile = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportXls.jasper");
			Map parameters = new HashMap();
			JasperPrint jasperPrint = 
			JasperFillManager.fillReport(jasperFile, parameters, dataSource);
			String destFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportXls.xls");
			ServletOutputStream outputStream = null;
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","inline; filename=PhysicalComponentReportXls.xls");
			try
			{
				outputStream = response.getOutputStream();
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.exportReport();
			}

			catch (JRException e)
			{
				if (outputStream != null) 
					outputStream.close();
				////System.out.println("Exception from PhysicalComponentReportXls = " + e);
			}
		}

		else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
		{
				logger.info("EXPORTING CSV for PhysicalComponentReport");
				ServletOutputStream outputStream = null;
				try
				{
				
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/PhysicalComponentReportXls.csv");
					
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
	}// end of Execute Func

	void calculateFieldHeight(String input)
	{
		if(input.length() > 0)
		{
			////System.out.println(" Large ::" + fieldHeight + " :: Current : " + input.length());
			if(input.length() > fieldHeight)
				fieldHeight = input.length();
		}
	}

}// end of Class
	
