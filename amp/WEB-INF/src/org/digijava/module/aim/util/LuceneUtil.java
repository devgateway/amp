package org.digijava.module.aim.util;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.util.HelpUtil;
import org.hibernate.Session;

/**
 * @author Alexandru Artimon
 * 
 */ 

public class LuceneUtil {
	private static Logger logger = Logger.getLogger(LuceneUtil.class);
        /**
         * StandardAnalyzer used to analyse text 
         */
        public final static Analyzer analyzer = new StandardAnalyzer();
        /**
         * 
         */
        public final static String idField = "id";
        /**
         * name of index directory
         */
        public final static String indexDirectory = "lucene-index";
	
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
			qryStr 				= "select * from v_titles" ;
			ResultSet rs		= st.executeQuery(qryStr);
			
			final class Items {
				int id;
				String amp_id;
				String title;
				String description;
				String objective;
				String purpose;
				String results;
				String numcont;
				ArrayList<String> componentcode=new ArrayList<String>();
			};
			
			HashMap list = new HashMap();
			
			Items x;
			
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " activities!");
			boolean isNext = rs.first();
			while (isNext){
				x = new Items();
				x.id = Integer.parseInt(rs.getString("amp_activity_id"));
				x.title = rs.getString("name");
				list.put(x.id, x);
				isNext = rs.next();
				//
			}
			//the correct view is v_amp_id, the view with name  v_ampid is not used 
			qryStr = "select * from v_amp_id" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " project id's!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.amp_id = rs.getString("amp_id");
				isNext = rs.next();
				//
			}

			qryStr = "select * from v_description" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " descriptions!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.description = rs.getString("trim(dg_editor.body)");
				isNext = rs.next();
				//
			}
			
			qryStr = "select * from v_objectives" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " objectives!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.objective = rs.getString("trim(dg_editor.body)");
				isNext = rs.next();
				//
			}

			qryStr = "select * from v_purposes" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " purposes!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.purpose = rs.getString("trim(dg_editor.body)");
				isNext = rs.next();
				//
			}
			
			qryStr = "select * from v_results" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " results!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.results = rs.getString("trim(dg_editor.body)");
				isNext = rs.next();
				//
			}
			
			
			//Bolivia contract number
			qryStr = "select * from v_convenio_numcont" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " results!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.numcont = rs.getString("numcont");
				isNext = rs.next();
				//
			}
			
			
			//Bolivia component code
			qryStr = "select a.amp_activity_id,c.code  from amp_activity_components a inner join amp_components c on a.amp_component_id=c.amp_component_id;" ;
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " amp_activity_components!");
			isNext = rs.first();
				
			while (isNext){
			    	int currActId = rs.getInt("amp_activity_id");
			    	x = (Items) list.get(currActId);
			    	if (rs.getString("code")!=null){
			    	    x.componentcode.add(rs.getString("code"));
			    	}
				isNext = rs.next();
			}
			
			
			conn.close();

			logger.info("Building the index ");
			Iterator it = list.values().iterator();
			while (it.hasNext()) {
				Items el = (Items) it.next();
				Document doc = activity2Document(String.valueOf(el.id),el.amp_id, el.title, el.description, el.objective, el.purpose, el.results,el.numcont,el.componentcode);
				if (doc != null)
					indexWriter.addDocument(doc);
			}
			list.clear();
			
			
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
	public static Document activity2Document(String actId, String projectId, String title, String description, String objective, String purpose, String results,String numcont,ArrayList<String> componentcodes){
		Document doc = new Document();
		String all = new String("");
		if (actId != null){
			doc.add(new Field(idField, actId, Field.Store.YES, Field.Index.UN_TOKENIZED));
			//all = all.concat(" " + actId);
		}
		if (projectId != null){
			doc.add(new Field("projectId", projectId, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + projectId);
		}
		if (title != null){
			doc.add(new Field("title", title, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + title);
		}
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
		
		//
		if (numcont != null && numcont.length()>0){
			doc.add(new Field("numcont", numcont, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + numcont);
		}
		
		int i =0;
		if (componentcodes != null && componentcodes.size()>0){
				
        		for (String value : componentcodes) {
        			if (value!=null){
        			doc.add(new Field("componentcode_"+String.valueOf(i), value, Field.Store.NO, Field.Index.TOKENIZED));
        			all = all.concat(" " + value);
        			}
        			i++;
        		}
			
		
		}
		
		if (all.length() == 0)
			return null;
		
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
			String projectid=null;
			if (act.getInternalIds().size()>0){
				projectid= String.valueOf(((AmpActivityInternalId)act.getInternalIds().iterator().next()).getInternalId());
			}
			
			ArrayList<String> componentsCode=new ArrayList<String>();
		 	Collection<AmpComponent> componentsList=act.getComponents();

		 	for(AmpComponent c:componentsList){
		 	   componentsCode.add(c.getCode());
		 	}
				
			
			doc = activity2Document(
					String.valueOf(act.getAmpActivityId()), 
					projectid, 
					String.valueOf(act.getName()), 
					Util.getEditorBody(site,act.getDescription(),navigationLanguage), 
					Util.getEditorBody(site,act.getObjective(),navigationLanguage), 
					Util.getEditorBody(site,act.getPurpose(),navigationLanguage), 
					Util.getEditorBody(site,act.getResults(),navigationLanguage),
					Util.getEditorBody(site,act.getContactName(),navigationLanguage),componentsCode
			);
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
			searchString = searchString.trim();
			if (searchString.charAt(0) == '*')
				searchString = searchString.substring(1);
			//AMP-3806
			searchString = searchString.replace("+","\\+");
			searchString = searchString.replace("-","\\-");
			searchString = searchString.replace("&","\\&");
			searchString = searchString.replace("(","\\(");
			searchString = searchString.replace(")","\\)");
			searchString = searchString.replace("{","\\{");
			searchString = searchString.replace("{","\\}");
			searchString = searchString.replace("[","\\[");
			searchString = searchString.replace("]","\\]");
			
			query = parser.parse(searchString.trim()+"*");
			BooleanQuery bol = new BooleanQuery();
			bol.add(query,BooleanClause.Occur.MUST);
			bol.setMaxClauseCount(2000);
			hits = indexSearcher.search(bol);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return hits;
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
        public static void createHelp() throws  DgException{
            
	
		boolean createDir = LuceneUtil.isDir();
	
		if(!createDir){
			logger.info("Building the help");
				  LuceneUtil.addUpdatehelp(false);
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
        public static void addUpdatehelp(boolean update) throws DgException {

        HelpSearchData item = new HelpSearchData();
		DateFormat formatter ; 
	    Date date ; 
	     System.out.println("Udate:"+update);
	  try{
            Long lastLucModDay = IndexReader.lastModified("lucene-index");
        System.out.println("lastLucModDay:"+lastLucModDay);
 	    formatter  = new SimpleDateFormat();
            String leastUpDate = formatter.format(lastLucModDay);
	    date = (Date)formatter.parse(leastUpDate);

  	    Collection data =  HelpUtil.getAllHelpData();

		for(Iterator<HelpSearchData> iter = data.iterator(); iter.hasNext(); ) {

                item = (HelpSearchData) iter.next();

             String article =  item.getBody();
                String title = item.getTopicKey();
                String titTrnKey = item.getTitleTrnKey();
                // Converts html formatted help topics body to plain text format.
             String newCode = article.replaceAll("\\<.*?\\>","");

             if(update){
            	 if(item.getLastModDate().after(date)){
            		 deleteHelp("title",title);
            		 indexArticle(newCode, title,titTrnKey);
                    }
             	}else if(!update){
             		indexArticle(newCode, title,titTrnKey);	
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
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
    public static Hits helpSearch(String field, String searchString){
		
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = null;
		Hits hits = null;
		Document document = new Document();
		
		Searcher indexSearcher = null;
		try {
			if(searchString != null){
			indexSearcher = new IndexSearcher(indexDirectory);
			searchString = searchString.trim();
			query = parser.parse("+"+searchString+"*");
		
			hits = indexSearcher.search(query);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
		return hits;
	}
	
        /**
         * Returns highlighted object
         * 
         * @param field
         * @param searchString
         * @return highlight object
         * @throws java.io.IOException
         * @throws org.apache.lucene.queryParser.ParseException
         */
        public static Object highlighter(Field field,String searchString) throws IOException, ParseException{
		Query query = null;
		QueryParser parser = new QueryParser(field.getClass().getName(), analyzer);
	
		query = parser.parse(searchString);
		
		Object hA= highlight(field,query);
		return hA;
	}
	
         /**
         * Returns highlighted object
         * 
         */
	 private static Object highlight(Field field, Query query) throws IOException {

		    query.rewrite(IndexReader.open(indexDirectory));
		    QueryScorer scorer = new QueryScorer(query);
		     SimpleHTMLFormatter formatter =
		        new SimpleHTMLFormatter("<span class=\"highlight\">",
		            "</span>");
		    Highlighter highlighter = new Highlighter(formatter, scorer);
		    Fragmenter fragmenter = new SimpleFragmenter(50);
		    highlighter.setTextFragmenter(fragmenter);
		
		    String value = field.stringValue();
		    TokenStream tokenStream = new StandardAnalyzer()
		        .tokenStream(field.name(), new StringReader(value));
		
		    return highlighter.getBestFragments(tokenStream, value, 5, "...");
		 }

	
         /**
          * Creates {@link Document} using {@link createDocument(String,String,String)}.
          * Adds newly created document to lucene-index directory
          *  
          * @param article body of help topic
          * @param title title of help topic
          * @param titTrnKey translation key used to translate title
          * @throws java.lang.Exception
          */
         public static void indexArticle(String article, String title,String titTrnKey)
    throws Exception {
		Document document = LuceneUtil.createDocument(article,title,titTrnKey);
		LuceneUtil.indexDocument(document);
		
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
        public static Document createDocument(String article, String title,String titTrnKey){

		 Document document = new Document();
		 document.add(new Field("title",title,Field.Store.YES,Field.Index.TOKENIZED));
		 document.add(new Field("titletrnKey",titTrnKey,Field.Store.YES,Field.Index.TOKENIZED));
		 document.add(new Field("article",article,Field.Store.YES,Field.Index.TOKENIZED));
	 return document;
		
	}

	
        /**
         * Shows whether lucene-index
         * directory exists or no
         * 
         * @return true if lucene-index directory exists otherwise false
         */
        public static boolean isDir(){
		boolean createDir = IndexReader.indexExists(indexDirectory);
		return createDir;
	}
	
	
        /**
         * Creates lucene-index
         * directory if it doesn't exist.
         * Adds document to it 
         * 
         * @param document
         * @throws java.io.IOException
         */
        public static void indexDocument(Document document) throws IOException {
		try{
    	
		boolean createDir = IndexReader.indexExists(indexDirectory);
			
    	if(createDir == false){
		
				createDir= true;
		
			}else if (createDir == true){
			
				createDir= false;
			}
		
		StandardAnalyzer analyzer  = new StandardAnalyzer();
        IndexWriter writer = new IndexWriter(indexDirectory, analyzer, createDir);
        writer.addDocument(document);
        writer.optimize();
        writer.close();
        
    } catch (IOException e) {
                logger.error(e);
                throw e;

            }
    
  }
        /**
         * 
         * @param field
         * @param search
         */
        public static void deleteHelp(String field, String search){
		Term term = new Term(field,search);
		Directory directory;
		IndexReader indexReader;

		try {
			indexReader = IndexReader.open(indexDirectory);
			Integer deleted = indexReader.deleteDocuments(term);
			indexReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
