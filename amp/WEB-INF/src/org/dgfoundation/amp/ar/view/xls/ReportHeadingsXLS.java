/**
 * ReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mihai
 * @since 22.06.2007
 *
 */
public class ReportHeadingsXLS extends XLSExporter {
	private boolean machineFriendlyColName = false;
	/**
	 * @param parent
	 * @param item
	 */
	public ReportHeadingsXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param wb
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public ReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
			IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		ColumnReportData columnReport = (ColumnReportData) item;
	
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {
			
			if(this.machineFriendlyColName){
				machineFriendlygenerate();
			}else{
				generate(columnReport);
			}
		columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		}

	}
	
	private void machineFriendlygenerate(){
		rowId.inc();
		colId.reset();
		createHierarchyHeaderCell(0);
		colId.inc();
		createMachineFriendlyHeaderCells();
		colId.reset();			
	}
	
	public static int getAverageLengthOfCells(Column col, int minLength)
	{
		int medium = 0;
		int count = 0;
		for(Object o:col.getItems()) {
			int length = o.toString().length();
			if (length > minLength){ //we're only interested in cells that exceed our minimum cell length
				medium += length;
				count++;
			}
		}
		//medium += element2.getName().length();
		if (count > 0){
			medium = medium/count;
			medium += 5;	
		}
		return medium;
	}
	
	private void generate(ColumnReportData columnReport){

//		requirements for translation purposes
//		Long siteId=this.getMetadata().getSiteId();
//		String locale=this.getMetadata().getLocale();
//		boolean fundingReached = false;
		
		// column headings:

		rowId.inc();
		
		columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		int maxColumnDepth = columnReport.getMaxColumnDepth();
		for (int curDepth = 0; curDepth < maxColumnDepth; curDepth++)
		{
			colId.reset();
			row = sheet.createRow(rowId.shortValue());
			
			
			this.createHierarchyHeaderCell(curDepth);
			
			this.createHeadingBorders(curDepth);
			int colIdBaseValue = colId.intValue();
			for(Column col:columnReport.getItems())
			{
				col.setCurrentDepth(curDepth);
				List<Column> columnsOnCurrentLine = col.getSubColumns(curDepth);
				if (!columnsOnCurrentLine.isEmpty())
				{
					for(Column element2:columnsOnCurrentLine)
					{
						colId.set(colIdBaseValue + element2.getPositionInHeading().getStartColumn());
						int medium = getAverageLengthOfCells(element2, 10);
						
						//if (!"-".equalsIgnoreCase(element2.getName(metadata.getHideActivities())))
						{						
							HSSFCell cell =  this.getCell(row,this.getHighlightedStyle());
							HSSFCellStyle style = cell.getCellStyle();
							style.setWrapText(true);
							cell.setCellStyle(style);
							String cellValue=element2.getName(metadata.getHideActivities());
							//if (rowId.value == 8){
							if (cellValue!=null && cellValue.length() > 0)
							{
								short val;
								if ((short)(medium*256) < 2560)
									val = 2560; //at least 10 chars
								else
									val = (short)(medium*256);
								sheet.setColumnWidth((short)colId.value, val);

							}
							//this value should be translated
							String translatedCellValue = getColumnDisplayName(cellValue);
							//String prefix="aim:reportBuilder:";
							if(translatedCellValue.compareTo("")==0)
								cell.setCellValue(cellValue);
							else 
								cell.setCellValue(translatedCellValue);
						
							//int rowsp = element2.getPositionInHeading().getRowSpan();
							makeColSpanAndRowSpan(element2.getPositionInHeading().getColSpan(), element2.getPositionInHeading().getRowSpan(), true);
						}

					}		
				}
			}
			rowId.inc();
		}
		colId.reset();
	}
	
	protected void createHierarchyHeaderCell (int curDepth) {
		HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
		if (curDepth == 0) {
			String hierarchyPath = this.getMetadata().getHierarchiesPath();
			if(this.machineFriendlyColName){
				hierarchyPath = hierarchyPath.replace("/ ", "/").replace(" /", "/");
			}
			String colName = getColumnDisplayName(hierarchyPath);
			cell1.setCellValue( colName );
		}
		else {
			cell1.setCellValue("");
		}
	}
	
	protected void createHeadingBorders (int curDepth) {
		ColumnReportData columnReport = (ColumnReportData) item;
		if (curDepth == 0) {
			int maxRowSpan		= 1;
			Boolean summaryReport		= this.getMetadata().getHideActivities();
			if (  summaryReport == null  || !summaryReport )
			{
				Column tempCol			= (Column)columnReport.getItems().get(0);
				maxRowSpan				= (tempCol.getPositionInHeading().getRowSpan()>maxRowSpan)?tempCol.getPositionInHeading().getRowSpan():maxRowSpan;
                if (maxRowSpan>1) maxRowSpan -= 1;
			}
			else {
				maxRowSpan = columnReport.getMaxColumnDepth() - 1;
				//if ( maxRowSpan > 3 ) maxRowSpan = 3;                                    
			}
			makeRowSpan(maxRowSpan,true);
			sheet.setColumnWidth((short)colId.value, (short)5120);
		}
		colId.inc();
	}

	public void setMachineFriendlyColName(boolean machineFriendlyColName) {
		this.machineFriendlyColName = machineFriendlyColName;
	}
	
	protected String getColumnDisplayName(String colName){
		String translColName	= null;
		if (this.machineFriendlyColName){
			if (colName != null){
				return colName.toLowerCase().trim().replace(" ", "_");
			}
		}else{
			try{			
				translColName	= TranslatorWorker.translateText(colName, this.getMetadata().getLocale(), this.getMetadata().getSiteId());
			}catch(WorkerException e){
				translColName = colName;
			}
		}
		return translColName;

	}
	
	
	protected void createMachineFriendlyHeaderCells () {
		ArrayList<String> cellValues	= new ArrayList<String>();
		this.prepareMachineFriendlyHeaderCellsList(null, null, cellValues);
		if ( cellValues != null ) {
			for (String val : cellValues) {
				HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
				cell1.setCellValue( val );
				colId.inc();
			}
		}
	}
	
	protected void prepareMachineFriendlyHeaderCellsList (List columns, String parentName, List<String> cellValues) {
		if ( columns == null ) {
			ColumnReportData columnReport = (ColumnReportData) item;
			columns			= columnReport.getItems();
		}
		
		if ( columns != null ) {
			Iterator iter	= columns.iterator();
			while (iter.hasNext()) {
				Column tempCol 		= (Column) iter.next();
				String colName		= tempCol.getName(metadata.getHideActivities());

				List items = tempCol.getItems();
				String currentColumnDisplayName = getColumnDisplayName(colName);
				String name = (parentName == null) ?  currentColumnDisplayName : parentName
						+ " - " + currentColumnDisplayName;
				if (items != null && items.size() > 0
						&& items.get(0) instanceof Column) {
					this.prepareMachineFriendlyHeaderCellsList(items, name, cellValues);
				} else {
					if (name != null && !name.trim().equals("-")){
						cellValues.add(name);
					}
				}
			}
		}
		
	}
}
