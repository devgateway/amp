package org.digijava.kernel.validators.common;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.digijava.kernel.ampapi.endpoints.activity.TestFieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.kernel.validators.activity.ValidatorMatchers;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.util.Identifiable;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class RequiredValidatorTest {

    @TranslatableClass(displayName = "Foo")
    public static class Foo implements Identifiable {

        private Long id;

        @Interchangeable(fieldTitle = "title", fmPath = "title",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private String title;

        @Interchangeable(fieldTitle = "editor", fmPath = "editor",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        @VersionableFieldTextEditor
        private String editor;

        @Interchangeable(fieldTitle = "content_translation", fmPath = "contentTranslation",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        @TranslatableField
        private String contentTranslation;

        @Interchangeable(fieldTitle = "toggle", fmPath = "toggle",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Boolean toggle;

        @Interchangeable(fieldTitle = "number", fmPath = "number",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Integer number;

        @Interchangeable(fieldTitle = "bars", fmPath = "bars",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Set<Bar> bars = new HashSet<>();

        @Interchangeable(fieldTitle = "bar", fmPath = "bar",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private Bar bar;
    
        @Interchangeable(fieldTitle = "multilingual_translation", fmPath = "multilingualTranslation",
                interValidators = @InterchangeableValidator(RequiredValidator.class))
        private MultilingualContent multilingualTranslation;

        @Override
        public Object getIdentifier() {
            return id;
        }
    }

    public static class Bar {

        @InterchangeableId
        @Interchangeable(fieldTitle = "id")
        private Long id;
    }

    private static APIField titleField;
    private static APIField toggleField;
    private static APIField numberField;
    private static APIField barsField;
    private static APIField barField;
    private static APIField editorField;
    private static APIField multilingualEditorField;
    private static APIField ctField;
    private static APIField multilingualCtField;
    private static APIField mtField;
    private static APIField multilingualMtField;

    private static final ImmutableSet<String> ALL_FM_PATHS =
            ImmutableSet.of("title", "editor", "contentTranslation", "toggle", "number", "bars", "bar",
                    "multilingualTranslation");

    private static Set<String> getAllTestFmPathsExcept(String fmPath) {
        HashSet<String> fmPaths = new HashSet<>(ALL_FM_PATHS);
        fmPaths.remove(fmPath);
        return fmPaths;
    }

    @BeforeClass
    public static void setUp() {
        titleField = ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept("title"));
        toggleField = ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept("toggle"));
        numberField = ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept("number"));
        barsField = ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept("bars"));
        barField = ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept("bar"));
        editorField = getApiField(false, "editor");
        multilingualEditorField = getApiField(true, "editor");
        ctField = getApiField(false, "contentTranslation");
        multilingualCtField = getApiField(true, "contentTranslation");
        mtField = getApiField(false, "multilingualTranslation");
        multilingualMtField = getApiField(true, "multilingualTranslation");
    }

    private static APIField getApiField(boolean multilingual, String fmPath) {
        FieldInfoProvider fieldInfoProvider = new TestFieldInfoProvider(multilingual);
        return ValidatorUtil.getMetaData(Foo.class, getAllTestFmPathsExcept(fmPath), fieldInfoProvider);
    }

    @Test
    public void test_title_valid() {
        Foo foo = new Foo();
        foo.title = "Foo";

        Set<ConstraintViolation> violations = getConstraintViolations(titleField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_title_null() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(titleField, foo);

        assertThat(violations, contains(violation("title")));
    }

    @Test
    public void test_title_empty() {
        Foo foo = new Foo();
        foo.title = "";

        Set<ConstraintViolation> violations = getConstraintViolations(titleField, foo);

        assertThat(violations, contains(violation("title")));
    }

    @Test
    public void test_title_blank() {
        Foo foo = new Foo();
        foo.title = " ";

        Set<ConstraintViolation> violations = getConstraintViolations(titleField, foo);

        assertThat(violations, contains(violation("title")));
    }

    @Test
    public void test_toggle_null() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(toggleField, foo);

        assertThat(violations, contains(violation("toggle")));
    }

    @Test
    public void test_toggle_valid() {
        Foo foo = new Foo();
        foo.toggle = true;

        Set<ConstraintViolation> violations = getConstraintViolations(toggleField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_number_null() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(numberField, foo);

        assertThat(violations, contains(violation("number")));
    }

    @Test
    public void test_number_valid() {
        Foo foo = new Foo();
        foo.number = 43;

        Set<ConstraintViolation> violations = getConstraintViolations(numberField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_bars_empty() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(barsField, foo);

        assertThat(violations, contains(violation("bars")));
    }

    @Test
    public void test_bars_valid() {
        Foo foo = new Foo();
        foo.bars = ImmutableSet.of(new Bar());

        Set<ConstraintViolation> violations = getConstraintViolations(barsField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_bar_null() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(barField, foo);

        assertThat(violations, contains(violation("bar")));
    }

    @Test
    public void test_bar_valid() {
        Foo foo = new Foo();
        foo.bar = new Bar();

        Set<ConstraintViolation> violations = getConstraintViolations(barField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_ml_editor_nullKey() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_ml_editor_missingEditor() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_ml_editor_nullValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", null));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_ml_editor_emptyValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", ""));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_ml_editor_blankValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", " "));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_ml_editor_validValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", "Foo Editor"));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_ml_editor_wrongLanguage() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("fr", "Foo Editor"));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualEditorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_nullKey() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_nullValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", null));

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_missingEditor() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_emptyValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", ""));

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_blankValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("en", " "));

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo, editorStore);

        assertThat(violations, contains(violation("editor")));
    }

    @Test
    public void test_editor_validValue() {
        Foo foo = new Foo();
        foo.editor = "rvt-001";

        EditorStore editorStore = new EditorStore();
        editorStore.getValues().put("rvt-001", Collections.singletonMap("fr", "Foo Editor"));

        Set<ConstraintViolation> violations = getConstraintViolations(editorField, foo, editorStore);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_contentTranslation_null() {
        Foo foo = new Foo();

        Set<ConstraintViolation> violations = getConstraintViolations(ctField, foo);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_contentTranslation_empty() {
        Foo foo = new Foo();
        foo.contentTranslation = "";

        Set<ConstraintViolation> violations = getConstraintViolations(ctField, foo);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_contentTranslation_blank() {
        Foo foo = new Foo();
        foo.contentTranslation = " ";

        Set<ConstraintViolation> violations = getConstraintViolations(ctField, foo);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_contentTranslation_valid() {
        Foo foo = new Foo();
        foo.contentTranslation = "Foo CT";

        Set<ConstraintViolation> violations = getConstraintViolations(ctField, foo);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void test_ml_contentTranslation_missing() {
        Foo foo = new Foo();
        foo.id = 143L;

            Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_ml_contentTranslation_null() {
        Foo foo = new Foo();
        foo.id = 143L;

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(newContentTranslation("en", null));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo, contentTranslations);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_ml_contentTranslation_empty() {
        Foo foo = new Foo();
        foo.id = 143L;

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(newContentTranslation("en", ""));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo, contentTranslations);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_ml_contentTranslation_blank() {
        Foo foo = new Foo();
        foo.id = 143L;

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(newContentTranslation("en", " "));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo, contentTranslations);

        assertThat(violations, contains(violation("content_translation")));
    }

    @Test
    public void test_ml_contentTranslation_wrongLanguage() {
        Foo foo = new Foo();
        foo.id = 143L;

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(newContentTranslation("fr", "Foo CT"));

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo, contentTranslations);

        assertThat(violations, contains(violation("content_translation")));
    }

    private AmpContentTranslation newContentTranslation(String languageCode, String value) {
        AmpContentTranslation ct1 = new AmpContentTranslation();
        ct1.setTranslation(value);
        ct1.setLocale(languageCode);
        ct1.setObjectClass(Foo.class.getName());
        ct1.setFieldName("contentTranslation");
        ct1.setObjectId(143L);
        return ct1;
    }

    private Matcher<ConstraintViolation> violation(String path) {
        return ValidatorMatchers.violationFor(RequiredValidator.class, path, anything(),
                ValidationErrors.FIELD_REQUIRED);
    }

    @Test
    public void test_ml_contentTranslation_valid() {
        Foo foo = new Foo();
        foo.id = 143L;

        AmpContentTranslation ct1 = newContentTranslation("en", "Foo CT");

        List<AmpContentTranslation> contentTranslations = ImmutableList.of(ct1);

        Set<ConstraintViolation> violations = getConstraintViolations(multilingualCtField, foo, contentTranslations);

        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void test_multilingualTranslation_null() {
        Foo foo = new Foo();
        
        Set<ConstraintViolation> violations = getConstraintViolations(mtField, foo);
        
        assertThat(violations, contains(violation("multilingual_translation")));
    }
    
    @Test
    public void test_multilingualTranslation_empty() {
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent("");
        
        Set<ConstraintViolation> violations = getConstraintViolations(mtField, foo);
        
        assertThat(violations, contains(violation("multilingual_translation")));
    }
    
    @Test
    public void test_multilingualTranslation_blank() {
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent(" ");
        
        Set<ConstraintViolation> violations = getConstraintViolations(mtField, foo);
        
        assertThat(violations, contains(violation("multilingual_translation")));
    }
    
    @Test
    public void test_multilingualTranslation_valid() {
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent("Foo Mt");
        
        Set<ConstraintViolation> violations = getConstraintViolations(mtField, foo);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void test_ml_multilingualTranslation_blankValue() {
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent(" ", getMultilingualTranslationSettings());
    
        Set<ConstraintViolation> violations = getConstraintViolations(multilingualMtField, foo);
        
        assertThat(violations, contains(violation("multilingual_translation")));
    }
    
    @Test
    public void test_ml_multilingualTranslation_validValue() {
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent("title", getMultilingualTranslationSettings());
        
        Set<ConstraintViolation> violations = getConstraintViolations(multilingualMtField, foo);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void test_ml_multilingualTranslation_invalidValue_lang() {
        Map<String, String> translations = new HashMap<>();
        translations.put("fr", "text");
        
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent(translations, getMultilingualTranslationSettings());
        
        Set<ConstraintViolation> violations = getConstraintViolations(multilingualMtField, foo);
        
        assertThat(violations, contains(violation("multilingual_translation")));
    }
    
    @Test
    public void test_ml_multilingualTranslation_validValue_lang() {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", "text");
        
        Foo foo = new Foo();
        foo.multilingualTranslation = new MultilingualContent(translations, getMultilingualTranslationSettings());
        
        Set<ConstraintViolation> violations = getConstraintViolations(multilingualMtField, foo);
        
        assertThat(violations, emptyIterable());
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField objField, Object object) {
        return getConstraintViolations(objField, object, new EditorStore());
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField objField, Object object,
            EditorStore editorStore) {
        return getConstraintViolations(objField, object, editorStore, new ArrayList<>());
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField objField, Object object,
            List<AmpContentTranslation> contentTranslations) {
        return getConstraintViolations(objField, object, new EditorStore(), contentTranslations);
    }

    private Set<ConstraintViolation> getConstraintViolations(APIField objField, Object object,
            EditorStore editorStore,
            List<AmpContentTranslation> contentTranslations) {
        Validator validator = new Validator();
        return validator.validate(objField, object,
                ValidatorUtil.getDefaultTranslationContext(editorStore, contentTranslations));
    }
    
    private TranslationSettings getMultilingualTranslationSettings() {
        Set<String> languages = Stream.of("en", "fr").collect(Collectors.toSet());
        TranslationSettings settings = new TranslationSettings("en", "en", languages, true);
        return settings;
    }
}
