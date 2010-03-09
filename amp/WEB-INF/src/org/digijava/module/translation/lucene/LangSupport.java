package org.digijava.module.translation.lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.digijava.kernel.lucene.analyzers.SpanishAnalyzer;

/**
 * Enumeration for supported languages.
 * Also encapsulates Lucene analyzer factory.
 * Supported languages means languages we provide translations officially.
 * For example we support Help in English, French and Spanish.
 * @author Irakli Kobiashvili
 *
 */
public enum LangSupport {
	ENGLISH ("en"), 
	FRENCH ("fr"), 
	SPANISH ("es");
	
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
	 * @return Analayzer instance
	 * @see Analyzer
	 */
	public Analyzer getAnalyzer(){
		if (FRENCH.equals(this)){
			return new FrenchAnalyzer();
		} else if (SPANISH.equals(this)){
			return new SpanishAnalyzer();
		}
		//For all other languages including ENGLISH we use StandardAnalzyer which is English language one.
		return new StandardAnalyzer();
	}
	
	/**
	 * Returns all supported languages except default English
	 * @return list of supported languages except english
	 */
	public static List<LangSupport> supported(){
		List<LangSupport> result = new ArrayList<LangSupport>(2);
		result.add(LangSupport.FRENCH);
		result.add(LangSupport.SPANISH);
		//ADD new languages here too whne new analyzer is added. 
		//P.S. please do not think this is stupid implementation and I missed ENGLISH.
		//We do not need ENGLISH because all unsupported languages go 
		//together with ENGLISH which is kind of default language. 
		return result;
	}

}
