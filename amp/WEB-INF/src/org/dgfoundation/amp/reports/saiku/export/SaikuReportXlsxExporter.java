package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportXlsxExporter implements SaikuReportExporter {
	
	private SaikuReportExcelTemplate template;
	
	public final String reportSheetName = "Formatted";
	public final String summarySheetName = "Summary Information";
	
	/**
	 * Map<String sheetName, Map<Integer header-column-number, Integer columnWidth>> 
	 */
	private final Map<String, Map<Integer, Integer>> cachedWidths = new HashMap<String, Map<Integer, Integer>>();
	
	private static final Logger logger = Logger.getLogger(SaikuReportXlsxExporter.class);

	/**
	 * Cutoff threshold above which a report is considered large, and the SXSSF API is employed
	 * (flushing to disk instead of keeping all rows in memory)
	 */
	private static int SXSSF_THRESHOLD = 1000*1000;
	/**
	 * counter of rows written to output before being flushed (SXSSF behaviour)
	 */
	protected int flushCounter = 0;
	/**
	 * size of a batch of rows being accumulated until being flushed
	 */
	protected int flushBatchSize = 100;

	
//	protected boolean requiresMemoryCare = false;
	
	private static int countReportRows(ReportArea area) {
		if (area.getChildren() == null)
			return 1;
		else 
			return area.getChildren().stream().mapToInt(z -> countReportRows(z)).sum();
	}
	
	/**
	 * Decides whether a report is large (requires XSSF) or not. 
	 * @param report the report to be converted
	 * @return whether SXSSF API is going to be used
	 */
	protected boolean requiresMemoryCare(GeneratedReport report) {
		int cols = report.leafHeaders.size();
		int rows = countReportRows(report.reportContents);
		return cols * rows > SXSSF_THRESHOLD;
	}
	
	/**
	 * generates a workbook containing data about 1 or 2 reports. Normally you'd want both reports to actually be the
	 * same one generated in different currencies, but the code does not care and you can put as different reports as
	 * you want
	 * 
	 * @param report1
	 *            - the first report
	 * @param report2
	 *            - the second report, might be null
	 */
	@Override
	public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
		logger.info("Start generating Excel Report...");
//		this.requiresMemoryCare = requiresMemoryCare(report);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		SXSSFWorkbook wb = new SXSSFWorkbook(-1);
		template = new SaikuReportExcelTemplate(wb);
		
		addReportSheetToWorkbook(wb, report, getReportSheetName());
		addSummarySheetToWorkbook(wb, report, getSummarySheetName());
		
		addDualReportSheetToWorkbook(wb, dualReport);
		addDualSummarySheetToWorkbook(wb, dualReport);
		
		wb.write(os);
		os.flush();
		os.close();
		wb.dispose();
		logger.info("Stop generating Excel Report.");
		return os.toByteArray();
	}	
	

	/**
	 * @param wb
	 * @param report
	 * @param sheetName
	 */
	protected void addReportSheetToWorkbook(SXSSFWorkbook wb, GeneratedReport report, String sheetName) {
		SXSSFSheet reportSheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
		cachedWidths.computeIfAbsent(reportSheet.getSheetName(), k -> new HashMap<Integer, Integer>());
		
		generateReportSheet(wb, reportSheet, report);
		postProcessGeneratedSheet(reportSheet, report);
	}
	
	/**
	 * @param wb
	 * @param dualReport
	 */
	protected void addDualReportSheetToWorkbook(SXSSFWorkbook wb, GeneratedReport dualReport) {
		if (dualReport != null) {
			String ampCurrencyCode = dualReport.spec.getSettings().getCurrencyCode();
			String sheetName = TranslatorWorker.translateText(getReportSheetName()) + String.format(" - %s", ampCurrencyCode);
			addReportSheetToWorkbook(wb, dualReport, sheetName);
		}
	}
	
	protected void addSummarySheetToWorkbook(SXSSFWorkbook wb, GeneratedReport report, String sheetName) {
		SXSSFSheet summarySheet = wb.createSheet(TranslatorWorker.translateText(sheetName));
		generateSummarySheet(wb, summarySheet, report.spec);
	}
	
	protected void addDualSummarySheetToWorkbook(SXSSFWorkbook wb, GeneratedReport dualReport) {
		if (dualReport != null) {
			String ampCurrencyCode = dualReport.spec.getSettings().getCurrencyCode();
			String sheetName = TranslatorWorker.translateText(getSummarySheetName()) + String.format(" - %s", ampCurrencyCode);
			addSummarySheetToWorkbook(wb, dualReport, sheetName);
		}
	}
	
	protected void postProcessGeneratedSheet(Sheet sheet, GeneratedReport report) {
		calculateColumnsWidth(sheet, report);
	}

	protected void generateReportSheet(SXSSFWorkbook wb, SXSSFSheet sheet, GeneratedReport report) {
		renderReportTableHeader(wb, sheet, report);
		renderReportData(sheet, report);
	}
	
	/**
	 * @param wb
	 * @param sheet
	 * @param report
	 */
	protected void renderReportTableHeader(Workbook wb, Sheet sheet, GeneratedReport report) {
		int hiddenColumnsCnt = 0;
		Set<CellRangeAddress> mergedCells = new HashSet<CellRangeAddress>();
		
		for(int i=0; i < report.generatedHeaders.size(); i++) {
			Row row = sheet.createRow(i);
			for(HeaderCell headerCell : report.generatedHeaders.get(i)) {
				
				if (isHiddenColumn(headerCell.originalName)) {
					hiddenColumnsCnt++;
					continue;
				}
				
				int cellColumnPos = headerCell.getStartColumn() - hiddenColumnsCnt;
				Cell cell = row.createCell(cellColumnPos);
				cell.setCellValue(headerCell.getName());
				setMaxColWidth(sheet, cell, cellColumnPos);
				
				CellRangeAddress mergedHeaderCell = new CellRangeAddress(i, i + headerCell.getRowSpan() - 1, 
						cellColumnPos, cellColumnPos + headerCell.getColSpan() - 1);
				if (mergedHeaderCell.getNumberOfCells()  > 1)
					sheet.addMergedRegion(mergedHeaderCell);
				mergedCells.add(mergedHeaderCell);
				cell.setCellStyle(template.getHeaderCellStyle());
			}
		}

		for (CellRangeAddress ca : mergedCells) {
			SaikuReportExcelTemplate.fillHeaderRegionWithBorder(wb, sheet, ca);
		}
	}
	
	/**
	 * @param sheet
	 * @param report
	 */
	protected void renderReportData(SXSSFSheet sheet, GeneratedReport report) {
		Row row = sheet.createRow(report.generatedHeaders.size());
		renderTableRow(sheet, report, report.reportContents, 0, row);
		renderTableTotals(sheet, report, report.reportContents);
	}

	/**
	 * @param sheet
	 * @param report
	 * @param reportContents
	 * @param level
	 * @param row
	 * @return
	 */
	protected int renderTableRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row) {
		if (reportContents.getChildren() != null) {
			return renderGroupRow(sheet, report, reportContents, level, row);
		} else {
			// Totals are rendered in renderTableTotals method()
			if (level == 0) {
				return row.getRowNum();
			}
			
			IntWrapper intWrapper = new IntWrapper();
			report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {
				if (!(report.spec.getHierarchies().size() > 0 && intWrapper.value < level - 1)) {
					ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
					createCell(sheet, row, intWrapper.value, rc);
				}
				intWrapper.inc();
			});
			try {
				((SXSSFSheet)sheet).flushRows(10);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		
		return row.getRowNum();
	}
	
	/**
	 * @param sheet
	 * @param report
	 * @param reportContents
	 * @param level
	 * @param row
	 * @return
	 */
	protected int renderGroupRow(SXSSFSheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row) {
		for (ReportArea reportArea : reportContents.getChildren()) {
			if (reportArea.getNrEntities() > 0) {
				Cell cell = row.createCell(level);
				cell.setCellValue(reportArea.getOwner().debugString);
				cell.setCellStyle(template.getHierarchyStyle());
				setMaxColWidth(sheet, cell, level);
			} 
			
			int rowPosInit = row.getRowNum();
			int rowPos = renderTableRow(sheet, report, reportArea, level+1, row);
			CellRangeAddress hierarchyCell = new CellRangeAddress(rowPosInit, rowPos, level, level);
			if (hierarchyCell.getNumberOfCells()  > 1)
				sheet.addMergedRegion(hierarchyCell);
			row = sheet.createRow(rowPos + 1);
		}
		
		if (level > 0)
			renderSubTotalRow(sheet, report, reportContents, level, row);
		
		return row.getRowNum();
	}
	
	/**
	 * @param sheet
	 * @param report
	 * @param reportContents
	 * @param level
	 * @param row
	 */
	protected void renderSubTotalRow(Sheet sheet, GeneratedReport report, ReportArea reportContents, int level, Row row) {
		IntWrapper intWrapper = new IntWrapper();
		report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {
			if (intWrapper.value >= level) {
				ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
				Cell cell = createCell(sheet, row, intWrapper.value, rc);
				cell.setCellStyle(template.getSubtotalStyle(level));
			}
			intWrapper.inc();
		});
	}
	
	/**
	 * @param sheet
	 * @param report
	 * @param reportContents
	 */
	protected void renderTableTotals(Sheet sheet, GeneratedReport report, ReportArea reportContents) {
		IntWrapper intWrapper = new IntWrapper();
		Row row = sheet.createRow(sheet.getLastRowNum());
		report.leafHeaders.stream().filter(roc -> !isHiddenColumn(roc.originalColumnName)).forEach(roc -> {	
			ReportCell rc = reportContents.getContents().get(roc) != null ? reportContents.getContents().get(roc) : roc.emptyCell;
			Cell cell = createTotalCell(sheet, row, intWrapper.value, report, rc);
			cell.setCellStyle(template.getTotalNumberStyle());
			intWrapper.inc();
		});
	}
	
	/**
	 * @param row
	 * @param i
	 * @param report
	 * @param rc
	 * @return
	 */
	protected Cell createTotalCell(Sheet sheet, Row row, int i, GeneratedReport report, ReportCell rc) {
		Cell cell = null;
		if (i == 0 && report.spec.getColumns().size() > 0) {
			cell = row.createCell(i, Cell.CELL_TYPE_STRING);
			String value = TranslatorWorker.translateText("Report Totals");
			cell.setCellValue(value + " (" + report.reportContents.getNrEntities() + ")");
		} else {
			return createCell(sheet, row, i, rc);
		}
		
		return cell;
	}
	
	/**
	 * @param row
	 * @param i
	 * @param rc
	 * @return
	 */
	protected Cell createCell(Sheet sheet, Row row, int i, ReportCell rc) {
		int cellType = getCellType(rc);
		Cell cell = row.createCell(i, cellType);
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			cell.setCellValue(getDoubleValue(rc));
		} else if (cellType == Cell.CELL_TYPE_STRING) {
			cell.setCellValue(getStringValue(rc));
		}
		
		setMaxColWidth(sheet, cell, i);
		
		return cell;
	}
	
	protected void setMaxColWidth(Sheet sheet, Cell cell, int i) {
		Map<Integer, Integer> widths = cachedWidths.get(sheet.getSheetName());
		IntWrapper width = new IntWrapper().inc(10);
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				width.set(cell.getStringCellValue().length());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				width.set(Double.toString(cell.getNumericCellValue()).length());
				break;
		}
		
		widths.compute(i, (k, v) -> v == null ? width.value : v < width.value ? width.value : v);
	}


	protected int getCellType(ReportCell reportCell) {
		if (reportCell instanceof AmountCell) {
			return Cell.CELL_TYPE_NUMERIC;
		}
		
		return Cell.CELL_TYPE_STRING;
	}
	
	protected double getDoubleValue(ReportCell reportCell) {
		return reportCell != null ? new Double(reportCell.value.toString()) : 0d;
	}
	
	protected String getStringValue(ReportCell reportCell) {
		return reportCell != null ? reportCell.displayedValue : "";
	}
	
	
	
	/**
	 * Add extra info about filters applied, currency and settings.
	 * 
	 * @param wb
	 * @param summarySheet
	 * @param reportSpec
	 * @param queryObject
	 */
	private void generateSummarySheet(SXSSFWorkbook workbook, SXSSFSheet summarySheet, ReportSpecification reportSpec) {
		IntWrapper currLine = new IntWrapper();
		
		renderSummaryFilters(summarySheet, reportSpec, currLine);
		renderSummarySettings(summarySheet, reportSpec, currLine);
			summarySheet.trackAllColumnsForAutoSizing();
		for (int l = 0; l < 3; l++) {
			summarySheet.autoSizeColumn(l, true);
		}
	}
	
	/**
	 * Add information about applied filters in the summary sheet 
	 * 
	 * @param summarySheet
	 * @param reportSpec
	 * @param currLine
	 */
	private void renderSummaryFilters(Sheet summarySheet, ReportSpecification reportSpec, IntWrapper currLine) {
		// the report specification contains only IDs in filter rules. 
		// we need to export in the summary sheet the names instead of ids
		Map<String, List<String>> extractedFilters = SaikuExportFilterUtils.getFilterValuesForIds(reportSpec.getFilters());
		
		// Create header row for filters.
		int group = 0;
		Row filterRowTitle = summarySheet.createRow(currLine.intValue());
		Cell filterTitleCell = filterRowTitle.createCell(0);
		filterTitleCell.setCellValue(TranslatorWorker.translateText("Applied Filters"));
		filterTitleCell.setCellStyle(template.getOptionSettingsStyle());
		
		for (Map.Entry<String, List<String>> filter : extractedFilters.entrySet()) {
			group = 0;
			currLine.inc();
			Row filterCategoryRow = summarySheet.createRow(currLine.intValue());
			Cell filterCategoryCell = filterCategoryRow.createCell(0);
			filterCategoryCell.setCellValue(filter.getKey());
			filterCategoryCell.setCellStyle(template.getFilterSettingsStyle());
			
			for (String filterValue : filter.getValue()) {
				// Check if the row 'i' exists so we don't add an extra row for the first filter result.
				if (summarySheet.getRow(currLine.intValue()) != null) {
					summarySheet.getRow(currLine.intValue()).createCell(1).setCellValue(filterValue);
				} else {
					summarySheet.createRow(currLine.intValue()).createCell(1).setCellValue(filterValue);
				}
				
				currLine.inc();
				group++;
			}
			
			if (group > 0) {
				summarySheet.addMergedRegion(new CellRangeAddress(currLine.intValue() - group, currLine.intValue() - 1, 0, 0));
				summarySheet.getRow(currLine.intValue() - group).getCell(0).setCellStyle(template.getHierarchyStyle());
			}
			
			currLine.dec();
		}
				
	}
	
	/**
	 * 
	 * Add report settings information (currency, calendar, units) in the summary sheet 
	 *
	 * @param summarySheet
	 * @param reportSpec
	 * @param currLine
	 */
	private void renderSummarySettings(Sheet summarySheet, ReportSpecification reportSpec, IntWrapper currLine) {
		String currency = reportSpec.getSettings().getCurrencyCode();
		if (currency == null) {
			currency = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
		}		
		String calendar = reportSpec.getSettings().getCalendar().getName();
		String units = reportSpec.getSettings().getUnitsOption().userMessage;
			
		renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Currency"), currency);
		renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Calendar"), calendar);
		renderSummaryLine(summarySheet, currLine, TranslatorWorker.translateText("Units"), units);
	}


	public void renderSummaryLine(Sheet summarySheet, IntWrapper currLine, String title, String value) {
		currLine.inc(2);
		Row calendarRow = summarySheet.createRow(currLine.intValue());
		Cell calendarTitleCell = calendarRow.createCell(0);
		calendarTitleCell.setCellValue(TranslatorWorker.translateText(title));
		calendarTitleCell.setCellStyle(template.getOptionSettingsStyle());
		calendarRow.createCell(1).setCellValue(value);
	}
	

	/**
	 * We need an alternative way to calculate the column's width because sheet.autoSizeColumn can add several minutes
	 * to the process.
	 * 
	 * @param sheet
	 * @param totalColNumber
	 * @param hierarchies
	 * @param headers
	 */
	protected void calculateColumnsWidth(Sheet sheet, GeneratedReport report) {
		Map<Integer, Integer> sheetWidths = cachedWidths.get(sheet.getSheetName());
		
		for (int i = 0; i < report.leafHeaders.size(); i++) {
			try {
				if (sheetWidths.containsKey(i)) {
					int width = (int) ((sheetWidths.get(i)) * template.getCharWidth());
					width = width < template.getMaxColumnWidth() ? width : template.getMaxColumnWidth() - 1;
					sheet.setColumnWidth(i, width);
				} else {
					sheet.setColumnWidth(i, (int) (template.getDefaultColumnWidth() * template.getCharWidth()));
				}
			} catch (Exception e) {
				// Alternative slow method.
				sheet.autoSizeColumn(i);
			}
		}
	}

	protected boolean isHiddenColumn(String columnName) {
		return columnName.equals("Draft") || columnName.equals("Approval Status");
	}
	
	protected String getReportSheetName() {
		return this.reportSheetName;
	}
	
	protected String getSummarySheetName() {
		return this.summarySheetName;
	}
}
