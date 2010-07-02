package org.digijava.module.help.util;

import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.help.dbentity.HelpTopic;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Utility method fro glossary
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class GlossaryUtil {

	public static final Integer TYPE_GLOSSARY = new Integer(2);

	/**
	 * Loads all glossary topics from DB.
	 * Both parameters are used to filter results and can be null, 
	 * but be careful with Admin and User help instances.
	 * Glossary topics are with topicType=TYPE_GLOSSARY
	 * @param moduleInstance can be null, but use correct value to not mess with admin and user helps.
	 * @param siteId can be null.
	 * @return list of help topics with topicType=TYPE_GLOSSARY
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<HelpTopic> getAllGlosaryTopics(String moduleInstance, String siteId) throws DgException{
		List<HelpTopic> result = null;
		String oql = " from " + HelpTopic.class.getName()+ " as ht where ht.topicType=:GLOSS_TYPE";
		if (moduleInstance != null){
			oql += " and ht.moduleInstance=:MOD_INST";
		}
		if (siteId != null){
			oql += " and ht.siteId = :SITE_ID";
		}
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		if (moduleInstance != null){
			query.setString("MOD_INST", moduleInstance);
		}
		if (siteId != null){
			query.setString("SITE_ID", siteId);
		}
		result = query.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public static List<HelpTopic> searchGlossary(String keyWord, String moduleInstance, String siteId) throws DgException{
		
		List<HelpTopic> result = null;
		String oql = "select ht from "; 
		oql += HelpTopic.class.getName()+ " as ht, ";
		oql += Editor.class.getName()+" as e ";
		oql += " where ht.topicType=:GLOSS_TYPE";
		oql += " and ht.bodyEditKey = e.editorKey";
		oql += " and (ht.topicKey like :KEY_WORD";
		oql += " or e.body like :KEY_WORD)";
		if (moduleInstance != null){
			oql += " and ht.moduleInstance=:MOD_INST";
		}
		if (siteId != null){
			oql += " and ht.siteId = :SITE_ID";
		}
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		query.setString("KEY_WORD", "%"+keyWord+"%");
		if (moduleInstance != null){
			query.setString("MOD_INST", moduleInstance);
		}
		if (siteId != null){
			query.setString("SITE_ID", siteId);
		}
		result = query.list();
		return result;
	}
	
	/**
	 * Loads glossary topic object by ID.
	 * @param id pk in db.
	 * @return glossary topic bean
	 * @throws DgException
	 */
	public static HelpTopic getGlosaryTopic(Long id) throws DgException{
		String oql = " from " + HelpTopic.class.getName()+ " as ht where ht.topicType=:GLOSS_TYPE and ht.helpTopicId=:TOPIC_ID";
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery(oql);
		query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
		query.setLong("TOPIC_ID",id);
		HelpTopic result = (HelpTopic) query.uniqueResult();
		return result;
	}
	
	public static void createOrUpdateGlossaryTopic(HelpTopic topic) throws DgException{
		topic.setTopicType(TYPE_GLOSSARY);
		HelpUtil.saveOrUpdateHelpTopic(topic);
	}
	
	public static void deleteGlossaryTopic(HelpTopic topic) throws DgException{
		HelpUtil.deleteHelpTopic(topic);
	}
}
