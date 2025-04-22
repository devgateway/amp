package org.digijava.kernel.ampapi.endpoints.gpi;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.digijava.module.aim.dbentity.EntityResolver;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.function.Function;

/**
 * A JUnit 5 extension to provide context for serializing/deserializing with Jackson
 * and without having access to a real database.
 */
public class JacksonInTestRule implements BeforeEachCallback, AfterEachCallback {

    private final Function<ObjectIdGenerator.IdKey, Object> resolver;

    public JacksonInTestRule(Function<ObjectIdGenerator.IdKey, Object> resolver) {
        this.resolver = resolver;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        EntityResolver.doWithResolver(resolver, () -> {
            ApplyThousandsForVisibilityConverter.setInTest(true);
            ApplyThousandsForEntryConverter.setInTest(true);
        });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ApplyThousandsForVisibilityConverter.setInTest(false);
        ApplyThousandsForEntryConverter.setInTest(false);
    }
}
