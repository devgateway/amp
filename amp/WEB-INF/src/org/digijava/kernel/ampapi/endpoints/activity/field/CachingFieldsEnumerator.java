package org.digijava.kernel.ampapi.endpoints.activity.field;

import org.digijava.kernel.ampapi.endpoints.common.CommonSettings;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.services.sync.SyncDAO;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpContact;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * A faster version of {@link FieldsEnumerator}.
 *
 * @author Octavian Ciubotaru
 */
public class CachingFieldsEnumerator {

    private SyncDAO syncDAO;

    private FieldsEnumerator fieldsEnumerator;

    private Map<Class, APIField> cache = new ConcurrentHashMap<>();
    private Map<Class, APIField> iatiImporterCache = new ConcurrentHashMap<>();

    private Timestamp cachedUpToDate;

    public CachingFieldsEnumerator(SyncDAO syncDAO, FieldsEnumerator fieldsEnumerator) {
        this.syncDAO = syncDAO;
        this.fieldsEnumerator = fieldsEnumerator;
    }

    public List<APIField> getContactFields() {
        return getContactField().getChildren();
    }

    public APIField getContactField() {
        return getAllAvailableFields(AmpContact.class);
    }

    public List<APIField> getActivityFields() {
        return getActivityField().getChildren();
    }

    public APIField getActivityField() {
        return getAllAvailableFields(AmpActivityFields.class, AmpClientModeHolder.isIatiImporterClient());
    }

    public List<APIField> getResourceFields() {
        return getResourceField().getChildren();
    }

    public APIField getResourceField() {
        return getAllAvailableFields(AmpResource.class);
    }

    public List<APIField> getCommonSettingsFields() {
        return getAllAvailableFields(CommonSettings.class).getChildren();
    }

    /**
     * Cached version of {@link FieldsEnumerator#getAllAvailableFields(Class)}
     */
    private APIField getAllAvailableFields(Class<?> clazz) {
        return getAllAvailableFields(clazz, false);
    }
    
    private APIField getAllAvailableFields(Class<?> clazz, boolean useIatiImporterCache) {
        Map<Class, APIField> actualCache = useIatiImporterCache ? iatiImporterCache : cache;
        Timestamp lastModificationDate = syncDAO.getLastModificationDateForFieldDefinitions();
        if (cachedUpToDate == null) {
            cachedUpToDate = lastModificationDate;
        }
        if (lastModificationDate.after(cachedUpToDate)) {
            cache.clear();
            iatiImporterCache.clear();
        }
        cachedUpToDate = lastModificationDate;
        return actualCache.computeIfAbsent(clazz, key -> fieldsEnumerator.getMetaModel(clazz));
    }

    public List<String> findActivityFieldPaths(Predicate<APIField> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, getActivityFields());
    }

    public List<String> findContactFieldPaths(Predicate<APIField> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, getContactFields());
    }

    public List<String> findResourceFieldPaths(Predicate<APIField> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, getResourceFields());
    }

    public List<String> findCommonFieldPaths(Predicate<APIField> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, getCommonSettingsFields());
    }
}
