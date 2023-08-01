package org.digijava.module.help.util;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpContent;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.helper.HelpTopicHelper;
import org.digijava.module.help.helper.HelpTopicsTreeItem;
import org.digijava.module.help.jaxbi.AmpHelpType;
import org.digijava.module.help.jaxbi.HelpLang;
import org.digijava.module.help.jaxbi.ObjectFactory;
import org.digijava.module.help.lucene.LucHelpModule;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.exception.SDMException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public static Collection<HelpTopicsTreeItem> getHelpTopicsTree(Site site,
            String moduleInstance) throws AimException {
        List<HelpTopic> helpTopics= getHelpTopics(site, moduleInstance,null, null);
        Collection<HelpTopicsTreeItem> themeTree = CollectionUtils.getHierarchy(
                helpTopics,
                new HelpTopicHierarchyDefinition(), 
                new HelpTopicTreeItemFactory());
        return themeTree;
    }
    
    public static Collection<HelpTopicsTreeItem> getGlossaryTopicsTree(Site site,
            String moduleInstance) throws DgException {
        List<HelpTopic> glossaryTopics= GlossaryUtil.getAllGlosaryTopics(moduleInstance, site);
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
    public static List<HelpTopic> getHelpTopics(Site site,
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
                query.setString("locale", locale);
            }
            
            query.setString("siteId", site.getSiteId());
            query.setString("moduleInstance", moduleInstance);          
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
            logger.error(ex.getMessage(), ex);
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
            logger.error(ex.getMessage(), ex);
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
            logger.error(ex.getMessage(), ex);
            throw new AimException(ex);
        }
        return helpTopic;
    }

    public static HelpTopic getHelpTopic(String key, Site site, String moduleInstance) throws AimException {
        Session session = null;
        Query query = null;
        HelpTopic helpTopic = null;
        if (key != null && !key.equals("")) {
            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString="from "+ HelpTopic.class.getName()+" topic where (topic.topicKey=:key) " +
                        " and (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) ";
                query=session.createQuery(queryString);
                query.setString("siteId", site.getSiteId());
                query.setString("moduleInstance", moduleInstance);
                query.setString("key", key);
                helpTopic=(HelpTopic) query.uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AimException(e);
            }
        } else {
            throw new AimException("incorrect parameter key");
        }
        return helpTopic;
    }
    
    public static HelpTopic getHelpTopicByBodyEditKey(String bodyEditKey, Site site, String moduleInstance) throws AimException {
        Session session = null;
        Query query = null;
        HelpTopic helpTopic = null;
        if (bodyEditKey != null && !bodyEditKey.equals("")) {
            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString="from "+ HelpTopic.class.getName()+" topic where (topic.bodyEditKey=:bodyEditKey) " +
                        " and (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) ";
                query=session.createQuery(queryString);
                query.setString("siteId", site.getSiteId());
                query.setString("moduleInstance", moduleInstance);
                query.setString("bodyEditKey", bodyEditKey);
                helpTopic=(HelpTopic) query.uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
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
                String queryString = "from " + HelpTopic.class.getName()+" topic where (topic.bodyEditKey=:key) ";
                query = session.createQuery(queryString);
                query.setString("key", key);
                helpTopic=(HelpTopic) query.uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AimException(e);
            }
        } else {
            throw new AimException("incorrect parameter key");
        }
        return helpTopic;
    }

    public static boolean cheackEditKey(String key, Site site,
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
            query.setString("key", key);
            query.setString("siteId", site.getSiteId());
            query.setString("instance", moduleInstance);
            if (query.uniqueResult() == null) {
                return true;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AimException(e);
        }
        return false;
    }

    public static void saveOrUpdateHelpTopic(HelpTopic topic, HttpServletRequest request) throws AimException{
        Session session = null;
        boolean update = topic.getHelpTopicId() != null;
        try {
            session = PersistenceManager.getRequestDBSession();
            session.saveOrUpdate(topic);
            if (topic.getTopicType()==null || !topic.getTopicType().equals(GlossaryUtil.TYPE_GLOSSARY)){
                //skip lucene work for glossary topics.
                saveOrUpdateFromLucene(topic, request, update);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AimException("Can't update help topic", e);
        }
    }

    /**
     * scans the input string for all the "<img>" tags (usually there is only one, but occasionally there are more of them) and deleted documents referenced in them
     * @param session
     * @param imgTag - a string of the form 
     * <img   width="259" height="125" src="/sdm/showImage.do?activeParagraphOrder=0&documentId=179" /></span></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img   width="345" height="201" src="/sdm/showImage.do?activeParagraphOrder=2&documentId=179" />
     */
    private static void deleteAllMatchedImages(Session session, String imgTags) throws SDMException
    {
        while(true)
        {
            // invariant: string always starts with <img - once this invariant is violated, exit the loop
            if (imgTags == null || !imgTags.startsWith("<img"))
                break;
            int imgTagEndPos = imgTags.indexOf("/>");
            if (imgTagEndPos < 5) // malformed or non-closed tag 
            {
                logger.error("invalidly parsed img " + imgTags, new RuntimeException());
                break;
            }
            String imgTag = imgTags.substring(0, imgTagEndPos + 2); // include "/>"
        
            if(imgTag.contains("documentId=")){
                String docId = imgTag.substring(imgTag.indexOf("documentId=")+11);
                if(docId.contains("&")){
                    docId = docId .substring(0,docId.indexOf("&"));
                }else{
                    docId = docId .substring(0,docId.indexOf("\""));
                }
                
                if (!AmpMath.isLong(docId))
                {
                    logger.error("invalidly parsed docId " + docId, new RuntimeException());
                    break;
                }
                
                try
                {
                    Sdm doc = org.digijava.module.sdm.util.DbUtil.getDocument(new Long (docId), session);
                    session.delete(doc);
                }
                catch(SDMException e)
                {
                    if (e.getCause() instanceof org.hibernate.ObjectNotFoundException)
                    {
                        // swallow exception
                        logger.warn("could not delete document with id " + docId, new RuntimeException());
                    }
                    else
                        throw e;
                }
            }
            
            int nextImgTagPos = imgTags.indexOf("<img", imgTagEndPos);
            if (nextImgTagPos <= 0)
                break;
            imgTags = imgTags.substring(nextImgTagPos);
        }
    }
    
    public static void deleteHelpTopic(HelpTopic topic, HttpServletRequest request) throws AimException{
        Session session = null;
        Transaction tx = null;
        try {
            //session = PersistenceManager.getRequestDBSession();
//beginTransaction();
             session = PersistenceManager.openNewSession();
             tx = session.beginTransaction();
            if (topic.getBodyEditKey()!=null && topic.getSiteId()!=null){
                List<Editor> editors = DbUtil.getEditorList(topic.getBodyEditKey(), SiteCache.lookupByName(topic.getSiteId()), session);
                if (editors!=null && editors.size()>0){
                    for (Editor editor : editors) {
                        if(editor.getBody()!=null){
                            //if (editor.getEditorKey().equals("help-admin-2116913595-1357837306810"))
                                //System.out.println("I am atrociously handsome");
                            String imgPart="<img\\s.*?src\\=\"/sdm/showImage\\.do\\?.*?activeParagraphOrder\\=.*\"\\s?/>" ;
                            Pattern pattern = Pattern.compile(imgPart,Pattern.MULTILINE);
                            Matcher matcher = pattern.matcher(editor.getBody());
                            if (matcher.find()){                
                                String imgTag = matcher.group(0);
                                deleteAllMatchedImages(session, imgTag);
                            }                           
                        }   
                        session.delete(editor);
                    }
                }
            }
//          session.delete(topic);
            StringBuilder qstr = new StringBuilder("delete from ");
            qstr.append(HelpTopic.class.getName());
            qstr.append(" ht where ht.helpTopicId=:HT_ID");
            Query q = session.createQuery(qstr.toString());
            q.setLong("HT_ID", topic.getHelpTopicId());
            q.executeUpdate();


            tx.commit();
            session.flush();
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
        }finally {
            PersistenceManager.closeSession(session);
        }
    }
    
    public static void saveOrUpdateFromLucene(HelpTopic topic, HttpServletRequest request, boolean update) throws DgException{
        String moduleInstanceName = RequestUtils.getRealModuleInstance(request).getInstanceName();
        ServletContext context = request.getSession().getServletContext();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String title = TranslatorWorker.translateText(topic.getTopicKey());

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
    
    public static List<HelpTopic> getFirstLevelTopics(Site site, String moduleInstance,String key)throws AimException{
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "from "+ HelpTopic.class.getName()
            + " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent is null) and (topic.topicType=NULL)";
            query = session.createQuery(queryString);           
            query.setString("siteId", site.getSiteId());
            query.setString("moduleInstance", moduleInstance);
            helpTopics = query.list();

        } catch (Exception e) {
            logger.error("Unable to load help topics");
            throw new AimException("Unable to Load Help Topics", e);
        }
        if (key!=null){
            HelpTopic curTopic=getHelpTopic(key, site, moduleInstance);
            helpTopics.remove(curTopic);
        }
        return helpTopics;
    }
    
    /**
     * Load all first level help topics(admin and default)
     * @param siteId
     * @throws Exception
     */
    public static List<HelpTopic> getFirstLevelTopics(Site site) throws Exception{
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            //queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.parent is null and topic.topicType=NULL";
            queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.parent is null ";
            query = session.createQuery(queryString);
            query.setString("siteId", site.getSiteId());
            helpTopics = query.list();
        } catch (Exception e) {
            logger.error("Unable to load help topics");
            throw new AimException("Unable to Load Help Topics", e);
        }
        return helpTopics;
    }

    public static List<HelpTopic> getFirstLevelTopics(Site site, String moduleInstance) throws Exception{
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "from "+ HelpTopic.class.getName()+ " topic where topic.siteId=:siteId and topic.moduleInstance=:moduleInstance and topic.parent is null and topic.topicType=NULL";
            query = session.createQuery(queryString);
            query.setString("siteId", site.getSiteId());
            query.setString("moduleInstance", moduleInstance);
            helpTopics = query.list();
        } catch (Exception e) {
            logger.error("Unable to load help topics");
            throw new AimException("Unable to Load Help Topics", e);
        }
        return helpTopics;
    }

    public static boolean hasChildren(Site site, String moduleInstance, Long parentId)throws AimException{
        List<HelpTopic> helpTopics = getChildTopics(site, moduleInstance,   parentId);
        if (helpTopics==null || helpTopics.size()==0){
            return false;
        }
    return true;    
    }

    public static List<HelpTopic> getChildTopics(Site site, String moduleInstance, Long parentId) throws AimException {
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "from "+ HelpTopic.class.getName()
            + " topic where (topic.siteId=:siteId) and (topic.moduleInstance=:moduleInstance) and (topic.parent.helpTopicId=:id) and (topic.topicType=NULL)";
            query = session.createQuery(queryString);           
            query.setString("siteId", site.getSiteId());
            query.setString("moduleInstance", moduleInstance);
            query.setLong("id", parentId);
            helpTopics = query.list();          

        } catch (Exception e) {
            logger.error("Unable to load help topics");
            throw new AimException("Unable to Load Help Topics", e);
        }
        return helpTopics;
    }

    public static List<HelpTopic> getChildTopics(Site site, Long parentId) throws AimException {
        Session session = null;
        Query query = null;
        List<HelpTopic> helpTopics = null;
        String queryString=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "from "+ HelpTopic.class.getName()
            + " topic where (topic.siteId=:siteId) and (topic.parent.helpTopicId=:id) ";
            query = session.createQuery(queryString);
            query.setString("siteId", site.getSiteId());
            query.setLong("id", parentId);
            helpTopics = query.list();

        } catch (Exception e) {
            logger.error("Unable to load help topics");
            throw new AimException("Unable to Load Help Topics", e);
        }
        return helpTopics;
    }

    public static List<Editor> getAllHelpKey(String lang) throws
    EditorException {
//System.out.println("lang:"+lang);
        
    Session session = null;
    List<Editor> helpTopics = new ArrayList<Editor>();
    
    try {
        session = PersistenceManager.getRequestDBSession();
         Query q = session.createQuery(" from e in class " +
                 Editor.class.getName() +" where e.editorKey like 'help%' and e.language=:lang order by e.lastModDate");
            q.setString("lang", lang);
        helpTopics = q.list();
        
        
    } catch (Exception e) {
        logger.error("Unable to load help data", e);
            throw new EditorException("Unable to Load Help data", e);
    }
    return helpTopics;
}
    
    public static Collection getAllHelpData() throws 
    EditorException {
    
    Session session = null;
    Query query = null;
      //System.out.println("GetAllHelpData");
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
            
               ////System.out.println("body:"+edt.getBody());
            
                helpsearch.setBody(edt.getBody());
                helpsearch.setLastModDate(edt.getLastModDate());
                helpsearch.setTitleTrnKey(getHelpTopic(edt.getEditorKey()).getTitleTrnKey());
                helpsearch.setTopicKey(getHelpTopic(edt.getEditorKey()).getTopicKey());
                helpsearch.setLang(edt.getLanguage());
                helpsearch.setBodyKey(edt.getEditorKey());
        
                helpTopics.add(helpsearch);   
        }
        
        
        
    } catch (Exception e) {
        logger.error("Unable to load help data", e);
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
             query.setString("bodyKey", bodyKey);
             result = query.list();
             
        } catch (Exception e) {
            logger.error("Unable to load help data", e);
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
             query.setString("bodyKey", bodyKey);
             query.setString("lang", lang);
             result = query.list();
             
        } catch (Exception e) {
            logger.error("Unable to load help data", e);
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
                        //System.out.println("name:"+newCode+" Topic_PRNT:"+topic.getHelpTopicId());
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
    
    public static String renderHelpPrintPreview(Collection topics,String helpType,HttpServletRequest request) throws Exception{
        String  lang    = RequestUtils.getNavigationLanguage(request).getCode();
        String retVal = "";
        Iterator iter = topics.iterator();
        if(iter.hasNext()){         
            while (iter.hasNext()) {
                retVal += "<table width=\"1000px\">";
                retVal +="<tr height=\"5px\"><td>&nbsp;</td></tr>";
                HelpTopicsTreeItem item = (HelpTopicsTreeItem) iter.next();
                HelpTopic topic = (HelpTopic) item.getMember();
                if(topic.getTopicKey()!=null){
                    retVal +="<tr>";
                    retVal += "<td align=\"center\"><span style=\"font: 20px bold;\">";
                    retVal += getTrn(topic.getTopicKey(), request);
                    retVal += "</span></td>";
                    retVal +="</tr>";
                }
                retVal +="<tr height=\"5px\"><td>&nbsp;</td></tr>";
                if(topic.getBodyEditKey()!=null){                   
                    Editor editor = DbUtil.getEditor(topic.getBodyEditKey(), lang);
                    
                    String editorBody = editor == null ? "(" + TranslatorWorker.translateText("missing topic") + ")" : editor.getBody();
                    retVal +="<tr><td style=\"text-align:left;\">" + editorBody + "</td></tr>";
                }
                
                retVal += "</table>";
                if (item.getChildren() != null || item.getChildren().size() > 0) {
                    retVal += renderHelpPrintPreview(item.getChildren(),helpType,request);
                }
            }
            
        }       
        return retVal;
    }
    
     public static String getTrn(String defResult, HttpServletRequest request){
        return TranslatorWorker.translateText(defResult);
     }
     
    public static String getTrn(String defResult,String lange, Long siteId){
        return TranslatorWorker.translateText(defResult, lange, siteId);
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
     
     /**
      * fills ZipStream with image data and also builds help's export xml data
      * @author Dare
      * @return export data
      */
     public static List <AmpHelpType> getExportData ( ZipOutputStream out){
        logger.info("Starting helpExport");
        List <AmpHelpType> retVal = new ArrayList<AmpHelpType>(); 
        Session session = null;
        Query query = null;
        ObjectFactory objFactory = new ObjectFactory();     
         Set imageUniqueChecker = new HashSet();
         
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "from "+ HelpTopic.class.getName();
            query = session.createQuery(queryString);

             List<HelpTopic> qRes = query.list();
            //Iterator<HelpTopic> itr = qRes.iterator();
            

             
            for (HelpTopic item : qRes) {
                AmpHelpType helpout=objFactory.createAmpHelpType();


                //HelpTopic item = (HelpTopic) itr.next();


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

                 Iterator<String> langIter = allLang.iterator();
                 while (langIter.hasNext()){
                    String lang = (String) langIter.next();
                     if(item.getBodyEditKey() != null){                             
                        Editor editor = DbUtil.getEditor(item.getBodyEditKey(), lang);
                            if(editor != null){
                                 HelpLang helplang = new HelpLang();
                                 String editorBody = editor.getBody();
                                     
                            //String imgPart="(<img\\s.*?src\\=\"/sdm/showImage\\.do\\?.*?activeParagraphOrder\\=.*?\"\\s?/>)" ;//<img\s.*?src\=\".*showImage\.do\?.*?activeParagraphOrder\=.*\"\s?/>;
                                String imgPart="<img.*?src=\"/sdm/showImage\\.do.*?>";
                             Pattern pattern = Pattern.compile(imgPart,Pattern.MULTILINE);
                             Matcher matcher = pattern.matcher(editorBody);

                             while (matcher.find()){                                     
                                 String imgTag=matcher.group(0);
                                  /*
                                  String urlParams = "activeParagraphOrder=(\\d+).*?documentId=(\\d+)";
                                  Pattern urlPattern = Pattern.compile(urlParams,Pattern.MULTILINE);
                                  Matcher urlMatcher = urlPattern.matcher(imgTag);
                                  */
                                 //get the image and put it in zip
                                  String paragraphOrder = null;
                                  int activeParagraphOrderIdxOf = imgTag.indexOf("activeParagraphOrder=");
                                  int idxOfAmp = imgTag.indexOf("&");
                                  if (activeParagraphOrderIdxOf > -1 && idxOfAmp > -1) {
                                    paragraphOrder = imgTag.substring(activeParagraphOrderIdxOf + 21, idxOfAmp);
                                  }
                                  String docId = null;
                                  int documentIdIdxOf = imgTag.lastIndexOf("documentId=");
                                  if (documentIdIdxOf > -1) {
                                    docId = imgTag.substring(documentIdIdxOf + 11, imgTag.indexOf("\"",imgTag.indexOf("documentId=")+11));
                                  }

                                  String imgName = null;

                                  if (paragraphOrder != null && docId != null) {
                                    imgName = item.getBodyEditKey()+"_langIs_"+lang+"_poIs_"+paragraphOrder;
                                  }

                                  if (!imageUniqueChecker.contains(imgName) && paragraphOrder != null && docId != null) {
                                      imageUniqueChecker.add(imgName);


                                  Long docIdObj = new Long (docId);
                                  Long paragraphOrderObj = new Long (paragraphOrder);

                                 SdmItem sdmItem = org.digijava.module.sdm.util.DbUtil.getSdmItem(new Long (docId), new Long (paragraphOrder));
                                 if (sdmItem != null) {
                                     //get image extensions
                                     String imgType=sdmItem.getContentType();
                                     String imgExtension = ".jpg";
                                     if(imgType.indexOf("jpeg")==-1){
                                         imgExtension = "." + imgType.substring(imgType.lastIndexOf("/")+1);
                                     }

                                     out.putNextEntry(new ZipEntry(imgName +imgExtension));
                                     byte[] buf =  sdmItem.getContent();
                                     // Transfer bytes from the file to the ZIP file
                                     int len=buf.length;
                                     out.write(buf, 0, len);
                                     // Complete the entry
                                     out.closeEntry();
                                 }
                              }
                                 //replace editor body with new tag
                                 String newTag = "<sdmTag " + imgTag.substring(5,imgTag.indexOf("src=")) + "imgName=\""+imgName+"\" />";
                                 editorBody = matcher.replaceFirst(newTag);
                                 matcher = pattern.matcher(editorBody);
                             }
                                     
                              helplang.setBody(editorBody);
                              helplang.setTitle(HelpUtil.getTrn(item.getTopicKey(),lang,new Long(3)));
                              helplang.setCode(lang);
                              helpout.getLang().add(helplang);

                              Calendar cal_u = Calendar.getInstance();
                              cal_u.setTime(editor.getLastModDate());
                              helpout.setLastModDate(cal_u);
                        }else{
//                              HelpLang helplang = new HelpLang();
//                              helplang.setBody("");
//                              helplang.setTitle(HelpUtil.getTrn(item.getTopicKey(),lang,new Long(3)));
//                              helplang.setCode(lang);
//                              helpout.getLang().add(helplang);
//
//                              Calendar cal_u = Calendar.getInstance();
//                              cal_u.setTime(new Date());
//                              helpout.setLastModDate(cal_u);
                         }                          
                    }
             }
              retVal.add(helpout);
         }  
        } catch (Exception e) {
            logger.error("Unable to load help data", e);
                
        }
        return retVal;
    }

        
     public static void updateNewEditHelpData(AmpHelpType help,HashMap<Long,HelpTopic> storeMap, Site site, HashMap<String, Long> helpDocIdHolder,HttpServletRequest request){      
            
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
            Iterator<HelpLang> editrLang = help.getLang().listIterator();
            while(editrLang.hasNext()) {
                HelpLang xmlLangTag = (HelpLang) editrLang.next();
                Message newMsg = new Message();
                newMsg.setSite(site);
                newMsg.setMessage(xmlLangTag.getTitle());
                newMsg.setKey(TranslatorWorker.generateTrnKey(help.getTopicKey()));
                newMsg.setLocale(xmlLangTag.getCode());
                                                        
                // Message msg = TranslatorWorker.getInstance("").getByBody(xmlLangTag.getTitle().trim(), xmlLangTag.getCode(), siteId.toString());
                Message msg = TranslatorWorker.getInstance("").getByKey(newMsg.getKey(), xmlLangTag.getCode(), site);
                if(msg != null){
                    msg.setSiteId(newMsg.getSiteId());
                    msg.setMessage(newMsg.getMessage());
                    msg.setKey(newMsg.getKey());
                    msg.setLocale(newMsg.getLocale());
                    TranslatorWorker.getInstance("").update(msg);
                }else{
                    TranslatorWorker.getInstance("").save(newMsg);
                }
                updateEditData(xmlLangTag,help.getEditorKey(),help.getLastModDate(),helpDocIdHolder);
            }
            //insertHelp(helptopic);
            saveOrUpdateHelpTopic(helptopic, request);
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
     
     public static void updateEditData(HelpLang help,String key,Calendar lastModDate,HashMap<String, Long> helpDocIdHolder){
       List<HelpTopic> result = null;
       Session session = null;
       Query query = null;
        
            try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            String queryString = "select editTopic from "+ Editor.class.getName() + " editTopic where (editTopic.editorKey=:key) and  (editTopic.language=:lang)";
            query = session.createQuery(queryString);
            query.setString("lang", help.getCode());
            query.setString("key", key);
            List<Editor> res =query.list();
            
            String helpBody=help.getBody();
            //<sdmTag width="240" height="320" imgName="help-admin-271423211-1296048546820_langIs_en_poIs_0.jpg" /> 
            String imgPart="<sdmTag.*?>" ;
            Pattern pattern = Pattern.compile(imgPart,Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(helpBody);
             
             while (matcher.find()){                                     
                 String sdmTag=matcher.group(0);
                 String paragrapgOrder=sdmTag.substring(sdmTag.indexOf("_poIs_")+6,sdmTag.lastIndexOf("\""));
                 String sdmDocId=(helpDocIdHolder.get(key+"_langIs_"+help.getCode())).toString();
                //replace editor body with new tag
                String newTag = "<img " + sdmTag.substring(7,sdmTag.indexOf("imgName=")) + "src=\"/sdm/showImage.do?activeParagraphOrder="+paragrapgOrder+"&documentId="+sdmDocId+"\" />";
                helpBody = matcher.replaceFirst(newTag);
                matcher = pattern.matcher(helpBody);
            
             }
                 
                if(res!= null && res.iterator().hasNext()){
                 Iterator itr = res.iterator();
                 while (itr.hasNext()) {
                       Editor edit = (Editor) itr.next();                      
                       edit.setBody(helpBody);
                       edit.setLastModDate(lastModDate.getTime());
                       edit.setSiteId("amp"); //TODO: nice bugs source if we ever move to multi-sites
                       edit.setLanguage(help.getCode());
                       session.saveOrUpdate(edit);
                 }
                 //tx.commit();
               }else if(help != null) {
      
                   Editor edit = new  Editor();
                   edit.setBody(helpBody);
                   edit.setLastModDate(lastModDate.getTime());
                   edit.setSiteId("amp"); //TODO: nice bugs source if we ever move to multi-sites
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
                    session = PersistenceManager.getSession();
//beginTransaction();
                    session.save(editor);
                    //tx.commit();
            }
            catch (Exception ex) {
                logger.error("Exception : " + ex.getMessage());
                ex.printStackTrace(System.out);
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


    public static void saveNewTreeState(HelpTopic help, HashMap<Long, HelpTopic> storeMap, Site site) {

        // Thread th = new Thread();
        Session session = PersistenceManager.getSession();
        try {
            HelpTopic helptopic = new HelpTopic();
            helptopic.setTopicKey(help.getTopicKey());
            helptopic.setSiteId("amp");
            helptopic.setTitleTrnKey(help.getTitleTrnKey());
            helptopic.setModuleInstance(help.getModuleInstance());
            helptopic.setKeywordsTrnKey(help.getKeywordsTrnKey());
            helptopic.setBodyEditKey(help.getBodyEditKey());

            if (help.getParent() != null) {
                HelpTopic top = storeMap.get(help.getParent().getHelpTopicId());
                if (top != null) {
                    helptopic.setParent(top);
                }
            }

            session.save(helptopic);
            List<HelpContent> helpContents = help.getHelpContent();
            if (helpContents != null && helpContents.size() > 0) {
                for (HelpContent helpContent : helpContents) {
                    Editor editor = helpContent.getEditor();
                    Sdm oldDoc = helpContent.getDocument();
                    if (editor != null) {
                        if (oldDoc != null) {
                            // save sdm back in db
                            Sdm newDoc = new Sdm();
                            newDoc.setInstanceId(oldDoc.getInstanceId());
                            newDoc.setName(oldDoc.getName());
                            newDoc.setSiteId(oldDoc.getSiteId());

                            HashSet<SdmItem> items = new HashSet<SdmItem>();
                            for (SdmItem sdmItem : oldDoc.getItems()) {
                                SdmItem newItem = new SdmItem();
                                newItem.setContentType(sdmItem.getContentType());
                                newItem.setRealType(sdmItem.getRealType());
                                newItem.setContent(sdmItem.getContent());
                                newItem.setContentText(sdmItem.getContentText());
                                newItem.setContentTitle(sdmItem.getContentTitle());
                                newItem.setParagraphOrder(sdmItem.getParagraphOrder());

                                items.add(newItem);
                            }

                            newDoc.setItems(items);
                            newDoc = org.digijava.module.sdm.util.DbUtil.saveOrUpdateDocument(newDoc);

                            // update editor
                            String imgPart = "<img\\s.*?src\\=\"/sdm/showImage\\.do\\?.*?activeParagraphOrder\\=.*documentId="
                                    + oldDoc.getId() + ".*\"\\s?/>";
                            Pattern pattern = Pattern.compile(imgPart, Pattern.MULTILINE);
                            String editorBody = editor.getBody();
                            Matcher matcher = pattern.matcher(editorBody);
                            String containsStr = "documentId=";
                            while (matcher.find()) {
                                String imgTag = matcher.group(0);
                                if (imgTag.contains(containsStr)) {
                                    String docId = oldDoc.getId().toString();
                                    // String docId =
                                    // imgTag.substring(imgTag.indexOf("documentId=")+11);
                                    // if(docId.contains("&")){
                                    // docId = docId
                                    // .substring(0,docId.indexOf("&"));
                                    // }else{
                                    // docId = docId
                                    // .substring(0,docId.indexOf("\""));
                                    // }
                                    imgTag = imgTag.replace("documentId=" + docId, "documentId=" + newDoc.getId());
                                    editorBody = matcher.replaceFirst(imgTag);
                                    containsStr = "documentId=" + docId;
                                    matcher = pattern.matcher(editorBody);
                                } else {
                                    break;
                                }
                            }
                            editor.setBody(editorBody);
                        }

                        org.digijava.module.editor.util.DbUtil.saveEditor(editor);
                    }

                }
            }

            Thread.sleep(200); // TODO: WA...DA...FA. Constantin: 60% speedup!
                                // :D

            HelpTopic newTopic = getHelpTopic(help.getTopicKey(), site, help.getModuleInstance());

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
            logger.error("Unable to Save help data" + e.getMessage());
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
            Session session = PersistenceManager.getSession();
            //Group by module instances.
            //Instead of such grouping we may have hardcoded admin and default instances.
            String oql = "select h.moduleInstance ";
            oql += " from "+HelpTopic.class.getName() + " as h";
            oql += " group by h.moduleInstance";
            Query query = session.createQuery(oql);
            rows = query.list();
        } catch (HibernateException e) {
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
    public static List<HelpTopicHelper> getHelpItems(Site site, String moduleInstance, EnumSet<LangSupport> langs, boolean exclude) throws DgException{
        
        String oql = "select new org.digijava.module.help.helper.HelpTopicHelper(h.helpTopicId, h.topicKey, e.body, h.siteId, h.moduleInstance, e.language, h.titleTrnKey, h.bodyEditKey) "; 
        oql += " from "+HelpTopic.class.getName()+" as h, "+Editor.class.getName()+" as e where ";
        oql += " h.bodyEditKey = e.editorKey ";
        oql += " and h.topicType is null";
        if (site != null){
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
        
        if (site != null){
            query.setString("siteID", site.getSiteId());
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
    
 
    public static List<HelpContent> getHelpTopicContentObjects (List<Editor> editors) throws Exception{
        List<HelpContent> retVal = null;
        if (editors!=null && editors.size()>0){
            for (Editor editor : editors) {
                if(retVal==null){
                    retVal = new ArrayList<HelpContent>();
                }               
                HelpContent helpContent = new HelpContent();
                helpContent.setEditor(editor);
                
                String imgPart="<img\\s.*?src\\=\"/sdm/showImage\\.do\\?.*?activeParagraphOrder\\=.*\"\\s?/>" ;
                Pattern pattern = Pattern.compile(imgPart,Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(editor.getBody());
                if (matcher.find()){                
                    String imgTag = matcher.group(0);
                    if(imgTag.contains("documentId=")){
                        String docId = imgTag.substring(imgTag.indexOf("documentId=")+11);
                        if(docId.contains("&")){
                            docId = docId .substring(0,docId.indexOf("&"));
                        }else{
                            docId = docId .substring(0,docId.indexOf("\""));
                        }
                        Sdm doc = org.digijava.module.sdm.util.DbUtil.getDocument(new Long (docId));
                        
                        helpContent.setDocument(doc);
                    }
                }
                retVal.add(helpContent);
            }
        }
        return retVal;
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
