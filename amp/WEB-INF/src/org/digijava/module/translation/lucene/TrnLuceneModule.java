package org.digijava.module.translation.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.translator.util.TrnUtil;

import java.util.EnumSet;
import java.util.List;

/**
 * Separate lucene module for each language in translations 
 * @author Irakli Kobiashvili
 *
 */
public class TrnLuceneModule extends LucTranslationModule {

    /**
     * INCREASE THIS EACH TIME YOU CHANGE HERE SOMTHING!!!!!
     * increasing this value will trigger rebuild of indexes for new code.
     */
    private static final long serialVersionUID = 4L;
    private LangSupport lang;

    /**
     * Construct module directly from {@link LangSupport} instance.
     * @param lang
     */
    public TrnLuceneModule(LangSupport lang) {
        this.lang = lang;
    }

    @Override
    public Analyzer getAnalyzer() {
        return lang.getAnalyzer(Version.LUCENE_36);
    }

    @Override
    public String getSuffix() {
        return lang.getLangCode();
    }

    @Override
    public List<Message> getItemsToIndex() throws DgException {
        
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
        
        List<Message> messages = TrnUtil.getMessagesForLocales(langs, exclude);
        return messages;
    }

    @Override
    public long getSerialVersionUID() {
        return serialVersionUID;
    }

}
