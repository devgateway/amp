package org.dgfoundation.amp.test.contacts;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.aim.action.AddressBookActions;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.util.ContactInfoUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class PrimaryContactCheckTest extends BasicActionTestCaseAdapter{
	
	private static Logger logger = Logger.getLogger(PrimaryContactCheckTest.class);
	private AddressBookActions action;
	MockHttpSession session;
	MockHttpServletRequest request;	
	
	protected void setUp() throws Exception {
		super.setUp();

		Configuration.initConfig();
		action = new AddressBookActions();
		// register the action class
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(AddressBookActions.class.getName(), action);
		
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCheckUniqueMail() throws Exception{
		logger.info("getting contact");
		List<AmpContact> contacts=ContactInfoUtil.getPagedContacts(1, 1, null, null, null);
		if(contacts!=null){
			for (AmpContact ampContact : contacts) {
				//create contact with the same email
				AmpContact newContact=createContact(ampContact.getEmail());	
				assertTrue(ContactInfoUtil.getContactsCount(newContact.getEmail(),null)!=0);				
			}
		}
	}
	
	private AmpContact createContact(String email){
		AmpContact contact= new AmpContact();
		contact.setEmail(email);
		return contact;
	}
}
