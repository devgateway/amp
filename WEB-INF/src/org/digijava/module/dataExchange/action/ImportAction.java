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
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.form.ImportForm;
import org.digijava.module.dataExchange.utils.ImportBuilder;
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
		
		String[] options = {"update","overwrite"};
		if(iform.getOptions() == null || iform.getOptions().length < 1) iform.setOptions(options);
		
		if(request.getParameter("loadFile")!=null) {
			session.setAttribute("DEfileUploaded", "true");
			return modeLoadFile(mapping, iform, request, response);
		}
		//if(request.getParameter("import")!=null) return modeUploadedFile(mapping, iform, request, response);
		if(request.getParameter("saveImport")!=null) 
			return modeSaveImport(mapping, iform, request, response);
	
		return mapping.findForward("forward");
	}

	private ActionForward modeSaveImport(ActionMapping mapping, ImportForm form, HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		ImportForm deImportForm= (ImportForm) form;
		TeamMember tm = null;
        if (session.getAttribute("currentMember") != null)
        	tm = (TeamMember) session.getAttribute("currentMember");
        AmpDEImportLog iLog = deImportForm.getActivityTree();
        ImportBuilder iBuilder = (ImportBuilder) session.getAttribute("importBuilder");
        iBuilder.saveActivities(request, tm);
		
        return mapping.findForward("forward");
	}

	private ActionForward modeLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		

		ImportForm deImportForm= (ImportForm) form;
		
		FormFile myFile = deImportForm.getUploadedFile();
        byte[] fileData    = myFile.getFileData();
        if(fileData == null || fileData.length < 1)
        	{
        		logger.info("The file is empty or not choosed any file");
        		//return error
        	}
        
        InputStream inputStream= new ByteArrayInputStream(fileData);
        InputStream inputStream1= new ByteArrayInputStream(fileData);
        
        OutputStream outputStream = new ByteArrayOutputStream(); 
        FileCopyUtils.copy(inputStream1, outputStream);
        
        TeamMember tm = null;
        if (session.getAttribute("currentMember") != null)
        	tm = (TeamMember) session.getAttribute("currentMember");
        
        ImportBuilder importBuilder = new ImportBuilder();
		importBuilder.splitInChunks(inputStream);
        
		importBuilder.generateLogForActivities(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd");
        
        importBuilder.createActivityTree();
        
        deImportForm.setActivityTree(importBuilder.getRoot());
        
        session.setAttribute("DELogGenerated", importBuilder.printLogs());
        session.setAttribute("importBuilder", importBuilder);
        session.setAttribute("DEfileUploaded", "true");
        
		return mapping.findForward("afterUploadFile");
	}

//	public ActionForward modeUploadedFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// TODO Auto-generated method stub
//		
//		HttpSession session = request.getSession();
//		
//
//			ImportForm deImportForm= (ImportForm) form;
//			
//			FormFile myFile = deImportForm.getUploadedFile();
//	        byte[] fileData    = myFile.getFileData();
//	        InputStream inputStream= new ByteArrayInputStream(fileData);
//	        InputStream inputStream1= new ByteArrayInputStream(fileData);
//	        
//	        TeamMember tm = null;
//	        if (session.getAttribute("currentMember") != null)
//	        	tm = (TeamMember) session.getAttribute("currentMember");
//	       
//	        ImportBuilder importBuilder = new ImportBuilder(request, tm, inputStream);
//	        boolean isOk = true;
//	        
//	        //FileCopyUtils.copy(arg0, arg1);
//	        
//	        try{
//	        	isOk = importBuilder.checkXMLIntegrity(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd",inputStream, inputStream1) ;
//	        }catch(Exception ex){
//	        	ex.printStackTrace();
//	        }
//	        
//	        if(isOk)
//	        {
//	        	importBuilder.builImportActivitiesToAMP();
//	        }
//	        else{
//	        	for (Iterator it = importBuilder.getGeneratedActivities().iterator(); it.hasNext();) {
//					InputStream is = (InputStream) it.next();
//					if(importBuilder.checkXMLIntegrityNoChunks(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd", is)){
//						importBuilder.builImportActivitiesToAMP();
//					}
//					else logger.error(" error found in one activity!!!!!!");
//				}
//	        }
//	      	AmpActivity activity = new AmpActivity();
//	        	
//		
//        
//        session=request.getSession();
//        session.setAttribute("errorLogForDE",FeaturesUtil.errorLog);
// 		if("".equals(FeaturesUtil.errorLog)) 
// 			session.setAttribute("messageLogForDe","There are no errors after import. <br/> Import successfully");
//        return mapping.findForward("forward");
//	
//	}
	
}
