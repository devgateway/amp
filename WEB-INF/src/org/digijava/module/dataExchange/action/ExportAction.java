package org.digijava.module.dataExchange.action;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.ampharvester.action.ImportExportManagerAction;
import org.digijava.module.ampharvester.api.ExportManager;
import org.digijava.module.ampharvester.form.ImportExportManagerForm;
import org.digijava.module.ampharvester.jaxb10.ActivityType;
import org.digijava.module.ampharvester.util.DbUtil;
import org.digijava.module.ampharvester.util.XmlTransformerHelper;

public class ExportAction extends DispatchAction{

	  private static Logger log = Logger.getLogger(ExportAction.class);
	
	  public ActionForward export(ActionMapping mapping, ActionForm form,
		      HttpServletRequest request, HttpServletResponse response) throws  Exception {

		    response.setContentType("text/xml");
		    response.setHeader("content-disposition", "attachment; filename=exportActivities.xml"); // file neme will generate by date
		    ServletOutputStream outputStream = null;
		    try {
		      outputStream = response.getOutputStream();

		      outputStream.println("export test");
		    } catch (Exception ex) {
		      log.error("dataExchange.export.error", ex);
		    } finally {
		    	if (outputStream != null){
		    		outputStream.close();
		    	}
		    }
		    return null;

		  }
	

}
