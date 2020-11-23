package org.digijava.module.aim.util.activity;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.List;

public class ActivityCloser {
    public static final String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    public static final String AMP_MODIFIER_FIRST_NAME = "AMP";
    public static final String AMP_MODIFIER_LAST_NAME = "Activities Modifier";

    private static final Logger LOGGER = Logger.getLogger(ActivityCloser.class);
    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    public void closeActivities(List<AmpActivityVersion> closeableActivities, Long closedCategoryValue,
                                SaveContext saveContext) throws Exception {
        ApprovalStatus newStatus = ApprovalStatus.APPROVED;
        LOGGER.error(closeableActivities.size());
        for (AmpActivityVersion ver : closeableActivities) {

            if ("On".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION))) {
                newStatus = ver.getApprovalStatus().equals(ApprovalStatus.STARTED_APPROVED)
                        ? ApprovalStatus.STARTED : ApprovalStatus.EDITED;
            }

            AmpCategoryValue oldActivityStatus =
                    CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME,
                            ver.getCategories());

            LOGGER.info(String.format("\t%s activity %d, changing status ID from %s to %d and approvalStatus from "
                            + "<%s> to <%s>...",
                    saveContext.equals(SaveContext.job())
                            ? "autoclosing" : "closing", ver.getAmpActivityId(),
                    oldActivityStatus == null
                            ? "<null>" : Long.toString(oldActivityStatus.getId()), closedCategoryValue,
                    ver.getApprovalStatus(), newStatus));

            AmpTeamMember ampClosingMember =
                    AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(ver.getTeam(), user);
            AmpActivityVersion newVer = this.cloneActivity(ampClosingMember, ver, newStatus,
                    closedCategoryValue, saveContext);

            LOGGER.info(String.format("... done, new amp_activity_id=%d\n", newVer.getAmpActivityId()));
            PersistenceManager.getSession().flush();
        }
    }

    /**
     * clones activity, sets modifying member, modification date, etc
     *
     * @param member
     * @param oldActivity
     * @return
     * @throws CloneNotSupportedException
     */
    private AmpActivityVersion cloneActivity(AmpTeamMember member, AmpActivityVersion oldActivity,
                                             ApprovalStatus newStatus, Long closedProjectStatusCategoryValue,
                                             SaveContext saveContext) throws Exception {
        AmpActivityVersion prevVersion = oldActivity.getAmpActivityGroup().getAmpActivityLastVersion();
        oldActivity.getAmpActivityGroup().setAutoClosedOnExpiration(true);

        oldActivity.setApprovalStatus(newStatus);
        oldActivity.getCategories().remove(CategoryManagerUtil.
                getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME, oldActivity.getCategories()));
        oldActivity.getCategories().add(CategoryManagerUtil.
                getAmpCategoryValueFromDb(closedProjectStatusCategoryValue));

        EditorStore editorStore = new EditorStore();
        Site site = SiteUtils.getDefaultSite();

        AmpActivityVersion auxActivity = null;

        auxActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(oldActivity, null,
                member, oldActivity.getDraft(), PersistenceManager.getSession(), saveContext, editorStore, site);
        java.util.Locale javaLocale = new java.util.Locale("en");
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true,
                SiteUtils.getDefaultSite(), javaLocale,
                auxActivity, prevVersion);
        return auxActivity;
    }
}
