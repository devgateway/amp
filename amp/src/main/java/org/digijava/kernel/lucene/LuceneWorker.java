package org.digijava.kernel.lucene;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpLuceneIndexStamp;
import org.digijava.module.aim.util.LuceneUtil;
import org.jfree.util.Log;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lucene worker contains general code for working with Lucene.
 * It is oriented on AMP modules hence name of items it works with is {@link LucModule}.
 * For example Translation implements this interface in LucTranslationModule.
 * If we want add new type of objects to index, then we should implement module for those type 
 * and provide specific information through this new implementation. Then we should add this module
 * in static initializer section of this worker.
 * @see LucModule
 * @author Irakli Kobiashvili
 *
 */
public class LuceneWorker {
    
    private static Logger logger = Logger.getLogger(LuceneWorker.class);

    /**
     * Lucene folder name inside AMP folder.
     */
    private static final String LUCENE_BASE_DIR = "lucene";
    
    /**
     * Lucene stamp files extensions.
     */
    private static final String LUCENE_STAMP_EXT = ".stamp";
    
    /**
     * Stores all modules by class name they work with.
     */
    private static Map<String, LucModule<?>> modulesByClass = null;//new HashMap<String, LucModule<?>>();
    
    /**
     * Seperates suffix from module name or key.
     */
    private static final String SUFFIX_SEPARATOR = "_";
    
    /**
     * Is worker initialized or not.
     */
    private static volatile boolean initialized = false;
    
    /**
     * Lock for initialization process
     */
    private static ReentrantLock initLock = new ReentrantLock();
    
    /**
     * Initializes Lucene.
     * Checks each module and if needed rebuilds their indexes.
     * @param context
     */
    public static void init(ServletContext context,LuceneModuleRegistry registry){
        initLock.lock();
        if (initialized){
            initLock.unlock();
            throw new RuntimeException("LuceneWorker already initialized!");
        }
        loadModules(registry);
        checkModuleIndexes(context);
        initialized = true;
        initLock.unlock();
    }

    /**
     * Checks each module index and rebuilds it if required.
     * @param context
     */
    private static void checkModuleIndexes(ServletContext context) {
        boolean rebuildAllIndexes = needToRebuilAllIndexes();
        if (rebuildAllIndexes) logInfo(null,"Forced all indexes to rebuild");
        Collection<LucModule<?>> modules = getAllKnownModules();
        for (LucModule<?> module : modules) {
            if (rebuildAllIndexes || needIndexRebuild(module, context)){
                try {
                    recreateIndext(module, context);
                } catch (DgException e) {
                    logger.error(e.getMessage(), e);
                }
                //Instead we can put modules in queue and create indexes later or in seperate thread.
            }else{
                logInfo(module,"Index ok.");
            }
        }
    }
    
    /**
     * Loads all lucene modules from registry.
     */
    private static void loadModules(LuceneModuleRegistry registry){
        if (modulesByClass != null) throw new RuntimeException("modules map already initialized");
        modulesByClass = new HashMap<String, LucModule<?>>();
        Map<String, LucModule<?>> modulesByKey = new HashMap<String, LucModule<?>>();
        
        List<LucModule<?>> modules = registry.getAll();

        //Add all to map.
        for (LucModule<?> lucModule : modules) {
            String moduleKey = getModuleKey(lucModule);
            LucModule<?> oldModule = modulesByKey.get(moduleKey);
            if (oldModule!=null) throw new RuntimeException("Duplicate module key: "+moduleKey);
            modulesByClass.put(moduleKey, lucModule);
        }
    }
    
    private static Collection<LucModule<?>> getAllKnownModules(){
        return modulesByClass.values();
    }

    /**
     * Checks if index rebuild for module is required.
     * @param module
     * @param context
     * @return true - index should be rebuild. false - no need in rebuild.
     */
    private static boolean needIndexRebuild(LucModule<?> module, ServletContext context){
        return (!checkIndexDirExists(module, context)) || !stampIsOk(module, context) || (module.needIndexRebuild());
    }
    
    /**
     * Check if module directory exists
     * @param module
     * @param context
     * @return
     */
    private static boolean checkIndexDirExists(LucModule<?> module, ServletContext context){
        String dir = getModuleDirPath(module, context);
        boolean exists = false;
        
        try {
            exists = IndexReader.indexExists(FSDirectory.open(new File(dir)));
        } catch (IOException e) {
            Log.error("Error in opening index directory");
        }
        
        if(!exists){
            logInfo(module, "Index directory missing. Need rebuild.");
        }
        return exists;
    }
    
    /**
     * Returns path to directory of module.
     * @param module
     * @param context
     * @return
     */
    private static String getModuleDirPath(LucModule<?> module, ServletContext context){
        String dir = context.getRealPath("/") + LUCENE_BASE_DIR + "/" + module.getName();
        String subDir = module.getSuffix();
        if (subDir !=null && !subDir.trim().equalsIgnoreCase("")){
            dir += "/"+subDir;
        }
        return dir;
    }
    
    /**
     * Returns path to stamp file of the module.
     * @param module
     * @param context
     * @return path to stamp file of the module.
     */
    private static String getStampFilePath(LucModule<?> module, ServletContext context){
        return context.getRealPath("/") + LUCENE_BASE_DIR + "/" + getModuleNameAndSuffix(module) + LUCENE_STAMP_EXT;
    }
    
    /**
     * Checks if module stamp file and db record are all right.
     * If not then this will trigger index and stamp file rebuild.
     * Actually stamp file contains serial ID of the module class and check if value in file matches current class serial version UID.
     * If it does not then this means that there are some changes in algorithm of index generation or search 
     * and so we need indexes to be rebuilt.  
     * @param module
     * @param context
     * @return true - stamp is ok, no need to rebuild index. false - stamp is not ok, we need to rebuild index.
     */
    private static boolean stampIsOk(LucModule<?> module, ServletContext context){
        
        boolean stampOK=true;
        String stampFileName = getStampFilePath(module, context);
        long serialVersionUIDfromModule = module.getSerialVersionUID();

        try {

            File stampFile = new File(stampFileName);
            
            if (!stampFile.exists()){
                stampOK = false;
                logWarn(module,"Stamp file not found: need rebuild.");
            }else if (!stampFile.canRead()){
                stampOK = false;
                logWarn(module,"Stamp file is not readable: need rebuild.");
                //at least lets try to delete and create file again cos it may be stupid file permissions.
                //but usually we should not see this message in logs. 
            }else{

                //Open stamp file, read two long values and close file.
                DataInputStream stream          = new DataInputStream(new FileInputStream(stampFile));
                long serialVersionUIDfromStamp  = stream.readLong();
                long dbIdFromStamp              = stream.readLong();
                stream.close();
                //Now compare serial version UIDs from module and file. 
                if (serialVersionUIDfromModule != serialVersionUIDfromStamp){
                    stampOK = false;
                    logWarn(module,"Algorithm serial ID mismatch : need rebuild.");
                }else{
                    //if code and file serial version UIDs are ok, then:
                    try {
                        //load stamp value from db and compare with value from file.
                        AmpLuceneIndexStamp stamp = LuceneUtil.getIdxStamp(getModuleNameAndSuffix(module));
                        if (stamp ==null || stamp.getStamp().longValue() != dbIdFromStamp){
                            stampOK = false;
                            logWarn(module,"DB timestamp mismatch: need rebuild.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        stampOK = false;
                    }
                }
            }
            
        } catch (IOException e) {
            stampOK = false;
            logWarn(module,"Cannot read from file. Need rebuild. Message: "+e.getMessage());
        }
        return stampOK;
    }
    
    /**
     * Recreates index of the module.
     * @param <E>
     * @param module
     * @param context
     * @throws DgException 
     */
    public static <E> void recreateIndext(LucModule<E> module, ServletContext context) throws DgException{
        try {
            String dir = getModuleDirPath(module, context);
            LuceneUtil.deleteDirectory(dir);
            Directory directory = FSDirectory.open(new File(dir));
            logInfo(module,"Index directory deleted.");
            Analyzer analyzer = module.getAnalyzer();
            logInfo(module,"Loading items to index...");
            List<E> items = module.getItemsToIndex();
            long startTime = System.currentTimeMillis();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
            IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
            if (items!=null && items.size()>0){
                logInfo(module,"Creating index for "+items.size()+" items. This may take some time...");
                for (E item : items) {
                    Document doc = module.convertToDocument(item);
                    writer.addDocument(doc);
                }
            }else{
                logInfo(module,"Creating empty index.");
            }
            writer.optimize();
            writer.close();
            long stopTime = System.currentTimeMillis();
            long seconds = (stopTime - startTime)/1000;
            logInfo(module,"Index rebuild finished in "+seconds+" seconds.");
            //recreate file and db stamps.
            recreateFileStamp(module, context, module.getSerialVersionUID(), stopTime);
            rectreateDbStamp(module, stopTime);
        } catch (IOException e) {
            throw new DgException("Cannot recreate index for "+module.getName(),e);
        }
    }
    
    /**
     * Recreates stamp file for module.
     * @param module
     * @param context
     * @param serialVersionUID
     * @param stopTimestamp
     * @throws IOException 
     */
    private static void recreateFileStamp(LucModule<?> module, ServletContext context, long serialVersionUID, long stopTimestamp) throws IOException{
            String stampFilePath = getStampFilePath(module, context);
            File stampFile = new File(stampFilePath);
            if (stampFile.exists() && stampFile.delete()){
                logInfo(module,"Stamp file deleted.");
            }
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(stampFile));
            outputStream.writeLong(serialVersionUID);
            outputStream.writeLong(stopTimestamp);
            outputStream.close();
            logInfo(module,"Stamp file created.");
    }

    /**
     * Recreates db timestamp for module.
     * @param module
     * @param stopTimestamp
     * @throws DgException 
     */
    private static void rectreateDbStamp(LucModule<?> module, long stopTimestamp) throws DgException {
        String moduleFullName = getModuleNameAndSuffix(module);
        if (LuceneUtil.deleteIdxStamps(moduleFullName)){
            logInfo(module,"DB stamp deleted.");
        }
        LuceneUtil.createStamp(moduleFullName, stopTimestamp);
        logInfo(module,"DB stamp created.");
    }
    
    private static boolean needToRebuilAllIndexes(){
        //TODO do some check like modules do.
        return false;
    }

    /**
     * Forms module name including separator and suffix.
     * If suffix is null than only module name is retrned.
     * @param module
     * @return
     */
    private static String getModuleNameAndSuffix(LucModule<?> module){
        String name = module.getName();
        String suffix = module.getSuffix();
        if (suffix !=null && !suffix.trim().equalsIgnoreCase("")){
            name += SUFFIX_SEPARATOR + suffix;
        }
        return name;
    }
    
    /**
     * Constructs module key from module.
     * Retrieves class and suffix from module.
     * @param <E>
     * @param module
     * @return
     */
    private static <E> String getModuleKey(LucModule<E> module){
        String className = module.getItemClass().getName();
        String suffix = module.getSuffix();
        if (suffix!=null){
            className += SUFFIX_SEPARATOR + suffix;
        }
        return className;
    }

    /**
     * Constructs key for module from class name and suffix.
     * If suffix is null then only class name is used.
     * Used when we want to find module from some item written in index.
     * @param <E>
     * @param clazz
     * @param suffix
     * @return
     */
    private static <E> String getModuleKey(Class<E> clazz, String suffix){
        String className = clazz.getName();
        if (suffix!=null){
            className += SUFFIX_SEPARATOR + suffix;
        }
        return className;
    }

    /**
     * Retrieves module for clazz and suffix.
     * @param <E>
     * @param clazz
     * @param suffix
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <E> LucModule<E> getModule(Class<E> clazz, String suffix){
        LucModule<E> module = (LucModule<E>) modulesByClass.get(getModuleKey(clazz, suffix)); 
        return module;
    }
    
    /**
     * Retrieves module for item and suffix.
     * Used when we want to find module of one particular item by its class.
     * @param <E>
     * @param item
     * @param suffix
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <E> LucModule<E> getModule(E item, String suffix){
        String key = getModuleKey(item.getClass(), suffix);
        return (LucModule<E>) modulesByClass.get(key);
    }
    
    /**
     * Adds item to index.
     * Which module should be used is determined from item class E.
     * @param <E>
     * @param item
     * @param context
     */
    public static <E> void addItemToIndex(E item, ServletContext context) throws DgException{
        addItemToIndex(item, context,null);
    }

    /**
     * Adds item to index.
     * Module to work with is determined from E class and suffix.
     * @param <E>
     * @param item
     * @param context
     * @param suffix
     * @throws DgException
     */
    public static <E> void addItemToIndex(E item, ServletContext context, String suffix) throws DgException{
        
        LucModule<E> module = getModule(item, suffix);
        
        if (module!=null){
            try {
                Document doc = module.convertToDocument(item);
                String dir = getModuleDirPath(module, context);
                Directory directory = FSDirectory.open(new File(dir)); 
                Analyzer analyzer = module.getAnalyzer();
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
                IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
                writer.addDocument(doc);
                writer.close();
            } catch (IOException e) {
                throw new DgException("Cannot add item to index.",e);
            }
        }else{
            //TODO do something if we do not have indexes of
            logger.error("Cannot fined module for item: "+item + " and suffix: "+suffix);
        }
    }

    /**
     * Deletes item from index.
     * Call this when removing entity from db which is also in index.
     * @param <E>
     * @param item
     * @param context
     * @throws DgException
     */
    public static <E> void deleteItemFromIndex(E item, ServletContext context) throws DgException{
        deleteItemFromIndex(item, context, null);
    }
    
    /**
     * Deletes item from index.
     * Item class and suffix value are used to determine index to work with.
     * @param <E>
     * @param item
     * @param context
     * @param suffix
     * @throws DgException
     */
    public static <E> void deleteItemFromIndex(E item, ServletContext context, String suffix) throws DgException{
        LucModule<E> module = getModule(item, suffix);
        try {
            if (module!=null){
                String dir = getModuleDirPath(module, context);
                IndexReader reader = IndexReader.open(FSDirectory.open(new File(dir)), false);
                Term term = module.getIdFieldTerm(item);
                reader.deleteDocuments(term);
                reader.close();
            }
        } catch (IOException e) {
            throw new DgException("Cannot delete item from index",e);
        }
    }
    
    /**
     * Search items in index.
     * Module (index to be searched) is determined from clazz parameter.
     * @param <E> 
     * @param clazz
     * @param textToSearch search term.
     * @param context
     * @return docs in index sorted by relevance
     * @throws DgException
     */
    public static <E> AmpLuceneTopDocs search(Class<E> clazz, String textToSearch, ServletContext context) 
            throws DgException {
        return search(clazz, textToSearch, context, null);
    }

    /**
     * Search items in index.
     * Module (index to be searched) is determined from clazz parameter and classNameSuffix.
     * Use classNameSuffix if you have separate indexes for same class.
     * @param <E>
     * @param clazz
     * @param textToSearch
     * @param context
     * @param classNameSuffix suffix for classes to separate them by some feature in different indexes. 
     * @return docs
     * @throws DgException
     */
    public static <E> AmpLuceneTopDocs search(Class<E> clazz, String textToSearch, ServletContext context, 
            String suffix) throws DgException {
        AmpLuceneTopDocs luceneTopDocs = new AmpLuceneTopDocs();
        try {
            LucModule<E> module = getModule(clazz, suffix);
            String dir = getModuleDirPath(module, context);
            String[] searchFieldNames = module.getSearchFieldNames();
            String searchText = textToSearch;
            Analyzer analyzer = module.getAnalyzer();
            IndexSearcher searcher = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(dir))));
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, searchFieldNames, 
                    analyzer);
            String escapedSearchText = QueryParser.escape(searchText);
            Query query = queryParser.parse(escapedSearchText);
            TopDocs topDocs = searcher.search(query, LuceneUtil.MAX_LUCENE_RESULTS);
            
            
            ScoreDoc[] hits = topDocs.scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                float score = hits[i].score;
                Document document = searcher.doc(hits[i].doc);
                luceneTopDocs.addDocument(new AmpLuceneDoc(document, score));
            }
            
            searcher.close();
        } catch (IOException e1) {
            throw new DgException("Cannot search index",e1);
        } catch (ParseException e2) {
            throw new DgException("Cannot search index",e2);
        }
        
        return luceneTopDocs;
    }

    /**
     * Converts docs to list of beans of specified type.
     * @param <E> type of beans in result list. Also used to determine module.
     * @param docs
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <E> List<E> docsToSortedList(AmpLuceneTopDocs topDocs, Class<E> clazz) throws IOException {
        return topDocsToSortedList(topDocs, clazz, null);
    }

    /**
     * Converts docs to list of beans of specified type.
     * @param <E>
     * @param docs
     * @param clazz
     * @param suffix
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> topDocsToSortedList(AmpLuceneTopDocs luceneTopDocs, Class<E> clazz, String suffix) 
            throws IOException {
        LucModule<E> module = getModule(clazz, suffix);
        List<E> items = null;
        if (luceneTopDocs != null && luceneTopDocs.size() > 0) {
            items = new ArrayList<E>(luceneTopDocs.size());
            for (int i = 0; i < luceneTopDocs.size(); i++) {
                E item = module.luceneDocToItem(luceneTopDocs.getDocument(i));
                items.add(item);
            }
        }
        
        return items;
    }
    
    private static void logWarn(LucModule<?> module, String message){
        log(module,message, true);
    }
    
    private static void logInfo(LucModule<?> module, String message){
        log(module,message, false);
    }
    
    private static void log(LucModule<?> module, String message, boolean warn){
        String msg = "";
        if (module!=null){
            msg+= module.getName();
            if (module.getSuffix()!=null){
                msg += SUFFIX_SEPARATOR + module.getSuffix();
            }
        }
        msg += ": " + message;
        if (warn){
            logger.info(msg);
        }else {
            logger.warn(msg);
        }
    }
    
    
}
