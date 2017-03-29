package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;
import java.util.List;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * @author Octavian Ciubotaru
 */
public class AMPTranslatorService implements TranslatorService {

    public static final TranslatorService INSTANCE = new AMPTranslatorService();

    @Override
    public Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException {
        return TranslatorWorker.getAllTranslationOfBody(text, siteId);
    }

    @Override
    public String translateText(String text) {
        return TranslatorWorker.translateText(text);
    }

    @Override
    public List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName) {
        return ContentTranslationUtil.loadFieldTranslations(objClass, objId, fieldName);
    }

    @Override
    public String getEditorBodyEmptyInclude(Site site, String editorKey, String language) throws EditorException {
        return DbUtil.getEditorBodyEmptyInclude(site, editorKey, language);
    }
}
