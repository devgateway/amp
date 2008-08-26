package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.IndicatorSector;
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
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
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

		Rectangle page=PageSize.A4;
		Document document = new Document();

		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		
		//Get the Chart Image
		 //TODO: GIS, this should come through parameter
		ByteArrayOutputStream outChartByteArray = getChartImage(request);
		Image imgChart = Image.getInstance(outChartByteArray.toByteArray());
		
		//Get the Map Image
		ByteArrayOutputStream outMapByteArray = getMapImage("TZA"); //TODO: GIS, this should come through parameter
		Image imgMap = Image.getInstance(outMapByteArray.toByteArray());
		
		//put the Chart and map on a table
		float[] widths1 = { 2f, 1f};
		PdfPTable imagesTable = new PdfPTable(widths1);
		imagesTable.setWidthPercentage(100);
		imagesTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		imagesTable.addCell(imgMap);
		imagesTable.addCell(imgChart);


		//First batch of widgets
		float[] layoutWidths = { 2f, 1f};
		PdfPTable layoutTable1 = new PdfPTable(layoutWidths);
		layoutTable1.setWidthPercentage(100);
		layoutTable1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		float[] chartsWidths = { 1f, 1f, 1f};
		PdfPTable layoutCharts = new PdfPTable(chartsWidths);
		layoutCharts.getDefaultCell().setPadding(1);

		//Get the Chart Images
		//TODO: make the magic numbers disappear. Magic numbers gone, see if there's a better way
		ByteArrayOutputStream chart_place1 = getWidgetImage("chart_place1");
		if(chart_place1 != null)
			layoutCharts.addCell(Image.getInstance(chart_place1.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));
		
		ByteArrayOutputStream chart_place2 = getWidgetImage("chart_place2");
		if(chart_place2 != null)
			layoutCharts.addCell(Image.getInstance(chart_place2.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));
		
		ByteArrayOutputStream chart_place3 = getWidgetImage("chart_place3");
		if(chart_place3 != null)
			layoutCharts.addCell(Image.getInstance(chart_place3.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));

		ByteArrayOutputStream chart_place4 = getWidgetImage("chart_place4");
		if(chart_place4 != null)
			layoutCharts.addCell(Image.getInstance(chart_place4.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));
		
		ByteArrayOutputStream chart_place5 = getWidgetImage("chart_place5");
		if(chart_place5 != null)
			layoutCharts.addCell(Image.getInstance(chart_place5.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));
		
		ByteArrayOutputStream chart_place6 = getWidgetImage("chart_place6");
		if(chart_place6 != null)
			layoutCharts.addCell(Image.getInstance(chart_place6.toByteArray()));
		else
			layoutCharts.addCell(new Phrase("Empty"));

		float[] resourcesAtAGlanceWidths = { 2f, 1f};
		PdfPTable layoutResourcesAtAGlance = new PdfPTable(resourcesAtAGlanceWidths);
		layoutResourcesAtAGlance.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlance.getDefaultCell().setPadding(0);
		
		//TODO: make the magic numbers disappear
		//TODO: GIS, this should come through parameters or at least constants
		PdfPTable layoutResourcesAtAGlanceTable1 = getWidgetTable("atGlanceTable_Place1");
		PdfPTable layoutResourcesAtAGlanceTable2 = getWidgetTable("atGalnceTable_Place2");
		PdfPTable layoutResourcesAtAGlanceTable3 = getWidgetTable("atGlanceTable_Place3");

		PdfPTable layoutResourcesAtAGlanceTable1and2 = new PdfPTable(1);
		layoutResourcesAtAGlanceTable1and2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable1and2.addCell(layoutResourcesAtAGlanceTable1);
		layoutResourcesAtAGlanceTable1and2.addCell(layoutResourcesAtAGlanceTable2);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable1and2);

		PdfPTable layoutResourcesAtAGlanceTable3alone = new PdfPTable(1);
		layoutResourcesAtAGlanceTable3alone.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable3alone.addCell(layoutResourcesAtAGlanceTable3);

		layoutResourcesAtAGlance.addCell(layoutResourcesAtAGlanceTable3alone);

		layoutTable1.addCell(layoutCharts);
		layoutTable1.addCell(layoutResourcesAtAGlance);
		
		float[] layoutAEIndicatorsWidths = { 1f, 1f};
		PdfPTable layoutAEIndicators = new PdfPTable(layoutAEIndicatorsWidths);
		layoutAEIndicators.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutAEIndicators.setWidthPercentage(100);

		PdfPTable table1AEIndicators = getWidgetTable("table_place1");
		PdfPTable table2AEIndicators =  getWidgetTable("table_place2");

		layoutAEIndicators.addCell(table1AEIndicators);
		layoutAEIndicators.addCell(table2AEIndicators);

		float[] layoutTotalResourcesWidths = { 1f };
		PdfPTable layoutTotalResources = new PdfPTable(layoutTotalResourcesWidths);
		layoutTotalResources.setWidthPercentage(100);
		layoutTotalResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		

		
		PdfPTable tableTotalResources = getWidgetTable("table_place3");
		layoutTotalResources.addCell(tableTotalResources);

		float[] layoutExAidResourcesWidths = { 1f,  1f };
		PdfPTable layoutExAidResources = new PdfPTable(layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);
		

		PdfPTable table1ExAidResources = getWidgetTable("table_place4");
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");

		document.open();
		document.add(imagesTable);
		document.add(layoutTable1);
		document.add(layoutAEIndicators);
		document.add(layoutTotalResources);
		document.add(layoutExAidResources);
		
		document.close();
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		return null;
	}

	private PdfPTable getWidgetTable(String codePlace) {
		try {

			String code = codePlace;

			AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
			AmpWidget widget = place.getAssignedWidget();
			if(widget == null)
				return null;
			
			
			WiTable table = new WiTable.TableBuilder(widget.getId()).build();

			String tableName = table.getName();
			
			List<WiColumn> columns = table.getColumns();
			List<WiRow> rows = table.getDataRows();
			

			PdfPTable pdfTable = new PdfPTable(columns.size());
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);

			Font fontCell = new Font();
			fontCell.setSize(5);

			for (WiColumn column : columns){
				pdfTable.addCell(new Phrase(column.getName(), fontHeader));
			}
			

			for (WiRow row : rows){
				List<WiCell> cells = row.getCells();
				for(WiCell cell : cells){
					pdfTable.addCell(new Phrase(cell.getValue(), fontCell));
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

        gisUtil.addDataToImage(g2d,
                               map.getSegments(),
                               -1,
                               canvasWidth, canvasHeight,
                               rect.getLeft(), rect.getRight(),
                               rect.getTop(), rect.getBottom(), true);

        gisUtil.addCaptionsToImage(g2d,
                                   map.getSegments(),
                                   canvasWidth, canvasHeight,
                                   rect.getLeft(), rect.getRight(),
                                   rect.getTop(), rect.getBottom());

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
	
	public ByteArrayOutputStream getChartImage(HttpServletRequest request) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		Integer year = null;
		Long[] donorIDs = null;
		GregorianCalendar cal = new GregorianCalendar();
		year = new Integer(cal.get(Calendar.YEAR));
		ChartOption opt = new ChartOption();
		opt.setShowTitle(true);
		opt.setShowLegend(true);
		opt.setShowLabels(true);
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
	
	public ByteArrayOutputStream getWidgetImage(String codePlace) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();


		String code = codePlace;

		AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
		AmpWidget widget = place.getAssignedWidget();
		AmpWidgetIndicatorChart cWidget = (AmpWidgetIndicatorChart) widget;
		if(widget == null)
			return null;
//		if (widgetId != null) {
//			widget = ChartWidgetUtil.getIndicatorChartWidget(widgetId);
//		} else {
//			// System.out.println("No chart assigned to this teaser!");//TODO this should go to form as error message.
//			return null;
//		}
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
			JFreeChart chart = ChartWidgetUtil.getIndicatorChart(indicatorCon,opt);
			ChartRenderingInfo info = new ChartRenderingInfo();
			// write image in response
			ChartUtilities.writeChartAsJPEG(
					outByteStream, 
					chart,
					opt.getWidth().intValue(), 
					opt.getHeight().intValue(), 
					info);
		}
		return outByteStream;		
	}	

}
