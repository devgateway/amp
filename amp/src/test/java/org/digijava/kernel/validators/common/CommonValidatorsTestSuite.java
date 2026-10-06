package org.digijava.kernel.validators.common;

import org.junit.platform.suite.api.SelectClasses;


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
