/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.dataExchange.form.ImportForm;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.utils.ImportBuilder;
import org.xml.sax.SAXException;

/**
 * @author dan
 *
 */
public class ImportAction extends MultiAction {

	private static Logger logger = Logger.getLogger(ImportAction.class);
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		if(request.getParameter("import")!=null) return modeUploadedFile(mapping, form, request, response);
				
		return mapping.findForward("forward");
	}

	public ActionForward modeUploadedFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		

			ImportForm deImportForm= (ImportForm) form;
			
			FormFile myFile = deImportForm.getUploadedFile();
	        byte[] fileData    = myFile.getFileData();
	        InputStream inputStream= new ByteArrayInputStream(fileData);
	        InputStream inputStream1= new ByteArrayInputStream(fileData);
	        
	        TeamMember tm = null;
	        if (session.getAttribute("currentMember") != null)
	        	tm = (TeamMember) session.getAttribute("currentMember");
	       
	        ImportBuilder importBuilder = new ImportBuilder(request, tm, inputStream);
	        boolean isOk = true;
	        
	        try{
	        	isOk = importBuilder.checkXMLIntegrity(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd",inputStream, inputStream1) ;
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }
	        
	        if(isOk)
	        {
	        	importBuilder.builImportActivitiesToAMP();
	        }
	        else{
	        	for (Iterator it = importBuilder.getGeneratedActivities().iterator(); it.hasNext();) {
					InputStream is = (InputStream) it.next();
					if(importBuilder.checkXMLIntegrityNoChunks(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd", is)){
						importBuilder.builImportActivitiesToAMP();
					}
					else logger.error(" error found in one activity!!!!!!");
				}
	        }
	      	AmpActivity activity = new AmpActivity();
	        	
		
        
        session=request.getSession();
        session.setAttribute("errorLogForDE",FeaturesUtil.errorLog);
 		if("".equals(FeaturesUtil.errorLog)) 
 			session.setAttribute("messageLogForDe","There are no errors after import. <br/> Import successfully");
        return mapping.findForward("forward");
	
	}
	
}
