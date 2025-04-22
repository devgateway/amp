package org.digijava.kernel.ampapi.endpoints.activity.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.util.SiteUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Viorel Chihai
 */
public class ValidLocaleValidatorTest {

    private static final String TRANSLATABLE_FIELD = "translatable_field";

    private static final Set<String> LOCALES = ImmutableSet.of("en", "fr");

    private ActivityImporter importer;

    private APIField translatableField;

    private MockedStatic<SiteUtils> mockedStatic;

    @BeforeEach
    public void setUp() throws Exception {
        mockedStatic = Mockito.mockStatic(SiteUtils.class);
        when(SiteUtils.getUserLanguagesCodes(Matchers.any())).thenReturn(LOCALES);

        translatableField = new APIField();
        translatableField.setFieldName(TRANSLATABLE_FIELD);
        translatableField.setImportable(true);
        translatableField.setApiType(new APIType(String.class, FieldType.STRING));

        importer = mock(ActivityImporter.class);
    }

    @AfterEach
    public void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
    }

    @Test
    public void testInvalidMultilingualInputLocales() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(TRANSLATABLE_FIELD, ImmutableMap.of(
                "en", "EN Text",
                "ro", "RO Text"
        ));

        mockTranslatableField("en", "fr", LOCALES, true);

        ValidLocaleValidator localeValidator = new ValidLocaleValidator();

        assertFalse(
                localeValidator.isValid(importer, newFieldParent, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputLocale() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(TRANSLATABLE_FIELD, ImmutableMap.of(
                "ro", "RO Text"
        ));

        mockTranslatableField("en", "fr", LOCALES, true);

        ValidLocaleValidator localeValidator = new ValidLocaleValidator();

        assertFalse(
                localeValidator.isValid(importer, newFieldParent, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidMultilingualInputLocales() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(TRANSLATABLE_FIELD, ImmutableMap.of(
                "en", "EN Text",
                "fr", "FR Text"
        ));

        mockTranslatableField("en", "fr", LOCALES, true);

        ValidLocaleValidator localeValidator = new ValidLocaleValidator();

        assertTrue(
                localeValidator.isValid(importer, newFieldParent, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidMultilingualInputLocale() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(TRANSLATABLE_FIELD, ImmutableMap.of(
                "fr", "FR Text"
        ));

        mockTranslatableField("en", "fr", LOCALES, true);

        ValidLocaleValidator localeValidator = new ValidLocaleValidator();

        assertTrue(
                localeValidator.isValid(importer, newFieldParent, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputLocaleKey() {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(TRANSLATABLE_FIELD, ImmutableMap.of(
                Collections.EMPTY_SET, "FR Text"
        ));

        mockTranslatableField("en", "fr", LOCALES, true);

        ValidLocaleValidator localeValidator = new ValidLocaleValidator();

        assertFalse(
                localeValidator.isValid(importer, newFieldParent, translatableField, TRANSLATABLE_FIELD));
    }

    private void mockTranslatableField(String currentLangCode, String defaultLangCode,
                                       Set<String> trnLocaleCodes, boolean multilingual) {
        translatableField.setTranslatable(multilingual);

        TranslationSettings trnSettings =
                new TranslationSettings(currentLangCode, defaultLangCode, trnLocaleCodes, multilingual);
        when(importer.getTrnSettings()).thenReturn(trnSettings);
    }

}
