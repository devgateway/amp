package org.digijava.kernel.ampapi.endpoints.reports;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.ErrorGatherer;
import net.sf.saxon.s9api.*;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.converters.GeneratedReportToXmlConverter;
import org.dgfoundation.amp.reports.xml.Report;
import org.dgfoundation.amp.reports.xml.ReportParameter;
import org.dgfoundation.amp.reports.xml.XmlReportUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an API for manipulating xml reports
 * 
 * @author Viorel Chihai
 */
public class ApiXMLService {
    
    /** Method used to create a xml Report for a specific amp report
     * @param reportParameter
     * @param reportId
     * @return report JAXB XML Report
     */
    public static Report getXmlReport(ReportParameter reportParameter, Long reportId) {
        ReportFormParameters formParams = XmlReportUtil.convertXmlCustomReportToJsonObj(reportParameter);
        
        if (reportId == null) {
            ApiErrorResponse errorResponse = ReportsUtil.validateReportConfig(formParams, true);
            if (errorResponse != null) {
                throw new ApiRuntimeException(Response.Status.BAD_REQUEST, errorResponse);
            }
            
            // we need reportId only to store the report result in cache
            reportId = (long) formParams.getReportName().hashCode();
            formParams.setCustom(true);
        }
        
        GeneratedReport generatedReport = ReportsUtil.getGeneratedReport(reportId, formParams);
        GeneratedReportToXmlConverter xmlConverter = new GeneratedReportToXmlConverter(generatedReport);
        Report xmlReport = xmlConverter.convert();

        return xmlReport;
    }

    /**
     * Marshal the report into an xml string or apply XSL transformation if one is provided.
     *
     * @param xsl XSL stylesheet
     * @param report report to be marshalled
     * @return xml representation of the report
     */
    public static String marshallOrTransform(String xsl, JAXBElement<Report> report) {
        if (xsl != null) {
            Source source;
            try {
                source = new JAXBSource(JAXBContext.newInstance(Report.class), report);
            } catch (JAXBException e) {
                throw new RuntimeException("Failed to create JAXBSource.", e);
            }
            return transform(xsl, source);
        } else {
            return marshall(report);
        }
    }

    /**
     * Marshal the jaxb report into xml string.
     */
    private static String marshall(JAXBElement<Report> report) {
        try {
            JAXBContext context = JAXBContext.newInstance(Report.class);
            StringWriter writer = new StringWriter();
            context.createMarshaller().marshal(report, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to marshall the report.", e);
        }
    }

    /**
     * Applies XLS Transform. XSLT up to v3 is supported.
     *
     * @param xsl xls stylesheet to use for transform
     * @param source xml source
     * @return result of the transformation, already marshalled into a string
     */
    private static String transform(String xsl, Source source) {
        List<StaticError> errors = new ArrayList<>();
        try {
            StreamSource xslSource = new StreamSource(new StringReader(xsl));

            Configuration config = new Configuration();
            Processor processor = new Processor(config);

            XsltCompiler compiler = processor.newXsltCompiler();
            compiler.setErrorListener(new ErrorGatherer(errors));
            XsltExecutable executable = compiler.compile(xslSource);

            XsltTransformer transformer = executable.load();

            transformer.setSource(source);

            StringWriter out = new StringWriter();
            Serializer serializer = processor.newSerializer(out);
            transformer.setDestination(serializer);

            transformer.transform();

            return out.toString();
        } catch (SaxonApiException e) {
            StringBuilder errorsAsStr = new StringBuilder();
            for (StaticError error : errors) {
                errorsAsStr.append("line:").append(error.getLineNumber())
                        .append(" col:").append(error.getColumnNumber())
                        .append(" msg:").append(error.getMessage()).append("\n");
            }
            throw new RuntimeException("Failed to transform. " + errorsAsStr, e);
        }
    }
}
