package org.dgfoundation.amp.ar.view.pdf;

import java.awt.Color;
import java.util.ArrayList;

import com.lowagie.text.pdf.PdfPCell;

/**
 * utility class for holding the global state of an export
 * @author simple
 *
 */
public class ReportPdfExportState
{
    /**
     * this static array will hold the heading cells that need to be displayed on the start of each page.
     * it will get initialized only once and then used by onStartPage
     */
    public ArrayList<PdfPCell> headingCells;    
    public float[] widths;
    public Color currentBackColor;
    public Color lastedUsedColor;

    public ReportPdfExportState(float[] widths)
    {
        this.widths = widths;
    }
}
