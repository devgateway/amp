package org.digijava.kernel.ampapi.endpoints.resource;

import static java.util.Collections.singletonList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.AmpFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectConversionException;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.StreamUtils;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
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

    /**
     * Create a web link resource.
     *
     * @param newJson json description of the resource
     * @return errors if any
     */
    public List<ApiErrorMessage> createResource(JsonBean newJson) {
        return importResource(newJson, null);
    }

    /**
     * Create a web link or document resource.
     *
     * @param newJson json description of the resource
     * @param formFile file for document resource, may be null for web link resources
     * @return errors if any
     */
    public List<ApiErrorMessage> createResource(JsonBean newJson, FormFile formFile) {
        return importResource(newJson, formFile);
    }

    private List<ApiErrorMessage> importResource(JsonBean newJson, FormFile formFile) {
        this.newJson = newJson;

        List<APIField> fieldsDef = AmpFieldsEnumerator.PRIVATE_ENUMERATOR.getResourceFields();
        
        String privateAttr = newJson.getString(ResourceEPConstants.PRIVATE);
        
        if (StringUtils.isBlank(privateAttr)) {
            return singletonList(ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.PRIVATE));
        }
        
        if (!Boolean.parseBoolean(privateAttr)) {
            return singletonList(ResourceErrors.PRIVATE_RESOURCE_SUPPORTED_ONLY
                    .withDetails(ResourceEPConstants.PRIVATE));
        }
        
        TeamMember teamMemberCreator = null;
        if (AmpOfflineModeHolder.isAmpOfflineMode()) {
            List<ApiErrorMessage> errorMessages = validateCreatorEmailTeam(newJson);
            if (errorMessages != null) {
                return errorMessages;
            }
            
            String creatorEmail = newJson.getString(ResourceEPConstants.CREATOR_EMAIL);
            Long teamId = getLongOrNull(newJson.get(ResourceEPConstants.TEAM));
            AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(creatorEmail, teamId);
            teamMemberCreator = TeamMemberUtil.getTeamMember(teamMember.getAmpTeamMemId());
        } else {
            teamMemberCreator = TeamMemberUtil.getLoggedInTeamMember();
        }

        if (formFile == null && StringUtils.isBlank(String.valueOf(newJson.get(ResourceEPConstants.WEB_LINK)))) {
            return singletonList(ResourceErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.WEB_LINK));
        }
        
        if (formFile != null) {
            long maxSizeInMB = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
            long maxFileSizeInBytes = maxSizeInMB * FileUtils.ONE_MB;
            if (formFile.getFileSize() > maxFileSizeInBytes) {
                long fileSizeInMB = formFile.getFileSize() / FileUtils.ONE_MB;
                String errorMessage = String.format("%s %sMB. %s %sMB", TranslatorWorker.translateText("File size is"), 
                        fileSizeInMB, TranslatorWorker.translateText("Max size allowed is"), maxSizeInMB);
                return singletonList(ResourceErrors.FILE_SIZE_INVALID.withDetails(errorMessage));
            }
        }
        
        try {
            resource = new AmpResource();
            resource = (AmpResource) validateAndImport(resource, null, fieldsDef, newJson.any(), null, null);

            if (resource == null) {
                throw new ObjectConversionException();
            }
            
            resource.setCreatorEmail(teamMemberCreator.getEmail());
            resource.setTeam(teamMemberCreator.getTeamId());
            resource.setAddingDate(new Date());
            
            if (errors.isEmpty()) {
                ActionMessages messages = new ActionMessages();
                TemporaryDocumentData tdd = getTemporaryDocumentData(resource, formFile);
                NodeWrapper node = tdd.saveToRepository(TLSUtils.getRequest(), teamMemberCreator, messages);
    
                if (node != null) {
                    resource.setUuid(node.getUuid());
                } else {
                    reportErrors(messages);
                }
            }
        } catch (ObjectConversionException | RuntimeException e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException("Failed to create resource", e);
            }
        }

        return new ArrayList<>(errors.values());
    }

    /**
     * Copies struts messages to API error messages. If struts messages are empty will report a single API error
     * message 'Failed without specifying a reason.'.
     * @param messages struts messages to copy to API error messages
     */
    private void reportErrors(ActionMessages messages) {
        if (messages.isEmpty()) {
            errors.put(0, new ApiErrorMessage(0, "Failed without specifying a reason."));
        } else {
            StreamUtils.asStream((Iterator<Object>) messages.get())
                    .forEach(m -> errors.put(0, new ApiErrorMessage(0, m.toString())));
        }
    }

    private TemporaryDocumentData getTemporaryDocumentData(AmpResource resource, FormFile formFile) {
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
        } else {
            tdd.setFileSize(formFile.getFileSize());
            tdd.setFormFile(formFile);
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
    
    private String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue) {
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
    
    private Long getLongOrNull(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            return null;
        }
    }
    
    /**
     * validates creator email and team
     * 
     * @param newJson
     * @return
     */
    private List<ApiErrorMessage> validateCreatorEmailTeam(JsonBean newJson) {
        
        Object creatorEmail = newJson.get(ResourceEPConstants.CREATOR_EMAIL);
        if (creatorEmail == null || StringUtils.isBlank(creatorEmail.toString())) {
            return singletonList(ResourceErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.CREATOR_EMAIL));
        }
        
        Object team = newJson.get(ResourceEPConstants.TEAM);
        if (team == null || getLongOrNull(team) == null) {
            return singletonList(ResourceErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.TEAM));
        }
        
        User creatorUser = creatorEmail != null ? UserUtils.getUserByEmailAddress(creatorEmail.toString()) : null;
        if (creatorUser == null) {
            return singletonList(ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.CREATOR_EMAIL));
        }
        
        Long teamId = getLongOrNull(team);
        AmpTeam ampTeam = TeamUtil.getAmpTeam(teamId);
        
        if (ampTeam == null) {
            return singletonList(ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.TEAM));
        }

        AmpTeamMember ampTeamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(creatorUser.getEmail(),
                ampTeam.getAmpTeamId());

        if (ampTeamMember == null) {
            List<String> errorDetails = new ArrayList<>();
            errorDetails.add(ResourceEPConstants.CREATOR_EMAIL);
            errorDetails.add(ResourceEPConstants.TEAM);

            return singletonList(ResourceErrors.INVALID_TEAM_MEMBER.withDetails(errorDetails));
        }

        return null;
    }
    
    @Override
    protected boolean ignoreUnknownFields() {
        return AmpOfflineModeHolder.isAmpOfflineMode();
    }
    
}
