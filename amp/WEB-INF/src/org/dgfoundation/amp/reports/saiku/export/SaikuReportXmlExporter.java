package org.dgfoundation.amp.reports.saiku.export;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.converters.GeneratedReportToXmlConverter;
import org.dgfoundation.amp.reports.xml.ObjectFactory;
import org.dgfoundation.amp.reports.xml.Report;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportXmlExporter implements SaikuReportExporter {
    
    @Override
    public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
        // Convert the report into xml document
        GeneratedReportToXmlConverter xmlConverter = new GeneratedReportToXmlConverter(report);
        Report xmlReport = xmlConverter.convert();
        JAXBContext jc = JAXBContext.newInstance(Report.class);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        Marshaller m = jc.createMarshaller();
        ObjectFactory xmlReportObjFactory = new ObjectFactory();
        m.marshal(xmlReportObjFactory.createReport(xmlReport), baos);
        
        baos.flush();
        baos.close();
        
        return baos.toByteArray();
    }
}
