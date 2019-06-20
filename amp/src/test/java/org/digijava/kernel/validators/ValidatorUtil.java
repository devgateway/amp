package org.digijava.kernel.validators;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.digijava.kernel.ampapi.endpoints.activity.TestFMService;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.TranslationContext;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpContentTranslation;

/**
 * @author Octavian Ciubotaru
 */
public class ValidatorUtil {

    /**
     * Metadata for activity. All FM paths are enabled.
     */
    public static APIField getMetaData() {
        return getMetaData(ImmutableSet.of());
    }

    /**
     * Metadata for activity with some FM paths disabled.
     */
    public static APIField getMetaData(Set<String> disabledFmPaths) {
        return getMetaData(AmpActivityFields.class, disabledFmPaths);
    }

    /**
     * Metadata for a type.
     */
    public static APIField getMetaData(Class<?> clazz) {
        return getMetaData(clazz, ImmutableSet.of());
    }

    /**
     * Metadata for a type with some FM paths disabled.
     */
    public static APIField getMetaData(Class<?> clazz, Set<String> disabledFmPaths) {
        return getMetaData(clazz, disabledFmPaths, new TestFieldInfoProvider());
    }

    public static APIField getMetaData(Class<?> clazz, Set<String> disabledFmPaths,
            FieldInfoProvider fieldInfoProvider) {
        TestFMService fmService = new TestFMService(ImmutableSet.of(), disabledFmPaths);
        TestTranslatorService translatorService = new TestTranslatorService();

        FieldsEnumerator fieldsEnumerator = new FieldsEnumerator(fieldInfoProvider, fmService, translatorService,
                name -> true);
        return fieldsEnumerator.getMetaModel(clazz);
    }

    /**
     * Default translation context for tests. Current language is 'en'. Default language is 'fr'.
     */
    public static TranslationContext getDefaultTranslationContext() {
        return getDefaultTranslationContext(new EditorStore(), ImmutableList.of());
    }

    /**
     * Default translation context for tests. Current language is 'en'. Default language is 'fr'.
     * Editor or content translation values can be overridden here and will be used in validation.
     */
    public static TranslationContext getDefaultTranslationContext(
            EditorStore editorStore,
            List<AmpContentTranslation> contentTranslations) {

        TranslationContext.EditorLoader editorLoader = k -> ImmutableList.of();

        TranslationContext.ContentTranslationLoader ctLoader = (o, i, f) -> ImmutableList.of();

        return new TranslationContext("en", "fr", editorStore, contentTranslations, editorLoader, ctLoader);
    }

    /**
     * Filter violations and leave only the ones matching a specific validator class.
     *
     * Useful when testing a validator on an object annotated with other validators.
     */
    public static Set<ConstraintViolation> filter(Set<ConstraintViolation> violations, Class<?> validatorClass) {
        return violations.stream()
                .filter(v -> v.getConstraintDescriptor().getConstraintValidatorClass().equals(validatorClass))
                .collect(toSet());
    }
}
