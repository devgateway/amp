package org.digijava.kernel.validators;

import org.digijava.kernel.validators.activity.ActivityValidatorsTestSuite;
import org.digijava.kernel.validators.common.CommonValidatorsTestSuite;
import org.digijava.kernel.validators.resource.ResourceRequiredValidatorTest;
import org.junit.platform.suite.api.SelectClasses;

/**
 * @author Octavian Ciubotaru
 */
@org.junit.platform.suite.api.Suite
@SelectClasses({
        ActivityValidatorsTestSuite.class,
        ResourceRequiredValidatorTest.class,
        CommonValidatorsTestSuite.class
})
public class AllValidatorsTestSuite {
}
