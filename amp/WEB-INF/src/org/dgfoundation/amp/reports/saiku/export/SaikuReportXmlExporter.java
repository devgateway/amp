package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.reports.converters.GeneratedReportToXmlConverter;
import org.w3c.dom.Document;

/**
 * @author Viorel Chihai
 *
 */
public class SaikuReportXmlExporter implements SaikuReportExporter {
	
	@Override
	public byte[] exportReport(GeneratedReport report, GeneratedReport dualReport) throws Exception {
		// Convert the report into xml document
		GeneratedReportToXmlConverter xmlConverter = new GeneratedReportToXmlConverter(report);
		Document xmlDocument = xmlConverter.convert();
		DOMSource source = new DOMSource(xmlDocument);
		
		// Convert the stream where the xml document will be saved
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(baos);
		
		// Transform the xml document
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, streamResult);
		
		baos.flush();
		baos.close();
		
		return baos.toByteArray();
	}
}
