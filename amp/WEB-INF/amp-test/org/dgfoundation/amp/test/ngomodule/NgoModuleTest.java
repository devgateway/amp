package org.dgfoundation.amp.test.ngomodule;

import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.digijava.module.aim.action.EditOrganisation;
import org.digijava.module.aim.form.AddOrgForm;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;


public class NgoModuleTest extends BasicActionTestCaseAdapter {

    private static Logger logger = Logger.getLogger(NgoModuleTest.class);
    private MockHttpSession session;
    private MockHttpServletRequest request;
    private AddOrgForm form;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initConfig();
        ServletContext context = getActionMockObjectFactory().getMockServletContext();
        form = (AddOrgForm) createActionForm(AddOrgForm.class);
        session = getActionMockObjectFactory().getMockSession();
        request = getActionMockObjectFactory().getMockRequest();
        setRelatedObjects();
        setValidate(false);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    protected void setRelatedObjects() throws Exception {
        TestUtil.setLocaleEn(request);
        TestUtil.setSiteDomain(request);
        session.setAttribute("ampAdmin", "yes");
        getActionMockObjectFactory().getMockActionMapping().setParameter("actionFlag");
        actionPerform(EditOrganisation.class, form);

    }

    /**
     * verifying forwards
     */
    public void testForwards() {
        logger.info("starting verifying process...");

        form.setActionFlag("create");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("edit");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("delete");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("delete");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("addPledge");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("deletePledge");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("addSector");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("removeSector");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("addStaffInfo");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("deleteStaffInfo");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("removeRecipient");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("deleteLocation");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("deleteContact");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("typeChanged");
        verifyNoActionErrors();
        verifyForward("forward");

        form.setActionFlag("save");
        verifyNoActionErrors();
        verifyForward("forward");

        logger.info("ending verifying process...");



    }
}
