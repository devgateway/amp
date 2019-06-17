package org.digijava.kernel.validators.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RegexValidatorTest.class,
        SizeValidatorTest.class,
        RequiredValidatorTest.class
})
public class CommonValidatorsTestSuite {
}
