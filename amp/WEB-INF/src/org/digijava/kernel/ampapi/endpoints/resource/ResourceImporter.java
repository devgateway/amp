package org.digijava.kernel.ampapi.endpoints.resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings.TranslationType;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;

import java.time.LocalDate;
import java.util.*;

import static org.dgfoundation.amp.ar.ArConstants.MIN_SUPPORTED_YEAR;
import static org.digijava.module.aim.util.AmpMath.isLong;

/**
 * @author Viorel Chihai
 */
public class ResourceImporter extends ObjectImporter<AmpResource> {

    private static final Logger logger = Logger.getLogger(ResourceImporter.class);

    private AmpResource resource;

    public ResourceImporter() {
        super(new InputValidatorProcessor(InputValidatorProcessor.getFormatValidators()),
                AmpFieldsEnumerator.getEnumerator().getResourceField(),
                TLSUtils.getSite());
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
     * @param newJson  json description of the resource
     * @param formFile file for document resource, may be null for web link resources
     * @return ResourceImporter instance
     */
    public ResourceImporter createResource(Map<String, Object> newJson, FormFile formFile) {
        this.newJson = newJson;

        String privateAttr = String.valueOf(newJson.get(ResourceEPConstants.PRIVATE));

        if (StringUtils.isBlank(privateAttr)) {
            addError(ValidationErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.PRIVATE));
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

        ApiErrorMessage errorMessage = validateYearOfPublication(newJson);
        if (errorMessage != null) {
            addError(errorMessage);
            return this;
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
            throw new RuntimeException("Failed to create resource", e);
        }

        return this;
    }

    private TemporaryDocumentData getTemporaryDocumentData(AmpResource resource, FormFile formFile) {
        TemporaryDocumentData tdd = new TemporaryDocumentData();

        tdd.setName(resource.getFileName());
        if (resource.getTitle() != null) {
            tdd.setTitle(resource.getTitle().getOrBuildText());
            tdd.setTranslatedTitles(resource.getTitle().getOrBuildTranslations());
        }
        if (resource.getDescription() != null) {
            tdd.setDescription(resource.getDescription().getOrBuildText());
            tdd.setTranslatedDescriptions(resource.getDescription().getOrBuildTranslations());
        }
        if (resource.getNote() != null) {
            tdd.setNotes(resource.getNote().getOrBuildText());
            tdd.setTranslatedNotes(resource.getNote().getOrBuildTranslations());
        }

        if (resource.getType() != null) {
            tdd.setCmDocTypeId(resource.getType().getId());
        }

        if (StringUtils.isBlank(resource.getYearOfPublication())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(resource.getAddingDate());
            tdd.setDate(calendar.getTime());
            resource.setYearOfPublication(String.valueOf(calendar.get(Calendar.YEAR)));
        }
        tdd.setYearofPublication(resource.getYearOfPublication());

        if (ResourceType.LINK.equals(resource.getResourceType())) {
            tdd.setWebLink(resource.getWebLink());
        } else {
            tdd.setWebLink(null);
            tdd.setFileSize(formFile.getFileSize());
            tdd.setFormFile(formFile);
        }

        if (AmpClientModeHolder.isIatiImporterClient()) {
            tdd.setCreatorClient(ClientMode.IATI_IMPORTER.name());
        }

        return tdd;
    }

    @Override
    protected Object extractString(APIField apiField, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(apiField, parentObj, jsonValue);
    }

    private Object extractTranslationsOrSimpleValue(APIField apiField, Object parentObj, Object jsonValue) {
        TranslationType trnType = apiField.getTranslationType();
        String value = null;
        if (TranslationType.NONE == trnType) {
            return jsonValue;
        } else if (TranslationType.MULTILINGUAL == trnType) {
            MultilingualContent mc = null;
            if (trnSettings.isMultilingual()) {
                mc = new MultilingualContent((Map<String, String>) jsonValue);
            } else {
                mc = new MultilingualContent((String) jsonValue);
            }
            return mc;
        }

        return value;
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
            return ValidationErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.CREATOR_EMAIL);
        }

        Object team = newJson.get(ResourceEPConstants.TEAM);
        if (team == null || getLongOrNull(team) == null) {
            return ValidationErrors.FIELD_REQUIRED.withDetails(ResourceEPConstants.TEAM);
        }

        User creatorUser = creatorEmail != null ? UserUtils.getUserByEmailAddress(creatorEmail.toString()) : null;
        if (creatorUser == null) {
            return ValidationErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.CREATOR_EMAIL);
        }

        Long teamId = getLongOrNull(team);
        AmpTeam ampTeam = TeamUtil.getAmpTeam(teamId);

        if (ampTeam == null) {
            return ValidationErrors.FIELD_INVALID_VALUE.withDetails(ResourceEPConstants.TEAM);
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

    private ApiErrorMessage validateYearOfPublication(Map<String, Object> newJson) {
        String yearOfPublication = String.valueOf(newJson.get(ResourceEPConstants.YEAR_OF_PUBLICATION));
        if ("null".equals(yearOfPublication)) {
            yearOfPublication = String.valueOf(newJson.get(ResourceEPConstants.CLIENT_YEAR_OF_PUBLICATION));
        }
        if (yearOfPublication != null) {
            Long year = isLong(yearOfPublication) ? Long.valueOf(yearOfPublication) : null;
            int currentYear = LocalDate.now().getYear();
            if (year == null || year < MIN_SUPPORTED_YEAR || year > LocalDate.now().getYear()) {
                String errorMessage = String.format("%s %s-%s", TranslatorWorker.translateText("Allowed values are"),
                        MIN_SUPPORTED_YEAR, currentYear);
                return ResourceErrors.INVALID_YEAR_OF_PUBLICATION.withDetails(errorMessage);
            }
        }

        return null;
    }

    @Override
    public AmpResource getImportResult() {
        return resource;
    }

    @Override
    protected String getInvalidInputFieldName() {
        return ResourceEPConstants.RESOURCE;
    }

}
