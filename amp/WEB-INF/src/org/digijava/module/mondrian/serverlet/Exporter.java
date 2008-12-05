package org.digijava.module.mondrian.serverlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.digijava.module.aim.helper.FormatHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tonbeller.jpivot.chart.ChartComponent;
import com.tonbeller.jpivot.print.PrintComponent;
import com.tonbeller.jpivot.print.PrintServlet;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.wcf.component.RendererParameters;
import com.tonbeller.wcf.controller.RequestContext;
import com.tonbeller.wcf.controller.RequestContextFactoryFinder;
import com.tonbeller.wcf.utils.XmlUtils;

public class Exporter extends com.tonbeller.jpivot.print.PrintServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6087209070122985886L;
	private static Logger logger = Logger.getLogger(PrintServlet.class);
	private static final int XML = 0;
	private static final int PDF = 1;
	String basePath;
	String filename;

	protected void processRequest(RequestContext context)
			throws ServletException, IOException {
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		if (request.getParameter("cube") != null
				&& request.getParameter("type") != null) {
			try {
				String xslUri = null;
				int type = Integer.parseInt(request.getParameter("type"));
				switch (type) {
				case XML:
					xslUri = "/WEB-INF/jpivot/table/xls_mdxtable.xsl";
					RendererParameters.setParameter(context.getRequest(),"mode", "excel", "request");
					response.setContentType("application/vnd.ms-excel");
					filename = "xls_export.xls";
					break;
				case PDF:
					xslUri = "/WEB-INF/jpivot/table/fo_mdxtable.xsl";
					RendererParameters.setParameter(context.getRequest(),"mode", "print", "request");
					response.setContentType("application/pdf");
					filename = "xls_export.pdf";
					break;
				}
				if (xslUri != null) {
					boolean xslCache = true;
					// get references to needed elements
					String tableRef = "table" + request.getParameter("cube");
					String chartRef = "chart" + request.getParameter("cube");
					String printRef = "print" + request.getParameter("cube");

					Map parameters = new HashMap();

					OutputStream outStream = response.getOutputStream();
					PrintWriter out = new PrintWriter(outStream);
					HttpSession session = request.getSession();
					// set up filename for download.
					response.setHeader("Content-Disposition","attachment; filename=" + filename);

					// get TableComponent
					TableComponent table = (TableComponent) context.getModelReference(tableRef);
					// only proceed if table component exists
					if (table != null) {
						// add parameters from printConfig
						PrintComponent printConfig = (PrintComponent) context.getModelReference(printRef);
						if (printConfig != null) {
							if (printConfig.isSetTableWidth()) {
								parameters.put(printConfig.PRINT_TABLE_WIDTH,new Double(printConfig.getTableWidth()));
							}
							if (printConfig.getReportTitle().trim().length() != 0) {
								parameters.put(printConfig.PRINT_TITLE,printConfig.getReportTitle().trim());
							}
							parameters.put(printConfig.PRINT_PAGE_ORIENTATION,printConfig.getPageOrientation());
							parameters.put(printConfig.PRINT_PAPER_TYPE,printConfig.getPaperType());
							if (printConfig.getPaperType().equals("custom")) {
								parameters.put(printConfig.PRINT_PAGE_WIDTH,new Double(printConfig.getPageWidth()));
								parameters.put(printConfig.PRINT_PAGE_HEIGHT,new Double(printConfig.getPageHeight()));
							}
							parameters.put(printConfig.PRINT_CHART_PAGEBREAK,new Boolean(printConfig.isChartPageBreak()));
							}

						// add parameters and image from chart if visible
						ChartComponent chart = (ChartComponent) request.getSession().getAttribute(chartRef);
					
						parameters.put("context", context.getRequest().getContextPath());

						// Some FOP-PDF versions require a complete URL, not a path
						//parameters.put("contextUrl", createContextURLValue(context));

						table.setDirty(true);
						Document document = table.render(context);
						table.setDirty(true);
						DOMSource source = new DOMSource(document);
						// set up xml transformation
						Transformer transformer = XmlUtils.getTransformer(
								session, xslUri, xslCache);
						for (Iterator it = parameters.keySet().iterator(); it
								.hasNext();) {
							String name = (String) it.next();
							Object value = parameters.get(name);
							transformer.setParameter(name, value);
						}
						StringWriter sw = new StringWriter();
						StreamResult result = new StreamResult(sw);
						//do transform
						transformer.transform(source, result);
						sw.flush();

						if (type == XML) {
							String host = request.getServerName();
							OutputStream outstr = response.getOutputStream();
							int port = request.getServerPort();
							String location = request.getContextPath();
							String scheme = request.getScheme();
						
							HSSFWorkbook wb = new HSSFWorkbook();
							HSSFSheet thesheet = wb.createSheet("data");
							FileOutputStream fos = new FileOutputStream(filename);
							HSSFSheet sheet = wb.getSheet("data");
							HSSFRichTextString str = null;
							
							HSSFFont frowheading = wb.createFont();
							frowheading.setFontName("Arial Unicode MS");
							frowheading.setFontHeightInPoints((short)8);
							frowheading.setBoldweight(frowheading.BOLDWEIGHT_BOLD);
							HSSFCellStyle rowheading = wb.createCellStyle();
							rowheading.setFont(frowheading);
							rowheading.setBorderLeft(HSSFCellStyle.BORDER_THIN);
							rowheading.setBorderRight(HSSFCellStyle.BORDER_THIN);
							rowheading.setBorderTop(HSSFCellStyle.BORDER_THIN);
							rowheading.setBorderBottom(HSSFCellStyle.BORDER_THIN);
							rowheading.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
							
							HSSFFont fdataitem = wb.createFont();
							fdataitem.setFontName("Arial Unicode MS");
							fdataitem.setFontHeightInPoints((short)8);
							fdataitem.setBoldweight(frowheading.BOLDWEIGHT_NORMAL);
							HSSFCellStyle dataitem = wb.createCellStyle();
							dataitem.setFont(fdataitem);
							dataitem.setBorderLeft(HSSFCellStyle.BORDER_THIN);
							dataitem.setBorderRight(HSSFCellStyle.BORDER_THIN);
							dataitem.setBorderTop(HSSFCellStyle.BORDER_THIN);
							dataitem.setBorderBottom(HSSFCellStyle.BORDER_THIN);
							dataitem.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
							boolean addrow = false;
							int nrowspan=0;
							int lastrow=0;
							int lasti=0;
							NodeList rowslist = document.getElementsByTagName("row");
							for (int i = 0; i < rowslist.getLength(); i++) {
								HSSFRow row = sheet.createRow((short)(i));
								for (int j = 0; j < rowslist.item(i).getChildNodes().getLength(); j++) {
									HSSFCell cell=null;
									for (int k = 0; k < rowslist.item(i).getChildNodes().item(j).getChildNodes().getLength(); k++) {
										if (addrow &&  i >lasti && i <=((nrowspan-1) +lasti)){
											cell = row.createCell((short)(j+1));
										}else{
											cell = row.createCell((short)j);
										}
										str = new HSSFRichTextString(rowslist.item(i).getChildNodes().item(j).getChildNodes().item(k).getAttributes().item(0).getNodeValue());
										String style = rowslist.item(i).getChildNodes().item(j).getAttributes().getNamedItem("style").getNodeValue();
										String rowspan = rowslist.item(i).getChildNodes().item(j).getAttributes().getNamedItem("rowspan").getNodeValue();
										String colspan = rowslist.item(i).getChildNodes().item(j).getAttributes().getNamedItem("colspan").getNodeValue();
										if (Integer.parseInt(rowspan)>1){
											addrow = true;
											nrowspan=Integer.parseInt(rowspan);
											lasti = i;
										}
										cell.setCellValue (str);
										cell.setCellStyle(rowheading);
										for (int l = 0; l < rowslist.item(i).getChildNodes().getLength(); l++) {
											HSSFCell celldata=null;
											if (rowslist.item(i).getChildNodes().item(l).getNodeName().equalsIgnoreCase("cell")){
												if (addrow &&  i >lasti && i <=((nrowspan-1) +lasti)){
													celldata = row.createCell((short)(l+1));
												}else{
													celldata = row.createCell((short)(l));	
												}
												str = new HSSFRichTextString(rowslist.item(i).getChildNodes().item(l).getAttributes().item(3).getNodeValue());
												HSSFDataFormat nformat = wb.createDataFormat();
												celldata.setCellValue(str);
												celldata.setCellType(HSSFCell.CELL_TYPE_STRING);
												celldata.setCellStyle(dataitem);
												sheet.autoSizeColumn((short)l);
											}
										}
										sheet.autoSizeColumn((short)j);
									}
								}
								lastrow = i;
							}
							//insert the chart into the excel file 
							
							if (chart != null && chart.isVisible()) {
								String chartServlet = scheme + "://" + host + ":" + port + location + "/GetChart.out";
								java.io.InputStream fis;
								fis = new URL(chartServlet + "?filename="+ chart.getFilename()).openStream();
								ByteArrayOutputStream img_bytes = new ByteArrayOutputStream();
								int b;
								while ((b = fis.read()) != -1)
									img_bytes.write(b);
								fis.close();
								lastrow = lastrow+4;
								HSSFClientAnchor anchor = new HSSFClientAnchor();
								anchor.setRow1(lastrow);
								int index = wb.addPicture(img_bytes.toByteArray(),HSSFWorkbook.PICTURE_TYPE_PNG);
								HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
								HSSFPicture pic = patriarch.createPicture(anchor, index);
								pic.resize();
								
								anchor.setAnchorType(2);
								wb.write(fos);
								fos.close();
							}
							//logger.info(sw.toString());
							wb.write(outstr);
							
							/*
							System.out.println("Writing XLS");
							response.setContentLength(sw.toString().length());
							out.write(sw.toString());
							*/
							RendererParameters.removeParameter(context.getRequest(), "mode", "excel", "request");
							// process FO to PDF
						} else {
							// if this is PDF, then need to generate PDF from the FO xml
							System.out.println("Creating PDF!");
							try {
								ByteArrayInputStream bain = new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
								ByteArrayOutputStream baout = new ByteArrayOutputStream(16384);
								convertFO2PDF(bain, baout);
								final byte[] content = baout.toByteArray();
								response.setContentLength(content.length);
								outStream.write(content);
								RendererParameters.removeParameter(context.getRequest(), "mode", "print","request");
								//convertXML2PDF(document.toString(), xslUri, outStream);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//close output streams
						out.flush();
						out.close();
						outStream.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String indentstr(int indent){
		String str="";
		for (int i = 0; i < indent; i++) {
			 str = "  " + str;
		}
		return str;
	}

	/**
	 * converts FO xml into PDF using the FOP processor
	 */
	public void convertFO2PDF(ByteArrayInputStream bain,
			ByteArrayOutputStream baout) throws IOException, FOPException {

		System.out.println("Construct driver");
		Driver driver = new Driver();

		System.out.println("Setup Renderer (output format)");
		driver.setRenderer(Driver.RENDER_PDF);

		try {
			driver.setOutputStream(baout);
			System.out.println("Setup input");
			try {
				driver.setInputSource(new InputSource(bain));

				System.out.println("Process FO");
				driver.run();
				System.out.println("PDF file generation completed");
			} finally {
			}
		} finally {
		}
	}

	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/** Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestContext context = RequestContextFactoryFinder.createContext(
				request, response, true);
		try {
			processRequest(context);
		} finally {
			context.invalidate();
		}
	}

	/** Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Export OLAP table";
	}

}
