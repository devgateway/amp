package org.digijava.module.help.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.util.HelpUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.aim.util.LuceneUtil;






public class HelpActions extends DispatchAction {

//	@Override
//	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		// TODO Auto-generated method stub
//		return mapping.findForward("help");
//	}

	
	private Collection<LabelValueBean> getSearchedData() throws DgException{
		ArrayList<LabelValueBean> result = new ArrayList<LabelValueBean>(2);
		//TODO temporary solution, improve this.
		result.add(new LabelValueBean("Standard","1"));
		result.add(new LabelValueBean("Dropdown Filter - Donors","2"));
		return result;
	}

	public ActionForward searchHelpTopic(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		 HelpForm helpForm = (HelpForm) form;
		 String siteId = RequestUtils.getSite(request).getSiteId();
		 String moduleInstance = RequestUtils.getRealModuleInstance(request)
		 .getInstanceName();
		 String locale=RequestUtils.getNavigationLanguage(request).getCode();
		 String keywords = helpForm.getKeywords();
		 
		 if(keywords != ""){
		 Collection<LabelValueBean> Searched = new ArrayList<LabelValueBean>();
		
		 Hits hits =  LuceneUtil.helpSearch("title", helpForm.getKeywords());
		 
		 HelpForm help = (HelpForm) form;
		 
		  int hitCount = hits.length();   
    	   
    	  if(hitCount == 0){
    		  
    		  help.setTitle(null);
			  help.setBody(null);
    	  
    	  }else{
    	  
    		  for(int i=0; (i < hitCount && i < 10); i++){

    		  Document doc = hits.doc(i);

			   String title = doc.get("title");
			   String article = doc.get("article");
			   Searched.add(new LabelValueBean(title,article));
			   help.setSearched(Searched);
			   help.setTopicKey(null);
			   
    	  }
    	}
   	}
		 helpForm.setHelpTopics(HelpUtil.getHelpTopics(siteId, moduleInstance,locale,
		 keywords));
		 return mapping.findForward("help");		
	}

	public ActionForward viewSelectedHelpTopic(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getTopicKey(),
				siteId, moduleInstance);
		helpForm.setBodyEditKey(helpTopic.getBodyEditKey());
		helpForm.setTitleTrnKey(helpTopic.getTitleTrnKey());
		helpForm.setHelpErrors(null);
		helpForm.setSearched(null);
		LuceneUtil.addUpdatehelp(true);
		if(helpTopic.getParent()!=null){
			helpForm.setParentId(helpTopic.getParent().getHelpTopicId());
		}		
		return mapping.findForward("help");
	}

	public ActionForward deleteHelpTopic(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();	
		helpForm.setHelpErrors(null);
		if (helpForm.getTopicKey() != null) {
			HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getTopicKey(),
					siteId, moduleInstance);
			if(HelpUtil.hasChildren(siteId, moduleInstance, helpTopic.getHelpTopicId())){
				
				List<String> helpErrors=new ArrayList<String>();
				helpErrors.add("errors:help:delete:cannotDeleteTopic");
				helpForm.setHelpErrors(helpErrors);	
				helpForm.setBlankPage(false);
			
			}else {
				HelpUtil.deleteHelpTopic(helpTopic);
				helpForm.setTopicKey("");	
				helpForm.setBlankPage(false);
			}			
		}

		return mapping.findForward("helpHome");
	}

	public ActionForward createHelpTopic(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		 
		
		  
		int wizardStep = helpForm.getWizardStep();
		helpForm.setEdit(false);
		switch (wizardStep) {
		case 0: {
			createTopicStep0(helpForm, request);
			return mapping.findForward("create");
		}
		case 1: {
			createTopicStep1(helpForm, request);
			return mapping.findForward("create");
		}
		case 2: {
			return mapping.findForward("create");
		}
		case 3: {
			createTopicStep3(helpForm, request);
			break;
		}
		default:
			break;
		}

		return mapping.findForward("help");
	}

	public ActionForward editHelpTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getTopicKey(),
				siteId, moduleInstance);
		if (helpTopic == null) {
			throw new AimException("helpTopic Not Found");
		} else {
			int wizardStep = helpForm.getWizardStep();
			switch (wizardStep) {
			case 0: {
				editTopicStep0(helpForm, request, helpTopic);
				return mapping.findForward("edit");
			}
			case 1: {
				return mapping.findForward("edit");
			}
			case 2: {
				editTopicStep1(helpForm, request, helpTopic);
			}
			default:
				break;
			}
		}

		return mapping.findForward("help");
	}

	/**
	 * Edit wizard: step 0
	 * 
	 * @param form
	 * @param topic
	 */
	private void editTopicStep0(HelpForm form, HttpServletRequest request,
			HelpTopic topic) throws Exception {

		setDefaultValues(form);
		form.setWizardStep(1);
		form.setEdit(true);
		form.setTitleTrnKey(topic.getTitleTrnKey());
		form.setKeywordsTrnKey(topic.getKeywordsTrnKey());
		form.setBodyEditKey(topic.getBodyEditKey());
		form.setHelpTopicId(topic.getHelpTopicId());
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		List<HelpTopic> parentTopics=(List)HelpUtil.getFirstLevelTopics(siteId,
				moduleInstance, null);
		form.setFirstLevelTopics(new ArrayList<HelpTopic>());
		for (HelpTopic helpTopic : parentTopics) {	
			if(!helpTopic.getHelpTopicId().equals(topic.getHelpTopicId())){
				//helpTopic.setTitleTrnKey(TranslatorWorker.translate(helpTopic.getTitleTrnKey(), locale, siteId)); 			
				form.getFirstLevelTopics().add(helpTopic);
			}		
			
		}
		if (topic.getParent() != null) {
			form.setParentId(topic.getParent().getHelpTopicId());
		} else {
			form.setParentId(null);
		}
		form.setTopicKey(topic.getTopicKey());
	}

	/**
	 * Edit wizard: step 1
	 * 
	 * @param form
	 * @param request
	 * @param topic
	 */
	private void editTopicStep1(HelpForm form, HttpServletRequest request,
			HelpTopic topic) throws AimException {
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		if (form.getParentId() != null
				&& !form.getParentId().equals(new Long(0))) {
			topic.setParent(HelpUtil.getHelpTopic(form.getParentId()));
		} else {
			topic.setParent(null);
		}
		HelpUtil.saveOrUpdateHelpTopic(topic);
		form.setEdit(false);
	}

	public ActionForward cancelHelpTopic(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		setDefaultValues(helpForm);
		return mapping.findForward("help");
	}

	/**
	 * Validates form
	 * 
	 * @param form
	 * @param siteId
	 * @param moduleInstance
	 * @return errors
	 * @throws Exception
	 */
	private List<String> validate(HelpForm form, String siteId,
			String moduleInstance) throws AimException {
		List<String> helpErrors=new ArrayList<String>();
		if (!HelpUtil.cheackEditKey(form.getTopicKey(), siteId, moduleInstance)) {
			helpErrors.add("errors.help.createTopic.keyIsUsed");
		}
		return helpErrors;
	}

	/**
	 * Create wizard:step 0
	 * 
	 * @param form
	 */
	private void createTopicStep0(HelpForm form, HttpServletRequest request)
			throws Exception {
		if (request.getParameter("actionBack") == null) {
			setDefaultValues(form);
		}
		form.setEdit(false);
		form.setWizardStep(1);
		form.setHelpErrors(null);
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		List<HelpTopic> parentTopics=(List)HelpUtil.getFirstLevelTopics(siteId,
				moduleInstance, null);
		form.setFirstLevelTopics(new ArrayList<HelpTopic>());
		for (HelpTopic topic : parentTopics) {		
			//topic.setTitleTrnKey(TranslatorWorker.translate(topic.getTitleTrnKey(), locale, siteId)); 
			form.getFirstLevelTopics().add(topic);
			
		}		
	}

	/**
	 * Create wizard: step 1
	 * 
	 * @param form
	 * @param request
	 * @throws AimException
	 */
	private void createTopicStep1(HelpForm form, HttpServletRequest request)
			throws AimException {
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		form.setHelpErrors(null);	 
		if (!validate(form, siteId, moduleInstance).isEmpty()) {
			form.setHelpErrors(validate(form, siteId, moduleInstance));	
		} else {
			form.setWizardStep(2);
			form.setBodyEditKey("help:topic:body:" + form.getTopicKey());
			form.setKeywordsTrnKey("help:topic:keywords:" + form.getTopicKey());
			form.setTitleTrnKey("help:topic:title:" + form.getTopicKey());

		}
	}

	/**
	 * Create wizard: step 2
	 * 
	 * @param form
	 * @param request
	 * @throws AimException
	 */
	private void createTopicStep3(HelpForm form, HttpServletRequest request)
			throws AimException {
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		HelpTopic helpTopic = new HelpTopic();
		helpTopic.setTitleTrnKey(form.getTitleTrnKey());
		helpTopic.setKeywordsTrnKey(form.getKeywordsTrnKey());
		helpTopic.setTopicKey(form.getTopicKey());
		helpTopic.setBodyEditKey(form.getBodyEditKey());
		helpTopic.setModuleInstance(moduleInstance);
		if (form.getParentId() != null
				&& !form.getParentId().equals(new Long(0))) {
			helpTopic.setParent(HelpUtil.getHelpTopic(form.getParentId()));
		} else {
			helpTopic.setParent(null);
		}
		helpTopic.setSiteId(siteId);
		HelpUtil.saveOrUpdateHelpTopic(helpTopic);
		// cleaning form
		form.setParentId(null);
		form.setFirstLevelTopics(null);
		form.setWizardStep(0);
		form.setHelpErrors(null);
	}

	/**
	 * resets form values
	 * 
	 * @param form
	 */
	private void setDefaultValues(HelpForm form) {
		form.setTopicKey(null);
		form.setKeywordsTrnKey(null);
		form.setTitleTrnKey(null);
		form.setParentId(null);
		form.setFirstLevelTopics(null);
	}
}