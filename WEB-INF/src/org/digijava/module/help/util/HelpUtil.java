package org.digijava.module.help.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.helper.HelpTopicsTreeItem;
import org.digijava.module.help.jaxb.AmpHelpType;
import org.digijava.module.help.jaxb.ObjectFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.digijava.module.help.jaxb.HelpLang;
import org.digijava.kernel.translator.TranslatorWorker;
import java.util.Date;

public class HelpUtil {
	private static Logger logger = Logger.getLogger(HelpUtil.class);

	/**
	 * Retrieves all help topics
	 * 
	 * @param String
	 *            siteId
	 * @param String
	 *            moduleInstance
	 * @returns List<HelpTopic> helpTopics
	 * @throws AimException
	 */
	public static Collection getHelpTopicsTree(String siteId,
			String moduleInstance) throws AimException {
		List<HelpTopic> helpTopics= getHelpTopics(siteId, moduleInstance,null, null);
		Collection themeTree = CollectionUtils.getHierarchy(helpTopics,
                new HelpTopicHierarchyDefinition(), new HelpTopicTreeItemFactory());
		return themeTree;
	}
	


    public static class HelpTopicTreeItemFactory implements HierarchyMemberFactory{
        public HierarchyMember createHierarchyMember(){
        	HelpTopicsTreeItem item=new HelpTopicsTreeItem();
            item.setChildren(new ArrayList());
            return item;
        }
    }

    public static class HelpTopicHierarchyDefinition implements
            HierarchyDefinition {
        public Object getObjectIdentity(Object object) {
            HelpTopic topic = (HelpTopic) object;
            return topic.getHelpTopicId();

        }

        public Object getParentIdentity(Object object) {
        	HelpTopic topic = (HelpTopic) object;
            if (topic.getParent() == null) {
                return null;
            }
            else {
                return topic.getParent().getHelpTopicId();
            }
        }
    }

	/**
	 * Filters help topics using keywords
	 * 
	 * @param String
	 *            siteId
	 * @param String
	 *            moduleInstance
	 * @param String
	 *            keyWords
	 * @returns List<HelpTopic> helpTopics
	 * @throws AimException
	 */
	public static List<HelpTopic> getHelpTopics(String siteId,
			String moduleInstance,String locale, String keyWords) throws AimException {
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			if ((keyWords == null || keyWords.equals(""))&&(locale==null)) {
				String queryString = "from "
						+ HelpTopic.class.getName()
						+ " topics where (topics.siteId=:siteId) and (topics.moduleInstance=:moduleInstance) order by topics.keywordsTrnKey";
				query = session.createQuery(queryString);
			} else {
				String queryString = "select distinct t from "
						+ HelpTopic.class.getName()+ " t, "+ Message.class.getName()+ " msg "
						+"where (t.titleTrnKey=msg.key or t.keywordsTrnKey=msg.key) "
						+ "and t.siteId=:siteId and t.moduleInstance=:moduleInstance "
						+ "and msg.locale=:locale and msg.message like '%"+ keyWords+ "%'";
				query = session.createQuery(queryString);
				query.setParameter("locale", locale);
			}
			
			query.setParameter("siteId", siteId);
			query.setParameter("moduleInstance", moduleInstance);			
			helpTopics = query.list();

		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}

		return helpTopics;

	}

	public static HelpTopic getHelpTopic(Long helpTopicId)throws AimException{
		Session session = null;
		HelpTopic helpTopic=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			helpTopic = (HelpTopic) session.load(HelpTopic.class, helpTopicId);
			
		} catch (Exception ex) {
			logger.error(ex);
			throw new AimException(ex);
		}
		return helpTopic;
		
	}
	
	public static HelpTopic loadhelpTopic(Long topicId) throws Exception{
		Session session = null;
		Query query = null;
		HelpTopic helpTopic = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString="from "+ HelpTopic.class.getName()+" topic where topic.helpTopicId="+topicId;
			query=session.createQuery(queryString);
			helpTopic=(HelpTopic)query.uniqueResult();
		} catch (Exception ex) {
			logger.error(ex);
			throw new AimException(ex);
		}
		return helpTopic;
	}
	
	public static HelpTopic getHelpTopic(String key,String siteId,String moduleInstance) throws AimException {
		Session session = null;
		Query query = null;
		HelpTopic helpTopic = null;
		if (key != null && !key.equals("")) {
			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString="from "+ HelpTopic.class.getName()+" topic where (topic.topicKey=:key) " +
						" and (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) ";
				query=session.createQuery(queryString);
				query.setParameter("siteId", siteId);
				query.setParameter("moduleInstance", moduleInstance);
				query.setParameter("key", key);
				helpTopic=(HelpTopic) query.uniqueResult();
			} catch (Exception e) {
				logger.error(e);
				throw new AimException(e);
			}
		} else {
			throw new AimException("incorrect parameter key");
		}
		return helpTopic;
	}

	public static boolean cheackEditKey(String key, String siteId,
			String moduleInstance) throws AimException {
		Session session = null;
		Query query = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "from "
					+ HelpTopic.class.getName()
					+ " topic where (topic.topicKey=:key) and "
					+ "(topic.siteId=:siteId) and (topic.moduleInstance=:instance)";
			query = session.createQuery(queryString);
			query.setParameter("key", key);
			query.setParameter("siteId", siteId);
			query.setParameter("instance", moduleInstance);
			if (query.uniqueResult() == null) {
				return true;
			}

		} catch (Exception e) {
			logger.error(e);
			throw new AimException(e);
		}
		return false;
	}

	public static void saveOrUpdateHelpTopic(HelpTopic topic) throws AimException{
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(topic);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", ex);
				}
			}
			throw new AimException("Can't update help topic", e);
		}
	}
	
	public static void deleteHelpTopic(HelpTopic topic) throws AimException{
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			session.delete(topic);			
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", ex);
				}
			}
			throw new AimException("Can't remove help topic", e);
		}
	}
	public static List<HelpTopic> getFirstLevelTopics(String siteId,String moduleInstance,String key)throws AimException{
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()
			+ " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent is null)";
			query = session.createQuery(queryString);			
			query.setParameter("siteId", siteId);
			query.setParameter("moduleInstance", moduleInstance);
			helpTopics = query.list();

		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}
		if (key!=null){
			HelpTopic curTopic=getHelpTopic(key, siteId, moduleInstance);
			helpTopics.remove(curTopic);
		}
		return helpTopics;
	}
	
	/**
	 * Load all first level help topics(admin and default)
	 * @param siteId
	 * @throws Exception
	 */
	public static List<HelpTopic> getFirstLevelTopics(String siteId) throws Exception{
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.parent is null";
			query = session.createQuery(queryString);			
			query.setParameter("siteId", siteId);
			helpTopics = query.list();
		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}
		return helpTopics;
	}
	
	public static boolean hasChildren(String siteId,String moduleInstance,Long parentId)throws AimException{
		List<HelpTopic> helpTopics = getChildTopics(siteId, moduleInstance,	parentId);
		if (helpTopics==null || helpTopics.size()==0){
			return false;
		}
	return true;	
	}

	public static List<HelpTopic> getChildTopics(String siteId,String moduleInstance, Long parentId) throws AimException {
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()
			+ " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent.helpTopicId=:id)";
			query = session.createQuery(queryString);			
			query.setParameter("siteId", siteId);
			query.setParameter("moduleInstance", moduleInstance);
			query.setParameter("id", parentId);
			helpTopics = query.list();			

		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}
		return helpTopics;
	}
	
	
	public static List<Editor> getAllHelpKey() throws 
    EditorException {
	
	Session session = null;
	List<Editor> helpTopics = new ArrayList<Editor>();
	
	try {
		session = PersistenceManager.getRequestDBSession();
		 Query q = session.createQuery(" from e in class " +
                 Editor.class.getName() +" where e.editorKey like 'help%' order by e.lastModDate");

		helpTopics = q.list();
		
		
	} catch (Exception e) {
		logger.error("Unable to load help data");
			throw new EditorException("Unable to Load Help data", e);
	}
	return helpTopics;
}
	
    public static Collection getAllHelpData() throws 
    EditorException {
	
	Session session = null;
	Query query = null;
	  System.out.println("GetAllHelpData");
	Collection helpTopics = new ArrayList();
	
	try {
		session = PersistenceManager.getRequestDBSession();
		String queryString = "select t from "
			+ HelpTopic.class.getName()+ " t, "+ Editor.class.getName()+ " e "
			+"where (t.bodyEditKey=e.editorKey) order by e.lastModDate";
		 query = session.createQuery(queryString);

		
		//		 Query q = session.createQuery(" from e in class " +
//                 Editor.class.getName() +" where e.editorKey like 'help%' order by e.lastModDate");
//
		//helpTopics = query.list();
		Iterator itr = query.list().iterator();
		HelpSearchData helpsearch;
		
		while (itr.hasNext()) {
			HelpTopic help = (HelpTopic) itr.next();
			helpsearch = new HelpSearchData();
			helpsearch.setTitleTrnKey(help.getTitleTrnKey());
			helpsearch.setTopicKey(help.getTopicKey());
			List<Editor> Body = getbody(help.getBodyEditKey());
            System.out.println("bodyeditkey:"+help.getBodyEditKey());
            System.out.println("body:"+Body);
			Iterator iter = Body.iterator();
			if(Body != null){
			while (iter.hasNext()) {
				Editor item = (Editor) iter.next();
				helpsearch.setBody(item.getBody());
				helpsearch.setLastModDate(item.getLastModDate());
			}

			helpTopics.add(helpsearch);	
		}
        }
		
		
	} catch (Exception e) {
		logger.error("Unable to load help data");
			throw new EditorException("Unable to Load Help data", e);
	}
	return helpTopics;
}
	
    
    public static List<Editor> getbody(String bodyKey){
    	List<Editor> result = null;
    	Session session = null;
    	Query query = null;	
    
    	try {
    	session = PersistenceManager.getRequestDBSession();
    	
    	String queryString = "select topic from "+ Editor.class.getName() + " topic where (topic.editorKey=:bodyKey)";
    	     query = session.createQuery(queryString);
    	     query.setParameter("bodyKey", bodyKey);
    	     result = query.list();
    	     
		} catch (Exception e) {
			logger.error("Unable to load help data");
		}
        logger.debug("getBodyResult:"+result);
    	return result;
    }

    
    public static List<Editor> getEditor(String bodyKey,String lang){
    	List<Editor> result = null;
    	Session session = null;
    	Query query = null;	
    
    	try {
    	session = PersistenceManager.getRequestDBSession();
    	
    	String queryString = "select topic from "+ Editor.class.getName() + " topic where (topic.editorKey=:bodyKey) and (topic.language=:lang)";
    	     query = session.createQuery(queryString);
    	     query.setParameter("bodyKey", bodyKey);
    	     query.setParameter("lang", lang);
    	     result = query.list();
    	     
		} catch (Exception e) {
			logger.error("Unable to load help data");
		}
    
    	return result;
    }
    
	 public static String renderLevelGroup(Collection topics) {
		String retVal="";
		Iterator iter = topics.iterator();
		while (iter.hasNext()) {
			HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
			HelpTopic theme = (HelpTopic) item.getMember();
			retVal += "<option value="+theme.getHelpTopicId()+">"+theme.getTitleTrnKey()+"</option>\n";
			if (item.getChildren() != null || item.getChildren().size() > 0) {
				retVal += renderLevelGroup(item.getChildren());
			}
		}
		return retVal;
	}
	
	 public static String renderTopicsTree(Collection topics,HttpServletRequest request) {
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		String retVal = "";
		Iterator iter = topics.iterator();
                String instanceName=RequestUtils.getModuleInstance(request).getInstanceName();
		while (iter.hasNext()) {
			HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
			HelpTopic topic = (HelpTopic) item.getMember();
					// visible div start
			retVal += " <div>";
			if(item.getChildren().isEmpty()){
				retVal += "<img src=\"../ampTemplate/images/tree_minus.gif\";\">\n";
			}else{
			retVal += "<img id=\"img_" + topic.getHelpTopicId()+ "\" onclick=\"expandProgram(" +topic.getHelpTopicId()+ ")\"  src=\"../ampTemplate/images/tree_plus.gif\"/>\n";
			}
			retVal += "<img id=\"imgh_"+ topic.getHelpTopicId()+ "\" onclick=\"collapseProgram(" +topic.getHelpTopicId()+ ")\"  src=\"../ampTemplate/images/tree_minus.gif\" style=\"display : none;\">\n";
			if(topic.getTitleTrnKey()!=null && topic.getTopicKey()!=null){
			retVal += "<a href=\"../../help/"+instanceName+"/helpActions.do?actionType=viewSelectedHelpTopic&topicKey="+topic.getTopicKey()+"\">"+getTrn(topic.getTopicKey(), request)+"</a>";
			}
			retVal += "</div>\n";
			// hidden div start
			retVal += "<div id=\"div_theme_"+ topic.getHelpTopicId()+ "\" style=\"display:none;padding:4px;\">\n";
			if (item.getChildren() != null || item.getChildren().size() > 0) {
				retVal += renderTopicsTree(item.getChildren(),request);
			}
			retVal += "</div>\n";
		}
		return retVal;
	}
	 
	 public static String renderTopicTree(Collection topics,HttpServletRequest request,boolean child){
		 String xml="";
		 Iterator iter = topics.iterator();
		 String instanceName=RequestUtils.getModuleInstance(request).getInstanceName();
		 while (iter.hasNext()) {
			 HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
			 HelpTopic topic = (HelpTopic) item.getMember();
		
			 if(topic.getTopicKey().length() != 0 ){
				   
				    String article = getTrn(topic.getTopicKey(), request);
					String newCode = article.replaceAll("&","&amp;");
		
					if(item.getChildren().isEmpty()){	
					
					xml+= "<item text=\""+newCode+"\" id=\"" + topic.getHelpTopicId()+"\"/>";
				}else{
					xml+= "<item text=\""+newCode+"\" id=\"" + topic.getHelpTopicId()+"\">";
					 if (!item.getChildren().isEmpty() || item.getChildren().size() > 0) {
						 xml += renderTopicTree(item.getChildren(),request,true);
					 }
					xml+= "</item>";
				} 
			 }
	}
		 return xml;
	 }
	 
	 
	 public static String renderSelectTopicTree(Collection topics,String helpType,HttpServletRequest request) {
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
			String retVal = "";
			Iterator iter = topics.iterator();
	                String instanceName=RequestUtils.getModuleInstance(request).getInstanceName();
			while (iter.hasNext()) {
				HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
				HelpTopic topic = (HelpTopic) item.getMember();
						// visible div start
				retVal += " <div onmouseover=\"this.className='silverThing'\" onmouseout=\"this.className='whiteThing'\">";
				retVal += "<table width=\"100%\"  border=\"1\" style=\"border-collapse: collapse;border-color: #ffffff\">";
				retVal += "<tr>";
				retVal += "<td>";
				if(item.getChildren().isEmpty()){
					retVal += "<img src=\"../ampTemplate/images/tree_minus.gif\";\">\n";
				}else{
					retVal += "<img id=\"img_" + topic.getHelpTopicId()+ "\" onclick=\"expandProgram(" +topic.getHelpTopicId()+ ")\"  src=\"../ampTemplate/images/tree_plus.gif\"/>\n";
				}
				retVal += "<img id=\"imgh_"+ topic.getHelpTopicId()+ "\" onclick=\"collapseProgram(" +topic.getHelpTopicId()+ ")\"  src=\"../ampTemplate/images/tree_minus.gif\" style=\"display : none;\">\n";
				if(topic.getTitleTrnKey()!=null && topic.getTopicKey()!=null){
					retVal += "<a href=\"javascript:editTopic('"+ topic.getTopicKey()+ "','"+helpType+"')\">"+getTrn(topic.getTopicKey(), request)+"</a>";
				}
				retVal += "   </td>";
				//checkbox
				retVal+="   <td width=\"12\">";
				retVal+="<input type=\"checkbox\" value=\" "+topic.getHelpTopicId()+"\" ";				
				if(topic.getParent()!=null){
						retVal+=" id=\"checkbox_"+topic.getParent().getHelpTopicId()+"_"+topic.getHelpTopicId()+ "\" ";
				}else{
						retVal+=" id=\"checkbox_0_"+topic.getHelpTopicId()+ "\" ";
				}
				//if(!item.getChildren().isEmpty()){					
					String secondParameter="checkbox_0_"; //used to get helpTopic state (is it checked or not)
					if(item.getparent()!=null){
						secondParameter="checkbox_"+item.getparent().getHelpTopicId()+"_";
					}
					retVal+=" onchange=\"javascript:changeRelatedCheckboxesState('"+topic.getHelpTopicId()+"','"+secondParameter+"')\" ";
				//}
				retVal+="/>";
				retVal+="   </td>";
				//delete link
				retVal += "   <td width=\"12\">";
				if(helpType != "admin"){
					retVal += "<a href=\"/help/helpActions.do~actionType=deleteHelpTopics~multi=false~topicKey="+topic.getTopicKey()+"~page=admin\" onclick=\"return deleteProgram()\"><img src=\"../ampTemplate/images/trash_12.gif\" border=\"0\"></a>";
				}else{
					retVal += "<a href=\"/help~admin/helpActions.do~actionType=deleteHelpTopics~multi=false~topicKey="+topic.getTopicKey()+"~page=admin\" onclick=\"return deleteProgram()\"><img src=\"../ampTemplate/images/trash_12.gif\" border=\"0\"></a>";
				}
				retVal += "   </td>";
				retVal += " </tr></table>";
				retVal += "</div>\n";
				// hidden div start
				retVal += "<div id=\"div_theme_"+ topic.getHelpTopicId()+ "\" style=\"display:none;padding:4px;\">\n";
				if (item.getChildren() != null || item.getChildren().size() > 0) {
					retVal += renderSelectTopicTree(item.getChildren(),helpType,request);
				}
				retVal += "</div>\n";
			}
			return retVal;
		}
	
	 public static String getTrn(String defResult, HttpServletRequest request){
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //return CategoryManagerUtil.translate(key, request, defResult);
		String	lange	= RequestUtils.getNavigationLanguage(request).getCode();
        //Locale  lange    = RequestUtils.getNavigationLanguage(request);
        Long	siteId	= RequestUtils.getSite(request).getId();
		
		Message m = null;
         
		try {
            m = TranslatorWorker.getInstance("").getByBody(defResult, lange, siteId.toString());
            //m = DbUtil.getMessage(key.toLowerCase(), lang, siteId);
		} catch (WorkerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (m == null)
		 {
			 return defResult;
		 }
		 else
		 {
			 return m.getMessage();
		 }
		 
	 }
    public static String getTrn(String defResult,String	lange, Long	siteId){
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //return CategoryManagerUtil.translate(key, request, defResult);
		//String	lange	= RequestUtils.getNavigationLanguage(request).getCode();
        //Locale  lange    = RequestUtils.getNavigationLanguage(request);
        //Long	siteId	= RequestUtils.getSite(request).getId();

		Message m = null;

		try {
            m = TranslatorWorker.getInstance("").getByBody(defResult, lange, siteId.toString());
            //m = DbUtil.getMessage(key.toLowerCase(), lang, siteId);
		} catch (WorkerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (m == null)
		 {
			 return defResult;
		 }
		 else
		 {
			 return m.getMessage();
		 }

	 }

     public static List<HelpTopic> getAllHelpTopics() throws Exception{
		 Session session = null;
		 Query query;
		 String qryStr;
		 List<HelpTopic> topics=null;
		 try {
			 session=PersistenceManager.getRequestDBSession();
			 qryStr="select t from "+HelpTopic.class.getName()+" t";
			 query=session.createQuery(qryStr);
			 topics=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return topics;
	 }

	 public static Vector getAllHelpdataForExport(String lang) {
        System.out.println("Start");
		Vector vector = new Vector(); 
		Session session = null;
		Query query = null;
		ObjectFactory objFactory = new ObjectFactory();
		
		
		try {
			session = PersistenceManager.getRequestDBSession();
			
		 String queryString = "select topic from "+ HelpTopic.class.getName() + " topic where (topic.bodyEditKey like 'help%')";
   	       query = session.createQuery(queryString);
		   
		    Iterator itr = query.list().iterator();
			HelpSearchData helpsearch;
			  System.out.println("itr:"+itr);
			while (itr.hasNext()) {
				AmpHelpType helpout=objFactory.createAmpHelpType();
				
				HelpTopic item = (HelpTopic) itr.next();
				helpout.setTitleTrnKey(item.getTitleTrnKey());
				helpout.setTopicKey(item.getTopicKey());
				helpout.setKeywordsTrnKey(item.getKeywordsTrnKey());
				helpout.setTopicId(item.getHelpTopicId());
				helpout.setModuleInstance(item.getModuleInstance());
				helpout.setEditorKey(item.getBodyEditKey());
				if(item.getParent()!= null){
					helpout.setParentId(item.getParent().getHelpTopicId());
				}else{
					helpout.setParentId(0);
				}

                  List <String> allLang = TranslatorWorker.getAllUsedLanguages();

                        Iterator iterato = allLang.iterator();
                             while (iterato.hasNext()){
                             String lan = (String) iterato.next();


                            if(item.getBodyEditKey() != null){

                                List <Editor> Edit = getEditor(item.getBodyEditKey(),lan);
				                
				                    if(!Edit.isEmpty()){

                                        Iterator iter = Edit.iterator();
					                    while (iter.hasNext()) {
                                            Editor help = (Editor) iter.next();

                                            HelpLang helplang = new HelpLang();
                                            helplang.setBody(help.getBody());
                                            helplang.setTitle(getTrn(item.getTopicKey(),lan,new Long(3)));
                                            helplang.setCode(lan);

                                            helpout.getLang().add(helplang);

                                            Calendar cal_u = Calendar.getInstance();
                                            cal_u.setTime(help.getLastModDate());
                                            helpout.setLastModDate(cal_u);
                                      }

			   	            }
				        	
			        }

             }

              vector.add(helpout);
         }	
		} catch (Exception e) {
			logger.error("Unable to load help data");
				
		}
		return vector;
	}
		
	 public static void updateNewEditHelpData(AmpHelpType help,HashMap<Long,HelpTopic> storeMap,Long siteId){
		Session session = null;
		Query query;
		String qryStr;
			
		try {
			
			session = PersistenceManager.getRequestDBSession();
			Transaction tx=session.beginTransaction();

			   
			String queryString = "select topic from "+ HelpTopic.class.getName() + " topic where (topic.bodyEditKey=:editorKey)";
	    	     query = session.createQuery(queryString);
	    	     query.setParameter("editorKey", help.getEditorKey()); 
			    
			   if(query.list().iterator().hasNext()){
				   Iterator itr = query.list().iterator();
			    while (itr.hasNext()) {
			    	 HelpTopic helptopic = (HelpTopic) itr.next();

			    	 helptopic.setSiteId("amp");
                     helptopic.setTopicKey(help.getTopicKey());
                     helptopic.setTitleTrnKey(help.getTitleTrnKey());
	    	    	 helptopic.setModuleInstance(help.getModuleInstance());
	    	    	 helptopic.setBodyEditKey(help.getEditorKey());

                     Iterator editr = help.getLang().listIterator();
                                         while(editr.hasNext()){
                                             HelpLang ll = (HelpLang) editr.next();

                                            Message newMsg = new Message();

                                                newMsg.setSiteId(siteId.toString());
                                                System.out.println("siteId:"+siteId);
                                                newMsg.setMessage(ll.getTitle());
                                                System.out.println("Message:"+ll.getTitle());
                                                        newMsg.setKey(TranslatorWorker.generateTrnKey(help.getTopicKey()));

                                                newMsg.setLocale(ll.getCode());

                                            TranslatorWorker.getInstance("").save(newMsg);

                                         udateEditpData(ll,help.getEditorKey(),help.getLastModDate());
                                         }
			       session.saveOrUpdate(helptopic);
		    }
		tx.commit();
			   }else{
				   HelpTopic helptopic = new HelpTopic(); 
				   helptopic.setTopicKey(help.getTopicKey());
				   helptopic.setSiteId("amp");
				   helptopic.setTitleTrnKey(help.getTitleTrnKey());
	    	    	 helptopic.setModuleInstance(help.getModuleInstance());
	    	    	 helptopic.setBodyEditKey(help.getEditorKey());
	    	    	 helptopic.setKeywordsTrnKey(help.getKeywordsTrnKey());
	    	    	 HelpTopic top = storeMap.get(help.getParentId());
	    	    	 if(top != null){
	    	    		 
	    	    		 helptopic.setParent(top);
	    	    	 }
                     Iterator editr = help.getLang().listIterator();
                                         while(editr.hasNext()){
                                             HelpLang ll = (HelpLang) editr.next();

                                            Message newMsg = new Message();

                                                newMsg.setSiteId(siteId.toString());
                                                System.out.println("siteId:"+siteId);
                                                newMsg.setMessage(ll.getTitle());
                                                System.out.println("Message:"+ll.getTitle());
                                                        newMsg.setKey(TranslatorWorker.generateTrnKey(help.getTopicKey()));
                                                newMsg.setLocale(ll.getCode());
                                             
                                            TranslatorWorker.getInstance("").save(newMsg);

                                         udateEditpData(ll,help.getEditorKey(),help.getLastModDate());
                                         }
                 	 insertHelp(helptopic);
	    	    	 HelpTopic parent = new HelpTopic();
	    	    	 parent.setBodyEditKey(helptopic.getBodyEditKey());
	    	    	 parent.setHelpTopicId(helptopic.getHelpTopicId());
	    	    	 parent.setKeywordsTrnKey(helptopic.getKeywordsTrnKey());
	    	    	 parent.setModuleInstance(helptopic.getModuleInstance());
	    	    	 parent.setSiteId(helptopic.getSiteId());
	    	    	 parent.setParent(helptopic.getParent());
	    	    	 parent.setTitleTrnKey(helptopic.getTitleTrnKey());
	    	    	 parent.setTopicKey(helptopic.getTopicKey());
	    	    		 
	    	    	 Long oldid = help.getTopicId();
	    	    	 storeMap.put(oldid, parent);
	    	    	 
			   }
			
		} catch (Exception e) {
			logger.error("Unable to Update help data"+e.getMessage());
		}
		 
	 }
	 
	 public static void udateEditpData(HelpLang help,String key,Calendar lastModDate){
 	   List<HelpTopic> result = null;
	   Session session = null;
	   Query query = null;
	    
	    	try {
	    	session = PersistenceManager.getRequestDBSession();
	    	Transaction tx=session.beginTransaction();
			String queryString = "select editTopic from "+ Editor.class.getName() + " editTopic where (editTopic.editorKey=:key) and  (editTopic.language=:lang)";
   	        query = session.createQuery(queryString);
		    query.setParameter("lang", help.getCode());
		    query.setParameter("key", key);

		    
	    	     
	    	    if(query.list().iterator().hasNext()){
	    	     Iterator itr = query.list().iterator();
	    	     while (itr.hasNext()) {
				       Editor edit = (Editor) itr.next();
				       
				       edit.setBody(help.getBody());
				       edit.setLastModDate(lastModDate.getTime());
				       edit.setSiteId("amp");
				       edit.setLanguage(help.getCode());
	    	    	   session.saveOrUpdate(edit);
	    	     }
	    	     tx.commit();
	    	   }else if(help != null) {
	  
	    		   Editor edit = new  Editor();
			       edit.setBody(help.getBody());
			       edit.setLastModDate(lastModDate.getTime());
			       edit.setSiteId("amp");
			       edit.setLanguage(help.getCode());
			       edit.setEditorKey(key);
			       insertEdit(edit);
	    	   }
	     	}catch (Exception e) {
		
	     		logger.error("Unable to Update Editor data"+e.getMessage());
		 }
	 }
	 
	 private static void insertEdit(Object o)
		{
			Session session = null;
			Editor editor=(Editor)o;
			try{
					session	= PersistenceManager.getSession();
					Transaction tx=session.beginTransaction();
					session.save(editor);
					tx.commit();
			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
			} 
			finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}
		}
	 
	private static void insertHelp(Object o)
		{
			Session session = null;
			HelpTopic help =(HelpTopic)o;
			try{
					session	= PersistenceManager.getSession();
					Transaction tx=session.beginTransaction();
					session.save(help);
					tx.commit();
			}
			catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
				ex.printStackTrace(System.out);
			} 
			finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}
		}
	
	private String[] getChildTopicsIds(Collection <HelpTopicsTreeItem> items){
		String[] retValue=new String[items.size()];
		int i=0;
		for (HelpTopicsTreeItem helpTopicsTreeItem : items) {
			HelpTopic ht=(HelpTopic)helpTopicsTreeItem.getMember();
			retValue[i]=ht.getHelpTopicId().toString();
			i++;
		}
		return retValue;
	}
	 
}
