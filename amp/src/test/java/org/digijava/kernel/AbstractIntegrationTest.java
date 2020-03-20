package org.digijava.kernel;

import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.testutils.InTransactionRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Abstract class for tests that require standalone AMP with spring application context.
 *
 * @author Octavian Ciubotaru
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Category(DatabaseTests.class)
public abstract class AbstractIntegrationTest {

    @Rule
    public InTransactionRule inTransactionRule = new InTransactionRule();

    @BeforeClass
    public static void staticSetUp() {
        StandaloneAMPInitializer.initialize();
    }
}
