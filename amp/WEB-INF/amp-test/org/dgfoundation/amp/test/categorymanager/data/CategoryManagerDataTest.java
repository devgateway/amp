package org.dgfoundation.amp.test.categorymanager.data;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.dgfoundation.amp.test.util.mock.AMPMockServletContext;
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
		super.setUp();
	}

	public void testCategoriesByCurrentFeaturesEnabled() throws Exception {
		if (FeaturesUtil.isVisibleField("A.C. Chapter", ctx))
			assertNotNull("Category not found: " + CategoryConstants.ACCHAPTER_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.ACCHAPTER_NAME));

		if (FeaturesUtil.isVisibleField("Accession Instrument", ctx))
			assertNotNull("Category not found: " + CategoryConstants.ACCESSION_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.ACCESSION_INSTRUMENT_NAME));

		if (FeaturesUtil.isVisibleField("Document Type", ctx))
			assertNotNull("Category not found: " + CategoryConstants.DOCUMENT_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.DOCUMENT_TYPE_NAME));

		if (FeaturesUtil.isVisibleField("Status", ctx))
			assertNotNull("Category not found: " + CategoryConstants.ACTIVITY_STATUS_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.ACTIVITY_STATUS_NAME));

		if (FeaturesUtil.isVisibleField("Implementation Level", ctx))
			assertNotNull("Category not found: " + CategoryConstants.IMPLEMENTATION_LEVEL_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IMPLEMENTATION_LEVEL_NAME));

		if (FeaturesUtil.isVisibleField("Implementation Location", ctx))
			assertNotNull("Category not found: " + CategoryConstants.IMPLEMENTATION_LOCATION_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IMPLEMENTATION_LOCATION_NAME));

		if (FeaturesUtil.isVisibleField("Financing Instrument", ctx))
			assertNotNull("Category not found: " + CategoryConstants.FINANCING_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.FINANCING_INSTRUMENT_NAME));

		if (FeaturesUtil.isVisibleField("Program Type", ctx))
			assertNotNull("Category not found: " + CategoryConstants.PROGRAM_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.PROGRAM_TYPE_NAME));

		if (FeaturesUtil.isVisibleField("Financial Instrument", ctx))
			assertNotNull("Category not found: " + CategoryConstants.FINANCIAL_INSTRUMENT_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.FINANCIAL_INSTRUMENT_NAME));

		if (FeaturesUtil.isVisibleField("MTEF Projections", ctx))
			assertNotNull("Category not found: " + CategoryConstants.MTEF_PROJECTION_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.MTEF_PROJECTION_NAME));

		if (FeaturesUtil.isVisibleField("Project Category", ctx))
			assertNotNull("Category not found: " + CategoryConstants.PROJECT_CATEGORY_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.PROJECT_CATEGORY_NAME));

		if (FeaturesUtil.isVisibleFeature("Contracting", ctx)) {
			assertNotNull("Category not found: " + CategoryConstants.IPA_ACTIVITY_CATEGORY_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IPA_ACTIVITY_CATEGORY_NAME));

			assertNotNull("Category not found: " + CategoryConstants.IPA_STATUS_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IPA_STATUS_NAME));

			assertNotNull("Category not found: " + CategoryConstants.IPA_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IPA_TYPE_NAME));

			assertNotNull("Category not found: " + CategoryConstants.IPA_ACTIVITY_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.IPA_ACTIVITY_TYPE_NAME));
		}

		// no match with any feture
		assertNotNull("Category not found: " + CategoryConstants.LOGFRAME_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.LOGFRAME_NAME));
		assertNotNull("Category not found: " + CategoryConstants.ACTIVITY_LEVEL_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.ACTIVITY_LEVEL_NAME));
		assertNotNull("Category not found: " + CategoryConstants.TEAM_TYPE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.TEAM_TYPE_NAME));
		assertNotNull("Category not found: " + CategoryConstants.TYPE_OF_ASSISTENCE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.TYPE_OF_ASSISTENCE_NAME));
		assertNotNull("Category not found: " + CategoryConstants.DOCUMENT_LANGUAGE_NAME, CategoryManagerUtil.loadAmpCategoryClass(CategoryConstants.DOCUMENT_LANGUAGE_NAME));

	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}
