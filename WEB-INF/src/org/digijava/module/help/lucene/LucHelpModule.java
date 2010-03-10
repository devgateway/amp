package org.digijava.module.help.lucene;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hit;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.module.help.helper.HelpTopicHelper;
import org.digijava.module.help.util.HelpUtil;
import org.digijava.module.translation.lucene.LangSupport;


/**
 * Lucene module for Help topics
 * @author Irakli Kobiashvili
 *
 */
public class LucHelpModule implements LucModule<HelpTopicHelper> {

	/**
	 * PLEASE INCREMENT VALUE ECH TIME CLASS IS CHANGED. 
	 */
	private static final long serialVersionUID = 1L;

	private final LangSupport lang;
	private final String moduleInstance;
	
	private static final String MODULE_NAME 		= "help";
	private static final String ID_FIELD_TERM 		= "helpTopicId";
	private static final String FIELD_TITLE_KEY 	= "helpTitleKey";
	private static final String FIELD_TITLE 		= "helpTitle";
	private static final String FIELD_BODY_KEY 		= "helpBodyKey";
	private static final String FIELD_BODY 			= "helpBody";
	private static final String FIELD_LANG_ISO 		= "helpLangIso";
	private static final String FIELD_INSTANCE_NAME = "helpModuleInstance";
	private static final String FIELD_INDEXED_TEXT	= "helpIndxedText";

	
	/**
	 * Creates lucene module for help of English and all unsupported languages.
	 * This means that all unsupported languages will be indexed together with English.
	 * @see LangSupport
	 */
	public LucHelpModule(){
		this.lang = LangSupport.ENGLISH;
		this.moduleInstance = null;
	}

	/**
	 * Creates lucene module for help for specified module instance
	 * Languages will be English and all unsupported languages. 
	 * @param moduleInstance
	 */
	public LucHelpModule(String moduleInstance){
		this.lang = LangSupport.ENGLISH;
		this.moduleInstance = moduleInstance;
	}
	
	/**
	 * Creates lucene module for help for specified supported language and module instance. 
	 * @param moduleInstance
	 * @param lang
	 */
	public LucHelpModule(String moduleInstance, LangSupport lang){
		this.lang = lang;
		this.moduleInstance = moduleInstance;
	}
	
	@Override
	public Document convertToDocument(HelpTopicHelper item) {
		
		//Filter title and body from HTML tags.
		String bodyText = item.getBody();
		String titleText = item.getTitle();
		String textToIndex = "";
		if (titleText != null){
			textToIndex += titleText;
		}
		if (bodyText!=null){
			textToIndex += ". " + bodyText; 
		}
		textToIndex = HelpUtil.stripOutHTML(textToIndex);
		
		//create lucene fields
		Field id		= new Field(ID_FIELD_TERM, item.getId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field title_key	= new Field(FIELD_TITLE_KEY, item.getTitleKey(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field title		= new Field(FIELD_TITLE, item.getTitle(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field body_key	= new Field(FIELD_BODY_KEY, item.getBodyKey(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field body		= new Field(FIELD_BODY, item.getBody(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field lang_iso	= new Field(FIELD_LANG_ISO, item.getLangIso(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field instance	= new Field(FIELD_INSTANCE_NAME, item.getModuleInstance(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field indexed	= new Field(FIELD_INDEXED_TEXT, textToIndex, Field.Store.NO, Field.Index.TOKENIZED);
		//add fields to document
		Document doc 	= new Document();
		doc.add(indexed);
		doc.add(id);
		doc.add(title);
		doc.add(body);
		doc.add(title_key);
		doc.add(body_key);
		doc.add(lang_iso);
		doc.add(instance);
		
		return doc;
	}

	@Override
	public Analyzer getAnalyzer() {
		return lang.getAnalyzer();
	}

	@Override
	public String getSuffix() {
		String suffix = ""; 
		if (moduleInstance!=null && !moduleInstance.trim().equals("")){
			suffix += moduleInstance.trim()+"_";
		}
		suffix += lang.getLangCode();
		return suffix;
	}

	@Override
	public Class<HelpTopicHelper> getItemClass(){
		return HelpTopicHelper.class;
	}
	
	@Override
	public Term getIdFieldTerm(HelpTopicHelper item) {
		// TODO Check this if it is correct.
		Term term = new Term(ID_FIELD_TERM, item.getId().toString());
		return term;
	}

	@Override
	public List<HelpTopicHelper> getItemsToIndex() throws DgException {
		String siteId = "amp"; //null;
		List<HelpTopicHelper> topicHelpers = null;
		EnumSet<LangSupport> langs = null;
		boolean exclude = false;
		
		//prepare params
		if (this.lang.equals(LangSupport.ENGLISH)){
			//all except supported languages: English + all unsupported will go as English. 
			langs = EnumSet.copyOf(LangSupport.supported());
			exclude = true;
		}else{
			//only that one supported language.
			langs = EnumSet.of(this.lang);
		}
		
		//do search
		topicHelpers = HelpUtil.getHelpItems(siteId, this.moduleInstance, langs, exclude);

		return topicHelpers;
	}

	@Override
	public String getName() {
		return MODULE_NAME;
	}

	@Override
	public String[] getSearchFieldNames() {
		String[] fieldsNames = {FIELD_INDEXED_TEXT};//{FIELD_TITLE, FIELD_BODY};
		return fieldsNames;
	}

	@Override
	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public HelpTopicHelper hitToItem(Hit hit) throws IOException {
		Document doc = hit.getDocument();
		HelpTopicHelper topic = new HelpTopicHelper();
		topic.setTitle(doc.get(FIELD_TITLE));
		topic.setBody(doc.get(FIELD_BODY));
		topic.setId(Long.valueOf(doc.get(ID_FIELD_TERM)));
		topic.setTitleKey(doc.get(FIELD_TITLE_KEY));
		topic.setBodyKey(doc.get(FIELD_BODY_KEY));
		topic.setLangIso(doc.get(FIELD_LANG_ISO));
		topic.setModuleInstance(doc.get(FIELD_INSTANCE_NAME));
		topic.setSortIndex(new Float(hit.getScore()));
		return topic;
	}

	@Override
	public boolean needIndexRebuild() {
		return false;
	}

}
