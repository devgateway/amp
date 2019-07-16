package org.digijava.kernel.ampapi.endpoints.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Octavian Ciubotaru
 */
public interface TranslatorService {

    Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException;

    String translateText(String text);

    /**
     * Translate one label in multiple languages and return translations grouped by locale.
     * @param label label to be translated
     * @return translated labels grouped by locale
     */
    Map<String, String> translateLabel(String label);

    List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName);

    String getEditorBodyEmptyInclude(Site site, String editorKey, String language) throws EditorException;
}
