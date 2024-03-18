package org.digijava.kernel.ampapi.endpoints.activity;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.request.TLSUtils;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.*;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils.WORKSPACE_PREFIX;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Octavian Ciubotaru
 */
public class DiscriminatedFieldAccessorTest {


    private static class Obj {

        private List<Category> categories = new ArrayList<>();

        private Set<Category> attributes = new HashSet<>();
    }

    public static class Category {

        private String kind;
        private String value;

        public Category(String kind, String value) {
            this.kind = kind;
            this.value = value;
        }

        public String getKind() {
            return kind;
        }

        public String getValue() {
            return value;
        }
    }

    private static Field attributes;
    private static Field categories;

    @BeforeClass
    public static void setUp() throws Exception {
        TransactionUtil.setUpWorkspaceEmptyPrefixes();
        TLSUtils.getRequest().setAttribute(WORKSPACE_PREFIX, "");

        attributes = Obj.class.getDeclaredField("attributes");
        categories = Obj.class.getDeclaredField("categories");
    }

    @Test
    public void testCollectionReadFromNull() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories = null;

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories), "kind",
                    "A", true);
            accessor.get(obj);
        });
    }

    @Test
    public void testCollectionWriteWriteToNull() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories = null;

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories), "kind",
                    "A", true);
            accessor.set(obj, ImmutableList.of(new Category("A", "1")));
        });
    }

    @Test
    public void testDiscriminatedRead() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("A", "3"));
        obj.categories.add(new Category("B", "4"));

        FieldAccessor accessor = newMultipleValuesCategoryAccessor("A");
        Collection<Category> o = accessor.get(obj);

        assertThat(o, containsInAnyOrder(cat("A", "1"), cat("A", "3")));
    }

    @Test
    public void testSingleValueRead() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        Category o = accessor.get(obj);

        assertThat(o, cat("A", "1"));
    }

    @Test
    public void testSingleValueReadInvalid() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories.add(new Category("A", "1"));
            obj.categories.add(new Category("A", "2"));

            FieldAccessor accessor = newSingleValueCategoryAccessor("A");
            accessor.get(obj);
        });
    }

    @Test
    public void testSingleValueWriteInvalid() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories.add(new Category("A", "1"));
            obj.categories.add(new Category("A", "2"));

            FieldAccessor accessor = newSingleValueCategoryAccessor("A");
            accessor.set(obj, null);
        });
    }

    @Test
    public void testDiscriminatedReadSet() {
        Obj obj = new Obj();
        obj.attributes.add(new Category("A", "1"));
        obj.attributes.add(new Category("B", "2"));
        obj.attributes.add(new Category("A", "3"));
        obj.attributes.add(new Category("B", "4"));

        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(attributes), "kind", "A", true);
        Collection<Category> o = accessor.get(obj);

        assertThat(o, containsInAnyOrder(cat("A", "1"), cat("A", "3")));
    }

    @Test
    public void testDiscriminatedWrite() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("C", "3"));
        obj.categories.add(new Category("B", "4"));

        FieldAccessor accessor = newMultipleValuesCategoryAccessor("B");
        List<Category> newCats = ImmutableList.of(new Category("B", "5"), new Category("B", "6"));
        accessor.set(obj, newCats);

        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "1"), cat("C", "3"), cat("B", "5"), cat("B", "6")));
    }

    @Test
    public void testSingleValueInsert() {
        Obj obj = new Obj();
        obj.categories.add(new Category("B", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        accessor.set(obj, new Category("A", "1"));

        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "1"), cat("B", "2")));
    }

    @Test
    public void testDiscriminatedUpdate() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("C", "3"));
        obj.categories.add(new Category("B", "4"));

        List<Category> newCatsA = ImmutableList.of(new Category("A", "7"));
        FieldAccessor accessorA = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories), "kind",
                "A",true);
        accessorA.set(obj, newCatsA);

        List<Category> newCatsB = ImmutableList.of(new Category("B", "5"), new Category("B", "6"));
        FieldAccessor accessorB = newMultipleValuesCategoryAccessor("B");
        accessorB.set(obj, newCatsB);


        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "7"), cat("C", "3"), cat("B", "5"), cat("B", "6")));
    }

    @Test
    public void testSingleValueUpdate() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "5"));
        obj.categories.add(new Category("B", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        accessor.set(obj, new Category("A", "1"));

        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "1"), cat("B", "2")));
    }

    @Test
    public void testSingleValueDelete() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "5"));
        obj.categories.add(new Category("B", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        accessor.set(obj, null);

        assertThat(obj.categories, contains(cat("B", "2")));
    }

    private FieldAccessor newSingleValueCategoryAccessor(String discriminatorValue) {
        return newAccessor(categories, discriminatorValue, false);
    }

    private FieldAccessor newMultipleValuesCategoryAccessor(String discriminatorValue) {
        return newAccessor(categories, discriminatorValue, true);
    }

    private FieldAccessor newAccessor(Field field, String discriminatorValue, boolean multipleValues) {
        return new DiscriminatedFieldAccessor(
                new SimpleFieldAccessor(field), "kind", discriminatorValue, multipleValues);
    }

    private Matcher<Category> cat(String kind, String value) {
        return allOf(hasProperty("kind", is(kind)), hasProperty("value", is(value)));
    }

    @Test
    public void testObjectReadFromNull() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories = null;

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                    "kind", "A", false);
            accessor.get(obj);
        });
    }

    @Test
    public void testObjectWriteToNull() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories = null;

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                    "kind", "A", false);
            accessor.set(obj, new Category("A", "1"));
        });
    }

    @Test
    public void testObjectReadUncertain() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories.add(new Category("A", "1"));
            obj.categories.add(new Category("A", "3"));

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                    "kind", "A", false);
            accessor.get(obj);
        });
    }

    @Test
    public void testObjectWriteUncertain() {
        assertThrows(RuntimeException.class,()-> {
            Obj obj = new Obj();
            obj.categories.add(new Category("A", "1"));
            obj.categories.add(new Category("A", "3"));

            FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                    "kind", "A", false);
            accessor.set(obj, new Category("A", "1"));
        });
    }

    @Test
    public void testObjectRead() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "3"));

        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                "kind", "B", false);

        assertThat(accessor.get(obj), is(cat("B", "3")));
    }

    @Test
    public void testObjectWrite() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "3"));

        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor(categories),
                "kind", "B", false);

        accessor.set(obj, new Category("B", "4"));

        assertThat(obj.categories, containsInAnyOrder(cat("A", "1"), cat("B", "4")));
    }
}
