package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;

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
        attributes = Obj.class.getDeclaredField("attributes");
        categories = Obj.class.getDeclaredField("categories");
    }

    @Test
    public void testMultipleValuesRead() {
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

    @Test(expected = RuntimeException.class)
    public void testSingleValueReadInvalid() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("A", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        accessor.get(obj);
    }

    @Test(expected = RuntimeException.class)
    public void testSingleValueWriteInvalid() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("A", "2"));

        FieldAccessor accessor = newSingleValueCategoryAccessor("A");
        accessor.set(obj, null);
    }
    
    @Test
    public void testMultipleValuesReadFromSetDataType() {
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
    public void testMultipleValuesWrite() {
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
    public void testMultipleValuesUpdate() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("C", "3"));
        obj.categories.add(new Category("B", "4"));
    
        List<Category> newCatsA = ImmutableList.of(new Category("A", "7"));
        FieldAccessor accessorA = newMultipleValuesCategoryAccessor("A");
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
}
