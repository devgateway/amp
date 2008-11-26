package org.digijava.module.message.helper;

import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
/**
 * Constants for AmpMessage class and it's subclasses
 * @author Dare Roinishvili
 *
 */
public class MessageConstants {

    /**
     * Add here all the other classes that extend Trigger class. Alternatively you can use a singleton for dynamic instantiation, see:
     * @see org.digijava.module.gateperm.util.PermissionUtil#getAvailableGates(javax.servlet.ServletContext)
     */
    public static final  Class[] availableTriggers=new Class[] {ActivitySaveTrigger.class,
                                                                ActivityDisbursementDateTrigger.class,
                                                                UserRegistrationTrigger.class,
                                                                ApprovedActivityTrigger.class,
                                                                NotApprovedActivityTrigger.class,
                                                                CalendarEventTrigger.class, 
                                                                CalendarEventSaveTrigger.class,
                                                                ActivityActualStartDateTrigger.class,
                                                                ActivityCurrentCompletionDateTrigger.class,
                                                                ActivityFinalDateForContractingTrigger.class,
                                                                ActivityFinalDateForDisbursementsTrigger.class,
                                                                ActivityProposedApprovalDateTrigger.class,
                                                                ActivityProposedCompletionDateTrigger.class,
                                                                ActivityProposedStartDateTrigger.class};

    public static final  String[] triggerName=new String[] {"Save Actvity",
                                                            "Activity Disbursement Date",
                                                            "New User Registration",
                                                            "Activity Approved",
                                                            "Activity Not Approved",
                                                            "Calendar Event", 
                                                            "Save Calendar Event",
                                                            "Activity Actual Start Date",
                                                            "Activity Current Completion Date",
                                                            "Activity Final Date For Contracting",
                                                            "Activity Final Date For Disbursements",
                                                            "Activity Proposed Approval Date",
                                                            "Activity Proposed Completion Date",
                                                            "Activity Proposed Start Date"};

	public static final String PRIORITY_LEVEL_NAME= "Priorty level";
	public static final String PRIORITY_LEVEL_KEY= "priorty_level";

	public static final String MESSAGE_TYPE_NAME= "Message Type";
	public static final String MESSAGE_TYPE_KEY= "message_type";

	/**
	 * What or who sends the message
	 */
	public static final String SENDER_TYPE_USER= "User";
	public static final String SENDER_TYPE_SYSTEM= "System";
	public static final String SENDER_TYPE_ACTIVITY= "Activity";
	public static final String SENDER_TYPE_USER_MANAGER= "UM";

	/**
	 * Defines a priority Level for each message
	 */
	public static final Long PRIORITY_LEVEL_LOW= new Long(1);
	public static final Long PRIORITY_LEVEL_MEDIUM= new Long(2);
	public static final Long PRIORITY_LEVEL_CRITICAL= new Long(3);

	/**
	 * pagination Elements
	 */
	public static final int MESSAGES_PER_PAGE=15;
	public static final int PAGES_TO_SHOW=5;

	/**
	 * Template Alert helper fields
	 */
	public static final String OBJECT_NAME="name";
	public static final String OBJECT_URL="url";
	public static final String OBJECT_AUTHOR="created by";
    public static final String OBJECT_TEAM="team";
    public static final String START_DATE="start date";
    public static final String END_DATE="end date";

}
