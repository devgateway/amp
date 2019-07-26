package org.digijava.module.help.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Utility method for glossary
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class GlossaryUtil {

    private static Logger logger = Logger.getLogger(GlossaryUtil.class);

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
    public static List<HelpTopic> getAllGlosaryTopics(String moduleInstance, Site site) throws DgException{
        List<HelpTopic> result = null;
        String oql = " from " + HelpTopic.class.getName()+ " as ht where ht.topicType=:GLOSS_TYPE";
        if (moduleInstance != null){
            oql += " and ht.moduleInstance=:MOD_INST";
        }
        if (site != null){
            oql += " and ht.siteId = :SITE_ID";
        }
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery(oql);
        query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
        if (moduleInstance != null){
            query.setString("MOD_INST", moduleInstance);
        }
        if (site != null){
            query.setString("SITE_ID", site.getSiteId());
        }
        result = query.list();
        return result;
    }

    /**
     * Search glossary.
     * Tries to match keywords in title and in body.
     * @param keyWords list of strings that will be used in LIKE operation. can be null
     * @param moduleInstance help module instance name, can be null
     * @param siteId digi site ID- value of siteId field, can be null.
     * @param locale language code to search in
     * @return list of found glossary items.
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<HelpTopic> searchGlossary(List<String> keyWords, String moduleInstance, Site site, String locale) throws DgException{

        StringBuffer trnTitlesWhr = new StringBuffer();

        if (locale != null && !locale.isEmpty() && !locale.equalsIgnoreCase("en")) {
            List<Long> matchingTranslations = getMatchingTitles(keyWords, moduleInstance, site, locale);
            //Build translated titles whereclause
            if (matchingTranslations != null && !matchingTranslations.isEmpty()) {
                trnTitlesWhr.append(" or ht.helpTopicId in (");
                Iterator <Long> it = matchingTranslations.iterator();
                while (it.hasNext()) {
                    trnTitlesWhr.append(it.next());
                    if (it.hasNext()) {
                        trnTitlesWhr.append(",");
                    }
                }
                trnTitlesWhr.append(") ");
            }
        }




        List<HelpTopic> result = null;
        String oql = "select distinct ht from ";
        oql += HelpTopic.class.getName()+ " as ht, ";
        oql += Editor.class.getName()+" as e ";
        oql += " where ht.topicType=:GLOSS_TYPE";
        oql += " and ht.bodyEditKey = e.editorKey";
        if (locale!=null && locale.length()>0){
            oql += " and e.language = :LANG";
        }
        if (keyWords!=null && keyWords.size()>0){
            oql += " and ( ";
            int c = 0;
            for (int i=0;i<keyWords.size();i++){
                if (c>0) oql += " or ";
                oql += " lower(unaccent(ht.topicKey)) LIKE unaccent(:KEY_WORD_"+c + ")";
                oql += " or lower(unaccent(e.body)) LIKE unaccent(:KEY_WORD_"+c + ")";
                c++;
            }
            oql+=" )";
        }


        //Translated help topic search
        oql += trnTitlesWhr.toString();

        if (moduleInstance != null){
            oql += " and ht.moduleInstance=:MOD_INST";
        }
        if (site != null){
            oql += " and ht.siteId = :SITE_ID";
        }
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery(oql);
        query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
        if (locale!=null && locale.length()>0){
            query.setString("LANG", locale);
        }
        if (keyWords!=null && keyWords.size()>0){
            int c = 0;
            for (String kw : keyWords) {
                query.setString("KEY_WORD_"+c, "%"+kw.toLowerCase()+"%");
                c++;
            }
        }
        if (moduleInstance != null){
            query.setString("MOD_INST", moduleInstance);
        }
        if (site != null){
            query.setString("SITE_ID", site.getSiteId());
        }
        result = query.list();
        return result;
    }


    private static List<Long> getMatchingTitles (List<String> keyWords, String moduleInstance, Site site, String locale) throws DgException {
        List retVal = null;
        StringBuffer queryStr = new StringBuffer ("select distinct ht.helpTopicId from ");
        queryStr.append(Message.class.getName());
        queryStr.append(" as trn, ");
        queryStr.append(Message.class.getName());
        queryStr.append(" as eng, ");
        queryStr.append(HelpTopic.class.getName());
        queryStr.append(" as ht ");
        queryStr.append(" where trn.key = eng.key and ");
        queryStr.append(" ht.topicKey = eng.message");


        if (keyWords!=null && keyWords.size()>0){
            queryStr.append(" and (");
            int c = 0;
            for (int i=0;i<keyWords.size();i++){
                if (c>0) {
                    queryStr.append(" or ");
                }
                queryStr.append("lower(unaccent(trn.message)) like lower(unaccent(:KEY_WORD_" + c + "))");
                c++;
            }
            queryStr.append(" )");
        }

        if (moduleInstance != null){
            queryStr.append(" and ht.moduleInstance=:MOD_INST");
        }

        if (site != null){
            queryStr.append(" and trn.siteId = :SITE_ID");
            queryStr.append(" and eng.siteId = :SITE_ID");
            queryStr.append(" and ht.siteId = :SITE_ID");

        }
        if (locale!=null && locale.length()>0){
            queryStr.append(" and trn.locale = :LANG");
        }

        Session session = PersistenceManager.getRequestDBSession();
        /*Logger log = Logger.getLogger("org.hibernate.SQL");
        log.setLevel(Level.DEBUG);*/
        
        Query query = session.createQuery(queryStr.toString());



        if (keyWords!=null && keyWords.size()>0){
            int c = 0;
            for (String kw : keyWords) {
                query.setString("KEY_WORD_"+c, "%"+kw+"%");
                c++;
            }
        }

        if (moduleInstance != null){
            query.setString("MOD_INST", moduleInstance);
        }

        if (site != null){
            query.setString("SITE_ID", site.getSiteId());
            //query.setLong("SITE_ID_LONG", site.getId());
        }

        if (locale!=null && locale.length()>0){
            query.setString("LANG", locale);
        }
        retVal = query.list();

        //log.setLevel(Level.INFO);
        return retVal;
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
    
    /**
     * Creates new glossary topic.
     * @param topic
     * @throws DgException
     */
    public static void createOrUpdateGlossaryTopic(HelpTopic topic,HttpServletRequest request) throws DgException{
        topic.setTopicType(TYPE_GLOSSARY);
        HelpUtil.saveOrUpdateHelpTopic(topic, request);
    }
    
    /**
     * Deletes glossary topic.
     * Also does all necessary cleanup of editors and children.
     * @param glossaryTopic
     * @throws DgException
     */
    public static void deleteGlossaryTopic(HelpTopic glossaryTopic) throws DgException{
        Session dbSession = null;
//      Transaction tx = null;
        try {
            dbSession = PersistenceManager.getRequestDBSession();
//beginTransaction();                       //Start transaction
            deleteTopic(glossaryTopic, dbSession);                  //delete topic, its editor and children
            //tx.commit();                                          //commit changes
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DgException("Can't remove Glossary items", e);
        }
    }

    /**
     * Delete glossary, its editor, and its children recursively.
     * All deletes are done in one session for which client should start
     * transaction and later commit it or rollback.
     * @param glossaryTopic
     * @param dbSession
     * @throws DgException
     */
    private static void deleteTopic(HelpTopic glossaryTopic, Session dbSession) throws DgException{
        List<HelpTopic> children = getChildren(glossaryTopic, dbSession);
        if (children!=null && children.size()>0){
            for (HelpTopic child : children) {
                deleteTopic(child, dbSession);
            }
        }
        deleteEditor(glossaryTopic, dbSession);
        dbSession.delete(glossaryTopic);
    }
    
    /**
     * Removes all editor records for key stored in help topic.
     * @param topic
     * @param dbSession
     * @throws DgException
     */
    private static void deleteEditor(HelpTopic topic, Session dbSession) throws DgException{
        if (topic.getBodyEditKey()!=null && topic.getSiteId()!=null){
            List<Editor> editors = DbUtil.getEditorList(topic.getBodyEditKey(), SiteCache.lookupByName(topic.getSiteId()));
            if (editors!=null && editors.size()>0){
                for (Editor editor : editors) {
                    dbSession.delete(editor);
                }
            }
        }
    }
    
    /**
     * Retrieves direct children topics of specified parent topic.
     * @param topic paremt topic, should NOT be NULL
     * @param dbSession db session, should NOT be NULL
     * @return list of one level children topics or NULL
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    private static List<HelpTopic> getChildren(HelpTopic topic, Session dbSession) throws DgException{
        List<HelpTopic> helpTopics = null;
        try {
            String queryString = "from "+ HelpTopic.class.getName() + " topic where topic.parent.helpTopicId=:id";
            Query query = dbSession.createQuery(queryString);
            query.setLong("id", topic.getHelpTopicId());
            helpTopics = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AimException("Unable to load glossary children", e);
        }
        return helpTopics;
    }

    
    /**
     * /**
     * Retrieves direct children topics of specified parent topic.
     *
     * @param siteId , should NOT be NULL
     * @param moduleInstance, should NOT be NULL
     * @param parentId , should NOT be NULL
     * @return direct children topics
     * @throws DgException
     */
    public static List<HelpTopic> getChildTopics(Site site, String moduleInstance, Long parentId) throws DgException {
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "from "+ HelpTopic.class.getName()
                        + " topic where (topic.topicType=:GLOSS_TYPE) and (topic.siteId=:siteId) "
                        + " and (topic.moduleInstance=:moduleInstance) ";
            if (parentId == null) {
                queryString += "and topic.parent is null";
            } else {
                queryString += "and (topic.parent.helpTopicId=:id)";
            }
            query = session.createQuery(queryString);   
            query.setInteger("GLOSS_TYPE",TYPE_GLOSSARY);
            query.setString("siteId", site.getSiteId());
            query.setString("moduleInstance", moduleInstance);
            if (parentId != null) {
                query.setLong("id", parentId);
            }
            helpTopics = query.list();          

        } catch (Exception e) {
            logger.error("Unable to load Glossary topics");
            throw new AimException("Unable to Load Glossary Topics", e);
        }
        return helpTopics;
    }
    
}
