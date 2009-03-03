package org.digijava.module.help.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.ecs.xhtml.code;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;
import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.jaxbi.AmpHelpRoot;
import org.digijava.module.help.jaxbi.AmpHelpType;
import org.digijava.module.help.jaxbi.ObjectFactory;
import org.digijava.module.help.util.HelpUtil;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;



import java.io.StringReader;


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


     public ActionForward getbody(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)throws Exception{
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		HelpForm helpForm = (HelpForm) form;
		OutputStreamWriter os = null;
	    PrintWriter out = null;
	    String loadStatus = request.getParameter("body");
         String siteId = RequestUtils.getSite(request).getSiteId();
         String	lange	= RequestUtils.getNavigationLanguage(request).getCode();

        try {
			if(loadStatus != null){
				LuceneUtil.createHelp(request.getSession().getServletContext());
				os = new OutputStreamWriter(response.getOutputStream());
	            out = new PrintWriter(os, true);
				String id = loadStatus.toLowerCase();
				HelpTopic key = HelpUtil.getHelpTopic(new Long(id));
	            String bodyKey =  key.getBodyEditKey();
	            String article = HelpUtil.getTrn(key.getTopicKey(),request);
	            out.println("<b>"+article+"</b>");
	            List editor = HelpUtil.getEditor(bodyKey, lang);
	            helpForm.setTopicKey(bodyKey);
	
	
	            if(!editor.isEmpty()){
	               Iterator iter = editor.iterator();
					while (iter.hasNext()) {
						Editor help = (Editor) iter.next();
	                   out.println(help.getBody());
	                   out.println(help.getEditorKey());
	                    helpForm.setTopicKey(help.getEditorKey());
	                    //System.out.println("TopicKey:"+helpForm.getTopicKey());
	                }
				}else{
	               out.println(helpForm.getTopicKey()); 
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
		    
			 try {
				 os = new OutputStreamWriter(response.getOutputStream());
				 out = new PrintWriter(os, true);	 
				 if(loadStatus != null){
						 List<Editor> data =  HelpUtil.getAllHelpKey(lange);
						 
						 for(Iterator iter = data.iterator(); iter.hasNext(); ) {
							  item = (Editor) iter.next();
				 			
				           
							  int editkey = item.getEditorKey().indexOf("body:");
					          String title = item.getEditorKey().substring(editkey+5);
				             if(title.length()>=loadStatus.length()){
				             if(loadStatus.toLowerCase().equals(title.toLowerCase().substring(0,loadStatus.length()))){
				            	
				            	String xs = HelpUtil.getTrn(title,request);
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
	
	
	public ActionForward searchHelpTopic(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		 String key =request.getParameter("key");
		 //String keywords = HelpUtil.getTrn(key,request);
		 String treKey = HelpUtil.getTrn("Topic Not Found", request);
		 String locale=RequestUtils.getNavigationLanguage(request).getCode();
		 Object artidcle = "";
		 OutputStreamWriter os = null;	
		 PrintWriter out = null;
		
	try{	
	     os = new OutputStreamWriter(response.getOutputStream());
	     out = new PrintWriter(os, true);	 
				if(key != null){
					 Collection<LabelValueBean> Searched = new ArrayList<LabelValueBean>();
					 Hits hits =  LuceneUtil.helpSearch("title", key, request.getSession().getServletContext());
			
			         String artikleTitle;
					 
					 HelpForm help = (HelpForm) form;	
					 
					  int hitCount = hits.length();   
			    	   
			    	  if(hitCount == 0){
			    		
			    		  out.println("<div style=\"font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;\"><a class=\"link\"><b>"+key+"</b></a></div>");

			    		  out.println("<div>"+treKey+"...</div>");
			    	  
			    	  }else{
			    	  
			    		  for(int i=0; (i < hitCount && i < 10); i++){
			    		
			
			    		  Document doc = hits.doc(i);
			    		  	if(doc.get("lang").equals(locale)){  
			    			  
						   String title = doc.get("title");
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
							   out.println("<div id=\"bodyTitle\" style=\"font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;\"><a class=\"link\" onclick=\"showBody()\"><b>"+t.getLabel()+"</b></a></div>");
							   out.println("<div id=\"bodyShort\"  style=\"display:block;\">"+t.getValue()+"</div>");
						   }
						  
						  List<Editor> wholeBody = HelpUtil.getEditor("help:topic:body:"+key, locale);
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
				HelpTopic helpTopic = HelpUtil.getHelpTopicByBodyEditKey("help:topic:body:"+helpForm.getTopicKey(),	siteId, moduleInstance);
				if(helpTopic!=null){
					removeLastLevelTopic(helpTopic);
					helpForm.setTopicKey("");
					helpForm.setBlankPage(false);
			  }
			}
		}else if(request.getParameter("multi").equals("true")){
			// delete Lucene helpSearch Directory 
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
                if(helpForm.getAdminTopicTree()!=null){
                helpForm.getAdminTopicTree().clear();
                }
                helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId, "admin"));
                helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, "default"));
                return mapping.findForward("admin");
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
			break;
		}
		default:
			break;
		}

		return mapping.findForward("help");
	}

	public ActionForward editHelpTopic(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)	throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String siteId = RequestUtils.getSite(request).getSiteId();
		String moduleInstance = RequestUtils.getRealModuleInstance(request)
				.getInstanceName();
		String page  = request.getParameter("page");
		String topicKey = request.getParameter("topicKey");
		if(topicKey == null && helpForm.getTopicKey() != null){
			topicKey =helpForm.getBodyEditKey().substring(helpForm.getBodyEditKey().indexOf("y:")+2);
			
		}
		helpForm.setPage(page);
		//String key = HelpUtil.getTrn(topicKey, request);
		HelpTopic helpTopic = HelpUtil.getHelpTopicByBodyEditKey("help:topic:body:"+topicKey,
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
	private void editTopicStep0(HelpForm form, HttpServletRequest request,HelpTopic topic) throws Exception {
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

	public ActionForward cancelHelpTopic(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HelpForm helpForm = (HelpForm) form;
		String topickey=null;
		if(helpForm.getBodyEditKey()!=null){
			int editkey = helpForm.getBodyEditKey().indexOf("body:");
	        topickey = helpForm.getBodyEditKey().substring(editkey+5);
		}		
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
	private List<String> validate(HelpForm form, String siteId,String moduleInstance) throws AimException {
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
		List<HelpTopic> parentTopics=(List)HelpUtil.getFirstLevelTopics(siteId,	moduleInstance, null);
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
	private void createTopicStep3(HelpForm form, HttpServletRequest request)throws AimException {
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
	
	public ActionForward viewAdmin(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		logger.info("Vew Admin");
        HelpForm helpForm = (HelpForm) form;
        logger.info("helpForm");
        String siteId=RequestUtils.getSite(request).getSiteId();
        logger.info("siteId"+siteId);
        String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
		helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));

        helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
        logger.info("Gettreee");


        helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
	
	  return mapping.findForward("admin");
	}
	
	
	public ActionForward export(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {


        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxbi");
        Marshaller m = jc.createMarshaller();
        response.setContentType("text/xml");
		response.setHeader("content-disposition", "attachment; filename=exportHelp.xml");
		ObjectFactory objFactory = new ObjectFactory();
		AmpHelpRoot help_out = objFactory.createAmpHelpRoot();
		Vector rsAux;
        logger.info("loading helpData");
        rsAux= HelpUtil.getAllHelpdataForExport();


        help_out.getAmpHelp().addAll(rsAux);
    
        m.marshal(help_out,response.getOutputStream());
      
        return null;

	}
	
	public ActionForward importing(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		    HashMap<Long,HelpTopic> storeMap=new HashMap<Long, HelpTopic>();
			HelpForm helpForm = (HelpForm) form;
			String siteId=RequestUtils.getSite(request).getSiteId();
            Long Id =RequestUtils.getSite(request).getId();
            String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();
			
		FormFile myFile = helpForm.getFileUploaded();
        byte[] fileData    = myFile.getFileData();


        InputStream inputStream= new ByteArrayInputStream(fileData);
        

        JAXBContext jc = JAXBContext.newInstance("org.digijava.module.help.jaxbi");
        Unmarshaller m = jc.createUnmarshaller();
        AmpHelpRoot help_in;
        
            help_in = (AmpHelpRoot) m.unmarshal(inputStream);
            //remove all existing help topics           
            List<HelpTopic> firstLevelTopics=HelpUtil.getFirstLevelTopics(siteId);
            
            for (HelpTopic helpTopic : firstLevelTopics) {
				removeLastLevelTopic(helpTopic);
			}
            
            if (help_in.getAmpHelp()!= null) {
				Iterator it = help_in.getAmpHelp().iterator();
				while(it.hasNext())
				{
					AmpHelpType element  = (AmpHelpType) it.next();
                    HelpUtil.updateNewEditHelpData(element,storeMap,Id);
				}
			}
            logger.info("Finished Help Exporting");
			helpForm.getTopicTree().clear();
			helpForm.setTopicTree(HelpUtil.getHelpTopicsTree(siteId, moduleInstance));
			helpForm.setAdminTopicTree(HelpUtil.getHelpTopicsTree(siteId,"admin"));
			return mapping.findForward("admin");
	}
	
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
          String moduleInstance=RequestUtils.getRealModuleInstance(request).getInstanceName();

          String xmlString = request.getParameter("changedXml");
          
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
                  org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(xmlString)));

                  
                  
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
    