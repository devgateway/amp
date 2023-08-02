package org.dgfoundation.amp.ar.view.pdf;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;

import java.awt.*;
import java.util.ArrayList;

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
    public BaseColor currentBackColor;
    public BaseColor lastedUsedColor;

    public ReportPdfExportState(float[] widths)
    {
        this.widths = widths;
    }
}
