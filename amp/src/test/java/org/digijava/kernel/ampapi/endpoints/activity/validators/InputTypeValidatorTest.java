package org.digijava.kernel.ampapi.endpoints.activity.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.util.SiteUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.AfterEach;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nadejda Mandrescu
 */
public class InputTypeValidatorTest {

    private static final String TRANSLATABLE_FIELD = "translatable_field";
    private static final Set<String> LOCALES = ImmutableSet.of("en", "fr");
    private static final Map<String, String> MULTILINGUAL_INPUT = ImmutableMap.of(
            "en", "EN Text",
            "fr", "FR Text"
    );

    private ActivityImporter importer;
    private APIField translatableField;
    private MockedStatic<SiteUtils> siteUtilsMockedStatic;

    @BeforeEach
    public void setUp() throws Exception {
        siteUtilsMockedStatic = Mockito.mockStatic(SiteUtils.class);
        when(SiteUtils.getUserLanguagesCodes(Mockito.any())).thenReturn(LOCALES);
        translatableField = new APIField();

        translatableField.setFieldName(TRANSLATABLE_FIELD);
        translatableField.setImportable(true);
        translatableField.setApiType(new APIType(String.class, FieldType.STRING));

        importer = mock(ActivityImporter.class);
    }

    @AfterEach
    public void tearDown() {
        if (siteUtilsMockedStatic != null) {
            siteUtilsMockedStatic.close();
        }
    }

    @Test
    public void testValidMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, MULTILINGUAL_INPUT);

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Multilingual input must be valid");
    }

    @Test
    public void testValidMultilingualInputAsNull() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, null);

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Null must be a valid multilingual input");
    }

    @Test
    public void testValidMultilingualInputAsEmpty() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_MAP);

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Empty map must be a valid multilingual input");
    }

    @Test
    public void testInvalidMultilingualInputAsString() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, "anything");

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "String must not be allowed as multingual input");
    }

    @Test
    public void testInvalidMultilingualInputAsObject() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_SET);

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "Other objects must not be allowed as multingual input");
    }

    @Test
    public void testInvalidMultilingualInputLocales() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of("ro", "RO text"));

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Only allowed locales must be accepted");
    }

    @Test
    public void testInvalidMultilingualInputLocaleType() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of(Collections.EMPTY_SET, "RO text"));

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "Locales used by multilingual input must be of string type");
    }

    @Test
    public void testInvalidMultilingualInputTranslationType() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of("en", Collections.EMPTY_SET));

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "Translated text used by multilingual input must be of string type");
    }

    @Test
    public void testValidNonMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, "simple text");

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Non-multilingual input must be valid");
    }

    @Test
    public void testValidNonMultilingualInputAsNull() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, null);

        Assertions.assertTrue(isValid(root, translatableField, TRANSLATABLE_FIELD), "Non-multilingual null input must be valid");
    }

    @Test
    public void testInvalidNonMultilingualInputAsMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, MULTILINGUAL_INPUT);

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "Multilingual input disallowed when multingual is off");
    }

    @Test
    public void testInvalidNonMultilingualInputAsObject() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_SET);

        Assertions.assertFalse(isValid(root, translatableField, TRANSLATABLE_FIELD), "Only string type allowed for translatable field when multilingual is off");
    }

    private void mockTranslatableField(String currentLangCode, String defaultLangCode,
                                       Set<String> trnLocaleCodes, boolean multilingual) {
        translatableField.setTranslatable(multilingual);

        TranslationSettings trnSettings =
                new TranslationSettings(currentLangCode, defaultLangCode, trnLocaleCodes, multilingual);
        when(importer.getTrnSettings()).thenReturn(trnSettings);
    }

    private boolean isValid(Map<String, Object> root, APIField fieldDesc, String fieldPath) {
        InputTypeValidator validator = new InputTypeValidator();
        return validator.isValid(importer, root, fieldDesc, fieldPath);
    }

}
