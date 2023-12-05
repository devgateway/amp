package org.digijava.kernel.ampapi.endpoints.activity.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.util.SiteUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Viorel Chihai
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SiteUtils.class })
public class ValidLocaleValidatorTest {
    
    private static final String TRANSLATABLE_FIELD = "translatable_field";
    
    private static final Set<String> LOCALES = ImmutableSet.of("en", "fr");
    
    private ActivityImporter importer;
    
    private APIField translatableField;
    
    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(SiteUtils.class);
        when(SiteUtils.getUserLanguagesCodes(Matchers.any())).thenReturn(LOCALES);
        
        translatableField = new APIField();
        translatableField.setFieldName(TRANSLATABLE_FIELD);
        translatableField.setImportable(true);
        translatableField.setApiType(new APIType(String.class, FieldType.STRING));
        
        importer = mock(ActivityImporter.class);
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
    
        assertFalse("Only allowed locales must be accepted",
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
        
        assertFalse("Only allowed locales must be accepted",
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
        
        assertTrue("Multilingual input must be valid",
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
        
        assertTrue("Multilingual input must be valid",
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
        
        assertFalse("Only allowed locales must be accepted",
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