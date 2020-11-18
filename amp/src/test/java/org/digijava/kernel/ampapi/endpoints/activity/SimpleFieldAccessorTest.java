package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class SimpleFieldAccessorTest {

    private static Field set;

    static class TestObject {
        private Set<Long> set;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        set = TestObject.class.getDeclaredField("set");
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullSet() {
        TestObject obj = new TestObject();

        SimpleFieldAccessor acc = new SimpleFieldAccessor(set);
        acc.get(obj);
    }

    @Test
    public void testReadEmptySet() {
        TestObject obj = new TestObject();
        TreeSet<Long> originalSet = new TreeSet<>();
        obj.set = originalSet;

        SimpleFieldAccessor acc = new SimpleFieldAccessor(set);
        Collection<?> value = (Collection) acc.get(obj);

        assertThat(value, both(emptyIterable()).and(not(sameInstance(originalSet))));
    }

    @Test
    public void testReadSingletonSet() {
        TestObject obj = new TestObject();
        TreeSet<Long> originalSet = new TreeSet<>();
        originalSet.add(1L);
        obj.set = originalSet;

        SimpleFieldAccessor acc = new SimpleFieldAccessor(set);
        Collection<Long> value = (Collection) acc.get(obj);

        assertThat(value, both(contains(1L)).and(not(sameInstance(originalSet))));
    }
}
