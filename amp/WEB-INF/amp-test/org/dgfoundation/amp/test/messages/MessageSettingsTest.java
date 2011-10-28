

package org.dgfoundation.amp.test.messages;


import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.message.action.AmpMessageActions;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.UserMessage;
import org.digijava.module.message.util.AmpMessageUtil;



public class MessageSettingsTest extends BasicActionTestCaseAdapter{
    private static Logger logger = Logger.getLogger(MessageSettingsTest.class);
    private AmpMessageActions messageAction;
	private AmpMessageForm messageForm;
	MockHttpSession session;




	protected void setUp() throws Exception {
        logger.info("Strating init process");
		// TODO Auto-generated method stub
		super.setUp();
		org.dgfoundation.amp.test.util.Configuration.initConfig();
		messageAction = new AmpMessageActions();
		// register the action class
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(AmpMessageActions.class.getName(), messageAction);


		// create the form
		messageForm= (AmpMessageForm) createActionForm(AmpMessageForm.class);

		session = getActionMockObjectFactory().getMockSession();
	
        setRelatedObjects();
		setValidate(true);

	}
    protected void setRelatedObjects() {
        try {
            logger.info("Setting atl user ... ");
            TestUtil.setCurrentMemberFirstATLTeam(session);

        } catch (Exception ex) {
           fail(ex.getMessage());
        }

	}


    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMsgStoragePerMsgType() {
        try {
            TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
            logger.info("atl member id="+teamMember.getMemberId());
            int alertType = 0;
            int msgType = 0;
            int approvalType = 0;
            int calEventType = 0;
            int alertTypeHidden = 0;
            int msgTypeHidden = 0;
            int approvalTypeHidden = 0;
            int calEventTypeHidden = 0;

            logger.info("getting message settings ... ");
            AmpMessageSettings settings = AmpMessageUtil.getMessageSettings();
            int maxStorage = 0;
            if (settings.getMsgStoragePerMsgType() != null) {
                maxStorage = settings.getMsgStoragePerMsgType().intValue();
            }
            logger.info("counting visible messages by type ... ");
            msgType = AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(), false, false, maxStorage);
            alertType = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false, false, maxStorage);
            approvalType = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(), false, false, maxStorage);
            calEventType = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(), false, false, maxStorage);
            
            logger.info("counting hidden messages by type ... ");
            msgTypeHidden = AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(), false, true, maxStorage);
            alertTypeHidden = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false, true, maxStorage);
            approvalTypeHidden = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(), false, true, maxStorage);
            calEventTypeHidden = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(), false, true, maxStorage);

            //max. storage value must be greater then visible messages number
            assertTrue(maxStorage>=msgType);
            assertTrue(maxStorage>=alertType);
            assertTrue(maxStorage>=approvalType);
            assertTrue(maxStorage>=calEventType);

            /*max. storage value must be less or equal 
             visible messages + hidden message */
            if (msgTypeHidden > 0) {
                assertFalse(maxStorage > msgType + msgTypeHidden);
            }
            if (alertTypeHidden > 0) {
                assertFalse(maxStorage > alertType + alertTypeHidden);
            }
            if (approvalTypeHidden > 0) {
                assertFalse(maxStorage > approvalType + approvalTypeHidden);
            }
            if (calEventTypeHidden > 0) {
                assertFalse(maxStorage > calEventType + calEventTypeHidden);
            }
        } catch (Exception ex) {
               logger.error(ex.getMessage());
            fail(ex.getMessage());
         
        }

    }

}
