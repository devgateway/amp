package org.digijava.kernel.ampapi.endpoints.common;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
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

    @Override
    public Map<String, String> translateLabel(String label) {
        Set<String> trnLocaleCodes = TranslationSettings.getCurrent().getTrnLocaleCodes();
        Site site = SiteUtils.getDefaultSite();
        return trnLocaleCodes.stream()
                .collect(toMap(Function.identity(), locale -> TranslatorWorker.translateText(label, locale, site)));
    }
}
