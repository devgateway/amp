package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.legacy.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * entry point for AMP 2.6 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DirectedDisbursementsTests.class,
        MtefTests.class,
        ActivityPreviewTests.class,
        MiscColumnsTests.class,
        FiltersTests.class,
        MiscReportsTests.class,
        OldReportsNewFeaturesTests.class,
        HierarchyTests27.class,
        DirectedDisbursementsTests_amp27.class,
        MultilingualTests.class,
        MultilingualThroughTrnTests.class,
        CategoryManagerTests.class,
        MultilingualTests28.class,
        PledgesFormTests.class,
        MiscTests28.class,
        ProgramsTests.class,
})
public class LegacyReportsTestSuite {
}
