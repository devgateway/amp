/**
 * PDFExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.view.pdf.GroupReportDataPDF;
import org.dgfoundation.amp.ar.view.pdf.PDFExporter;
import org.dgfoundation.amp.ar.view.pdf.ReportPdfExportState;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class PDFExportAction extends Action implements PdfPageEvent
{           
    protected static Logger logger = Logger.getLogger(PDFExportAction.class);
    
    private HttpSession session;
    private String locale;
//  private Site site;
    private GroupReportData rd;
    private AmpARFilter arf;
    private AmpReports report;
    private PdfPTable contenTable;
    private GroupReportDataPDF rootReportExporter;
    private HttpServletRequest request;

    public PDFExportAction() {

    }
    
    float[] buildWidths()
    {
        float[] widths = new float[rd.getTotalDepth()];     
        for (int k = 0; k < widths.length; k++)
        {
            if ( k < rd.getTotalDepth())
                widths[k] = 0.120f;
        }
    
        for (int k = widths.length; k < rd.getTotalDepth() ; k++)
            widths[k] = 0.08f;
        return widths;
    }
    
    public void doPdfExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // This a temporary fix to avoid stack overflow error in large reports AMP-5324
        // temporary my a$$
        Rectangle page = new Rectangle(new Float("1500"), new Float("1500"));
        Document document = new Document(page.rotate(),5, 5, 15, 50);
            
        //
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, report.getName() + ".pdf"));
        //
        PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
        //
        writer.setPageEvent(this);
        // noteFromSession=AmpReports.getNote(request.getSession());    
        Map<Long, MetaInfo<String>> sorters = ReportContextData.getFromRequest().getReportSorters();
            
        String sortBy = ReportContextData.getFromRequest().getSortBy();     
        if(sortBy != null)
        {
            rd.setSorterColumn(sortBy);
            rd.setSortAscending(ReportContextData.getFromRequest().getSortAscending());
        }
            
        float[] widths = buildWidths();
        contenTable = new PdfPTable(widths);
        contenTable.setWidthPercentage(100);
            
        if ( sorters != null && sorters.size() > 0 )
        {
            rd.importLevelSorters(sorters, report.getHierarchies().size());
            rd.applyLevelSorter();
        }
        rootReportExporter = new GroupReportDataPDF(contenTable,(Viewable) rd, null);
        this.rootReportExporter.state = new ReportPdfExportState(widths);
        
        //it is for not to show first group it is always the report title;
        //in a few grdp.setVisible(false);
        rootReportExporter.setMetadata(report);
        rootReportExporter.generate();
        // open document
        document.open();
        // add content
        document.add(contenTable);
        // document.add(grdp.getTable());
        document.close();   
    }
    
    @SuppressWarnings("unchecked")
        public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        
        //Site siteInExec = RequestUtils.getSite(request);
        this.request = request;
        this.session = request.getSession();
        this.locale = RequestUtils.getNavigationLanguage(request).getCode();
        
        AdvancedReportForm reportForm = (AdvancedReportForm) form;
        request.setAttribute("statementPositionOptions", reportForm.getStatementPositionOptions());
        request.setAttribute("logoPositionOptions", reportForm.getLogoPositionOptions());
        request.setAttribute("statementOptions", reportForm.getStatementOptions());
        request.setAttribute("logoOptions", reportForm.getLogoOptions());
        request.setAttribute("dateOptions", reportForm.getDateOptions());


        boolean initFromDB = false;
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm == null || tm.getTeamId() == null )
            tm = null;
        
        if (tm == null)
        {
            initFromDB = "true".equals(request.getParameter("resetFilter"));
        }
        
        logger.info("reportContextId: " + ReportContextData.getFromRequest(true).getContextId()); // DO NOT DELETE THIS CALL - it ensures that a ReportContextMap exists
        
        try {
            report = ARUtil.getReferenceToReport();
        } catch (Exception e) {
            ARUtil.generateReportNotFoundPage(response);
            return null;
        }
        report.validateColumnsAndHierarchies();
        
        this.arf = ReportContextData.getFromRequest().loadOrCreateFilter(initFromDB, report);
        
        if (tm == null){
            arf.setPublicView(true);
            }
     
        this.rd = ARUtil.generateReport(report, arf, true, false);
        
        ARUtil.cleanReportOfHtmlCodes(rd);
        
        rd.setCurrentView(GenericViews.PDF);
        
        //AmpReports r =  ReportContextData.getFromRequest().getReportMeta();
        /*
         * this should not be used anymore as the page size has been included in the ARFilters.
         * String pageSize=formBean.getPdfPageSize();
         */
        //use the session to get the existing filters
        if (session.getAttribute("currentMember") != null || arf.isPublicView())
        {
            try
            {
                doPdfExport(request, response);
            }
            catch(Exception e)
            {
                logger.error("error while generating PDF", e);
            }
            return null;
        }else{
            session.setAttribute("sessionExpired", true);
            response.setContentType("text/html");   
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            String url = FeaturesUtil.getGlobalSettingValue("Site Domain");
            String alert = TranslatorWorker.translateText("Your session has expired. Please log in again.");
            String script = "<script>opener.close();" 
                + "alert('"+ alert +"');" 
                + "window.location=('"+ url +"');"
                + "</script>";
            out.println(script);
            out.close();    
            outputStream.close();
            return null;
        }
    }

    public void onOpenDocument(PdfWriter arg0, Document arg1) {
        
        
    }

    @Override
    public void onStartPage(PdfWriter writer, Document arg1) {
        if (rootReportExporter.state.headingCells == null)
            return;
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        PdfPTable table = new PdfPTable(rootReportExporter.state.widths);

        table.setWidthPercentage(100);

        String translatedCurrency="";
        String translatedCurrentFilter="";
        String translatedAmount = TranslatorWorker.translateText(arf.getUnitsOptions().userMessage);
        String translatedReportDescription="Description:";

        // HEADER/FOOTER logo/statement             
        if ( this.request.getAttribute("logoOptions") == null ||  this.request.getAttribute("logoOptions").equals("0")) {//disabled
            // do nothing 
        } else if (this.request.getAttribute("logoOptions").equals("1")) {//enabled                                                                                                             
            if (this.request.getAttribute("logoPositionOptions").equals("0")) {//header                     
                Image logo = null;
                int end = this.request.getRequestURL().length() - this.request.getServletPath().length();
                String urlPrefix = this.request.getRequestURL().substring(0, end); 
                try {
                    logo = Image.getInstance(urlPrefix + "/TEMPLATE/ampTemplate/images/AMPLogo.png");
                } catch (BadElementException | IOException e) {
                    e.printStackTrace();
                }
                PdfPCell pdfc;  
                pdfc = new PdfPCell(logo);
                pdfc.setPaddingBottom(10);
                pdfc.setPaddingTop(10);
                pdfc.setPaddingLeft(10);
                pdfc.setColspan(rd.getTotalDepth());
                table.addCell(pdfc);
            } else if (this.request.getAttribute("logoPositionOptions").equals("1")) {//footer
                // see endPage function
            }               
        }
        if ( this.request.getAttribute("statementOptions") == null || this.request.getAttribute("statementOptions").equals("0")) {//disabled
            // do nothing 
        } else if (this.request.getAttribute("statementOptions").equals("1")) {//enabled                                        
            String stmt = TranslatorWorker.translateText("This REPORT was created by AMP");
            stmt += " " + FeaturesUtil.getCurrentCountryName();
            if (this.request.getAttribute("dateOptions").equals("0")) {//disabled
                // no date
            } else if (this.request.getAttribute("dateOptions").equals("1")) {//enable      
                stmt += " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
            }                                                       
            if (this.request.getAttribute("statementPositionOptions").equals("0")) {//header        
                PdfPCell pdfc;
                    Font font = new Font(Font.FontFamily.COURIER, 8);
                pdfc = new PdfPCell(new Paragraph(stmt, font));
                pdfc.setPaddingBottom(10);
                pdfc.setPaddingTop(10);
                pdfc.setColspan(rd.getTotalDepth());
                table.addCell(pdfc);
            } else if (this.request.getAttribute("statementPositionOptions").equals("1")) {//footer
                // see endPage function
            }               
        }
        //
        translatedCurrentFilter=TranslatorWorker.translateText("Currently Selected Filters:");
        translatedCurrentFilter=("".equalsIgnoreCase(translatedCurrentFilter))?"Currently Selected Filters":translatedCurrentFilter;

        translatedCurrency=TranslatorWorker.translateText(ReportContextData.getFromRequest().getSelectedCurrencyText());
        
        translatedReportDescription=TranslatorWorker.translateText("Description:");
    
        PdfPCell pdfc;
            Font titleFont = new Font(Font.FontFamily.COURIER, 8);
            //Font titleFont = new Font(Font.COURIER, 16, Font.BOLD);               
        pdfc = new PdfPCell(new Paragraph(rd.getName(),titleFont));
        pdfc.setPaddingBottom(10);
        pdfc.setPaddingTop(10);
        pdfc.setColspan(rd.getTotalDepth());
        table.addCell(pdfc);        


        if(!"".equalsIgnoreCase(report.getReportDescription())){
            pdfc = new PdfPCell(new Paragraph(translatedReportDescription+" " + report.getReportDescription()));
            pdfc.setColspan(rd.getTotalDepth());
            table.addCell(pdfc);
        }


        pdfc = null;
        //translatedNotes
        //ArConstants.SELECTED_CURRENCY
        //Currency
            Font currencyFont = new Font(Font.FontFamily.COURIER, 10, Font.ITALIC);
        pdfc = new PdfPCell(new Paragraph(translatedAmount+": "+translatedCurrency,currencyFont));
        pdfc.setPaddingBottom(2);
        pdfc.setPaddingTop(2);
        pdfc.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        pdfc.setPaddingLeft(20);
        pdfc.setColspan(rd.getTotalDepth());
        table.addCell(pdfc);
        //filters
        Map<String, Object> props=arf.getPropertiesMap();

        Iterator<String> keys=props.keySet().iterator();
        StringBuffer strFilters=new StringBuffer();

        
            while (keys.hasNext()) {
                String key = keys.next();

                //String translatedName=TranslatorWorker.translate("filterproperty:" + key,locale,siteId);
                String translatedName=TranslatorWorker.translateText(key);
                translatedName=("".equalsIgnoreCase(translatedName))?key:translatedName;

                String translatedValue=TranslatorWorker.translateText(props.get(key).toString());
                translatedValue=("".equalsIgnoreCase(translatedValue))?props.get(key).toString():translatedValue;

                strFilters.append(translatedName);
                strFilters.append(":");
                strFilters.append(translatedValue);
                strFilters.append(",");
            }

        strFilters.delete(strFilters.length()-2,strFilters.length());
        try {
            table.completeRow();
            //adding the table to the document before adding cells avoid stack overflow error, we can flush the memory and send the content to the client
            for(PdfPCell element:rootReportExporter.state.headingCells)
            {
                if (element.getPhrase() != null && element.getPhrase().getContent().equals(PDFExporter.FORCE_NEW_LINE))
                    table.completeRow(); // workaround for lowagie's inability to render AxB merged cells when both A and B are > 1 (text is rendered and positioned correctly, but internal borders are drawn and calculations are screwed)
                else
                {
                    table.addCell(element);
                }
            }
            arg1.add(table);
        } catch (Exception e) {
            logger.error("Error onStartPage",e);
        }
        
    }

    public void onEndPage(PdfWriter writer, Document document) {
    try {
      
        PdfContentByte cb = writer.getDirectContent();
        //cb.saveState();
        
            Long siteId = RequestUtils.getSite(request).getId();
            AmpReports r =  ReportContextData.getFromRequest().getReportMeta();
            r.setSiteId(siteId);
            r.setLocale(locale);
            BaseFont font = BaseFont.createFont(BaseFont.COURIER,BaseFont.CP1250,false);
            // HEADER/FOOTER logo/statement             
            if (this.request.getAttribute("logoOptions") ==null || this.request.getAttribute("logoOptions").equals("0")) {//disabled
                // do nothing 
            } else if (this.request.getAttribute("logoOptions").equals("1")) {//enabled                                                                                                             
                if (this.request.getAttribute("logoPositionOptions").equals("0")) {//header                     
                    // see startPage function
                } else if (this.request.getAttribute("logoPositionOptions").equals("1")) {//footer
                    Image logo = null;
                    byte[] b = new byte[900];
                    this.session.getServletContext().getResourceAsStream("/TEMPLATE/ampTemplate/images/AMPLogo.png").read(b);
                    try {
                        logo = Image.getInstance(b);
                        logo.setAbsolutePosition(10, 20);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cb.beginText();
                    cb.setFontAndSize(font, 8);
                    cb.addImage(logo);
                    cb.endText();
                }               
            }
            if (this.request.getAttribute("statementOptions") == null || this.request.getAttribute("statementOptions").equals("0")) {//disabled
                // do nothing 
            } else if (this.request.getAttribute("statementOptions").equals("1")) {//enabled                                        
                String stmt = "";
                try {
                    //TODO TRN:this should use default text instead of this key. if there is no such default text in this case, then leaving key is jut all right.
                    stmt = TranslatorWorker.translateText("This REPORT was created by AMP");
                    stmt += " " + FeaturesUtil.getCurrentCountryName();
                } catch (Exception e){
                    logger.error("Error translating ", e);}
                stmt += " " + FeaturesUtil.getCurrentCountryName();
                if (this.request.getAttribute("dateOptions").equals("0")) {//disabled
                    // no date
                } else if (this.request.getAttribute("dateOptions").equals("1")) {//enable      
                    stmt += " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
                }                                                       
                if (this.request.getAttribute("statementPositionOptions").equals("0")) {//header        
                    // see startpage function
                } else if (this.request.getAttribute("statementPositionOptions").equals("1")) {//footer
                    float textBase = document.bottom() - 20;
                    cb.beginText();
                    cb.setFontAndSize(font, 8);
                    cb.setTextMatrix(document.left() + 50, textBase);
                    cb.showText(stmt);
                    cb.endText();                   
                }               
            }          
            StringBuffer text =new StringBuffer();
            
//          if (r.getFormatedUpdatedDate() != null) {
//          text.append( TranslatorWorker.translate("rep:print:lastupdate", locale, siteId));
//          text.append(" ");
//          text.append(r.getFormatedUpdatedDate());
//          text.append(" ");
//          } 
            if(r.getUser()!=null){
            String translatedUser=TranslatorWorker.translateText("User :", locale, siteId);
            if ("".equalsIgnoreCase(translatedUser)){
                    translatedUser="User:";
               }
                text.append(translatedUser);
                text.append(" ");
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
        //TODO TRN: Key is all right here but lets think about using body as translation.
        String translatedPage=TranslatorWorker.translateText("Page", locale, siteId);
       if ("".equalsIgnoreCase(translatedPage)){
           translatedPage="Page:";
       }
        pageText.append(translatedPage +" "+writer.getPageNumber());
        
        adjust = font.getWidthPoint("0",  10);
        textSize = font.getWidthPoint(pageText.toString(),  10);
        cb.setTextMatrix(document.right() - textSize - adjust, textBase);
        cb.showText(pageText.toString());
        cb.endText();
        cb.restoreState();
    
    } catch (Exception e) {
        logger.error("Error onEndPage", e);
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
