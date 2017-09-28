package org.digijava.module.translation.importexport;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.entity.Message;
import org.digijava.module.translation.util.ImportExportUtil;
import org.hibernate.Session;

/**
 * Defines parameters set for import/export of translations.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class ImportExportOption {
    
    /**
     * Translator searcher.
     * Implementations may search translations in different way.
     * For example one can search in cache, other directly in db or use some other methods.
     * Most common implementations are defined inside {@link ImportExportUtil} and 
     * can be retrieved for static methods in that class.
     */
    private TranslationSearcher searcher;
    
    /**
     * Import types for each language.
     * Keys in this map are language codes like en, fr...
     */
    private Map<String, ImportType> typeByLanguage;
    
    /**
     * Set of language codes that should take part in operations.
     * This is usually user selected language codes.  
     */
    private Set<String> localesToSave;
    
    /**
     * Database session to save all messages with.
     */
    private Session dbSession;
    
    /**
     * Messages that have bee affected
     */
    private List<Message> affectedMessages;
    
    public TranslationSearcher getSearcher() {
        return searcher;
    }
    public void setSearcher(TranslationSearcher searcher) {
        this.searcher = searcher;
    }
    public Set<String> getLocalesToSave() {
        return localesToSave;
    }
    public void setLocalesToSave(Set<String> localesToSave) {
        this.localesToSave = localesToSave;
    }
    public Session getDbSession() {
        return dbSession;
    }
    public void setDbSession(Session dbSession) {
        this.dbSession = dbSession;
    }
    public void setTypeByLanguage(Map<String, ImportType> typeByLanguage) {
        this.typeByLanguage = typeByLanguage;
    }
    public Map<String, ImportType> getTypeByLanguage() {
        return typeByLanguage;
    }
    public void setAffectedMessages(List<Message> affectedMessages) {
        this.affectedMessages = affectedMessages;
    }
    public List<Message> getAffectedMessages() {
        return affectedMessages;
    }
    
}
