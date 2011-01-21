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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.form.CreateSourceForm;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.util.CreateSourceUtil;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.dataExchange.utils.ImportBuilder;
import org.springframework.util.FileCopyUtils;

/**
 * @author dan
 *
 */
public class CreateSourceAction extends MultiAction {

	private static Logger logger = Logger.getLogger(CreateSourceAction.class);
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		CreateSourceForm iform = (CreateSourceForm) form;
		
		if(request.getParameter("saveImport")!=null) 
			return modeSave(mapping, iform, request, response);
		else 
			this.modeReset(mapping, iform, request, response);
		
		String[] langArray = {"en","fr","es"};
		if(iform.getLanguages() == null || iform.getLanguages().length < 1) iform.setLanguages(langArray);
		
//		String siteId = RequestUtils.getSite(request).getId().toString();
//		String locale= RequestUtils.getNavigationLanguage(request).getCode();
//		String[] options = {TranslatorWorker.translateText("update",locale, siteId),TranslatorWorker.translateText("overwrite",locale, siteId)};
//		if(iform.getOptions() == null || iform.getOptions().length < 1) iform.setOptions(options);

		List<KeyValue> importStrategyValues	= new ArrayList<KeyValue>();
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_NEW_PROJ, "Import only new projects") );
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_UPD_PROJ, "Update existing projects") );
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ, "Both") );
		iform.setImportStrategyValues(importStrategyValues);
		
		List<KeyValue> sourceValues	= new ArrayList<KeyValue>();
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_FILE, "From file") );
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_URL, "From url") );
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_WEB_SERVICE, "From Web Service") );
		iform.setSourceValues(sourceValues);
		
		List<KeyValue> approvalStatusValues	= new ArrayList<KeyValue>();
		approvalStatusValues.add(new KeyValue(Constants.STARTED_STATUS, "New") );
		approvalStatusValues.add(new KeyValue(Constants.STARTED_APPROVED_STATUS, "New and validated") );
		approvalStatusValues.add(new KeyValue(Constants.EDITED_STATUS, "Edited but not validated") );
		approvalStatusValues.add(new KeyValue(Constants.APPROVED_STATUS, "Edited and validated") );
		iform.setApprovalStatusValues(approvalStatusValues);
		
		Collection<AmpTeam> teams	= TeamUtil.getAllTeams();
		iform.setTeamValues(teams);
		
		iform.setActivityTree(ExportHelper.getActivityStruct("activity","activityTree","activity",ActivityType.class,true) );
		
		ActionMessages errors = (ActionMessages) session.getAttribute("DEimportErrors");
		if(errors != null){
			saveErrors(request, errors);
			session.setAttribute("DEimportErrors", null);
		}
		return mapping.findForward("forward");
	}

	private ActionForward modeSave(ActionMapping mapping, CreateSourceForm form, HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		
		DESourceSetting srcSetting	= new DESourceSetting();
		srcSetting.setName(form.getName() );
		srcSetting.setSource( form.getSource() );
		srcSetting.setFields(  CreateSourceUtil.getFieldNames(form.getActivityTree(), null) );
		srcSetting.setImportStrategy( form.getImportStrategy() );
		srcSetting.setUniqueIdentifier( form.getUniqueIdentifier() );
		srcSetting.setApprovalStatus( form.getApprovalStatus() );
		
		if ( form.getTeamValues() != null ) {
			for (AmpTeam selTeam: form.getTeamValues() ) {
				if ( selTeam.getAmpTeamId().equals(form.getTeamId() )) {
					srcSetting.setImportWorkspace( selTeam );
					break;
				}
			}
		}
		if ( form.getSelectedLanguages() != null && form.getSelectedLanguages().length > 0 ) {
			srcSetting.setLanguageId("");
			for (String lang: form.getSelectedLanguages() ) {
				srcSetting.setLanguageId( srcSetting.getLanguageId() + lang + "|" );
			}
			srcSetting.setLanguageId( srcSetting.getLanguageId().substring(0, srcSetting.getLanguageId().length()-1 ) );
		}
		
		try {
			//srcSetting.setLogs(new ArrayList<DELogPerExecution>() );
			//srcSetting.getLogs().add(CreateSourceUtil.createTestLogObj() );
			//srcSetting.getLogs().add(CreateSourceUtil.createTestLogObj() );
			new SourceSettingDAO().saveObject(srcSetting);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mapping.findForward("showSources");
		
		
	}
	private void modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		CreateSourceForm myForm	= (CreateSourceForm) form;
		myForm.setName( null );
		myForm.setApprovalStatus( null );
		myForm.setImportStrategy( null );
		myForm.setUniqueIdentifier(null);
		myForm.setImportStrategy(null);
		myForm.setTeamId(null);
		myForm.setSelectedLanguages(null);
		myForm.setSelectedOptions(null);
		
		
	}

//	private ActionForward modeLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, Exception {
//		// TODO Auto-generated method stub
//		
//		HttpSession session = request.getSession();
//		String siteId = RequestUtils.getSite(request).getId().toString();
//		String locale= RequestUtils.getNavigationLanguage(request).getCode();
//
//		CreateSourceForm deCreateSourceForm= (CreateSourceForm) form;
//		
//		FormFile myFile = deCreateSourceForm.getUploadedFile();
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
//        //deCreateSourceForm.setActivityTree(importBuilder.getRoot());
//        
//        session.setAttribute("DELogGenerated", importBuilder.printLogs());
//        session.setAttribute("importBuilder", importBuilder);
//        session.setAttribute("DEfileUploaded", "true");
//        
//		return mapping.findForward("afterUploadFile");
//	}

	
}
