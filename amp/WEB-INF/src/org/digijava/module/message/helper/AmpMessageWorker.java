package org.digijava.module.message.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.CompleteWorkspaceFilter;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpEmail;
import org.digijava.module.message.dbentity.AmpEmailReceiver;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.triggers.AbstractCalendarEventTrigger;
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ActivityValidationWorkflowTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.ApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.ApprovedResourceShareTrigger;
import org.digijava.module.message.triggers.AwaitingApprovalCalendarTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.PendingResourceShareTrigger;
import org.digijava.module.message.triggers.RejectResourceSharetrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.triggers.UserAddedToFirstWorkspaceTrigger;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.hibernate.jdbc.Work;

public class AmpMessageWorker {

	private static Logger logger = Logger.getLogger(AmpMessageWorker.class);

	public static void processEvent(Event e) throws Exception {
		String triggerClassName = e.getTrigger().getName();
		List<TemplateAlert> tempAlerts = AmpMessageUtil.getTemplateAlerts(triggerClassName);
		if (tempAlerts != null && !tempAlerts.isEmpty()) {
			for (TemplateAlert template : tempAlerts) {
				// AmpAlert newAlert=createAlertFromTemplate(template);
				AmpMessage newMsg = null;

				AmpAlert newAlert = new AmpAlert();
				Approval newApproval = new Approval();
				CalendarEvent newEvent = new CalendarEvent();

				if (e.getTrigger().equals(ActivitySaveTrigger.class)) { // <------
																		// Someone
																		// created
																		// new
																		// Activity
					newMsg = processActivitySaveEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(UserAddedToFirstWorkspaceTrigger.class)) { // <-----
																							// User
																							// added
																							// to
																							// his
																							// first
																							// workspace
					newMsg = proccessUserWorkspaceAssignmentEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(UserRegistrationTrigger.class)) { // <-----
																					// Registered
																					// New
																					// User
					newMsg = proccessUserRegistrationEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityDisbursementDateTrigger.class)) {
					newMsg = processActivityDisbursementDateComingEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(CalendarEventTrigger.class)) {
					newMsg = proccessCalendarEvent(e, newEvent, template, false);
				} else if (e.getTrigger().equals(CalendarEventSaveTrigger.class)) {
					newMsg = proccessCalendarEvent(e, newEvent, template, true);
				} else if (e.getTrigger().equals(RemoveCalendarEventTrigger.class)) {
					newMsg = proccessCalendarEventRemoval(e, newEvent, template);
				} else if (e.getTrigger().equals(ApprovedActivityTrigger.class)) {
					newMsg = processApprovedActivityEvent(e, newApproval, template);
				} else if (e.getTrigger().equals(NotApprovedCalendarEventTrigger.class)
						|| e.getTrigger().equals(ApprovedCalendarEventTrigger.class)
						|| e.getTrigger().equals(AwaitingApprovalCalendarTrigger.class)) {
					newMsg = processApprovedCalendarEvent(e, newApproval, template);
				} else if (e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
					newMsg = processNotApprovedActivityEvent(e, newApproval, template);
				} else if (e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
					newMsg = processActivityActualStartDateEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
					newMsg = processActivityCurrentCompletionDateEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
					newMsg = processActivityFinalDateForContractingEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
					newMsg = processActivityFinalDateForDisbursementsEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
					newMsg = processActivityProposedApprovalDateEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
					newMsg = processActivityProposedCompletionDateEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
					newMsg = processActivityProposedStartDateEvent(e, newAlert, template);
				} else if (e.getTrigger().equals(PendingResourceShareTrigger.class)) {
					newMsg = processResourceShareEvent(e, newApproval, template, true);
				} else if (e.getTrigger().equals(ApprovedResourceShareTrigger.class)
						|| e.getTrigger().equals(RejectResourceSharetrigger.class)) {
					newMsg = processResourceShareEvent(e, newApproval, template, false);
				} else if (e.getTrigger().equals(ActivityValidationWorkflowTrigger.class)) {
					// This should be generalize but not to modify already
					// running (for long time) code we process this particular
					// case in a separate piece of
					// code

					List<AmpAlert> listNewMsg = processActivityValidationWorkflowEvent(e, newAlert, template);

					for (AmpAlert ampMessage : listNewMsg) {
						List<String> receivers = new ArrayList<>();
						AmpMessageUtil.saveOrUpdateMessage(ampMessage);
						receivers.add(StringUtils.split(ampMessage.getReceivers(), ";")[0]);
						createEmailsAndReceivers(ampMessage, receivers, false);
					}

				}
				if (newMsg != null) {
					AmpMessageUtil.saveOrUpdateMessage(newMsg);
				} else {
					return;
				}
				/**
				 * getting states according to tempalteId New Requirement for
				 * ApprovedActiviti and Activity waiting approval.only
				 * teamLeader(in case approval is needed) and activity
				 * creator/updater should get an alert regardless of receivers
				 * list in template
				 */
				if (e.getTrigger().equals(ApprovedActivityTrigger.class)
						|| e.getTrigger().equals(NotApprovedActivityTrigger.class)) {
					defineReceiversForApprovedAndNotApprovedActivities(e.getTrigger(), newMsg,
							(Long) e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM),
							(AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_APPROVED_BY));
				} else if (e.getTrigger().equals(NotApprovedCalendarEventTrigger.class)
						|| e.getTrigger().equals(ApprovedCalendarEventTrigger.class)
						|| e.getTrigger().equals(AwaitingApprovalCalendarTrigger.class)) {
					AmpTeamMember creator = (AmpTeamMember) e.getParameters()
							.get(AbstractCalendarEventTrigger.PARAM_AUTHOR);
					defineReceiversForApprovedCalendarEvent(creator, newMsg);
				} else if (e.getTrigger().equals(CalendarEventTrigger.class)) {
					defineReceiversForCalendarEvents(e, template, newMsg, false, false);
				} else if (e.getTrigger().equals(CalendarEventSaveTrigger.class)) {
					defineReceiversForCalendarEvents(e, template, newMsg, true, false);
				} else if (e.getTrigger().equals(RemoveCalendarEventTrigger.class)) {
					defineReceiversForCalendarEvents(e, template, newMsg, false, true);
				} else if (e.getTrigger().equals(ActivitySaveTrigger.class)) {
					defineActivityCreationReceivers(template, newMsg,
							new Long(e.getParameters().get(ActivitySaveTrigger.PARAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityActualStartDateTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg,
							new Long(e.getParameters().get(ActivityActualStartDateTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityCurrentCompletionDateTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg, new Long(
							e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityFinalDateForContractingTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg, new Long(
							e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityFinalDateForDisbursementsTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg, new Long(
							e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityProposedApprovalDateTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg, new Long(
							e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityProposedCompletionDateTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg, new Long(
							e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(ActivityProposedStartDateTrigger.class)) {
					defineReceievrsByActivityTeam(template, newMsg,
							new Long(e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_TEAM_ID).toString()));
				} else if (e.getTrigger().equals(PendingResourceShareTrigger.class)) {
					defineReceiversForResourceShare(template, newMsg, true);
				} else if (e.getTrigger().equals(ApprovedResourceShareTrigger.class)
						|| e.getTrigger().equals(RejectResourceSharetrigger.class)) {
					defineReceiversForResourceShare(template, newMsg, false);

				} else if (e.getTrigger().equals(UserAddedToFirstWorkspaceTrigger.class)) {
					defineReceiversForUserAddedToWorkspace(newMsg, e);
				} else { // <-- currently for else is left user registration
							// or activity disbursement date triggers
					List<String> emailReceivers = new ArrayList<String>();
					List<AmpMessageState> statesRelatedToTemplate = null;
					statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
					HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState>();
					if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
						for (AmpMessageState state : statesRelatedToTemplate) {
							createMsgState(state, newMsg, false);
							if (!msgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
								msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
								emailReceivers.add(state.getReceiver().getUser().getEmail());
							}
						}
						createEmailsAndReceivers(newMsg, emailReceivers, false);
						// sendMailes(msgStateMap.values());
					}
				}
			}
		}
	}

	public static Approval processResourceShareEvent(Event e, Approval approval, TemplateAlert template,
			boolean needsApproval) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		Long teamId = (Long) e.getParameters().get(PendingResourceShareTrigger.PARAM_CREATOR_TEAM);
		String userMail = (String) e.getParameters().get(PendingResourceShareTrigger.PARAM_SHARED_BY);
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(PendingResourceShareTrigger.PARAM_NAME));
		myHashMap.put(MessageConstants.OBJECT_AUTHOR, userMail); // holds email
																	// of
																	// creator
		myHashMap.put(MessageConstants.OBJECT_TEAM, teamId.toString());

		AmpTeamMember tm = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(userMail, teamId);
		approval.setSenderId(tm.getAmpTeamMemId());

		approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createApprovalFromTemplate(template, myHashMap, approval, true, false, false, null);
	}

	/**
	 * Calendar Event processing
	 */
	private static CalendarEvent proccessCalendarEvent(Event e, CalendarEvent event, TemplateAlert template,
			boolean saveActionWasCalled) {
		// get event creator
		AmpTeamMember tm = (AmpTeamMember) e.getParameters().get(CalendarEventSaveTrigger.SENDER);

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(CalendarEventTrigger.PARAM_NAME));
		myHashMap.put(MessageConstants.OBJECT_URL,
				"<a href=\"" + "/" + e.getParameters().get(CalendarEventTrigger.PARAM_URL) + "\">View Event</a>");
		event.setObjectURL("/" + e.getParameters().get(CalendarEventTrigger.PARAM_URL));
		if (saveActionWasCalled) {
			event.setSenderType(MessageConstants.SENDER_TYPE_USER);
			event.setSenderId(tm.getAmpTeamMemId());
			event.setSenderName(tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<"
					+ tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName());
			event.setSenderEmail(tm.getUser().getEmail());
			// put event's start/end dates in map.
			myHashMap.put(MessageConstants.START_DATE,
					(String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_START_DATE));
			myHashMap.put(MessageConstants.END_DATE,
					(String) e.getParameters().get(CalendarEventSaveTrigger.EVENT_END_DATE));
		} else {
			myHashMap.put(MessageConstants.START_DATE,
					(String) e.getParameters().get(CalendarEventTrigger.EVENT_START_DATE));
			myHashMap.put(MessageConstants.END_DATE,
					(String) e.getParameters().get(CalendarEventTrigger.EVENT_END_DATE));
			event.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
		}

		CalendarEvent newEvent = createEventFromTemplate(template, myHashMap, event);

		String receivers = new String();

		Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
		AmpCalendar ampCal = AmpDbUtil.getAmpCalendar(calId);
		Set<AmpCalendarAttendee> att = ampCal.getAttendees();

		if (att != null) {
			receivers = createReceiversFieldForEvent(receivers, att);
		}
		// In case this event is created when new calendar event was added,
		// message should go to it's creator too
		// so in the receivers list we should also add it's creator (AMP-3775)
		if (saveActionWasCalled) {
			User user = tm.getUser();
			receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;"
					+ tm.getAmpTeam().getName() + ";";
		}

		newEvent.setReceivers(receivers.substring(", ".length()));
		return newEvent;
	}

	private static CalendarEvent proccessCalendarEventRemoval(Event e, CalendarEvent event, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();

		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(RemoveCalendarEventTrigger.PARAM_NAME));
		// get event creator
		AmpTeamMember tm = (AmpTeamMember) e.getParameters().get(RemoveCalendarEventTrigger.SENDER);
		event.setSenderType(MessageConstants.SENDER_TYPE_USER);
		event.setSenderId(tm.getAmpTeamMemId());
		event.setSenderName(tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<"
				+ tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName());
		event.setSenderEmail(tm.getUser().getEmail());
		// put event's start/end dates in map.
		myHashMap.put(MessageConstants.START_DATE,
				(String) e.getParameters().get(RemoveCalendarEventTrigger.EVENT_START_DATE));
		myHashMap.put(MessageConstants.END_DATE,
				(String) e.getParameters().get(RemoveCalendarEventTrigger.EVENT_END_DATE));

		CalendarEvent newEvent = createEventFromTemplate(template, myHashMap, event);

		String receivers = new String();
		Set<AmpCalendarAttendee> att = (Set<AmpCalendarAttendee>) e.getParameters()
				.get(RemoveCalendarEventTrigger.ATTENDEES);
		if (att != null) {
			receivers = createReceiversFieldForEvent(receivers, att);
		}
		return newEvent;
	}

	private static String createReceiversFieldForEvent(String receivers, Set<AmpCalendarAttendee> att) {
		for (AmpCalendarAttendee ampAtt : att) {
			if (ampAtt.getMember() != null) {
				AmpTeamMember member = ampAtt.getMember();
				User user = member.getUser();
				receivers += ", " + user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;"
						+ member.getAmpTeam().getName() + ";";
			}
			if (ampAtt.getGuest() != null) { // guests e-mails should also be
												// included in receivers list
				receivers += ", <" + ampAtt.getGuest() + ">;";
			}
		}
		return receivers;
	}

	/**
	 * Not Approved Activity Event processing
	 */
	private static Approval processNotApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		AmpTeamMember savedBy = e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY) != null
				? (AmpTeamMember) e.getParameters().get(NotApprovedActivityTrigger.PARAM_SAVED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
			approval.setSenderId(savedBy.getAmpTeamMemId());
		}
		Long creatorTeam = e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM) != null
				? (Long) e.getParameters().get(NotApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM) : null;
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(NotApprovedActivityTrigger.PARAM_NAME));
		if (creatorTeam != null) {
			myHashMap.put(MessageConstants.OBJECT_TEAM, creatorTeam.toString());
		}

		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
		approval.setObjectURL("/" + e.getParameters().get(NotApprovedActivityTrigger.PARAM_URL));

		approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createApprovalFromTemplate(template, myHashMap, approval, true, false, true, null);
	}

	/**
	 * Approved Activity Event processing
	 */
	private static Approval processApprovedActivityEvent(Event e, Approval approval, TemplateAlert template) {
		AmpTeamMember approver = (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_APPROVED_BY);
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ApprovedActivityTrigger.PARAM_NAME));
		AmpTeamMember savedBy = e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ApprovedActivityTrigger.PARAM_SAVED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
			approval.setSenderId(savedBy.getAmpTeamMemId());
		}
		Long creatorTeam = e.getParameters().get(ApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM) != null
				? (Long) e.getParameters().get(ApprovedActivityTrigger.PARAM_ACTIVIY_CREATOR_TEAM) : null;
		if (creatorTeam != null) {
			myHashMap.put(MessageConstants.OBJECT_TEAM, creatorTeam.toString());
		}
		myHashMap.put(MessageConstants.APPROVED_BY, approver.getUser().getName());
		// url
		myHashMap.put(MessageConstants.OBJECT_URL,
				"<a href=\"" + "/" + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL) + "\">activity URL</a>");
		approval.setObjectURL("/" + e.getParameters().get(ApprovedActivityTrigger.PARAM_URL));

		approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createApprovalFromTemplate(template, myHashMap, approval, false, false, true, approver);
	}

	private static Approval processApprovedCalendarEvent(Event e, Approval approval, TemplateAlert template) {

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(AbstractCalendarEventTrigger.PARAM_TITLE));
		// url
		if (e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL) != null) {
			myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
					+ e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL) + "\">activity URL</a>");
			approval.setObjectURL("/" + e.getParameters().get(AbstractCalendarEventTrigger.PARAM_URL));
		}
		AmpTeamMember creator = (AmpTeamMember) e.getParameters().get(AbstractCalendarEventTrigger.PARAM_AUTHOR);
		approval.setSenderId(creator.getAmpTeamMemId());
		approval.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		approval.setCreationDate(new Date());
		//
		if (e.getParameters().get(AbstractCalendarEventTrigger.PARAM_TEAM_MANAGER) != null) {
			myHashMap.put(MessageConstants.OBJECT_TEAM, creator.getAmpTeam().getAmpTeamId().toString());

		}
		myHashMap.put(MessageConstants.OBJECT_AUTHOR,
				(String) e.getParameters().get(AbstractCalendarEventTrigger.PARAM_TEAM_MANAGER));
		User user = creator.getUser();
		String receivers = user.getFirstNames() + " " + user.getLastName() + "<" + user.getEmail() + ">;"
				+ user.getName() + ";";

		approval.setReceivers(receivers);
		return createApprovalFromTemplate(template, myHashMap, approval, false, false, false, null);
	}

	private static AmpAlert processActivitySaveEvent(Event e, AmpAlert alert, TemplateAlert template) {

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(ActivitySaveTrigger.PARAM_NAME));
		AmpTeamMember savedBy = e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
			alert.setSenderId(savedBy.getAmpTeamMemId());
		}
		myHashMap.put(MessageConstants.OBJECT_URL,
				"<a href=\"" + "/" + e.getParameters().get(ActivitySaveTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivitySaveTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityActualStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_NAME));
		AmpTeamMember savedBy = e.getParameters().get(ActivityActualStartDateTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityActualStartDateTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityActualStartDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityCurrentCompletionDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_NAME));
		AmpTeamMember savedBy = e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityCurrentCompletionDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityFinalDateForContractingEvent(Event e, AmpAlert alert,
			TemplateAlert template) {

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivitySaveTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}

		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityFinalDateForContractingTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityFinalDateForDisbursementsEvent(Event e, AmpAlert alert,
			TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_CREATED_BY)
				: null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityFinalDateForDisbursementsTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityProposedApprovalDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityProposedApprovalDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityProposedCompletionDateEvent(Event e, AmpAlert alert,
			TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityProposedCompletionDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}

	private static AmpAlert processActivityProposedStartDateEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityProposedStartDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);

	}
	// proccessUserWorkspaceAssignmentEvent(e, newAlert, template);

	/**
	 * User Assignation to his first workspace event
	 */
	private static AmpAlert proccessUserWorkspaceAssignmentEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(UserAddedToFirstWorkspaceTrigger.PARAM_NAME));
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(UserAddedToFirstWorkspaceTrigger.PARAM_URL) + "\">User Profile URL</a>");
		myHashMap.put(MessageConstants.OBJECT_LOGIN,
				(String) e.getParameters().get(UserAddedToFirstWorkspaceTrigger.PARAM_LOGIN));
		myHashMap.put(MessageConstants.OBJECT_ORGANIZATION,
				(String) e.getParameters().get(UserAddedToFirstWorkspaceTrigger.PARAM_ORGANIZATION));
		alert.setObjectURL("/" + e.getParameters().get(UserAddedToFirstWorkspaceTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
		return createAlertFromTemplate(template, myHashMap, alert);
	}

	/**
	 * User Registration Event processing
	 */
	private static AmpAlert proccessUserRegistrationEvent(Event e, AmpAlert alert, TemplateAlert template) {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME, (String) e.getParameters().get(UserRegistrationTrigger.PARAM_NAME));
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(UserRegistrationTrigger.PARAM_URL) + "\">User Profile URL</a>");
		myHashMap.put(MessageConstants.OBJECT_LOGIN,
				(String) e.getParameters().get(UserRegistrationTrigger.PARAM_LOGIN));
		myHashMap.put(MessageConstants.OBJECT_ORGANIZATION,
				(String) e.getParameters().get(UserRegistrationTrigger.PARAM_ORGANIZATION));
		alert.setObjectURL("/" + e.getParameters().get(UserRegistrationTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_USER_MANAGER);
		return createAlertFromTemplate(template, myHashMap, alert);
	}

	/**
	 * Activity's disbursement date Event processing
	 */
	private static AmpAlert processActivityDisbursementDateComingEvent(Event e, AmpAlert alert,
			TemplateAlert template) {

		HashMap<String, String> myHashMap = new HashMap<String, String>();
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_NAME));
		// creator
		AmpTeamMember savedBy = e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_CREATED_BY) != null
				? (AmpTeamMember) e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_CREATED_BY) : null;
		if (savedBy != null) {
			myHashMap.put(MessageConstants.OBJECT_AUTHOR, savedBy.getUser().getName());
		}
		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\"" + "/"
				+ e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("/" + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);
		return createAlertFromTemplate(template, myHashMap, alert);
	}

	private static Approval createApprovalFromTemplate(TemplateAlert template, HashMap<String, String> myMap,
			Approval newApproval, boolean needsApproval, boolean sourceIsResource, boolean activityApproval,
			AmpTeamMember approver) {
		newApproval.setName(DgUtil.fillPattern(template.getName(), myMap));
		newApproval.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
		newApproval.setDraft(false);
		newApproval.setCreationDate(new Date());
		//
		Long teamId = null;
		if (myMap.containsKey(MessageConstants.OBJECT_TEAM)) {
			teamId = Long.parseLong(myMap.get(MessageConstants.OBJECT_TEAM));
		} else {
			if (approver.getAmpTeam() != null) {
				teamId = approver.getAmpTeam().getAmpTeamId();
			}
		}
		// receivers
		String receivers = "";
		/*
		 * currently approvers only approve activity and not calendar event,
		 * resources
		 */
		if (activityApproval) {
			AmpTeamMember tm = TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
			receivers += tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<" + tm.getUser().getEmail()
					+ ">;" + tm.getAmpTeam().getName() + ";";
			List<AmpTeamMember> teamHeadAndAndApprovers = TeamMemberUtil.getTeamHeadAndApprovers(teamId);

			for (AmpTeamMember member : teamHeadAndAndApprovers) {
				if (approver != null && !needsApproval && member.getAmpTeamMemId().equals(approver)) {
					continue;
				}
				receivers += ", " + member.getUser().getFirstNames() + " " + member.getUser().getLastName() + "<"
						+ member.getUser().getEmail() + ">;" + member.getAmpTeam().getName() + ";";
			}
		} else {
			if ((needsApproval && !sourceIsResource) || !needsApproval) {
				AmpTeamMember tm = TeamMemberUtil.getAmpTeamMember(newApproval.getSenderId());
				receivers += tm.getUser().getFirstNames() + " " + tm.getUser().getLastName() + "<"
						+ tm.getUser().getEmail() + ">;" + tm.getAmpTeam().getName() + ";";
			} else {

				AmpTeamMember teamHead = TeamMemberUtil.getTeamHead(teamId);
				if (teamHead != null) {
					receivers += ", " + teamHead.getUser().getFirstNames() + " " + teamHead.getUser().getLastName()
							+ "<" + teamHead.getUser().getEmail() + ">;" + teamHead.getAmpTeam().getName() + ";";
				}
			}

		}

		newApproval.setReceivers(receivers);
		return newApproval;
	}

	private static AmpAlert createAlertFromTemplate(TemplateAlert template, HashMap<String, String> myMap,
			AmpAlert newAlert) {
		return createAlertFromTemplate(template, myMap, newAlert, null);
	}

	/**
	 * created different kinds of alerts(not approvals or calendar events )
	 */
	private static AmpAlert createAlertFromTemplate(TemplateAlert template, HashMap<String, String> myMap,
			AmpAlert newAlert, String receivers) {
		newAlert.setName(DgUtil.fillPattern(template.getName(), myMap));
		newAlert.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
		if (receivers == null) {
			newAlert.setReceivers(template.getReceivers());
		} else {
			newAlert.setReceivers(receivers);
		}
		newAlert.setDraft(false);
		Calendar cal = Calendar.getInstance();
		newAlert.setCreationDate(cal.getTime());
		return newAlert;
	}

	/**
	 * Activity's validation workflow Event processing
	 */
	private static List<AmpAlert> processActivityValidationWorkflowEvent(Event e, AmpAlert alert,
			TemplateAlert template) {

		StringBuffer teamsToNotify = new StringBuffer();
		String[] receivers;
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		List<AmpAlert> alerts = new ArrayList<AmpAlert>();

		Collection<Team> listTeamsToNotify;

		// this needs to be redone once we have a proper representation of
		// receivers in TemplateAlerts
		myHashMap.put(MessageConstants.OBJECT_NAME,
				(String) e.getParameters().get(ActivityValidationWorkflowTrigger.PARAM_NAME));

		// url
		myHashMap.put(MessageConstants.OBJECT_URL, "<a href=\""
				+ e.getParameters().get(ActivityValidationWorkflowTrigger.PARAM_URL) + "\">activity URL</a>");
		alert.setObjectURL("" + e.getParameters().get(ActivityDisbursementDateTrigger.PARAM_URL));
		alert.setSenderType(MessageConstants.SENDER_TYPE_SYSTEM);

		listTeamsToNotify = getTeamsForActivity(
				(Long) e.getParameters().get(ActivityValidationWorkflowTrigger.PARAM_ACTIVITY_ID),
				template.getRelatedTriggerName());

		for (Team ampTeam : listTeamsToNotify) {
			teamsToNotify.append(ampTeam.getTeamName());
			teamsToNotify.append(";");
		}
		receivers = StringUtils.split(template.getReceivers(), ",");
		for (int j = 0; j < receivers.length; j++) {
			if (teamsToNotify.toString().contains(StringUtils.split(receivers[j], ";")[1])) {
				if (receivers[j].indexOf("<") > 0) {
					myHashMap.put(MessageConstants.OBJECT_LOGIN,
							StringUtils.left(receivers[j], receivers[j].indexOf("<")));
				}
				alerts.add(createAlertFromTemplate(template, myHashMap, alert, receivers[j]));

			}
		}

		return alerts;
	}

	/**
	 * List of teams that have to be notified
	 * 
	 * @param ampActivityId
	 * @param relatedTrigger
	 * @return
	 */
	private static Collection<Team> getTeamsForActivity(Long ampActivityId, String relatedTrigger) {

		List<Team> teamsToReturn = new ArrayList<Team>();

		if (TLSUtils.getRequest() == null) {
			TLSUtils.populateMockTlsUtils();
		}				
		TeamMember requestMember = (TeamMember) TLSUtils.getRequest().getSession()
				.getAttribute(Constants.CURRENT_MEMBER);


		if (TLSUtils.getRequest().getAttribute("activityTeams") != null) {
			Map<Long, List<Team>> activityTeams;
			activityTeams = (Map<Long, List<Team>>) TLSUtils.getRequest().getAttribute("activityTeams");
			teamsToReturn = activityTeams.get(ampActivityId);
		} else {
			final Map<Long, List<Team>> activityTeams = new HashMap<Long, List<Team>>();
			// we need to use a set here to hold unique values, will change
			// before
			// closing the ticket
			String teamsConfigured = null;
			StringBuffer teamsToSearch = new StringBuffer();
			// ids of team to filter out the AmpTeamLeads query
			final List<Long> teamIds = new ArrayList<Long>();

			// we get the set of unique teams configured to receive alter so we
			// can
			// limit
			// the count of ws in which look for the activity
			try {
				StringBuffer bufferReceivers = new StringBuffer();
				String[] aReceivers;
				List<TemplateAlert> tempAlerts = AmpMessageUtil.getTemplateAlerts(relatedTrigger);
				for (TemplateAlert templateAlert : tempAlerts) {
					bufferReceivers.append(templateAlert.getReceivers());
				}
				if (bufferReceivers.length() == 0) {
					// we don't have any recipient configured
					return null;
				}
				aReceivers = StringUtils.split(bufferReceivers.toString(), ",");
				for (int i = 0; i < aReceivers.length; i++) {
					String t = StringUtils.split(aReceivers[i], ";")[1];
					if (!teamsToSearch.toString().contains(t)) {
						teamsToSearch.append("'" + t + "'" + ",");
					}
				}
				teamsConfigured = teamsToSearch.substring(0, teamsToSearch.lastIndexOf(","));

			} catch (Exception e) {
				logger.error("couldnt get teams configured", e);
			}

			final String query = "select min(tm.amp_team_mem_id),tm.amp_team_id from amp_team_member tm ,amp_team  t "
					+ " where tm.amp_member_role_id in(1,3) " + " and tm.amp_team_id=t.amp_team_id "
					+ " and t.name in (" + teamsConfigured + ") " + " group by tm.amp_team_id";

			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					RsInfo teamIdQry = SQLUtils.rawRunQuery(conn, query, null);
					while (teamIdQry.rs.next()) {
						teamIds.add(teamIdQry.rs.getLong(1));
					}
					teamIdQry.close();
				}

			});
			final StringBuffer wsQueries = new StringBuffer();
			Collection<AmpTeamMember> l = TeamMemberUtil.getAllAmpTeamMembersByAmpTeamMemberId(teamIds);
			for (AmpTeamMember ampTeamMember : l) {

				TeamMember member = new TeamMember(ampTeamMember);

				TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_MEMBER, member);
				AmpARFilter af = FilterUtil.buildFilter(ampTeamMember.getAmpTeam(), null);
				af.generateFilterQuery(TLSUtils.getRequest(), true);

				CompleteWorkspaceFilter completeWorkspaceFilter = new CompleteWorkspaceFilter(member, af);
				String wsQuery1 = WorkspaceFilter.generateWorkspaceFilterQuery(completeWorkspaceFilter.tm);
				String wsQuery2 = completeWorkspaceFilter.workspaceFilter.getGeneratedFilterQuery();

				if (wsQueries.length() > 0) {
					wsQueries.append(" UNION ");
				}
				wsQueries.append(addTeamIdToQuery(wsQuery1, ampTeamMember.getAmpTeam().getAmpTeamId(),
						ampTeamMember.getAmpTeam().getName())).append(" UNION ");
				wsQueries.append("select amp_activity_id ," + ampTeamMember.getAmpTeam().getAmpTeamId()
						+ " as  ampTeamId, '" + ampTeamMember.getAmpTeam().getName() + "' as teamName from ( ");
				wsQueries.append(wsQuery2);
				wsQueries.append(") as activityTemp ");

			}
			// we now turn queries into map, and store it at request level in
			// case its needed again
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					RsInfo teamsInActivityQuery = SQLUtils.rawRunQuery(conn, wsQueries.toString(), null);
					while (teamsInActivityQuery.rs.next()) {
						// activityTeams
						Long ampActivityId = teamsInActivityQuery.rs.getLong(1);
						if (activityTeams.get(ampActivityId) == null) {
							activityTeams.put(ampActivityId, new ArrayList<Team>());
						}
						activityTeams.get(ampActivityId).add(
								new Team(teamsInActivityQuery.rs.getLong(2), teamsInActivityQuery.rs.getString(3)));
					}
					teamsInActivityQuery.close();
				}

			});
			TLSUtils.getRequest().setAttribute("activityTeams", activityTeams);
			if (requestMember != null) {
				TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_MEMBER, requestMember);
			} else {
				TLSUtils.getRequest().getSession().removeAttribute(Constants.CURRENT_MEMBER);
			}
			teamsToReturn = activityTeams.get(ampActivityId);
		}


		return teamsToReturn;
	}

	private static CalendarEvent createEventFromTemplate(TemplateAlert template, HashMap<String, String> myMap,
			CalendarEvent newEvent) {
		newEvent.setName(DgUtil.fillPattern(template.getName(), myMap));
		newEvent.setDescription(DgUtil.fillPattern(template.getDescription(), myMap));
		newEvent.setReceivers(template.getReceivers());
		newEvent.setDraft(false);
		Calendar cal = Calendar.getInstance();
		newEvent.setCreationDate(cal.getTime());
		return newEvent;
	}

	/**
	 * does the ugly hack for AMP-18045: since we don't have an outside
	 * templating engine for emails, we'll get the template from the alert
	 * template and issue one, though it doesn't make much sense to send it (the
	 * user has to first log in to get said message).
	 * 
	 * 
	 * @param newMsg
	 * @param e
	 * @throws Exception
	 */
	private static void defineReceiversForUserAddedToWorkspace(AmpMessage newMsg, Event e) throws Exception {

		String email = (String) e.getParameters().get("login");
		if (email == null) {
			throw new RuntimeException("login parameter not set for event");
		}
		List<String> emailReceivers = new ArrayList<String>();
		Collection<AmpTeamMember> receivers = TeamMemberUtil.getTeamMembers(email);
		// since the user had just been assigned to his first team, this
		// shouldn't have >1 member
		for (AmpTeamMember mmb : receivers) {
			AmpMessageState state = new AmpMessageState();
			state.setReceiver(mmb);
			emailReceivers.add(email);
			createMsgState(state, newMsg, false);
		}
		createEmailsAndReceivers(newMsg, emailReceivers, false);
		// this is the email the fresh user will get
		// shouldn't belong here, but the framework is the way it is -- and no
		// other place
		// of getting the message template other than here
		// he'll also get a message alert
		DgEmailManager.sendMail(email, newMsg.getName(), newMsg.getDescription());
	}

	private static void defineReceiversForResourceShare(TemplateAlert template, AmpMessage approval,
			boolean needsApproval) throws Exception {
		List<String> emailReceivers = new ArrayList<String>();
		List<TeamMember> receiverTeamMembers = new ArrayList<TeamMember>();
		List<AmpMessageState> statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
		if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
			// create receivers list for resources
			TeamMember sharedBy = TeamMemberUtil.getTeamMember(approval.getSenderId());
			Long teamId = sharedBy.getTeamId();
			String receivers;
			if (needsApproval) {
				TeamMember teamLead = TeamMemberUtil.getTMTeamHead(teamId);
				receiverTeamMembers.add(teamLead);
			} else {
				receiverTeamMembers.add(sharedBy);
			}
			receivers = fillTOfieldForReceivers(receiverTeamMembers, statesRelatedToTemplate);
			approval.setReceivers(receivers);

			for (AmpMessageState state : statesRelatedToTemplate) {
				AmpTeamMember teamMember = state.getReceiver();
				/**
				 * Approval should get TL or creator of resource depending
				 * whether it's approved/not approved message
				 */
				if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
					boolean createNewMsgState = false;
					if (needsApproval) {
						AmpTeamMemberRoles headRole = TeamMemberUtil.getAmpTeamHeadRole();
						AmpTeamMemberRoles ampRole = teamMember.getAmpMemberRole();
						if (headRole != null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) { // TL
							createNewMsgState = true;
						}
					} else {
						if (teamMember.getAmpTeamMemId().equals(sharedBy.getMemberId())) { // member
																							// who
																							// requested
																							// to
																							// shared
																							// resource
							createNewMsgState = true;
						}
					}
					if (createNewMsgState) {
						createMsgState(state, approval, false);
						emailReceivers.add(state.getReceiver().getUser().getEmail());
						break;
					}

				}
			}
			createEmailsAndReceivers(approval, emailReceivers, false);
		}

	}

	/**
	 * this method defines approval receivers and creates corresponding
	 * AmpMessageStates.
	 */
	private static void defineReceiversForApprovedAndNotApprovedActivities(Class triggerClass, AmpMessage approval,
			Long teamId, AmpTeamMember approver) throws Exception {
		List<String> emailReceivers = new ArrayList<String>();
		AmpTeamMember msgSender = TeamMemberUtil.getAmpTeamMember(approval.getSenderId());
		AmpMessageState state = new AmpMessageState();

		emailReceivers.add(msgSender.getUser().getEmail());
		state.setReceiver(msgSender);
		createMsgState(state, approval, false);
		List<AmpTeamMember> teamHeadAndAndApprovers = TeamMemberUtil.getTeamHeadAndApprovers(teamId);

		for (AmpTeamMember member : teamHeadAndAndApprovers) {
			if (approver != null && triggerClass.equals(ApprovedActivityTrigger.class) && member.equals(approver)) {
				continue;
			}
			emailReceivers.add(member.getUser().getEmail());
			state = new AmpMessageState();
			state.setReceiver(member);
			createMsgState(state, approval, false);

		}

		// define emails and receivers
		createEmailsAndReceivers(approval, emailReceivers, false);
	}

	private static void defineReceiversForApprovedCalendarEvent(AmpTeamMember msgSender, AmpMessage approval)
			throws Exception {
		List<String> emailReceivers = new ArrayList<String>();

		AmpMessageState state = new AmpMessageState();
		emailReceivers.add(msgSender.getUser().getEmail());
		state.setReceiver(msgSender);
		createMsgState(state, approval, false);

		// define emails and receivers
		createEmailsAndReceivers(approval, emailReceivers, false);
	}

	/**
	 * this method defines calendar event receivers and creates corresponding
	 * AmpMessageStates. saveActionWasCalled field is used to define whether
	 * user created new calendar event or not
	 */
	private static void defineReceiversForCalendarEvents(Event e, TemplateAlert template, AmpMessage calEvent,
			boolean saveActionWasCalled, boolean eventRemoved) throws Exception {
		HashMap<Long, AmpMessageState> temMsgStateMap = new HashMap<Long, AmpMessageState>();
		List<AmpMessageState> lstMsgStates = AmpMessageUtil.loadMessageStates(template.getId());
		List<String> emailReceivers = new ArrayList<String>();
		if (lstMsgStates != null) {
			for (AmpMessageState state : lstMsgStates) {
				if (!temMsgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
					temMsgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
				}
			}
		}

		HashMap<Long, AmpMessageState> eventMsgStateMap = new HashMap<Long, AmpMessageState>();
		Long calId = new Long(e.getParameters().get(CalendarEventTrigger.PARAM_ID).toString());
		AmpCalendar ampCal = null;
		Set<AmpCalendarAttendee> att = null;
		if (eventRemoved) {
			att = (Set<AmpCalendarAttendee>) e.getParameters().get(RemoveCalendarEventTrigger.ATTENDEES);
		} else {
			ampCal = AmpDbUtil.getAmpCalendar(calId);
			att = ampCal.getAttendees();
		}
		if (att != null) {
			for (AmpCalendarAttendee ampAtt : att) {
				if (ampAtt.getMember() != null) {
					AmpTeamMember member = ampAtt.getMember();
					if (!eventMsgStateMap.containsKey(member.getAmpTeamMemId())) {
						AmpMessageState state = new AmpMessageState();
						// state.setMemberId(member.getAmpTeamMemId());
						state.setReceiver(member);
						state.setSenderId(calEvent.getSenderId());
						eventMsgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
					}
				} else if (ampAtt.getGuest() != null) { // <---guests should
														// always get e-mails
														// about event
					String emailAddress = ampAtt.getGuest();
					emailReceivers.add(emailAddress);
					// sendMail(((CalendarEvent)calEvent).getSenderEmail(),emailAddress,
					// calEvent.getName(), "UTF-8", calEvent.getDescription());
				}
			}
		}
		HashMap<Long, AmpMessageState> msgStateMap = new HashMap<Long, AmpMessageState>();

		// calendar event creator should also get a message (AMP-3775)
		if (saveActionWasCalled) {
			AmpTeamMember calEventcreator = ampCal.getMember();
			AmpMessageState msgState = new AmpMessageState();
			// msgState.setMemberId(calEventcreator.getAmpTeamMemId());
			msgState.setReceiver(calEventcreator);
			msgState.setSenderId(calEvent.getSenderId());
			msgStateMap.put(msgState.getReceiver().getAmpTeamMemId(), msgState);
		}

		for (AmpMessageState state : temMsgStateMap.values()) {
			if (eventMsgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
				msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
			}
		}

		for (AmpMessageState state : eventMsgStateMap.values()) {
			if (!msgStateMap.containsKey(state.getReceiver().getAmpTeamMemId())) {
				msgStateMap.put(state.getReceiver().getAmpTeamMemId(), state);
			}
		}

		for (AmpMessageState state : msgStateMap.values()) {
			createMsgState(state, calEvent, saveActionWasCalled);
			emailReceivers.add(state.getReceiver().getUser().getEmail());
		}

		createEmailsAndReceivers(calEvent, emailReceivers, saveActionWasCalled);
	}

	/**
	 * this method defines alert receivers and creates corresponding
	 * AmpMessageStates.
	 */

	private static void defineReceievrsByActivityTeam(TemplateAlert template, AmpMessage alert, Long teamId)
			throws Exception {
		List<AmpMessageState> statesRelatedToTemplate = null;
		// get the member who created an activity. it's the current member

		statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
		if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
			List<String> emailReceivers = new ArrayList<String>();
			// create receivers list for activity
			String receivers;
			Collection<TeamMember> teamMembers = TeamMemberUtil.getAllTeamMembers(teamId);
			receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
			alert.setReceivers(receivers);

			for (AmpMessageState state : statesRelatedToTemplate) {
				// get receiver Team Member.
				AmpTeamMember teamMember = state.getReceiver();
				/**
				 * Alert about new activity creation should get only members of
				 * the same team in which activity was created,if this team is
				 * listed as receivers in template.
				 */
				if (teamMember.getAmpTeam().getAmpTeamId().equals(teamId)) {
					createMsgState(state, alert, false);
					emailReceivers.add(state.getReceiver().getUser().getEmail());
				}
			}
			createEmailsAndReceivers(alert, emailReceivers, false);
		}
	}

	private static void defineActivityCreationReceivers(TemplateAlert template, AmpMessage alert, Long activityId)
			throws Exception {
		List<AmpMessageState> statesRelatedToTemplate = null;
		// get the member who created an activity. it's the current member
		AmpTeamMember activityCreator = TeamMemberUtil.getAmpTeamMember(alert.getSenderId());

		statesRelatedToTemplate = AmpMessageUtil.loadMessageStates(template.getId());
		if (statesRelatedToTemplate != null && statesRelatedToTemplate.size() > 0) {
			List<String> receiversAddresses = new ArrayList<String>(); // receivers
																		// that
																		// should
																		// get
																		// emails
			// create receivers list for activity
			String receivers;
			Collection<TeamMember> teamMembers = TeamMemberUtil
					.getAllTeamMembers(activityCreator.getAmpTeam().getAmpTeamId());
			receivers = fillTOfieldForReceivers(teamMembers, statesRelatedToTemplate);
			alert.setReceivers(receivers);

			// and here we define the receivers
			for (AmpMessageState state : statesRelatedToTemplate) {
				// get receiver Team Member.
				AmpTeamMember teamMember = state.getReceiver();
				/**
				 * Alert about new activity creation should get only members of
				 * the same team in which activity was created,if this team is
				 * listed as receivers in template.
				 */
				/*
				 * alert about new activity creation should also be delivered to
				 * all other workspace validators, if cross team validation is
				 * enabled
				 */
				boolean ctv = (teamMember.getAmpTeam().getCrossteamvalidation());

				if (teamMember.getAmpTeam().getAmpTeamId().equals(activityCreator.getAmpTeam().getAmpTeamId())
						|| (ctv && teamMember.isActivityValidatableByUser(activityId))) {
					createMsgState(state, alert, false);
					receiversAddresses.add(teamMember.getUser().getEmail());
				}
			}
			// Emails and Receivers
			createEmailsAndReceivers(alert, receiversAddresses, false);
		}
	}

	private static void createMsgState(AmpMessageState state, AmpMessage newMsg, boolean calendarSaveActionWasCalled)
			throws Exception {
		AmpMessageState newState = new AmpMessageState();
		Class clazz = newMsg.getClass();
		newState.setMessage(newMsg);
		// newState.setMemberId(state.getMemberId());
		newState.setReceiver(state.getReceiver());
		newState.setRead(false);
		if (newMsg.getClassName().equals("c")) {
			newState.setSender(((CalendarEvent) newMsg).getSenderEmail());
		}
		// will this message be visible in user's mailbox
		int maxStorage = -1;
		AmpMessageSettings setting = AmpMessageUtil.getMessageSettings();
		if (setting != null && setting.getMsgStoragePerMsgType() != null) {
			maxStorage = setting.getMsgStoragePerMsgType().intValue();
		}
		if (AmpMessageUtil.isInboxFull(newMsg.getClass(), state.getReceiver().getAmpTeamMemId())
				|| (maxStorage >= 0 && AmpMessageUtil.getInboxMessagesCount(clazz,
						state.getReceiver().getAmpTeamMemId(), false, false, maxStorage) >= maxStorage)) {
			newState.setMessageHidden(true);
		} else {
			newState.setMessageHidden(false);
		}
		try {
			AmpMessageUtil.saveOrUpdateMessageState(newState);
		} catch (AimException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This function is used to create receivers String, which will be shown on
	 * view message page in TO: section
	 */
	private static String fillTOfieldForReceivers(Collection<TeamMember> teamMembers, List<AmpMessageState> states) {
		String receivers = "";// TODO:this is potentially ridiculously slow
		for (AmpMessageState state : states) {
			for (TeamMember tm : teamMembers) {
				if (state.getReceiver().getAmpTeamMemId().equals(tm.getMemberId())) {
					receivers += tm.getMemberName() + " " + "<" + tm.getEmail() + ">;" + tm.getTeamName() + ";" + ", ";
				}
			}
		}
		return receivers;
	}

	/**
	 * Create AmpEmails with receivers that Quartz Job will use to send emails
	 * when called
	 * 
	 * @param message
	 * @param receiversAddresses
	 * @param calendarSaveActionWasCalled
	 * @throws Exception
	 */
	private static void createEmailsAndReceivers(AmpMessage message, List<String> receiversAddresses,
			boolean calendarSaveActionWasCalled) throws Exception {
		AmpMessageSettings messageSettings = AmpMessageUtil.getMessageSettings();
		DigiConfig config = DigiConfigManager.getConfig();
		String partialURL = config.getSiteDomain().getContent();
		String objUrl = message.getObjectURL();
		String description = message.getDescription();
		if (objUrl != null) {
			if (partialURL.endsWith("/")) {
				partialURL = partialURL.substring(0, partialURL.length() - 1);
			}
			String url = partialURL + objUrl;
			description = description.replaceAll(objUrl, url);
		}
		if (messageSettings != null) {
			Long sendMail = messageSettings.getEmailMsgs();
			if (sendMail != null && sendMail.intValue() == 1) {
				AmpEmail ampEmail = null;
				AmpTeamMember msgSender = TeamMemberUtil.getAmpTeamMember(message.getSenderId());
				// create AmpEmail
				if (calendarSaveActionWasCalled) { // <---means that user
													// created new calendar
													// event. if so, a bit
													// different e-mail should
													// be sent
					ampEmail = new AmpEmail(msgSender.getUser().getEmail(), message.getName(), description);
				} else {
					ampEmail = new AmpEmail("system@digijava.org", message.getName(), description);
				}

				DbUtil.saveOrUpdateObject(ampEmail);
				// email receivers
				if (receiversAddresses.size() > 0) {
					for (String emailAddr : receiversAddresses) {
						AmpEmailReceiver emailReceiver = new AmpEmailReceiver(emailAddr, ampEmail,
								MessageConstants.UNSENT_STATUS);
						DbUtil.saveOrUpdateObject(emailReceiver);
					}
				}
			}
		}
	}

	private static String addTeamIdToQuery(String wsQuery, Long teamId, String teamName) {
		Integer indexToReplace = StringUtils.indexOf(wsQuery, "FROM amp_activity");
		wsQuery = StringUtils.left(wsQuery, indexToReplace) + " , " + teamId + " as ampTeamId , '" + teamName
				+ "' as teamName " + StringUtils.mid(wsQuery, indexToReplace, wsQuery.length() - 1);
		return wsQuery;
	}
}