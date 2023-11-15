package org.digijava.module.aim.util;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.query.lucene.MoreLikeThis;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.ActivityLuceneDocument;
import org.digijava.kernel.lucene.AmpLuceneDoc;
import org.digijava.kernel.lucene.AmpLuceneTopDocs;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.module.admin.helper.AmpPledgeFake;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.util.HelpUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.type.StringType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.lucene.store.Lock.LOCK_OBTAIN_WAIT_FOREVER;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !                                                                          !
 * ! The serial version needs to be changed if the index generating algorithm !
 * ! is changed, so index will be regenerated                                 !
 * !                                                                          !
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
 *
 * @author Alexandru Artimon
 *
 */

public class LuceneUtil implements Serializable {
    /**
     * We use the serialVersion to know the version of the lucene index
     * saved on the disk, if versions mismatch then we need to increment
     * the index
     */
    private static final long serialVersionUID = 15L;


    private static Logger logger = Logger.getLogger(LuceneUtil.class);
    /**
     * StandardAnalyzer used to analyse text 
     */
    public static final Analyzer ANALYZER = new StandardAnalyzer(Version.LUCENE_36);
    /**
     *
     */
    public static final String ID_FIELD = "id";
    
    public static final String AMP_ID_FIELD = "ampId";

    public static final int MAX_LUCENE_RESULTS  = 2000;

    /**
     * LUCENE INDEX PATH: use the LUCENE_BASE_DIR + new 
     *                    directory for your index
     */

    /**
     * lucene base directory
     */
    public final static String LUCENE_BASE_DIR = "lucene";
    public final static String LUCENE_STAMP_EXT = ".stamp";

    /**
     * name of help index directory
     */
    public final static String HELP_INDEX_SUFIX = "help";
    public final static String HELP_INDEX_DIRECTORY = LUCENE_BASE_DIR +"/" + HELP_INDEX_SUFIX;


    /**
     * name of the activity index directory
     * please don't use for other indexes
     * @author Arty
     */
    public final static String ACTIVITY_INDEX_SUFFIX = "activity";
    public final static String ACTIVITY_INDEX_DIRECTORY = LUCENE_BASE_DIR + "/" + ACTIVITY_INDEX_SUFFIX;

    public final static String PLEDGE_INDEX_SUFFIX = "pledge";
    public final static String PLEDGE_INDEX_DIRECTORY = LUCENE_BASE_DIR + "/" + PLEDGE_INDEX_SUFFIX;

    private static final int CHUNK_SIZE = 10000;

    public final static Integer SEARCH_MODE_AND = 1;

    private static final String FUZZY_SEARCH_FM_ENTRY = "Fuzzy Search";
    private static final float DEFAULT_MINIMUM_SIMILARITY = 0.5f;
    private static final float MINIMUM_SIMILARITY_TO_NUMBERS = 0.99f;

    public static AmpLuceneIndexStamp getIdxStamp(String name) throws Exception{
        logger.info("Getting lucene index stamp for index name: " + name);
        String oql= "select idx from "+AmpLuceneIndexStamp.class.getName()+" idx"+
                " where idx.idxName=:theName";
        try {
            Session session=PersistenceManager.getRequestDBSession();

            org.hibernate.query.Query query=session.createQuery(oql);
            query.setParameter("theName", name, StringType.INSTANCE);

            return ((AmpLuceneIndexStamp)query.uniqueResult());
        } catch (Exception e) {
            throw new Exception("Cannot get index stamp for:"+name,e);
        }
    }

    /**
     * Deletes all stamps from db with specified name.
     * @param name
     * @return true - at least one record has been deleted. false - nothing was deleted.
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static boolean deleteIdxStamps(String name) throws DgException {
        String oql = "select idx from " + AmpLuceneIndexStamp.class.getName()
                + " idx" + " where idx.idxName=:theName";
        Transaction transaction = null;
        boolean oldRecordDeleted = false;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            org.hibernate.query.Query query = session.createQuery(oql);
            query.setParameter("theName", name,StringType.INSTANCE);
            List<AmpLuceneIndexStamp> stamps = query.list();
            // Delete all fund stamps for this module name.
            if (stamps != null && stamps.size() > 0) {
//beginTransaction();
                for (AmpLuceneIndexStamp stamp : stamps) {
                    session.delete(stamp);
                }
                //transaction.commit();
                oldRecordDeleted = true;
            }
        } catch (HibernateException e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback stamp deletion for "
                            + name, e1);
                }
            }
            throw new DgException("Cannot delete stams for " + name, e);
        }
        return oldRecordDeleted;
    }

    /**
     * Creates new stamp record.
     * @param name
     * @param timestamp
     * @throws DgException
     */
    public static void createStamp(String name, long timestamp) throws DgException {
        AmpLuceneIndexStamp stamp = new AmpLuceneIndexStamp();
        stamp.setIdxName(name);
        stamp.setStamp(timestamp);
        Session session = PersistenceManager.getRequestDBSession();
        try {
            session.save(stamp);
        } catch (HibernateException e) {
            throw new DgException("Cannot create stamp for "+name, e);
        }

    }

    static public boolean deleteDirectory(String path) {
        File dir = new File(path);
        return deleteDirectory(dir);
    }

    static public boolean deleteDirectory(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return(path.delete());
    }


    public static void checkActivityIndex(ServletContext sc) {
        checkLuceneIndex(sc, false, ACTIVITY_INDEX_SUFFIX, ACTIVITY_INDEX_DIRECTORY);
    }
    
    public static void checkPledgeIndex(ServletContext sc) {
        checkLuceneIndex(sc, true, PLEDGE_INDEX_SUFFIX, PLEDGE_INDEX_DIRECTORY);
    }
    
    public static void checkLuceneIndex(ServletContext sc, boolean pledge, String indexSuffix, String indexDirectory){
        logger.info("Lucene startup!");
        
        File idxStamp = new File(sc.getRealPath("/") + LUCENE_BASE_DIR + "/" + indexSuffix + LUCENE_STAMP_EXT);
        File idxDir = new File(sc.getRealPath("/") + indexDirectory);
        boolean deleteIndex = false;
        
        checkStamp:{
            if (idxStamp.exists()){
                logger.info("Checking stamp ...");
                
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(idxStamp);
                } catch (FileNotFoundException e) {
                    logger.error("Stamp file missing:",e);
                    logger.info("Checking for index directory ...");
                    deleteIndex = true;
                    break checkStamp;
                }
                
                DataInputStream dis = new DataInputStream(fis);
                long serialId;
                long dbId;
                try {
                    serialId = dis.readLong();
                    dbId = dis.readLong();
                    dis.close();
                } catch (IOException e) {
                    logger.error("Error while reading stamp file:",e);
                    logger.info("Checking for index directory ...");
                    deleteIndex = true;
                    break checkStamp;
                }
                
                //checking if the indexing algorithm has changed - new source code?
                if (serialVersionUID != serialId){
                    logger.warn("Algorithm serial ID mismatch ... regenerating index");
                    idxStamp.delete();
                    logger.info("Stamp deleted, looking for index directory ...");
                    deleteIndex = true;
                    break checkStamp;
                }
                else{
                    logger.info("Lucene Index algorithm serial ID is OK");
                }
                
                //getting DB timestamp
                long dbStamp = -1;
                try {
                    dbStamp = getIdxStamp(indexSuffix).getStamp();
                } catch (Exception e) {
                    logger.error("", e);
                }
                
                //checking the database for timestamp
                if (dbStamp != dbId){
                    logger.warn("Database time stamp mismatch ... regenerating index");
                    idxStamp.delete();
                    logger.info("Stamp deleted, looking for index directory ...");
                    deleteIndex = true;
                    break checkStamp;
                }
                else{
                    logger.info("Lucene Index database stamp is OK");
                }
            }
            else{
                logger.info("Stamp file missing, checking for index directory ...");
                deleteIndex = true;
            }
        }
        
        if (deleteIndex){
            if (idxDir.exists()){
            if (deleteIndex){
                logger.info("Found, deleting ...");
                if (deleteDirectory(idxDir))
                    logger.info("Done");
                else
                    logger.info("Can't delete!");
            }
        }
            else{
                logger.info("Not found ... will be generated!");
            }
        }
    
        if (!idxDir.exists()){ //we need to create the index from 0  
            if (!idxDir.mkdirs()){
                logger.error("**********************************************************************");
                logger.error("*                                                                    *");
                logger.error("*                           WARNING                                  *");
                logger.error("*     Can't create Lucene Index directory, searching won't work!     *");
                logger.error("*                                                                    *");
                logger.error("**********************************************************************");
                return;
            }
            
            int mId = getMaxId(pledge);
            long startTime = System.currentTimeMillis();
            try {
                Directory directory = FSDirectory.open(idxDir);
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, ANALYZER);
                IndexWriter fsWriter = new IndexWriter(directory, indexWriterConfig);
                
                int chunkNo = 0;
        
                while (createIndex(chunkNo, mId, fsWriter) != null){
                    chunkNo++;
                }
                fsWriter.forceMerge(1);
                fsWriter.close();
                long stopTime = System.currentTimeMillis();
                
                try {
                    AmpLuceneIndexStamp currentStamp = getIdxStamp(indexSuffix);
                    if (currentStamp != null)
                        org.digijava.module.aim.util.DbUtil.deleteAllStamps(indexSuffix);
                } catch (Exception e1) {
                }
                
                AmpLuceneIndexStamp stamp = new AmpLuceneIndexStamp();
                stamp.setIdxName(indexSuffix);
                stamp.setStamp(stopTime);
                
                try {
                    Session session = PersistenceManager.getRequestDBSession();
                    //beginTransaction();
                    session.save(stamp);
                    //tx.commit();
                    //PersistenceManager.releaseSession(session);
                }
                catch (Exception e) {
                    logger.error("Error while saving lucene db stamp:", e);
                }

                if (idxStamp.exists()){
                    idxStamp.delete();
                }
                
                try{
                    FileOutputStream fos = new FileOutputStream(idxStamp);
                    DataOutputStream dos = new DataOutputStream(fos);
                    
                    dos.writeLong(serialVersionUID);
                    dos.writeLong(stopTime);
                    dos.close();
                }
                catch (Exception e) {
                    logger.error("Error while saving index stamp file:", e);
                }
                logger.info("Indexing took: " + (stopTime - startTime) + " ms");
            } catch (IOException e) {
                logger.error("Error while creting Lucene index:", e);
                deleteDirectory(idxDir); //no directory .. no index
                return;
            }
            
            logger.info("Lucene Index successfully created!");
        }
        else
            logger.info("Lucene Index found, using saved one:" + idxDir.getAbsolutePath());
    }

    private static int getMaxId(boolean pledge) {
        int ret = -1;
        try{
            Session session = PersistenceManager.getSession();
            Connection  conn    = ((SessionImplementor)session).connection();
            Statement st        = conn.createStatement();
            String columnName = pledge? "pledge_id" : "amp_activity_id";
            String tableName = pledge ? "v_pledges_titles" : "v_titles";
            String qryStr       = String.format("select max(%s) mid from %s",columnName,tableName );

            ResultSet rs        = st.executeQuery(qryStr);
            
            rs.next();
            if(rs.getString("mid")==null) return 0;
            ret = Integer.parseInt(rs.getString("mid"));
        }
        catch(Exception ex){
            logger.error("Error while getting the max " + (pledge? "pledge" : "activity" )+ " id:", ex);
        }
        return ret;
    }
    /**
     * Metod is used for first time index creation
     *
     * @return
     */
    private static Integer createIndex(final int chunkNo, final int mId, final IndexWriter indexWriter) {
        // RAMDirectory index = new RAMDirectory();
        /*
         * IndexWriter indexWriter = null; try { indexWriter = new
         * IndexWriter(index, analyzer, true); } catch (Exception e) {
         * e.printStackTrace(); }
         */

        logger.info("Getting activity List for chunk no " + chunkNo + " !");
        Session session = null;

        final class Items {
            int id;
            String amp_id;
            String title;
            String description;
            String objective;
            String purpose;
            String results;
            String numcont;
            String CRIS;
            String budgetNumber;
            String newBudgetNumber;
            // String contractingArr;
            ArrayList<String> componentcode = new ArrayList<String>();
        }
        ;

        session = PersistenceManager.getSession();

        Integer wret = session.doReturningWork(new ReturningWork<Integer>() {
            @Override
            public Integer execute(Connection conn) throws SQLException {
                int chunkStart = chunkNo * CHUNK_SIZE, chunkEnd = (chunkNo + 1) * CHUNK_SIZE;

                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String qryStr = String.format("SELECT vt.* FROM v_titles vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);

                ResultSet rs = st.executeQuery(qryStr);

                HashMap list = new HashMap();

                Items x;

                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " activities!");
                boolean isNext = rs.first();

                if (!isNext) {
                    if ((mId != -1) && (mId > chunkEnd)) {
                        return new Integer(1);
                    } else if ((mId == -1) || (mId < chunkEnd))
                        return null;
                }

                while (isNext) {
                    x = new Items();
                    x.id = Integer.parseInt(rs.getString("amp_activity_id"));
                    x.title = rs.getString("name");
                    list.put(x.id, x);
                    isNext = rs.next();
                    //
                }
                // the correct view is v_amp_id, the view with name v_ampid
                // is not used
                qryStr = String.format("SELECT vt.* FROM v_amp_id vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);
                
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " project id's!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("amp_activity_id"));
                    x = (Items) list.get(actId);
                    if (x != null)
                        x.amp_id = rs.getString("amp_id");
                    isNext = rs.next();
                    //
                }

                qryStr = String.format("SELECT vt.* FROM v_description vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);

                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " descriptions!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("amp_activity_id"));
                    x = (Items) list.get(actId);
                    if (x != null)
                        x.description = rs.getString("ebody");
                    isNext = rs.next();
                    //
                }

                qryStr = String.format("SELECT vt.* FROM v_objectives vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);
                
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " objectives!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("amp_activity_id"));
                    x = (Items) list.get(actId);
                    if (x != null)
                        x.objective = rs.getString("ebody");
                    isNext = rs.next();
                    //
                }

                qryStr = String.format("SELECT vt.* FROM v_purposes vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);
                
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " purposes!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("amp_activity_id"));
                    x = (Items) list.get(actId);
                    // you can't use "trim(dg_editor.body)" as column name
                    // ....
                    if (x != null)
                        x.purpose = rs.getString("ebody");
                    isNext = rs.next();
                    //
                }

                qryStr = String.format("SELECT vt.* FROM v_results vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);
                
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " results!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("amp_activity_id"));
                    x = (Items) list.get(actId);
                    // you can't use "trim(dg_editor.body)" as column name
                    // ....
                    if (x != null)
                        x.results = rs.getString("ebody");
                    isNext = rs.next();
                    //
                }
//
//              // Bolivia contract number
//              qryStr = "select * from v_convenio_numcont where amp_activity_id >= " + chunkStart
//                      + " and amp_activity_id < " + chunkEnd + " ";
//              rs = st.executeQuery(qryStr);
//              rs.last();
//              logger.info("Starting iteration of " + rs.getRow() + " results!");
//              isNext = rs.first();
//              while (isNext) {
//                  int actId = Integer.parseInt(rs.getString("amp_activity_id"));
//                  x = (Items) list.get(actId);
//                  if (x != null)
//                      x.numcont = rs.getString("numcont");
//                  isNext = rs.next();
//                  //
//              }

                // Bolivia component code
                qryStr = String.format("SELECT vt.* FROM v_bolivia_component_code vt JOIN amp_activity lv "
                        + "ON vt.amp_activity_id = lv.amp_activity_id WHERE (vt.amp_activity_id >= %d) "
                        + "AND (vt.amp_activity_id < %d)", chunkStart, chunkEnd);
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " v_bolivia_component_code!");
                isNext = rs.first();

                while (isNext) {
                    int currActId = rs.getInt("amp_activity_id");
                    x = (Items) list.get(currActId);
                    if (x != null)
                        if (rs.getString("code") != null) {
                            x.componentcode.add(rs.getString("code"));
                        }
                    isNext = rs.next();
                }
                // new budget codes
                qryStr = "select r.activity, string_agg(r.budget_code, ' ; ' ) as budget_codes "
                        + "from amp_org_role r, amp_activity a where a.amp_activity_id=r.activity and activity >= "
                        + chunkStart + " and activity < " + chunkEnd + " group by activity";
                rs = st.executeQuery(qryStr);
                rs.last();
                logger.info("Starting iteration of " + rs.getRow() + " new budget codes!");
                isNext = rs.first();
                while (isNext) {
                    int actId = Integer.parseInt(rs.getString("activity"));
                    x = (Items) list.get(actId);
                    if (x != null)
                        x.newBudgetNumber = rs.getString("budget_codes");
                    isNext = rs.next();
                }

                // New fields for Senegal.
                /*
                 * qryStr =
                 * "select * from v_senegal_cris_budget where amp_activity_id >= "
                 * + chunkStart + " and amp_activity_id < " + chunkEnd + " "; rs
                 * = st.executeQuery(qryStr); rs.last();
                 * logger.info("Starting iteration of " + rs.getRow() +
                 * " v_senegal_cris_budget!"); isNext = rs.first();
                 * 
                 * while (isNext) { int currActId =
                 * rs.getInt("amp_activity_id"); x = (Items)
                 * list.get(currActId); x.CRIS = rs.getString("cris_number");
                 * x.budgetNumber = rs.getString("budget_number"); isNext =
                 * rs.next(); }
                 */

                logger.info("Building the index ");
                Iterator it = list.values().iterator();
                while (it.hasNext()) {
                    Items el = (Items) it.next();
                    
                    ActivityLuceneDocument actLuceneDoc = new ActivityLuceneDocument();
                    actLuceneDoc.setAmpActivityId(String.valueOf(el.id));
                    actLuceneDoc.setProjectId(el.amp_id);
                    actLuceneDoc.setName(el.title);
                    actLuceneDoc.setDescription(el.description);
                    actLuceneDoc.setObjective(el.objective);
                    actLuceneDoc.setPurpose(el.purpose);
                    actLuceneDoc.setResults(el.results);
                    actLuceneDoc.setContactName(el.numcont);
                    actLuceneDoc.setCrisNumber(el.CRIS);
                    actLuceneDoc.setBudgetCodeProjectId(el.budgetNumber);
                    actLuceneDoc.setBudgetCodes(el.newBudgetNumber);
                    actLuceneDoc.setComponentCodes(el.componentcode);
                    
                    Document doc = activityToLuceneDocument(actLuceneDoc);
                    
                    if (doc != null)
                        try {
                            indexWriter.addDocument(doc);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
                list.clear();

                try {
                    indexWriter.forceMerge(1);
                    // indexWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new Integer(1);
            }
        });

        return wret;

    }

    static Map<String, String> digestDocument(Document doc)
    {
        Map<String, String> res = new LinkedHashMap<String, String>();
        List<Fieldable> fields = doc.getFields();
        for (Fieldable field:fields) {
            res.put(field.name(), field.stringValue());
        }
        
        return res;
    }

    public static boolean deleteEntry(String idx, String field, String search) {
        Term term = new Term(field, search);
        IndexWriter indexWriter = null;
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, LuceneUtil.ANALYZER);
            indexWriterConfig.setWriteLockTimeout(LOCK_OBTAIN_WAIT_FOREVER);
            Directory directory = FSDirectory.open(new File(idx));
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteDocuments(term);
            return indexWriter.hasDeletions();
        } catch (Exception e) {
            logger.error("Error deleting activity from Lucene index", e);
            return false;
        } finally {
            try {
                if (indexWriter != null) {
                    indexWriter.close();
                }
            } catch (IOException e) {
                logger.error("Error closing Lucene index writer", e);
            }
        }
    }

    public static boolean deletePledge(String rootRealPath, Long pledgeId) {
        return deleteEntry(rootRealPath + PLEDGE_INDEX_DIRECTORY, ID_FIELD, String.valueOf(pledgeId));
    }

    public static boolean deleteActivity(String rootRealPath, Long activityId) {
        return deleteEntry(rootRealPath + ACTIVITY_INDEX_DIRECTORY, ID_FIELD, String.valueOf(activityId));
    }

    static String activityClassName = AmpActivityVersion.class.getName();

    /**
     * concatenates the values translated in all the languages
     * returns NULL if no translations present
     * @param id
     * @param fieldName
     * @return
     */
    protected static String buildLuceneValueForField(long id, String fieldName)
    {
        List<String> languages = TranslatorUtil.getLanguages();

        List<AmpContentTranslation> valueTranslationsList = ContentTranslationUtil.loadFieldTranslations(activityClassName, id, fieldName);

        StringBuilder result = new StringBuilder();

        if (valueTranslationsList.isEmpty())
            return null; // no translations in the DB: this is an old untranslated entity

        HashMap<String, String> tempHash = new HashMap<String, String>();
        for (AmpContentTranslation pId: valueTranslationsList)
        {
            tempHash.put(pId.getLocale(), pId.getTranslation());
        }

        for (String lang: languages)
            if (tempHash.get(lang) != null)
            {
                if (result.length() != 0)
                    result.append(" ");
                result.append(tempHash.get(lang));
            }

        return result.toString();
    }


    public static Document pledge2Document(Long pledgeId, String title, String additionalInfo) {
        Document doc = new Document();
        String all = new String("");

        if (pledgeId != null){
            doc.add(new Field(ID_FIELD, pledgeId.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            //all = all.concat(" " + actId);
        }
        
        HashMap<String, String> regularFieldNames = new HashMap<String, String>();
        regularFieldNames.put("pledgeId", String.valueOf(pledgeId));
        regularFieldNames.put("title", title);

        Long id = pledgeId;

        for (String field: regularFieldNames.keySet())
        {
            String luceneValue = buildLuceneValueForField(id, field);
            if (luceneValue == null)
                luceneValue = regularFieldNames.get(field);

         // Added try/catch because Field can throw an exception if any of the parameters is wrong and that would break the process. 
            try {
                if ("title".equals(field)){
                    doc.add(new Field(field, luceneValue, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
                } else {
                    doc.add(new Field(field, luceneValue, Field.Store.NO, Field.Index.ANALYZED));
                }
            } catch (Exception e) {
                logger.error("Error reindexing document - field:" + field + " - value:" + luceneValue);
                logger.error("", e);
            }
            all = all.concat(" " + luceneValue);
        }

/*
        if (projectId != null){
            doc.add(new Field("projectId", projectId, Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + projectId);
        }
        if (title != null){
            doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));
            all = all.concat(" " + title);
        }*/
        if (additionalInfo!= null && additionalInfo.length()>0){
            doc.add(new Field("additionalInfo", additionalInfo, Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + additionalInfo);
        }
        
        
        
        if (all.length() == 0)
            return null;
        
        doc.add(new Field("all", all, Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }    

    /**
     * Add an activity to the index
     *
     * @param request is used to retrieve curent site and navigation language
     * @param act the activity that will be added
     */
    public static Document activityToLuceneDocument(ActivityLuceneDocument actLuceneDoc) {
        Document doc = new Document();
        String all = "";
        if (actLuceneDoc.getAmpActivityId() != null) {
            doc.add(new Field(ID_FIELD, actLuceneDoc.getAmpActivityId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        HashMap<String, String> regularFieldNames = new HashMap<String, String>();
        regularFieldNames.put(AMP_ID_FIELD, actLuceneDoc.getProjectId());
        regularFieldNames.put("name", actLuceneDoc.getName());

        Long id = Long.valueOf(actLuceneDoc.getAmpActivityId());

        for (String field : regularFieldNames.keySet()) {

            List<AmpContentTranslation> valueTranslationsList = new ArrayList<>();
            if (actLuceneDoc.getTranslations() != null) {
                valueTranslationsList = actLuceneDoc.getTranslations();
            } else {
                valueTranslationsList = ContentTranslationUtil.loadFieldTranslations(activityClassName, id, field);
            }

            if (!valueTranslationsList.isEmpty()) {
                for (AmpContentTranslation translation : valueTranslationsList) {
                    if (translation.getTranslation() == null) {
                        continue;
                    }
                    // Added try/catch because Field can throw an exception if any of the parameters is wrong and that would break the process.
                    try {
                        if ("name".equals(field)){
                                /*logger.info("Adding activity name to lucene index. Name: "+translation.getTranslation()
                                        +" Locale: "+translation.getLocale());*/
                            doc.add(new Field(field + "_" + translation.getLocale(),
                                    translation.getTranslation(),
                                    Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
                        } else {
                            doc.add(new Field(field + "_" + translation.getLocale(),
                                    translation.getTranslation(),
                                    Field.Store.NO, Field.Index.ANALYZED));
                        }
                    } catch (Exception e) {
                        logger.error("Error reindexing document - field:" + field +
                                " - value:" + translation.getTranslation() + " locale:" + translation.getLocale());
                        logger.error("", e);
                    }
                    all = all.concat(" " + translation.getTranslation());
                }
            } else {
                if (regularFieldNames.get(field) != null) {
                    // no translations in the DB: this is an old untranslated entity
                    doc.add(new Field(field, regularFieldNames.get(field), 
                            Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
                    all = all.concat(" " + regularFieldNames.get(field));
                }
            }
        }
    
        if (StringUtils.isNotBlank(actLuceneDoc.getDescription())) {
            doc.add(new Field("description", actLuceneDoc.getDescription(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getDescription());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getObjective())) {
            doc.add(new Field("objective", actLuceneDoc.getObjective(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getObjective());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getPurpose())) {
            doc.add(new Field("purpose", actLuceneDoc.getPurpose(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getPurpose());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getResults())) {
            doc.add(new Field("results", actLuceneDoc.getResults(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getResults());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getContactName())) {
            doc.add(new Field("numcont", actLuceneDoc.getContactName(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getContactName());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getCrisNumber())) {
            doc.add(new Field("CRIS", actLuceneDoc.getCrisNumber(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getCrisNumber());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getBudgetCodeProjectId())) {
            doc.add(new Field("budgetNumber", actLuceneDoc.getBudgetCodeProjectId(), 
                    Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getBudgetCodeProjectId());
        }
        if (StringUtils.isNotBlank(actLuceneDoc.getBudgetCodes())) {
            doc.add(new Field("newBudgetNumber", actLuceneDoc.getBudgetCodes(), Field.Store.NO, Field.Index.ANALYZED));
            all = all.concat(" " + actLuceneDoc.getBudgetCodes());
        }

        int i = 0;
        if (actLuceneDoc.getComponentCodes() != null && actLuceneDoc.getComponentCodes().size() > 0) {
            for (String value : actLuceneDoc.getComponentCodes()) {
                if (value!=null){
                    doc.add(new Field("componentcode_"+String.valueOf(i), value, Field.Store.NO, Field.Index.ANALYZED));
                    all = all.concat(" " + value);
                }
                i++;
            }
        }

        if (all.length() == 0)
            return null;

        doc.add(new Field("all", all, Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }

    public static void addUpdatePledge(String rootRealPath, boolean update, AmpPledgeFake newfakePledge){
        logger.info("Updating activity pledge!");
        try {

            if (update/* && false*/) {
                if (!deletePledge(rootRealPath, newfakePledge.getAmpId())) {
                    logger.warn("Lucene.addUpdatePledge(): didn't delete pledges from index.");
                }
            }

            IndexWriter indexWriter = null;
            Directory directory = FSDirectory.open(new File(rootRealPath + PLEDGE_INDEX_DIRECTORY));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, LuceneUtil.ANALYZER);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            Document doc = null;
            doc = pledge2Document(newfakePledge.getAmpId(), newfakePledge.getName(), newfakePledge.getAdditionalInfo());

            if (doc != null) {
                try {
                    indexWriter.addDocument(doc);
                    indexWriter.forceMerge(1);
                    indexWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void addUpdateActivity(String rootRealPath, boolean update, Site site, Locale navigationLanguage, 
            AmpActivityVersion newActivity, AmpActivityVersion previousActivity) {
        addUpdateActivity(rootRealPath, update, site, navigationLanguage, newActivity, previousActivity, null);
    }


    public static void addUpdateActivity(String rootRealPath, boolean update, Site site, Locale navigationLanguage, 
            AmpActivityVersion newActivity, AmpActivityVersion previousActivity, 
            List<AmpContentTranslation> translations) {
        
        logger.info("Updating Lucene index for activity!");
        String projectid = newActivity.getAmpId();

        // In theory, it's not possible, but on practice it happens for some reason
        if (projectid == null || newActivity.getName() == null) {
            return;
        }

        try {
            if (update/* && false*/) {
                if (!deleteActivity(rootRealPath, previousActivity.getAmpActivityId())) {
                    logger.warn("Lucene.addUpdateActivity(): didn't delete the activity fron index");
                }
            }
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, LuceneUtil.ANALYZER);
            indexWriterConfig.setWriteLockTimeout(LOCK_OBTAIN_WAIT_FOREVER);
            Directory directory = FSDirectory.open(new File(rootRealPath + ACTIVITY_INDEX_DIRECTORY));
            try (IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig)) {

                ArrayList<String> componentsCode = new ArrayList<String>();
                if (newActivity.getComponents() != null && Hibernate.isInitialized(newActivity.getComponents())) {
                    for (AmpComponent c : newActivity.getComponents()) {
                        componentsCode.add(c.getCode());
                    }
                }

                String language = navigationLanguage.getLanguage();

                ActivityLuceneDocument actLuceneDoc = new ActivityLuceneDocument();
                actLuceneDoc.setAmpActivityId(String.valueOf(newActivity.getAmpActivityId()));
                actLuceneDoc.setProjectId(projectid);
                actLuceneDoc.setName(String.valueOf(newActivity.getName()));
                actLuceneDoc.setDescription(DbUtil.getEditorBody(site, newActivity.getDescription(), language));
                actLuceneDoc.setObjective(DbUtil.getEditorBody(site, newActivity.getObjective(), language));
                actLuceneDoc.setPurpose(DbUtil.getEditorBody(site, newActivity.getPurpose(), language));
                actLuceneDoc.setResults(DbUtil.getEditorBody(site, newActivity.getResults(), language));
                actLuceneDoc.setContactName(DbUtil.getEditorBody(site, newActivity.getContactName(), language));
                actLuceneDoc.setCrisNumber(newActivity.getCrisNumber());
                actLuceneDoc.setBudgetCodeProjectId(newActivity.getBudgetCodeProjectID());
                actLuceneDoc.setBudgetCodes(LuceneUtil.getBudgetCodesForActivity(newActivity));
                actLuceneDoc.setComponentCodes(componentsCode);
                actLuceneDoc.setTranslations(translations);

                Document doc = activityToLuceneDocument(actLuceneDoc);

                if (doc != null) {
                    try {
                        indexWriter.addDocument(doc);
                        indexWriter.forceMerge(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in updating lucene index for activity", e);
        }
    }

    private static String getBudgetCodesForActivity(AmpActivityVersion newActivity) {
        StringBuilder sBuffer = new StringBuilder();
        if (newActivity.getOrgrole() != null && Hibernate.isInitialized(newActivity.getOrgrole()) ) {
            for (AmpOrgRole role : newActivity.getOrgrole()) {
                if(role.getBudgets()!=null && Hibernate.isInitialized(role.getBudgets())){
                    for (AmpOrgRoleBudget budget : role.getBudgets()){
                        if (budget.getBudgetCode() != null) {
                            sBuffer.append(budget.getBudgetCode()).append(" ; ");
                        }
                    }
                }
            }
        }

        return sBuffer.toString();
    }


    /**
     * @deprecated Shouldn't be used anymore, code commented because it doesn't work anymore
     * @param request
     * @param update
     * @param id
     */
    @Deprecated
    public static void addUpdateActivity(HttpServletRequest request, boolean update, Long id) {
        /**
         * Code removed
         */
    }


    public static Document[] search(String index, String field, String searchString){
        return LuceneUtil.search(index, field, searchString, MAX_LUCENE_RESULTS, true, "0");
    }

    public static Document[] search(String index, String field, String searchString, String searchMode){
        return LuceneUtil.search(index, field, searchString, MAX_LUCENE_RESULTS, true, searchMode);
    }


    /**
     * Searches for similar {@link AmpActivityVersion}S based on title
     * similarity<br/>
     * The settings are tuned for short text strings (the title), so
     * if you adapt this to search on fields like
     * {@link AmpActivityFields#getDescription()} make sure to tune
     * {@link MoreLikeThis#setMinDocFreq(int)} and
     * {@link MoreLikeThis#setMinTermFreq(int)}
     *
     * @param index
     *            the {@link LuceneUtil}{@link #ACTVITY_INDEX_DIRECTORY}
     * @param origSearchString
     *            the text searched as {@link AmpActivityFields#getName()} which in
     *            {@link LuceneUtil#activityToLuceneDocument(ActivityLuceneDocument)}
     *            is indexed as "title"
     * @param langCode for multilingual activities is the lang code we are looking for
     *        If activity is NOT multilingual, the language code is not used, even if the default language is not English
     * @param maxLuceneResults
     *            the maximum number of results returned
     * @see MoreLikeThis
     * @return a list of similar {@link AmpActivityVersion} titles
     *
     */
    public static List<ActivityLuceneDocument> findActivitiesMoreLikeThis(String index,
                                                               String origSearchString,
                                                               String langCode,
                                                               int maxLuceneResults) {
        IndexSearcher indexSearcher = null;
        IndexReader ir = null;
        logger.info("Searching similar activities with title:  " + origSearchString + " with language " + langCode
                + " on Lucene directory: " + index);
        try {
            ir = IndexReader.open(FSDirectory.open(new File(index)), false);
            logger.info("Lucene index reader has " + ir.numDocs() + " docs in it");
            indexSearcher = new IndexSearcher(ir);

            MoreLikeThis mlt = new MoreLikeThis(ir);
            mlt.setMinDocFreq(1);
            mlt.setMinWordLen(2);
            mlt.setBoost(true);
            mlt.setMinTermFreq(1);

            String fieldName = "name";
            if (langCode != null) {
                fieldName += "_";
                fieldName += langCode;
            }

            mlt.setFieldNames(new String[] { fieldName });
            mlt.setAnalyzer(ANALYZER);
            //System.out.println("mlt.describeparams="+mlt.describeParams());


            Reader reader = new StringReader(origSearchString);
            Query query = mlt.like(reader);
            reader.close();
            TopDocs topDocs = indexSearcher.search(query, maxLuceneResults);
            logger.info("found " + topDocs.totalHits + " topDocs");

            int minDocumentScore = Integer.parseInt(FeaturesUtil
                    .getGlobalSettingValue(GlobalSettingsConstants.ACTIVITY_TITLE_SIMILARITY_SENSITIVITY));


            List<ActivityLuceneDocument> activityLuceneDocuments = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                //skip documents with a score lower than #minDocumentScore
                if (scoreDoc.score < minDocumentScore) {
                    continue;
                }

                Document doc = indexSearcher.doc(scoreDoc.doc);
    
                ActivityLuceneDocument activityLuceneDocument = new ActivityLuceneDocument();
                activityLuceneDocument.setProjectId(doc.get(ID_FIELD));
                activityLuceneDocument.setAmpActivityId(doc.get(AMP_ID_FIELD));
                // Set the title of the activity
                activityLuceneDocument.setName(doc.get(fieldName));
                logger.info("Found similar named activity with a score: " + scoreDoc.score + " Title: "
                        + activityLuceneDocument.getName() + " AmpId: " + activityLuceneDocument.getAmpActivityId()
                        + " ActivityId: " + activityLuceneDocument.getProjectId());
                activityLuceneDocuments.add(activityLuceneDocument);
            }

            return activityLuceneDocuments;

        } catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                ir.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                indexSearcher.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String escape(String s) {
        StringBuilder bld = new StringBuilder(); 
        for (int i = 0; i < s.length(); i++) {
            bld.append("\\");
            bld.append(s.charAt(i));
        }
        return bld.toString();
    }

    private static String getEscapedSearchString(String searchString) {
        List<String> symbols = Arrays.asList("+", "-", "&&", "||", "!", "(", ")", "{", "}",
                "[", "]", "^", "\"", "~", "*", "?", ":", "\\", "/");
        for (String symbol : symbols) {
            searchString = searchString.replace(symbol, escape(symbol));
        }
        return searchString;
    }

    private static String buildWrapKeyword(String searchString) {
        searchString = getEscapedSearchString(searchString);
        StringBuilder bld = new StringBuilder();
        for (String word : searchString.split(" ")) {
            bld.append(String.format("\"%s\" ", word));
        }
        return bld.toString().trim();
    }

    private static List<FuzzyQuery> buildFuzzyQueryList(String searchString, QueryParser parser) {
        searchString = parser.escape(searchString);
        List<FuzzyQuery> fuzzyTerms = new ArrayList<FuzzyQuery>();
        String[] keywords = searchString.split(" ");
        Set<String> ampIds = ActivityUtil.findExistingAmpIds(Arrays.asList(keywords));
        for (String word : keywords) {
            if (StringUtils.isNotBlank(word) && word.length() > 1) {
                FuzzyQuery fuzzyQuery = null;
                try {
                    Query query = parser.parse(word);
                    if (query instanceof PhraseQuery) {
                        if (((PhraseQuery) query).getTerms() != null) {
                            for (Term term : ((PhraseQuery) query).getTerms()) {
                                fuzzyQuery = new FuzzyQuery(term, getMinimumSimilarity(term.text(), ampIds.contains(term.text())));
                                fuzzyTerms.add(fuzzyQuery);
                            }
                        }
                    } else {
                        if (query instanceof TermQuery) {
                            fuzzyQuery = new FuzzyQuery(((TermQuery) query).getTerm(), getMinimumSimilarity(word, ampIds.contains(word)));
                            fuzzyTerms.add(fuzzyQuery);
                        }
                    }
                } catch (ParseException e) {
                    logger.error("Error while building fuzzy query list");
                    throw new RuntimeException("Error while building fuzzy query list", e);
                }
            }
        }
        return fuzzyTerms;
    }

    private static boolean isFuzzy() {
        return FeaturesUtil.isVisibleField(FUZZY_SEARCH_FM_ENTRY);
    }

    private static float getMinimumSimilarity(String word, boolean isAmpId) {
        boolean isNumeric = word.chars().allMatch(Character::isDigit);
        if (isNumeric || isAmpId) {
            return MINIMUM_SIMILARITY_TO_NUMBERS;
        }
        float minimumSimilarity =
                FeaturesUtil.getGlobalSettingDouble(GlobalSettingsConstants.FUZZY_SEARCH_DISTANCE).floatValue();
        return minimumSimilarity != -1f ? minimumSimilarity : DEFAULT_MINIMUM_SIMILARITY;
    }

    public static Document[] search(String index, String field, String origSearchString, int maxLuceneResults, boolean retry, String searchMode) {
        QueryParser parser = new QueryParser(Version.LUCENE_36, field, ANALYZER);
        parser.setDefaultOperator(getDefaultOperator(searchMode));
        Query query = null;
        Document[] resultDocuments = null;
        IndexSearcher indexSearcher = null;
        try {
            indexSearcher = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(index)), false));

            if (isFuzzy()) {
                query = getFuzzyQuery(origSearchString, parser);
            } else {
                query = getStandardQuery(origSearchString, parser);
            }

            if (query != null) {
                TopDocs topDocs = indexSearcher.search(query, maxLuceneResults);
                resultDocuments = new Document[topDocs.totalHits > topDocs.scoreDocs.length ? topDocs.scoreDocs.length
                        : topDocs.totalHits];
                for (int i = 0; i < topDocs.totalHits; i++) {
                    resultDocuments[i] = indexSearcher.doc(topDocs.scoreDocs[i].doc);
                }
            } else {
                resultDocuments = new Document[0];
            }
        } catch (BooleanQuery.TooManyClauses e1) {
            //TODO Make lucene search only through the activities from the current workspace
            String msg = "Too many clauses found. More than " + maxLuceneResults + ".";
            if (retry) {
                msg += "Will retry with " + maxLuceneResults * 10;
            }
            logger.warn(msg);
            if (retry) {
                return LuceneUtil.search(index, field, origSearchString, maxLuceneResults * 10, false, searchMode);
            } else {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDocuments;
    }

    private static QueryParser.Operator getDefaultOperator(String searchMode) {
        if (LuceneUtil.SEARCH_MODE_AND.toString().equals(searchMode)) {
            return QueryParser.AND_OPERATOR;
        } else {
            return QueryParser.OR_OPERATOR;
        }
    }

    private static Query getStandardQuery(String origSearchString, QueryParser parser) throws ParseException {
        return parser.parse(QueryParser.escape(buildWrapKeyword(origSearchString)));
    }

    private static Query getFuzzyQuery(String origSearchString, QueryParser parser) {
        Query query;
        List<FuzzyQuery> keywords = buildFuzzyQueryList(origSearchString, parser);
        Occur occur = getOccur(parser);
        if (keywords.isEmpty()) {
            return null;
        }
        if (keywords.size() == 1) {
            query = keywords.get(0);
        } else {
            BooleanQuery q = new BooleanQuery();
            for (FuzzyQuery fq : keywords) {
                q.add(new BooleanClause(fq, occur));
            }
            query = q;
        }
        return query;
    }

    private static Occur getOccur(QueryParser parser) {
        Occur occur = Occur.SHOULD;
        if (parser.getDefaultOperator() == QueryParser.AND_OPERATOR) {
            occur = Occur.MUST;
        }
        return occur;
    }

    /**
     * Uses {@link isDir()} method to determine whether 
     * index directory exists or not. 
     * If directory doesn't exist create new one using
     * {@link addUpdatehelp(boolean)}
     *
     * @throws org.digijava.kernel.exception.DgException
     *
     */

    public static void createHelp(ServletContext sc, ModuleInstance modInstance , String lang) throws  DgException{
//      boolean createDir = LuceneUtil.checkHelpDir(sc);
        boolean createDir = LuceneUtil.checkHelpDir(sc,modInstance,lang);

        if(!createDir){
            logger.info("Building the help");
            LuceneUtil.addUpdatehelp(false, sc,modInstance , lang);
        }

    }

    /**
     * Converts html formatted help topics body to plain text format.
     * Creates lucene-index directory if it doesn't exist.
     * Adds or updates converted data to lucene-index directory
     * using {@link indexArticle(String,String,String)} method.
     * <p>
     * In update mode method updates only  data which have last 
     * modified date is greater than the  last modified date of lucene-index directory.
     *
     * @param update if true then method is used to update the lucene-index directory
     * otherwise to create new lucene-index directory and add data to it.
     *
     * @throws org.digijava.kernel.exception.DgException
     *
     * @see org.digijava.module.help.helper.HelpSearchData
     */

    public static void addUpdatehelp(boolean update, ServletContext sc,ModuleInstance modInstance , String lang ) throws DgException {

        HelpSearchData item = new HelpSearchData();
        DateFormat formatter ;
        Date date ;

        try{
            String dir = sc.getRealPath("/") +  HELP_INDEX_DIRECTORY + "/" + modInstance.getInstanceName() + "_" + lang;
            Directory directory = FSDirectory.open(new File(dir));
            Long lastLucModDay = IndexReader.lastModified(directory);

            formatter  = new SimpleDateFormat();
            String leastUpDate = formatter.format(lastLucModDay);
            date = (Date)formatter.parse(leastUpDate);

            Collection data =  HelpUtil.getAllHelpData();

            for (Iterator<HelpSearchData> iter = data.iterator(); iter.hasNext();) {

                item = (HelpSearchData) iter.next();

                String article =  item.getBody();
                //String title = item.getTopicKey();
                String titTrnKey = item.getBodyKey();
                String language = item.getLang();
                String title =HelpUtil.getTrn(item.getTopicKey(), language,new Long(3));
                // Converts html formatted help topics body to plain text format.
                String newCode = article.replaceAll("\\<.*?\\>","");

                if(update){
                    if(item.getLastModDate().after(date)){
                        deleteHelp("title",title, sc);
                        indexArticle(newCode, title,titTrnKey,language,sc);
                    }
                }else if(!update){
                    indexArticle(newCode, title,titTrnKey,language,sc);
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
            throw new DgException(ex);
        }
    }






    /**
     * Searches searchString in the indexDirectory for fields.
     * Returns founded hits
     *
     * @param field
     * @param searchString
     * @return founded hits
     */
    public static AmpLuceneTopDocs helpSearch(String field, String searchString, ServletContext sc) {

        QueryParser parser = new QueryParser(Version.LUCENE_36, field, ANALYZER);
        Query query = null;
        AmpLuceneTopDocs luceneTopDocs = new AmpLuceneTopDocs();

        IndexSearcher indexSearcher = null;
        try {
            if (searchString != null) {
                Directory directory = FSDirectory.open(new File(sc.getRealPath("/") + HELP_INDEX_DIRECTORY));
                IndexReader indexReader = IndexReader.open(directory);
                indexSearcher = new IndexSearcher(indexReader);
                searchString = QueryParser.escape(searchString.trim());
                query = parser.parse("+"+searchString+"*");

                TopDocs topDocs = indexSearcher.search(query, MAX_LUCENE_RESULTS);
                
                ScoreDoc[] hits = topDocs.scoreDocs;
                for (int i = 0; i < hits.length; i++) {
                    float score = hits[i].score;
                    Document document = indexSearcher.doc(hits[i].doc);
                    luceneTopDocs.addDocument(new AmpLuceneDoc(document, score));
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return luceneTopDocs;
    }

    /**
     * Returns highlighted object
     *
     * @param field
     * @param searchString
     * @return highlight object
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryParser.ParseException
     * @throws InvalidTokenOffsetsException 
     */
    public static Object highlighter(Field field, String searchString, ServletContext sc) throws IOException, 
    ParseException, InvalidTokenOffsetsException {
        Query query = null;
        QueryParser parser = new QueryParser(Version.LUCENE_36, "article", ANALYZER);

        query = parser.parse(QueryParser.escape(searchString));

        Object hA = null;
        try {
            hA = highlight(field, query, sc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return hA;
    }

    /**
     * Returns highlighted object
     * @throws InvalidTokenOffsetsException 
     *
     */
    private static Object highlight(Field field, Query query, ServletContext sc) throws IOException, 
        InvalidTokenOffsetsException {

        query.rewrite(IndexReader.open(FSDirectory.open(new File(sc.getRealPath("/") + HELP_INDEX_DIRECTORY)), false));
        QueryScorer scorer = new QueryScorer(query);
        SimpleHTMLFormatter formatter =
                new SimpleHTMLFormatter("<span class=\"highlight\">",
                        "</span>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        Fragmenter fragmenter = new SimpleFragmenter(50);
        highlighter.setTextFragmenter(fragmenter);

        String value = field.stringValue();
        TokenStream tokenStream = new StandardAnalyzer(Version.LUCENE_36)
                .tokenStream(field.name(), new StringReader(value));

        return highlighter.getBestFragments(tokenStream, value, 5, "...");
    }


    /**
     * Creates {@link Document} using {@link createDocument(String,String,String)}.
     * Adds newly created document to lucene help directory
     *
     * @param article body of help topic
     * @param title title of help topic
     * @param titTrnKey translation key used to translate title
     * @throws java.lang.Exception
     * @deprecated instead use {@link LuceneWorker#addItemToIndex(Object, ServletContext, String)} method
     */
    public static void indexArticle(String article, String title,String titTrnKey, String lang,ServletContext sc)
            throws Exception {
        Document document = LuceneUtil.createHelpDocument(article,title,titTrnKey,lang);
        LuceneUtil.indexHelpDocument(document, sc);
    }

    /**
     * Creates new {@link Document}. 
     * Adds fields title,titletrnKey,article 
     * to document using passed parameters.
     *
     * @param article body of help topic
     * @param title title of help topic
     * @param titTrnKey translation key used to translate title
     * @return newly created document
     */
    public static Document createHelpDocument(String article, String title,String titTrnKey,String lang){

        Document document = new Document();
        document.add(new Field("title",title,Field.Store.YES,Field.Index.ANALYZED));
        document.add(new Field("titletrnKey",titTrnKey,Field.Store.YES,Field.Index.ANALYZED));
        document.add(new Field("article",article,Field.Store.YES,Field.Index.ANALYZED));
        document.add(new Field("lang",lang,Field.Store.YES,Field.Index.ANALYZED));
        return document;

    }


    /**
     * Shows whether lucene help
     * directory exists or no
     *
     * @return true if lucene-index directory exists otherwise false
     */

    public static boolean checkHelpDir(ServletContext sc, ModuleInstance modInstance, String lang) {
        String dir = sc.getRealPath("/") + HELP_INDEX_DIRECTORY + "/" + modInstance.getInstanceName() + "_" + lang;
        boolean createDir = false;
        try {
            createDir = IndexReader.indexExists(FSDirectory.open(new File(dir)));
        } catch (IOException e) {
            
        }
        
        return createDir;
    }


    /**
     * Creates lucene help
     * directory if it doesn't exist.
     * Adds document to it 
     *
     * @param document
     * @throws java.io.IOException
     */
    public static void indexHelpDocument(Document document, ServletContext sc) throws IOException {
        try {
            
            String dir = sc.getRealPath("/") + HELP_INDEX_DIRECTORY;
            Directory directory = FSDirectory.open(new File(dir));
        
            StandardAnalyzer analyzer  = new StandardAnalyzer(Version.LUCENE_36);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            
            IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
            writer.addDocument(document);
            writer.close();
        } catch (IOException e) {
            logger.error("", e);
            throw e;
        }
    }
    /**
     *
     * @param field
     * @param search
     * @deprecated instead of this {@link LuceneWorker#deleteItemFromIndex(Object, ServletContext, String)} is used
     */
    @Deprecated
    public static void deleteHelp(String field, String search, ServletContext sc){
        Term term = new Term(field,search);
        Directory directory;
        IndexReader indexReader;

        try {
            indexReader = IndexReader.open(FSDirectory.open(new File(sc.getRealPath("/") + HELP_INDEX_DIRECTORY)));
            Integer deleted = indexReader.deleteDocuments(term);
            indexReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
