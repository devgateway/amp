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
    	public static final Class[] availableTriggers=new Class[] {ActivitySaveTrigger.class}; 
    
	public static final String PRIORITY_LEVEL_NAME= "Priorty level";
	public static final String PRIORITY_LEVEL_KEY= "priorty_level";
	
	public static final String MESSAGE_TYPE_NAME= "Message Type";
	public static final String MESSAGE_TYPE_KEY= "message_type";
	
	/**
	 * What or who sends the message
	 */
	public static final String SENDER_TYPE_USER= "User";
	public static final String SENDER_TYPE_ACTIVITY= "Activity";
	public static final String SENDER_TYPE_USER_MANAGER= "UM";
	
	/**
	 * Defines a priority Level for each message	 
	 */
	public static final Long PRIORITY_LEVEL_LOW= new Long(1);
	public static final Long PRIORITY_LEVEL_NORMAL= new Long(2);
	public static final Long PRIORITY_LEVEL_HIGH= new Long(3);
	public static final Long PRIORITY_LEVEL_MODERATE= new Long(4);
}
