package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.APIWorkspaceMemberFieldList;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;

import org.digijava.kernel.services.AmpFieldsEnumerator;

import static org.digijava.kernel.services.AmpFieldsEnumerator.TYPE_ACTIVITY;
import static org.digijava.kernel.services.AmpFieldsEnumerator.TYPE_CONTACT;
import static org.digijava.kernel.services.AmpFieldsEnumerator.TYPE_RESOURCE;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_ACTIVITY;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_CONTACT;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_RESOURCE;

/**
 * Utility class used for detecting structural changes of api fields during AMP offline synchronization
 */
public final class ApiFieldStructuralService {

    private static ApiFieldStructuralService structuralService;

    private ApiFieldStructuralService() {

    }

    public static ApiFieldStructuralService getInstance() {
        if (structuralService == null) {
            structuralService = new ApiFieldStructuralService();
        }

        return structuralService;
    }

    public boolean existsStructuralChangesBasedOnWs(List<APIWorkspaceMemberFieldList> clientFields, String collection) {
        AtomicBoolean changes = new AtomicBoolean(false);
        clientFields.forEach(clientField -> {
            List<APIWorkspaceMemberFieldList> ampFields = null;
            if (collection.equals(COLLECTION_TYPE_ACTIVITY)) {
                ampFields = AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(clientField.getWsMemberIds(),
                        TYPE_ACTIVITY);
            } else if (collection.equals(COLLECTION_TYPE_CONTACT)) {
                ampFields = AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(clientField.getWsMemberIds(),
                        TYPE_CONTACT);
            } else if (collection.equals(COLLECTION_TYPE_RESOURCE)) {
                ampFields = AmpFieldsEnumerator.getAvailableFieldsBasedOnWs(clientField.getWsMemberIds(),
                        TYPE_RESOURCE);
            }
            ampFields.forEach(ampField -> {
                if (ampField.getWsMemberIds().equals(clientField.getWsMemberIds())) {
                    boolean localChanges = existsStructuralChanges(ampField.getFields(), clientField.getFields());
                    if (localChanges) {
                        changes.set(true);
                    }
                }
            });
        });
        return changes.get();
    }

    public boolean existsStructuralChanges(List<APIField> ampFields, List<APIField> clientFields) {

        if (clientFields == null || clientFields.isEmpty()) {
            return true;
        }

        Map<String, APIType> ampFieldsType = getFieldsTypeMap(ampFields);
        Map<String, APIType> clientFieldsType = getFieldsTypeMap(clientFields);

        for (String fieldName : clientFieldsType.keySet()) {
            APIType clientApiType = clientFieldsType.get(fieldName);
            APIType ampApiType = ampFieldsType.getOrDefault(fieldName, null);
            if (ampApiType != null) {
                if (clientApiType.getFieldType() != ampApiType.getFieldType()
                        || clientApiType.getItemType() != ampApiType.getItemType()) {
                    return true;
                }
            }
        }

        ampFieldsType.keySet().removeAll(clientFieldsType.keySet());

        return ampFieldsType.size() > 0;
    }

    private Map<String, APIType> getFieldsTypeMap(List<APIField> apiFields) {
        Map<String, APIType> fieldsTypeMap = new HashMap<>();
        for (APIField apiField : apiFields) {
            addApiFieldToMap(apiField, fieldsTypeMap, "");
        }
        return fieldsTypeMap;
    }

    private void addApiFieldToMap(APIField apiField, Map<String, APIType> apiFieldMap, String fieldPath) {
        if (StringUtils.isNotEmpty(fieldPath)) {
            fieldPath += "~";
        }
        fieldPath += apiField.getFieldName();

        apiFieldMap.put(fieldPath, apiField.getApiType());
        for (APIField field : apiField.getChildren()) {
            addApiFieldToMap(field, apiFieldMap, fieldPath);
        }
    }

}
