package org.digijava.kernel.services.sync.model;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Holds a translation for a label. It's purpose is for bulk querying db for translations
 * that must be synchronized with AMP Offline.
 *
 * This class is sibling to Message with most notable difference that label (orig_message) is always non-null and
 * is equal with label (orig_message) for the same message in English.
 *
 * @author Octavian Ciubotaru
 */
public class Translation {

    private String key;
    private String label;
    private String locale;
    private String translatedLabel;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTranslatedLabel() {
        return translatedLabel;
    }

    public void setTranslatedLabel(String translatedLabel) {
        this.translatedLabel = translatedLabel;
    }

    /**
     * Checks if hash of label is equal to key.
     * @return true if hash of label is equal to key.
     */
    public boolean labelMatchesKey() {
        return key.equals(TranslatorWorker.generateTrnKey(label));
    }
}
