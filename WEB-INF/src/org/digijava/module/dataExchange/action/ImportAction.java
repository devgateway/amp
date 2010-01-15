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
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.dataExchange.Exception.AmpImportException;
import org.digijava.module.dataExchange.dbentity.AmpDEImportLog;
import org.digijava.module.dataExchange.form.ExportForm;
import org.digijava.module.dataExchange.form.ImportForm;
import org.digijava.module.dataExchange.form.ExportForm.LogStatus;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.utils.ImportBuilder;
import org.springframework.util.FileCopyUtils;

/**
 * @author dan
 *
 */
public class ImportAction extends MultiAction {

	private static Logger logger = Logger.getLogger(ImportAction.class);
	private Long siteId = new Long(0);
	private String locale = "";
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String str = (String) session.getAttribute("ampAdmin");

		if(!RequestUtils.isAdmin(response, session, request)){
			return null;
		}
		siteId = RequestUtils.getSite(request).getId();
		locale= RequestUtils.getNavigationLanguage(request).getCode();
		if(request.getParameter("method")!=null) 
			return downloadLog(mapping, form, request, response);
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
		

		String[] options = {TranslatorWorker.translateText("insert",locale, siteId),TranslatorWorker.translateText("update",locale, siteId)};
		if(iform.getOptions() == null || iform.getOptions().length < 1) iform.setOptions(options);
		
		if(request.getParameter("loadFile")!=null) {
			session.setAttribute("DEfileUploaded", "true");
			return modeLoadFileForLog(mapping, iform, request, response);
		}
		//if(request.getParameter("import")!=null) return modeUploadedFile(mapping, iform, request, response);
		if(request.getParameter("saveImport")!=null) 
			return modeSaveImport(mapping, iform, request, response);

		iform.setActivityStructure(ExportHelper.getActivityStruct("activity","activityStructure","activity",ActivityType.class,true));
	
		ActionErrors errors = (ActionErrors) session.getAttribute("DEimportErrors");
		if(errors != null){
			saveErrors(request, errors);
			session.setAttribute("DEimportErrors", null);
		}
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

	private ActionForward modeLoadFileForLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, Exception {
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
      //  InputStream inputStream1= new ByteArrayInputStream(fileData);
        
        OutputStream outputStream = new ByteArrayOutputStream(); 
        FileCopyUtils.copy(inputStream, outputStream);
        
        TeamMember tm = null;
        if (session.getAttribute("currentMember") != null)
        	tm = (TeamMember) session.getAttribute("currentMember");
        
        ImportBuilder importBuilder = new ImportBuilder();

        session.setAttribute("DEimportErrors", null);
        String log = "";

        try{
        	importBuilder.checkXMLIntegrityNoChunks(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd", new ByteArrayInputStream(outputStream.toString().getBytes()), locale, siteId);
        }catch(AmpImportException aie){
        	
        	log = aie.getMessage();
        	ActionErrors errors = new ActionErrors();
        	errors.add("title", new ActionError("error.aim.dataExchange.corruptedFile", log));
        	request.setAttribute("loadFile",null);
        	
        	if (errors.size() > 0){
        		session.setAttribute("DEimportErrors", errors);
        	}
        	
        	//return mapping.findForward("forwardError");
        }
        
        //importBuilder.splitInChunks(inputStream1);
        importBuilder.splitInChunks(new ByteArrayInputStream(outputStream.toString().getBytes()));
        importBuilder.generateLogForActivities(this.getServlet().getServletContext().getRealPath("/")+"/doc/IDML2.0.xsd",locale, siteId);
        importBuilder.createActivityTree();

        deImportForm.setActivityTree(importBuilder.getRoot());
        session.setAttribute("DELogGenerated", importBuilder.printLogs());
        session.setAttribute("importBuilder", importBuilder);
        session.setAttribute("DEfileUploaded", "true");
		
		return mapping.findForward("afterUploadFile");
	}
	
	public ActionForward downloadLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ImportForm eForm = (ImportForm)form;

		response.setContentType("text/html");
		response.setHeader("content-disposition","attachment; filename=importLog.html"); // file name will generate by date
		OutputStreamWriter outputStream =  null;
		HttpSession session = request.getSession();
		try {
            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
			outputStream.write(session.getAttribute("DELogGenerated").toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}


		return null;

	}

}
