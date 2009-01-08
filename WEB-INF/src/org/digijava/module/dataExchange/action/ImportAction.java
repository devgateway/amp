/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.dataExchange.form.ImportForm;
import org.xml.sax.SAXException;

/**
 * @author dan
 *
 */
public class ImportAction extends MultiAction {

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
		
		try{
		ImportForm deImportForm= (ImportForm) form;
		
		FormFile myFile = deImportForm.getUploadedFile();
        byte[] fileData    = myFile.getFileData();
        InputStream inputStream= new ByteArrayInputStream(fileData);
        
	        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
	        Unmarshaller m = jc.createUnmarshaller();
	        org.digijava.module.dataExchange.jaxb.Activities activities;
	        FeaturesUtil.errorLog="";
	        boolean xsdValidate = true;
	        try {
	        	
	        	if(xsdValidate){
	                // create a SchemaFactory that conforms to W3C XML Schema
	                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	                 // parse the purchase order schema
	                 Schema schema = sf.newSchema(new File(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd"));
	
	                 m.setSchema(schema);
	                 // set your error handler to catch errors during schema construction
	                 // we can use custom validation event handler
	                 m.setEventHandler(new ImportValidationEventHandler());
	           }
	        	
	        	activities = (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(inputStream);
	        	
	        	ArrayList a=(ArrayList) activities.getActivity();
	        	System.out.println("asd");
			} catch (SAXException ex) {
	            System.out.println(ex.getMessage());
	            ex.printStackTrace();
	        } 
			
			System.out.println("asd");
        
        } 
         catch (javax.xml.bind.JAXBException jex) {
            System.out.println("JAXB Exception!") ;
            jex.printStackTrace();
          }
         catch (java.io.FileNotFoundException fex) {
             System.out.println("File not Found!");
             fex.printStackTrace();
         }
         HttpSession session=request.getSession();
 		session.setAttribute("errorLogForDE",FeaturesUtil.errorLog);
 		if("".equals(FeaturesUtil.errorLog)) 
 			session.setAttribute("messageLogForDe","There are no errors after import. <br/> Import successfully");
        return mapping.findForward("forward");
	}
	
}
