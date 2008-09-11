package org.digijava.module.help.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.TreeItem;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.form.HelpForm;
import org.digijava.module.help.helper.HelpTopicsTreeItem;
import org.digijava.module.translation.util.DbUtil;

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
	public static List<HelpTopic> getFirstLevelTopics(String siteId,
			String moduleInstance,String key)throws AimException{
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
	
	public static boolean hasChildren(String siteId,
			String moduleInstance,Long parentId)throws AimException{
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
		if (helpTopics==null || helpTopics.size()==0){
			return false;
		}
	return true;	
	}
	
    public static List<Editor> getAllHelpData() throws 
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
			retVal += "<a href=\"../../help/"+instanceName+"/helpActions.do?actionType=viewSelectedHelpTopic&topicKey="+topic.getTopicKey()+"\">"+getTrn(topic.getTitleTrnKey(),topic.getTopicKey(), request)+"</a>";
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
	 public static String getTrn(String key, String defResult, HttpServletRequest request){
		 //CategoryManagerUtil cat = new CategoryManagerUtil();
		 //return CategoryManagerUtil.translate(key, request, defResult);
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		Long	siteId	= RequestUtils.getSite(request).getId();
		
		Message m = null;
		
		try {
			m = DbUtil.getMessage(key.toLowerCase(), lang, siteId);
		} catch (DgException e) {
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
}
