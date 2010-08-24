package org.dgfoundation.amp.test.categorymanager.functionality;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.categorymanager.CategoryManagerBaseTest;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.PossibleValue;

public class CategoryManagerAddCategoryTest extends CategoryManagerBaseTest{
	
	private static Logger logger	= Logger.getLogger(CategoryManagerAddCategoryTest.class);

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
	
	
	public void testAddCategory() {
		assertTrue(setupOk);
		actionPerform(CategoryManager.class, myForm);
		
		verifyNoActionMessages();
		verifyNoActionMessages();
		verifyForward("forward");
		
		try {
			CategoryManagerUtil.loadAmpCategoryClassByKey(TEST_ADD_CATEGORY_KEY);
		} catch (NoCategoryClassException e) {
			fail("Category not found");
		}
	}
	

}
