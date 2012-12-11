/**
 * ReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.Iterator;

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
//		requirements for translation purposes
		String siteId=this.getMetadata().getSiteId();
		String locale=this.getMetadata().getLocale();
		boolean fundingReached = false;
		
		// column headings:
		if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {

		rowId.inc();
		colId.reset();
		
		columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
		int maxColumnDepth = columnReport.getMaxColumnDepth();
		for (int curDepth = 0; curDepth <= maxColumnDepth; curDepth++) {
			row = sheet.createRow(rowId.shortValue());
			
			
			this.createHierarchyHeaderCell(curDepth);
			
			this.createHeadingBorders(curDepth);
			
			colId.inc();			
			Iterator i = columnReport.getItems().iterator();
			//int cellCount = 0;
			while (i.hasNext()) {
				Column col = (Column) i.next();
				col.setCurrentDepth(curDepth);
				int rowsp = col.getCurrentRowSpan();
				Iterator ii = col.getSubColumnList().iterator();
				if (ii.hasNext()){
					while (ii.hasNext()) {
						//cellCount++;
						Column element2 = (Column) ii.next();
						
						int medium = 0;
						int count = 0;
						Iterator ei = element2.getItems().iterator();
						while (ei.hasNext()) {
							Object o = (Object) ei.next();
							int length = o.toString().length();
							if (length > 10){ //we're only interested in cells that exceed our minimum cell length
								medium += length;
								count++;
							}
						}
						//medium += element2.getName().length();
						if (count > 0){
							medium = medium/count;
							medium += 5;	
						}
						
						if (!"-".equalsIgnoreCase(element2.getName(metadata.getHideActivities()))){
						
						HSSFCell cell =  this.getCell(row,this.getHighlightedStyle());
						HSSFCellStyle style = null;
						try{	
							style = cell.getCellStyle();
						}
						catch (ClassCastException ex) {
							
							throw ex;
						}
						style.setWrapText(true);
						cell.setCellStyle(style);
						String cellValue=element2.getName(metadata.getHideActivities());
						//if (rowId.value == 8){
						if (rowId.value < 10 && cellValue!=null && cellValue.length() > 0){
							//here we set the cell width
							//if (sheet.getColumnWidth((short)colId.value)<cellValue.length()*256){
								short val;
								if ((short)(medium*256) < 2560)
									val = 2560; //at least 10 chars
								else
									val = (short)(medium*256);
								/*
								if (rowId.value == 6 && !fundingReached){
									if (cellValue == "Funding")
										fundingReached = true;
									else
										val *=3;
								}
								*/
								if (colId.value == 0)
									sheet.setColumnWidth((short)colId.value, (short)10240);
								else
									sheet.setColumnWidth((short)colId.value, val);
							//}

						}
						//this value should be translated
						String translatedCellValue = getColumnDisplayName(cellValue);
						//String prefix="aim:reportBuilder:";
						if(translatedCellValue.compareTo("")==0)
							cell.setCellValue(cellValue + "III");
						else 
							cell.setCellValue(translatedCellValue);
						
					   /*This is a quick fix related to AMP-13537 there should be a better way to fix the problem
						* We have to review this afterwards.
						*/
						if (!this.getMetadata().getHideActivities()){
							if(rowsp>1) makeRowSpan(rowsp-1,true);
						}
						
						/**
						 * on the totals column there are no years to be displayed
						 */
						if ( this.getMetadata().getHideActivities() && curDepth == 1 && 
								ArConstants.COLUMN_TOTAL.equals( col.getName() )) { 
							
								makeRowSpan(maxColumnDepth-1, true);
							
						}
						
						if (element2.getWidth() > 1){
							makeColSpan(element2.getWidth(),true);
						}else{
							colId.inc();
						}
						
					}
					}		
				}
				else {
					//add padding cells before creating a colspan here. Making a colspan before these cells are created 
					//will make the borders look ugly
					if (!"-".equalsIgnoreCase(col.getName(metadata.getHideActivities()))){
						if(col.getWidth()>1) {
							for(int k=0;k<col.getWidth();k++) {
								HSSFCell cell = row.getCell((short) (colId.intValue()+k));
								if(cell==null) cell=row.createCell((short) (colId.intValue()+k));
								HSSFCellStyle cellstyle = wb.createCellStyle();
								cellstyle.cloneStyleFrom(this.getHighlightedStyle());
								cell.setCellStyle(cellstyle);
							}
						}

					 if(col.getWidth()==1) makeColSpan(col.getWidth(),true);
					}
				}
			}
			rowId.inc();
			colId.reset();
		}
		}

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
			if (  summaryReport == null  || !summaryReport ) {
				Column tempCol			= (Column)columnReport.getItems().get(0);
				maxRowSpan				= (tempCol.getRowSpan()>maxRowSpan)?tempCol.getRowSpan():maxRowSpan;
                                    if(maxRowSpan>1) maxRowSpan-=1;
			}
			else {
				Iterator<Column> colIter	= columnReport.getItems().iterator();
				while ( colIter.hasNext() ) {
					Column tempCol	= colIter.next();
					int currentRowSpan = tempCol.getCurrentRowSpan() - 1;
					maxRowSpan		= (currentRowSpan>maxRowSpan)?currentRowSpan:maxRowSpan;
				}
				if ( maxRowSpan > 3 ) maxRowSpan = 3;
                                    
			}
			makeRowSpan(maxRowSpan,true);
			sheet.setColumnWidth((short)colId.value, (short)5120);
		}
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
}
