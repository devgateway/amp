package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;

/**
 * @author Viorel Chihai
 */
public class ResourceImporter extends ObjectImporter {

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
        
        if (resource.getType() != null) {
            tdd.setCmDocTypeId(resource.getType().getId());
        }
        
        Calendar calendar = Calendar.getInstance();
        tdd.setDate(calendar.getTime());
        tdd.setYearofPublication(String.valueOf(calendar.get(Calendar.YEAR)));
        
        if (StringUtils.isNotBlank(tdd.getWebLink())) {
            tdd.setWebLink(resource.getWebLink());
        }
        
        return tdd;
    }

    @Override
    protected void configureCustom(Object obj, APIField fieldDef) {
        super.configureCustom(obj, fieldDef);

        if (obj instanceof AmpContactProperty) {
            ((AmpContactProperty) obj).setName(fieldDef.getDiscriminator());
        }
    }

    public AmpResource getResource() {
        return resource;
    }
}
