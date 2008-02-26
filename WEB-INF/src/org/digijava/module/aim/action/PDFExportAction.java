/**
 * PDFExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.pdf.GroupReportDataPDF;
import org.dgfoundation.amp.ar.view.pdf.PDFExporter;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class PDFExportAction extends Action implements PdfPageEvent{
    	
   	
	protected static Logger logger = Logger.getLogger(PDFExportAction.class);
	private  HttpSession session=null;
	private  String locale=null;
	private  Site site =null;
	
	public PDFExportAction(HttpSession session,String locale,Site site) {
	    this.session=session;
	    this.locale=locale;
	    this.site=site;
        }
	public PDFExportAction() {
	 super();
        }
	
	@SuppressWarnings("unchecked")
        public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		
		//for translation purposes
	    	Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
				
		 String siteId=site.getSiteId();
		 String locale=navigationLanguage.getCode();
		
		 
		GroupReportData rd=ARUtil.generateReport(mapping,form,request,response);
		rd.setCurrentView(GenericViews.PDF);
		HttpSession session = request.getSession();
		AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		AmpARFilter arf=(AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
		
		/*
		 * this should not be used anymore as the page size has been included in the ARFilters.
		 * String pageSize=formBean.getPdfPageSize();
		 */
		//use the session to get the existing filters
		String pageSize=null;
		if(arf!=null)
			pageSize=arf.getPageSize();//use the page size set in the filters 

		//the pagesize is not initialized in the filters	
		Rectangle page=null;
		
		if(pageSize==null)
			page=PageSize.A4;
		else{
			if(pageSize.equals("A0")) page=PageSize.A0;
			if(pageSize.equals("A1")) page=PageSize.A1;
			if(pageSize.equals("A2")) page=PageSize.A2;
			if(pageSize.equals("A3")) page=PageSize.A3;
			if(pageSize.equals("A4")) page=PageSize.A4;
		}

		response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition","attachment; filename="+r.getName());
	   
	        
		Document document = new Document(page.rotate(),5, 5, 5, 50);
		 
		
		PdfWriter writer=PdfWriter.getInstance(document,response.getOutputStream());
		
		writer.setPageEvent(new PDFExportAction(session,locale,site));
		
		//noteFromSession=AmpReports.getNote(request.getSession());
		
		String sortBy=(String) session.getAttribute("sortBy");
		
		if(sortBy!=null) rd.setSorterColumn(sortBy); 
		
		document.open();

		//	create source cols spanning:		
		
		PDFExporter.widths=new float[rd.getTotalDepth()];		
		for (int k = 0; k < rd.getSourceColsCount().intValue(); k++) {
		    PDFExporter.widths[k]=0.120f;
		}
		
		for (int k = rd.getSourceColsCount().intValue();k<rd.getTotalDepth() ; k++) {
			PDFExporter.widths[k]=0.08f;
		}
		
		
		PdfPTable table = new PdfPTable(PDFExporter.widths);
		table.setWidthPercentage(100);
		
		Font titleFont = new Font(Font.COURIER, 16, Font.BOLD);
		String translatedReportDescription="Description:";
	
		String translatedCurrency="";
		String translatedCurrentFilter="";
		String translatedAmount="";
		try{	
		     
		    translatedCurrentFilter=TranslatorWorker.translate("rep:pop:SelectedFilters",locale,siteId);
		    translatedCurrentFilter=("".equalsIgnoreCase(translatedCurrentFilter))?"Currently Selected Filters":translatedCurrentFilter;
		    
		    
		    translatedCurrency=TranslatorWorker.translate("aim:currency:" +((String) session.getAttribute(org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY)).toLowerCase().replaceAll(" ", ""),locale,siteId);
		    translatedCurrency=("".equalsIgnoreCase(translatedCurrency))?((String) session.getAttribute(org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY)):translatedCurrency;
		    
		    
		    translatedAmount=TranslatorWorker.translate("rep:pop:AllAmount",locale,siteId);
		    translatedAmount=("".equalsIgnoreCase(translatedAmount))?AmpReports.getNote(session):translatedAmount;
		    
		    //String currencyKey="aim:currency:" + org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY.toLowerCase().replaceAll(" ", "");
		    translatedReportDescription=TranslatorWorker.translate("rep:pop:Description",locale,siteId);
		}catch (WorkerException e){
		    logger.error("Error translating ", e);}
		
		
		
		PdfPCell pdfc = new PdfPCell(new Paragraph(r.getName(),titleFont));
		pdfc.setPaddingBottom(10);
		pdfc.setPaddingTop(10);
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		//translatedNotes
		//ArConstants.SELECTED_CURRENCY
		
		Font currencyFont = new Font(Font.COURIER, 10, Font.ITALIC);
		pdfc = new PdfPCell(new Paragraph(translatedAmount+": "+translatedCurrency,currencyFont));
		pdfc.setPaddingBottom(2);
		pdfc.setPaddingTop(2);
		pdfc.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		pdfc.setPaddingLeft(20);
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		Map<String, Object> props=arf.getPropertiesMap();
		
		Iterator<String> keys=props.keySet().iterator();
		StringBuffer strFilters=new StringBuffer();
		
        		try {
        	    while (keys.hasNext()) {
        		String key = keys.next();
        		strFilters.append(("".equalsIgnoreCase(TranslatorWorker.translate(" filterproperty:" + key, locale, siteId)))?key:TranslatorWorker.translate(" filterproperty:" + key, locale, siteId));
        		strFilters.append(": ");
        		strFilters.append(("".equalsIgnoreCase(TranslatorWorker.translate(" filterproperty:" + props.get(key), locale, siteId)))?props.get(key):TranslatorWorker.translate(" filterproperty:" + props.get(key), locale, siteId));
        	        strFilters.append(", ");
        	    }
        	} catch (WorkerException e) {
        	    logger.error("Error translating", e);
        	}
		
        	strFilters.delete(strFilters.length()-2,strFilters.length());
		
		pdfc = new PdfPCell(new Paragraph(translatedCurrentFilter +" :"+strFilters.toString(),currencyFont));
		pdfc.setPaddingBottom(2);
		pdfc.setPaddingTop(2);
		pdfc.setPaddingLeft(20);
		pdfc.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		
		if(!"".equalsIgnoreCase(r.getReportDescription())){
        		pdfc = new PdfPCell(new Paragraph(translatedReportDescription+" "+r.getReportDescription()));
        		pdfc.setColspan(rd.getTotalDepth());
        		table.addCell(pdfc);
        	}
		
		    GroupReportDataPDF grdp=new GroupReportDataPDF(table,(Viewable) rd,null);
		    //it is for not to show first group it is always the report title;
		    //in a few grdp.setVisible(false);
		    grdp.setMetadata(r);
	            grdp.generate();
              
		
		document.add(table);
		document.close();
		return null;

	}

	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		
		
	}

	public void onStartPage(PdfWriter writer, Document arg1) {
		if(PDFExporter.headingCells==null) return;
		
		  PdfContentByte cb = writer.getDirectContent();
		  cb.saveState();
		
		  if(writer.getPageNumber()==1) 
		      return;
		  
		  
        		Iterator i=PDFExporter.headingCells.iterator();
			PdfPTable table = new PdfPTable(PDFExporter.widths);
			table.setWidthPercentage(100);
			while (i.hasNext()) {
				PdfPCell element = (PdfPCell) i.next();
				table.addCell(element);
			}
			try {
				arg1.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
				logger.error("Error onStartPage",e);
			}
		
	}

	public void onEndPage(PdfWriter writer, Document document) {
	try {
	  
	    PdfContentByte cb = writer.getDirectContent();
	    cb.saveState();  
	    String siteId = site.getSiteId();
    	    AmpReports r = (AmpReports) session.getAttribute("reportMeta");
    	    r.setSiteId(siteId);
    	    r.setLocale(locale);
    	    
    	    
    	    BaseFont font = BaseFont.createFont(BaseFont.COURIER,BaseFont.CP1250,false);
    	    StringBuffer text =new StringBuffer();
    		
    	    if (r.getFormatedUpdatedDate() != null) {
    		text.append( TranslatorWorker.translate("rep:print:lastupdate", locale, siteId));
    		text.append(r.getFormatedUpdatedDate());
    		text.append(" ");
    		text.append(TranslatorWorker.translate("rep:print:user", locale, siteId));
    		text.append(r.getUser());
    		
    	    } else {
    		text.append(TranslatorWorker.translate("rep:print:user", locale, siteId));
    		text.append(r.getUser());
    	    }
    	
    	    
    	    //text.append( "Page " + writer.getPageNumber());
    	    float textSize = font.getWidthPoint(text.toString(),  10);
    	    float textBase = document.bottom() - 20;
    	   // float adjust = font.getWidthPoint("0", 10);
    	    textSize = font.getWidthPoint(text.toString(),  10);
    	    textBase = document.bottom() - 15;
    	    cb.beginText();
    	    cb.setFontAndSize(font, 10);
    	    float adjust = font.getWidthPoint("0",  10);
    	    cb.setTextMatrix(document.right() - textSize - adjust, textBase);
    	    cb.showText(text.toString());
    	    cb.endText();
    	    
    	    
    	    cb.beginText();
	    cb.setFontAndSize(font, 10);
	    textBase = document.bottom() - 30;
	    StringBuffer pageText=new StringBuffer();
	    pageText.append("Page " + writer.getPageNumber());
	    
	    adjust = font.getWidthPoint("0",  10);
	    textSize = font.getWidthPoint(pageText.toString(),  10);
	    cb.setTextMatrix(document.right() - textSize - adjust, textBase);
	    cb.showText(pageText.toString());
	    cb.endText();
    	    cb.restoreState();
	
	} catch (Exception e) {
	    logger.error("Error onEndPage",e);
	    throw new ExceptionConverter(e);
	}

    }

	public void onCloseDocument(PdfWriter arg0, Document arg1) {
	   
		
	}

	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {
		
	}

	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onChapter(PdfWriter arg0, Document arg1, float arg2, Paragraph arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3, Paragraph arg4) {
		// TODO Auto-generated method stub
		
	}

	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
}
