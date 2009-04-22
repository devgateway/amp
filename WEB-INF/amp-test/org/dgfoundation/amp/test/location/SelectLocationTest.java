package org.dgfoundation.amp.test.location;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.example.UserRegistrationTest;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.SelectLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.um.action.UserRegisterBlank;
import org.digijava.module.um.form.UserRegisterForm;
import org.jboss.remoting.samples.chat.utility.Debug;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class SelectLocationTest extends BasicActionTestCaseAdapter {

	private static Logger logger = Logger.getLogger(SelectLocationTest.class);

	private SelectLocation action;
	private EditActivityForm form;

	public SelectLocationTest(String name) {
		super(name);

	}

	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		action = new SelectLocation();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(SelectLocation.class.getName(), action);
		form = (EditActivityForm) createActionForm(EditActivityForm.class);
		setValidate(false);
	}
	
	public void testSelectLocationByLevel() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpServletResponse response = getActionMockObjectFactory().getMockResponse();

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		setValidate(false);
		setReset(false);
		//long level = 77l;
		//form.getLocation().setImplemLocationLevel(level);
		form.getLocation().setParentLocId(null);
		actionPerform(SelectLocation.class, form);
		Map<Integer, Collection<KeyValue>> locs = form.getLocation().getLocationByLayers();
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
