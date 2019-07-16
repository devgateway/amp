package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.legacy.ActivityPreviewTests;
import org.dgfoundation.amp.ar.legacy.CategoryManagerTests;
import org.dgfoundation.amp.ar.legacy.DirectedDisbursementsTests;
import org.dgfoundation.amp.ar.legacy.DirectedDisbursementsTests_amp27;
import org.dgfoundation.amp.ar.legacy.FiltersTests;
import org.dgfoundation.amp.ar.legacy.HierarchyTests27;
import org.dgfoundation.amp.ar.legacy.MiscColumnsTests;
import org.dgfoundation.amp.ar.legacy.MiscReportsTests;
import org.dgfoundation.amp.ar.legacy.MiscTests28;
import org.dgfoundation.amp.ar.legacy.MtefTests;
import org.dgfoundation.amp.ar.legacy.MultilingualTests;
import org.dgfoundation.amp.ar.legacy.MultilingualTests28;
import org.dgfoundation.amp.ar.legacy.MultilingualThroughTrnTests;
import org.dgfoundation.amp.ar.legacy.OldReportsNewFeaturesTests;
import org.dgfoundation.amp.ar.legacy.PledgesFormTests;
import org.dgfoundation.amp.ar.legacy.ProgramsTests;

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
