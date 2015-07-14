package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;

/**
 * Class for storing translating settings for activity export. <br>
 * The bean is created in AuthRequestFilter and stored in request.
 * 
 * @author Viorel Chihai
 */
public class TranslationSettings {

	/** Base language code (retrieved from 'language' parameter) */
	private String baseLangCode;
	/** Default language code for the current site */
	private String defaultLangCode;
	/** Allowed language codes */
	private Set<String> allowedLangCodes = null;
	/** List of translations. 
	 *  Reunion of language codes from <default_language>, 'language' and 'translations' parameter
	 *  */
	private Set<String> trnLocaleCodes = new HashSet<String>();
	
	/**
	 * @return default trn settings for the current request environment
	 */
	public static TranslationSettings getDefault() {
		return new TranslationSettings();
	}

	public void setTrnLocaleCodes(Set<String> trnLocaleCodes) {
		this.trnLocaleCodes = trnLocaleCodes;
	}

	public TranslationSettings() {
		this.baseLangCode = TLSUtils.getEffectiveLangCode();
		this.trnLocaleCodes.add(baseLangCode);
		this.trnLocaleCodes.add(getDefaultLangCode());
	}
	
	public TranslationSettings(String langCode, Set<String> trnLocaleCodes) {
		this.baseLangCode = langCode;
		this.trnLocaleCodes = trnLocaleCodes;
	}
	
	/**
	 * @return default language code for the current site
	 */
	public String getDefaultLangCode() {
		if (defaultLangCode == null) {
			this.defaultLangCode = TLSUtils.getSite().getDefaultLanguage().getCode();
		}
		return defaultLangCode;
	}
	
	/**
	 * @return user selected language, that can be configured also via 'language' parameter
	 */
	public String getBaseLangCode() {
		return baseLangCode;
	}

	public void setBaseLangCode(String baseLangCode) {
		this.baseLangCode = baseLangCode;
	}

	public Set<String> getTrnLocaleCodes() {
		return trnLocaleCodes;
	}
	
	/**
	 * @param langCode code of the language (locale)  
	 * @return boolean if the langCode is equal with the default system language code
	 * */
	public boolean isDefaultLanguage(String langCode) {
		return getDefaultLangCode().equalsIgnoreCase(langCode);
	}

	/**
	 * @return the allowedLangCodes
	 */
	public Set<String> getAllowedLangCodes() {
		if (allowedLangCodes == null) {
			allowedLangCodes = (Set<String>) TLSUtils.getSite().getTranslationLanguages();
		}
		return allowedLangCodes;
	}

}
