package org.digijava.module.help.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.jaxbi.AmpHelpRoot;
import org.digijava.module.help.jaxbi.AmpHelpType;
import org.digijava.module.help.jaxbi.ObjectFactory;
import org.digijava.module.help.util.HelpUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.xml.sax.InputSource;


public class HelpActions extends DispatchAction {
    private static Logger logger = Logger.getLogger("HelpActions");
	
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
	
	public ActionForward printPreview(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)throws Exception{
		HelpForm helpForm = (HelpForm) form;
		Long helpTopicId=helpForm.getHelpTopicId();
		if(helpTopicId!=null){
			String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
			HelpTopic helpTopic = HelpUtil.getHelpTopic(helpTopicId);
			helpForm.setTopicKey(helpTopic.getTopicKey());
			helpForm.setBodyEditKey(helpTopic.getBodyEditKey());
			//Editor helpTopicBody= DbUtil.getEditor(helpTopic.getBodyEditKey(), lang); 
		}
		return mapping.findForward("helpPrintPreview");
	}


     public ActionForward getbody(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)throws Exception{
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		HelpForm helpForm = (HelpForm) form;
		OutputStreamWriter os = null;
	    PrintWriter out = null;
         String siteId = RequestUtils.getSite(request).getSiteId();
         String	lange	= RequestUtils.getNavigationLanguage(request).getCode();
         
         
 	    String loadStatus = request.getParameter("body");

        try {
			if(loadStatus != null){
				LuceneUtil.createHelp(request.getSession().getServletContext());
				os = new OutputStreamWriter(response.getOutputStream());
	            out = new PrintWriter(os, true);
				String id = loadStatus.toLowerCase();
				HelpTopic key = HelpUtil.getHelpTopic(new Long(id));
	            String bodyKey =  key.getBodyEditKey();
	            String article = HelpUtil.getTrn(key.getTopicKey(),request);
	            String encodedArticle = HelpUtil.HTMLEntityEncode(article);
	            out.println("<b>"+encodedArticle+"</b>");
	            List editor = HelpUtil.getEditor(bodyKey, lang);
	            helpForm.setTopicKey(bodyKey);

	            
	            if(!editor.isEmpty()){
	               Iterator iter = editor.iterator();
					while (iter.hasNext()) {
						Editor help = (Editor) iter.next();
	                   out.println(help.getBody());
	                   //TODO The ugliest solution I have ever seen! Cannot refactor - codefreeze...
	                   String editorKey = "_editor_key_=" +( (help==null)?" " : help.getEditorKey());
	                   String topicId = "_topic_db_id_=" + ( (key==null)?" ": key.getHelpTopicId() );
	                   out.println(editorKey);
	                   out.println(topicId);
	                    helpForm.setTopicKey(help.getEditorKey());
	                }
				}else{
	                   String editorKey = "_editor_key_=" +( (key.getBodyEditKey()==null)?" " : key.getBodyEditKey());
	                   String topicId = "_topic_db_id_=" + ( (key==null)?" ": key.getHelpTopicId() );
	                   out.println(editorKey);
	                   out.println(topicId);
//	               out.println(helpForm.getTopicKey()); 
	            }
			}
			out.flush();
			out.close();
	
		} catch (Exception e) {
			 e.printStackTrace();
	   }
		return null;
	}
     
   public ActionForward vewSearchKey(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		  	OutputStreamWriter os = null;	
		    PrintWriter out = null;
		    String loadStatus =request.getParameter("loadKey");
		    Editor item = new Editor();
		    String	lange	= RequestUtils.getNavigationLanguage(request).getCode();
		    String siteId = RequestUtils.getSite(request).getSiteId();
		    String moduleInstance = request.getParameter("instance");
		    
			 try {
				 os = new OutputStreamWriter(response.getOutputStream());
				 out = new PrintWriter(os, true);	 
				 if(loadStatus != null){
						 List<Editor> data =  HelpUtil.getAllHelpKey(lange);
						 
						 for(Iterator iter = data.iterator(); iter.hasNext(); ) {
							  item = (Editor) iter.next();
				 			
					          HelpTopic helptopic = HelpUtil.getHelpTopicByBodyEditKey(item.getEditorKey(), siteId, moduleInstance);
					          if(helptopic!=null){
					              String title = helptopic.getTopicKey();
					              String xs = HelpUtil.getTrn(title,request);
					              String encodeTitle = HelpUtil.HTMLEntityEncode(xs);
						           if(encodeTitle.length()>=loadStatus.length()){
					                if(loadStatus.toLowerCase().equals(encodeTitle.toLowerCase().substring(0,loadStatus.length()))){
					            	
					            	String removerSpacedtitle = HelpUtil.removeSpaces(encodeTitle);
					            	
					                out.println("<div id="+removerSpacedtitle+" onclick=\"select("+removerSpacedtitle+")\" onmouseover=\"this.className='silverThing'\" onmouseout=\"this.className='whiteThing'\">"+encodeTitle+"</div>");
					             }
					          }
						}
					    //else{
						//	break;
						//}
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
	
	
	
 	public ActionForward searchHelpTopic(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		 String key =request.getParameter("key");
		 //String keywords = HelpUtil.getTrn(key,request);
		 String treKey = HelpUtil.getTrn("Topic Not Found", request);
		 String locale=RequestUtils.getNavigationLanguage(request).getCode();
		 String siteId = RequestUtils.getSite(request).getSiteId();
        String	lange	= RequestUtils.getNavigationLanguage(request).getCode();
        String moduleInstance = RequestUtils.getRealModuleInstance(request)
			.getInstanceName();
		 Object artidcle = "";
		 List<Editor> wholeBody;
		 OutputStreamWriter os = null;	
		 PrintWriter out = null;
		 String topicKey = null;
		 
		
	try{	
	     os = new OutputStreamWriter(response.getOutputStream());
	     out = new PrintWriter(os, true);	
	     		if(key.length() != 0){
					 Collection<LabelValueBean> Searched = new ArrayList<LabelValueBean>();
					 System.out.println("Key:"+key);
					 Hits hits =  LuceneUtil.helpSearch("title", key, request.getSession().getServletContext());
			
			         String artikleTitle;
					 
					 HelpForm help = (HelpForm) form;	
					 System.out.println("hits.length():"+hits.length());
					  int hitCount = hits.length();   
			    	   
			    	  if(hitCount == 0){
			    		
						  out.println("<div style=\"font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;\"><a class=\"link\"><b>"+key+"</b></a></div>");

			    		  out.println("<div>"+treKey+"...</div>");
			    	  
			    	  }else{
			    	  
			    		  for(int i=0; (i < hitCount && i < 10); i++){
			    		
			
			    		  Document doc = hits.doc(i);
			    		  	if(doc.get("lang").equals(locale)){  
			    			  
						   String title = doc.get("title");
						   String titleKey = doc.get("titletrnKey");
						   String art;
					
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
						   
						   String titlelink = HelpUtil.getTrn(title,request);
						   Searched.add(new LabelValueBean(titlelink,art+"..."));
						 
						 for (LabelValueBean t : Searched){
							 topicKey = t.getLabel();
							   out.println("<div id=\"bodyTitle\" style=\"font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;\"><a class=\"link\" onclick=\"showBody()\"><b>"+t.getLabel()+"</b></a></div>");
							   out.println("<div id=\"bodyShort\"  style=\"display:block;\">"+t.getValue()+"</div>");
						   }
						  HelpTopic bodykey = HelpUtil.getHelpTopic(topicKey, siteId, moduleInstance);
						  if(bodykey !=null){
							   wholeBody = HelpUtil.getEditor(bodykey.getBodyEditKey(), locale);
						  }else{
							  wholeBody = HelpUtil.getEditor(titleKey, locale);
						  }
						  
						  for (Editor wb : wholeBody){
							  out.println("<div id=\"bodyFull\"  style=\"display:none;\">"+wb.getBody()+"</div>");
						  }
						  out.flush();
						  out.close();	
			    	  }
			    	}
			      }
			   	}
    }catch (Exception e) {
        e.printStackTrace();
    }
		return null;
		
	}
	
	public ActionForward viewSelectedHelpTopic(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
        HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getTopicKey(),
				siteId, moduleInstance);
		if(helpTopic != null){
			helpForm.setBodyEditKey(helpTopic.getBodyEditKey());
			helpForm.setTitleTrnKey(helpTopic.getTitleTrnKey());
            int title = helpTopic.getTitleTrnKey().indexOf("e:");
            String tit = helpTopic.getTitleTrnKey().substring(title+2);
            helpForm.setTitleTrnKey(tit);

            helpForm.setHelpErrors(null);
			helpForm.setSearched(null);
			helpForm.setFlag(true);
			LuceneUtil.addUpdatehelp(true, request.getSession().getServletContext());
			
		if(helpTopic.getParent()!=null){
			helpForm.setParentId(helpTopic.getParent().getHelpTopicId());
		}
		}else{
		helpForm.setBodyEditKey("");
		helpForm.setTitleTrnKey("");
		}
		return mapping.findForward("help");
	}

	public ActionForward deleteHelpTopics(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		ServletContext sc = request.getSession().getServletContext();
		String moduleInstance = RequestUtils.getRealModuleInstance(request).getInstanceName();	
		String page  = request.getParameter("page");
		helpForm.setHelpErrors(null);
		if(request.getParameter("multi")!=null && request.getParameter("multi").equals("false")){
			if (helpForm.getTopicKey() != null) {
//				HelpTopic helpTopic = HelpUtil.getHelpTopicByBodyEditKey("help:topic:body:"+helpForm.getTopicKey(),	siteId, moduleInstance);
				HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getHelpTopicId());
				if(helpTopic!=null){
					removeLastLevelTopic(helpTopic);
					helpForm.setTopicKey("");
					helpForm.setBlankPage(false);
			  }
			}
		}else if(request.getParameter("multi").equals("true")){
			// delete Lucene helpSearch Directory 
			//WTF???
			File path = new File(sc.getRealPath("/") +"lucene" +"/" + "help");
			LuceneUtil.deleteDirectory(path);
			//remove all topics that were selected
			String tIds=request.getParameter("tIds");
			List<Long> topicsIds=getTopicsIds(tIds.trim());
			for (Long id : topicsIds) {
				HelpTopic ht=HelpUtil.loadhelpTopic(id);
				if(ht!=null){
					removeLastLevelTopic(ht);
				}
			}			
		}
		
        if(page != null){
            if(!page.equals("admin")){
                return mapping.findForward("helpHome");
            }else{
            	return mapping.findForward("helpAdminPage");
            }
        }else {
            return mapping.findForward("helpHome");            
        }
    }
	

	public ActionForward createHelpTopic(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
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
			//clear session if it contains sdm doc
	        if(request.getSession().getAttribute("document")!=null){
	        	request.getSession().removeAttribute("document");
	        }
			break;
		}
		default:
			break;
		}

		return mapping.findForward("help");
	}

	public ActionForward editHelpTopic(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)	throws Exception {
		HelpForm helpForm = (HelpForm) form;
//		String siteId = RequestUtils.getSite(request).getSiteId();
//		String moduleInstance = RequestUtils.getRealModuleInstance(request).getInstanceName();
		String page  = request.getParameter("page");
//		String topicKey = request.getParameter("topicKey");
//		if(topicKey == null && helpForm.getTopicKey() != null){
//			topicKey =helpForm.getBodyEditKey().substring(helpForm.getBodyEditKey().indexOf("y:")+2);
//			
//		}
		helpForm.setPage(page);
		//String key = HelpUtil.getTrn(topicKey, request);
//		HelpTopic helpTopic = HelpUtil.getHelpTopicByBodyEditKey("help:topic:body:"+topicKey,siteId, moduleInstance);
		HelpTopic helpTopic = HelpUtil.getHelpTopic(helpForm.getHelpTopicId());
		
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
				//clear session if it contains sdm doc
		        if(request.getSession().getAttribute("document")!=null){
		        	request.getSession().removeAttribute("document");
		        }
			}
			default:
				break;
			}
		}
		if (helpForm.getGlossaryMode()!=null && helpForm.getGlossaryMode().booleanValue()==true){
			mapping.findForward("glossaryHome");
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
	private void editTopicStep0(HelpForm form, HttpServletRequest request,HelpTopic topic) throws Exception {
		setDefaultValues(form);
		form.setWizardStep(1);
		form.setEdit(true);
		form.setTitleTrnKey(topic.getTitleTrnKey());
		form.setKeywordsTrnKey(topic.getKeywordsTrnKey());
		form.setBodyEditKey(topic.getBodyEditKey());
		form.setHelpTopicId(topic.getHelpTopicId());
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request).getInstanceName();
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		List<HelpTopic> parentTopics=(List)HelpUtil.getFirstLevelTopics(siteId,moduleInstance, null);
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
	private void editTopicStep1(HelpForm form, HttpServletRequest request,HelpTopic topic) throws AimException {
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

	public ActionForward cancelHelpTopic(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String topickey=null;
		if(helpForm.getBodyEditKey()!=null){
			int editkey = helpForm.getBodyEditKey().indexOf("body:");
	        topickey = helpForm.getBodyEditKey().substring(editkey+5);
		}		
		helpForm.setTopicKey(topickey);
		setDefaultValues(helpForm);
		//clear session if it contains sdm doc
        if(request.getSession().getAttribute("document")!=null){
        	request.getSession().removeAttribute("document");
        }
        //if we were creating new help topic and decided to cancel it, than editor object which was created has to be removed, not to have garbage data in db
        if(helpForm.getHelpTopicId()==null){
        	ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
            Site site;
            if (moduleInstance == null) {
                site = RequestUtils.getSite(request);
            }
            else {
                site = moduleInstance.getSite();
            }
            
        	List<Editor> editors = org.digijava.module.editor.util.DbUtil.getEditorList(helpForm.getBodyEditKey(),site.getSiteId());
    		if (editors!=null && editors.size()>0){
    			for (Editor editor : editors) {
    				org.digijava.module.editor.util.DbUtil.deleteEditor(editor);
    			}
    		}
        	
        	
        	
        	//Editor helpBody= org.digijava.module.editor.util.DbUtil.getEditor(site.getSiteId(), helpForm.getBodyEditKey(),RequestUtils.getNavigationLanguage(request).getCode());
        	//org.digijava.module.editor.util.DbUtil.deleteEditor(helpBody);
        }
        
		if (helpForm.getGlossaryMode()!=null && helpForm.getGlossaryMode().booleanValue()==true){
			mapping.findForward("glossaryHome");
		}
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
	private List<String> validate(HelpForm form, String siteId,String moduleInstance) throws AimException {
		List<String> helpErrors=new ArrayList<String>();
//		if (!HelpUtil.cheackEditKey(form.getTopicKey(), siteId, moduleInstance)) {
//			helpErrors.add("errors.help.createTopic.keyIsUsed");
//		}
		return helpErrors;
	}

	/**
	 * Create wizard:step 0
	 * 
	 * @param form
	 */
	private void createTopicStep0(HelpForm form, HttpServletRequest request)throws Exception {
		if (request.getParameter("actionBack") == null) {
			form.setTopicKey(null);
			form.setBodyEditKey(null);
			setDefaultValues(form);
		}
		form.setEdit(false);
		form.setWizardStep(1);
		form.setHelpErrors(null);
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		String locale=RequestUtils.getNavigationLanguage(request).getCode();
		List<HelpTopic> parentTopics=HelpUtil.getFirstLevelTopics(siteId,	moduleInstance, null);
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
	private void createTopicStep1(HelpForm form, HttpServletRequest request)throws AimException {
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		form.setHelpErrors(null);	 
		if (!validate(form, siteId, moduleInstance).isEmpty()) {
			form.setHelpErrors(validate(form, siteId, moduleInstance));	
		} else {
			form.setWizardStep(2);
			String editorKey="help-" + moduleInstance + "-"+request.getSession().getId().hashCode() + "-" + String.valueOf(System.currentTimeMillis());
			
			form.setBodyEditKey(editorKey);
//			form.setBodyEditKey("help:topic:body:" + form.getTopicKey());
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
	private void createTopicStep3(HelpForm form, HttpServletRequest request)throws AimException, Exception {
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request).getInstanceName();		
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
	
	public ActionForward viewAdmin(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        HelpForm helpForm = (HelpForm) form;
        String siteId=RequestUtils.getSite(request).getSiteId();
        String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
		helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
        helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
        helpForm.setGlossaryTree(HelpUtil.getGlossaryTopicsTree(siteId, moduleInstance));
	  return mapping.findForward("admin");
	}
	
	
	public ActionForward export(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {

        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxbi");
        Marshaller m = jc.createMarshaller();
        //response.setContentType("text/xml");
        response.setContentType("application/zip");
		response.setHeader("content-disposition", "attachment; filename=exportHelp.zip");
		ObjectFactory objFactory = new ObjectFactory();
		AmpHelpRoot help_out = objFactory.createAmpHelpRoot();
		List <AmpHelpType> rsAux;
        logger.info("loading helpData");
        ServletOutputStream ouputStream = response.getOutputStream();
        ZipOutputStream out = new ZipOutputStream(ouputStream);
        rsAux= HelpUtil.getExportData(out);


        help_out.getAmpHelp().addAll(rsAux);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        m.marshal(help_out,bos);
        out.putNextEntry(new ZipEntry("helpExport.xml"));
        byte [] myArray= bos.toByteArray();
        out.write(myArray, 0, myArray.length);
        // Complete the entry
        out.closeEntry();        
        // Complete the ZIP file     
	    out.close();
      
        return null;

	}

	
	public ActionForward importing(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		HashMap<Long,HelpTopic> storeMap=new HashMap<Long, HelpTopic>();
		HelpForm helpForm = (HelpForm) form;
		String siteId=RequestUtils.getSite(request).getSiteId();
        Long Id =RequestUtils.getSite(request).getId();
        String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
        
		FormFile myFile = helpForm.getFileUploaded();

        ZipInputStream zis = new ZipInputStream(myFile.getInputStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipEntry entry;
        //Sdm mySdm=null;
        byte[] xmlContent=null;
        HashMap <String,Set<SdmItem>> imgsHolder =new HashMap <String,Set<SdmItem>> (); 
        HashMap <String,Long> topicAttachmentHolder =new HashMap <String,Long> (); 
        AmpHelpRoot help_in;
        
        Sdm kuku=new Sdm();
        kuku.setItems(new HashSet<SdmItem>());
        
        while ((entry = zis.getNextEntry()) != null) {        	
        	if(entry.getName().endsWith(".xml")){
        		
        		int size = 0;
    	        byte[] buffer = new byte[1*1024*1024];
    	        int realZize = 0;
    	        byte[] largeBuffer = new byte[10*1024*1024];  // 10 MB size
    	        
    	        while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
    	        	System.arraycopy(buffer, 0, largeBuffer, realZize, buffer.length);    	        	
    	        	realZize+=size;
    	        }
    	        
    	        xmlContent = Arrays.copyOfRange(largeBuffer, 0, realZize);
        	}else{
        		int size = 0;
    	        byte[] buffer = new byte[1*1024*1024];
    	        int realZize = 0;
    	        byte[] largeBuffer = new byte[5*1024*1024];  // 5 MB size
    	        
    	        while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
    	        	System.arraycopy(buffer, 0, largeBuffer, realZize, buffer.length);    	        	
    	        	realZize+=size;
    	        }
    	        byte[] content = Arrays.copyOfRange(largeBuffer, 0, realZize);
    	
    	        SdmItem sdmItem = new SdmItem();
    	        sdmItem.setContentType("image/jpeg");
    	        sdmItem.setRealType(SdmItem.TYPE_IMG);
    	        sdmItem.setContent(content);
    	        sdmItem.setContentText(entry.getName());
    	        sdmItem.setContentTitle(entry.getName());
    	        //paragraph order
    	        String parOrd = entry.getName().substring(entry.getName().lastIndexOf("_")+1,entry.getName().indexOf("."));
    	        sdmItem.setParagraphOrder(new Long(parOrd));
    	        
    	        //get parent help topic
    	        String topicEditKey = entry.getName().substring(0,entry.getName().indexOf("_poIs")); 
    	        Set<SdmItem> items =imgsHolder.get(topicEditKey);
    	        if(items==null) {
    	        	items=new HashSet<SdmItem>();
    	        }
    	        items.add(sdmItem);
    	        imgsHolder.put(topicEditKey, items); 
        	}
        }       
        
        
	    //xml import
	    JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxbi");
        Unmarshaller m = jc.createUnmarshaller();
        help_in = (AmpHelpRoot) m.unmarshal(new ByteArrayInputStream(xmlContent));
        //remove all existing help topics           
        List<HelpTopic> firstLevelTopics=HelpUtil.getFirstLevelTopics(siteId);
      
        for (HelpTopic helpTopic : firstLevelTopics) {
			removeLastLevelTopic(helpTopic);
		}
        
        for (String  helpTopicEditKey : imgsHolder.keySet()) { //store which topic has reference for what Sdm doc
        	Sdm helpAttachmentsHolder = new Sdm();
        	helpAttachmentsHolder.setName(helpTopicEditKey);
        	helpAttachmentsHolder.setItems(imgsHolder.get(helpTopicEditKey));
        	helpAttachmentsHolder = org.digijava.module.sdm.util.DbUtil.saveOrUpdateDocument(helpAttachmentsHolder);

        	topicAttachmentHolder.put(helpTopicEditKey, helpAttachmentsHolder.getId());
		}
        
        if (help_in.getAmpHelp()!= null) {
        	logger.info("Starting Help Import");
        	Iterator it = help_in.getAmpHelp().iterator();
        	while(it.hasNext()) {
				AmpHelpType element  = (AmpHelpType) it.next();
	            HelpUtil.updateNewEditHelpData(element,storeMap,Id,topicAttachmentHolder);
			}
		}
	    logger.info("Finished Help Import");
		helpForm.getTopicTree().clear();
		helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
		helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
		return mapping.findForward("admin");
	}
	
//	public ActionForward importing(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
//		    HashMap<Long,HelpTopic> storeMap=new HashMap<Long, HelpTopic>();
//			HelpForm helpForm = (HelpForm) form;
//			String siteId=RequestUtils.getSite(request).getSiteId();
//            Long Id =RequestUtils.getSite(request).getId();
//            String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
//			
//		FormFile myFile = helpForm.getFileUploaded();
//        byte[] fileData    = myFile.getFileData();
//
//
//        InputStream inputStream= new ByteArrayInputStream(fileData);
//        
//
//        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxbi");
//        Unmarshaller m = jc.createUnmarshaller();
//        AmpHelpRoot help_in;
//        
//            help_in = (AmpHelpRoot) m.unmarshal(inputStream);
//            //remove all existing help topics           
//            List<HelpTopic> firstLevelTopics=HelpUtil.getFirstLevelTopics(siteId);
//            
//            for (HelpTopic helpTopic : firstLevelTopics) {
//				removeLastLevelTopic(helpTopic);
//			}
//            
//            if (help_in.getAmpHelp()!= null) {
//            	logger.info("Starting Help Import");
//				Iterator it = help_in.getAmpHelp().iterator();
//				while(it.hasNext())
//				{
//					AmpHelpType element  = (AmpHelpType) it.next();
//                    HelpUtil.updateNewEditHelpData(element,storeMap,Id);
//				}
//			}
//            logger.info("Finished Help Import");
//			helpForm.getTopicTree().clear();
//			helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
//			helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
//			return mapping.findForward("admin");
//	}
	
	/**
	 * recurrent function. If topic is last level,then it is deleted.  If not,function calls itself until it finds last level child topic and
	 * removes it, e.t.c. until topic(function parameter) becomes last level itself and after that its also removed
	 * @param topic
	 * @throws Exception
	 */
	private void removeLastLevelTopic(HelpTopic topic) throws Exception{
		List<HelpTopic> childs=HelpUtil.getChildTopics(topic.getSiteId(),topic.getHelpTopicId());
		if(childs==null || childs.size()==0){
			HelpUtil.deleteHelpTopic(topic);
		}else{			
			for (HelpTopic child : childs) {
				removeLastLevelTopic(child);
			}
			HelpUtil.deleteHelpTopic(topic);
		}
	}

    private void removeLastLevelTopic(HelpTopic topic,String moduleInstance) throws Exception{
        
        List<HelpTopic> childs=HelpUtil.getChildTopics(topic.getSiteId(), moduleInstance, topic.getHelpTopicId());
		if(childs==null || childs.size()==0){
			HelpUtil.deleteHelpTopic(topic);
		}else{
			for (HelpTopic child : childs) {
				removeLastLevelTopic(child);
			}
			HelpUtil.deleteHelpTopic(topic);
		}
	}

    private List<Long> getTopicsIds(String ids){
		List<Long> topicsIds=new ArrayList<Long>();
		while(ids.indexOf(",")!= -1){
			Long id= new Long(ids.substring(0,ids.indexOf(",")).trim());
			topicsIds.add(id);
			ids=ids.substring(ids.indexOf(",")+1);
		}
		topicsIds.add(new Long(ids.trim()));
		return topicsIds;
	}

  
    public void saveTreeState(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
                throws Exception{

          String siteId=RequestUtils.getSite(request).getSiteId();
          
         // String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();

          String xmlString = request.getParameter("changedXml");
          String replacedXmlString =  xmlString.replaceAll("&", "&amp;");
          
          String moduleInstance = request.getParameter("Request");
          
          
          org.w3c.dom.Element e;
          org.w3c.dom.NamedNodeMap nnm;
          int i;
          org.w3c.dom.Node n;
          String attrname;
          String attrval;

       List<HelpTopic> listOfTree = new ArrayList<HelpTopic>();
        HashMap<Long,HelpTopic> storeMap=new HashMap<Long, HelpTopic>();



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                  DocumentBuilder builder = factory.newDocumentBuilder();
                  org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(replacedXmlString)));

                  
                  
        org.w3c.dom.NodeList nl = document.getElementsByTagName("item");
        
        for(int j=0; j<nl.getLength(); j++){
        	org.w3c.dom.Node node = nl.item(j);
        	HelpTopic helpTopick = new HelpTopic();
        	helpTopick.setTopicKey(node.getAttributes().getNamedItem("text").getNodeValue());
        	helpTopick.setHelpTopicId(new Long(node.getAttributes().getNamedItem("id").getNodeValue()));
        	HelpTopic topic =  HelpUtil.loadhelpTopic(new Long(helpTopick.getHelpTopicId()));
        	helpTopick.setSiteId(topic.getSiteId());
        	helpTopick.setBodyEditKey(topic.getBodyEditKey());
        	helpTopick.setModuleInstance(topic.getModuleInstance());
        	helpTopick.setKeywordsTrnKey(topic.getKeywordsTrnKey());
        	helpTopick.setTitleTrnKey(topic.getTitleTrnKey());
        	helpTopick.setHelpTopicId(topic.getHelpTopicId());
        	
        	org.w3c.dom.Node parentNode = node.getParentNode();

        	if (parentNode != null && !parentNode.getNodeName().trim().equalsIgnoreCase("tree")){

        		HelpTopic parentHelpTopick = new HelpTopic();
        		parentHelpTopick.setTopicKey(parentNode.getAttributes().getNamedItem("text").getNodeValue());
        		parentHelpTopick.setHelpTopicId(new Long(parentNode.getAttributes().getNamedItem("id").getNodeValue()));
        		helpTopick.setParent(parentHelpTopick);
        	}
        	storeMap.put(helpTopick.getHelpTopicId(), helpTopick);
        	listOfTree.add(helpTopick);
        }
        	
        	List<HelpTopic> firstLevelTopics=HelpUtil.getFirstLevelTopics(siteId,moduleInstance);

            for (HelpTopic helpTopic : firstLevelTopics) {
				removeLastLevelTopic(helpTopic,moduleInstance);
			}
            for (HelpTopic topic : listOfTree){
                HelpUtil.saveNewTreeState(topic,storeMap,siteId);
            }
         }


    	public ActionForward saved(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		return mapping.findForward("helpHome");
    	}
	}
    