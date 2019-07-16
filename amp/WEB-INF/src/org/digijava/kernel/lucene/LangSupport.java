package org.digijava.kernel.lucene;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Enumeration for supported languages.
 * Also encapsulates Lucene analyzer factory.
 * Supported languages means languages we provide translations officially.
 * For example we support Help in English, French and Spanish.
 * @author Irakli Kobiashvili
 *
 */
public enum LangSupport {
    ENGLISH("en"),
    FRENCH("fr"),
    SPANISH("es"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    ALBANIAN("sq"),
    ARABIC("ar"),
    AMHARIC("am");
    
    private String languageCode;
    
    private LangSupport(String langIso){
        languageCode = langIso.toLowerCase();
    }
    
    /**
     * Returns language code like en, fr, es
     * @return
     */
    public String getLangCode(){
        return languageCode;
    }
    
    /**
     * Returns analyzer for language
     * @return Analyzer instance
     * @see Analyzer
     */
    public Analyzer getAnalyzer(Version version) {
        if (FRENCH.equals(this)){
            return new FrenchAnalyzer(version);
        } else if (SPANISH.equals(this)){
            return new SpanishAnalyzer(version);
        }
        //For all other languages including ENGLISH we use StandardAnalzyer which is English language one.
        return new StandardAnalyzer(version);
    }
    
    /**
     * Returns all supported languages except default English
     * @return set of supported languages except English
     */
    public static EnumSet<LangSupport> supported(){
        //ADD new languages here too when new analyzer is added. 
        //P.S. please do not think this is stupid implementation and I missed ENGLISH.
        //We do not need ENGLISH because all unsupported languages go 
        //together with ENGLISH which is kind of default language. 
        return EnumSet.of(LangSupport.FRENCH, LangSupport.SPANISH, LangSupport.ROMANIAN, LangSupport.RUSSIAN,LangSupport.ALBANIAN );
    }

    /**
     * Converts EnumSet of LangSupport enumerations to list of language code strings 
     * @param langs
     * @return
     */
    public static List<String> toCodeList(EnumSet<LangSupport> langs) {
        if (langs==null || langs.size()==0) return null;
        List<String> result = new ArrayList<String>(langs.size());
        for (LangSupport lang : langs) {
            result.add(lang.getLangCode());
        }
        return result;
    }

}
