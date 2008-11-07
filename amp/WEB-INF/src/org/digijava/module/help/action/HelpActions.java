package org.digijava.module.help.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;


import javax.jcr.Session;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.help.jaxb.HelpType;
import org.digijava.module.help.jaxb.Helps;
import org.digijava.module.help.jaxb.HelpsType;
import org.digijava.module.help.jaxb.ObjectFactory;
import org.dgfoundation.amp.te.ampte.Translations;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.TranslatorManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.VisibilityManagerExportHelper;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.helper.HelpSearchData;
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

import sun.misc.Regexp;






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

public ActionForward getbody(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response)throws Exception{
	String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
	HelpForm helpForm = (HelpForm) form;
	OutputStreamWriter os = null;	
    PrintWriter out = null;
    String loadStatus = request.getParameter("body");
	
	try {
		if(loadStatus != null){
			os = new OutputStreamWriter(response.getOutputStream());
			out = new PrintWriter(os, true);
			String id = loadStatus.toLowerCase();
			HelpTopic key = HelpUtil.getHelpTopic(new Long(id));
			String bodyKey =  key.getBodyEditKey();
			List editor = HelpUtil.getEditor(bodyKey, lang);
			
			if(!editor.isEmpty()){
				Iterator iter = editor.iterator();
				while (iter.hasNext()) {
					Editor help = (Editor) iter.next();
					out.println(help.getBody());
				}
			}
		}
		out.flush();
		out.close();
		
	} catch (Exception e) {
		 e.printStackTrace();
   }
	return null;
}
	
	
	public ActionForward vewSearchKey(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		  	OutputStreamWriter os = null;	
		    PrintWriter out = null;
		    String loadStatus =request.getParameter("loadKey");
		    Editor item = new Editor();
			 
			 try {
				 os = new OutputStreamWriter(response.getOutputStream());
				 out = new PrintWriter(os, true);	 
				 if(loadStatus != null){
						 List<Editor> data =  HelpUtil.getAllHelpKey();
						 
						 for(Iterator iter = data.iterator(); iter.hasNext(); ) {
							  item = (Editor) iter.next();
				 			
				           
							  int editkey = item.getEditorKey().indexOf("body:");
					          String title = item.getEditorKey().substring(editkey+5);
				             if(title.length()>=loadStatus.length()){
				             if(loadStatus.toLowerCase().equals(title.toLowerCase().substring(0,loadStatus.length()))){
				            	
				            	
				                out.println("<div id="+title+" onclick=\"select("+title+")\" onmouseover=\"this.className='silverThing'\" onmouseout=\"this.className='whiteThing'\">"+title+"</div>");
				             }
				           }
						}
					 }
			      if(out == null){
			    	 
			    	  out.println("<div onmouseover=\"this.className='silverThing'\" onmouseout=\"this.className='whiteThing'\">Not found</div>");
			    	  out.flush();
					  out.close();	
			      }else{
				 out.flush();
				 out.close();
			    }
	} catch (Exception e) {
	     e.printStackTrace();
	 }
	 return null;
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
		
	 	
		 if(keywords != null){
		 Collection<LabelValueBean> Searched = new ArrayList<LabelValueBean>();
		 String instanceName=RequestUtils.getModuleInstance(request).getInstanceName();
		 Hits hits =  LuceneUtil.helpSearch("title", helpForm.getKeywords());
		 String artikleTitle;
		 
		 HelpForm help = (HelpForm) form;
		 
		  int hitCount = hits.length();   
    	   
    	  if(hitCount == 0){
    		  
    		  help.setTitle(null);
			  help.setBody(null);
    	  
    	  }else{
    	  
    		  for(int i=0; (i < hitCount && i < 10); i++){

    		  Document doc = hits.doc(i);

			   String title = doc.get("title");
			   String titletrnKey = doc.get("titletrnKey");
			   String art;
			   Field field = doc.getField("article");
			   Object artidcle = LuceneUtil.highlighter(field,title);
			
                      if (!artidcle.equals("")) {

                          art = artidcle.toString();
                      } else {
                          artikleTitle = doc.get("article");
                          if (artikleTitle == null) {
                              artikleTitle = "";
                          }
                          if (artikleTitle.length() > 100) {
                              art = artikleTitle.substring(0, 100);
                          } else {
                              art = artikleTitle;
                          }
                      }

			   String titlelink = 
				   "<a href=\"../../help/"+instanceName+"/helpActions.do?actionType=viewSelectedHelpTopic&topicKey="+title+"\">"+HelpUtil.getTrn(titletrnKey,title,request)+"</a>";
			   Searched.add(new LabelValueBean(titlelink,art+"..."));
			   help.setSearched(Searched);
			   help.setTopicKey(title);
			   help.setFlag(false);
    	  }
    	}
   	}
		// helpForm.setHelpTopics(HelpUtil.getHelpTopics(siteId, moduleInstance,locale,
		// keywords));
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
		if(helpTopic != null){
			helpForm.setBodyEditKey(helpTopic.getBodyEditKey());
			helpForm.setTitleTrnKey(helpTopic.getTitleTrnKey());
			
			helpForm.setHelpErrors(null);
			helpForm.setSearched(null);
			helpForm.setFlag(true);
			LuceneUtil.addUpdatehelp(true);
	  
		if(helpTopic.getParent()!=null){
			helpForm.setParentId(helpTopic.getParent().getHelpTopicId());
		}
		}else{
		helpForm.setBodyEditKey("");
		helpForm.setTitleTrnKey("");
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
		String page  = request.getParameter("page");
		helpForm.setHelpErrors(null);
		if (helpForm.getTopicKey() != null) {
			HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getTopicKey(),
					siteId, moduleInstance);
			if(helpTopic!=null){
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
		}
		if(!page.equals("admin")){
			return mapping.findForward("helpHome");
		}else{
			if(helpForm.getAdminTopicTree()!=null){
			helpForm.getAdminTopicTree().clear();
			}
			helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId, "admin"));
			helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, "default"));
			return mapping.findForward("admin");
		}
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
		String page  = request.getParameter("page");
		helpForm.setPage(page);
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
		
		if(page == null){
				return mapping.findForward("help");
			
		}else{
				return mapping.findForward("admin");	
			}
		
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
		
		 int editkey = helpForm.getBodyEditKey().indexOf("body:");
         String topickey = helpForm.getBodyEditKey().substring(editkey+5);
		helpForm.setTopicKey(topickey);
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
		//form.setTopicKey(null);
		form.setKeywordsTrnKey(null);
		//form.setTitleTrnKey(null);
		form.setParentId(null);
		form.setFirstLevelTopics(null);
	}
	
	public ActionForward viewAdmin(ActionMapping mapping,
			ActionForm form, 
			HttpServletRequest request,
	HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId=RequestUtils.getSite(request).getSiteId();
		String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
		helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
		helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));

    
	
	  return mapping.findForward("admin");
	}
	
	
	public ActionForward export(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxb");
		Marshaller m = jc.createMarshaller();
		response.setContentType("text/xml");
		response.setHeader("content-disposition", "attachment; filename=exportHelp.xml");
		ObjectFactory objFactory = new ObjectFactory();
		Helps help_out =  objFactory.createHelps();
		Vector rsAux=new Vector();
	
		
		rsAux= HelpUtil.getAllHelpdataForExport(lang);
		help_out.getHelp().addAll(rsAux);
		m.marshal(help_out,response.getOutputStream());
	    return null;

	}
	
	public ActionForward importing(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		    HashMap<Long,HelpTopic> storeMap=new HashMap<Long, HelpTopic>();
			HelpForm helpForm = (HelpForm) form;
			String siteId=RequestUtils.getSite(request).getSiteId();
			String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
			
		FormFile myFile = helpForm.getFileUploaded();
        byte[] fileData    = myFile.getFileData();
        InputStream inputStream= new ByteArrayInputStream(fileData);
        
        
        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxb");
        Unmarshaller m = jc.createUnmarshaller();
        Helps help_in;
    
        
			help_in = (Helps) m.unmarshal(inputStream);
			if (help_in.getHelp() != null) {
				Iterator it = help_in.getHelp().iterator();
				while(it.hasNext())
				{
					HelpType element  = (HelpType) it.next();
					HelpUtil.updateNewEditHelpData(element,storeMap);
				}
			}
			helpForm.getTopicTree().clear();
			helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
			helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
			return mapping.findForward("admin");
	}
	
}