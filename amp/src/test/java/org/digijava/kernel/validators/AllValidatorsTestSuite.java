package org.digijava.kernel.validators;

import org.digijava.kernel.validators.activity.ActivityValidatorsTestSuite;
import org.digijava.kernel.validators.resource.ResourceRequiredValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ActivityValidatorsTestSuite.class,
        ResourceRequiredValidatorTest.class
})
public class AllValidatorsTestSuite {
}
