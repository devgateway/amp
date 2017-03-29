package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;
import java.util.List;

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

    List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName);

    String getEditorBodyEmptyInclude(Site site, String editorKey, String language) throws EditorException;
}
