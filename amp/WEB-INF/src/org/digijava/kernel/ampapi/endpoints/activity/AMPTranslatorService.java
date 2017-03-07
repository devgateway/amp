package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author Octavian Ciubotaru
 */
public class AMPTranslatorService implements TranslatorService {

    @Override
    public Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException {
        return TranslatorWorker.getAllTranslationOfBody(text, siteId);
    }
}
