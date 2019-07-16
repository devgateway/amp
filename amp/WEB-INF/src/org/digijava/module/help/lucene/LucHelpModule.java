package org.digijava.module.help.lucene;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.AmpLuceneDoc;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.text.regex.RegexBatch;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.help.helper.HelpTopicHelper;
import org.digijava.module.help.util.HelpUtil;


/**
 * Lucene module for Help topics
 * @author Irakli Kobiashvili
 *
 */
public class LucHelpModule implements LucModule<HelpTopicHelper> {

    /**
     * PLEASE INCREMENT VALUE ECH TIME CLASS IS CHANGED. 
     */
    private static final long serialVersionUID = 2L;

    private final LangSupport LANG;
    private final String MODULE_INSTANCE;
    private final RegexBatch HTML_STRIPPER;
    
    //fields
    private static final String MODULE_NAME         = "help";
    private static final String ID_FIELD_TERM       = "helpTopicId";
    private static final String FIELD_TITLE_KEY     = "helpTitleKey";
    private static final String FIELD_TITLE         = "helpTitle";
    private static final String FIELD_BODY_KEY      = "helpBodyKey";
    private static final String FIELD_BODY          = "helpBody";
    private static final String FIELD_LANG_ISO      = "helpLangIso";
    private static final String FIELD_INSTANCE_NAME = "helpModuleInstance";
    private static final String FIELD_INDEXED_TEXT  = "helpIndxedText";

    /**
     * Flags used for regex matching to strip out all unneeded html.
     * Can combine multiple patterns like this = Pattern.DOTALL | Pattern.MULTILINE; 
     */
    private static final int REGEX_FLAGS = Pattern.DOTALL;
    /**
     * Regexes split and ordered so that it will leave only 
     * text required for Lucene indexing.  
     */
    private static final String[] HTML_STRIP_REGEXES = 
    {
            "<!--.*?-->"                        //commented texts
            , "<!DOCTYPE.*?>"                   //doc type tags
            , "<head.*?>.*?</head>"             //head tag with content
            , "<script.*?>.*?</script>"         //script tag with content
            , "<style.*?>.*?</style>"           //style tag with content
            , "<(link|input|a|br|hr|meta).*?>"  //some tags
            , "<\\s*?[a-z]+(:[a-z0-9]+)?.*?>"   //Beginnings of tags 
            , "</\\s*?[a-z]+(:[a-z0-9]+)?.*?>"  //Endings of tags
            , "&[a-z]*?;"                       //&nbsp; and things like that
            ,"\\s{2,}"                          //multiple spaces
    };
    
    /**
     * Creates lucene module for help of English and all unsupported languages.
     * This means that all unsupported languages will be indexed together with English.
     * @see LangSupport
     */
    public LucHelpModule(){
        this.LANG = LangSupport.ENGLISH;
        this.MODULE_INSTANCE = null;
        this.HTML_STRIPPER = new RegexBatch(HTML_STRIP_REGEXES,REGEX_FLAGS);
    }

    /**
     * Creates lucene module for help for specified module instance
     * Languages will be English and all unsupported languages. 
     * @param moduleInstance
     */
    public LucHelpModule(String moduleInstance){
        this.LANG = LangSupport.ENGLISH;
        this.MODULE_INSTANCE = moduleInstance;
        this.HTML_STRIPPER = new RegexBatch(HTML_STRIP_REGEXES,REGEX_FLAGS);
    }
    
    /**
     * Creates lucene module for help for specified supported language and module instance. 
     * @param moduleInstance
     * @param lang
     */
    public LucHelpModule(String moduleInstance, LangSupport lang){
        this.LANG = lang;
        this.MODULE_INSTANCE = moduleInstance;
        this.HTML_STRIPPER = new RegexBatch(HTML_STRIP_REGEXES,REGEX_FLAGS);
    }
    
    @Override
    public Document convertToDocument(HelpTopicHelper item) {
        
        //join title and body to index together
        String bodyText = item.getBody();
        String titleText = item.getTitle();
        String textToIndex = null;
        //build text to index
        if (titleText != null){
            textToIndex = titleText;
        }
        if (bodyText !=null){
            //"title. body" or only "body"
            textToIndex = (textToIndex!=null)?textToIndex+". "+bodyText:bodyText;
        }
        if (textToIndex == null) textToIndex = ""; //NULL not allowed for these values.
        if (bodyText == null) bodyText = "";
        if (titleText == null) titleText = "";
        
        String titleTrnKey=item.getTitleKey();
        if (titleTrnKey==null){
            titleTrnKey = titleText;
        }

        //Filter title and body from HTML tags.
        textToIndex = HTML_STRIPPER.replaceAll(textToIndex, " "); // this will slow down, but wee need to strip out many things..
        
        //create lucene fields
        //All fields are stored but not tokenized. One combined field is tokenized and not stored.
        Field id = new Field(ID_FIELD_TERM, item.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field titleKey = new Field(FIELD_TITLE_KEY, titleTrnKey, Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field title = new Field(FIELD_TITLE, titleText, Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field bodyKey = new Field(FIELD_BODY_KEY, item.getBodyKey(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field body = new Field(FIELD_BODY, bodyText, Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field langIso = new Field(FIELD_LANG_ISO, item.getLangIso(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        Field instance = new Field(FIELD_INSTANCE_NAME, item.getModuleInstance(), Field.Store.YES,
                Field.Index.NOT_ANALYZED);
        Field indexed = new Field(FIELD_INDEXED_TEXT, textToIndex, Field.Store.NO, Field.Index.ANALYZED);
        
        //add fields to document
        Document doc    = new Document();
        doc.add(indexed);
        doc.add(id);
        doc.add(title);
        doc.add(body);
        doc.add(titleKey);
        doc.add(bodyKey);
        doc.add(langIso);
        doc.add(instance);
        
        return doc;
    }

    @Override
    public Analyzer getAnalyzer() {
        return LANG.getAnalyzer(Version.LUCENE_36);
    }

    @Override
    public String getSuffix() {
        String suffix = ""; 
        if (MODULE_INSTANCE!=null && !MODULE_INSTANCE.trim().equals("")){
            suffix += MODULE_INSTANCE.trim()+"_";
        }
        suffix += LANG.getLangCode();
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
        Site site = SiteCache.lookupByName("amp"); // hack, but there are other places in AMP which assume "amp" too
        List<HelpTopicHelper> topicHelpers = null;
        EnumSet<LangSupport> langs = null;
        boolean exclude = false;
        
        //prepare params
        if (this.LANG.equals(LangSupport.ENGLISH)){
            //all except supported languages: English + all unsupported will go as English. 
            langs = EnumSet.copyOf(LangSupport.supported());
            exclude = true;
        }else{
            //only that one supported language.
            langs = EnumSet.of(this.LANG);
        }
        
        //do search
        topicHelpers = HelpUtil.getHelpItems(site, this.MODULE_INSTANCE, langs, exclude);

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
    public HelpTopicHelper luceneDocToItem(AmpLuceneDoc luceneDocument) throws IOException {
        HelpTopicHelper topic = new HelpTopicHelper();
        Document doc = luceneDocument.getDocument();
        topic.setTitle(doc.get(FIELD_TITLE));
        topic.setBody(doc.get(FIELD_BODY));
        topic.setId(Long.valueOf(doc.get(ID_FIELD_TERM)));
        topic.setTitleKey(doc.get(FIELD_TITLE_KEY));
        topic.setBodyKey(doc.get(FIELD_BODY_KEY));
        topic.setLangIso(doc.get(FIELD_LANG_ISO));
        topic.setModuleInstance(doc.get(FIELD_INSTANCE_NAME));
        topic.setSortIndex(new Float(luceneDocument.getScore()));
        return topic;
    }

    @Override
    public boolean needIndexRebuild() {
        return false;
    }

}
