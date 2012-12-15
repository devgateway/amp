package org.digijava.module.mondrian.serverlet;
/**
 * @author Diego DImunzio
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.mondrian.query.QueryThread;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

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
				String currency = QueryThread.getCurrency();
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
			            if (chart != null && chart.isVisible()) {

			              String host = request.getServerName();
			              int port = request.getServerPort();
			              String location = request.getContextPath();
			              String scheme = request.getScheme();

			              String chartServlet = scheme + "://" + host + ":" + port + location + "/GetChart.out";
			              parameters.put("chartimage", chartServlet + "?filename=" + chart.getFilename());
			              parameters.put("chartheight", new Integer(chart.getChartHeight()));
			              parameters.put("chartwidth", new Integer(chart.getChartWidth()));
			            }
						
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
							ExporterHelper helper = new ExporterHelper(wb);
							
							NodeList rowslist = document.getElementsByTagName("row");
							
							String textamount = "";
							String textcurrency = "";
							String currencytext = null;
							//for translation purposes
							Site site = QueryThread.getSite();
							Locale navigationLanguage = QueryThread.getLocale();
							
							if (site != null && location !=null){
								textamount = AmpReports.getNote(request.getSession());
							
								if(currency!= null) {
									AmpCurrency currobj = CurrencyUtil.getCurrencyByCode(currency);
									if (currobj.getCountryName()!=null){
										textcurrency=TranslatorWorker.translateText(currobj.getCountryName());
									} else {
										textcurrency = currobj.getCurrencyName();
									}
									currencytext = TranslatorWorker.translateText("Currency");
								}
							
								helper.addMergenoBorder(rowslist.getLength()+1, 0, rowslist.getLength()+1, 1);
								HSSFRow currencyrow = wb.getSheet("data").createRow((short) (rowslist.getLength()+1));
								helper.addCellBlue(textamount + " - " + currencytext +" : " + textcurrency,0 , currencyrow);
							}
							
							
							for (int i = 0; i < rowslist.getLength(); i++) {
								HSSFRow row = wb.getSheet("data").createRow((short) (i));
								NodeList rownode = rowslist.item(i).getChildNodes();
								
								for (int j = 0; j < rownode.getLength(); j++) {
									Node currentnode = rownode.item(j);
									int rowspan=0;
									int colspan =0;
									if (currentnode.getNodeName().equalsIgnoreCase("corner")){
										rowspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("rowspan").getNodeValue());
										colspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("colspan").getNodeValue());
										if (colspan >1){
											helper.addMerge(i, j, rowspan-1, colspan-1);
											for (int k = i; k < rowspan; k++) {
												for (int k2 = 0; k2 < rowslist.item(k).getChildNodes().getLength(); k2++) {
													Element element = (Element)rowslist.item(k).getChildNodes().item(k2);
													Attr offset = document.createAttribute("offset");
													offset.setNodeValue(currentnode.getAttributes().getNamedItem("colspan").getNodeValue());
													element.setAttributeNode(offset);
												}
											}
										}
									}else if (currentnode.getNodeName().equalsIgnoreCase("column-heading")){
										rowspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("rowspan").getNodeValue());
										colspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("colspan").getNodeValue());
										if (colspan >1){
											//Temporal Fix for AMP-6656 TODO :Review the exportation process.
											if (i>rowspan)rowspan = i;
											if (j>colspan)rowspan = j;
											helper.addMerge(i, j, rowspan, colspan);
										}
										int colid =j;
										if(currentnode.getAttributes().getNamedItem("offset")!=null){
											colid = Integer.parseInt(currentnode.getAttributes().getNamedItem("offset").getNodeValue())+j;
										}
										String text = currentnode.getFirstChild().getAttributes().getNamedItem("caption").getNodeValue();
										helper.addCaption(text, colid, row);
										
									}else if (currentnode.getNodeName().equalsIgnoreCase("row-heading")){
										int colid =j;
										rowspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("rowspan").getNodeValue());
										colspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("colspan").getNodeValue());
										if (rowspan >1){
											helper.addMerge(i, j, (rowspan+i)-1, j);
											for (int k = i+1; k < rowspan+i; k++) {
												for (int k2 = 0; k2 < rowslist.item(k).getChildNodes().getLength(); k2++) {
													Element element = (Element)rowslist.item(k).getChildNodes().item(k2);
													Integer offsetint = k2+1;
													if (element.getAttribute("offset")==null || element.getAttribute("offset")==""){
														Attr offset = document.createAttribute("offset");
														offset.setNodeValue(offsetint.toString());
														element.setAttributeNode(offset);
													}else{
														int oldcolid = Integer.parseInt(element.getAttribute("offset"));
														Integer newcolid = oldcolid + 1;
														element.setAttribute("offset", newcolid.toString());
													}
												}
											}
										}
										if(currentnode.getAttributes().getNamedItem("offset")!=null){
											colid = Integer.parseInt(currentnode.getAttributes().getNamedItem("offset").getNodeValue());
										}
										String text = currentnode.getFirstChild().getAttributes().getNamedItem("caption").getNodeValue();
										helper.addCaption(text, colid, row);
										
									}else if (currentnode.getNodeName().equalsIgnoreCase("heading-heading")){
										int colid =j;
										if(currentnode.getAttributes().getNamedItem("offset")!=null){
											colid = Integer.parseInt(currentnode.getAttributes().getNamedItem("offset").getNodeValue())+j;
										}
										rowspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("rowspan").getNodeValue());
										colspan = Integer.parseInt(currentnode.getAttributes().getNamedItem("colspan").getNodeValue());
										
										String text = currentnode.getFirstChild().getAttributes().getNamedItem("caption").getNodeValue();
										helper.addCaption(text, colid, row);
										
									}else if (currentnode.getNodeName().equalsIgnoreCase("cell")){
										int colid =j;
										if(currentnode.getAttributes().getNamedItem("offset")!=null){
											colid = Integer.parseInt(currentnode.getAttributes().getNamedItem("offset").getNodeValue());
										}
										if(currentnode.getAttributes().getNamedItem("rawvalue")!=null){
											helper.addCell(currentnode.getAttributes().getNamedItem("rawvalue").getNodeValue(), colid, row);
										}else{
											helper.addCell("0", colid, row);
										}
									}
									
									
										sheet.autoSizeColumn((short)j);
									}
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
								HSSFClientAnchor anchor =  new HSSFClientAnchor();
								anchor.setCol1((short)0);
								anchor.setRow1(30);
								int index = wb.addPicture(img_bytes.toByteArray(),HSSFWorkbook.PICTURE_TYPE_PNG);
								HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
								HSSFPicture pic = patriarch.createPicture(anchor, index);
								pic.resize();
							
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
