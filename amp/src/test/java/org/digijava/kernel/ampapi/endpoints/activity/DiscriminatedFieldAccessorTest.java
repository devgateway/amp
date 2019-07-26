package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import org.hamcrest.Matcher;
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

    @Test
    public void testDiscriminatedRead() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("A", "3"));
        obj.categories.add(new Category("B", "4"));

        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor("categories"), "kind", "A");
        Collection<Category> o = (Collection) accessor.get(obj);

        assertThat(o, containsInAnyOrder(cat("A", "1"), cat("A", "3")));
    }
    
    @Test
    public void testDiscriminatedReadSet() {
        Obj obj = new Obj();
        obj.attributes.add(new Category("A", "1"));
        obj.attributes.add(new Category("B", "2"));
        obj.attributes.add(new Category("A", "3"));
        obj.attributes.add(new Category("B", "4"));
        
        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor("attributes"), "kind", "A");
        Collection<Category> o = (Collection) accessor.get(obj);
        
        assertThat(o, containsInAnyOrder(cat("A", "1"), cat("A", "3")));
    }

    @Test
    public void testDiscriminatedWrite() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("C", "3"));
        obj.categories.add(new Category("B", "4"));

        FieldAccessor accessor = new DiscriminatedFieldAccessor(new SimpleFieldAccessor("categories"), "kind", "B");
        List<Category> newCats = ImmutableList.of(new Category("B", "5"), new Category("B", "6"));
        accessor.set(obj, newCats);

        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "1"), cat("C", "3"), cat("B", "5"), cat("B", "6")));
    }
    
    @Test
    public void testDiscriminatedUpdate() {
        Obj obj = new Obj();
        obj.categories.add(new Category("A", "1"));
        obj.categories.add(new Category("B", "2"));
        obj.categories.add(new Category("C", "3"));
        obj.categories.add(new Category("B", "4"));
    
        List<Category> newCatsA = ImmutableList.of(new Category("A", "7"));
        FieldAccessor accessorA = new DiscriminatedFieldAccessor(new SimpleFieldAccessor("categories"), "kind", "A");
        accessorA.set(obj, newCatsA);
        
        List<Category> newCatsB = ImmutableList.of(new Category("B", "5"), new Category("B", "6"));
        FieldAccessor accessorB = new DiscriminatedFieldAccessor(new SimpleFieldAccessor("categories"), "kind", "B");
        accessorB.set(obj, newCatsB);
        
        
        assertThat(obj.categories,
                containsInAnyOrder(cat("A", "7"), cat("C", "3"), cat("B", "5"), cat("B", "6")));
    }

    private Matcher<Category> cat(String kind, String value) {
        return allOf(hasProperty("kind", is(kind)), hasProperty("value", is(value)));
    }
}
