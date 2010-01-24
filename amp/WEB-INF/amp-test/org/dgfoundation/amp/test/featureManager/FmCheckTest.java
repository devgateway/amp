package org.dgfoundation.amp.test.featureManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.action.DeleteAllReports;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.fmtool.action.FeatureManagerCheckAction;
import org.digijava.module.aim.fmtool.form.FeatureManagerCheckForm;
import org.digijava.module.aim.fmtool.types.FMVisibilityWrapper;
import org.digijava.module.aim.fmtool.util.FMToolConstants;
import org.digijava.module.aim.fmtool.util.FeatureManagerCheckDBUtil;
import org.digijava.module.aim.fmtool.util.FeatureManagerTreeHelper;
import org.digijava.module.aim.form.reportwizard.ReportWizardForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class FmCheckTest extends BasicActionTestCaseAdapter {

	private FeatureManagerCheckAction action;
	private FeatureManagerCheckForm form;
	
	MockHttpSession session;
	MockHttpServletRequest request;	

	FeatureManagerTreeHelper fmth = null;
	
	public FmCheckTest(String name){
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();

		Configuration.initConfig();
		action = new FeatureManagerCheckAction();
		// register the action class
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(FeatureManagerCheckAction.class.getName(), action);

		// create the form
		form = (FeatureManagerCheckForm) createActionForm(FeatureManagerCheckForm.class);

		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();		
		
		getData();
	}
	
	private void getData() throws DgException{
//		Map<Long, FMVisibilityWrapper> modules = FeatureManagerCheckDBUtil.getAllAmpModulesVisibility();
//		Map<Long, FMVisibilityWrapper> features = FeatureManagerCheckDBUtil.getAllAmpFeaturesVisibility();
//		Map<Long, FMVisibilityWrapper> fields = FeatureManagerCheckDBUtil.getAllAmpFieldsVisibility();

		Map<Long, FMVisibilityWrapper> modules = new HashMap<Long, FMVisibilityWrapper>();
		Map<Long, FMVisibilityWrapper> features = new HashMap<Long, FMVisibilityWrapper>();
		Map<Long, FMVisibilityWrapper> fields = new HashMap<Long, FMVisibilityWrapper>();
		
		FMVisibilityWrapper mod_0 = new FMVisibilityWrapper();
		mod_0.setValues(new Long(9990), "TEST_MODULE_0",false,null,"TEST MODULE 0 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_0.getId(), mod_0);
		
		FMVisibilityWrapper mod_1 = new FMVisibilityWrapper();
		mod_1.setValues(new Long(9991), "TEST_MODULE_1",false,new Long(9990),"TEST MODULE 1 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_1.getId(), mod_1);

		FMVisibilityWrapper mod_2 = new FMVisibilityWrapper();
		mod_2.setValues(new Long(9992), "TEST_MODULE_2",false,null,"TEST MODULE 2 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_2.getId(), mod_2);

		FMVisibilityWrapper mod_3 = new FMVisibilityWrapper();
		mod_3.setValues(new Long(9993), "TEST_MODULE_0",false,null,"TEST MODULE 3 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_3.getId(), mod_3);

		FMVisibilityWrapper mod_c0 = new FMVisibilityWrapper();
		mod_c0.setValues(new Long(9994), "TEST_MODULE_c0",false,new Long(9996),"TEST MODULE c0 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_c0.getId(), mod_c0);

		FMVisibilityWrapper mod_c1 = new FMVisibilityWrapper();
		mod_c1.setValues(new Long(9995), "TEST_MODULE_c1",false,new Long(9994),"TEST MODULE c1 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_c1.getId(), mod_c1);

		FMVisibilityWrapper mod_c2 = new FMVisibilityWrapper();
		mod_c2.setValues(new Long(9996), "TEST_MODULE_c2",false,new Long(9995),"TEST MODULE c2 description", FMToolConstants.FEATURE_TYPE_MODULE);
		modules.put(mod_c2.getId(), mod_c2);
		
		

		FMVisibilityWrapper fea_0 = new FMVisibilityWrapper();
		fea_0.setValues(new Long(9990), "TEST_FEATURE_0",false,new Long(9990),"TEST_FEATURE 0 description", FMToolConstants.FEATURE_TYPE_FEATURE);
		fields.put(fea_0.getId(), fea_0);

		FMVisibilityWrapper fea_1 = new FMVisibilityWrapper();
		fea_1.setValues(new Long(9991), "TEST_FEATURE_1",false,new Long(9992),"TEST_FEATURE 1 description", FMToolConstants.FEATURE_TYPE_FEATURE);
		fields.put(fea_1.getId(), fea_1);
		
		
		FMVisibilityWrapper fie_0 = new FMVisibilityWrapper();
		fie_0.setValues(new Long(9990), "TEST_FEATURE_0",false,new Long(9990),"TEST_FIELD 0 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_0.getId(), fie_0);
		
		FMVisibilityWrapper fie_1 = new FMVisibilityWrapper();
		fie_1.setValues(new Long(9990), "TEST_FEATURE_1",false,new Long(9990),"TEST_FIELD 1 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_1.getId(), fie_1);
		
		FMVisibilityWrapper fie_2 = new FMVisibilityWrapper();
		fie_2.setValues(new Long(9990), "TEST_FEATURE_2",false,new Long(9990),"TEST_FIELD 2 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_2.getId(), fie_2);
		
		FMVisibilityWrapper fie_3 = new FMVisibilityWrapper();
		fie_3.setValues(new Long(9990), "TEST_FEATURE_3",false,new Long(9991),"TEST_FIELD 3 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_3.getId(), fie_3);
		
		FMVisibilityWrapper fie_4 = new FMVisibilityWrapper();
		fie_4.setValues(new Long(9990), "TEST_FEATURE_4",false,new Long(9991),"TEST_FIELD 4 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_4.getId(), fie_4);
		
		FMVisibilityWrapper fie_5 = new FMVisibilityWrapper();
		fie_5.setValues(new Long(9990), "TEST_FEATURE_5",false,new Long(9991),"TEST_FIELD 5 description", FMToolConstants.FEATURE_TYPE_FIELD);
		fields.put(fie_5.getId(), fie_5);
		
		fmth = new FeatureManagerTreeHelper(modules, features, fields);
		
	}
	
	
	public void testCheckCircularity() throws Exception {
		fmth.getTree();
		if (fmth.getCircularityElements().size() != 1)
			fail("Checking Circularity fail");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
