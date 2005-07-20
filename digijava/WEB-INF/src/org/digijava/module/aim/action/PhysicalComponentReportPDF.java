package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
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
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.AmpComponent;
import org.digijava.module.aim.helper.MultilateralDonorDatasource;
import org.digijava.module.aim.helper.PhysicalCompReportPDF;



public class PhysicalComponentReportPDF extends Action 
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
			System.out.println("formBean is not null");
			coll= formBean.getMultiReport();
		} else {
			System.out.println("formbean is null");
		}
		if(coll == null)
			System.out.println("coll is null");
		else
			System.out.println("formBean is not null");
		
		Iterator iter = null;
		if (coll.size() == 0) {
			System.out.println("collection is empty");
		} else {
			System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		System.out.println("col size "+ coll.size());
		
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
						components = new ArrayList(project.getComponent());
						componentIter = components.iterator();
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
						//data[row][0] += (Object) " Team Namex: "+report.getTeamName() ;
						temp = "                           Team Name: " + report.getTeamName();
						data[row][0] = data[row][0].toString()+temp;
//						calculateFieldHeight(report.getTeamName());
						
						data[row][3] = teamDonors.getDonorAgency();
						calculateFieldHeight(teamDonors.getDonorAgency());
						
						project = (Project) projectIter.next();
						data[row][4] = project.getName();
						calculateFieldHeight(project.getName());
						
						components = new ArrayList(project.getComponent());
						componentIter = components.iterator();
						while(componentIter.hasNext())
						{
							ampComponent = (AmpComponent) componentIter.next();
							data[row][5] = ampComponent.getName();
							if(ampComponent.getObjective() != null)
							{
								temp = "\n Objective: " + ampComponent.getObjective();
								data[row][5] = data[row][5].toString()+temp;
								calculateFieldHeight(ampComponent.getObjective());
							}
							else
							{
								temp = " ";
								//data[row][5] = data[row][5].toString() + temp;
							}
							data[row][6] = ampComponent.getSignatureDate();
							data[row][7] = ampComponent.getPlannedCompletionDate();
							data[row][8] = ampComponent.getAcCommitment();
							data[row][9] = ampComponent.getAcExpenditure();
							data[row][10] = ampComponent.getAcBalance();
							temp = " * ";
							data[row][11] = temp+ampComponent.getStatus().toString().replace('[',' ').replace(']',' ');
							calculateFieldHeight(data[row][11].toString());
							
							data[row][12] = ampComponent.getIssues().toString().replace('[',' ').replace(']',' ');
							calculateFieldHeight(data[row][12].toString());
							
							data[row][13] = ampComponent.getMeasures().toString().replace('[',' ').replace(']',' ');
							calculateFieldHeight(data[row][13].toString());
							
							data[row][14] = ampComponent.getResponsibleActor().toString().replace('[',' ').replace(']',' ');
							calculateFieldHeight(data[row][14].toString());
							
							
						}	// End of Component Collection
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
			System.out.println("Collection empty");
		}
		
		int height = (( fieldHeight / 25 ) * 10 ) + 50;
		logger.info(" Column Height = " + height );

		if(flag == 1)
		{

			MultilateralDonorDatasource dataSource = new MultilateralDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportPdf.jrxml");
			
			PhysicalCompReportPDF jrxml = new PhysicalCompReportPDF();
			jrxml.createJrxml(height, realPathJrxml);
			
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			try
			{
				String jasperFile = s.getServletContext().getRealPath(
						"/WEB-INF/classes/org/digijava/module/aim/reports/PhysicalComponentReportPdf.jasper");
				Map parameters = new HashMap();
				System.out.println(jasperFile );
				System.out.println(parameters);
				bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
			}
			catch (JRException e)
			{
				System.out.println("Exception from PhysicalComponentReportPdf = " + e);
			}
			if (bytes != null && bytes.length > 0)
			{
				ServletOutputStream ouputStream = response.getOutputStream();
				System.out.println("Generating PDF");
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=PhysicalComponentReportPdf.pdf");
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
	}// end of Execute Func

	void calculateFieldHeight(String input)
	{
		System.out.println(" Large ::" + fieldHeight + " :: Current : " + input.length());
		if(input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}// end of Class
	
