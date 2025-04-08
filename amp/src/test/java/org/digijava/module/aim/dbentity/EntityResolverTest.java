package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Octavian Ciubotaru
 */
//@ExtendWith(JacksonInTestRule.class)
public class EntityResolverTest {
    private ObjectMapper objectMapper;


    private static class X {
        @JsonDeserialize(using = MyCustomYDeserializer.class)
        private Y one;
        @JsonDeserialize(using = MyCustomYDeserializer.class)
        private Y two;
        @JsonDeserialize(using = MyCustomYDeserializer.class)
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

    public static class MyCustomYDeserializer extends JsonDeserializer<Y> {
        public MyCustomYDeserializer(){
        }

        @Override
        public Y deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

            Long id = p.getLongValue();
            Y y = new Y();
            y.setId(id);
            y.setName(p.getCurrentName());
            System.out.println("Setting "+ y.name );
            return y;
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
        public Y(){
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Y)) return false;
            Y y = (Y) o;
            return Objects.equals(getId(), y.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getId());
        }
    }

    @Test
    public void testNullFields() throws IOException {
         objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":null,\"two\":null,\"three\":null}", X.class);
        assertNull(x.getOne());
        assertNull(x.getTwo());
        assertNull(x.getThree());
    }

    @Test
    public void testMissingFields() throws IOException {
         objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{}", X.class);
        assertNull(x.getOne());
        assertNull(x.getTwo());
        assertNull(x.getThree());
    }

    @Test
    public void testOneObject() throws IOException {
         objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":1}", X.class);
        System.out.println(x.getOne().id);
        assertYPresence(1L, "one", x.getOne());
    }

    @Test
    public void testThreeObjects() throws IOException {
         objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":3,\"two\":2,\"three\":1}", X.class);
        assertYPresence(3L, "one", x.getOne());
        assertYPresence(2L, "two", x.getTwo());
        assertYPresence(1L, "three", x.getThree());
    }

    private void assertYPresence(Long expectedId, String expectedName, Y actualY) {
        assertNotNull(actualY);
        assertEquals(expectedId, actualY.getId());
        assertEquals(expectedName, actualY.getName());
    }

    @Test
    public void testCaching() throws IOException {
         objectMapper = new ObjectMapper();
        X x = objectMapper.readValue("{\"one\":1,\"two\":1,\"three\":1}", X.class);
        assertNotNull(x.getOne());
        assertEquals(x.getOne(), x.getTwo());
        assertEquals(x.getOne(), x.getThree());
    }

    @Test
    public void testThatCachingWorksAndIsLocal() throws IOException {
         objectMapper = new ObjectMapper();

        X x1 = objectMapper.readValue("{\"one\":1,\"two\":1}", X.class);
        assertNotNull(x1.getOne());
        assertEquals(x1.getOne(), x1.getTwo());

        X x2 = objectMapper.readValue("{\"one\":1,\"two\":1}", X.class);
        assertNotNull(x2.getOne());
        assertEquals(x2.getOne(), x2.getTwo());

        assertEquals(x1.getOne(), x2.getOne());
    }


    @Test
    public void testInvalidIdType() throws IOException {
        assertThrows(JsonMappingException.class,()-> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue("{\"one\":false}", X.class);
        });
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
