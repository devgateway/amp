package org.dgfoundation.amp.test.categorymanager;

import org.dgfoundation.amp.test.categorymanager.api.CategoryManagerApiTest;
import org.dgfoundation.amp.test.categorymanager.data.CategoryManagerDataTest;
import org.dgfoundation.amp.test.categorymanager.functionality.CategoryManagerAddCategoryTest;
import org.dgfoundation.amp.test.categorymanager.functionality.CategoryManagerDeleteCategoryTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCategoryManagerTest {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CategoryManagerAddCategoryTest.class);
		suite.addTestSuite(CategoryManagerDeleteCategoryTest.class);
		suite.addTestSuite(CategoryManagerApiTest.class);
		suite.addTestSuite(CategoryManagerDataTest.class);
		return suite;
	}
}
