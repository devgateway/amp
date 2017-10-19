package org.digijava.module.message.helper;

import org.digijava.module.message.triggers.ActivityActualStartDateTrigger;
import org.digijava.module.message.triggers.ActivityCurrentCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityDisbursementDateTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForContractingTrigger;
import org.digijava.module.message.triggers.ActivityFinalDateForDisbursementsTrigger;
import org.digijava.module.message.triggers.ActivityMeassureComparisonTrigger;
import org.digijava.module.message.triggers.ActivityProposedApprovalDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedCompletionDateTrigger;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.ActivityValidationWorkflowTrigger;
import org.digijava.module.message.triggers.ApprovedActivityTrigger;
import org.digijava.module.message.triggers.ApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.ApprovedResourceShareTrigger;
import org.digijava.module.message.triggers.CalendarEventSaveTrigger;
import org.digijava.module.message.triggers.CalendarEventTrigger;
import org.digijava.module.message.triggers.DataFreezeEmailNotificationTrigger;
import org.digijava.module.message.triggers.AwaitingApprovalCalendarTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;
import org.digijava.module.message.triggers.NotApprovedCalendarEventTrigger;
import org.digijava.module.message.triggers.SummaryChangeNotificationTrigger;
import org.digijava.module.message.triggers.PendingResourceShareTrigger;
import org.digijava.module.message.triggers.PerformanceRuleAlertTrigger;
import org.digijava.module.message.triggers.RejectResourceSharetrigger;
import org.digijava.module.message.triggers.RemoveCalendarEventTrigger;
import org.digijava.module.message.triggers.UserAddedToFirstWorkspaceTrigger;
import org.digijava.module.message.triggers.UserRegistrationTrigger;
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
    public static final  Class[] availableTriggers=new Class[] {
                                                                UserAddedToFirstWorkspaceTrigger.class,
                                                                ActivitySaveTrigger.class,
                                                                ActivityDisbursementDateTrigger.class,
                                                                ApprovedActivityTrigger.class,
                                                                NotApprovedActivityTrigger.class,
                                                                CalendarEventTrigger.class, 
                                                                CalendarEventSaveTrigger.class,
                                                                RemoveCalendarEventTrigger.class,
                                                                ActivityActualStartDateTrigger.class,
                                                                ActivityCurrentCompletionDateTrigger.class,
                                                                ActivityFinalDateForContractingTrigger.class,
                                                                ActivityFinalDateForDisbursementsTrigger.class,
                                                                ActivityProposedApprovalDateTrigger.class,
                                                                ActivityProposedCompletionDateTrigger.class,
                                                                ActivityProposedStartDateTrigger.class,
                                                                ApprovedCalendarEventTrigger.class,
                                                                NotApprovedCalendarEventTrigger.class,
                                                                AwaitingApprovalCalendarTrigger.class,
                                                                PendingResourceShareTrigger.class,
                                                                ApprovedResourceShareTrigger.class,
                                                                RejectResourceSharetrigger.class,
                                                                UserRegistrationTrigger.class,
                                                                ActivityValidationWorkflowTrigger.class,
                                                                ActivityMeassureComparisonTrigger.class,
                                                                DataFreezeEmailNotificationTrigger.class,
                                                                SummaryChangeNotificationTrigger.class,
                                                                PerformanceRuleAlertTrigger.class
                                                                };

    public static final  String[] triggerName=new String[] {"User added to workspace", 
                                                            "Save Actvity",
                                                            "Activity Disbursement Date",
                                                            "Activity Approved",
                                                            "Activity Not Approved",
                                                            "Calendar Event", 
                                                            "Save Calendar Event",
                                                            "Remove Calendar Event",
                                                            "Activity Actual Start Date",
                                                            "Activity Current Completion Date",
                                                            "Activity Final Date For Contracting",
                                                            "Activity Final Date For Disbursements",
                                                            "Activity Proposed Approval Date",
                                                            "Activity Proposed Completion Date",
                                                            "Activity Proposed Start Date",
                                                            "Calendar Event Approved",
                                                            "Calendar Event Not Approved",
                                                            "Calendar Event Awaiting Approval",
                                                            "Resource Share is Pending Approval",
                                                            "Resource Share is Approved",
                                                            "Reject Resource Share",
                                                            "New User Registration",
                                                            "Activity validation workflow notification",
                                                            "Activity measure comparison notification",
                                                            "Data Freeze Email Notification",
                                                            "Notify Summary Change",
                                                            "Performance Rule Alert Email notification"};

    public static final String PRIORITY_LEVEL_NAME= "Priorty level";
    public static final String PRIORITY_LEVEL_KEY= "priorty_level";

    public static final String MESSAGE_TYPE_NAME= "Message Type";
    public static final String MESSAGE_TYPE_KEY= "message_type";
    
    public static final int ATTACHMENTS_MAX_SIZE=10*1024*1024; //max limit is 10Mb

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
     * Sorting options for Messages
     */
    public static final String SORT_BY_NAME="name";
    public static final String SORT_BY_DATE="creationDate";

    /**
     * Template Alert helper fields
     */
    public static final String OBJECT_NAME="name";
    public static final String OBJECT_URL="url";
    public static final String OBJECT_AUTHOR="created by";
    public static final String OBJECT_TEAM="team";
    public static final String START_DATE="start date";
    public static final String END_DATE="end date";
    public static final String APPROVED_BY="approved by";
    public static final String OBJECT_LOGIN="login";
    public static final String OBJECT_ORGANIZATION = "organization";
    public static final String AMP_ID = "ampId";
    
    /**
     * email statuses
     */
    public static final String SENT_STATUS="sent";
    public static final String UNSENT_STATUS="unsent";
    public static final String FAILED_STATUS="failed";

    /**
     * for Calendar events. 
     * 0    - Awaiting Approval
     * 1    - Approved
     * -1   - Not Approved 
     */
    public static final int CALENDAR_EVENT_AWAITING = 0;
    public static final int CALENDAR_EVENT_APPROVED = 1;
    public static final int CALENDAR_EVENT_AWAITING_REAPPROVE = 2;
    public static final int CALENDAR_EVENT_NOT_APPROVED = -1;
   
}
