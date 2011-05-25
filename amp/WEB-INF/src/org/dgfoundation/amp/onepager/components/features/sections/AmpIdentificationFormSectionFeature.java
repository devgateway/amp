/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetExtrasPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetField;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.components.fields.AmpBudgetClassificationField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTab;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTabsFieldWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Identification section in activity form. This is also an AMP feature
 * @author mpostelnicu@dgateway.org since Oct 3, 2010
 * @see OnePager
 */
public class AmpIdentificationFormSectionFeature extends AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = 8568986144567957699L;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception 
	 *
	 */
	public AmpIdentificationFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
			super(id, fmName, am);
			this.fmType = AmpFMTypes.MODULE;
			
			IModel<String> m = new PropertyModel<String>(am, "name");
			// textField - mandatory
			AmpTextFieldPanel<String> title = new AmpTextFieldPanel<String>(
					"title", m, "Project Title", AmpFMTypes.FEATURE);
			title.getTextContainer().setRequired(true);
			title.getTextContainer().add(new AttributeAppender("size", new Model("36"), ";"));
			add(title);
			
			AmpCategorySelectFieldPanel status = new AmpCategorySelectFieldPanel(
					"status", CategoryConstants.ACTIVITY_STATUS_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACTIVITY_STATUS_KEY),
							CategoryConstants.ACTIVITY_STATUS_NAME, true, false, null, AmpFMTypes.FEATURE);
			add(status);
			
			add(new AmpTextAreaFieldPanel<String>("statusReason",
					new PropertyModel<String>(am, "statusReason"), "Status Reason", true, AmpFMTypes.FEATURE));
			
			AmpTextFieldPanel<String> budgetCodeProjectId = new AmpTextFieldPanel<String>(
					"budgetCodeProjectID", new PropertyModel<String>(am,
							"budgetCodeProjectID"), "Budget Code Project ID", AmpFMTypes.FEATURE);
			add(budgetCodeProjectId);
	
			AmpTextFieldPanel<String> donorProjectCode = new AmpTextFieldPanel<String>(
					"donorProjectCode", new PropertyModel<String>(am,
							"projectCode"), "Donor Project Code", AmpFMTypes.FEATURE);
			add(donorProjectCode);
	
			AmpTextFieldPanel<String> crisNumber = new AmpTextFieldPanel<String>(
					"crisNumber", new PropertyModel<String>(am,
							"crisNumber"), "Donor Project Code", AmpFMTypes.FEATURE);
			add(crisNumber);

			AmpCategorySelectFieldPanel acChapter = new AmpCategorySelectFieldPanel(
					"acChapter", CategoryConstants.ACCHAPTER_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACCHAPTER_KEY), 
							CategoryConstants.ACCHAPTER_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(acChapter);
			
			AmpActivityBudgetExtrasPanel budgetExtras = new AmpActivityBudgetExtrasPanel("budgetExtras", am, "Budget Extras");
			budgetExtras.setOutputMarkupId(true);

			WebMarkupContainer budgetExtrasContainter = new WebMarkupContainer("budgetExtrasContainer");
			budgetExtrasContainter.add(budgetExtras);
			budgetExtrasContainter.setOutputMarkupId(true);
			add(budgetExtrasContainter);
			
			WebMarkupContainer budgetClassificationContainer = new WebMarkupContainer("budgetClassificationContainer");
			budgetClassificationContainer.setOutputMarkupId(true);
			AmpBudgetClassificationField budgetClassification = new AmpBudgetClassificationField("budgetClassification", am, "Budget Classification");
			budgetClassification.setOutputMarkupId(true);
			budgetClassificationContainer.add(budgetClassification);
			add(budgetClassificationContainer);
			
			AmpActivityBudgetField activityBudget = new AmpActivityBudgetField("activityBudget", new PropertyModel(am, "budget"), "Activity Budget", budgetExtras, budgetClassification);
			add(activityBudget);

			AmpCategoryGroupFieldPanel financialInstrument = new AmpCategoryGroupFieldPanel(
					"financialInstrument",
					CategoryConstants.FINANCIAL_INSTRUMENT_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.FINANCIAL_INSTRUMENT_KEY),
							CategoryConstants.FINANCIAL_INSTRUMENT_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(financialInstrument);

			AmpCategorySelectFieldPanel procurementSystem = new AmpCategorySelectFieldPanel(
					"procurementSystem",
					CategoryConstants.PROCUREMENT_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.PROCUREMENT_SYSTEM_KEY),
							CategoryConstants.PROCUREMENT_SYSTEM_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(procurementSystem);
			
			AmpCategorySelectFieldPanel reportingSystem = new AmpCategorySelectFieldPanel(
					"reportingSystem",
					CategoryConstants.REPORTING_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.REPORTING_SYSTEM_KEY),
							CategoryConstants.REPORTING_SYSTEM_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(reportingSystem);
			
			AmpCategorySelectFieldPanel auditSystem = new AmpCategorySelectFieldPanel(
					"auditSystem",
					CategoryConstants.AUDIT_SYSTEM_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.AUDIT_SYSTEM_KEY),
							CategoryConstants.AUDIT_SYSTEM_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(auditSystem);
			
			AmpCategorySelectFieldPanel institutions = new AmpCategorySelectFieldPanel(
					"institutions",
					CategoryConstants.INSTITUTIONS_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.INSTITUTIONS_KEY),
							CategoryConstants.INSTITUTIONS_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(institutions);
			
			AmpCategorySelectFieldPanel accessionInstrument = new AmpCategorySelectFieldPanel(
					"accessionInstrument",
					CategoryConstants.ACCESSION_INSTRUMENT_KEY,
					new AmpCategoryValueByKeyModel(
							new PropertyModel<Set<AmpCategoryValue>>(am,"categories"),
							CategoryConstants.ACCESSION_INSTRUMENT_KEY),
							CategoryConstants.ACCESSION_INSTRUMENT_NAME, true, true, null, AmpFMTypes.FEATURE);
			add(accessionInstrument);
			
			add(new AmpBooleanChoiceField("governmentApprovalProcedures", 
					new PropertyModel<Boolean>(am, "governmentApprovalProcedures"), "Government Approval Procedures"));

			add(new AmpBooleanChoiceField("jointCriteria", 
					new PropertyModel<Boolean>(am, "jointCriteria"), "Joint Criteria"));

			add(new AmpBooleanChoiceField("humanitarianAid", 
					new PropertyModel<Boolean>(am, "humanitarianAid"), "Humanitarian Aid"));
			
			
	
		
			add(new AmpTextAreaFieldPanel<String>("projectComments",
					new PropertyModel<String>(am, "projectComments"),
					"Project Comments", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("description",
					new PropertyModel<String>(am, "description"), "Description",
					true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("objective",
					new PropertyModel<String>(am, "objective"), "Objective", true, AmpFMTypes.FEATURE));
			
			
			
			List<ITab> objectiveTabs = new ArrayList<ITab>();
			objectiveTabs.add(new AmpCommentTab("OV Indicators" , "Objective Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			objectiveTabs.add(new AmpCommentTab("Assumption" , "Objective Assumption", am, AmpCommentPanel.class));
			objectiveTabs.add(new AmpCommentTab("Verification" , "Objective Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper objTabs = new AmpCommentTabsFieldWrapper("objectiveTabs", "Objective Comments", objectiveTabs);
			add(objTabs);
			
			add(new AmpTextAreaFieldPanel<String>("purpose",
					new PropertyModel<String>(am, "purpose"), "Purpose", true, AmpFMTypes.FEATURE));
			
			List<ITab> tabs = new ArrayList<ITab>();
			tabs.add(new AmpCommentTab("OV Indicators" , "Purpose Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab("Assumption" , "Purpose Assumption", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab("Verification" , "Purpose Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper purposeTabs = new AmpCommentTabsFieldWrapper("purposeTabs", "Purpose Comments", tabs);
			add(purposeTabs);
			
			add(new AmpTextAreaFieldPanel<String>("results",
					new PropertyModel<String>(am, "results"), "Results", true, AmpFMTypes.FEATURE));
	
			tabs = new ArrayList<ITab>();
			tabs.add(new AmpCommentTab("OV Indicators" , "Results Objectively Verifiable Indicators", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab("Assumption" , "Results Assumption", am, AmpCommentPanel.class));
			tabs.add(new AmpCommentTab("Verification" , "Results Verification", am, AmpCommentPanel.class));
			
			AmpCommentTabsFieldWrapper resultsTabs = new AmpCommentTabsFieldWrapper("resultsTabs", "Results Comments", tabs);
			add(resultsTabs);
			
			add(new AmpTextAreaFieldPanel<String>("lessonsLearned",
					new PropertyModel<String>(am, "lessonsLearned"), "Lessons Learned", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("projectImpact",
					new PropertyModel<String>(am, "projectImpact"), "Project Impact", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("activitySummary",
					new PropertyModel<String>(am, "activitySummary"), "Activity Summary", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("contractingArrangements",
					new PropertyModel<String>(am, "contractingArrangements"), "Contracting Arrangements", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("conditionalitySequencing",
					new PropertyModel<String>(am, "condSeq"), "Conditionality and Sequencing", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("linkedActivities",
					new PropertyModel<String>(am, "linkedActivities"), "Linked Activities", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("conditionalities",
					new PropertyModel<String>(am, "conditionality"), "Conditionalities", true, AmpFMTypes.FEATURE));
			add(new AmpTextAreaFieldPanel<String>("projectManagement",
					new PropertyModel<String>(am, "projectManagement"), "Project Management", true, AmpFMTypes.FEATURE));
	}

}
