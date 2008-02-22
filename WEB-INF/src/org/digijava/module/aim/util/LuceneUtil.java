package org.digijava.module.aim.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Alexandru Artimon
 * 
 */ 

public class LuceneUtil {
	private static Logger logger = Logger.getLogger(LuceneUtil.class);
	public final static Analyzer analyzer = new StandardAnalyzer();
	public final static String idField = "id";
	
//	/**
//	 * Opens the writer so information can be added to the index
//	 * @param create set it to true to create the index filestructure
//	 */
//	public void openWriter(boolean create){
//		if (indexWriter == null){
//			try {
//				indexWriter = new IndexWriter(appPath + indexName, analyzer, create);
//			} catch (CorruptIndexException e) {
//				e.printStackTrace();
//			} catch (LockObtainFailedException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * Opens the searcher
//	 */
//
//	public void openSearcher(){
//		if (indexSearcher == null){
//			try {
//				indexSearcher = new IndexSearcher(appPath + indexName);
//			} catch (CorruptIndexException e) {
//				e.printStackTrace();
//			} catch (LockObtainFailedException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * Closes the writer and insures that all data has been written to the index
//	 */
//	public void closeWriter(){
//		if (indexWriter != null){
//			try {
//				indexWriter.close();
//				indexWriter = null;
//			} catch (CorruptIndexException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * Closes the searcher
//	 */
//	public void closeSearcher(){
//		if (indexSearcher != null){
//			try {
//				indexSearcher.close();
//				indexSearcher = null;
//			} catch (CorruptIndexException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	public static Directory createIndex(){ 
		RAMDirectory index = new RAMDirectory();
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(index, analyzer, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Getting activity List!");
		Session session = null;
		String qryStr = null;

		try{
			session				= PersistenceManager.getSession();
			Connection	conn	= session.connection();
			Statement st		= conn.createStatement();
			qryStr 				= "select * from v_lucene_index_data" ;
			ResultSet rs		= st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " activities!");
			boolean isNext = rs.first();
			while (isNext){
				//
				Document doc = activity2Document(rs.getString("amp_activity_id"), rs.getString("title"), rs.getString("description"), rs.getString("objective"), rs.getString("purpose"), rs.getString("results"));
				indexWriter.addDocument(doc);
				isNext = rs.next();
			}
			conn.close();

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
		
		try {
			indexWriter.optimize();
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Done creating index!");
		return index;
	}
	
	/**
	 * Add an activity to the index
	 * 
	 * @param request is used to retreive curent site and navigation language
	 * @param act the activity that will be added
	 */
	public static Document activity2Document(String actId, String title, String description, String objective, String purpose, String results){
		Document doc = new Document();
		String all = new String("");
		doc.add(new Field(idField, actId, Field.Store.YES, Field.Index.UN_TOKENIZED));
		all = all.concat(" " + actId);
		doc.add(new Field("title", title, Field.Store.NO, Field.Index.TOKENIZED));
		all = all.concat(" " + title);
		if (description != null && description.length()>0){
			doc.add(new Field("description", description, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + description);
		}
		if (objective != null && objective.length()>0){
			doc.add(new Field("objective", objective, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + objective);
		}
		if (purpose != null && purpose.length()>0){
			doc.add(new Field("purpose", purpose, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + purpose);
		}
		if (results != null && results.length()>0){
			doc.add(new Field("results", results, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + results);
		}
		
		doc.add(new Field("all", all, Field.Store.NO, Field.Index.TOKENIZED));
		return doc;
	}
	
	public static void deleteActivity(Directory idx, String field, String search){
		Term term = new Term(field, search);
		IndexReader indexReader;
		try {
			indexReader = IndexReader.open(idx);
			indexReader.deleteDocuments(term);
			indexReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addUpdateActivity(HttpServletRequest request, boolean update, Long id){
		ServletContext ampContext = request.getSession().getServletContext();
		Directory idx = (Directory) ampContext.getAttribute(Constants.LUCENE_INDEX);
		logger.info("Updating activity!");
		if (update){
			deleteActivity(idx, idField, String.valueOf(id));
		}

		
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(idx, LuceneUtil.analyzer, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AmpActivity act = ActivityUtil.getAmpActivity(id);
		
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		//Util.getEditorBody(site,act.getDescription(),navigationLanguage);
		Document doc = null;
		try {
			doc = activity2Document(String.valueOf(act.getAmpActivityId()), String.valueOf(act.getName()), Util.getEditorBody(site,act.getDescription(),navigationLanguage), Util.getEditorBody(site,act.getObjective(),navigationLanguage), Util.getEditorBody(site,act.getPurpose(),navigationLanguage), Util.getEditorBody(site,act.getResults(),navigationLanguage));
		} catch (EditorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (doc != null){
			try {
				indexWriter.addDocument(doc);
				indexWriter.optimize();
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Runs a search in the index and returns the results
	 * 
	 * @param index the index where the search will be done
	 * @param field the field where you do the search
	 * @param searchString
	 * 
	 * @return a Hits object that contains the results
	 */
	public static Hits search(Directory index, String field, String searchString){
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = null;
		Hits hits = null;
		
		
		Searcher indexSearcher = null;
		try {
			indexSearcher = new IndexSearcher(index);
			query = parser.parse(searchString);

			hits = indexSearcher.search(query);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return hits;
	}
	
	
}
