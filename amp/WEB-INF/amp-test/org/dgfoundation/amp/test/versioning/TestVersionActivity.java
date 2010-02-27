package org.dgfoundation.amp.test.versioning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.action.DeleteAllReports;
import org.digijava.module.aim.action.SaveActivity;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.Step;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.um.util.AmpUserUtil;
import org.hibernate.Session;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class TestVersionActivity extends BasicActionTestCaseAdapter {

	private EditActivityForm form;
	private SaveActivity action;
	private MockHttpSession session;
	private MockHttpServletRequest request;
	private static AmpActivity testActivity;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();

		action = new SaveActivity();
		form = new EditActivityForm();
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		TestUtil.initializeContext(context);
		context.setAttribute(SaveActivity.class.getName(), action);
		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getTemplateById(FeaturesUtil
				.getGlobalSettingValueLong("Visibility Template"));
		ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
		context.setAttribute("ampTreeVisibility", ampTreeVisibility);
		TestUtil.setSiteDomain(request);
		TestUtil.setLocaleEn(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);
		session.setAttribute("org.digijava.kernel.user", AmpUserUtil.getAllUsers(false).iterator().next());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Create a new EditForm instance, fill all properties and collections
	 * simulating a new activity, then use the form to start the saving process
	 * and save in the testActivity property the new activity.
	 * 
	 * @throws Exception
	 */
	public void testCreateActivity() throws Exception {
		form.setEditAct(false);
		form.setActivityId(null);
		String activityTitle = "CREATED AUTOMATICALLY BY JUNIT. FEEL FREE TO DELETE IT. " + System.currentTimeMillis();
		form.getIdentification().setTitle(activityTitle);
		form.getIdentification().setDraft(new Boolean(false));
		form.getIdentification().setApprovalStatus(Constants.APPROVED_STATUS);

		// Editor text.
		String auxEditorKey = DbUtil.getEditor("amp", 0).getEditorKey();
		form.getIdentification().setDescription(auxEditorKey);
		form.getIdentification().setActivitySummary(auxEditorKey);
		form.getIdentification().setObjectives(auxEditorKey);
		form.getIdentification().setResults(auxEditorKey);
		form.getIdentification().setPurpose(auxEditorKey);
		form.getIdentification().setLessonsLearned(auxEditorKey);
		form.getCrossIssues().setEnvironment(auxEditorKey);
		form.getCrossIssues().setEqualOpportunity(auxEditorKey);
		form.getCrossIssues().setMinorities(auxEditorKey);

		// Steps.
		setFormSteps(form);

		// Sectors.
		form.getSectors().setActivitySectors(new ArrayList<ActivitySector>());
		form.getSectors().setClassificationConfigs(new ArrayList<AmpClassificationConfiguration>());
		for (int i = 0; i < 2; i++) {
			AmpSector auxAmpSector = SectorUtil.getAmpSectors().get(i);
			ActivitySector auxActivitySector = new ActivitySector();
			//auxActivitySector.setSectorScheme(auxAmpSector.getAmpSecSchemeId()
			// .getSecSchemeName());
			if (auxAmpSector.getParentSectorId() != null) {
				auxActivitySector.setSectorName(auxAmpSector.getParentSectorId().getName());
				auxActivitySector.setSectorId(auxAmpSector.getParentSectorId().getAmpSectorId());
				auxActivitySector.setSubsectorLevel1Id(auxAmpSector.getAmpSectorId());
				auxActivitySector.setSubsectorLevel1Name(auxAmpSector.getName());
				if (auxAmpSector.getParentSectorId().getParentSectorId() != null) {
					auxActivitySector.setSectorName(auxAmpSector.getParentSectorId().getParentSectorId().getName());
					auxActivitySector.setSectorId(auxAmpSector.getParentSectorId().getAmpSectorId());
					auxActivitySector.setSubsectorLevel1Id(auxAmpSector.getParentSectorId().getAmpSectorId());
					auxActivitySector.setSubsectorLevel1Name(auxAmpSector.getParentSectorId().getName());
					auxActivitySector.setSubsectorLevel2Id(auxAmpSector.getAmpSectorId());
					auxActivitySector.setSubsectorLevel2Name(auxAmpSector.getName());
				}
			} else {
				auxActivitySector.setSectorName(auxAmpSector.getName());
				auxActivitySector.setSectorId(auxAmpSector.getAmpSectorId());
			}
			auxActivitySector.setConfigId(new Long(i + 1));
			auxActivitySector.setSectorPercentage(new Float(100));
			form.getSectors().getActivitySectors().add(auxActivitySector);

			AmpClassificationConfiguration auxClassification = new AmpClassificationConfiguration();
			auxClassification.setClassification((AmpSectorScheme) ((List) SectorUtil.getAllSectorSchemes()).get(i));
			auxClassification.setId(new Long(i));
			auxClassification.setName(i == 0 ? "Primary" : "Secondary");
			auxClassification.setPrimary(i == 0 ? true : false);
			form.getSectors().getClassificationConfigs().add(auxClassification);
		}

		actionPerform(SaveActivity.class, form);
		verifyNoActionErrors();
		verifyNoActionMessages();
		// verifyForward("index");

		testActivity = ActivityUtil.getActivityByName(activityTitle);
	}

	/**
	 * Save a new version of the activity.
	 * 
	 * @throws Exception
	 */
	public void testEditActivity() throws Exception {
		form.setEditAct(false);
		form.setActivityId(testActivity.getAmpActivityId());
		form.getIdentification().setTitle(testActivity.getName() + "edited");
		form.getIdentification().setDraft(testActivity.getDraft());
		form.getIdentification().setApprovalStatus(testActivity.getApprovalStatus());

		// Editor text.
		String auxEditorKey = testActivity.getDescription();
		form.getIdentification().setDescription(auxEditorKey);
		form.getIdentification().setActivitySummary(auxEditorKey);
		form.getIdentification().setObjectives(auxEditorKey);
		form.getIdentification().setResults(auxEditorKey);
		form.getIdentification().setPurpose(auxEditorKey);
		form.getIdentification().setLessonsLearned(auxEditorKey);
		form.getCrossIssues().setEnvironment(auxEditorKey);
		form.getCrossIssues().setEqualOpportunity(auxEditorKey);
		form.getCrossIssues().setMinorities(auxEditorKey);

		// Steps.
		setFormSteps(form);

		// Sectors.
		form.getSectors().setActivitySectors(new ArrayList<ActivitySector>());
		form.getSectors().setClassificationConfigs(new ArrayList<AmpClassificationConfiguration>());
		Session session = PersistenceManager.getRequestDBSession();
		session.refresh(testActivity);
		//form.getSectors().getActivitySectors().addAll(testActivity.getSectors(
		// ));
		Iterator<AmpActivitySector> iAmpActivitySectors = testActivity.getSectors().iterator();
		while (iAmpActivitySectors.hasNext()) {
			AmpActivitySector auxAmpActivitySector = iAmpActivitySectors.next();
			ActivitySector auxActivitySector = new ActivitySector();
			auxActivitySector.setConfigId(auxAmpActivitySector.getClassificationConfig().getId());
			auxActivitySector.setId(auxAmpActivitySector.getSectorId().getAmpSectorId());

			if (auxAmpActivitySector.getSectorId().getParentSectorId() != null) {
				auxActivitySector.setSectorName(auxAmpActivitySector.getSectorId().getParentSectorId().getName());
				auxActivitySector.setSectorId(auxAmpActivitySector.getSectorId().getParentSectorId().getAmpSectorId());
				auxActivitySector.setSubsectorLevel1Id(auxAmpActivitySector.getSectorId().getAmpSectorId());
				auxActivitySector.setSubsectorLevel1Name(auxAmpActivitySector.getSectorId().getName());
				if (auxAmpActivitySector.getSectorId().getParentSectorId().getParentSectorId() != null) {
					auxActivitySector.setSectorName(auxAmpActivitySector.getSectorId().getParentSectorId()
							.getParentSectorId().getName());
					auxActivitySector.setSectorId(auxAmpActivitySector.getSectorId().getParentSectorId()
							.getAmpSectorId());
					auxActivitySector.setSubsectorLevel1Id(auxAmpActivitySector.getSectorId().getParentSectorId()
							.getAmpSectorId());
					auxActivitySector.setSubsectorLevel1Name(auxAmpActivitySector.getSectorId().getParentSectorId()
							.getName());
					auxActivitySector.setSubsectorLevel2Id(auxAmpActivitySector.getSectorId().getAmpSectorId());
					auxActivitySector.setSubsectorLevel2Name(auxAmpActivitySector.getSectorId().getName());
				}
			} else {
				auxActivitySector.setSectorName(auxAmpActivitySector.getSectorId().getName());
				auxActivitySector.setSectorId(auxAmpActivitySector.getSectorId().getAmpSectorId());
			}
			auxActivitySector.setSectorPercentage(auxAmpActivitySector.getSectorPercentage());

			form.getSectors().getActivitySectors().add(auxActivitySector);

		}

		for (int i = 0; i < 2; i++) {
			AmpClassificationConfiguration auxClassification = new AmpClassificationConfiguration();
			auxClassification.setClassification((AmpSectorScheme) ((List) SectorUtil.getAllSectorSchemes()).get(i));
			auxClassification.setId(new Long(i));
			auxClassification.setName(i == 0 ? "Primary" : "Secondary");
			auxClassification.setPrimary(i == 0 ? true : false);
			form.getSectors().getClassificationConfigs().add(auxClassification);
		}

		actionPerform(SaveActivity.class, form);
		verifyNoActionErrors();
		verifyNoActionMessages();
		// verifyForward("index");

		testActivity = ActivityUtil.getActivityByName(testActivity.getName() + "edited");
		assertNotNull("Edited activity not saved.", testActivity);
	}

	/**
	 * Delete the new activity and their versions.
	 * 
	 * @throws Exception
	 */
	public void testDeleteActivity() throws Exception {
		Long auxId = testActivity.getAmpActivityId();
		testActivity = null;
		ActivityUtil.deleteActivity(auxId);
		verifyNoActionErrors();
		verifyNoActionMessages();
	}

	private static void setFormSteps(EditActivityForm form) {
		// Steps.
		List<Step> auxSteps = new ArrayList<Step>();
		Step auxStep = new Step();
		auxStep.setStepActualNumber(1);
		auxStep.setStepNumber("1");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(2);
		auxStep.setStepNumber("2");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(3);
		auxStep.setStepNumber("3");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(4);
		auxStep.setStepNumber("5");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(5);
		auxStep.setStepNumber("6");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(6);
		auxStep.setStepNumber("7");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(7);
		auxStep.setStepNumber("8");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(8);
		auxStep.setStepNumber("17");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
		auxStep = new Step();
		auxStep.setStepActualNumber(9);
		auxStep.setStepNumber("10");
		auxSteps.add(auxStep);
		form.setSteps(auxSteps);
	}
}
