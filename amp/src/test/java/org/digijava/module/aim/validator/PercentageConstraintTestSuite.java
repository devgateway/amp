package org.digijava.module.aim.validator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationTotalPercentageValidatorTest.class,
        OrgRoleTotalPercentageValidatorTest.class,
        SectorsTotalPercentageValidatorTest.class,
        ProgramTotalPercentageValidatorTest.class
})
public class PercentageConstraintTestSuite {
}
