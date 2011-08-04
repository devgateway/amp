package org.digijava.module.dataExchange.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.form.CreateSourceForm;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.util.CreateSourceUtil;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.util.DbUtil;

public class CreateEditSourceActions extends DispatchAction {

	public ActionForward gotoCreatePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		CreateSourceForm myform = (CreateSourceForm) form;
		modeReset(myform);
		fillForm(myform);
		return mapping.findForward("forward");
	}
	
	public ActionForward gotoEditPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		CreateSourceForm myform = (CreateSourceForm) form;
		fillForm(myform);
		
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( myform.getSourceId() );
		myform.setName(ss.getName());
		myform.setApprovalStatus(ss.getApprovalStatus());
		myform.setImportStrategy(ss.getImportStrategy());
		myform.setSelectedLanguages(ss.getLanguageId().split("\\"+ss.getUniqueIdentifierSeparator()));
		myform.setSource(ss.getSource());
		myform.setUniqueIdentifier(ss.getUniqueIdentifier());
		myform.setSdmDocument(ss.getAttachedFile());
		return mapping.findForward("forward");
	}
	
	public ActionForward saveSource(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");		
		
		CreateSourceForm myForm = (CreateSourceForm) form;
		
		DESourceSetting srcSetting	= null;
		if(myForm.getSourceId() !=null && ! myForm.getSourceId().equals(new Long(-1))){
			srcSetting	= new SourceSettingDAO().getSourceSettingById(myForm.getSourceId());
		}else{
			srcSetting	= new DESourceSetting();
		}
		
		srcSetting.setName(myForm.getName() );
		srcSetting.setSource( myForm.getSource() );
		srcSetting.setFields(  CreateSourceUtil.getFieldNames(myForm.getActivityTree(), null) );
		srcSetting.setImportStrategy( myForm.getImportStrategy() );
		srcSetting.setUniqueIdentifier( myForm.getUniqueIdentifier() );
		srcSetting.setApprovalStatus( myForm.getApprovalStatus() );
		
		if ( myForm.getTeamValues() != null ) {
			for (AmpTeam selTeam: myForm.getTeamValues() ) {
				if ( selTeam.getAmpTeamId().equals(myForm.getTeamId() )) {
					srcSetting.setImportWorkspace( selTeam );
					break;
				}
			}
		}
		if ( myForm.getSelectedLanguages() != null && myForm.getSelectedLanguages().length > 0 ) {
			srcSetting.setLanguageId("");
			for (String lang: myForm.getSelectedLanguages() ) {
				srcSetting.setLanguageId( srcSetting.getLanguageId() + lang + "|" );
			}
			srcSetting.setLanguageId( srcSetting.getLanguageId().substring(0, srcSetting.getLanguageId().length()-1 ) );
		}
		
		//attach doc
		Sdm oldDOc = null;
		if(myForm.getSdmDocument() == null){
			oldDOc = srcSetting.getAttachedFile();
		}
		
		if(myForm.getUploadedFile() != null && myForm.getUploadedFile().getFileSize() >0){
			attachFile(myForm);
		}
		
		try {
			//save attached files
        	Sdm document=myForm.getSdmDocument();
        	Sdm doc=null;
        	if(document!=null){
        		document.setName(srcSetting.getName());
        		doc=DbUtil.saveOrUpdateDocument(document);        		
        	}
        	srcSetting.setAttachedFile(doc);
			
			new SourceSettingDAO().saveObject(srcSetting);
			
			if(oldDOc!=null){
				DbUtil.deleteDocument(oldDOc);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modeReset(myForm);
		return mapping.findForward("showSources");
	}
	
	public ActionForward removeAttachment(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response){
		CreateSourceForm myForm=(CreateSourceForm)form;
    	    	
    	Sdm attachmentsHolder=myForm.getSdmDocument(); //this won't be null, because if we are removing any attachment, this automatically means that we have Sdm
    	Long attachmentOrder=null; //order number or sdmItem in Sdm
    	if(request.getParameter("attachmentOrder")!=null){
    		attachmentOrder=new Long(request.getParameter("attachmentOrder"));
    	}
    	//messageHolder should contain at least one attachment(the one we want to remove)
    	if(attachmentOrder!=null){
    		for (Object item : attachmentsHolder.getItems()) {
    			SdmItem attachment=(SdmItem)item;
    			if(attachment.getParagraphOrder().equals(attachmentOrder)){    				
    				attachmentsHolder.getItems().remove(attachment);
    				break;
    			}
    		}
    	}
    	if(attachmentsHolder.getItems().size()==0){ //if all attachments were removed,we don't need to create any Sdm
    		myForm.setSdmDocument(null);
    	}else{
    		myForm.setSdmDocument(attachmentsHolder);
    	}    	
    	return mapping.findForward("forward");
	}
	
	private void fillForm (CreateSourceForm myform){
		String[] langArray = {"en","fr","es"};
		if(myform.getLanguages() == null || myform.getLanguages().length < 1) myform.setLanguages(langArray);
		
		List<KeyValue> importStrategyValues	= new ArrayList<KeyValue>();
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_NEW_PROJ, "Import only new projects") );
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_UPD_PROJ, "Update existing projects") );
		importStrategyValues.add(new KeyValue(DESourceSetting.IMPORT_STRATEGY_NEW_PROJ_AND_UPD_PROJ, "Both") );
		myform.setImportStrategyValues(importStrategyValues);
		
		List<KeyValue> sourceValues	= new ArrayList<KeyValue>();
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_FILE, "From file") );
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_URL, "From url") );
		sourceValues.add(new KeyValue(DESourceSetting.SOURCE_WEB_SERVICE, "From Web Service") );
		myform.setSourceValues(sourceValues);
		
		List<KeyValue> approvalStatusValues	= new ArrayList<KeyValue>();
		approvalStatusValues.add(new KeyValue(Constants.STARTED_STATUS, "New") );
		approvalStatusValues.add(new KeyValue(Constants.STARTED_APPROVED_STATUS, "New and validated") );
		approvalStatusValues.add(new KeyValue(Constants.EDITED_STATUS, "Edited but not validated") );
		approvalStatusValues.add(new KeyValue(Constants.APPROVED_STATUS, "Edited and validated") );
		myform.setApprovalStatusValues(approvalStatusValues);
		
		Collection<AmpTeam> teams	= TeamUtil.getAllTeams();
		myform.setTeamValues(teams);
		
		myform.setActivityTree(ExportHelper.getActivityStruct("activity","activityTree","activity",ActivityType.class,true) );
	}
	
	private void attachFile(CreateSourceForm form) throws FileNotFoundException, IOException {
		Sdm attachmentHolder=null;
    	if(form.getUploadedFile() != null){
    		if(form.getSdmDocument()!=null){
    			attachmentHolder= form.getSdmDocument();
    		}else{
    			attachmentHolder=new Sdm();
    		}    		
    		
    		SdmItem sdmItem = new SdmItem();
        	sdmItem.setContentType(form.getUploadedFile().getContentType());
            sdmItem.setRealType(SdmItem.TYPE_FILE);
            sdmItem.setContent(form.getUploadedFile().getFileData());
            sdmItem.setContentText(form.getUploadedFile().getFileName());
            sdmItem.setContentTitle(form.getUploadedFile().getFileName());
            
            HashSet items = new HashSet();
            sdmItem.setParagraphOrder(new Long(0));
            items.add(sdmItem);
            attachmentHolder.setItems(items);  
            
            form.setSdmDocument(attachmentHolder);
    	}		
	}
	
	private void modeReset(CreateSourceForm myForm) {
		myForm.setName( null );
		myForm.setApprovalStatus( null );
		myForm.setImportStrategy( null );
		myForm.setUniqueIdentifier(null);
		myForm.setImportStrategy(null);
		myForm.setTeamId(null);
		myForm.setSelectedLanguages(null);
		myForm.setSelectedOptions(null);
		myForm.setSourceId(new Long(-1));
		myForm.setSdmDocument(null);
		
	}
}
