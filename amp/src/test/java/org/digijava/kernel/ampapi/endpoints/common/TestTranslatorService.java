package org.digijava.kernel.ampapi.endpoints.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Octavian Ciubotaru
 */
public class TestTranslatorService implements TranslatorService {

    @Override
    public Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException {
        return Arrays.asList(msg("en", text + " en"), msg("fr", text + " fr"));
    }

    @Override
    public String translateText(String text) {
        throw new NotImplementedException("NOT IN USE YET");
    }

    @Override
    public Map<String, String> translateLabel(String label) {
        throw new NotImplementedException("NOT IN USE YET");
    }

    @Override
    public List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName) {
        throw new NotImplementedException("NOT IN USE YET");
    }

    @Override
    public String getEditorBodyEmptyInclude(Site site, String editorKey, String language) {
        if (editorKey == null) {
            return null;
        } else {
            return String.format("%s-%s-%s", site.getName(), editorKey, language);
        }
    }

    private Message msg(String locale, String text) {
        Message msg = new Message();
        msg.setLocale(locale);
        msg.setMessage(text);
        return msg;
    }
}
