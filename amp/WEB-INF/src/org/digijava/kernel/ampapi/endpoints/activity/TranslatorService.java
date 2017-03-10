package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;

/**
 * @author Octavian Ciubotaru
 */
public interface TranslatorService {

    Collection<Message> getAllTranslationOfBody(String text, Long siteId) throws WorkerException;
}
