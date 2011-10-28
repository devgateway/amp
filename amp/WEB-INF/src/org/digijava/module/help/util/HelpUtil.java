package org.digijava.module.help.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.kernel.lucene.LuceneWorker;
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
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.helper.HelpTopicHelper;
import org.digijava.module.help.helper.HelpTopicsTreeItem;
import org.digijava.module.help.jaxbi.AmpHelpType;
import org.digijava.module.help.jaxbi.HelpLang;
import org.digijava.module.help.jaxbi.ObjectFactory;
import org.digijava.module.help.lucene.LucHelpModule;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	public static Collection<HelpTopicsTreeItem> getHelpTopicsTree(String siteId,
			String moduleInstance) throws AimException {
		List<HelpTopic> helpTopics= getHelpTopics(siteId, moduleInstance,null, null);
		Collection<HelpTopicsTreeItem> themeTree = CollectionUtils.getHierarchy(
				helpTopics,
                new HelpTopicHierarchyDefinition(), 
                new HelpTopicTreeItemFactory());
		return themeTree;
	}
	
	public static Collection<HelpTopicsTreeItem> getGlossaryTopicsTree(String siteId,
			String moduleInstance) throws DgException {
		List<HelpTopic> glossaryTopics= GlossaryUtil.getAllGlosaryTopics(moduleInstance, siteId);
		Collection<HelpTopicsTreeItem> themeTree = CollectionUtils.getHierarchy(
				glossaryTopics,
                new HelpTopicHierarchyDefinition(), 
                new HelpTopicTreeItemFactory());
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
						+ " topics where (topics.siteId=:siteId) and (topics.moduleInstance=:moduleInstance) and (topics.topicType=NULL)";
				query = session.createQuery(queryString);
			} else {
				String queryString = "select distinct t from "
						+ HelpTopic.class.getName()+ " t, "+ Message.class.getName()+ " msg "
						+"where (t.titleTrnKey=msg.key or t.keywordsTrnKey=msg.key) "
						+ "and t.siteId=:siteId  and t.topicType=NULL and t.moduleInstance=:moduleInstance "
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

    public static HelpTopic loadhelpTopic(String topicKey) throws Exception{
		Session session = null;
		Query query = null;
		HelpTopic helpTopic = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString="from "+ HelpTopic.class.getName()+" topic where topic.topicKey="+topicKey;
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
    
    public static HelpTopic getHelpTopicByBodyEditKey(String bodyEditKey,String siteId,String moduleInstance) throws AimException {
		Session session = null;
		Query query = null;
		HelpTopic helpTopic = null;
		if (bodyEditKey != null && !bodyEditKey.equals("")) {
			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString="from "+ HelpTopic.class.getName()+" topic where (topic.bodyEditKey=:bodyEditKey) " +
						" and (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) ";
				query=session.createQuery(queryString);
				query.setParameter("siteId", siteId);
				query.setParameter("moduleInstance", moduleInstance);
				query.setParameter("bodyEditKey", bodyEditKey);
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
    
    public static HelpTopic getHelpTopic(String key) throws AimException {
		Session session = null;
		Query query = null;
		HelpTopic helpTopic = null;
		if (key != null && !key.equals("")) {
			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString="from "+ HelpTopic.class.getName()+" topic where (topic.bodyEditKey=:key) ";
				query=session.createQuery(queryString);
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

	public static void saveOrUpdateHelpTopic(HelpTopic topic, HttpServletRequest request) throws AimException{
		Session session = null;
		Transaction tx = null;
		boolean update = topic.getHelpTopicId() != null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(topic);
			tx.commit();
			if (topic.getTopicType()!=GlossaryUtil.TYPE_GLOSSARY){
				//skip lucene work for glossary topics.
				saveOrUpdateFromLucene(topic, request, update);
			}
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback topic update", ex);
				}
			}
			throw new AimException("Can't update help topic", e);
		}
	}
	
	public static void deleteHelpTopic(HelpTopic topic, HttpServletRequest request) throws AimException{
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			if (topic.getBodyEditKey()!=null && topic.getSiteId()!=null){
				List<Editor> editors = DbUtil.getEditorList(topic.getBodyEditKey(), topic.getSiteId());
				if (editors!=null && editors.size()>0){
					for (Editor editor : editors) {
						session.delete(editor);
					}
				}
			}
			session.delete(topic);
			tx.commit();
			if (topic.getTopicType()!=GlossaryUtil.TYPE_GLOSSARY){
				//skip lucene work for glossary topics.
				removeFromLucene(topic, request);
			}
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
	
	public static void saveOrUpdateFromLucene(HelpTopic topic, HttpServletRequest request, boolean update) throws DgException{
		String moduleInstanceName = RequestUtils.getRealModuleInstance(request).getInstanceName();
		ServletContext context = request.getSession().getServletContext();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		String title = null;
		try {
			title = TranslatorWorker.translateText(topic.getTopicKey(), request);
		} catch (WorkerException ex) {
			logger.error(ex);
		}
		HelpTopicHelper item = new HelpTopicHelper(topic, title, locale);
		String suffix = moduleInstanceName + "_" + locale;
		String msg = "New help topic added to lucene index";
		if (update){
			LuceneWorker.deleteItemFromIndex(item, context, suffix);
			msg = "Existing help topic updated in lucene index";
		}
		LuceneWorker.addItemToIndex(item, context, suffix);
		logger.debug(msg);
	}

	public static void removeFromLucene(HelpTopic topic, HttpServletRequest request) throws DgException{
		String moduleInstanceName = RequestUtils.getRealModuleInstance(request).getInstanceName();
		ServletContext context = request.getSession().getServletContext();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
		HelpTopicHelper item = new HelpTopicHelper(topic);
		String suffix = moduleInstanceName + "_" + locale;
		LuceneWorker.deleteItemFromIndex(item, context, suffix);
		logger.debug("Help topic removed from lucene index");
	}
	
	public static List<HelpTopic> getFirstLevelTopics(String siteId,String moduleInstance,String key)throws AimException{
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()
			+ " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent is null) and (topic.topicType=NULL)";
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
			queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.parent is null and topic.topicType=NULL";
			query = session.createQuery(queryString);
			query.setParameter("siteId", siteId);
			helpTopics = query.list();
		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}
		return helpTopics;
	}

    public static List<HelpTopic> getFirstLevelTopics(String siteId,String moduleInstance) throws Exception{
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.moduleInstance=:moduleInstance and topic.parent is null and topic.topicType=NULL";
			query = session.createQuery(queryString);
			query.setParameter("siteId", siteId);
            query.setParameter("moduleInstance", moduleInstance);
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
			+ " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent.helpTopicId=:id) and (topic.topicType=NULL)";
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

    public static List<HelpTopic> getChildTopics(String siteId,Long parentId) throws AimException {
		Session session = null;
		Query query = null;
		List<HelpTopic> helpTopics = null;
		String queryString=null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "from "+ HelpTopic.class.getName()
			+ " topic where (topic.siteId=:siteId) and (topic.parent.helpTopicId=:id) ";
			query = session.createQuery(queryString);
			query.setParameter("siteId", siteId);
        	query.setParameter("id", parentId);
			helpTopics = query.list();

		} catch (Exception e) {
			logger.error("Unable to load help topics");
  			throw new AimException("Unable to Load Help Topics", e);
		}
		return helpTopics;
	}

    public static List<Editor> getAllHelpKey(String lang) throws
    EditorException {
System.out.println("lang:"+lang);
    	
	Session session = null;
	List<Editor> helpTopics = new ArrayList<Editor>();
	
	try {
		session = PersistenceManager.getRequestDBSession();
		 Query q = session.createQuery(" from e in class " +
                 Editor.class.getName() +" where e.editorKey like 'help%' and e.language=:lang order by e.lastModDate");
			q.setParameter("lang", lang);
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
	HelpSearchData helpsearch;
	
	
	try {
		session = PersistenceManager.getRequestDBSession();
		String queryString = "select e from "
			+ HelpTopic.class.getName()+ " t, "+ Editor.class.getName()+ " e "
			+"where (t.bodyEditKey=e.editorKey) and (t.topicType=NULL) order by e.lastModDate";
		 query = session.createQuery(queryString);

		Iterator itr = query.list().iterator();
		while (itr.hasNext()) {
			helpsearch = new HelpSearchData();
			  Editor edt = (Editor) itr.next();
			
			   //System.out.println("body:"+edt.getBody());
			
				helpsearch.setBody(edt.getBody());
				helpsearch.setLastModDate(edt.getLastModDate());
				helpsearch.setTitleTrnKey(getHelpTopic(edt.getEditorKey()).getTitleTrnKey());
				helpsearch.setTopicKey(getHelpTopic(edt.getEditorKey()).getTopicKey());
				helpsearch.setLang(edt.getLanguage());
				helpsearch.setBodyKey(edt.getEditorKey());
		
				helpTopics.add(helpsearch);	  
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
		 return renderLeveledItems(topics, "");
	 }
	 
	 public static String renderLeveledItems(Collection topics,String prefix) {
			String retVal="";
			if (topics != null && topics.size()>0){
				Iterator iter = topics.iterator();
				while (iter.hasNext()) {
					HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
					HelpTopic theme = (HelpTopic) item.getMember();
					retVal += "<option value="+theme.getHelpTopicId()+">"+ prefix + theme.getTopicKey()+"</option>\n";
					if (item.getChildren() != null || item.getChildren().size() > 0) {
						retVal += renderLeveledItems(item.getChildren(),prefix+"&nbsp;&nbsp;");
					}
				}
			}
			return retVal;
		}
	
	 public static String renderTopicsTree(Collection topics,HttpServletRequest request) throws Exception{
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
	 
	 public static String renderTopicTree(Collection topics,HttpServletRequest request,boolean child) throws Exception{
		 String xml="";
		 Iterator iter = topics.iterator();
		 while (iter.hasNext()) {
			 HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
			 HelpTopic topic = (HelpTopic) item.getMember();
		
			 if(topic.getTopicKey() != null && topic.getTopicKey().length() != 0 ){
				   
				    String article = getTrn(topic.getTopicKey(), request);
				    String newCode = HTMLEntityEncode(article);
					if(item.getChildren().isEmpty()){	
					
					xml+= "<item text=\""+newCode+"\" id=\""+ topic.getHelpTopicId()+"\"/>";
				}else{
					xml+= "<item  text=\""+newCode+"\" id=\"" +topic.getHelpTopicId()+"\">";
                        System.out.println("name:"+newCode+" Topic_PRNT:"+topic.getHelpTopicId());
                     if (!item.getChildren().isEmpty() || item.getChildren().size() > 0) {
						 xml += renderTopicTree(item.getChildren(),request,true);
					 }
					xml+= "</item>";
				} 
			 }
	      }
		 return xml;
	 }


	public static String renderSelectTopicTree(Collection topics,String helpType,HttpServletRequest request) throws Exception{
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
				if(topic.getTopicKey()!=null){
					//retVal += "<a href=\"javascript:editTopic('"+ topic.getTopicKey()+ "','"+helpType+"')\">"+getTrn(topic.getTopicKey(), request)+"</a>";
					retVal += "<a>"+getTrn(topic.getTopicKey(), request)+"</a>";
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
					if(item.getParent()!=null){
						secondParameter="checkbox_"+item.getParent().getHelpTopicId()+"_";
					}
					retVal+=" onchange=\"javascript:changeRelatedCheckboxesState('"+topic.getHelpTopicId()+"','"+secondParameter+"')\" ";
				//}
				retVal+="/>";
				retVal+="   </td>";
				//delete link
				retVal += "   <td width=\"12\">";
				if(helpType != "admin"){
					retVal += "<a href=\"/help/helpActions.do~actionType=deleteHelpTopics~multi=false~topicKey="+topic.getTopicKey()+"~helpTopicId="+topic.getHelpTopicId()+"~page=admin\" onclick=\"return deleteProgram()\"><img src=\"../ampTemplate/images/trash_12.gif\" border=\"0\"></a>";
				}else{
					retVal += "<a href=\"/help~admin/helpActions.do~actionType=deleteHelpTopics~multi=false~topicKey="+topic.getTopicKey()+"~helpTopicId="+topic.getHelpTopicId()+"~page=admin\" onclick=\"return deleteProgram()\"><img src=\"../ampTemplate/images/trash_12.gif\" border=\"0\"></a>";
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
        try {
			return TranslatorWorker.translateText(defResult, request);
		} catch (WorkerException e) {
			throw new RuntimeException("Cannot translate text"+defResult, e);
		}
	 }
	 
    public static String getTrn(String defResult,String	lange, Long	siteId){
    	try {
			return TranslatorWorker.translateText(defResult, lange, siteId.toString());
		} catch (WorkerException e) {
			throw new RuntimeException("Cannot translate text"+defResult, e);
		}
	 }

     public static List<HelpTopic> getAllHelpTopics() throws Exception{
		 Session session = null;
		 Query query;
		 String qryStr;
		 List<HelpTopic> topics=null;
		 try {
			 session=PersistenceManager.getRequestDBSession();
			 qryStr="select t from "+HelpTopic.class.getName()+" t where t.topicType=NULL";
			 query=session.createQuery(qryStr);
			 topics=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return topics;
	 }

	 public static Vector getAllHelpdataForExport() {
		 //WTH is Vector ?!
         logger.info("Starting helpExport");

		Vector vector = new Vector(); 
		Session session = null;
		Query query = null;
		ObjectFactory objFactory = new ObjectFactory();
		
		
		try {
			session = PersistenceManager.getRequestDBSession();
//			String queryString = "select topic from "+ HelpTopic.class.getName() + " topic where (topic.bodyEditKey like 'help%')";
			String queryString = "from "+ HelpTopic.class.getName();
			
            query = session.createQuery(queryString);

		    Iterator itr = query.list().iterator();

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
					helpout.setParentId(new Long(0));
				}
				helpout.setTopicType(item.getTopicType());

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

			   	            }else{

                                            HelpLang helplang = new HelpLang();
                                            helplang.setBody("");
                                            helplang.setTitle(getTrn(item.getTopicKey(),lan,new Long(3)));
                                            helplang.setCode(lan);

                                            helpout.getLang().add(helplang);

                                            Calendar cal_u = Calendar.getInstance();
                                            cal_u.setTime(new Date());
                                            helpout.setLastModDate(cal_u);


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
		
			
		try {
		
				   HelpTopic helptopic = new HelpTopic(); 
				   helptopic.setTopicKey(help.getTopicKey());
				   helptopic.setSiteId("amp");
				   helptopic.setTitleTrnKey(help.getTitleTrnKey());
	    	    	 helptopic.setModuleInstance(help.getModuleInstance());
	    	    	 helptopic.setBodyEditKey(help.getEditorKey());
	    	    	 helptopic.setKeywordsTrnKey(help.getKeywordsTrnKey());
	    	    	 helptopic.setTopicType(help.getTopicType());
	    	    	 HelpTopic top = storeMap.get(help.getParentId());
	    	    	 if(top != null){
	    	    		 
	    	    		 helptopic.setParent(top);
	    	    	 }
                     Iterator editr = help.getLang().listIterator();
                                         while(editr.hasNext()){
                                             HelpLang xmlLangTag = (HelpLang) editr.next();

                                            Message newMsg = new Message();

                                                newMsg.setSiteId(siteId.toString());
                                                newMsg.setMessage(xmlLangTag.getTitle());
                                                newMsg.setKey(TranslatorWorker.generateTrnKey(help.getTopicKey()));
                                                newMsg.setLocale(xmlLangTag.getCode());
                                                
                                            
                                               // Message msg = TranslatorWorker.getInstance("").getByBody(xmlLangTag.getTitle().trim(), xmlLangTag.getCode(), siteId.toString());
                                                Message msg = TranslatorWorker.getInstance("").getByKey(newMsg.getKey(),xmlLangTag.getCode(), siteId.toString());
                                                if(msg != null){
                                                	TranslatorWorker.getInstance("").update(newMsg);
                                                }else{
                                                	 TranslatorWorker.getInstance("").save(newMsg);
                                                }
                                           
                                         
                                         udateEditpData(xmlLangTag,help.getEditorKey(),help.getLastModDate());
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
	    	    	 parent.setTopicType(helptopic.getTopicType());
	    	    		 
	    	    	 Long oldid = help.getTopicId();
	    	    	 storeMap.put(oldid, parent);
	    	    	 
			   
			
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


    public static void saveNewTreeState(HelpTopic help,HashMap<Long,HelpTopic> storeMap,String siteId){

         Thread th = new Thread();
      try{
                   HelpTopic helptopic = new HelpTopic();
                   helptopic.setTopicKey(help.getTopicKey());
				   helptopic.setSiteId("amp");
				   helptopic.setTitleTrnKey(help.getTitleTrnKey());
	    	       helptopic.setModuleInstance(help.getModuleInstance());
	    	       helptopic.setKeywordsTrnKey(help.getKeywordsTrnKey());
                   helptopic.setBodyEditKey(help.getBodyEditKey()); 

                      if(help.getParent() != null){
                        HelpTopic top = storeMap.get(help.getParent().getHelpTopicId());
                           if(top!=null){

                                     helptopic.setParent(top);
                                 }
                            }
                   
                     insertHelp(helptopic);

                     //TODO What's that?
                    th.sleep(500);
                 
                    HelpTopic newTopic = getHelpTopic(help.getTopicKey(),siteId,help.getModuleInstance());
        
                     HelpTopic parent = new HelpTopic();
	    	    	 parent.setBodyEditKey(newTopic.getBodyEditKey());
	    	    	 parent.setHelpTopicId(newTopic.getHelpTopicId());
	    	    	 parent.setKeywordsTrnKey(newTopic.getKeywordsTrnKey());
	    	    	 parent.setModuleInstance(newTopic.getModuleInstance());
	    	    	 parent.setSiteId(newTopic.getSiteId());
	    	    	 parent.setParent(newTopic.getParent());
	    	    	 parent.setTitleTrnKey(newTopic.getTitleTrnKey());
	    	    	 parent.setTopicKey(newTopic.getTopicKey());

                     Long oldid = help.getHelpTopicId();
                     storeMap.put(oldid, parent);
   
        } catch (Exception e) {
			logger.error("Unable to Save help data"+e.getMessage());
		}


    }
    public static String HTMLEntityEncode(String s) {
        StringBuffer buf = new StringBuffer();
        int len = (s == null ? -1 : s.length());

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
                buf.append(c);
            } else {
                buf.append("&#" + (int) c + ";");
            }
        }
        return buf.toString();
    }
    
    public  static String removeSpaces(String s) {
  	  StringTokenizer st = new StringTokenizer(s,"&#32;",false);
  	  String t="";
  	  while (st.hasMoreElements()) t += st.nextElement();
  	  return t;
  	  }

    /**
     * Returns list of required lucene modules for help.
     * Groups by module instance and language to separate search areas and indexes.
     * @return list of lucene modules for help
     */
    public static List<LucModule<?>> getLuceneModules(){
    	//search db
		List<String> rows = null;
		try {
			Session session = PersistenceManager.getRequestDBSession();
			//Group by module instances.
			//Instead of such grouping we may have hardcoded admin and default instances.
			String oql = "select h.moduleInstance ";
			oql += " from "+HelpTopic.class.getName() + " as h";
			oql += " group by h.moduleInstance";
			Query query = session.createQuery(oql);
			rows = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (DgException e) {
			e.printStackTrace();
		}finally{
			if (rows == null){
				//TODO bad recover, find other solution or throw exception and process outside.
				rows = new ArrayList<String>();
				rows.add("default");
				rows.add("admin");
			}
		}
    	//prepare results
    	List<LucModule<?>> results = new ArrayList<LucModule<?>>();
    	//get supported languages
    	EnumSet<LangSupport> languages = LangSupport.supported();
    	//for all module instances
    	for (String moduleInstance : rows) {
    		for (LangSupport lang : languages) {
    			//add for module instance + each supported languages
    			results.add(new LucHelpModule(moduleInstance,lang));
			}
    		//add for module instance + English and all unsupported languages
    		results.add(new LucHelpModule(moduleInstance));
		}
    	return results;
    }
    
    
    
    /**
     * Returns list of {@link HelpTopicHelper} beans for specified parameters. 
     * @param siteId can be null in which case it will not filter by siteId.
     * @param moduleInstance can be null in which case it will not filter by module instances
     * @param langs can be null in which case it will not filter by languages of help topic body
     * @param exclude can be null which means false. If true language match is inverted. 
     * @return list of help topic helpers {@link HelpTopicHelper}
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
	public static List<HelpTopicHelper> getHelpItems(String siteId,String moduleInstance, EnumSet<LangSupport> langs, boolean exclude) throws DgException{
    	
    	String oql = "select new org.digijava.module.help.helper.HelpTopicHelper(h.helpTopicId, h.topicKey, e.body, h.siteId, h.moduleInstance, e.language, h.titleTrnKey, h.bodyEditKey) "; 
    	oql += " from "+HelpTopic.class.getName()+" as h, "+Editor.class.getName()+" as e where ";
    	oql += " h.bodyEditKey = e.editorKey ";
    	oql += " and h.topicType is null";
    	if (siteId != null){
    		oql += " and (h.siteId = :siteID) ";
    	}
    	if (moduleInstance != null){
    		oql += " and (h.moduleInstance like :modInst) ";
    	}
    	if (langs != null){
    		if (exclude){
        		oql += " and (e.language not in (:langISOs)) ";
    		}else{
        		oql += " and (e.language in (:langISOs)) ";
    		}
    	}
    	
    	Session session = PersistenceManager.getRequestDBSession();
    	Query query = session.createQuery(oql);
    	
    	if (siteId != null){
    		query.setString("siteID", siteId);
    	}
    	if (moduleInstance != null){
    		query.setString("modInst", moduleInstance);
    	}
    	if (langs != null){
        	query.setParameterList("langISOs", LangSupport.toCodeList(langs));
    	}
    	
    	List<HelpTopicHelper> result = (List<HelpTopicHelper>) query.list();

    	return result;
    }
    

	/**
	 * Compares two {@link HelpTopicHelper} by its sort index field.
	 * this field is also used by lucene to set score values.
	 * @author Irakli Kobiashvili
	 *
	 */
    public static class HelpTopicHelperScoreComparator implements Comparator<HelpTopicHelper>{
		@Override
		public int compare(HelpTopicHelper o1, HelpTopicHelper o2) {
			Float f1 = o1.getSortIndex();
			Float f2 = o2.getSortIndex();
			if (f1!=null && f2 != null){
				return f2.compareTo(f1);
			}else if (f1 != null && f2 == null){
				return -11;
			} else if (f1 == null && f2 !=null){
				return 1;
			}
			return 0;
		}
    }

    /**
     * Resolves key of {@link HelpTopicHelper} bean.
     *
     */
    public static class HelpTopicHelperKeyResolver implements KeyResolver<Long, HelpTopicHelper>{
		@Override
		public Long resolveKey(HelpTopicHelper element) {
			return element.getId();
		}
    }
    
}
