
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

public class AddNewOrgProfileWidget extends BasicActionTestCaseAdapter {

private static Logger logger	= Logger.getLogger(AddNewOrgProfileWidget.class);


	protected OrgProfileManager orgProfileManagerAction;
	protected OrgProfileWidgetForm orgProfForm;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;
    protected AmpWidgetOrgProfile widget;
    protected AmpDaWidgetPlace place;
    protected final Long TEST_TYPE=100l;



	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		orgProfileManagerAction	= new OrgProfileManager();
		ServletContext context 	= getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(OrgProfileManager.class.getName(), orgProfileManagerAction);
		orgProfForm = (OrgProfileWidgetForm) createActionForm(OrgProfileWidgetForm.class);
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
        getActionMockObjectFactory().getMockActionMapping().setParameter("actType");
		setValidate(false);

		clean();

		setRelatedObjects();

	}
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		clean();
	}

	protected void setRelatedObjects() {
        // creating test widget
        actionPerform(OrgProfileManager.class, orgProfForm);
        widget = new AmpWidgetOrgProfile();
        widget.setName("test");
        widget.setType(TEST_TYPE);
        place=new AmpDaWidgetPlace();
        place.setModule("widget");
        place.setModuleInstance("default");
        place.setName("orgprof_chart_test_place");
        place.setCode("orgprof_chart_test_place");
        session.setAttribute("ampAdmin", "true");

	}

	protected void clean() {
        logger.info("start cleaning process");
        try {
            AmpDaWidgetPlace plc=WidgetUtil.getPlace("orgprof_chart_test_place");
            if (plc != null) {
                AmpWidgetOrgProfile wdg = (AmpWidgetOrgProfile) plc.getAssignedWidget();
                WidgetUtil.clearPlacesForWidget(wdg.getId());
                OrgProfileWidgetUtil.delete(wdg);
                WidgetUtil.deleteWidgetPlace(plc);
            }
        } catch (DgException ex) {
            logger.error("Unable to delete widget "+ex.getMessage());
        }

	}

	public void testAddWidget() {
        try {
            OrgProfileWidgetUtil.saveWidget(widget);
            place.setAssignedWidget(widget);
            WidgetUtil.savePlace(place);
        } catch (DgException ex) {
            logger.error("Unable to save place or widget"+ex.getMessage());
        }

		
	}

	/**
	 * verifying action forwards...
	 */
	public void testForwards() {
           
            
              //verify update
            addRequestParameter("actType", "update");
            verifyNoActionErrors();
            verifyForward("forward");

              //verify save
            addRequestParameter("actType", "save");
            verifyNoActionErrors();
            verifyForward("forward");

             //verify view all
            addRequestParameter("actType", "viewAll");
            verifyNoActionErrors();
            verifyForward("forward");

              //verify delete
            addRequestParameter("actType", "delete");
            verifyNoActionErrors();
            verifyForward("forward");
       
    }

}

