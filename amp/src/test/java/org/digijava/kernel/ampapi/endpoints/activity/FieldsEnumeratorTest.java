package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NOT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.ALWAYS;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.NONE;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;
import static org.digijava.kernel.ampapi.endpoints.activity.TestFMService.HIDDEN_FM_PATH;
import static org.digijava.kernel.ampapi.endpoints.activity.TestFMService.VISIBLE_FM_PATH;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.CommonSettings;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.ampapi.endpoints.dto.UnwrappedTranslations;
import org.digijava.kernel.ampapi.endpoints.resource.dto.AmpResource;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.services.sync.model.SyncConstants;
import org.digijava.kernel.validators.ValidatorUtil;
import org.digijava.kernel.validators.common.TotalPercentageValidator;
import org.digijava.kernel.validators.common.SizeValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.TimestampField;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Octavian Ciubotaru
 */
public class FieldsEnumeratorTest {

    private static final int SIZE_LIMIT = 3;

    private TranslatorService translatorService;
    private FMService fmService;
    private FieldInfoProvider provider;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Mock private TranslatorService throwingTranslatorService;
    @Mock private TranslatorService emptyTranslatorService;

    @Mock private PossibleValuesDAO possibleValuesDAO;

    private FieldsEnumerator fieldsEnumerator;
    private PossibleValuesEnumerator pvEnumerator;

    private ValueConverter valueConverter;

    @Before
    public void setUp() throws Exception {
        translatorService = new TestTranslatorService();
        fmService = new TestFMService();
        provider = new TestFieldInfoProvider();

        when(throwingTranslatorService.getAllTranslationOfBody(any(), any())).thenThrow(new WorkerException());

        when(emptyTranslatorService.getAllTranslationOfBody(any(), any())).thenReturn(Collections.emptyList());

        fieldsEnumerator = new FieldsEnumerator(provider, fmService, translatorService, name -> true);
        pvEnumerator = new PossibleValuesEnumerator(possibleValuesDAO, translatorService);
        valueConverter = new ValueConverter();
    }

    @Test
    public void testEmpty() {
        List<APIField> fields = fieldsFor(Object.class);
        assertTrue(fields.isEmpty());
    }

    private static class NotAnnotated {
        @SuppressWarnings("unused")
        private String field;
    }

    @Test
    public void testNotAnnotated() {
        List<APIField> fields = fieldsFor(NotAnnotated.class);
        assertTrue(fields.isEmpty());
    }

    private static class OneFieldClass {

        @Interchangeable(fieldTitle = "One Field")
        private String field;
    }

    private static class DateTimesatmpFieldClass {

        @Interchangeable(fieldTitle = "date-field")
        private Date dateField;

        @Interchangeable(fieldTitle = "timestamp-field")
        @TimestampField
        private Date timestampField;
    }

    private static class ReadOnlyFieldClass {

        @Interchangeable(fieldTitle = "Read Only Empty Field", importable = true)
        private String readOnlyEmpty;

        @Interchangeable(fieldTitle = "Read Only Hidden Field", importable = true, readOnlyFmPath = HIDDEN_FM_PATH)
        private String readOnlyHidden;

        @Interchangeable(fieldTitle = "Read Only Visible Field", importable = true, readOnlyFmPath = VISIBLE_FM_PATH)
        private String readOnlyVisible;

        @Interchangeable(fieldTitle = "Not Importable Read Only Hidden Field", readOnlyFmPath = HIDDEN_FM_PATH)
        private String notImportableReadOnlyHidden;
    }

    private static class IatiFieldClass {
        @Interchangeable(fieldTitle = ActivityFieldsConstants.IATI_IDENTIFIER, importable = true,
                readOnlyFmPath = VISIBLE_FM_PATH)
        private String iatiField;
    }

    @Test
    public void testOneField() {
        List<APIField> actual = fieldsFor(OneFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName("one_field");
        expected.setFieldLabel(fieldLabelFor("One Field"));

        assertEqualsSingle(expected, actual);
    }

    @Test
    public void testDateTimesatmpField() {
        List<APIField> actual = fieldsFor(DateTimesatmpFieldClass.class);

        APIField dateField = newListField();
        dateField.setFieldName("date-field");
        dateField.setFieldLabel(fieldLabelFor("date-field"));
        dateField.setApiType(new APIType(Date.class, FieldType.DATE));

        APIField timestampField = newListField();
        timestampField.setFieldName("timestamp-field");
        timestampField.setFieldLabel(fieldLabelFor("timestamp-field"));
        timestampField.setApiType(new APIType(Date.class, FieldType.TIMESTAMP));

        assertEqualsDigest(Arrays.asList(dateField, timestampField), actual);
    }

    @Test
    public void testReadOnlyField() {
        List<APIField> actual = fieldsFor(ReadOnlyFieldClass.class);

        APIField readOnlyEmpty = newStringField();
        readOnlyEmpty.setFieldName("read_only_empty_field");
        readOnlyEmpty.setFieldLabel(fieldLabelFor("Read Only Empty Field"));
        readOnlyEmpty.setImportable(true);

        APIField readOnlyHidden = newStringField();
        readOnlyHidden.setFieldName("read_only_hidden_field");
        readOnlyHidden.setFieldLabel(fieldLabelFor("Read Only Hidden Field"));
        readOnlyHidden.setImportable(true);

        APIField readOnlyVisible = newStringField();
        readOnlyVisible.setFieldName("read_only_visible_field");
        readOnlyVisible.setFieldLabel(fieldLabelFor("Read Only Visible Field"));
        readOnlyVisible.setImportable(false);

        APIField notImportableReadOnlyVisible = newStringField();
        notImportableReadOnlyVisible.setFieldName("not_importable_read_only_hidden_field");
        notImportableReadOnlyVisible.setFieldLabel(fieldLabelFor("Not Importable Read Only Hidden Field"));
        notImportableReadOnlyVisible.setImportable(false);

        assertEqualsDigest(
                Arrays.asList(readOnlyEmpty, readOnlyHidden, readOnlyVisible, notImportableReadOnlyVisible), actual);
    }

    @Test
    public void testIatiFieldIatiImporterMode() {
        AmpClientModeHolder.setClientMode(ClientMode.IATI_IMPORTER);
        try {
            List<APIField> actual = fieldsFor(IatiFieldClass.class);

            APIField expected = newStringField();
            expected.setFieldName(FieldMap.underscorify(ActivityFieldsConstants.IATI_IDENTIFIER));
            expected.setFieldLabel(fieldLabelFor(ActivityFieldsConstants.IATI_IDENTIFIER));
            expected.setImportable(true);
            expected.setRequired(FIELD_ALWAYS_REQUIRED);

            assertEqualsSingle(expected, actual);
        } finally {
            AmpClientModeHolder.setClientMode(null);
        }
    }

    @Test
    public void testIatiFieldOfflineMode() {
        AmpClientModeHolder.setClientMode(ClientMode.AMP_OFFLINE);
        try {
            List<APIField> actual = fieldsFor(IatiFieldClass.class);

            APIField expected = newStringField();
            expected.setFieldName(FieldMap.underscorify(ActivityFieldsConstants.IATI_IDENTIFIER));
            expected.setFieldLabel(fieldLabelFor(ActivityFieldsConstants.IATI_IDENTIFIER));
            expected.setImportable(false);
            expected.setRequired(FIELD_NOT_REQUIRED);

            assertEqualsSingle(expected, actual);
        } finally {
            AmpClientModeHolder.setClientMode(null);
        }
    }

    @Test
    public void testIatiFieldDefaultMode() {
        List<APIField> actual = fieldsFor(IatiFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName(FieldMap.underscorify(ActivityFieldsConstants.IATI_IDENTIFIER));
        expected.setFieldLabel(fieldLabelFor(ActivityFieldsConstants.IATI_IDENTIFIER));
        expected.setImportable(false);
        expected.setRequired(FIELD_NOT_REQUIRED);

        assertEqualsSingle(expected, actual);
    }

    @Test
    public void testInvisibleField() {
        FMService invisibleFmService = mock(FMService.class);

        when(invisibleFmService.isVisible(any())).thenReturn(false);

        List<APIField> actual =
                new FieldsEnumerator(provider, invisibleFmService, translatorService, name -> true)
                        .getAllAvailableFields(OneFieldClass.class);

        assertEquals(Collections.emptyList(), actual);
    }

    private static class Composition {

        @Interchangeable(fieldTitle = "field")
        private NestedField field;
    }

    private static class NestedField {

        @Interchangeable(fieldTitle = "field")
        private Long field;
    }

    @Test
    public void testComposition() {
        List<APIField> actual = fieldsFor(Composition.class);

        APIField expected = newObjectField();
        APIField nestedField = newLongField();
        expected.setChildren(Arrays.asList(nestedField));

        assertEqualsSingle(expected, actual);
    }

    private static class RequiredFieldClass {

        @Interchangeable(fieldTitle = "field_not_required_implicit")
        private String fieldNotRequiredImplicit;

        @Interchangeable(fieldTitle = "field_not_required_explicit", required = NONE)
        private String fieldNotRequiredExplicit;

        @Interchangeable(fieldTitle = "field_required_always", required = ALWAYS)
        private String fieldRequiredAlways;

        @Interchangeable(fieldTitle = "field_required_non_draft", required = SUBMIT)
        private String fieldRequiredNonDraft;

        @Interchangeable(fieldTitle = "field_required_submit_fm_path_visible", requiredFmPath = VISIBLE_FM_PATH,
                required = SUBMIT)
        private String fieldRequiredSubmitFmPathVisible;

        @Interchangeable(fieldTitle = "field_required_submit_fm_path_hidden", requiredFmPath = HIDDEN_FM_PATH,
                required = SUBMIT)
        private String fieldRequiredSubmitFmPathHidden;

        @Interchangeable(fieldTitle = "field_required_always_fm_path_visible", requiredFmPath = VISIBLE_FM_PATH,
                required = ALWAYS)
        private String fieldRequiredAlwaysFmPathVisible;

        @Interchangeable(fieldTitle = "field_required_always_fm_path_hidden", requiredFmPath = HIDDEN_FM_PATH,
                required = ALWAYS)
        private String fieldRequiredAlwaysFmPathHidden;
    }

    @Test
    public void testRequired() {
        List<APIField> actual = fieldsFor(RequiredFieldClass.class);

        List<APIField> expected = Arrays.asList(
                newRequiredField("field_not_required_implicit", FIELD_NOT_REQUIRED),
                newRequiredField("field_not_required_explicit", FIELD_NOT_REQUIRED),
                newRequiredField("field_required_always", FIELD_ALWAYS_REQUIRED),
                newRequiredField("field_required_non_draft", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_submit_fm_path_visible", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_submit_fm_path_hidden", FIELD_NOT_REQUIRED),
                newRequiredField("field_required_always_fm_path_visible", FIELD_ALWAYS_REQUIRED),
                newRequiredField("field_required_always_fm_path_hidden", FIELD_NOT_REQUIRED)
        );

        assertEqualsDigest(expected, actual);
    }

    private APIField newRequiredField(String fieldName, String required) {
        APIField field = newStringField();
        field.setFieldName(fieldName);
        field.setFieldLabel(fieldLabelFor(fieldName));
        field.setRequired(required);
        return field;
    }

    private static class ImportableFieldClass {

        @Interchangeable(fieldTitle = "field", importable = true)
        private Long field;
    }

    @Test
    public void testImportable() {
        List<APIField> actual = fieldsFor(ImportableFieldClass.class);

        APIField expected = newLongField();
        expected.setImportable(true);

        assertEqualsSingle(expected, actual);
    }

    private static class MultipleValuesClass {

        @Interchangeable(fieldTitle = "field", multipleValues = false)
        private List<Object> field;
    }

    @Test
    public void testMultipleValues() {
        List<APIField> actual = fieldsFor(MultipleValuesClass.class);

        APIField expected = newObjectField();

        assertEqualsSingle(expected, actual);
    }

    private static class SimpleTypeListClass {

        @Interchangeable(fieldTitle = "field")
        private List<Long> field;
    }

    @Test
    public void testSimpleTypeList() {
        List<APIField> actual = fieldsFor(SimpleTypeListClass.class);

        APIField expected = newListOfLongField();
        expected.setMultipleValues(true);

        assertEqualsSingle(expected, actual);
    }

    private static class LongFieldClass {

        @Interchangeable(fieldTitle = "field")
        private Long field;
    }

    @Test
    public void testId() {
        List<APIField> actual = fieldsFor(LongFieldClass.class);

        APIField expected = newAPIField();
        expected.setApiType(new APIType(Long.class));

        assertEqualsSingle(expected, actual);
    }

    private static class PickIdOnlyClass {

        // FIXME such annotations should be illegal
        @Interchangeable(fieldTitle = "field", pickIdOnly = true)
        private Long field;
    }

    @Test
    public void testPickIdOnly() {
        List<APIField> actual = fieldsFor(PickIdOnlyClass.class);

        APIField expected = newLongField();
        expected.setIdOnly(true);

        assertEqualsSingle(expected, actual);
    }

    private static class DiscriminatedClass {

        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "type_a", discriminatorOption = "a", fmPath = HIDDEN_FM_PATH),
                @Interchangeable(fieldTitle = "type_b", discriminatorOption = "b")
        })
        private Long field;
    }

    @Test
    public void testDiscriminatedField() {
        List<APIField> actual = fieldsFor(DiscriminatedClass.class);

        APIField expected = newLongField();
        expected.setFieldName("type_b");
        expected.setFieldLabel(fieldLabelFor("type_b"));

        assertEqualsSingle(expected, actual);
    }

    private static class InternalConstraints {

        @Interchangeable(fieldTitle = "1",
                interValidators = @InterchangeableValidator(
                        value = SizeValidator.class,
                        attributes = "max=1"))
        private Collection<ObjWithId> field1;

        @Interchangeable(fieldTitle = "2")
        private Collection<ObjWithId> field2;

        @Interchangeable(fieldTitle = "3", interValidators = @InterchangeableValidator(
                value = TotalPercentageValidator.class, fmPath = "percentageFmName"))
        private Collection<PercentageConstrained> field3;

        @Interchangeable(fieldTitle = "4", validators = @Validators(unique = "uniqueFmName"))
        private Collection<UniqueConstrained> field4;

        @Interchangeable(fieldTitle = "8", validators = @Validators(unique = "uniqueFmName"), uniqueConstraint = true)
        private Collection<Integer> field8;

        @Interchangeable(fieldTitle = "5", validators = @Validators(treeCollection = "treeCollectionFmName"))
        private Collection<ObjWithId> field5;

        @Interchangeable(fieldTitle = "6",
                interValidators = @InterchangeableValidator(
                        value = TotalPercentageValidator.class, fmPath = "percentageFmName"),
                validators = @Validators(unique = "uniqueFmName"))
        private Collection<PercentageConstrained> field6;

        @Interchangeable(fieldTitle = "7",
                interValidators = @InterchangeableValidator(
                        value = SizeValidator.class,
                        attributes = "max=" + SIZE_LIMIT))
        private Collection<ObjWithId> field7;
    }

    private static class ObjWithId {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;
    }

    private static class PercentageConstrained extends ObjWithId {

        @Interchangeable(fieldTitle = "field", percentageConstraint = true)
        private Long field;
    }

    private static class UniqueConstrained extends ObjWithId {

        @Interchangeable(fieldTitle = "field", uniqueConstraint = true)
        private Long field;
    }

    @Test
    public void testInternalConstraints() {
        List<APIField> actual = fieldsFor(InternalConstraints.class);

        APIField idField = newLongField();
        idField.setId(true);
        idField.setFieldName("id");
        idField.setFieldLabel(fieldLabelFor("Id"));

        APIField expected1 = newListField();
        expected1.setMultipleValues(false);
        expected1.setFieldName("1");
        expected1.setFieldLabel(fieldLabelFor("1"));
        expected1.setChildren(Arrays.asList(idField));

        APIField expected2 = newListField();
        expected2.setFieldName("2");
        expected2.setFieldLabel(fieldLabelFor("2"));
        expected2.setMultipleValues(true);
        expected2.setChildren(Arrays.asList(idField));

        APIField percentageField = newLongField();
        percentageField.setPercentage(true);

        APIField expected3 = newListField();
        expected3.setFieldName("3");
        expected3.setFieldLabel(fieldLabelFor("3"));
        expected3.setPercentageConstraint("field");
        expected3.setChildren(Arrays.asList(percentageField, idField));
        expected3.setMultipleValues(true);

        APIField expected4child = newLongField();

        APIField expected4 = newListField();
        expected4.setFieldName("4");
        expected4.setFieldLabel(fieldLabelFor("4"));
        expected4.setUniqueConstraint("field");
        expected4.setChildren(Arrays.asList(expected4child, idField));
        expected4.setMultipleValues(true);

        APIField expected8 = newListField(Integer.class);
        expected8.setFieldName("8");
        expected8.setFieldLabel(fieldLabelFor("8"));
        expected8.setUniqueConstraint("8");
        expected8.setMultipleValues(true);

        APIField expected5 = newListField();
        expected5.setFieldName("5");
        expected5.setFieldLabel(fieldLabelFor("5"));
        expected5.setTreeCollectionConstraint(true);
        expected5.setMultipleValues(true);
        expected5.setChildren(Arrays.asList(idField));

        APIField expected6 = newListField();
        expected6.setFieldName("6");
        expected6.setFieldLabel(fieldLabelFor("6"));
        expected6.setMultipleValues(true);
        expected6.setPercentageConstraint("field");
        expected6.setChildren(Arrays.asList(percentageField, idField));

        APIField expected7 = newListField();
        expected7.setFieldName("7");
        expected7.setFieldLabel(fieldLabelFor("7"));
        expected7.setMultipleValues(true);
        expected7.setSizeLimit(SIZE_LIMIT);
        expected7.setChildren(Arrays.asList(idField));

        assertEqualsDigest(Arrays.asList(expected1, expected2, expected3, expected4, expected8, expected5,
                expected6, expected7), actual);
    }

    @Test
    public void testFieldNameInternal() {
        List<APIField> fields = fieldsFor(OneFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName("one_field");
        expected.setFieldLabel(fieldLabelFor("One Field"));
        expected.setFieldNameInternal("field");

        assertEqualsSingle(expected, fields);
    }

    private static class RefActivity {

        @Interchangeable(fieldTitle = "field1", pickIdOnly = true)
        private AmpActivityVersion activity2;
    }

    @Test
    public void testActivityFlag() {
        List<APIField> fields = fieldsFor(RefActivity.class);

        APIField expected1 = newLongField();
        expected1.setFieldName("field1");
        expected1.setFieldLabel(fieldLabelFor("field1"));
        expected1.setFieldNameInternal("activity2");
        expected1.setIdOnly(true);
        expected1.setApiType(new APIType(Long.class));

        assertEqualsDigest(Arrays.asList(expected1), fields);
    }

    private static class MaxLen {

        @Interchangeable(fieldTitle = "field")
        private String noMaxLen;
    }

    @Test
    public void testNoMaxLen() {
        List<APIField> fields = fieldsFor(MaxLen.class);

        APIField expected = newStringField();
        expected.setFieldLength(null);

        assertEqualsSingle(expected, fields);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInTranslator() {
        new FieldsEnumerator(provider, fmService, throwingTranslatorService, name -> true)
                .getAllAvailableFields(OneFieldClass.class);
    }

    @Test
    public void testDefaultTranslation() {
        List<APIField> fields = new FieldsEnumerator(provider, fmService, emptyTranslatorService, name -> true)
                .getAllAvailableFields(OneFieldClass.class);

        assertEquals(1, fields.size());
        assertEquals("One Field", fields.get(0).getFieldLabel().get("EN"));
    }

    @Test
    public void testNonEmptyChildren() {
        String originalJson = "[{\"field_name\":\"field\"," +
                "\"field_type\":\"object\"," +
                "\"field_label\":{\"en\":\"field en\",\"fr\":\"field fr\"}," +
                "\"required\":\"N\"," +
                "\"importable\":false," +
                "\"id\":false," +
                "\"children\":[{" +
                    "\"field_name\":\"field\"," +
                    "\"field_type\":\"long\"," +
                    "\"field_label\":{\"en\":\"field en\",\"fr\":\"field fr\"}," +
                    "\"required\":\"N\"," +
                    "\"importable\":false," +
                    "\"id\":false" +
                    "}]" +
                "}]";
        try {
            List<APIField> actual = fieldsFor(Composition.class);
            String actualJson = new ObjectMapper().writeValueAsString(actual);
            assertEquals(originalJson, actualJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEmptyChildren() {
        String originalJson = "[{\"field_name\":\"field\"" +
                ",\"field_type\":\"long\"," +
                "\"field_label\":{\"en\":\"field en\",\"fr\":\"field fr\"}," +
                "\"required\":\"N\"," +
                "\"importable\":false," +
                "\"id\":false}]";
        try {
            List<APIField> actual = fieldsFor(NestedField.class);
            String actualJson = new ObjectMapper().writeValueAsString(actual);
            assertEquals(originalJson, actualJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Dependencies {

        @Interchangeable(fieldTitle = "field", dependencies = {"dep1", "dep2"})
        private String field;
    }

    @Test
    public void testDependencies() {
        List<APIField> fields = fieldsFor(Dependencies.class);

        APIField expected = newStringField();
        expected.setDependencies(Arrays.asList("dep1", "dep2"));

        assertEqualsSingle(expected, fields);
    }

    @Test
    public void testDatabaseIsNotAccessedAndConfigurationIsValid() {
        fieldsFor(AmpActivityVersion.class);
        fieldsFor(AmpContact.class);
        fieldsFor(CommonSettings.class);
        fieldsFor(AmpResource.class);
    }

    @Test
    public void testFieldsWithPossibleValuesPredicate() {
        List<APIField> fields = fieldsEnumerator.getAllAvailableFields(AmpActivityFields.class);

        Predicate<APIField> fieldFilter = pvEnumerator.fieldsWithPossibleValues();

        List<String> fieldPaths = fieldsEnumerator.findFieldPaths(fieldFilter, fields);

        assertThat(fieldPaths, hasItems(
                "team", // ref by type
                "primary_sectors~sector", // nested ref by type
                "approval_status", // ref by possible value
                "fundings~commitments~pledge")); // nested ref by possible value
    }

    @Test
    public void testFieldsDependingOnPredicate() {
        List<APIField> fields = fieldsEnumerator.getAllAvailableFields(AmpActivityFields.class);
        Predicate<APIField> fieldFilter = pvEnumerator.fieldsDependingOn(ImmutableSet.of(SyncConstants.Entities.SECTOR));

        List<String> fieldPaths = fieldsEnumerator.findFieldPaths(fieldFilter, fields);

        assertThat(fieldPaths, hasItems("primary_sectors~sector"));
        assertThat(fieldPaths, not(hasItems("team", "approval_status", "fundings~commitments~pledge")));
    }

    private static class ObjWithImportableCollectionWithoutId {

        @Interchangeable(fieldTitle = "col", importable = true)
        private Set<Object> col;
    }

    @Test(expected = RuntimeException.class)
    public void testEnumerationFailsIfObjectFromCollectionDoesntExposeId() {
        fieldsEnumerator.getAllAvailableFields(ObjWithImportableCollectionWithoutId.class);
    }

    private static class ObjWithReadOnlyCollectionWithoutId {

        @Interchangeable(fieldTitle = "col")
        private Set<Object> col;
    }

    @Test(expected = RuntimeException.class)
    public void testEnumerationFailsIfObjectFromReadOnlyCollectionDoesntExposeId() {
        fieldsEnumerator.getAllAvailableFields(ObjWithReadOnlyCollectionWithoutId.class);
    }

    private static class ObjWithPrimitiveCollections {

        @Interchangeable(fieldTitle = "col1")
        private Set<String> col1;

        @Interchangeable(fieldTitle = "col2")
        private Set<Long> col2;
    }

    @Test
    public void testEnumerationDoesNotFailForCollectionsOfPrimitives() {
        List<APIField> fields = fieldsEnumerator.getAllAvailableFields(ObjWithPrimitiveCollections.class);
        assertThat(fields.size(), is(2));
    }

    private static class ObjWithObjId {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Object id;
    }

    @Test(expected = RuntimeException.class)
    public void testObjectIdsNotAllowed() {
        fieldsEnumerator.getAllAvailableFields(ObjWithObjId.class);
    }

    private static class ObjWithListId {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private List<Long> id;
    }

    @Test(expected = RuntimeException.class)
    public void testListIdsNotAllowed() {
        fieldsEnumerator.getAllAvailableFields(ObjWithListId.class);
    }


    private static class ObjWithCollectionWithTwoIds {

        @Interchangeable(fieldTitle = "Col")
        private List<ObjWithTwoIds> col;
    }

    private static class ObjWithTwoIds {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id 1")
        private Long id1;

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id 2")
        private Long id2;
    }

    @Test(expected = RuntimeException.class)
    public void testTwoIdsNotAllowed() {
        fieldsEnumerator.getAllAvailableFields(ObjWithCollectionWithTwoIds.class);
    }


    @Test
    public void testAPIFieldActivityFields() {
        List<APIField> nullableAPIFields = getAPIFieldWithNullCollections(AmpActivityVersion.class,
                fieldsFor(AmpActivityFields.class));
        assertEquals(nullableAPIFields, Collections.emptyList());
    }

    private List<APIField> getAPIFieldWithNullCollections(Class<?> type, List<APIField> apiFields) {
        List<APIField> nullableAPIFields = new ArrayList<>();
        Object object = valueConverter.getNewInstance(type);
        for (APIField apiField : apiFields) {
            if (apiField.isCollection() && apiField.getFieldAccessor().get(object) == null) {
                nullableAPIFields.add(apiField);
            }
            if (apiField.isCollection() && !InterchangeUtils.isSimpleType(apiField.getApiType().getType())) {
                nullableAPIFields.addAll(
                        getAPIFieldWithNullCollections(apiField.getApiType().getType(), apiField.getChildren()));
            }
        }

        return nullableAPIFields;
    }

    @Test
    public void testTreeValidatorVisibleAndUniqueValidatorHidden() {
        APIField apiField = ValidatorUtil.getMetaData(AmpActivityFields.class,
                ImmutableSet.of("/Activity Form/Sectors/Primary Sectors/uniqueSectorsValidator"));

        assertThat(apiField.getChildren(), hasItem(allOf(
                        hasProperty("fieldName", equalTo("primary_sectors")),
                        hasProperty("uniqueConstraint", equalTo("sector")),
                        hasProperty("treeCollectionConstraint", equalTo(true))
                )));
    }

    @Test
    public void testTreeValidatorAndUniqueValidatorHidden() {
        APIField apiField = ValidatorUtil.getMetaData(AmpActivityFields.class,
                ImmutableSet.of("/Activity Form/Sectors/Primary Sectors/uniqueSectorsValidator",
                        "/Activity Form/Sectors/Primary Sectors/treeSectorsValidator"));

        assertThat(apiField.getChildren(), hasItem(allOf(
                hasProperty("fieldName", equalTo("primary_sectors")),
                hasProperty("uniqueConstraint", nullValue()),
                hasProperty("treeCollectionConstraint", nullValue())
        )));
    }

    private APIField newListField() {
        APIField field = newAPIField();
        field.setApiType(new APIType(Object.class, FieldType.LIST));
        return field;
    }

    private APIField newListField(Class<?> type) {
        APIField field = newAPIField();
        field.setApiType(new APIType(type, FieldType.LIST));
        return field;
    }

    private APIField newObjectField() {
        APIField field = newAPIField();
        field.setApiType(new APIType(Object.class));
        return field;
    }

    private APIField newListOfLongField() {
        APIField field = newAPIField();
        field.setApiType(new APIType(Long.class, FieldType.LIST));
        return field;
    }

    private APIField newLongField() {
        APIField field = newAPIField();
        field.setApiType(new APIType(Long.class));
        return field;
    }

    private APIField newStringField() {
        APIField field = newAPIField();
        field.setApiType(new APIType(String.class));
        field.setFieldLength(TestFieldInfoProvider.MAX_STR_LEN);
        field.setTranslatable(false);
        return field;
    }

    private APIField newAPIField() {
        APIField field = new APIField();
        field.setFieldName("field");
        field.setFieldNameInternal("field");
        field.setRequired(FIELD_NOT_REQUIRED);
        field.setImportable(false);
        field.setFieldLabel(fieldLabelFor("field"));
        return field;
    }

    private void assertEqualsSingle(APIField expected, List<APIField> actual) {
        assertEqualsDigest(Arrays.asList(expected), actual);
    }

    private List<APIField> fieldsFor(Class<?> theClass) {
        return fieldsEnumerator.getAllAvailableFields(theClass);
    }

    private void assertEqualsDigest(List<APIField> expected, List<APIField> actual) {
        assertEquals(
                expected.stream().map(this::digest).collect(Collectors.toList()),
                actual.stream().map(this::digest).collect(Collectors.toList()));
    }

    private UnwrappedTranslations fieldLabelFor(String baseText) {
        return new UnwrappedTranslations()
                .set("en", baseText + " en")
                .set("fr", baseText + " fr");
    }

    private <T> String digest(T obj) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create digest for " + obj, e);
        }
    }
}
