package org.digijava.module.translation.lucene;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.digijava.kernel.entity.Message;

/**
 * Separate lucene module for each language in translations 
 * @author Irakli Kobiashvili
 *
 */
public class TrnLuceneModule extends LucTranslationModule {

	private static final long serialVersionUID = 2L;
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
		return lang.getAnalyzer();
	}

	@Override
	public String getSuffix() {
		return lang.getLangCode();
	}

	@Override
	public List<Message> getItemsToIndex() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public String getName() {
//		return super.getName()+"_"+lang.getLangCode();
//	}

	@Override
	public long getSerialVersionUID() {
		return serialVersionUID;
	}

}
