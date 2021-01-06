package org.digijava.kernel.xmlpatches;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.activity.ActivityCloser;

import java.util.List;

public abstract class AbstractAmpActivityCloserPatch {
    private static final  Logger LOGGER = Logger.getLogger(AbstractAmpActivityCloserPatch.class);

    public void closeActivities(Integer index) {
        this.doCloseActivities(index);
    }

    public abstract List<String> getAmpIds(Integer index);

    private void doCloseActivities(Integer index) {
        List<AmpActivityVersion> activitiesToClose = ActivityUtil.getLastActivitiesVersionByAmpIds(this.getAmpIds(index));
        try {
            Long closedCategoryValue =
                    FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);
            (new ActivityCloser()).closeActivities(activitiesToClose, closedCategoryValue, SaveContext.patch());
        } catch (Exception e) {
            LOGGER.error("Could not close activities ", e);
            throw new RuntimeException(e);
        }
    }
}
