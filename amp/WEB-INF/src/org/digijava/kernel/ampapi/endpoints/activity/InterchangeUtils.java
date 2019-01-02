package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.core.PathSegment;

import com.sun.jersey.spi.container.ContainerRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.resource.AmpResource;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnnualProjectBudget;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.ValidationStatus;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.exception.EditorException;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import static java.util.function.Function.identity;

import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Activity Import/Export Utility methods 
 * 
 */
public class InterchangeUtils {

    public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);
    private static Map<String, String> titleToUnderscoreMap = new HashMap<String, String>();
    
    /**
     * map from discriminated field name (i.e. "orgroles") to actual field title (i.e. "Donor Organization")
     */
    private static Map<String, List<String>> discriminatedFieldTitlesByFieldName = new HashMap<>();
    static {
        addUnderscoredTitlesToMap(AmpActivityFields.class);
        addUnderscoredTitlesToMap(AmpContact.class);
        addUnderscoredTitlesToMap(AmpResource.class);
    }

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>();

    private static TranslatorService translatorService = AMPTranslatorService.INSTANCE;

    public static Map<String, List<String>> getDiscriminatedFieldTitlesByFieldName() {
        return discriminatedFieldTitlesByFieldName;
    }

    public static void setTranslatorService(TranslatorService translatorService) {
        InterchangeUtils.translatorService = translatorService;
    }

    /**
     * Decides whether a class is enumerable (may be called in the Possible Values EP)
     *  
     * @param clazz
     * @return true if possible values are limited to a set for this field, false if otherwise
     */
    public static boolean isEnumerable(Class<?> clazz) {
        if (isSimpleType(clazz)) {
            return false;
        }
        Field[] fields = FieldUtils.getFieldsWithAnnotation(clazz, PossibleValueId.class);
        return fields.length > 0;
    }

    public static String getAmpIatiIdentifierFieldName() {
        String iatiIdGsField = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.IATI_IDENTIFIER_AMP_FIELD);
        String iatiIdAmpField = StringUtils.isEmpty(iatiIdGsField)
                ? ActivityEPConstants.IATI_IDENTIFIER_AMP_FIELD_DEFAULT_NAME : iatiIdGsField;

        return iatiIdAmpField;
    }

    public static Double getDoubleFromJsonNumber(Object obj) {
        if (!Number.class.isInstance(obj))
            return null;
        Number n = (Number) obj;
        return n.doubleValue();
    }
    
    /**
     * Maps every title in the AF fields model to an underscorified version
     * TODO: this section needs heavy rewrite -- we hope to remove the need to underscorify at all
     * and use the Field Label separately from the Field Title
     * @param clazz
     */
    private static void addUnderscoredTitlesToMap(Class<?> clazz) {
        for (Field field : getFieldsAnnotatedWith(clazz, Interchangeable.class, InterchangeableDiscriminator.class)) {
            Interchangeable ant = field.getAnnotation(Interchangeable.class);
            if (ant != null) {
                titleToUnderscoreMap.put(ant.fieldTitle(), underscorify(ant.fieldTitle()));
                if (!isSimpleType(getClassOfField(field)) && !ant.pickIdOnly()) {
                    addUnderscoredTitlesToMap(getClassOfField(field));
                }
            }

            InterchangeableDiscriminator antd = field.getAnnotation(InterchangeableDiscriminator.class);
            if (antd != null) {
                Interchangeable[] settings = antd.settings();
                for (Interchangeable ants : settings) {
                    titleToUnderscoreMap.put(ants.fieldTitle(), underscorify(ants.fieldTitle()));
                    discriminatedFieldTitlesByFieldName
                            .computeIfAbsent(field.getName(), z -> new ArrayList<>())
                            .add(underscorify(ants.fieldTitle()));
                }
                if (!isSimpleType(getClassOfField(field))) {
                    addUnderscoredTitlesToMap(getClassOfField(field));
                }
            }
        }
    }

    /**
     * Gets the value at the specified path from the JSON description of the activity. 
     * 
     * @param activity a JsonBean description of the activity
     * @param path path to the field
     * @return null if the path abruptly stops before reaching the end, or the value itself,
     * if the end of the path is reached
     */
    public static Object getFieldValuesFromJsonActivity(JsonBean activity, String path) {
        String fieldPath = path;
        
        JsonBean currentBranch = activity;
        while (fieldPath.contains("~")) {
            String pathSegment = fieldPath.substring(0, fieldPath.indexOf('~'));
            Object obj = currentBranch.get(pathSegment);
            if (obj != null && JsonBean.class.isAssignableFrom(obj.getClass())) {
                currentBranch = (JsonBean) obj;
            } else {
                return null;
            }
            fieldPath = path.substring(fieldPath.indexOf('~') + 1);
            }
        //path is complete, object is set to proper value
        return currentBranch.get(fieldPath);
    }
    
    /**
     * transforms a Map<String,String> to a JsonBean with equal structure
     * 
     * @param map the map to be transformed
     * @return a JsonBean of the structure {"\<code1\>":"\<translation1\>", "\<code2\>":"\<translation2\>", ...}
     */
    public static JsonBean mapToBean(Map<String, String> map) {
        if (map.isEmpty())
            return null;
        JsonBean bean = new JsonBean();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            bean.set(entry.getKey(), entry.getValue());
        }
        return bean;
    }
    
    /**
     * checks whether a Field is assignable from a Collection
     * 
     * @param field a Field
     * @return true/false
     */
    public static boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    /**
     * returns the generic class defined within a Collection, e.g.
     * Collection<Class_returned>
     * 
     * @param field
     * @return the generic class
     */
    public static Class<?> getGenericClass(Field field) {
        if (!isCollection(field))
            throw new RuntimeException("Not a collection: " + field.toString());
        ParameterizedType collectionType = null;
        collectionType = (ParameterizedType) field.getGenericType();
        Type[] genericTypes = collectionType.getActualTypeArguments();
        if (genericTypes.length > 1) {
            // dealing with a map or anything else having > 1 parameterized
            // types
            // throw an exception, this is a very unexpected case
            throw new RuntimeException("Only collections with one generic type expected!");
        }
        if (genericTypes.length == 0) {
            // return null;
            // dealing with a raw type
            // throw an exception, it won't be complete with no parameterization
            throw new RuntimeException("Raw types are not allowed!");
        }
        return ((Class<?>) genericTypes[0]);
    }

    /**
     * converts the uppercase letters of a string to underscore + lowercase (except for first one)
     * 
     * @param input String to be converted
     * @return converted string
     */
    public static String underscorify(String input) {
        if (titleToUnderscoreMap.containsKey(input))
            return titleToUnderscoreMap.get(input);
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ')
                bld.append('_');
            else
                bld.append(Character.toLowerCase(input.charAt(i)));
        }
        return bld.toString();
    }

    /**
     * Obtains the possible values provider class of the field, if it has one
     * @param field
     * @return null if the field doesn't have a provider class attached, otherwise -- the class
     */
    public static Class<? extends PossibleValuesProvider> getPossibleValuesProvider(Field field) {
        PossibleValues ant = field.getAnnotation(PossibleValues.class);
        if (ant != null) {
            return ant.value();
        }
        return null;
    }

    public static Class<?> getPossibleValuesClass(Field field) {
        Class<? extends PossibleValuesProvider> provider = getPossibleValuesProvider(field);
        if (provider == null) {
            return null;
        }
        PossibleValuesEntity possibleValuesEntity = provider.getAnnotation(PossibleValuesEntity.class);
        if (possibleValuesEntity == null) {
            return null;
        }
        return possibleValuesEntity.value();
    }

    public static boolean isCompositeField(Field field) {
        return field.getAnnotation(InterchangeableDiscriminator.class) != null;
    }

    public static boolean isVersionableTextField(Field field) {
        return field.getAnnotation(VersionableFieldTextEditor.class) != null;
    }
    
    public static boolean isResourceTextField(Field field) {
        return field.getAnnotation(ResourceTextField.class) != null;
    }

    public static JsonBean getActivityByAmpId(String ampId) {
        Long activityId = ActivityUtil.findActivityIdByAmpId(ampId);
        return getActivity(activityId);
    }

    /**
     * Activity Export as JSON
     * 
     * @param projectId is amp_activity_id
     * @return
     */
    public static JsonBean getActivity(Long projectId) {
        return getActivity(projectId, null);
    }

    public static AmpActivityVersion loadActivity(Long projectId) {
        AmpActivityVersion activity = null;
        try {
            activity = ActivityUtil.loadActivity(projectId);
            if (activity == null) {
                //so far project will never be null since an exception will be thrown
                //I leave the code prepared to throw the appropriate response code
                ApiErrorResponse.reportResourceNotFound(ActivityErrors.ACTIVITY_NOT_FOUND);
            }
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
        return activity;
    }
    /**
     * Activity Export as JSON 
     * 
     * @param projectId is amp_activity_id
     * @param filter is the JSON with a list of fields
     * @return
     */
    public static JsonBean getActivity(Long projectId, JsonBean filter) {
        return getActivity(loadActivity(projectId), filter);
    }
    
    /**
     * Activity Export as JSON 
     * 
     * @param activity is the activity
     * @param filter is the JSON with a list of fields
     * @return Json Activity
     */
    public static JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) {
        try {
            ActivityExporter exporter = new ActivityExporter(filter);
        
            return exporter.export(activity);
        } catch (Exception e) {
            LOGGER.error("Error in loading activity. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the translation values of the field.
     * @param field
     * @param clazz class used for retrieving translation
     * @param fieldValue 
     * @param parentObject is the parent that contains the object in order to retrieve translations throu parent object id
     * @return object with the translated values
     * @throws NoSuchFieldException 
     */
    public static Object getTranslationValues(Field field, Class<?> clazz, Object fieldValue, Object parentObject) 
            throws IllegalAccessException, EditorException, NoSuchFieldException {
        
        TranslationSettings translationSettings = TranslationSettings.getCurrent();
        
        // check if this is translatable field
        boolean isTranslatable = translationSettings.isTranslatable(field);
        boolean isEditor = InterchangeUtils.isVersionableTextField(field);
        boolean isResource = InterchangeUtils.isResourceTextField(field);
        
        // provide map for translatable fields
        if (isTranslatable) {
            String fieldText = (String) fieldValue;
            if (isEditor) {
                Map<String, Object> fieldTrnValues = new HashMap<String, Object>();
                for (String translation : translationSettings.getTrnLocaleCodes()) {
                    // AMP-20884: no html tags cleanup so far
                    //String translatedText = DgUtil.cleanHtmlTags(DbUtil.getEditorBodyEmptyInclude(SiteUtils.getGlobalSite(), fieldText, translation));
                    String translatedText = translatorService.getEditorBodyEmptyInclude(TLSUtils.getSite(),
                            fieldText, translation);
                    fieldTrnValues.put(translation, getJsonStringValue(translatedText));
                }
                return fieldTrnValues;
            } else if (isResource) {
                return loadTranslationsForResourceField(field, parentObject, translationSettings);
            } else {
                return loadTranslationsForField(clazz, field.getName(), fieldText, parentObject, 
                        translationSettings.getTrnLocaleCodes());
            }
        }
        
        // for reach text editors
        if (isEditor) {
            // AMP-20884: no html tags cleanup so far
            return translatorService.getEditorBodyEmptyInclude(TLSUtils.getSite(), (String) fieldValue,
                    translationSettings.getDefaultLangCode());
        }
        
        // other translatable options
        if (fieldValue instanceof String) {
            boolean toTranslate = clazz.equals(AmpCategoryValue.class) && field.getName().equals("value");
            
            // now we check if is only a CategoryValue field and the field name is value
            String translatedText = toTranslate ? translatorService.translateText((String) fieldValue)
                    : (String) fieldValue;
            return getJsonStringValue(translatedText);
        } else if (fieldValue instanceof Date) {
            return InterchangeUtils.formatISO8601Date((Date) fieldValue);
        }
        
        return fieldValue;
    }

    public static Object getObjectById(Class<?> entityClass, Long id) {
        // TODO: cache it
        return PersistenceManager.getSession().get(entityClass.getName(), id);
    }
    
    /**
     * 
     * @param value 
     * @return value if is not blank or null (in order to have for empty strings null values in result JSON) 
     */
    private static String getJsonStringValue(String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    /**
     * Gets a date formatted in ISO 8601 format. If the date is null, returns null.
     * 
     * @param date the date to be formatted
     * @return String, date in ISO 8601 format
     */
    public static String formatISO8601Date(Date date) {
        return date == null ? null : getDateFormatter().format(date);
    }
    
    /**
     * Rebuilds the date from the source
     * @param date the source
     * @return Date object
     */
    public static Date parseISO8601Date(String date) {
        try {
            return date == null ? null : getDateFormatter().parse(date);
        } catch (ParseException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
    }   

    /**
     * Gets the ID of an enumerable object (used in Possible Values EP)
     * @param obj
     * @return ID if it's identifiable, null otherwise
     */
    public static Long getId(Object obj) {
        //TODO: throw an exception here? it's currently swallowed
        if (obj == null || !Identifiable.class.isAssignableFrom(obj.getClass())) {
            return null;
        }
        
        Identifiable identifiableObject = (Identifiable) obj;
        Long id = (Long) identifiableObject.getIdentifier(); 
        return id;
    }

    public static boolean isSimpleType(Class<?> clazz) {
        return InterchangeableClassMapper.containsSimpleClass(clazz);
    }

    public static Class<?> getClassOfField(Field field) {
        if (!isCollection(field))
            return field.getType();
        else
            return getGenericClass(field);
    }

    /**
     * Imports or Updates an activity
     * @param newJson new activity configuration
     * @param update flags whether this is an import or an update request
     * @param endpointContextPath full API method path where this method has been called
     *
     * @return latest project overview or an error if invalid configuration is received 
     */
    public static JsonBean importActivity(JsonBean newJson, boolean update, String endpointContextPath) {
        List<APIField> activityFields = AmpFieldsEnumerator.getPrivateEnumerator().getActivityFields();
        ActivityImporter importer = new ActivityImporter(activityFields);
        List<ApiErrorMessage> errors = importer.importOrUpdate(newJson, update, endpointContextPath);
        
        return getImportResult(importer.getNewActivity(), importer.getNewJson(), errors);
    }
    
    protected static JsonBean getImportResult(AmpActivityVersion newActivity, JsonBean newJson, 
            List<ApiErrorMessage> errors) {
        JsonBean result = null;
        if (errors.size() == 0 && newActivity == null) {
            // no new activity, but also errors are missing -> unknown error
            result = ApiError.toError(ApiError.UNKOWN_ERROR); 
        } else if (errors.size() > 0) {
            result = ApiError.toError(errors);
            result.set(ActivityEPConstants.ACTIVITY, newJson);
        } else {
            List<JsonBean> activities = null;
            if (newActivity != null && newActivity.getAmpActivityId() != null) {
                // editable, viewable, since was just created/updated
                activities = Arrays.asList(ProjectList.getActivityInProjectListFormat(newActivity, true, true));
            }
            if (activities == null || activities.size() == 0) {
                result = ApiError.toError(ApiError.UNKOWN_ERROR);
                result.set(ActivityEPConstants.ACTIVITY, newJson);
            } else {
                result = activities.get(0);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static boolean validateFilterActivityFields(JsonBean filterJson, JsonBean result, List<APIField> fields) {
        List<String> filteredItems = new ArrayList<String>();
        
        if (filterJson != null) {
            try {
                filteredItems = (List<String>) filterJson.get(ActivityEPConstants.FILTER_FIELDS);
                if (filteredItems == null) {
                    addFilterValidationErrorToResult(result);
                    
                    return false;
                }
            } catch (Exception e) {
                LOGGER.warn("Error in validating fields of the filter attribute. " + e.getMessage());
                addFilterValidationErrorToResult(result);;
                
                return false;
            }
        }

        try {
            for (String field : filteredItems) {
                PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(field, fields);
            }
        } catch (ApiRuntimeException e) {
            result.set(ApiError.JSON_ERROR_CODE, e.getUnwrappedError());
            return false;
        }
        
        return true;
    }
    
    private static void addFilterValidationErrorToResult(JsonBean result) {
        String message = "Invalid filter. The usage should be {\"" + ActivityEPConstants.FILTER_FIELDS + "\" : [\"field1\", \"field2\", ..., \"fieldn\"]}";
        
        JsonBean errorBean = ApiError.toError(message);
        result.set(ApiError.JSON_ERROR_CODE, errorBean.get(ApiError.JSON_ERROR_CODE));
    }

    /**
     * @param teamMember    team member
     * @param activityId    activity id
     * @return true if team member can edit the activity
     */
    public static boolean isEditableActivity(TeamMember teamMember, Long activityId) {
        // we reuse the same approach as the one done by Project List EP
        return activityId != null && ActivityUtil.getEditableActivityIdsNoSession(teamMember).contains(activityId);
    }

    /**
     * @return true if add activity is allowed
     */
    public static boolean addActivityAllowed(TeamMember tm) {
        return tm != null && Boolean.TRUE.equals(tm.getAddActivity()) &&
                (FeaturesUtil.isVisibleField("Add Activity Button") || FeaturesUtil.isVisibleField("Add SSC Button"));
    }
    
    /**
     * @param containerReq
     * @return true if request is valid to view an activity
     */
    public static boolean isViewableActivity(ContainerRequest containerReq) {
        Long id = getRequestId(containerReq);
        // we reuse the same approach as the one done by Project List EP
        // however there are some known issues: AMP-20496
        return id != null && ProjectList.getViewableActivityIds(TeamUtil.getCurrentMember()).contains(id);
    }
    
    private static Long getRequestId(ContainerRequest containerReq) {
        List<PathSegment> paths = containerReq.getPathSegments();
        Long id = null;
        if (paths != null && paths.size() > 0) {
            PathSegment segment = paths.get(paths.size() - 1);
            if (StringUtils.isNumeric(segment.getPath())) {
                id = Long.valueOf(segment.getPath());
            }
        }
        return id;
    }

    private static Object loadTranslationsForField(Class<?> clazz, String propertyName, String fieldValue, 
            Object parentObject, Set<String> languages) {
        
        Map<String, String> translations = new LinkedHashMap<>();
        TranslationSettings translationSettings = TranslationSettings.getDefault();
        String defLangCode = translationSettings.getDefaultLangCode();
        
        for (String l : languages) {
            translations.put(l, null);
        }
        Long id = parentObject instanceof Long ? (Long) parentObject : InterchangeUtils.getId(parentObject);
        
        if (id == null)
            return translations; 
        
        List<AmpContentTranslation> trns = translatorService.loadFieldTranslations(clazz.getName(), id, propertyName);
        for (AmpContentTranslation trn : trns) {
            if (languages.contains(trn.getLocale())) {
                translations.put(trn.getLocale(), trn.getTranslation());
            }
        }

        translations.putIfAbsent(defLangCode, fieldValue);
        
        return translations;
    }
    
    /**
     * @param field
     * @param parentObject
     * @param translationSettings
     * @return translations
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Map<String, Object> loadTranslationsForResourceField(Field field, Object parentObject,
            TranslationSettings translationSettings) throws NoSuchFieldException, IllegalAccessException {
        
        Map<String, Object> fieldTrnValues = new HashMap<String, Object>();
        AmpResource resource = (AmpResource) parentObject;
        ResourceTextField resourceAnnotation = field.getAnnotation(ResourceTextField.class);
        Field translationsField = resource.getClass().getDeclaredField(resourceAnnotation.translationsField());
        translationsField.setAccessible(true);
        
        Map<String, String> resourceTranslations = (Map<String, String>) translationsField.get(resource);
        for (String translation : translationSettings.getTrnLocaleCodes()) {
            fieldTrnValues.put(translation, resourceTranslations.get(translation));
        }
        
        return fieldTrnValues;
    }
    
    protected static SimpleDateFormat getDateFormatter() {
        if (DATE_FORMATTER.get() == null) {
            SimpleDateFormat format = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            DATE_FORMATTER.set(format);
        }
        return DATE_FORMATTER.get();
    }
    
    /**
     * @param apb AmpAnnualProject budget having amount, currency and date
     * @parm toCurrCode target currency code to which apb amount would be calculated
     * @return double the amount in toCurrCode
     */
    public static Double doPPCCalculations(AmpAnnualProjectBudget apb, String toCurrCode) {
        DecimalWraper calculatedAmount = new DecimalWraper();
        
        if (apb.getAmpCurrencyId() != null && apb.getYear() != null && apb.getAmount() != null && toCurrCode != null) {
            String frmCurrCode = apb.getAmpCurrencyId().getCurrencyCode();
            java.sql.Date dt = new java.sql.Date(apb.getYear().getTime());
            Double amount = apb.getAmount();
        
            double frmExRt = Util.getExchange(frmCurrCode, dt);
            double toExRt = frmCurrCode.equalsIgnoreCase(toCurrCode) ? frmExRt : Util.getExchange(toCurrCode, dt);
    
            calculatedAmount = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
        } else {
            LOGGER.error("Some info is missed in PPC Calculations");
        } 
        
        return calculatedAmount.doubleValue();
    }

    /**
     * Determine if this is an AmpActivityVersion field reference
     */
    public static boolean isAmpActivityVersion(Class<?> clazz) {
        return AmpActivityVersion.class.isAssignableFrom(clazz);
    }

    /**
     * This is a special adjusted Session with FlusMode = Commit so that Hiberante doesn't try to commit intermediate 
     * changes while we still query some information
     * TODO: AMP-20869: we'll need to give it a more thought during refactoring if either rewrite related queries as JDBC queries
     * or investigate more for 
     * 
     * @return Session with no AutoFlush mode
     */
    public static Session getSessionWithPendingChanges() {
        Session session = PersistenceManager.getSession();
        session.setFlushMode(FlushMode.COMMIT);
        return session;
    }
    
    public static ActivityInformation getActivityInformation(Long projectId) {
        AmpActivityVersion project = loadActivity(projectId);

        ActivityInformation activityInformation = new ActivityInformation(projectId);
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        activityInformation.setActivityTeam(project.getTeam());
        if (tm != null) {
            activityInformation.setEdit(isEditableActivity(tm, projectId));
            if (activityInformation.isEdit()) {
                activityInformation.setValidate(ActivityUtil.canValidateAcitivty(project, tm));
            }
            activityInformation.setValidationStatus(ActivityUtil.getValidationStatus(project, tm));
            if (activityInformation.getValidationStatus() == ValidationStatus.AUTOMATIC_VALIDATION) {
                activityInformation.setDaysForAutomaticValidation(ActivityUtil.daysToValidation(project));
            }

            AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());

            boolean isCurrentWorkspaceManager = ampCurrentMember.getAmpMemberRole().getTeamHead();
            boolean isPartOfMamanagetmentWorkspace = ampCurrentMember.getAmpTeam().getAccessType()
                    .equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);

            activityInformation.setUpdateCurrentVersion(isCurrentWorkspaceManager && !isPartOfMamanagetmentWorkspace);
            activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));
        } else {
            // if not logged in but the show version history in public preview is on, then we should show
            // version history information
            if (FeaturesUtil.isVisibleFeature("Version History")) {
                activityInformation.setVersionHistory(ActivityUtil.getActivityHistories(projectId));
                activityInformation.setUpdateCurrentVersion(false);
            }
        }

        activityInformation.setAmpActiviylastVersionId(ActivityVersionUtil.getLastVersionForVersion(projectId));

        return activityInformation;
    }

    public static boolean canViewActivityIfCreatedInPrivateWs(ContainerRequest containerReq) {
        Long id = getRequestId(containerReq);
        AmpActivityVersion project = InterchangeUtils.loadActivity(id);
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        return !(project.getTeam().getIsolated() && (tm == null || !tm.getTeamId().equals(project.getTeam().
                getAmpTeamId())));
    }

    /**
     * Get values for requested ids of fields
     *
     * @param fieldIds
     * @param apiFields
     * @return
     */
    public static Map<String, List<FieldIdValue>> getIdValues(Map<String, List<Long>> fieldIds,
            List<APIField> apiFields) {
        Map<String, List<FieldIdValue>> response = new HashMap<>();

        if (fieldIds != null) {
            for (Entry<String, List<Long>> field : fieldIds.entrySet()) {
                String fieldName = field.getKey();
                List<Long> ids = field.getValue();

                List<PossibleValue> allValues = possibleValuesFor(fieldName, apiFields).stream()
                        .map(PossibleValue::flattenPossibleValues)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

                Map<Object, PossibleValue> allValuesMap = allValues.stream()
                        .collect(Collectors.toMap(PossibleValue::getId, identity()));

                List<FieldIdValue> idValues = ids.stream()
                        .map(id -> getIdValue(id, allValuesMap))
                        .collect(Collectors.toList());

                response.put(fieldName, idValues);
            }
        }
        return response;
    }

    private static FieldIdValue getIdValue(Long id, Map<Object, PossibleValue> allValuesMap) {
        if (allValuesMap.containsKey(id)) {
            PossibleValue pv = allValuesMap.get(id);
            return new FieldIdValue((Long) pv.getId(), pv.getValue(), pv.getTranslatedValues(),
                    getAncestorValues(allValuesMap, pv.getId(), new ArrayList<>()));
        }

        return new FieldIdValue(id);
    }

    private static List<String> getAncestorValues(Map<Object, PossibleValue> allValuesMap, Object id,
            List<String> values) {
        PossibleValue obj = allValuesMap.get(id);
        List<String> ancestorValues = new ArrayList<>(values);
        if (obj.getExtraInfo() instanceof ParentExtraInfo) {
            ParentExtraInfo parentExtraInfo = (ParentExtraInfo) obj.getExtraInfo();
            if (parentExtraInfo.getParentId() != null) {
                ancestorValues.addAll(getAncestorValues(allValuesMap, parentExtraInfo.getParentId(), ancestorValues));
            }
            ancestorValues.add(obj.getValue());
            return ancestorValues;
        }

        return null;
    }

    public static List<PossibleValue> possibleValuesFor(String fieldName, List<APIField> apiFields) {
        return PossibleValuesEnumerator.INSTANCE.getPossibleValuesForField(fieldName, apiFields);
    }

    public static List<Field> getFieldsAnnotatedWith(Class<?> cls, Class<? extends Annotation> annotationCls1,
            Class<? extends Annotation> annotationCls2) {
        final List<Field> allFields = FieldUtils.getAllFieldsList(cls);
        final List<Field> annotatedFields = new ArrayList<Field>();
        for (final Field field : allFields) {
            if (field.getAnnotation(annotationCls1) != null || field.getAnnotation(annotationCls2) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }
}
