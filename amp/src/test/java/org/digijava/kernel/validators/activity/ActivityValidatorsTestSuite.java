package org.digijava.kernel.validators.activity;

import org.junit.platform.suite.api.SelectClasses;

/**
 * @author Octavian Ciubotaru
 */
@org.junit.platform.suite.api.Suite
@SelectClasses({
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
