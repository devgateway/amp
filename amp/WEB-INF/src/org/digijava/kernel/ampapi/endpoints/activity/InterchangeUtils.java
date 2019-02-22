package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.function.Function.identity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.InterchangeableClassMapper;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;

/**
 * Activity Import/Export Utility methods 
 * 
 */
public class InterchangeUtils {

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

    public static Object getObjectById(Class<?> entityClass, Object id) {
        if (Collection.class.isAssignableFrom(entityClass)) {
            throw new RuntimeException("Can't handle a collection of ID-linked objects yet!");
        }
        if (ApprovalStatus.class.isAssignableFrom(entityClass)) {
            return ApprovalStatus.fromId((Integer) id);
        } else if (ResourceType.class.isAssignableFrom(entityClass)) {
            return ResourceType.fromId((Integer) id);
        } else if (InterchangeUtils.isSimpleType(entityClass)) {
            return ConvertUtils.convert(id, entityClass);
        } else {
            return PersistenceManager.getSession().get(entityClass.getName(), Long.valueOf(id.toString()));
        }
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
