package org.digijava.module.visualization.action;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.visualization.form.VisualizationForm;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

public class ExportProjectList extends Action {

    private static Logger logger = Logger.getLogger(ExportProjectList.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font TITLEFONT = new Font(Font.TIMES_ROMAN, 24, Font.BOLD);
    public static final Font SUBTITLEFONT = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/download");
        response.setHeader("content-disposition", "attachment;filename=dashboard.pdf");
        com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        VisualizationForm vForm = (VisualizationForm) form;
     
    	try {
        	PdfWriter pdfWriter =PdfWriter.getInstance(doc, baos);
            HttpSession session = request.getSession();
            doc.setPageCount(1);
            
            doc.open();
            //Paragraph pageTitle;
            //pageTitle = new Paragraph(vForm.getDashboard().getName().toUpperCase(), TITLEFONT);
            //pageTitle.setAlignment(Element.ALIGN_CENTER);
            //doc.add(pageTitle);
            //doc.add(new Paragraph(" "));
        
            Map<String, Map<AmpActivityVersion, String>> map = vForm.getItemProjectsList();
            
            for (Map.Entry<String, Map<AmpActivityVersion, String>> entry : map.entrySet()){
            	
				PdfPTable listTbl = null;
				listTbl = new PdfPTable(11);
				listTbl.setWidthPercentage(100);
	            PdfPCell itemTitleCell = new PdfPCell(new Paragraph(entry.getKey(), HEADERFONT));
	            itemTitleCell.setColspan(11);
	            listTbl.addCell(itemTitleCell);
	            
	            PdfPCell cell = new PdfPCell(new Paragraph("N°", HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setColspan(1);
	            listTbl.addCell(cell);
	            
	            cell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Project Title"), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setColspan(7);
	            listTbl.addCell(cell);
	            
	            cell = new PdfPCell(new Paragraph(TranslatorWorker.translateText("Amount"), HEADERFONT));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setColspan(3);
	            listTbl.addCell(cell);
	            
	            Map<AmpActivityVersion, String> map2 = entry.getValue();
	            
	           	Integer index = 1;
	           	for (Map.Entry<AmpActivityVersion, String> entry2 : map2.entrySet()){
	           		AmpActivityVersion act = entry2.getKey();
	           		
					cell = new PdfPCell(new Paragraph(index.toString(), HEADERFONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(1);
					listTbl.addCell(cell);
					
					cell = new PdfPCell(new Paragraph(act.getName(), HEADERFONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(7);
					listTbl.addCell(cell);
					
					cell = new PdfPCell(new Paragraph(entry2.getValue(), HEADERFONT));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setColspan(3);
					listTbl.addCell(cell);
					
					index++;
	           	}
	           	doc.add(listTbl);
            }
            //close document
            doc.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (Exception ex) {
            logger.error("error", ex);
        }

        return null;
    }
    
    public String getFormattedNumber(BigDecimal number){
    	 return FormatHelper.formatNumberNotRounded(number.doubleValue());
    }
    
}