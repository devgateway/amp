package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lowagie.text.pdf.PdfTable;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.util.*;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpParisIndicatorBaseTargetValues;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.dbentity.AmpWidgetTopTenDonorGroups;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.SectorTableHelper;
import org.digijava.module.widget.helper.TopDonorGroupHelper;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.table.filteredColumn.WiColumnDropDownFilter;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.ParisIndicatorTableWidgetUtil;
import org.digijava.module.widget.util.SectorTableWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

public class PDFExportAction extends Action implements PdfPageEvent {
	protected static Logger logger = Logger.getLogger(PDFExportAction.class);
	private HttpSession session = null;
	private String locale = null;
	private Site site = null;
	private Locale navigationLanguage = null;

	private PdfPTable contenTable;
	private HttpServletResponse response;

	private Long[] tableId = null;
	private Long[] columnId = null;
	private Long[] itemId = null;
	private Long siteId;

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
		this.site = RequestUtils.getSite(request);
		this.navigationLanguage = RequestUtils.getNavigationLanguage(request);

		this.siteId = site.getId();
		this.locale = navigationLanguage.getCode();

		HttpSession session = request.getSession();

		Rectangle page = PageSize.A4;
		Document document = new Document();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		// This sets up default values for options in the dashboard
		Long selectedDonor = null;
		Integer selectedFromYear = null;
		Integer selectedTotYear = null;
		Boolean showLabels = true;
		Boolean showLegends = true;
		String selectedDonorName = "";
		boolean publicMode = false;

        Object[] filterResults = null;

		if (request.getParameter("publicMode") != null
				&& request.getParameter("publicMode").equals("true")) {
			publicMode = true;
		}

		// Breakdown by sector parameters

		if (request.getParameter("selectedDonor") != null
				&& !request.getParameter("selectedDonor").equals("-1")) {
			selectedDonor = Long.parseLong(request
					.getParameter("selectedDonor"));
		}
		if (request.getParameter("selectedDonorName") != null
				&& !request.getParameter("selectedDonorName").equals("-1")) {
			selectedDonorName = URLDecoder.decode(
					request.getParameter("selectedDonorName"), "UTF-8");
		}
		if (request.getParameter("selectedFromYear") != null
				&& !request.getParameter("selectedFromYear").equals("-1")) {
			selectedFromYear = Integer.parseInt(request
					.getParameter("selectedFromYear"));
		}
		if (request.getParameter("selectedToYear") != null
				&& !request.getParameter("selectedToYear").equals("-1")) {
			selectedTotYear = Integer.parseInt(request
					.getParameter("selectedToYear"));
		}
		if (request.getParameter("showLabels") != null
				&& !request.getParameter("showLabels").equals("-1")) {
			showLabels = Boolean.parseBoolean(request
					.getParameter("showLabels"));
		}
		if (request.getParameter("showLegends") != null
				&& !request.getParameter("showLegends").equals("-1")) {
			showLegends = Boolean.parseBoolean(request
					.getParameter("showLegends"));
		}

		ByteArrayOutputStream outChartByteArray = getChartImage(request,
				selectedDonor, selectedFromYear, selectedTotYear, showLegends,
				showLabels);
		Image imgChart = Image.getInstance(outChartByteArray.toByteArray());

		// GIS Map parameters
		String secIdStr = null;
		Long indId = null;
        Long secId = null;
		if (request.getParameter("sectorId") != null && !request.getParameter("sectorId").equals("-1")) {
			secIdStr = request.getParameter("sectorId");
            secId = Long.parseLong(secIdStr);
		}




		if (request.getParameter("indicatorId") != null
				&& !request.getParameter("indicatorId").equals("-1")) {
			indId = Long.parseLong(request.getParameter("indicatorId"));
		}

		// Widgets parameters (selected filters)
		if (request.getParameter("tableId") != null
				&& !request.getParameter("tableId").equals("-1")) {
			String tableStr = request.getParameter("tableId");
			String tableIdStr[] = tableStr.split(",");
			this.tableId = new Long[tableIdStr.length];

			int tableIdIndex = 0;
			for (String str : tableIdStr) {
				this.tableId[tableIdIndex] = Long.parseLong(str);
				tableIdIndex++;
			}
		}
		if (request.getParameter("columnId") != null
				&& !request.getParameter("columnId").equals("-1")) {

			String columnStr = request.getParameter("columnId");
			String columnIdStr[] = columnStr.split(",");
			this.columnId = new Long[columnIdStr.length];

			int columnIdIndex = 0;
			for (String str : columnIdStr) {
				this.columnId[columnIdIndex] = Long.parseLong(str);
				columnIdIndex++;
			}

		}
		if (request.getParameter("itemId") != null
				&& !request.getParameter("itemId").equals("-1")) {
			String itemStr = request.getParameter("itemId");
			String itemIdStr[] = itemStr.split(",");
			this.itemId = new Long[itemIdStr.length];

			int itemIdIndex = 0;
			for (String str : itemIdStr) {
				this.itemId[itemIdIndex] = Long.parseLong(str);
				itemIdIndex++;
			}
		}

		// Check for sector and indicator
		ByteArrayOutputStream outMapByteArray = null;
		// String translatedNone = TranslatorWorker.translateText("None",
		// locale, siteId);
		String translatedNone = "None";
		String indicatorName = translatedNone;
		String sectorName = translatedNone;
		String subGroupName = translatedNone;
		String timeInterval = translatedNone;

		String subGroup = request.getParameter("subgroupId");
		if (subGroup != null) {
			Long subGroupId = new Long(subGroup);
			if (!subGroupId.equals(new Long(-1))) {
				AmpIndicatorSubgroup auxSubGroup = IndicatorUtil
						.getIndicatorSubGroup(subGroupId);
				subGroupName = auxSubGroup.getSubgroupName();
			}
		}

		String indYear = request.getParameter("indYear");
		DateInterval datInt = null;
		if (indYear != null) {
			String startDateStr = null;
			String endDateStr = null;

			if (!indYear.equals("-1")) {
				if (indYear.startsWith("-")) {
					startDateStr = indYear
							.substring(0, indYear.indexOf("-", 2));
				} else {
					startDateStr = indYear.substring(0, indYear.indexOf("-"));
				}

				if (indYear.indexOf("--") > -1) {
					endDateStr = indYear.substring(indYear.indexOf("--") + 1);
				} else {
					endDateStr = indYear.substring(indYear.indexOf("-") + 1);
				}
				datInt = new DateInterval(new Date(
						new Long(startDateStr).longValue()), new Date(new Long(
						endDateStr).longValue()));
				timeInterval = datInt.getFormatedStartTime() + " - "
						+ datInt.getFormatedEndTime();
			}
		}

		String mapCode = request.getParameter("mapCode");

		String mapLevel = request.getParameter("mapLevel");
		if (mapLevel == null) {
			mapLevel = "2";
		}
        
        int mapLevelInt = Integer.parseInt(mapLevel);

        MapColorScheme colorScheme = GisUtil.getActiveColorScheme(request);
		if (request.getParameter("mapMode").equalsIgnoreCase("DevInfo")) {
			if (secId != null && indId != null) {
				AmpIndicator indicator = IndicatorUtil.getIndicator(indId);
				indicatorName = indicator.getName();
				sectorName = SectorUtil.getAmpSector(secId).getName();

				if (mapCode != null && mapCode.trim().length() > 0) {
					outMapByteArray = getMapImageSectorIndicator(mapCode,
							secId, indId, subGroup, indYear, mapLevel, colorScheme);
				} else {
					outMapByteArray = getMapImageSectorIndicator("TZA", secId,
							indId, subGroup, indYear, mapLevel, colorScheme);
				}

			} else {
				if (mapCode != null && mapCode.trim().length() > 0) {
					outMapByteArray = getMapImage(mapCode, mapLevelInt, colorScheme);
				} else {
					outMapByteArray = getMapImage("TZA", mapLevelInt,   colorScheme);
				}
			}
		} else if (request.getParameter("mapMode").equalsIgnoreCase("FinInfo")) {

            GisFilterForm filterForm = GisUtil.parseFilterRequest (request);

            String isRegSetStr = FeaturesUtil.getGlobalSettingValue("GIS Funding Type");
            boolean isRegional = (isRegSetStr == null || isRegSetStr.trim().equalsIgnoreCase("donor"))?GisUtil.GIS_DONOR_FUNDINGS:GisUtil.GIS_REGIONAL_FUNDINGS;
            boolean isPublic = request.getParameter("publicMode") == null?false:true;
            if (!isPublic && request.getSession().getAttribute("currentMember") == null) {
                isPublic = true;
            }
            filterResults = RMMapCalculationUtil.getAllFundingsFiltered(filterForm, isRegional, isPublic);

            if (filterForm.isFilterAllSectors()) {
                sectorName = "All";
            } else {
                Long[] selSecIds = filterForm.getSelectedSectors();
                if (selSecIds.length == 1) {
                    sectorName = org.digijava.module.gis.util.DbUtil.getSectorName(selSecIds[0]);
                } else if (selSecIds.length > 1) {
                    StringBuilder multySecStr = new StringBuilder(org.digijava.module.gis.util.DbUtil.getSectorName(selSecIds[0]));
                    multySecStr.append(" and ");
                    multySecStr.append(selSecIds.length - 1);
                    multySecStr.append(" more");
                    sectorName = multySecStr.toString();
                }

            }

            outMapByteArray = getMapImageFinancial (filterResults, filterForm.getFundingType(), (mapCode != null && mapCode.trim().length() > 0) ? mapCode : "TZA", filterForm.getMapLevel(), colorScheme);

        }
		// Get the Map Image
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

		if (request.getParameter("mapMode").equalsIgnoreCase("DevInfo")) {
			imagesTable.addCell(getImageMap(request, imgMap, sectorName, indicatorName,
					subGroupName, timeInterval));
		} else {
			Long donorId = request.getParameter("donorId") != null ? Long.parseLong(request.getParameter("donorId")):new Long(-1);
			String donorName = null;
			if (donorId.longValue() == -1l) {
				donorName = "All Donors";
			} else {
				AmpOrganisation organisation = (AmpOrganisation) org.digijava.module.aim.util.DbUtil
						.getObject(AmpOrganisation.class, donorId);
				donorName = organisation.getName();
			}
			imagesTable.addCell(getImageMap(imgMap, sectorName, null, null,
					null, donorName, request.getParameter("fundingType"),
					request.getParameter("mapMode"), request));
			String fundingType = request.getParameter("fundingType");
		}

		imagesTable.addCell(getImageChart(imgChart, selectedDonorName,
				selectedFromYear, selectedTotYear, request.getParameter("selectedCurrency"), publicMode));
		// imagesTable.addCell(" ");

		// First batch of widgets
		float[] layoutWidths = { 2f, 1f };
		PdfPTable layoutTable1 = new PdfPTable(layoutWidths);
		layoutTable1.setExtendLastRow(false);
		layoutTable1.setWidthPercentage(100);
		layoutTable1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		if (!publicMode) {

			ServletContext ampContext = getServlet().getServletContext();
			AmpTreeVisibility ampTreeVisibility = (AmpTreeVisibility) ampContext
					.getAttribute("ampTreeVisibility");

            /*
            PdfPTable regionalFundingTable = getRegionalFundingTable(filterResults);
            layoutTable1.addCell(regionalFundingTable);
            */


			if (FeaturesUtil.getFieldVisibility("Millennium Development Goals")
					.isFieldActive(ampTreeVisibility)) {
				PdfPTable mdgsBox = getMDGSBox();
				layoutTable1.addCell(mdgsBox);
			} else {
				layoutTable1.addCell(" ");
			}

			if (FeaturesUtil.getFieldVisibility("Resources at a glance")
					.isFieldActive(ampTreeVisibility)) {
				PdfPTable resourcesBox = getResourcesBox(selectedFromYear,
						selectedTotYear);
				layoutTable1.addCell(resourcesBox);
				layoutTable1.addCell(" ");
				layoutTable1.addCell(" ");
			}

			PdfPCell tempCell = null;

			if (FeaturesUtil.getFieldVisibility(
					"Aid Effectiveness Process Indicators").isFieldActive(
					ampTreeVisibility)) {
				PdfPTable aeProcessIndicatorBox = getAEPIBox(selectedFromYear,
						selectedTotYear);
				tempCell = new PdfPCell(aeProcessIndicatorBox);
				tempCell.setColspan(2);
				layoutTable1.addCell(tempCell);
				layoutTable1.addCell(" ");
				layoutTable1.addCell(" ");
			}

			if (FeaturesUtil.getFieldVisibility("Output Indicators")
					.isFieldActive(ampTreeVisibility)) {
				PdfPTable IOBox = getIntermediateOutputBox(selectedFromYear,
						selectedTotYear);
				tempCell = new PdfPCell(IOBox);
				tempCell.setColspan(2);
				tempCell.setBorder(Rectangle.NO_BORDER);
				tempCell.setPaddingBottom(10);
				layoutTable1.addCell(tempCell);
			}

			if (FeaturesUtil.getFieldVisibility("Total resources")
					.isFieldActive(ampTreeVisibility)) {
				PdfPTable totalResourcesBox = getTotalResourcesBox(
						selectedFromYear, selectedTotYear);
				tempCell = new PdfPCell(totalResourcesBox);
				tempCell.setColspan(2);
				tempCell.setBorder(Rectangle.NO_BORDER);
				layoutTable1.addCell(tempCell);
				layoutTable1.addCell(" ");
				layoutTable1.addCell(" ");
			}

			if (FeaturesUtil.getFieldVisibility("External Aid Resources")
					.isFieldActive(ampTreeVisibility)) {
				PdfPTable EAResourcesBox = getEAResourcesBox(selectedFromYear,
						selectedTotYear);
				tempCell = new PdfPCell(EAResourcesBox);
				tempCell.setColspan(2);
				tempCell.setBorder(Rectangle.NO_BORDER);
				tempCell.setPaddingBottom(10);
				layoutTable1.addCell(tempCell);
			}
		}

		document.open();
		String countryName = "";
		String ISO = null;
		Iterator itr1 = FeaturesUtil.getDefaultCountryISO().iterator();
		while (itr1.hasNext()) {
			AmpGlobalSettings ampG = (AmpGlobalSettings) itr1.next();
			ISO = ampG.getGlobalSettingsValue();
		}

		if (ISO != null && !ISO.equals("")) {
			Country cntry = DbUtil.getDgCountry(ISO);
			countryName = cntry.getCountryName();
		} else {
			countryName = "";
		}
		// Translation for Result Matrix
		// Paragraph title = new
		// Paragraph(TranslatorWorker.translate("gis:resultsmatrix", locale,
		// siteId) + countryName, new Font(Font.HELVETICA, 24, Font.BOLD));
		Paragraph title = new Paragraph(TranslatorWorker.translateText(
				"Results Matrix:", locale, siteId) + countryName, new Font(
				Font.HELVETICA, 24, Font.BOLD));

		String generatedOnTranslation = TranslatorWorker.translateText(
				"Generated on: ", locale, siteId);
		if (generatedOnTranslation == null || generatedOnTranslation.equals(""))
			generatedOnTranslation = "Generated on: ";

		Paragraph updateDate = new Paragraph(generatedOnTranslation
				+ FormatHelper.formatDate(new Date(System.currentTimeMillis()))
				+ "\n\n", new Font(Font.HELVETICA, 6, Font.BOLDITALIC));

		document.add(title);
		document.add(updateDate);
		document.add(imagesTable);
        document.add(getRegionalFundingTable(filterResults));

		document.add(layoutTable1);

		document.close();
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		

		return null;
	}

	private PdfPTable getIntermediateOutputBox(Integer selectedFromYear,
			Integer selectedToYear) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 3f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
				"Output Indicators", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(8);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] layoutExAidResourcesWidths = { 1f };
		PdfPTable layoutExAidResources = new PdfPTable(
				layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);

		PdfPTable table1ExAidResources = getWidgetTable("table_place4",
				selectedFromYear, selectedToYear);
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");
		layoutExAidResources.addCell(new Paragraph(TranslatorWorker
				.translateText("Source: Official government sources", locale,
						siteId), new Font(Font.HELVETICA, 6)));

		layoutExAidResources.addCell(" ");

		layoutCell.addElement(layoutExAidResources);
		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getTotalResourcesBox(Integer selectedFromYear,
			Integer selectedTotYear) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 3f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
				"Total resources", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(8);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] layoutTotalResourcesWidths = { 1f };
		PdfPTable layoutTotalResources = new PdfPTable(
				layoutTotalResourcesWidths);
		layoutTotalResources.setWidthPercentage(100);
		layoutTotalResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPTable tableTotalResources1 = getWidgetTable("table_place6",
				selectedFromYear, selectedTotYear);
		layoutTotalResources.addCell(tableTotalResources1);
		// PdfPTable tableTotalResources2 = getWidgetTable("table_place6");
		// layoutTotalResources.addCell(tableTotalResources2);

		layoutTotalResources.addCell(new Paragraph(TranslatorWorker
				.translateText("Source: Ministry of Finance", locale, siteId),
				new Font(Font.HELVETICA, 6)));

		layoutCell.addElement(layoutTotalResources);
		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getAEPIBox(Integer selectedFromYear,
			Integer selectedToYear) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 3f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(
				TranslatorWorker.translateText(
						"Aid Effectiveness Process Indicators", locale, siteId)
						+ "\n", new Font(Font.HELVETICA, 7, Font.BOLD,
						new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(6);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setPadding(0);
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(0);
		lineCell.setBorder(Rectangle.NO_BORDER);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] layoutAEIndicatorsWidths = { 1f };
		PdfPTable layoutAEIndicators = new PdfPTable(layoutAEIndicatorsWidths);
		layoutAEIndicators.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutAEIndicators.setWidthPercentage(100);

		PdfPTable table1AEIndicators = getWidgetTable("table_place1",
				selectedFromYear, selectedToYear);
		PdfPTable table2AEIndicators = getWidgetTable("table_place2",
				selectedFromYear, selectedToYear);
		PdfPTable table3AEIndicators = getWidgetTable("table_place3",
				selectedFromYear, selectedToYear);

		if (table1AEIndicators != null)
			layoutAEIndicators.addCell(table1AEIndicators);
		if (table2AEIndicators != null)
			layoutAEIndicators.addCell(table2AEIndicators);
		if (table3AEIndicators != null)
			layoutAEIndicators.addCell(table3AEIndicators);
		layoutAEIndicators.addCell(" ");
		layoutAEIndicators.addCell(new Paragraph(TranslatorWorker
				.translateText("Source: 2006 Paris Declaration Survey", locale,
						siteId), new Font(Font.HELVETICA, 6)));
		layoutAEIndicators.addCell(" ");

		layoutCell.addElement(layoutAEIndicators);

		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getEAResourcesBox(Integer selectedFromYear,
			Integer selectedToYear) throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 3f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
				"External Aid Resources", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(3);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.setBorder(Rectangle.NO_BORDER);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] layoutExAidResourcesWidths = { 1f, 1f };
		PdfPTable layoutExAidResources = new PdfPTable(
				layoutExAidResourcesWidths);
		layoutExAidResources.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutExAidResources.setWidthPercentage(100);

		PdfPTable table1ExAidResources = getWidgetTable("table_place5",
				selectedFromYear, selectedToYear);
		layoutExAidResources.addCell(table1ExAidResources);

		layoutExAidResources.addCell(" ");
		// widget:sourceAMPdatabase
		layoutExAidResources.addCell(new Paragraph(TranslatorWorker
				.translateText("Source: AMP database", locale, siteId),
				new Font(Font.HELVETICA, 6)));

		layoutExAidResources.addCell(" ");

		layoutCell.addElement(layoutExAidResources);
		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getImageChart(Image imgChart, String selectedDonorName,
			Integer selectedStartYear, Integer selectedEndYear, String selectedCurency, boolean isPublic)
			throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 2f, 1f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		// gis:breakdownbysector

		PdfPCell layoutCell = new PdfPCell();
		if (!isPublic) {
			Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
					"Breakdown by sector", locale, siteId) + "\n", new Font(
					Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
			paragraph.setAlignment(Element.ALIGN_CENTER);
			firstCell.setCellEvent(border);
			firstCell.addElement(paragraph);
			firstCell.setColspan(4);

			PdfPCell secondCell = new PdfPCell();
			secondCell.setPadding(0);
			secondCell
					.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

			// Add rounded tab
			headerTable.addCell(firstCell);
			// Add empty space
			headerTable.addCell(secondCell);

			// add the table with the rounded tab and the empty space to the
			// cell
			headerCell.addElement(headerTable);
			// add the full header cell to the general layout of the box
			generalBox.addCell(headerCell);

			PdfPCell lineCell = new PdfPCell();
			lineCell.setBorder(Rectangle.NO_BORDER);
			lineCell.setBackgroundColor(new Color(34, 46, 93));
			lineCell.setPadding(0);
			lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
			generalBox.addCell(lineCell);
			// Work the layout

			layoutCell.setPadding(2);
			layoutCell.setBackgroundColor(new Color(206, 226, 251));
			layoutCell.addElement(imgChart);
			layoutCell.setBorder(Rectangle.NO_BORDER);

			PdfPCell textCell = new PdfPCell();
			textCell.setPadding(2);
			textCell.setBackgroundColor(new Color(206, 226, 251));
			// widget:piechart:allAmountsin000USD
			String selectedDonorTranslation = "Selected donor";
			if (selectedDonorTranslation == null
					|| selectedDonorTranslation.equals(""))
				selectedDonorTranslation = "Selected donor";
			String selectedStartYearTranslation = "Start year";
			if (selectedStartYearTranslation == null
					|| selectedStartYearTranslation.equals(""))
				selectedStartYearTranslation = "Start year";
			String selectedEndYearTranslation = "End year";
			if (selectedEndYearTranslation == null
					|| selectedEndYearTranslation.equals(""))
				selectedEndYearTranslation = "End year";

            /*
            String baseCurr = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
					*/
            String baseCurr = selectedCurency != null ? selectedCurency : FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);

			if (baseCurr == null) {
				baseCurr = "USD";
			}

			textCell.addElement(new Paragraph("All amounts in 000s of "+baseCurr + "\n" + TranslatorWorker.translateText(selectedDonorTranslation, locale, siteId) + ": " + selectedDonorName + "\n"
				+ TranslatorWorker.translateText(selectedStartYearTranslation, locale, siteId) + ": " + selectedStartYear + "\n" + TranslatorWorker.translateText(selectedEndYearTranslation, locale, siteId) + ": " + selectedEndYear + "\n\n", new Font(Font.HELVETICA, 6)));

			textCell.setBorder(Rectangle.NO_BORDER);
			generalBox.addCell(textCell);

			PdfPCell text2Cell = new PdfPCell();
			text2Cell.setPadding(2);
			text2Cell.setBackgroundColor(new Color(206, 226, 251));
			// widget:SourceAmpdatabase

			text2Cell.addElement(new Paragraph(TranslatorWorker.translateText(
					"Source: AMP database", locale, siteId), new Font(
					Font.HELVETICA, 6)));

			generalBox.addCell(layoutCell);
			generalBox.addCell(text2Cell);
		}

		return generalBox;
	}

	private PdfPTable getImageMap(HttpServletRequest request, Image imgMap, String sectorName,
			String indicatorName, String subGroup, String timeInterval)
			throws WorkerException {
		return getImageMap(imgMap, sectorName, indicatorName, subGroup,
				timeInterval, null, null, null, request);
	}

	private PdfPTable getImageMap(Image imgMap, String sectorName,
			String indicatorName, String subGroup, String timeInterval,
			String donorName, String FundingType, String renderMode, HttpServletRequest request)
			throws WorkerException {
		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 2f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		// gis:regionalview

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
				"Regional View", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(4);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));
		layoutCell.addElement(imgMap);
		generalBox.addCell(layoutCell);

		PdfPCell textCell = new PdfPCell();
		textCell.setPadding(2);
		textCell.setBackgroundColor(new Color(206, 226, 251));
		// gis:minmax:message
		// TODO TRN: no record for this key.
		String selectedSectorTranslation = TranslatorWorker.translateText(
				"Selected sector", locale, siteId);

		if (selectedSectorTranslation == null
				|| selectedSectorTranslation.equals(""))
			selectedSectorTranslation = "Selected sector";
		// TODO TRN: no record for this key
		String selectedIndicatorTranslation = TranslatorWorker.translateText(
				"Selected Indicator", locale, siteId);
		if (selectedIndicatorTranslation == null
				|| selectedIndicatorTranslation.equals(""))
			selectedIndicatorTranslation = "Selected Indicator";

		String defaultMinMaxMessage = "Regions with the lowest (MIN) values for the selected indicator are shaded dark green. "
				+ "Regions with the highest (MAX) value are shaded light green. "
				+ "For some indicators (such as mortality rates), having the MAX value indicates the lowest performance";

        if (renderMode == null || renderMode.equalsIgnoreCase("devinfo")) {
		textCell.addElement(new Paragraph(TranslatorWorker.translateText(
				defaultMinMaxMessage, locale, siteId)
				+ "\n"
				+ selectedSectorTranslation
				+ ": "
				+ sectorName
				+ "\n"
				+ selectedIndicatorTranslation
				+ ": "
				+ indicatorName
				+ "\n\n"
				+ TranslatorWorker.translateText("Data Source: Dev Info",
						locale, siteId), new Font(Font.HELVETICA, 6)));
        } else if (renderMode.equalsIgnoreCase("fininfo")) {
            textCell.addElement(new Paragraph(TranslatorWorker.translateText(
				defaultMinMaxMessage, locale, siteId)
				+ "\n"
				+ selectedSectorTranslation
				+ ": "
				+ sectorName
				+ "\n\n"
				+ TranslatorWorker.translateText("Data Source: AMP Database",
						locale, siteId), new Font(Font.HELVETICA, 6)));

        }
		PdfPCell legendCell = new PdfPCell();
		legendCell.setPadding(0);
		legendCell.setBorder(Rectangle.NO_BORDER);
		try {
            byte[] gradientLegendBytes = null;
            gradientLegendBytes = GisUtil.getDefaultGradienLegendBytes(request, 500, 20);
			Image image = Image.getInstance(gradientLegendBytes);
			// image.scaleAbsoluteWidth(320f);
			image.setAlignment(Image.ALIGN_RIGHT);
			// image.scaleAbsoluteHeight(20f);
			legendCell.addElement(image);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		generalBox.addCell(legendCell);
		generalBox.addCell(textCell);

		return generalBox;
	}

    private PdfPTable getRegionalFundingTable (Object[] filterResults) throws WorkerException {
        PdfPTable retVal = new PdfPTable(1);
        retVal.setWidthPercentage(100f);
        // Work the header
        PdfPCell headerCell = new PdfPCell();
        headerCell.setPadding(0);
        headerCell.setBorder(Rectangle.NO_BORDER);

        float[] widths = { 1f, 2f };
        PdfPTable headerTable = new PdfPTable(widths);
        headerTable.setWidthPercentage(100f);
        // Get the first cell, with the rounded edges and the text
        RoundRectangle border = new RoundRectangle();
        PdfPCell firstCell = new PdfPCell();
        firstCell.setPadding(0);
        firstCell.setBorder(Rectangle.NO_BORDER);
        // gis:millenniumdevelopmentgoals

        Paragraph paragraph = new Paragraph(
                TranslatorWorker.translateText("Funding by Locations",
                        locale, siteId) + "\n",
                new Font(Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        firstCell.setCellEvent(border);
        firstCell.addElement(paragraph);
        firstCell.setColspan(8);
        firstCell.setPaddingBottom(5);
        PdfPCell secondCell = new PdfPCell();
        secondCell.setPadding(0);
        secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

        // Add rounded tab
        headerTable.addCell(firstCell);
        // Add empty space
        headerTable.addCell(secondCell);

        // add the table with the rounded tab and the empty space to the cell
        headerCell.addElement(headerTable);
        // add the full header cell to the general layout of the box
        retVal.addCell(headerCell);
        		PdfPCell lineCell = new PdfPCell();
        		lineCell.setBorder(Rectangle.NO_BORDER);
        		lineCell.setBackgroundColor(new Color(34, 46, 93));
        		lineCell.setPadding(0);
        		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
        retVal.addCell(lineCell);
        		// Work the layout
        		PdfPCell layoutCell = new PdfPCell();
        		layoutCell.setPadding(2);
        		layoutCell.setBackgroundColor(new Color(206, 226, 251));

        
        Map <String, FundingData> regFnd = (Map <String, FundingData>) filterResults[0];
        Set <String> regNames = (Set <String>) regFnd.keySet();

        PdfPTable regFndTable = new PdfPTable(5);
                retVal.setWidthPercentage(100f);


        //Table header
        PdfPCell regCellHeader = new PdfPCell();
        regCellHeader.setPadding(0);
        regCellHeader.setBorder(Rectangle.NO_BORDER);

        regCellHeader.setBackgroundColor(new Color(34, 46, 93));
        regCellHeader.addElement(new Phrase(TranslatorWorker.translateText("Location", locale, siteId), new Font(Font.HELVETICA, 6f, Font.BOLD, new Color(255, 255, 255))));

        PdfPCell regActComHeader = new PdfPCell();
        regActComHeader.setPadding(0);
        regActComHeader.setPaddingBottom(5);
        regActComHeader.setBorder(Rectangle.NO_BORDER);
        regActComHeader.setBackgroundColor(new Color(34, 46, 93));
        regActComHeader.addElement(new Phrase(TranslatorWorker.translateText("Actual Commitment",locale, siteId), new Font(Font.HELVETICA, 6f, Font.BOLD, new Color(255, 255, 255))));

        PdfPCell regActDisbHeader = new PdfPCell();
        regActDisbHeader.setPadding(0);
        regActDisbHeader.setBorder(Rectangle.NO_BORDER);
        regActDisbHeader.setBackgroundColor(new Color(34, 46, 93));
        regActDisbHeader.addElement(new Phrase(TranslatorWorker.translateText("Actual Disbursement",locale, siteId), new Font(Font.HELVETICA, 6f, Font.BOLD, new Color(255, 255, 255))));

        PdfPCell regActExpHeader = new PdfPCell();
        regActExpHeader.setPadding(0);
        regActExpHeader.setBorder(Rectangle.NO_BORDER);
        regActExpHeader.setBackgroundColor(new Color(34, 46, 93));
        regActExpHeader.addElement(new Phrase(TranslatorWorker.translateText("Actual Expenditure",locale, siteId), new Font(Font.HELVETICA, 6f, Font.BOLD, new Color(255, 255, 255))));

        PdfPCell regPlanDisbHeader = new PdfPCell();
        regPlanDisbHeader.setPadding(0);
        regPlanDisbHeader.setBorder(Rectangle.NO_BORDER);
        regPlanDisbHeader.setBackgroundColor(new Color(34, 46, 93));
        regPlanDisbHeader.addElement(new Phrase(TranslatorWorker.translateText("Planned Disbursement",locale, siteId), new Font(Font.HELVETICA, 6f, Font.BOLD, new Color(255, 255, 255))));

        regFndTable.addCell(regCellHeader);
        regFndTable.addCell(regActComHeader);
        regFndTable.addCell(regActDisbHeader);
        regFndTable.addCell(regActExpHeader);
        regFndTable.addCell(regPlanDisbHeader);

        List <String>regNamesSorted = new ArrayList<String>();
        regNamesSorted.addAll(regNames);

        
        Collections.sort(regNamesSorted);

        int counter = 0;
        Color odd = new Color(206, 226, 251);
        Color even = Color.white;
        for (String regName : regNamesSorted) {

            counter ++;

            Color rowColor = counter%2==0 ? odd : even;
            
            FundingData curRegFnd = regFnd.get(regName);

            PdfPCell regCell = new PdfPCell();
            regCell.setBackgroundColor(rowColor);
            regCell.setPadding(0);
            regCell.setBorder(Rectangle.NO_BORDER);
            regCell.setPaddingBottom(5);
            regCell.addElement(new Phrase(regName, new Font(Font.HELVETICA, 6f)));

            PdfPCell regActCom = new PdfPCell();
            regActCom.setBackgroundColor(rowColor);
            regActCom.setPadding(0);
            regActCom.setBorder(Rectangle.NO_BORDER);
            regActCom.addElement(new Phrase(FormatHelper.formatNumber(curRegFnd.getCommitment().doubleValue()), new Font(Font.HELVETICA, 6f)));

            PdfPCell regActDisb = new PdfPCell();
            regActDisb.setBackgroundColor(rowColor);
            regActDisb.setPadding(0);
            regActDisb.setBorder(Rectangle.NO_BORDER);
            regActDisb.addElement(new Phrase(FormatHelper.formatNumber(curRegFnd.getDisbursement().doubleValue()), new Font(Font.HELVETICA, 6f)));

            PdfPCell regActExp = new PdfPCell();
            regActExp.setBackgroundColor(rowColor);
            regActExp.setPadding(0);
            regActExp.setBorder(Rectangle.NO_BORDER);
            regActExp.addElement(new Phrase(FormatHelper.formatNumber(curRegFnd.getExpenditure().doubleValue()), new Font(Font.HELVETICA, 6f)));

            PdfPCell regPlanDisb = new PdfPCell();
            regPlanDisb.setBackgroundColor(rowColor);
            regPlanDisb.setPadding(0);
            regPlanDisb.setBorder(Rectangle.NO_BORDER);
            regPlanDisb.addElement(new Phrase(FormatHelper.formatNumber(curRegFnd.getPlannedDisbursement().doubleValue()), new Font(Font.HELVETICA, 6f)));


            regFndTable.addCell(regCell);
            regFndTable.addCell(regActCom);
            regFndTable.addCell(regActDisb);
            regFndTable.addCell(regActExp);
            regFndTable.addCell(regPlanDisb);
        }


        retVal.addCell(regFndTable);
        
        return retVal;
    }

	private PdfPTable getMDGSBox() throws WorkerException {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 1f, 2f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		// gis:millenniumdevelopmentgoals

		Paragraph paragraph = new Paragraph(
				TranslatorWorker.translateText("Millennium Development Goals",
						locale, siteId) + "\n",
				new Font(Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(8);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);
		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] chartsWidths = { 1f, 1f, 1f };
		PdfPTable layoutCharts = new PdfPTable(chartsWidths);
		layoutCharts.getDefaultCell().setPadding(1);
		layoutCharts.setExtendLastRow(false);
		layoutCharts.setWidthPercentage(100f);

		try {
			ByteArrayOutputStream chart_place1 = getWidgetImage("chart_place1");
			if (chart_place1 != null)
				layoutCharts.addCell(Image.getInstance(chart_place1
						.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place2 = getWidgetImage("chart_place2");
			if (chart_place2 != null)
				layoutCharts.addCell(Image.getInstance(chart_place2
						.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place3 = getWidgetImage("chart_place3");
			if (chart_place3 != null)
				layoutCharts.addCell(Image.getInstance(chart_place3
						.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place4 = getWidgetImage("chart_place4");
			if (chart_place4 != null)
				layoutCharts.addCell(Image.getInstance(chart_place4
						.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place5 = getWidgetImage("chart_place5");
			if (chart_place5 != null)
				layoutCharts.addCell(Image.getInstance(chart_place5
						.toByteArray()));
			else
				layoutCharts.addCell(new Phrase(" "));

			ByteArrayOutputStream chart_place6 = getWidgetImage("chart_place6");
			if (chart_place6 != null)
				layoutCharts.addCell(Image.getInstance(chart_place6
						.toByteArray()));
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
		PdfPCell legendCell = new PdfPCell();
		legendCell.setPadding(0);
		legendCell.setBorder(Rectangle.NO_BORDER);
		try {
			Image image = Image
					.getInstance(this
							.getServlet()
							.getServletContext()
							.getRealPath(
									"/TEMPLATE/ampTemplate/imagesSource/common/legend1.jpg"));
			image.scaleAbsoluteWidth(50f);
			image.setAlignment(Image.ALIGN_RIGHT);
			// image.scaleAbsoluteHeight(20f);
			legendCell.addElement(image);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		PdfPCell legendTextCell = new PdfPCell();
		legendTextCell.setPadding(0);
		legendTextCell.setBorder(Rectangle.NO_BORDER);
		// widget:SourceOfficialgovernmentsources

		legendTextCell.addElement(new Paragraph(TranslatorWorker.translateText(
				"Source: Official government sources", locale, siteId),
				new Font(Font.HELVETICA, 6)));

		PdfPTable legendTable = new PdfPTable(1);
		legendTable.setWidthPercentage(100f);
		legendTable.addCell(legendCell);
		legendTable.addCell(legendTextCell);
		PdfPCell legendCellLayout = new PdfPCell();
		legendCellLayout.setColspan(3);
		legendCellLayout.addElement(legendTable);
		layoutCharts.addCell(legendCellLayout);
		layoutCell.addElement(layoutCharts);

		generalBox.addCell(layoutCell);

		return generalBox;
	}

	private PdfPTable getResourcesBox(Integer selectedFromYear,
			Integer selectedToYear) throws WorkerException {

		PdfPTable generalBox = new PdfPTable(1);
		generalBox.setWidthPercentage(100f);

		// Work the header
		PdfPCell headerCell = new PdfPCell();
		headerCell.setPadding(0);
		headerCell.setBorder(Rectangle.NO_BORDER);

		float[] widths = { 2f, 1f };
		PdfPTable headerTable = new PdfPTable(widths);
		headerTable.setWidthPercentage(100f);
		// Get the first cell, with the rounded edges and the text
		RoundRectangle border = new RoundRectangle();
		PdfPCell firstCell = new PdfPCell();
		firstCell.setPadding(0);
		firstCell.setBorder(Rectangle.NO_BORDER);
		// gis:resourcesatglance

		Paragraph paragraph = new Paragraph(TranslatorWorker.translateText(
				"Resources at a glance", locale, siteId) + "\n", new Font(
				Font.HELVETICA, 7, Font.BOLD, new Color(255, 255, 255)));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		firstCell.setCellEvent(border);
		firstCell.addElement(paragraph);
		firstCell.setColspan(8);

		PdfPCell secondCell = new PdfPCell();
		secondCell.setPadding(0);
		secondCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 10f)));

		// Add rounded tab
		headerTable.addCell(firstCell);
		// Add empty space
		headerTable.addCell(secondCell);

		// add the table with the rounded tab and the empty space to the cell
		headerCell.addElement(headerTable);
		// add the full header cell to the general layout of the box
		generalBox.addCell(headerCell);
		PdfPCell lineCell = new PdfPCell();
		lineCell.setBorder(Rectangle.NO_BORDER);
		lineCell.setBackgroundColor(new Color(34, 46, 93));
		lineCell.setPadding(0);
		lineCell.addElement(new Phrase(" ", new Font(Font.HELVETICA, 1f)));
		generalBox.addCell(lineCell);

		// Work the layout
		PdfPCell layoutCell = new PdfPCell();
		layoutCell.setPadding(2);
		layoutCell.setBackgroundColor(new Color(206, 226, 251));

		float[] resourcesAtAGlanceWidths = { 2f, 1f };
		PdfPTable layoutResourcesAtAGlance = new PdfPTable(
				resourcesAtAGlanceWidths);
		layoutResourcesAtAGlance.setWidthPercentage(100f);
		layoutResourcesAtAGlance.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		layoutResourcesAtAGlance.getDefaultCell().setPadding(0);
		layoutResourcesAtAGlance.setExtendLastRow(false);

		PdfPTable layoutResourcesAtAGlanceTable1 = getWidgetTable(
				"atGlanceTable_Place1", selectedFromYear, selectedToYear);
		PdfPTable layoutResourcesAtAGlanceTable2 = getWidgetTable(
				"atGalnceTable_Place2", selectedFromYear, selectedToYear);
		PdfPTable layoutResourcesAtAGlanceTable3 = getWidgetTable(
				"atGlanceTable_Place3", selectedFromYear, selectedToYear);

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
		if (layoutResourcesAtAGlanceTable3 != null) {
			layoutResourcesAtAGlanceTable3.setExtendLastRow(false);
			layoutResourcesAtAGlanceTable3alone.getDefaultCell().setBorder(
					PdfPCell.NO_BORDER);
			layoutResourcesAtAGlanceTable3alone
					.addCell(layoutResourcesAtAGlanceTable3);

			layoutResourcesAtAGlance
					.addCell(layoutResourcesAtAGlanceTable3alone);
		} else {
			layoutResourcesAtAGlance.addCell("");
		}
		layoutResourcesAtAGlanceTable3alone.getDefaultCell().setBorder(
				PdfPCell.NO_BORDER);
		layoutResourcesAtAGlanceTable3alone
				.addCell(layoutResourcesAtAGlanceTable3);
		layoutCell.addElement(layoutResourcesAtAGlance);
		// widget:SourceOECD

		layoutCell.addElement(new Paragraph(TranslatorWorker.translateText(
				"Source: OECD", locale, siteId), new Font(Font.HELVETICA, 6)));

		generalBox.addCell(layoutCell);

		// Make the innerLayout
		return generalBox;
	}

	private PdfPTable getWidgetTable(String codePlace,
			Integer selectedFromYear, Integer selectedToYear) {
		try {

			String code = codePlace;

			AmpDaWidgetPlace place = WidgetUtil.getPlace(code);
			AmpWidget widget = place.getAssignedWidget();
			if (widget == null)
				return null;
			   final ArrayList rendertype = new ArrayList();
		        WidgetVisitor adapter = new WidgetVisitorAdapter() {
		        
		            @Override
		            public void visit(AmpDaTable table){
		                 rendertype.add(WidgetUtil.TABLE);
		            }
		            @Override
		            public void visit(AmpSectorTableWidget sectorTable){
		                 rendertype.add(WidgetUtil.SECTOR_TABLE);
		            }
		            @Override
		            public void visit(AmpParisIndicatorTableWidget table){
		                 rendertype.add(WidgetUtil.PARIS_INDICAROR_TABLE);
		            }
		            @Override
		            public void visit(AmpWidgetTopTenDonorGroups table) {
		                rendertype.add(WidgetUtil.TOP_TEN_DONORS);
		            }
		        };
		        widget.accept(adapter);
		        int rType = (Integer) rendertype.get(0);
		        switch(rType){
		        case WidgetUtil.SECTOR_TABLE: return getSectorTableWidget(widget.getId());
		        case WidgetUtil.PARIS_INDICAROR_TABLE: return getAEPIndicators(widget.getId());
		        case WidgetUtil.TOP_TEN_DONORS: return getTopTenDonors(selectedFromYear, selectedToYear);
		        }
			try {
				WiTable table = new WiTable.TableBuilder(widget.getId())
						.build();
				WiColumnDropDownFilter filter = null;
				if (table == null)
					return null;
				int currentTableIdIndex = matchesId(table.getId());
				if (currentTableIdIndex > -1) {
					table = new WiTable.TableBuilder(
							this.tableId[currentTableIdIndex]).build();
					if (itemId != null && columnId != null
							&& columnId[currentTableIdIndex] != null
							&& itemId[currentTableIdIndex] != null
							&& columnId[currentTableIdIndex].longValue() > 0
							&& itemId[currentTableIdIndex].longValue() > 0) {
						filter = (WiColumnDropDownFilter) table
								.getColumnById(columnId[currentTableIdIndex]);
						// TODO this is not correct, check why columnId and
						// itemId
						// are not null when table is normal table.
						if (filter != null) {
							filter.setActiveItemId(itemId[currentTableIdIndex]);
						}
					}
				}
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
					String columnName = "";
					if (filter != null && column.getId() == filter.getId())
						column = filter;

					if (column instanceof WiColumnDropDownFilter
							&& filter != null)
						columnName = filter.getProvider()
								.getItem(filter.getActiveItemId()).getName();
					else
						columnName = column.getName();

					PdfPCell cell = new PdfPCell(new Phrase(columnName,
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
						PdfPCell cellPdf = new PdfPCell(new Phrase(
								cell.getValue(), fontCell));
						cellPdf.setBackgroundColor(cellColor);

						pdfTable.addCell(cellPdf);
					}

				}
				return pdfTable;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private int matchesId(Long ptableId) {
		if (ptableId != null && this.tableId != null) {
			for (int a = 0; a < this.tableId.length; a++) {
				if (this.tableId[a].equals(ptableId))
					return a;
			}
		}
		return -1;
	}

	private ByteArrayOutputStream getMapImageSectorIndicator(String mapCode,
			Long secId, Long indId, String subgroupId, String indYear,
			String mapLevel, MapColorScheme colorScheme) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode);
		}

		Long subGroupId = new Long(subgroupId);

		// Get segments with funding for dashed paint map
		List secFundings = org.digijava.module.gis.util.DbUtil
				.getSectorFoundings(secId);

		Iterator it = secFundings.iterator();

		Object[] fundingList = getFundingsByLocations(secFundings);
		Map fundingLocationMap = (Map) fundingList[0];

		List segmentDataDasheList = new ArrayList();

		Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
		while (locFoundingMapIt.hasNext()) {
			String key = (String) locFoundingMapIt.next();
			org.digijava.module.gis.util.SegmentData segmentData = new SegmentData();
			segmentData.setSegmentCode(key);
			segmentData.setSegmentValue("100");
			segmentDataDasheList.add(segmentData);
		}

		List hilightDashData = prepareDashSegments(segmentDataDasheList,
				new ColorRGB(0, 0, 0), map);

		// Need to pass year and subgroup ID in the future to get correct
		// results.
		// List inds =
		// org.digijava.module.gis.util.DbUtil.getIndicatorValuesForSectorIndicator(secId,
		// indId, new Long(-1));

		List inds = null;
		if (indYear != null && !indYear.equals("-1")) {

			String startDateStr = null;
			String endDateStr = null;

			if (indYear.startsWith("-")) {
				startDateStr = indYear.substring(0, indYear.indexOf("-", 2));
			} else {
				startDateStr = indYear.substring(0, indYear.indexOf("-"));
			}

			if (indYear.indexOf("--") > -1) {
				endDateStr = indYear.substring(indYear.indexOf("--") + 1);
			} else {
				endDateStr = indYear.substring(indYear.indexOf("-") + 1);
			}

			DateInterval datInt = new DateInterval(new Date(new Long(
					startDateStr).longValue()), new Date(
					new Long(endDateStr).longValue()));
			inds = org.digijava.module.gis.util.DbUtil
					.getIndicatorValuesForSectorIndicator(secId, indId, datInt,
							subGroupId, Integer.parseInt(mapLevel));
		} else {
			inds = new ArrayList();
		}

		List segmentDataList = new ArrayList();
		Iterator indsIt = inds.iterator();
		Double min = null;
		Double max = null;

		Set regSet = new HashSet();

		while (indsIt.hasNext()) {
			Object[] indData = (Object[]) indsIt.next();

			String segmentCode = (String) indData[1];



                Double indValue = (Double) indData[0];

                if (RMMapCalculationUtil.isRegion(map, segmentCode)) {

                    SegmentData indHilightData = new SegmentData();
                    indHilightData.setSegmentCode(segmentCode);
                    indHilightData.setSegmentValue(indValue.toString());

                    if (min == null) {
                        min = indValue;
                        max = indValue;
                    }

                    if (indValue < min) {
                        min = indValue;
                    }

                    if (indValue > max) {
                        max = indValue;
                    }

                    // regSet.add(segmentCode);
                    segmentDataList.add(indHilightData);
                }


		}

		if (min == null) {
			min = new Double(0);
			max = new Double(0);
		}

		List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList, map, min,
				max, colorScheme);

		int canvasWidth = 700;
		int canvasHeight = 700;

		BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = graph.createGraphics();

		g2d.setBackground(new Color(0, 0, 100, 255));

		g2d.clearRect(0, 0, canvasWidth, canvasHeight);
		GisUtil gisUtil = new GisUtil();
		CoordinateRect rect = gisUtil.getMapRect(map);

		if (map != null) {

			gisUtil.addDataToImage(g2d, map.getSegments(), hilightData,
					hilightDashData, canvasWidth, canvasHeight, rect.getLeft(),
					rect.getRight(), rect.getTop(), rect.getBottom(), true,
					false, colorScheme);

			gisUtil.addCaptionsToImage(g2d, map.getSegments(), canvasWidth,
					canvasHeight, rect.getLeft(), rect.getRight(),
					rect.getTop(), rect.getBottom());
		}
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

   private ByteArrayOutputStream getMapImageFinancial(Object[] fundingList, String fundingType, String mapCode, int mapLevel, MapColorScheme colorScheme) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode, mapLevel);
		}

		Map fundingLocationMap = (Map) fundingList[0];

		List segmentDataList = new ArrayList();

		Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();

		BigDecimal min = null;
		BigDecimal max = null;
		while (locFoundingMapIt.hasNext()) {
			String key = (String) locFoundingMapIt.next();

            if (RMMapCalculationUtil.isRegion(map, key)) {

                FundingData fData = (FundingData) fundingLocationMap.get(key);
                SegmentData segmentData = new SegmentData();
                segmentData.setSegmentCode(key);

                BigDecimal selValue = null;

                if (fundingType.equals("commitment")) {
                    selValue = fData.getCommitment();
                } else if (fundingType.equals("disbursement")) {
                    selValue = fData.getDisbursement();
                } else if (fundingType.equals("expenditure")) {
                    selValue = fData.getExpenditure();
                }

                segmentData.setSegmentValue(selValue.toString());

                if (min == null) {
                    min = selValue;
                    max = selValue;
                }

                if (selValue.compareTo(min) < 0) {
                    min = selValue;
                }

                if (selValue.compareTo(max) > 0) {
                    max = selValue;
                }

                segmentDataList.add(segmentData);
            }
		}

		if (min == null) {
			min = new BigDecimal(0);
			max = new BigDecimal(0);
		}

		List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList, map,
				new Double(min.doubleValue()), new Double(max.doubleValue()), colorScheme);

		int canvasWidth = 700;
		int canvasHeight = 700;

		BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = graph.createGraphics();

		g2d.setBackground(new Color(0, 0, 100, 255));

		g2d.clearRect(0, 0, canvasWidth, canvasHeight);

		GisUtil gisUtil = new GisUtil();
		CoordinateRect rect = gisUtil.getMapRect(map);

		gisUtil.addDataToImage(g2d, map.getSegments(), hilightData, null,
				canvasWidth, canvasHeight, rect.getLeft(), rect.getRight(),
				rect.getTop(), rect.getBottom(), true, false,
				colorScheme);

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

	private ByteArrayOutputStream getMapImage(String mapCode, int mapLevel, MapColorScheme colorScheme) {
		GisUtil gisUtil = new GisUtil();
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		GisMap map = null;

		if (mapCode != null && mapCode.trim().length() > 0) {
			map = GisUtil.getMap(mapCode, mapLevel);
		}

		int canvasWidth = 700;
		int canvasHeight = 700;

		CoordinateRect rect = gisUtil.getMapRect(map);

		BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = graph.createGraphics();

		g2d.setBackground(new Color(0, 0, 100, 255));

		g2d.clearRect(0, 0, canvasWidth, canvasHeight);

		if (map != null) {
			gisUtil.addDataToImage(g2d, map.getSegments(), -1, canvasWidth,
					canvasHeight, rect.getLeft(), rect.getRight(),
					rect.getTop(), rect.getBottom(), true, false, colorScheme);

			gisUtil.addCaptionsToImage(g2d, map.getSegments(), canvasWidth,
					canvasHeight, rect.getLeft(), rect.getRight(),
					rect.getTop(), rect.getBottom());
		}
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
			Long donorId, Integer fromYear, Integer toYear,
			Boolean showLegends, Boolean showLabels) throws Exception {
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		Long[] donorIDs = null;
		if (donorId != null) {
			donorIDs = new Long[1];
			donorIDs[0] = donorId;
		}
		GregorianCalendar cal = new GregorianCalendar();
		if (fromYear == null) {
			fromYear = new Integer(cal.get(Calendar.YEAR));
		}
		if (toYear == null) {
			toYear = new Integer(cal.get(Calendar.YEAR));
		}

		ChartOption opt = new ChartOption();
		opt.setShowTitle(true);
		opt.setShowLegend(showLegends);
		opt.setShowLabels(showLabels);
		opt.setHeight(new Integer(660));
		opt.setWidth(new Integer(420));
		// Long sitId = RequestUtils.getSiteDomain(request).getSite().getId();
		String sitId = RequestUtils.getSiteDomain(request).getSite()
				.getSiteId();
		opt.setSiteId(sitId);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		opt.setLangCode(langCode);

		// generate chart
		JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(donorIDs,
				fromYear, toYear, opt);
		chart.setBackgroundPaint(new Color(206, 226, 251));
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(new Color(206, 226, 251));
        String noDatamessage = TranslatorWorker.translateText("There is no data available for the selected filters. Please adjust the date and/or donor filters",locale,siteId);
        plot.setNoDataMessage(noDatamessage);
		java.awt.Font font = new java.awt.Font(null, 0, 15);
		plot.setNoDataMessageFont(font);

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
		// AmpWidgetIndicatorChart cWidget =
		// ChartWidgetUtil.getIndicatorChartWidget(place.getId());
		if (widget == null)
			return null;
		AmpWidgetIndicatorChart cWidget = ChartWidgetUtil
				.getIndicatorChartWidget(widget.getId());

		ChartOption opt = new ChartOption();

		opt.setShowTitle(true);
		if (widget != null) {
			opt.setTitle(widget.getName());
		}
		opt.setShowLegend(false);
		opt.setShowLabels(true);
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

	class RoundRectangle implements PdfPCellEvent {
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			cb.setColorStroke(new Color(255, 255, 255));
			cb.setColorFill(new Color(34, 46, 93));
			roundRectangleUpper(cb, rect.getLeft(), rect.getBottom(),
					rect.getWidth() - 2, rect.getHeight(), 4);
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
		}
	}

	private Object[] getFundingsByLocations(List activityList) throws Exception {

		Map locationFundingMap = new HashMap();
		FundingData totalFundingForSector = new FundingData();
		// Calculate total funding
		Iterator<Object[]> actIt = activityList.iterator();
		while (actIt.hasNext()) {
			Object[] actData = actIt.next();
			AmpActivityVersion activity = (AmpActivityVersion) actData[0];
			Float percentsForSectorSelected = (Float) actData[1];
			FundingData totalFunding = getActivityTotalFundingInBaseCurrency(activity);

			totalFundingForSector
					.setCommitment(totalFundingForSector.getCommitment().add(
							totalFunding.getCommitment().multiply(
									new BigDecimal(
											percentsForSectorSelected / 100f))));
			totalFundingForSector
					.setDisbursement(totalFundingForSector.getDisbursement()
							.add(totalFunding.getDisbursement().multiply(
									new BigDecimal(
											percentsForSectorSelected / 100f))));
			totalFundingForSector
					.setExpenditure(totalFundingForSector.getExpenditure().add(
							totalFunding.getExpenditure().multiply(
									new BigDecimal(
											percentsForSectorSelected / 100f))));
			FundingData fundingForSector = new FundingData();
			fundingForSector
					.setCommitment(totalFunding.getCommitment().multiply(
							new BigDecimal(percentsForSectorSelected / 100f)));
			fundingForSector
					.setDisbursement(totalFunding.getDisbursement().multiply(
							new BigDecimal(percentsForSectorSelected / 100f)));
			fundingForSector
					.setExpenditure(totalFunding.getExpenditure().multiply(
							new BigDecimal(percentsForSectorSelected / 100f)));

			Set locations = activity.getLocations();
			Iterator<AmpActivityLocation> locIt = locations.iterator();

			BigDecimal a100percent = new BigDecimal(100);
			while (locIt.hasNext()) {
				AmpActivityLocation loc = locIt.next();
				if (loc.getLocation().getAmpRegion() != null
						&& loc.getLocationPercentage().floatValue() > 0.0f) {
					String regCode = loc.getLocation().getAmpRegion().getName();
					if (locationFundingMap.containsKey(regCode)) {
						FundingData existingVal = (FundingData) locationFundingMap
								.get(regCode);

						FundingData newVal = new FundingData();
						newVal.setCommitment(existingVal
								.getCommitment()
								.add(fundingForSector
										.getCommitment()
										.multiply(
												new BigDecimal(
														loc.getLocationPercentage()))
										.divide(a100percent)));
						newVal.setDisbursement(existingVal
								.getDisbursement()
								.add(fundingForSector
										.getDisbursement()
										.multiply(
												new BigDecimal(
														loc.getLocationPercentage()))
										.divide(a100percent)));
						newVal.setExpenditure(existingVal
								.getExpenditure()
								.add(fundingForSector
										.getExpenditure()
										.multiply(
												new BigDecimal(
														loc.getLocationPercentage()))
										.divide(a100percent)));

						locationFundingMap.put(regCode, newVal);
					} else {
						FundingData newVal = new FundingData();
						newVal.setCommitment(fundingForSector.getCommitment()
								.multiply(
										new BigDecimal(loc
												.getLocationPercentage())
												.divide(a100percent)));
						newVal.setDisbursement(fundingForSector
								.getDisbursement().multiply(
										new BigDecimal(loc
												.getLocationPercentage())
												.divide(a100percent)));
						newVal.setExpenditure(fundingForSector.getExpenditure()
								.multiply(
										new BigDecimal(loc
												.getLocationPercentage())
												.divide(a100percent)));

						locationFundingMap.put(regCode, newVal);
					}
				}
			}

			// Set activiactivity.getFunding();
		}
		Object[] retVal = new Object[2];
		retVal[0] = locationFundingMap;
		retVal[1] = totalFundingForSector;
		return retVal;
	}

	private List prepareDashSegments(List segmentData, ColorRGB dashColor,
			GisMap map) {
		List retVal = new ArrayList();
		Iterator it = map.getSegments().iterator();

		while (it.hasNext()) {
			GisMapSegment segment = (GisMapSegment) it.next();
			for (int idx = (int) 0; idx < segmentData.size(); idx++) {
				SegmentData sd = (SegmentData) segmentData.get(idx);
				if (sd.getSegmentCode().equalsIgnoreCase(
						segment.getSegmentCode())) {
					HilightData hData = new HilightData();
					hData.setSegmentId((int) segment.getSegmentId());
					hData.setColor(dashColor);
					retVal.add(hData);
				}
			}
		}
		return retVal;
	}




	private FundingData getActivityTotalFundingInBaseCurrency(AmpActivityVersion activity) {
		FundingData retVal = null;
		Set fundSet = activity.getFunding();
		Iterator<AmpFunding> fundIt = fundSet.iterator();

		BigDecimal commitment = null;
		BigDecimal disbursement = null;
		BigDecimal expenditure = null;
        BigDecimal plannedDisbursement = null;

		FundingCalculationsHelper fch = new FundingCalculationsHelper();
		// fch.doCalculations();

		Set fundDetSet = new HashSet();

		try {
			while (fundIt.hasNext()) {
				AmpFunding fund = fundIt.next();

				Set fundDetails = fund.getFundingDetails();

				Iterator fdIt = fundDetails.iterator();
				while (fdIt.hasNext()) {
					AmpFundingDetail fd = (AmpFundingDetail) fdIt.next();
					fundDetSet.add(fd);
				}

			}
			String baseCurr = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (baseCurr == null) {
				baseCurr = "USD";
			}

			fch.doCalculations(fundDetSet, baseCurr);

			commitment = fch.getTotActualComm().getValue();
			disbursement = fch.getTotActualDisb().getValue();
			expenditure = fch.getTotActualExp().getValue();
            plannedDisbursement = fch.getTotPlanDisb().getValue();

		} catch (Exception ex1) {
			ex1.printStackTrace();
			// Add exception reporting
		}

		retVal = new FundingData(commitment, disbursement, expenditure, plannedDisbursement);

		return retVal;
	}

	private PdfPTable getTopTenDonors(Integer selectedFromYear,
			Integer selectedTotYear) {

		try {
			List<TopDonorGroupHelper> donorGroups = WidgetUtil
					.getTopTenDonorGroups(selectedFromYear, selectedTotYear);
			String baseCurr = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (baseCurr == null) {
				baseCurr = "USD";
			}
			PdfPTable pdfTable = new PdfPTable(3);
			pdfTable.setExtendLastRow(false);
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);
			fontHeader.setColor(new Color(255, 255, 255));
			Font fontCell = new Font();
			fontCell.setSize(4);
			PdfPCell cell1 = new PdfPCell(new Phrase("", fontHeader));
			PdfPCell cell2 = new PdfPCell(new Phrase("Top 10 Donors",
					fontHeader));
			PdfPCell cell3 = new PdfPCell(new Phrase("Commitments in "
					+ baseCurr + " Millions for", fontHeader));

			cell1.setBackgroundColor(new Color(34, 46, 93));
			cell2.setBackgroundColor(new Color(34, 46, 93));
			cell3.setBackgroundColor(new Color(34, 46, 93));

			pdfTable.addCell(cell1);
			pdfTable.addCell(cell2);
			pdfTable.addCell(cell3);

			Iterator<TopDonorGroupHelper> donorGroupIter = donorGroups
					.iterator();
			int order = 1;
			int counter = 1;
			while (donorGroupIter.hasNext()) {
				TopDonorGroupHelper donorGroup = donorGroupIter.next();
				Color cellColor;
				if (counter % 2 == 0)
					cellColor = new Color(255, 255, 255);
				else
					cellColor = new Color(219, 229, 241);
				counter++;
				PdfPCell ordCell = new PdfPCell(new Phrase(
						Integer.toString(order), fontCell));
				PdfPCell nameCell = new PdfPCell(new Phrase(
						donorGroup.getName(), fontCell));
				PdfPCell valCell = new PdfPCell(new Phrase(donorGroup
						.getValue().toString(), fontCell));
				ordCell.setBackgroundColor(cellColor);
				nameCell.setBackgroundColor(cellColor);
				valCell.setBackgroundColor(cellColor);

				pdfTable.addCell(ordCell);
				pdfTable.addCell(nameCell);
				pdfTable.addCell(valCell);
				order++;
			}
			return pdfTable;

		} catch (Exception e) {
			logger.error(e);
		}

		return null;
	}

	private PdfPTable getAEPIndicators(Long widgetId) {
		try {


			AmpParisIndicatorTableWidget table = ParisIndicatorTableWidgetUtil
					.getAmpParisIndicatorTableWidget(widgetId);

			List<AmpParisIndicatorBaseTargetValues> pInd = table
					.getParisIndicators();

			PdfPTable pdfTable = new PdfPTable(4);
			pdfTable.setExtendLastRow(false);
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);
			fontHeader.setColor(new Color(255, 255, 255));
			Font fontCell = new Font();
			fontCell.setSize(4);
			PdfPCell cell1 = new PdfPCell(new Phrase("", fontHeader));
			PdfPCell cell2 = new PdfPCell(new Phrase(
					"Paris Declaration Indicators: ", fontHeader));
			PdfPCell cell3 = new PdfPCell(new Phrase("2005 Baseline",
					fontHeader));
			PdfPCell cell4 = new PdfPCell(new Phrase("2010 Target", fontHeader));
			// PdfPCell cell5 = new PdfPCell(new Phrase("", fontHeader));

			cell1.setBackgroundColor(new Color(34, 46, 93));
			cell2.setBackgroundColor(new Color(34, 46, 93));
			cell3.setBackgroundColor(new Color(34, 46, 93));
			cell4.setBackgroundColor(new Color(34, 46, 93));
			// cell5.setBackgroundColor(new Color(34, 46, 93));

			pdfTable.addCell(cell1);
			pdfTable.addCell(cell2);
			pdfTable.addCell(cell3);
			pdfTable.addCell(cell4);
			// pdfTable.addCell(cell5);

			Iterator<AmpParisIndicatorBaseTargetValues> pIndIter = pInd
					.iterator();

			int counter = 1;
			while (pIndIter.hasNext()) {
				AmpParisIndicatorBaseTargetValues pIndVal = (AmpParisIndicatorBaseTargetValues) pIndIter
						.next();
				Color cellColor;
				if (counter % 2 == 0)
					cellColor = new Color(255, 255, 255);
				else
					cellColor = new Color(219, 229, 241);
				PdfPCell indCell = new PdfPCell(new Phrase("Indicator "
						+ pIndVal.getParisIndicator().getIndicatorCode(),
						fontCell));
				PdfPCell nameCell = new PdfPCell(new Phrase(pIndVal
						.getParisIndicator().getName(), fontCell));
				PdfPCell baseCell = new PdfPCell(new Phrase(
						pIndVal.getBaseValue(), fontCell));
				PdfPCell targetCell = new PdfPCell(new Phrase(
						pIndVal.getTargetValue(), fontCell));
				// PdfPCell donorCell = new PdfPCell(new Phrase("", fontCell));
				indCell.setBackgroundColor(cellColor);
				nameCell.setBackgroundColor(cellColor);
				baseCell.setBackgroundColor(cellColor);
				targetCell.setBackgroundColor(cellColor);
				// donorCell.setBackgroundColor(cellColor);

				pdfTable.addCell(indCell);
				pdfTable.addCell(nameCell);
				pdfTable.addCell(baseCell);
				pdfTable.addCell(targetCell);
				// pdfTable.addCell(donorCell);
				counter++;
			}

			return pdfTable;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private PdfPTable getSectorTableWidget(Long widgetId) {
		try {
			DecimalFormat format= FormatHelper.getDecimalFormat();
			String baseCurr = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (baseCurr == null) {
				baseCurr = "USD";
			}

			String headingUSMil = TranslatorWorker.translateText(
					baseCurr+" millions", locale, siteId);
			String headingFY = TranslatorWorker.translateText("FY", locale,
					siteId);
			String headingPercent = TranslatorWorker.translateText("Of",
					locale, siteId);
			String headingOther = TranslatorWorker.translateText("Other",
					locale, siteId);
			String headingTotal = TranslatorWorker.translateText("Total",
					locale, siteId);
			String headingSector = TranslatorWorker.translateText("Sector",
					locale, siteId);

			List<String> yearsHeader = new ArrayList<String>();
			List<SectorTableHelper> sectorsInfo = new ArrayList<SectorTableHelper>();
			ArrayList<String> totalValues = new ArrayList<String>();
			ArrayList<String> otherValues = new ArrayList<String>();
			String fiscalCalendarId = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
			AmpFiscalCalendar calendar = FiscalCalendarUtil
					.getAmpFiscalCalendar(Long.parseLong(fiscalCalendarId));

			AmpSectorTableWidget secTableWidget = SectorTableWidgetUtil
					.getAmpSectorTableWidget(widgetId);

			List<AmpSectorTableYear> sectorTableYears = new ArrayList(
					secTableWidget.getYears());
			List<AmpSectorOrder> sectorOrders = new ArrayList(
					secTableWidget.getSectorsColumns());
			Iterator<AmpSectorOrder> sectorOrderIter = sectorOrders.iterator();
			SectorTableHelper sectorTableRowOther = new SectorTableHelper();
			sectorTableRowOther.setSectorName(headingOther);
			sectorTableRowOther
					.setSectorId(SectorTableHelper.OTHER_ROW_SECTOR_ID);

			SectorTableHelper sectorTableRowTotal = new SectorTableHelper();
			sectorTableRowTotal.setSectorName(headingTotal);
			sectorTableRowTotal.setApplyStyle(true);
			sectorTableRowTotal
					.setSectorId(SectorTableHelper.TOTAL_ROW_SECTOR_ID);

			List<Double> wholeAmounts = new ArrayList<Double>();

			List<Double> otherAmounts = new ArrayList<Double>();

			List<Long> otherPercents = new ArrayList<Long>();

			boolean isTotalsCalcualted = false;
			while (sectorOrderIter.hasNext()) {
				AmpSectorOrder sectorOrder = sectorOrderIter.next();

				SectorTableHelper sectorTableRow = new SectorTableHelper();
				AmpSector sector = sectorOrder.getSector();
				Long sectorId = sector.getAmpSectorId();
				sectorTableRow.setSectorId(sectorId);
				sectorTableRow.setSectorName(sector.getName());
				ArrayList<String> cells = new ArrayList<String>();
				Iterator<AmpSectorTableYear> totalYearIter = sectorTableYears
						.iterator();

				int index = 0;
				while (totalYearIter.hasNext()) {
					AmpSectorTableYear sectorTableYear = totalYearIter.next();
					Long year = sectorTableYear.getYear();
					Date startDate = ChartWidgetUtil.getStartOfYear(
							year.intValue(), calendar.getStartMonthNum() - 1,
							calendar.getStartDayNum());
					// we need data including the last day of toYear,this is
					// till the first day of toYear+1
					int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
					Date endDate = new Date(ChartWidgetUtil.getStartOfYear(
							year.intValue() + 1,
							calendar.getStartMonthNum() - 1,
							calendar.getStartDayNum()).getTime()
							- MILLISECONDS_IN_DAY);
					Double amount = SectorTableWidgetUtil.calculateFunding(
							null, new Long[] { sectorId }, startDate, endDate);
					// doing such thing because of rounding problem ... :(
					amount = format.parse(format.format(amount)).doubleValue();
					Double wholeAmount = null;
					// we are calculating whole amounts only once for each year
					if (wholeAmounts.size() <= index) {
						wholeAmount = SectorTableWidgetUtil.calculateFunding(
								null, null, startDate, endDate);
						// doing such thing because of rounding problem ... :(
						wholeAmount = format.parse(format.format(wholeAmount))
								.doubleValue();
						wholeAmounts.add(wholeAmount);
					} else {
						wholeAmount = wholeAmounts.get(index);
					}
					double allExceptOthersAmount = 0;

					// summing sector amounts for each year separately
					if (otherAmounts.size() <= index) {
						allExceptOthersAmount = amount;
						otherAmounts.add(allExceptOthersAmount);
					} else {
						allExceptOthersAmount = otherAmounts.get(index)
								+ amount;
						otherAmounts.remove(index);
						otherAmounts.add(index, allExceptOthersAmount);
					}

					String heading = "";
					if (sectorTableYear.getType().equals(
							AmpSectorTableYear.TOTAL_TYPE_YEAR)) {
						if (calendar.getIsFiscal()) {
							heading = headingFY + " " + year + "/" + (year + 1)
									+ " (" + headingUSMil + ")";
						} else {
							heading = year + " (" + headingUSMil + ")";
						}
						// adding total values
						if (!isTotalsCalcualted) {
							totalValues.add(format.format(wholeAmount));
						}
						// doing such thing because of rounding problem ... :(
						Double othersAmount = format.parse(
								format.format(wholeAmount
										- allExceptOthersAmount)).doubleValue();
						if (otherValues.size() <= index) {
							otherValues.add(format.format(othersAmount));
						} else {
							otherValues.remove(index);
							otherValues.add(index,
									(format.format(othersAmount)));
						}

						cells.add(format.format(amount));
						if (otherPercents.size() <= index) {
							otherPercents.add(0l);
						}
					} else {
						if (calendar.getIsFiscal()) {
							heading = headingFY + " % " + headingPercent + " "
									+ year + "/" + (year + 1);
						} else {
							heading = " % " + headingPercent + " " + year;
						}
						long percent = 0;
						long allExcludeOthersPercent = 0;
						if (wholeAmount != 0) {
							percent = Math.round(1.0 * amount / wholeAmount
									* 100);
						}
						if (otherPercents.size() <= index) {
							allExcludeOthersPercent = percent;
							otherPercents.add(allExcludeOthersPercent);
						} else {
							allExcludeOthersPercent = otherPercents.get(index)
									.longValue() + percent;
							otherPercents.remove(index);
							otherPercents.add(index, allExcludeOthersPercent);

						}
						long wholePercent = 0;
						if (wholeAmount != 0) {
							wholePercent = 100;
						}
						if (otherValues.size() <= index) {
							otherValues
									.add((wholePercent - allExcludeOthersPercent)
											+ "%");
						} else {
							otherValues.remove(index);
							otherValues.add(index,
									(wholePercent - allExcludeOthersPercent)
											+ "%");
						}
						if (!isTotalsCalcualted) {
							totalValues.add(wholePercent + "%");
						}
						cells.add(percent + "%");
					}
					if (!isTotalsCalcualted) {
						yearsHeader.add(heading);
					}
					index++;
				}
				isTotalsCalcualted = true;
				// adding year cells values to row
				sectorTableRow.setValues(cells);
				sectorsInfo.add(sectorTableRow);
			}

			sectorTableRowTotal.setValues(totalValues);
			sectorTableRowOther.setValues(otherValues);
			sectorsInfo.add(sectorTableRowOther);
			sectorsInfo.add(sectorTableRowTotal);

			PdfPTable pdfTable = new PdfPTable(totalValues.size()+1);
			pdfTable.setExtendLastRow(false);
			Font fontHeader = new Font();
			fontHeader.setSize(5);
			fontHeader.setStyle(Font.BOLD);
			fontHeader.setColor(new Color(255, 255, 255));
			Font fontCell = new Font();
			fontCell.setSize(4);
			PdfPCell secCell = new PdfPCell(new Phrase(headingSector,
					fontHeader));
			secCell.setBackgroundColor(new Color(34, 46, 93));
			pdfTable.addCell(secCell);

			Iterator<String> headerYear = yearsHeader.iterator();

			while (headerYear.hasNext()) {
				String hYear = headerYear.next();
				PdfPCell yearCell = new PdfPCell(new Phrase(hYear, fontHeader));
				yearCell.setBackgroundColor(new Color(34, 46, 93));
				pdfTable.addCell(yearCell);
			}
			// PdfPCell selCell = new PdfPCell(new Phrase("", fontHeader));
			// selCell.setBackgroundColor(new Color(34, 46, 93));
			// pdfTable.addCell(selCell);

			Iterator<SectorTableHelper> sectors = sectorsInfo.iterator();
			int counter = 1;
			while (sectors.hasNext()) {
				Color cellColor;
				if (counter % 2 == 0)
					cellColor = new Color(255, 255, 255);
				else
					cellColor = new Color(219, 229, 241);
				SectorTableHelper sectorTable = (SectorTableHelper) sectors
						.next();
				PdfPCell dataCell = new PdfPCell(new Phrase(
						sectorTable.getSectorName(), fontCell));
				dataCell.setBackgroundColor(cellColor);
				pdfTable.addCell(dataCell);
				Iterator<String> secValues = sectorTable.getValues().iterator();
				while (secValues.hasNext()) {
					String val = secValues.next();
					PdfPCell valCell = new PdfPCell(new Phrase(val, fontCell));
					valCell.setBackgroundColor(cellColor);
					pdfTable.addCell(valCell);
				}
				// PdfPCell testCell = new PdfPCell(new Phrase("", fontCell));
				// testCell.setBackgroundColor(cellColor);
				// pdfTable.addCell(testCell);
				counter++;

			}

			return pdfTable;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
