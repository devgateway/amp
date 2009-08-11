package org.dgfoundation.amp.test.ngomodule;

import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import java.util.Collection;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.aim.action.EditOrganisation;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.util.DbUtil;

public class TypeNGOTest extends BasicActionTestCaseAdapter {

    private static Logger logger = Logger.getLogger(TypeNGOTest.class);
    private MockHttpSession session;
    private MockHttpServletRequest request;
    private AddOrgForm typeForm;
 

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initConfig();
        ServletContext context = getActionMockObjectFactory().getMockServletContext();
        typeForm = (AddOrgForm) createActionForm(AddOrgForm.class);
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
        getActionMockObjectFactory().getMockActionMapping().setParameter("actionFlag");

        session.setAttribute("ampAdmin", "yes");
       

    }

    public void testAddStaffInfo() {
        try {
            Collection<AmpCategoryValue> types = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ORGANIZATION_STAFF_INFO_KEY);
            if (types != null && !types.isEmpty()) {
                typeForm.setSelectedYear("2007");
                typeForm.setNumberOfStaff("5");
                typeForm.setTypeOfStaff(types.iterator().next().getId());
                addRequestParameter("action", "addStaffInfo");
                actionPerform(EditOrganisation.class, typeForm);
                logger.info("adding process finished");
            }
        } catch (Exception ex) {
            logger.error("Unable to add staff " + ex.getMessage());
            fail("Unable to add staff");
        }


    }

     public void testTypeChanged() {
        try {
            Collection<AmpOrgType> types= DbUtil.getAllOrgTypes();
            if(types!=null&&!types.isEmpty()){
                typeForm.setType(types.iterator().next().getClassification());
                addRequestParameter("action", "typeChanged");
                actionPerform(EditOrganisation.class, typeForm);
                logger.info("type changed process finished");
            }
            
        } catch (Exception ex) {
            logger.error("Unable to add staff " + ex.getMessage());
            fail("Unable to add staff");
        }


    }

   
}
