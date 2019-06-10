package org.digijava.kernel.validators;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.TestFMService;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * @author Octavian Ciubotaru
 */
public class ValidatorUtil {

    public static APIField getMetaData() {
        return getMetaData(ImmutableSet.of());
    }

    public static APIField getMetaData(Set<String> disabledFmPaths) {
        return getMetaData(AmpActivityFields.class, disabledFmPaths);
    }

    public static APIField getMetaData(Class<?> clazz) {
        return getMetaData(clazz, ImmutableSet.of());
    }

    public static APIField getMetaData(Class<?> clazz, Set<String> disabledFmPaths) {
        TestFMService fmService = new TestFMService(disabledFmPaths);
        TestTranslatorService translatorService = new TestTranslatorService();
        TestFieldInfoProvider provider = new TestFieldInfoProvider();

        FieldsEnumerator fieldsEnumerator = new FieldsEnumerator(provider, fmService, translatorService, name -> true);
        return fieldsEnumerator.getMetaModel(clazz);
    }
}
