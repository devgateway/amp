package org.dgfoundation.amp.test.categorymanager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public abstract class CategoryManagerBaseTest extends BasicActionTestCaseAdapter {
	private static Logger logger	= Logger.getLogger(CategoryManagerBaseTest.class);
	public static final String TEST_ADD_CATEGORY_KEY	= "junit_category_1";
	
	protected CategoryManager categoryManagerAction;
	protected CategoryManagerForm myForm;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;
	
	protected boolean setupOk;
	
	@Override
	protected void setUp() throws Exception {
		logger.info("Starting setup phase");
		super.setUp();
		Configuration.initConfig();
		categoryManagerAction	= new CategoryManager(); 
		ServletContext context 	= getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(CategoryManager.class.getName(), categoryManagerAction);
		myForm = (CategoryManagerForm) createActionForm(CategoryManagerForm.class);

		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		
		setupOk	= true;

		setValidate(false);
		
		TestUtil.setLocaleEn(request);

		try {
			TestUtil.setSiteDomain(request);
		} catch (DgException e) {
			setupOk	= false;
			e.printStackTrace();
		}
		
		cleanup();
		
		setRelatedObjects();
		
	}
	@Override
	protected void tearDown() throws Exception {
		logger.info("Starting teardown phase");
		super.tearDown();
		cleanup();
	}

	protected void setRelatedObjects() {
			
		myForm.setCategoryName("JUNIT test category");
		myForm.setDescription("testing category creation");
		myForm.setKeyName( TEST_ADD_CATEGORY_KEY );
		
		PossibleValue pv1	= new PossibleValue();
		pv1.setValue("value1");
		PossibleValue pv2	= new PossibleValue();
		pv2.setValue("value2");
		PossibleValue pv3	= new PossibleValue();
		pv3.setValue("value3");
		
		myForm.setPossibleVals( new ArrayList<PossibleValue>() ) ;
		myForm.getPossibleVals().add( pv1 );
		myForm.getPossibleVals().add( pv2 );
		myForm.getPossibleVals().add( pv3 );
		myForm.setSubmitPressed(true);
		
		
		session.setAttribute("ampAdmin", "true");


	}
	
	protected void cleanup() {
	AmpCategoryClass category	= CategoryManagerUtil.loadAmpCategoryClassByKey(TEST_ADD_CATEGORY_KEY);
	Object [] params			= new Object[1];
	params[0] 					= category.getId();
	TestUtil.runPrivateMethod(CategoryManager.class, categoryManagerAction, "deleteCategory", params );

	}
	
	public void testAddCategory() {
		assertTrue(setupOk);
		actionPerform(CategoryManager.class, myForm);
		
		verifyNoActionMessages();
		verifyNoActionMessages();
		verifyForward("forward");
		
		Collection<AmpCategoryValue> values	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey(TEST_ADD_CATEGORY_KEY);
		assertNotNull(values);
		assertEquals(3, values.size() );
	}
}
