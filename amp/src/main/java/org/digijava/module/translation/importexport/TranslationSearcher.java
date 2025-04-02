package org.digijava.module.translation.importexport;

import org.digijava.kernel.entity.Message;
import org.digijava.module.translation.util.ImportExportUtil;
import org.hibernate.Session;

/**
 * Defines interface for searching translation during import export.
 * There are several ways for searching, through cache, bypassing cache or some combined version.
 * @see ImportExportUtil
 * @see ImportExportOption
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public interface TranslationSearcher {
    Message get(String key, String locale, Long siteId, Session dbSession) throws Exception;
    Message get(String key, String locale, Long siteId) throws Exception;
}
