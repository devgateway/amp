package org.digijava.kernel.ampapi.endpoints.activity.utils;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.junit.Test;

public class ApiFieldStructuralServiceTest {
    
    ApiFieldStructuralService service = ApiFieldStructuralService.getInstance();
    
    @Test
    public void existsStructuralChangesEquals() {
        assertFalse(service.existsStructuralChanges(getAmpApiFields(), getAmpApiFields()));
    }
    
    @Test
    public void existsStructuralChangesEmpty() {
        assertTrue(service.existsStructuralChanges(getAmpApiFields(), new ArrayList<>()));
    }
    
    @Test
    public void existsStructuralChangesMissingClientField() {
        List<APIField> clientFields = new ArrayList<>();
    
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
    
        clientFields.add(newLongField("id"));
        clientFields.add(listFields);
        
        assertTrue(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    @Test
    public void existsStructuralChangesMissingAmpField() {
        List<APIField> clientFields = new ArrayList<>();
    
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
    
        clientFields.add(newLongField("id"));
        clientFields.add(newStringField("title"));
        clientFields.add(newStringField("title2"));
        clientFields.add(newListPrimitiveField("primitives", Integer.class, FieldType.LONG));
        clientFields.add(listFields);
        
        assertFalse(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    @Test
    public void existsStructuralChangesDifferentType() {
        List<APIField> clientFields = new ArrayList<>();
    
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
    
        clientFields.add(newStringField("id"));
        clientFields.add(newStringField("title"));
        clientFields.add(listFields);
        
        assertTrue(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    @Test
    public void existsStructuralChangesDifferentTypeLists() {
        List<APIField> clientFields = new ArrayList<>();
        
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newStringField("id")));
        
        clientFields.add(newLongField("id"));
        clientFields.add(newStringField("title"));
        clientFields.add(listFields);
        
        assertTrue(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    @Test
    public void existsStructuralChangesDifferentEntityTypeInList() {
        List<APIField> clientFields = new ArrayList<>();
    
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
    
        clientFields.add(newLongField("id"));
        clientFields.add(newStringField("title"));
        clientFields.add(listFields);
        clientFields.add(newListPrimitiveField("primitives", String.class, FieldType.STRING));
        
        assertTrue(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    @Test
    public void notExistsStructuralChangesSameEntityTypeInList() {
        List<APIField> clientFields = new ArrayList<>();
        
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
        
        clientFields.add(newLongField("id"));
        clientFields.add(newStringField("title"));
        clientFields.add(listFields);
        clientFields.add(newListPrimitiveField("primitives", Integer.class, FieldType.LONG));
        
        assertFalse(service.existsStructuralChanges(getAmpApiFields(), clientFields));
    }
    
    public List<APIField> getAmpApiFields() {
        List<APIField> fields = new ArrayList<>();
        
        APIField listFields = newListField("list");
        listFields.setChildren(ImmutableList.of(newLongField("id")));
    
        fields.add(newLongField("id"));
        fields.add(newStringField("title"));
        fields.add(newListPrimitiveField("primitives", Integer.class, FieldType.LONG));
        fields.add(listFields);
        
        return fields;
    }
    
    
    private APIField newListField(String fieldName) {
        APIField field = new APIField();
        field.setFieldName(fieldName);
        field.setApiType(new APIType(Object.class, FieldType.LIST, FieldType.OBJECT));
        return field;
    }
    
    private APIField newListPrimitiveField(String fieldName, Class<?> type, FieldType itemType) {
        APIField field = new APIField();
        field.setFieldName(fieldName);
        field.setApiType(new APIType(type, FieldType.LIST, itemType));
        return field;
    }
    
    private APIField newStringField(String fieldName) {
        APIField field = new APIField();
        field.setFieldName(fieldName);
        field.setApiType(new APIType(String.class, FieldType.STRING));
        return field;
    }
    
    private APIField newLongField(String fieldName) {
        APIField field = new APIField();
        field.setFieldName(fieldName);
        field.setApiType(new APIType(Long.class, FieldType.LONG));
        return field;
    }
}