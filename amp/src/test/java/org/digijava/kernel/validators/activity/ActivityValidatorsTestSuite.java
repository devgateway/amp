package org.digijava.kernel.validators.activity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Octavian Ciubotaru
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ImplementationLevelValidatorTest.class,
        OnBudgetValidatorTest.class,
        FundingWithTransactionsValidatorTest.class,
        ComponentFundingOrgRoleValidatorTest.class,
        PledgeOrgValidatorTest.class,
        UniqueActivityTitleValidatorTest.class,
        UniqueValidatorTest.class,
        TreeCollectionValidatorTest.class,
        PrivateResourceValidatorTest.class,
        AgreementCodeValidatorTest.class
})
public class ActivityValidatorsTestSuite {
}
