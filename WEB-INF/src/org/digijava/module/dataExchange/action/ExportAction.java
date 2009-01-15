package org.digijava.module.dataExchange.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.form.ExportForm;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.util.ExportBuilder;
import org.xml.sax.SAXException;



public class ExportAction extends DispatchAction {

	private static Logger log = Logger.getLogger(ExportAction.class);

	public ActionForward export(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ExportForm eForm = (ExportForm)form;
		
		if (eForm.getActivityTree() != null){
			// TODO: add search criteria. 
			
			Activities activities = (new ObjectFactory()).createActivities();
			
	
			try {
				ExportBuilder eBuilder = new ExportBuilder(10190, RequestUtils.getSite(request).getSiteId());
				activities.getActivity().add(eBuilder.getActivityType(eForm.getActivityTree()));
			} catch (AmpExportException e) {
				log.error(e);
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
			response.setContentType("text/xml");
			response.setHeader("content-disposition","attachment; filename=exportActivities.xml"); // file name will generate by date
			ServletOutputStream outputStream = null;
			try {

				// package name
				JAXBContext	jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
	
	
	
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
				outputStream = response.getOutputStream();
				m.marshal(activities, outputStream);
	
			} catch (javax.xml.bind.JAXBException jex) {
				log.error("dataExchange.export.error JAXB Exception!",jex);
			} catch (java.io.FileNotFoundException fex) {
				log.error("dataExchange.export.error File not Found!",fex);
			} catch (Throwable ex) {
				log.error("dataExchange.export.error", ex);
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
	
		} else {
			log.debug("Wrong information from Export wizard.");
		}
		return null;

	}

}
