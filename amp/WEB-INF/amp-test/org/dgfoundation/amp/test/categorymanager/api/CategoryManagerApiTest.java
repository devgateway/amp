package org.dgfoundation.amp.test.categorymanager.api;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.form.CategoryManagerForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class CategoryManagerApiTest extends BasicActionTestCaseAdapter {
	
	private static Logger logger	= Logger.getLogger(CategoryManagerApiTest.class);
	
	protected CategoryManager categoryManagerAction;
	protected CategoryManagerForm myForm;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;
	
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
		

		setValidate(false);
		
		TestUtil.setLocaleEn(request);

		try {
			TestUtil.setSiteDomain(request);
		} catch (DgException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testRetrieveOrderedCollection() {
		try {
			Collection<AmpCategoryValue> vals	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey("activity_status", true, request);
			assertNotNull(vals);
			if ( vals.size() > 1 ) {
				Iterator<AmpCategoryValue> iter	= vals.iterator();
				AmpCategoryValue categVal1		= iter.next();
				String val1						= CategoryManagerUtil.translateAmpCategoryValue(categVal1, request);
				assertNotNull(val1);
				while ( iter.hasNext() ) {
					AmpCategoryValue categVal2		= iter.next();
					String val2						= CategoryManagerUtil.translateAmpCategoryValue(categVal2, request);
					assertNotNull(val2);
					assertTrue( val2.compareTo(val1) > 0 );
				}
			}
		} catch (Exception e) {
			fail("getting ampcategoryvalue collection failed ");
			e.printStackTrace();
		}
	}
	
	public void testGetValuesCollectionByCategoryId() {
		try {
			Collection<AmpCategoryValue> vals	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey("activity_status", true, request);
			assertNotNull(vals);
			Long categoryId						= vals.iterator().next().getAmpCategoryClass().getId();
			assertNotNull( CategoryManagerUtil.getAmpCategoryValueCollection(categoryId, false, null) );
		} catch (Exception e) {
			fail("getting ampcategoryvalue collection failed ");
			e.printStackTrace();
		}
	}
	
	
}
