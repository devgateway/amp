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
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.ArrayList;
import java.util.List;

import static org.digijava.module.aim.util.activity.GenericUserHelper.getAmpTeamMemberModifier;

public class ActivityCloser {
    public static final String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    public static final String AMP_MODIFIER_FIRST_NAME = "AMP";
    public static final String AMP_MODIFIER_LAST_NAME = "Activities Modifier";

    private static final Logger LOGGER = Logger.getLogger(ActivityCloser.class);
    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    public void closeActivities(List<AmpActivityVersion> closeableActivities, Long closedCategoryValue,
                                SaveContext saveContext) throws Exception {
        ApprovalStatus newStatus = ApprovalStatus.approved;
        LOGGER.error(closeableActivities.size());
        for (AmpActivityVersion ver : closeableActivities) {

            if ("On".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION))) {
                newStatus = ver.getApprovalStatus().equals(ApprovalStatus.started_approved)
                        ? ApprovalStatus.started : ApprovalStatus.edited;
            }

            AmpCategoryValue oldActivityStatus =
                    CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_KEY,
                            ver.getCategories());

            LOGGER.info(String.format("\t%s activity %d, changing status ID from %s to %d and approvalStatus from "
                            + "<%s> to <%s>...",
                    saveContext.equals(SaveContext.job())
                            ? "autoclosing" : "closing", ver.getAmpActivityId(),
                    oldActivityStatus == null
                            ? "<null>" : Long.toString(oldActivityStatus.getId()), closedCategoryValue,
                    ver.getApprovalStatus(), newStatus));

            AmpTeamMember ampClosingMember = getAmpTeamMemberModifier(ver.getTeam());

            AmpActivityVersion newVer = this.cloneAndCloseActivity(ampClosingMember, ver, newStatus,
                    closedCategoryValue, saveContext, oldActivityStatus);

            LOGGER.info(String.format("... done, new amp_activity_id=%d\n", newVer.getAmpActivityId()));
            PersistenceManager.getSession().flush();
        }
    }

    private AmpActivityVersion cloneAndCloseActivity(AmpTeamMember member, AmpActivityVersion oldActivity,
                                                     ApprovalStatus newStatus, Long closedProjectStatusCategoryValue,
                                                     SaveContext saveContext, AmpCategoryValue oldActivityStatus)
            throws Exception {
        modifyProjectStatus(oldActivity, newStatus, closedProjectStatusCategoryValue, oldActivityStatus);
        return cloneActivity(member, oldActivity, saveContext);
    }

    /**
     * clones activity, sets modifying member, modification date, etc
     *
     * @param member
     * @param oldActivity
     * @return
     * @throws CloneNotSupportedException
     */
    public static  AmpActivityVersion cloneActivity(AmpTeamMember member, AmpActivityVersion oldActivity,
                                             SaveContext saveContext)
            throws Exception {

        AmpActivityVersion prevVersion = oldActivity.getAmpActivityGroup().getAmpActivityLastVersion();
        EditorStore editorStore = new EditorStore();
        Site site = SiteUtils.getDefaultSite();

        AmpActivityVersion auxActivity = null;
        auxActivity = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(oldActivity, null,
                new ArrayList<>(),
                member, oldActivity.getDraft(), PersistenceManager.getSession(), saveContext, editorStore, site);
        java.util.Locale javaLocale = new java.util.Locale("en");
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true,
                SiteUtils.getDefaultSite(), javaLocale,
                auxActivity, prevVersion);
        return auxActivity;
    }

    private void modifyProjectStatus(AmpActivityVersion oldActivity, ApprovalStatus newStatus,
                                     Long closedProjectStatusCategoryValue, AmpCategoryValue oldActivityStatus) {
        oldActivity.getAmpActivityGroup().setAutoClosedOnExpiration(true);
        oldActivity.setApprovalStatus(newStatus);
        oldActivity.getCategories().remove(oldActivityStatus);
        oldActivity.getCategories().add(CategoryManagerUtil.
                getAmpCategoryValueFromDb(closedProjectStatusCategoryValue));
    }
}
