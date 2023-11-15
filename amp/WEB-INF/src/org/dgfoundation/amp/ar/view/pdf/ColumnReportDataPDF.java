/**
 * ColumnReportDataPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.dgfoundation.amp.ar.*;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.ExportActivityToPDF;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class ColumnReportDataPDF extends PDFExporter {

    /**
     * @param parent
     */
    public ColumnReportDataPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public ColumnReportDataPDF(PdfPTable table, Viewable item, Long ownerId)
    {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

    protected int deductFontSize(String translatedValue)
    {
        if (translatedValue == null)
            return 12;
        if (translatedValue.length() < 18)
            return 12;
        return 9;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        ColumnReportData columnReport = (ColumnReportData) item;

        Font titleFont = new Font(ExportActivityToPDF.basefont, Font.DEFAULTSIZE, Font.BOLD);
        
        
        ReportData parent=(ReportData)columnReport.getParent();
        
        while (parent.getReportMetadata()==null)
        {
            parent=parent.getParent();
        }
        //when we get to the top of the hierarchy we have access to AmpReports
        
        //requirements for translation purposes
        TranslatorWorker translator=TranslatorWorker.getInstance();
        Long siteId=parent.getReportMetadata().getSiteId();
        String locale=parent.getReportMetadata().getLocale();
        
//      title
        if ((columnReport.getParent() != null)&&(!columnReport.getName().equalsIgnoreCase(columnReport.getParent().getName()))) {
            
            //introducing the translaton issues
            
            //String prefix="rep:pop:";
            String translatedName=null;
            //AMP-6253  
            String simplename = "";
            if (columnReport.getName().indexOf(":") > 0) {
                simplename = columnReport.getName().substring(0,columnReport.getName().indexOf(":"));
            }else{
                simplename = columnReport.getName();
            }
                
                
            translatedName=TranslatorWorker.translateText(simplename,locale,siteId);
            if (columnReport.getName().indexOf(":") > 0) {
                translatedName += columnReport.getName().substring(columnReport.getName().indexOf(":"));
            }
            
            PdfPCell pdfc;
            if("".equals(translatedName))
                pdfc= new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(columnReport.getName()),titleFont));
            else 
                pdfc=new PdfPCell(new Paragraph((ExportActivityToPDF.postprocessText(translatedName)),titleFont));
            pdfc.setColspan(columnReport.getTotalDepth());
            table.addCell(pdfc);
        }
        
        
        // headings
        Font font = new Font(ExportActivityToPDF.basefont, 9, Font.BOLD);
        font.setColor(new BaseColor(255,255,255));
        if(! columnReport.getGlobalHeadingsDisplayed()) {
            getExportState().headingCells = new ArrayList<PdfPCell>();
            columnReport.setGlobalHeadingsDisplayed(true);
            int maxColumnDepth = columnReport.getMaxColumnDepth();
            
            int zzz = 0;
            for (int curDepth = 0; curDepth < maxColumnDepth; curDepth++)
            {
                boolean anyCellsOnThisRow = false;

                for (Column col : columnReport.getColumns()) {
                    col.setCurrentDepth(curDepth);
                    List<Column> columnsOnCurrentLine = col.getSubColumnList();
                    for (Column element2 : columnsOnCurrentLine) {
                        zzz ++;
                        // String suffix = " (" + zzz + ")"; DEBUG, leave it here
                        String suffix = "";
                        anyCellsOnThisRow = true;
                        int rowsp = element2.getPositionInHeading().getRowSpan();
                        int colsp = element2.getPositionInHeading().getColSpan();
                        //element2.setMaxNameDisplayLength(16);
                    //if ( !StringUtils.isEmpty(element2.getName()) ){
                        String cellValue = element2.getName(metadata.getHideActivities());
                        //this value should be translated
                        String translatedCellValue = TranslatorWorker.translateText(cellValue);
                        //String prefix="aim:reportBuilder:";
                        
                        font.setSize(deductFontSize(translatedCellValue));
                        PdfPCell pdfc = new MyPdfPCell(new Paragraph((ExportActivityToPDF.postprocessText(translatedCellValue + suffix)), font));
                            
                        pdfc.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfc.setVerticalAlignment(Element.ALIGN_MIDDLE);

                        pdfc.setColspan(colsp);
                        pdfc.setRowspan(rowsp);
                        pdfc.setBackgroundColor(new BaseColor(51,102,153));
                        //table.addCell(pdfc);
                        getExportState().headingCells.add(pdfc);
                    }
                }
                getExportState().headingCells.add(new MyPdfPCell(new Paragraph(PDFExporter.FORCE_NEW_LINE)));
//              if (!anyCellsOnThisRow)
//              {
//                  PdfPCell pdfc = new MyPdfPCell(new Paragraph(PDFExporter.FORCE_NEW_LINE));
//                  pdfc.setColspan(columnReport.getTotalDepth());
//                  pdfc.setBackgroundColor(new Color(51,102,153));
//                  headingCells.add(pdfc);
//              }
            }
        }

        // add data

        if (metadata.getHideActivities() == null || !metadata.getHideActivities()) {
            for (Long element:columnReport.getOwnerIds()) {
                this.setOwnerId(element);
                for (Viewable velement:columnReport.getItems()) {
                    velement.invokeExporter(this);
                }
            }
        }
        
        //add trail cells
        TrailCellsPDF trails=new TrailCellsPDF(this,columnReport);
        trails.generate();


    }

}
