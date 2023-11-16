package org.digijava.kernel.ampapi.endpoints.gpi;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.digijava.module.aim.dbentity.EntityResolver;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.function.Function;

/**
 * A JUnit rule to provide context for serializing/deserializing with jackson and without having access to a
 * real database.
 *
 * @author Octavian Ciubotaru
 */
public class JacksonInTestRule implements TestRule {

    private Function<ObjectIdGenerator.IdKey, Object> resolver;

    public JacksonInTestRule(
            Function<ObjectIdGenerator.IdKey, Object> resolver) {
        this.resolver = resolver;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    EntityResolver.doWithResolver(resolver, () -> {
                        try {
                            ApplyThousandsForVisibilityConverter.setInTest(true);
                            ApplyThousandsForEntryConverter.setInTest(true);
                            base.evaluate();
                        } catch (Throwable e) {
                            throw new WrappedThrowable(e);
                        } finally {
                            ApplyThousandsForVisibilityConverter.setInTest(false);
                            ApplyThousandsForEntryConverter.setInTest(false);
                        }
                    });
                } catch (WrappedThrowable e) {
                    throw e.getCause(); // unwrapping since the cause might as well be an Error
                }
            }
        };
    }

    /**
     * This exception is intentionally local to this class as to avoid catching wrong "wrapped exception".
     */
    private static class WrappedThrowable extends RuntimeException {

        WrappedThrowable(Throwable throwable) {
            super(throwable);
        }
    }
}
