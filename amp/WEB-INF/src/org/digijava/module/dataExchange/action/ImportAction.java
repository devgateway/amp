/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.form.ImportForm;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.webservice.SinergyClient;
import org.springframework.util.FileCopyUtils;

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
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		ImportForm iform = (ImportForm) form;
		
		String[] langArray = {"en","fr","es"};
		if(iform.getLanguages() == null || iform.getLanguages().length < 1) iform.setLanguages(langArray);
		
		String[] options = {TranslatorWorker.translateText("update"),TranslatorWorker.translateText("overwrite")};
		if(iform.getOptions() == null || iform.getOptions().length < 1) iform.setOptions(options);
		
		if(request.getParameter("loadFile")!=null) {
			session.setAttribute("DEfileUploaded", "true");
			return modeLoadFile(mapping, iform, request, response);
		}
		if(request.getParameter("loadWs")!=null) {
			return modeLoadFromWs(mapping, iform, request, response);
		}
		//if(request.getParameter("import")!=null) return modeUploadedFile(mapping, iform, request, response);
		if(request.getParameter("saveImport")!=null) 
			return modeSaveImport(mapping, iform, request, response);
	
		ActionMessages errors = (ActionMessages) session.getAttribute("DEimportErrors");
		if(errors != null){
			saveErrors(request, errors);
			session.setAttribute("DEimportErrors", null);
		}
		return mapping.findForward("forward");
	}

	private ActionForward modeSaveImport(ActionMapping mapping, ImportForm form, HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
//		HttpSession session = request.getSession();
//		
//		ImportForm deImportForm= (ImportForm) form;
//		TeamMember tm = null;
//        if (session.getAttribute("currentMember") != null)
//        	tm = (TeamMember) session.getAttribute("currentMember");
//        AmpDEImportLog iLog = deImportForm.getActivityTree();
//        ImportBuilder iBuilder = (ImportBuilder) session.getAttribute("importBuilder");
//        iBuilder.saveActivities(request, tm);
		
        return mapping.findForward("forward");
	}

	private ActionForward modeLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, Exception {
		// TODO Auto-generated method stub
//		
//		HttpSession session = request.getSession();
//		String siteId = RequestUtils.getSite(request).getId().toString();
//		String locale= RequestUtils.getNavigationLanguage(request).getCode();
//
//		ImportForm deImportForm= (ImportForm) form;
//		
//		FormFile myFile = deImportForm.getUploadedFile();
//        byte[] fileData    = myFile.getFileData();
//        if(fileData == null || fileData.length < 1)
//        	{
//        		logger.info("The file is empty or not choosed any file");
//        		//return error
//        	}
//        
//        InputStream inputStream= new ByteArrayInputStream(fileData);
//        InputStream inputStream1= new ByteArrayInputStream(fileData);
//        
//        OutputStream outputStream = new ByteArrayOutputStream(); 
//        FileCopyUtils.copy(inputStream1, outputStream);
//        
//        TeamMember tm = null;
//        if (session.getAttribute("currentMember") != null)
//        	tm = (TeamMember) session.getAttribute("currentMember");
//        
//        ImportBuilder importBuilder = new ImportBuilder();
//        boolean importOk = false;
//		importOk = importBuilder.splitInChunks(inputStream);
//		if(!importOk) {
//			ActionMessages errors = new ActionMessages();
//			errors.add("title", new ActionMessage("error.aim.dataExchange.corruptedFile", TranslatorWorker.translateText("The file you have uploaded is corrupted. Please verify it and try upload again",locale,siteId)));
//			request.setAttribute("loadFile",null);
//			
//			if (errors.size() > 0){
//				session.setAttribute("DEimportErrors", errors);
//			}
//			else session.setAttribute("DEimportErrors", null);
//			return mapping.findForward("forwardError");
//		}
//		
//		importBuilder.generateLogForActivities(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd");
//        
//        importBuilder.createActivityTree();
//        
//        deImportForm.setActivityTree(importBuilder.getRoot());
//        
//        session.setAttribute("DELogGenerated", importBuilder.printLogs());
//        session.setAttribute("importBuilder", importBuilder);
//        session.setAttribute("DEfileUploaded", "true");
//        
		return mapping.findForward("afterUploadFile");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	
	private ActionForward modeLoadFromWs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, Exception {
//		String URL = "http://production.arm.synisys.com/sampleservice/";
//		String LOGIN = "testuser";
//		String PASSWORD = "testpassword"; 
//		String xsltfile = this.getServlet().getServletContext().getRealPath("/")+Constants.SYNERGY_IATI_IDML_XSL;
//		
//		HttpSession session = request.getSession();
//		ImportForm deImportForm= (ImportForm) form;
//		
//		String siteId = RequestUtils.getSite(request).getId().toString();
//		String locale= RequestUtils.getNavigationLanguage(request).getCode();
//		SinergyClient client = new SinergyClient(URL);
//		
//		InputStream inputStream= client.OutToIn(client.getFileFromService(LOGIN, PASSWORD,xsltfile));
//		
//		if (inputStream!=null){
//			InputStream inputStream1= client.OutToIn(client.getFileFromService(LOGIN, PASSWORD,xsltfile));
//			OutputStream outputStream = new ByteArrayOutputStream(); 
//	        FileCopyUtils.copy(inputStream1, outputStream);
//	        
//	        TeamMember tm = null;
//	        if (session.getAttribute("currentMember") != null)
//	        	tm = (TeamMember) session.getAttribute("currentMember");
//	        
//	        ImportBuilder importBuilder = new ImportBuilder();
//	        boolean importOk = false;
//			importOk = importBuilder.splitInChunks(inputStream);
//			if(!importOk) {
//				ActionMessages errors = new ActionMessages();
//				errors.add("title", new ActionMessage("error.aim.dataExchange.corruptedFile", TranslatorWorker.translateText("The Data could not be downloaded",locale,siteId)));
//				request.setAttribute("loadFile",null);
//				
//				if (errors.size() > 0){
//					session.setAttribute("DEimportErrors", errors);
//				}
//				else session.setAttribute("DEimportErrors", null);
//				return mapping.findForward("forwardError");
//			}
//			
//			importBuilder.generateLogForActivities(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd");
//	        
//	        importBuilder.createActivityTree();
//	        
//	        deImportForm.setActivityTree(importBuilder.getRoot());
//	        
//	        session.setAttribute("DELogGenerated", importBuilder.printLogs());
//	        session.setAttribute("importBuilder", importBuilder);
//	        session.setAttribute("DEfileUploaded", "true");
//	        
//			return mapping.findForward("afterUploadFile");
//		}
		return mapping.findForward("forwardError");
	}	
}
