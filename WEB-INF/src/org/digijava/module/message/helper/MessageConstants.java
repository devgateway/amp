package org.digijava.module.message.helper;

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
    public static final  Class[] availableTriggers=new Class[] {ActivitySaveTrigger.class,ActivityDisbursementDateTrigger.class,UserRegistrationTrigger.class,ApprovedActivityTrigger.class,NotApprovedActivityTrigger.class};
    public static final  String[] triggerName=new String[] {"Save Actvity","Activity Disbursement Date","New User Registration","Activity Approved", "Activity Not Approved"};

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

}
