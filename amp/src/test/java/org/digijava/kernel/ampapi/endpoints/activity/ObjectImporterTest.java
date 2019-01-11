package org.digijava.kernel.ampapi.endpoints.activity;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.activity.validators.InputValidatorProcessor;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectImporterTest {

    static class Parent {

        @Interchangeable(fieldTitle = "Children", importable = true)
        private List<Child> children;
    }

    static class Child {

        @InterchangeableBackReference
        private Parent parent;

        @Interchangeable(fieldTitle = "Grand Children", importable = true)
        private List<GrandChild> grandChildren;
    }

    static class GrandChild {

        @InterchangeableBackReference
        private Child child;

        @Interchangeable(fieldTitle = "Payload", importable = true)
        private String payload;
    }

    @Test
    public void testBackReferences() throws IOException {
        FMService fmService = new TestFMService();
        FieldInfoProvider provider = new TestFieldInfoProvider();
        TranslatorService translatorService = new TestTranslatorService();

        FieldsEnumerator fe =
                new FieldsEnumerator(provider, fmService, translatorService, s -> true);
        List<APIField> apiFields = fe.getAllAvailableFields(Parent.class);

        InputValidatorProcessor validator = new InputValidatorProcessor(Collections.emptyList());

        TranslationSettings plainEnglish = new TranslationSettings("en", Collections.singleton("en"), false);

        ObjectImporter importer = new ObjectImporter(validator, plainEnglish, apiFields);

        ObjectMapper om = new ObjectMapper();
        InputStream stream = ObjectImporterTest.class.getResourceAsStream("examples.json");
        Map examples = om.readValue(stream, Map.class);

        Map<String, Object> json = (Map<String, Object>) examples.get("back-references-example");

        Parent parent = (Parent) importer.validateAndImport(new Parent(), json);

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
}
