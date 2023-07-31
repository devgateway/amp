package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.common.util.DateTimeUtil;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectExporterTest {

    private ObjectExporter<Dummy> exporter;

    @Before
    public void setUp() {
        TestTranslatorService translatorService = new TestTranslatorService();

        TransactionUtil.setUpWorkspaceEmptyPrefixes();

        FieldsEnumerator enumerator = new FieldsEnumerator(
                new TestFieldInfoProvider(),
                new TestFMService(),
                translatorService,
                program -> false);
        List<APIField> fields = enumerator.getAllAvailableFields(Dummy.class);

        ActivityTranslationUtils.setTranslatorService(translatorService);

        exporter = new ObjectExporter<>(new DummyTranslatedFieldReader(), fields);
    }

    private static class DummyTranslatedFieldReader implements TranslatedFieldReader {

        @Override
        public Object get(Field field, Class<?> clazz, Object fieldValue, Object parentObject) {
            return fieldValue == null ? null : "trn " + fieldValue;
        }

        @Override
        public boolean isTranslatable(Field field, Class<?> clazz) {
            return field.getName().equals("translated");
        }
    }

    public static class Dummy implements Identifiable {

        private Long id;

        @Interchangeable(fieldTitle = "Simple Value")
        private Integer simpleValue;

        @Interchangeable(fieldTitle = "Date Value")
        private Date dateValue;

        @Interchangeable(fieldTitle = "Translated")
        private String translated;

        @Interchangeable(fieldTitle = "Dummy Ref", pickIdOnly = true)
        private Dummy dummyRef;

        /**
         * This should be identical to dummyRef but it isn't because ApprovalStatus implements Identifiable which uses
         * string for ids. ObjectExporter implements a hack to use ApprovalStatus#getId() instead.
         * TODO support this case properly
         */
        @Interchangeable(fieldTitle = "Approval Status", pickIdOnly = true)
        private ApprovalStatus approvalStatus;

        /**
         * Exporting single objects is not supported. ObjectExporter implements a hack that
         * exports this AmpActivityGroup as an unwrapped object.
         * TODO add proper support for exporting a single object. useful for AmpActivityGroup and PPC/RPC.
         */
        @Interchangeable(fieldTitle = "Activity Group")
        private AmpActivityGroup activityGroup;

        @Interchangeable(fieldTitle = "List Of Integers")
        private List<Integer> listOfIntegers = new ArrayList<>();

        @Interchangeable(fieldTitle = "Simple Value With PV")
        @PossibleValues(OnePV.class)
        private Long simpleValueWithPV;

        /**
         * It is impossible to export multiple ids when field is discriminated and pickIdOnly=true,
         * multipleValues parameter is irrelevant.
         */
        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Category A", discriminatorOption = "A", pickIdOnly = true,
                        multipleValues = false),
                @Interchangeable(fieldTitle = "Category B", discriminatorOption = "B", pickIdOnly = true,
                        multipleValues = false),
                @Interchangeable(fieldTitle = "Category C", discriminatorOption = "C", pickIdOnly = true)})
        private List<DummyCategory> categories = new ArrayList<>();

        /**
         * It is impossible to export a single object in discriminated case, output will be an object
         * wrapped in array and multipleValues parameter is irrelevant.
         */
        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Sub A", discriminatorOption = "A", multipleValues = false),
                @Interchangeable(fieldTitle = "Sub B", discriminatorOption = "B")
        })
        private List<DummySub> discriminatedSubs = new ArrayList<>();

        @Interchangeable(fieldTitle = "List of PickIdOnly", pickIdOnly = true)
        private List<DummyCategory> listOfPickIdOnly = new ArrayList<>();

        @Override
        public Object getIdentifier() {
            return id;
        }
    }

    public static class DummyCategory implements Identifiable {

        private Long id;
        private String type;

        DummyCategory(Long id, String type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public Object getIdentifier() {
            return id;
        }

        public String getType() {
            return type;
        }
    }

    public static class DummySub {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        private String type;

        @Interchangeable(fieldTitle = "Sub Name")
        private String name;

        DummySub(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }
    }

    private static class OnePV implements PossibleValueProviderTest {

        @Override
        public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
            return ImmutableList.of(new PossibleValue(1L, "1"));
        }
    }

    @Test
    public void testSimpleValue() {
        Dummy dummy = new Dummy();
        dummy.simpleValue = 123;

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("simple_value", 123));
    }

    @Test
    public void testRef() {
        Dummy otherDummy = new Dummy();
        otherDummy.id = 512L;

        Dummy dummy = new Dummy();
        dummy.dummyRef = otherDummy;

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("dummy_ref", 512L));
    }

    @Test
    public void testApprovalStatusRefHack() {
        Dummy dummy = new Dummy();
        dummy.approvalStatus = ApprovalStatus.started;

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("approval_status", ApprovalStatus.started.getId()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListOfIntegers() {
        Dummy dummy = new Dummy();
        dummy.listOfIntegers = ImmutableList.of(1, 4, 8);

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, (Matcher) hasEntry(
                equalTo("list_of_integers"),
                containsInAnyOrder(1, 4, 8)));
    }

    /**
     * Why this matters? Shouldn't be special.
     */
    @Test
    public void testSimpleValueWithPV() {
        Dummy dummy = new Dummy();
        dummy.simpleValueWithPV = 1L;

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("simple_value_with_pv", 1L));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testActivityGroupHack() {
        AmpActivityGroup group = new AmpActivityGroup();
        group.setVersion(2L);

        Dummy dummy = new Dummy();
        dummy.activityGroup = group;

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, (Matcher) hasEntry(
                equalTo("activity_group"),
                hasEntry("version", 2L)));
    }

    @Test
    public void testDiscriminatedPickIdOnly() {
        Dummy dummy = new Dummy();
        dummy.categories = ImmutableList.of(
                new DummyCategory(1L, "A"),
                new DummyCategory(2L, "B"),
                new DummyCategory(7L, "C"),
                new DummyCategory(8L, "C"));

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj,
                allOf(
                        hasEntry("category_a", 1L),
                        hasEntry("category_b", 2L),
                        hasEntry(is("category_c"), (Matcher) containsInAnyOrder(7L, 8L))));
    }

    @Test(expected = RuntimeException.class)
    public void testDiscriminatedPickIdOnlyMultiple() {
        Dummy dummy = new Dummy();
        dummy.categories = ImmutableList.of(
                new DummyCategory(1L, "A"),
                new DummyCategory(2L, "A"));

        exporter.export(dummy);
    }

    @Test
    public void testListOfPickIdOnly() {
        Dummy dummy = new Dummy();
        dummy.listOfPickIdOnly = ImmutableList.of(
                new DummyCategory(1L, "A"),
                new DummyCategory(2L, "B"));

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry(is("list_of_pickidonly"), (Matcher) containsInAnyOrder(1L, 2L)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDiscriminatedObj() {
        Dummy dummy = new Dummy();
        dummy.discriminatedSubs = ImmutableList.of(
                new DummySub("A", "First Sub"),
                new DummySub("B", "Second Sub"));

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, (Matcher) hasEntry(equalTo("sub_a"), hasEntry("sub_name", "First Sub")));
        assertThat(jsonObj, (Matcher) hasEntry(equalTo("sub_b"), contains(hasEntry("sub_name", "Second Sub"))));
    }

    @Test(expected = RuntimeException.class)
    public void testDiscriminatedObjMultiple() {
        Dummy dummy = new Dummy();
        dummy.discriminatedSubs = ImmutableList.of(
                new DummySub("A", "First Sub"),
                new DummySub("A", "Second Sub"),
                new DummySub("B", "Third Sub"));

        exporter.export(dummy);
    }

    @Test
    public void testDatesAreFormatted() {
        Dummy dummy = new Dummy();
        dummy.dateValue = new Date();

        String formattedDate = DateTimeUtil.formatISO8601Date(dummy.dateValue);

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("date_value", formattedDate));
    }

    @Test
    public void testTranslatedField() {
        Dummy dummy = new Dummy();
        dummy.translated = "yes";

        Map<String, Object> jsonObj = exporter.export(dummy);

        assertThat(jsonObj, hasEntry("translated", "trn yes"));
    }
}
