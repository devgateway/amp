package org.digijava.module.aim.services.auditcleaner;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class AuditCleanerMsgJob extends ConnectionCleaningJob {
    public static final String FROM = "Administrator";
    public static final String MESSAGE_TITLE = "Audit Cleanup Sevice";
    public static final String BODY_1 = "All logs older than  ";
    public static final String BODY_2 = " days will be deleted from the Audit Trail on ";
    
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Collection<AmpTeamMember> alllead = TeamMemberUtil.getMembersUsingRole(new Long("1"));
            if (alllead != null) {
                AmpMessage message = new AmpAlert();
                message.setName(MESSAGE_TITLE);
                message.setSenderName("Administrator<admin@amp.org>");
                message.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
                message.setPriorityLevel(MessageConstants.PRIORITY_LEVEL_CRITICAL);
                
                message.setDescription(BODY_1
                        + FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATIC_AUDIT_LOGGER_CLEANUP)
                        + BODY_2 + FormatHelper.formatDate(AuditCleaner.getInstance().getNextcleanup()));
                message.setCreationDate(new Date(System.currentTimeMillis()));
                //message.setReceivers(strreceiveirs);
                message.setDraft(false);
                message.setMessageType(0L);
                AmpMessageUtil.saveOrUpdateMessage(message);

                AmpMessageState state = new AmpMessageState();
                state.setMessage(message);
                state.setSender("Admin");
                AmpMessageUtil.saveOrUpdateMessageState(state);
                             
                
                for (AmpTeamMember tm:alllead) {
                    if (tm != null && tm.getAmpTeamMemId() != null) {
                        AmpMessageUtil.createMessageState(message, tm);
                        message.addMessageReceiver(tm);
                    }
                }
                AmpMessageUtil.saveOrUpdateMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
