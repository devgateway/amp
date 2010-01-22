package org.dgfoundation.amp.test.messages;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.message.util.AmpMessageUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class ExternalReceiversTest extends BasicActionTestCaseAdapter {
	private static Logger logger = Logger.getLogger(ExternalReceiversTest.class);	
	MockHttpSession session;
	MockHttpServletRequest request;	
	
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		
		//session = getActionMockObjectFactory().getMockSession();
		//request = getActionMockObjectFactory().getMockRequest();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testReceivers() throws Exception{
		logger.info("getting receivers");
		String[] externalReceivers=AmpMessageUtil.buildExternalReceiversFromContacts();
		if(externalReceivers!=null){
			String expression="^.+\\s.+\\s<.+@.+\\..+>";
			for (String receiver : externalReceivers) {
				logger.info("current receiver"+receiver);
				assertTrue(receiver.matches(expression));
			}
		}
		
	}
}
