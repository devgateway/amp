package org.digijava.kernel;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.testutils.InTransactionRule;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract class for tests that require standalone AMP with spring application context.
 *
 * @author Octavian Ciubotaru
 */
@ContextConfiguration("/applicationContext.xml")
@Tag("databasetests")
@ExtendWith(InTransactionRule.class)
public abstract class AbstractIntegrationTest {


//    public InTransactionRule inTransactionRule = new InTransactionRule();

    @BeforeAll
    public static void staticSetUp() {
        StandaloneAMPInitializer.initialize();
    }
}
