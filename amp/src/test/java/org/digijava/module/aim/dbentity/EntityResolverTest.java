package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.digijava.kernel.ampapi.endpoints.gpi.JacksonInTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Octavian Ciubotaru
 */
public class EntityResolverTest {

    @Rule
    public JacksonInTestRule jacksonInTestRule = new JacksonInTestRule(this::resolve);

    private static class X {

        private Y one;
        private Y two;
        private Y three;

        public Y getOne() {
            return one;
        }

        public void setOne(Y one) {
            this.one = one;
        }

        public Y getTwo() {
            return two;
        }

        public void setTwo(Y two) {
            this.two = two;
        }

        public Y getThree() {
            return three;
        }

        public void setThree(Y three) {
            this.three = three;
        }
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id",
            resolver = EntityResolver.class,
            scope = Y.class)
    @JsonIdentityReference(alwaysAsId = true)
    private static class Y {

        private Long id;
        private String name;

        public Y(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testNullFields() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":null,\"two\":null,\"three\":null}", X.class);
        assertNull(x.getOne());
        assertNull(x.getTwo());
        assertNull(x.getThree());
    }

    @Test
    public void testMissingFields() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{}", X.class);
        assertNull(x.getOne());
        assertNull(x.getTwo());
        assertNull(x.getThree());
    }

    @Test
    public void testOneObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":1}", X.class);
        assertYPresence(1L, "one", x.getOne());
    }

    @Test
    public void testThreeObjects() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":3,\"two\":2,\"three\":1}", X.class);
        assertYPresence(3L, "three", x.getOne());
        assertYPresence(2L, "two", x.getTwo());
        assertYPresence(1L, "one", x.getThree());
    }

    private void assertYPresence(Long expectedId, String expectedName, Y actualY) {
        assertNotNull(actualY);
        assertEquals(expectedId, actualY.getId());
        assertEquals(expectedName, actualY.getName());
    }

    @Test
    public void testCaching() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":1,\"two\":1,\"three\":1}", X.class);
        assertNotNull(x.getOne());
        assertTrue(x.getOne() == x.getTwo());
        assertTrue(x.getOne() == x.getThree());
    }

    @Test
    public void testThatCachingWorksAndIsLocal() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        X x1 = objectMapper.readValue("{\"one\":1,\"two\":1}", X.class);
        assertNotNull(x1.getOne());
        assertTrue(x1.getOne() == x1.getTwo());

        X x2 = objectMapper.readValue("{\"one\":1,\"two\":1}", X.class);
        assertNotNull(x2.getOne());
        assertTrue(x2.getOne() == x2.getTwo());

        assertTrue(x1.getOne() != x2.getOne());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testUnknownId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue("{\"one\":999}", X.class);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testInvalidIdType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue("{\"one\":false}", X.class);
    }

    private Object resolve(ObjectIdGenerator.IdKey idKey) {
        if (idKey.scope.equals(Y.class)) {
            if (idKey.key.equals(1L)) {
                return new Y(1L, "one");
            }
            if (idKey.key.equals(2L)) {
                return new Y(2L, "two");
            }
            if (idKey.key.equals(3L)) {
                return new Y(3L, "three");
            }
        }
        return null;
    }
}
