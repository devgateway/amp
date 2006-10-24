package org.digijava.module.aim.action;

import java.text.DecimalFormat;
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
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.Parisindicator3Jrxml;
import org.digijava.module.aim.helper.ParisIndicator4Jrxml;
import org.digijava.module.aim.helper.ParisIndicator5aJrxml;
import org.digijava.module.aim.helper.ParisIndicator5bJrxml;
import org.digijava.module.aim.helper.ParisIndicator6Jrxml;
import org.digijava.module.aim.helper.ParisIndicator7Jrxml;
import org.digijava.module.aim.helper.ParisIndicator9Jrxml;
import org.digijava.module.aim.helper.ParisIndicator10aJrxml;
import org.digijava.module.aim.helper.ParisIndicatorDataSource;


/*
 *@author Govind G Dalwani
 */
public class ParisIndicatorReportPDFXLSCSV extends Action {
	private static Logger logger = Logger
			.getLogger(ParisIndicatorReportPDFXLSCSV.class);

	private static int fieldHeight = 0;

	DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		String pId = request.getParameter("pid");
		String type = request.getParameter("docType");
		
		int row, col;
		ParisIndicatorReportForm formBean = (ParisIndicatorReportForm) form;
		Collection coll = null;
		if (formBean != null) {
		//	System.out.println("formBean is not null");
			coll = formBean.getDonorsColl();
		} else {
			//System.out.println("formbean is null");
		}
		if (coll == null)
			System.out.println("coll is null");
		else
			System.out.println("formBean is not null");

		Iterator iter = null;
		Iterator iter1 = null;
		if (coll.size() == 0) {
			System.out.println("collection is empty");
		} else {
			System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		int colCnt1 = coll.size();
		ArrayList ans = new ArrayList();
		int rowCnt1 = 0;
		int b1 = 0;
		if (coll.size() > 0) {
			while (iter.hasNext()) {
				ParisIndicator pi = (ParisIndicator) iter.next();
				ans = pi.getAnswers();
				int arrSize = ans.size();
				rowCnt1 = rowCnt1 + 1;
				for (int i = 0; i < arrSize; i++) {
					double test[] = (double[]) ans.get(i);
					for (int j = 0; j < test.length; j++) {
						b1++;
					}
				}
				b1++;
			}
		}
		
		colCnt1 = b1 / coll.size();
		//logger.info(" this is b1 here..." + b1 + " colcount size...." + b1coll.size() + "COLL CNT 1 is    "+ colCnt1 + " row size  "+rowCnt1);
		Object[][] data2 = new Object[rowCnt1][colCnt1];

		if (coll.size() > 0) {
			iter1 = coll.iterator();
			row = col = 0;
			while (iter1.hasNext()) {
				ParisIndicator pi = (ParisIndicator) iter1.next();
				ans = pi.getAnswers();
				int arrSize = ans.size();
			
				// rowCnt1 = rowCnt1+1;
				String name = pi.getDonor();
				data2[row][col] = name;
				
				for (int i = 0; i < arrSize; i++) {
					double test[] = (double[]) ans.get(i);

					for (int j = 0; j < test.length; j++) {
						col++;
						double val = test[j];
						
						if(val==-1)
						{
							data2[row][col] = "na";
						}
						else
						{
							data2[row][col] = mf.format(val);
						}
					}
				}
				col = 0;
				row = row + 1;
			}
		}
		if (pId.equals("6")) {
			////logger.info(" came inside the 6th god damn report");
			
			if (coll.size() > 0) {
				iter1 = coll.iterator();
				row = col = 0;
				data2 = new Object[rowCnt1][b1];
				int yr = formBean.getStartYear().intValue();
				while (iter1.hasNext()) {
					ParisIndicator pi = (ParisIndicator) iter1.next();
					ans = pi.getAnswers();
					int arrSize = ans.size();
					String name = pi.getDonor();
					data2[row][col] = name;
					for (int i = 0; i < arrSize; i++) {
						double test[] = (double[]) ans.get(i);
						for (int j = 0; j < test.length; j++) {
							col++;
							double val = test[j];
							data2[row][col]= ""+yr;							
							col++;
							data2[row][col] = "" + val;							
							yr=yr+1;
						}
					}
					col = 0;
					row = row + 1;
				}
			}
		}
		if (pId.equals("3")) {
			//logger.info("in the 3rd report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);

			//logger.info("coming Into the 3rd report!!!!");
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.jrxml");
			Parisindicator3Jrxml jrxml = new Parisindicator3Jrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.jasper");

					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out
							.println("Exception from MultilateralDonorDatasource = "
									+ e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator3.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator3.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");

				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				}

				catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out
							.println("Exception from PhysicalComponentReportXls = "
									+ e);
				}
			}

		}
		// this is for the 4th indicator.....
		if (pId.equals("4")) {

			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);

			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator4pdf.jrxml");
			ParisIndicator4Jrxml jrxml = new ParisIndicator4Jrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator4pdf.jasper");

					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out.println("Exception from ParisDataSource = " + e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator4.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator4pdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator4.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator4pdf.xls");

				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				}

				catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out
							.println("Exception from PhysicalComponentReportXls = "
									+ e);
				}
			}

		}
		
		
		/*
		 * this is for the 6th indicator
		 */

		if (pId.equals("6")) {
			//logger.info("in the 6th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator6pdf.jrxml");
			ParisIndicator6Jrxml jrxml = new ParisIndicator6Jrxml();
			jrxml.createJrxml(realPathJrxml, b1-1, rowCnt1, type);
			//logger.info("got back here");
			JasperCompileManager.compileReportToFile(realPathJrxml);
			//logger.info("is it here??");
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator6pdf.jasper");
					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out.println("Exception from PI7 = " + e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator6.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator6pdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator6.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator6pdf.xls");
				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				} catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out.println("Exception from PI6Xls = " + e);
				}
			}
		}
		
		

		/*
		 * this is for the 7th indicator
		 */

		if (pId.equals("7")) {
			//logger.info("in the 7th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.jrxml");
			ParisIndicator7Jrxml jrxml = new ParisIndicator7Jrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			//logger.info("got back here");
			JasperCompileManager.compileReportToFile(realPathJrxml);
			//logger.info("is it here??");
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.jasper");
					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out.println("Exception from PI7 = " + e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator7.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator7.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator7pdf.xls");
				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				} catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out.println("Exception from PI7Xls = " + e);
				}
			}
		}

		/*
		 * this is for the 9th indicator
		 */
		if (pId.equals("9")) {
			//logger.info("in the 9th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);

			//logger.info("coming Into the 3rd report!!!!");
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator9pdf.jrxml");
			ParisIndicator9Jrxml jrxml = new ParisIndicator9Jrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			//logger.info("got back here");
			JasperCompileManager.compileReportToFile(realPathJrxml);
			//logger.info("is it here??");
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator9pdf.jasper");
					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out
							.println("Exception from MultilateralDonorDatasource = "
									+ e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator9.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator9pdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator9.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator9pdf.xls");
				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				} catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out.println("Exception from PI9Xls = " + e);
				}
			}
		}
		
		/*
		 * this is for 5a
		 */
		
		if (pId.equals("5a")) {
			//logger.info("in the 5th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);

			//logger.info("coming Into the 5a report!!!!");
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5apdf.jrxml");
			ParisIndicator5aJrxml jrxml = new ParisIndicator5aJrxml();
			//logger.info(" the type is .... "+ type);
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			//logger.info("got back here");
			JasperCompileManager.compileReportToFile(realPathJrxml);
			//logger.info("is it here??");
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5apdf.jasper");
					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out
							.println("Exception from MultilateralDonorDatasource = "
									+ e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator5a.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5apdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator5a.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5apdf.xls");
				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				} catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out.println("Exception from PI5aXls = " + e);
				}
			}
		}
		
		/*
		 * report 5b
		 */
		
		if (pId.equals("5b")) {
			//logger.info("in the 5th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5bpdf.jrxml");
			ParisIndicator5bJrxml jrxml = new ParisIndicator5bJrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5bpdf.jasper");

					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out
							.println("Exception from MultilateralDonorDatasource = "
									+ e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator5b.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5bpdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator5b.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator5bpdf.xls");

				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				}

				catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out
							.println("Exception from PhysicalComponentReportXls = "
									+ e);
				}
			}

		}
		
		/*
		 * report 10a
		 */
		if (pId.equals("10a")) {
			//logger.info("in the 5th report");
			String jasperFile = null;
			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);
			String realPathJrxml = s
					.getServletContext()
					.getRealPath(
							"/WEB-INF/classes/org/digijava/module/aim/reports/indicator10apdf.jrxml");
			ParisIndicator10aJrxml jrxml = new ParisIndicator10aJrxml();
			jrxml.createJrxml(realPathJrxml, colCnt1, rowCnt1, type);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			byte[] bytes = null;
			if (type.equals("pdf")) {
				try {

					jasperFile = s
							.getServletContext()
							.getRealPath(
									"/WEB-INF/classes/org/digijava/module/aim/reports/indicator10apdf.jasper");

					Map parameters = new HashMap();
					System.out.println(jasperFile);
					System.out.println(parameters);
					bytes = JasperRunManager.runReportToPdf(jasperFile,
							parameters, dataSource);
				} catch (JRException e) {
					System.out
							.println("Exception from MultilateralDonorDatasource = "
									+ e);
				}
				if (bytes != null && bytes.length > 0) {
					ServletOutputStream ouputStream = response
							.getOutputStream();
					System.out.println("Generating PDF");
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition",
							"inline; filename=ParisIndicator10a.pdf");

					response.setContentLength(bytes.length);
					ouputStream.write(bytes, 0, bytes.length);
					ouputStream.flush();
					ouputStream.close();
				} else {
					System.out.println("Nothing to display");
				}
			}
			// xls
			else if (type.equals("xls")) {

				jasperFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator10apdf.jasper");

				Map parameters = new HashMap();
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperFile, parameters, dataSource);
				String destFile = null;
				// s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/indicator3pdf.xls");
				ServletOutputStream outputStream = null;
				response.setContentType("application/vnd.ms-excel");

				response.setHeader("Content-Disposition",
						"inline; filename=ParisIndicator10a.xls");
				destFile = s
						.getServletContext()
						.getRealPath(
								"/WEB-INF/classes/org/digijava/module/aim/reports/indicator10apdf.xls");

				try {
					outputStream = response.getOutputStream();
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				}

				catch (JRException e) {
					if (outputStream != null)
						outputStream.close();
					System.out
							.println("Exception from PhysicalComponentReportXls = "
									+ e);
				}
			}

		}


		return null;
	}// end of Execute Func

	void calculateFieldHeight(String input) {
		System.out.println(" Large ::" + fieldHeight + " :: Current : "
				+ input.length());
		if (input.length() > fieldHeight)
			fieldHeight = input.length();
	}

}// end of Class

