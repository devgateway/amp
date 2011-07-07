package org.digijava.module.dataExchange.webservice;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.digijava.module.dataExchange.utils.Constants;

@Path("/activities.out")
public class Porjects {
    @GET 
    @Produces("text/xml")
    public String getActivities() {
    	JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Constants.JAXB_INSTANCE);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			java.io.StringWriter sw = new StringWriter();
			m.marshal(WsHelper.GenerateWsExport(),sw);
			return sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
    }
}
