/**
 * 
 */
package org.digijava.module.help.action;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.util.GlossaryUtil;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author mouhamad
 *
 */
public class PdfExport extends Action {

    /**
     * 
     */
    private static Logger logger = Logger.getLogger(PdfExport.class);
        
    /**
     * 
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //
        ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/pdf");
        response.setHeader("content-disposition", "attachment;filename=glossary.pdf");
        //
        try {
            String language = RequestUtils.getNavigationLanguage(request).getCode();
            String moduleInstance = RequestUtils.getModuleInstance(request).getInstanceName();
            Site site = RequestUtils.getSite(request);
            //
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);          
            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
            //
            document.open();
            Font titleFont = FontFactory.getFont("Arial", 24, Font.BOLD);
            Paragraph p = new Paragraph(TranslatorWorker.translateText("Glossary export", language, site), titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(new Paragraph(" "));
            //
            List<HelpTopic> helpTopics = GlossaryUtil.getChildTopics(site, moduleInstance, null);
            //
            writechild(20, helpTopics, moduleInstance, site, language, document);
            //
            document.close();
            pdfWriter.close();
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            logger.error("error", ex);
        }
        //
        return null;        
    }
    
    /**
     * 
     * @param size
     * @param helpTopics
     * @param moduleInstance
     * @param siteId
     * @param language
     * @param document
     */
    @SuppressWarnings("unchecked")
    private void writechild (float size, List<HelpTopic> helpTopics, String moduleInstance, Site site, String language, Document document) {
        //
        try {
            List<HelpTopic> childs = null;
            String body = null;
            Paragraph pp = null;
            List<Element> objects = null;
            if (size < 10) size = 10;
            Font fontT = FontFactory.getFont("Arial", size, Font.BOLD);
            Font fontB = FontFactory.getFont("Arial", 10, Font.NORMAL);
            for (HelpTopic helpTopic : helpTopics) {
                //
                pp = new Paragraph(TranslatorWorker.translateText(helpTopic.getTopicKey(), language, site), fontT);
                //
                document.add(pp);
                document.add(new Paragraph(" "));
                //
                body = DbUtil.getEditorBody(site, helpTopic.getBodyEditKey(), language);
                if (body == null) body = "";
                //
                objects = HTMLWorker.parseToList(new StringReader(TranslatorWorker.translateText(body, language, site)), null);
                for (Element element : objects) {
                    pp = (Paragraph) element;
                    pp.setFont(fontB);
                    //
                    document.add(pp);
                }
                document.add(new Paragraph(" "));
                //
                childs = GlossaryUtil.getChildTopics(site, moduleInstance, helpTopic.getHelpTopicId());
                if ((childs != null) && (!childs.isEmpty())) {
                    writechild((size-2), childs, moduleInstance, site, language, document); 
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
