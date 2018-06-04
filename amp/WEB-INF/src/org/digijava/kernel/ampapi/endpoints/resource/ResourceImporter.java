package org.digijava.kernel.ampapi.endpoints.resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;

/**
 * @author Viorel Chihai
 */
public class ResourceImporter extends ObjectImporter {
    
    private static final Logger logger = Logger.getLogger(ResourceImporter.class);

    private AmpResource resource;

    public ResourceImporter() {
        super(AmpResource.class, new InputValidatorProcessor(InputValidatorProcessor.getResourceValidators()));
    }

    public List<ApiErrorMessage> createResource(JsonBean newJson) {
        return importResource(null, newJson);
    }

    private List<ApiErrorMessage> importResource(Long resourceId, JsonBean newJson) {
        this.newJson = newJson;

        List<APIField> fieldsDef = AmpFieldsEnumerator.PRIVATE_ENUMERATOR.getResourceFields();

        try {
            resource = new AmpResource();
            resource = (AmpResource) validateAndImport(resource, null, fieldsDef, newJson.any(), null, null);

            if (resource == null) {
                throw new ObjectConversionException();
            }
            
            resource.setAddingDate(new Date());
            
            ActionMessages messages = new ActionMessages();
            TemporaryDocumentData tdd = getTemporaryDocumentData(resource);
            NodeWrapper node = tdd.saveToRepository(TLSUtils.getRequest(), messages);
            
            resource.setUuid(node.getUuid());
        } catch (ObjectConversionException | RuntimeException e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException("Failed to create resource", e);
            }
        }

        return new ArrayList<>(errors.values());
    }

    protected TemporaryDocumentData getTemporaryDocumentData(AmpResource resource) {
        TemporaryDocumentData tdd = new TemporaryDocumentData();
        
        tdd.setTitle(resource.getTitle());
        tdd.setName(resource.getFileName());
        tdd.setDescription(resource.getDescription());
        tdd.setNotes(resource.getNote());
        tdd.setTranslatedTitles(resource.getTranslatedTitles());
        tdd.setTranslatedNotes(resource.getTranslatedNotes());
        tdd.setTranslatedDescriptions(resource.getTranslatedDescriptions());
        
        if (resource.getType() != null) {
            tdd.setCmDocTypeId(resource.getType().getId());
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resource.getAddingDate());
        tdd.setDate(calendar.getTime());
        tdd.setYearofPublication(String.valueOf(calendar.get(Calendar.YEAR)));
        
        if (StringUtils.isNotBlank(resource.getWebLink())) {
            tdd.setWebLink(resource.getWebLink());
        }
        
        return tdd;
    }

    public AmpResource getResource() {
        return resource;
    }
    
    @Override
    protected String extractString(Field field, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(field, parentObj, jsonValue);
    }
    
    protected String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue) {
        TranslationType trnType = trnSettings.getTranslatableType(field);
        String value = null;
        if (TranslationType.NONE == trnType) {
            value = (String) jsonValue;
        } else if (TranslationType.RESOURCE == trnType) {
            Map<String, Object> resourceText = null;
            if (trnSettings.isMultilingual()) {
                resourceText = (Map<String, Object>) jsonValue;
            } else {
                resourceText = new HashMap<String, Object>();
                resourceText.put(trnSettings.getDefaultLangCode(), jsonValue);
            }
            value = extractResourceTranslation(field, parentObj, resourceText);
        }
        
        return value;
    }

    private String extractResourceTranslation(Field field, Object parentObj, Map<String, Object> jsonValue) {
        String translatedMapFieldName = field.getAnnotation(ResourceTextField.class).translationsField();
        try {
            Field translatedMapField = parentObj.getClass().getDeclaredField(translatedMapFieldName);
            translatedMapField.setAccessible(true);
            translatedMapField.set(parentObj, jsonValue);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        
        return (String) jsonValue.get(trnSettings.getDefaultLangCode());
    }
    
}
