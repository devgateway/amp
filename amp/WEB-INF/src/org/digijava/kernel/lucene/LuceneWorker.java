package org.digijava.kernel.lucene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.impl.org.LucOrganisationModule;
import org.digijava.module.aim.dbentity.AmpLuceneIndexStamp;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.translation.lucene.LucTranslationModule;

/**
 * Lucene worker contains general code for working with Lucene.
 * It is oriented on AMP modules hence name of items it works with is {@link LucModule}.
 * For example Translation implements this interface in {@link LucTranslationModule}.
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
	private static final Map<String, LucModule<?>> modulesByClass = new HashMap<String, LucModule<?>>();

	static {
		modulesByClass.put(Message.class.getName(), new LucTranslationModule());
		modulesByClass.put(AmpOrganisation.class.getName(), new LucOrganisationModule());
	}
	
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
	public static void init(ServletContext context){
		initLock.lock();
		if (initialized){
			initLock.unlock();
			throw new RuntimeException("LuceneWorker already initialized!");
		}
		checkAllModulesNames();
		boolean rebuildAllIndexes = needToRebuilAllIndexes();
		Collection<LucModule<?>> modules = getAllKnownModules();
		for (LucModule<?> module : modules) {
			logger.info(module.getName()+": Checking index");
			if (rebuildAllIndexes || needIndexRebuild(module, context)){
				try {
					recreateIndext(module, context);
				} catch (DgException e) {
					logger.error(e);
				}
				//Instead we can put modules in queue and create indexes later or in seperate thread.
			}else{
				logger.info(module.getName()+": Index ok.");
			}
		}
		initialized = true;
		initLock.unlock();
	}
	
	private static Collection<LucModule<?>> getAllKnownModules(){
		return modulesByClass.values();
	}

	/**
	 * Checks all known modules for duplicate names and suffixes.
	 */
	private static void checkAllModulesNames(){
		Map<String, LucModule<?>> nameMap = new HashMap<String, LucModule<?>>();
		Map<String, LucModule<?>> suffixMap = new HashMap<String, LucModule<?>>();
		boolean problemFound=false;
		Collection<LucModule<?>> modules = getAllKnownModules();
		for (LucModule<?> module : modules) {
			LucModule<?> otherModule = null;
			
			otherModule = nameMap.get(module.getName());
			if (otherModule == null){
				nameMap.put(module.getName(), module);
			}else{
				logger.error("Two modules have same name: "+module.getName());
				problemFound = true;
				break;
			}
			
			otherModule = null;
			otherModule = suffixMap.get(module.getDirSuffix());
			if(otherModule == null){
				suffixMap.put(module.getDirSuffix(), module);
			}else{
				logger.error("Modules with names "+module.getName()+" and "+ otherModule.getName()+" have same suffix: "+module.getDirSuffix());
				problemFound = true;
				break;
			}
		}
		if (problemFound){
			//TODO do some action! for example throw init error to stop deploy process.
		}
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
		boolean exists = IndexReader.indexExists(dir);
		if(!exists){
			logger.info(module.getName()+": Index directory missing. Need rebuild.");
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
		return context.getRealPath("/") + LUCENE_BASE_DIR + "/" + module.getDirSuffix();
	}
	
	/**
	 * Creates path to stamp file of the module.
	 * @param module
	 * @param context
	 * @return path to stamp file of the module.
	 */
	private static String getModuleStampPath(LucModule<?> module, ServletContext context){
		return context.getRealPath("/") + LUCENE_BASE_DIR + "/" + module.getDirSuffix() + LUCENE_STAMP_EXT;
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
		String stampFileName = getModuleStampPath(module, context);
		long serialVersionUIDfromModule = module.getSerialVersionUID();

		try {

			File stampFile = new File(stampFileName);
			
			if (!stampFile.exists()){
				stampOK = false;
				logger.warn(module.getName() + ": stamp file not found: need rebuild.");
			}else if (!stampFile.canRead()){
				stampOK = false;
				logger.warn(module.getName() + ": stamp file is not readable: need rebuild.");
				//at least lets try to delete and create file again cos it may be stupid file permissions.
				//but usually we should not see this message in logs. 
			}else{

				//Open stamp file, read two long values and close file.
				DataInputStream stream 			= new DataInputStream(new FileInputStream(stampFile));
				long serialVersionUIDfromStamp 	= stream.readLong();
				long dbIdFromStamp 				= stream.readLong();
				stream.close();
				//Now compare serial version UIDs from module and file.	
				if (serialVersionUIDfromModule != serialVersionUIDfromStamp){
					stampOK = false;
					logger.warn(module.getName() + ": Algorithm serial ID mismatch : need rebuild.");
				}else{
					//if code and file serial version UIDs are ok, then:
					try {
						//load stamp value from db and compare with value from file.
						AmpLuceneIndexStamp stamp = LuceneUtil.getIdxStamp(module.getDirSuffix());
						if (stamp ==null || stamp.getStamp().longValue() != dbIdFromStamp){
							stampOK = false;
							logger.warn(module.getName() + ": DB timestamp mismatch: need rebuild.");
						}
					} catch (Exception e) {
						e.printStackTrace();
						stampOK = false;
					}
				}
			}
			
		} catch (IOException e) {
			stampOK = false;
			logger.warn(module.getName()+": cannot read from file. Need rebuild. Message: "+e.getMessage());
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
	private static <E> void recreateIndext(LucModule<E> module, ServletContext context) throws DgException{
		try {
			String dir = getModuleDirPath(module, context);
			LuceneUtil.deleteDirectory(dir);
			logger.info(module.getName()+": Index directory deleted.");
			Analyzer analyzer = module.getAnalyzer();
			logger.info(module.getName()+": loading items to index...");
			List<E> items = module.getItemsToIndex();
			long startTime = System.currentTimeMillis();
			IndexWriter writer = new IndexWriter(dir, analyzer, true);
			if (items!=null && items.size()>0){
				logger.info(module.getName()+": creating index for "+items.size()+" items. This may take some time...");
				for (E item : items) {
					Document doc = module.convertToDocument(item);
					writer.addDocument(doc);
				}
			}else{
				logger.info(module.getName()+": creating empty index.");
			}
			writer.optimize();
			writer.close();
			long stopTime = System.currentTimeMillis();
			long seconds = (stopTime - startTime)/1000;
			logger.info(module.getName()+": index rebuild finished in "+seconds+" seconds.");
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
			String stampFilePath = getModuleStampPath(module, context);
			File stampFile = new File(stampFilePath);
			if (stampFile.exists() && stampFile.delete()){
				logger.info(module.getName()+": stamp file deleted.");
			}
			DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(stampFile));
			outputStream.writeLong(serialVersionUID);
			outputStream.writeLong(stopTimestamp);
			outputStream.close();
			logger.info(module.getName()+": stamp file created.");
	}

	/**
	 * Recreates db timestamp for module.
	 * @param module
	 * @param stopTimestamp
	 * @throws DgException 
	 */
	private static void rectreateDbStamp(LucModule<?> module, long stopTimestamp) throws DgException {
		if (LuceneUtil.deleteIdxStamps(module.getDirSuffix())){
			logger.info(module.getName()+": DB stamp deleted.");
		}
		LuceneUtil.createStamp(module.getDirSuffix(), stopTimestamp);
		logger.info(module.getName()+": DB stamp created.");
	}
	
	private static boolean needToRebuilAllIndexes(){
		//TODO do some check like modules do.
		return false;
	}
	
	/**
	 * Returns module for class name.
	 * @param <E>
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <E> LucModule<E> getModuleFor(String className){
		return (LucModule<E>)modulesByClass.get(className);
	}
	
	/**
	 * Adds item to index.
	 * Which module should be used is determined from item class E.
	 * @param <E>
	 * @param item
	 * @param context
	 */
	public static <E> void addItemToIndex(E item, ServletContext context) throws DgException{
		
		LucModule<E> module = getModuleFor(item.getClass().getName());
		
		if (module!=null){
			try {
				Document doc = module.convertToDocument(item);
				String dir = getModuleDirPath(module, context);
				Analyzer analyzer = module.getAnalyzer();
				IndexWriter writer = new IndexWriter(dir, analyzer, false);
				writer.addDocument(doc);
			} catch (IOException e) {
				throw new DgException("Cannot add item to index.",e);
			}
		}else{
			//TODO do something if we do not have indexes of
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
		LucModule<E> module = getModuleFor(item.getClass().getName());
		try {
			if (module!=null){
				String dir = getModuleDirPath(module, context);
				IndexReader reader = IndexReader.open(dir);
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
	 * Module is determined from class
	 * @param <E>
	 * @param clazz
	 * @param textToSearch
	 * @param context
	 * @return
	 * @throws DgException
	 */
	public static <E> Hits search(Class<E> clazz, String textToSearch, ServletContext context) throws DgException{
		Hits hits = null;
		try {
			LucModule<E> module = getModuleFor(clazz.getName());
			String dir = getModuleDirPath(module, context);
			String searchFieldName = module.getSearchFieldName();
			String searchText = textToSearch;
			Analyzer analyzer = module.getAnalyzer();
			IndexSearcher searcher = new IndexSearcher(dir);
			QueryParser parser = new QueryParser(searchFieldName, analyzer);
			Query query = parser.parse(searchText);
			hits = searcher.search(query);
		} catch (IOException e1) {
			throw new DgException("Cannot search index",e1);
		} catch (ParseException e2) {
			throw new DgException("Cannot search index",e2);
		}
		return hits;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> List<E> hitsToSortedList(Hits hits, Class<E> clazz) throws IOException{
		LucModule<E> module = getModuleFor(clazz.getName());
		List<E> items = null;
		if (hits!=null && hits.length()>0){
			items = new ArrayList<E>(hits.length());
			Iterator<Hit> iterator = hits.iterator();
			while (iterator.hasNext()) {
				Hit hit = iterator.next();
				E item = module.hitToItem(hit);
				items.add(item);
			}
		}
		return items;
	}
	
}
