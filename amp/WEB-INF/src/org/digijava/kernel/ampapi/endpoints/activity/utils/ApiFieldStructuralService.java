package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;

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
