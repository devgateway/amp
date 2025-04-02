package org.digijava.kernel.validation;

import java.util.Map;

import org.digijava.module.editor.dbentity.Editor;

/**
 * Used for fields that are persisted in {@link Editor}.
 *
 * @author Octavian Ciubotaru
 */
public class EditorTranslatedValueContext extends TranslatedValueContext {

    private TranslationContext translationContext;

    private boolean multilingual;

    private String editorKey;

    EditorTranslatedValueContext(TranslationContext translationContext, String editorKey, boolean multilingual) {
        super(translationContext);
        this.translationContext = translationContext;
        this.editorKey = editorKey;
        this.multilingual = multilingual;
    }

    @Override
    public String getLang() {
        return multilingual ? translationContext.getLanguage() : translationContext.getDefaultLanguage();
    }

    @Override
    public String getValue(String lang) {
        if (editorKey == null) {
            return null;
        }
        Map<String, String> translatedValues = translationContext.getEditor(editorKey);
        return translatedValues != null ? translatedValues.get(lang) : null;
    }

    @Override
    public Map<String, String> getValues() {
        return translationContext.getEditor(editorKey);
    }
}
