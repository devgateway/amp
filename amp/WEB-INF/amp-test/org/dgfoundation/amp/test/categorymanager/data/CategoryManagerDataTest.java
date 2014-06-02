package org.dgfoundation.amp.test.categorymanager.data;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.dgfoundation.amp.test.util.mock.AMPMockServletContext;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.mockrunner.servlet.ServletTestCaseAdapter;

public class CategoryManagerDataTest extends ServletTestCaseAdapter {
	Logger log = Logger.getLogger(CategoryManagerDataTest.class);
	private ServletContext ctx = null;

	public CategoryManagerDataTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		Configuration.initConfig();
		ctx = AMPMockServletContext.getWrapeerServletContext(getWebMockObjectFactory().getMockServletConfig());
		TestUtil.initializeContext(ctx);
		org.apache.struts.mock.MockHttpSession mockSession = new org.apache.struts.mock.MockHttpSession(ctx);
		org.apache.struts.mock.MockHttpServletRequest mockRequest = new org.apache.struts.mock.MockHttpServletRequest(new org.apache.struts.mock.MockHttpSession());
		mockRequest.setHttpSession(mockSession);
		TLSUtils.populate(mockRequest);
	
		super.setUp();
	}

	public void testCategoriesByCurrentFeaturesEnabled() throws Exception {
		if (FeaturesUtil.isVisibleField("A.C. Chapter"))
			assertNotNull("Category not found: " + CategoryConstants.ACCHAPTER_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ACCHAPTER_KEY));

		if (FeaturesUtil.isVisibleField("Accession Instrument"))
			assertNotNull("Category not found: " + CategoryConstants.ACCESSION_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ACCESSION_INSTRUMENT_KEY));

		if (FeaturesUtil.isVisibleField("Document Type"))
			assertNotNull("Category not found: " + CategoryConstants.DOCUMENT_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.DOCUMENT_TYPE_KEY));

		if (FeaturesUtil.isVisibleField("Status"))
			assertNotNull("Category not found: " + CategoryConstants.ACTIVITY_STATUS_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ACTIVITY_STATUS_KEY));

		if (FeaturesUtil.isVisibleField("Implementation Level"))
			assertNotNull("Category not found: " + CategoryConstants.IMPLEMENTATION_LEVEL_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IMPLEMENTATION_LEVEL_KEY));

		if (FeaturesUtil.isVisibleField("Implementation Location"))
			assertNotNull("Category not found: " + CategoryConstants.IMPLEMENTATION_LOCATION_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));

		if (FeaturesUtil.isVisibleField("Financing Instrument"))
			assertNotNull("Category not found: " + CategoryConstants.FINANCING_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY));

		if (FeaturesUtil.isVisibleField("Program Type"))
			assertNotNull("Category not found: " + CategoryConstants.PROGRAM_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.PROGRAM_TYPE_KEY));

		if (FeaturesUtil.isVisibleField("Financial Instrument"))
			assertNotNull("Category not found: " + CategoryConstants.FINANCIAL_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.FINANCIAL_INSTRUMENT_KEY));

		if (FeaturesUtil.isVisibleField("MTEF Projections"))
			assertNotNull("Category not found: " + CategoryConstants.MTEF_PROJECTION_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.MTEF_PROJECTION_KEY));

		if (FeaturesUtil.isVisibleField("Project Category"))
			assertNotNull("Category not found: " + CategoryConstants.PROJECT_CATEGORY_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.PROJECT_CATEGORY_KEY));

		if (FeaturesUtil.isVisibleFeature("Contracting")) {
			assertNotNull("Category not found: " + CategoryConstants.IPA_ACTIVITY_CATEGORY_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_ACTIVITY_CATEGORY_KEY));

			assertNotNull("Category not found: " + CategoryConstants.IPA_STATUS_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_STATUS_KEY));

			assertNotNull("Category not found: " + CategoryConstants.IPA_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_TYPE_KEY));

			assertNotNull("Category not found: " + CategoryConstants.IPA_ACTIVITY_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.IPA_ACTIVITY_TYPE_KEY));
		}

		// no match with any feature
		//Nobody can guarantee that the below categories are in the database. So I commented them out.
//		assertNotNull("Category not found: " + CategoryConstants.LOGFRAME_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.LOGFRAME_NAME));
//		assertNotNull("Category not found: " + CategoryConstants.ACTIVITY_LEVEL_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.ACTIVITY_LEVEL_NAME));
//		assertNotNull("Category not found: " + CategoryConstants.TEAM_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.TEAM_TYPE_NAME));
//		assertNotNull("Category not found: " + CategoryConstants.TYPE_OF_ASSISTENCE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.TYPE_OF_ASSISTENCE_NAME));
//		assertNotNull("Category not found: " + CategoryConstants.DOCUMENT_LANGUAGE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.DOCUMENT_LANGUAGE_NAME));

	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}
