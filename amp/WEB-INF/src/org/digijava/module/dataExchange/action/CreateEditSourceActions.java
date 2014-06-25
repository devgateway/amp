package org.digijava.module.dataExchange.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javassist.bytecode.Descriptor.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.form.CreateSourceForm;
import org.digijava.module.dataExchange.form.CreateSourceForm.ComponentFM;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.util.CreateSourceUtil;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.util.DbUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class CreateEditSourceActions extends DispatchAction {

	public ActionForward gotoCreatePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		CreateSourceForm myform = (CreateSourceForm) form;
		modeReset(myform);
		fillForm(myform,request);
		String[] lang = {"1"};
		myform.setSelectedLanguages(lang);
		myform.setImportStrategy(DESourceSetting.IMPORT_STRATEGY_NEW_PROJ);
		myform.setApprovalStatus(Constants.STARTED_STATUS);
		myform.setSource(DESourceSetting.SOURCE_FILE);
		myform.setErrorName(false);
		return mapping.findForward("forward");
	}
	
	public ActionForward gotoEditPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");
		
		CreateSourceForm myform = (CreateSourceForm) form;
		fillForm(myform, request);
		
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( myform.getSourceId() );
		List<String> importComponents =ss.getFields();
		for(int i=0;i<importComponents.size();i++){
			String comp = importComponents.get(i);
			String[] info = comp.split("\\|\\|\\|");
			if(info.length==3){
				for(int j=0;j<myform.getListComponents().size();j++){
					if(myform.getListComponents().get(j).getIatiName().compareTo(info[0])==0){
						if(myform.getListComponents().get(j).isState()){
							myform.getListComponents().get(j).setImportComp(Boolean.parseBoolean(info[1]));
							myform.getListComponents().get(j).setAutoOverwrite(Boolean.parseBoolean(info[2]));
						}
						break;
					}
				}
			}
		}

        if (ss.getDefaultLocation() != null) {
            myform.setDefaultLocationID(ss.getDefaultLocation().getId());
        }
        myform.setRegionalFunding(ss.isRegionalFundings());

		myform.setName(ss.getName());
		myform.setApprovalStatus(ss.getApprovalStatus());
		myform.setImportStrategy(ss.getImportStrategy());
		myform.setTeamId(ss.getImportWorkspace().getAmpTeamId());
		if(ss.getLanguageId() != null){
			myform.setSelectedLanguages(ss.getLanguageId().split("\\"+ss.getUniqueIdentifierSeparator()));
		}		
		myform.setSource(ss.getSource());
		myform.setUniqueIdentifier(ss.getUniqueIdentifier());
		myform.setSdmDocument(ss.getAttachedFile());
		myform.setErrorName(false);
		return mapping.findForward("forward");
	}
	
	public ActionForward saveSource(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session=request.getSession();
		session.setAttribute("errorLogForDE","");
		session.setAttribute("messageLogForDe","");
		
		session.setAttribute("DEfileUploaded", "false");		
		
		CreateSourceForm myForm = (CreateSourceForm) form;
		
		myForm.getListComponents();
		DESourceSetting srcSetting	= null;
		if(myForm.isDisplaySource()){
			//System.out.println(myForm.getConfigurationId());
			//System.out.println(myForm.getConfigurations().size());
		}
		if(!myForm.isDisplaySource()){
			for(int i=0; i<myForm.getListComponents().size();i++){
				myForm.getListComponents().get(i).setImportComp(Boolean.parseBoolean(myForm.getModuleImport()[i]));
				if(Boolean.parseBoolean(myForm.getModuleImport()[i]))
					myForm.getListComponents().get(i).setAutoOverwrite(Boolean.parseBoolean(myForm.getModuleAOW()[i]));
				else
					myForm.getListComponents().get(i).setAutoOverwrite(false);
			}
			
			if(myForm.getSourceId() !=null && ! myForm.getSourceId().equals(new Long(-1))){
				srcSetting	= new SourceSettingDAO().getSourceSettingById(myForm.getSourceId());
			}else{
				srcSetting	= new DESourceSetting();
			}
			DESourceSetting nameTest = new SourceSettingDAO().getSourceSettingByName(myForm.getName());
			if (nameTest!=null){
				if(myForm.getSourceId()==-1){
					myForm.setErrorName(true);
					return mapping.findForward("forward");
				}
				if(myForm.getSourceId()!=-1 && srcSetting.getName().compareToIgnoreCase(nameTest.getName())!=0){
					myForm.setErrorName(true);
					return mapping.findForward("forward");
				}
				
			}
			
			srcSetting.setName(myForm.getName() );
			srcSetting.setSource( myForm.getSource() );
			//srcSetting.setFields(  CreateSourceUtil.getFieldNames(myForm.getActivityTree(), null) );
			List<String> importModules = new ArrayList<String>();
			for(int i=0; i<myForm.getListComponents().size();i++){
				importModules.add(myForm.getListComponents().get(i).getIatiName()+"|||"+myForm.getListComponents().get(i).isImportComp()+"|||"+myForm.getListComponents().get(i).isAutoOverwrite());
			}
			srcSetting.setFields( importModules );
			srcSetting.setImportStrategy( myForm.getImportStrategy() );
			srcSetting.setUniqueIdentifier( myForm.getUniqueIdentifier() );
			srcSetting.setApprovalStatus( myForm.getApprovalStatus() );

            if (myForm.getDefaultLocationID() != null) {
                Collection<AmpCategoryValueLocations> locs = DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();
                for (AmpCategoryValueLocations loc : locs) {
                    if (loc.getId().equals(myForm.getDefaultLocationID())) {
                        srcSetting.setDefaultLocation(loc);
                        break;
                    }
                }
            }

            srcSetting.setRegionalFundings(myForm.isRegionalFunding());
			
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
		}else{
			DESourceSetting configuration = null;
			for(int i=0;i<myForm.getConfigurations().size();i++){
				if(myForm.getConfigurations().get(i).getId().compareTo(myForm.getConfigurationId())==0){
					configuration = myForm.getConfigurations().get(i);
					break;
				}
			}
			srcSetting	= new DESourceSetting();
			
			srcSetting.setName(myForm.getName());
			srcSetting.setSource(configuration.getSource());
			srcSetting.setImportStrategy( configuration.getImportStrategy() );
			srcSetting.setUniqueIdentifier( configuration.getUniqueIdentifier() );
			srcSetting.setApprovalStatus( configuration.getApprovalStatus() );
			srcSetting.setImportWorkspace( configuration.getImportWorkspace() );
			srcSetting.setLanguageId(configuration.getLanguageId());
			
		}
		//attach doc
		Sdm oldDoc = null;
		Sdm oldPrevDoc = null;
		if(myForm.isDisplaySource()){
			if(myForm.getSdmDocument() == null){
				oldDoc = srcSetting.getAttachedFile();
			}
			
			
			if(myForm.getSdmDocument() == null){
				oldPrevDoc = srcSetting.getPreviousAttachedFile();
			}
			
			
			if(myForm.getUploadedFile() != null && myForm.getUploadedFile().getFileSize() >0){
				attachFile(myForm);
			}
		
		}
		try {
			//save attached files
			if(myForm.isDisplaySource()){
	        	Sdm document=myForm.getSdmDocument();
	        	Sdm doc=null;
	        	if(document!=null){
	        		document.setName(srcSetting.getName());
	        		doc=DbUtil.saveOrUpdateDocument(document);        		
	        	}
	        	srcSetting.setAttachedFile(doc);
	        	srcSetting.setPreviousAttachedFile(oldDoc);
			}
			new SourceSettingDAO().saveObject(srcSetting);
			
			if(myForm.isDisplaySource()){
				if(oldPrevDoc!=null){
					DbUtil.deleteDocument(oldPrevDoc);
				}
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
    		for (SdmItem attachment : attachmentsHolder.getItems()) {
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
	
	private void fillForm (CreateSourceForm myform, HttpServletRequest request) throws Exception{
		String[] langArray = {"1","2","3"};
		if(myform.getLanguages() == null || myform.getLanguages().length < 1) myform.setLanguages(langArray);

        Collection<AmpCategoryValueLocations> locs = DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();
        myform.setLocationList(new ArrayList(locs));



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
		approvalStatusValues.add(new KeyValue(Constants.REJECTED_STATUS, "Edited and rejected"));
		myform.setApprovalStatusValues(approvalStatusValues);
		if(myform.isDisplaySource()){
			//get sources
			List<DESourceSetting> sources		= new SessionSourceSettingDAO().getPagedAmpSourceSettingsObjects(0, "name");
			List<DESourceSetting> viewSources = new ArrayList<DESourceSetting>();
				for(int i=0;i<sources.size();i++){
					if(sources.get(i).getAttachedFile()==null)
						viewSources.add(sources.get(i));
				}
				myform.setConfigurations(viewSources);
		}
		
		Collection<AmpTeam> teams	= TeamUtil.getAllTeams();
		myform.setTeamValues(teams);
		Properties prop = new Properties();
		List<ComponentFM> componentsList = new ArrayList<CreateSourceForm.ComponentFM>();
		try {
			InputStream inStream = CreateEditSourceActions.class.getClassLoader().
		            getResourceAsStream("org/digijava/module/dataExchange/action/iatiModule.properties");
			prop.load(inStream);
			Enumeration keys = prop.keys();
			while(keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				String value = (String)prop.get(key);
				
				ComponentFM compFM = myform.new ComponentFM();
				compFM.setName(key);
				compFM.setIatiName(value);
				compFM.setId(AmpDbUtil.getComponentFMIdfromName(compFM.getName()));
				//compFM.setState(AmpDbUtil.getComponentState(compFM.getName()));
                compFM.setState(true);
				compFM.setImportComp(compFM.isState());
				compFM.setAutoOverwrite(false);
				componentsList.add(compFM);
			}
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		
		myform.setListComponents(componentsList);
		
		//myform.setActivityTree(ExportHelper.getActivityStruct("activity","activityTree","activity",ActivityType.class,true) );
		myform.setActivityTree(ExportHelper.getIATIActivityStruct("Activity","activityTree","Activity",request) );
		//System.out.println(" ");
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
	
	private DESourceSetting getSourceSetting (Long id){
		Session session=null;
		String queryString =null;
		Query query=null;
		DESourceSetting returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + DESourceSetting.class.getName()+ " a where a.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", id);
			returnValue=(DESourceSetting)query.uniqueResult();
		}catch(Exception ex) {
			//logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
}
