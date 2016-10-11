package org.digijava.module.translation.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hit;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.module.aim.util.DbUtil;

/**
 * Translation module for lucene.
 * @author Irakli Kobiashvili
 *
 */
public class LucTranslationModule implements LucModule<Message> {

	/**
	 * PLEASE INCREMENT THIS VALUE EACH TIME CLASS IS CHANGED.
	 */
	private static final long serialVersionUID 		= 3L;
//	public static final String ID_FIELD = "key";
	public static final String MODULE_NAME 			= "translations";
	public static final String FIELD_KEY 			= "trnKey";
	public static final String FIELD_LANG 			= "trnLang";
	public static final String FIELD_MESSAGE 		= "trnMessage";
	
	private boolean needIndexRebuild = false;

	@Override
	public String getSuffix() {
		return null;
	}

	@Override
	public Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}

	@Override
	public String getName() {
		return "Translations";
	}

	@Override
	public List<Message> getItemsToIndex() throws DgException {
		return new ArrayList<>(DbUtil.getAll(Message.class));
	}

	@Override
	public boolean needIndexRebuild() {
		return needIndexRebuild;
	}

	@Override
	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public Document convertToDocument(Message item) {
		Document doc = new Document();
//		Field id=new Field(ID_FIELD, item.getKey()+"_", Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field key=new Field(FIELD_KEY, item.getKey(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field lang=new Field(FIELD_LANG, item.getLocale(), Field.Store.YES, Field.Index.UN_TOKENIZED);
		Field message=new Field(FIELD_MESSAGE, item.getMessage(), Field.Store.YES, Field.Index.TOKENIZED);
//		Field original=new Field("trnOriginal", item.getOriginalMessage(), Field.Store.YES, Field.Index.TOKENIZED);
		doc.add(key);
		doc.add(lang);
		doc.add(message);
//		doc.add(original);
		return doc;
	}

	@Override
	public Term getIdFieldTerm(Message msg) {
		Term term = new Term(FIELD_KEY, msg.getKey());
		return term;
	}

	@Override
	public String[] getSearchFieldNames() {
		String[] fieldNames = {FIELD_MESSAGE}; 
		return fieldNames;
	}

	@Override
	public Message hitToItem(Hit hit) throws IOException {
		Message msg = new Message();
		Document doc = hit.getDocument();
		msg.setKey(doc.get(FIELD_KEY));
		msg.setLocale(doc.get(FIELD_LANG));
		msg.setMessage(doc.get(FIELD_MESSAGE));
		return msg;
	}

	@Override
	public Class<Message> getItemClass() {
		return Message.class;
	}

	
}
