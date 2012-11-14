package org.digijava.module.aim.services.auditcleaner;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
import org.digijava.module.message.util.AmpMessageUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class AuditCleanerMsgJob implements Job {
	public static final String FROM = "Administrator";
	public static final String MESSAGE_TITLE = "Audit Cleanup Sevice";
	public static final String BODY_1 = "All logs older than  ";
	public static final String BODY_2 = " days will be deleted from the Audit Trail on ";

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		String strreceiveirs = null;

		try {
			Collection<AmpTeamMember> alllead = TeamMemberUtil.getMembersUsingRole(new Long("1"));

			for (Iterator iterator = alllead.iterator(); iterator.hasNext();) {
				AmpTeamMember ampTeamMember = (AmpTeamMember) iterator.next();
				if (ampTeamMember != null) {
					strreceiveirs += ampTeamMember.getUser().getFirstNames()
							+ " " + ampTeamMember.getUser().getLastName() + "<"
							+ ampTeamMember.getUser().getEmail() + ">,";
				}
			}
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
                             
				
				for (Iterator iterator = alllead.iterator(); iterator.hasNext();) {
					AmpTeamMember tm = (AmpTeamMember) iterator.next();
					if (tm != null && tm.getAmpTeamMemId() != null) {
						createMessageState(message, tm);
					}
				}
               AmpMessageUtil.saveOrUpdateMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    private void createMessageState(AmpMessage message, AmpTeamMember receiver) throws Exception {
        AmpMessageState newMessageState = new AmpMessageState();
        newMessageState.setMessage(message);
        newMessageState.setSender(receiver.getUser().getName());
        //newMessageState.setMemberId(memberId);
        newMessageState.setReceiver(receiver);
        String receivers = message.getReceivers();
        if (receivers == null) {
            receivers = "";
        } else {
            if (receivers.length() > 0) {
                receivers += ", ";
            }
        }
        User user = receiver.getUser();

        receivers += user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;" + receiver.getAmpTeam().getName() + ";";
        message.setReceivers(receivers);
        newMessageState.setRead(false);
        //check if user's inbox is already full

        Class clazz = AmpAlert.class;

        int maxStorage = -1;
        AmpMessageSettings setting = AmpMessageUtil.getMessageSettings();
        if (setting != null && setting.getMsgStoragePerMsgType() != null) {
            maxStorage = setting.getMsgStoragePerMsgType().intValue();
        }
        if (AmpMessageUtil.isInboxFull(clazz, receiver.getAmpTeamMemId()) || AmpMessageUtil.getInboxMessagesCount(clazz, receiver.getAmpTeamMemId(), false, false, maxStorage) >= maxStorage) {
            newMessageState.setMessageHidden(true);
        } else {
            newMessageState.setMessageHidden(false);
        }
        //saving current state in db
        AmpMessageUtil.saveOrUpdateMessageState(newMessageState);
    }
}
