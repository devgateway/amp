package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.GenericInterchangeableValueProvider;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.newServerErrorException;


public class GenericPossibleValuesProvider implements PossibleValuesProvider {
    private Class clazz;
    private Supplier<List> possibleValuesSupplier;
    private Function<Long, Boolean> isValidFunc;

    public GenericPossibleValuesProvider(Class<?> clazz, Supplier<List> possibleValuesSupplier) {
        this(clazz, possibleValuesSupplier, null);
    }

    public GenericPossibleValuesProvider(Class<?> clazz, Supplier<List> possibleValuesSupplier,
            Function<Long, Boolean> isValidFunc) {
        this.clazz = clazz;
        this.possibleValuesSupplier = possibleValuesSupplier;
        this.isValidFunc = isValidFunc;
    }

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        return getPossibleValuesGenericCase(translatorService);
    }

    private <T> List<PossibleValue> getPossibleValuesGenericCase(TranslatorService translatorService) {

        String idFieldName = getIdFieldName(clazz);

        InterchangeableValueProvider<T> valueProvider = getValueProvider(clazz);

        List<T> objectList = possibleValuesSupplier.get();
        return objectList.stream()
                .map(o -> getGenericPossibleValue(o, idFieldName, valueProvider, translatorService))
                .collect(toList());
    }

    private <T> String getIdFieldName(Class<T> clazz) {
        Field[] idFields = FieldUtils.getFieldsWithAnnotation(clazz, PossibleValueId.class);
        if (idFields.length == 0) {
            throw newServerErrorException("Could not find a field annotated with @PossibleValueId in " + clazz);
        }
        if (idFields.length > 1) {
            throw newServerErrorException("Found more than one field annotated with @PossibleValueId in " + clazz);
        }
        return idFields[0].getName();
    }

    private <T> InterchangeableValueProvider<T> getValueProvider(Class<T> clazz) {
        if (clazz.isAnnotationPresent(InterchangeableValue.class)) {
            return getInterchangeableValueProvider(clazz);
        }

        Field[] valueFields = FieldUtils.getFieldsWithAnnotation(clazz, PossibleValueValue.class);
        if (valueFields.length == 0) {
            throw newServerErrorException("Could not find a field annotated with @PossibleValueValue in " + clazz);
        }
        if (valueFields.length > 1) {
            throw newServerErrorException("Found more than one field annotated with @PossibleValueValue in " + clazz);
        }
        String valueFieldName = valueFields[0].getName();

        return new GenericInterchangeableValueProvider<>(clazz, valueFieldName);
    }

    private <T> PossibleValue getGenericPossibleValue(T object, String idProperty,
            InterchangeableValueProvider<T> valueProvider, TranslatorService translatorService) {
        try {
            Long id = (Long) PropertyUtils.getProperty(object, idProperty);
            String value = valueProvider.getValue(object);
            Map<String, String> translatedValue = ImmutableMap.of();
            if (valueProvider.isTranslatable()) {
                translatedValue = translatorService.translateLabel(value);
            }
            Object extraInfo = valueProvider.getExtraInfo(object);
            return new PossibleValue(id, value, translatedValue, extraInfo);
        } catch (ReflectiveOperationException e) {
            throw newServerErrorException("Failed to extract possible value object from " + object, e);
        }
    }

    private <T> InterchangeableValueProvider<T> getInterchangeableValueProvider(Class<T> clazz) {
        InterchangeableValue annotation = clazz.getAnnotation(InterchangeableValue.class);
        try {
            return annotation.value().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw newServerErrorException("Failed to instantiate value provider for " + clazz, e);
        }
    }

    @Override
    public boolean isAllowed(Long id) {
        if (isValidFunc != null) {
            return isValidFunc.apply(id);
        }
        return GenericPossibleValuesProvider.isAllowed(clazz, id);
    }

    public static boolean isAllowed(Class<?> clazz, Long id) {
        return PersistenceManager.getSession().get(clazz, id) != null;
    }

}
