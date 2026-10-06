package org.dgfoundation.amp.reports.saiku.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.HeaderCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportPdfExporter implements SaikuReportExporter {
    
    private GeneratedReport report;
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.reports.saiku.export.SaikuReportExporter#exportReport(org.dgfoundation.amp.newreports.GeneratedReport, org.dgfoundation.amp.newreports.GeneratedReport)
     */
    @Override
    public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
        this.report = report;
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document doc = createDocument();
        PdfWriter writer = PdfWriter.getInstance(doc, os);
        doc.open();
        populateReportWithData(doc, writer);
        doc.close();
        
        return os.toByteArray();
    }
    
    /**
     * @param doc
     * @param writer
     * @throws Exception
     */
    public void populateReportWithData(Document doc, PdfWriter writer) throws Exception {
        SaikuReportHtmlRenderer htmlRenederer = new SaikuReportHtmlRenderer(report);
        
        InputStream contentIs = new ByteArrayInputStream(htmlRenederer.renderFullHtml().toString().getBytes("UTF-8"));
        
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(getClass().getResourceAsStream("saiku.table.pdf.css"));
        cssResolver.addCss(cssFile);
        
        // HTML
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(this.getClass().getResource("").getPath());
        fontProvider.defaultEncoding = "UTF-8";
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        
        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(doc, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(contentIs, true);
        int n = contentIs.available();
        byte[] bytes = new byte[n];
        contentIs.read(bytes, 0, n);
    }
    
    private Document createDocument() {
        Document doc = new Document(calculateDocumentSize(calculateWidth()));
        return doc;
    }

    private int calculateWidth() {
        int ret = 0;
        if (report.generatedHeaders != null) {
            for (HeaderCell cell : report.generatedHeaders.get(0))
                ret += cell.getColSpan();
        }
        
        return ret;
    }
    
    private Rectangle calculateDocumentSize(int resultWidth) {
        Rectangle size = PageSize.A4.rotate();
        if (resultWidth >= 5) {
            size = PageSize.A3.rotate();
        }
        if (resultWidth >= 10) {
            size = PageSize.A2.rotate();
        }
        if (resultWidth >= 15) {
            size = PageSize.A1.rotate();
        }
        if (resultWidth >= 20) {
            size = PageSize.A0.rotate();
        }
        return size;
    }
}
