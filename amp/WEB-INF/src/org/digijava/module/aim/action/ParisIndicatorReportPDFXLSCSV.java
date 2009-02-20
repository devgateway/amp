package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.ParisIndicator10aJrxml;
import org.digijava.module.aim.helper.ParisIndicator4Jrxml;
import org.digijava.module.aim.helper.ParisIndicator5aJrxml;
import org.digijava.module.aim.helper.ParisIndicator5aSubJrxml;
import org.digijava.module.aim.helper.ParisIndicator5bJrxml;
import org.digijava.module.aim.helper.ParisIndicator5bSubJrxml;
import org.digijava.module.aim.helper.ParisIndicator6Jrxml;
import org.digijava.module.aim.helper.ParisIndicator7Jrxml;
import org.digijava.module.aim.helper.ParisIndicator9Jrxml;
import org.digijava.module.aim.helper.ParisIndicatorDataSource;
import org.digijava.module.aim.helper.ParisIndicatorJrxml;
import org.digijava.module.aim.helper.Parisindicator3Jrxml;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/*
 *@author Govind G Dalwani
 */
public class ParisIndicatorReportPDFXLSCSV extends Action {
	private static Logger logger = Logger
			.getLogger(ParisIndicatorReportPDFXLSCSV.class);

	private static int fieldHeight = 0;

	DecimalFormat mf = new DecimalFormat("###,###,###,###,###");

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
			// ////System.out.println("formBean is not null");
			coll = formBean.getDonorsColl();
		}
		Iterator iter = null;
		Iterator iter1 = null;
		if (coll.size() == 0) {
			// //System.out.println("collection is empty");
		} else {
			// //System.out.println("collection is not empty");
			iter = coll.iterator();
		}
		int colCnt1 = coll.size();
		logger.info(" this is the initail col count " + colCnt1);
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
				if (!(pId.equals("6"))) {
					b1++;
				}
			}
		}

		colCnt1 = b1 / coll.size();
		logger.info(" this is b1 here..." + b1 + " colcount size...."
				+ coll.size() + "COLL CNT 1 is    " + colCnt1 + " row size  "
				+ rowCnt1);
		Object[][] data2 = new Object[rowCnt1][colCnt1];
		if (!(pId.equals("6"))) {
			logger.info("not the 6th one.....");
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

							if (val == -1) {
								data2[row][col] = "n.a.";
								// //System.out.println(
								// " this sis where na is coming "+ col);
							} else {
								double d = Math.round(val);
								int temp1 = (int) d;
								data2[row][col] = temp1 + "";
								// data2[row][col] = mf.format(temp1);
							}
						}
					}
					if (pId.equals("3") || pId.equals("4") || pId.equals("7")
							|| pId.equals("10a") || pId.equals("5b")) {
						int c = 4;
						for (int i = 0; i < arrSize; i++) {
							// //System.out.println(" i am dead "+data2[row][i]+
							// "   i       "+i);
							if (!data2[row][c].toString().equalsIgnoreCase(
									"n.a.")) {
								data2[row][c] = data2[row][c] + "%";
							}
							c = c + 4;

						}
					}
					if (pId.equals("9")) {
						int c = 5;
						for (int i = 0; i < arrSize; i++) {
							// //System.out.println(" i am dead "+data2[row][i]+
							// "   i       "+i);
							if (!data2[row][c].toString().equalsIgnoreCase(
									"n.a.")) {
								data2[row][c] = data2[row][c] + "%";
							}
							c = c + 5;

						}
					}
					if (pId.equals("5a")) {
						int c = 7;
						for (int i = 0; i < arrSize; i++) {
							// //System.out.println(" i am dead "+data2[row][i]+
							// "   i       "+i);
							if (!data2[row][c].toString().equalsIgnoreCase(
									"n.a.")) {
								data2[row][c] = data2[row][c] + "%";
								data2[row][c + 1] = data2[row][c + 1] + "%";
							}
							c = c + 8;

						}
					}
					col = 0;
					row = row + 1;
				}
			}
		}

		if (pId.equals("6")) {
			logger.info(" came inside the 6th god damn report");
			colCnt1 = 0;

			if (coll.size() > 0) {
				iter1 = coll.iterator();

				row = col = 0;
				data2 = new Object[rowCnt1][b1];
				int yr = formBean.getStartYear().intValue();
				int yr2 = formBean.getCloseYear().intValue();
				logger.info(" this is the end year " + (yr2 - yr));
				colCnt1 = (yr2 - yr) + 1;
				colCnt1 = (colCnt1 * 2) + 1;
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
							data2[row][col] = "" + yr;
							col++;
							data2[row][col] = "" + (long) test[j];
							yr = yr + 1;

							logger.info(" this is the 6th report " + b1
									+ "  data  " + data2[row][col]);
						}
					}
					col = 0;
					row = row + 1;
				}
			}
		}
		logger.info(" this is the 6th ka count " + colCnt1);

		if (!isEmpty(pId) && !isEmpty(type)) {
			ActionServlet s = getServlet();
			String reportsFolderPath = s.getServletContext().getRealPath(
					"/WEB-INF/classes/org/digijava/module/aim/reports");
			java.io.File reportsFolder = new java.io.File(reportsFolderPath);
			if (!reportsFolder.exists() || !reportsFolder.isDirectory()) {
				reportsFolder.mkdirs();
			}
			reportsFolderPath = reportsFolder.getAbsolutePath().replace("\\",
					"/");

			String jarFile = s.getServletContext().getRealPath(
					"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path", jarFile);

			String reportName = "indicator" + pId + type;
			String reportPath = reportsFolderPath + "/" + reportName;

			String realPathJrxml = reportPath + ".jrxml";
			String realPathSubJrxml = null;

			ParisIndicatorJrxml jrxml = null;
			if (pId.equals("3")) {
				jrxml = new Parisindicator3Jrxml();

			} else if (pId.equals("4")) {
				jrxml = new ParisIndicator4Jrxml();

			} else if (pId.equals("5a")) {
				realPathSubJrxml = reportPath + "_sub.jrxml";

				ParisIndicator5aSubJrxml subJrxml = new ParisIndicator5aSubJrxml();
				subJrxml.createSubJrxml(realPathSubJrxml, reportName, formBean
						.getDonorsCollIndc5());

				jrxml = new ParisIndicator5aJrxml();

			} else if (pId.equals("5b")) {
				realPathSubJrxml = reportPath + "_sub.jrxml";

				ParisIndicator5bSubJrxml subJrxml = new ParisIndicator5bSubJrxml();
				subJrxml.createSubJrxml(realPathSubJrxml, reportName, formBean
						.getDonorsCollIndc5());

				jrxml = new ParisIndicator5bJrxml();

			} else if (pId.equals("6")) {
				jrxml = new ParisIndicator6Jrxml();

			} else if (pId.equals("7")) {
				jrxml = new ParisIndicator7Jrxml();

			} else if (pId.equals("9")) {
				jrxml = new ParisIndicator9Jrxml();

			} else if (pId.equals("10a")) {
				jrxml = new ParisIndicator10aJrxml();

			} else {
				return null;
			}

			if (jrxml != null) {
				jrxml.createJrxml(realPathJrxml, reportName, formBean
						.getCurrency(), colCnt1, rowCnt1, type);
			} else {
				return null;
			}

			String jasperFile = reportPath + ".jasper";

			Map parameters = new HashMap();

			if (!isEmpty(realPathSubJrxml)) {
				JasperCompileManager.compileReportToFile(realPathSubJrxml);
				JasperCompileManager.compileReportToFile(realPathJrxml);

				String subJasperFile = reportPath + "_sub.jasper";
				parameters.put("SUBREPORT_DIR", subJasperFile);
			} else {
				JasperCompileManager.compileReportToFile(realPathJrxml);
			}

			ParisIndicatorDataSource dataSource = new ParisIndicatorDataSource(
					data2);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,
					parameters, dataSource);

			ServletOutputStream outputStream = response.getOutputStream();
			response.setHeader("Content-Disposition",
					"attachment; filename=ParisIndicator" + pId + "." + type);

			if (type.equals("pdf")) {
				response.setContentType("application/pdf");
				try {
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT,
							jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
							outputStream);
					exporter.setParameter(
							JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
					exporter.exportReport();
				} catch (JRException e) {
					throw new RuntimeException(e);
				}
			} else if (type.equals("xls")) {
				response.setContentType("application/vnd.ms-excel");
				try {
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
					throw new RuntimeException(e);
				}
			}

			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}

	boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	void calculateFieldHeight(String input) {
		if (input.length() > fieldHeight) {
			fieldHeight = input.length();
		}
	}
}