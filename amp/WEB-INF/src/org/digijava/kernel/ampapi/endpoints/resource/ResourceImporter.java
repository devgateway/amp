package org.digijava.kernel.ampapi.endpoints.resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.CommonErrors;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.common.ReflectionUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
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
        super(new InputValidatorProcessor(InputValidatorProcessor.getResourceFormatValidators()),
                new InputValidatorProcessor(InputValidatorProcessor.getResourceBusinessRulesValidators()),
                AmpFieldsEnumerator.getEnumerator().getResourceFields());
    }

    /**
     * Create a web link resource.
     *
     * @param newJson json description of the resource
     * @return errors if any
     */
    public ResourceImporter createResource(Map<String, Object> newJson) {
        return createResource(newJson, null);
    }

    /**
     * Create a web link or document resource.
     *
     * @param newJson json description of the resource
     * @param formFile file for document resource, may be null for web link resources
     * @return ResourceImporter instance
     */
    public ResourceImporter createResource(Map<String, Object> newJson, FormFile formFile) {
        this.newJson = newJson;

        String privateAttr = String.valueOf(newJson.get(ResourceEPConstants.PRIVATE));

        if (StringUtils.isBlank(privateAttr)) {
            addError(ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.PRIVATE));
            return this;
        }

        if (!Boolean.parseBoolean(privateAttr)) {
            addError(ResourceErrors.PRIVATE_RESOURCE_SUPPORTED_ONLY.withDetails(ResourceEPConstants.PRIVATE));
            return this;
        }

        TeamMember teamMemberCreator = null;
        if (AmpClientModeHolder.isOfflineClient()) {
            ApiErrorMessage errorMessage = validateCreatorEmailTeam(newJson);
            if (errorMessage != null) {
                addError(errorMessage);
                return this;
            }

            String creatorEmail = String.valueOf(newJson.get(ResourceEPConstants.CREATOR_EMAIL));
            Long teamId = getLongOrNull(newJson.get(ResourceEPConstants.TEAM));
            AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(creatorEmail, teamId);
            teamMemberCreator = TeamMemberUtil.getTeamMember(teamMember.getAmpTeamMemId());
        } else {
            teamMemberCreator = TeamMemberUtil.getLoggedInTeamMember();
        }

        if (formFile == null && ResourceType.FILE.getId().equals(newJson.get(ResourceEPConstants.RESOURCE_TYPE))) {
            addError(ResourceErrors.FILE_NOT_FOUND);
            return this;
        }

        if (formFile != null) {
            if (ResourceType.LINK.getId().equals(newJson.get(ResourceEPConstants.RESOURCE_TYPE))) {
                String details = String.format("%s '%s'. %s '%s'", TranslatorWorker.translateText("Resource type is"),
                        ResourceType.LINK.getId(), TranslatorWorker.translateText("Resource type should be"),
                        ResourceType.FILE.getId());
                addError(ResourceErrors.RESOURCE_TYPE_INVALID.withDetails(details));
            }

            long maxSizeInMB = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
            long maxFileSizeInBytes = maxSizeInMB * FileUtils.ONE_MB;
            if (formFile.getFileSize() > maxFileSizeInBytes) {
                long fileSizeInMB = formFile.getFileSize() / FileUtils.ONE_MB;
                String errorMessage = String.format("%s %sMB. %s %sMB", TranslatorWorker.translateText("File size is"),
                        fileSizeInMB, TranslatorWorker.translateText("Max size allowed is"), maxSizeInMB);
                addError(ResourceErrors.FILE_SIZE_INVALID.withDetails(errorMessage));
                return this;
            }
        }

        try {
            resource = new AmpResource();
            validateAndImport(resource, newJson);

            if (errors.isEmpty()) {
                if (ResourceType.LINK.equals(resource.getResourceType())) {
                    resource.setFileName(null);
                } else {
                    resource.setWebLink(null);
                }

                resource.setCreatorEmail(teamMemberCreator.getEmail());
                resource.setTeam(teamMemberCreator.getTeamId());
                resource.setAddingDate(new Date());
                TemporaryDocumentData tdd = getTemporaryDocumentData(resource, formFile);
                NodeWrapper node = tdd.saveToRepository(TLSUtils.getRequest(), teamMemberCreator);

                if (node != null) {
                    resource.setUuid(node.getUuid());
                }
            } else {
                PersistenceManager.rollbackCurrentSessionTx();
            }
        } catch (RuntimeException e) {
            // FIXME is any other RuntimeException simply ignored?
            if (e instanceof RuntimeException) {
                throw new RuntimeException("Failed to create resource", e);
            }
        }

        return this;
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

        if (ResourceType.LINK.equals(resource.getResourceType())) {
            tdd.setWebLink(resource.getWebLink());
        } else {
            tdd.setWebLink(null);
            tdd.setFileSize(formFile.getFileSize());
            tdd.setFormFile(formFile);
        }

        return tdd;
    }

    public AmpResource getResource() {
        return resource;
    }

    @Override
    protected String extractString(APIField apiField, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(apiField, parentObj, jsonValue);
    }

    private String extractTranslationsOrSimpleValue(APIField apiField, Object parentObj, Object jsonValue) {
        TranslationType trnType = apiField.getTranslationType();
        String value = null;
        if (TranslationType.NONE == trnType) {
            value = (String) jsonValue;
        } else if (TranslationType.RESOURCE == trnType) {
            Map<String, Object> resourceText = null;
            if (trnSettings.isMultilingual()) {
                resourceText = (Map<String, Object>) jsonValue;
            } else {
                resourceText = new HashMap<>();
                resourceText.put(trnSettings.getDefaultLangCode(), jsonValue);
            }
            value = extractResourceTranslation(apiField, parentObj, resourceText);
        }

        return value;
    }

    private String extractResourceTranslation(APIField apiField, Object parentObj, Map<String, Object> jsonValue) {
        try {
            Field field = ReflectionUtil.getField(parentObj, apiField.getFieldNameInternal());
            String translatedMapFieldName = field.getAnnotation(ResourceTextField.class).translationsField();
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
     * @return ApiErrorMessage
     */
    private ApiErrorMessage validateCreatorEmailTeam(Map<String, Object> newJson) {

        Object creatorEmail = newJson.get(ResourceEPConstants.CREATOR_EMAIL);
        if (creatorEmail == null || StringUtils.isBlank(creatorEmail.toString())) {
            return ResourceErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.CREATOR_EMAIL);
        }

        Object team = newJson.get(ResourceEPConstants.TEAM);
        if (team == null || getLongOrNull(team) == null) {
            return ResourceErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.TEAM);
        }

        User creatorUser = creatorEmail != null ? UserUtils.getUserByEmailAddress(creatorEmail.toString()) : null;
        if (creatorUser == null) {
            return ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.CREATOR_EMAIL);
        }

        Long teamId = getLongOrNull(team);
        AmpTeam ampTeam = TeamUtil.getAmpTeam(teamId);

        if (ampTeam == null) {
            return ResourceErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.TEAM);
        }

        AmpTeamMember ampTeamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(creatorUser.getEmail(),
                ampTeam.getAmpTeamId());

        if (ampTeamMember == null) {
            List<String> errorDetails = new ArrayList<>();
            errorDetails.add(ResourceEPConstants.CREATOR_EMAIL);
            errorDetails.add(ResourceEPConstants.TEAM);

            return ResourceErrors.INVALID_TEAM_MEMBER.withDetails(errorDetails);
        }

        return null;
    }

    /**
     * Get the result of import/update resource
     *
     * @return JsonApiResponse the result of the import or update action
     */
    public JsonApiResponse getResult() {
        Map<String, Object> result = null;
        if (errors.isEmpty() && resource != null) {
            result = new LinkedHashMap<>();
            result.put(ResourceEPConstants.UUID, resource.getUuid());
            if (TranslationSettings.getCurrent().isMultilingual()) {
                result.put(ResourceEPConstants.TITLE, resource.getTranslatedTitles());
                result.put(ResourceEPConstants.DESCRIPTION, resource.getTranslatedDescriptions());
                result.put(ResourceEPConstants.NOTE, resource.getTranslatedNotes());
            } else {
                result.put(ResourceEPConstants.TITLE, resource.getTitle());
                result.put(ResourceEPConstants.DESCRIPTION, resource.getDescription());
                result.put(ResourceEPConstants.NOTE, resource.getNote());
            }

            if (resource.getType() != null) {
                result.put(ResourceEPConstants.TYPE, resource.getType().getId());
            }
            if (ResourceType.LINK.equals(resource.getResourceType())) {
                result.put(ResourceEPConstants.WEB_LINK, resource.getWebLink());
            } else {
                result.put(ResourceEPConstants.FILE_NAME, resource.getFileName());
            }
            result.put(ResourceEPConstants.RESOURCE_TYPE, resource.getResourceType().getId());
            result.put(ResourceEPConstants.ADDING_DATE,
                    DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(resource.getAddingDate()));
            result.put(ResourceEPConstants.TEAM, resource.getTeam());
        }

        if (result == null) {
            result = new HashMap<String, Object>() {{
                put(ResourceEPConstants.RESOURCE, getNewJson());
            }};
            if (errors.isEmpty()) {
                addError(CommonErrors.UNKOWN_ERROR);
            }
        }

        return buildResponse(result, null);
    }

}
