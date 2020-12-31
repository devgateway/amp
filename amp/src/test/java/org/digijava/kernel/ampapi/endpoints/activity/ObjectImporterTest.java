package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.ObjectImporterAnyType;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.persistence.InMemoryValueConverter;
import org.digijava.module.aim.annotations.interchange.Independent;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

/**
 * Important cases to test.
 * <p>
 * By field type:
 * <ul><li>primitive
 * <li>pick id only
 * <li>object
 * <li>discriminated object
 * <li>discriminated pick id only
 * <li>list of primitives
 * <li>list of pick id only (not supported yet)
 * <li>list of objects
 * <li>list of discriminated objects
 * <li>list of discriminated pick id only (not supported yet)</ul>
 * <p>
 * By action:
 * <ul><li>field can be cleared (had a value, and with null or [] in json we're able to clear the field)
 * <li>field can be set (value was null or empty and changed to new value)
 * <li>field can be modified (one value can be replaced with another value)
 * <li>field is not modified (null or empty value is kept)
 * <li>read only field cannot be modified</ul>
 *
 * @author Octavian Ciubotaru
 */
public class ObjectImporterTest {

    public static class Parent {

        @Interchangeable(fieldTitle = "Children", importable = true)
        private List<Child> children = new ArrayList<>();

        @Interchangeable(fieldTitle = "Read Only Children")
        private List<Child> readOnlyChildren = new ArrayList<>();

        @Interchangeable(fieldTitle = "Adopted Children", importable = true)
        private Set<Child> adoptedChildren = new HashSet<>();

        @Interchangeable(fieldTitle = "Years", importable = true)
        private Set<Integer> years = new HashSet<>();

        @Interchangeable(fieldTitle = "Read Only Years")
        private Set<Integer> readOnlyYears = new HashSet<>();

        @Interchangeable(fieldTitle = "Name", importable = true)
        private String name;

        @Interchangeable(fieldTitle = "Read Only Name")
        private String readOnlyName;

        private Integer age;

        @Interchangeable(fieldTitle = "Gender")
        private String gender = "X";

        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Home", discriminatorOption = "H", importable = true),
                @Interchangeable(fieldTitle = "Work", discriminatorOption = "W", importable = true,
                        fmPath = TestFMService.HIDDEN_FM_PATH),
                @Interchangeable(fieldTitle = "Read Only Address", discriminatorOption = "R")})
        private Set<Address> addresses = new HashSet<>();

        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Home Phone", importable = true, discriminatorOption = "H",
                        multipleValues = false),
                @Interchangeable(fieldTitle = "Read Only Phone", discriminatorOption = "R",
                        multipleValues = false),
                @Interchangeable(fieldTitle = "Work Phone", importable = true, discriminatorOption = "W",
                        multipleValues = false)})
        private Set<Phone> phones = new HashSet<>();

        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Hair Color", discriminatorOption = "Hair", importable = true,
                        multipleValues = false, pickIdOnly = true),
                @Interchangeable(fieldTitle = "Height", discriminatorOption = "Height", importable = true,
                        multipleValues = false, pickIdOnly = true),
                @Interchangeable(fieldTitle = "Read Only Intelligence", discriminatorOption = "Intelligence",
                        multipleValues = false, pickIdOnly = true),
                @Interchangeable(fieldTitle = "Character", discriminatorOption = "Character", importable = true,
                        multipleValues = true, pickIdOnly = true)})
/*                @Interchangeable(fieldTitle = "Character", discriminatorOption = "Character", importable = true,
                        pickIdOnly = true)})*/
        private Set<PersonAttribute> attributes = new HashSet<>();

        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Emails", discriminatorOption = "E", importable = true,
                        type = Email.class),
                @Interchangeable(fieldTitle = "Web Sites", discriminatorOption = "W", importable = true,
                        type = WebSite.class)})
        private Set<ContactAttribute> contactAttributes = new HashSet<>();

        @Interchangeable(fieldTitle = "Favorite Color", discriminatorOption = "Color",
                importable = true, pickIdOnly = true)
        private PersonAttribute color;

        @Interchangeable(fieldTitle = "Read Only Favorite Color", discriminatorOption = "Color",
                pickIdOnly = true)
        private PersonAttribute readOnlyColor;

        @Independent
        @Interchangeable(fieldTitle = "Agreement", importable = true)
        private Agreement agreement;

        @Interchangeable(fieldTitle = "Details", importable = true)
        private ParentDetails details;

        @Interchangeable(fieldTitle = "Read Only Details")
        private ParentDetails readOnlyDetails;

        @Interchangeable(fieldTitle = "Status", importable = true, pickIdOnly = true)
        private Long status;

        @Interchangeable(fieldTitle = "Statuses", importable = true, pickIdOnly = true)
        private Set<Long> statuses = new HashSet<>();

        public Parent() {
        }

        public Parent(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public List<Child> getChildren() {
            return children;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        void addChild(Child child) {
            children.add(child);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Set<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(Set<Address> addresses) {
            this.addresses = addresses;
        }

        void addAddress(Address address) {
            addresses.add(address);
        }

        public Set<Phone> getPhones() {
            return phones;
        }

        public void setPhones(Set<Phone> phones) {
            this.phones = phones;
        }

        public Set<PersonAttribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(Set<PersonAttribute> attributes) {
            this.attributes = attributes;
        }

        public PersonAttribute getColor() {
            return color;
        }

        public void setColor(PersonAttribute color) {
            this.color = color;
        }

        void addPhone(Phone phone) {
            phones.add(phone);
        }

        public Set<Child> getAdoptedChildren() {
            return adoptedChildren;
        }

        public void setAdoptedChildren(Set<Child> adoptedChildren) {
            this.adoptedChildren = adoptedChildren;
        }

        void addAdoptedChild(Child child) {
            adoptedChildren.add(child);
        }

        void addAttribute(PersonAttribute attribute) {
            attributes.add(attribute);
        }

        public Agreement getAgreement() {
            return agreement;
        }

        public void setAgreement(Agreement agreement) {
            this.agreement = agreement;
        }

        public ParentDetails getDetails() {
            return details;
        }

        public void setDetails(ParentDetails details) {
            this.details = details;
        }

        public Set<Integer> getYears() {
            return years;
        }

        public void setYears(Set<Integer> years) {
            this.years = years;
        }

        public void addYear(Integer year) {
            years.add(year);
        }

        public List<Child> getReadOnlyChildren() {
            return readOnlyChildren;
        }

        public String getReadOnlyName() {
            return readOnlyName;
        }

        public PersonAttribute getReadOnlyColor() {
            return readOnlyColor;
        }

        public ParentDetails getReadOnlyDetails() {
            return readOnlyDetails;
        }

        public Set<Integer> getReadOnlyYears() {
            return readOnlyYears;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getStatus() {
            return status;
        }

        public Set<Long> getStatuses() {
            return statuses;
        }

        public void setStatuses(Set<Long> statuses) {
            this.statuses = statuses;
        }

        public Set<? extends ContactAttribute> getContactAttributes() {
            return contactAttributes;
        }
    }

    public abstract static class ContactAttribute {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Email extends ContactAttribute {

        @Interchangeable(fieldTitle = "Value", importable = true)
        private String value;

        public String getValue() {
            return value;
        }
    }

    public static class WebSite extends ContactAttribute {

        @Interchangeable(fieldTitle = "Value", importable = true)
        private String value;

        public String getValue() {
            return value;
        }
    }

    public static class ParentDetails {


        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        @Interchangeable(fieldTitle = "Detail", importable = true)
        private String detail;

        public ParentDetails(Long id, String detail) {
            this.id = id;
            this.detail = detail;
        }

        public ParentDetails() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    public static class Agreement {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        @Interchangeable(fieldTitle = "Description", importable = true)
        private String description;

        public Agreement(Long id, String description) {
            this.id = id;
            this.description = description;
        }

        public Agreement() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Phone {

        private String type; // H=home, W=work

        @Interchangeable(fieldTitle = "Number", importable = true)
        private String number;

        private String extraInfo;

        public Phone() {
        }

        public Phone(String type, String number, String extraInfo) {
            this.type = type;
            this.number = number;
            this.extraInfo = extraInfo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getExtraInfo() {
            return extraInfo;
        }

        public void setExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
        }
    }

    public static class Address {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        private String type; // H=home / W=work

        @Interchangeable(fieldTitle = "Address", importable = true)
        private String address;

        @Interchangeable(fieldTitle = "Phone", importable = true)
        private String phone;

        public Address() {
        }

        public Address(Long id, String type, String address, String phone) {
            this.id = id;
            this.type = type;
            this.address = address;
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getId() {
            return id;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Address.class.getSimpleName() + "[", "]")
                    .add("id=" + id)
                    .add("type='" + type + "'")
                    .add("address='" + address + "'")
                    .add("phone='" + phone + "'")
                    .toString();
        }
    }

    public static class Child {

        @InterchangeableBackReference
        private Parent parent;

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        @Interchangeable(fieldTitle = "Grand Children", importable = true)
        private List<GrandChild> grandChildren = new ArrayList<>();

        @Interchangeable(fieldTitle = "Name", importable = true)
        private String name;

        private String description;

        public Child() {
        }

        public Child(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<GrandChild> getGrandChildren() {
            return grandChildren;
        }

        public void setGrandChildren(List<GrandChild> grandChildren) {
            this.grandChildren = grandChildren;
        }

        public void addGrandChild(GrandChild grandChild) {
            grandChildren.add(grandChild);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class GrandChild {

        @InterchangeableId
        @Interchangeable(fieldTitle = "Id")
        private Long id;

        @InterchangeableBackReference
        private Child child;

        @Interchangeable(fieldTitle = "Payload", importable = true)
        private String payload;

        private String internalPayload;

        public GrandChild() {
        }

        public GrandChild(Long id, String payload, String internalPayload) {
            this.id = id;
            this.payload = payload;
            this.internalPayload = internalPayload;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getInternalPayload() {
            return internalPayload;
        }

        public void setInternalPayload(String internalPayload) {
            this.internalPayload = internalPayload;
        }
    }

    public static class PersonAttribute {

        @PossibleValueId
        private Long id; // string ids not really supported

        private String type; // Hair, Height

        @PossibleValueValue
        private String value;

        public PersonAttribute() {
        }

        public PersonAttribute(Long id, String type, String value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "PersonAttribute{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    private ObjectImporter importer;

    /**
     * Examples might be modified during import. It is important to recreate them before each test.
     */
    private Map examples;

    @Before
    public void setUp() throws IOException {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();

        createObjectImporter();
        readJsonExamples();
    }

    private void createObjectImporter() {
        FeatureManagerService fmService = new TestFMService();
        FieldInfoProvider provider = new TestFieldInfoProvider();
        TranslatorService translatorService = new TestTranslatorService();

        FieldsEnumerator fe =
                new FieldsEnumerator(provider, fmService, translatorService, s -> true);
        APIField apiField = fe.getMetaModel(Parent.class);

        InputValidatorProcessor formatValidator = new InputValidatorProcessor(Collections.emptyList());

        InMemoryValueConverter valueConverter = new InMemoryValueConverter();

        TranslationSettings plainEnglish = new TranslationSettings("en", "en", Collections.singleton("en"), false);

        importer = new ObjectImporterAnyType(formatValidator, plainEnglish, apiField, valueConverter);
    }

    private void readJsonExamples() throws IOException {
        ObjectMapper om = new ObjectMapper();
        InputStream stream = ObjectImporterTest.class.getResourceAsStream("examples.json");
        examples = om.readValue(stream, Map.class);
    }

    @Test
    public void testBackReferences() {
        Map<String, Object> json = (Map<String, Object>) examples.get("back-references-example");

        Parent parent = new Parent();
        importer.validateAndImport(parent, json);

        Child child1 = parent.children.get(0);
        Child child2 = parent.children.get(1);
        assertEquals(child1.parent, parent);
        assertEquals(child1.parent, child2.parent);

        GrandChild grandChild1 = child1.grandChildren.get(0);
        assertEquals(grandChild1.child, child1);

        GrandChild grandChild2 = child2.grandChildren.get(0);
        GrandChild grandChild3 = child2.grandChildren.get(1);
        assertEquals(grandChild2.child, grandChild3.child);
        assertEquals(grandChild2.child, child2);
    }

    @Test
    public void testNoOverwrite() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite");

        Parent parent = new Parent("Leonidas", 45);

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parent("Wise Leonidas", 45));
    }

    @Test
    public void testNoOverwriteInCollection() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite-in-collection");

        Parent parent = new Parent("Leonidas", 45);
        parent.addChild(new Child(1L, "Alexios", "Defender"));
        parent.addChild(new Child(2L, "Herodotus", "Historian"));
        parent.addChild(new Child(null, "Persian", null));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithChildren("Wise Leonidas", 45,
                containsInAnyOrder(
                        child(1L, "Young Alexios", "Defender"),
                        child(null, "Pericles", null),
                        child(2L, "Prominent Herodotus", "Historian"))));
    }

    @Test
    public void testNoOverwriteCollection() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite");

        Parent parent = new Parent("Leonidas", 45);
        parent.addChild(new Child(1L, "Alexios", "Defender"));
        parent.addChild(new Child(2L, "Herodotus", "Historian"));
        parent.addChild(new Child(null, "Persian", null));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithChildren("Wise Leonidas", 45,
                containsInAnyOrder(
                        child(1L, "Alexios", "Defender"),
                        child(null, "Persian", null),
                        child(2L, "Herodotus", "Historian"))));
    }

    @Test
    public void testNoOverwriteInSet() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite-in-set");

        Parent parent = new Parent("Leonidas", 45);
        parent.addChild(new Child(1L, "Alexios", "Defender"));
        parent.addChild(new Child(2L, "Herodotus", "Historian"));
        parent.addChild(new Child(null, "Persian", null));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithChildren("Wise Leonidas", 45,
                containsInAnyOrder(
                        child(1L, "Young Alexios", "Defender"),
                        child(null, "Pericles", null),
                        child(2L, "Prominent Herodotus", "Historian"))));
    }

    @Test
    public void testNoOverwriteInCollectionTwoLevelsDeep() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite-in-collection-2-levels");

        Parent parent = new Parent("Leonidas", 45);

        Child alexios = new Child(1L, "Alexios", "Defender");
        alexios.addGrandChild(new GrandChild(1L, "one", "unu"));
        alexios.addGrandChild(new GrandChild(2L, "two", "doi"));
        alexios.addGrandChild(new GrandChild(3L, "Three", "trei"));
        parent.addChild(alexios);

        Child herodotus = new Child(2L, "Herodotus", "Historian");
        herodotus.addGrandChild(new GrandChild(5L, "Five", "cinci"));
        parent.addChild(herodotus);

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithChildren("Wise Leonidas", 45, containsInAnyOrder(
                child(1L, "Young Alexios", "Defender", containsInAnyOrder(
                        grandChild(1L, "One", "unu"),
                        grandChild(2L, "Two", "doi"),
                        grandChild(null, "Three", null))),
                child(null, "Pericles", null, contains(grandChild(null, "Four", null))),
                child(2L, "Prominent Herodotus", "Historian", containsInAnyOrder(
                        grandChild(null, "Five", null))))));
    }

    @Test(expected = IllegalStateException.class)
    public void testTwoDuplicateIds() {
        Map<String, Object> json = (Map<String, Object>) examples.get("back-references-example");

        Parent parent = new Parent();
        parent.addChild(new Child(1L, "A", "A"));
        parent.addChild(new Child(1L, "A", "A"));

        importer.validateAndImport(parent, json);
    }

    @Test
    public void testImportableFieldsAreNotClearedIfMissingInJson() {
        Map<String, Object> json = new HashMap<>();

        Parent parent = new Parent("Name", 13);

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parent("Name", 13));
    }

    @Test
    public void testHiddenDiscriminatedCollectionIsNotCleared() {
        Map<String, Object> json = (Map<String, Object>) examples.get("no-overwrite-in-discriminated-collection");

        Parent parent = new Parent("Leonidas", 45);
        parent.addAddress(new Address(1L, "W", "Washington DC - floor 1", "123"));
        parent.addAddress(new Address(null, "W", "Washington DC - floor 2", "124"));
        parent.addAddress(new Address(2L, "H", "Arlington VA (a)", "345"));
        parent.addAddress(new Address(null, "H", "Arlington VA (b)", "346"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAddresses("Leonidas", 45, containsInAnyOrder(
                address(1L, "W", "Washington DC - floor 1", "123"),
                address(null, "W", "Washington DC - floor 2", "124"),
                address(2L, "H", "home address", "home phone"),
                address(null, "H", "home address 2", "home phone 2"))));
    }

    @Test
    public void testLongHandling() {
        Map<String, Object> json = (Map<String, Object>) examples.get("ids-with-long-type");

        Parent parent = new Parent();
        parent.addChild(new Child(1L, "Alexios", "Defender"));
        parent.addChild(new Child(17179869184L, "Herodotus", "Historian"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithChildren("Leonidas", null, containsInAnyOrder(
                child(1L, "Young Alexios", "Defender"),
                child(17179869184L, "Prominent Herodotus", "Historian"))));
    }

    @Test
    public void testMatchingDiscriminatedButNotRepeatable() {
        Map<String, Object> json = (Map<String, Object>) examples.get("match-discriminated-but-not-repeatable");

        Parent parent = new Parent();
        parent.addPhone(new Phone("H", "123", "no soliciting"));
        parent.addPhone(new Phone("W", "678", "9-16 only"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithPhones(null, null, containsInAnyOrder(
                phone("H", "123-1", "no soliciting"),
                phone("W", "123-2", "9-16 only"))));
    }

    @Test
    public void testMatchingDiscriminatedButNotRepeatableIdOnly() {
        Map<String, Object> json = (Map<String, Object>) examples.get("match-discriminated-id-only");

        Parent parent = new Parent();
        parent.addAttribute(new PersonAttribute(1L, "Hair", "Blond"));
        parent.addAttribute(new PersonAttribute(2L, "Height", "Tall"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAttributes(null, null, containsInAnyOrder(
                attribute("Hair", 1L, "Blond"),
                attribute("Height", 10L, "Small"))));
    }

    /**
     * Object import must fail because there must just one home phone in the original object.
     */
    @Test(expected = RuntimeException.class)
    public void testDiscriminatedNotRepeatableInvalidOriginalObject() {
        Map<String, Object> json = (Map<String, Object>) examples.get("match-discriminated-but-not-repeatable");

        Parent parent = new Parent();
        parent.addPhone(new Phone("H", "123", null));
        parent.addPhone(new Phone("H", "124", null));

        importer.validateAndImport(parent, json);
    }

    @Test
    public void testRemoveElementInCollectionOnNullValue() {
        Map<String, Object> json = new HashMap<>();
        json.put("home", null);

        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAddresses(null, null, emptyIterable()));
    }

    @Test
    public void testRemoveElementInCollectionOnEmptyValue() {
        Map<String, Object> json = new HashMap<>();
        json.put("home", new ArrayList<>());

        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAddresses(null, null, emptyIterable()));
    }

    @Test
    public void testMissingFieldForDiscriminatedCollection() {
        Map<String, Object> json = new HashMap<>();

        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAddresses(null, null, contains(address(1L, "H", "Home", "123"))));
    }

    // no longer possible
    @Test
    public void testAgreementUpdate() {
        Parent parent = new Parent();
        Agreement origAgreement = new Agreement(1L, "x");
        parent.setAgreement(origAgreement);

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-update"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", allOf(
                agreement(null, "Desc 1"),
                not(sameInstance(origAgreement)))));
    }

    @Test
    public void testAgreementWasNull() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(1L, "x"));

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-null"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", nullValue()));
    }

    @Test
    public void testAgreementMissing() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(1L, "x"));

        importer.validateAndImport(parent, new HashMap<>());

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(1L, "x")));
    }

    @Test
    public void testDiscriminatorMissing() {
        Parent parent = new Parent();
        parent.addPhone(new Phone("H", "123", "no soliciting"));
        parent.addPhone(new Phone("W", "678", "9-16 only"));

        importer.validateAndImport(parent, new HashMap<>());

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithPhones(null, null, containsInAnyOrder(
                phone("H", "123", "no soliciting"),
                phone("W", "678", "9-16 only"))));
    }

    @Test
    public void testDiscriminatorIdOnly() {
        Parent parent = new Parent();
        parent.addAttribute(new PersonAttribute(1L, "Hair", "Blond"));
        parent.addAttribute(new PersonAttribute(2L, "Height", "Tall"));

        importer.validateAndImport(parent, new HashMap<>());

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAttributes(null, null, containsInAnyOrder(
                attribute("Hair", 1L, "Blond"),
                attribute("Height", 2L, "Tall"))));
    }

    @Test
    public void testAgreementEmpty() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(1L, "x"));

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-empty"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(null, null)));
    }

    @Test
    public void testAgreementInsert() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-insert"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(null, "Desc")));
    }

    @Test
    public void testAgreementOverwriteMissingId() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(1L, "x"));

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-insert"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(null, "Desc")));
    }

    @Test
    public void testAgreementOverwriteNullId() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(2L, "x"));

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-null-id"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(null, "Desc 1")));
    }

    @Test
    public void testAgreementIdMismatch() {
        Parent parent = new Parent();
        parent.setAgreement(new Agreement(2L, "x"));

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("agreement-update"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("agreement", agreement(null, "Desc 1")));
    }

    @Test
    public void testDetailUpdated() {
        Parent parent = new Parent();
        ParentDetails originalDetails = new ParentDetails(2L, "draft details");
        parent.setDetails(originalDetails);

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("details-full"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("details",
                allOf(
                        detail(2L, "Detail A"),
                        sameInstance(originalDetails))));
    }

    @Test
    public void testDetailInserted() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("details-full"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, hasProperty("details", detail(null, "Detail A")));
    }

    // primitive tests

    @Test
    public void testClearPrimitive() {
        Parent parent = new Parent();
        parent.setName("name");

        importer.validateAndImport(parent, Collections.singletonMap("name", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getName(), is(nullValue()));
    }

    @Test
    public void testReplacePrimitive() {
        Parent parent = new Parent();
        parent.setName("One");

        importer.validateAndImport(parent, Collections.singletonMap("name", "Two"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getName(), is("Two"));
    }

    @Test
    public void testPutPrimitive() {
        Parent parent = new Parent();
        parent.setName(null);

        importer.validateAndImport(parent, Collections.singletonMap("name", "X"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getName(), is("X"));
    }

    // non discriminated object tests
    @Test
    public void testInsertNonDiscriminatedObject() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of("details", ImmutableMap.of("detail", "X")));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getDetails(), detail(null, "X"));
    }

    @Test
    public void testClearNonDiscriminatedObject() {
        Parent parent = new Parent();
        parent.setDetails(new ParentDetails());

        importer.validateAndImport(parent, Collections.singletonMap("details", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getDetails(), is(nullValue()));
    }

    @Test
    public void testNotCreatedNonDiscriminatedObject() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, Collections.singletonMap("details", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getDetails(), is(nullValue()));
    }

    @Test
    public void testReplaceNonDiscriminatedObject() {
        Parent parent = new Parent();
        ParentDetails details = new ParentDetails(1L, "X");
        parent.setDetails(details);

        importer.validateAndImport(parent, ImmutableMap.of("details", ImmutableMap.of("detail", "Y")));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getDetails(), allOf(detail(1L, "Y"), sameInstance(details)));
    }

    @Test
    public void testReplace2NonDiscriminatedObject() {
        Parent parent = new Parent();
        ParentDetails details = new ParentDetails(1L, "X");
        parent.setDetails(details);

        importer.validateAndImport(parent,
                ImmutableMap.of("details",
                        ImmutableMap.of("id", 2L, "detail", "Y")));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getDetails(), allOf(detail(1L, "Y"), sameInstance(details)));
    }

    // discriminated object tests

    @Test
    public void testClearDiscriminatedSingleValueObject() {
        Parent parent = new Parent();
        parent.addPhone(new Phone("H", "123", null));

        importer.validateAndImport(parent, Collections.singletonMap("home_phone", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getPhones(), emptyIterable());
    }

    @Test
    public void testSetDiscriminatedSingleValueObject() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of("home_phone", ImmutableMap.of("number", "12345")));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getPhones(), contains(phone("H", "12345", null)));
    }

    @Test
    public void testReplaceDiscriminatedSingleValueObject() {
        Parent parent = new Parent();
        Phone phone = new Phone("H", "123", null);
        parent.addPhone(phone);

        importer.validateAndImport(parent, ImmutableMap.of("home_phone", ImmutableMap.of("number", "12345")));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getPhones(), contains(
                both(phone("H", "12345", null))
                        .and(sameInstance(phone))));
    }

    // non discriminated pick id only

    @Test
    public void testClearNonDiscriminatedPickIdOnly() {
        Parent parent = new Parent();
        parent.setColor(new PersonAttribute(5L, "Color", "Green"));

        importer.validateAndImport(parent, Collections.singletonMap("favorite_color", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getColor(), is(nullValue()));
    }

    @Test
    public void testPutNonDiscriminatedPickIdOnly() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, Collections.singletonMap("favorite_color", 1));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getColor(), attribute("Color", 1L, "Yellow"));
    }

    @Test
    public void testReplaceNonDiscriminatedPickIdOnly() {
        Parent parent = new Parent();
        parent.setColor(new PersonAttribute(1L, "Color", "Yellow"));

        importer.validateAndImport(parent, Collections.singletonMap("favorite_color", 2));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getColor(), attribute("Color", 2L, "Red"));
    }

    // discriminated pick id only

    @Test
    public void testClearDiscriminatedPickIdOnly() {
        Parent parent = new Parent();
        parent.addAttribute(new PersonAttribute(1L, "Hair", "Blond"));

        importer.validateAndImport(parent, Collections.singletonMap("hair_color", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAttributes(), emptyIterable());
    }

    @Test
    public void testPutDiscriminatedPickIdOnly() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of("height", 10));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAttributes(), contains(attribute("Height", 10L, "Small")));
    }

    @Test
    public void testReplaceDiscriminatedPickIdOnly() {
        Parent parent = new Parent();
        parent.addAttribute(new PersonAttribute(2L, "Height", "Tall"));

        importer.validateAndImport(parent, ImmutableMap.of("height", 10));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAttributes(), contains(attribute("Height", 10L, "Small")));
    }

    // list of primitives

    @Test
    public void testClearListOfPrimitivesWithNull() {
        Parent parent = new Parent();
        parent.addYear(2000);

        importer.validateAndImport(parent, Collections.singletonMap("years", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getYears(), emptyIterable());
    }

    @Test
    public void testClearListOfPrimitivesWithEmpty() {
        Parent parent = new Parent();
        parent.addYear(2000);

        importer.validateAndImport(parent, Collections.singletonMap("years", Collections.emptyList()));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getYears(), emptyIterable());
    }

    @Test
    public void testReplaceListOfPrimitives() {
        Parent parent = new Parent();
        parent.addYear(2000);

        importer.validateAndImport(parent, Collections.singletonMap("years", Collections.singletonList(2001)));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getYears(), contains(2001));
    }

    @Test
    public void testPutListOfPrimitives() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, Collections.singletonMap("years", ImmutableList.of(2000, 2001)));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getYears(), containsInAnyOrder(2000, 2001));
    }

    // list of non discriminated objects

    @Test
    public void testClearNonDiscriminatedListOfObjectsWithNull() {
        Parent parent = new Parent();
        parent.addChild(new Child(2L, "Herodotus", "Historian"));

        importer.validateAndImport(parent, Collections.singletonMap("children", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getChildren(), emptyIterable());
    }

    @Test
    public void testClearNonDiscriminatedListOfObjectsWithEmpty() {
        Parent parent = new Parent();
        parent.addChild(new Child(2L, "Herodotus", "Historian"));

        importer.validateAndImport(parent, Collections.singletonMap("children", Collections.emptyList()));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getChildren(), emptyIterable());
    }

    @Test
    public void testPutNonDiscriminatedListOfObjects() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of("children", ImmutableList.of(
                ImmutableMap.of("name", "Herodotus")
        )));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getChildren(), contains(child(null, "Herodotus", null)));
    }

    @Test
    public void testReplaceNonDiscriminatedListOfObjects() {
        Parent parent = new Parent();
        parent.addChild(new Child(2L, "Herodotus", "Historian"));

        importer.validateAndImport(parent, ImmutableMap.of("children", ImmutableList.of(
                ImmutableMap.of("name", "Archimedes")
        )));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getChildren(), contains(child(null, "Archimedes", null)));
    }

    // list of discriminated objects

    @Test
    public void testClearDiscriminatedListOfObjectsWithNull() {
        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, Collections.singletonMap("home", null));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAddresses(), emptyIterable());
    }

    @Test
    public void testClearDiscriminatedListOfObjectsWithEmpty() {
        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, Collections.singletonMap("home", Collections.emptyList()));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAddresses(), emptyIterable());
    }

    @Test
    public void testPutDiscriminatedListOfObjects() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of("home", ImmutableList.of(
                ImmutableMap.of("address", "X"))));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAddresses(), contains(address(null, "H", "X", null)));
    }

    @Test
    public void testReplaceDiscriminatedListOfObjects() {
        Parent parent = new Parent();
        parent.addAddress(new Address(1L, "H", "Home", "123"));

        importer.validateAndImport(parent, ImmutableMap.of("home", ImmutableList.of(
                ImmutableMap.of("address", "X"))));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getAddresses(), contains(address(null, "H", "X", null)));
    }

    @Test
    public void testReadOnlyFieldsAreNotImported() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, (Map<String, Object>) examples.get("read-only-fields"));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, allOf(
                hasProperty("readOnlyName", nullValue()), // primitive
                hasProperty("readOnlyColor", nullValue()), // pick id only
                hasProperty("readOnlyDetails", nullValue()), // object
                hasProperty("addresses", emptyIterable()), // list of discriminated objects
                hasProperty("phones", emptyIterable()), // discriminated object
                hasProperty("attributes", emptyIterable()), // discriminated pick id only
                hasProperty("readOnlyYears", emptyIterable()), // list of primitives
                hasProperty("readOnlyChildren", emptyIterable()) // list of objects
        ));
    }

    // contact tests for simple field accessor

    @Test
    public void testDiscriminationWithDifferentClasses() {
        Parent parent = new Parent();

        importer.validateAndImport(parent, ImmutableMap.of(
                "emails", ImmutableList.of(ImmutableMap.of("value", "admin@dg.org")),
                "web_sites", ImmutableList.of(ImmutableMap.of("value", "https://dg.org/"))));

        assertThat(importer.errors.size(), is(0));
        assertThat(parent.getContactAttributes(),
                containsInAnyOrder(email("admin@dg.org"), website("https://dg.org/")));
    }

    private Matcher email(String value) {
        return allOf(hasProperty("type", equalTo("E")),
                hasProperty("value", equalTo(value)),
                instanceOf(Email.class));
    }

    private Matcher website(String value) {
        return allOf(hasProperty("type", equalTo("W")),
                hasProperty("value", equalTo(value)),
                instanceOf(WebSite.class));
    }

    @Test
    public void testListOfPickIdOnly() {
        Map<String, Object> json = (Map<String, Object>) examples.get("list-of-pick-id-only");

        Parent parent = new Parent("Leonidas", 45);

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, parentWithAttributes("Leonidas", 45,
                containsInAnyOrder(
                        attribute("Character", 55L, "ch1"),
                        attribute("Character", 56L, "ch2"),
                        attribute("Character", 57L, "ch3")
                )));
    }

    @Test
    public void testPickIdOnlyForSimpleType() {
        Parent parent = new Parent();

        Map<String, Object> json = new HashMap<>();
        json.put("status", 1L);
        json.put("statuses", ImmutableList.of(3L, 4L));

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, allOf(
                hasProperty("status", is(1L)),
                hasProperty("statuses", containsInAnyOrder(3L, 4L))));
    }

    @Test
    public void testPickIdOnlyCleared() {
        Parent parent = new Parent();
        parent.setStatus(1L);
        parent.getStatuses().add(5L);
        parent.getStatuses().add(7L);

        Map<String, Object> json = new HashMap<>();
        json.put("status", null);
        json.put("statuses", null);

        importer.validateAndImport(parent, json);

        assertThat(importer.errors.size(), is(0));
        assertThat(parent, allOf(
                hasProperty("status", is(nullValue())),
                hasProperty("statuses", emptyIterable())));
    }

    private Matcher<Agreement> agreement(Long id, String desc) {
        return allOf(hasProperty("id", equalTo(id)),
                hasProperty("description", equalTo(desc)));
    }

    private Matcher<ParentDetails> detail(Long id, String detail) {
        return allOf(hasProperty("id", equalTo(id)),
                hasProperty("detail", equalTo(detail)));
    }

    private Matcher<Parent> parent(String name, Integer age) {
        return parent(name, age, emptyIterable(), emptyIterable(), emptyIterable());
    }

    private Matcher<Parent> parentWithAddresses(String name, Integer age,
                                                Matcher<Iterable<? extends Address>> addresses) {
        return parent(name, age, emptyIterable(), addresses, emptyIterable());
    }

    private Matcher<Parent> parentWithChildren(String name, Integer age,
                                               Matcher<Iterable<? extends Child>> children) {
        return parent(name, age, children, emptyIterable(), emptyIterable());
    }

    private Matcher<Parent> parentWithPhones(String name, Integer age,
                                             Matcher<Iterable<? extends Phone>> phones) {
        return parent(name, age, emptyIterable(), emptyIterable(), phones);
    }

    private Matcher<Parent> parentWithAttributes(String name, Integer age,
                                                 Matcher<Iterable<? extends PersonAttribute>> attributes) {
        return parent(name, age, emptyIterable(), emptyIterable(), emptyIterable(), attributes);
    }

    private Matcher<Parent> parent(String name, Integer age,
                                   Matcher<Iterable<? extends Child>> children,
                                   Matcher<Iterable<? extends Address>> addresses,
                                   Matcher<Iterable<? extends Phone>> phones) {
        return allOf(
                hasProperty("name", is(name)),
                hasProperty("age", is(age)),
                hasProperty("gender", is("X")),
                hasProperty("children", children),
                hasProperty("addresses", addresses),
                hasProperty("phones", phones));
    }

    private Matcher<Parent> parent(String name, Integer age,
                                   Matcher<Iterable<? extends Child>> children,
                                   Matcher<Iterable<? extends Address>> addresses,
                                   Matcher<Iterable<? extends Phone>> phones,
                                   Matcher<Iterable<? extends PersonAttribute>> attributes) {
        return allOf(
                hasProperty("name", is(name)),
                hasProperty("age", is(age)),
                hasProperty("gender", is("X")),
                hasProperty("children", children),
                hasProperty("addresses", addresses),
                hasProperty("phones", phones),
                hasProperty("attributes", attributes));
    }

    private Matcher<Child> child(Long id, String name, String desc) {
        return child(id, name, desc, emptyIterable());
    }

    private Matcher<Child> child(Long id, String name, String desc,
                                 Matcher<Iterable<? extends GrandChild>> grandChildren) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("name", is(name)),
                hasProperty("description", is(desc)),
                hasProperty("grandChildren", grandChildren));
    }

    private Matcher<GrandChild> grandChild(Long id, String payload, String internalPayload) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("payload", is(payload)),
                hasProperty("internalPayload", is(internalPayload)));
    }

    private Matcher<Address> address(Long id, String type, String address, String phone) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("type", is(type)),
                hasProperty("address", is(address)),
                hasProperty("phone", is(phone)));
    }

    private Matcher<Phone> phone(String type, String number, String extraInfo) {
        return allOf(
                hasProperty("number", is(number)),
                hasProperty("type", is(type)),
                hasProperty("extraInfo", is(extraInfo)));
    }

    private Matcher<PersonAttribute> attribute(String type, Long id, String value) {
        return allOf(
                hasProperty("id", is(id)),
                hasProperty("type", is(type)),
                hasProperty("value", is(value)));
    }

}
