package org.digijava.kernel.validators.common;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Octavian Ciubotaru
 */
@org.junit.platform.suite.api.Suite
@SelectClasses({
        RegexValidatorTest.class,
        SizeValidatorTest.class,
        TotalPercentageValidatorTest.class,
        RequiredValidatorTest.class
})
public class CommonValidatorsTestSuite {
}
