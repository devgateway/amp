package org.dgfoundation.amp.test.location;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.DynLocationSelected;
import org.digijava.module.aim.action.SelectorAction;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.SelectLocationForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class SelectDynLocationTest extends BasicActionTestCaseAdapter {
	private static Logger logger = Logger.getLogger(SelectDynLocationTest.class);

	private DynLocationSelected action;
	private SelectLocationForm form;

	public SelectDynLocationTest(String name) {
		super(name);

	}

	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		action = new DynLocationSelected();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(DynLocationSelected.class.getName(), action);
		form = (SelectLocationForm) createActionForm(SelectLocationForm.class);
		setValidate(false);
	}
	
	public void testDynLocationSelectedByLevel() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpServletResponse response = getActionMockObjectFactory().getMockResponse();
		request.setAttribute(SelectorAction.ATTRIBUTE_START, "/aim/selectLocation");

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		setValidate(false);
		setReset(false);
		form.setParentLocId(null);
		actionPerform(DynLocationSelected.class, form);
		Map<Integer, Collection<KeyValue>> locs = form.getLocationByLayers();
		AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getLocationByIso(FeaturesUtil.getDefaultCountryIso(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
    	
		Entry<Integer, Collection<KeyValue>> entry = locs.entrySet().iterator().next();
		Collection<KeyValue> col = entry.getValue();
		KeyValue kvLoc = col.iterator().next();
		assertTrue(kvLoc.getKey().equals(defCountry.getId().toString()) && kvLoc.getValue().equals(defCountry.getName()));
		
		verifyForward("forward");
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
