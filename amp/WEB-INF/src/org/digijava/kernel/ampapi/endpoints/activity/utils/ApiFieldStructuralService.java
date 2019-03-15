package org.digijava.kernel.ampapi.endpoints.activity.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;

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
        
        Map<String, FieldType> ampFieldsType = getFieldsTypeMap(ampFields);
        Map<String, FieldType> clientFieldsType = getFieldsTypeMap(clientFields);
        
        for (String fieldName : clientFieldsType.keySet()) {
            if (ampFieldsType.containsKey(fieldName)
                    && ampFieldsType.get(fieldName) != clientFieldsType.get(fieldName)) {
                return true;
            }
        }
        
        ampFieldsType.keySet().removeAll(clientFieldsType.keySet());
        
        return ampFieldsType.size() > 0;
    }
    
    private Map<String, FieldType> getFieldsTypeMap(List<APIField> apiFields) {
        Map<String, FieldType> fieldsTypeMap = new HashMap<>();
        for (APIField apiField : apiFields) {
            addApiFieldToMap(apiField, fieldsTypeMap, "");
        }
        return fieldsTypeMap;
    }
    
    private void addApiFieldToMap(APIField apiField, Map<String, FieldType> apiFieldMap, String fieldPath) {
        if (StringUtils.isNotEmpty(fieldPath)) {
            fieldPath += "~";
        }
        fieldPath += apiField.getFieldName();
        
        apiFieldMap.put(fieldPath, apiField.getApiType().getFieldType());
        for (APIField field : apiField.getChildren()) {
            addApiFieldToMap(field, apiFieldMap, fieldPath);
        }
    }
    
}
