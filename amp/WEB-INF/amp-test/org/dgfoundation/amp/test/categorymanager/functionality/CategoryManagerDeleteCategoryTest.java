package org.dgfoundation.amp.test.categorymanager.functionality;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.categorymanager.CategoryManagerBaseTest;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;



public class CategoryManagerDeleteCategoryTest extends CategoryManagerBaseTest {
	private static Logger logger	= Logger.getLogger(CategoryManagerAddCategoryTest.class);
	
	AmpCategoryClass category		= null;

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
		
		actionPerform(CategoryManager.class, myForm);
		
		myForm.setSubmitPressed(false);
		
		try {
			category 	= CategoryManagerUtil.loadAmpCategoryClassByKey(TEST_ADD_CATEGORY_KEY);
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			setupOk		= false;
			e.printStackTrace();
		}
		
	}
	
	public void testDeleteCategory() {
		assertNotNull(category);
		addRequestParameter("delete", category.getId().toString() );
		actionPerform(CategoryManager.class, myForm);
		
		try {
			CategoryManagerUtil.loadAmpCategoryClassByKey(TEST_ADD_CATEGORY_KEY);
			fail("Category should not exist");
		} catch (NoCategoryClassException e) {
		}
	}
}
