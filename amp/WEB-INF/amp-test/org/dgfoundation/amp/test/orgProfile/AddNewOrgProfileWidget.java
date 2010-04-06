package org.dgfoundation.amp.test.orgProfile;

import org.dgfoundation.amp.test.util.Configuration;

import org.apache.log4j.Logger;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import javax.servlet.ServletContext;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.action.OrgProfileManager;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.form.OrgProfileWidgetForm;
import org.digijava.module.widget.util.OrgProfileWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.dbentity.AmpWidget;
import java.util.ArrayList;
import java.util.Collection;

public class AddNewOrgProfileWidget extends BasicActionTestCaseAdapter {

    private static Logger logger = Logger.getLogger(AddNewOrgProfileWidget.class);
    protected OrgProfileManager orgProfileManagerAction;
    protected OrgProfileWidgetForm orgProfForm;
    protected MockHttpSession session;
    protected MockHttpServletRequest request;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initConfig();
        orgProfileManagerAction = new OrgProfileManager();
        ServletContext context = getActionMockObjectFactory().getMockServletContext();
        context.setAttribute(OrgProfileManager.class.getName(), orgProfileManagerAction);
        orgProfForm = (OrgProfileWidgetForm) createActionForm(OrgProfileWidgetForm.class);
        session = getActionMockObjectFactory().getMockSession();
        session.setAttribute("ampAdmin", "true");
        request = getActionMockObjectFactory().getMockRequest();
        getActionMockObjectFactory().getMockActionMapping().setParameter("actType");
        setValidate(false);
        setRelatedObjects();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void setRelatedObjects() {
         logger.info("starting setup process");
        actionPerform(OrgProfileManager.class, orgProfForm);


    }
        
   
	/**
	 * verifying action forwards...
	 */
	public void testForwards() {
                   
              //verify save
            addRequestParameter("actType", "save");
            verifyNoActionErrors();
            verifyForward("forward");

             //verify view all
            addRequestParameter("actType", "viewAll");
            verifyNoActionErrors();
            verifyForward("forward");

                  
    }


}

