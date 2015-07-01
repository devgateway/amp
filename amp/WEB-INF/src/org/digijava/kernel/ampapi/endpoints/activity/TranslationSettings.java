package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;

/**
 * Class for storing translating settings for activity export
 * The bean is created in AuthRequestFilter and stored in session
 * @author Viorel Chihai
 */
public class TranslationSettings {

	private String language;
	private Set<String> translations = new HashSet<String>();
	
	public TranslationSettings() {
		this.language = TLSUtils.getEffectiveLangCode();
		
		this.translations.add(language);
		this.translations.add(SiteUtils.getGlobalSite().getDefaultLanguage().getCode());
	}
	
	public TranslationSettings(String language, Set<String> translations) {
		this.language = language;
		this.translations = translations;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set<String> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<String> translations) {
		this.translations = translations;
	}

}
