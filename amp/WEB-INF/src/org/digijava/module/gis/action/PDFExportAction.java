package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gis.action.PDFExportAction;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFExportAction extends Action implements PdfPageEvent {
	protected static Logger logger = Logger.getLogger(PDFExportAction.class);
	private HttpSession session = null;
	private String locale = null;
	private Site site = null;

	private PdfPTable contenTable;
	private HttpServletResponse response;

	public PDFExportAction(HttpSession session, String locale, Site site,
			HttpServletResponse response) {
		this.session = session;
		this.locale = locale;
		this.site = site;
		this.response = response;
	}

	public PDFExportAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		// for translation purposes
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);

		String siteId = site.getSiteId();
		String locale = navigationLanguage.getCode();

		HttpSession session = request.getSession();

		Rectangle page = PageSize.A4;
		Document document = new Document();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		// Get the Chart Image
		// TODO: GIS, this should come through parameter
		Long selectedDonor = null;
		Integer selectedYear = null;
		Boolean showLabels = true;
		Boolean showLegends = true;

		if (request.getParameter("selectedDonor") != null
				&& !request.getParameter("selectedDonor").equals("-1")) {
			selectedDonor = Long.parseLong(request
					.getParameter("selectedDonor"));
		}
		if (request.getParameter("selectedYear") != null
				&& request.getParameter("selectedYear") != "-1") {
			selectedYear = Integer.parseInt(request
					.getParameter("selectedYear"));
		}
		if (request.getParameter("showLabels") != null
				&& request.getParameter("showLabels") != "-1") {
			showLabels = Boolean.parseBoolean(request
					.getParameter("showLabels"));
		}
		if (request.getParameter("showLegends") != null
				&& request.getParameter("showLegends") != "-1") {
			showLegends = Boolean.parseBoolean(request
					.getParameter("showLegends"));
		}

		ByteArrayOutputStream outChartByteArray = getChartImage(request,
				selectedDonor, selectedYear, showLegends, showLabels);
		Image imgChart = Image.getInstance(outChartByteArray.toByteArray());

		// Get the Map Image
		ByteArrayOutputStream outMapByteArray = getMapImage("TZA"); // TODO:
		// GIS, this
		// should
		// come
		// through
		// parameter
		Image imgMap = Image.getInstance(outMapByteArray.toByteArray());

		// put the Chart and map on a table
		float[] widths1 = { 2f, 1f };
		PdfPTable imagesTable = new PdfPTable(widths1);
		imagesTable.setWidthPercentage(100);
		imagesTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		imagesTable.addCell(getImageMap(imgMap));
		imagesTable.addCell(getImageChart(imgChart));
//		imagesTable.addCell(" ");

		// First batch of widgets
		float[] layoutWidths = { 2f, 1f };
		PdfPTable layoutTable1 = new PdfPTable(layoutWidths);
		layoutTable1.setExtendLastRow(false);
		layoutTable1.setWidthPercentage(100);
		layoutTable1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		PdfPTable resourcesBox = getResourcesBox();
		PdfPTable mdgsBox = getMDGSBox();
		
		layoutTable1.addCell(mdgsBox);
		layoutTable1.addCell(resourcesBox);
		

		PdfPTable aeProcessIndicatorBox = getAEPIBox();
		PdfPCell tempCell = new PdfPCell(aeProcessIndicatorBox);
		tempCell.setColspan(2);
		layoutTable1.addCell(tempCell);
		
		PdfPTable EAResourcesBox = getEAResourcesBox();
		tempCell = new PdfPCell(EAResourcesBox);
		tempCell.setColspan(2);
		tempCell.setBorder(Rectangle.NO_BORDER);
		tempCell.setPaddingBottom(10);
		layoutTable1.addCell(tempCell);

		PdfPTable totalResourcesBox = getTotalResourcesBox();
		tempCell = new PdfPCell(totalResourcesBox);
		tempCell.setColspan(2);
		tempCell.setBorder(Rectangle.NO_BORDER);
		layoutTable1.addCell(tempCell);

		document.open();
		String countryName = "";
        String ISO = null;
        Iterator itr1 = FeaturesUtil.getDefaultCountryISO().iterator();
        while (itr1.hasNext()) {
          AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
          ISO = ampG.getGlobalSettingsValue();
        }
        
        if(ISO != null && !ISO.equals("")){
            Country cntry = DbUtil.getDgCountry(ISO);
            countryName = cntry.getCountryName();
        }
        else
        {
        	countryName = "";
        }

        Paragraph title = new Paragraph("Results Matrix: " + countryName, new Font(
				Font.HELVETICA, 24, Font.BOLD));
		Paragraph updateDate = new Paragraph("Generated on: "
				+ FormatHelper.formatDate(new Date(System.currentTimeMillis()))
				+ "\n\n", new Font(Font.HELVETICA, 6, Font.BOLDITALIC));

		document.add(title);
		document.add(updateDate);
		document.add(imagesTable);
		document.add(layoutTable1);
//		document.add(layoutTotalResources);
//		document.add(layoutExAidResources);

		document.close();
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		return null;
	}
	
	private PdfPTable getEAResourcesBox() {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Intermediate Output Indicators\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutExAidResourcesWidths = { 1f, 1f };
		PdfPTable layoutExAidResources = new PdfPTable(
				layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);

		PdfPTable table1ExAidResources = getWidgetTable("table_place6");
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");
		
		layoutCell.addElement(layoutExAidResources);
		generalBox.addCell(layoutCell);

		
		return generalBox;
	}

	
	private PdfPTable getTotalResourcesBox() {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Total Resources\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutTotalResourcesWidths = {1f };
		PdfPTable layoutTotalResources = new PdfPTable(
				layoutTotalResourcesWidths);
		layoutTotalResources.setWidthPercentage(100);
		layoutTotalResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPTable tableTotalResources1 = getWidgetTable("table_place4");
		layoutTotalResources.addCell(tableTotalResources1);
//		PdfPTable tableTotalResources2 = getWidgetTable("table_place6");
//		layoutTotalResources.addCell(tableTotalResources2);
		
		layoutCell.addElement(layoutTotalResources);
		generalBox.addCell(layoutCell);

		
		return generalBox;
	}

	private PdfPTable getAEPIBox() {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {1f,3f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Aid Effectiveness Process Indicators\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] layoutAEIndicatorsWidths = { 1f, 1f };
		PdfPTable layoutAEIndicators = new PdfPTable(layoutAEIndicatorsWidths);
		layoutAEIndicators.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutAEIndicators.setWidthPercentage(100);

		PdfPTable table1AEIndicators = getWidgetTable("table_place1");
		PdfPTable table2AEIndicators = getWidgetTable("table_place3");

		layoutAEIndicators.addCell(table1AEIndicators);
		layoutAEIndicators.addCell(table2AEIndicators);
		
		
		
		layoutCell.addElement(layoutAEIndicators);

		
		
		generalBox.addCell(layoutCell);

		
		return generalBox;
	}

	private PdfPTable getImageChart(Image imgChart) {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {2f,1f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Breakdown by sector\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));
		layoutCell.addElement(imgChart);
		generalBox.addCell(layoutCell);

		
		return generalBox;
	}

	private PdfPTable getImageMap(Image imgMap) {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {1f,2f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Regional View\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));
		layoutCell.addElement(imgMap);
		generalBox.addCell(layoutCell);

		
		return generalBox;
	}

	private PdfPTable getMDGSBox() {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {1f,2f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Millennium Development Goals\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] chartsWidths = { 1f, 1f, 1f };
		PdfPTable layoutCharts = new PdfPTable(chartsWidths);
		layoutCharts.getDefaultCell().setPadding(1);
		layoutCharts.setExtendLastRow(false);
		layoutCharts.setWidthPercentage(100f);


		try {
			// Get the Chart Images
			// TODO: make the magic numbers disappear. Magic numbers gone, see if
			// there's a better way
			ByteArrayOutputStream chart_place1 = getWidgetImage("chart_place1");
			if (chart_place1 != null)
				layoutCharts.addCell(Image.getInstance(chart_place1.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place2 = getWidgetImage("chart_place2");
			if (chart_place2 != null)
				layoutCharts.addCell(Image.getInstance(chart_place2.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place3 = getWidgetImage("chart_place3");
			if (chart_place3 != null)
				layoutCharts.addCell(Image.getInstance(chart_place3.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place4 = getWidgetImage("chart_place4");
			if (chart_place4 != null)
				layoutCharts.addCell(Image.getInstance(chart_place4.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place5 = getWidgetImage("chart_place5");
			if (chart_place5 != null)
				layoutCharts.addCell(Image.getInstance(chart_place5.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place6 = getWidgetImage("chart_place6");
			if (chart_place6 != null)
				layoutCharts.addCell(Image.getInstance(chart_place6.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		layoutCell.addElement(layoutCharts);

		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getResourcesBox() {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);
		
		//Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);
		
		float[] widths = {2f,1f};
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		//Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph("Resources at a glance\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255,255,255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		
		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		//Add rounded tab
		headerTable.addCell(firstCell);
		//Add empty space
		headerTable.addCell(secondCell);

		//add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		//add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);

		//Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206,226,251));

		float[] resourcesAtAGlanceWidths = { 2f, 1f };
		PdfPTable layoutResourcesAtAGlance = new PdfPTable(
				resourcesAtAGlanceWidths);
		layoutResourcesAtAGlance.setWidthPercentage(100f);
		layoutResourcesAtAGlance.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlance.getDefaultCell().setPadding(0);
		layoutResourcesAtAGlance.setExtendLastRow(false);

		PdfPTable layoutResourcesAtAGlanceTable1 = getWidgetTable("atGlanceTable_Place1");
		PdfPTable layoutResourcesAtAGlanceTable2 = getWidgetTable("atGalnceTable_Place2");
		PdfPTable layoutResourcesAtAGlanceTable3 = getWidgetTable("atGlanceTable_Place3");

		PdfPTable layoutResourcesAtAGlanceTable1and2 = new PdfPTable(1);
		layoutResourcesAtAGlanceTable1and2.setExtendLastRow(false);
		layoutResourcesAtAGlanceTable1and2.getDefaultCell().setBorder(
				PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable1and2
				.addCell(layoutResourcesAtAGlanceTable1);
		layoutResourcesAtAGlanceTable1and2
				.addCell(layoutResourcesAtAGlanceTable2);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable1and2);

		PdfPTable layoutResourcesAtAGlanceTable3alone = new PdfPTable(1);
		layoutResourcesAtAGlanceTable3.setExtendLastRow(false);
		layoutResourcesAtAGlanceTable3alone.getDefaultCell().setBorder(
				PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable3alone
				.addCell(layoutResourcesAtAGlanceTable3);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable3alone);
		layoutCell.addElement(layoutResourcesAtAGlance);
		
		generalBox.addCell(layoutCell);
		
		//Make the innerLayout
		return generalBox;
	}

	private PdfPTable getWidgetTable(String codePlace) {
		try {

			String code = codePlace;

			AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
			AmpWidget widget = place.getAssignedWidget();
			if (widget == null)
				return null;

			WiTable table = new WiTable.TableBuilder(widget.getId()).build();

			List<WiColumn> columns = table.getColumns();
			List<WiRow> rows = table.getDataRows();

			PdfPTable pdfTable = new PdfPTable(columns.size());
			pdfTable.setExtendLastRow(false);
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);
			fontHeader.setColor(new Color(255, 255, 255));

			Font fontCell = new Font();
			fontCell.setSize(4);

			for (WiColumn column : columns) {
				PdfPCell cell = new PdfPCell(new Phrase(column.getName(),
						fontHeader));
				cell.setBackgroundColor(new Color(34, 46, 93));

				pdfTable.addCell(cell);
			}

			int counter = 0;
			for (WiRow row : rows) {
				List<WiCell> cells = row.getCells();
				Color cellColor;
				if (counter % 2 == 0)
					cellColor = new Color(255, 255, 255);
				else
					cellColor = new Color(219, 229, 241);
				counter++;

				for (WiCell cell : cells) {
					PdfPCell cellPdf = new PdfPCell(new Phrase(cell.getValue(),
							fontCell));
					cellPdf.setBackgroundColor(cellColor);

					pdfTable.addCell(cellPdf);
				}

			}
			return pdfTable;
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private ByteArrayOutputStream getMapImage(String mapCode) {
		GisUtil gisUtil = new GisUtil();
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode);
		}

		int canvasWidth = 700;
		int canvasHeight = 700;

		CoordinateRect rect = gisUtil.getMapRect(map);

		BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = graph.createGraphics();

		g2d.setBackground(new Color(0, 0, 100, 255));

		g2d.clearRect(0, 0, canvasWidth, canvasHeight);

		gisUtil.addDataToImage(g2d, map.getSegments(), -1, canvasWidth,
				canvasHeight, rect.getLeft(), rect.getRight(), rect.getTop(),
				rect.getBottom(), true);

		gisUtil.addCaptionsToImage(g2d, map.getSegments(), canvasWidth,
				canvasHeight, rect.getLeft(), rect.getRight(), rect.getTop(),
				rect.getBottom());

		g2d.dispose();

		RenderedImage ri = graph;

		try {
			ImageIO.write(ri, "png", outByteStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		graph.flush();

		return outByteStream;
	}

	public void onChapter(PdfWriter arg0, Document arg1, float arg2,
			Paragraph arg3) {
		// TODO Auto-generated method stub

	}

	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onCloseDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onEndPage(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3,
			Paragraph arg4) {
		// TODO Auto-generated method stub

	}

	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public void onStartPage(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub

	}

	public ByteArrayOutputStream getChartImage(HttpServletRequest request,
			Long donorId, Integer year, Boolean showLegends, Boolean showLabels)
			throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		Long[] donorIDs = null;
		if (donorId != null) {
			donorIDs = new Long[1];
			donorIDs[0] = donorId;
		}
		GregorianCalendar cal = new GregorianCalendar();
		if (year == null)
			year = new Integer(cal.get(Calendar.YEAR));

		ChartOption opt = new ChartOption();
		opt.setShowTitle(true);
		opt.setShowLegend(showLegends);
		opt.setShowLabels(showLabels);
		opt.setHeight(new Integer(660));
		opt.setWidth(new Integer(420));
		Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
		opt.setSiteId(siteId);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		opt.setLangCode(langCode);

		// generate chart
		JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(donorIDs,
				year, opt);
		ChartRenderingInfo info = new ChartRenderingInfo();

		// write image in response

		ChartUtilities.writeChartAsJPEG(outByteStream, chart, opt.getWidth()
				.intValue(), opt.getHeight().intValue(), info);

		return outByteStream;
	}

	public ByteArrayOutputStream getWidgetImage(String codePlace)
			throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		String code = codePlace;

		AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
		AmpWidget widget = place.getAssignedWidget();
		AmpWidgetIndicatorChart cWidget = (AmpWidgetIndicatorChart) widget;
		if (widget == null)
			return null;
		// if (widgetId != null) {
		// widget = ChartWidgetUtil.getIndicatorChartWidget(widgetId);
		// } else {
		// // System.out.println("No chart assigned to this teaser!");//TODO
		// this should go to form as error message.
		// return null;
		// }
		ChartOption opt = new ChartOption();

		opt.setShowTitle(true);
		if (widget != null) {
			opt.setTitle(widget.getName());
		}
		opt.setShowLegend(false);
		opt.setShowLabels(false);
		opt.setHeight(new Integer(160));
		opt.setWidth(new Integer(220));
		IndicatorSector indicatorCon = cWidget.getIndicator();

		if (indicatorCon != null) {
			// generate chart
			JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicatorCon,
					opt);
			ChartRenderingInfo info = new ChartRenderingInfo();
			// write image in response
			ChartUtilities.writeChartAsJPEG(outByteStream, chart, opt
					.getWidth().intValue(), opt.getHeight().intValue(), info);
		}
		return outByteStream;
	}

	class MyContentByte extends PdfContentByte {

		public MyContentByte(PdfWriter wr) {
			super(wr);
		}
		/**
		 * Adds a round rectangle to the current path.
		 * 
		 * @param x
		 *            x-coordinate of the starting point
		 * @param y
		 *            y-coordinate of the starting point
		 * @param w
		 *            width
		 * @param h
		 *            height
		 * @param r
		 *            radius of the arc corner
		 */

	}


	class RoundRectangle implements PdfPCellEvent {
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			cb.setColorStroke(new Color(255, 255, 255));
			cb.setColorFill(new Color(34, 46, 93));
			roundRectangleUpper(cb, rect.getLeft(), rect.getBottom(), rect
					.getWidth()-2, rect.getHeight(), 4);
			cb.fill();
		}

		public void roundRectangleUpper(PdfContentByte cb, float x, float y,
				float w, float h, float r) {
			if (w < 0) {
				x += w;
				w = -w;
			}
			if (h < 0) {
				y += h;
				h = -h;
			}
			if (r < 0)
				r = -r;
			float b = 0.4477f;
			cb.moveTo(x + w, y);
			cb.lineTo(x + w, y + h - r);
			cb.curveTo(x + w, y + h - r * b, x + w - r * b, y + h, x + w - r, y
					+ h);
			cb.lineTo(x + r, y + h);
			cb.curveTo(x + r * b, y + h, x, y + h - r * b, x, y + h - r);
			cb.lineTo(x, y);
//			cb.curveTo(x, y + r * b, x + r * b, y, x + r, y);
		}
	}
}
