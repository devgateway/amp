package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.digijava.kernel.ampapi.endpoints.common.CommonSettings;
import org.digijava.kernel.ampapi.endpoints.resource.AmpResource;
import org.digijava.kernel.services.sync.SyncDAO;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * A faster version of {@link FieldsEnumerator}.
 *
 * @author Octavian Ciubotaru
 */
public class CachingFieldsEnumerator {

    private SyncDAO syncDAO;

    private FieldsEnumerator fieldsEnumerator;

    private Map<Class, List<APIField>> cache = new ConcurrentHashMap<>();

    private Timestamp cachedUpToDate;

    public CachingFieldsEnumerator(SyncDAO syncDAO, FieldsEnumerator fieldsEnumerator) {
        this.syncDAO = syncDAO;
        this.fieldsEnumerator = fieldsEnumerator;
    }

    public List<APIField> getContactFields() {
        return getAllAvailableFields(AmpContact.class);
    }

    public List<APIField> getActivityFields() {
        return getAllAvailableFields(AmpActivityFields.class);
    }

    public List<APIField> getResourceFields() {
        return getAllAvailableFields(AmpResource.class);
    }

    public List<APIField> getCommonSettingsFields() {
        return getAllAvailableFields(CommonSettings.class);
    }

    /**
     * Cached version of {@link FieldsEnumerator#getAllAvailableFields(Class)}
     */
    private List<APIField> getAllAvailableFields(Class<?> clazz) {
        Timestamp lastModificationDate = syncDAO.getLastModificationDateForFieldDefinitions();
        if (cachedUpToDate == null) {
            cachedUpToDate = lastModificationDate;
        }
        if (lastModificationDate.after(cachedUpToDate)) {
            cache.clear();
        }
        cachedUpToDate = lastModificationDate;
        return cache.computeIfAbsent(clazz, key -> fieldsEnumerator.getAllAvailableFields(clazz));
    }

    public List<String> findActivityFieldPaths(Predicate<Field> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, AmpActivityFields.class);
    }

    public List<String> findContactFieldPaths(Predicate<Field> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, AmpContact.class);
    }

    public List<String> findResourceFieldPaths(Predicate<Field> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, AmpResource.class);
    }

    public List<String> findCommonFieldPaths(Predicate<Field> fieldFilter) {
        return fieldsEnumerator.findFieldPaths(fieldFilter, CommonSettings.class);
    }
}
