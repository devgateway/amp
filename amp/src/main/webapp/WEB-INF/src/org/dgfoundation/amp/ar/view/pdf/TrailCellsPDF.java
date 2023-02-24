/**
 * TrailCellsPDF.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.pdf;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.action.ExportActivityToPDF;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 29, 2006
 *
 */
public class TrailCellsPDF extends PDFExporter {

    private static Logger logger = Logger.getLogger(TrailCellsPDF.class);
    
    /**
     * @param parent
     * @param item
     */
    public TrailCellsPDF(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param table
     * @param item
     * @param ownerId
     */
    public TrailCellsPDF(PdfPTable table, Viewable item, Long ownerId) {
        super(table, item, ownerId);
        // TODO Auto-generated constructor stub
    }

    /**
     * counts the number of columns to be drawn in the PDF export table
     * @param ar
     * @return
     */
    public final static int countDrawnColumns(AmpReports ar, Viewable item)
    {
        if (item instanceof ReportData)
            return ((ReportData) item).getTotalDepth();
        
        int res = 0;
        res = ar.getColumns().size() + ar.getMeasures().size() - ar.getHierarchies().size();
        if (res < 0)
            logger.warn("the report will come out with a corrupted layout");
        return res;
    }
        
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        //generate totals:
        ReportData grd=(ReportData) item;
        Font totalFont = new Font(ExportActivityToPDF.basefont, 10, Font.BOLD);
        
        if(grd.getParent()!=null) 
        {
            ReportData parent=(ReportData)grd.getParent();
            //int depthToIgnore = 0;
            while (parent.getReportMetadata()==null)
            {
                parent=parent.getParent();
                //depthToIgnore ++;
            }
            //when we get to the top of the hierarchy we have access to AmpReports
            
            //requirements for translation purposes
            //this is the translation part for the rep:pop:totalsfor and the rep:pop:($donorName} part
            TranslatorWorker translator=TranslatorWorker.getInstance();
            Long siteId = parent.getReportMetadata().getSiteId();
            String locale = parent.getReportMetadata().getLocale();
            String totalsFor = "Totals For";
            
            int totalNrOfDrawnColumns = countDrawnColumns(parent.getReportMetadata(), this.getItem());
                    

            //String translatedName=grd.getName();
            
            //AMP-6253 grd.getName()is (field : Name) for report hierarchies simplename hold only the field name until it's translated 
            String simplename ="";

            if (grd.getName().indexOf(":")>0){
                simplename = grd.getName().substring(0,grd.getName().indexOf(":"));
            }else{
                simplename = grd.getName();
            }
            simplename = TranslatorWorker.translateText(simplename,locale,siteId);
            
            //TODO TRN: no record for this key. its all right to have key here but it is better to replace with default text
            totalsFor = TranslatorWorker.translateText(totalsFor,locale,siteId);
            //String namePrefix="rep:pop:";
            //translatedName=TranslatorWorker.translateText(simplename,locale,siteId);
            if (grd.getName().indexOf(":")>0){
                simplename += grd.getName().substring(grd.getName().indexOf(":"));
            }
            String result;
            
            //create the actual output string for the totals line
            result=totalsFor+": ";
            
            if(simplename.compareTo("")==0 )
                result+=grd.getName();
            else result+=simplename;
            
            PdfPCell pdfc = null;
            if (grd.getReportMetadata().getHideActivities()){
                PdfPCell pdfc2 = new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(result+" ("+grd.getTotalUniqueRows()+")"),totalFont));
                pdfc2.setColspan(grd.getTotalDepth());
                table.addCell(pdfc2);
                getExportState().currentBackColor = new Color(235,235,235);
                pdfc2.setBackgroundColor(getExportState().currentBackColor);
            }else{
                pdfc = new PdfPCell(new Paragraph(ExportActivityToPDF.postprocessText(result+" ("+grd.getTotalUniqueRows()+")"),totalFont));
//              Integer sourceColsCount=grd.getSourceColsCount();
//              if( sourceColsCount!=null&&sourceColsCount>1){
//                  //When a column becomes hierarchy we have to subtract it from source columns.
//                  int span = sourceColsCount-grd.getReportMetadata().getHierarchies().size() - grd.getReportMetadata().getExtraTotalsCount();
//                  if (span!=0){
//                      pdfc.setColspan(span);
//                  }
//              }
                getExportState().currentBackColor = new Color(235,235,235);
                pdfc.setBackgroundColor(getExportState().currentBackColor);
                table.addCell(pdfc);
            }
            
            List<Cell> allTrailCells = grd.getTrailCells();
            
            // +1 is because we use the first column for title
            // trailCells which should be drawn: [n - totalNrOfDrawnColumns + 1, n-1]
            int nrOfTitleColumns = parent.getReportMetadata().getHideActivities() ? 0 : 1;
            int firstRelevantTrailCell = allTrailCells.size() - totalNrOfDrawnColumns + nrOfTitleColumns; 
            
            for(int i = firstRelevantTrailCell; i < allTrailCells.size(); i++)
            {
                Cell element = allTrailCells.get(i);
                if (element == null)
                {                       
                    PdfPCell emptyCell = new PdfPCell(new Paragraph(" ", totalFont));
                    emptyCell.setBackgroundColor(getExportState().currentBackColor);
                    table.addCell(emptyCell);
                }
                else
                {
                    // non-null cell
                    element.invokeExporter(this);
                }
            }
            
            getExportState().currentBackColor = null;
        }
    

    }

}
