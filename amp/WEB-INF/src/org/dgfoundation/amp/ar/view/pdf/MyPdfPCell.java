package org.dgfoundation.amp.ar.view.pdf;


import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public class MyPdfPCell extends PdfPCell
{

    public MyPdfPCell(Paragraph para)
    {
        super(para);
    }
    
    @Override
    public String toString()
    {
        return String.format("[%s %dx%d]", this.getPhrase().getContent(), this.getColspan(), this.getRowspan());
    }
}
