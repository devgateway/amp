/**
 * ReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.*;
import org.dgfoundation.amp.ar.*;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    
    /**
     * returns the maximum length of the contents of a column
     * @param col
     * @return
     */
    protected int getMaxCellLength(CellColumn<?> col)
    {
        int max = 0;
        for(Object obj:col.getItems())
            if (obj != null)
                max = Math.max(max, obj.toString().length());
        return max;
    }
    
    private void generate(ColumnReportData columnReport){

//      requirements for translation purposes
//      Long siteId=this.getMetadata().getSiteId();
//      String locale=this.getMetadata().getLocale();
//      boolean fundingReached = false;
        
        // column headings:

        rowId.inc();
        
//      java.util.Map<Integer, Integer> columnTitleLengths = new java.util.TreeMap<Integer, Integer>();
        
        columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
        int maxColumnDepth = columnReport.getMaxColumnDepth();
        for (int curDepth = 0; curDepth < maxColumnDepth; curDepth++)
        {
            colId.reset();
            row = sheet.createRow(rowId.shortValue());
            
            //AMP-17511: we should call first create Borders and then create Hierarchy where colId is incremented and create borders won't properly work in some cases  
            this.createHeadingBorders(curDepth);
            this.createHierarchyHeaderCell(curDepth);
            
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
                        int colcol = colId.intValue(); // save the value because processing further after might change it
                        //int medium = getAverageLengthOfCells(element2, 10);
                        
                        //if (!"-".equalsIgnoreCase(element2.getName(metadata.getHideActivities())))
                        {                       
                            HSSFCell cell =  this.getCell(row,this.getHighlightedStyle());                          
                            HSSFCellStyle style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            String cellValue=element2.getName(metadata.getHideActivities());
                            //this value should be translated
                            String translatedCellValue = getColumnDisplayName(cellValue);
                            //String prefix="aim:reportBuilder:";
                            if(translatedCellValue.compareTo("")==0)
                                cell.setCellValue(cellValue);
                            else 
                                cell.setCellValue(translatedCellValue);
                        
                            makeColSpanAndRowSpan(element2.getPositionInHeading().getColSpan(), element2.getPositionInHeading().getRowSpan(), true);
                            
                            if (element2 instanceof CellColumn) // we are at the bottom of the columns hierarchy
                            {
                                calculateAndSetColumnWidth(colcol, (CellColumn) element2, translatedCellValue);
                            }
                        }

                    }       
                }
            }
            rowId.inc();
        }
        colId.reset();
        sheet.getRow(rowId.intValue() - 1).setHeightInPoints(30); // when the function is ended, we are at the last-level row, which holds the column titles. Because column titles are usually longer than the contents they hosts ("Actual Disbursements" in a column which only has short numbers), we allocate 2 rows for it but lower the width by 33%
    }
    
    protected void calculateAndSetColumnWidth(int sheetColumnNr, CellColumn<?> terminalColumn, String translatedColumnName)
    {
        double columnNameLength = 0.75 * translatedColumnName.length(); // we have a row-height of 2, so we can afford some slack
        int contentLength = getMaxCellLength(terminalColumn);
        
        double usedColumnWidth = Math.max(columnNameLength, contentLength);
        ((GroupReportDataXLS) this.getParent()).columnWidths.put(sheetColumnNr, (int) (256 * usedColumnWidth)); 
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
    
    protected void createHeadingBorders (int curDepth)
    {
        ColumnReportData columnReport = (ColumnReportData) item;
        if (curDepth == 0)
        {
            int maxRowSpan      = 1;
            Boolean summaryReport       = this.getMetadata().getHideActivities();
            if (  summaryReport == null  || !summaryReport )
            {
                Column tempCol          = (Column) columnReport.getItems().get(0);
                maxRowSpan              = (tempCol.getPositionInHeading().getRowSpan()>maxRowSpan)?tempCol.getPositionInHeading().getRowSpan():maxRowSpan;
                if (maxRowSpan>1) maxRowSpan -= 1;
            }
            else {
                maxRowSpan = columnReport.getMaxColumnDepth() - 1;
            }
            makeRowSpan(maxRowSpan,true);
            sheet.setColumnWidth(colId.intValue(), 5120);
        }
        colId.inc();
    }

    public void setMachineFriendlyColName(boolean machineFriendlyColName) {
        this.machineFriendlyColName = machineFriendlyColName;
    }
    
    protected String getColumnDisplayName(String colName){
        String translColName    = null;
        if (this.machineFriendlyColName){
            if (colName != null){
                return colName.toLowerCase().trim().replace(" ", "_");
            }
        }else{
            translColName   = TranslatorWorker.translateText(colName, this.getMetadata().getLocale(), this.getMetadata().getSiteId());
        }
        return translColName;

    }
    
    
    protected void createMachineFriendlyHeaderCells () {
        ArrayList<String> cellValues    = new ArrayList<String>();
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
            columns         = columnReport.getItems();
        }
        
        if ( columns != null ) {
            Iterator iter   = columns.iterator();
            while (iter.hasNext()) {
                Column tempCol      = (Column) iter.next();
                String colName      = tempCol.getName(metadata.getHideActivities());

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
