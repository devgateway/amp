package org.digijava.module.translation.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.AmpLuceneDoc;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.module.aim.util.DbUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Translation module for lucene.
 * @author Irakli Kobiashvili
 *
 */
public class LucTranslationModule implements LucModule<Message> {

    /**
     * PLEASE INCREMENT THIS VALUE EACH TIME CLASS IS CHANGED.
     */
    private static final long serialVersionUID      = 3L;
//  public static final String ID_FIELD = "key";
    public static final String MODULE_NAME          = "translations";
    public static final String FIELD_KEY            = "trnKey";
    public static final String FIELD_LANG           = "trnLang";
    public static final String FIELD_MESSAGE        = "trnMessage";
    
    private boolean needIndexRebuild = false;

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_36);
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
        Field key = new Field(FIELD_KEY, item.getKey(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field lang = new Field(FIELD_LANG, item.getLocale(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field message = new Field(FIELD_MESSAGE, item.getMessage(), Field.Store.YES, Field.Index.ANALYZED);
        doc.add(key);
        doc.add(lang);
        doc.add(message);
        
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
    public Message luceneDocToItem(AmpLuceneDoc luceneDocument) throws IOException {
        Message msg = new Message();
        Document doc = luceneDocument.getDocument();
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
