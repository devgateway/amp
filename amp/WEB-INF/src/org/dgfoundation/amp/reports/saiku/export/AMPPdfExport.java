package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.saiku.web.export.PdfReport;
import org.saiku.web.rest.objects.resultset.QueryResult;

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

public class AMPPdfExport extends PdfReport {

	public byte[] pdf(JsonBean jb, String type, ReportSpecificationImpl report, LinkedHashMap<String, Object> queryModel)
			throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Document doc = createDocument(jb);
		PdfWriter writer = PdfWriter.getInstance(doc, os);
		doc.open();
		populatePdf(doc, writer, jb, type, report, queryModel);
		doc.close();
		return os.toByteArray();
	}

	public void populatePdf(Document doc, PdfWriter writer, JsonBean jb, String type, ReportSpecificationImpl report,
			LinkedHashMap<String, Object> queryModel) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		String content = AMPJSConverter.convertToHtml(jb, type);
		StringBuilder htmlString = new StringBuilder(
				"<!DOCTYPE html><html><head><title></title></head><body><div>AMP Export - ").append(
				dateFormat.format(date)).append("</div><div>&nbsp;</div>");
		String currency = report.getSettings().getCurrencyCode();
		if (queryModel.containsKey("settings")) {
			LinkedHashMap<String, Object> settings = (LinkedHashMap<String, Object>) queryModel.get("settings");
			currency = settings.get("1").toString();
		}
		htmlString.append("<div><b>").append(TranslatorWorker.translateText("Currency")).append(": </b>")
				.append(currency).append("</div>");
		String units = "Amounts in units";
		if (report.getSettings().getUnitsMultiplier() == (1d / 1000000d)) {
			units = "Amounts in millions";
		} else if (report.getSettings().getUnitsMultiplier() == (1d / 1000d)) {
			units = "Amounts in thousands";
		}
		htmlString.append("<div><b>").append(TranslatorWorker.translateText(units)).append("</b></div>")
				.append(content).append("</body></html>");

		InputStream contentIs = new ByteArrayInputStream(htmlString.toString().getBytes("UTF-8"));
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

	private Document createDocument(JsonBean jb) {
		Document doc = new Document(calculateDocumentSize(calculateWidth(jb)));
		return doc;
	}

	private int calculateWidth(JsonBean jb) {
		int ret = 0;
		if (jb.get("headers") != null) {
			ret = ((List) jb.get("headers")).size();
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
