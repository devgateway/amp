package org.digijava.kernel.ampapi.endpoints.resource;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.digijava.kernel.ampapi.endpoints.activity.AMPRequestRule;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityTranslationUtils;
import org.digijava.kernel.ampapi.endpoints.activity.NoTranslatedFieldReader;
import org.digijava.kernel.ampapi.endpoints.activity.TestFMService;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.ampapi.endpoints.util.JSONUtils;
import org.digijava.kernel.request.TLSUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Nadejda Mandrescu
 */
public class ResourceExporterTest {
    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    private Map<String, String> translations;
    private Map<String, String> allTranslations;

    private TranslationSettings multilingualTranslationSettings;
    private TranslationSettings nonMultilingualTranslationSettings;
    private APIField root;

    @Before
    public void setUp() {
        Set<String> trnCodes = ImmutableSet.of("en", "fr");
        multilingualTranslationSettings = new TranslationSettings("en", trnCodes, true);
        nonMultilingualTranslationSettings = new TranslationSettings("en", trnCodes, false);
        translations = ImmutableMap.of("en", "EN text");
        allTranslations = new LinkedHashMap<>();
        for (String locale: trnCodes) {
            allTranslations.put(locale, null);
        }

        TestTranslatorService translatorService = new TestTranslatorService();

        FieldsEnumerator enumerator = new FieldsEnumerator(
                new TestFieldInfoProvider(),
                new TestFMService(),
                translatorService,
                program -> false);

        ActivityTranslationUtils.setTranslatorService(translatorService);

        root = enumerator.getMetaModel(AmpResource.class);
    }

    @Test
    public void testNonMultilingualValidTitle() {
        boolean isMultilingual = false;
        mockMultilingual(isMultilingual);

        AmpResource resource = new AmpResource();
        resource.setTitle(MultilingualContent.build(isMultilingual, "Title", translations));

        Map<String, Object> json = testMultilingualField(resource, ResourceEPConstants.TITLE, isMultilingual);
        assertThat("Simple string must be reported when multilignual is off",
                json, hasEntry(ResourceEPConstants.TITLE, "Title"));
    }

    @Test
    public void testNonMultilingualNullTitle() {
        boolean isMultilingual = false;
        mockMultilingual(isMultilingual);

        AmpResource resource = new AmpResource();
        resource.setTitle(MultilingualContent.build(isMultilingual, null, translations));

        Map<String, Object> json = testMultilingualField(resource, ResourceEPConstants.TITLE, isMultilingual);
        assertThat("Null must be reported as null when multilignual is off",
                json, hasEntry(ResourceEPConstants.TITLE, null));
    }

    @Test
    public void testMultilingualValidTitle() {
        boolean isMultilingual = true;
        mockMultilingual(isMultilingual);

        AmpResource resource = new AmpResource();
        resource.setTitle(MultilingualContent.build(isMultilingual, "", translations));

        Map<String, String> expected = new LinkedHashMap<>(allTranslations);
        expected.putAll(translations);

        Map<String, Object> json = testMultilingualField(resource, ResourceEPConstants.TITLE, isMultilingual);

        assertThat("A translations map must provide existing and null trnalsations requested when multilignual is on",
                json, hasEntry(ResourceEPConstants.TITLE, expected));
    }

    @Test
    public void testMultilingualNullTitle() {
        boolean isMultilingual = true;
        mockMultilingual(isMultilingual);

        AmpResource resource = new AmpResource();
        resource.setTitle(MultilingualContent.build(isMultilingual, "Title", null));

        Map<String, Object> json = testMultilingualField(resource, ResourceEPConstants.TITLE, isMultilingual);
        assertThat("Null translations map must report null translations for requested locales when multilignual is on",
                json, hasEntry(ResourceEPConstants.TITLE, allTranslations));
    }

    private void mockMultilingual(boolean isMultilingual) {
        TranslationSettings ts = isMultilingual ? multilingualTranslationSettings: nonMultilingualTranslationSettings;

        TLSUtils.getRequest().setAttribute(EPConstants.TRANSLATIONS, ts);
        TranslationSettings.setDefaultOverride(ts);
    }

    private Map<String, Object> testMultilingualField(AmpResource resource, String fieldName, boolean isMultilingual) {
        APIField field = root.getField(fieldName);
        field.setTranslatable(isMultilingual);

        ResourceExporter exporter = new ResourceExporter(new NoTranslatedFieldReader(), root.getChildren());
        return JSONUtils.readValueFromJson(JSONUtils.serialize(exporter.export(resource)), Map.class);
    }

}
